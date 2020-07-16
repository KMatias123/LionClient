/*     */ package xaero.map.region;
/*     */ 
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.nio.ByteBuffer;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.world.World;
/*     */ import org.lwjgl.BufferUtils;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ import org.lwjgl.opengl.GL15;
/*     */ import org.lwjgl.opengl.OpenGLException;
/*     */ import org.lwjgl.opengl.Util;
/*     */ import xaero.map.MapProcessor;
/*     */ import xaero.map.WorldMap;
/*     */ import xaero.map.file.IOHelper;
/*     */ import xaero.map.graphics.TextureUploader;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MapTileChunk
/*     */ {
/*     */   public static final int SIDE_LENGTH = 4;
/*     */   private static final int PBO_UNPACK_LENGTH = 16384;
/*     */   public static final int PBO_PACK_LENGTH = 16384;
/*     */   private MapRegion inRegion;
/*  33 */   private byte loadState = 0;
/*     */   private int X;
/*     */   private int Z;
/*  36 */   private MapTile[][] tiles = new MapTile[4][4];
/*  37 */   private byte[][] tileGridsCache = new byte[this.tiles.length][this.tiles.length];
/*     */   private int glColorTexture;
/*     */   private int glLightTexture;
/*     */   private ByteBuffer colorBuffer;
/*     */   private ByteBuffer lightBuffer;
/*     */   private boolean toUpdateBuffers;
/*     */   private boolean toUpload;
/*     */   private boolean lightBufferPrepared;
/*     */   private int colorBufferFormat;
/*     */   private boolean colorBufferIsCompressed;
/*     */   private byte[][] heightValues;
/*     */   private int heightValueMask;
/*     */   private byte topTileSuccessMask;
/*     */   private boolean cachePrepared;
/*     */   private boolean updatingBuffers;
/*     */   private boolean changed;
/*     */   private Object updateBuffersSync;
/*     */   private int packPbo;
/*     */   private int[] unpackPbo;
/*     */   private boolean shouldDownloadFromPBO;
/*     */   private int timer;
/*     */   private boolean includeInSave;
/*     */   
/*     */   public MapTileChunk(MapRegion r, int x, int z) {
/*  61 */     this.X = x;
/*  62 */     this.Z = z;
/*  63 */     this.inRegion = r;
/*  64 */     this.updateBuffersSync = new Object();
/*  65 */     this.unpackPbo = new int[2];
/*  66 */     this.glColorTexture = -1;
/*  67 */     this.glLightTexture = -1;
/*  68 */     this.colorBufferFormat = -1;
/*  69 */     this.heightValues = new byte[64][64];
/*  70 */     synchronized (r) {
/*  71 */       r.setAllCachePrepared(false);
/*     */     } 
/*     */   }
/*     */   
/*     */   public ByteBuffer createBuffer() {
/*  76 */     return BufferUtils.createByteBuffer(16384);
/*     */   }
/*     */   
/*     */   public void updateBuffers(World world) {
/*  80 */     if (!Minecraft.func_71410_x().func_152345_ab())
/*  81 */       throw new RuntimeException("Wrong thread!"); 
/*  82 */     synchronized (this.updateBuffersSync) {
/*  83 */       synchronized (this) {
/*  84 */         this.updatingBuffers = true;
/*     */       } 
/*  86 */       if (WorldMap.settings.detailed_debug)
/*  87 */         System.out.println("Updating buffers: " + this.X + " " + this.Z + " " + this.loadState); 
/*  88 */       synchronized (this.inRegion) {
/*  89 */         this.cachePrepared = false;
/*  90 */         this.shouldDownloadFromPBO = false;
/*  91 */         this.inRegion.setAllCachePrepared(false);
/*     */       } 
/*  93 */       if (this.colorBuffer != null) {
/*  94 */         this.colorBuffer.clear();
/*  95 */         BufferUtils.zeroBuffer(this.colorBuffer);
/*     */       } else {
/*  97 */         this.colorBuffer = createBuffer();
/*  98 */       }  if (this.lightBuffer != null) {
/*  99 */         this.lightBuffer.clear();
/* 100 */         BufferUtils.zeroBuffer(this.lightBuffer);
/*     */       } else {
/* 102 */         this.lightBuffer = createBuffer();
/* 103 */       }  int[] result = this.inRegion.getPixelResultBuffer();
/* 104 */       boolean hasLight = false;
/* 105 */       BlockPos.MutableBlockPos mutableGlobalPos = this.inRegion.getMutableGlobalPos();
/* 106 */       this.topTileSuccessMask = 15;
/* 107 */       MapTileChunk prevTileChunk = null;
/* 108 */       for (int o = 0; o < this.tiles.length; o++) {
/* 109 */         int offX = o * 16;
/* 110 */         for (int p = 0; p < this.tiles.length; p++) {
/* 111 */           MapTile tile = this.tiles[o][p];
/* 112 */           if (tile != null && tile.isLoaded()) {
/*     */             
/* 114 */             int offZ = p * 16;
/* 115 */             if (tile.getPrevTile() == null || (p == 0 && (this.Z & 0x7) == 0))
/* 116 */               prevTileChunk = findPrevTile(null, tile, o, p); 
/* 117 */             for (int z = 0; z < 16; z++) {
/* 118 */               for (int x = 0; x < 16; x++) {
/* 119 */                 tile.getBlock(x, z).getPixelColour(result, MapProcessor.instance.getMapWriter(), world, this, prevTileChunk, tile.getPrevTile(), tile, x, z, mutableGlobalPos);
/* 120 */                 putColour(offX + x, offZ + z, result[0], result[1], result[2], this.colorBuffer, 64);
/* 121 */                 if (result[3] != 0)
/* 122 */                   hasLight = true; 
/* 123 */                 putColour(offX + x, offZ + z, result[3], result[3], result[3], this.lightBuffer, 64);
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } 
/* 128 */       }  if (!hasLight) {
/* 129 */         deleteLightBuffer();
/*     */       }
/* 131 */       this.colorBufferFormat = -1;
/* 132 */       this.colorBufferIsCompressed = false;
/* 133 */       this.toUpdateBuffers = false;
/* 134 */       this.toUpload = true;
/* 135 */       this.lightBufferPrepared = hasLight;
/* 136 */       synchronized (this) {
/* 137 */         this.updatingBuffers = false;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean bindColorTexture(boolean create, int magFilter) {
/* 143 */     boolean result = false;
/* 144 */     if (this.glColorTexture == -1)
/* 145 */       if (create) {
/* 146 */         this.glColorTexture = GL11.glGenTextures();
/* 147 */         result = true;
/*     */       } else {
/* 149 */         return false;
/*     */       }  
/* 151 */     GlStateManager.func_179144_i(this.glColorTexture);
/* 152 */     if (result)
/* 153 */       setupTextureParameters(); 
/* 154 */     GL11.glTexParameteri(3553, 10240, magFilter);
/* 155 */     return true;
/*     */   }
/*     */   
/*     */   public void bindLightTexture(boolean create, int magFilter) {
/* 159 */     boolean result = false;
/* 160 */     if (this.glLightTexture == -1) {
/* 161 */       if (create) {
/* 162 */         this.glLightTexture = GL11.glGenTextures();
/* 163 */         result = true;
/*     */       } else {
/* 165 */         GlStateManager.func_179144_i(0);
/*     */         return;
/*     */       } 
/*     */     }
/* 169 */     GlStateManager.func_179144_i(this.glLightTexture);
/* 170 */     if (result)
/* 171 */       setupTextureParameters(); 
/* 172 */     GL11.glTexParameteri(3553, 10240, magFilter);
/*     */   }
/*     */   
/*     */   private void setupTextureParameters() {
/* 176 */     GL11.glTexParameteri(3553, 33084, 0);
/* 177 */     GL11.glTexParameteri(3553, 33085, 0);
/* 178 */     GL11.glTexParameterf(3553, 33082, 0.0F);
/* 179 */     GL11.glTexParameterf(3553, 33083, 1.0F);
/* 180 */     GL11.glTexParameterf(3553, 34049, 0.0F);
/*     */ 
/*     */     
/* 183 */     GL11.glTexParameteri(3553, 10241, 9729);
/* 184 */     GL11.glTexParameteri(3553, 10242, 33071);
/* 185 */     GL11.glTexParameteri(3553, 10243, 33071);
/*     */   }
/*     */   public long uploadBuffer(TextureUploader textureUploader) throws OpenGLException, IllegalArgumentException, IllegalAccessException {
/*     */     int length;
/* 189 */     if (WorldMap.settings.detailed_debug)
/* 190 */       System.out.println("Uploading buffer: " + this.X + " " + this.Z + " " + this.colorBufferFormat + " " + this.colorBufferIsCompressed); 
/* 191 */     if (this.colorBufferFormat != -1) {
/*     */ 
/*     */       
/* 194 */       int i = this.colorBuffer.remaining();
/* 195 */       writeToUnpackPBO(0, this.colorBuffer);
/* 196 */       boolean bool = this.colorBufferIsCompressed;
/* 197 */       int internalFormat = this.colorBufferFormat;
/* 198 */       this.colorBufferIsCompressed = false;
/* 199 */       this.colorBufferFormat = -1;
/* 200 */       bindColorTexture(true, 9728);
/* 201 */       Util.checkGLError();
/* 202 */       long totalEstimatedTime = 0L;
/* 203 */       if (bool) {
/* 204 */         totalEstimatedTime = textureUploader.requestCompressed(this.glColorTexture, this.unpackPbo[0], 3553, 0, internalFormat, 64, 64, 0, 0L, i);
/*     */       } else {
/* 206 */         totalEstimatedTime = textureUploader.requestNormal(this.glColorTexture, this.unpackPbo[0], 3553, 0, internalFormat, 64, 64, 0, 0L, 32993, 32821);
/* 207 */       }  totalEstimatedTime += requestLightBufferUpload(textureUploader);
/* 208 */       return totalEstimatedTime;
/*     */     } 
/* 210 */     if (!this.shouldDownloadFromPBO) {
/*     */       long totalEstimatedTime;
/* 212 */       writeToUnpackPBO(0, this.colorBuffer);
/*     */       
/* 214 */       if (WorldMap.settings.compression) {
/* 215 */         this.timer = 5;
/* 216 */         this.shouldDownloadFromPBO = true;
/* 217 */         bindPackPBO();
/* 218 */         unbindPackPBO();
/* 219 */         bindColorTexture(true, 9728);
/* 220 */         Util.checkGLError();
/* 221 */         totalEstimatedTime = textureUploader.requestNormalWithDownload(this.glColorTexture, this.unpackPbo[0], 3553, 0, 34029, 64, 64, 0, 0L, 32993, 32821, this.packPbo);
/*     */       } else {
/* 223 */         this.colorBuffer.position(0);
/* 224 */         this.colorBufferFormat = 32856;
/* 225 */         bindColorTexture(true, 9728);
/* 226 */         Util.checkGLError();
/* 227 */         totalEstimatedTime = textureUploader.requestNormal(this.glColorTexture, this.unpackPbo[0], 3553, 0, 32856, 64, 64, 0, 0L, 32993, 32821);
/*     */       } 
/* 229 */       boolean toUploadImmediately = this.inRegion.isBeingWritten();
/* 230 */       if (toUploadImmediately)
/* 231 */         textureUploader.finishNewestRequestImmediately(); 
/* 232 */       boolean lightBufferPreparedBU = this.lightBufferPrepared;
/* 233 */       totalEstimatedTime += requestLightBufferUpload(textureUploader);
/* 234 */       if (toUploadImmediately && lightBufferPreparedBU)
/* 235 */         textureUploader.finishNewestRequestImmediately(); 
/* 236 */       return totalEstimatedTime;
/*     */     } 
/*     */     
/* 239 */     int glTexture = this.glColorTexture;
/* 240 */     GlStateManager.func_179144_i(glTexture);
/* 241 */     int isCompressed = GL11.glGetTexLevelParameteri(3553, 0, 34465);
/*     */     
/* 243 */     if (isCompressed == 1) {
/* 244 */       length = GL11.glGetTexLevelParameteri(3553, 0, 34464);
/*     */     } else {
/* 246 */       length = 16384;
/* 247 */     }  Util.checkGLError();
/* 248 */     bindPackPBO();
/* 249 */     ByteBuffer mappedPBO = GL15.glMapBuffer(35051, 35000, length, null);
/* 250 */     Util.checkGLError();
/*     */     
/* 252 */     this.colorBuffer.clear();
/* 253 */     this.colorBuffer.put(mappedPBO);
/* 254 */     this.colorBuffer.flip();
/*     */     
/* 256 */     GL15.glUnmapBuffer(35051);
/* 257 */     Util.checkGLError();
/* 258 */     unbindPackPBO();
/* 259 */     Util.checkGLError();
/* 260 */     int format = GL11.glGetTexLevelParameteri(3553, 0, 4099);
/* 261 */     Util.checkGLError();
/* 262 */     this.colorBufferFormat = format;
/* 263 */     if (format == -1)
/* 264 */       throw new RuntimeException("Invalid texture internal format returned by the driver."); 
/* 265 */     this.colorBufferIsCompressed = (isCompressed == 1);
/* 266 */     this.shouldDownloadFromPBO = false;
/* 267 */     return 0L;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private long requestLightBufferUpload(TextureUploader textureUploader) {
/* 273 */     if (this.lightBufferPrepared) {
/* 274 */       writeToUnpackPBO(1, this.lightBuffer);
/* 275 */       this.lightBuffer.position(0);
/* 276 */       bindLightTexture(true, 9728);
/*     */       
/* 278 */       this.lightBufferPrepared = false;
/* 279 */       return textureUploader.requestNormal(this.glLightTexture, this.unpackPbo[1], 3553, 0, 6408, 64, 64, 0, 0L, 32993, 32821);
/* 280 */     }  if (this.glLightTexture != -1) {
/*     */       
/* 282 */       MapProcessor.instance.requestTextureDeletion(this.glLightTexture);
/* 283 */       this.glLightTexture = -1;
/*     */     } 
/* 285 */     return 0L;
/*     */   }
/*     */   
/*     */   private void writeToUnpackPBO(int pboIndex, ByteBuffer buffer) {
/* 289 */     bindUnpackPBO(pboIndex);
/* 290 */     ByteBuffer mappedPBO = GL15.glMapBuffer(35052, 35001, 16384L, null);
/* 291 */     Util.checkGLError();
/* 292 */     mappedPBO.put(buffer);
/* 293 */     GL15.glUnmapBuffer(35052);
/* 294 */     unbindUnpackPBO();
/*     */   }
/*     */   
/*     */   public void writeCacheData(DataOutputStream output, byte[] usableBuffer, byte[] integerByteBuffer) throws IOException {
/* 298 */     output.write(this.colorBufferIsCompressed ? 1 : 0);
/* 299 */     output.writeInt(this.colorBufferFormat);
/*     */     
/* 301 */     int length = this.colorBuffer.remaining();
/* 302 */     output.writeInt(length);
/*     */ 
/*     */     
/* 305 */     this.colorBuffer.get(usableBuffer, 0, length);
/* 306 */     output.write(usableBuffer, 0, length);
/* 307 */     if (this.lightBuffer != null) {
/* 308 */       int lightLength = this.lightBuffer.remaining();
/* 309 */       output.writeInt(lightLength);
/* 310 */       this.lightBuffer.get(usableBuffer, 0, lightLength);
/* 311 */       output.write(usableBuffer, 0, lightLength);
/*     */     } else {
/* 313 */       output.writeInt(0);
/* 314 */     }  output.writeInt(this.heightValueMask);
/* 315 */     for (int i = 0; i < 64; i++)
/* 316 */       output.write(this.heightValues[i], 0, 64); 
/* 317 */     if ((this.Z & 0x7) == 0)
/* 318 */       output.write(this.topTileSuccessMask); 
/* 319 */     deleteBuffers();
/* 320 */     synchronized (this.inRegion) {
/* 321 */       this.cachePrepared = false;
/* 322 */       this.inRegion.setAllCachePrepared(false);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void readCacheData(int cacheSaveVersion, DataInputStream input, byte[] usableBuffer, byte[] integerByteBuffer) throws IOException {
/* 327 */     int lightLevelsInCache = (cacheSaveVersion < 3) ? 4 : 1; int i;
/* 328 */     for (i = 0; i < lightLevelsInCache; i++) {
/* 329 */       if (i == 0) {
/* 330 */         this.colorBufferIsCompressed = true;
/* 331 */         if (cacheSaveVersion > 1)
/* 332 */           this.colorBufferIsCompressed = (input.read() == 1); 
/* 333 */         this.colorBufferFormat = input.readInt();
/*     */       } else {
/* 335 */         if (cacheSaveVersion > 1)
/* 336 */           input.read(); 
/* 337 */         input.readInt();
/*     */       } 
/* 339 */       int length = input.readInt();
/*     */       
/* 341 */       IOHelper.readToBuffer(usableBuffer, length, input);
/* 342 */       if (i == 0) {
/* 343 */         if (this.colorBuffer == null)
/* 344 */           this.colorBuffer = createBuffer(); 
/* 345 */         if (length == 16384 && this.colorBufferIsCompressed) {
/* 346 */           this.colorBufferIsCompressed = false;
/* 347 */           this.colorBufferFormat = 32856;
/* 348 */           this.inRegion.setShouldCache(true, "broken texture compression fix");
/* 349 */           this.colorBuffer.clear();
/* 350 */           this.colorBuffer.limit(16384);
/*     */         } else {
/* 352 */           this.colorBuffer.put(usableBuffer, 0, length);
/* 353 */           this.colorBuffer.flip();
/*     */         } 
/*     */       } 
/*     */     } 
/* 357 */     if (cacheSaveVersion > 2) {
/* 358 */       int lightLength = input.readInt();
/* 359 */       if (lightLength > 0) {
/* 360 */         IOHelper.readToBuffer(usableBuffer, lightLength, input);
/* 361 */         if (this.lightBuffer == null)
/* 362 */           this.lightBuffer = createBuffer(); 
/* 363 */         this.lightBuffer.put(usableBuffer, 0, lightLength);
/* 364 */         this.lightBuffer.flip();
/* 365 */         this.lightBufferPrepared = true;
/*     */       } 
/*     */     } 
/* 368 */     if (cacheSaveVersion == 4) {
/* 369 */       boolean hasBottomHeightValues = (input.read() == 1);
/* 370 */       if (hasBottomHeightValues) {
/* 371 */         this.heightValueMask = (input.readByte() & 0xFF) << 12;
/* 372 */         byte[] bottomHeights = new byte[64];
/* 373 */         IOHelper.readToBuffer(bottomHeights, 64, input);
/* 374 */         for (int j = 0; j < 64; j++) {
/* 375 */           this.heightValues[j][63] = bottomHeights[j];
/*     */         }
/*     */       } 
/* 378 */     } else if (cacheSaveVersion >= 5) {
/* 379 */       this.heightValueMask = input.readInt();
/* 380 */       for (i = 0; i < 64; i++)
/* 381 */         IOHelper.readToBuffer(this.heightValues[i], 64, input); 
/*     */     } 
/* 383 */     if (cacheSaveVersion >= 4 && (
/* 384 */       this.Z & 0x7) == 0) {
/* 385 */       this.topTileSuccessMask = input.readByte();
/*     */     }
/* 387 */     this.toUpload = true;
/* 388 */     this.loadState = 2;
/*     */   }
/*     */   
/*     */   public void putColour(int x, int y, int red, int green, int blue, ByteBuffer buffer, int size) {
/* 392 */     int pos = (y * size + x) * 4;
/*     */     
/* 394 */     buffer.putInt(pos, blue << 24 | green << 16 | red << 8 | 0xFF);
/*     */   }
/*     */   
/*     */   public MapTileChunk findPrevTile(MapRegion prevRegion, MapTile tile, int o, int p) {
/* 398 */     if (p > 0) {
/* 399 */       tile.setPrevTile(this.tiles[o][p - 1]);
/*     */     } else {
/* 401 */       int chunkXInsideRegion = this.X & 0x7;
/* 402 */       int chunkZInsideRegion = this.Z & 0x7;
/* 403 */       MapTileChunk prevTileChunk = null;
/* 404 */       if (chunkZInsideRegion > 0) {
/* 405 */         prevTileChunk = this.inRegion.getChunk(chunkXInsideRegion, chunkZInsideRegion - 1);
/*     */       } else {
/* 407 */         if (prevRegion == null)
/* 408 */           prevRegion = MapProcessor.instance.getMapRegion(this.inRegion.getRegionX(), this.inRegion.getRegionZ() - 1, false); 
/* 409 */         if (prevRegion != null)
/* 410 */           prevTileChunk = prevRegion.getChunk(chunkXInsideRegion, 7); 
/* 411 */         return prevTileChunk;
/*     */       } 
/* 413 */       if (prevTileChunk != null)
/* 414 */         tile.setPrevTile(prevTileChunk.tiles[o][prevTileChunk.tiles.length - 1]); 
/*     */     } 
/* 416 */     return null;
/*     */   }
/*     */   
/*     */   private void bindPackPBO() {
/* 420 */     boolean created = false;
/* 421 */     if (this.packPbo == 0) {
/* 422 */       this.packPbo = GL15.glGenBuffers();
/* 423 */       created = true;
/*     */     } 
/* 425 */     GL15.glBindBuffer(35051, this.packPbo);
/* 426 */     if (created)
/* 427 */       GL15.glBufferData(35051, 16384L, 35041); 
/*     */   }
/*     */   
/*     */   private void bindUnpackPBO(int index) {
/* 431 */     boolean created = false;
/* 432 */     if (this.unpackPbo[index] == 0) {
/* 433 */       this.unpackPbo[index] = GL15.glGenBuffers();
/* 434 */       created = true;
/*     */     } 
/* 436 */     GL15.glBindBuffer(35052, this.unpackPbo[index]);
/* 437 */     if (created)
/* 438 */       GL15.glBufferData(35052, 16384L, 35040); 
/*     */   }
/*     */   
/*     */   private void unbindPackPBO() {
/* 442 */     GL15.glBindBuffer(35051, 0);
/*     */   }
/*     */   
/*     */   private void unbindUnpackPBO() {
/* 446 */     GL15.glBindBuffer(35052, 0);
/*     */   }
/*     */   
/*     */   public void deletePBOs() {
/* 450 */     if (this.packPbo > 0)
/* 451 */       MapProcessor.instance.requestBufferToDelete(this.packPbo); 
/* 452 */     this.packPbo = 0;
/* 453 */     for (int i = 0; i < this.unpackPbo.length; i++) {
/* 454 */       if (this.unpackPbo[i] > 0) {
/* 455 */         MapProcessor.instance.requestBufferToDelete(this.unpackPbo[i]);
/* 456 */         this.unpackPbo[i] = 0;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   public void clean() {
/* 461 */     for (int o = 0; o < 4; o++) {
/* 462 */       for (int p = 0; p < 4; p++) {
/* 463 */         MapTile tile = this.tiles[o][p];
/* 464 */         if (tile != null)
/*     */         
/* 466 */         { if (tile.getPrevTile() != null)
/* 467 */             tile.setPrevTile(null); 
/* 468 */           MapProcessor.instance.getTilePool().addToPool(tile);
/* 469 */           this.tiles[o][p] = null; } 
/*     */       } 
/* 471 */     }  this.toUpdateBuffers = false;
/* 472 */     this.includeInSave = false;
/*     */   }
/*     */   
/*     */   public int getX() {
/* 476 */     return this.X;
/*     */   }
/*     */   
/*     */   public int getZ() {
/* 480 */     return this.Z;
/*     */   }
/*     */   
/*     */   public byte[][] getTileGridsCache() {
/* 484 */     return this.tileGridsCache;
/*     */   }
/*     */   
/*     */   public int getLoadState() {
/* 488 */     return this.loadState;
/*     */   }
/*     */   
/*     */   public void setLoadState(byte loadState) {
/* 492 */     this.loadState = loadState;
/*     */   }
/*     */   
/*     */   public boolean shouldUpload() {
/* 496 */     return this.toUpload;
/*     */   }
/*     */   
/*     */   public void setToUpload(boolean value) {
/* 500 */     this.toUpload = value;
/*     */   }
/*     */   
/*     */   public ByteBuffer getColorBuffer() {
/* 504 */     return this.colorBuffer;
/*     */   }
/*     */   
/*     */   public void deleteBuffers() {
/* 508 */     MapProcessor.instance.getBufferDeallocator().deallocate(this.colorBuffer, WorldMap.settings.debug);
/* 509 */     this.colorBuffer = null;
/* 510 */     deleteLightBuffer();
/*     */   }
/*     */   
/*     */   private void deleteLightBuffer() {
/* 514 */     if (this.lightBuffer != null) {
/* 515 */       MapProcessor.instance.getBufferDeallocator().deallocate(this.lightBuffer, WorldMap.settings.debug);
/* 516 */       this.lightBuffer = null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isCachePrepared() {
/* 521 */     return this.cachePrepared;
/*     */   }
/*     */   
/*     */   public void setCachePrepared(boolean cachePrepared) {
/* 525 */     this.cachePrepared = cachePrepared;
/*     */   }
/*     */   
/*     */   public boolean isUpdatingBuffers() {
/* 529 */     return this.updatingBuffers;
/*     */   }
/*     */   
/*     */   public MapTile getTile(int x, int z) {
/* 533 */     return this.tiles[x][z];
/*     */   }
/*     */   
/*     */   public void setTile(int x, int z, MapTile tile) {
/* 537 */     if (tile != null) {
/* 538 */       this.includeInSave = true;
/* 539 */       for (int i = 0; i < 16; i++) {
/* 540 */         for (int j = 0; j < 16; j++)
/* 541 */           this.heightValues[x * 16 + i][z * 16 + j] = tile.getBlock(i, j).getSignedHeight(); 
/*     */       } 
/* 543 */       this.heightValueMask |= 1 << x + (z << 2);
/* 544 */     } else if (this.tiles[x][z] != null) {
/* 545 */       this.heightValueMask ^= 1 << x + (z << 2);
/* 546 */     }  this.tiles[x][z] = tile;
/*     */   }
/*     */   
/*     */   public MapRegion getInRegion() {
/* 550 */     return this.inRegion;
/*     */   }
/*     */   
/*     */   public byte getSuccessMask() {
/* 554 */     return this.topTileSuccessMask;
/*     */   }
/*     */   
/*     */   public void setUnsuccessful(int x) {
/* 558 */     if ((this.topTileSuccessMask >> x & 0x1) != 0)
/* 559 */       this.topTileSuccessMask = (byte)(this.topTileSuccessMask - (1 << x)); 
/*     */   }
/*     */   
/*     */   public boolean wasChanged() {
/* 563 */     return this.changed;
/*     */   }
/*     */   
/*     */   public void setChanged(boolean changed) {
/* 567 */     this.changed = changed;
/*     */   }
/*     */   
/*     */   public int getGlColorTexture() {
/* 571 */     return this.glColorTexture;
/*     */   }
/*     */   
/*     */   public boolean getColorBufferIsCompressed() {
/* 575 */     return this.colorBufferIsCompressed;
/*     */   }
/*     */   
/*     */   public int getColorBufferFormat() {
/* 579 */     return this.colorBufferFormat;
/*     */   }
/*     */   
/*     */   public int getTimer() {
/* 583 */     return this.timer;
/*     */   }
/*     */   
/*     */   public void decTimer() {
/* 587 */     this.timer--;
/*     */   }
/*     */   
/*     */   public boolean shouldDownloadFromPBO() {
/* 591 */     return this.shouldDownloadFromPBO;
/*     */   }
/*     */   
/*     */   public boolean includeInSave() {
/* 595 */     return this.includeInSave;
/*     */   }
/*     */   
/*     */   public void unincludeInSave() {
/* 599 */     this.includeInSave = false;
/*     */   }
/*     */   
/*     */   public ByteBuffer getLightBuffer() {
/* 603 */     return this.lightBuffer;
/*     */   }
/*     */   
/*     */   public int getGlLightTexture() {
/* 607 */     return this.glLightTexture;
/*     */   }
/*     */   
/*     */   public void resetMasks() {
/* 611 */     this.heightValueMask = 0;
/* 612 */     this.topTileSuccessMask = 0;
/*     */   }
/*     */   
/*     */   public boolean getToUpdateBuffers() {
/* 616 */     return this.toUpdateBuffers;
/*     */   }
/*     */   
/*     */   public void setToUpdateBuffers(boolean toUpdateBuffers) {
/* 620 */     this.toUpdateBuffers = toUpdateBuffers;
/*     */   }
/*     */   
/*     */   public int getHeight(int x, int z) {
/* 624 */     if (this.heightValueMask == 0 || (this.heightValueMask & 1 << (x >> 4) + (z >> 4 << 2)) == 0)
/* 625 */       return -1; 
/* 626 */     return this.heightValues[x][z] & 0xFF;
/*     */   }
/*     */   
/*     */   public int getHeightValueMask() {
/* 630 */     return this.heightValueMask;
/*     */   }
/*     */   
/*     */   public boolean tileChunkShouldTriggerUpdate(MapTileChunk topChunk) {
/* 634 */     if (this.topTileSuccessMask == 15)
/* 635 */       return false; 
/* 636 */     if (this.topTileSuccessMask != topChunk.heightValueMask >> 12)
/* 637 */       for (int i = 0; i < 4; i++) {
/* 638 */         if ((topChunk.heightValueMask >> 12 + i & 0x1) > (this.topTileSuccessMask >> i & 0x1))
/* 639 */           return true; 
/* 640 */       }   return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\ioanm\Downloads\XaerosWorldMap_1.7.3_Forge_1.12.jar!\xaero\map\region\MapTileChunk.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */