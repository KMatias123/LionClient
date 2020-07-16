/*     */ package xaero.minimap;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.CopyOption;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.StandardCopyOption;
/*     */ import net.minecraftforge.common.MinecraftForge;
/*     */ import net.minecraftforge.fml.common.Mod;
/*     */ import net.minecraftforge.fml.common.Mod.EventHandler;
/*     */ import net.minecraftforge.fml.common.Mod.Instance;
/*     */ import net.minecraftforge.fml.common.event.FMLInitializationEvent;
/*     */ import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
/*     */ import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
/*     */ import net.minecraftforge.fml.common.network.NetworkRegistry;
/*     */ import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
/*     */ import net.minecraftforge.fml.relauncher.Side;
/*     */ import xaero.common.IXaeroMinimap;
/*     */ import xaero.common.api.spigot.message.in.InMessageWaypoint;
/*     */ import xaero.common.api.spigot.message.out.OutMessageHandshake;
/*     */ import xaero.common.api.spigot.message.out.OutMessageWaypoint;
/*     */ import xaero.common.controls.ControlsHandler;
/*     */ import xaero.common.controls.event.KeyEventHandler;
/*     */ import xaero.common.core.XaeroMinimapCore;
/*     */ import xaero.common.events.FMLEventHandler;
/*     */ import xaero.common.events.ForgeEventHandler;
/*     */ import xaero.common.file.SimpleBackup;
/*     */ import xaero.common.gui.GuiHelper;
/*     */ import xaero.common.gui.widget.WidgetLoadingHandler;
/*     */ import xaero.common.gui.widget.WidgetScreenHandler;
/*     */ import xaero.common.interfaces.IInterfaceLoader;
/*     */ import xaero.common.interfaces.InterfaceManager;
/*     */ import xaero.common.interfaces.render.InterfaceRenderer;
/*     */ import xaero.common.minimap.waypoints.WaypointSharingHandler;
/*     */ import xaero.common.minimap.waypoints.WaypointsManager;
/*     */ import xaero.common.misc.Internet;
/*     */ import xaero.common.mods.SupportMods;
/*     */ import xaero.common.settings.ModSettings;
/*     */ import xaero.common.validator.FieldValidatorHolder;
/*     */ import xaero.common.validator.NumericFieldValidator;
/*     */ import xaero.minimap.controls.MinimapControlsHandler;
/*     */ import xaero.minimap.gui.MinimapGuiHelper;
/*     */ import xaero.minimap.interfaces.MinimapInterfaceLoader;
/*     */ import xaero.patreon.Patreon4;
/*     */ import xaero.patreon.PatreonMod2;
/*     */ 
/*     */ @Mod(modid = "xaerominimap", name = "Xaero's Minimap", version = "20.15.3", clientSideOnly = true, acceptedMinecraftVersions = "[1.12,1.12.2]")
/*     */ public class XaeroMinimap
/*     */   implements IXaeroMinimap
/*     */ {
/*     */   @Instance("xaerominimap")
/*     */   public static XaeroMinimap instance;
/*     */   private static final String versionID = "1.12_20.15.3";
/*     */   private int newestUpdateID;
/*     */   private boolean isOutdated = true;
/*  57 */   private String fileLayoutID = "1.12_20.15.3".endsWith("fair") ? "minimapfair" : "minimap";
/*     */   private String latestVersion;
/*  59 */   private static final File old_optionsFile = new File("xaerominimap.txt");
/*  60 */   private static final File oldConfigFile = new File("config/xaerominimap.txt");
/*     */   
/*     */   private ModSettings settings;
/*  63 */   private String message = "";
/*     */   
/*     */   private ControlsHandler controls;
/*     */   private ForgeEventHandler events;
/*     */   private FMLEventHandler fmlEvents;
/*     */   private InterfaceManager interfaces;
/*     */   private InterfaceRenderer interfaceRenderer;
/*     */   private GuiHelper guiHelper;
/*     */   private SupportMods supportMods;
/*     */   private WaypointsManager waypointsManager;
/*     */   private WaypointSharingHandler waypointSharing;
/*     */   private FieldValidatorHolder fieldValidators;
/*     */   private WidgetScreenHandler widgetScreenHandler;
/*     */   private WidgetLoadingHandler widgetLoader;
/*  77 */   private File modJAR = null;
/*     */   private File configFile;
/*     */   public File waypointsFile;
/*     */   public File waypointsFolder;
/*     */   private SimpleNetworkWrapper network;
/*     */   
/*     */   @EventHandler
/*     */   public void preInit(FMLPreInitializationEvent event) throws IOException {
/*     */     Path wrongWaypointsFolder2;
/*  86 */     SupportMods.checkForMinimapDuplicates();
/*  87 */     if (event.getSourceFile().getName().endsWith(".jar"))
/*  88 */       this.modJAR = event.getSourceFile(); 
/*  89 */     Path config = event.getModConfigurationDirectory().toPath();
/*  90 */     this.waypointsFile = config.resolve("xaerowaypoints.txt").toFile();
/*     */     
/*  92 */     Path wrongWaypointsFolder3 = config.resolve("XaeroWaypoints");
/*  93 */     if (this.modJAR != null) {
/*  94 */       wrongWaypointsFolder2 = this.modJAR.toPath().getParent().resolve("XaeroWaypoints");
/*     */     } else {
/*  96 */       wrongWaypointsFolder2 = config.getParent().resolve("mods").resolve("XaeroWaypoints");
/*  97 */     }  Path wrongWaypointsFolder4 = (new File(config.toFile().getCanonicalPath())).toPath().getParent().resolve("XaeroWaypoints");
/*  98 */     Path wrongWaypointsFolder5 = config.getParent().resolve("XaeroWaypoints");
/*  99 */     this.waypointsFolder = (new File("XaeroWaypoints")).toPath().toAbsolutePath().toFile();
/* 100 */     if (wrongWaypointsFile.exists() && !this.waypointsFile.exists())
/* 101 */       Files.move(wrongWaypointsFile.toPath(), this.waypointsFile.toPath(), new CopyOption[0]); 
/* 102 */     if (wrongWaypointsFolder.exists() && !this.waypointsFolder.exists()) {
/* 103 */       Files.move(wrongWaypointsFolder.toPath(), this.waypointsFolder.toPath(), new CopyOption[0]);
/* 104 */     } else if (wrongWaypointsFolder2.toFile().exists() && !this.waypointsFolder.exists()) {
/* 105 */       Files.move(wrongWaypointsFolder2, this.waypointsFolder.toPath(), new CopyOption[0]);
/* 106 */     } else if (wrongWaypointsFolder3.toFile().exists() && !this.waypointsFolder.exists()) {
/* 107 */       Files.move(wrongWaypointsFolder3, this.waypointsFolder.toPath(), new CopyOption[0]);
/* 108 */     } else if (wrongWaypointsFolder4.toFile().exists() && !this.waypointsFolder.exists()) {
/* 109 */       Files.move(wrongWaypointsFolder4, this.waypointsFolder.toPath(), new CopyOption[0]);
/* 110 */     } else if (wrongWaypointsFolder5.toFile().exists() && !this.waypointsFolder.exists()) {
/* 111 */       Files.move(wrongWaypointsFolder5, this.waypointsFolder.toPath(), new CopyOption[0]);
/*     */     } 
/* 113 */     Path waypointsFolderBackup062020 = this.waypointsFolder.toPath().resolveSibling(this.waypointsFolder.getName() + "_BACKUP062020");
/* 114 */     if (!Files.exists(waypointsFolderBackup062020, new java.nio.file.LinkOption[0]) && this.waypointsFolder.exists()) {
/* 115 */       System.out.println("Backing up XaeroWaypoints...");
/* 116 */       SimpleBackup.copyDirectoryWithContents(this.waypointsFolder.toPath(), waypointsFolderBackup062020, 32, new CopyOption[] { StandardCopyOption.REPLACE_EXISTING });
/* 117 */       System.out.println("Done backing up XaeroWaypoints!");
/*     */     } 
/*     */     
/* 120 */     this.configFile = config.resolve("xaerominimap.txt").toFile();
/* 121 */     if (oldConfigFile.exists() && !this.configFile.getAbsolutePath().equals(oldConfigFile.getAbsolutePath()))
/* 122 */       Files.move(oldConfigFile.toPath(), this.configFile.toPath(), new CopyOption[0]); 
/* 123 */     Path waypointTempToAddFolder = this.waypointsFolder.toPath().resolve("temp_to_add");
/* 124 */     if (Files.exists(waypointTempToAddFolder, new java.nio.file.LinkOption[0])) {
/* 125 */       ModSettings.copyTempFilesBack(waypointTempToAddFolder);
/*     */     }
/*     */   }
/*     */   
/*     */   public String getVersionsURL() {
/* 130 */     return "http://data.chocolateminecraft.com/Versions/Minimap.txt";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getUpdateLink() {
/* 135 */     return "http://chocolateminecraft.com/update/minimap.html";
/*     */   }
/*     */ 
/*     */   
/*     */   @EventHandler
/*     */   public void load(FMLInitializationEvent event) throws IOException {
/* 141 */     Patreon4.checkPatreon();
/* 142 */     Patreon4.rendersCapes = this.fileLayoutID;
/*     */ 
/*     */     
/* 145 */     this.waypointsManager = new WaypointsManager(this);
/* 146 */     this.waypointSharing = new WaypointSharingHandler(this);
/* 147 */     this.fieldValidators = new FieldValidatorHolder(new NumericFieldValidator());
/* 148 */     this.interfaceRenderer = new InterfaceRenderer(this);
/* 149 */     this.widgetScreenHandler = new WidgetScreenHandler();
/* 150 */     this.widgetLoader = new WidgetLoadingHandler(this.widgetScreenHandler);
/* 151 */     MinimapInterfaceLoader interfaceLoader = new MinimapInterfaceLoader();
/* 152 */     this.interfaces = new InterfaceManager(this, (IInterfaceLoader)interfaceLoader);
/* 153 */     this.settings = new ModSettings(this);
/* 154 */     this.controls = (ControlsHandler)new MinimapControlsHandler(this);
/* 155 */     XaeroMinimapCore.modMain = this;
/* 156 */     if (old_optionsFile.exists() && !this.configFile.exists()) {
/* 157 */       this.configFile.getParentFile().mkdirs();
/* 158 */       Files.move(old_optionsFile.toPath(), this.configFile.toPath(), new CopyOption[0]);
/*     */     } 
/* 160 */     if (old_waypointsFile.exists() && !this.waypointsFile.exists()) {
/* 161 */       this.waypointsFile.getParentFile().mkdirs();
/* 162 */       Files.move(old_waypointsFile.toPath(), this.waypointsFile.toPath(), new CopyOption[0]);
/*     */     } 
/*     */   }
/*     */   
/*     */   @EventHandler
/*     */   public void load(FMLPostInitializationEvent event) throws IOException {
/* 168 */     this.settings.loadSettings();
/* 169 */     this.events = new ForgeEventHandler(this);
/* 170 */     KeyEventHandler keyEventHandler = new KeyEventHandler();
/* 171 */     this.fmlEvents = new FMLEventHandler(this, keyEventHandler);
/* 172 */     Internet.checkModVersion(this);
/* 173 */     if (Patreon4.patronPledge >= 5 && this.isOutdated) {
/* 174 */       (getPatreon()).modJar = this.modJAR;
/* 175 */       (getPatreon()).currentVersion = "1.12_20.15.3";
/* 176 */       (getPatreon()).latestVersion = this.latestVersion;
/* 177 */       Patreon4.addOutdatedMod(getPatreon());
/*     */     } 
/*     */ 
/*     */     
/* 181 */     MinecraftForge.EVENT_BUS.register(this.events);
/* 182 */     MinecraftForge.EVENT_BUS.register(this.fmlEvents);
/* 183 */     this.guiHelper = (GuiHelper)new MinimapGuiHelper(this);
/* 184 */     this.supportMods = new SupportMods(this);
/*     */     
/* 186 */     this.network = NetworkRegistry.INSTANCE.newSimpleChannel("XaeroMinimap");
/* 187 */     this.network.registerMessage(InMessageWaypoint.Handler.class, InMessageWaypoint.class, 0, Side.CLIENT);
/* 188 */     this.network.registerMessage(OutMessageWaypoint.Handler.class, OutMessageWaypoint.class, 1, Side.SERVER);
/* 189 */     this.network.registerMessage(OutMessageHandshake.Handler.class, OutMessageHandshake.class, 2, Side.SERVER);
/*     */   }
/*     */ 
/*     */   
/*     */   public SimpleNetworkWrapper getNetwork() {
/* 194 */     return this.network;
/*     */   }
/*     */ 
/*     */   
/*     */   public File getOldOptionsFile() {
/* 199 */     return old_optionsFile;
/*     */   }
/*     */ 
/*     */   
/*     */   public File getOldConfigFile() {
/* 204 */     return oldConfigFile;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFileLayoutID() {
/* 210 */     return this.fileLayoutID;
/*     */   }
/*     */ 
/*     */   
/*     */   public File getConfigFile() {
/* 215 */     return this.configFile;
/*     */   }
/*     */ 
/*     */   
/*     */   public File getModJAR() {
/* 220 */     return this.modJAR;
/*     */   }
/*     */ 
/*     */   
/*     */   public ModSettings getSettings() {
/* 225 */     return this.settings;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSettings(ModSettings minimapSettings) {
/* 230 */     this.settings = minimapSettings;
/*     */   }
/*     */ 
/*     */   
/*     */   public void resetSettings() {
/* 235 */     this.settings = new ModSettings(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public ControlsHandler getControls() {
/* 240 */     return this.controls;
/*     */   }
/*     */ 
/*     */   
/*     */   public SupportMods getSupportMods() {
/* 245 */     return this.supportMods;
/*     */   }
/*     */ 
/*     */   
/*     */   public InterfaceManager getInterfaces() {
/* 250 */     return this.interfaces;
/*     */   }
/*     */ 
/*     */   
/*     */   public ForgeEventHandler getEvents() {
/* 255 */     return this.events;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOutdated() {
/* 260 */     return this.isOutdated;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setOutdated(boolean value) {
/* 265 */     this.isOutdated = value;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getMessage() {
/* 270 */     return this.message;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setMessage(String string) {
/* 275 */     this.message = string;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getLatestVersion() {
/* 280 */     return this.latestVersion;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setLatestVersion(String string) {
/* 285 */     this.latestVersion = string;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getNewestUpdateID() {
/* 290 */     return this.newestUpdateID;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setNewestUpdateID(int parseInt) {
/* 295 */     this.newestUpdateID = parseInt;
/*     */   }
/*     */ 
/*     */   
/*     */   public PatreonMod2 getPatreon() {
/* 300 */     return (PatreonMod2)Patreon4.mods.get(this.fileLayoutID);
/*     */   }
/*     */ 
/*     */   
/*     */   public GuiHelper getGuiHelper() {
/* 305 */     return this.guiHelper;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getVersionID() {
/* 310 */     return "1.12_20.15.3";
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getSettingsKey() {
/* 315 */     return MinimapControlsHandler.keyBindSettings;
/*     */   }
/*     */ 
/*     */   
/*     */   public File getWaypointsFile() {
/* 320 */     return this.waypointsFile;
/*     */   }
/*     */ 
/*     */   
/*     */   public File getWaypointsFolder() {
/* 325 */     return this.waypointsFolder;
/*     */   }
/*     */ 
/*     */   
/*     */   public WaypointsManager getWaypointsManager() {
/* 330 */     return this.waypointsManager;
/*     */   }
/*     */ 
/*     */   
/*     */   public FieldValidatorHolder getFieldValidators() {
/* 335 */     return this.fieldValidators;
/*     */   }
/*     */ 
/*     */   
/*     */   public InterfaceRenderer getInterfaceRenderer() {
/* 340 */     return this.interfaceRenderer;
/*     */   }
/*     */ 
/*     */   
/*     */   public WaypointSharingHandler getWaypointSharing() {
/* 345 */     return this.waypointSharing;
/*     */   }
/*     */ 
/*     */   
/*     */   public WidgetScreenHandler getWidgetScreenHandler() {
/* 350 */     return this.widgetScreenHandler;
/*     */   }
/*     */ 
/*     */   
/*     */   public WidgetLoadingHandler getWidgetLoader() {
/* 355 */     return this.widgetLoader;
/*     */   }
/*     */ }


/* Location:              C:\Users\ioanm\Downloads\Xaeros_Minimap_20.15.3_Forge_1.12.jar!\xaero\minimap\XaeroMinimap.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */