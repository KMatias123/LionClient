/*    */ package xaero.map.graphics;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import org.lwjgl.opengl.GL11;
/*    */ import xaero.map.pool.TextureUploadPool;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class TextureUploader
/*    */ {
/*    */   public static final int NORMAL = 0;
/*    */   public static final int NORMALDOWNLOAD = 1;
/*    */   public static final int COMPRESSED = 2;
/*    */   private static final int DEFAULT_NORMALDOWNLOAD_TIME = 3000000;
/*    */   private static final int DEFAULT_NORMAL_TIME = 1000000;
/*    */   private static final int DEFAULT_COMPRESSED_TIME = 1000000;
/*    */   private List<TextureUpload> textureUploadRequests;
/*    */   private TextureUploadBenchmark textureUploadBenchmark;
/*    */   private TextureUploadPool.Normal normalTextureUploadPool;
/*    */   private TextureUploadPool.NormalWithDownload normalWithDownloadTextureUploadPool;
/*    */   private TextureUploadPool.Compressed compressedTextureUploadPool;
/*    */   
/*    */   public TextureUploader(TextureUploadPool.Normal normalTextureUploadPool, TextureUploadPool.NormalWithDownload normalWithDownloadTextureUploadPool, TextureUploadPool.Compressed compressedTextureUploadPool, TextureUploadBenchmark textureUploadBenchmark) {
/* 27 */     this.textureUploadRequests = new ArrayList<>();
/* 28 */     this.normalTextureUploadPool = normalTextureUploadPool;
/* 29 */     this.normalWithDownloadTextureUploadPool = normalWithDownloadTextureUploadPool;
/* 30 */     this.compressedTextureUploadPool = compressedTextureUploadPool;
/* 31 */     this.textureUploadBenchmark = textureUploadBenchmark;
/*    */   }
/*    */   
/*    */   public long requestUpload(TextureUpload upload) {
/* 35 */     this.textureUploadRequests.add(upload);
/* 36 */     if (upload instanceof TextureUpload.NormalWithDownload)
/* 37 */       return this.textureUploadBenchmark.isFinished(1) ? Math.min(this.textureUploadBenchmark.getAverage(1), 3000000L) : 3000000L; 
/* 38 */     if (upload instanceof TextureUpload.Normal) {
/* 39 */       return this.textureUploadBenchmark.isFinished(0) ? Math.min(this.textureUploadBenchmark.getAverage(0), 1000000L) : 1000000L;
/*    */     }
/* 41 */     return this.textureUploadBenchmark.isFinished(2) ? Math.min(this.textureUploadBenchmark.getAverage(2), 1000000L) : 1000000L;
/*    */   }
/*    */   
/*    */   public long requestNormal(int glTexture, int glPbo, int target, int level, int internalFormat, int width, int height, int border, long pixels_buffer_offset, int format, int type) {
/* 45 */     TextureUpload upload = this.normalTextureUploadPool.get(glTexture, glPbo, target, level, internalFormat, width, height, border, pixels_buffer_offset, format, type);
/* 46 */     return requestUpload(upload);
/*    */   }
/*    */   
/*    */   public long requestNormalWithDownload(int glTexture, int glPbo, int target, int level, int internalFormat, int width, int height, int border, long pixels_buffer_offset, int format, int type, int glPackPbo) {
/* 50 */     TextureUpload upload = this.normalWithDownloadTextureUploadPool.get(glTexture, glPbo, target, level, internalFormat, width, height, border, pixels_buffer_offset, format, type, glPackPbo);
/* 51 */     return requestUpload(upload);
/*    */   }
/*    */   
/*    */   public long requestCompressed(int glTexture, int glPbo, int target, int level, int internalFormat, int width, int height, int border, long pixels_buffer_offset, int dataSize) {
/* 55 */     TextureUpload upload = this.compressedTextureUploadPool.get(glTexture, glPbo, target, level, internalFormat, width, height, border, pixels_buffer_offset, dataSize);
/* 56 */     return requestUpload(upload);
/*    */   }
/*    */   
/*    */   public void finishNewestRequestImmediately() {
/* 60 */     TextureUpload newestRequest = this.textureUploadRequests.remove(this.textureUploadRequests.size() - 1);
/* 61 */     newestRequest.run();
/* 62 */     addToPool(newestRequest);
/*    */   }
/*    */   
/*    */   public void uploadTextures() {
/* 66 */     if (!this.textureUploadRequests.isEmpty()) {
/* 67 */       boolean prepared = false;
/* 68 */       for (int i = 0; i < this.textureUploadRequests.size(); i++) {
/* 69 */         TextureUpload tu = this.textureUploadRequests.get(i);
/* 70 */         int type = tu.getUploadType();
/* 71 */         if (!this.textureUploadBenchmark.isFinished(type)) {
/* 72 */           if (!prepared) {
/* 73 */             GL11.glFinish();
/* 74 */             prepared = true;
/*    */           } 
/* 76 */           this.textureUploadBenchmark.pre();
/*    */         } 
/* 78 */         tu.run();
/* 79 */         if (!this.textureUploadBenchmark.isFinished(type)) {
/* 80 */           this.textureUploadBenchmark.post(type);
/* 81 */           prepared = true;
/*    */         } 
/* 83 */         addToPool(tu);
/*    */       } 
/* 85 */       this.textureUploadRequests.clear();
/*    */     } 
/*    */   }
/*    */   
/*    */   private void addToPool(TextureUpload tu) {
/* 90 */     switch (tu.getUploadType()) {
/*    */       case 0:
/* 92 */         this.normalTextureUploadPool.addToPool(tu);
/*    */         break;
/*    */       case 1:
/* 95 */         this.normalWithDownloadTextureUploadPool.addToPool(tu);
/*    */         break;
/*    */       case 2:
/* 98 */         this.compressedTextureUploadPool.addToPool(tu);
/*    */         break;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\ioanm\Downloads\XaerosWorldMap_1.7.3_Forge_1.12.jar!\xaero\map\graphics\TextureUploader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */