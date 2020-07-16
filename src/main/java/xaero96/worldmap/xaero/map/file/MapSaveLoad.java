/*      */ package xaero.map.file;
/*      */ 
/*      */ import java.io.BufferedInputStream;
/*      */ import java.io.BufferedOutputStream;
/*      */ import java.io.DataInputStream;
/*      */ import java.io.DataOutputStream;
/*      */ import java.io.File;
/*      */ import java.io.FileInputStream;
/*      */ import java.io.FileOutputStream;
/*      */ import java.io.IOException;
/*      */ import java.nio.file.CopyOption;
/*      */ import java.nio.file.FileSystemException;
/*      */ import java.nio.file.Files;
/*      */ import java.nio.file.Path;
/*      */ import java.nio.file.StandardCopyOption;
/*      */ import java.nio.file.attribute.FileAttribute;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.stream.Stream;
/*      */ import java.util.zip.ZipEntry;
/*      */ import java.util.zip.ZipInputStream;
/*      */ import java.util.zip.ZipOutputStream;
/*      */ import net.minecraft.block.Block;
/*      */ import net.minecraft.init.Blocks;
/*      */ import net.minecraft.world.World;
/*      */ import org.lwjgl.opengl.OpenGLException;
/*      */ import xaero.map.MapProcessor;
/*      */ import xaero.map.Misc;
/*      */ import xaero.map.WorldMap;
/*      */ import xaero.map.file.worldsave.WorldDataHandler;
/*      */ import xaero.map.region.MapBlock;
/*      */ import xaero.map.region.MapRegion;
/*      */ import xaero.map.region.MapTile;
/*      */ import xaero.map.region.MapTileChunk;
/*      */ import xaero.map.region.Overlay;
/*      */ import xaero.map.region.OverlayBuilder;
/*      */ import xaero.map.region.OverlayManager;
/*      */ import xaero.map.world.MapDimension;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class MapSaveLoad
/*      */ {
/*      */   private static final int currentSaveMajorVersion = 0;
/*      */   private static final int currentSaveVersion = 1;
/*      */   public static final int SAVE_TIME = 60000;
/*      */   public static final int currentCacheSaveVersion = 5;
/*      */   private ArrayList<MapRegion> toSave;
/*      */   private ArrayList<MapRegion> toLoad;
/*      */   private ArrayList<MapRegion> toRequestToLoad;
/*      */   private ArrayList<MapRegion> prioritizedToRequestToLoad;
/*      */   private ArrayList<File> cacheToConvertFromTemp;
/*      */   private MapRegion nextToLoadByViewing;
/*      */   private boolean regionDetectionComplete;
/*      */   private ArrayList<Path> cacheFolders;
/*      */   private Path lastRealmOwnerPath;
/*      */   public boolean loadingFiles;
/*      */   private OverlayBuilder overlayBuilder;
/*      */   private PNGExporter pngExporter;
/*      */   private List<MapDimension> workingDimList;
/*      */   public boolean saveAll;
/*      */   
/*      */   public MapSaveLoad(OverlayManager overlayManager, PNGExporter pngExporter) {
/*   66 */     this.toSave = new ArrayList<>();
/*   67 */     this.toLoad = new ArrayList<>();
/*   68 */     this.toRequestToLoad = new ArrayList<>();
/*   69 */     this.prioritizedToRequestToLoad = new ArrayList<>();
/*   70 */     this.cacheFolders = new ArrayList<>();
/*   71 */     this.cacheToConvertFromTemp = new ArrayList<>();
/*   72 */     this.overlayBuilder = new OverlayBuilder(overlayManager);
/*   73 */     this.pngExporter = pngExporter;
/*   74 */     this.workingDimList = new ArrayList<>();
/*      */   }
/*      */   
/*      */   public void exportPNG() throws OpenGLException {
/*   78 */     MapProcessor.instance.pushProcessorPause();
/*      */     try {
/*   80 */       this.pngExporter.export();
/*   81 */     } catch (Throwable e) {
/*   82 */       System.out.println("Failed to export PNG with exception!");
/*   83 */       MapProcessor.instance.setCrashedBy(e);
/*      */     } 
/*   85 */     MapProcessor.instance.popProcessorPause();
/*      */   }
/*      */   
/*      */   private File getSecondaryFile(String extension, File realFile) {
/*   89 */     if (realFile == null)
/*   90 */       return null; 
/*   91 */     String p = realFile.getPath();
/*   92 */     return new File(p.substring(0, p.lastIndexOf(".")) + extension);
/*      */   }
/*      */   
/*      */   public File getTempFile(File realFile) {
/*   96 */     return getSecondaryFile(".zip.temp", realFile);
/*      */   }
/*      */   
/*      */   public void updateCacheFolderList(Path subFolder) {
/*      */     Stream<Path> allFiles;
/*      */     try {
/*  102 */       allFiles = Files.list(subFolder);
/*  103 */     } catch (Exception e) {
/*  104 */       this.cacheFolders.clear();
/*      */       return;
/*      */     } 
/*  107 */     Object[] filesArray = allFiles.toArray();
/*  108 */     allFiles.close();
/*  109 */     this.cacheFolders.clear();
/*  110 */     for (int i = 0; i < filesArray.length; i++) {
/*  111 */       Path path = (Path)filesArray[i];
/*  112 */       if (Files.isDirectory(path, new java.nio.file.LinkOption[0]) && path.getFileName().toString().startsWith("cache_"))
/*      */       {
/*  114 */         if (!path.getFileName().toString().split("_")[1].equals("" + MapProcessor.instance.getGlobalVersion())) {
/*      */           
/*  116 */           this.cacheFolders.add(path);
/*  117 */           if (WorldMap.settings.debug)
/*  118 */             System.out.println(path.toString()); 
/*      */         }  } 
/*  120 */     }  this.cacheFolders.add(subFolder);
/*      */   }
/*      */   
/*      */   public Path getCacheFolder(Path subFolder) {
/*  124 */     if (subFolder != null)
/*  125 */       return subFolder.resolve("cache_" + MapProcessor.instance.getGlobalVersion()); 
/*  126 */     return null;
/*      */   }
/*      */   
/*      */   public File getCacheFile(MapRegionInfo region, boolean checkOldFolders, boolean requestCache) throws IOException {
/*  130 */     Path subFolder = getSubFolder(region.getWorld());
/*  131 */     Path latestCacheFolder = getCacheFolder(subFolder);
/*  132 */     if (latestCacheFolder == null)
/*  133 */       return null; 
/*  134 */     Files.createDirectories(latestCacheFolder, (FileAttribute<?>[])new FileAttribute[0]);
/*  135 */     Path cacheFile = latestCacheFolder.resolve(region.getRegionX() + "_" + region.getRegionZ() + ".xwmc");
/*  136 */     if (!checkOldFolders || Files.exists(cacheFile, new java.nio.file.LinkOption[0]))
/*  137 */       return cacheFile.toFile(); 
/*  138 */     if (requestCache)
/*  139 */       region.setShouldCache(true, "cache file"); 
/*  140 */     for (int i = 0; i < this.cacheFolders.size(); i++) {
/*  141 */       Path oldCacheFolder = this.cacheFolders.get(i);
/*  142 */       Path oldCacheFile = oldCacheFolder.resolve(region.getRegionX() + "_" + region.getRegionZ() + ".xwmc");
/*  143 */       if (Files.exists(oldCacheFile, new java.nio.file.LinkOption[0]))
/*  144 */         return oldCacheFile.toFile(); 
/*      */     } 
/*  146 */     return cacheFile.toFile();
/*      */   }
/*      */   
/*      */   public File getFile(MapRegion region) {
/*  150 */     if (region.getWorld() == null)
/*  151 */       return null; 
/*  152 */     File detectedFile = region.getRegionFile();
/*  153 */     boolean realms = MapProcessor.instance.isWorldRealms(region.getWorld());
/*  154 */     boolean multiplayer = region.isMultiplayer();
/*  155 */     if (!multiplayer) {
/*  156 */       if (detectedFile != null) {
/*  157 */         return detectedFile;
/*      */       }
/*  159 */       return MapProcessor.instance.getWorldDataHandler().getWorldDir().toPath().resolve("region").resolve("r." + region.getRegionX() + "." + region.getRegionZ() + ".mca").toFile();
/*      */     } 
/*  161 */     Path mainFolder = getMainFolder(region.getWorld(), true);
/*  162 */     Path subFolder = getSubFolder(region.getWorld(), mainFolder, true);
/*      */     try {
/*  164 */       File subFolderFile = subFolder.toFile();
/*  165 */       if (!subFolderFile.exists()) {
/*  166 */         Files.createDirectories(subFolderFile.toPath(), (FileAttribute<?>[])new FileAttribute[0]);
/*  167 */         if (realms && WorldMap.events.getLatestRealm() != null) {
/*  168 */           Path ownerPath = mainFolder.resolve((WorldMap.events.getLatestRealm()).owner + ".owner");
/*  169 */           if (!ownerPath.equals(this.lastRealmOwnerPath)) {
/*  170 */             if (!Files.exists(ownerPath, new java.nio.file.LinkOption[0]))
/*  171 */               Files.createFile(ownerPath, (FileAttribute<?>[])new FileAttribute[0]); 
/*  172 */             this.lastRealmOwnerPath = ownerPath;
/*      */           } 
/*      */         } 
/*      */       } 
/*  176 */     } catch (IOException e1) {
/*  177 */       e1.printStackTrace();
/*      */     } 
/*  179 */     if (detectedFile != null && detectedFile.getName().endsWith(".xaero")) {
/*  180 */       File zipFile = subFolder.resolve(region.getRegionX() + "_" + region.getRegionZ() + ".zip").toFile();
/*  181 */       if (detectedFile.exists() && !zipFile.exists())
/*  182 */         xaeroToZip(detectedFile); 
/*  183 */       region.setRegionFile(zipFile);
/*  184 */       return zipFile;
/*      */     } 
/*  186 */     return (detectedFile == null) ? subFolder.resolve(region.getRegionX() + "_" + region.getRegionZ() + ".zip").toFile() : detectedFile;
/*      */   }
/*      */   
/*      */   public Path getMainFolder(String world, boolean hasMW) {
/*  190 */     if (world == null)
/*  191 */       return null; 
/*  192 */     if (!hasMW)
/*  193 */       return WorldMap.saveFolder.toPath().resolve(world); 
/*  194 */     return WorldMap.saveFolder.toPath().resolve(world.substring(0, world.lastIndexOf("_")));
/*      */   }
/*      */   
/*      */   Path getSubFolder(String world, Path mainFolder, boolean hasMW) {
/*  198 */     if (world == null)
/*  199 */       return null; 
/*  200 */     if (!hasMW)
/*  201 */       return mainFolder; 
/*  202 */     return mainFolder.resolve(world.substring(world.lastIndexOf("_") + 1));
/*      */   }
/*      */ 
/*      */   
/*      */   public Path getSubFolder(String world) {
/*  207 */     if (world == null)
/*  208 */       return null; 
/*  209 */     boolean mp = (MapProcessor.instance.isWorldMultiplayer(false, world) || MapProcessor.instance.isWorldRealms(world));
/*  210 */     return getSubFolder(world, getMainFolder(world, mp), mp);
/*      */   }
/*      */   
/*      */   private void xaeroToZip(File xaero) {
/*  214 */     File zipFile = xaero.toPath().getParent().resolve(xaero.getName().substring(0, xaero.getName().lastIndexOf('.')) + ".zip").toFile();
/*      */     try {
/*  216 */       BufferedInputStream in = new BufferedInputStream(new FileInputStream(xaero), 1024);
/*  217 */       ZipOutputStream zipOutput = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipFile)));
/*  218 */       ZipEntry e = new ZipEntry("region.xaero");
/*  219 */       zipOutput.putNextEntry(e);
/*  220 */       byte[] bytes = new byte[1024];
/*      */       int got;
/*  222 */       while ((got = in.read(bytes)) > 0)
/*  223 */         zipOutput.write(bytes, 0, got); 
/*  224 */       zipOutput.closeEntry();
/*  225 */       zipOutput.flush();
/*  226 */       zipOutput.close();
/*  227 */       in.close();
/*  228 */       Files.deleteIfExists(xaero.toPath());
/*  229 */     } catch (IOException e) {
/*  230 */       e.printStackTrace();
/*      */       return;
/*      */     } 
/*      */   }
/*      */   
/*      */   public void detectRegions() {
/*  236 */     MapProcessor mapProcessor = MapProcessor.instance;
/*  237 */     mapProcessor.getMapWorld().getCurrentDimension().createDetectedRegions().clear();
/*  238 */     String worldString = mapProcessor.getCurrentWorldString();
/*  239 */     if (worldString == null)
/*      */       return; 
/*  241 */     if (mapProcessor.isWorldMultiplayer(mapProcessor.isWorldRealms(worldString), worldString)) {
/*  242 */       Path mapFolder = getSubFolder(worldString);
/*  243 */       if (!mapFolder.toFile().exists())
/*      */         return; 
/*  245 */       detectRegionsFromFiles(worldString, mapFolder, "^-?\\d+_-?\\d+\\.(zip|xaero)$", "_", 0, 1, 0);
/*      */     } else {
/*  247 */       File worldDir = mapProcessor.getWorldDataHandler().getWorldDir();
/*  248 */       if (worldDir == null)
/*      */         return; 
/*  250 */       Path worldFolder = worldDir.toPath().resolve("region");
/*  251 */       if (!worldFolder.toFile().exists())
/*      */         return; 
/*  253 */       detectRegionsFromFiles(worldString, worldFolder, "^r\\.(-{0,1}[0-9]+\\.){2}mc[ar]$", "\\.", 1, 2, 8192);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void detectRegionsFromFiles(String worldString, Path folder, String regex, String splitRegex, int xIndex, int zIndex, int emptySize) {
/*  258 */     int total = 0;
/*      */     try {
/*  260 */       Stream<Path> files = Files.list(folder);
/*  261 */       Iterator<Path> iter = files.iterator();
/*  262 */       while (iter.hasNext()) {
/*  263 */         Path file = iter.next();
/*  264 */         if (Files.isDirectory(file, new java.nio.file.LinkOption[0]))
/*      */           continue; 
/*  266 */         String regionName = file.getFileName().toString();
/*  267 */         if (!regionName.matches(regex))
/*      */           continue; 
/*  269 */         if (Files.size(file) <= emptySize)
/*      */           continue; 
/*  271 */         String[] args = regionName.substring(0, regionName.lastIndexOf('.')).split(splitRegex);
/*  272 */         int x = Integer.parseInt(args[xIndex]);
/*  273 */         int z = Integer.parseInt(args[zIndex]);
/*  274 */         RegionDetection regionDetection = new RegionDetection(worldString, x, z, file.toFile());
/*  275 */         File cacheFile = getCacheFile(regionDetection, true, true);
/*  276 */         regionDetection.setCacheFile(cacheFile);
/*  277 */         MapProcessor.instance.addRegionDetection(regionDetection);
/*  278 */         total++;
/*      */       } 
/*      */       
/*  281 */       files.close();
/*  282 */     } catch (IOException e) {
/*  283 */       e.printStackTrace();
/*      */       return;
/*      */     } 
/*  286 */     if (WorldMap.settings.debug) {
/*  287 */       System.out.println(String.format("%d regions detected!", new Object[] { Integer.valueOf(total) }));
/*      */     }
/*      */   }
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean saveRegion(MapRegion region) {
/*      */     try {
/*  343 */       if (!region.isMultiplayer()) {
/*  344 */         if (WorldMap.settings.debug)
/*  345 */           System.out.println("Save not required for singleplayer: " + region + " " + region.getWorld()); 
/*  346 */         return (region.countChunks() > 0);
/*      */       } 
/*  348 */       File permFile = getFile(region);
/*  349 */       File file = getTempFile(permFile);
/*  350 */       if (file == null)
/*  351 */         return true; 
/*  352 */       if (!file.exists())
/*  353 */         file.createNewFile(); 
/*  354 */       boolean regionIsEmpty = true;
/*  355 */       DataOutputStream out = null;
/*      */       try {
/*  357 */         ZipOutputStream zipOut = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
/*  358 */         out = new DataOutputStream(zipOut);
/*  359 */         ZipEntry e = new ZipEntry("region.xaero");
/*  360 */         zipOut.putNextEntry(e);
/*      */         
/*  362 */         out.write(255);
/*  363 */         out.writeInt(1);
/*      */         
/*  365 */         for (int o = 0; o < 8; o++) {
/*  366 */           for (int p = 0; p < 8; p++) {
/*  367 */             MapTileChunk chunk = region.getChunk(o, p);
/*  368 */             if (chunk != null)
/*  369 */               if (chunk.includeInSave()) {
/*  370 */                 out.write(o << 4 | p);
/*  371 */                 boolean chunkIsEmpty = true;
/*  372 */                 for (int i = 0; i < 4; i++) {
/*  373 */                   for (int j = 0; j < 4; j++) {
/*  374 */                     MapTile tile = chunk.getTile(i, j);
/*  375 */                     if (tile != null && tile.isLoaded())
/*  376 */                     { chunkIsEmpty = false;
/*  377 */                       for (int x = 0; x < 16; x++) {
/*  378 */                         MapBlock[] c = tile.getBlockColumn(x);
/*  379 */                         for (int z = 0; z < 16; z++) {
/*  380 */                           savePixel(c[z], out);
/*      */                         
/*      */                         }
/*      */                       
/*      */                       }
/*      */                        }
/*      */                     
/*      */                     else
/*      */                     
/*  389 */                     { out.writeInt(-1); } 
/*      */                   } 
/*  391 */                 }  if (!chunkIsEmpty)
/*  392 */                   regionIsEmpty = false; 
/*      */               } else {
/*  394 */                 region.setChunk(o, p, null);
/*      */               }  
/*      */           } 
/*  397 */         }  zipOut.closeEntry();
/*      */       } finally {
/*  399 */         if (out != null)
/*  400 */           out.close(); 
/*      */       } 
/*  402 */       if (regionIsEmpty) {
/*  403 */         safeDelete(permFile.toPath(), ".zip");
/*  404 */         safeDelete(file.toPath(), ".temp");
/*  405 */         if (WorldMap.settings.debug)
/*  406 */           System.out.println("Save cancelled because the region is empty: " + region + " " + region.getWorld()); 
/*  407 */         return false;
/*      */       } 
/*      */       
/*  410 */       safeMove(file.toPath(), permFile.toPath(), ".temp", ".zip", new CopyOption[] { StandardCopyOption.REPLACE_EXISTING });
/*  411 */       if (WorldMap.settings.debug)
/*  412 */         System.out.println("Region saved: " + region + " " + region.getWorld() + ", " + region.getRegionX() + "_" + region.getRegionZ() + " " + MapProcessor.instance.getMapWriter().getUpdateCounter()); 
/*  413 */       return true;
/*      */     }
/*  415 */     catch (IOException e) {
/*  416 */       e.printStackTrace();
/*      */       
/*  418 */       return true;
/*      */     } 
/*      */   }
/*      */   private Path getBackupFolder(Path filePath, int saveVersion, int backupVersion) {
/*  422 */     return filePath.getParent().resolve(saveVersion + "_backup_" + backupVersion);
/*      */   }
/*      */   
/*      */   public void backupFile(File file, int saveVersion) throws IOException {
/*  426 */     if (file.getName().endsWith(".mca") || file.getName().endsWith(".mcr"))
/*  427 */       throw new RuntimeException("World save protected: " + file); 
/*  428 */     Path filePath = file.toPath();
/*  429 */     int backupVersion = 0;
/*  430 */     Path backupFolder = getBackupFolder(filePath, saveVersion, backupVersion);
/*  431 */     String backupName = filePath.getFileName().toString();
/*  432 */     Path backup = backupFolder.resolve(backupName);
/*      */     
/*  434 */     while (Files.exists(backup, new java.nio.file.LinkOption[0])) {
/*  435 */       backupVersion++;
/*  436 */       backupFolder = getBackupFolder(filePath, saveVersion, backupVersion);
/*  437 */       backup = backupFolder.resolve(backupName);
/*      */     } 
/*      */     
/*  440 */     Files.createDirectories(backupFolder, (FileAttribute<?>[])new FileAttribute[0]);
/*  441 */     Files.move(file.toPath(), backup, new CopyOption[0]);
/*  442 */     System.out.println("File " + file.getPath() + " backed up to " + backupFolder.toFile().getPath());
/*      */   }
/*      */   
/*      */   public boolean loadRegion(World world, MapRegion region) {
/*  446 */     boolean multiplayer = region.isMultiplayer();
/*  447 */     File file = getFile(region);
/*  448 */     if (file == null || !file.exists()) {
/*  449 */       if (region.getLoadState() == 4) {
/*  450 */         region.setBeingWritten(false);
/*  451 */         region.setSaveExists(null);
/*      */       } 
/*  453 */       return false;
/*      */     } 
/*  455 */     int saveVersion = -1;
/*  456 */     boolean versionReached = false;
/*  457 */     int[] overlayBiomeBuffer = new int[3];
/*      */     try {
/*  459 */       synchronized (region) {
/*  460 */         region.setLoadState((byte)1);
/*      */       } 
/*  462 */       region.setSaveExists(Boolean.valueOf(true));
/*  463 */       region.restoreBufferUpdateObjects();
/*  464 */       int totalChunks = 0;
/*      */       
/*  466 */       if (multiplayer) {
/*      */ 
/*      */         
/*  469 */         DataInputStream in = null;
/*      */         try {
/*  471 */           ZipInputStream zipIn = new ZipInputStream(new BufferedInputStream(new FileInputStream(file), 2048));
/*  472 */           in = new DataInputStream(zipIn);
/*  473 */           zipIn.getNextEntry();
/*  474 */           int firstByte = in.read();
/*  475 */           if (firstByte == 255) {
/*  476 */             saveVersion = in.readInt();
/*      */             
/*  478 */             if (1 < saveVersion) {
/*  479 */               zipIn.closeEntry();
/*  480 */               in.close();
/*  481 */               System.out.println("Trying to load a newer region " + region.getRegionX() + "_" + region.getRegionZ() + " save using an older version of Xaero's World Map!");
/*  482 */               backupFile(file, saveVersion);
/*  483 */               region.setSaveExists(null);
/*  484 */               return false;
/*      */             } 
/*  486 */             firstByte = -1;
/*      */           } 
/*  488 */           versionReached = true;
/*      */           while (true) {
/*  490 */             int chunkCoords = (firstByte == -1) ? in.read() : firstByte;
/*  491 */             if (chunkCoords == -1)
/*      */               break; 
/*  493 */             firstByte = -1;
/*  494 */             int o = chunkCoords >> 4;
/*  495 */             int p = chunkCoords & 0xF;
/*  496 */             MapTileChunk chunk = region.getChunk(o, p);
/*  497 */             if (chunk == null)
/*  498 */               region.setChunk(o, p, chunk = new MapTileChunk(region, region.getRegionX() * 8 + o, region.getRegionZ() * 8 + p)); 
/*  499 */             chunk.setLoadState((byte)1);
/*  500 */             chunk.resetMasks();
/*  501 */             for (int i = 0; i < 4; i++) {
/*  502 */               for (int j = 0; j < 4; j++) {
/*  503 */                 Integer nextTile = Integer.valueOf(in.readInt());
/*  504 */                 if (nextTile.intValue() != -1)
/*      */                 
/*  506 */                 { MapTile tile = MapProcessor.instance.getTilePool().get(MapProcessor.instance.getCurrentDimension(), chunk.getX() * 4 + i, chunk.getZ() * 4 + j);
/*  507 */                   for (int x = 0; x < 16; x++) {
/*  508 */                     MapBlock[] c = tile.getBlockColumn(x);
/*  509 */                     for (int z = 0; z < 16; z++) {
/*  510 */                       if (c[z] == null) {
/*  511 */                         c[z] = new MapBlock();
/*      */                       } else {
/*  513 */                         c[z].prepareForWriting();
/*  514 */                       }  loadPixel(nextTile, c[z], in, saveVersion, world, tile.getChunkX() * 16 + x, tile.getChunkZ() * 16 + z, overlayBiomeBuffer);
/*  515 */                       nextTile = null;
/*      */                     } 
/*      */                   } 
/*  518 */                   chunk.setTile(i, j, tile);
/*  519 */                   tile.setLoaded(true); } 
/*      */               } 
/*  521 */             }  if (!chunk.includeInSave()) {
/*  522 */               chunk = null;
/*  523 */               region.setChunk(o, p, null);
/*      */               continue;
/*      */             } 
/*  526 */             region.pushWriterPause();
/*  527 */             totalChunks++;
/*  528 */             chunk.setToUpdateBuffers(true);
/*  529 */             chunk.setLoadState((byte)2);
/*  530 */             region.popWriterPause();
/*      */           } 
/*      */           
/*  533 */           zipIn.closeEntry();
/*      */         } finally {
/*  535 */           if (in != null)
/*  536 */             in.close(); 
/*      */         } 
/*  538 */         if (totalChunks > 0) {
/*  539 */           if (WorldMap.settings.debug)
/*  540 */             System.out.println("Region loaded: " + region + " " + region.getWorld() + ", " + region.getRegionX() + "_" + region.getRegionZ() + " " + saveVersion); 
/*  541 */           return true;
/*      */         } 
/*  543 */         region.setSaveExists(null);
/*  544 */         safeDelete(file.toPath(), ".zip");
/*  545 */         if (WorldMap.settings.debug)
/*  546 */           System.out.println("Cancelled loading an empty region: " + region + " " + region.getWorld() + ", " + region.getRegionX() + "_" + region.getRegionZ() + " " + saveVersion); 
/*  547 */         return false;
/*      */       } 
/*      */       
/*  550 */       int[] chunkCount = new int[1];
/*  551 */       WorldDataHandler.Result buildResult = MapProcessor.instance.getWorldDataHandler().buildRegion(region, world, true, chunkCount);
/*  552 */       if (buildResult == WorldDataHandler.Result.CANCEL) {
/*  553 */         RegionDetection restoredDetection = new RegionDetection(region.getWorld(), region.getRegionX(), region.getRegionZ(), region.getRegionFile());
/*  554 */         restoredDetection.transferInfoFrom(region);
/*  555 */         MapProcessor.instance.addRegionDetection(restoredDetection);
/*  556 */         MapProcessor.instance.removeMapRegion(region);
/*  557 */         MapProcessor.instance.removeToProcess(region);
/*  558 */         System.out.println("Region cancelled from world save: " + region + " " + region.getWorld() + ", " + region.getRegionX() + "_" + region.getRegionZ());
/*  559 */         return false;
/*      */       } 
/*  561 */       region.setRegionFile(file);
/*  562 */       boolean result = (buildResult == WorldDataHandler.Result.SUCCESS && chunkCount[0] > 0);
/*  563 */       if (!result) {
/*  564 */         region.setSaveExists(null);
/*  565 */         if (WorldMap.settings.debug)
/*  566 */           System.out.println("Region failed to load from world save: " + region + " " + region.getWorld() + ", " + region.getRegionX() + "_" + region.getRegionZ()); 
/*  567 */       } else if (WorldMap.settings.debug) {
/*  568 */         System.out.println("Region loaded from world save: " + region + " " + region.getWorld() + ", " + region.getRegionX() + "_" + region.getRegionZ());
/*  569 */       }  return result;
/*      */     
/*      */     }
/*  572 */     catch (Exception e) {
/*  573 */       region.setSaveExists(null);
/*  574 */       System.out.println("Region failed to load: " + region.getRegionX() + "_" + region.getRegionZ() + (versionReached ? (" " + saveVersion) : ""));
/*  575 */       e.printStackTrace();
/*  576 */       return false;
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean beingLoaded(int regX, int regZ) {
/*  581 */     if (this.loadingFiles)
/*  582 */       return true; 
/*  583 */     for (int i = 0; i < this.toLoad.size(); i++) {
/*  584 */       synchronized (this.toLoad) {
/*  585 */         if (i >= this.toLoad.size())
/*  586 */           return false; 
/*  587 */         MapRegion r = this.toLoad.get(i);
/*  588 */         if (r != null && r.getRegionX() == regX && r.getRegionZ() == regZ)
/*  589 */           return true; 
/*      */       } 
/*      */     } 
/*  592 */     return false;
/*      */   }
/*      */   
/*      */   public boolean beingSaved(MapDimension dim, int regX, int regZ) {
/*  596 */     for (int i = 0; i < this.toSave.size(); i++) {
/*  597 */       MapRegion r = this.toSave.get(i);
/*  598 */       if (r != null && r.getDim() == dim && r.getRegionX() == regX && r.getRegionZ() == regZ)
/*  599 */         return true; 
/*      */     } 
/*  601 */     return false;
/*      */   }
/*      */   
/*      */   public void requestLoad(MapRegion region, String reason) {
/*  605 */     requestLoad(region, reason, true);
/*      */   }
/*      */   
/*      */   public void requestLoad(MapRegion region, String reason, boolean prioritize) {
/*  609 */     region.setReloadHasBeenRequested(true, reason);
/*  610 */     if (prioritize) {
/*  611 */       synchronized (this.prioritizedToRequestToLoad) {
/*  612 */         this.prioritizedToRequestToLoad.remove(region);
/*  613 */         this.prioritizedToRequestToLoad.add(region);
/*      */       } 
/*      */     } else {
/*  616 */       synchronized (this.toRequestToLoad) {
/*  617 */         if (!this.toRequestToLoad.contains(region))
/*  618 */           this.toRequestToLoad.add(region); 
/*      */       } 
/*  620 */     }  if (WorldMap.settings.debug && reason != null)
/*  621 */       System.out.println("Requesting load for: " + region + " " + region.getWorld() + ", " + region.getRegionX() + "_" + region.getRegionZ() + " " + reason); 
/*      */   }
/*      */   
/*      */   public void cancelAllLoadRequests(MapRegion region) {
/*  625 */     synchronized (this.prioritizedToRequestToLoad) {
/*  626 */       this.prioritizedToRequestToLoad.remove(region);
/*      */     } 
/*  628 */     synchronized (this.toRequestToLoad) {
/*  629 */       this.toRequestToLoad.remove(region);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void clearLoadRequests() {
/*  634 */     synchronized (this.prioritizedToRequestToLoad) {
/*  635 */       this.prioritizedToRequestToLoad.clear();
/*      */     } 
/*  637 */     synchronized (this.toRequestToLoad) {
/*  638 */       this.toRequestToLoad.clear();
/*      */     } 
/*      */   }
/*      */   
/*      */   public void addToLoad(MapRegion region) {
/*  643 */     synchronized (this.toLoad) {
/*  644 */       this.toLoad.add(region);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void removeToLoad(MapRegion region) {
/*  649 */     synchronized (this.toLoad) {
/*  650 */       this.toLoad.remove(region);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void clearToLoad() {
/*  655 */     synchronized (this.toLoad) {
/*  656 */       this.toLoad.clear();
/*      */     } 
/*      */   }
/*      */   
/*      */   public int getSizeOfToLoad() {
/*  661 */     return this.toLoad.size();
/*      */   }
/*      */   
/*      */   public boolean saveExists(MapRegion region) {
/*  665 */     if (region.getSaveExists() != null)
/*  666 */       return region.getSaveExists().booleanValue(); 
/*  667 */     boolean result = true;
/*  668 */     File file = getFile(region);
/*  669 */     if (file == null || !file.exists())
/*  670 */       result = false; 
/*  671 */     region.setSaveExists(Boolean.valueOf(result));
/*  672 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateSave(MapRegion region) {
/*  677 */     if (region.getLoadState() == 2 && region.isBeingWritten() && 
/*  678 */       System.currentTimeMillis() - region.getLastSaveTime() > 60000L && 
/*  679 */       !beingSaved(region.getDim(), region.getRegionX(), region.getRegionZ())) {
/*  680 */       this.toSave.add(region);
/*  681 */       region.setSaveExists(Boolean.valueOf(true));
/*  682 */       region.setLastSaveTime(System.currentTimeMillis());
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void run(World world) throws Exception {
/*  689 */     int limit = this.toRequestToLoad.size();
/*  690 */     while (!this.toRequestToLoad.isEmpty() && limit > 0) {
/*      */       MapRegion region;
/*  692 */       synchronized (this.toRequestToLoad) {
/*  693 */         if (this.toRequestToLoad.isEmpty())
/*      */           break; 
/*  695 */         region = this.toRequestToLoad.remove(0);
/*      */       } 
/*  697 */       if ((region.getLoadState() == 0 || region.getLoadState() == 4) && !beingLoaded(region.getRegionX(), region.getRegionZ()))
/*  698 */         addToLoad(region); 
/*  699 */       limit--;
/*      */     } 
/*  701 */     limit = this.prioritizedToRequestToLoad.size();
/*  702 */     while (!this.prioritizedToRequestToLoad.isEmpty() && limit > 0) {
/*      */       MapRegion region;
/*  704 */       synchronized (this.prioritizedToRequestToLoad) {
/*  705 */         if (this.prioritizedToRequestToLoad.isEmpty())
/*      */           break; 
/*  707 */         region = this.prioritizedToRequestToLoad.remove(0);
/*      */       } 
/*  709 */       if (region.getLoadState() == 0 || region.getLoadState() == 4)
/*  710 */         synchronized (this.toLoad) {
/*  711 */           this.toLoad.remove(region);
/*  712 */           this.toLoad.add(0, region);
/*      */         }  
/*  714 */       limit--;
/*      */     } 
/*  716 */     if (!this.toLoad.isEmpty() && MapProcessor.instance.caveStartIsDetermined()) {
/*      */       
/*  718 */       boolean loaded = false;
/*  719 */       synchronized (MapProcessor.instance.loadingSync) {
/*  720 */         this.loadingFiles = true;
/*  721 */         while (!MapProcessor.instance.isWaitingForWorldUpdate() && !loaded && !this.toLoad.isEmpty()) {
/*      */           MapRegion region; boolean needsLoading;
/*  723 */           synchronized (this.toLoad) {
/*  724 */             if (this.toLoad.isEmpty())
/*      */               break; 
/*  726 */             region = this.toLoad.get(0);
/*      */           } 
/*      */           
/*  729 */           synchronized (region) {
/*  730 */             needsLoading = (region.getLoadState() == 0 || region.getLoadState() == 4);
/*  731 */             if (needsLoading) {
/*  732 */               if ((region.hasVersion() && region.getVersion() != MapProcessor.instance.getGlobalVersion()) || (!region.hasVersion() && region.getInitialVersion() != MapProcessor.instance.getGlobalVersion()))
/*  733 */                 region.setShouldCache(true, "loading"); 
/*  734 */               region.setVersion(MapProcessor.instance.getGlobalVersion());
/*      */             } 
/*      */           } 
/*  737 */           if (needsLoading) {
/*  738 */             boolean shouldLoadProperly; if (region.getLoadState() == 0) {
/*  739 */               requestCacheIfNeeded(region);
/*      */             }
/*      */             
/*  742 */             MapProcessor.instance.addToProcess(region);
/*      */ 
/*      */             
/*  745 */             synchronized (region) {
/*  746 */               boolean goingToPrepareCache = (region.shouldCache() && (region.getLoadState() == 4 || region.getCacheFile() == null || !region.getCacheFile().exists()));
/*  747 */               shouldLoadProperly = (region.isBeingWritten() || goingToPrepareCache);
/*  748 */               if (!shouldLoadProperly) {
/*  749 */                 region.setLoadState((byte)3);
/*  750 */               } else if (region.shouldCache()) {
/*  751 */                 region.setRecacheHasBeenRequested(true, "loading");
/*      */               } 
/*      */             } 
/*  754 */             if (shouldLoadProperly) {
/*  755 */               boolean bool = loadRegion(world, region);
/*  756 */               if (!bool) {
/*  757 */                 region.setShouldCache(false, "couldn't load");
/*  758 */                 region.setRecacheHasBeenRequested(false, "couldn't load");
/*  759 */                 if (region.getSaveExists() == null) {
/*  760 */                   region.deleteTexturesAndBuffers();
/*  761 */                   if (region.isBeingWritten()) {
/*  762 */                     region.clean();
/*      */                   } else {
/*  764 */                     MapProcessor.instance.removeMapRegion(region);
/*  765 */                     MapProcessor.instance.removeToProcess(region);
/*      */                   } 
/*      */                 } 
/*      */               } else {
/*  769 */                 MapRegion nextRegion = MapProcessor.instance.getMapRegion(region.getRegionX(), region.getRegionZ() + 1, false);
/*  770 */                 if (nextRegion != null) {
/*  771 */                   nextRegion.onTopRegionLoaded(region);
/*      */                 }
/*      */               } 
/*      */ 
/*      */               
/*  776 */               synchronized (region) {
/*  777 */                 if (region.getLoadState() <= 1)
/*  778 */                   region.setLoadState((byte)2); 
/*  779 */                 region.setLastSaveTime(System.currentTimeMillis());
/*      */               } 
/*      */             } else {
/*  782 */               MapRegion nextRegion = MapProcessor.instance.getMapRegion(region.getRegionX(), region.getRegionZ() + 1, false);
/*  783 */               if (nextRegion != null)
/*  784 */                 nextRegion.onTopRegionLoaded(region); 
/*  785 */               if (WorldMap.settings.debug)
/*  786 */                 System.out.println("Loaded from cache only for " + region + " " + ((region.getCacheFile() == null) ? "null" : (String)region.getCacheFile())); 
/*      */             } 
/*  788 */             region.setReloadHasBeenRequested(false, "loading");
/*      */           } 
/*      */ 
/*      */ 
/*      */           
/*  793 */           removeToLoad(region);
/*      */         } 
/*      */         
/*  796 */         this.loadingFiles = false;
/*      */       } 
/*      */     } 
/*  799 */     int regionsToSave = 3;
/*  800 */     while (!this.toSave.isEmpty() && (this.saveAll || regionsToSave > 0)) {
/*  801 */       boolean regionLoaded; MapRegion region = this.toSave.get(0);
/*      */ 
/*      */       
/*  804 */       synchronized (region) {
/*  805 */         regionLoaded = (region.getLoadState() == 2);
/*      */       } 
/*  807 */       if (regionLoaded) {
/*  808 */         if (!region.isBeingWritten())
/*  809 */           throw new Exception("Saving a weird region: " + region.getRegionX() + "_" + region.getRegionZ()); 
/*  810 */         region.pushWriterPause();
/*      */ 
/*      */         
/*  813 */         boolean notEmpty = saveRegion(region);
/*  814 */         if (notEmpty) {
/*  815 */           if (!region.isAllCachePrepared())
/*  816 */             synchronized (region) {
/*      */ 
/*      */               
/*  819 */               region.requestRefresh();
/*      */             }  
/*  821 */           region.setRecacheHasBeenRequested(true, "saving");
/*  822 */           region.setShouldCache(true, "saving");
/*  823 */           region.setBeingWritten(false);
/*  824 */           regionsToSave--;
/*      */         } else {
/*  826 */           MapProcessor.instance.removeMapRegion(region);
/*  827 */           MapProcessor.instance.removeToProcess(region);
/*      */         } 
/*  829 */         region.popWriterPause();
/*  830 */         if (region.getWorld() == null || !region.getWorld().equals(MapProcessor.instance.getCurrentWorldString())) {
/*  831 */           if (region.getCacheFile() != null) {
/*      */             
/*      */             try {
/*  834 */               Files.deleteIfExists(region.getCacheFile().toPath());
/*  835 */             } catch (IOException e) {
/*  836 */               e.printStackTrace();
/*      */             } 
/*  838 */             if (WorldMap.settings.debug)
/*  839 */               System.out.println(String.format("Deleting cache for region %s because it IS outdated.", new Object[] { region })); 
/*      */           } 
/*  841 */           region.clearRegion();
/*      */         } 
/*  843 */       } else if (WorldMap.settings.debug) {
/*  844 */         System.out.println("Tried to save a weird region: " + region + " " + region.getWorld() + " " + region.getRegionX() + "_" + region.getRegionZ() + " " + region.getLoadState());
/*  845 */       }  this.toSave.remove(region);
/*      */     } 
/*  847 */     this.saveAll = false;
/*  848 */     if (MapProcessor.instance.getLastNonNullWorld() != null) {
/*  849 */       this.workingDimList.clear();
/*  850 */       synchronized (MapProcessor.instance.uiSync) {
/*  851 */         this.workingDimList.addAll(MapProcessor.instance.getLastNonNullWorld().getDimensions().values());
/*      */       } 
/*  853 */       for (int d = 0; d < this.workingDimList.size(); d++) {
/*  854 */         MapDimension dim = this.workingDimList.get(d);
/*  855 */         while (!dim.regionsToCache.isEmpty()) {
/*  856 */           MapRegion region = removeToCache(dim, 0);
/*  857 */           region.pushWriterPause();
/*  858 */           if (!region.shouldCache() || region.getVersion() != MapProcessor.instance.getGlobalVersion()) {
/*  859 */             if (WorldMap.settings.detailed_debug)
/*  860 */               System.out.println("toCache cancel: " + region.getRegionX() + "_" + region.getRegionZ() + " " + (!region.shouldCache() ? 1 : 0) + " " + (!region.isAllCachePrepared() ? 1 : 0) + " " + ((region.getVersion() != MapProcessor.instance.getGlobalVersion()) ? 1 : 0) + " " + region.getVersion() + " " + MapProcessor.instance.getGlobalVersion()); 
/*  861 */             if (region.shouldCache())
/*  862 */               region.deleteBuffers(); 
/*  863 */             region.setShouldCache(false, "toCache cancel");
/*  864 */             region.setRecacheHasBeenRequested(false, "toCache cancel");
/*  865 */             region.popWriterPause();
/*      */             continue;
/*      */           } 
/*  868 */           if (!region.isAllCachePrepared())
/*  869 */             throw new RuntimeException("Trying to save cache for a region with cache not prepared: " + region); 
/*  870 */           File permFile = getCacheFile((MapRegionInfo)region, false, false);
/*  871 */           File tempFile = getSecondaryFile(".xwmc.temp", permFile);
/*  872 */           region.saveCacheTextures(tempFile);
/*      */           
/*  874 */           this.cacheToConvertFromTemp.add(permFile);
/*  875 */           region.setCacheFile(permFile);
/*  876 */           region.setShouldCache(false, "toCache normal");
/*  877 */           region.setRecacheHasBeenRequested(false, "toCache normal");
/*  878 */           region.popWriterPause();
/*  879 */           for (int j = 0; j < this.cacheFolders.size(); j++) {
/*  880 */             Path oldCacheFolder = this.cacheFolders.get(j);
/*  881 */             Path oldCacheFile = oldCacheFolder.resolve(permFile.getName());
/*  882 */             Path oldPngFile = oldCacheFolder.resolve(permFile.getName().substring(0, permFile.getName().indexOf('.')) + ".png");
/*  883 */             Files.deleteIfExists(oldCacheFile);
/*  884 */             Files.deleteIfExists(oldPngFile);
/*  885 */             if (oldCacheFolder.getFileName().toString().startsWith("cache_")) {
/*  886 */               Stream<Path> dirContent = Files.list(oldCacheFolder);
/*  887 */               if (!dirContent.iterator().hasNext()) {
/*  888 */                 Files.deleteIfExists(oldCacheFolder);
/*  889 */                 this.cacheFolders.remove(j--);
/*      */               } 
/*  891 */               dirContent.close();
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/*  896 */       for (int i = 0; i < this.cacheToConvertFromTemp.size(); i++) {
/*  897 */         File permFile = this.cacheToConvertFromTemp.get(i);
/*  898 */         File tempFile = getSecondaryFile(".xwmc.temp", permFile);
/*      */         try {
/*  900 */           if (Files.exists(tempFile.toPath(), new java.nio.file.LinkOption[0]))
/*  901 */             Files.move(tempFile.toPath(), permFile.toPath(), new CopyOption[] { StandardCopyOption.REPLACE_EXISTING }); 
/*  902 */           this.cacheToConvertFromTemp.remove(i);
/*  903 */           i--;
/*  904 */         } catch (FileSystemException fileSystemException) {}
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void requestCacheIfNeeded(MapRegion region) {
/*  912 */     if (!region.isBeingWritten())
/*  913 */       region.loadCacheTextures(); 
/*      */   }
/*      */   
/*      */   private void savePixel(MapBlock pixel, DataOutputStream out) throws IOException {
/*  917 */     out.writeInt(pixel.getParametres());
/*  918 */     if (!pixel.isGrass()) {
/*  919 */       out.writeInt(pixel.getState());
/*      */     }
/*      */     
/*  922 */     if (pixel.getNumberOfOverlays() != 0) {
/*  923 */       out.write(pixel.getOverlays().size());
/*  924 */       for (int i = 0; i < pixel.getOverlays().size(); i++)
/*  925 */         saveOverlay(pixel.getOverlays().get(i), out); 
/*      */     } 
/*  927 */     if (pixel.getColourType() == 3)
/*  928 */       out.writeInt(pixel.getCustomColour()); 
/*  929 */     if (pixel.getBiome() != -1)
/*  930 */       out.write(pixel.getBiome()); 
/*      */   }
/*      */   
/*      */   private void loadPixel(Integer next, MapBlock pixel, DataInputStream in, int saveVersion, World world, int globalX, int globalZ, int[] overlayBiomeBuffer) throws IOException {
/*      */     int parametres;
/*  935 */     if (next != null) {
/*  936 */       parametres = next.intValue();
/*      */     } else {
/*  938 */       parametres = in.readInt();
/*  939 */     }  if ((parametres & 0x1) != 0) {
/*  940 */       pixel.setState(in.readInt());
/*      */     } else {
/*  942 */       pixel.setState(Block.func_176210_f(Blocks.field_150349_c.func_176223_P()));
/*  943 */     }  pixel.setHeightType((byte)(parametres >> 4 & 0x3));
/*  944 */     if ((parametres & 0x40) != 0) {
/*  945 */       pixel.setHeight(in.read());
/*      */     } else {
/*  947 */       pixel.setHeight(parametres >> 12 & 0xFF);
/*  948 */     }  if ((parametres & 0x2) != 0) {
/*  949 */       int amount = in.read();
/*      */ 
/*      */ 
/*      */       
/*  953 */       this.overlayBuilder.startBuilding();
/*  954 */       for (int i = 0; i < amount; i++)
/*  955 */         loadOverlay(pixel, in, saveVersion, world, overlayBiomeBuffer); 
/*  956 */       this.overlayBuilder.finishBuilding(pixel);
/*      */     } 
/*  958 */     pixel.setColourType((byte)(parametres >> 2 & 0x3));
/*  959 */     if (pixel.getColourType() == 3)
/*  960 */       pixel.setCustomColour(in.readInt()); 
/*  961 */     if ((pixel.getColourType() != 0 && pixel.getColourType() != 3) || (parametres & 0x100000) != 0)
/*  962 */       pixel.setBiome(in.read()); 
/*  963 */     if (pixel.getColourType() == 3 && pixel.getCustomColour() == -1) {
/*  964 */       pixel.setColourType((byte)0);
/*      */     }
/*      */     
/*  967 */     pixel.setCaveBlock(((parametres & 0x80) != 0));
/*  968 */     pixel.setLight((byte)(parametres >> 8 & 0xF));
/*  969 */     pixel.setGlowing(MapProcessor.instance.getMapWriter().isGlowing(Misc.getStateById(pixel.getState())));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void saveOverlay(Overlay o, DataOutputStream out) throws IOException {
/*  980 */     out.writeInt(o.getParametres());
/*  981 */     if (!o.isWater())
/*  982 */       out.writeInt(o.getState()); 
/*  983 */     if (o.getColourType() == 2)
/*  984 */       out.writeInt(o.getCustomColour()); 
/*  985 */     if (o.getOpacity() > 1) {
/*  986 */       out.writeInt(o.getOpacity());
/*      */     }
/*      */   }
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
/*      */   private void loadOverlay(MapBlock pixel, DataInputStream in, int saveVersion, World world, int[] overlayBiomeBuffer) throws IOException {
/* 1010 */     int state, parametres = in.readInt();
/*      */     
/* 1012 */     if ((parametres & 0x1) != 0) {
/* 1013 */       state = in.readInt();
/*      */     } else {
/* 1015 */       state = Block.func_176210_f(Blocks.field_150355_j.func_176223_P());
/* 1016 */     }  int opacity = 1;
/*      */     
/* 1018 */     if (saveVersion < 1 && (parametres & 0x2) != 0) {
/* 1019 */       in.readInt();
/*      */     }
/* 1021 */     overlayBiomeBuffer[2] = -1; overlayBiomeBuffer[1] = -1;
/* 1022 */     overlayBiomeBuffer[0] = (byte)(parametres >> 8 & 0x3);
/* 1023 */     if (overlayBiomeBuffer[0] == 2 || (parametres & 0x4) != 0) {
/* 1024 */       overlayBiomeBuffer[0] = 2;
/* 1025 */       overlayBiomeBuffer[2] = in.readInt();
/* 1026 */       if (overlayBiomeBuffer[2] == -1)
/* 1027 */         overlayBiomeBuffer[0] = 0; 
/*      */     } 
/* 1029 */     if ((parametres & 0x8) != 0)
/* 1030 */       opacity = in.readInt(); 
/* 1031 */     byte light = (byte)(parametres >> 4 & 0xF);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1037 */     this.overlayBuilder.build(state, overlayBiomeBuffer, opacity, light, world);
/*      */   }
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isRegionDetectionComplete() {
/* 1118 */     return this.regionDetectionComplete;
/*      */   }
/*      */   
/*      */   public void setRegionDetectionComplete(boolean regionDetectionComplete) {
/* 1122 */     this.regionDetectionComplete = regionDetectionComplete;
/*      */   }
/*      */   
/*      */   public void requestCache(MapRegion region) {
/* 1126 */     if (!toCacheContains(region)) {
/* 1127 */       synchronized ((region.getDim()).regionsToCache) {
/* 1128 */         (region.getDim()).regionsToCache.add(region);
/*      */       } 
/* 1130 */       if (WorldMap.settings.debug)
/* 1131 */         System.out.println("Requesting cache! " + region.getRegionX() + "_" + region.getRegionZ()); 
/*      */     } 
/*      */   }
/*      */   
/*      */   public MapRegion removeToCache(MapDimension mapDim, int index) {
/* 1136 */     synchronized (mapDim.regionsToCache) {
/* 1137 */       return mapDim.regionsToCache.remove(index);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void removeToCache(MapRegion region) {
/* 1142 */     synchronized ((region.getDim()).regionsToCache) {
/* 1143 */       (region.getDim()).regionsToCache.remove(region);
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean toCacheContains(MapRegion region) {
/* 1148 */     synchronized ((region.getDim()).regionsToCache) {
/* 1149 */       return (region.getDim()).regionsToCache.contains(region);
/*      */     } 
/*      */   }
/*      */   
/*      */   public ArrayList<MapRegion> getToSave() {
/* 1154 */     return this.toSave;
/*      */   }
/*      */   
/*      */   public MapRegion getNextToLoadByViewing() {
/* 1158 */     return this.nextToLoadByViewing;
/*      */   }
/*      */   
/*      */   public void setNextToLoadByViewing(MapRegion nextToLoadByViewing) {
/* 1162 */     this.nextToLoadByViewing = nextToLoadByViewing;
/*      */   }
/*      */   
/*      */   public void safeDelete(Path filePath, String extension) throws IOException {
/* 1166 */     if (!filePath.getFileName().toString().endsWith(extension))
/* 1167 */       throw new RuntimeException("Incorrect file extension: " + filePath); 
/* 1168 */     Files.deleteIfExists(filePath);
/*      */   }
/*      */   
/*      */   public void safeMove(Path fromPath, Path toPath, String fromExtension, String toExtension, CopyOption... options) throws IOException {
/* 1172 */     if (!toPath.getFileName().toString().endsWith(toExtension) || !fromPath.getFileName().toString().endsWith(fromExtension))
/* 1173 */       throw new RuntimeException("Incorrect file extension: " + fromPath + " " + toPath); 
/* 1174 */     Files.move(fromPath, toPath, options);
/*      */   }
/*      */ }


/* Location:              C:\Users\ioanm\Downloads\XaerosWorldMap_1.7.3_Forge_1.12.jar!\xaero\map\file\MapSaveLoad.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */