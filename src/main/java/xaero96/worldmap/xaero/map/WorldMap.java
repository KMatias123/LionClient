/*     */ package xaero.map;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.attribute.FileAttribute;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import net.minecraftforge.common.MinecraftForge;
/*     */ import net.minecraftforge.fml.common.Mod;
/*     */ import net.minecraftforge.fml.common.Mod.EventHandler;
/*     */ import net.minecraftforge.fml.common.Mod.Instance;
/*     */ import net.minecraftforge.fml.common.event.FMLInitializationEvent;
/*     */ import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
/*     */ import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
/*     */ import xaero.deallocator.ByteBufferDeallocator;
/*     */ import xaero.map.biome.MapBiomes;
/*     */ import xaero.map.cache.BlockStateColorTypeCache;
/*     */ import xaero.map.capabilities.ServerWorldCapabilities;
/*     */ import xaero.map.controls.ControlsHandler;
/*     */ import xaero.map.events.Events;
/*     */ import xaero.map.events.FMLEvents;
/*     */ import xaero.map.file.MapSaveLoad;
/*     */ import xaero.map.file.PNGExporter;
/*     */ import xaero.map.file.worldsave.WorldDataHandler;
/*     */ import xaero.map.file.worldsave.WorldDataReader;
/*     */ import xaero.map.graphics.TextureUploadBenchmark;
/*     */ import xaero.map.graphics.TextureUploader;
/*     */ import xaero.map.mods.SupportMods;
/*     */ import xaero.map.mods.gui.WaypointSymbolCreator;
/*     */ import xaero.map.pool.MapTilePool;
/*     */ import xaero.map.pool.TextureUploadPool;
/*     */ import xaero.map.region.OverlayManager;
/*     */ import xaero.map.settings.ModSettings;
/*     */ import xaero.patreon.Patreon4;
/*     */ import xaero.patreon.PatreonMod2;
/*     */ 
/*     */ 
/*     */ @Mod(modid = "xaeroworldmap", name = "Xaero's World Map", version = "1.7.3", clientSideOnly = true, acceptedMinecraftVersions = "[1.12,1.12.2]")
/*     */ public class WorldMap
/*     */ {
/*     */   @Instance("xaeroworldmap")
/*     */   public static WorldMap instance;
/*     */   public static final String versionID = "1.12_1.7.3";
/*     */   public static int newestUpdateID;
/*     */   public static boolean isOutdated = true;
/*  51 */   public static String fileLayout = "XaerosWorldMap_&mod_Forge_&mc.jar";
/*  52 */   public static String fileLayoutID = "worldmap";
/*     */   public static String latestVersion;
/*     */   
/*     */   public static PatreonMod2 getPatreon() {
/*  56 */     return (PatreonMod2)Patreon4.mods.get(fileLayoutID);
/*     */   }
/*     */   
/*     */   public static Events events;
/*     */   public static FMLEvents fmlEvents;
/*     */   public static ControlsHandler ch;
/*     */   public static WaypointSymbolCreator waypointSymbolCreator;
/*  63 */   public static final ResourceLocation guiTextures = new ResourceLocation("xaeroworldmap", "gui/gui.png");
/*  64 */   public static File oldOptionsFile = new File("./xaeroworldmap.txt");
/*  65 */   public static File oldSaveFolder = new File("./mods/XaeroWorldMap");
/*  66 */   public static File oldSaveFolder2 = new File("./config/XaeroWorldMap");
/*     */   public static ModSettings settings;
/*  68 */   public static int globalVersion = 1;
/*     */   
/*     */   @EventHandler
/*     */   public void preInit(FMLPreInitializationEvent event) throws IOException {
/*  72 */     if (event.getSourceFile().getName().endsWith(".jar"))
/*  73 */       modJAR = event.getSourceFile(); 
/*  74 */     Path gameDir = (new File("./")).toPath().toAbsolutePath();
/*  75 */     configFolder = event.getModConfigurationDirectory();
/*  76 */     optionsFile = configFolder.toPath().resolve("xaeroworldmap.txt").toFile();
/*  77 */     saveFolder = gameDir.resolve("XaeroWorldMap").toFile();
/*  78 */     Path oldSaveFolder3 = configFolder.toPath().getParent().resolve("XaeroWorldMap");
/*  79 */     if (oldOptionsFile.exists() && !optionsFile.exists())
/*  80 */       Files.move(oldOptionsFile.toPath(), optionsFile.toPath(), new java.nio.file.CopyOption[0]); 
/*  81 */     if (oldSaveFolder.exists() && !saveFolder.exists())
/*  82 */       Files.move(oldSaveFolder.toPath(), saveFolder.toPath(), new java.nio.file.CopyOption[0]); 
/*  83 */     if (oldSaveFolder2.exists() && !saveFolder.exists())
/*  84 */       Files.move(oldSaveFolder2.toPath(), saveFolder.toPath(), new java.nio.file.CopyOption[0]); 
/*  85 */     if (oldSaveFolder3.toFile().exists() && !saveFolder.exists())
/*  86 */       Files.move(oldSaveFolder3, saveFolder.toPath(), new java.nio.file.CopyOption[0]); 
/*  87 */     if (!saveFolder.exists())
/*  88 */       Files.createDirectories(saveFolder.toPath(), (FileAttribute<?>[])new FileAttribute[0]); 
/*     */   }
/*  90 */   public static File modJAR = null;
/*     */   
/*     */   public static File configFolder;
/*     */   
/*     */   public static File optionsFile;
/*     */   public static File saveFolder;
/*     */   public static Thread mapProcessorThread;
/*     */   public static Thread mapWriterThread;
/*     */   
/*     */   @EventHandler
/*     */   public void load(FMLInitializationEvent event) throws IOException, ClassNotFoundException, NoSuchMethodException, SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
/* 101 */     Patreon4.checkPatreon();
/* 102 */     Patreon4.rendersCapes = fileLayoutID;
/* 103 */     settings = new ModSettings();
/* 104 */     settings.loadSettings();
/* 105 */     checkModVersion();
/* 106 */     if (Patreon4.patronPledge >= 5 && isOutdated) {
/* 107 */       (getPatreon()).modJar = modJAR;
/* 108 */       (getPatreon()).currentVersion = "1.12_1.7.3";
/* 109 */       (getPatreon()).latestVersion = latestVersion;
/* 110 */       Patreon4.addOutdatedMod(getPatreon());
/*     */     } 
/* 112 */     waypointSymbolCreator = new WaypointSymbolCreator();
/* 113 */     ch = new ControlsHandler();
/* 114 */     events = new Events();
/* 115 */     fmlEvents = new FMLEvents();
/* 116 */     MinecraftForge.EVENT_BUS.register(events);
/* 117 */     MinecraftForge.EVENT_BUS.register(fmlEvents);
/* 118 */     ServerWorldCapabilities.registerCapabilities();
/*     */     
/* 120 */     ByteBufferDeallocator bufferDeallocator = new ByteBufferDeallocator();
/* 121 */     MapTilePool tilePool = new MapTilePool();
/* 122 */     OverlayManager overlayManager = new OverlayManager();
/* 123 */     PNGExporter pngExporter = new PNGExporter(configFolder.toPath().getParent().resolve("map exports"));
/* 124 */     MapSaveLoad mapSaveLoad = new MapSaveLoad(overlayManager, pngExporter);
/* 125 */     BlockStateColorTypeCache blockStateColorTypeCache = new BlockStateColorTypeCache();
/* 126 */     MapWriter mapWriter = new MapWriter(overlayManager, blockStateColorTypeCache);
/* 127 */     MapLimiter mapLimiter = new MapLimiter();
/* 128 */     TextureUploadPool.Normal normalTextureUploadPool = new TextureUploadPool.Normal(256);
/* 129 */     TextureUploadPool.NormalWithDownload normalWithDownloadTextureUploadPool = new TextureUploadPool.NormalWithDownload(256);
/* 130 */     TextureUploadPool.Compressed compressedTextureUploadPool = new TextureUploadPool.Compressed(256);
/*     */     
/* 132 */     TextureUploadBenchmark textureUploadBenchmark = new TextureUploadBenchmark(new int[] { 512, 512, 512 });
/* 133 */     TextureUploader textureUploader = new TextureUploader(normalTextureUploadPool, normalWithDownloadTextureUploadPool, compressedTextureUploadPool, textureUploadBenchmark);
/* 134 */     WorldDataHandler worldDataHandler = new WorldDataHandler(new WorldDataReader(overlayManager, blockStateColorTypeCache));
/* 135 */     MapProcessor mapProcessor = new MapProcessor(mapSaveLoad, mapWriter, mapLimiter, bufferDeallocator, tilePool, overlayManager, textureUploader, worldDataHandler, new MapBiomes());
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 152 */     (mapProcessorThread = new Thread(mapProcessor)).start();
/*     */   }
/*     */   
/*     */   @EventHandler
/*     */   public void postInit(FMLPostInitializationEvent event) {
/* 157 */     MapProcessor.instance.getMapWriter().getColorTypeCache().updateGrassColor();
/* 158 */     SupportMods.load();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void checkModVersion() {
/* 164 */     String s = "http://data.chocolateminecraft.com/Versions/WorldMap.txt";
/* 165 */     s = s.replaceAll(" ", "%20");
/*     */     
/*     */     try {
/* 168 */       URL url = new URL(s);
/*     */       
/* 170 */       URLConnection conn = url.openConnection();
/* 171 */       conn.setConnectTimeout(900);
/* 172 */       BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
/* 173 */       String line = reader.readLine();
/* 174 */       if (line != null) {
/* 175 */         newestUpdateID = Integer.parseInt(line);
/* 176 */         if (!ModSettings.updateNotification || newestUpdateID == ModSettings.ignoreUpdate) {
/* 177 */           isOutdated = false;
/* 178 */           reader.close();
/*     */           return;
/*     */         } 
/*     */       } 
/* 182 */       String[] current = "1.12_1.7.3".split("_");
/* 183 */       while ((line = reader.readLine()) != null) {
/* 184 */         if (line.equals("1.12_1.7.3")) {
/* 185 */           isOutdated = false; break;
/*     */         } 
/* 187 */         if (Patreon4.patronPledge >= 5 && line.startsWith(current[0] + "_")) {
/* 188 */           String[] args = line.split("_");
/* 189 */           if (args.length == current.length) {
/* 190 */             boolean sameType = true;
/* 191 */             if (current.length > 2)
/* 192 */               for (int i = 2; i < current.length && sameType; i++) {
/* 193 */                 if (!args[i].equals(current[i]))
/* 194 */                   sameType = false; 
/* 195 */               }   if (sameType)
/* 196 */               latestVersion = args[1]; 
/*     */           } 
/*     */         } 
/* 199 */       }  reader.close();
/* 200 */     } catch (Exception e) {
/* 201 */       isOutdated = false;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\ioanm\Downloads\XaerosWorldMap_1.7.3_Forge_1.12.jar!\xaero\map\WorldMap.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */