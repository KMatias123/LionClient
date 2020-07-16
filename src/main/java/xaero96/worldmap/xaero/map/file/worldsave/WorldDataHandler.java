/*    */ package xaero.map.file.worldsave;
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import net.minecraft.world.World;
/*    */ import net.minecraft.world.WorldServer;
/*    */ import xaero.map.MapProcessor;
/*    */ import xaero.map.capabilities.ServerWorldCapabilities;
/*    */ import xaero.map.capabilities.ServerWorldLoaded;
/*    */ import xaero.map.region.MapRegion;
/*    */ 
/*    */ public class WorldDataHandler {
/*    */   private WorldDataReader worldDataReader;
/*    */   private WorldServer worldServer;
/*    */   private File worldDir;
/*    */   
/*    */   public enum Result {
/* 17 */     SUCCESS,
/* 18 */     FAIL,
/* 19 */     CANCEL;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public WorldDataHandler(WorldDataReader worldDataReader) {
/* 27 */     this.worldDataReader = worldDataReader;
/*    */   }
/*    */   
/*    */   public void prepareSingleplayer(World world, MapProcessor mapProcessor) {
/* 31 */     String worldString = mapProcessor.getCurrentWorldString();
/* 32 */     if (world != null && !mapProcessor.isWorldMultiplayer(mapProcessor.isWorldRealms(worldString), worldString))
/* 33 */     { this.worldServer = DimensionManager.getWorld(world.field_73011_w.getDimension(), false);
/* 34 */       if (this.worldServer != null) {
/* 35 */         this.worldDir = this.worldServer.getChunkSaveLocation();
/*    */       } else {
/* 37 */         this.worldDir = null;
/*    */       }  }
/* 39 */     else { this.worldDir = null; }
/*    */   
/*    */   }
/*    */   public Result buildRegion(MapRegion region, World world, boolean loading, int[] chunkCountDest) throws IOException {
/* 43 */     if (this.worldServer == null) {
/* 44 */       System.out.println("Tried loading a region for a null server world!");
/* 45 */       return Result.CANCEL;
/*    */     } 
/* 47 */     ServerWorldLoaded loadedCap = (ServerWorldLoaded)this.worldServer.getCapability(ServerWorldCapabilities.LOADED_CAP, null);
/* 48 */     if (loadedCap != null) {
/* 49 */       boolean shouldCancel = false;
/* 50 */       synchronized (loadedCap) {
/* 51 */         if (loadedCap.loaded) {
/* 52 */           boolean buildResult = this.worldDataReader.buildRegion(region, this.worldDir, world, loading, chunkCountDest);
/* 53 */           return buildResult ? Result.SUCCESS : Result.FAIL;
/*    */         } 
/* 55 */         shouldCancel = true;
/*    */       } 
/* 57 */       if (shouldCancel) {
/*    */ 
/*    */         
/* 60 */         System.out.println("Tried loading a region for an unloaded server world!");
/* 61 */         return Result.CANCEL;
/*    */       } 
/*    */     } 
/* 64 */     System.out.println("Server world capability required for Xaero's World Map not present!");
/* 65 */     return Result.FAIL;
/*    */   }
/*    */   
/*    */   public void onServerWorldUnload(WorldServer sw) {
/* 69 */     ServerWorldLoaded loadedCap = (ServerWorldLoaded)sw.getCapability(ServerWorldCapabilities.LOADED_CAP, null);
/* 70 */     if (loadedCap != null) {
/* 71 */       synchronized (loadedCap) {
/* 72 */         loadedCap.loaded = false;
/*    */       } 
/*    */     }
/*    */   }
/*    */   
/*    */   public WorldServer getWorldServer() {
/* 78 */     return this.worldServer;
/*    */   }
/*    */   
/*    */   public WorldDataReader getWorldDataReader() {
/* 82 */     return this.worldDataReader;
/*    */   }
/*    */   
/*    */   public File getWorldDir() {
/* 86 */     return this.worldDir;
/*    */   }
/*    */ }


/* Location:              C:\Users\ioanm\Downloads\XaerosWorldMap_1.7.3_Forge_1.12.jar!\xaero\map\file\worldsave\WorldDataHandler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */