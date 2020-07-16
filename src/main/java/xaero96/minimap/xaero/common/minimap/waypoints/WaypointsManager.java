/*     */ package xaero.common.minimap.waypoints;
/*     */ 
/*     */ import com.mojang.realmsclient.dto.RealmsServer;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Hashtable;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.GuiScreen;
/*     */ import net.minecraft.client.gui.ScaledResolution;
/*     */ import net.minecraft.client.resources.I18n;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.util.text.ITextComponent;
/*     */ import net.minecraft.util.text.TextComponentString;
/*     */ import net.minecraft.util.text.event.ClickEvent;
/*     */ import net.minecraft.util.text.event.HoverEvent;
/*     */ import net.minecraft.world.DimensionType;
/*     */ import net.minecraft.world.World;
/*     */ import xaero.common.IXaeroMinimap;
/*     */ import xaero.common.api.spigot.ServerWaypointStorage;
/*     */ import xaero.common.misc.OptimizedMath;
/*     */ import xaero.common.settings.ModSettings;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class WaypointsManager
/*     */ {
/*     */   private IXaeroMinimap modMain;
/*     */   private Minecraft mc;
/*  35 */   private HashMap<String, WaypointWorldContainer> waypointMap = new HashMap<>();
/*  36 */   private WaypointSet waypoints = null;
/*  37 */   private List<Waypoint> serverWaypoints = null;
/*  38 */   public static final Hashtable<String, Hashtable<Integer, Waypoint>> customWaypoints = new Hashtable<>();
/*     */   
/*  40 */   private String containerID = null;
/*     */   private String containerIDIgnoreCaseCache;
/*  42 */   private String customContainerID = null;
/*  43 */   private String worldID = null;
/*  44 */   private String customWorldID = null;
/*     */   private BlockPos currentSpawn;
/*     */   private String currentSpawnRootContainerId;
/*     */   private RealmsServer latestRealm;
/*     */   public long setChanged;
/*     */   public static final String TELEPORT_ANYWAY_COMMAND = "/xaero_tp_anyway";
/*     */   private Waypoint teleportAnywayWP;
/*     */   private WaypointWorld teleportAnywayWorld;
/*     */   
/*     */   public WaypointsManager(IXaeroMinimap modMain) {
/*  54 */     this.modMain = modMain;
/*  55 */     this.mc = Minecraft.func_71410_x();
/*     */   }
/*     */   
/*     */   public boolean divideBy8(String worldContainerID) {
/*  59 */     return (worldContainerID != null && (Minecraft.func_71410_x()).field_71441_e != null && (Minecraft.func_71410_x()).field_71441_e.field_73011_w.func_186058_p() == DimensionType.NETHER && worldContainerID.endsWith(getDimensionDirectoryName(0)));
/*     */   }
/*     */   
/*     */   public String getDimensionDirectoryName(int dim) {
/*  63 */     return "dim%" + dim;
/*     */   }
/*     */   
/*     */   public DimensionType findDimensionType(String validatedName) {
/*  67 */     DimensionType[] allDimensionTypes = DimensionType.values();
/*  68 */     for (DimensionType dt : allDimensionTypes) {
/*  69 */       if (validatedName.equals(dt.func_186065_b().replaceAll("[^a-zA-Z0-9_]+", "")))
/*  70 */         return dt; 
/*  71 */     }  return null;
/*     */   }
/*     */   public Integer getDimensionForDirectoryName(String dirName) {
/*     */     int dimId;
/*  75 */     String dimIdPart = dirName.substring(4);
/*  76 */     if (!dimIdPart.matches("-{0,1}[0-9]+")) {
/*  77 */       return null;
/*     */     }
/*     */     try {
/*  80 */       dimId = Integer.parseInt(dimIdPart);
/*  81 */     } catch (NumberFormatException nfe) {
/*  82 */       return null;
/*     */     } 
/*  84 */     return Integer.valueOf(dimId);
/*     */   }
/*     */   
/*     */   private String getContainer(World world) {
/*  88 */     String dim = getDimensionDirectoryName(world.field_73011_w.getDimension());
/*     */     
/*  90 */     if (this.mc.func_71401_C() != null) {
/*  91 */       potentialContainerID = this.mc.func_71401_C().func_71270_I().replace("_", "%us%").replace("/", "%fs%").replace("\\", "%bs%") + "/" + dim;
/*  92 */     } else if (this.mc.func_147104_D() != null) {
/*  93 */       String serverIP = (this.modMain.getSettings()).differentiateByServerAddress ? (this.mc.func_147104_D()).field_78845_b : "Any Address";
/*  94 */       if (serverIP.contains(":"))
/*  95 */         serverIP = serverIP.substring(0, serverIP.indexOf(":")); 
/*  96 */       if (this.mc.func_147104_D() != null && ServerWaypointStorage.autoWorldUID != null)
/*  97 */       { potentialContainerID = ServerWaypointStorage.getAutoContainer(); }
/*     */       else
/*  99 */       { potentialContainerID = "Multiplayer_" + serverIP.replace(":", "§").replace("_", "%us%").replace("/", "%fs%").replace("\\", "%bs%") + "/" + dim; } 
/* 100 */     } else if (this.mc.func_181540_al() && this.latestRealm != null) {
/* 101 */       potentialContainerID = "Realms_" + this.latestRealm.ownerUUID + "." + this.latestRealm.id + "/" + dim;
/*     */     } else {
/* 103 */       potentialContainerID = "Unknown";
/* 104 */     }  String potentialContainerID = ignoreContainerCase(potentialContainerID, this.containerIDIgnoreCaseCache);
/* 105 */     this.containerIDIgnoreCaseCache = potentialContainerID;
/* 106 */     return potentialContainerID;
/*     */   }
/*     */   
/*     */   public String ignoreContainerCase(String potentialContainerID, String current) {
/* 110 */     if (potentialContainerID.equalsIgnoreCase(current)) {
/* 111 */       return current;
/*     */     }
/*     */ 
/*     */     
/* 115 */     Set<Map.Entry<String, WaypointWorldContainer>> entries = this.waypointMap.entrySet();
/* 116 */     for (Map.Entry<String, WaypointWorldContainer> e : entries) {
/* 117 */       String containerSearch = ((WaypointWorldContainer)e.getValue()).getEqualIgnoreCaseSub(potentialContainerID);
/* 118 */       if (containerSearch != null) {
/* 119 */         return containerSearch;
/*     */       }
/*     */     } 
/* 122 */     return potentialContainerID;
/*     */   }
/*     */   
/*     */   public String getCurrentContainerAndWorldID() {
/* 126 */     return getCurrentContainerID() + "_" + getCurrentWorldID();
/*     */   }
/*     */   
/*     */   private String getWorld(World world) {
/* 130 */     if (this.mc.func_71401_C() != null)
/* 131 */       return "waypoints"; 
/* 132 */     if (this.currentSpawn != null && this.currentSpawnRootContainerId.equals(getAutoRootContainerID())) {
/* 133 */       if (this.mc.func_147104_D() != null && ServerWaypointStorage.autoWorldUID != null) {
/* 134 */         return ServerWaypointStorage.getAutoWorld();
/*     */       }
/* 136 */       String actualMultiworldId = "mw" + (this.currentSpawn.func_177958_n() >> 6) + "," + (this.currentSpawn.func_177956_o() >> 6) + "," + (this.currentSpawn.func_177952_p() >> 6);
/* 137 */       if (this.modMain.getSupportMods().worldmap()) {
/* 138 */         int dimId = world.field_73011_w.getDimension();
/* 139 */         String worldmapMultiworldId = (this.modMain.getSupportMods()).worldmapSupport.getMultiworldId(dimId);
/* 140 */         if (worldmapMultiworldId != null)
/* 141 */           actualMultiworldId = worldmapMultiworldId; 
/*     */       } 
/* 143 */       WaypointWorldRootContainer rootContainer = getWorldContainer(this.containerID).getRootContainer();
/* 144 */       if (!rootContainer.isUsingMultiworldDetection()) {
/* 145 */         String defaultMultiworldId = rootContainer.getDefaultMultiworldId();
/* 146 */         if (defaultMultiworldId == null) {
/* 147 */           rootContainer.setDefaultMultiworldId(actualMultiworldId);
/* 148 */           rootContainer.saveConfig();
/* 149 */           return actualMultiworldId;
/*     */         } 
/* 151 */         return defaultMultiworldId;
/*     */       } 
/* 153 */       return actualMultiworldId;
/*     */     } 
/*     */     
/* 156 */     return null;
/*     */   }
/*     */   
/*     */   public String getCurrentContainerID() {
/* 160 */     if (this.customContainerID == null) {
/* 161 */       return this.containerID;
/*     */     }
/* 163 */     return this.customContainerID;
/*     */   }
/*     */   
/*     */   public String getCurrentWorldID() {
/* 167 */     if (this.customWorldID == null) {
/* 168 */       return this.worldID;
/*     */     }
/* 170 */     return this.customWorldID;
/*     */   }
/*     */   
/*     */   public WaypointWorld getCurrentWorld() {
/* 174 */     return getWorld(getCurrentContainerID(), getCurrentWorldID());
/*     */   }
/*     */   
/*     */   public WaypointWorld getAutoWorld() {
/* 178 */     return getWorld(getAutoContainerID(), getAutoWorldID());
/*     */   }
/*     */   
/*     */   public String getCurrentOriginContainerID() {
/* 182 */     if (getCurrentContainerID() == null)
/* 183 */       return null; 
/* 184 */     return getCurrentContainerID().split("/")[0];
/*     */   }
/*     */   
/*     */   public String getAutoRootContainerID() {
/* 188 */     if (this.containerID == null)
/* 189 */       return null; 
/* 190 */     return this.containerID.split("/")[0];
/*     */   }
/*     */   
/*     */   public String getAutoContainerID() {
/* 194 */     return this.containerID;
/*     */   }
/*     */   
/*     */   public String getAutoWorldID() {
/* 198 */     return this.worldID;
/*     */   }
/*     */   
/*     */   public WaypointWorld getWorld(String container, String world) {
/* 202 */     return addWorld(container, world);
/*     */   }
/*     */   
/*     */   public WaypointWorld addWorld(String container, String world) {
/* 206 */     if (container == null)
/* 207 */       return null; 
/* 208 */     WaypointWorldContainer wc = addWorldContainer(container);
/* 209 */     return wc.addWorld(world);
/*     */   }
/*     */   
/*     */   public WaypointWorldContainer getWorldContainer(String id) {
/* 213 */     return addWorldContainer(id);
/*     */   }
/*     */   
/*     */   public WaypointWorldContainer addWorldContainer(String id) {
/* 217 */     WaypointWorldContainer container = null;
/* 218 */     String[] subs = id.split("/");
/* 219 */     for (int i = 0; i < subs.length; i++) {
/* 220 */       if (i == 0) {
/* 221 */         container = this.waypointMap.get(subs[i]);
/* 222 */         if (container == null) {
/* 223 */           this.waypointMap.put(subs[i], container = new WaypointWorldRootContainer(this.modMain, subs[i]));
/* 224 */           WaypointWorldRootContainer rootContainer = (WaypointWorldRootContainer)container;
/* 225 */           if (!rootContainer.configLoaded)
/* 226 */             rootContainer.loadConfig(); 
/*     */         } 
/*     */       } else {
/* 229 */         container = container.addSubContainer(subs[i]);
/*     */       } 
/* 231 */     }  return container;
/*     */   }
/*     */   
/*     */   public WaypointWorldContainer getWorldContainerNullable(String id) {
/* 235 */     WaypointWorldContainer container = null;
/* 236 */     String[] subs = id.split("/");
/* 237 */     for (int i = 0; i < subs.length; i++) {
/* 238 */       if (i == 0) {
/* 239 */         container = this.waypointMap.get(subs[i]);
/*     */       } else {
/* 241 */         container = container.subContainers.get(subs[i]);
/* 242 */       }  if (container == null)
/* 243 */         return null; 
/*     */     } 
/* 245 */     return container;
/*     */   }
/*     */   
/*     */   public void removeContainer(String id) {
/* 249 */     WaypointWorldContainer container = null;
/* 250 */     String[] subs = id.split("/");
/* 251 */     for (int i = 0; i < subs.length; i++) {
/* 252 */       if (i == 0) {
/* 253 */         container = this.waypointMap.get(subs[i]);
/* 254 */         if (container == null)
/*     */           return; 
/* 256 */         if (i == subs.length - 1) {
/* 257 */           this.waypointMap.remove(subs[i]);
/*     */           return;
/*     */         } 
/* 260 */       } else if (container.containsSub(subs[i])) {
/* 261 */         if (i == subs.length - 1) {
/* 262 */           container.deleteSubContainer(subs[i]);
/*     */           return;
/*     */         } 
/* 265 */         container = container.addSubContainer(subs[i]);
/*     */       } else {
/*     */         return;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   public boolean containerExists(String id) {
/* 272 */     WaypointWorldContainer container = null;
/* 273 */     String[] subs = id.split("/");
/* 274 */     for (int i = 0; i < subs.length; i++) {
/* 275 */       if (i == 0) {
/* 276 */         container = this.waypointMap.get(subs[i]);
/* 277 */         if (container == null)
/* 278 */           return false; 
/* 279 */         if (i == subs.length - 1)
/* 280 */           return true; 
/* 281 */       } else if (container.containsSub(subs[i])) {
/* 282 */         if (i == subs.length - 1) {
/* 283 */           return true;
/*     */         }
/* 285 */         container = container.addSubContainer(subs[i]);
/*     */       } else {
/* 287 */         return false;
/*     */       } 
/* 289 */     }  return false;
/*     */   }
/*     */   
/*     */   public void updateWorldIds() {
/* 293 */     String oldRootContainerID = getAutoRootContainerID();
/* 294 */     String oldContainerID = this.containerID;
/* 295 */     String oldWorldID = this.worldID;
/* 296 */     this.containerID = getContainer((World)this.mc.field_71441_e);
/* 297 */     String potentialWorldID = getWorld((World)this.mc.field_71441_e);
/* 298 */     if (potentialWorldID == null) {
/* 299 */       this.containerID = oldContainerID;
/* 300 */       this.worldID = oldWorldID;
/*     */     } else {
/* 302 */       this.worldID = potentialWorldID;
/* 303 */       if (this.containerID != null && !this.containerID.equals(oldContainerID)) {
/* 304 */         if (oldWorldID != null && oldWorldID.startsWith("plugin")) {
/* 305 */           WaypointWorldContainer oldContainer = getWorldContainer(oldContainerID);
/* 306 */           ArrayList<WaypointWorld> worlds = new ArrayList<>(oldContainer.worlds.values());
/* 307 */           for (int i = 0; i < worlds.size(); i++)
/* 308 */             ((WaypointWorld)worlds.get(i)).getServerWaypoints().clear(); 
/*     */         } 
/* 310 */         String rootContainerId = getAutoRootContainerID();
/* 311 */         WaypointWorldContainer rootContainer = getWorldContainer(rootContainerId);
/* 312 */         rootContainer.renameOldContainer(this.containerID);
/*     */       } 
/*     */     } 
/* 315 */     if (this.containerID != null && !getAutoRootContainerID().equals(oldRootContainerID)) {
/* 316 */       this.customContainerID = null;
/* 317 */       this.customWorldID = null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void updateWaypoints() {
/* 322 */     if (this.containerID != null && this.worldID != null) {
/* 323 */       addWorld(this.containerID, this.worldID);
/* 324 */       WaypointWorld world = getCurrentWorld();
/* 325 */       this.waypoints = world.getCurrentSet();
/* 326 */       if (!world.getServerWaypoints().isEmpty()) {
/* 327 */         this.serverWaypoints = new ArrayList<>(world.getServerWaypoints().values());
/*     */       } else {
/* 329 */         this.serverWaypoints = null;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void createDeathpoint(EntityPlayer p) {
/* 335 */     boolean disabled = false;
/* 336 */     if (this.waypoints == null)
/*     */       return; 
/* 338 */     List<Waypoint> list = this.waypoints.getList();
/* 339 */     for (int i = 0; i < list.size(); i++) {
/* 340 */       Waypoint w = list.get(i);
/* 341 */       if (w.getType() == 1) {
/* 342 */         disabled = w.isDisabled();
/* 343 */         if (!this.modMain.getSettings().getDeathpoints() || !this.modMain.getSettings().getOldDeathpoints()) {
/* 344 */           list.remove(w);
/*     */           break;
/*     */         } 
/* 347 */         w.setType(0);
/* 348 */         w.setName("gui.xaero_deathpoint_old");
/*     */         break;
/*     */       } 
/*     */     } 
/* 352 */     boolean divideBy8 = divideBy8(getCurrentContainerID());
/* 353 */     if (this.modMain.getSettings().getDeathpoints()) {
/*     */       
/* 355 */       Waypoint deathpoint = new Waypoint(OptimizedMath.myFloor(p.field_70165_t) * (divideBy8 ? 8 : 1), OptimizedMath.myFloor(p.field_70163_u), OptimizedMath.myFloor(p.field_70161_v) * (divideBy8 ? 8 : 1), "gui.xaero_deathpoint", "D", 0, 1);
/* 356 */       deathpoint.setDisabled(disabled);
/* 357 */       list.add(0, deathpoint);
/*     */     } 
/*     */     try {
/* 360 */       this.modMain.getSettings().saveWaypoints(getCurrentWorld());
/* 361 */     } catch (IOException e) {
/* 362 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void createTemporaryWaypoints(WaypointWorld waypointWorld, int x, int y, int z) {
/* 367 */     if (this.modMain.getSettings().waypointsGUI() && waypointWorld != null) {
/* 368 */       boolean divideBy8 = divideBy8(getCurrentContainerID());
/* 369 */       x *= divideBy8 ? 8 : 1;
/* 370 */       z *= divideBy8 ? 8 : 1;
/* 371 */       Waypoint instant = new Waypoint(x, y, z, "Waypoint", "X", (int)(Math.random() * ModSettings.ENCHANT_COLORS.length), 0, true);
/* 372 */       waypointWorld.getCurrentSet().getList().add(0, instant);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean canTeleport(boolean displayingTeleportableWorld, WaypointWorld displayedWorld) {
/* 377 */     return (((this.modMain.getSettings()).allowWrongWorldTeleportation || displayingTeleportableWorld) && displayedWorld.getContainer().getRootContainer().isTeleportationEnabled());
/*     */   }
/*     */   
/*     */   public void teleportAnyway() {
/* 381 */     if (this.teleportAnywayWP != null) {
/* 382 */       GuiScreen dummyScreen = new GuiScreen() {  };
/* 383 */       Minecraft minecraft = Minecraft.func_71410_x();
/* 384 */       ScaledResolution scaledresolution = new ScaledResolution(minecraft);
/* 385 */       int i = scaledresolution.func_78326_a();
/* 386 */       int j = scaledresolution.func_78328_b();
/* 387 */       dummyScreen.func_146280_a(minecraft, i, j);
/* 388 */       teleportToWaypoint(this.teleportAnywayWP, this.teleportAnywayWorld, dummyScreen, false);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void teleportToWaypoint(Waypoint selected, WaypointWorld displayedWorld, GuiScreen screen) {
/* 393 */     teleportToWaypoint(selected, displayedWorld, screen, true);
/*     */   }
/*     */   
/*     */   public void teleportToWaypoint(Waypoint selected, WaypointWorld displayedWorld, GuiScreen screen, boolean respectHiddenCoords) {
/* 397 */     boolean displayingTeleportableWorld = isWorldTeleportable(displayedWorld);
/* 398 */     if (selected != null && canTeleport(displayingTeleportableWorld, displayedWorld)) {
/* 399 */       if (respectHiddenCoords && (this.modMain.getSettings()).hideWaypointCoordinates && this.mc.field_71474_y.field_74343_n != EntityPlayer.EnumChatVisibility.HIDDEN) {
/* 400 */         this.mc.field_71456_v.func_146158_b().func_146227_a((ITextComponent)new TextComponentString("§b" + I18n.func_135052_a("gui.xaero_teleport_coordinates_hidden", new Object[0])));
/* 401 */         TextComponentString clickableQuestion = new TextComponentString("§e[" + I18n.func_135052_a("gui.xaero_teleport_anyway", new Object[0]) + "]");
/* 402 */         clickableQuestion.func_150256_b().func_150241_a(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/xaero_tp_anyway"))
/* 403 */           .func_150209_a(new HoverEvent(HoverEvent.Action.SHOW_TEXT, (ITextComponent)new TextComponentString("§c" + I18n.func_135052_a("gui.xaero_teleport_shows_coordinates", new Object[0]))));
/* 404 */         this.teleportAnywayWP = selected;
/* 405 */         this.teleportAnywayWorld = displayedWorld;
/* 406 */         this.mc.field_71456_v.func_146158_b().func_146227_a((ITextComponent)clickableQuestion);
/*     */       } else {
/* 408 */         boolean divideBy8 = divideBy8(displayedWorld.getContainer().getKey());
/* 409 */         int x = Math.floorDiv(selected.getX(), divideBy8 ? 8 : 1);
/* 410 */         int z = Math.floorDiv(selected.getZ(), divideBy8 ? 8 : 1);
/* 411 */         WaypointWorldRootContainer rootContainer = displayedWorld.getContainer().getRootContainer();
/* 412 */         String tpCommandPrefix = (rootContainer.isUsingDefaultTeleportCommand() || rootContainer.getTeleportCommand() == null) ? (this.modMain.getSettings()).waypointTp : rootContainer.getTeleportCommand();
/* 413 */         if (!selected.isRotation()) {
/* 414 */           screen.func_175281_b("/" + tpCommandPrefix + " " + x + " " + selected.getY() + " " + z, false);
/*     */         } else {
/* 416 */           screen.func_175281_b("/" + tpCommandPrefix + " " + x + " " + selected.getY() + " " + z + " " + selected.getYaw() + " ~", false);
/*     */         } 
/*     */       } 
/* 419 */       this.mc.func_147108_a(null);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isWorldTeleportable(WaypointWorld displayedWorld) {
/* 424 */     return (displayedWorld.getContainer().getRootContainer().getKey().equals(getAutoRootContainerID()) && displayedWorld == getAutoWorld());
/*     */   }
/*     */   
/*     */   public WaypointSet getWaypoints() {
/* 428 */     return this.waypoints;
/*     */   }
/*     */   
/*     */   public void setWaypoints(WaypointSet waypoints) {
/* 432 */     this.waypoints = waypoints;
/*     */   }
/*     */   
/*     */   public List<Waypoint> getServerWaypoints() {
/* 436 */     return this.serverWaypoints;
/*     */   }
/*     */   
/*     */   public HashMap<String, WaypointWorldContainer> getWaypointMap() {
/* 440 */     return this.waypointMap;
/*     */   }
/*     */   
/*     */   public RealmsServer getLatestRealm() {
/* 444 */     return this.latestRealm;
/*     */   }
/*     */   
/*     */   public void setLatestRealm(RealmsServer latestRealm) {
/* 448 */     this.latestRealm = latestRealm;
/*     */   }
/*     */   
/*     */   public void setCurrentSpawn(BlockPos currentSpawn) {
/* 452 */     String actualCurrentContainer = getContainer((World)this.mc.field_71441_e);
/* 453 */     this.currentSpawnRootContainerId = actualCurrentContainer.contains("/") ? actualCurrentContainer.split("/")[0] : actualCurrentContainer;
/* 454 */     this.currentSpawn = currentSpawn;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getCustomContainerID() {
/* 459 */     return this.customContainerID;
/*     */   }
/*     */   
/*     */   public void setCustomContainerID(String customContainerID) {
/* 463 */     this.customContainerID = customContainerID;
/*     */   }
/*     */   
/*     */   public String getCustomWorldID() {
/* 467 */     return this.customWorldID;
/*     */   }
/*     */   
/*     */   public void setCustomWorldID(String customWorldID) {
/* 471 */     this.customWorldID = customWorldID;
/*     */   }
/*     */   
/*     */   public static Hashtable<Integer, Waypoint> getCustomWaypoints(String modName) {
/* 475 */     Hashtable<Integer, Waypoint> wps = customWaypoints.get(modName);
/* 476 */     if (wps == null)
/* 477 */       customWaypoints.put(modName, wps = new Hashtable<>()); 
/* 478 */     return wps;
/*     */   }
/*     */ }


/* Location:              C:\Users\ioanm\Downloads\Xaeros_Minimap_20.15.3_Forge_1.12.jar!\xaero\common\minimap\waypoints\WaypointsManager.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */