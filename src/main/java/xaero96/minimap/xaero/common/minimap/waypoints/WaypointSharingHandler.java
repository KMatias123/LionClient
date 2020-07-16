/*     */ package xaero.common.minimap.waypoints;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.GuiScreen;
/*     */ import net.minecraft.client.gui.GuiYesNo;
/*     */ import net.minecraft.client.gui.GuiYesNoCallback;
/*     */ import net.minecraft.client.resources.I18n;
/*     */ import net.minecraft.util.text.ITextComponent;
/*     */ import net.minecraft.util.text.TextComponentString;
/*     */ import net.minecraft.util.text.TextFormatting;
/*     */ import net.minecraft.util.text.event.ClickEvent;
/*     */ import net.minecraft.util.text.event.HoverEvent;
/*     */ import net.minecraft.world.DimensionType;
/*     */ import net.minecraftforge.client.event.ClientChatReceivedEvent;
/*     */ import net.minecraftforge.common.DimensionManager;
/*     */ import xaero.common.IXaeroMinimap;
/*     */ import xaero.common.gui.GuiAddWaypoint;
/*     */ 
/*     */ 
/*     */ public class WaypointSharingHandler
/*     */   implements GuiYesNoCallback
/*     */ {
/*     */   public static final String WAYPOINT_SHARE_PREFIX = "xaero_waypoint:";
/*     */   public static final String WAYPOINT_ADD_PREFIX = "xaero_waypoint_add:";
/*     */   private IXaeroMinimap modMain;
/*     */   private GuiScreen parent;
/*     */   private Waypoint w;
/*     */   private WaypointWorld wWorld;
/*     */   
/*     */   public WaypointSharingHandler(IXaeroMinimap modMain) {
/*  34 */     this.modMain = modMain;
/*     */   }
/*     */   
/*     */   public void shareWaypoint(GuiScreen parent, Waypoint w, WaypointWorld wWorld) {
/*  38 */     this.parent = parent;
/*  39 */     this.w = w;
/*  40 */     this.wWorld = wWorld;
/*  41 */     Minecraft.func_71410_x().func_147108_a((GuiScreen)new GuiYesNo(this, I18n.func_135052_a("gui.xaero_share_msg1", new Object[0]), I18n.func_135052_a("gui.xaero_share_msg2", new Object[0]), 0));
/*     */   }
/*     */   
/*     */   public void onWaypointReceived(String text, ClientChatReceivedEvent e) {
/*  45 */     text = text.replaceAll("§.", "");
/*  46 */     String futureFormatPrefix = toNewFormat("xaero_waypoint:");
/*  47 */     boolean futureFormat = text.contains(futureFormatPrefix);
/*  48 */     String sharePrefix = futureFormat ? futureFormatPrefix : "xaero_waypoint:";
/*  49 */     String[] args = text.substring(text.indexOf(sharePrefix)).split(":");
/*  50 */     if (futureFormat) {
/*  51 */       args[1] = toOldFormat(args[1]);
/*  52 */       args[2] = toOldFormat(args[2]);
/*  53 */       if (args.length > 9)
/*  54 */         args[9] = toOldFormat(args[9]); 
/*     */     } 
/*  56 */     TextComponentString component = null;
/*  57 */     if (args.length < 9) {
/*  58 */       System.out.println("Incorrect format of the shared waypoint! Error: 0");
/*     */     } else {
/*  60 */       String playerName = text.substring(0, text.indexOf(sharePrefix));
/*  61 */       int lastGreater = playerName.lastIndexOf(">");
/*  62 */       if (lastGreater != -1)
/*  63 */         playerName = playerName.substring(0, lastGreater).replaceFirst("<", ""); 
/*  64 */       String newText = playerName + " shared a waypoint called \"" + I18n.func_135052_a(Waypoint.getStringFromStringSafe(args[1], "^col^"), new Object[0]) + "\"";
/*  65 */       if (args.length > 9 && args[9].startsWith("Internal_")) {
/*     */         try {
/*  67 */           String details = args[9].substring(9, args[9].lastIndexOf("_")).replace("^col^", ":");
/*  68 */           newText = newText + " from ";
/*  69 */           if (details.startsWith("dim%"))
/*  70 */           { if (details.length() == 4) {
/*  71 */               newText = newText + "an unknown dimension";
/*     */             } else {
/*  73 */               Integer dimId = this.modMain.getWaypointsManager().getDimensionForDirectoryName(details);
/*  74 */               if (dimId == null) {
/*  75 */                 newText = newText + "an unknown dimension";
/*     */               } else {
/*  77 */                 DimensionType dt = null;
/*     */                 try {
/*  79 */                   dt = DimensionManager.getProviderType(dimId.intValue());
/*  80 */                 } catch (IllegalArgumentException illegalArgumentException) {}
/*  81 */                 if (dt == null) {
/*  82 */                   newText = newText + "an unknown dimension";
/*     */                 } else {
/*  84 */                   newText = newText + dt.func_186065_b();
/*     */                 } 
/*     */               } 
/*     */             }  }
/*  88 */           else { newText = newText + details; } 
/*  89 */         } catch (IndexOutOfBoundsException indexOutOfBoundsException) {}
/*     */       }
/*     */       
/*  92 */       newText = newText + "! §2§n[Add]";
/*  93 */       newText = newText.replaceAll("§r", "§r§7").replaceAll("§f", "§7");
/*  94 */       component = new TextComponentString(newText);
/*  95 */       TextComponentString hoverComponent = new TextComponentString(args[3] + ", " + args[4] + ", " + args[5]);
/*  96 */       StringBuilder addCommandBuilder = new StringBuilder();
/*  97 */       addCommandBuilder.append("xaero_waypoint_add:");
/*  98 */       addCommandBuilder.append(args[1]);
/*  99 */       for (int i = 2; i < args.length; i++)
/* 100 */         addCommandBuilder.append(':').append(args[i]); 
/* 101 */       String addCommand = addCommandBuilder.toString();
/* 102 */       component.func_150256_b().func_150238_a(TextFormatting.GRAY).func_150241_a(new ClickEvent(ClickEvent.Action.RUN_COMMAND, addCommand))
/*     */         
/* 104 */         .func_150209_a(new HoverEvent(HoverEvent.Action.SHOW_TEXT, (ITextComponent)hoverComponent));
/*     */     } 
/*     */ 
/*     */     
/* 108 */     e.setMessage((ITextComponent)component);
/*     */   }
/*     */   
/*     */   public void onWaypointAdd(String[] args) {
/* 112 */     String waypointName = Waypoint.getStringFromStringSafe(args[1], "^col^");
/* 113 */     if (waypointName.length() < 1 || waypointName.length() > 32) {
/* 114 */       System.out.println("Incorrect format of the shared waypoint! Error: 1");
/*     */       return;
/*     */     } 
/* 117 */     String waypointSymbol = Waypoint.getStringFromStringSafe(args[2], "^col^");
/* 118 */     if (waypointSymbol.length() < 1 || waypointSymbol.length() > 3) {
/* 119 */       System.out.println("Incorrect format of the shared waypoint! Error: 2");
/*     */       return;
/*     */     } 
/*     */     try {
/* 123 */       if (this.modMain.getWaypointsManager().getAutoContainerID() == null) {
/* 124 */         System.out.println("Can't add a waypoint at this time!");
/*     */         return;
/*     */       } 
/* 127 */       int x = Integer.parseInt(args[3]);
/* 128 */       int y = Integer.parseInt(args[4]);
/* 129 */       int z = Integer.parseInt(args[5]);
/* 130 */       int color = Integer.parseInt(args[6]);
/* 131 */       String yawString = args[8];
/* 132 */       if (yawString.length() > 4) {
/* 133 */         System.out.println("Incorrect format of the shared waypoint! Error: 4");
/*     */         return;
/*     */       } 
/* 136 */       int yaw = Integer.parseInt(yawString);
/* 137 */       boolean rotation = args[7].equals("true");
/* 138 */       Waypoint w = new Waypoint(x, y, z, waypointName, waypointSymbol, color, 0);
/* 139 */       w.setRotation(rotation);
/* 140 */       w.setYaw(yaw);
/* 141 */       String externalContainerId = this.modMain.getWaypointsManager().getCurrentContainerID().split("/")[0];
/* 142 */       WaypointWorld externalWorld = this.modMain.getWaypointsManager().getCurrentWorld();
/* 143 */       String parentContainerId = externalContainerId;
/* 144 */       WaypointWorld currentWorld = externalWorld;
/* 145 */       if (args.length > 9) {
/* 146 */         String worldDetails = args[9];
/* 147 */         if (worldDetails.length() > 9 && worldDetails.startsWith("Internal_")) {
/* 148 */           String subContainers; int divider = worldDetails.lastIndexOf('_');
/* 149 */           if (divider < 1 || divider == worldDetails.length() - 1) {
/* 150 */             System.out.println("Incorrect format of the shared waypoint! Error: 5");
/*     */             return;
/*     */           } 
/* 153 */           String worldId = worldDetails.substring(divider + 1);
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 158 */           if (!worldId.replaceAll("[^a-zA-Z0-9,-]+", "").equals(worldId)) {
/* 159 */             System.out.println("Incorrect format of the shared waypoint! Error: 7");
/*     */             
/*     */             return;
/*     */           } 
/* 163 */           boolean destinationDimensionExists = true;
/*     */           try {
/* 165 */             subContainers = worldDetails.substring(9, divider);
/* 166 */           } catch (IndexOutOfBoundsException eoobe) {
/* 167 */             subContainers = null;
/*     */           } 
/* 169 */           parentContainerId = this.modMain.getWaypointsManager().getAutoRootContainerID();
/* 170 */           String containerId = null;
/* 171 */           if (subContainers != null) {
/* 172 */             subContainers = subContainers.replace("^col^", ":");
/* 173 */             String[] subContainersArgs = subContainers.split("/");
/* 174 */             if (subContainersArgs.length > 1) {
/*     */ 
/*     */               
/* 177 */               System.out.println("Incorrect format of the shared waypoint! Error: 8");
/*     */               return;
/*     */             } 
/* 180 */             for (int i = 0; i < subContainersArgs.length; i++) {
/* 181 */               String s = subContainersArgs[i];
/* 182 */               if (s.isEmpty()) {
/* 183 */                 System.out.println("Incorrect format of the shared waypoint! Error: 11");
/*     */                 
/*     */                 return;
/*     */               } 
/*     */             } 
/* 188 */             String dimContainer = subContainersArgs[0];
/* 189 */             Integer dimId = null;
/* 190 */             if (!dimContainer.startsWith("dim%")) {
/* 191 */               if (!dimContainer.replaceAll("[^a-zA-Z0-9_]+", "").equals(dimContainer)) {
/* 192 */                 System.out.println("Incorrect format of the shared waypoint! Error: 18");
/*     */                 return;
/*     */               } 
/* 195 */               DimensionType dt = this.modMain.getWaypointsManager().findDimensionType(dimContainer);
/* 196 */               if (dt != null) {
/* 197 */                 int[] dims = DimensionManager.getDimensions(dt);
/* 198 */                 if (dims.length > 0)
/* 199 */                   dimId = Integer.valueOf(dims[0]); 
/*     */               } 
/*     */             } else {
/* 202 */               dimId = this.modMain.getWaypointsManager().getDimensionForDirectoryName(dimContainer);
/* 203 */             }  if (dimId == null) {
/* 204 */               System.out.println("Destination dimension doesn't exists! Handling waypoint as external.");
/* 205 */               parentContainerId = externalContainerId;
/* 206 */               currentWorld = externalWorld;
/* 207 */               destinationDimensionExists = false;
/*     */             } else {
/* 209 */               subContainersArgs[0] = this.modMain.getWaypointsManager().getDimensionDirectoryName(dimId.intValue());
/* 210 */               subContainers = String.join("/", (CharSequence[])subContainersArgs);
/* 211 */               containerId = parentContainerId + "/" + subContainers;
/* 212 */               WaypointWorldContainer rootContainer = this.modMain.getWaypointsManager().getWorldContainer(parentContainerId);
/* 213 */               rootContainer.renameOldContainer(containerId);
/*     */             } 
/*     */           } else {
/* 216 */             containerId = parentContainerId;
/* 217 */           }  if (destinationDimensionExists) {
/*     */             
/* 219 */             String autoWorldId = this.modMain.getWaypointsManager().getAutoWorldID();
/* 220 */             if (worldId.equals("waypoints")) {
/* 221 */               worldId = autoWorldId;
/* 222 */             } else if (autoWorldId.equals("waypoints")) {
/* 223 */               worldId = "waypoints";
/*     */             } 
/*     */             
/* 226 */             File securityTest = new File(this.modMain.getWaypointsFolder(), containerId + "/" + worldId + (worldId.equals("waypoints") ? "" : "_1") + ".txt");
/*     */             
/*     */             try {
/* 229 */               if (!securityTest.getPath().equals(securityTest.getCanonicalPath())) {
/* 230 */                 System.out.println("Dangerously incorrect format of the shared waypoint! Error: 10");
/*     */                 return;
/*     */               } 
/* 233 */             } catch (IOException e) {
/* 234 */               System.out.println("IO error adding a shared waypoint!");
/*     */               
/*     */               return;
/*     */             } 
/* 238 */             currentWorld = this.modMain.getWaypointsManager().getWorld(containerId, worldId);
/*     */           } 
/* 240 */         } else if (!worldDetails.equals("External")) {
/* 241 */           System.out.println("Incorrect format of the shared waypoint! Error: 12");
/*     */           return;
/*     */         } 
/*     */       } 
/* 245 */       Minecraft.func_71410_x().func_147108_a((GuiScreen)new GuiAddWaypoint(this.modMain, null, Lists.newArrayList((Object[])new Waypoint[] { w }, ), parentContainerId, currentWorld, true));
/* 246 */     } catch (NumberFormatException nfe) {
/* 247 */       System.out.println("Incorrect format of the shared waypoint! Error: 3");
/*     */       return;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void func_73878_a(boolean p_confirmResult_1_, int p_confirmResult_2_) {
/* 254 */     switch (p_confirmResult_2_) {
/*     */       case 0:
/* 256 */         if (p_confirmResult_1_) {
/*     */           String worldDetails;
/* 258 */           WaypointWorldContainer rootContainer = this.wWorld.getContainer().getRootContainer();
/* 259 */           WaypointWorldContainer autoRootContainer = this.modMain.getWaypointsManager().getAutoWorld().getContainer().getRootContainer();
/* 260 */           if (rootContainer == autoRootContainer) {
/* 261 */             String details, containerId = this.wWorld.getContainer().getKey();
/* 262 */             int firstSlashIndex = containerId.indexOf("/");
/*     */             
/* 264 */             if (firstSlashIndex != -1) {
/* 265 */               String subContainers = containerId.substring(firstSlashIndex + 1);
/* 266 */               String[] subContainersSplit = subContainers.split("/");
/*     */               
/* 268 */               if (subContainersSplit[0].equals("dim%0")) {
/* 269 */                 subContainersSplit[0] = "overworld";
/* 270 */               } else if (subContainersSplit[0].equals("dim%-1")) {
/* 271 */                 subContainersSplit[0] = "the_nether";
/* 272 */               } else if (subContainersSplit[0].equals("dim%1")) {
/* 273 */                 subContainersSplit[0] = "the_end";
/* 274 */               }  subContainers = String.join("/", (CharSequence[])subContainersSplit);
/* 275 */               details = subContainers.replace(":", "^col^") + "_" + this.wWorld.getId();
/*     */             } else {
/* 277 */               details = this.wWorld.getId();
/* 278 */             }  worldDetails = "Internal_" + details;
/*     */           } else {
/* 280 */             worldDetails = "External";
/* 281 */           }  String message = "xaero_waypoint:" + this.w.getNameSafe("^col^") + ":" + this.w.getSymbolSafe("^col^") + ":" + this.w.getX() + ":" + this.w.getY() + ":" + this.w.getZ() + ":" + this.w.getColor() + ":" + this.w.isRotation() + ":" + this.w.getYaw() + ":" + worldDetails;
/* 282 */           (Minecraft.func_71410_x()).field_71462_r.func_175281_b(message, true);
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 287 */           Minecraft.func_71410_x().func_147108_a(null); return;
/*     */         } 
/*     */         break;
/*     */     } 
/* 291 */     Minecraft.func_71410_x().func_147108_a(this.parent);
/*     */   }
/*     */   
/*     */   private String toNewFormat(String old) {
/* 295 */     return old.replace("-", "^min^").replace("_", "-");
/*     */   }
/*     */   
/*     */   private String toOldFormat(String s) {
/* 299 */     return s.replace("-", "_").replace("^min^", "-");
/*     */   }
/*     */ }


/* Location:              C:\Users\ioanm\Downloads\Xaeros_Minimap_20.15.3_Forge_1.12.jar!\xaero\common\minimap\waypoints\WaypointSharingHandler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */