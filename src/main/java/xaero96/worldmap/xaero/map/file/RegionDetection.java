/*    */ package xaero.map.file;
/*    */ 
/*    */ import java.awt.image.BufferedImage;
/*    */ import java.io.File;
/*    */ import javax.imageio.ImageIO;
/*    */ import xaero.map.MapProcessor;
/*    */ import xaero.map.region.MapRegion;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RegionDetection
/*    */   implements MapRegionInfo
/*    */ {
/*    */   private int initialVersion;
/*    */   private String world;
/*    */   private int regionX;
/*    */   private int regionZ;
/*    */   private boolean shouldCache;
/*    */   private File cacheFile;
/*    */   private File regionFile;
/*    */   
/*    */   public RegionDetection(String world, int regionX, int regionZ, File regionFile) {
/* 24 */     this.world = world;
/* 25 */     this.regionX = regionX;
/* 26 */     this.regionZ = regionZ;
/* 27 */     this.regionFile = regionFile;
/* 28 */     this.initialVersion = MapProcessor.instance.getGlobalVersion();
/*    */   }
/*    */   
/*    */   public int getInitialVersion() {
/* 32 */     return this.initialVersion;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean shouldCache() {
/* 37 */     return this.shouldCache;
/*    */   }
/*    */   
/*    */   public File getCacheFile() {
/* 41 */     return this.cacheFile;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setShouldCache(boolean shouldCache, String fsdfs) {
/* 46 */     this.shouldCache = shouldCache;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setCacheFile(File cacheFile) {
/* 51 */     this.cacheFile = cacheFile;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getWorld() {
/* 56 */     return this.world;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getRegionX() {
/* 61 */     return this.regionX;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getRegionZ() {
/* 66 */     return this.regionZ;
/*    */   }
/*    */   
/*    */   public void transferInfoTo(MapRegion to) {
/* 70 */     to.setShouldCache(this.shouldCache, "transfer");
/* 71 */     to.setCacheFile(this.cacheFile);
/* 72 */     to.setInitialVersion(this.initialVersion);
/* 73 */     to.setRegionFile(this.regionFile);
/*    */   }
/*    */   
/*    */   public void transferInfoFrom(MapRegion from) {
/* 77 */     this.shouldCache = from.shouldCache();
/* 78 */     this.cacheFile = from.getCacheFile();
/* 79 */     this.initialVersion = from.getInitialVersion();
/* 80 */     this.regionFile = from.getRegionFile();
/*    */   }
/*    */   
/*    */   public BufferedImage getRegionTexture() {
/*    */     try {
/* 85 */       return ImageIO.read(this.cacheFile);
/* 86 */     } catch (Exception e) {
/* 87 */       return null;
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public File getRegionFile() {
/* 93 */     return this.regionFile;
/*    */   }
/*    */ }


/* Location:              C:\Users\ioanm\Downloads\XaerosWorldMap_1.7.3_Forge_1.12.jar!\xaero\map\file\RegionDetection.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */