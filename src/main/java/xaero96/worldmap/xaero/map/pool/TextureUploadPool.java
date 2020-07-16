/*    */ package xaero.map.pool;
/*    */ 
/*    */ import xaero.map.graphics.TextureUpload;
/*    */ 
/*    */ public abstract class TextureUploadPool<T extends TextureUpload>
/*    */   extends MapPool<T> {
/*    */   public TextureUploadPool(int maxSize) {
/*  8 */     super(maxSize);
/*    */   }
/*    */   
/*    */   public static class Normal
/*    */     extends TextureUploadPool<TextureUpload.Normal> {
/*    */     public Normal(int maxSize) {
/* 14 */       super(maxSize);
/*    */     }
/*    */ 
/*    */     
/*    */     protected TextureUpload.Normal construct(Object... args) {
/* 19 */       return new TextureUpload.Normal(args);
/*    */     }
/*    */     
/*    */     public TextureUpload.Normal get(int glTexture, int glPbo, int target, int level, int internalFormat, int width, int height, int border, long pixels_buffer_offset, int format, int type) {
/* 23 */       return (TextureUpload.Normal)get(new Object[] { Integer.valueOf(glTexture), Integer.valueOf(glPbo), Integer.valueOf(target), Integer.valueOf(level), Integer.valueOf(internalFormat), Integer.valueOf(width), Integer.valueOf(height), Integer.valueOf(border), Long.valueOf(pixels_buffer_offset), Integer.valueOf(format), Integer.valueOf(type) });
/*    */     }
/*    */   }
/*    */   
/*    */   public static class NormalWithDownload
/*    */     extends TextureUploadPool<TextureUpload.NormalWithDownload>
/*    */   {
/*    */     public NormalWithDownload(int maxSize) {
/* 31 */       super(maxSize);
/*    */     }
/*    */ 
/*    */     
/*    */     protected TextureUpload.NormalWithDownload construct(Object... args) {
/* 36 */       return new TextureUpload.NormalWithDownload(args);
/*    */     }
/*    */     
/*    */     public TextureUpload.NormalWithDownload get(int glTexture, int glPbo, int target, int level, int internalFormat, int width, int height, int border, long pixels_buffer_offset, int format, int type, int glPackPbo) {
/* 40 */       return (TextureUpload.NormalWithDownload)get(new Object[] { Integer.valueOf(glTexture), Integer.valueOf(glPbo), Integer.valueOf(target), Integer.valueOf(level), Integer.valueOf(internalFormat), Integer.valueOf(width), Integer.valueOf(height), Integer.valueOf(border), Long.valueOf(pixels_buffer_offset), Integer.valueOf(format), Integer.valueOf(type), Integer.valueOf(glPackPbo) });
/*    */     }
/*    */   }
/*    */   
/*    */   public static class Compressed
/*    */     extends TextureUploadPool<TextureUpload.Compressed>
/*    */   {
/*    */     public Compressed(int maxSize) {
/* 48 */       super(maxSize);
/*    */     }
/*    */ 
/*    */     
/*    */     protected TextureUpload.Compressed construct(Object... args) {
/* 53 */       return new TextureUpload.Compressed(args);
/*    */     }
/*    */     
/*    */     public TextureUpload.Compressed get(int glTexture, int glPbo, int target, int level, int internalFormat, int width, int height, int border, long pixels_buffer_offset, int dataSize) {
/* 57 */       return (TextureUpload.Compressed)get(new Object[] { Integer.valueOf(glTexture), Integer.valueOf(glPbo), Integer.valueOf(target), Integer.valueOf(level), Integer.valueOf(internalFormat), Integer.valueOf(width), Integer.valueOf(height), Integer.valueOf(border), Long.valueOf(pixels_buffer_offset), Integer.valueOf(dataSize) });
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ioanm\Downloads\XaerosWorldMap_1.7.3_Forge_1.12.jar!\xaero\map\pool\TextureUploadPool.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */