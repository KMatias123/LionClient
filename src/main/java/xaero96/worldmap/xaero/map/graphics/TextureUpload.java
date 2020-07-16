/*     */ package xaero.map.graphics;
/*     */ 
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ import org.lwjgl.opengl.GL13;
/*     */ import org.lwjgl.opengl.GL15;
/*     */ import org.lwjgl.opengl.Util;
/*     */ import xaero.map.pool.PoolUnit;
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
/*     */ public abstract class TextureUpload
/*     */   implements PoolUnit
/*     */ {
/*     */   private int glTexture;
/*     */   private int glUnpackPbo;
/*     */   private int target;
/*     */   private int level;
/*     */   private int internalFormat;
/*     */   private int width;
/*     */   private int height;
/*     */   private int border;
/*     */   private long pixels_buffer_offset;
/*     */   private int uploadType;
/*     */   
/*     */   public void create(Object... args) {
/*  34 */     this.glTexture = ((Integer)args[0]).intValue();
/*  35 */     this.glUnpackPbo = ((Integer)args[1]).intValue();
/*  36 */     this.target = ((Integer)args[2]).intValue();
/*  37 */     this.level = ((Integer)args[3]).intValue();
/*  38 */     this.internalFormat = ((Integer)args[4]).intValue();
/*  39 */     this.width = ((Integer)args[5]).intValue();
/*  40 */     this.height = ((Integer)args[6]).intValue();
/*  41 */     this.border = ((Integer)args[7]).intValue();
/*  42 */     this.pixels_buffer_offset = ((Long)args[8]).longValue();
/*     */   }
/*     */   
/*     */   public void run() {
/*  46 */     GlStateManager.func_179144_i(this.glTexture);
/*  47 */     GL15.glBindBuffer(35052, this.glUnpackPbo);
/*  48 */     Util.checkGLError();
/*  49 */     upload();
/*  50 */     Util.checkGLError();
/*  51 */     GL15.glBindBuffer(35052, 0);
/*     */ 
/*     */     
/*  54 */     GlStateManager.func_179144_i(0);
/*  55 */     Util.checkGLError();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getUploadType() {
/*  61 */     return this.uploadType;
/*     */   }
/*     */   
/*     */   abstract void upload();
/*     */   
/*     */   public static class Normal extends TextureUpload {
/*     */     private int format;
/*     */     
/*     */     public Normal(int uploadType) {
/*  70 */       this.uploadType = uploadType;
/*     */     }
/*     */     private int type;
/*     */     
/*     */     public Normal(Object... args) {
/*  75 */       this(0);
/*  76 */       create(args);
/*     */     }
/*     */ 
/*     */     
/*     */     void upload() {
/*  81 */       GL11.glHint(34031, 4354);
/*  82 */       GL11.glTexImage2D(this.target, this.level, this.internalFormat, this.width, this.height, this.border, this.format, this.type, this.pixels_buffer_offset);
/*     */     }
/*     */ 
/*     */     
/*     */     public void create(Object... args) {
/*  87 */       super.create(args);
/*  88 */       this.format = ((Integer)args[9]).intValue();
/*  89 */       this.type = ((Integer)args[10]).intValue();
/*     */     }
/*     */   }
/*     */   
/*     */   public static class NormalWithDownload
/*     */     extends Normal
/*     */   {
/*     */     private int glPackPbo;
/*     */     
/*     */     public NormalWithDownload(Object... args) {
/*  99 */       super(1);
/* 100 */       create(args);
/*     */     }
/*     */ 
/*     */     
/*     */     void upload() {
/* 105 */       super.upload();
/*     */       
/* 107 */       GL15.glBindBuffer(35051, this.glPackPbo);
/* 108 */       int target = this.target;
/* 109 */       int isCompressed = GL11.glGetTexLevelParameteri(target, 0, 34465);
/* 110 */       if (isCompressed == 1) {
/* 111 */         GL13.glGetCompressedTexImage(target, 0, 0L);
/*     */       } else {
/* 113 */         GL11.glGetTexImage(target, 0, 32993, 32821, 0L);
/*     */       } 
/*     */ 
/*     */       
/* 117 */       GL15.glBindBuffer(35051, 0);
/*     */     }
/*     */ 
/*     */     
/*     */     public void create(Object... args) {
/* 122 */       super.create(args);
/* 123 */       this.glPackPbo = ((Integer)args[11]).intValue();
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Compressed
/*     */     extends TextureUpload
/*     */   {
/*     */     private int dataSize;
/*     */     
/*     */     public Compressed(Object... args) {
/* 133 */       create(args);
/* 134 */       this.uploadType = 2;
/*     */     }
/*     */ 
/*     */     
/*     */     void upload() {
/* 139 */       GL13.glCompressedTexImage2D(this.target, this.level, this.internalFormat, this.width, this.height, this.border, this.dataSize, this.pixels_buffer_offset);
/*     */     }
/*     */ 
/*     */     
/*     */     public void create(Object... args) {
/* 144 */       super.create(args);
/* 145 */       this.dataSize = ((Integer)args[9]).intValue();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ioanm\Downloads\XaerosWorldMap_1.7.3_Forge_1.12.jar!\xaero\map\graphics\TextureUpload.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */