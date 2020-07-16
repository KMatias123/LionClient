/*     */ package xaero.common.api.spigot;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
/*     */ import xaero.common.IXaeroMinimap;
/*     */ import xaero.common.api.spigot.message.out.OutMessageHandshake;
/*     */ import xaero.common.minimap.waypoints.WaypointWorld;
/*     */ import xaero.common.minimap.waypoints.WaypointWorldContainer;
/*     */ import xaero.common.minimap.waypoints.WaypointsManager;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ServerWaypointStorage
/*     */ {
/*     */   public static final String CHANNEL_NAME = "XaeroMinimap";
/*     */   private static WaypointsManager waypointsManager;
/*  22 */   private static String containerID = null;
/*  23 */   public static String autoWorldUID = null;
/*     */   public static List<String[]> worldsToAdd;
/*     */   private static boolean handshakeSent = false;
/*     */   
/*     */   static {
/*  28 */     worldsToAdd = (List)new ArrayList<>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean working() {
/*  35 */     return (containerID != null);
/*     */   }
/*     */   
/*     */   public static void update(IXaeroMinimap modMain, WaypointsManager wm) {
/*  39 */     waypointsManager = wm;
/*  40 */     String realContainer = getWorldContainerId(modMain);
/*  41 */     if ((realContainer == null || !realContainer.equals(containerID)) && containerID != null) {
/*  42 */       handshakeSent = false;
/*     */     }
/*  44 */     containerID = realContainer;
/*     */     
/*  46 */     if (containerID == null) {
/*     */       return;
/*     */     }
/*  49 */     if (!handshakeSent) {
/*  50 */       modMain.getNetwork().sendToServer((IMessage)new OutMessageHandshake());
/*  51 */       handshakeSent = true;
/*     */     } 
/*     */ 
/*     */     
/*  55 */     while (worldsToAdd.size() > 0) {
/*  56 */       String[] args = worldsToAdd.remove(0);
/*  57 */       String multiworldID = getMultiWorldId(args[0]);
/*  58 */       WaypointWorldContainer container = waypointsManager.addWorldContainer(containerID);
/*  59 */       container.addWorld(multiworldID);
/*  60 */       container.addName(multiworldID, args[1]);
/*     */     } 
/*     */   }
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
/*     */   private static String getWorldContainerId(IXaeroMinimap modMain) {
/*  74 */     if (Minecraft.func_71410_x().func_147114_u() != null && Minecraft.func_71410_x().func_147104_D() != null) {
/*  75 */       String serverIP = (modMain.getSettings()).differentiateByServerAddress ? (Minecraft.func_71410_x().func_147104_D()).field_78845_b : "Any Address";
/*  76 */       return waypointsManager.ignoreContainerCase("Multiplayer_" + serverIP.replace(":", "§").replace("_", "%us%"), containerID);
/*     */     } 
/*  78 */     return null;
/*     */   }
/*     */   
/*     */   private static String getMultiWorldId(String UID) {
/*  82 */     return "plugin" + UID;
/*     */   }
/*     */   
/*     */   public static String getAutoContainer() {
/*  86 */     return containerID;
/*     */   }
/*     */   
/*     */   public static String getAutoWorld() {
/*  90 */     return getMultiWorldId(autoWorldUID);
/*     */   }
/*     */   
/*     */   public static void addWorld(String worldUID, String name) {
/*  94 */     worldsToAdd.add(new String[] { worldUID, name });
/*     */   }
/*     */   
/*     */   public static WaypointWorld getWorld(String worldUID) {
/*  98 */     return waypointsManager.getWorld(containerID, getMultiWorldId(worldUID));
/*     */   }
/*     */   
/*     */   public static void removeWaypoint(int id) {
/* 102 */     WaypointWorldContainer container = (WaypointWorldContainer)waypointsManager.getWaypointMap().get(containerID);
/* 103 */     if (container == null)
/*     */       return; 
/* 105 */     ArrayList<WaypointWorld> worlds = new ArrayList<>(container.worlds.values());
/* 106 */     for (int i = 0; i < worlds.size(); i++) {
/* 107 */       WaypointWorld w = worlds.get(i);
/* 108 */       if (!w.getServerWaypoints().isEmpty())
/*     */       {
/* 110 */         if (w.getServerWaypoints().remove(Integer.valueOf(id)) != null)
/*     */           return; 
/*     */       }
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\ioanm\Downloads\Xaeros_Minimap_20.15.3_Forge_1.12.jar!\xaero\common\api\spigot\ServerWaypointStorage.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */