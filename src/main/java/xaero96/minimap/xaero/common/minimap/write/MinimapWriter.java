/*      */ package xaero.common.minimap.write;
/*      */ 
/*      */ import java.awt.image.BufferedImage;
/*      */ import java.io.FileNotFoundException;
/*      */ import java.io.InputStream;
/*      */ import java.util.ArrayList;
/*      */ import java.util.HashMap;
/*      */ import java.util.List;
/*      */ import net.minecraft.block.Block;
/*      */ import net.minecraft.block.material.MapColor;
/*      */ import net.minecraft.block.state.IBlockState;
/*      */ import net.minecraft.client.Minecraft;
/*      */ import net.minecraft.client.renderer.BlockModelShapes;
/*      */ import net.minecraft.client.renderer.block.model.BakedQuad;
/*      */ import net.minecraft.client.renderer.block.model.IBakedModel;
/*      */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*      */ import net.minecraft.client.renderer.texture.TextureUtil;
/*      */ import net.minecraft.client.resources.IResource;
/*      */ import net.minecraft.init.Blocks;
/*      */ import net.minecraft.util.BlockRenderLayer;
/*      */ import net.minecraft.util.EnumBlockRenderType;
/*      */ import net.minecraft.util.EnumFacing;
/*      */ import net.minecraft.util.ReportedException;
/*      */ import net.minecraft.util.ResourceLocation;
/*      */ import net.minecraft.util.math.BlockPos;
/*      */ import net.minecraft.world.EnumSkyBlock;
/*      */ import net.minecraft.world.IBlockAccess;
/*      */ import net.minecraft.world.World;
/*      */ import net.minecraft.world.chunk.Chunk;
/*      */ import xaero.common.IXaeroMinimap;
/*      */ import xaero.common.core.XaeroMinimapCore;
/*      */ import xaero.common.minimap.MinimapProcessor;
/*      */ import xaero.common.minimap.region.MinimapChunk;
/*      */ import xaero.common.minimap.region.MinimapTile;
/*      */ import xaero.common.misc.OptimizedMath;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class MinimapWriter
/*      */ {
/*   48 */   private static final String[] dimensionsToIgnore = new String[] { "FZHammer" };
/*      */   
/*      */   private static final int UPDATE_EVERY_RUNS = 5;
/*      */   
/*      */   private static final int MAXIMUM_OVERLAYS = 5;
/*      */   
/*      */   private IXaeroMinimap modMain;
/*      */   
/*      */   private MinimapWriterHelper helper;
/*      */   private int loadingSideInChunks;
/*      */   private int updateRadius;
/*      */   private MinimapChunk[][] loadingBlocks;
/*      */   private int loadingMapChunkX;
/*      */   private int loadingMapChunkZ;
/*      */   private int loadingCaving;
/*      */   private int loadingLevels;
/*      */   private boolean loadingTerrainSlopes;
/*      */   private boolean loadingTerrainSlopesExperiment;
/*      */   private boolean loadingTerrainDepth;
/*      */   private boolean loadingRedstone;
/*      */   private int loadingColours;
/*      */   private boolean loadingTransparency;
/*      */   private boolean loadingBiomesVanillaMode;
/*      */   private int loadingDimension;
/*      */   private int loadingCaveMapsDepth;
/*      */   private MinimapChunk[][] loadedBlocks;
/*      */   private int loadedMapChunkX;
/*      */   private int loadedMapChunkZ;
/*      */   private int loadedCaving;
/*      */   private int loadedLevels;
/*      */   private boolean loadedTerrainSlopes;
/*      */   private boolean loadedTerrainSlopesExperiment;
/*      */   private boolean loadedTerrainDepth;
/*      */   private boolean loadedRedstone;
/*      */   private int loadedColours;
/*      */   private boolean loadedTransparency;
/*      */   private boolean loadedBiomesVanillaMode;
/*      */   private int loadedDimension;
/*      */   private int loadedCaveMapsDepth;
/*      */   private boolean settingsChanged;
/*      */   private int updateChunkX;
/*      */   private int updateChunkZ;
/*      */   private int tileInsideX;
/*      */   private int tileInsideZ;
/*      */   private int runNumber;
/*      */   private boolean previousShouldLoad;
/*      */   private int lastCaving;
/*      */   private boolean clearBlockColours;
/*      */   private HashMap<String, Integer> textureColours;
/*      */   private HashMap<Integer, Integer> blockColours;
/*      */   private HashMap<Block, Boolean> transparentCache;
/*      */   private HashMap<IBlockState, Boolean> glowingCache;
/*  100 */   private long lastWrite = -1L;
/*  101 */   private long lastWriteTry = -1L;
/*      */   private boolean forcedRefresh;
/*      */   private MinimapChunk oldChunk;
/*  104 */   private int[][] lastBlockY = new int[4][16];
/*      */   
/*      */   private int updates;
/*      */   
/*      */   private int loads;
/*      */   
/*      */   private long before;
/*      */   
/*      */   private int processingTime;
/*      */   public boolean debugTotalTime = false;
/*  114 */   public long minTime = -1L;
/*  115 */   public long maxTime = -1L;
/*      */   public long totalTime;
/*      */   public long totalRuns;
/*  118 */   public long lastDebugTime = -1L;
/*      */   
/*      */   public long minTimeDebug;
/*      */   
/*      */   public long maxTimeDebug;
/*      */   
/*      */   public long averageTimeDebug;
/*      */   
/*      */   private long currentComparisonCode;
/*      */   
/*      */   private List<Integer> pixelTransparentSizes;
/*      */   
/*      */   private List<IBlockState> pixelBlockStates;
/*      */   
/*      */   private List<Integer> pixelBlockLights;
/*      */   
/*      */   private int firstBlockY;
/*      */   
/*      */   boolean isglowing;
/*      */   
/*      */   private int[] underRed;
/*      */   
/*      */   private int[] underGreen;
/*      */   
/*      */   private int[] underBlue;
/*      */   
/*      */   private int sun;
/*      */   private float currentTransparencyMultiplier;
/*      */   private int blockY;
/*      */   private int blockColor;
/*      */   private final int[] red;
/*      */   private final int[] green;
/*      */   private final int[] blue;
/*      */   private final float[] brightness;
/*      */   private final float[] postBrightness;
/*      */   private final int[] tempColor;
/*      */   private double secondaryB;
/*      */   private int[][] lastSlopeShades;
/*      */   private boolean[] topMostChunk;
/*      */   private BlockPos.MutableBlockPos mutableBlockPos;
/*      */   private BlockPos.MutableBlockPos mutableBlockPos2;
/*      */   private byte[][] bufferUpdateArrayBuffers;
/*      */   private Long seedForLoading;
/*      */   
/*      */   public MinimapWriter(IXaeroMinimap modMain) {
/*  163 */     this.modMain = modMain;
/*  164 */     this.loadingSideInChunks = 16;
/*  165 */     this.updateRadius = 16;
/*  166 */     this.loadingCaving = -1;
/*  167 */     this.lastCaving = -1;
/*  168 */     this.textureColours = new HashMap<>();
/*  169 */     this.blockColours = new HashMap<>();
/*  170 */     this.loadedCaving = -1;
/*      */     
/*  172 */     this.red = new int[5];
/*  173 */     this.green = new int[5];
/*  174 */     this.blue = new int[5];
/*  175 */     this.underRed = new int[5];
/*  176 */     this.underGreen = new int[5];
/*  177 */     this.underBlue = new int[5];
/*  178 */     this.brightness = new float[5];
/*  179 */     this.postBrightness = new float[5];
/*  180 */     this.tempColor = new int[3];
/*  181 */     this.lastSlopeShades = new int[4][16];
/*  182 */     this.helper = new MinimapWriterHelper();
/*  183 */     this.mutableBlockPos = new BlockPos.MutableBlockPos();
/*  184 */     this.mutableBlockPos2 = new BlockPos.MutableBlockPos();
/*  185 */     this.bufferUpdateArrayBuffers = new byte[5][16384];
/*  186 */     this.pixelBlockStates = new ArrayList<>();
/*  187 */     this.pixelTransparentSizes = new ArrayList<>();
/*  188 */     this.pixelBlockLights = new ArrayList<>();
/*  189 */     this.transparentCache = new HashMap<>();
/*  190 */     this.glowingCache = new HashMap<>();
/*  191 */     this.topMostChunk = new boolean[4];
/*  192 */     for (int i = 0; i < 4; i++)
/*  193 */       this.topMostChunk[i] = true; 
/*      */   }
/*      */   
/*      */   private void updateTimeDebug(long before) {
/*  197 */     if (this.debugTotalTime) {
/*  198 */       long debugPassed = System.nanoTime() - before;
/*  199 */       this.totalTime += debugPassed;
/*  200 */       this.totalRuns++;
/*  201 */       if (debugPassed > this.maxTime)
/*  202 */         this.maxTime = debugPassed; 
/*  203 */       if (this.minTime == -1L || debugPassed < this.minTime)
/*  204 */         this.minTime = debugPassed; 
/*  205 */       long time = System.currentTimeMillis();
/*  206 */       if (this.lastDebugTime == -1L) {
/*  207 */         this.lastDebugTime = time;
/*  208 */       } else if (time - this.lastDebugTime > 1000L) {
/*  209 */         this.maxTimeDebug = this.maxTime;
/*  210 */         this.minTimeDebug = this.minTime;
/*  211 */         this.averageTimeDebug = this.totalTime / this.totalRuns;
/*  212 */         this.maxTime = -1L;
/*  213 */         this.minTime = -1L;
/*  214 */         this.totalTime = 0L;
/*  215 */         this.totalRuns = 0L;
/*  216 */         this.lastDebugTime = time;
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public void onRender() {
/*  222 */     long before = System.nanoTime();
/*      */     try {
/*  224 */       World world = null;
/*  225 */       double playerX = 0.0D;
/*  226 */       double playerY = 0.0D;
/*  227 */       double playerZ = 0.0D;
/*  228 */       if (this.modMain.getInterfaces() != null) {
/*  229 */         MinimapProcessor minimapProcessor = this.modMain.getInterfaces().getMinimap();
/*  230 */         synchronized (minimapProcessor.mainStuffSync) {
/*  231 */           world = minimapProcessor.mainWorld;
/*  232 */           playerX = minimapProcessor.mainPlayerX;
/*  233 */           playerY = minimapProcessor.mainPlayerY;
/*  234 */           playerZ = minimapProcessor.mainPlayerZ;
/*      */         } 
/*      */       } 
/*  237 */       if (this.modMain.getSettings() == null || !this.modMain.getSettings().getMinimap() || world == null) {
/*  238 */         updateTimeDebug(before);
/*      */         return;
/*      */       } 
/*  241 */       boolean shouldLoad = (!ignoreWorld(world) && (!this.modMain.getSupportMods().shouldUseWorldMapChunks() || this.loadingCaving != -1 || this.loadedCaving != -1 || getCaving(playerX, playerY, playerZ, world) != -1));
/*  242 */       if (shouldLoad != this.previousShouldLoad) {
/*  243 */         this.tileInsideX = this.tileInsideZ = this.updateChunkX = this.updateChunkZ = 0;
/*  244 */         this.previousShouldLoad = shouldLoad;
/*      */       } 
/*  246 */       if (!shouldLoad) {
/*  247 */         updateTimeDebug(before);
/*      */         return;
/*      */       } 
/*  250 */       XaeroMinimapCore.ensureField();
/*  251 */       long time = System.currentTimeMillis();
/*  252 */       long passed = (this.lastWrite == -1L) ? 0L : (time - this.lastWrite);
/*  253 */       long tilesToUpdate = Math.min(passed * 3L / 4L, 100L);
/*      */       
/*  255 */       if (this.lastWrite == -1L || tilesToUpdate != 0L)
/*  256 */         this.lastWrite = time; 
/*  257 */       long sinceLastTry = time - this.lastWriteTry;
/*  258 */       int timeLimit = (this.lastWriteTry == -1L) ? 1000000 : (int)(Math.min(sinceLastTry, 50L) * 0.08696D * 1000000.0D);
/*  259 */       this.lastWriteTry = time;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  264 */       for (int i = 0; i < tilesToUpdate * 2L && System.nanoTime() - before < timeLimit; i += 2) {
/*  265 */         if (writeChunk(playerX, playerY, playerZ, world)) {
/*  266 */           i--;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     }
/*  287 */     catch (Throwable e) {
/*  288 */       MinimapProcessor.instance.setCrashedWith(e);
/*      */     } 
/*  290 */     updateTimeDebug(before);
/*      */   }
/*      */   
/*      */   private boolean writeChunk(double playerX, double playerY, double playerZ, World world) {
/*  294 */     long processStart = System.nanoTime();
/*  295 */     if (this.tileInsideX == 0 && this.tileInsideZ == 0) {
/*  296 */       if (this.updateChunkX == 0 && this.updateChunkZ == 0) {
/*  297 */         this.settingsChanged = false;
/*  298 */         if (this.clearBlockColours) {
/*  299 */           this.settingsChanged = true;
/*  300 */           this.clearBlockColours = false;
/*  301 */           if (!this.blockColours.isEmpty()) {
/*  302 */             this.blockColours.clear();
/*  303 */             this.textureColours.clear();
/*  304 */             System.out.println("Minimap block colour cache cleaned.");
/*      */           } 
/*      */         } 
/*  307 */         this.loadingSideInChunks = getLoadSide();
/*  308 */         this.updateRadius = getUpdateRadiusInChunks();
/*  309 */         this.loadingMapChunkX = getMapCoord(this.loadingSideInChunks, playerX);
/*  310 */         this.loadingMapChunkZ = getMapCoord(this.loadingSideInChunks, playerZ);
/*  311 */         this.loadingCaving = getCaving(playerX, playerY, playerZ, world);
/*  312 */         if (this.loadingCaving != this.loadedCaving)
/*  313 */           this.runNumber = 0; 
/*  314 */         this.loadingLevels = this.modMain.getSettings().getLighting() ? 5 : 1;
/*  315 */         this.loadingTerrainSlopes = this.modMain.getSettings().getTerrainSlopes();
/*  316 */         this.loadingTerrainSlopesExperiment = this.modMain.getSettings().getTerrainSlopesExperiment();
/*  317 */         this.loadingTerrainDepth = this.modMain.getSettings().getTerrainDepth();
/*  318 */         this.loadingRedstone = this.modMain.getSettings().getDisplayRedstone();
/*  319 */         this.loadingColours = this.modMain.getSettings().getBlockColours();
/*  320 */         this.loadingTransparency = (this.modMain.getSettings()).blockTransparency;
/*  321 */         this.loadingBiomesVanillaMode = this.modMain.getSettings().getBiomeColorsVanillaMode();
/*  322 */         this.loadingDimension = world.field_73011_w.getDimension();
/*  323 */         this.loadingCaveMapsDepth = (this.modMain.getSettings()).caveMapsDepth;
/*      */         
/*  325 */         this.settingsChanged = (this.settingsChanged || this.loadedDimension != this.loadingDimension);
/*  326 */         this.settingsChanged = (this.settingsChanged || this.loadedTerrainSlopes != this.loadingTerrainSlopes);
/*  327 */         this.settingsChanged = (this.settingsChanged || this.loadedTerrainSlopesExperiment != this.loadingTerrainSlopesExperiment);
/*  328 */         this.settingsChanged = (this.settingsChanged || this.loadedTerrainDepth != this.loadingTerrainDepth);
/*  329 */         this.settingsChanged = (this.settingsChanged || this.loadedRedstone != this.loadingRedstone);
/*  330 */         this.settingsChanged = (this.settingsChanged || this.loadedColours != this.loadingColours);
/*  331 */         this.settingsChanged = (this.settingsChanged || this.loadedTransparency != this.loadingTransparency);
/*  332 */         this.settingsChanged = (this.settingsChanged || this.loadingBiomesVanillaMode != this.loadedBiomesVanillaMode);
/*  333 */         this.settingsChanged = (this.settingsChanged || this.loadingCaveMapsDepth != this.loadedCaveMapsDepth);
/*  334 */         if (this.loadingBlocks == null || this.loadingBlocks.length != this.loadingSideInChunks)
/*  335 */           this.loadingBlocks = new MinimapChunk[this.loadingSideInChunks][this.loadingSideInChunks]; 
/*  336 */         if (MinimapProcessor.instance.usingFBO() && MinimapProcessor.instance.isToResetImage()) {
/*  337 */           this.forcedRefresh = true;
/*  338 */           MinimapProcessor.instance.setToResetImage(false);
/*      */         } 
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  345 */       this.oldChunk = null;
/*  346 */       if (this.loadedBlocks != null) {
/*  347 */         int updateChunkXOld = this.loadingMapChunkX + this.updateChunkX - this.loadedMapChunkX;
/*  348 */         int updateChunkZOld = this.loadingMapChunkZ + this.updateChunkZ - this.loadedMapChunkZ;
/*  349 */         if (updateChunkXOld > -1 && updateChunkXOld < this.loadedBlocks.length && updateChunkZOld > -1 && updateChunkZOld < this.loadedBlocks.length)
/*  350 */           this.oldChunk = this.loadedBlocks[updateChunkXOld][updateChunkZOld]; 
/*      */       } 
/*      */     } 
/*  353 */     MinimapChunk mchunk = this.loadingBlocks[this.updateChunkX][this.updateChunkZ];
/*  354 */     if (mchunk == null) {
/*  355 */       mchunk = this.loadingBlocks[this.updateChunkX][this.updateChunkZ] = new MinimapChunk(this.loadingMapChunkX + this.updateChunkX, this.loadingMapChunkZ + this.updateChunkZ);
/*  356 */     } else if (this.tileInsideX == 0 && this.tileInsideZ == 0) {
/*  357 */       mchunk.reset(this.loadingMapChunkX + this.updateChunkX, this.loadingMapChunkZ + this.updateChunkZ);
/*      */     } 
/*      */     
/*  360 */     boolean onlyLoad = (this.runNumber % 5 != 0);
/*  361 */     writeTile(playerX, playerY, playerZ, world, mchunk, this.oldChunk, this.updateChunkX, this.updateChunkZ, this.tileInsideX, this.tileInsideZ, onlyLoad);
/*      */     
/*  363 */     this.tileInsideZ++;
/*  364 */     if (this.tileInsideZ >= 4) {
/*  365 */       this.tileInsideZ = 0;
/*  366 */       this.tileInsideX++;
/*  367 */       if (this.tileInsideX >= 4) {
/*  368 */         this.tileInsideX = 0;
/*  369 */         mchunk = this.loadingBlocks[this.updateChunkX][this.updateChunkZ];
/*  370 */         if (MinimapProcessor.instance.usingFBO() && mchunk.isHasSomething() && mchunk.isChanged()) {
/*  371 */           mchunk.updateBuffers(this.loadingLevels, this.bufferUpdateArrayBuffers);
/*      */ 
/*      */           
/*  374 */           mchunk.setChanged(false);
/*      */         } 
/*      */         
/*  377 */         mchunk.setLevelsBuffered(this.loadingLevels);
/*  378 */         if (this.updateChunkX == this.loadingSideInChunks - 1 && this.updateChunkZ == this.loadingSideInChunks - 1) {
/*      */           
/*  380 */           if (this.runNumber % 5 == 0 && !MinimapTile.recycled.isEmpty())
/*      */           {
/*  382 */             MinimapTile.recycled.subList(0, MinimapTile.recycled.size() / 2).clear();
/*      */           }
/*      */ 
/*      */           
/*  386 */           if (this.loadedBlocks != null) {
/*  387 */             for (int i = 0; i < this.loadedBlocks.length; i++) {
/*  388 */               for (int j = 0; j < this.loadedBlocks.length; j++) {
/*  389 */                 MinimapChunk m = this.loadedBlocks[i][j];
/*  390 */                 MinimapChunk lm = null;
/*  391 */                 if (m != null) {
/*  392 */                   m.recycleTiles();
/*  393 */                   int loadingX = this.loadedMapChunkX + i - this.loadingMapChunkX;
/*  394 */                   int loadingZ = this.loadedMapChunkZ + j - this.loadingMapChunkZ;
/*  395 */                   if (loadingX > -1 && loadingZ > -1 && loadingX < this.loadingSideInChunks && loadingZ < this.loadingSideInChunks)
/*  396 */                     lm = this.loadingBlocks[loadingX][loadingZ]; 
/*  397 */                   boolean shouldTransfer = (m.getLevelsBuffered() == this.loadingLevels && lm != null);
/*  398 */                   if (shouldTransfer)
/*  399 */                     synchronized (m) {
/*  400 */                       m.setBlockTextureUpload(true);
/*      */                     }  
/*  402 */                   for (int l = 0; l < m.getLevelsBuffered(); l++) {
/*  403 */                     if (m.getGlTexture(l) != 0) {
/*  404 */                       if (shouldTransfer)
/*  405 */                       { lm.setGlTexture(l, m.getGlTexture(l)); }
/*      */                       else
/*  407 */                       { MinimapProcessor.instance.requestTextureDelete(m.getGlTexture(l)); } 
/*  408 */                     } else if (shouldTransfer && !lm.isRefreshRequired(l) && m.isRefreshRequired(l)) {
/*  409 */                       lm.copyBuffer(l, m.getBuffer(l));
/*      */                       
/*  411 */                       lm.setRefreshRequired(l, true);
/*  412 */                       m.setRefreshRequired(l, false);
/*      */                     } 
/*      */                   } 
/*      */                 } 
/*      */               } 
/*      */             } 
/*      */           }
/*      */ 
/*      */           
/*  421 */           synchronized (this) {
/*  422 */             MinimapChunk[][] bu = this.loadedBlocks;
/*  423 */             this.loadedBlocks = this.loadingBlocks;
/*  424 */             this.loadingBlocks = bu;
/*  425 */             this.loadedMapChunkX = this.loadingMapChunkX;
/*  426 */             this.loadedMapChunkZ = this.loadingMapChunkZ;
/*  427 */             this.loadedLevels = this.loadingLevels;
/*  428 */             this.loadedTerrainSlopes = this.loadingTerrainSlopes;
/*  429 */             this.loadedTerrainSlopesExperiment = this.loadingTerrainSlopesExperiment;
/*  430 */             this.loadedTerrainDepth = this.loadingTerrainDepth;
/*  431 */             this.loadedRedstone = this.loadingRedstone;
/*  432 */             this.loadedColours = this.loadingColours;
/*  433 */             this.loadedTransparency = this.loadingTransparency;
/*  434 */             this.loadedBiomesVanillaMode = this.loadingBiomesVanillaMode;
/*  435 */             this.loadedDimension = this.loadingDimension;
/*  436 */             this.loadedCaveMapsDepth = this.loadingCaveMapsDepth;
/*      */           } 
/*  438 */           this.loadedCaving = this.loadingCaving;
/*  439 */           this.forcedRefresh = false;
/*  440 */           this.runNumber++;
/*      */         } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  450 */         this.updateChunkZ++;
/*  451 */         if (this.updateChunkZ >= this.loadingSideInChunks) {
/*  452 */           this.updateChunkZ = 0;
/*  453 */           this.updateChunkX = (this.updateChunkX + 1) % this.loadingSideInChunks;
/*  454 */           this.lastBlockY = new int[4][16];
/*  455 */           this.lastSlopeShades = new int[4][16];
/*  456 */           for (int i = 0; i < 4; i++)
/*  457 */             this.topMostChunk[i] = true; 
/*      */         } 
/*      */       } 
/*      */     } 
/*  461 */     int passed = (int)(System.nanoTime() - processStart);
/*      */ 
/*      */     
/*  464 */     return onlyLoad;
/*      */   }
/*      */   
/*      */   private void writeTile(double playerX, double playerY, double playerZ, World world, MinimapChunk mchunk, MinimapChunk oldChunk, int canvasX, int canvasZ, int insideX, int insideZ, boolean onlyLoad) {
/*  468 */     int tileX = mchunk.getX() * 4 + insideX;
/*  469 */     int tileZ = mchunk.getZ() * 4 + insideZ;
/*  470 */     int halfSide = this.loadingSideInChunks / 2;
/*  471 */     int tileFromCenterX = canvasX - halfSide;
/*  472 */     int tileFromCenterZ = canvasZ - halfSide;
/*  473 */     MinimapTile oldTile = null;
/*  474 */     if (oldChunk != null)
/*  475 */       oldTile = oldChunk.getTile(insideX, insideZ); 
/*  476 */     Chunk bchunk = world.func_72964_e(tileX, tileZ);
/*      */ 
/*      */     
/*  479 */     boolean neighborsLoaded = true;
/*  480 */     for (int i = -1; i < 2; i++) {
/*  481 */       for (int j = -1; j < 2; j++) {
/*  482 */         if (i != 0 || j != 0) {
/*  483 */           Chunk nchunk = world.func_72964_e(tileX + i, tileZ + j);
/*  484 */           if (nchunk == null || !nchunk.func_177410_o()) {
/*  485 */             neighborsLoaded = false; break;
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*  490 */     boolean chunkUpdated = false;
/*      */     try {
/*  492 */       chunkUpdated = ((bchunk != null && !((Boolean)XaeroMinimapCore.chunkCleanField.get(bchunk)).booleanValue()) || oldTile == null || oldTile.caveLevel != this.loadingCaving);
/*  493 */     } catch (IllegalArgumentException|IllegalAccessException e) {
/*  494 */       throw new RuntimeException(e);
/*      */     } 
/*  496 */     if (bchunk == null || bchunk instanceof net.minecraft.world.chunk.EmptyChunk || !neighborsLoaded || ((!chunkUpdated || onlyLoad || tileFromCenterX > this.updateRadius || tileFromCenterZ > this.updateRadius || tileFromCenterX < -this.updateRadius || tileFromCenterZ < -this.updateRadius) && oldTile != null && oldTile
/*  497 */       .isSuccess() && oldChunk.getLevelsBuffered() == this.loadingLevels && !this.settingsChanged)) {
/*  498 */       if (oldTile != null && oldChunk.getLevelsBuffered() == this.loadingLevels && !this.settingsChanged) {
/*  499 */         mchunk.setTile(insideX, insideZ, oldTile);
/*  500 */         oldTile.setWasTransfered(true);
/*  501 */         for (int j = 0; j < 16; j++) {
/*  502 */           this.lastBlockY[insideX][j] = oldTile.getLastHeight(j);
/*  503 */           this.lastSlopeShades[insideX][j] = oldTile.getLastSlopeShade(j);
/*      */         } 
/*  505 */         mchunk.setHasSomething(oldChunk.isHasSomething());
/*  506 */         this.topMostChunk[insideX] = false;
/*  507 */         if (this.forcedRefresh)
/*  508 */           mchunk.setChanged(true); 
/*      */       } else {
/*  510 */         for (int j = 0; j < 16; j++) {
/*  511 */           this.lastBlockY[insideX][j] = 0;
/*  512 */           this.lastSlopeShades[insideX][j] = 0;
/*      */         } 
/*      */       } 
/*      */       
/*      */       return;
/*      */     } 
/*      */     
/*  519 */     if (oldTile != null && oldChunk.getLevelsBuffered() != this.loadingLevels) {
/*  520 */       oldTile = null;
/*      */     }
/*  522 */     int x1 = tileX * 16;
/*  523 */     int z1 = tileZ * 16;
/*  524 */     MinimapTile tile = null;
/*  525 */     for (int blockX = x1; blockX < x1 + 16; blockX++) {
/*  526 */       for (int blockZ = z1; blockZ < z1 + 16; blockZ++) {
/*  527 */         tile = loadBlockColor(playerX, playerY, playerZ, world, blockX, blockZ, bchunk, canvasX, canvasZ, tileX, tileZ, insideX, insideZ, oldTile, mchunk);
/*  528 */         if ((blockZ & 0xF) == 15 && 
/*  529 */           tile != null) {
/*  530 */           tile.setLastHeight(blockX & 0xF, this.lastBlockY[insideX][blockX & 0xF]);
/*  531 */           tile.setLastSlopeShade(blockX & 0xF, this.lastSlopeShades[insideX][blockX & 0xF]);
/*      */         } 
/*      */       } 
/*      */     } 
/*  535 */     tile.caveLevel = this.loadingCaving;
/*  536 */     this.topMostChunk[insideX] = false;
/*      */     try {
/*  538 */       XaeroMinimapCore.chunkCleanField.set(bchunk, Boolean.valueOf(true));
/*  539 */     } catch (IllegalArgumentException|IllegalAccessException e) {
/*  540 */       throw new RuntimeException(e);
/*      */     } 
/*      */   }
/*      */   
/*      */   private int getShadeValue(int y, int prevY) {
/*  545 */     if (y < prevY) {
/*  546 */       if (this.loadingTerrainSlopesExperiment || this.loadingCaving != -1) {
/*  547 */         return 3 + Math.min(4, prevY - y);
/*      */       }
/*  549 */       return 2;
/*  550 */     }  if (y > prevY)
/*  551 */       return 3; 
/*  552 */     return 1;
/*      */   }
/*      */ 
/*      */   
/*      */   public MinimapTile loadBlockColor(double playerX, double playerY, double playerZ, World world, int par1, int par2, Chunk bchunk, int canvasX, int canvasZ, int tileX, int tileZ, int tileInsideX, int tileInsideZ, MinimapTile oldTile, MinimapChunk mchunk) {
/*  557 */     int insideX = par1 & 0xF;
/*  558 */     int insideZ = par2 & 0xF;
/*  559 */     int playerYi = (int)playerY;
/*  560 */     int height = bchunk.func_76611_b(insideX, insideZ);
/*  561 */     if (height == -1)
/*  562 */       height = 255; 
/*  563 */     int highY = (this.loadingCaving != -1) ? this.loadingCaving : (height + 3);
/*  564 */     int lowY = (this.loadingCaving != -1) ? (playerYi - this.loadingCaveMapsDepth) : 0;
/*  565 */     if (lowY < 0)
/*  566 */       lowY = 0; 
/*  567 */     this.pixelTransparentSizes.clear();
/*  568 */     this.pixelBlockStates.clear();
/*  569 */     this.pixelBlockLights.clear();
/*  570 */     this.currentComparisonCode = 0L;
/*  571 */     byte currentComparisonCodeAdd = 0;
/*  572 */     byte currentComparisonCodeAdd2 = 0;
/*  573 */     this.blockY = 0;
/*  574 */     for (int i = 0; i < this.loadingLevels; i++) {
/*  575 */       this.underRed[i] = 0;
/*  576 */       this.underGreen[i] = 0;
/*  577 */       this.underBlue[i] = 0;
/*      */     } 
/*      */     
/*  580 */     this.currentTransparencyMultiplier = 1.0F;
/*  581 */     this.sun = 15;
/*      */     
/*  583 */     this.blockColor = 0;
/*  584 */     this.isglowing = false;
/*  585 */     this.secondaryB = 1.0D;
/*  586 */     Block block = findBlock(world, bchunk, insideX, insideZ, highY, lowY);
/*  587 */     boolean success = true;
/*  588 */     if (this.topMostChunk[tileInsideX] && insideZ == 0) {
/*  589 */       if (this.pixelTransparentSizes.isEmpty()) {
/*  590 */         this.lastBlockY[tileInsideX][insideX] = this.blockY;
/*      */         try {
/*  592 */           Chunk prevChunk = world.func_72964_e(tileX, tileZ - 1);
/*  593 */           if (prevChunk != null && prevChunk.func_177410_o()) {
/*  594 */             this.lastBlockY[tileInsideX][insideX] = prevChunk.func_76611_b(insideX, 15) - 1;
/*  595 */             int prevPrevY = prevChunk.func_76611_b(insideX, 14) - 1;
/*  596 */             this.lastSlopeShades[tileInsideX][insideX] = getShadeValue(this.lastBlockY[tileInsideX][insideX], prevPrevY);
/*      */           }
/*      */         
/*      */         }
/*  600 */         catch (IllegalStateException illegalStateException) {}
/*      */       } 
/*      */ 
/*      */       
/*  604 */       success = false;
/*      */     } 
/*  606 */     int slopeShade = 1;
/*  607 */     if (this.loadingTerrainSlopes) {
/*  608 */       if (this.lastSlopeShades[tileInsideX][insideX] != 0)
/*  609 */         slopeShade = getShadeValue(this.blockY, this.lastBlockY[tileInsideX][insideX]); 
/*  610 */       this.lastSlopeShades[tileInsideX][insideX] = slopeShade;
/*      */     } 
/*  612 */     this.currentComparisonCode |= (slopeShade << 18);
/*  613 */     for (int j = 0; j < this.pixelBlockLights.size(); j++) {
/*  614 */       int l = ((Integer)this.pixelBlockLights.get(j)).intValue();
/*  615 */       if (j <= 1)
/*  616 */         currentComparisonCodeAdd = (byte)(currentComparisonCodeAdd | l << 4 * j + 2); 
/*  617 */       if (j >= 1)
/*  618 */         this.currentComparisonCode |= (l << 4 * (j - 1) >> 2); 
/*      */     } 
/*  620 */     int add2Calculation = 17;
/*  621 */     for (int k = 0; k < this.pixelTransparentSizes.size(); k++) {
/*  622 */       add2Calculation = add2Calculation * 37 + ((Integer)this.pixelTransparentSizes.get(k)).intValue();
/*      */     }
/*  624 */     currentComparisonCodeAdd = (byte)(currentComparisonCodeAdd | add2Calculation >> 8 & 0x3);
/*  625 */     currentComparisonCodeAdd2 = (byte)add2Calculation;
/*  626 */     boolean reuseColour = (!this.settingsChanged && oldTile != null && this.loadingCaving == oldTile.caveLevel && !oldTile.pixelChanged(insideX, insideZ, this.currentComparisonCode, currentComparisonCodeAdd, currentComparisonCodeAdd2));
/*  627 */     if (!reuseColour) {
/*  628 */       calculateBlockColors(world, bchunk, insideX, insideZ);
/*  629 */       this.isglowing = (block != null && !(block instanceof net.minecraft.block.BlockOre) && this.isglowing);
/*      */       
/*  631 */       if (!this.isglowing) {
/*  632 */         boolean lighting = (this.loadingLevels != 1);
/*  633 */         boolean hasTransparentLayer = !this.pixelTransparentSizes.isEmpty();
/*  634 */         for (int i1 = 0; i1 < this.loadingLevels; i1++) {
/*  635 */           this.postBrightness[i1] = 1.0F;
/*  636 */           if (!lighting && this.loadingCaving != -1) {
/*  637 */             if (!hasTransparentLayer) {
/*  638 */               this.brightness[i1] = (float)Math.min(this.blockY / height, 1.0D);
/*      */             } else {
/*  640 */               this.brightness[i1] = 1.0F;
/*  641 */               this.postBrightness[i1] = (float)Math.min(this.blockY / height, 1.0D);
/*      */             } 
/*      */           } else {
/*  644 */             this.brightness[i1] = getBlockBrightness(5.0F, this.sun, i1, this.pixelBlockLights.isEmpty() ? 0 : ((Integer)this.pixelBlockLights.get(this.pixelBlockLights.size() - 1)).intValue());
/*      */           } 
/*  646 */         }  if (this.loadingCaving == -1 && this.loadingTerrainDepth) {
/*  647 */           this.secondaryB = this.blockY / 63.0D;
/*  648 */           if (this.secondaryB > 1.15D) {
/*  649 */             this.secondaryB = 1.15D;
/*  650 */           } else if (this.secondaryB < 0.7D) {
/*  651 */             this.secondaryB = 0.7D;
/*      */           } 
/*  653 */         }  if (this.loadingTerrainSlopes) {
/*      */           float shadeMultiplier;
/*  655 */           if (slopeShade <= 3) {
/*  656 */             shadeMultiplier = (slopeShade == 2) ? 0.85F : ((slopeShade == 3) ? 1.15F : 1.0F);
/*      */           } else {
/*  658 */             shadeMultiplier = 1.0F + (3 - slopeShade) * 0.1F;
/*  659 */           }  this.secondaryB *= shadeMultiplier;
/*      */         } 
/*      */       } 
/*  662 */       if (this.isglowing) {
/*  663 */         this.helper.getBrightestColour(this.blockColor >> 16 & 0xFF, this.blockColor >> 8 & 0xFF, this.blockColor & 0xFF, this.tempColor);
/*      */       }
/*  665 */       for (int n = 0; n < this.loadingLevels; n++) {
/*  666 */         float b; if (this.isglowing) {
/*  667 */           this.red[n] = this.tempColor[0];
/*  668 */           this.green[n] = this.tempColor[1];
/*  669 */           this.blue[n] = this.tempColor[2];
/*  670 */           b = this.currentTransparencyMultiplier;
/*      */         } else {
/*  672 */           this.red[n] = this.blockColor >> 16 & 0xFF;
/*  673 */           this.green[n] = this.blockColor >> 8 & 0xFF;
/*  674 */           this.blue[n] = this.blockColor & 0xFF;
/*  675 */           b = this.brightness[n] * this.currentTransparencyMultiplier;
/*      */         } 
/*  677 */         this.red[n] = (int)(((this.red[n] * b) * this.secondaryB + this.underRed[n]) * this.postBrightness[n]);
/*  678 */         if (this.red[n] > 255)
/*  679 */           this.red[n] = 255; 
/*  680 */         this.green[n] = (int)(((this.green[n] * b) * this.secondaryB + this.underGreen[n]) * this.postBrightness[n]);
/*  681 */         if (this.green[n] > 255)
/*  682 */           this.green[n] = 255; 
/*  683 */         this.blue[n] = (int)(((this.blue[n] * b) * this.secondaryB + this.underBlue[n]) * this.postBrightness[n]);
/*  684 */         if (this.blue[n] > 255) {
/*  685 */           this.blue[n] = 255;
/*      */         }
/*      */       } 
/*      */     } else {
/*  689 */       for (int n = 0; n < this.loadingLevels; n++) {
/*  690 */         this.red[n] = oldTile.getRed(n, insideX, insideZ);
/*  691 */         this.green[n] = oldTile.getGreen(n, insideX, insideZ);
/*  692 */         this.blue[n] = oldTile.getBlue(n, insideX, insideZ);
/*      */       } 
/*      */     } 
/*  695 */     this.lastBlockY[tileInsideX][insideX] = this.blockY;
/*  696 */     if (notEmptyColor())
/*  697 */       mchunk.setHasSomething(true); 
/*  698 */     MinimapTile tile = mchunk.getTile(tileInsideX, tileInsideZ);
/*  699 */     if (tile == null) {
/*  700 */       tile = MinimapTile.getANewTile(this.modMain.getSettings(), tileX, tileZ, this.seedForLoading);
/*  701 */       mchunk.setTile(tileInsideX, tileInsideZ, tile);
/*      */     } 
/*  703 */     tile.setCode(insideX, insideZ, this.currentComparisonCode, currentComparisonCodeAdd, currentComparisonCodeAdd2);
/*      */     
/*  705 */     if (tile.isSuccess())
/*  706 */       tile.setSuccess(success); 
/*  707 */     if (oldTile != null) {
/*  708 */       int oldTileDarkestLevel = this.loadedLevels - 1;
/*  709 */       int tileDarkestLevel = this.loadingLevels - 1;
/*  710 */       if (oldTile.getRed(oldTileDarkestLevel, insideX, insideZ) != this.red[tileDarkestLevel] || oldTile.getGreen(oldTileDarkestLevel, insideX, insideZ) != this.green[tileDarkestLevel] || oldTile.getBlue(oldTileDarkestLevel, insideX, insideZ) != this.blue[tileDarkestLevel])
/*  711 */         mchunk.setChanged(true); 
/*      */     } else {
/*  713 */       mchunk.setChanged(true);
/*  714 */     }  for (int m = 0; m < this.loadingLevels; m++) {
/*  715 */       tile.setRed(m, insideX, insideZ, this.red[m]);
/*  716 */       tile.setGreen(m, insideX, insideZ, this.green[m]);
/*  717 */       tile.setBlue(m, insideX, insideZ, this.blue[m]);
/*      */     } 
/*  719 */     return tile;
/*      */   }
/*      */   
/*      */   public Block findBlock(World world, Chunk bchunk, int insideX, int insideZ, int highY, int lowY) {
/*  723 */     boolean underair = false;
/*  724 */     IBlockState previousTransparentState = null;
/*  725 */     for (int i = highY; i >= lowY; i--) {
/*  726 */       IBlockState state = bchunk.func_186032_a(insideX, i, insideZ);
/*  727 */       if (state != null) {
/*      */         
/*  729 */         Block got = state.func_177230_c();
/*  730 */         if (!(got instanceof net.minecraft.block.BlockAir) && (underair || this.loadingCaving == -1))
/*  731 */         { boolean isRedstone = false;
/*  732 */           if (state.func_185901_i() != EnumBlockRenderType.INVISIBLE)
/*      */           {
/*  734 */             if (got != Blocks.field_150478_aa)
/*      */             {
/*  736 */               if (got != Blocks.field_150329_H)
/*      */               {
/*  738 */                 if (got != Blocks.field_150398_cm)
/*      */                 {
/*  740 */                   if (this.modMain.getSettings().getShowFlowers() || !(got instanceof net.minecraft.block.BlockBush))
/*      */                   
/*      */                   { 
/*  743 */                     isRedstone = (got == Blocks.field_150429_aA || got == Blocks.field_150488_af || got instanceof net.minecraft.block.BlockRedstoneRepeater || got instanceof net.minecraft.block.BlockRedstoneComparator);
/*  744 */                     if (this.loadingRedstone || !isRedstone)
/*      */                     
/*      */                     { 
/*  747 */                       this.blockY = i;
/*  748 */                       BlockPos.MutableBlockPos mutableBlockPos1 = this.mutableBlockPos.func_181079_c(insideX, Math.min(255, this.blockY + 1), insideZ);
/*  749 */                       BlockPos.MutableBlockPos mutableBlockPos2 = this.mutableBlockPos2.func_181079_c(bchunk.field_76635_g * 16 + insideX, this.blockY, bchunk.field_76647_h * 16 + insideZ);
/*  750 */                       MapColor mapColor = state.func_185909_g((IBlockAccess)world, (BlockPos)mutableBlockPos2);
/*  751 */                       if ((isRedstone && this.loadingColours != 1) || (mapColor != null && mapColor.field_76291_p != 0))
/*      */                       
/*  753 */                       { if (this.currentComparisonCode == 0L) {
/*  754 */                           this.firstBlockY = this.blockY;
/*  755 */                           if (this.loadingLevels != 1)
/*  756 */                             this.sun = bchunk.func_177413_a(EnumSkyBlock.SKY, (BlockPos)mutableBlockPos1); 
/*      */                         } 
/*  758 */                         int stateId = Block.func_176210_f(state);
/*  759 */                         if (this.loadingTransparency && (
/*  760 */                           state == previousTransparentState || isTransparent(state, got, bchunk, (BlockPos)mutableBlockPos2)))
/*  761 */                         { if (this.pixelBlockStates.size() < 5 && state != previousTransparentState) {
/*  762 */                             this.currentComparisonCode += stateId & 0xFFFFFFFFL;
/*  763 */                             this.pixelBlockStates.add(state);
/*  764 */                             this.pixelTransparentSizes.add(Integer.valueOf(1));
/*  765 */                             this.pixelBlockLights.add(Integer.valueOf((this.loadingLevels == 1) ? 0 : bchunk.func_177413_a(EnumSkyBlock.BLOCK, (BlockPos)mutableBlockPos1)));
/*  766 */                             previousTransparentState = state;
/*      */                           } else {
/*  768 */                             this.pixelTransparentSizes.set(this.pixelTransparentSizes.size() - 1, Integer.valueOf(((Integer)this.pixelTransparentSizes.get(this.pixelTransparentSizes.size() - 1)).intValue() + 1));
/*      */                           }  }
/*      */                         else
/*      */                         
/*  772 */                         { this.currentComparisonCode += stateId & 0xFFFFFFFFL;
/*  773 */                           this.currentComparisonCode <<= 29L;
/*  774 */                           this.currentComparisonCode |= this.blockY << 21L;
/*  775 */                           this.pixelBlockLights.add(Integer.valueOf((this.loadingLevels == 1) ? 0 : bchunk.func_177413_a(EnumSkyBlock.BLOCK, (BlockPos)mutableBlockPos1)));
/*  776 */                           this.pixelBlockStates.add(state);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */                           
/*  785 */                           this.isglowing = isGlowing(state, bchunk.func_177412_p(), (BlockPos)mutableBlockPos2);
/*  786 */                           return got; }  }  }  }  }  }  }  }  }
/*  787 */         else if (got instanceof net.minecraft.block.BlockAir)
/*  788 */         { underair = true; } 
/*      */       } 
/*  790 */     }  return null;
/*      */   }
/*      */   
/*      */   private void calculateBlockColors(World world, Chunk bchunk, int insideX, int insideZ) {
/*  794 */     BlockPos.MutableBlockPos globalPos = this.mutableBlockPos2.func_181079_c(bchunk.field_76635_g * 16 + insideX, this.firstBlockY, bchunk.field_76647_h * 16 + insideZ);
/*  795 */     if (!this.pixelTransparentSizes.isEmpty()) {
/*  796 */       BlockPos.MutableBlockPos lightPos = this.mutableBlockPos.func_181079_c(insideX, this.firstBlockY + 1, insideZ);
/*      */ 
/*      */ 
/*      */       
/*  800 */       for (int i = 0; i < this.pixelTransparentSizes.size(); i++) {
/*  801 */         IBlockState state = this.pixelBlockStates.get(i);
/*  802 */         Block b = state.func_177230_c();
/*  803 */         int size = ((Integer)this.pixelTransparentSizes.get(i)).intValue();
/*  804 */         int opacity = b.getLightOpacity(state, (IBlockAccess)bchunk.func_177412_p(), (BlockPos)globalPos);
/*  805 */         applyTransparentLayer(world, bchunk, b, state, opacity * size, (BlockPos)globalPos, ((Integer)this.pixelBlockLights.get(i)).intValue());
/*  806 */         int nextY = globalPos.func_177956_o() - size;
/*  807 */         globalPos.func_185336_p(nextY);
/*  808 */         lightPos.func_185336_p(nextY + 1);
/*      */       } 
/*      */     } 
/*  811 */     if (!this.pixelBlockStates.isEmpty()) {
/*  812 */       IBlockState state = this.pixelBlockStates.get(this.pixelBlockStates.size() - 1);
/*  813 */       Block b = state.func_177230_c();
/*  814 */       if (this.loadingColours == 1) {
/*  815 */         MapColor minimapColor = state.func_185909_g((IBlockAccess)world, (BlockPos)globalPos);
/*  816 */         this.blockColor = minimapColor.field_76291_p;
/*      */       } else {
/*  818 */         this.blockColor = loadBlockColourFromTexture(world, state, b, (BlockPos)globalPos, true);
/*  819 */       }  this.blockColor = addBlockColorMultipliers(this.blockColor, state, world, (BlockPos)globalPos);
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean isGlowing(IBlockState state, World world, BlockPos pos) {
/*  824 */     Boolean cachedValue = this.glowingCache.get(state);
/*  825 */     if (cachedValue != null)
/*  826 */       return cachedValue.booleanValue(); 
/*  827 */     boolean isGlowing = false;
/*      */     try {
/*  829 */       isGlowing = (state.getLightValue((IBlockAccess)world, pos) >= 0.5D);
/*  830 */     } catch (Exception exception) {}
/*      */     
/*  832 */     this.glowingCache.put(state, Boolean.valueOf(isGlowing));
/*  833 */     return isGlowing;
/*      */   }
/*      */   
/*      */   private boolean isTransparent(IBlockState state, Block b, Chunk bchunk, BlockPos globalPos) {
/*  837 */     Boolean cachedValue = this.transparentCache.get(b);
/*  838 */     if (cachedValue != null)
/*  839 */       return cachedValue.booleanValue(); 
/*  840 */     int lightOpacity = b.getLightOpacity(state, (IBlockAccess)bchunk.func_177412_p(), globalPos);
/*  841 */     boolean transparent = ((b instanceof net.minecraft.block.BlockLiquid && lightOpacity != 255 && lightOpacity != 0) || b.func_180664_k() == BlockRenderLayer.TRANSLUCENT || b instanceof net.minecraft.block.BlockGlass);
/*  842 */     this.transparentCache.put(b, Boolean.valueOf(transparent));
/*  843 */     return transparent;
/*      */   }
/*      */   
/*      */   private void applyTransparentLayer(World world, Chunk bchunk, Block b, IBlockState state, int opacity, BlockPos globalPos, int blockLight) {
/*  847 */     int red = 0;
/*  848 */     int green = 0;
/*  849 */     int blue = 0;
/*  850 */     float transparency = (b instanceof net.minecraft.block.BlockLiquid) ? 0.66F : ((b instanceof net.minecraft.block.BlockIce) ? 0.83F : 0.5F);
/*      */     
/*  852 */     if (this.loadingColours == 0) {
/*  853 */       color = loadBlockColourFromTexture(world, state, b, globalPos, true);
/*      */     }
/*  855 */     else if (b instanceof net.minecraft.block.BlockLiquid) {
/*  856 */       color = -16751391;
/*      */     } else {
/*  858 */       color = (state.func_185909_g((IBlockAccess)world, globalPos)).field_76291_p;
/*      */     } 
/*  860 */     int color = addBlockColorMultipliers(color, state, world, globalPos);
/*  861 */     red = color >> 16 & 0xFF;
/*  862 */     green = color >> 8 & 0xFF;
/*  863 */     blue = color & 0xFF;
/*  864 */     if (isGlowing(state, bchunk.func_177412_p(), globalPos)) {
/*  865 */       this.helper.getBrightestColour(red, green, blue, this.tempColor);
/*  866 */       red = this.tempColor[0];
/*  867 */       green = this.tempColor[1];
/*  868 */       blue = this.tempColor[2];
/*      */     } 
/*  870 */     for (int i = 0; i < this.loadingLevels; i++) {
/*  871 */       float overlayIntensity = this.currentTransparencyMultiplier * transparency * getBlockBrightness(5.0F, this.sun, i, blockLight);
/*      */       
/*  873 */       this.underRed[i] = (int)(this.underRed[i] + red * overlayIntensity);
/*  874 */       this.underGreen[i] = (int)(this.underGreen[i] + green * overlayIntensity);
/*  875 */       this.underBlue[i] = (int)(this.underBlue[i] + blue * overlayIntensity);
/*      */     } 
/*  877 */     this.currentTransparencyMultiplier *= 1.0F - transparency;
/*  878 */     this.sun -= opacity;
/*  879 */     if (this.sun < 0) {
/*  880 */       this.sun = 0;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private int loadBlockColourFromTexture(World world, IBlockState state, Block b, BlockPos pos, boolean convert) {
/*  886 */     int stateHash = Block.func_176210_f(state);
/*  887 */     Integer c = this.blockColours.get(Integer.valueOf(stateHash));
/*  888 */     int red = 0;
/*  889 */     int green = 0;
/*  890 */     int blue = 0;
/*  891 */     if (c == null) {
/*      */ 
/*      */ 
/*      */       
/*  895 */       String name = null; try {
/*      */         TextureAtlasSprite texture;
/*  897 */         List<BakedQuad> upQuads = null;
/*  898 */         BlockModelShapes bms = Minecraft.func_71410_x().func_175602_ab().func_175023_a();
/*  899 */         IBakedModel model = bms.func_178125_b(state);
/*  900 */         if (convert) {
/*  901 */           upQuads = model.func_188616_a(state, EnumFacing.UP, 0L);
/*      */         }
/*  903 */         if (upQuads == null || upQuads.isEmpty() || ((BakedQuad)upQuads.get(0)).func_187508_a() == bms.func_178126_b().func_174952_b().func_174944_f())
/*  904 */         { texture = bms.func_178122_a(state);
/*  905 */           if (texture == bms.func_178126_b().func_174952_b().func_174944_f())
/*  906 */             for (int i = EnumFacing.field_82609_l.length - 1; i >= 0; i--) {
/*  907 */               if (i != 1) {
/*  908 */                 List<BakedQuad> quads = model.func_188616_a(state, EnumFacing.field_82609_l[i], 0L);
/*  909 */                 if (!quads.isEmpty())
/*  910 */                 { texture = ((BakedQuad)quads.get(0)).func_187508_a();
/*  911 */                   if (texture != bms.func_178126_b().func_174952_b().func_174944_f())
/*      */                     break;  } 
/*      */               } 
/*      */             }   }
/*  915 */         else { texture = ((BakedQuad)upQuads.get(0)).func_187508_a(); }
/*  916 */          name = texture.func_94215_i() + ".png";
/*      */         
/*  918 */         if (b instanceof net.minecraft.block.BlockOre && b != Blocks.field_150449_bY)
/*  919 */           name = "minecraft:blocks/stone.png"; 
/*  920 */         c = Integer.valueOf(-1);
/*  921 */         String[] args = name.split(":");
/*  922 */         if (args.length < 2)
/*  923 */           args = new String[] { "minecraft", args[0] }; 
/*  924 */         Integer cachedColour = this.textureColours.get(name);
/*  925 */         if (cachedColour == null) {
/*      */ 
/*      */           
/*  928 */           ResourceLocation location = new ResourceLocation(args[0], "textures/" + args[1]);
/*  929 */           IResource resource = Minecraft.func_71410_x().func_110442_L().func_110536_a(location);
/*      */           
/*  931 */           InputStream input = resource.func_110527_b();
/*  932 */           BufferedImage img = TextureUtil.func_177053_a(input);
/*  933 */           red = 0;
/*  934 */           green = 0;
/*  935 */           blue = 0;
/*  936 */           int total = 64;
/*  937 */           int tw = img.getWidth();
/*  938 */           int diff = tw / 8;
/*  939 */           for (int i = 0; i < 8; i++) {
/*  940 */             for (int j = 0; j < 8; j++) {
/*  941 */               int rgb = img.getRGB(i * diff, j * diff);
/*  942 */               int alpha = rgb >> 24 & 0xFF;
/*  943 */               if (rgb == 0 || alpha == 0) {
/*  944 */                 total--;
/*      */               } else {
/*      */                 
/*  947 */                 red += rgb >> 16 & 0xFF;
/*  948 */                 green += rgb >> 8 & 0xFF;
/*  949 */                 blue += rgb & 0xFF;
/*      */               } 
/*      */             } 
/*  952 */           }  input.close();
/*  953 */           if (total == 0)
/*  954 */             total = 1; 
/*  955 */           red /= total;
/*  956 */           green /= total;
/*  957 */           blue /= total;
/*  958 */           if (convert && red == 0 && green == 0 && blue == 0)
/*      */           {
/*  960 */             throw new Exception("Black texture");
/*      */           }
/*      */ 
/*      */           
/*  964 */           c = Integer.valueOf(0xFF000000 | red << 16 | green << 8 | blue);
/*  965 */           this.textureColours.put(name, c);
/*      */         } else {
/*      */           
/*  968 */           c = cachedColour;
/*      */         } 
/*  970 */       } catch (FileNotFoundException e) {
/*  971 */         if (convert) {
/*  972 */           return loadBlockColourFromTexture(world, state, b, pos, false);
/*      */         }
/*  974 */         c = Integer.valueOf(0);
/*  975 */         if (state != null && state.func_185909_g((IBlockAccess)world, pos) != null)
/*  976 */           c = Integer.valueOf((state.func_185909_g((IBlockAccess)world, pos)).field_76291_p); 
/*  977 */         if (name != null) {
/*  978 */           this.textureColours.put(name, c);
/*      */         }
/*  980 */         System.out.println("Block file not found: " + Block.field_149771_c.func_177774_c(b));
/*  981 */       } catch (Exception e) {
/*  982 */         if (state.func_185909_g((IBlockAccess)world, pos) != null)
/*  983 */           c = Integer.valueOf((state.func_185909_g((IBlockAccess)world, pos)).field_76291_p); 
/*  984 */         if (name != null)
/*  985 */           this.textureColours.put(name, c); 
/*  986 */         System.out.println("Exception when loading " + Block.field_149771_c.func_177774_c(b) + " texture, using material colour.");
/*      */       } 
/*      */       
/*  989 */       if (c != null) {
/*  990 */         this.blockColours.put(Integer.valueOf(stateHash), c);
/*      */       }
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  997 */     return c.intValue();
/*      */   }
/*      */   
/*      */   private int addBlockColorMultipliers(int c, IBlockState state, World world, BlockPos pos) {
/* 1001 */     if (this.modMain.getSettings().getBlockColours() == 1 && !this.loadingBiomesVanillaMode) {
/* 1002 */       return c;
/*      */     }
/*      */ 
/*      */     
/* 1006 */     int grassColor = 16777215;
/*      */     try {
/* 1008 */       grassColor = Minecraft.func_71410_x().func_184125_al().func_186724_a(state, (IBlockAccess)world, pos, 0);
/* 1009 */     } catch (IllegalArgumentException illegalArgumentException) {
/*      */     
/* 1011 */     } catch (NullPointerException nullPointerException) {
/*      */     
/* 1013 */     } catch (IllegalStateException illegalStateException) {
/*      */     
/* 1015 */     } catch (IndexOutOfBoundsException indexOutOfBoundsException) {
/*      */     
/* 1017 */     } catch (ReportedException reportedException) {}
/*      */ 
/*      */     
/* 1020 */     if (grassColor != 16777215) {
/* 1021 */       float rMultiplier = (c >> 16 & 0xFF) / 255.0F;
/* 1022 */       float gMultiplier = (c >> 8 & 0xFF) / 255.0F;
/* 1023 */       float bMultiplier = (c & 0xFF) / 255.0F;
/*      */       
/* 1025 */       int red = (int)((grassColor >> 16 & 0xFF) * rMultiplier);
/* 1026 */       int green = (int)((grassColor >> 8 & 0xFF) * gMultiplier);
/* 1027 */       int blue = (int)((grassColor & 0xFF) * bMultiplier);
/* 1028 */       c = 0xFF000000 | red << 16 | green << 8 | blue;
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1035 */     return c;
/*      */   }
/*      */   
/*      */   private boolean ignoreWorld(World world) {
/* 1039 */     for (int i = 0; i < dimensionsToIgnore.length; i++) {
/* 1040 */       if (dimensionsToIgnore[i].equals(world.field_73011_w.func_186058_p().func_186065_b()))
/* 1041 */         return true; 
/* 1042 */     }  return false;
/*      */   }
/*      */   private int getCaving(double playerX, double playerY, double playerZ, World world) {
/*      */     int skyLight;
/* 1046 */     if (!this.modMain.getSettings().getCaveMaps())
/* 1047 */       return -1; 
/* 1048 */     if (ignoreWorld(world))
/* 1049 */       return this.lastCaving; 
/* 1050 */     int y = Math.max((int)playerY + 1, 0);
/* 1051 */     if (y > 255 || y < 0)
/* 1052 */       return -1; 
/* 1053 */     int x = OptimizedMath.myFloor(playerX);
/* 1054 */     int z = OptimizedMath.myFloor(playerZ);
/* 1055 */     this.mutableBlockPos.func_181079_c(x, y, z);
/*      */ 
/*      */     
/* 1058 */     Chunk bchunk = world.func_72964_e(x >> 4, z >> 4);
/* 1059 */     if (bchunk != null && bchunk.func_177410_o()) {
/* 1060 */       skyLight = bchunk.func_177413_a(EnumSkyBlock.SKY, (BlockPos)this.mutableBlockPos);
/*      */     } else {
/* 1062 */       return -1;
/*      */     } 
/*      */ 
/*      */     
/* 1066 */     if (skyLight < 15) {
/* 1067 */       int roofRadius = (this.modMain.getSettings()).caveMaps - 1;
/* 1068 */       int insideX = x & 0xF;
/* 1069 */       int insideZ = z & 0xF;
/* 1070 */       int top = bchunk.func_76611_b(insideX, insideZ);
/* 1071 */       for (int i = y; i <= top; i++) {
/*      */         
/* 1073 */         boolean roofExists = true;
/*      */         int o;
/* 1075 */         label37: for (o = x - roofRadius; o <= x + roofRadius; o++) {
/* 1076 */           for (int p = z - roofRadius; p <= z + roofRadius; p++) {
/* 1077 */             this.mutableBlockPos.func_181079_c(o, i, p);
/* 1078 */             IBlockState state = world.func_180495_p((BlockPos)this.mutableBlockPos);
/* 1079 */             if (!state.func_185904_a().func_76218_k()) {
/* 1080 */               roofExists = false; break label37;
/*      */             } 
/*      */           } 
/*      */         } 
/* 1084 */         if (roofExists)
/* 1085 */           return this.lastCaving = Math.min(i, y + 3); 
/*      */       } 
/*      */     } 
/* 1088 */     return -1;
/*      */   }
/*      */   
/*      */   public int getLoadSide() {
/* 1092 */     return 9;
/*      */   }
/*      */   
/*      */   public int getUpdateRadiusInChunks() {
/* 1096 */     return 
/* 1097 */       (int)Math.ceil(this.loadingSideInChunks / 2.0D / this.modMain.getInterfaces().getMinimap().getMinimapZoom());
/*      */   }
/*      */   
/*      */   public int getMapCoord(int side, double coord) {
/* 1101 */     return (OptimizedMath.myFloor(coord) >> 6) - side / 2;
/*      */   }
/*      */   
/*      */   public int getLoadedCaving() {
/* 1105 */     return this.loadedCaving;
/*      */   }
/*      */   
/*      */   private boolean notEmptyColor() {
/* 1109 */     return (this.red[0] != 0 || this.green[0] != 0 || this.blue[0] != 0);
/*      */   }
/*      */   
/*      */   public float getBlockBrightness(float min, int sun, int lightLevel, int blockLight) {
/* 1113 */     if (this.loadingLevels == 1)
/* 1114 */       return (min + sun) / (15.0F + min); 
/* 1115 */     return (min + Math.max(((lightLevel == -1 || lightLevel == 0) ? 1.0F : ((this.loadingLevels - 1.0F - lightLevel) / (this.loadingLevels - 1.0F))) * sun, blockLight)) / (15.0F + min);
/*      */   }
/*      */ 
/*      */   
/*      */   public MinimapChunk[][] getLoadedBlocks() {
/* 1120 */     return this.loadedBlocks;
/*      */   }
/*      */   
/*      */   public int getLoadedMapChunkZ() {
/* 1124 */     return this.loadedMapChunkZ;
/*      */   }
/*      */   
/*      */   public int getLoadedMapChunkX() {
/* 1128 */     return this.loadedMapChunkX;
/*      */   }
/*      */   
/*      */   public int getLoadedLevels() {
/* 1132 */     return this.loadedLevels;
/*      */   }
/*      */   
/*      */   public void setClearBlockColours(boolean clearBlockColours) {
/* 1136 */     this.clearBlockColours = clearBlockColours;
/*      */   }
/*      */   
/*      */   public Long getSeedForLoading() {
/* 1140 */     return this.seedForLoading;
/*      */   }
/*      */   
/*      */   public void setSeedForLoading(Long seedForLoading) {
/* 1144 */     this.seedForLoading = seedForLoading;
/*      */   }
/*      */ }


/* Location:              C:\Users\ioanm\Downloads\Xaeros_Minimap_20.15.3_Forge_1.12.jar!\xaero\common\minimap\write\MinimapWriter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */