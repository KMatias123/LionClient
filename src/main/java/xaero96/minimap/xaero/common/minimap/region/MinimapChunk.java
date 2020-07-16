/*     */ package xaero.common.minimap.region;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ import org.lwjgl.opengl.GL15;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MinimapChunk
/*     */ {
/*     */   private static final int SIZE_TILES = 4;
/*     */   public static final int BUFFER_SIZE = 16384;
/*     */   public static final int LIGHT_LEVELS = 5;
/*     */   private boolean blockTextureUpload;
/*     */   private int X;
/*     */   private int Z;
/*     */   private boolean hasSomething;
/*     */   private MinimapTile[][] tiles;
/*     */   private int[] glTexture;
/*     */   private boolean[] refreshRequired;
/*     */   private boolean refreshed;
/*     */   private ByteBuffer[] buffer;
/*     */   private boolean changed;
/*  29 */   private int levelsBuffered = 0;
/*     */   private int workaroundPBO;
/*     */   
/*     */   public MinimapChunk(int X, int Z) {
/*  33 */     this.X = X;
/*  34 */     this.Z = Z;
/*  35 */     this.tiles = new MinimapTile[4][4];
/*  36 */     this.glTexture = new int[5];
/*  37 */     this.refreshRequired = new boolean[5];
/*  38 */     this.buffer = new ByteBuffer[5];
/*     */   }
/*     */   
/*     */   public void reset(int X, int Z) {
/*  42 */     this.X = X;
/*  43 */     this.Z = Z;
/*  44 */     this.hasSomething = false; int i;
/*  45 */     for (i = 0; i < this.glTexture.length; i++) {
/*  46 */       this.glTexture[i] = 0;
/*  47 */       this.refreshRequired[i] = false;
/*  48 */       if (this.buffer[i] != null)
/*  49 */         this.buffer[i].clear(); 
/*     */     } 
/*  51 */     this.refreshed = false;
/*  52 */     this.changed = false;
/*  53 */     this.levelsBuffered = 0;
/*  54 */     for (i = 0; i < this.tiles.length; i++) {
/*  55 */       for (int j = 0; j < this.tiles.length; j++)
/*  56 */         this.tiles[i][j] = null; 
/*  57 */     }  this.blockTextureUpload = false;
/*     */   }
/*     */   
/*     */   public void recycleTiles() {
/*  61 */     for (int i = 0; i < this.tiles.length; i++) {
/*  62 */       for (int j = 0; j < this.tiles.length; j++) {
/*  63 */         MinimapTile tile = this.tiles[i][j];
/*  64 */         if (tile != null)
/*  65 */           if (!tile.isWasTransfered()) {
/*  66 */             tile.recycle();
/*     */           } else {
/*  68 */             tile.setWasTransfered(false);
/*     */           }  
/*     */       } 
/*     */     } 
/*     */   }
/*     */   public int getLevelToRefresh(int currentLevel) {
/*  74 */     if (this.refreshed || this.levelsBuffered == 0 || currentLevel == -1)
/*  75 */       return -1; 
/*  76 */     int prev = currentLevel - 1;
/*  77 */     if (prev < 0) {
/*  78 */       prev = this.levelsBuffered - 1;
/*     */     }
/*  80 */     for (int i = currentLevel;; i = (i + 1) % this.levelsBuffered) {
/*  81 */       if (this.refreshRequired[i])
/*  82 */         return i; 
/*  83 */       if (i == prev)
/*     */         break; 
/*     */     } 
/*  86 */     this.refreshed = true;
/*  87 */     return -1;
/*     */   }
/*     */   
/*     */   public void bindTexture(int level) {
/*  91 */     synchronized (this) {
/*  92 */       if (!this.hasSomething) {
/*  93 */         GlStateManager.func_179144_i(0);
/*     */         return;
/*     */       } 
/*  96 */       if (!this.blockTextureUpload) {
/*  97 */         int levelToRefresh = getLevelToRefresh(Math.min(level, this.levelsBuffered - 1));
/*  98 */         if (levelToRefresh != -1) {
/*  99 */           boolean result = false;
/* 100 */           if (this.glTexture[levelToRefresh] == 0) {
/* 101 */             this.glTexture[levelToRefresh] = GL11.glGenTextures();
/* 102 */             result = true;
/*     */           } 
/* 104 */           GlStateManager.func_179144_i(this.glTexture[levelToRefresh]);
/* 105 */           if (result) {
/* 106 */             GL11.glTexParameteri(3553, 33085, 0);
/* 107 */             GL11.glTexParameterf(3553, 33082, 0.0F);
/* 108 */             GL11.glTexParameterf(3553, 33083, 0.0F);
/* 109 */             GL11.glTexParameterf(3553, 34049, 0.0F);
/* 110 */             GL11.glTexParameteri(3553, 10240, 9728);
/* 111 */             GL11.glTexParameteri(3553, 10242, 33071);
/* 112 */             GL11.glTexParameteri(3553, 10243, 33071);
/* 113 */             GL11.glTexImage2D(3553, 0, 32856, 64, 64, 0, 32993, 32821, (ByteBuffer)null);
/*     */           } 
/* 115 */           if (this.workaroundPBO == 0)
/* 116 */             this.workaroundPBO = GL15.glGenBuffers(); 
/* 117 */           GL15.glBindBuffer(35052, this.workaroundPBO);
/* 118 */           GL15.glBufferData(35052, 16384L, 35040);
/* 119 */           ByteBuffer mappedPBO = GL15.glMapBuffer(35052, 35001, 16384L, null);
/* 120 */           mappedPBO.put(this.buffer[levelToRefresh]);
/* 121 */           GL15.glUnmapBuffer(35052);
/* 122 */           GL11.glTexSubImage2D(3553, 0, 0, 0, 64, 64, 32993, 32821, 0L);
/* 123 */           GL15.glBindBuffer(35052, 0);
/*     */ 
/*     */           
/* 126 */           this.refreshRequired[levelToRefresh] = false;
/*     */         } 
/*     */       } 
/* 129 */       if (this.glTexture[level] != 0) {
/* 130 */         GlStateManager.func_179144_i(this.glTexture[level]);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public void updateBuffers(int levelsToLoad, byte[][] byteArrayBuffer) {
/* 136 */     this.refreshed = true;
/* 137 */     for (int l = 0; l < levelsToLoad; l++) {
/* 138 */       this.refreshRequired[l] = false;
/* 139 */       if (this.buffer[l] == null)
/* 140 */         this.buffer[l] = ByteBuffer.allocate(16384); 
/*     */     } 
/* 142 */     for (int o = 0; o < this.tiles.length; o++) {
/* 143 */       int offX = o * 16;
/* 144 */       for (int p = 0; p < this.tiles.length; p++) {
/* 145 */         MinimapTile tile = this.tiles[o][p];
/* 146 */         int offZ = p * 16;
/* 147 */         for (int z = 0; z < 16; z++) {
/* 148 */           for (int x = 0; x < 16; x++) {
/* 149 */             for (int j = 0; j < levelsToLoad; j++) {
/* 150 */               if (tile == null) {
/* 151 */                 putColour(offX + x, offZ + z, 0, 0, 0, byteArrayBuffer[j], 64);
/*     */               } else {
/* 153 */                 putColour(offX + x, offZ + z, tile.getRed(j, x, z), tile.getGreen(j, x, z), tile.getBlue(j, x, z), byteArrayBuffer[j], 64);
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/* 159 */     }  for (int i = 0; i < levelsToLoad; i++) {
/* 160 */       synchronized (this) {
/* 161 */         this.blockTextureUpload = true;
/*     */       } 
/* 163 */       this.buffer[i].clear();
/* 164 */       this.buffer[i].put(byteArrayBuffer[i]);
/* 165 */       this.buffer[i].flip();
/* 166 */       this.refreshRequired[i] = true;
/* 167 */       synchronized (this) {
/* 168 */         this.blockTextureUpload = false;
/*     */       } 
/*     */     } 
/* 171 */     this.refreshed = false;
/*     */   }
/*     */   
/*     */   public void putColour(int x, int y, int red, int green, int blue, byte[] texture, int size) {
/* 175 */     int pos = (y * size + x) * 4;
/* 176 */     texture[pos] = -1;
/* 177 */     texture[++pos] = (byte)red;
/* 178 */     texture[++pos] = (byte)green;
/* 179 */     texture[++pos] = (byte)blue;
/*     */   }
/*     */   
/*     */   public void copyBuffer(int level, ByteBuffer toCopy) {
/* 183 */     if (this.buffer[level] == null) {
/* 184 */       this.buffer[level] = ByteBuffer.allocate(16384);
/*     */     } else {
/* 186 */       this.buffer[level].clear();
/* 187 */     }  this.buffer[level].put(toCopy);
/* 188 */     this.buffer[level].flip();
/*     */   }
/*     */   
/*     */   public int getLevelsBuffered() {
/* 192 */     return this.levelsBuffered;
/*     */   }
/*     */   
/*     */   public boolean isHasSomething() {
/* 196 */     return this.hasSomething;
/*     */   }
/*     */   
/*     */   public void setHasSomething(boolean hasSomething) {
/* 200 */     this.hasSomething = hasSomething;
/*     */   }
/*     */   
/*     */   public int getX() {
/* 204 */     return this.X;
/*     */   }
/*     */   
/*     */   public int getZ() {
/* 208 */     return this.Z;
/*     */   }
/*     */   
/*     */   public int getGlTexture(int l) {
/* 212 */     return this.glTexture[l];
/*     */   }
/*     */   
/*     */   public void setGlTexture(int l, int t) {
/* 216 */     this.glTexture[l] = t;
/*     */   }
/*     */   
/*     */   public MinimapTile getTile(int x, int z) {
/* 220 */     return this.tiles[x][z];
/*     */   }
/*     */   
/*     */   public void setTile(int x, int z, MinimapTile t) {
/* 224 */     this.tiles[x][z] = t;
/*     */   }
/*     */   
/*     */   public boolean isChanged() {
/* 228 */     return this.changed;
/*     */   }
/*     */   
/*     */   public void setChanged(boolean changed) {
/* 232 */     this.changed = changed;
/*     */   }
/*     */   
/*     */   public void setLevelsBuffered(int levelsBuffered) {
/* 236 */     this.levelsBuffered = levelsBuffered;
/*     */   }
/*     */   
/*     */   public boolean isBlockTextureUpload() {
/* 240 */     return this.blockTextureUpload;
/*     */   }
/*     */   
/*     */   public void setBlockTextureUpload(boolean blockTextureUpload) {
/* 244 */     this.blockTextureUpload = blockTextureUpload;
/*     */   }
/*     */   
/*     */   public boolean isRefreshRequired(int l) {
/* 248 */     return this.refreshRequired[l];
/*     */   }
/*     */   
/*     */   public void setRefreshRequired(int l, boolean r) {
/* 252 */     this.refreshRequired[l] = r;
/*     */   }
/*     */   
/*     */   public ByteBuffer getBuffer(int l) {
/* 256 */     return this.buffer[l];
/*     */   }
/*     */ }


/* Location:              C:\Users\ioanm\Downloads\Xaeros_Minimap_20.15.3_Forge_1.12.jar!\xaero\common\minimap\region\MinimapChunk.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */