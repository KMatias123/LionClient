/*     */ package xaero.map;
/*     */ 
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.InputStream;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.material.MapColor;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.renderer.BlockModelShapes;
/*     */ import net.minecraft.client.renderer.block.model.BakedQuad;
/*     */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*     */ import net.minecraft.client.renderer.texture.TextureUtil;
/*     */ import net.minecraft.client.resources.IResource;
/*     */ import net.minecraft.init.Biomes;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.util.BlockRenderLayer;
/*     */ import net.minecraft.util.EnumBlockRenderType;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.world.EnumSkyBlock;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.biome.Biome;
/*     */ import net.minecraft.world.chunk.Chunk;
/*     */ import xaero.map.biome.MapBiomes;
/*     */ import xaero.map.cache.BlockStateColorTypeCache;
/*     */ import xaero.map.core.XaeroWorldMapCore;
/*     */ import xaero.map.region.MapBlock;
/*     */ import xaero.map.region.MapRegion;
/*     */ import xaero.map.region.MapTile;
/*     */ import xaero.map.region.MapTileChunk;
/*     */ import xaero.map.region.Overlay;
/*     */ import xaero.map.region.OverlayBuilder;
/*     */ import xaero.map.region.OverlayManager;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MapWriter
/*     */ {
/*  48 */   public static final String[] DEFAULT_RESOURCE = new String[] { "minecraft", "" };
/*     */   
/*     */   private int X;
/*     */   private int Z;
/*     */   private int playerChunkX;
/*     */   private int playerChunkZ;
/*     */   private int insideX;
/*     */   private int insideZ;
/*     */   private long updateCounter;
/*     */   private boolean clearCachedColours;
/*     */   private MapBlock loadingObject;
/*     */   private OverlayBuilder overlayBuilder;
/*     */   private BlockPos.MutableBlockPos mutableLocalPos;
/*     */   private BlockPos.MutableBlockPos mutableGlobalPos;
/*     */   private int[] biomeBuffer;
/*  63 */   private long lastWrite = -1L;
/*  64 */   private long lastWriteTry = -1L;
/*     */   
/*     */   private BlockStateColorTypeCache colorTypeCache;
/*     */   private HashMap<String, Integer> textureColours;
/*     */   private HashMap<Integer, Integer> blockColours;
/*     */   
/*     */   public MapWriter(OverlayManager overlayManager, BlockStateColorTypeCache colorTypeCache) {
/*  71 */     this.loadingObject = new MapBlock();
/*  72 */     this.textureColours = new HashMap<>();
/*  73 */     this.blockColours = new HashMap<>();
/*  74 */     this.overlayBuilder = new OverlayBuilder(overlayManager);
/*  75 */     this.mutableLocalPos = new BlockPos.MutableBlockPos();
/*  76 */     this.mutableGlobalPos = new BlockPos.MutableBlockPos();
/*  77 */     this.biomeBuffer = new int[3];
/*  78 */     this.colorTypeCache = colorTypeCache;
/*     */   }
/*     */   
/*     */   public void onRender() {
/*  82 */     long before = System.nanoTime();
/*     */     
/*     */     try {
/*  85 */       if (MapProcessor.instance.getCrashedBy() == null)
/*  86 */         synchronized (MapProcessor.instance.renderThreadPauseSync) {
/*  87 */           if (!MapProcessor.instance.isWritingPaused() && !MapProcessor.instance.isWaitingForWorldUpdate() && MapProcessor.instance.getMapSaveLoad().isRegionDetectionComplete() && MapProcessor.instance.isCurrentMultiworldWritable()) {
/*  88 */             if (MapProcessor.instance.getWorld() == null || !MapProcessor.instance.caveStartIsDetermined())
/*     */               return; 
/*  90 */             if (MapProcessor.instance.getCurrentWorldString() != null && !MapProcessor.instance.ignoreWorld(MapProcessor.instance.getWorld()) && (WorldMap.settings.updateChunks || WorldMap.settings.loadChunks)) {
/*     */               double playerX, playerY, playerZ;
/*     */ 
/*     */               
/*  94 */               synchronized (MapProcessor.instance.mainStuffSync) {
/*  95 */                 if (MapProcessor.instance.mainWorld != MapProcessor.instance.getWorld()) {
/*     */                   return;
/*     */                 }
/*  98 */                 playerX = MapProcessor.instance.mainPlayerX;
/*  99 */                 playerY = MapProcessor.instance.mainPlayerY;
/* 100 */                 playerZ = MapProcessor.instance.mainPlayerZ;
/*     */               } 
/* 102 */               XaeroWorldMapCore.ensureField();
/* 103 */               long time = System.currentTimeMillis();
/* 104 */               long passed = (this.lastWrite == -1L) ? 0L : (time - this.lastWrite);
/* 105 */               long tilesToUpdate = Math.min(passed * 3L / 4L, 100L);
/*     */               
/* 107 */               if (this.lastWrite == -1L || tilesToUpdate != 0L)
/* 108 */                 this.lastWrite = time; 
/* 109 */               long sinceLastTry = time - this.lastWriteTry;
/* 110 */               int timeLimit = (this.lastWriteTry == -1L) ? 1000000 : (int)(Math.min(sinceLastTry, 50L) * 0.08696D * 1000000.0D);
/* 111 */               this.lastWriteTry = time;
/* 112 */               for (int i = 0; i < tilesToUpdate * 2L && System.nanoTime() - before < timeLimit; i += 2) {
/* 113 */                 if (writeMap(MapProcessor.instance.getWorld(), playerX, playerY, playerZ)) {
/* 114 */                   i--;
/*     */                 }
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         }  
/* 120 */     } catch (Throwable e) {
/* 121 */       MapProcessor.instance.setCrashedBy(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean writeMap(World world, double playerX, double playerY, double playerZ) {
/* 126 */     int loadDistance = (Minecraft.func_71410_x()).field_71474_y.field_151451_c;
/* 127 */     boolean onlyLoad = (WorldMap.settings.loadChunks && (!WorldMap.settings.updateChunks || this.updateCounter % 5L != 0L));
/* 128 */     synchronized (world) {
/* 129 */       if (this.insideX == 0 && this.insideZ == 0) {
/* 130 */         this.playerChunkX = (int)Math.floor(playerX) >> 4;
/* 131 */         this.playerChunkZ = (int)Math.floor(playerZ) >> 4;
/*     */       } 
/* 133 */       writeChunk(world, loadDistance, onlyLoad);
/*     */     } 
/* 135 */     return onlyLoad;
/*     */   }
/*     */   
/*     */   public void writeChunk(World world, int distance, boolean onlyLoad) {
/* 139 */     int playerTileChunkX = this.playerChunkX >> 2;
/* 140 */     int playerTileChunkZ = this.playerChunkZ >> 2;
/* 141 */     int distanceInTileChunks = playerTileChunkX - (this.playerChunkX - distance >> 2);
/* 142 */     int tileChunkX = playerTileChunkX - distanceInTileChunks + this.X;
/* 143 */     int tileChunkZ = playerTileChunkZ - distanceInTileChunks + this.Z;
/* 144 */     int regionX = tileChunkX >> 3;
/* 145 */     int regionZ = tileChunkZ >> 3;
/* 146 */     MapRegion region = MapProcessor.instance.getMapRegion(regionX, regionZ, true);
/* 147 */     MapTileChunk tileChunk = null;
/* 148 */     synchronized (region.writerThreadPauseSync) {
/* 149 */       if (!region.isWritingPaused()) {
/*     */         boolean regionIsResting;
/* 151 */         synchronized (region) {
/* 152 */           if (region.getLoadState() == 2)
/* 153 */             region.registerVisit(); 
/* 154 */           regionIsResting = region.isResting();
/* 155 */           if (regionIsResting) {
/* 156 */             region.setBeingWritten(true);
/* 157 */             int tileChunkLocalX = tileChunkX & 0x7;
/* 158 */             int tileChunkLocalZ = tileChunkZ & 0x7;
/*     */             
/* 160 */             tileChunk = region.getChunk(tileChunkLocalX, tileChunkLocalZ);
/* 161 */             if (region.getLoadState() == 2 && tileChunk == null) {
/* 162 */               region.setChunk(tileChunkLocalX, tileChunkLocalZ, tileChunk = new MapTileChunk(region, tileChunkX, tileChunkZ));
/* 163 */               tileChunk.setLoadState((byte)2);
/* 164 */             } else if (!region.reloadHasBeenRequested() && !region.recacheHasBeenRequested() && (region.getLoadState() == 0 || region.getLoadState() == 4)) {
/* 165 */               MapProcessor.instance.getMapSaveLoad().requestLoad(region, "writing");
/*     */             } 
/*     */           } 
/* 168 */         }  if (regionIsResting && region.getLoadState() == 2)
/*     */         {
/*     */           
/* 171 */           if (tileChunk != null && tileChunk.getLoadState() == 2) {
/* 172 */             if (!tileChunk.shouldUpload()) {
/* 173 */               int caveStart = MapProcessor.instance.getCaveStart();
/* 174 */               int chunkX = tileChunkX * 4 + this.insideX;
/* 175 */               int chunkZ = tileChunkZ * 4 + this.insideZ;
/* 176 */               if (chunkX >= this.playerChunkX - distance && chunkX < this.playerChunkX + distance && chunkZ >= this.playerChunkZ - distance && chunkZ < this.playerChunkZ + distance) {
/*     */                 
/* 178 */                 Chunk chunk = world.func_72964_e(chunkX, chunkZ);
/* 179 */                 MapTile mapTile = tileChunk.getTile(this.insideX, this.insideZ);
/* 180 */                 boolean chunkUpdated = false;
/*     */                 try {
/* 182 */                   chunkUpdated = (chunk != null && (mapTile == null || !((Boolean)XaeroWorldMapCore.chunkCleanField.get(chunk)).booleanValue()));
/* 183 */                 } catch (IllegalArgumentException|IllegalAccessException e) {
/* 184 */                   throw new RuntimeException(e);
/*     */                 } 
/* 186 */                 if (chunkUpdated && chunk.func_177410_o()) {
/*     */                   
/* 188 */                   boolean connectedToOthers = false;
/*     */                   int i;
/* 190 */                   label154: for (i = -1; i < 2; i++) {
/* 191 */                     for (int j = -1; j < 2; j++) {
/* 192 */                       if (i != 0 || j != 0) {
/*     */                         
/* 194 */                         Chunk neighbor = world.func_72964_e(chunkX + i, chunkZ + j);
/* 195 */                         if (neighbor != null && neighbor.func_177410_o()) {
/* 196 */                           connectedToOthers = true; break label154;
/*     */                         } 
/*     */                       } 
/*     */                     } 
/* 200 */                   }  if (connectedToOthers) {
/* 201 */                     MapTileChunk prevTileChunk = null;
/* 202 */                     if ((mapTile == null && WorldMap.settings.loadChunks) || (mapTile != null && WorldMap.settings.updateChunks && !onlyLoad)) {
/* 203 */                       if (mapTile == null) {
/* 204 */                         mapTile = MapProcessor.instance.getTilePool().get(MapProcessor.instance.getCurrentDimension(), chunkX, chunkZ);
/* 205 */                         tileChunk.setChanged(true);
/*     */                       } 
/* 207 */                       if (mapTile.getPrevTile() == null)
/* 208 */                         prevTileChunk = tileChunk.findPrevTile(null, mapTile, this.insideX, this.insideZ); 
/* 209 */                       for (int x = 0; x < 16; x++) {
/* 210 */                         for (int z = 0; z < 16; z++) {
/*     */                           
/* 212 */                           int startHeight = chunk.func_76611_b(x, z) + 3;
/* 213 */                           if (caveStart != -1)
/* 214 */                             startHeight = caveStart; 
/* 215 */                           MapBlock currentPixel = mapTile.getBlock(x, z);
/* 216 */                           loadPixel(world, this.loadingObject, currentPixel, chunk, x, z, startHeight, 0, (caveStart != -1), mapTile.wasWrittenOnce());
/* 217 */                           this.loadingObject.fixHeightType(x, z, mapTile.getPrevTile(), mapTile, tileChunk, prevTileChunk);
/* 218 */                           if (!this.loadingObject.equals(currentPixel)) {
/* 219 */                             mapTile.setBlock(x, z, this.loadingObject);
/* 220 */                             if (currentPixel != null) {
/* 221 */                               this.loadingObject = currentPixel;
/*     */                             } else {
/* 223 */                               this.loadingObject = new MapBlock();
/* 224 */                             }  tileChunk.setChanged(true);
/*     */                           } 
/*     */                         } 
/* 227 */                       }  tileChunk.setTile(this.insideX, this.insideZ, mapTile);
/* 228 */                       mapTile.setWrittenOnce(true);
/* 229 */                       mapTile.setLoaded(true);
/*     */                       
/*     */                       try {
/* 232 */                         XaeroWorldMapCore.chunkCleanField.set(chunk, Boolean.valueOf(true));
/*     */                       }
/* 234 */                       catch (IllegalArgumentException|IllegalAccessException e) {
/* 235 */                         throw new RuntimeException(e);
/*     */                       } 
/*     */                     } 
/*     */                   } 
/*     */                 } 
/*     */               } 
/*     */             } 
/* 242 */             if (!tileChunk.includeInSave()) {
/* 243 */               tileChunk = null;
/* 244 */               region.setChunk(tileChunkX & 0x7, tileChunkZ & 0x7, null);
/*     */             } 
/*     */           } 
/*     */         }
/*     */       } 
/*     */     } 
/* 250 */     this.insideZ++;
/* 251 */     if (this.insideZ > 3) {
/* 252 */       this.insideZ = 0;
/* 253 */       this.insideX++;
/* 254 */       if (this.insideX > 3) {
/* 255 */         this.insideX = 0;
/* 256 */         synchronized (region.writerThreadPauseSync) {
/* 257 */           if (tileChunk != null && !region.isWritingPaused()) {
/* 258 */             boolean regionIsResting, regionIsStillWritable = false;
/* 259 */             synchronized (region) {
/* 260 */               regionIsResting = (region.isResting() && region.getLoadState() == 2);
/* 261 */               if (regionIsResting)
/* 262 */                 region.setBeingWritten(true); 
/*     */             } 
/* 264 */             if (tileChunk.wasChanged()) {
/* 265 */               if (regionIsResting) {
/* 266 */                 tileChunk.updateBuffers(world);
/*     */                 
/* 268 */                 int localTileChunkZ = tileChunkZ & 0x7;
/* 269 */                 if (localTileChunkZ < 7) {
/* 270 */                   MapTileChunk nextChunk = region.getChunk(tileChunkX & 0x7, localTileChunkZ + 1);
/* 271 */                   if (nextChunk != null && nextChunk.tileChunkShouldTriggerUpdate(tileChunk))
/* 272 */                     nextChunk.updateBuffers(world); 
/*     */                 } else {
/* 274 */                   MapRegion nextRegion = MapProcessor.instance.getMapRegion(region.getRegionX(), region.getRegionZ() + 1, false);
/* 275 */                   if (nextRegion != null) {
/* 276 */                     int nextRegionVersion = nextRegion.getVersion();
/* 277 */                     if (nextRegionVersion > 0) {
/* 278 */                       MapTileChunk nextChunk = nextRegion.getChunk(tileChunkX & 0x7, 0);
/* 279 */                       if (nextChunk != null && nextChunk.tileChunkShouldTriggerUpdate(tileChunk)) {
/* 280 */                         nextRegion.setVersion(nextRegionVersion - 1);
/*     */                       }
/*     */                     } 
/*     */                   } 
/*     */                 } 
/*     */               } 
/*     */               
/* 287 */               tileChunk.setChanged(false);
/*     */             } 
/*     */           } 
/*     */         } 
/* 291 */         this.Z++;
/* 292 */         if (this.Z > distanceInTileChunks * 2) {
/* 293 */           this.Z = 0;
/* 294 */           this.X++;
/* 295 */           if (this.X > distanceInTileChunks * 2) {
/* 296 */             this.X = 0;
/* 297 */             this.updateCounter++;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int getBiomeColor(int type, BlockPos.MutableBlockPos pos, MapTile tile, World world) {
/* 306 */     int i = 0;
/* 307 */     int j = 0;
/* 308 */     int k = 0;
/* 309 */     int total = 0;
/* 310 */     int initX = pos.func_177958_n();
/* 311 */     int initZ = pos.func_177952_p();
/* 312 */     MapBiomes mapBiomes = MapProcessor.instance.getMapBiomes();
/* 313 */     for (int o = -1; o < 2; o++) {
/* 314 */       for (int p = -1; p < 2; p++) {
/* 315 */         pos.func_181079_c(initX + o, pos.func_177956_o(), initZ + p);
/* 316 */         int b = getBiomeAtPos((BlockPos)pos, tile);
/* 317 */         if (b != -1) {
/* 318 */           int l = 0;
/* 319 */           Biome gen = Biome.func_150568_d(b);
/* 320 */           if (gen == null) {
/* 321 */             gen = world.field_73011_w.func_177499_m().func_180300_a((BlockPos)pos, Biomes.field_76772_c);
/* 322 */             b = Biome.func_185362_a(gen);
/*     */           } 
/* 324 */           if (gen != null) {
/* 325 */             if (type == 0) {
/* 326 */               l = mapBiomes.getBiomeGrassColour(b, gen, (BlockPos)pos);
/* 327 */             } else if (type == 1) {
/* 328 */               l = mapBiomes.getBiomeFoliageColour(b, gen, (BlockPos)pos);
/*     */             } else {
/* 330 */               l = mapBiomes.getBiomeWaterColour(b, gen);
/* 331 */             }  i += l >> 16 & 0xFF;
/* 332 */             j += l >> 8 & 0xFF;
/* 333 */             k += l & 0xFF;
/* 334 */             total++;
/*     */           } 
/*     */         } 
/*     */       } 
/* 338 */     }  pos.func_181079_c(initX, pos.func_177956_o(), initZ);
/* 339 */     if (total == 0)
/* 340 */       return 0; 
/* 341 */     return (i / total & 0xFF) << 16 | (j / total & 0xFF) << 8 | k / total & 0xFF;
/*     */   }
/*     */   
/*     */   public int getBiomeAtPos(BlockPos pos, MapTile centerTile) {
/* 345 */     int tileX = pos.func_177958_n() >> 4;
/* 346 */     int tileZ = pos.func_177952_p() >> 4;
/* 347 */     MapTile tile = (tileX == centerTile.getChunkX() && tileZ == centerTile.getChunkZ()) ? centerTile : MapProcessor.instance.getMapTile(tileX, tileZ);
/* 348 */     if (tile != null && tile.isLoaded())
/* 349 */       return tile.getBlock(pos.func_177958_n() & 0xF, pos.func_177952_p() & 0xF).getBiome(); 
/* 350 */     return -1;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isGlowing(IBlockState state) {
/* 355 */     return (state.func_185906_d() >= 0.5D);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean shouldOverlay(IBlockState state, Block b, int lightOpacity) {
/* 362 */     return ((b instanceof net.minecraft.block.BlockLiquid && lightOpacity != 255 && lightOpacity != 0) || (b.func_180664_k() == BlockRenderLayer.TRANSLUCENT && lightOpacity != 255) || b instanceof net.minecraft.block.BlockGlass);
/*     */   }
/*     */   
/*     */   public boolean isInvisible(World world, IBlockState state, Block b) {
/* 366 */     if (state.func_185901_i() == EnumBlockRenderType.INVISIBLE)
/* 367 */       return true; 
/* 368 */     if (b == Blocks.field_150478_aa)
/* 369 */       return true; 
/* 370 */     if (b == Blocks.field_150329_H)
/* 371 */       return true; 
/* 372 */     if (b == Blocks.field_150398_cm)
/* 373 */       return true; 
/* 374 */     if ((b instanceof net.minecraft.block.BlockFlower || b instanceof net.minecraft.block.BlockDoublePlant) && !WorldMap.settings.flowers)
/* 375 */       return true; 
/* 376 */     MapColor materialColor = state.func_185909_g((IBlockAccess)world, (BlockPos)this.mutableGlobalPos);
/* 377 */     if (materialColor == null || materialColor.field_76291_p == 0)
/* 378 */       return true; 
/* 379 */     return false;
/*     */   }
/*     */   
/*     */   public void loadPixel(World world, MapBlock pixel, MapBlock currentPixel, Chunk bchunk, int insideX, int insideZ, int highY, int lowY, boolean cave, boolean canReuseBiomeColours) {
/* 383 */     pixel.prepareForWriting();
/* 384 */     pixel.setHeight(highY);
/* 385 */     this.overlayBuilder.startBuilding();
/* 386 */     int overlayBiome = -1;
/* 387 */     IBlockState prevOverlay = null;
/* 388 */     boolean underair = !cave;
/* 389 */     for (int h = highY; h >= lowY; h--) {
/* 390 */       this.mutableLocalPos.func_181079_c(insideX, h, insideZ);
/* 391 */       IBlockState state = bchunk.func_177435_g((BlockPos)this.mutableLocalPos);
/* 392 */       Block b = state.func_177230_c();
/* 393 */       if (b instanceof net.minecraft.block.BlockAir) {
/* 394 */         underair = true;
/*     */       
/*     */       }
/* 397 */       else if (underair) {
/*     */ 
/*     */         
/* 400 */         int stateId = Block.func_176210_f(state);
/* 401 */         this.mutableGlobalPos.func_181079_c(bchunk.field_76635_g * 16 + insideX, h, bchunk.field_76647_h * 16 + insideZ);
/* 402 */         this.mutableLocalPos.func_181079_c(insideX, Math.min(h + 1, 255), insideZ);
/* 403 */         byte light = (byte)bchunk.func_177413_a(EnumSkyBlock.BLOCK, (BlockPos)this.mutableLocalPos);
/* 404 */         if (shouldOverlay(state, b, b.getLightOpacity(state, (IBlockAccess)world, (BlockPos)this.mutableGlobalPos))) {
/* 405 */           if (state != prevOverlay) {
/* 406 */             if (canReuseBiomeColours && currentPixel != null && currentPixel.getNumberOfOverlays() > 0 && ((Overlay)currentPixel.getOverlays().get(0)).getState() == stateId) {
/* 407 */               Overlay currentTopOverlay = currentPixel.getOverlays().get(0);
/* 408 */               this.biomeBuffer[0] = currentTopOverlay.getColourType();
/* 409 */               this.biomeBuffer[1] = (currentTopOverlay.getColourType() == 1) ? currentPixel.getBiome() : -1;
/* 410 */               this.biomeBuffer[2] = currentTopOverlay.getCustomColour();
/*     */             } else {
/* 412 */               this.colorTypeCache.getOverlayBiomeColour(world, state, (BlockPos)this.mutableGlobalPos, this.biomeBuffer, -1);
/* 413 */             }  if (overlayBiome == -1)
/* 414 */               overlayBiome = this.biomeBuffer[1]; 
/* 415 */             prevOverlay = state;
/*     */           } 
/* 417 */           this.overlayBuilder.build(stateId, this.biomeBuffer, b.getLightOpacity(state, (IBlockAccess)world, (BlockPos)this.mutableGlobalPos), light, world);
/*     */         }
/* 419 */         else if (!isInvisible(world, state, b)) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 449 */           this.overlayBuilder.finishBuilding(pixel);
/* 450 */           if (!canReuseBiomeColours || currentPixel == null || currentPixel.getState() != stateId) {
/* 451 */             this.colorTypeCache.getBlockBiomeColour(world, state, (BlockPos)this.mutableGlobalPos, this.biomeBuffer, -1);
/*     */           } else {
/* 453 */             this.biomeBuffer[0] = currentPixel.getColourType();
/* 454 */             this.biomeBuffer[1] = currentPixel.getBiome();
/* 455 */             this.biomeBuffer[2] = currentPixel.getCustomColour();
/*     */           } 
/* 457 */           if (overlayBiome != -1)
/* 458 */             this.biomeBuffer[1] = overlayBiome; 
/* 459 */           boolean glowing = isGlowing(state);
/* 460 */           pixel.write(stateId, h, this.biomeBuffer, light, glowing, cave);
/*     */           return;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int loadBlockColourFromTexture(int stateId, boolean convert, World world, BlockPos globalPos) {
/* 474 */     if (this.clearCachedColours) {
/* 475 */       this.textureColours.clear();
/* 476 */       this.blockColours.clear();
/* 477 */       this.clearCachedColours = false;
/* 478 */       if (WorldMap.settings.debug) {
/* 479 */         System.out.println("Xaero's World Map cache cleared!");
/*     */       }
/*     */     } 
/* 482 */     Integer c = this.blockColours.get(Integer.valueOf(stateId));
/* 483 */     int red = 0;
/* 484 */     int green = 0;
/* 485 */     int blue = 0;
/*     */     
/* 487 */     IBlockState state = Misc.getStateById(stateId);
/* 488 */     Block b = state.func_177230_c();
/* 489 */     if (c == null) {
/* 490 */       String name = null; try {
/*     */         TextureAtlasSprite texture;
/* 492 */         List<BakedQuad> upQuads = null;
/* 493 */         BlockModelShapes bms = Minecraft.func_71410_x().func_175602_ab().func_175023_a();
/* 494 */         if (convert) {
/* 495 */           upQuads = bms.func_178125_b(state).func_188616_a(state, EnumFacing.UP, 0L);
/*     */         }
/* 497 */         if (upQuads == null || upQuads.isEmpty() || ((BakedQuad)upQuads.get(0)).func_187508_a() == bms.func_178126_b().func_174952_b().func_174944_f()) {
/* 498 */           texture = bms.func_178122_a(state);
/*     */         } else {
/* 500 */           texture = ((BakedQuad)upQuads.get(0)).func_187508_a();
/* 501 */         }  name = texture.func_94215_i() + ".png";
/* 502 */         if (b instanceof net.minecraft.block.BlockOre && b != Blocks.field_150449_bY)
/* 503 */           name = "minecraft:blocks/stone.png"; 
/* 504 */         c = Integer.valueOf(-1);
/* 505 */         String[] args = name.split(":");
/* 506 */         if (args.length < 2) {
/* 507 */           DEFAULT_RESOURCE[1] = args[0];
/* 508 */           args = DEFAULT_RESOURCE;
/*     */         } 
/* 510 */         Integer cachedColour = this.textureColours.get(name);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 517 */         if (cachedColour == null)
/*     */         
/*     */         { 
/* 520 */           ResourceLocation location = new ResourceLocation(args[0], "textures/" + args[1]);
/* 521 */           IResource resource = Minecraft.func_71410_x().func_110442_L().func_110536_a(location);
/*     */           
/* 523 */           InputStream input = resource.func_110527_b();
/* 524 */           BufferedImage img = TextureUtil.func_177053_a(input);
/* 525 */           red = 0;
/* 526 */           green = 0;
/* 527 */           blue = 0;
/* 528 */           int total = 64;
/* 529 */           int tw = img.getWidth();
/* 530 */           int diff = tw / 8;
/* 531 */           for (int i = 0; i < 8; i++) {
/* 532 */             for (int j = 0; j < 8; j++) {
/* 533 */               int rgb = img.getRGB(i * diff, j * diff);
/* 534 */               int alpha = rgb >> 24 & 0xFF;
/* 535 */               if (rgb == 0 || alpha == 0) {
/* 536 */                 total--;
/*     */               } else {
/*     */                 
/* 539 */                 red += rgb >> 16 & 0xFF;
/* 540 */                 green += rgb >> 8 & 0xFF;
/* 541 */                 blue += rgb & 0xFF;
/*     */               } 
/*     */             } 
/* 544 */           }  input.close();
/* 545 */           if (total == 0)
/* 546 */             total = 1; 
/* 547 */           red /= total;
/* 548 */           green /= total;
/* 549 */           blue /= total;
/* 550 */           if (convert && red == 0 && green == 0 && blue == 0) {
/* 551 */             throw new Exception("Black texture");
/*     */           }
/* 553 */           c = Integer.valueOf(0xFF000000 | red << 16 | green << 8 | blue);
/* 554 */           this.textureColours.put(name, c); }
/*     */         else
/* 556 */         { c = cachedColour; } 
/* 557 */       } catch (FileNotFoundException e) {
/* 558 */         if (convert) {
/* 559 */           return loadBlockColourFromTexture(stateId, false, world, globalPos);
/*     */         }
/* 561 */         c = Integer.valueOf((state.func_185909_g((IBlockAccess)world, globalPos)).field_76291_p);
/* 562 */         if (name != null) {
/* 563 */           this.textureColours.put(name, c);
/*     */         }
/* 565 */         System.out.println("Block file not found: " + Block.field_149771_c.func_177774_c(b));
/*     */       }
/* 567 */       catch (Exception e) {
/* 568 */         c = Integer.valueOf((state.func_185909_g((IBlockAccess)world, globalPos)).field_76291_p);
/* 569 */         if (name != null)
/* 570 */           this.textureColours.put(name, c); 
/* 571 */         System.out.println("Block " + Block.field_149771_c.func_177774_c(b) + " has no texture, using material colour.");
/*     */       } 
/* 573 */       if (c != null)
/* 574 */         this.blockColours.put(Integer.valueOf(stateId), c); 
/*     */     } 
/* 576 */     return c.intValue();
/*     */   }
/*     */   
/*     */   public long getUpdateCounter() {
/* 580 */     return this.updateCounter;
/*     */   }
/*     */   
/*     */   public void resetPosition() {
/* 584 */     this.X = 0;
/* 585 */     this.Z = 0;
/* 586 */     this.insideX = 0;
/* 587 */     this.insideZ = 0;
/*     */   }
/*     */   
/*     */   public void requestCachedColoursClear() {
/* 591 */     this.clearCachedColours = true;
/*     */   }
/*     */   
/*     */   public BlockStateColorTypeCache getColorTypeCache() {
/* 595 */     return this.colorTypeCache;
/*     */   }
/*     */ }


/* Location:              C:\Users\ioanm\Downloads\XaerosWorldMap_1.7.3_Forge_1.12.jar!\xaero\map\MapWriter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */