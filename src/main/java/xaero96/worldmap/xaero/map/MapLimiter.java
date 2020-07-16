/*    */ package xaero.map;
/*    */ 
/*    */ import java.nio.IntBuffer;
/*    */ import java.util.List;
/*    */ import org.lwjgl.BufferUtils;
/*    */ import org.lwjgl.opengl.GL11;
/*    */ import xaero.map.file.RegionDetection;
/*    */ import xaero.map.region.MapRegion;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MapLimiter
/*    */ {
/*    */   private static final int MIN_LIMIT = 50;
/* 16 */   private int availableVRAM = -1;
/*    */   private int mostRegionsAtATime;
/* 18 */   private IntBuffer vramBuffer = BufferUtils.createByteBuffer(64).asIntBuffer();
/*    */   
/*    */   public int getAvailableVRAM() {
/* 21 */     return this.availableVRAM;
/*    */   }
/*    */   
/*    */   public void updateAvailableVRAM() {
/* 25 */     this.vramBuffer.clear();
/* 26 */     this.vramBuffer.put(0, -1);
/* 27 */     GL11.glGetInteger(36937, this.vramBuffer);
/* 28 */     int availableVRAM = this.vramBuffer.get(0);
/* 29 */     int error = GL11.glGetError();
/* 30 */     if (availableVRAM == -1 || error != 0) {
/* 31 */       this.vramBuffer.position(0);
/* 32 */       GL11.glGetInteger(34812, this.vramBuffer);
/* 33 */       availableVRAM = this.vramBuffer.get(0);
/* 34 */       error = GL11.glGetError();
/* 35 */       if (error != 0)
/* 36 */         availableVRAM = -1; 
/*    */     } 
/* 38 */     this.availableVRAM = availableVRAM;
/*    */   }
/*    */   
/*    */   public int getMostRegionsAtATime() {
/* 42 */     return this.mostRegionsAtATime;
/*    */   }
/*    */   
/*    */   public void setMostRegionsAtATime(int mostRegionsAtATime) {
/* 46 */     this.mostRegionsAtATime = mostRegionsAtATime;
/*    */   }
/*    */   
/*    */   public void applyLimit(List<MapRegion> regionsSorted) {
/* 50 */     int limit = Math.max(this.mostRegionsAtATime, 50);
/* 51 */     int vramDetermined = 0;
/* 52 */     int test = this.availableVRAM;
/* 53 */     if (test != -1) {
/* 54 */       vramDetermined = regionsSorted.size();
/* 55 */       if (test < 102400)
/* 56 */       { vramDetermined = vramDetermined * 3 / 4; }
/*    */       else { return; }
/*    */     
/*    */     } else {
/* 60 */       vramDetermined = 400;
/* 61 */     }  if (vramDetermined > limit)
/* 62 */       limit = vramDetermined; 
/* 63 */     MapProcessor.instance.pushRenderPause(false, true);
/* 64 */     MapRegion nextToLoad = MapProcessor.instance.getMapSaveLoad().getNextToLoadByViewing();
/* 65 */     for (int i = 0; i < regionsSorted.size() && regionsSorted.size() > limit; i++) {
/* 66 */       MapRegion region = regionsSorted.get(i);
/* 67 */       if (region.getLoadState() == 4 || region.getLoadState() == 0) {
/* 68 */         region.deleteTexturesAndBuffers();
/* 69 */         region.pushWriterPause();
/* 70 */         RegionDetection restoredDetection = new RegionDetection(region.getWorld(), region.getRegionX(), region.getRegionZ(), region.getRegionFile());
/* 71 */         restoredDetection.transferInfoFrom(region);
/* 72 */         MapProcessor.instance.addRegionDetection(restoredDetection);
/* 73 */         MapProcessor.instance.removeMapRegion(region);
/* 74 */         MapProcessor.instance.removeToProcess(region);
/* 75 */         MapProcessor.instance.getMapSaveLoad().removeToLoad(region);
/* 76 */         MapProcessor.instance.getMapSaveLoad().cancelAllLoadRequests(region);
/* 77 */         MapProcessor.instance.getMapSaveLoad().removeToCache(region);
/* 78 */         if (WorldMap.settings.debug)
/* 79 */           System.out.println("Region removed by limiter: " + region + " " + test + " " + limit); 
/* 80 */         region.popWriterPause();
/* 81 */         if (region == nextToLoad)
/* 82 */           MapProcessor.instance.getMapSaveLoad().setNextToLoadByViewing(null); 
/* 83 */         i--;
/*    */       } 
/*    */     } 
/* 86 */     MapProcessor.instance.popRenderPause(false, true);
/*    */   }
/*    */ }


/* Location:              C:\Users\ioanm\Downloads\XaerosWorldMap_1.7.3_Forge_1.12.jar!\xaero\map\MapLimiter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */