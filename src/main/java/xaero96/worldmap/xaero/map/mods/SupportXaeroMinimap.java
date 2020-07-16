/*     */ package xaero.map.mods;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.regex.Pattern;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.GuiScreen;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ import xaero.common.IXaeroMinimap;
/*     */ import xaero.common.api.spigot.ServerWaypointStorage;
/*     */ import xaero.common.gui.GuiAddWaypoint;
/*     */ import xaero.common.minimap.waypoints.Waypoint;
/*     */ import xaero.common.minimap.waypoints.WaypointSet;
/*     */ import xaero.common.minimap.waypoints.WaypointWorld;
/*     */ import xaero.common.minimap.waypoints.WaypointWorldContainer;
/*     */ import xaero.common.minimap.waypoints.WaypointsManager;
/*     */ import xaero.common.settings.ModSettings;
/*     */ import xaero.map.WorldMap;
/*     */ import xaero.map.mods.gui.Waypoint;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SupportXaeroMinimap
/*     */ {
/*     */   IXaeroMinimap modMain;
/*     */   private WaypointsManager waypointsManager;
/*     */   private boolean deathpoints = true;
/*     */   private boolean refreshWaypoints = true;
/*     */   private WaypointWorld waypointWorld;
/*     */   private WaypointSet waypointSet;
/*     */   private ArrayList<Waypoint> waypoints;
/*     */   
/*     */   public SupportXaeroMinimap() {
/*     */     try {
/*  36 */       this.modMain = SupportBetterPVP.getMain();
/*  37 */       System.out.println("Xaero's WorldMap Mod: Better PVP found!");
/*  38 */     } catch (NoClassDefFoundError e) {
/*     */       try {
/*  40 */         this.modMain = SupportMinimap.getMain();
/*  41 */         System.out.println("Xaero's WorldMap Mod: Xaero's minimap found!");
/*  42 */       } catch (NoClassDefFoundError noClassDefFoundError) {}
/*     */     } 
/*     */     
/*  45 */     if (this.modMain != null) {
/*  46 */       this.waypointsManager = this.modMain.getWaypointsManager();
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean waypointIsGood(Waypoint w) {
/*  51 */     return (w != null && !w.isDisabled() && (w.getType() != 1 || this.deathpoints));
/*     */   }
/*     */   
/*     */   public ArrayList<Waypoint> convertWaypoints() {
/*  55 */     if (this.waypointSet == null)
/*  56 */       return null; 
/*  57 */     ArrayList<Waypoint> result = new ArrayList<>();
/*  58 */     ArrayList<Waypoint> list = this.waypointSet.getList();
/*  59 */     for (int i = 0; i < list.size(); i++) {
/*  60 */       Waypoint w = list.get(i);
/*  61 */       result.add(convertWaypoint(w, true));
/*     */     } 
/*  63 */     if (ServerWaypointStorage.working() && this.waypointsManager.getServerWaypoints() != null)
/*  64 */       for (Waypoint w : this.waypointsManager.getServerWaypoints())
/*  65 */         result.add(convertWaypoint(w, false));  
/*  66 */     this.deathpoints = this.modMain.getSettings().getDeathpoints();
/*  67 */     return result;
/*     */   }
/*     */   
/*     */   public Waypoint convertWaypoint(Waypoint w, boolean editable) {
/*  71 */     Waypoint converted = new Waypoint(w, w.getX(), w.getY(), w.getZ(), w.getName(), w.getSymbol(), ModSettings.COLORS[w.getColor()], w.getType(), editable);
/*  72 */     converted.setDisabled(w.isDisabled());
/*  73 */     converted.setYaw(w.getYaw());
/*  74 */     converted.setRotation(w.isRotation());
/*  75 */     return converted;
/*     */   }
/*     */   
/*     */   public void openWaypoint(GuiScreen parent, Waypoint waypoint) {
/*  79 */     if (!waypoint.isEditable())
/*     */       return; 
/*  81 */     Minecraft.func_71410_x().func_147108_a((GuiScreen)new GuiAddWaypoint(this.modMain, parent, (Waypoint)waypoint.getOriginal(), this.waypointWorld.getContainer().getRootContainer().getKey(), this.waypointWorld));
/*     */   }
/*     */   
/*     */   public void createWaypoint(GuiScreen parent, int x, int y, int z) {
/*  85 */     if (this.waypointWorld == null)
/*  86 */       return;  boolean divideBy8 = this.waypointsManager.divideBy8(this.waypointWorld.getContainer().getKey());
/*  87 */     Waypoint w = new Waypoint(x * (divideBy8 ? 8 : 1), y, z * (divideBy8 ? 8 : 1), "", "", -1);
/*  88 */     Minecraft.func_71410_x().func_147108_a((GuiScreen)new GuiAddWaypoint(this.modMain, parent, w, this.waypointWorld.getContainer().getRootContainer().getKey(), this.waypointWorld));
/*     */   }
/*     */   
/*     */   public void createTempWaypoint(int x, int y, int z) {
/*  92 */     if (this.waypointWorld == null)
/*  93 */       return;  this.waypointsManager.createTemporaryWaypoints(this.waypointWorld, x, y, z);
/*  94 */     requestWaypointsRefresh();
/*     */   }
/*     */   
/*     */   public boolean canTeleport() {
/*  98 */     return (this.waypointWorld != null && this.waypointsManager.canTeleport(this.waypointsManager.isWorldTeleportable(this.waypointWorld), this.waypointWorld));
/*     */   }
/*     */   
/*     */   public void teleportToWaypoint(GuiScreen screen, Waypoint w) {
/* 102 */     this.waypointsManager.teleportToWaypoint((Waypoint)w.getOriginal(), this.waypointWorld, screen);
/*     */   }
/*     */   
/*     */   public void disableWaypoint(Waypoint waypoint) {
/* 106 */     ((Waypoint)waypoint.getOriginal()).setDisabled(!((Waypoint)waypoint.getOriginal()).isDisabled());
/*     */     try {
/* 108 */       this.modMain.getSettings().saveWaypoints(this.waypointWorld);
/* 109 */     } catch (IOException e) {
/* 110 */       e.printStackTrace();
/*     */     } 
/* 112 */     waypoint.setDisabled(!waypoint.isDisabled());
/*     */   }
/*     */   
/*     */   public void deleteWaypoint(Waypoint waypoint) {
/* 116 */     this.waypointSet.getList().remove(waypoint.getOriginal());
/*     */     try {
/* 118 */       this.modMain.getSettings().saveWaypoints(this.waypointWorld);
/* 119 */     } catch (IOException e) {
/* 120 */       e.printStackTrace();
/*     */     } 
/* 122 */     this.waypoints.remove(waypoint);
/*     */   }
/*     */   
/*     */   public boolean isOnScreen(Waypoint w, double cameraX, double cameraZ, int width, int height, double scale) {
/* 126 */     double xOnScreen = (w.getX() - cameraX) * scale + (width / 2);
/* 127 */     double zOnScreen = (w.getZ() - cameraZ) * scale + (height / 2);
/* 128 */     return (xOnScreen < width && xOnScreen > 0.0D && zOnScreen < height && zOnScreen > 0.0D);
/*     */   }
/*     */   
/*     */   public void checkWaypoints(boolean multiplayer, int dimId, String multiworldId) {
/* 132 */     String containerId = this.waypointsManager.getAutoRootContainerID() + "/" + this.waypointsManager.getDimensionDirectoryName(dimId);
/* 133 */     WaypointWorldContainer container = this.waypointsManager.getWorldContainerNullable(containerId);
/* 134 */     WaypointWorld checkingWaypointWorld = (container == null) ? null : (WaypointWorld)container.worlds.get(!multiplayer ? "waypoints" : multiworldId);
/* 135 */     boolean shouldRefresh = this.refreshWaypoints;
/* 136 */     if (checkingWaypointWorld != this.waypointWorld) {
/* 137 */       this.waypointWorld = checkingWaypointWorld;
/* 138 */       shouldRefresh = true;
/*     */     } 
/* 140 */     WaypointSet checkingSet = (checkingWaypointWorld == null) ? null : checkingWaypointWorld.getCurrentSet();
/* 141 */     if (checkingSet != this.waypointSet) {
/* 142 */       this.waypointSet = checkingSet;
/* 143 */       shouldRefresh = true;
/*     */     } 
/* 145 */     if (shouldRefresh) {
/* 146 */       this.waypoints = convertWaypoints();
/* 147 */       if (this.waypoints != null)
/* 148 */         Collections.sort(this.waypoints); 
/*     */     } 
/* 150 */     this.refreshWaypoints = false;
/*     */   }
/*     */ 
/*     */   
/*     */   public ArrayList<Waypoint> renderWaypoints(GuiScreen gui, double cameraX, double cameraZ, int width, int height, double guiScale, double scale, double mouseX, double mouseZ, Pattern regex, Pattern regexStartsWith) {
/* 155 */     if (this.waypoints == null)
/* 156 */       return null; 
/* 157 */     Waypoint viewed = null;
/* 158 */     Minecraft.func_71410_x().func_110434_K().func_110577_a(WorldMap.guiTextures);
/* 159 */     GL11.glTexParameteri(3553, 10241, 9729);
/* 160 */     GL11.glTexParameteri(3553, 10240, 9729);
/* 161 */     for (int i = this.waypoints.size() - 1; i >= 0; i--) {
/* 162 */       Waypoint w = this.waypoints.get(i);
/* 163 */       if (waypointIsGood(w) && w.getZ() > mouseZ && w.getZ() - 41.0D / scale < mouseZ && Math.abs(w.getX() - mouseX) <= ((w.getSymbol().length() > 1) ? 20.5F : 14.0F) / scale) {
/* 164 */         viewed = w;
/*     */         break;
/*     */       } 
/*     */     } 
/* 168 */     ArrayList<Waypoint> outsideWaypoints = new ArrayList<>();
/* 169 */     for (Waypoint w : this.waypoints) {
/* 170 */       if (waypointIsGood(w)) {
/* 171 */         if (isOnScreen(w, cameraX, cameraZ, width, height, scale)) {
/* 172 */           w.renderShadow(gui, guiScale, scale, w.getX(), w.getZ()); continue;
/*     */         } 
/* 174 */         if (regex == null) {
/* 175 */           outsideWaypoints.add(w); continue;
/* 176 */         }  if (regexStartsWith.matcher(w.getName().toLowerCase()).find()) {
/* 177 */           outsideWaypoints.add(0, w); continue;
/* 178 */         }  if (regex.matcher(w.getName().toLowerCase()).find())
/* 179 */           outsideWaypoints.add(w); 
/*     */       } 
/*     */     } 
/* 182 */     for (Waypoint w : this.waypoints) {
/* 183 */       if (waypointIsGood(w) && 
/* 184 */         w != viewed && isOnScreen(w, cameraX, cameraZ, width, height, scale))
/* 185 */         w.renderWaypoint(gui, guiScale, scale, w.getX(), w.getZ(), false); 
/*     */     } 
/* 187 */     if (viewed != null)
/* 188 */       viewed.renderWaypoint(gui, guiScale, scale, viewed.getX(), viewed.getZ(), true); 
/* 189 */     outsideWaypoints.add(0, viewed);
/* 190 */     Minecraft.func_71410_x().func_110434_K().func_110577_a(WorldMap.guiTextures);
/* 191 */     GL11.glTexParameteri(3553, 10241, 9728);
/* 192 */     GL11.glTexParameteri(3553, 10240, 9728);
/* 193 */     return outsideWaypoints;
/*     */   }
/*     */   
/*     */   public void renderSideWaypoints(GuiScreen gui, ArrayList<Waypoint> sideWaypoints, double scale, int width, int height, int offset, int selected) {
/* 197 */     int yPos = height - 2;
/* 198 */     if (offset > 0)
/* 199 */       yPos = drawThreeDots(yPos, width); 
/* 200 */     for (int i = offset; i < sideWaypoints.size(); i++) {
/* 201 */       float wpScale = (i == selected) ? 1.5F : 1.0F;
/* 202 */       yPos = (int)(yPos - 5.0F * wpScale);
/* 203 */       ((Waypoint)sideWaypoints.get(i)).renderSideWaypoint(gui, (width - 2) - 5.0F * wpScale, yPos, wpScale);
/* 204 */       yPos = (int)(yPos - 1.0F + 4.0F * wpScale);
/*     */       
/* 206 */       if (i + 1 < sideWaypoints.size() && i - offset == 6) {
/* 207 */         yPos = drawThreeDots(yPos, width);
/*     */         break;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private int drawThreeDots(int yPos, int width) {
/* 214 */     yPos -= 9;
/* 215 */     (Minecraft.func_71410_x()).field_71466_p.func_175065_a("...", (width - 7 - (Minecraft.func_71410_x()).field_71466_p.func_78256_a("...") / 2), (yPos - 4), Waypoint.white, true);
/* 216 */     yPos -= 5;
/* 217 */     return yPos;
/*     */   }
/*     */   
/*     */   public void requestWaypointsRefresh() {
/* 221 */     this.refreshWaypoints = true;
/*     */   }
/*     */   
/*     */   public int getWaypointKeyCode() {
/* 225 */     return ModSettings.newWaypoint.func_151463_i();
/*     */   }
/*     */   
/*     */   public int getTempWaypointKeyCode() {
/* 229 */     return ModSettings.keyInstantWaypoint.func_151463_i();
/*     */   }
/*     */ }


/* Location:              C:\Users\ioanm\Downloads\XaerosWorldMap_1.7.3_Forge_1.12.jar!\xaero\map\mods\SupportXaeroMinimap.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */