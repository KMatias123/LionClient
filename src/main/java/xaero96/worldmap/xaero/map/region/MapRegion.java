/*     */ package xaero.map.region;
/*     */ 
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.util.zip.ZipEntry;
/*     */ import java.util.zip.ZipInputStream;
/*     */ import java.util.zip.ZipOutputStream;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import xaero.map.MapProcessor;
/*     */ import xaero.map.WorldMap;
/*     */ import xaero.map.file.MapRegionInfo;
/*     */ import xaero.map.world.MapDimension;
/*     */ 
/*     */ 
/*     */ public class MapRegion
/*     */   implements Comparable<MapRegion>, MapRegionInfo
/*     */ {
/*     */   public static final int SIDE_LENGTH = 8;
/*     */   private Boolean saveExists;
/*     */   private File regionFile;
/*     */   private boolean beingWritten;
/*     */   private long lastVisited;
/*  30 */   private long lastSaveTime = System.currentTimeMillis();
/*     */   private byte loadState;
/*  32 */   private int version = -1;
/*     */   private int initialVersion;
/*     */   private final boolean isMultiplayer;
/*     */   private String world;
/*     */   private MapDimension dim;
/*  37 */   private MapTileChunk[][] chunks = new MapTileChunk[8][8];
/*     */   
/*     */   private int regionX;
/*     */   
/*     */   private int regionZ;
/*     */   
/*     */   private boolean shouldCache;
/*     */   
/*     */   private boolean isRefreshing;
/*     */   
/*     */   private boolean allCachePrepared;
/*  48 */   private File cacheFile = null;
/*     */   
/*  50 */   public final Object writerThreadPauseSync = new Object();
/*     */   private int pauseWriting;
/*     */   private boolean recacheHasBeenRequested;
/*     */   private boolean reloadHasBeenRequested;
/*  54 */   private static int comparisonX = 0;
/*  55 */   private static int comparisonZ = 0;
/*     */ 
/*     */ 
/*     */   
/*  59 */   private int[] pixelResultBuffer = new int[4];
/*  60 */   private BlockPos.MutableBlockPos mutableGlobalPos = new BlockPos.MutableBlockPos();
/*     */   
/*     */   public MapRegion(String world, MapDimension dim, int x, int z) {
/*  63 */     this.world = world;
/*  64 */     this.regionX = x;
/*  65 */     this.regionZ = z;
/*  66 */     this.dim = dim;
/*  67 */     this.initialVersion = MapProcessor.instance.getGlobalVersion();
/*  68 */     this.isMultiplayer = MapProcessor.instance.isWorldMultiplayer(MapProcessor.instance.isWorldRealms(world), world);
/*     */   }
/*     */   
/*     */   public void destroyBufferUpdateObjects() {
/*  72 */     this.pixelResultBuffer = null;
/*  73 */     this.mutableGlobalPos = null;
/*     */   }
/*     */   
/*     */   public void restoreBufferUpdateObjects() {
/*  77 */     this.pixelResultBuffer = new int[4];
/*  78 */     this.mutableGlobalPos = new BlockPos.MutableBlockPos();
/*     */   }
/*     */   
/*     */   public void requestRefresh() {
/*  82 */     if (!this.isRefreshing) {
/*  83 */       this.isRefreshing = true;
/*  84 */       MapProcessor.instance.addToRefresh(this);
/*  85 */       if (WorldMap.settings.debug)
/*  86 */         System.out.println(String.format("Requesting refresh for region %s.", new Object[] { this })); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void cancelRefresh() {
/*  91 */     if (this.isRefreshing) {
/*  92 */       this.isRefreshing = false;
/*  93 */       MapProcessor.instance.removeToRefresh(this);
/*  94 */       if (WorldMap.settings.debug)
/*  95 */         System.out.println(String.format("Canceling refresh for region %s.", new Object[] { this })); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void setComparison(EntityPlayer player) {
/* 100 */     setComparison((int)Math.floor(player.field_70165_t) >> 9, (int)Math.floor(player.field_70161_v) >> 9);
/*     */   }
/*     */   
/*     */   public static void setComparison(int x, int z) {
/* 104 */     comparisonX = x;
/* 105 */     comparisonZ = z;
/*     */   }
/*     */ 
/*     */   
/*     */   public int compareTo(MapRegion arg0) {
/* 110 */     if (this.loadState < arg0.loadState)
/* 111 */       return -1; 
/* 112 */     if (this.loadState > arg0.loadState)
/* 113 */       return 1; 
/* 114 */     if (this.shouldCache && !arg0.shouldCache)
/* 115 */       return 1; 
/* 116 */     if (arg0.shouldCache && !this.shouldCache) {
/* 117 */       return -1;
/*     */     }
/* 119 */     int toRegion = distanceFromPlayer();
/* 120 */     int toRegion2 = arg0.distanceFromPlayer();
/* 121 */     if (toRegion > toRegion2)
/* 122 */       return 1; 
/* 123 */     if (toRegion == toRegion2) {
/* 124 */       return 0;
/*     */     }
/* 126 */     return -1;
/*     */   }
/*     */ 
/*     */   
/*     */   public int distanceFromPlayer() {
/* 131 */     int toRegionX = this.regionX - comparisonX;
/* 132 */     int toRegionZ = this.regionZ - comparisonZ;
/* 133 */     return (int)Math.sqrt((toRegionX * toRegionX + toRegionZ * toRegionZ));
/*     */   }
/*     */   
/*     */   public void deleteTexturesAndBuffers() {
/* 137 */     for (int i = 0; i < this.chunks.length; i++) {
/* 138 */       for (int j = 0; j < this.chunks.length; j++) {
/* 139 */         MapTileChunk c = this.chunks[i][j];
/* 140 */         if (c != null) {
/* 141 */           if (c.getGlColorTexture() != -1)
/* 142 */             MapProcessor.instance.requestTextureDeletion(c.getGlColorTexture()); 
/* 143 */           if (c.getGlLightTexture() != -1)
/* 144 */             MapProcessor.instance.requestTextureDeletion(c.getGlLightTexture()); 
/* 145 */           if (c.getColorBuffer() != null)
/* 146 */             c.deleteBuffers(); 
/* 147 */           c.deletePBOs();
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void deleteBuffers() {
/* 155 */     synchronized (this) {
/* 156 */       setAllCachePrepared(false);
/*     */     } 
/* 158 */     for (int i = 0; i < this.chunks.length; i++) {
/* 159 */       for (int j = 0; j < this.chunks.length; j++) {
/* 160 */         MapTileChunk c = this.chunks[i][j];
/* 161 */         if (c != null && c.getColorBuffer() != null) {
/* 162 */           synchronized (this) {
/* 163 */             setAllCachePrepared(false);
/* 164 */             c.setCachePrepared(false);
/*     */           } 
/* 166 */           c.setToUpload(false);
/* 167 */           c.deleteBuffers();
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   public void deleteGLBuffers() {
/* 173 */     for (int i = 0; i < this.chunks.length; i++) {
/* 174 */       for (int j = 0; j < this.chunks.length; j++) {
/* 175 */         MapTileChunk c = this.chunks[i][j];
/* 176 */         if (c != null)
/* 177 */           c.deletePBOs(); 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   public void clean() {
/* 182 */     for (int i = 0; i < this.chunks.length; i++) {
/* 183 */       for (int j = 0; j < this.chunks.length; j++) {
/* 184 */         MapTileChunk c = this.chunks[i][j];
/* 185 */         if (c != null) {
/* 186 */           c.clean();
/* 187 */           this.chunks[i][j] = null;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void saveCacheTextures(File outputFile) throws IOException {
/* 237 */     if (WorldMap.settings.debug)
/* 238 */       System.out.println("Saving cache: " + this.regionX + "_" + this.regionZ); 
/* 239 */     ZipOutputStream zipOutput = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(outputFile)));
/* 240 */     DataOutputStream output = new DataOutputStream(zipOutput);
/* 241 */     ZipEntry e = new ZipEntry("cache.xaero");
/* 242 */     zipOutput.putNextEntry(e);
/* 243 */     byte[] usableBuffer = new byte[16384];
/* 244 */     byte[] integerByteBuffer = new byte[4];
/* 245 */     output.writeInt(5);
/* 246 */     for (int i = 0; i < this.chunks.length; i++) {
/* 247 */       for (int j = 0; j < this.chunks.length; j++) {
/* 248 */         MapTileChunk chunk = this.chunks[i][j];
/* 249 */         if (chunk != null) {
/* 250 */           if (!chunk.isCachePrepared())
/* 251 */             throw new RuntimeException("Trying to save cache but " + chunk.getX() + " " + chunk.getZ() + " is not prepared."); 
/* 252 */           output.write(i << 4 | j);
/* 253 */           chunk.writeCacheData(output, usableBuffer, integerByteBuffer);
/*     */         } 
/*     */       } 
/* 256 */     }  zipOutput.closeEntry();
/* 257 */     output.close();
/*     */   }
/*     */   
/*     */   public void loadCacheTextures() {
/* 261 */     if (this.cacheFile == null)
/*     */       return; 
/* 263 */     if (this.cacheFile.exists()) {
/* 264 */       DataInputStream input = null;
/*     */       try {
/* 266 */         MapRegion prevRegion = null;
/* 267 */         boolean prevRegionChecked = false;
/* 268 */         ZipInputStream zipInput = new ZipInputStream(new BufferedInputStream(new FileInputStream(this.cacheFile)));
/* 269 */         input = new DataInputStream(zipInput);
/* 270 */         ZipEntry entry = zipInput.getNextEntry();
/* 271 */         if (entry != null) {
/* 272 */           byte[] integerByteBuffer = new byte[4];
/* 273 */           int cacheSaveVersion = input.readInt();
/* 274 */           if (cacheSaveVersion > 5) {
/* 275 */             input.close();
/* 276 */             System.out.println("Trying to load newer region cache " + this.regionX + "_" + this.regionZ + " using an older version of Xaero's World Map!");
/* 277 */             MapProcessor.instance.getMapSaveLoad().backupFile(this.cacheFile, cacheSaveVersion);
/* 278 */             this.cacheFile = null;
/* 279 */             this.shouldCache = true;
/*     */             return;
/*     */           } 
/* 282 */           if (cacheSaveVersion < 5)
/* 283 */             this.shouldCache = true; 
/* 284 */           byte[] usableBuffer = new byte[16384];
/* 285 */           int chunkCoords = input.read();
/* 286 */           while (chunkCoords != -1) {
/* 287 */             int x = chunkCoords >> 4;
/* 288 */             int z = chunkCoords & 0xF;
/* 289 */             MapTileChunk chunk = this.chunks[x][z];
/* 290 */             if (chunk == null)
/* 291 */               this.chunks[x][z] = chunk = new MapTileChunk(this, this.regionX * 8 + x, this.regionZ * 8 + z); 
/* 292 */             chunk.readCacheData(cacheSaveVersion, input, usableBuffer, integerByteBuffer);
/* 293 */             if (!this.shouldCache && z == 0 && chunk.getSuccessMask() != 15) {
/* 294 */               if (!prevRegionChecked && prevRegion == null) {
/* 295 */                 prevRegion = MapProcessor.instance.getMapRegion(this.regionX, this.regionZ - 1, false);
/* 296 */                 prevRegionChecked = true;
/*     */               } 
/* 298 */               if (prevRegion != null) {
/* 299 */                 MapTileChunk prevChunk = prevRegion.getChunk(x, 7);
/* 300 */                 if (prevChunk != null && chunk.tileChunkShouldTriggerUpdate(prevChunk))
/* 301 */                   this.shouldCache = true; 
/*     */               } 
/*     */             } 
/* 304 */             chunkCoords = input.read();
/*     */           } 
/* 306 */           zipInput.closeEntry();
/*     */         } 
/* 308 */         input.close();
/* 309 */       } catch (IOException ioe) {
/* 310 */         this.cacheFile = null;
/* 311 */         this.shouldCache = true;
/* 312 */         System.out.println("Failed to load cache for region " + this + "! " + this.cacheFile);
/* 313 */         ioe.printStackTrace();
/* 314 */         if (input != null)
/*     */           try {
/* 316 */             input.close();
/* 317 */           } catch (IOException e) {
/* 318 */             e.printStackTrace();
/*     */           }  
/*     */       } 
/*     */     } else {
/* 322 */       this.cacheFile = null;
/* 323 */       this.shouldCache = true;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void onTopRegionLoaded(MapRegion topRegion) {
/* 328 */     if (this.version > 0) {
/* 329 */       for (int o = 0; o < 8; o++) {
/* 330 */         MapTileChunk edgeTopChunk = topRegion.getChunk(o, 7);
/* 331 */         if (edgeTopChunk != null) {
/* 332 */           MapTileChunk edgeBottomChunk = this.chunks[o][0];
/* 333 */           if (edgeBottomChunk != null && edgeBottomChunk.tileChunkShouldTriggerUpdate(edgeTopChunk)) {
/* 334 */             this.version--;
/*     */             break;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void clearRegion() {
/* 344 */     setRecacheHasBeenRequested(false, "clearing");
/* 345 */     cancelRefresh();
/* 346 */     for (int i = 0; i < 8; i++) {
/* 347 */       for (int j = 0; j < 8; j++) {
/* 348 */         MapTileChunk c = getChunk(i, j);
/* 349 */         if (c != null) {
/*     */ 
/*     */ 
/*     */           
/* 353 */           c.setLoadState((byte)3);
/* 354 */           setLoadState((byte)3);
/* 355 */           c.clean();
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 360 */     if (this.dim.getMapWorld() != MapProcessor.instance.getLastNonNullWorld() || !MapProcessor.instance.getMapSaveLoad().toCacheContains(this))
/* 361 */       deleteBuffers(); 
/* 362 */     deleteGLBuffers();
/* 363 */     setLoadState((byte)4);
/* 364 */     if (WorldMap.settings.debug) {
/* 365 */       System.out.println("Cleared region! " + this + " " + getWorld() + " " + getRegionX() + "_" + getRegionZ());
/*     */     }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isResting() {
/* 424 */     return (this.loadState != 3 && this.loadState != 1 && !this.recacheHasBeenRequested);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean shouldCache() {
/* 435 */     return this.shouldCache;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setShouldCache(boolean shouldCache, String by) {
/* 440 */     this.shouldCache = shouldCache;
/* 441 */     if (WorldMap.settings.detailed_debug) {
/* 442 */       System.out.println("shouldCache set to " + shouldCache + " by " + by + " for " + this.regionX + "_" + this.regionZ);
/*     */     }
/*     */   }
/*     */   
/*     */   public File getCacheFile() {
/* 447 */     return this.cacheFile;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCacheFile(File cacheFile) {
/* 452 */     this.cacheFile = cacheFile;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getWorld() {
/* 457 */     return this.world;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getRegionX() {
/* 462 */     return this.regionX;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getRegionZ() {
/* 467 */     return this.regionZ;
/*     */   }
/*     */   
/*     */   public int getVersion() {
/* 471 */     return this.version;
/*     */   }
/*     */   
/*     */   public void setVersion(int version) {
/* 475 */     this.version = version;
/* 476 */     if (WorldMap.settings.detailed_debug)
/* 477 */       System.out.println("Version set to " + version + " by for " + this); 
/*     */   }
/*     */   
/*     */   public boolean isBeingWritten() {
/* 481 */     return this.beingWritten;
/*     */   }
/*     */   
/*     */   public void setBeingWritten(boolean beingWritten) {
/* 485 */     this.beingWritten = beingWritten;
/*     */   }
/*     */   
/*     */   public byte getLoadState() {
/* 489 */     return this.loadState;
/*     */   }
/*     */   
/*     */   public void setLoadState(byte loadState) {
/* 493 */     this.loadState = loadState;
/*     */   }
/*     */   
/*     */   public MapTileChunk getChunk(int x, int z) {
/* 497 */     return this.chunks[x][z];
/*     */   }
/*     */   
/*     */   public void setChunk(int x, int z, MapTileChunk chunk) {
/* 501 */     this.chunks[x][z] = chunk;
/*     */   }
/*     */   
/*     */   public int getInitialVersion() {
/* 505 */     return this.initialVersion;
/*     */   }
/*     */   
/*     */   public void setInitialVersion(int initialVersion) {
/* 509 */     this.initialVersion = initialVersion;
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
/*     */   public MapDimension getDim() {
/* 521 */     return this.dim;
/*     */   }
/*     */   
/*     */   public int[] getPixelResultBuffer() {
/* 525 */     return this.pixelResultBuffer;
/*     */   }
/*     */   
/*     */   public BlockPos.MutableBlockPos getMutableGlobalPos() {
/* 529 */     return this.mutableGlobalPos;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public File getRegionFile() {
/* 538 */     return this.regionFile;
/*     */   }
/*     */   
/*     */   public void setRegionFile(File loadedFromFile) {
/* 542 */     this.regionFile = loadedFromFile;
/*     */   }
/*     */   
/*     */   public Boolean getSaveExists() {
/* 546 */     return this.saveExists;
/*     */   }
/*     */   
/*     */   public void setSaveExists(Boolean saveExists) {
/* 550 */     this.saveExists = saveExists;
/*     */   }
/*     */   
/*     */   public long getLastSaveTime() {
/* 554 */     return this.lastSaveTime;
/*     */   }
/*     */   
/*     */   public void setLastSaveTime(long lastSaveTime) {
/* 558 */     this.lastSaveTime = lastSaveTime;
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
/*     */   public boolean isAllCachePrepared() {
/* 570 */     return this.allCachePrepared;
/*     */   }
/*     */   
/*     */   public void setAllCachePrepared(boolean allCachePrepared) {
/* 574 */     if (this.allCachePrepared && !allCachePrepared && WorldMap.settings.detailed_debug)
/* 575 */       System.out.println("Cancelling cache: " + getRegionX() + "_" + getRegionZ() + " " + getLoadState()); 
/* 576 */     this.allCachePrepared = allCachePrepared;
/*     */   }
/*     */   
/*     */   public boolean isRefreshing() {
/* 580 */     return this.isRefreshing;
/*     */   }
/*     */   
/*     */   public void setRefreshing(boolean isRefreshing) {
/* 584 */     this.isRefreshing = isRefreshing;
/*     */   }
/*     */   
/*     */   public boolean isWritingPaused() {
/* 588 */     return (this.pauseWriting > 0);
/*     */   }
/*     */   
/*     */   public void pushWriterPause() {
/* 592 */     synchronized (this.writerThreadPauseSync) {
/* 593 */       this.pauseWriting++;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void popWriterPause() {
/* 598 */     synchronized (this.writerThreadPauseSync) {
/* 599 */       this.pauseWriting--;
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean recacheHasBeenRequested() {
/* 604 */     return this.recacheHasBeenRequested;
/*     */   }
/*     */   
/*     */   public void setRecacheHasBeenRequested(boolean recacheHasBeenRequested, String by) {
/* 608 */     if (WorldMap.settings.detailed_debug && recacheHasBeenRequested != this.recacheHasBeenRequested)
/* 609 */       System.out.println("Recache set to " + recacheHasBeenRequested + " by " + by + " for " + this.regionX + "_" + this.regionZ); 
/* 610 */     this.recacheHasBeenRequested = recacheHasBeenRequested;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 614 */     return this.regionX + "_" + this.regionZ + " " + super.toString();
/*     */   }
/*     */   
/*     */   public boolean hasVersion() {
/* 618 */     return (this.version != -1);
/*     */   }
/*     */   
/*     */   public boolean reloadHasBeenRequested() {
/* 622 */     return this.reloadHasBeenRequested;
/*     */   }
/*     */   
/*     */   public void setReloadHasBeenRequested(boolean reloadHasBeenRequested, String by) {
/* 626 */     if (WorldMap.settings.detailed_debug && reloadHasBeenRequested != this.reloadHasBeenRequested)
/* 627 */       System.out.println("Reload set to " + reloadHasBeenRequested + " by " + by + " for " + this.regionX + "_" + this.regionZ); 
/* 628 */     this.reloadHasBeenRequested = reloadHasBeenRequested;
/*     */   }
/*     */   
/*     */   public boolean isMultiplayer() {
/* 632 */     return this.isMultiplayer;
/*     */   }
/*     */   
/*     */   public long getLastVisited() {
/* 636 */     return this.lastVisited;
/*     */   }
/*     */   
/*     */   public long getTimeSinceVisit() {
/* 640 */     return System.currentTimeMillis() - this.lastVisited;
/*     */   }
/*     */   
/*     */   public void registerVisit() {
/* 644 */     this.lastVisited = System.currentTimeMillis();
/*     */   }
/*     */   
/*     */   public int countChunks() {
/* 648 */     int count = 0;
/* 649 */     for (int i = 0; i < this.chunks.length; i++) {
/* 650 */       for (int j = 0; j < this.chunks.length; j++)
/* 651 */       { if (this.chunks[i][j] != null)
/* 652 */           count++;  } 
/* 653 */     }  return count;
/*     */   }
/*     */ }


/* Location:              C:\Users\ioanm\Downloads\XaerosWorldMap_1.7.3_Forge_1.12.jar!\xaero\map\region\MapRegion.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */