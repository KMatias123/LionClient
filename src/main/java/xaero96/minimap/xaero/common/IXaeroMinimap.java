/*    */ package xaero.common;
/*    */ 
/*    */ import java.io.File;
/*    */ import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
/*    */ import xaero.common.controls.ControlsHandler;
/*    */ import xaero.common.events.ForgeEventHandler;
/*    */ import xaero.common.gui.GuiHelper;
/*    */ import xaero.common.gui.widget.WidgetLoadingHandler;
/*    */ import xaero.common.gui.widget.WidgetScreenHandler;
/*    */ import xaero.common.interfaces.InterfaceManager;
/*    */ import xaero.common.interfaces.render.InterfaceRenderer;
/*    */ import xaero.common.minimap.waypoints.WaypointSharingHandler;
/*    */ import xaero.common.minimap.waypoints.WaypointsManager;
/*    */ import xaero.common.mods.SupportMods;
/*    */ import xaero.common.settings.ModSettings;
/*    */ import xaero.common.validator.FieldValidatorHolder;
/*    */ import xaero.patreon.PatreonMod2;
/*    */ 
/*    */ public interface IXaeroMinimap
/*    */ {
/* 21 */   public static final File old_waypointsFile = new File("xaerowaypoints.txt");
/* 22 */   public static final File wrongWaypointsFile = new File("config/xaerowaypoints.txt");
/* 23 */   public static final File wrongWaypointsFolder = new File("mods/XaeroWaypoints");
/*    */   
/*    */   File getOldOptionsFile();
/*    */   
/*    */   File getOldConfigFile();
/*    */   
/*    */   String getVersionID();
/*    */   
/*    */   String getFileLayoutID();
/*    */   
/*    */   File getConfigFile();
/*    */   
/*    */   File getModJAR();
/*    */   
/*    */   ModSettings getSettings();
/*    */   
/*    */   void setSettings(ModSettings paramModSettings);
/*    */   
/*    */   ControlsHandler getControls();
/*    */   
/*    */   SupportMods getSupportMods();
/*    */   
/*    */   InterfaceManager getInterfaces();
/*    */   
/*    */   InterfaceRenderer getInterfaceRenderer();
/*    */   
/*    */   ForgeEventHandler getEvents();
/*    */   
/*    */   boolean isOutdated();
/*    */   
/*    */   void setOutdated(boolean paramBoolean);
/*    */   
/*    */   String getMessage();
/*    */   
/*    */   void setMessage(String paramString);
/*    */   
/*    */   String getLatestVersion();
/*    */   
/*    */   void setLatestVersion(String paramString);
/*    */   
/*    */   int getNewestUpdateID();
/*    */   
/*    */   void setNewestUpdateID(int paramInt);
/*    */   
/*    */   PatreonMod2 getPatreon();
/*    */   
/*    */   GuiHelper getGuiHelper();
/*    */   
/*    */   String getVersionsURL();
/*    */   
/*    */   void resetSettings();
/*    */   
/*    */   String getUpdateLink();
/*    */   
/*    */   Object getSettingsKey();
/*    */   
/*    */   SimpleNetworkWrapper getNetwork();
/*    */   
/*    */   File getWaypointsFile();
/*    */   
/*    */   File getWaypointsFolder();
/*    */   
/*    */   WaypointsManager getWaypointsManager();
/*    */   
/*    */   WaypointSharingHandler getWaypointSharing();
/*    */   
/*    */   FieldValidatorHolder getFieldValidators();
/*    */   
/*    */   WidgetScreenHandler getWidgetScreenHandler();
/*    */   
/*    */   WidgetLoadingHandler getWidgetLoader();
/*    */ }


/* Location:              C:\Users\ioanm\Downloads\Xaeros_Minimap_20.15.3_Forge_1.12.jar!\xaero\common\IXaeroMinimap.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */