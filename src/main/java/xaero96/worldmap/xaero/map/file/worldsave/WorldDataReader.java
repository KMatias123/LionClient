/*     */ package xaero.map.file.worldsave;
/*     */ 
/*     */ import java.io.DataInputStream;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.nbt.CompressedStreamTools;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.nbt.NBTTagList;
/*     */ import net.minecraft.util.datafix.FixTypes;
/*     */ import net.minecraft.util.datafix.IFixType;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.chunk.storage.RegionFile;
/*     */ import net.minecraft.world.chunk.storage.RegionFileCache;
/*     */ import xaero.map.MapProcessor;
/*     */ import xaero.map.Misc;
/*     */ import xaero.map.cache.BlockStateColorTypeCache;
/*     */ import xaero.map.pool.PoolUnit;
/*     */ import xaero.map.region.MapBlock;
/*     */ import xaero.map.region.MapRegion;
/*     */ import xaero.map.region.MapTile;
/*     */ import xaero.map.region.MapTileChunk;
/*     */ import xaero.map.region.OverlayBuilder;
/*     */ import xaero.map.region.OverlayManager;
/*     */ 
/*     */ public class WorldDataReader
/*     */ {
/*     */   private boolean[] underair;
/*     */   private boolean[] blockFound;
/*     */   private byte[] lightLevels;
/*     */   private int[] biomeBuffer;
/*     */   private MapBlock buildingObject;
/*     */   private OverlayBuilder[] overlayBuilders;
/*     */   private IBlockState[] prevOverlays;
/*     */   private BlockPos.MutableBlockPos mutableBlockPos;
/*     */   private BlockStateColorTypeCache colorTypeCache;
/*     */   
/*     */   public WorldDataReader(OverlayManager overlayManager, BlockStateColorTypeCache colorTypeCache) {
/*  43 */     this.colorTypeCache = colorTypeCache;
/*  44 */     this.buildingObject = new MapBlock();
/*  45 */     this.underair = new boolean[256];
/*  46 */     this.blockFound = new boolean[256];
/*  47 */     this.lightLevels = new byte[256];
/*  48 */     this.biomeBuffer = new int[3];
/*  49 */     this.prevOverlays = new IBlockState[256];
/*  50 */     this.overlayBuilders = new OverlayBuilder[256];
/*  51 */     this.mutableBlockPos = new BlockPos.MutableBlockPos();
/*  52 */     for (int i = 0; i < this.overlayBuilders.length; i++)
/*  53 */       this.overlayBuilders[i] = new OverlayBuilder(overlayManager); 
/*     */   }
/*     */   public boolean buildRegion(MapRegion region, File worldDir, World world, boolean loading, int[] chunkCountDest) {
/*     */     boolean regionIsResting;
/*  57 */     if (!loading)
/*  58 */       region.pushWriterPause(); 
/*  59 */     boolean result = true;
/*  60 */     int prevRegX = region.getRegionX();
/*  61 */     int prevRegZ = region.getRegionZ() - 1;
/*  62 */     MapRegion prevRegion = MapProcessor.instance.getMapRegion(prevRegX, prevRegZ, false);
/*     */ 
/*     */     
/*  65 */     synchronized (region) {
/*  66 */       regionIsResting = region.isResting();
/*  67 */       if (!loading && regionIsResting) {
/*  68 */         region.setBeingWritten(true);
/*  69 */         region.setShouldCache(false, "world save");
/*  70 */         region.setReloadHasBeenRequested(false, "world save");
/*  71 */         region.setVersion(MapProcessor.instance.getGlobalVersion());
/*  72 */         if (region.getLoadState() != 2) {
/*  73 */           if (region.getLoadState() == 4)
/*  74 */             region.restoreBufferUpdateObjects(); 
/*  75 */           region.setLoadState((byte)2);
/*  76 */           region.setLastSaveTime(System.currentTimeMillis() + 100000L);
/*  77 */           MapProcessor.instance.addToProcess(region);
/*     */         } else {
/*  79 */           MapProcessor.instance.removeToRefresh(region);
/*  80 */           region.setRefreshing(false);
/*     */         } 
/*     */       } 
/*     */     } 
/*  84 */     int caveStart = MapProcessor.instance.getCaveStart();
/*  85 */     if (loading || (region.getLoadState() == 2 && regionIsResting)) {
/*  86 */       RegionFile regionFile = RegionFileCache.func_76550_a(worldDir, region.getRegionX() * 32, region.getRegionZ() * 32);
/*  87 */       for (int i = 0; i < 8; i++) {
/*  88 */         for (int j = 0; j < 8; j++) {
/*  89 */           MapTileChunk tileChunk = region.getChunk(i, j);
/*  90 */           if (tileChunk == null)
/*  91 */             region.setChunk(i, j, tileChunk = new MapTileChunk(region, (region.getRegionX() << 3) + i, (region.getRegionZ() << 3) + j)); 
/*  92 */           buildTileChunk(tileChunk, caveStart, prevRegion, regionFile, world);
/*  93 */           tileChunk.setLoadState((byte)2);
/*  94 */           if (!tileChunk.includeInSave())
/*  95 */           { tileChunk = null;
/*  96 */             region.setChunk(i, j, null); }
/*     */           else
/*  98 */           { chunkCountDest[0] = chunkCountDest[0] + 1; } 
/*     */         } 
/* 100 */       }  if (region.isMultiplayer())
/* 101 */         region.setLastSaveTime(System.currentTimeMillis() - 60000L + 1500L); 
/*     */     } else {
/* 103 */       result = false;
/* 104 */     }  if (!loading)
/* 105 */       region.popWriterPause(); 
/* 106 */     return result;
/*     */   }
/*     */   
/*     */   private void buildTileChunk(MapTileChunk tileChunk, int caveStart, MapRegion prevRegion, RegionFile regionFile, World world) {
/* 110 */     tileChunk.unincludeInSave();
/* 111 */     tileChunk.resetMasks();
/* 112 */     for (int insideX = 0; insideX < 4; insideX++) {
/* 113 */       for (int insideZ = 0; insideZ < 4; insideZ++) {
/* 114 */         MapTile tile = tileChunk.getTile(insideX, insideZ);
/* 115 */         int chunkX = (tileChunk.getX() << 2) + insideX;
/* 116 */         int chunkZ = (tileChunk.getZ() << 2) + insideZ;
/* 117 */         DataInputStream datainputstream = regionFile.func_76704_a(chunkX & 0x1F, chunkZ & 0x1F);
/* 118 */         if (datainputstream == null) {
/* 119 */           if (tile != null) {
/* 120 */             tileChunk.setChanged(true);
/* 121 */             tileChunk.setTile(insideX, insideZ, null);
/* 122 */             MapProcessor.instance.getTilePool().addToPool((PoolUnit)tile);
/*     */           } 
/*     */         } else {
/*     */           
/* 126 */           boolean createdTile = false;
/* 127 */           if (tile == null) {
/* 128 */             tile = MapProcessor.instance.getTilePool().get(MapProcessor.instance.getCurrentDimension(), chunkX, chunkZ);
/* 129 */             createdTile = true;
/*     */           } 
/* 131 */           if (tile.getPrevTile() == null) {
/* 132 */             tileChunk.findPrevTile(prevRegion, tile, insideX, insideZ);
/*     */           }
/*     */           try {
/* 135 */             nbttagcompound = CompressedStreamTools.func_74794_a(datainputstream);
/* 136 */             datainputstream.close();
/* 137 */           } catch (IOException e) {
/*     */             try {
/* 139 */               datainputstream.close();
/* 140 */             } catch (IOException e1) {
/* 141 */               e1.printStackTrace();
/*     */             } 
/* 143 */             System.out.println(String.format("Error loading chunk nbt for chunk %d %d!", new Object[] { Integer.valueOf(chunkX), Integer.valueOf(chunkZ) }));
/* 144 */             e.printStackTrace();
/* 145 */             if (tile != null) {
/* 146 */               tileChunk.setTile(insideX, insideZ, null);
/* 147 */               MapProcessor.instance.getTilePool().addToPool((PoolUnit)tile);
/*     */             } 
/*     */           } 
/*     */           
/* 151 */           NBTTagCompound nbttagcompound = Minecraft.func_71410_x().func_184126_aj().func_188257_a((IFixType)FixTypes.CHUNK, nbttagcompound);
/* 152 */           if (buildTile(nbttagcompound, tile, tileChunk, chunkX, chunkZ, caveStart, world))
/* 153 */           { tileChunk.setTile(insideX, insideZ, tile);
/* 154 */             if (createdTile)
/* 155 */               tileChunk.setChanged(true);  }
/*     */           else
/* 157 */           { tileChunk.setTile(insideX, insideZ, null);
/* 158 */             MapProcessor.instance.getTilePool().addToPool((PoolUnit)tile); } 
/*     */         } 
/*     */       } 
/* 161 */     }  if (tileChunk.wasChanged()) {
/* 162 */       tileChunk.setToUpdateBuffers(true);
/* 163 */       tileChunk.setChanged(false);
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean buildTile(NBTTagCompound nbttagcompound, MapTile tile, MapTileChunk tileChunk, int chunkX, int chunkZ, int caveStart, World world) {
/* 168 */     NBTTagCompound levelCompound = nbttagcompound.func_74775_l("Level");
/* 169 */     if (levelCompound.func_74771_c("LightPopulated") == 0 || levelCompound.func_74771_c("TerrainPopulated") == 0)
/* 170 */       return false; 
/* 171 */     int fillCounter = 256;
/* 172 */     for (int i = 0; i < this.blockFound.length; i++) {
/* 173 */       this.overlayBuilders[i].startBuilding();
/* 174 */       this.blockFound[i] = false; this.underair[i] = false;
/* 175 */       this.prevOverlays[i] = null;
/*     */     } 
/* 177 */     int[] heightMap = levelCompound.func_74759_k("HeightMap");
/* 178 */     boolean heightMapExists = (heightMap.length == 256);
/* 179 */     byte[] biomes = null;
/* 180 */     int[] biomesInt = null;
/* 181 */     boolean biomesDataExists = false;
/* 182 */     if (levelCompound.func_150297_b("Biomes", 7)) {
/* 183 */       biomes = levelCompound.func_74770_j("Biomes");
/* 184 */       biomesDataExists = (biomes.length == 256);
/* 185 */     } else if (levelCompound.func_150297_b("Biomes", 11)) {
/* 186 */       biomesInt = levelCompound.func_74759_k("Biomes");
/* 187 */       biomesDataExists = (biomesInt.length == 256);
/*     */     } 
/* 189 */     boolean cave = (caveStart != -1);
/* 190 */     NBTTagList sectionsList = levelCompound.func_150295_c("Sections", 10);
/* 191 */     if (sectionsList.func_74745_c() == 0)
/* 192 */       return false; 
/* 193 */     for (int j = sectionsList.func_74745_c() - 1; j >= 0 && fillCounter > 0; j--) {
/* 194 */       NBTTagCompound sectionCompound = sectionsList.func_150305_b(j);
/*     */       
/* 196 */       boolean hasBlocks = sectionCompound.func_150297_b("Blocks", 7);
/* 197 */       boolean hasPalette = sectionCompound.func_150297_b("Palette", 11);
/* 198 */       byte[] blockIds_a = null;
/* 199 */       byte[] dataArray = null;
/* 200 */       byte[] addArray = null;
/* 201 */       byte[] add2Array = null;
/* 202 */       int[] palette = null;
/* 203 */       if (hasBlocks) {
/* 204 */         blockIds_a = sectionCompound.func_74770_j("Blocks");
/* 205 */         addArray = sectionCompound.func_150297_b("Add", 7) ? sectionCompound.func_74770_j("Add") : null;
/* 206 */         add2Array = sectionCompound.func_150297_b("Add2", 7) ? sectionCompound.func_74770_j("Add2") : null;
/* 207 */         dataArray = sectionCompound.func_74770_j("Data");
/*     */       } 
/* 209 */       if (hasPalette)
/* 210 */         palette = sectionCompound.func_74759_k("Palette"); 
/* 211 */       byte[] lightMap = null;
/* 212 */       if (sectionCompound.func_150297_b("BlockLight", 7)) {
/* 213 */         lightMap = sectionCompound.func_74770_j("BlockLight");
/* 214 */         if (lightMap.length != 2048)
/* 215 */           lightMap = null; 
/*     */       } 
/* 217 */       int sectionHeight = sectionCompound.func_74771_c("Y") * 16;
/* 218 */       for (int z = 0; z < 16; z++) {
/* 219 */         for (int x = 0; x < 16; x++) {
/* 220 */           int pos_2d = (z << 4) + x;
/* 221 */           if (!this.blockFound[pos_2d]) {
/*     */             
/* 223 */             int height = heightMapExists ? heightMap[pos_2d] : 256;
/* 224 */             int startHeight = height + 3;
/* 225 */             if (cave)
/* 226 */               startHeight = caveStart; 
/* 227 */             if (j <= 0 || startHeight >= sectionHeight)
/*     */             
/* 229 */             { int biome = biomesDataExists ? ((biomesInt != null) ? biomesInt[pos_2d] : (biomes[pos_2d] & 0xFF)) : 0;
/* 230 */               int localStartHeight = 15;
/* 231 */               if (!cave)
/* 232 */                 this.underair[pos_2d] = true; 
/* 233 */               if (startHeight >> 4 << 4 == sectionHeight)
/* 234 */                 localStartHeight = startHeight & 0xF; 
/* 235 */               for (int y = localStartHeight; y >= 0; y--)
/* 236 */               { int h = sectionHeight + y;
/* 237 */                 int pos = y << 8 | pos_2d;
/* 238 */                 int blockId = 0;
/* 239 */                 int blockMeta = 0;
/* 240 */                 if (hasPalette) {
/*     */                   
/* 242 */                   int leftIndexPart = hasBlocks ? ((blockIds_a[pos] & 0xFF) << 4) : 0;
/* 243 */                   int paletteIndex = leftIndexPart | nibbleValue(dataArray, pos);
/* 244 */                   int blockStateOtherId = palette[paletteIndex];
/* 245 */                   blockId = blockStateOtherId >> 4;
/* 246 */                   blockMeta = blockStateOtherId & 0xF;
/* 247 */                 } else if (hasBlocks) {
/* 248 */                   blockId = blockIds_a[pos] & 0xFF | ((addArray == null) ? 0 : (nibbleValue(addArray, pos) << 8));
/* 249 */                   if (add2Array != null)
/* 250 */                     blockId |= nibbleValue(add2Array, pos) << 12; 
/* 251 */                   blockMeta = nibbleValue(dataArray, pos);
/*     */                 } 
/* 253 */                 int stateId = Block.func_176210_f(Block.func_149729_e(blockId).func_176203_a(blockMeta));
/* 254 */                 this.mutableBlockPos.func_181079_c(chunkX << 4 | x, sectionHeight | y, chunkZ << 4 | z);
/* 255 */                 boolean buildResult = buildPixel(this.buildingObject, stateId, x, h, z, pos_2d, this.biomeBuffer, this.lightLevels[pos_2d], biome, cave, this.overlayBuilders[pos_2d], world, this.mutableBlockPos);
/* 256 */                 if (!buildResult && y == 0 && j == 0) {
/* 257 */                   this.buildingObject.prepareForWriting();
/* 258 */                   buildResult = true;
/*     */                 } 
/* 260 */                 if (buildResult) {
/* 261 */                   MapBlock currentPixel = tile.getBlock(x, z);
/* 262 */                   if (!this.buildingObject.equals(currentPixel)) {
/* 263 */                     tile.setBlock(x, z, this.buildingObject);
/* 264 */                     if (currentPixel != null) {
/* 265 */                       this.buildingObject = currentPixel;
/*     */                     } else {
/* 267 */                       this.buildingObject = new MapBlock();
/* 268 */                     }  tileChunk.setChanged(true);
/*     */                   } 
/* 270 */                   this.blockFound[pos_2d] = true;
/* 271 */                   fillCounter--;
/*     */                   break;
/*     */                 } 
/* 274 */                 this.lightLevels[pos_2d] = (lightMap == null) ? 0 : nibbleValue(lightMap, pos); }  } 
/*     */           } 
/*     */         } 
/*     */       } 
/* 278 */     }  tile.setWrittenOnce(true);
/* 279 */     tile.setLoaded(true);
/* 280 */     return true;
/*     */   }
/*     */   
/*     */   private boolean buildPixel(MapBlock pixel, int stateId, int x, int h, int z, int pos_2d, int[] biomeBuffer, byte light, int biome, boolean cave, OverlayBuilder overlayBuilder, World world, BlockPos.MutableBlockPos mutableBlockPos) {
/* 284 */     IBlockState state = Misc.getStateById(stateId);
/* 285 */     Block b = state.func_177230_c();
/* 286 */     if (b instanceof net.minecraft.block.BlockAir) {
/* 287 */       this.underair[pos_2d] = true;
/* 288 */       return false;
/*     */     } 
/* 290 */     if (!this.underair[pos_2d])
/* 291 */       return false; 
/* 292 */     int lightOpacity = state.getLightOpacity((IBlockAccess)world, (BlockPos)mutableBlockPos);
/* 293 */     if (MapProcessor.instance.getMapWriter().shouldOverlay(state, b, lightOpacity)) {
/* 294 */       if (state != this.prevOverlays[pos_2d]) {
/* 295 */         this.colorTypeCache.getOverlayBiomeColour(world, state, (BlockPos)mutableBlockPos, biomeBuffer, 1);
/* 296 */         this.prevOverlays[pos_2d] = state;
/*     */       } 
/* 298 */       overlayBuilder.build(stateId, biomeBuffer, lightOpacity, light, world);
/* 299 */       return false;
/* 300 */     }  if (MapProcessor.instance.getMapWriter().isInvisible(world, state, b)) {
/* 301 */       return false;
/*     */     }
/* 303 */     pixel.prepareForWriting();
/* 304 */     overlayBuilder.finishBuilding(pixel);
/* 305 */     this.colorTypeCache.getBlockBiomeColour(world, state, (BlockPos)mutableBlockPos, biomeBuffer, biome);
/* 306 */     if (pixel.getNumberOfOverlays() > 0)
/* 307 */       biomeBuffer[1] = biome; 
/* 308 */     boolean glowing = MapProcessor.instance.getMapWriter().isGlowing(state);
/* 309 */     pixel.write(stateId, h, biomeBuffer, light, glowing, cave);
/* 310 */     return true;
/*     */   }
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
/*     */   private byte nibbleValue(byte[] array, int index) {
/* 331 */     byte b = array[index >> 1];
/* 332 */     if ((index & 0x1) == 0) {
/* 333 */       return (byte)(b & 0xF);
/*     */     }
/* 335 */     return (byte)(b >> 4 & 0xF);
/*     */   }
/*     */ }


/* Location:              C:\Users\ioanm\Downloads\XaerosWorldMap_1.7.3_Forge_1.12.jar!\xaero\map\file\worldsave\WorldDataReader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */