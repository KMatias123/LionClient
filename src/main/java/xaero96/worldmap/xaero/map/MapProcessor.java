/*      */ package xaero.map;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.lang.reflect.Field;
/*      */ import java.nio.ByteBuffer;
/*      */ import java.nio.IntBuffer;
/*      */ import java.nio.file.Files;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Hashtable;
/*      */ import java.util.Queue;
/*      */ import java.util.concurrent.Callable;
/*      */ import java.util.concurrent.Executors;
/*      */ import java.util.concurrent.FutureTask;
/*      */ import net.minecraft.client.Minecraft;
/*      */ import net.minecraft.client.entity.EntityPlayerSP;
/*      */ import net.minecraft.client.renderer.GlStateManager;
/*      */ import net.minecraft.client.resources.I18n;
/*      */ import net.minecraft.entity.player.EntityPlayer;
/*      */ import net.minecraft.util.math.BlockPos;
/*      */ import net.minecraft.world.World;
/*      */ import net.minecraft.world.WorldProvider;
/*      */ import net.minecraft.world.chunk.Chunk;
/*      */ import org.apache.commons.lang3.ArrayUtils;
/*      */ import org.lwjgl.BufferUtils;
/*      */ import org.lwjgl.opengl.GL11;
/*      */ import org.lwjgl.opengl.GL15;
/*      */ import xaero.deallocator.ByteBufferDeallocator;
/*      */ import xaero.map.biome.MapBiomes;
/*      */ import xaero.map.controls.ControlsHandler;
/*      */ import xaero.map.file.MapSaveLoad;
/*      */ import xaero.map.file.RegionDetection;
/*      */ import xaero.map.file.worldsave.WorldDataHandler;
/*      */ import xaero.map.graphics.TextureUploader;
/*      */ import xaero.map.mods.SupportMods;
/*      */ import xaero.map.pool.MapTilePool;
/*      */ import xaero.map.region.MapRegion;
/*      */ import xaero.map.region.MapTile;
/*      */ import xaero.map.region.MapTileChunk;
/*      */ import xaero.map.region.OverlayManager;
/*      */ import xaero.map.world.MapDimension;
/*      */ import xaero.map.world.MapWorld;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class MapProcessor
/*      */   implements Runnable
/*      */ {
/*      */   public static MapProcessor instance;
/*      */   private Throwable crashedBy;
/*      */   private MapSaveLoad mapSaveLoad;
/*      */   private MapWriter mapWriter;
/*      */   private MapLimiter mapLimiter;
/*      */   private WorldDataHandler worldDataHandler;
/*      */   private ByteBufferDeallocator bufferDeallocator;
/*      */   private TextureUploader textureUploader;
/*      */   private World world;
/*      */   private World newWorld;
/*      */   public final Object mainStuffSync;
/*      */   public World mainWorld;
/*      */   public double mainPlayerX;
/*      */   public double mainPlayerY;
/*      */   public double mainPlayerZ;
/*      */   private boolean mainWorldUnloaded;
/*   79 */   private ArrayList<Double[]> footprints = (ArrayList)new ArrayList<>();
/*      */   
/*      */   private int footprintsTimer;
/*      */   
/*      */   private MapWorld mapWorld;
/*      */   
/*      */   private MapWorld lastNonNullWorld;
/*      */   
/*      */   private String currentWorldString;
/*      */   
/*      */   private String currentWorldStringNoDim;
/*      */   
/*      */   private Hashtable<Integer, BlockPos> usedSpawn;
/*      */   
/*      */   private Hashtable<Integer, BlockPos> latestSpawn;
/*      */   
/*      */   private MapWorld newMapWorld;
/*      */   
/*      */   private boolean caveStartDetermined;
/*      */   
/*      */   private int caveStart;
/*  100 */   public final Object renderThreadPauseSync = new Object();
/*      */   
/*      */   private int pauseUploading;
/*      */   private int pauseRendering;
/*      */   private int pauseWriting;
/*  105 */   public final Object processorThreadPauseSync = new Object();
/*      */   
/*      */   private int pauseProcessing;
/*  108 */   public final Object loadingSync = new Object();
/*      */   
/*  110 */   public final Object uiSync = new Object();
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean waitingForWorldUpdate;
/*      */ 
/*      */   
/*  117 */   private ArrayList<MapRegion> toProcess = new ArrayList<>();
/*  118 */   private ArrayList<MapRegion> toRefresh = new ArrayList<>();
/*  119 */   private ArrayList<Integer> texturesToDelete = new ArrayList<>();
/*  120 */   private ArrayList<Integer> buffersToDelete = new ArrayList<>();
/*  121 */   private ArrayList<Runnable> tasks = new ArrayList<>();
/*      */ 
/*      */   
/*      */   private static final int SPAWNPOINT_TIMEOUT = 3000;
/*      */ 
/*      */   
/*      */   private BlockPos spawnToRestore;
/*      */   
/*  129 */   private long mainWorldChangedTime = -1L;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private MapTilePool tilePool;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean currentMapNeedsDeletion;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private OverlayManager overlayManager;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private MapBiomes mapBiomes;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private long renderStartTime;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Field scheduledTasksField;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Callable<Object> renderStartTimeUpdater;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String[] dimensionsToIgnore;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Field selectedField;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void run() {
/*      */     try {
/*  188 */       while (this.crashedBy == null)
/*      */       {
/*  190 */         synchronized (this.processorThreadPauseSync) {
/*  191 */           if (!isProcessingPaused()) {
/*      */             
/*  193 */             updateWorld();
/*  194 */             if (this.world != null) {
/*  195 */               updateCaveStart(this.mainPlayerX, this.mainPlayerZ, this.world);
/*      */               
/*  197 */               updateFootprints(this.world, ((Minecraft.func_71410_x()).field_71462_r instanceof xaero.map.gui.GuiMap) ? 1 : 10);
/*      */             } 
/*      */             
/*  200 */             if (this.currentWorldString != null) {
/*  201 */               this.mapLimiter.applyLimit(this.mapWorld.getCurrentDimension().getMapRegionsList());
/*  202 */               for (int i = 0; i < this.toProcess.size(); i++) {
/*  203 */                 MapRegion region = this.toProcess.get(i);
/*  204 */                 this.mapSaveLoad.updateSave(region);
/*      */               } 
/*      */             } 
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
/*      */ 
/*      */ 
/*      */             
/*  230 */             this.mapSaveLoad.run(this.world);
/*  231 */             handleRefresh(this.world);
/*      */             
/*  233 */             while (!this.tasks.isEmpty()) {
/*      */               Runnable task;
/*  235 */               synchronized (this.tasks) {
/*  236 */                 if (this.tasks.isEmpty())
/*      */                   break; 
/*  238 */                 task = this.tasks.remove(0);
/*      */               } 
/*  240 */               task.run();
/*      */             } 
/*      */           } 
/*      */         } 
/*      */         try {
/*  245 */           Thread.sleep((this.world == null || (Minecraft.func_71410_x()).field_71462_r instanceof xaero.map.gui.GuiMap) ? 10L : 1000L);
/*  246 */         } catch (InterruptedException interruptedException) {}
/*      */       }
/*      */     
/*  249 */     } catch (Throwable e) {
/*  250 */       setCrashedBy(e);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void onRenderProcess(Minecraft mc) throws RuntimeException {
/*  255 */     this.mapWriter.onRender();
/*      */     try {
/*  257 */       synchronized (this.renderThreadPauseSync) {
/*  258 */         if (this.pauseUploading == 0 && this.mapWorld != null) {
/*  259 */           long totalTime; while (GL11.glGetError() != 0);
/*  260 */           GlStateManager.func_187425_g(3317, 4);
/*  261 */           GlStateManager.func_187425_g(3316, 0);
/*  262 */           GlStateManager.func_187425_g(3315, 0);
/*  263 */           GlStateManager.func_187425_g(3314, 0);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  279 */           int debugFPS = Minecraft.func_175610_ah();
/*  280 */           long uploadStart = System.nanoTime();
/*  281 */           long passed = uploadStart - this.renderStartTime;
/*  282 */           long time60FPS = 16666666L;
/*      */           
/*  284 */           if (debugFPS <= 0 || (debugFPS > 50 && debugFPS < 65)) {
/*  285 */             totalTime = time60FPS;
/*      */           } else {
/*  287 */             totalTime = (1000000000 / debugFPS);
/*  288 */           }  long timeAvailable = Math.max(3000000L, totalTime - passed);
/*  289 */           long uploadUntil = uploadStart + timeAvailable / 4L;
/*  290 */           long gpuLimit = ((Minecraft.func_71410_x()).field_71462_r instanceof xaero.map.gui.GuiMap) ? Math.max(1000000L, totalTime * 5L / 12L) : Math.min(totalTime / 5L, timeAvailable);
/*      */           
/*  292 */           for (int i = 0; i < this.toProcess.size() && System.nanoTime() < uploadUntil; i++) {
/*      */             MapRegion region;
/*  294 */             synchronized (this.toProcess) {
/*  295 */               if (i >= this.toProcess.size())
/*      */                 break; 
/*  297 */               region = this.toProcess.get(i);
/*      */             } 
/*      */             
/*  300 */             if (region != null && region.getWorld() != null && region.getWorld().equals(this.currentWorldString))
/*      */             {
/*  302 */               synchronized (region) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */                 
/*  312 */                 if (region.getLoadState() < 4) {
/*      */                   
/*  314 */                   boolean allCleaned = true;
/*  315 */                   boolean allCached = true;
/*  316 */                   boolean allUploaded = true;
/*  317 */                   boolean isEmpty = true;
/*      */                   
/*  319 */                   for (int x = 0; x < 8; x++) {
/*  320 */                     for (int z = 0; z < 8; z++) {
/*  321 */                       MapTileChunk chunk = region.getChunk(x, z);
/*  322 */                       if (chunk != null) {
/*  323 */                         isEmpty = false;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */                         
/*  330 */                         if (chunk.getLoadState() >= 2) {
/*  331 */                           if (gpuLimit > 0L) {
/*  332 */                             if (chunk.getToUpdateBuffers() && !isWritingPaused())
/*  333 */                               synchronized (region.writerThreadPauseSync) {
/*  334 */                                 if (!region.isWritingPaused()) {
/*  335 */                                   chunk.updateBuffers(this.mainWorld);
/*      */                                 }
/*      */                               }  
/*  338 */                             synchronized (chunk) {
/*  339 */                               if (!chunk.isUpdatingBuffers() && 
/*  340 */                                 chunk.shouldUpload()) {
/*  341 */                                 if (chunk.getTimer() == 0)
/*  342 */                                 { long estimatedGPUTime = chunk.uploadBuffer(this.textureUploader);
/*  343 */                                   gpuLimit -= estimatedGPUTime;
/*  344 */                                   if (!chunk.shouldDownloadFromPBO()) {
/*  345 */                                     chunk.setToUpload(false);
/*  346 */                                     if (chunk.getColorBufferFormat() == -1) {
/*  347 */                                       chunk.deleteBuffers();
/*      */                                     } else {
/*  349 */                                       chunk.setCachePrepared(true);
/*      */                                     } 
/*      */                                   }  }
/*  352 */                                 else { chunk.decTimer(); }
/*      */                               
/*      */                               }
/*      */                             } 
/*      */                           } 
/*  357 */                           if (region.getLoadState() >= 2 && 
/*  358 */                             !region.isBeingWritten() && (region.getLastVisited() == 0L || region.getTimeSinceVisit() > 1000L) && !region.isRefreshing() && !chunk.getToUpdateBuffers() && chunk.getLoadState() != 3) {
/*  359 */                             region.setLoadState((byte)3);
/*  360 */                             chunk.setLoadState((byte)3);
/*  361 */                             chunk.clean();
/*      */                           } 
/*      */                         } 
/*      */                         
/*  365 */                         if (chunk.getLoadState() != 3)
/*  366 */                           allCleaned = false; 
/*  367 */                         if (!chunk.isCachePrepared())
/*  368 */                           allCached = false; 
/*  369 */                         if (chunk.shouldUpload() || chunk.getToUpdateBuffers())
/*  370 */                           allUploaded = false; 
/*      */                       } 
/*      */                     } 
/*  373 */                   }  allUploaded = (allUploaded && region.getLoadState() >= 2);
/*  374 */                   allCached = (allCached && allUploaded && !isEmpty);
/*  375 */                   if ((!region.shouldCache() || !region.recacheHasBeenRequested()) && region.getLoadState() == 3 && allCleaned && allUploaded) {
/*  376 */                     region.setLoadState((byte)4);
/*  377 */                     region.destroyBufferUpdateObjects();
/*  378 */                     region.deleteGLBuffers();
/*  379 */                     synchronized (this.toProcess) {
/*  380 */                       if (i < this.toProcess.size()) {
/*  381 */                         this.toProcess.remove(i);
/*      */                         
/*  383 */                         i--;
/*      */                       } 
/*      */                     } 
/*      */ 
/*      */                     
/*  388 */                     if (WorldMap.settings.debug)
/*  389 */                       System.out.println("Region freed: " + region + " " + region.getWorld() + ", " + region.getRegionX() + "_" + region.getRegionZ() + " " + this.mapWriter.getUpdateCounter() + " " + this.currentWorldString); 
/*      */                   } 
/*  391 */                   if (allCached && !region.isAllCachePrepared())
/*  392 */                     region.setAllCachePrepared(true); 
/*  393 */                   if (region.shouldCache() && region.isAllCachePrepared() && !region.isRefreshing()) {
/*  394 */                     instance.getMapSaveLoad().requestCache(region);
/*      */                   }
/*      */                 } 
/*      */               } 
/*      */             }
/*      */           } 
/*  400 */           this.textureUploader.uploadTextures();
/*      */         } 
/*      */       } 
/*  403 */       this.mapLimiter.updateAvailableVRAM();
/*  404 */       if (!this.texturesToDelete.isEmpty()) {
/*  405 */         synchronized (this.texturesToDelete) {
/*  406 */           int[] obs = ArrayUtils.toPrimitive(this.texturesToDelete.<Integer>toArray(new Integer[0]));
/*  407 */           ByteBuffer buffer = BufferUtils.createByteBuffer(obs.length * 4);
/*  408 */           IntBuffer bufferIntView = buffer.asIntBuffer();
/*  409 */           bufferIntView.put(obs);
/*  410 */           bufferIntView.flip();
/*  411 */           GL11.glDeleteTextures(bufferIntView);
/*  412 */           this.bufferDeallocator.deallocate(buffer, WorldMap.settings.debug);
/*  413 */           this.texturesToDelete.clear();
/*      */         } 
/*      */       }
/*  416 */       if (!this.buffersToDelete.isEmpty()) {
/*  417 */         synchronized (this.buffersToDelete) {
/*  418 */           int[] obs = ArrayUtils.toPrimitive(this.buffersToDelete.<Integer>toArray(new Integer[0]));
/*  419 */           ByteBuffer buffer = BufferUtils.createByteBuffer(obs.length * 4);
/*  420 */           IntBuffer bufferIntView = buffer.asIntBuffer();
/*  421 */           bufferIntView.put(obs);
/*  422 */           bufferIntView.flip();
/*  423 */           GL15.glDeleteBuffers(bufferIntView);
/*  424 */           this.bufferDeallocator.deallocate(buffer, WorldMap.settings.debug);
/*  425 */           this.buffersToDelete.clear();
/*      */         }
/*      */       
/*      */       }
/*      */     }
/*  430 */     catch (Throwable e) {
/*  431 */       setCrashedBy(e);
/*      */     } 
/*  433 */     checkForCrashes();
/*      */   }
/*      */   
/*  436 */   public MapProcessor(MapSaveLoad mapSaveLoad, MapWriter mapWriter, MapLimiter mapLimiter, ByteBufferDeallocator bufferDeallocator, MapTilePool tilePool, OverlayManager overlayManager, TextureUploader textureUploader, WorldDataHandler worldDataHandler, MapBiomes mapBiomes) throws NoSuchFieldException { this.dimensionsToIgnore = new String[] { "FZHammer" };
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
/*      */     
/*  458 */     this.selectedField = null; this.mapSaveLoad = mapSaveLoad; this.mapWriter = mapWriter; this.mapLimiter = mapLimiter; this.bufferDeallocator = bufferDeallocator; this.tilePool = tilePool; this.overlayManager = overlayManager; this.textureUploader = textureUploader; this.worldDataHandler = worldDataHandler; this.mapBiomes = mapBiomes; instance = this; try { this.scheduledTasksField = Minecraft.class.getDeclaredField("field_152351_aB"); } catch (NoSuchFieldException e) { try { this.scheduledTasksField = Minecraft.class.getDeclaredField("scheduledTasks"); } catch (NoSuchFieldException nsfe) { throw nsfe; } catch (SecurityException se) { throw se; }  } catch (SecurityException se) { throw se; }  Runnable renderStartTimeUpdaterRunnable = new Runnable() {
/*      */         public void run() { MapProcessor.this.updateRenderStartTime(); }
/*      */       }; this.renderStartTimeUpdater = Executors.callable(renderStartTimeUpdaterRunnable); this.mainStuffSync = new Object(); this.caveStart = -1; this.latestSpawn = new Hashtable<>(); this.usedSpawn = new Hashtable<>(); } public boolean ignoreWorld(World world) { for (int i = 0; i < this.dimensionsToIgnore.length; i++) { if (this.dimensionsToIgnore[i].equals(world.field_73011_w.func_186058_p().func_186065_b()))
/*  461 */         return true;  }  return false; } public synchronized void changeWorld(World world, boolean clearSpawns) { pushWriterPause();
/*  462 */     synchronized (this.loadingSync) {
/*  463 */       this.waitingForWorldUpdate = true;
/*      */     } 
/*  465 */     this.newWorld = world;
/*  466 */     if (world == null) {
/*  467 */       if (clearSpawns) {
/*  468 */         this.latestSpawn.clear();
/*  469 */         this.usedSpawn.clear();
/*      */       } 
/*  471 */       this.newMapWorld = null;
/*      */     } else {
/*  473 */       Minecraft mc = Minecraft.func_71410_x();
/*  474 */       int dimId = world.field_73011_w.getDimension();
/*  475 */       String newMainId = getNewMainId(mc, dimId);
/*  476 */       BlockPos updatedSpawn = this.latestSpawn.get(Integer.valueOf(dimId));
/*  477 */       if (this.mapWorld == null || !newMainId.equals(this.mapWorld.getMainIdNoDim())) {
/*  478 */         if (this.lastNonNullWorld != null && this.lastNonNullWorld.getMainIdNoDim().equals(newMainId)) {
/*  479 */           this.newMapWorld = this.lastNonNullWorld;
/*      */         } else {
/*  481 */           this.newMapWorld = new MapWorld(newMainId);
/*  482 */           this.newMapWorld.loadConfig();
/*      */         } 
/*      */       } else {
/*  485 */         this.newMapWorld = this.mapWorld;
/*  486 */       }  MapDimension mapDimension = this.newMapWorld.getDimension(dimId);
/*  487 */       if (mapDimension == null) {
/*  488 */         mapDimension = this.newMapWorld.createDimension(world, dimId);
/*      */       }
/*  490 */       this.newMapWorld.setFutureDimensionId(dimId);
/*  491 */       mapDimension.resetCustomMultiworldUnsynced();
/*  492 */       mapDimension.updateFutureAutomaticUnsynced(Minecraft.func_71410_x(), updatedSpawn);
/*      */     } 
/*  494 */     popWriterPause(); }
/*      */   public String getDimensionName(int id) { String name = "null"; if (id != 0)
/*      */       name = "DIM" + id;  return name; }
/*      */   public String getDimensionLegacyName(WorldProvider worldProvider) { String legacyName = worldProvider.getSaveFolder(); if (legacyName != null)
/*  498 */       legacyName = legacyName.replaceAll("_", "^us^");  return legacyName; } private String getNewMainId(Minecraft mc, int dimId) { String result = null;
/*  499 */     if (mc.func_71401_C() != null) {
/*  500 */       result = mc.func_71401_C().func_71270_I().replaceAll("_", "^us^") + "_%DIMENSION%";
/*  501 */       if (isWorldMultiplayer(isWorldRealms(result), result)) {
/*  502 */         result = "^e^" + result;
/*      */       }
/*  504 */     } else if (mc.func_147104_D() != null) {
/*  505 */       String serverIP = WorldMap.settings.differentiateByServerAddress ? (mc.func_147104_D()).field_78845_b : "Any Address";
/*  506 */       if (serverIP.contains(":"))
/*  507 */         serverIP = serverIP.substring(0, serverIP.indexOf(":")); 
/*  508 */       result = "Multiplayer_" + serverIP.replaceAll(":", "§") + "_%DIMENSION%";
/*  509 */     } else if (mc.func_181540_al() && WorldMap.events.getLatestRealm() != null) {
/*  510 */       result = "Realms_" + (WorldMap.events.getLatestRealm()).ownerUUID + "." + (WorldMap.events.getLatestRealm()).id + "_%DIMENSION%";
/*      */     }
/*      */     else {
/*      */       
/*  514 */       result = "Multiplayer_Unknown_%DIMENSION%";
/*  515 */       this.latestSpawn.remove(Integer.valueOf(dimId));
/*      */     } 
/*      */     
/*  518 */     return result; }
/*      */ 
/*      */   
/*      */   public synchronized void toggleMultiworldType() {
/*  522 */     if (this.mapWorld != null && !this.waitingForWorldUpdate && this.mapWorld.isMultiplayer()) {
/*  523 */       this.mapWorld.toggleMultiworldTypeUnsynced();
/*      */     }
/*      */   }
/*      */   
/*      */   public synchronized void quickConfirmMultiworld() {
/*  528 */     if (this.mapWorld != null && this.mapWorld.getCurrentDimension().hasConfirmedMultiworld()) {
/*  529 */       confirmMultiworld();
/*      */     }
/*      */   }
/*      */   
/*      */   public synchronized void confirmMultiworld() {
/*  534 */     if (this.mapWorld != null && this.mapWorld.isMultiplayer() && this.mainWorld != null && this.mainWorld.field_73011_w.getDimension() == this.mapWorld.getCurrentDimensionId()) {
/*  535 */       this.mapWorld.confirmMultiworldTypeUnsynced();
/*  536 */       this.mapWorld.getCurrentDimension().confirmMultiworldUnsynced();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void setMultiworld(MapDimension dimToCompare, String customMW) {
/*  542 */     if (this.mapWorld != null && this.mapWorld.isMultiplayer() && dimToCompare == this.mapWorld.getCurrentDimension()) {
/*  543 */       this.mapWorld.getCurrentDimension().setMultiworldUnsynced(customMW);
/*      */     }
/*      */   }
/*      */   
/*      */   public String getCrosshairMessage() {
/*  548 */     synchronized (this.uiSync) {
/*  549 */       if (this.mapWorld != null && 
/*  550 */         !(this.mapWorld.getCurrentDimension()).futureMultiworldWritable && this.mainWorld.field_73011_w.getDimension() == this.mapWorld.getCurrentDimensionId()) {
/*  551 */         String selectedMWName = this.mapWorld.getCurrentDimension().getMultiworldName(this.mapWorld.getCurrentDimension().getFutureMultiworldUnsynced());
/*  552 */         String message = "§2(" + ControlsHandler.keyOpenMap.getDisplayName().toUpperCase() + ")§r " + I18n.func_135052_a("gui.xaero_map_unconfirmed", new Object[0]);
/*  553 */         if (this.mapWorld.getCurrentDimension().hasConfirmedMultiworld())
/*  554 */           message = message + " §2" + ControlsHandler.keyQuickConfirm.getDisplayName().toUpperCase() + "§r for map \"" + selectedMWName + "\""; 
/*  555 */         return message;
/*      */       } 
/*      */     } 
/*      */     
/*  559 */     return null;
/*      */   }
/*      */   
/*      */   private synchronized void checkForWorldUpdate() {
/*  563 */     if (this.mainWorld != null) {
/*  564 */       int dimId = this.mainWorld.field_73011_w.getDimension();
/*  565 */       BlockPos dimSpawn = this.latestSpawn.get(Integer.valueOf(dimId));
/*  566 */       if (dimSpawn != null) {
/*  567 */         boolean spawnChanged = !dimSpawn.equals(this.usedSpawn.get(Integer.valueOf(dimId)));
/*  568 */         if (spawnChanged && this.mapWorld != null) {
/*  569 */           MapDimension mapDimension = this.mapWorld.getDimension(dimId);
/*  570 */           if (mapDimension != null)
/*  571 */             mapDimension.updateFutureAutomaticUnsynced(Minecraft.func_71410_x(), dimSpawn); 
/*      */         } 
/*  573 */         if (this.mainWorld != this.world)
/*  574 */           changeWorld(this.mainWorld, true); 
/*  575 */         BlockPos updatedSpawn = this.latestSpawn.get(Integer.valueOf(dimId));
/*  576 */         if (updatedSpawn != null) {
/*  577 */           this.usedSpawn.put(Integer.valueOf(dimId), updatedSpawn);
/*      */         } else {
/*  579 */           this.usedSpawn.remove(Integer.valueOf(dimId));
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */   private void updateWorld() throws IOException {
/*  585 */     updateWorldSynced();
/*  586 */     if (this.mapWorld != null && !this.mapSaveLoad.isRegionDetectionComplete()) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  592 */       this.mapSaveLoad.detectRegions();
/*  593 */       this.mapSaveLoad.setRegionDetectionComplete(true);
/*      */     } 
/*      */   }
/*      */   
/*      */   private synchronized void updateWorldSynced() throws IOException {
/*  598 */     synchronized (this.uiSync) {
/*  599 */       if (this.newMapWorld != this.mapWorld || (this.newMapWorld != null && (this.newMapWorld
/*      */         
/*  601 */         .getFutureDimension() != this.newMapWorld.getCurrentDimension() || 
/*  602 */         !this.newMapWorld.getFutureDimension().getFutureMultiworldUnsynced().equals(this.newMapWorld.getFutureDimension().getCurrentMultiworld())))) {
/*  603 */         String newWorldStringNoDimMainId = (this.newMapWorld == null) ? null : this.newMapWorld.getMainIdNoDim();
/*  604 */         String newWorldStringNoDimMultiworldId = (this.newMapWorld == null) ? null : this.newMapWorld.getFutureMultiworldUnsynced();
/*  605 */         pushRenderPause(true, true);
/*  606 */         pushWriterPause();
/*  607 */         boolean multiplayer = (newWorldStringNoDimMainId != null && isWorldMultiplayer(isWorldRealms(newWorldStringNoDimMainId), newWorldStringNoDimMainId));
/*  608 */         String newWorldStringNoDim = (newWorldStringNoDimMainId == null) ? null : (newWorldStringNoDimMainId + (!multiplayer ? "" : ("_" + newWorldStringNoDimMultiworldId)));
/*  609 */         boolean shouldClearAllDimensions = (this.newMapWorld != null && this.lastNonNullWorld != this.newMapWorld);
/*  610 */         boolean shouldClearNewDimension = (this.newMapWorld != null && !this.newMapWorld.getFutureMultiworldUnsynced().equals(this.newMapWorld.getFutureDimension().getCurrentMultiworld()));
/*  611 */         this.mapSaveLoad.getToSave().clear();
/*  612 */         if (this.lastNonNullWorld != null) {
/*  613 */           MapDimension currentDim = this.lastNonNullWorld.getCurrentDimension();
/*  614 */           MapDimension reqDim = (this.newMapWorld == null) ? null : this.newMapWorld.getFutureDimension();
/*  615 */           boolean shouldFinishCurrentDim = (this.mapWorld != null && !this.currentMapNeedsDeletion);
/*  616 */           boolean currentDimChecked = false;
/*  617 */           if (shouldFinishCurrentDim)
/*  618 */             this.mapSaveLoad.saveAll = true; 
/*  619 */           if (shouldFinishCurrentDim || (shouldClearNewDimension && reqDim == currentDim)) {
/*  620 */             for (MapRegion region : currentDim.getMapRegionsList()) {
/*  621 */               if (shouldFinishCurrentDim) {
/*  622 */                 if (region.recacheHasBeenRequested() && region.getCacheFile() != null) {
/*      */ 
/*      */                   
/*  625 */                   Files.deleteIfExists(region.getCacheFile().toPath());
/*  626 */                   if (WorldMap.settings.debug)
/*  627 */                     System.out.println(String.format("Deleting cache for region %s because it might be outdated.", new Object[] { region })); 
/*      */                 } 
/*  629 */                 region.setReloadHasBeenRequested(false, "world/dim change");
/*  630 */                 if (region.getLoadState() == 2)
/*      */                 
/*      */                 { 
/*      */ 
/*      */                   
/*  635 */                   if (region.isBeingWritten()) {
/*  636 */                     this.mapSaveLoad.getToSave().add(region);
/*      */                   } else {
/*  638 */                     region.clearRegion();
/*      */                   }  }
/*  640 */                 else { region.setBeingWritten(false);
/*  641 */                   if (region.isRefreshing())
/*  642 */                     throw new RuntimeException("Detected non-loadstate 2 region with refreshing value being true.");  }
/*      */               
/*      */               } 
/*  645 */               if (shouldClearAllDimensions || (shouldClearNewDimension && reqDim == currentDim)) {
/*  646 */                 region.deleteTexturesAndBuffers();
/*      */               }
/*      */             } 
/*      */             
/*  650 */             currentDimChecked = true;
/*      */           } 
/*  652 */           if (reqDim != currentDim && shouldClearNewDimension) {
/*  653 */             for (MapRegion region : reqDim.getMapRegionsList()) {
/*  654 */               region.deleteTexturesAndBuffers();
/*      */             }
/*      */           }
/*      */           
/*  658 */           if (shouldClearAllDimensions) {
/*  659 */             for (MapDimension dim : this.lastNonNullWorld.getDimensions().values()) {
/*  660 */               if (!currentDimChecked || dim != currentDim) {
/*  661 */                 for (MapRegion region : dim.getMapRegionsList()) {
/*  662 */                   region.deleteTexturesAndBuffers();
/*      */                 }
/*      */               }
/*      */             } 
/*      */           }
/*  667 */           if (this.currentMapNeedsDeletion)
/*  668 */             this.lastNonNullWorld.getCurrentDimension().deleteMultiworldMapDataUnsynced(this.lastNonNullWorld.getCurrentDimension().getCurrentMultiworld()); 
/*      */         } 
/*  670 */         this.currentMapNeedsDeletion = false;
/*  671 */         if (shouldClearAllDimensions) {
/*      */           
/*  673 */           if (this.lastNonNullWorld != null)
/*  674 */             for (MapDimension dim : this.lastNonNullWorld.getDimensions().values()) {
/*  675 */               dim.clearLists();
/*      */             } 
/*  677 */           if (WorldMap.settings.debug)
/*  678 */             System.out.println("All map data cleared!"); 
/*  679 */         } else if (shouldClearNewDimension) {
/*  680 */           (this.newMapWorld.getFutureDimension()).regionsToCache.clear();
/*  681 */           this.newMapWorld.getFutureDimension().clearLists();
/*  682 */           if (WorldMap.settings.debug) {
/*  683 */             System.out.println("Dimension map data cleared!");
/*      */           }
/*      */         } 
/*  686 */         this.currentWorldStringNoDim = newWorldStringNoDim;
/*  687 */         if (WorldMap.settings.debug) {
/*  688 */           System.out.println("World changed!");
/*      */         }
/*  690 */         this.mapWorld = this.newMapWorld;
/*  691 */         if (this.newMapWorld != null) {
/*  692 */           this.lastNonNullWorld = this.newMapWorld;
/*  693 */           this.mapWorld.switchToFutureUnsynced();
/*      */         } 
/*  695 */         this.caveStartDetermined = false;
/*  696 */         this.caveStart = -1;
/*  697 */         String dimensionName = (this.newMapWorld == null) ? null : getDimensionName(this.newMapWorld.getFutureDimensionId());
/*  698 */         if (this.currentWorldStringNoDim != null) {
/*  699 */           this.currentWorldString = this.currentWorldStringNoDim.replace("%DIMENSION%", dimensionName);
/*      */         } else {
/*  701 */           this.currentWorldString = null;
/*  702 */         }  this.footprints.clear();
/*  703 */         this.mapSaveLoad.clearToLoad();
/*  704 */         this.mapSaveLoad.clearLoadRequests();
/*  705 */         this.mapSaveLoad.setNextToLoadByViewing(null);
/*  706 */         clearToRefresh();
/*  707 */         this.toProcess.clear();
/*  708 */         if (this.mapWorld != null)
/*  709 */           for (MapRegion region : this.mapWorld.getCurrentDimension().getMapRegionsList()) {
/*  710 */             if (region.getLoadState() != 4 && region.getLoadState() != 0)
/*  711 */               this.toProcess.add(region); 
/*  712 */           }   this.mapSaveLoad.updateCacheFolderList(this.mapSaveLoad.getSubFolder(this.currentWorldString));
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  717 */         this.mapWriter.resetPosition();
/*  718 */         this.world = this.newWorld;
/*  719 */         if (WorldMap.settings.debug)
/*  720 */           System.out.println("World/dimension changed to: " + this.currentWorldString); 
/*  721 */         this.worldDataHandler.prepareSingleplayer(this.world, this);
/*  722 */         if (this.worldDataHandler.getWorldDir() == null && this.currentWorldString != null && !isWorldMultiplayer(isWorldRealms(this.currentWorldString), this.currentWorldString))
/*  723 */           this.currentWorldString = this.currentWorldStringNoDim = null; 
/*  724 */         boolean shouldDetect = (this.mapWorld != null && this.mapWorld.getCurrentDimension().getDetectedRegions() == null);
/*      */         
/*  726 */         this.mapSaveLoad.setRegionDetectionComplete(!shouldDetect);
/*  727 */         popRenderPause(true, true);
/*  728 */         popWriterPause();
/*  729 */       } else if (this.newWorld != this.world) {
/*  730 */         pushWriterPause();
/*  731 */         this.world = this.newWorld;
/*  732 */         popWriterPause();
/*      */       } 
/*  734 */       if (this.mapWorld != null) {
/*  735 */         this.mapWorld.getCurrentDimension().switchToFutureMultiworldWritableValueUnsynced();
/*  736 */         this.mapWorld.switchToFutureMultiworldTypeUnsynced();
/*      */       } 
/*  738 */       this.waitingForWorldUpdate = false;
/*      */     } 
/*      */   }
/*      */   
/*      */   public void updateFootprints(World world, int step) {
/*  743 */     if (WorldMap.settings.footsteps) {
/*  744 */       if (this.footprintsTimer > 0) {
/*  745 */         this.footprintsTimer -= step;
/*      */       } else {
/*  747 */         Double[] coords = { Double.valueOf(this.mainPlayerX), Double.valueOf(this.mainPlayerZ) };
/*  748 */         this.footprints.add(coords);
/*  749 */         if (this.footprints.size() > 32)
/*  750 */           this.footprints.remove(0); 
/*  751 */         this.footprintsTimer = 20;
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   public void addToRefresh(MapRegion region) {
/*  757 */     synchronized (this.toRefresh) {
/*  758 */       if (!this.toRefresh.contains(region))
/*  759 */         this.toRefresh.add(0, region); 
/*      */     } 
/*      */   }
/*      */   
/*      */   public void removeToRefresh(MapRegion region) {
/*  764 */     synchronized (this.toRefresh) {
/*  765 */       this.toRefresh.remove(region);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void clearToRefresh() {
/*  770 */     synchronized (this.toRefresh) {
/*  771 */       this.toRefresh.clear();
/*      */     } 
/*      */   }
/*      */   
/*      */   private void handleRefresh(World world) throws RuntimeException {
/*  776 */     synchronized (this.loadingSync) {
/*  777 */       if (!this.waitingForWorldUpdate && !this.toRefresh.isEmpty()) {
/*  778 */         MapRegion region = this.toRefresh.get(0);
/*  779 */         if (region.isRefreshing()) {
/*      */           boolean regionLoaded;
/*  781 */           synchronized (region) {
/*  782 */             regionLoaded = (region.getLoadState() == 2);
/*  783 */             if (regionLoaded) {
/*  784 */               if ((region.hasVersion() && region.getVersion() != WorldMap.globalVersion) || (!region.hasVersion() && region.getInitialVersion() != WorldMap.globalVersion)) {
/*  785 */                 region.setRecacheHasBeenRequested(true, "refresh handle");
/*  786 */                 region.setShouldCache(true, "refresh handle");
/*      */               } 
/*  788 */               region.setVersion(WorldMap.globalVersion);
/*      */             } 
/*      */           } 
/*  791 */           boolean isEmpty = true;
/*  792 */           if (regionLoaded) {
/*  793 */             for (int i = 0; i < 8; i++) {
/*  794 */               for (int j = 0; j < 8; j++) {
/*  795 */                 MapTileChunk chunk = region.getChunk(i, j);
/*  796 */                 if (chunk != null && chunk.getLoadState() == 2) {
/*  797 */                   chunk.setToUpdateBuffers(true);
/*  798 */                   isEmpty = false;
/*      */                 } 
/*      */               } 
/*  801 */             }  if (WorldMap.settings.debug)
/*  802 */               System.out.println("Region refreshed: " + region + " " + region.getRegionX() + "_" + region.getRegionZ() + " " + this.mapWriter.getUpdateCounter()); 
/*      */           } 
/*  804 */           synchronized (region) {
/*  805 */             region.setRefreshing(false);
/*  806 */             if (isEmpty) {
/*  807 */               region.setRecacheHasBeenRequested(false, "refresh handle");
/*  808 */               region.setShouldCache(false, "refresh handle");
/*      */             } 
/*      */           } 
/*      */         } else {
/*  812 */           throw new RuntimeException(String.format("Trying to refresh region %s, which is not marked as being refreshed!", new Object[] { region }));
/*  813 */         }  removeToRefresh(region);
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean regionExists(int x, int z) {
/*  819 */     if (!this.mapSaveLoad.isRegionDetectionComplete())
/*  820 */       return false; 
/*  821 */     Hashtable<Integer, RegionDetection> column = (Hashtable<Integer, RegionDetection>)this.mapWorld.getCurrentDimension().getDetectedRegions().get(Integer.valueOf(x));
/*  822 */     return (column != null && column.containsKey(Integer.valueOf(z)));
/*      */   }
/*      */   
/*      */   public void addRegionDetection(RegionDetection regionDetection) {
/*  826 */     Hashtable<Integer, Hashtable<Integer, RegionDetection>> current = this.mapWorld.getCurrentDimension().getDetectedRegions();
/*  827 */     Hashtable<Integer, RegionDetection> column = current.get(Integer.valueOf(regionDetection.getRegionX()));
/*  828 */     if (column == null)
/*  829 */       current.put(Integer.valueOf(regionDetection.getRegionX()), column = new Hashtable<>()); 
/*  830 */     column.put(Integer.valueOf(regionDetection.getRegionZ()), regionDetection);
/*      */   }
/*      */   
/*      */   public RegionDetection getRegionDetection(int x, int z) {
/*  834 */     Hashtable<Integer, RegionDetection> column = (Hashtable<Integer, RegionDetection>)this.mapWorld.getCurrentDimension().getDetectedRegions().get(Integer.valueOf(x));
/*  835 */     if (column != null) {
/*  836 */       return column.get(Integer.valueOf(z));
/*      */     }
/*  838 */     return null;
/*      */   }
/*      */   
/*      */   private void removeRegionDetection(int x, int z) {
/*  842 */     Hashtable<Integer, Hashtable<Integer, RegionDetection>> current = this.mapWorld.getCurrentDimension().getDetectedRegions();
/*  843 */     Hashtable<Integer, RegionDetection> column = current.get(Integer.valueOf(x));
/*  844 */     if (column != null)
/*  845 */       column.remove(Integer.valueOf(z)); 
/*  846 */     if (column.isEmpty())
/*  847 */       current.remove(Integer.valueOf(x)); 
/*      */   }
/*      */   
/*      */   public void removeMapRegion(MapRegion region) {
/*  851 */     MapDimension regionDim = region.getDim();
/*  852 */     synchronized (regionDim.getMapRegions()) {
/*  853 */       Hashtable<Integer, MapRegion> mapColumn = (Hashtable<Integer, MapRegion>)regionDim.getMapRegions().get(Integer.valueOf(region.getRegionX()));
/*  854 */       if (mapColumn == null)
/*      */         return; 
/*  856 */       mapColumn.remove(Integer.valueOf(region.getRegionZ()));
/*  857 */       regionDim.getMapRegionsList().remove(region);
/*      */     } 
/*      */   }
/*      */   
/*      */   public MapRegion getMapRegion(int regX, int regZ, boolean create) {
/*  862 */     MapDimension mapDimension = this.mapWorld.getCurrentDimension();
/*  863 */     synchronized (mapDimension.getMapRegions()) {
/*  864 */       Hashtable<Integer, MapRegion> mapColumn = (Hashtable<Integer, MapRegion>)this.mapWorld.getCurrentDimension().getMapRegions().get(Integer.valueOf(regX));
/*  865 */       if (mapColumn == null)
/*  866 */         if (create) {
/*  867 */           mapColumn = new Hashtable<>();
/*  868 */           this.mapWorld.getCurrentDimension().getMapRegions().put(Integer.valueOf(regX), mapColumn);
/*      */         } else {
/*  870 */           return null;
/*      */         }  
/*  872 */       MapRegion region = mapColumn.get(Integer.valueOf(regZ));
/*  873 */       if (region == null)
/*  874 */         if (create) {
/*  875 */           region = new MapRegion(this.currentWorldString, mapDimension, regX, regZ);
/*  876 */           RegionDetection regionDetection = getRegionDetection(regX, regZ);
/*  877 */           if (regionDetection != null) {
/*  878 */             regionDetection.transferInfoTo(region);
/*  879 */             removeRegionDetection(regX, regZ);
/*      */           } 
/*  881 */           mapDimension.getMapRegionsList().add(region);
/*  882 */           mapColumn.put(Integer.valueOf(regZ), region);
/*      */         } else {
/*  884 */           return null;
/*      */         }  
/*  886 */       return region;
/*      */     } 
/*      */   }
/*      */   
/*      */   public MapTileChunk getMapChunk(int chunkX, int chunkZ) {
/*  891 */     int regionX = chunkX >> 3;
/*  892 */     int regionZ = chunkZ >> 3;
/*  893 */     MapRegion region = getMapRegion(regionX, regionZ, false);
/*  894 */     if (region == null)
/*  895 */       return null; 
/*  896 */     int localChunkX = chunkX & 0x7;
/*  897 */     int localChunkZ = chunkZ & 0x7;
/*  898 */     return region.getChunk(localChunkX, localChunkZ);
/*      */   }
/*      */   
/*      */   public MapTile getMapTile(int x, int z) {
/*  902 */     MapTileChunk tileChunk = getMapChunk(x >> 2, z >> 2);
/*  903 */     if (tileChunk == null)
/*  904 */       return null; 
/*  905 */     int tileX = x & 0x3;
/*  906 */     int tileZ = z & 0x3;
/*  907 */     return tileChunk.getTile(tileX, tileZ);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void updateWorldSpawn(BlockPos newSpawn, World world) {
/*  913 */     int dimId = world.field_73011_w.getDimension();
/*  914 */     this.latestSpawn.put(Integer.valueOf(dimId), newSpawn);
/*  915 */     if (WorldMap.settings.debug)
/*  916 */       System.out.println("Updated spawn for dimension " + dimId + " " + newSpawn); 
/*  917 */     this.spawnToRestore = newSpawn;
/*  918 */     if (world == this.mainWorld) {
/*  919 */       this.mainWorldChangedTime = -1L;
/*      */       
/*  921 */       if (WorldMap.settings.debug) {
/*  922 */         System.out.println("Done waiting for main spawn.");
/*      */       }
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  930 */     checkForWorldUpdate();
/*      */   }
/*      */   
/*      */   public void onWorldUnload() {
/*  934 */     if (this.mainWorldUnloaded)
/*      */       return; 
/*  936 */     if (WorldMap.settings.debug)
/*  937 */       System.out.println("Changing worlds, pausing the world map..."); 
/*  938 */     this.mainWorldUnloaded = true;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  943 */     this.mainWorldChangedTime = -1L;
/*  944 */     changeWorld(null, true);
/*      */   }
/*      */ 
/*      */   
/*      */   public void onClientTickStart() throws RuntimeException {
/*  949 */     checkForCrashes();
/*  950 */     if (this.mainWorld != null && this.spawnToRestore != null && this.mainWorldChangedTime != -1L && System.currentTimeMillis() - this.mainWorldChangedTime >= 3000L) {
/*  951 */       if (WorldMap.settings.debug)
/*  952 */         System.out.println("SPAWN SET TIME OUT"); 
/*  953 */       updateWorldSpawn(this.spawnToRestore, this.mainWorld);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void checkForCrashes() throws RuntimeException {
/*  958 */     if (this.crashedBy != null)
/*  959 */       throw new RuntimeException("Xaero's World Map has crashed! Please contact the author at planetminecraft.com/member/xaero96 or minecraftforum.net/members/xaero96", this.crashedBy); 
/*      */   }
/*      */   
/*      */   private void updateRenderStartTime() {
/*  963 */     if (this.renderStartTime == -1L)
/*      */     {
/*  965 */       this.renderStartTime = System.nanoTime();
/*      */     }
/*      */   }
/*      */   
/*      */   public void pushWriterPause() {
/*  970 */     synchronized (this.renderThreadPauseSync) {
/*  971 */       this.pauseWriting++;
/*      */     } 
/*      */   }
/*      */   
/*      */   public void popWriterPause() {
/*  976 */     synchronized (this.renderThreadPauseSync) {
/*  977 */       this.pauseWriting--;
/*      */     } 
/*      */   }
/*      */   
/*      */   public void pushRenderPause(boolean rendering, boolean uploading) {
/*  982 */     synchronized (this.renderThreadPauseSync) {
/*  983 */       if (rendering)
/*  984 */         this.pauseRendering++; 
/*  985 */       if (uploading)
/*  986 */         this.pauseUploading++; 
/*      */     } 
/*      */   }
/*      */   
/*      */   public void popRenderPause(boolean rendering, boolean uploading) {
/*  991 */     synchronized (this.renderThreadPauseSync) {
/*  992 */       if (rendering)
/*  993 */         this.pauseRendering--; 
/*  994 */       if (uploading)
/*  995 */         this.pauseUploading--; 
/*      */     } 
/*      */   }
/*      */   
/*      */   public void pushProcessorPause() {
/* 1000 */     synchronized (this.processorThreadPauseSync) {
/* 1001 */       this.pauseProcessing++;
/*      */     } 
/*      */   }
/*      */   
/*      */   public void popProcessorPause() {
/* 1006 */     synchronized (this.processorThreadPauseSync) {
/* 1007 */       this.pauseProcessing--;
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean isWritingPaused() {
/* 1012 */     return (this.pauseWriting > 0);
/*      */   }
/*      */   
/*      */   public boolean isRenderingPaused() {
/* 1016 */     return (this.pauseRendering > 0);
/*      */   }
/*      */   
/*      */   public boolean isUploadingPaused() {
/* 1020 */     return (this.pauseUploading > 0);
/*      */   }
/*      */   
/*      */   public boolean isProcessingPaused() {
/* 1024 */     return (this.pauseProcessing > 0);
/*      */   }
/*      */   
/*      */   public ArrayList<MapRegion> getToProcess() {
/* 1028 */     return this.toProcess;
/*      */   }
/*      */   
/*      */   public void addToProcess(MapRegion region) {
/* 1032 */     synchronized (this.toProcess) {
/* 1033 */       this.toProcess.add(region);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void removeToProcess(MapRegion region) {
/* 1038 */     synchronized (this.toProcess) {
/* 1039 */       this.toProcess.remove(region);
/*      */     } 
/*      */   }
/*      */   
/*      */   public MapSaveLoad getMapSaveLoad() {
/* 1044 */     return this.mapSaveLoad;
/*      */   }
/*      */   
/*      */   public World getWorld() {
/* 1048 */     return this.world;
/*      */   }
/*      */   
/*      */   public Throwable getCrashedBy() {
/* 1052 */     return this.crashedBy;
/*      */   }
/*      */   
/*      */   public void setCrashedBy(Throwable crashedBy) {
/* 1056 */     if (this.crashedBy == null)
/* 1057 */       this.crashedBy = crashedBy; 
/*      */   }
/*      */   
/*      */   public String getCurrentWorldString() {
/* 1061 */     return this.currentWorldString;
/*      */   }
/*      */   
/*      */   public MapWriter getMapWriter() {
/* 1065 */     return this.mapWriter;
/*      */   }
/*      */   
/*      */   public void addTask(Runnable task) {
/* 1069 */     synchronized (this.tasks) {
/* 1070 */       this.tasks.add(task);
/*      */     } 
/*      */   }
/*      */   
/*      */   public MapLimiter getMapLimiter() {
/* 1075 */     return this.mapLimiter;
/*      */   }
/*      */   
/*      */   public ArrayList<Double[]> getFootprints() {
/* 1079 */     return this.footprints;
/*      */   }
/*      */   
/*      */   public void requestTextureDeletion(int texture) {
/* 1083 */     synchronized (this.texturesToDelete) {
/* 1084 */       this.texturesToDelete.add(Integer.valueOf(texture));
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ByteBufferDeallocator getBufferDeallocator() {
/* 1093 */     return this.bufferDeallocator;
/*      */   }
/*      */   
/*      */   public MapTilePool getTilePool() {
/* 1097 */     return this.tilePool;
/*      */   }
/*      */   
/*      */   public OverlayManager getOverlayManager() {
/* 1101 */     return this.overlayManager;
/*      */   }
/*      */   
/*      */   public int getGlobalVersion() {
/* 1105 */     return WorldMap.globalVersion;
/*      */   }
/*      */   
/*      */   public void setGlobalVersion(int globalVersion) {
/* 1109 */     WorldMap.globalVersion = globalVersion;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void incrementGlobalVersion() {
/* 1115 */     instance.addTask(new Runnable()
/*      */         {
/*      */           
/*      */           public void run()
/*      */           {
/* 1120 */             MapProcessor.instance.setGlobalVersion(MapProcessor.instance.getGlobalVersion() + 1);
/* 1121 */             if (SupportMods.minimap())
/* 1122 */               WorldMap.waypointSymbolCreator.resetChars(); 
/* 1123 */             MapProcessor.instance.getMapSaveLoad().updateCacheFolderList(MapProcessor.instance.getMapSaveLoad().getSubFolder(MapProcessor.instance.getCurrentWorldString()));
/* 1124 */             if (WorldMap.settings.debug)
/* 1125 */               System.out.println("Version incremented to " + MapProcessor.instance.getGlobalVersion()); 
/*      */             try {
/* 1127 */               WorldMap.settings.saveSettings();
/* 1128 */             } catch (IOException iOException) {}
/*      */           }
/*      */         });
/*      */   }
/*      */ 
/*      */   
/*      */   public long getRenderStartTime() {
/* 1135 */     return this.renderStartTime;
/*      */   }
/*      */   
/*      */   public void resetRenderStartTime() {
/* 1139 */     this.renderStartTime = -1L;
/*      */   }
/*      */   
/*      */   public Queue<FutureTask<?>> getMinecraftScheduledTasks() {
/*      */     Queue<FutureTask<?>> result;
/* 1144 */     this.scheduledTasksField.setAccessible(true);
/*      */     
/*      */     try {
/* 1147 */       result = (Queue<FutureTask<?>>)this.scheduledTasksField.get(Minecraft.func_71410_x());
/* 1148 */     } catch (IllegalArgumentException e) {
/* 1149 */       result = null;
/* 1150 */     } catch (IllegalAccessException e) {
/* 1151 */       result = null;
/*      */     } 
/* 1153 */     this.scheduledTasksField.setAccessible(false);
/* 1154 */     return result;
/*      */   }
/*      */   
/*      */   public Callable<Object> getRenderStartTimeUpdater() {
/* 1158 */     return this.renderStartTimeUpdater;
/*      */   }
/*      */   
/*      */   public void requestBufferToDelete(int bufferId) {
/* 1162 */     synchronized (this.buffersToDelete) {
/* 1163 */       this.buffersToDelete.add(Integer.valueOf(bufferId));
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean isWaitingForWorldUpdate() {
/* 1168 */     return this.waitingForWorldUpdate;
/*      */   }
/*      */   
/*      */   public WorldDataHandler getWorldDataHandler() {
/* 1172 */     return this.worldDataHandler;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setMainValues() {
/* 1180 */     synchronized (this.mainStuffSync) {
/* 1181 */       EntityPlayerSP entityPlayerSP = (Minecraft.func_71410_x()).field_71439_g;
/* 1182 */       if (entityPlayerSP != null) {
/* 1183 */         World worldToChangeTo = ignoreWorld(((EntityPlayer)entityPlayerSP).field_70170_p) ? this.mainWorld : ((EntityPlayer)entityPlayerSP).field_70170_p;
/* 1184 */         boolean worldChanging = (worldToChangeTo != this.mainWorld);
/* 1185 */         if (worldChanging) {
/* 1186 */           this.mainWorldChangedTime = -1L;
/* 1187 */           if (this.spawnToRestore != null) {
/* 1188 */             int dimId = worldToChangeTo.field_73011_w.getDimension();
/* 1189 */             if (this.latestSpawn.get(Integer.valueOf(dimId)) == null)
/* 1190 */               this.mainWorldChangedTime = System.currentTimeMillis(); 
/*      */           } 
/* 1192 */           this.mainWorldUnloaded = false;
/*      */         } 
/* 1194 */         this.mainWorld = worldToChangeTo;
/* 1195 */         this.mainPlayerX = ((EntityPlayer)entityPlayerSP).field_70165_t;
/* 1196 */         this.mainPlayerY = ((EntityPlayer)entityPlayerSP).field_70163_u;
/* 1197 */         this.mainPlayerZ = ((EntityPlayer)entityPlayerSP).field_70161_v;
/* 1198 */         if (worldChanging)
/* 1199 */           checkForWorldUpdate(); 
/*      */       } else {
/* 1201 */         if (this.mainWorld != null && !this.mainWorldUnloaded)
/* 1202 */           onWorldUnload(); 
/* 1203 */         this.mainWorld = null;
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public void updateCaveStart(double playerX, double playerZ, World world) {
/* 1209 */     if (!this.caveStartDetermined) {
/* 1210 */       int chunkX = (int)Math.floor(playerX) >> 4;
/* 1211 */       int chunkZ = (int)Math.floor(playerZ) >> 4;
/* 1212 */       Chunk chunk = world.func_72964_e(chunkX, chunkZ);
/* 1213 */       if (chunk != null && chunk.func_177410_o()) {
/* 1214 */         if (world.func_72940_L() < 256 && 
/* 1215 */           Math.abs(chunk.func_76611_b(0, 0) - world.func_72940_L()) < 16) {
/* 1216 */           this.caveStart = world.func_72940_L() - 1;
/*      */         }
/* 1218 */         this.caveStartDetermined = true;
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean caveStartIsDetermined() {
/* 1224 */     return this.caveStartDetermined;
/*      */   }
/*      */   
/*      */   public int getCaveStart() {
/* 1228 */     return this.caveStart;
/*      */   }
/*      */   
/*      */   public float getBrightness() {
/* 1232 */     float sunBrightness = (WorldMap.settings.lighting && this.caveStart == -1) ? this.world.getSunBrightnessFactor(1.0F) : 1.0F;
/* 1233 */     return 0.25F + 0.75F * sunBrightness;
/*      */   }
/*      */   
/*      */   public MapBiomes getMapBiomes() {
/* 1237 */     return this.mapBiomes;
/*      */   }
/*      */   
/*      */   public boolean isWorldRealms(String world) {
/* 1241 */     return world.startsWith("Realms_");
/*      */   }
/*      */   
/*      */   public boolean isWorldMultiplayer(boolean realms, String world) {
/* 1245 */     return (realms || world.startsWith("Multiplayer_"));
/*      */   }
/*      */   
/*      */   public MapWorld getMapWorld() {
/* 1249 */     return this.mapWorld;
/*      */   }
/*      */   
/*      */   public boolean isCurrentMultiworldWritable() {
/* 1253 */     return (this.mapWorld != null && (this.mapWorld.getCurrentDimension()).currentMultiworldWritable);
/*      */   }
/*      */   
/*      */   public String getCurrentDimension() {
/* 1257 */     return "placeholder";
/*      */   }
/*      */   
/*      */   public MapWorld getLastNonNullWorld() {
/* 1261 */     return this.lastNonNullWorld;
/*      */   }
/*      */   
/*      */   public void requestCurrentMapDeletion() {
/* 1265 */     if (this.currentMapNeedsDeletion)
/* 1266 */       throw new RuntimeException("Requesting map deletion at a weird time!"); 
/* 1267 */     this.currentMapNeedsDeletion = true;
/*      */   }
/*      */ }


/* Location:              C:\Users\ioanm\Downloads\XaerosWorldMap_1.7.3_Forge_1.12.jar!\xaero\map\MapProcessor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */