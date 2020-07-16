/*      */ package xaero.common.settings;
/*      */ 
/*      */ import java.awt.Color;
/*      */ import java.io.BufferedReader;
/*      */ import java.io.File;
/*      */ import java.io.FileInputStream;
/*      */ import java.io.FileOutputStream;
/*      */ import java.io.FileReader;
/*      */ import java.io.FileWriter;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStreamReader;
/*      */ import java.io.OutputStreamWriter;
/*      */ import java.io.PrintWriter;
/*      */ import java.nio.charset.StandardCharsets;
/*      */ import java.nio.file.CopyOption;
/*      */ import java.nio.file.FileAlreadyExistsException;
/*      */ import java.nio.file.Files;
/*      */ import java.nio.file.Path;
/*      */ import java.nio.file.StandardCopyOption;
/*      */ import java.nio.file.attribute.FileAttribute;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.Map;
/*      */ import java.util.stream.Stream;
/*      */ import net.minecraft.client.Minecraft;
/*      */ import net.minecraft.client.gui.GuiScreen;
/*      */ import net.minecraft.client.resources.I18n;
/*      */ import net.minecraft.client.settings.KeyBinding;
/*      */ import net.minecraft.entity.player.EntityPlayer;
/*      */ import net.minecraft.item.Item;
/*      */ import net.minecraft.server.integrated.IntegratedServer;
/*      */ import net.minecraft.util.ResourceLocation;
/*      */ import xaero.common.IXaeroMinimap;
/*      */ import xaero.common.events.ForgeEventHandler;
/*      */ import xaero.common.file.SimpleBackup;
/*      */ import xaero.common.gui.GuiSlimeSeed;
/*      */ import xaero.common.interfaces.Interface;
/*      */ import xaero.common.minimap.MinimapProcessor;
/*      */ import xaero.common.minimap.waypoints.Waypoint;
/*      */ import xaero.common.minimap.waypoints.WaypointSet;
/*      */ import xaero.common.minimap.waypoints.WaypointWorld;
/*      */ import xaero.common.minimap.waypoints.WaypointWorldContainer;
/*      */ import xaero.patreon.Patreon4;
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
/*      */ public class ModSettings
/*      */ {
/*      */   public static int defaultSettings;
/*      */   public static int ignoreUpdate;
/*      */   public static final String format = "§";
/*      */   private IXaeroMinimap modMain;
/*      */   
/*      */   public ModSettings(IXaeroMinimap modMain) {
/*   94 */     this.minimap = true;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  103 */     this.zoom = 2;
/*  104 */     this.zooms = new float[] { 1.0F, 2.0F, 3.0F, 4.0F, 5.0F };
/*  105 */     this.entityAmount = 1;
/*  106 */     this.showPlayers = true;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  112 */     this.showMobs = true;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  118 */     this.showHostile = true;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  124 */     this.showItems = true;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  130 */     this.showOther = true;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  136 */     this.caveMaps = 1;
/*  137 */     this.caveZoom = 2;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  143 */     this.showOtherTeam = true;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  149 */     this.showWaypoints = true;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  155 */     this.deathpoints = true;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  161 */     this.oldDeathpoints = true;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  167 */     this.chunkGrid = -1;
/*      */     
/*  169 */     this.slimeChunks = false;
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
/*  202 */     this.showIngameWaypoints = true;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  212 */     this.showCoords = true;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  218 */     this.lockNorth = false;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  224 */     this.antiAliasing = true;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  230 */     this.displayRedstone = true;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  237 */     this.mapSafeMode = false;
/*  238 */     this.distance = 1;
/*      */     
/*  240 */     this.blockColours = 0;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  248 */     this.lighting = true;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  255 */     this.compassOverWaypoints = false;
/*  256 */     this.mapSize = -1;
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
/*  286 */     this.playersColor = 15;
/*  287 */     this.mobsColor = 14;
/*  288 */     this.hostileColor = 14;
/*  289 */     this.itemsColor = 12;
/*  290 */     this.otherColor = 5;
/*  291 */     this.otherTeamColor = -1;
/*  292 */     this.minimapOpacity = 100.0F;
/*  293 */     this.waypointsScale = 1.0F;
/*  294 */     this.dotsScale = 1.0F;
/*      */     
/*  296 */     this.showBiome = false;
/*      */     
/*  298 */     this.showEntityHeight = true;
/*  299 */     this.showFlowers = true;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  306 */     this.keepWaypointNames = false;
/*  307 */     this.waypointsDistance = 0.0F;
/*  308 */     this.waypointsDistanceMin = 0.0F;
/*  309 */     this.waypointTp = "tp";
/*  310 */     this.arrowScale = 1.5F;
/*  311 */     this.arrowColour = 0;
/*  312 */     this.arrowColourNames = new String[] { "gui.xaero_red", "gui.xaero_green", "gui.xaero_blue", "gui.xaero_yellow", "gui.xaero_purple", "gui.xaero_white", "gui.xaero_black", "gui.xaero_preset_classic" };
/*      */     
/*  314 */     this.arrowColours = new float[][] { { 0.8F, 0.1F, 0.1F, 1.0F }, { 0.09F, 0.57F, 0.0F, 1.0F }, { 0.0F, 0.55F, 1.0F, 1.0F }, { 1.0F, 0.93F, 0.0F, 1.0F }, { 0.73F, 0.33F, 0.83F, 1.0F }, { 1.0F, 1.0F, 1.0F, 1.0F }, { 0.0F, 0.0F, 0.0F, 1.0F }, { 0.4588F, 0.0F, 0.0F, 1.0F } };
/*      */ 
/*      */     
/*  317 */     this.smoothDots = true;
/*      */ 
/*      */ 
/*      */     
/*  321 */     this.playerHeads = false;
/*      */ 
/*      */ 
/*      */     
/*  325 */     this.heightLimit = 20;
/*  326 */     this.worldMap = true;
/*      */ 
/*      */ 
/*      */     
/*  330 */     this.terrainDepth = true;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  337 */     this.terrainSlopes = true;
/*  338 */     this.terrainSlopesExperiment = false;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  345 */     this.mainEntityAs = 0;
/*  346 */     this.blockTransparency = true;
/*  347 */     this.waypointOpacityIngame = 80;
/*  348 */     this.waypointOpacityMap = 90;
/*  349 */     this.allowWrongWorldTeleportation = false;
/*  350 */     this.hideWorldNames = 1;
/*  351 */     this.openSlimeSettings = true;
/*  352 */     this.alwaysShowDistance = false;
/*  353 */     this.playerNames = true;
/*  354 */     this.showLightLevel = false;
/*  355 */     this.renderLayerIndex = 1;
/*  356 */     this.showTime = 0;
/*  357 */     this.differentiateByServerAddress = true;
/*  358 */     this.biomeColorsVanillaMode = false;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  365 */     this.lookingAtAngle = 20;
/*  366 */     this.lookingAtAngleVertical = 180;
/*  367 */     this.centeredEnlarged = false;
/*  368 */     this.zoomedOutEnlarged = false;
/*  369 */     this.entityNametags = false;
/*  370 */     this.minimapTextAlign = 0;
/*      */     
/*  372 */     this.waypointsMutualEdit = true;
/*  373 */     this.compass = true;
/*  374 */     this.caveMapsDepth = 30;
/*  375 */     this.hideWaypointCoordinates = false;
/*  376 */     this.renderAllSets = false;
/*  377 */     this.playerArrowOpacity = 100; this.modMain = modMain; defaultSettings = modMain.getVersionID().endsWith("fair") ? 16188159 : Integer.MAX_VALUE; if (serverSettings == 0) serverSettings = defaultSettings; 
/*      */   }
/*      */   public static final String[] ENCHANT_COLORS = new String[] { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };
/*  380 */   public static final String[] ENCHANT_COLOR_NAMES = new String[] { "gui.xaero_black", "gui.xaero_dark_blue", "gui.xaero_dark_green", "gui.xaero_dark_aqua", "gui.xaero_dark_red", "gui.xaero_dark_purple", "gui.xaero_gold", "gui.xaero_gray", "gui.xaero_dark_gray", "gui.xaero_blue", "gui.xaero_green", "gui.xaero_aqua", "gui.xaero_red", "gui.xaero_purple", "gui.xaero_yellow", "gui.xaero_white" }; public static final int[] COLORS = new int[] { (new Color(0, 0, 0, 255)).hashCode(), (new Color(0, 0, 170, 255)).hashCode(), (new Color(0, 170, 0, 255)).hashCode(), (new Color(0, 170, 170, 255)).hashCode(), (new Color(170, 0, 0, 255)).hashCode(), (new Color(170, 0, 170, 255)).hashCode(), (new Color(255, 170, 0, 255)).hashCode(), (new Color(170, 170, 170, 255)).hashCode(), (new Color(85, 85, 85, 255)).hashCode(), (new Color(85, 85, 255, 255)).hashCode(), (new Color(85, 255, 85, 255)).hashCode(), (new Color(85, 255, 255, 255)).hashCode(), (new Color(255, 0, 0, 255)).hashCode(), (new Color(255, 85, 255, 255)).hashCode(), (new Color(255, 255, 85, 255)).hashCode(), (new Color(255, 255, 255, 255)).hashCode() }; public static final String[] MINIMAP_SIZE = new String[] { "gui.xaero_tiny", "gui.xaero_small", "gui.xaero_medium", "gui.xaero_large" }; public static int serverSettings; public static KeyBinding keyBindZoom = new KeyBinding("gui.xaero_zoom_in", 23, "Xaero's Minimap"); public static KeyBinding keyBindZoom1 = new KeyBinding("gui.xaero_zoom_out", 24, "Xaero's Minimap"); public static KeyBinding newWaypoint = new KeyBinding("gui.xaero_new_waypoint", 48, "Xaero's Minimap"); public static KeyBinding keyWaypoints = new KeyBinding("gui.xaero_waypoints_key", 22, "Xaero's Minimap"); public static KeyBinding keyLargeMap = new KeyBinding("gui.xaero_enlarge_map", 44, "Xaero's Minimap"); public static KeyBinding keyToggleMap = new KeyBinding("gui.xaero_toggle_map", 0, "Xaero's Minimap"); public static KeyBinding keyToggleWaypoints = new KeyBinding("gui.xaero_toggle_waypoints", 0, "Xaero's Minimap"); public static KeyBinding keyToggleMapWaypoints = new KeyBinding("gui.xaero_toggle_map_waypoints", 0, "Xaero's Minimap"); public static KeyBinding keyToggleSlimes = new KeyBinding("gui.xaero_toggle_slime", 0, "Xaero's Minimap"); public static KeyBinding keyToggleGrid = new KeyBinding("gui.xaero_toggle_grid", 0, "Xaero's Minimap"); public static KeyBinding keyInstantWaypoint = new KeyBinding("gui.xaero_instant_waypoint", 78, "Xaero's Minimap"); public static KeyBinding keySwitchSet = new KeyBinding("gui.xaero_switch_waypoint_set", 0, "Xaero's Minimap"); public static KeyBinding keyAllSets = new KeyBinding("gui.xaero_display_all_sets", 0, "Xaero's Minimap"); private boolean minimap; public boolean isKeyRepeat(KeyBinding kb) { if (kb == this.modMain.getSettingsKey() || kb == keyWaypoints || kb == newWaypoint || kb == keyLargeMap || kb == keyToggleMap || kb == keyToggleWaypoints || kb == keyToggleMapWaypoints || kb == keyToggleSlimes || kb == keyToggleGrid || kb == keyInstantWaypoint || kb == keySwitchSet || kb == keyAllSets) return false;  return true; } public boolean getMinimap() { return (this.minimap && !minimapDisabled() && (minimapItem == null || (Minecraft.func_71410_x()).field_71439_g == null || MinimapProcessor.hasMinimapItem((EntityPlayer)(Minecraft.func_71410_x()).field_71439_g))); } public static String minimapItemId = null; public static Item minimapItem = null; public int zoom; public float[] zooms; public int entityAmount; private boolean showPlayers; private boolean showMobs; private boolean showHostile; private boolean showItems; private boolean showOther; public int caveMaps; public int caveZoom; private boolean showOtherTeam; private boolean showWaypoints; private boolean deathpoints; private boolean oldDeathpoints; public int chunkGrid; public boolean slimeChunks; public boolean getShowPlayers() { return (this.showPlayers && !minimapDisplayPlayersDisabled()); } public boolean getShowMobs() { return (this.showMobs && !minimapDisplayMobsDisabled()); } public boolean getShowHostile() { return (this.showHostile && !minimapDisplayMobsDisabled()); } public boolean getShowItems() { return (this.showItems && !minimapDisplayItemsDisabled()); } public boolean getShowOther() { return (this.showOther && !minimapDisplayOtherDisabled()); } public boolean getCaveMaps() { return (this.caveMaps > 0 && !caveMapsDisabled()); } public boolean getShowOtherTeam() { return (this.showOtherTeam && !showOtherTeamDisabled()); } public boolean getShowWaypoints() { return (this.showWaypoints && !showWaypointsDisabled()); } public boolean getDeathpoints() { return (this.deathpoints && !deathpointsDisabled()); } public boolean getOldDeathpoints() { return this.oldDeathpoints; } private static HashMap<String, Long> serverSlimeSeeds = new HashMap<>(); private boolean showIngameWaypoints; private boolean showCoords; private boolean lockNorth; private boolean antiAliasing; private boolean displayRedstone; public boolean mapSafeMode; public int distance; public void setSlimeChunksSeed(long seed) { serverSlimeSeeds.put(this.modMain.getWaypointsManager().getCurrentContainerAndWorldID(), Long.valueOf(seed)); } public Long getSlimeChunksSeed() { IntegratedServer integratedServer = Minecraft.func_71410_x().func_71401_C(); if (integratedServer == null) return serverSlimeSeeds.get(this.modMain.getWaypointsManager().getCurrentContainerAndWorldID());  try { if ((integratedServer.func_130014_f_()).field_73011_w.getDimension() != 0) return null;  } catch (ArrayIndexOutOfBoundsException e) { return null; }  long seed = integratedServer.func_130014_f_().func_72905_C(); return Long.valueOf(seed); } public boolean customSlimeSeedNeeded() { return (!((Minecraft.func_71410_x()).field_71462_r instanceof GuiSlimeSeed) && Minecraft.func_71410_x().func_71401_C() == null && (Minecraft.func_71410_x()).field_71441_e != null); } public boolean getSlimeChunks() { return (this.slimeChunks && (Minecraft.func_71410_x().func_71401_C() != null || getSlimeChunksSeed() != null)); } public boolean getShowIngameWaypoints() { return (this.showIngameWaypoints && !showWaypointsDisabled() && (minimapItem == null || (Minecraft.func_71410_x()).field_71439_g == null || MinimapProcessor.hasMinimapItem((EntityPlayer)(Minecraft.func_71410_x()).field_71439_g))); } public boolean waypointsGUI() { return ((Minecraft.func_71410_x()).field_71439_g != null && this.modMain.getWaypointsManager().getWaypoints() != null && (minimapItem == null || (Minecraft.func_71410_x()).field_71439_g == null || MinimapProcessor.hasMinimapItem((EntityPlayer)(Minecraft.func_71410_x()).field_71439_g))); } public boolean getShowCoords() { return this.showCoords; } public boolean getLockNorth() { return (this.lockNorth || this.modMain.getInterfaces().getMinimap().isEnlargedMap()); } public boolean getAntiAliasing() { return (this.antiAliasing && ((!this.modMain.getInterfaces().getMinimap().getMinimapFBORenderer().isTriedFBO() && !this.mapSafeMode) || this.modMain.getInterfaces().getMinimap().usingFBO())); } public boolean getDisplayRedstone() { if (this.modMain.getSupportMods().shouldUseWorldMapChunks()) return this.displayRedstone;  return this.displayRedstone; } public static final String[] distanceTypes = new String[] { "gui.xaero_off", "gui.xaero_looking_at", "gui.xaero_all" }; private int blockColours; public int getBlockColours() { if (this.modMain.getSupportMods().shouldUseWorldMapChunks()) return (this.modMain.getSupportMods()).worldmapSupport.getWorldMapColours();  return this.blockColours; } public void convertWaypointFilesToFolders() throws IOException { Stream<Path> files = Files.list(this.modMain.getWaypointsFolder().toPath());
/*  381 */     Path backupFolder = this.modMain.getWaypointsFolder().toPath().resolve("backup");
/*  382 */     Files.createDirectories(backupFolder, (FileAttribute<?>[])new FileAttribute[0]);
/*  383 */     if (files != null)
/*  384 */     { Object[] fileArray = files.toArray();
/*  385 */       for (int i = 0; i < fileArray.length; i++) {
/*  386 */         Path filePath = (Path)fileArray[i];
/*  387 */         if (!filePath.toFile().isDirectory()) {
/*      */           
/*  389 */           String fileName = filePath.getFileName().toString();
/*  390 */           if (fileName.endsWith(".txt") && fileName.contains("_")) {
/*      */             
/*  392 */             int lastUnderscore = fileName.lastIndexOf("_");
/*  393 */             if (!fileName.startsWith("Multiplayer_") && !fileName.startsWith("Realms_"))
/*  394 */               fileName = fileName.substring(0, lastUnderscore).replace("_", "%us%") + fileName.substring(lastUnderscore); 
/*  395 */             String noExtension = fileName.substring(0, fileName.lastIndexOf("."));
/*  396 */             Path folderPath = filePath.getParent().resolve(noExtension);
/*  397 */             Path fixedFilePath = folderPath.resolve("waypoints.txt");
/*  398 */             Path backupFilePath = backupFolder.resolve(fileName);
/*  399 */             Files.createDirectories(folderPath, (FileAttribute<?>[])new FileAttribute[0]);
/*  400 */             if (!backupFilePath.toFile().exists())
/*  401 */               Files.copy(filePath, backupFilePath, new CopyOption[0]); 
/*      */             
/*  403 */             try { Files.move(filePath, fixedFilePath, new CopyOption[0]); }
/*  404 */             catch (FileAlreadyExistsException e)
/*  405 */             { if (backupFilePath.toFile().exists())
/*  406 */                 Files.deleteIfExists(filePath);  } 
/*      */           } 
/*      */         } 
/*  409 */       }  files.close(); }  }
/*      */   public static final String[] blockColourTypes = new String[] { "gui.xaero_accurate", "gui.xaero_vanilla" };
/*      */   private boolean lighting;
/*      */   public boolean compassOverWaypoints;
/*      */   private int mapSize;
/*  414 */   public int playersColor; public int mobsColor; public int hostileColor; public int itemsColor; public int otherColor; public int otherTeamColor; public float minimapOpacity; public float waypointsScale; public float dotsScale; public boolean getLighting() { if (this.modMain.getSupportMods().shouldUseWorldMapChunks()) return (this.modMain.getSupportMods()).worldmapSupport.getWorldMapLighting();  return this.lighting; } public int getMinimapSize() { if (this.mapSize > -1) return this.mapSize;  int height = (Minecraft.func_71410_x()).field_71440_d; int width = (Minecraft.func_71410_x()).field_71443_c; int size = (int)(((height <= width) ? height : width) / getMinimapScale()); if (size <= 480) return 0;  if (size <= 720) return 1;  if (size <= 1080) return 2;  return 3; } public float getMinimapScale() { int height = (Minecraft.func_71410_x()).field_71440_d; int width = (Minecraft.func_71410_x()).field_71443_c; int size = (height <= width) ? height : width; if (size > 1500) return 2.0F;  return 1.0F; } public static boolean settingsButton = false; public boolean showBiome; public static boolean updateNotification = true; public boolean showEntityHeight; private boolean showFlowers; public boolean keepWaypointNames; public float waypointsDistance; public float waypointsDistanceMin; public String waypointTp; public float arrowScale; public int arrowColour; public String[] arrowColourNames; public float[][] arrowColours; public boolean smoothDots; public boolean playerHeads; public int heightLimit; private boolean worldMap; private boolean terrainDepth; private boolean terrainSlopes; private boolean terrainSlopesExperiment; public int mainEntityAs; public boolean blockTransparency; public int waypointOpacityIngame; public int waypointOpacityMap; public boolean allowWrongWorldTeleportation; public int hideWorldNames; public boolean openSlimeSettings; public boolean alwaysShowDistance; private boolean playerNames; public boolean showLightLevel; public int renderLayerIndex; public int showTime; public boolean differentiateByServerAddress; private boolean biomeColorsVanillaMode; public int lookingAtAngle; public int lookingAtAngleVertical; public boolean centeredEnlarged; public boolean zoomedOutEnlarged; public boolean entityNametags; public int minimapTextAlign; public boolean showAngles; public boolean waypointsMutualEdit; public boolean compass; public int caveMapsDepth; public boolean hideWaypointCoordinates; public boolean renderAllSets; public int playerArrowOpacity; public boolean getShowFlowers() { if (this.modMain.getSupportMods().shouldUseWorldMapChunks()) return (this.modMain.getSupportMods()).worldmapSupport.getWorldMapFlowers();  return this.showFlowers; } public boolean getSmoothDots() { return (this.smoothDots && ((!this.modMain.getInterfaces().getMinimap().getMinimapFBORenderer().isTriedFBO() && !this.mapSafeMode) || this.modMain.getInterfaces().getMinimap().usingFBO())); } public boolean getPlayerHeads() { return (this.playerHeads && ((!this.modMain.getInterfaces().getMinimap().getMinimapFBORenderer().isTriedFBO() && !this.mapSafeMode) || this.modMain.getInterfaces().getMinimap().usingFBO())); } public boolean getUseWorldMap() { return (this.worldMap && ((!this.modMain.getInterfaces().getMinimap().getMinimapFBORenderer().isTriedFBO() && !this.mapSafeMode) || this.modMain.getInterfaces().getMinimap().usingFBO())); } public boolean getTerrainDepth() { if (this.modMain.getSupportMods().shouldUseWorldMapChunks()) return (this.modMain.getSupportMods()).worldmapSupport.getWorldMapTerrainDepth();  return this.terrainDepth; } public boolean getTerrainSlopes() { if (this.modMain.getSupportMods().shouldUseWorldMapChunks()) return (this.modMain.getSupportMods()).worldmapSupport.getWorldMapTerrainSlopes();  return this.terrainSlopes; } public boolean getBiomeColorsVanillaMode() { if (this.modMain.getSupportMods().shouldUseWorldMapChunks()) return (this.modMain.getSupportMods()).worldmapSupport.getWorldMapBiomeColorsVanillaMode();  return this.biomeColorsVanillaMode; } public void convertWaypointFoldersToSingleFolder() throws IOException { Stream<Path> folders = Files.list(this.modMain.getWaypointsFolder().toPath());
/*  415 */     if (folders != null) {
/*  416 */       Object[] folderArray = folders.toArray();
/*  417 */       for (int i = 0; i < folderArray.length; i++) {
/*  418 */         Path folderPath = (Path)folderArray[i];
/*  419 */         if (folderPath.toFile().isDirectory()) {
/*      */           
/*  421 */           String folderName = folderPath.getFileName().toString();
/*  422 */           String[] folderArgs = folderName.split("_");
/*  423 */           if (folderArgs.length > 1) {
/*  424 */             String lastArg = folderArgs[folderArgs.length - 1];
/*  425 */             if (lastArg.equals("null") || (lastArg.startsWith("DIM") && lastArg.length() > 3)) {
/*  426 */               String dimensionName; int dimensionId = lastArg.equals("null") ? 0 : Integer.parseInt(lastArg.substring(3));
/*      */               
/*      */               try {
/*  429 */                 dimensionName = this.modMain.getWaypointsManager().getDimensionDirectoryName(dimensionId);
/*  430 */               } catch (IllegalArgumentException iae) {}
/*      */ 
/*      */               
/*  433 */               Path correctFolder = folderPath.getParent().resolve(folderName.substring(0, folderName.lastIndexOf("_"))).resolve(dimensionName);
/*  434 */               Files.createDirectories(correctFolder, (FileAttribute<?>[])new FileAttribute[0]);
/*  435 */               Stream<Path> files = Files.list(folderPath);
/*  436 */               if (files != null) {
/*  437 */                 Object[] filesArray = files.toArray();
/*  438 */                 for (int j = 0; j < filesArray.length; j++) {
/*  439 */                   Path filePath = (Path)filesArray[j];
/*  440 */                   if (!filePath.toFile().isDirectory()) {
/*      */                     
/*  442 */                     Path correctFilePath = correctFolder.resolve(filePath.getFileName());
/*  443 */                     Files.move(filePath, correctFilePath, new CopyOption[0]);
/*      */                   } 
/*  445 */                 }  files.close();
/*      */               } 
/*  447 */               Stream<Path> deleteCheck = Files.list(folderPath);
/*  448 */               if (deleteCheck.count() == 0L)
/*  449 */                 Files.deleteIfExists(folderPath); 
/*  450 */               deleteCheck.close();
/*      */             } 
/*      */           } 
/*      */         } 
/*  454 */       }  folders.close();
/*      */     }  }
/*      */ 
/*      */   
/*      */   public static void copyTempFilesBack(Path folder) throws IOException {
/*  459 */     Stream<Path> tempFiles = Files.list(folder);
/*  460 */     if (tempFiles != null) {
/*  461 */       Iterator<Path> tempFilesIter = tempFiles.iterator();
/*  462 */       while (tempFilesIter.hasNext()) {
/*  463 */         Path tempFile = tempFilesIter.next();
/*  464 */         Path newLocation = folder.getParent().resolve(tempFile.getFileName());
/*  465 */         if (!Files.exists(newLocation, new java.nio.file.LinkOption[0]) || Files.size(newLocation) == 0L) {
/*  466 */           Files.move(tempFile, newLocation, new CopyOption[] { StandardCopyOption.REPLACE_EXISTING }); continue;
/*      */         } 
/*  468 */         Path backupPath = newLocation.resolveSibling("backup").resolve(tempFile.getFileName());
/*  469 */         Files.createDirectories(backupPath.getParent(), (FileAttribute<?>[])new FileAttribute[0]);
/*  470 */         Files.move(tempFile, backupPath, new CopyOption[] { StandardCopyOption.REPLACE_EXISTING });
/*      */       } 
/*      */       
/*  473 */       tempFiles.close();
/*      */     } 
/*  475 */     Files.delete(folder);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void loadAllWaypoints() throws IOException {
/*  484 */     Path waypointsFolderPath = this.modMain.getWaypointsFolder().toPath();
/*  485 */     if (!Files.exists(waypointsFolderPath, new java.nio.file.LinkOption[0]))
/*  486 */       Files.createDirectories(waypointsFolderPath, (FileAttribute<?>[])new FileAttribute[0]); 
/*  487 */     convertWaypointFilesToFolders();
/*  488 */     convertWaypointFoldersToSingleFolder();
/*  489 */     Stream<Path> folders = Files.list(this.modMain.getWaypointsFolder().toPath());
/*  490 */     if (folders != null) {
/*  491 */       Object[] paths = folders.toArray();
/*  492 */       for (int i = 0; i < paths.length; i++) {
/*  493 */         Path folderPath = (Path)paths[i];
/*  494 */         Path tempToAdd = folderPath.resolve("temp_to_add");
/*  495 */         if (Files.exists(tempToAdd, new java.nio.file.LinkOption[0]))
/*  496 */           copyTempFilesBack(tempToAdd); 
/*  497 */         if (folderPath.toFile().isDirectory()) {
/*  498 */           String folderName = folderPath.getFileName().toString();
/*  499 */           if (!folderName.equals("backup")) {
/*      */             
/*  501 */             Stream<Path> filesOrFolders = Files.list(folderPath);
/*  502 */             if (filesOrFolders != null)
/*      */             
/*  504 */             { Object[] fileArray = filesOrFolders.toArray();
/*  505 */               for (int j = 0; j < fileArray.length; j++) {
/*  506 */                 Path fileOrFolderPath = (Path)fileArray[j];
/*  507 */                 Path tempToAdd2 = fileOrFolderPath.resolve("temp_to_add");
/*  508 */                 if (Files.exists(tempToAdd2, new java.nio.file.LinkOption[0]))
/*  509 */                   copyTempFilesBack(tempToAdd2); 
/*  510 */                 String fileOrFolderName = fileOrFolderPath.getFileName().toString();
/*  511 */                 if (!fileOrFolderName.startsWith("backup"))
/*      */                 {
/*  513 */                   if (fileOrFolderPath.toFile().isDirectory()) {
/*  514 */                     String dimensionName = fileOrFolderName;
/*  515 */                     String fixedDimensionName = fixDimensionName(dimensionName);
/*  516 */                     boolean toDeleteOld = !fixedDimensionName.equals(dimensionName);
/*  517 */                     String containerKey = folderName + "/" + fixedDimensionName;
/*  518 */                     WaypointWorldContainer wc = this.modMain.getWaypointsManager().addWorldContainer(containerKey);
/*  519 */                     Stream<Path> files = Files.list(fileOrFolderPath);
/*  520 */                     if (files != null) {
/*  521 */                       Object[] filesArray = files.toArray();
/*  522 */                       if (filesArray.length == 0) {
/*  523 */                         this.modMain.getWaypointsManager().removeContainer(containerKey);
/*      */                       } else {
/*  525 */                         for (int k = 0; k < filesArray.length; k++) {
/*  526 */                           Path filePath = (Path)filesArray[k];
/*  527 */                           String fileName = filePath.getFileName().toString();
/*  528 */                           loadWaypointsFile(wc, fileName, filePath.toFile());
/*      */                         } 
/*  530 */                       }  files.close();
/*      */                     } 
/*  532 */                     if (this.modMain.getWaypointsManager().getWorldContainer(folderName).isEmpty())
/*  533 */                       this.modMain.getWaypointsManager().removeContainer(folderName); 
/*  534 */                     if (toDeleteOld) {
/*  535 */                       SimpleBackup.moveToBackup(fileOrFolderPath);
/*  536 */                       saveWorlds(wc.getAllWorlds());
/*      */                     } 
/*  538 */                   } else if (fileOrFolderName.contains("_")) {
/*  539 */                     WaypointWorldContainer wc = this.modMain.getWaypointsManager().addWorldContainer(folderName);
/*  540 */                     loadWaypointsFile(wc, fileOrFolderName, null);
/*      */                   }  } 
/*      */               } 
/*  543 */               filesOrFolders.close(); } 
/*      */           } 
/*      */         } 
/*  546 */       }  folders.close();
/*      */     } 
/*      */   }
/*      */   
/*      */   private String fixDimensionName(String savedDimName) {
/*  551 */     if (savedDimName.equals("Overworld"))
/*  552 */       return "dim%0"; 
/*  553 */     if (savedDimName.equals("Nether"))
/*  554 */       return "dim%-1"; 
/*  555 */     if (savedDimName.equals("The End"))
/*  556 */       return "dim%1"; 
/*  557 */     return savedDimName;
/*      */   }
/*      */   
/*      */   private boolean loadWaypointsFile(WaypointWorldContainer wc, String fileName, File file) throws IOException {
/*  561 */     if (!fileName.endsWith(".txt"))
/*  562 */       return false; 
/*  563 */     String noExtension = fileName.substring(0, fileName.lastIndexOf("."));
/*  564 */     String multiworldId = noExtension;
/*  565 */     if (!noExtension.equals("waypoints")) {
/*  566 */       String[] multiworld = noExtension.split("_");
/*  567 */       multiworldId = multiworld[0];
/*  568 */       String multiworldName = multiworld[1].replace("%us%", "_");
/*  569 */       wc.addName(multiworldId, multiworldName);
/*      */     } 
/*  571 */     WaypointWorld w = wc.addWorld(multiworldId);
/*  572 */     if (w != null)
/*  573 */       loadWaypoints(w, file); 
/*  574 */     return true;
/*      */   }
/*      */   
/*      */   public void saveAllWaypoints() throws IOException {
/*  578 */     String[] keys = (String[])this.modMain.getWaypointsManager().getWaypointMap().keySet().toArray((Object[])new String[0]);
/*  579 */     for (int i = 0; i < keys.length; i++) {
/*  580 */       String key = keys[i];
/*  581 */       WaypointWorldContainer wc = (WaypointWorldContainer)this.modMain.getWaypointsManager().getWaypointMap().get(key);
/*  582 */       saveWorlds(wc.getAllWorlds());
/*      */     } 
/*      */   }
/*      */   
/*      */   public void saveWorlds(ArrayList<WaypointWorld> worlds) throws IOException {
/*  587 */     for (int j = 0; j < worlds.size(); j++) {
/*  588 */       WaypointWorld w = worlds.get(j);
/*  589 */       if (w != null)
/*  590 */         saveWaypoints(w); 
/*      */     } 
/*      */   }
/*      */   
/*      */   public void saveWaypoints(WaypointWorld wpw) throws IOException {
/*  595 */     saveWaypoints(wpw, true);
/*      */   }
/*      */   
/*      */   public File getWaypointsFile(WaypointWorld w) throws IOException {
/*  599 */     File containerFolder = w.getContainer().getDirectory();
/*  600 */     Files.createDirectories(containerFolder.toPath(), (FileAttribute<?>[])new FileAttribute[0]);
/*  601 */     String filePath = containerFolder.getPath() + "/" + w.getId();
/*  602 */     String name = w.getContainer().getName(w.getId());
/*  603 */     if (name != null)
/*  604 */       filePath = filePath + "_" + name.replace("_", "%us%").replace(":", "§§"); 
/*  605 */     return new File(filePath + ".txt");
/*      */   }
/*      */   
/*      */   public void saveWaypoints(WaypointWorld wpw, boolean overwrite) throws IOException {
/*  609 */     if (wpw == null)
/*      */       return; 
/*  611 */     File worldFile = getWaypointsFile(wpw);
/*  612 */     if (worldFile.exists() && !overwrite)
/*      */       return; 
/*  614 */     File worldFileTemp = new File(worldFile.getParentFile(), worldFile.getName() + ".temp");
/*  615 */     OutputStreamWriter output = null;
/*      */     try {
/*  617 */       output = new OutputStreamWriter(new FileOutputStream(worldFileTemp), StandardCharsets.UTF_8);
/*  618 */       Object[] keys = wpw.getSets().keySet().toArray();
/*  619 */       if (keys.length > 1) {
/*  620 */         output.write("sets:" + wpw.getCurrent());
/*  621 */         for (int k = 0; k < keys.length; k++) {
/*  622 */           String name = (String)keys[k];
/*  623 */           if (!name.equals(wpw.getCurrent()))
/*  624 */             output.write(":" + (String)keys[k]); 
/*      */         } 
/*  626 */         output.write("\n");
/*      */       } 
/*  628 */       for (int i = 0; i < keys.length; i++) {
/*  629 */         String name = (String)keys[i];
/*  630 */         WaypointSet set = (WaypointSet)wpw.getSets().get(name);
/*  631 */         if (set != null) {
/*  632 */           ArrayList<Waypoint> list = set.getList();
/*  633 */           for (int k = 0; k < list.size(); k++) {
/*  634 */             Waypoint w = list.get(k);
/*  635 */             if (!w.isTemporary())
/*  636 */               output.write("waypoint:" + w.getNameSafe("§§") + ":" + w.getSymbolSafe("§§") + ":" + w.getX() + ":" + w.getY() + ":" + w
/*  637 */                   .getZ() + ":" + w.getColor() + ":" + w.isDisabled() + ":" + w.getType() + ":" + name + ":" + w
/*  638 */                   .isRotation() + ":" + w.getYaw() + ":" + w.isGlobal() + "\n"); 
/*      */           } 
/*      */         } 
/*      */       } 
/*  642 */       ArrayList<Map.Entry<String, Boolean>> serverWaypointsDisabled = new ArrayList<>(wpw.getServerWaypointsDisabled().entrySet());
/*  643 */       for (int j = 0; j < serverWaypointsDisabled.size(); j++) {
/*  644 */         Map.Entry<String, Boolean> e = serverWaypointsDisabled.get(j);
/*  645 */         output.write("server_waypoint:" + (String)e.getKey() + ":" + e.getValue() + "\n");
/*      */       } 
/*      */     } finally {
/*  648 */       if (output != null)
/*  649 */         output.close(); 
/*      */     } 
/*  651 */     Files.move(worldFileTemp.toPath(), worldFile.toPath(), new CopyOption[] { StandardCopyOption.REPLACE_EXISTING });
/*  652 */     if (wpw.hasSomethingToRemoveOnSave())
/*  653 */       wpw.onSaveCleanup(worldFile); 
/*      */   }
/*      */   
/*      */   public boolean checkWaypointsLine(String[] args, WaypointWorld wpw) {
/*  657 */     if (args[0].equalsIgnoreCase("sets")) {
/*  658 */       wpw.setCurrent(args[1]);
/*  659 */       for (int i = 1; i < args.length; i++) {
/*  660 */         if (wpw.getSets().get(args[i]) == null)
/*  661 */           wpw.getSets().put(args[i], new WaypointSet(args[i])); 
/*  662 */       }  return true;
/*  663 */     }  if (args[0].equalsIgnoreCase("waypoint")) {
/*  664 */       String setName = args[9];
/*  665 */       WaypointSet waypoints = (WaypointSet)wpw.getSets().get(setName);
/*  666 */       if (waypoints == null) {
/*  667 */         wpw.getSets().put(setName, waypoints = new WaypointSet(setName));
/*      */       }
/*  669 */       Waypoint loadWaypoint = new Waypoint(Integer.parseInt(args[3]), Integer.parseInt(args[4]), Integer.parseInt(args[5]), Waypoint.getStringFromStringSafe(args[1], "§§"), Waypoint.getStringFromStringSafe(args[2], "§§"), Integer.parseInt(args[6]));
/*      */       
/*  671 */       if (args.length > 7)
/*  672 */         loadWaypoint.setDisabled(args[7].equals("true")); 
/*  673 */       if (args.length > 8)
/*  674 */         loadWaypoint.setType(Integer.parseInt(args[8])); 
/*  675 */       if (args.length > 10)
/*  676 */         loadWaypoint.setRotation(args[10].equals("true")); 
/*  677 */       if (args.length > 11)
/*  678 */         loadWaypoint.setYaw(Integer.parseInt(args[11])); 
/*  679 */       if (args.length > 12)
/*  680 */         loadWaypoint.setGlobal(args[12].equals("true")); 
/*  681 */       waypoints.getList().add(loadWaypoint);
/*  682 */       return true;
/*  683 */     }  if (args[0].equalsIgnoreCase("server_waypoint")) {
/*  684 */       wpw.getServerWaypointsDisabled().put(args[1], Boolean.valueOf(args[2].equals("true")));
/*      */     }
/*  686 */     return false;
/*      */   }
/*      */   
/*      */   public void loadWaypoints(WaypointWorld wpw, File file) throws IOException {
/*  690 */     if (file == null)
/*  691 */       file = getWaypointsFile(wpw); 
/*  692 */     if (!file.exists())
/*      */       return; 
/*  694 */     BufferedReader reader = null;
/*      */     try {
/*  696 */       reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF8"));
/*      */       String s;
/*  698 */       while ((s = reader.readLine()) != null) {
/*  699 */         String[] args = s.split(":");
/*      */         try {
/*  701 */           checkWaypointsLine(args, wpw);
/*  702 */         } catch (Exception e) {
/*  703 */           System.out.println("Skipping:" + Arrays.toString((Object[])args));
/*  704 */           e.printStackTrace();
/*      */         } 
/*      */       } 
/*      */     } finally {
/*  708 */       if (reader != null) {
/*  709 */         reader.close();
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public String convertToNewConteinerID(String oldID) {
/*  715 */     String parentContainer = oldID.substring(0, oldID.lastIndexOf("_"));
/*  716 */     String dimension = oldID.substring(oldID.lastIndexOf("_") + 1);
/*  717 */     if (dimension.equals("null")) {
/*  718 */       dimension = "Overworld";
/*  719 */     } else if (dimension.startsWith("DIM")) {
/*  720 */       dimension = this.modMain.getWaypointsManager().getDimensionDirectoryName(Integer.parseInt(dimension.substring(3)));
/*  721 */     }  return parentContainer + "/" + fixDimensionName(dimension);
/*      */   }
/*      */   
/*      */   public boolean checkWaypointsLineOLD(String[] args) {
/*  725 */     if (args[0].equalsIgnoreCase("world")) {
/*  726 */       if (!args[1].contains("_"))
/*  727 */         args[1] = args[1] + "_null"; 
/*  728 */       WaypointWorldContainer wc = this.modMain.getWaypointsManager().addWorldContainer(convertToNewConteinerID(args[1]));
/*  729 */       WaypointWorld map = wc.addWorld("waypoints");
/*  730 */       map.setCurrent(args[2]);
/*  731 */       for (int i = 2; i < args.length; i++) {
/*  732 */         if (map.getSets().get(args[i]) == null)
/*  733 */           map.getSets().put(args[i], new WaypointSet(args[i])); 
/*  734 */       }  return true;
/*  735 */     }  if (args[0].equalsIgnoreCase("waypoint")) {
/*  736 */       if (!args[1].contains("_"))
/*  737 */         args[1] = args[1] + "_null"; 
/*  738 */       WaypointWorldContainer wc = this.modMain.getWaypointsManager().addWorldContainer(convertToNewConteinerID(args[1]));
/*  739 */       WaypointWorld map = wc.addWorld("waypoints");
/*  740 */       String setName = "gui.xaero_default";
/*  741 */       if (args.length > 10)
/*  742 */         setName = args[10]; 
/*  743 */       WaypointSet waypoints = (WaypointSet)map.getSets().get(setName);
/*  744 */       if (waypoints == null) {
/*  745 */         map.getSets().put(setName, waypoints = new WaypointSet(setName));
/*      */       }
/*  747 */       Waypoint loadWaypoint = new Waypoint(Integer.parseInt(args[4]), Integer.parseInt(args[5]), Integer.parseInt(args[6]), args[2].replace("§§", ":"), args[3].replace("§§", ":"), Integer.parseInt(args[7]));
/*  748 */       if (args.length > 8)
/*  749 */         loadWaypoint.setDisabled(args[8].equals("true")); 
/*  750 */       if (args.length > 9)
/*  751 */         loadWaypoint.setType(Integer.parseInt(args[9])); 
/*  752 */       if (args.length > 11)
/*  753 */         loadWaypoint.setRotation(args[11].equals("true")); 
/*  754 */       if (args.length > 12)
/*  755 */         loadWaypoint.setYaw(Integer.parseInt(args[12])); 
/*  756 */       waypoints.getList().add(loadWaypoint);
/*  757 */       return true;
/*      */     } 
/*  759 */     return false;
/*      */   }
/*      */   
/*      */   public void loadOldWaypoints(File file) throws IOException {
/*  763 */     if (!file.exists())
/*      */       return; 
/*  765 */     BufferedReader reader = null;
/*      */     try {
/*  767 */       reader = new BufferedReader(new FileReader(file));
/*      */       String s;
/*  769 */       while ((s = reader.readLine()) != null) {
/*  770 */         String[] args = s.split(":");
/*      */         try {
/*  772 */           checkWaypointsLineOLD(args);
/*  773 */         } catch (Exception e) {
/*  774 */           System.out.println("Skipping setting:" + args[0]);
/*      */         } 
/*      */       } 
/*      */     } finally {
/*  778 */       if (reader != null)
/*  779 */         reader.close(); 
/*      */     } 
/*  781 */     File backupFile = new File(file.getAbsolutePath() + ".backup");
/*  782 */     if (backupFile.exists()) {
/*  783 */       System.out.println("Waypoints old file backup already exists!");
/*      */       return;
/*      */     } 
/*  786 */     Files.move(file.toPath(), backupFile.toPath(), new CopyOption[0]);
/*      */   }
/*      */   
/*      */   public void writeSettings(PrintWriter writer) {
/*  790 */     writer.println("#CONFIG ONLY OPTIONS");
/*  791 */     writer.println("ignoreUpdate:" + ignoreUpdate);
/*  792 */     writer.println("updateNotification:" + updateNotification);
/*  793 */     writer.println("settingsButton:" + settingsButton);
/*  794 */     if (minimapItemId != null)
/*  795 */       writer.println("minimapItemId:" + minimapItemId); 
/*  796 */     writer.println("allowWrongWorldTeleportation:" + this.allowWrongWorldTeleportation);
/*  797 */     writer.println("differentiateByServerAddress:" + this.differentiateByServerAddress);
/*  798 */     writer.println("#INGAME SETTINGS (DO NOT EDIT!)");
/*  799 */     writer.println("minimap:" + this.minimap);
/*  800 */     writer.println("caveMaps:" + this.caveMaps);
/*  801 */     writer.println("caveZoom:" + this.caveZoom);
/*  802 */     writer.println("showPlayers:" + this.showPlayers);
/*  803 */     writer.println("showHostile:" + this.showHostile);
/*  804 */     writer.println("showMobs:" + this.showMobs);
/*  805 */     writer.println("showItems:" + this.showItems);
/*  806 */     writer.println("showOther:" + this.showOther);
/*  807 */     writer.println("showOtherTeam:" + this.showOtherTeam);
/*  808 */     writer.println("showWaypoints:" + this.showWaypoints);
/*  809 */     writer.println("showIngameWaypoints:" + this.showIngameWaypoints);
/*  810 */     writer.println("displayRedstone:" + this.displayRedstone);
/*  811 */     writer.println("deathpoints:" + this.deathpoints);
/*  812 */     writer.println("oldDeathpoints:" + this.oldDeathpoints);
/*  813 */     writer.println("distance:" + this.distance);
/*  814 */     writer.println("showCoords:" + this.showCoords);
/*  815 */     writer.println("lockNorth:" + this.lockNorth);
/*  816 */     writer.println("zoom:" + this.zoom);
/*  817 */     writer.println("mapSize:" + this.mapSize);
/*  818 */     writer.println("entityAmount:" + this.entityAmount);
/*  819 */     writer.println("chunkGrid:" + this.chunkGrid);
/*  820 */     writer.println("slimeChunks:" + this.slimeChunks);
/*  821 */     writer.println("playersColor:" + this.playersColor);
/*  822 */     writer.println("mobsColor:" + this.mobsColor);
/*  823 */     writer.println("hostileColor:" + this.hostileColor);
/*  824 */     writer.println("itemsColor:" + this.itemsColor);
/*  825 */     writer.println("otherColor:" + this.otherColor);
/*  826 */     writer.println("otherTeamColor:" + this.otherTeamColor);
/*  827 */     writer.println("mapSafeMode:" + this.mapSafeMode);
/*  828 */     writer.println("minimapOpacity:" + this.minimapOpacity);
/*  829 */     writer.println("waypointsScale:" + this.waypointsScale);
/*  830 */     writer.println("antiAliasing:" + this.antiAliasing);
/*  831 */     writer.println("blockColours:" + this.blockColours);
/*  832 */     writer.println("lighting:" + this.lighting);
/*  833 */     writer.println("dotsScale:" + this.dotsScale);
/*  834 */     writer.println("compassOverWaypoints:" + this.compassOverWaypoints);
/*  835 */     writer.println("showBiome:" + this.showBiome);
/*  836 */     writer.println("showEntityHeight:" + this.showEntityHeight);
/*  837 */     writer.println("showFlowers:" + this.showFlowers);
/*  838 */     writer.println("keepWaypointNames:" + this.keepWaypointNames);
/*  839 */     writer.println("waypointsDistance:" + this.waypointsDistance);
/*  840 */     writer.println("waypointsDistanceMin:" + this.waypointsDistanceMin);
/*  841 */     writer.println("waypointTp:" + this.waypointTp);
/*  842 */     writer.println("arrowScale:" + this.arrowScale);
/*  843 */     writer.println("arrowColour:" + this.arrowColour);
/*  844 */     writer.println("smoothDots:" + this.smoothDots);
/*  845 */     writer.println("playerHeads:" + this.playerHeads);
/*  846 */     writer.println("heightLimit:" + this.heightLimit);
/*  847 */     writer.println("worldMap:" + this.worldMap);
/*  848 */     writer.println("terrainDepth:" + this.terrainDepth);
/*  849 */     writer.println("terrainSlopes:" + this.terrainSlopes);
/*  850 */     writer.println("terrainSlopesExperiment:" + this.terrainSlopesExperiment);
/*  851 */     writer.println("mainEntityAs:" + this.mainEntityAs);
/*  852 */     writer.println("blockTransparency:" + this.blockTransparency);
/*  853 */     writer.println("waypointOpacityIngame:" + this.waypointOpacityIngame);
/*  854 */     writer.println("waypointOpacityMap:" + this.waypointOpacityMap);
/*  855 */     writer.println("hideWorldNames:" + this.hideWorldNames);
/*  856 */     writer.println("openSlimeSettings:" + this.openSlimeSettings);
/*  857 */     writer.println("alwaysShowDistance:" + this.alwaysShowDistance);
/*  858 */     writer.println("playerNames:" + this.playerNames);
/*  859 */     writer.println("showLightLevel:" + this.showLightLevel);
/*  860 */     writer.println("renderLayerIndex:" + this.renderLayerIndex);
/*  861 */     writer.println("showTime:" + this.showTime);
/*  862 */     writer.println("biomeColorsVanillaMode:" + this.biomeColorsVanillaMode);
/*  863 */     writer.println("lookingAtAngle:" + this.lookingAtAngle);
/*  864 */     writer.println("lookingAtAngleVertical:" + this.lookingAtAngleVertical);
/*  865 */     writer.println("centeredEnlarged:" + this.centeredEnlarged);
/*  866 */     writer.println("zoomedOutEnlarged:" + this.zoomedOutEnlarged);
/*  867 */     writer.println("entityNametags:" + this.entityNametags);
/*  868 */     writer.println("minimapTextAlign:" + this.minimapTextAlign);
/*  869 */     writer.println("showAngles:" + this.showAngles);
/*  870 */     writer.println("waypointsMutualEdit:" + this.waypointsMutualEdit);
/*  871 */     writer.println("compass:" + this.compass);
/*  872 */     writer.println("caveMapsDepth:" + this.caveMapsDepth);
/*  873 */     writer.println("hideWaypointCoordinates:" + this.hideWaypointCoordinates);
/*  874 */     writer.println("renderAllSets:" + this.renderAllSets);
/*  875 */     writer.println("playerArrowOpacity:" + this.playerArrowOpacity);
/*      */   }
/*      */   
/*      */   public void saveSettings() throws IOException {
/*  879 */     PrintWriter writer = new PrintWriter(new FileWriter(this.modMain.getConfigFile()));
/*  880 */     writeSettings(writer);
/*  881 */     Object[] keys = serverSlimeSeeds.keySet().toArray();
/*  882 */     Object[] values = serverSlimeSeeds.values().toArray();
/*  883 */     for (int i = 0; i < keys.length; i++)
/*  884 */       writer.println("seed:" + keys[i] + ":" + values[i]); 
/*  885 */     Iterator<Interface> iter = this.modMain.getInterfaces().getInterfaceIterator();
/*  886 */     while (iter.hasNext()) {
/*  887 */       Interface l = iter.next();
/*  888 */       writer.println("interface:" + l.getIname() + ":" + l.getActualx() + ":" + l.getActualy() + ":" + l.isCentered() + ":" + l
/*  889 */           .isFlipped() + ":" + l.isFromRight() + ":" + l.isFromBottom());
/*      */     } 
/*  891 */     writer.println("#WAYPOINTS HAVE BEEN MOVED TO xaerowaypoints.txt!");
/*  892 */     writer.close();
/*      */   }
/*      */ 
/*      */   
/*      */   public void readSetting(String[] args) {
/*  897 */     if (args[0].equalsIgnoreCase("ignoreUpdate")) {
/*  898 */       ignoreUpdate = Integer.parseInt(args[1]);
/*  899 */     } else if (args[0].equalsIgnoreCase("updateNotification")) {
/*  900 */       updateNotification = args[1].equals("true");
/*  901 */     } else if (args[0].equalsIgnoreCase("settingsButton")) {
/*  902 */       settingsButton = args[1].equals("true");
/*  903 */     } else if (args[0].equalsIgnoreCase("minimapItemId")) {
/*  904 */       minimapItemId = args[1] + ":" + args[2];
/*  905 */       minimapItem = (Item)Item.field_150901_e.func_82594_a(new ResourceLocation(args[1], args[2]));
/*      */     }
/*  907 */     else if (args[0].equalsIgnoreCase("allowWrongWorldTeleportation")) {
/*  908 */       this.allowWrongWorldTeleportation = args[1].equals("true");
/*  909 */     } else if (args[0].equalsIgnoreCase("differentiateByServerAddress")) {
/*  910 */       this.differentiateByServerAddress = args[1].equals("true");
/*      */     }
/*  912 */     else if (args[0].equalsIgnoreCase("minimap")) {
/*  913 */       this.minimap = args[1].equals("true");
/*  914 */     } else if (args[0].equalsIgnoreCase("caveMaps")) {
/*  915 */       this.caveMaps = args[1].equals("true") ? 1 : (args[1].equals("false") ? 0 : Integer.parseInt(args[1]));
/*  916 */     } else if (args[0].equalsIgnoreCase("caveZoom")) {
/*  917 */       this.caveZoom = args[1].equals("true") ? 2 : (args[1].equals("false") ? 0 : Integer.parseInt(args[1]));
/*  918 */     } else if (args[0].equalsIgnoreCase("showPlayers")) {
/*  919 */       this.showPlayers = args[1].equals("true");
/*  920 */     } else if (args[0].equalsIgnoreCase("showHostile")) {
/*  921 */       this.showHostile = args[1].equals("true");
/*  922 */     } else if (args[0].equalsIgnoreCase("showMobs")) {
/*  923 */       this.showMobs = args[1].equals("true");
/*  924 */     } else if (args[0].equalsIgnoreCase("showItems")) {
/*  925 */       this.showItems = args[1].equals("true");
/*  926 */     } else if (args[0].equalsIgnoreCase("showOther")) {
/*  927 */       this.showOther = args[1].equals("true");
/*  928 */     } else if (args[0].equalsIgnoreCase("showOtherTeam")) {
/*  929 */       this.showOtherTeam = args[1].equals("true");
/*  930 */     } else if (args[0].equalsIgnoreCase("showWaypoints")) {
/*  931 */       this.showWaypoints = args[1].equals("true");
/*  932 */     } else if (args[0].equalsIgnoreCase("deathpoints")) {
/*  933 */       this.deathpoints = args[1].equals("true");
/*  934 */     } else if (args[0].equalsIgnoreCase("oldDeathpoints")) {
/*  935 */       this.oldDeathpoints = args[1].equals("true");
/*  936 */     } else if (args[0].equalsIgnoreCase("showIngameWaypoints")) {
/*  937 */       this.showIngameWaypoints = args[1].equals("true");
/*  938 */     } else if (args[0].equalsIgnoreCase("displayRedstone")) {
/*  939 */       this.displayRedstone = args[1].equals("true");
/*  940 */     } else if (args[0].equalsIgnoreCase("distance")) {
/*  941 */       this.distance = Integer.parseInt(args[1]);
/*  942 */     } else if (args[0].equalsIgnoreCase("showCoords")) {
/*  943 */       this.showCoords = args[1].equals("true");
/*  944 */     } else if (args[0].equalsIgnoreCase("lockNorth")) {
/*  945 */       this.lockNorth = args[1].equals("true");
/*  946 */     } else if (args[0].equalsIgnoreCase("zoom")) {
/*  947 */       this.zoom = Integer.parseInt(args[1]);
/*  948 */       if (this.zoom >= this.zooms.length)
/*  949 */         this.zoom = this.zooms.length - 1; 
/*  950 */     } else if (args[0].equalsIgnoreCase("mapSize")) {
/*  951 */       this.mapSize = Integer.parseInt(args[1]);
/*  952 */     } else if (args[0].equalsIgnoreCase("entityAmount")) {
/*  953 */       this.entityAmount = Integer.parseInt(args[1]);
/*  954 */     } else if (args[0].equalsIgnoreCase("chunkGrid")) {
/*  955 */       this.chunkGrid = args[1].equals("true") ? 0 : (args[1].equals("false") ? -1 : Integer.parseInt(args[1]));
/*  956 */     } else if (args[0].equalsIgnoreCase("slimeChunks")) {
/*  957 */       this.slimeChunks = args[1].equals("true");
/*  958 */     } else if (args[0].equalsIgnoreCase("playersColor")) {
/*  959 */       this.playersColor = Integer.parseInt(args[1]);
/*  960 */     } else if (args[0].equalsIgnoreCase("mobsColor")) {
/*  961 */       this.mobsColor = Integer.parseInt(args[1]);
/*  962 */     } else if (args[0].equalsIgnoreCase("hostileColor")) {
/*  963 */       this.hostileColor = Integer.parseInt(args[1]);
/*  964 */     } else if (args[0].equalsIgnoreCase("itemsColor")) {
/*  965 */       this.itemsColor = Integer.parseInt(args[1]);
/*  966 */     } else if (args[0].equalsIgnoreCase("otherColor")) {
/*  967 */       this.otherColor = Integer.parseInt(args[1]);
/*  968 */     } else if (args[0].equalsIgnoreCase("otherTeamColor")) {
/*  969 */       this.otherTeamColor = Integer.parseInt(args[1]);
/*  970 */     } else if (args[0].equalsIgnoreCase("mapSafeMode")) {
/*  971 */       this.mapSafeMode = args[1].equals("true");
/*  972 */     } else if (args[0].equalsIgnoreCase("minimapOpacity")) {
/*  973 */       this.minimapOpacity = Float.valueOf(args[1]).floatValue();
/*  974 */     } else if (args[0].equalsIgnoreCase("waypointsScale")) {
/*  975 */       this.waypointsScale = Float.valueOf(args[1]).floatValue();
/*  976 */     } else if (args[0].equalsIgnoreCase("antiAliasing")) {
/*  977 */       this.antiAliasing = args[1].equals("true");
/*  978 */     } else if (args[0].equalsIgnoreCase("blockColours")) {
/*  979 */       this.blockColours = Integer.parseInt(args[1]);
/*  980 */     } else if (args[0].equalsIgnoreCase("lighting")) {
/*  981 */       this.lighting = args[1].equals("true");
/*  982 */     } else if (args[0].equalsIgnoreCase("dotsScale")) {
/*  983 */       this.dotsScale = Float.valueOf(args[1]).floatValue();
/*  984 */     } else if (args[0].equalsIgnoreCase("compassOverWaypoints")) {
/*  985 */       this.compassOverWaypoints = args[1].equals("true");
/*  986 */     } else if (args[0].equalsIgnoreCase("showBiome")) {
/*  987 */       this.showBiome = args[1].equals("true");
/*  988 */     } else if (args[0].equalsIgnoreCase("showEntityHeight")) {
/*  989 */       this.showEntityHeight = args[1].equals("true");
/*  990 */     } else if (args[0].equalsIgnoreCase("showFlowers")) {
/*  991 */       this.showFlowers = args[1].equals("true");
/*  992 */     } else if (args[0].equalsIgnoreCase("keepWaypointNames")) {
/*  993 */       this.keepWaypointNames = args[1].equals("true");
/*  994 */     } else if (args[0].equalsIgnoreCase("waypointsDistance")) {
/*  995 */       this.waypointsDistance = Float.valueOf(args[1]).floatValue();
/*  996 */     } else if (args[0].equalsIgnoreCase("waypointsDistanceMin")) {
/*  997 */       this.waypointsDistanceMin = Float.valueOf(args[1]).floatValue();
/*  998 */     } else if (args[0].equalsIgnoreCase("waypointTp")) {
/*  999 */       this.waypointTp = args[1];
/* 1000 */     } else if (args[0].equalsIgnoreCase("arrowScale")) {
/* 1001 */       this.arrowScale = Float.valueOf(args[1]).floatValue();
/* 1002 */     } else if (args[0].equalsIgnoreCase("arrowColour")) {
/* 1003 */       this.arrowColour = Integer.parseInt(args[1]);
/* 1004 */     } else if (args[0].equalsIgnoreCase("seed")) {
/* 1005 */       serverSlimeSeeds.put(args[1], Long.valueOf(Long.parseLong(args[2])));
/* 1006 */     } else if (args[0].equalsIgnoreCase("smoothDots")) {
/* 1007 */       this.smoothDots = args[1].equals("true");
/* 1008 */     } else if (args[0].equalsIgnoreCase("playerHeads")) {
/* 1009 */       this.playerHeads = args[1].equals("true");
/* 1010 */     } else if (args[0].equalsIgnoreCase("heightLimit")) {
/* 1011 */       this.heightLimit = Integer.parseInt(args[1]);
/* 1012 */     } else if (args[0].equalsIgnoreCase("worldMap")) {
/* 1013 */       this.worldMap = args[1].equals("true");
/* 1014 */     } else if (args[0].equalsIgnoreCase("terrainDepth")) {
/* 1015 */       this.terrainDepth = args[1].equals("true");
/* 1016 */     } else if (args[0].equalsIgnoreCase("terrainSlopes")) {
/* 1017 */       this.terrainSlopes = args[1].equals("true");
/* 1018 */     } else if (args[0].equalsIgnoreCase("terrainSlopesExperiment")) {
/* 1019 */       this.terrainSlopesExperiment = args[1].equals("true");
/* 1020 */     } else if (args[0].equalsIgnoreCase("alwaysArrow") && args[1].equals("true")) {
/* 1021 */       this.mainEntityAs = 2;
/* 1022 */     } else if (args[0].equalsIgnoreCase("mainEntityAs")) {
/* 1023 */       this.mainEntityAs = Integer.parseInt(args[1]);
/* 1024 */     } else if (args[0].equalsIgnoreCase("blockTransparency")) {
/* 1025 */       this.blockTransparency = args[1].equals("true");
/* 1026 */     } else if (args[0].equalsIgnoreCase("waypointOpacityIngame")) {
/* 1027 */       this.waypointOpacityIngame = Integer.parseInt(args[1]);
/* 1028 */     } else if (args[0].equalsIgnoreCase("waypointOpacityMap")) {
/* 1029 */       this.waypointOpacityMap = Integer.parseInt(args[1]);
/* 1030 */     } else if (args[0].equalsIgnoreCase("hideWorldNames")) {
/* 1031 */       this.hideWorldNames = args[1].equals("true") ? 2 : (args[1].equals("false") ? 1 : Integer.parseInt(args[1]));
/* 1032 */     } else if (args[0].equalsIgnoreCase("openSlimeSettings")) {
/* 1033 */       this.openSlimeSettings = args[1].equals("true");
/* 1034 */     } else if (args[0].equalsIgnoreCase("alwaysShowDistance")) {
/* 1035 */       this.alwaysShowDistance = args[1].equals("true");
/* 1036 */     } else if (args[0].equalsIgnoreCase("playerNames")) {
/* 1037 */       this.playerNames = args[1].equals("true");
/* 1038 */     } else if (args[0].equalsIgnoreCase("showLightLevel")) {
/* 1039 */       this.showLightLevel = args[1].equals("true");
/* 1040 */     } else if (args[0].equalsIgnoreCase("renderLayerIndex")) {
/* 1041 */       this.renderLayerIndex = Integer.parseInt(args[1]);
/* 1042 */       if (this.renderLayerIndex >= ForgeEventHandler.OVERLAY_LAYERS.length) {
/* 1043 */         this.renderLayerIndex = ForgeEventHandler.OVERLAY_LAYERS.length - 1;
/*      */       }
/* 1045 */     } else if (args[0].equalsIgnoreCase("showTime")) {
/* 1046 */       this.showTime = Integer.parseInt(args[1]);
/* 1047 */     } else if (args[0].equalsIgnoreCase("biomeColorsVanillaMode")) {
/* 1048 */       this.biomeColorsVanillaMode = args[1].equals("true");
/* 1049 */     } else if (args[0].equalsIgnoreCase("lookingAtAngle")) {
/* 1050 */       this.lookingAtAngle = Integer.parseInt(args[1]);
/* 1051 */     } else if (args[0].equalsIgnoreCase("lookingAtAngleVertical")) {
/* 1052 */       this.lookingAtAngleVertical = Integer.parseInt(args[1]);
/* 1053 */     } else if (args[0].equalsIgnoreCase("centeredEnlarged")) {
/* 1054 */       this.centeredEnlarged = args[1].equals("true");
/* 1055 */     } else if (args[0].equalsIgnoreCase("zoomedOutEnlarged")) {
/* 1056 */       this.zoomedOutEnlarged = args[1].equals("true");
/* 1057 */     } else if (args[0].equalsIgnoreCase("entityNametags")) {
/* 1058 */       this.entityNametags = args[1].equals("true");
/* 1059 */     } else if (args[0].equalsIgnoreCase("minimapTextAlign")) {
/* 1060 */       this.minimapTextAlign = Integer.parseInt(args[1]);
/* 1061 */     } else if (args[0].equalsIgnoreCase("showAngles")) {
/* 1062 */       this.showAngles = args[1].equals("true");
/* 1063 */     } else if (args[0].equalsIgnoreCase("waypointsMutualEdit")) {
/* 1064 */       this.waypointsMutualEdit = args[1].equals("true");
/* 1065 */     } else if (args[0].equalsIgnoreCase("compass")) {
/* 1066 */       this.compass = args[1].equals("true");
/* 1067 */     } else if (args[0].equalsIgnoreCase("caveMapsDepth")) {
/* 1068 */       this.caveMapsDepth = Integer.parseInt(args[1]);
/* 1069 */     } else if (args[0].equalsIgnoreCase("hideWaypointCoordinates")) {
/* 1070 */       this.hideWaypointCoordinates = args[1].equals("true");
/* 1071 */     } else if (args[0].equalsIgnoreCase("renderAllSets")) {
/* 1072 */       this.renderAllSets = args[1].equals("true");
/* 1073 */     } else if (args[0].equalsIgnoreCase("playerArrowOpacity")) {
/* 1074 */       this.playerArrowOpacity = Integer.parseInt(args[1]);
/*      */     } 
/*      */   }
/*      */   public void loadSettings() throws IOException {
/* 1078 */     (new File("./config")).mkdirs();
/* 1079 */     if (!this.modMain.getConfigFile().exists()) {
/* 1080 */       saveSettings();
/*      */       return;
/*      */     } 
/* 1083 */     this.modMain.getWaypointsManager().getWaypointMap().clear();
/* 1084 */     BufferedReader reader = null;
/* 1085 */     boolean saveWaypoints = false;
/*      */     try {
/* 1087 */       reader = new BufferedReader(new FileReader(this.modMain.getConfigFile()));
/*      */       String s;
/* 1089 */       while ((s = reader.readLine()) != null) {
/* 1090 */         String[] args = s.split(":");
/*      */         try {
/* 1092 */           readSetting(args);
/* 1093 */           if (args[0].equalsIgnoreCase("interface")) {
/* 1094 */             Iterator<Interface> iter = this.modMain.getInterfaces().getInterfaceIterator();
/* 1095 */             label43: while (iter.hasNext()) {
/* 1096 */               Interface l = iter.next();
/* 1097 */               if (args[1].equals(l.getIname())) {
/* 1098 */                 l.setX(Integer.parseInt(args[2]));
/* 1099 */                 l.setY(Integer.parseInt(args[3]));
/* 1100 */                 l.setActualx(l.getX());
/* 1101 */                 l.setActualy(l.getY());
/* 1102 */                 l.setCentered(args[4].equals("true"));
/* 1103 */                 l.setFlipped(args[5].equals("true"));
/* 1104 */                 l.setFromRight(args[6].equals("true"));
/* 1105 */                 if (args.length > 7) {
/* 1106 */                   l.setFromBottom(args[7].equals("true"));
/* 1107 */                   l.backup(); continue;
/*      */                 }  break label43;
/*      */               } 
/*      */             }  continue;
/* 1111 */           }  if (checkWaypointsLineOLD(args))
/* 1112 */             saveWaypoints = true; 
/* 1113 */         } catch (Exception e) {
/* 1114 */           System.out.println("Skipping setting:" + args[0]);
/*      */         } 
/*      */       } 
/*      */     } finally {
/* 1118 */       if (reader != null)
/* 1119 */         reader.close(); 
/*      */     } 
/* 1121 */     if (this.modMain.getWaypointsFile().exists()) {
/* 1122 */       loadOldWaypoints(this.modMain.getWaypointsFile());
/* 1123 */       saveWaypoints = true;
/*      */     } 
/* 1125 */     loadAllWaypoints();
/* 1126 */     if (saveWaypoints) {
/* 1127 */       saveAllWaypoints();
/* 1128 */       saveSettings();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public String getMoreKeybindings(String s, ModOptions par1EnumOptions) {
/* 1134 */     boolean clientSetting = getClientBooleanValue(par1EnumOptions);
/* 1135 */     boolean serverSetting = getBooleanValue(par1EnumOptions);
/*      */     
/* 1137 */     s = s + getTranslation(clientSetting) + ((serverSetting != clientSetting) ? ("§e (" + getTranslation(serverSetting) + ")") : "");
/* 1138 */     return s;
/*      */   }
/*      */   
/*      */   public String getKeyBinding(ModOptions par1EnumOptions) {
/* 1142 */     String s = par1EnumOptions.getEnumString() + ": ";
/* 1143 */     if (par1EnumOptions == ModOptions.DOTS_SCALE && ((this.modMain.getInterfaces().getMinimap().getMinimapFBORenderer().isTriedFBO() && !this.modMain.getInterfaces().getMinimap().usingFBO()) || this.mapSafeMode))
/* 1144 */       return s + "§e" + getTranslation(false); 
/* 1145 */     if (par1EnumOptions == ModOptions.CHUNK_GRID)
/*      */     
/*      */     { 
/*      */       
/* 1149 */       s = s + ((this.chunkGrid > -1) ? ("§" + ENCHANT_COLORS[this.chunkGrid] + I18n.func_135052_a(ENCHANT_COLOR_NAMES[this.chunkGrid], new Object[0])) : I18n.func_135052_a("gui.xaero_off", new Object[0])); }
/* 1150 */     else { if (par1EnumOptions.getEnumFloat())
/* 1151 */         return getEnumFloatSliderText(s, par1EnumOptions); 
/* 1152 */       if (par1EnumOptions == ModOptions.EDIT)
/* 1153 */       { s = par1EnumOptions.getEnumString(); }
/* 1154 */       else if (par1EnumOptions == ModOptions.DOTS)
/* 1155 */       { s = par1EnumOptions.getEnumString(); }
/* 1156 */       else if (par1EnumOptions == ModOptions.RESET)
/* 1157 */       { s = par1EnumOptions.getEnumString(); }
/* 1158 */       else if (par1EnumOptions == ModOptions.WAYPOINTS_DEFAULT_TP)
/*      */       
/* 1160 */       { s = par1EnumOptions.getEnumString(); }
/* 1161 */       else if (par1EnumOptions == ModOptions.ZOOM)
/* 1162 */       { s = s + this.zooms[this.zoom] + "x"; }
/* 1163 */       else if (par1EnumOptions == ModOptions.COLOURS)
/* 1164 */       { s = s + (this.modMain.getSupportMods().shouldUseWorldMapChunks() ? ("§e" + I18n.func_135052_a("gui.xaero_world_map", new Object[0])) : I18n.func_135052_a(blockColourTypes[getBlockColours()], new Object[0])); }
/* 1165 */       else if (par1EnumOptions == ModOptions.DISTANCE)
/*      */       
/* 1167 */       { s = s + I18n.func_135052_a(distanceTypes[this.distance], new Object[0]); }
/* 1168 */       else if (par1EnumOptions == ModOptions.SLIME_CHUNKS && customSlimeSeedNeeded())
/*      */       
/*      */       { 
/*      */         
/* 1172 */         s = par1EnumOptions.getEnumString(); }
/* 1173 */       else if (par1EnumOptions == ModOptions.SIZE)
/* 1174 */       { s = s + I18n.func_135052_a((this.mapSize > -1) ? MINIMAP_SIZE[this.mapSize] : "gui.xaero_auto_map_size", new Object[0]); }
/* 1175 */       else if (par1EnumOptions == ModOptions.EAMOUNT)
/* 1176 */       { s = (this.entityAmount == 0) ? (s + I18n.func_135052_a("gui.xaero_unlimited", new Object[0])) : (s + (100 * this.entityAmount)); }
/* 1177 */       else if (par1EnumOptions == ModOptions.CAVE_MAPS)
/* 1178 */       { if (this.caveMaps == 0) {
/* 1179 */           s = s + I18n.func_135052_a("gui.xaero_off", new Object[0]);
/*      */         } else {
/* 1181 */           int roofSideSize = (this.caveMaps - 1) * 2 + 1;
/* 1182 */           s = s + roofSideSize + "x" + roofSideSize + " " + I18n.func_135052_a("gui.xaero_roof", new Object[0]);
/* 1183 */           if (!getCaveMaps()) {
/* 1184 */             s = s + "§e (" + getTranslation(false) + ")";
/*      */           }
/*      */         }  }
/* 1187 */       else if (par1EnumOptions == ModOptions.CAVE_ZOOM)
/* 1188 */       { if (this.caveZoom == 0) {
/* 1189 */           s = s + I18n.func_135052_a("gui.xaero_off", new Object[0]);
/*      */         } else {
/* 1191 */           s = s + (1 + this.caveZoom) + "x";
/*      */         }
/*      */          }
/* 1194 */       else if (par1EnumOptions == ModOptions.HIDE_WORLD_NAMES)
/* 1195 */       { s = s + ((this.hideWorldNames == 0) ? I18n.func_135052_a("gui.xaero_off", new Object[0]) : ((this.hideWorldNames == 1) ? I18n.func_135052_a("gui.xaero_partial", new Object[0]) : I18n.func_135052_a("gui.xaero_full", new Object[0]))); }
/* 1196 */       else if (par1EnumOptions == ModOptions.SHOW_TIME)
/* 1197 */       { s = s + ((this.showTime == 0) ? I18n.func_135052_a("gui.xaero_off", new Object[0]) : ((this.showTime == 1) ? I18n.func_135052_a("gui.xaero_24h", new Object[0]) : I18n.func_135052_a("gui.xaero_12h", new Object[0]))); }
/* 1198 */       else if (par1EnumOptions == ModOptions.MINIMAP_TEXT_ALIGN)
/* 1199 */       { s = s + ((this.minimapTextAlign == 0) ? I18n.func_135052_a("gui.xaero_center", new Object[0]) : ((this.minimapTextAlign == 1) ? I18n.func_135052_a("gui.xaero_left", new Object[0]) : I18n.func_135052_a("gui.xaero_right", new Object[0]))); }
/* 1200 */       else if (par1EnumOptions == ModOptions.ARROW_COLOUR)
/* 1201 */       { String colourName = "gui.xaero_team";
/* 1202 */         if (this.arrowColour != -1)
/* 1203 */           colourName = this.arrowColourNames[this.arrowColour]; 
/* 1204 */         s = s + I18n.func_135052_a(colourName, new Object[0]); }
/*      */       
/* 1206 */       else if ((par1EnumOptions == ModOptions.REDSTONE || par1EnumOptions == ModOptions.FLOWERS || par1EnumOptions == ModOptions.BIOMES_VANILLA || par1EnumOptions == ModOptions.LIGHT || par1EnumOptions == ModOptions.TERRAIN_DEPTH || par1EnumOptions == ModOptions.TERRAIN_SLOPES) && this.modMain.getSupportMods().shouldUseWorldMapChunks())
/* 1207 */       { s = s + "§e" + I18n.func_135052_a("gui.xaero_world_map", new Object[0]); }
/* 1208 */       else if (par1EnumOptions == ModOptions.TERRAIN_SLOPES && this.terrainSlopesExperiment)
/* 1209 */       { s = s + "Experimental"; }
/* 1210 */       else if (par1EnumOptions == ModOptions.RENDER_LAYER)
/* 1211 */       { s = s + this.renderLayerIndex; }
/* 1212 */       else if (par1EnumOptions == ModOptions.MAIN_ENTITY_AS)
/* 1213 */       { s = s + ((this.mainEntityAs == 0) ? I18n.func_135052_a("gui.xaero_crosshair", new Object[0]) : ((this.mainEntityAs == 1) ? I18n.func_135052_a("gui.xaero_dot", new Object[0]) : I18n.func_135052_a("gui.xaero_arrow", new Object[0]))); }
/*      */       else
/* 1215 */       { s = getMoreKeybindings(s, par1EnumOptions); }  }
/* 1216 */      return s;
/*      */   }
/*      */   
/*      */   protected String getEnumFloatSliderText(String s, ModOptions par1EnumOptions) {
/* 1220 */     String f1 = String.format("%.1f", new Object[] { Float.valueOf(getOptionFloatValue(par1EnumOptions)) });
/* 1221 */     if (par1EnumOptions == ModOptions.WAYPOINTS_DISTANCE) {
/* 1222 */       if (this.waypointsDistance == 0.0F)
/* 1223 */       { f1 = I18n.func_135052_a("gui.xaero_unlimited", new Object[0]); }
/*      */       else
/* 1225 */       { f1 = (int)this.waypointsDistance + "m"; } 
/* 1226 */     } else if (par1EnumOptions == ModOptions.WAYPOINTS_DISTANCE_MIN) {
/* 1227 */       if (this.waypointsDistanceMin == 0.0F)
/* 1228 */       { f1 = I18n.func_135052_a("gui.xaero_off", new Object[0]); }
/*      */       else
/* 1230 */       { f1 = (int)this.waypointsDistanceMin + "m"; } 
/* 1231 */     } else if (par1EnumOptions == ModOptions.ARROW_SCALE) {
/* 1232 */       f1 = f1 + "x";
/* 1233 */     }  return s + f1;
/*      */   }
/*      */   
/*      */   public boolean getBooleanValue(ModOptions o) {
/* 1237 */     if (o == ModOptions.MINIMAP)
/* 1238 */       return getMinimap(); 
/* 1239 */     if (o == ModOptions.CAVE_MAPS)
/* 1240 */       return getCaveMaps(); 
/* 1241 */     if (o == ModOptions.DISPLAY_OTHER_TEAM)
/* 1242 */       return getShowOtherTeam(); 
/* 1243 */     if (o == ModOptions.WAYPOINTS)
/* 1244 */       return getShowWaypoints(); 
/* 1245 */     if (o == ModOptions.DEATHPOINTS)
/* 1246 */       return getDeathpoints(); 
/* 1247 */     if (o == ModOptions.OLD_DEATHPOINTS)
/* 1248 */       return getOldDeathpoints(); 
/* 1249 */     if (o == ModOptions.INGAME_WAYPOINTS)
/* 1250 */       return getShowIngameWaypoints(); 
/* 1251 */     if (o == ModOptions.COORDS)
/* 1252 */       return getShowCoords(); 
/* 1253 */     if (o == ModOptions.NORTH)
/* 1254 */       return getLockNorth(); 
/* 1255 */     if (o == ModOptions.PLAYERS)
/* 1256 */       return getShowPlayers(); 
/* 1257 */     if (o == ModOptions.HOSTILE)
/* 1258 */       return getShowHostile(); 
/* 1259 */     if (o == ModOptions.MOBS)
/* 1260 */       return getShowMobs(); 
/* 1261 */     if (o == ModOptions.ITEMS)
/* 1262 */       return getShowItems(); 
/* 1263 */     if (o == ModOptions.ENTITIES)
/* 1264 */       return getShowOther(); 
/* 1265 */     if (o == ModOptions.SAFE_MAP)
/* 1266 */       return (this.mapSafeMode || (this.modMain.getInterfaces().getMinimap().getMinimapFBORenderer().isTriedFBO() && !this.modMain.getInterfaces().getMinimap().getMinimapFBORenderer().isLoadedFBO())); 
/* 1267 */     if (o == ModOptions.AA)
/* 1268 */       return getAntiAliasing(); 
/* 1269 */     if (o == ModOptions.SMOOTH_DOTS)
/* 1270 */       return getSmoothDots(); 
/* 1271 */     if (o == ModOptions.PLAYER_HEADS)
/* 1272 */       return getPlayerHeads(); 
/* 1273 */     if (o == ModOptions.WORLD_MAP)
/* 1274 */       return getUseWorldMap(); 
/* 1275 */     if (o == ModOptions.TERRAIN_DEPTH)
/* 1276 */       return getTerrainDepth(); 
/* 1277 */     if (o == ModOptions.TERRAIN_SLOPES)
/* 1278 */       return getTerrainSlopes(); 
/* 1279 */     return getClientBooleanValue(o);
/*      */   }
/*      */   
/*      */   public boolean getClientBooleanValue(ModOptions o) {
/* 1283 */     if (o == ModOptions.MINIMAP)
/* 1284 */       return this.minimap; 
/* 1285 */     if (o == ModOptions.DISPLAY_OTHER_TEAM)
/* 1286 */       return this.showOtherTeam; 
/* 1287 */     if (o == ModOptions.WAYPOINTS)
/* 1288 */       return this.showWaypoints; 
/* 1289 */     if (o == ModOptions.DEATHPOINTS)
/* 1290 */       return this.deathpoints; 
/* 1291 */     if (o == ModOptions.OLD_DEATHPOINTS)
/* 1292 */       return this.oldDeathpoints; 
/* 1293 */     if (o == ModOptions.INGAME_WAYPOINTS)
/* 1294 */       return this.showIngameWaypoints; 
/* 1295 */     if (o == ModOptions.REDSTONE)
/* 1296 */       return this.displayRedstone; 
/* 1297 */     if (o == ModOptions.COORDS)
/* 1298 */       return this.showCoords; 
/* 1299 */     if (o == ModOptions.NORTH)
/* 1300 */       return this.lockNorth; 
/* 1301 */     if (o == ModOptions.PLAYERS)
/* 1302 */       return this.showPlayers; 
/* 1303 */     if (o == ModOptions.HOSTILE)
/* 1304 */       return this.showHostile; 
/* 1305 */     if (o == ModOptions.MOBS)
/* 1306 */       return this.showMobs; 
/* 1307 */     if (o == ModOptions.ITEMS)
/* 1308 */       return this.showItems; 
/* 1309 */     if (o == ModOptions.ENTITIES)
/* 1310 */       return this.showOther; 
/* 1311 */     if (o == ModOptions.SLIME_CHUNKS)
/* 1312 */       return this.slimeChunks; 
/* 1313 */     if (o == ModOptions.SAFE_MAP)
/* 1314 */       return this.mapSafeMode; 
/* 1315 */     if (o == ModOptions.AA)
/* 1316 */       return this.antiAliasing; 
/* 1317 */     if (o == ModOptions.LIGHT)
/* 1318 */       return this.lighting; 
/* 1319 */     if (o == ModOptions.COMPASS)
/* 1320 */       return this.compassOverWaypoints; 
/* 1321 */     if (o == ModOptions.BIOME)
/* 1322 */       return this.showBiome; 
/* 1323 */     if (o == ModOptions.ENTITY_HEIGHT)
/* 1324 */       return this.showEntityHeight; 
/* 1325 */     if (o == ModOptions.FLOWERS)
/* 1326 */       return this.showFlowers; 
/* 1327 */     if (o == ModOptions.KEEP_WP_NAMES)
/* 1328 */       return this.keepWaypointNames; 
/* 1329 */     if (o == ModOptions.SMOOTH_DOTS)
/* 1330 */       return this.smoothDots; 
/* 1331 */     if (o == ModOptions.PLAYER_HEADS)
/* 1332 */       return this.playerHeads; 
/* 1333 */     if (o == ModOptions.WORLD_MAP)
/* 1334 */       return this.worldMap; 
/* 1335 */     if (o == ModOptions.CAPES)
/* 1336 */       return Patreon4.showCapes; 
/* 1337 */     if (o == ModOptions.TERRAIN_DEPTH)
/* 1338 */       return this.terrainDepth; 
/* 1339 */     if (o == ModOptions.TERRAIN_SLOPES)
/* 1340 */       return this.terrainSlopes; 
/* 1341 */     if (o == ModOptions.BLOCK_TRANSPARENCY)
/* 1342 */       return this.blockTransparency; 
/* 1343 */     if (o == ModOptions.OPEN_SLIME_SETTINGS)
/* 1344 */       return this.openSlimeSettings; 
/* 1345 */     if (o == ModOptions.ALWAYS_SHOW_DISTANCE)
/* 1346 */       return this.alwaysShowDistance; 
/* 1347 */     if (o == ModOptions.PLAYER_NAMES)
/* 1348 */       return this.playerNames; 
/* 1349 */     if (o == ModOptions.SHOW_LIGHT_LEVEL)
/* 1350 */       return this.showLightLevel; 
/* 1351 */     if (o == ModOptions.BIOMES_VANILLA)
/* 1352 */       return this.biomeColorsVanillaMode; 
/* 1353 */     if (o == ModOptions.CENTERED_ENLARGED)
/* 1354 */       return this.centeredEnlarged; 
/* 1355 */     if (o == ModOptions.ZOOMED_OUT_ENLARGED)
/* 1356 */       return this.zoomedOutEnlarged; 
/* 1357 */     if (o == ModOptions.ENTITY_NAMETAGS)
/* 1358 */       return this.entityNametags; 
/* 1359 */     if (o == ModOptions.SHOW_ANGLES)
/* 1360 */       return this.showAngles; 
/* 1361 */     if (o == ModOptions.COMPASS_ENABLED)
/* 1362 */       return this.compass; 
/* 1363 */     if (o == ModOptions.HIDE_WP_COORDS)
/* 1364 */       return this.hideWaypointCoordinates; 
/* 1365 */     if (o == ModOptions.WAYPOINTS_ALL_SETS)
/* 1366 */       return this.renderAllSets; 
/* 1367 */     return false;
/*      */   }
/*      */   
/*      */   public static String getTranslation(boolean o) {
/* 1371 */     return I18n.func_135052_a("gui.xaero_" + (o ? "on" : "off"), new Object[0]);
/*      */   }
/*      */   
/*      */   public void setOptionValue(ModOptions par1EnumOptions, int par2) throws IOException {
/* 1375 */     if (par1EnumOptions == ModOptions.ZOOM) {
/* 1376 */       this.zoom = (this.zoom + 1) % this.zooms.length;
/* 1377 */     } else if (par1EnumOptions == ModOptions.SIZE) {
/* 1378 */       if (this.mapSize == 3)
/* 1379 */       { this.mapSize = -1; }
/*      */       else
/* 1381 */       { this.mapSize = (this.mapSize + 1) % 4; } 
/* 1382 */     } else if (par1EnumOptions == ModOptions.EAMOUNT) {
/* 1383 */       this.entityAmount = (this.entityAmount + 1) % 11;
/* 1384 */     } else if (par1EnumOptions == ModOptions.MINIMAP) {
/* 1385 */       this.minimap = !this.minimap;
/* 1386 */     } else if (par1EnumOptions == ModOptions.CAVE_MAPS) {
/* 1387 */       this.caveMaps = (this.caveMaps + 1) % 4;
/* 1388 */     } else if (par1EnumOptions == ModOptions.CAVE_ZOOM) {
/* 1389 */       this.caveZoom = (this.caveZoom + 1) % 4;
/* 1390 */     } else if (par1EnumOptions == ModOptions.DISPLAY_OTHER_TEAM) {
/* 1391 */       this.showOtherTeam = !this.showOtherTeam;
/* 1392 */     } else if (par1EnumOptions == ModOptions.WAYPOINTS) {
/* 1393 */       this.showWaypoints = !this.showWaypoints;
/* 1394 */     } else if (par1EnumOptions == ModOptions.DEATHPOINTS) {
/* 1395 */       this.deathpoints = !this.deathpoints;
/* 1396 */     } else if (par1EnumOptions == ModOptions.OLD_DEATHPOINTS) {
/* 1397 */       this.oldDeathpoints = !this.oldDeathpoints;
/* 1398 */     } else if (par1EnumOptions == ModOptions.INGAME_WAYPOINTS) {
/* 1399 */       this.showIngameWaypoints = !this.showIngameWaypoints;
/* 1400 */     } else if (par1EnumOptions == ModOptions.REDSTONE) {
/* 1401 */       if (!this.modMain.getSupportMods().shouldUseWorldMapChunks())
/* 1402 */       { this.displayRedstone = !this.displayRedstone; }
/*      */       else
/* 1404 */       { (this.modMain.getSupportMods()).worldmapSupport.openSettings(); } 
/* 1405 */     } else if (par1EnumOptions == ModOptions.DISTANCE) {
/* 1406 */       this.distance = (this.distance + 1) % distanceTypes.length;
/* 1407 */     } else if (par1EnumOptions == ModOptions.COORDS) {
/* 1408 */       this.showCoords = !this.showCoords;
/* 1409 */     } else if (par1EnumOptions == ModOptions.NORTH) {
/* 1410 */       this.lockNorth = !this.lockNorth;
/* 1411 */     } else if (par1EnumOptions == ModOptions.PLAYERS) {
/* 1412 */       this.showPlayers = !this.showPlayers;
/* 1413 */     } else if (par1EnumOptions == ModOptions.HOSTILE) {
/* 1414 */       this.showHostile = !this.showHostile;
/* 1415 */     } else if (par1EnumOptions == ModOptions.MOBS) {
/* 1416 */       this.showMobs = !this.showMobs;
/* 1417 */     } else if (par1EnumOptions == ModOptions.ITEMS) {
/* 1418 */       this.showItems = !this.showItems;
/* 1419 */     } else if (par1EnumOptions == ModOptions.ENTITIES) {
/* 1420 */       this.showOther = !this.showOther;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     }
/* 1429 */     else if (par1EnumOptions == ModOptions.SLIME_CHUNKS) {
/* 1430 */       if (customSlimeSeedNeeded()) {
/* 1431 */         Minecraft.func_71410_x().func_147108_a((GuiScreen)new GuiSlimeSeed(this.modMain, (Minecraft.func_71410_x()).field_71462_r));
/*      */         return;
/*      */       } 
/* 1434 */       this.slimeChunks = !this.slimeChunks;
/* 1435 */     } else if (par1EnumOptions == ModOptions.SAFE_MAP) {
/* 1436 */       this.mapSafeMode = !this.mapSafeMode;
/* 1437 */       this.modMain.getInterfaces().getMinimap().setToResetImage(true);
/* 1438 */     } else if (par1EnumOptions == ModOptions.AA) {
/* 1439 */       this.antiAliasing = !this.antiAliasing;
/* 1440 */     } else if (par1EnumOptions == ModOptions.COLOURS) {
/* 1441 */       if (!this.modMain.getSupportMods().shouldUseWorldMapChunks())
/* 1442 */       { this.blockColours = (this.blockColours + 1) % blockColourTypes.length; }
/*      */       else
/* 1444 */       { (this.modMain.getSupportMods()).worldmapSupport.openSettings(); } 
/* 1445 */     } else if (par1EnumOptions == ModOptions.LIGHT) {
/* 1446 */       if (!this.modMain.getSupportMods().shouldUseWorldMapChunks())
/* 1447 */       { this.lighting = !this.lighting; }
/*      */       else
/* 1449 */       { (this.modMain.getSupportMods()).worldmapSupport.openSettings(); } 
/* 1450 */     } else if (par1EnumOptions == ModOptions.COMPASS) {
/* 1451 */       this.compassOverWaypoints = !this.compassOverWaypoints;
/* 1452 */     } else if (par1EnumOptions == ModOptions.BIOME) {
/* 1453 */       this.showBiome = !this.showBiome;
/* 1454 */     } else if (par1EnumOptions == ModOptions.ENTITY_HEIGHT) {
/* 1455 */       this.showEntityHeight = !this.showEntityHeight;
/* 1456 */     } else if (par1EnumOptions == ModOptions.FLOWERS) {
/* 1457 */       if (!this.modMain.getSupportMods().shouldUseWorldMapChunks())
/* 1458 */       { this.showFlowers = !this.showFlowers; }
/*      */       else
/* 1460 */       { (this.modMain.getSupportMods()).worldmapSupport.openSettings(); } 
/* 1461 */     } else if (par1EnumOptions == ModOptions.KEEP_WP_NAMES) {
/* 1462 */       this.keepWaypointNames = !this.keepWaypointNames;
/* 1463 */     } else if (par1EnumOptions == ModOptions.ARROW_COLOUR) {
/* 1464 */       this.arrowColour++;
/* 1465 */       if (this.arrowColour == this.arrowColours.length)
/* 1466 */         this.arrowColour = -1; 
/* 1467 */     } else if (par1EnumOptions == ModOptions.SMOOTH_DOTS) {
/* 1468 */       this.smoothDots = !this.smoothDots;
/* 1469 */     } else if (par1EnumOptions == ModOptions.PLAYER_HEADS) {
/* 1470 */       this.playerHeads = !this.playerHeads;
/* 1471 */     } else if (par1EnumOptions == ModOptions.WORLD_MAP) {
/* 1472 */       this.worldMap = !this.worldMap;
/* 1473 */     } else if (par1EnumOptions == ModOptions.CAPES) {
/* 1474 */       Patreon4.showCapes = !Patreon4.showCapes;
/* 1475 */       Patreon4.saveSettings();
/* 1476 */     } else if (par1EnumOptions == ModOptions.TERRAIN_DEPTH) {
/* 1477 */       if (!this.modMain.getSupportMods().shouldUseWorldMapChunks())
/* 1478 */       { this.terrainDepth = !this.terrainDepth; }
/*      */       else
/* 1480 */       { (this.modMain.getSupportMods()).worldmapSupport.openSettings(); } 
/* 1481 */     } else if (par1EnumOptions == ModOptions.TERRAIN_SLOPES) {
/* 1482 */       if (!this.modMain.getSupportMods().shouldUseWorldMapChunks())
/* 1483 */       { if (this.terrainSlopes && !this.terrainSlopesExperiment) {
/* 1484 */           this.terrainSlopesExperiment = true;
/*      */         } else {
/* 1486 */           this.terrainSlopes = !this.terrainSlopes;
/* 1487 */           this.terrainSlopesExperiment = false;
/*      */         }  }
/*      */       else
/* 1490 */       { (this.modMain.getSupportMods()).worldmapSupport.openSettings(); } 
/* 1491 */     } else if (par1EnumOptions == ModOptions.MAIN_ENTITY_AS) {
/* 1492 */       this.mainEntityAs = (this.mainEntityAs + 1) % 3;
/* 1493 */     } else if (par1EnumOptions == ModOptions.BLOCK_TRANSPARENCY) {
/* 1494 */       this.blockTransparency = !this.blockTransparency;
/* 1495 */       this.modMain.getInterfaces().getMinimap().setToResetImage(true);
/* 1496 */     } else if (par1EnumOptions == ModOptions.HIDE_WORLD_NAMES) {
/* 1497 */       this.hideWorldNames = (this.hideWorldNames + 1) % 3;
/* 1498 */     } else if (par1EnumOptions == ModOptions.OPEN_SLIME_SETTINGS) {
/* 1499 */       this.openSlimeSettings = !this.openSlimeSettings;
/* 1500 */     } else if (par1EnumOptions == ModOptions.ALWAYS_SHOW_DISTANCE) {
/* 1501 */       this.alwaysShowDistance = !this.alwaysShowDistance;
/* 1502 */     } else if (par1EnumOptions == ModOptions.PLAYER_NAMES) {
/* 1503 */       this.playerNames = !this.playerNames;
/* 1504 */     } else if (par1EnumOptions == ModOptions.SHOW_LIGHT_LEVEL) {
/* 1505 */       this.showLightLevel = !this.showLightLevel;
/* 1506 */     } else if (par1EnumOptions == ModOptions.RENDER_LAYER) {
/* 1507 */       this.renderLayerIndex = (this.renderLayerIndex + 1) % ForgeEventHandler.OVERLAY_LAYERS.length;
/* 1508 */     } else if (par1EnumOptions == ModOptions.SHOW_TIME) {
/* 1509 */       this.showTime = (this.showTime + 1) % 3;
/* 1510 */     } else if (par1EnumOptions == ModOptions.BIOMES_VANILLA) {
/* 1511 */       if (!this.modMain.getSupportMods().shouldUseWorldMapChunks())
/* 1512 */       { this.biomeColorsVanillaMode = !this.biomeColorsVanillaMode; }
/*      */       else
/* 1514 */       { (this.modMain.getSupportMods()).worldmapSupport.openSettings(); } 
/* 1515 */     } else if (par1EnumOptions == ModOptions.CENTERED_ENLARGED) {
/* 1516 */       this.centeredEnlarged = !this.centeredEnlarged;
/* 1517 */     } else if (par1EnumOptions == ModOptions.ZOOMED_OUT_ENLARGED) {
/* 1518 */       this.zoomedOutEnlarged = !this.zoomedOutEnlarged;
/* 1519 */     } else if (par1EnumOptions == ModOptions.ENTITY_NAMETAGS) {
/* 1520 */       this.entityNametags = !this.entityNametags;
/* 1521 */     } else if (par1EnumOptions == ModOptions.MINIMAP_TEXT_ALIGN) {
/* 1522 */       this.minimapTextAlign = (this.minimapTextAlign + 1) % 3;
/* 1523 */     } else if (par1EnumOptions == ModOptions.SHOW_ANGLES) {
/* 1524 */       this.showAngles = !this.showAngles;
/* 1525 */     } else if (par1EnumOptions == ModOptions.COMPASS_ENABLED) {
/* 1526 */       this.compass = !this.compass;
/* 1527 */     } else if (par1EnumOptions == ModOptions.HIDE_WP_COORDS) {
/* 1528 */       this.hideWaypointCoordinates = !this.hideWaypointCoordinates;
/* 1529 */     } else if (par1EnumOptions == ModOptions.WAYPOINTS_ALL_SETS) {
/* 1530 */       this.renderAllSets = !this.renderAllSets;
/*      */     } 
/*      */     
/* 1533 */     saveSettings();
/* 1534 */     if ((Minecraft.func_71410_x()).field_71462_r != null)
/* 1535 */       (Minecraft.func_71410_x()).field_71462_r.func_73866_w_(); 
/*      */   }
/*      */   
/*      */   public void setOptionFloatValue(ModOptions options, float f) throws IOException {
/* 1539 */     if (options == ModOptions.OPACITY) {
/* 1540 */       this.minimapOpacity = f;
/*      */     }
/* 1542 */     if (options == ModOptions.WAYPOINTS_SCALE) {
/* 1543 */       this.waypointsScale = f;
/*      */     }
/* 1545 */     if (options == ModOptions.DOTS_SCALE) {
/* 1546 */       this.dotsScale = f;
/*      */     }
/* 1548 */     if (options == ModOptions.WAYPOINTS_DISTANCE) {
/* 1549 */       this.waypointsDistance = (int)f;
/*      */     }
/* 1551 */     if (options == ModOptions.WAYPOINTS_DISTANCE_MIN) {
/* 1552 */       this.waypointsDistanceMin = (int)f;
/*      */     }
/* 1554 */     if (options == ModOptions.ARROW_SCALE) {
/* 1555 */       this.arrowScale = f;
/*      */     }
/* 1557 */     if (options == ModOptions.HEIGHT_LIMIT) {
/* 1558 */       this.heightLimit = (int)f;
/*      */     }
/* 1560 */     if (options == ModOptions.WAYPOINT_OPACITY_INGAME) {
/* 1561 */       this.waypointOpacityIngame = (int)f;
/*      */     }
/* 1563 */     if (options == ModOptions.WAYPOINT_OPACITY_MAP) {
/* 1564 */       this.waypointOpacityMap = (int)f;
/*      */     }
/* 1566 */     if (options == ModOptions.WAYPOINT_LOOKING_ANGLE) {
/* 1567 */       this.lookingAtAngle = (int)f;
/*      */     }
/* 1569 */     if (options == ModOptions.WAYPOINT_VERTICAL_LOOKING_ANGLE) {
/* 1570 */       this.lookingAtAngleVertical = (int)f;
/*      */     }
/* 1572 */     if (options == ModOptions.CAVE_MAPS_DEPTH) {
/* 1573 */       this.caveMapsDepth = (int)f;
/*      */     }
/* 1575 */     if (options == ModOptions.CHUNK_GRID) {
/* 1576 */       this.chunkGrid = (int)f;
/*      */     }
/*      */ 
/*      */     
/* 1580 */     if (options == ModOptions.PLAYER_ARROW_OPACITY) {
/* 1581 */       this.playerArrowOpacity = (int)f;
/*      */     }
/* 1583 */     saveSettings();
/*      */   }
/*      */   
/*      */   public float getOptionFloatValue(ModOptions options) {
/* 1587 */     if (options == ModOptions.OPACITY) {
/* 1588 */       return this.minimapOpacity;
/*      */     }
/* 1590 */     if (options == ModOptions.WAYPOINTS_SCALE) {
/* 1591 */       return this.waypointsScale;
/*      */     }
/* 1593 */     if (options == ModOptions.DOTS_SCALE) {
/* 1594 */       return this.dotsScale;
/*      */     }
/* 1596 */     if (options == ModOptions.WAYPOINTS_DISTANCE) {
/* 1597 */       return this.waypointsDistance;
/*      */     }
/* 1599 */     if (options == ModOptions.WAYPOINTS_DISTANCE_MIN) {
/* 1600 */       return this.waypointsDistanceMin;
/*      */     }
/* 1602 */     if (options == ModOptions.ARROW_SCALE) {
/* 1603 */       return this.arrowScale;
/*      */     }
/* 1605 */     if (options == ModOptions.HEIGHT_LIMIT) {
/* 1606 */       return this.heightLimit;
/*      */     }
/* 1608 */     if (options == ModOptions.WAYPOINT_OPACITY_INGAME) {
/* 1609 */       return this.waypointOpacityIngame;
/*      */     }
/* 1611 */     if (options == ModOptions.WAYPOINT_OPACITY_MAP) {
/* 1612 */       return this.waypointOpacityMap;
/*      */     }
/* 1614 */     if (options == ModOptions.WAYPOINT_LOOKING_ANGLE) {
/* 1615 */       return this.lookingAtAngle;
/*      */     }
/* 1617 */     if (options == ModOptions.WAYPOINT_VERTICAL_LOOKING_ANGLE) {
/* 1618 */       return this.lookingAtAngleVertical;
/*      */     }
/* 1620 */     if (options == ModOptions.CAVE_MAPS_DEPTH) {
/* 1621 */       return this.caveMapsDepth;
/*      */     }
/* 1623 */     if (options == ModOptions.CHUNK_GRID) {
/* 1624 */       return this.chunkGrid;
/*      */     }
/* 1626 */     if (options == ModOptions.PLAYER_ARROW_OPACITY) {
/* 1627 */       return this.playerArrowOpacity;
/*      */     }
/* 1629 */     return 1.0F;
/*      */   }
/*      */   
/*      */   public boolean minimapDisabled() {
/* 1633 */     return ((serverSettings & 0x1) != 1);
/*      */   }
/*      */   
/*      */   public boolean minimapDisplayPlayersDisabled() {
/* 1637 */     return ((serverSettings & 0x400) != 1024);
/*      */   }
/*      */   
/*      */   public boolean minimapDisplayMobsDisabled() {
/* 1641 */     return ((serverSettings & 0x800) != 2048);
/*      */   }
/*      */   
/*      */   public boolean minimapDisplayItemsDisabled() {
/* 1645 */     return ((serverSettings & 0x1000) != 4096);
/*      */   }
/*      */   
/*      */   public boolean minimapDisplayOtherDisabled() {
/* 1649 */     return ((serverSettings & 0x2000) != 8192);
/*      */   }
/*      */   
/*      */   public boolean caveMapsDisabled() {
/* 1653 */     return ((serverSettings & 0x4000) != 16384);
/*      */   }
/*      */   
/*      */   public boolean showOtherTeamDisabled() {
/* 1657 */     return ((serverSettings & 0x8000) != 32768);
/*      */   }
/*      */   
/*      */   public boolean showWaypointsDisabled() {
/* 1661 */     return ((serverSettings & 0x10000) != 65536);
/*      */   }
/*      */   
/*      */   public boolean deathpointsDisabled() {
/* 1665 */     return ((serverSettings & 0x200000) == 0);
/*      */   }
/*      */   
/*      */   public void resetServerSettings() {
/* 1669 */     serverSettings = defaultSettings;
/*      */   }
/*      */ 
/*      */   
/*      */   public static void setServerSettings() {}
/*      */ 
/*      */   
/*      */   public boolean isPlayerNames() {
/* 1677 */     return this.playerNames;
/*      */   }
/*      */   
/*      */   public boolean getTerrainSlopesExperiment() {
/* 1681 */     return this.terrainSlopesExperiment;
/*      */   }
/*      */ }


/* Location:              C:\Users\ioanm\Downloads\Xaeros_Minimap_20.15.3_Forge_1.12.jar!\xaero\common\settings\ModSettings.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */