/*     */ package xaero.common.gui;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.concurrent.ConcurrentSkipListSet;
/*     */ import net.minecraft.client.gui.FontRenderer;
/*     */ import net.minecraft.client.gui.GuiButton;
/*     */ import net.minecraft.client.gui.GuiScreen;
/*     */ import net.minecraft.client.gui.GuiSlot;
/*     */ import net.minecraft.client.gui.ScaledResolution;
/*     */ import net.minecraft.client.resources.I18n;
/*     */ import net.minecraftforge.fml.relauncher.Side;
/*     */ import net.minecraftforge.fml.relauncher.SideOnly;
/*     */ import org.lwjgl.input.Mouse;
/*     */ import xaero.common.IXaeroMinimap;
/*     */ import xaero.common.minimap.waypoints.Waypoint;
/*     */ import xaero.common.minimap.waypoints.WaypointWorld;
/*     */ import xaero.common.minimap.waypoints.WaypointsManager;
/*     */ import xaero.common.minimap.waypoints.WaypointsSort;
/*     */ 
/*     */ @SideOnly(Side.CLIENT)
/*     */ public class GuiWaypoints
/*     */   extends GuiScreen
/*     */   implements IDropDownCallback
/*     */ {
/*     */   private static final int TEMPORARY = 0;
/*     */   private static final int DISABLED = 1;
/*     */   private static final int SERVER = 2;
/*     */   public static final int ROTATION = 3;
/*     */   private static final int FRAME_TOP_SIZE = 58;
/*     */   private static final int FRAME_BOTTOM_SIZE = 61;
/*     */   public static boolean distanceDivided;
/*     */   private GuiScreen parentScreen;
/*     */   private List list;
/*     */   private WaypointWorld displayedWorld;
/*     */   private ConcurrentSkipListSet<Integer> selectedListSet;
/*     */   private ArrayList<GuiDropDown> dropDowns;
/*     */   private GuiWaypointContainers containers;
/*     */   private GuiWaypointWorlds worlds;
/*     */   private GuiWaypointSets sets;
/*     */   private GuiDropDown containersDD;
/*     */   private GuiDropDown worldsDD;
/*     */   private GuiDropDown setsDD;
/*     */   private IXaeroMinimap modMain;
/*     */   private WaypointsManager waypointsManager;
/*     */   private int draggingFromX;
/*     */   private int draggingFromY;
/*     */   private int draggingFromSlot;
/*     */   private Waypoint draggingWaypoint;
/*     */   private boolean dropped = false;
/*     */   private boolean displayingTeleportableWorld;
/*     */   private int shiftSelectFirst;
/*     */   private ArrayList<Waypoint> waypointsSorted;
/*     */   
/*     */   public GuiWaypoints(IXaeroMinimap modMain, GuiScreen par1GuiScreen) {
/*  58 */     this.modMain = modMain;
/*  59 */     this.waypointsManager = modMain.getWaypointsManager();
/*  60 */     this.parentScreen = par1GuiScreen;
/*  61 */     this.selectedListSet = new ConcurrentSkipListSet<>();
/*  62 */     this.dropDowns = new ArrayList<>();
/*  63 */     this.draggingFromX = -1;
/*  64 */     this.draggingFromY = -1;
/*  65 */     this.draggingFromSlot = -1;
/*  66 */     this.displayedWorld = this.waypointsManager.getCurrentWorld();
/*  67 */     String currentContainer = this.displayedWorld.getContainer().getRootContainer().getKey();
/*  68 */     this.containers = new GuiWaypointContainers(modMain, this.waypointsManager, currentContainer);
/*  69 */     this.worlds = new GuiWaypointWorlds(this.waypointsManager.getWorldContainer(this.containers.getCurrentKey()), this.waypointsManager, this.displayedWorld.getFullId());
/*  70 */     this.displayingTeleportableWorld = this.waypointsManager.isWorldTeleportable(this.displayedWorld);
/*  71 */     this.waypointsSorted = new ArrayList<>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void func_73866_w_() {
/*  78 */     updateSortedList();
/*  79 */     this.sets = new GuiWaypointSets(true, this.displayedWorld);
/*  80 */     this.field_146292_n.clear();
/*  81 */     this.field_146292_n.add(new MyTinyButton(5, this.field_146294_l / 2 + 129, this.field_146295_m - 53, 
/*  82 */           I18n.func_135052_a("gui.xaero_delete", new Object[0])));
/*  83 */     this.field_146292_n
/*  84 */       .add(new GuiButton(6, this.field_146294_l / 2 - 100, this.field_146295_m - 29, I18n.func_135052_a("gui.done", new Object[0])));
/*  85 */     this.field_146292_n.add(new MyTinyButton(7, this.field_146294_l / 2 - 203, this.field_146295_m - 53, 
/*  86 */           I18n.func_135052_a("gui.xaero_add_edit", new Object[0])));
/*  87 */     this.field_146292_n.add(new MyTinyButton(8, this.field_146294_l / 2 - 120, this.field_146295_m - 53, 
/*  88 */           I18n.func_135052_a("gui.xaero_waypoint_teleport", new Object[0]) + " (T)"));
/*  89 */     this.field_146292_n.add(new MyTinyButton(9, this.field_146294_l / 2 + 46, this.field_146295_m - 53, 
/*  90 */           I18n.func_135052_a("gui.xaero_disable_enable", new Object[0])));
/*  91 */     this.field_146292_n
/*  92 */       .add(new MyTinyButton(10, this.field_146294_l / 2 + 130, 32, I18n.func_135052_a("gui.xaero_clear", new Object[0])));
/*  93 */     this.field_146292_n
/*  94 */       .add(new MyTinyButton(11, this.field_146294_l / 2 - 203, 32, I18n.func_135052_a("gui.xaero_options", new Object[0])));
/*  95 */     this.field_146292_n.add(new MyTinyButton(12, this.field_146294_l / 2 - 37, this.field_146295_m - 53, 
/*  96 */           I18n.func_135052_a("gui.xaero_share", new Object[0])));
/*  97 */     this.list = new List();
/*  98 */     this.list.func_148134_d(7, 8);
/*  99 */     this.dropDowns.clear();
/* 100 */     this.dropDowns.add(this.containersDD = new GuiDropDown(this.containers.options, this.field_146294_l / 2 - 202, 17, 200, Integer.valueOf(this.containers.current), this));
/* 101 */     this.dropDowns.add(this.worldsDD = new GuiDropDown(this.worlds.options, this.field_146294_l / 2 + 2, 17, 200, Integer.valueOf(this.worlds.current), this));
/* 102 */     this.dropDowns.add(this.setsDD = new GuiDropDown(this.sets.getOptions(), this.field_146294_l / 2 - 100, 33, 200, Integer.valueOf(this.sets.getCurrentSet()), this));
/*     */   }
/*     */   
/*     */   protected void func_146284_a(GuiButton p_146284_1_) {
/* 106 */     if (p_146284_1_.field_146124_l) {
/* 107 */       ArrayList<Waypoint> selectedWaypoints; String[] worldKeys; Waypoint selected; switch (p_146284_1_.field_146127_k) {
/*     */         case 5:
/* 109 */           if (!this.selectedListSet.isEmpty()) {
/* 110 */             undrag();
/* 111 */             boolean shouldRestore = true; Iterator<Integer> iterator;
/* 112 */             for (iterator = this.selectedListSet.iterator(); iterator.hasNext(); ) { int i = ((Integer)iterator.next()).intValue();
/* 113 */               Waypoint w = this.list.getWaypoint(i);
/* 114 */               if (w instanceof xaero.common.api.spigot.ServerWaypoint)
/*     */                 continue; 
/* 116 */               if (!w.isTemporary())
/* 117 */                 shouldRestore = false; 
/* 118 */               w.setTemporary(true); }
/*     */             
/* 120 */             if (shouldRestore)
/* 121 */               for (iterator = this.selectedListSet.iterator(); iterator.hasNext(); ) { int i = ((Integer)iterator.next()).intValue();
/* 122 */                 Waypoint w = this.list.getWaypoint(i);
/* 123 */                 if (w instanceof xaero.common.api.spigot.ServerWaypoint)
/*     */                   continue; 
/* 125 */                 w.setTemporary(false); }
/*     */                
/*     */             try {
/* 128 */               this.modMain.getSettings().saveWaypoints(this.displayedWorld);
/* 129 */             } catch (IOException e) {
/* 130 */               e.printStackTrace();
/*     */             } 
/*     */           } 
/*     */           break;
/*     */         case 6:
/* 135 */           this.field_146297_k.func_147108_a(this.parentScreen);
/*     */           break;
/*     */         case 7:
/* 138 */           selectedWaypoints = getSelectedWaypointsList(false);
/* 139 */           this.field_146297_k.func_147108_a(new GuiAddWaypoint(this.modMain, this, selectedWaypoints, this.displayedWorld.getContainer().getRootContainer().getKey(), this.displayedWorld, selectedWaypoints.isEmpty()));
/* 140 */           this.list.setSelected(-1);
/*     */           break;
/*     */         case 8:
/* 143 */           this.displayingTeleportableWorld = this.waypointsManager.isWorldTeleportable(this.displayedWorld);
/* 144 */           this.waypointsManager.teleportToWaypoint(this.list.getWaypoint(((Integer)this.selectedListSet.first()).intValue()), this.displayedWorld, this);
/*     */           break;
/*     */         case 9:
/* 147 */           selectedWaypoints = getSelectedWaypointsList(true);
/* 148 */           if (allWaypointsAre(selectedWaypoints, 0)) {
/* 149 */             for (Waypoint waypoint : selectedWaypoints)
/*     */             {
/* 151 */               this.displayedWorld.getCurrentSet().getList().remove(waypoint);
/*     */             }
/* 153 */             this.selectedListSet.clear();
/* 154 */           } else if (allWaypointsAre(selectedWaypoints, 1)) {
/* 155 */             for (Waypoint waypoint : selectedWaypoints)
/* 156 */               waypoint.setDisabled(false); 
/*     */           } else {
/* 158 */             for (Waypoint waypoint : selectedWaypoints)
/* 159 */               waypoint.setDisabled(true); 
/* 160 */           }  updateSortedList();
/*     */           try {
/* 162 */             this.modMain.getSettings().saveWaypoints(this.displayedWorld);
/* 163 */           } catch (IOException e) {
/* 164 */             e.printStackTrace();
/*     */           } 
/*     */           break;
/*     */         case 10:
/* 168 */           worldKeys = this.worlds.getCurrentKeys();
/* 169 */           if (shouldDeleteSet()) {
/* 170 */             this.field_146297_k.func_147108_a((GuiScreen)new GuiDeleteSet(this.modMain, I18n.func_135052_a(this.sets.getOptions()[this.sets.getCurrentSet()], new Object[0]), worldKeys[0], worldKeys[1], this.sets
/* 171 */                   .getOptions()[this.sets.getCurrentSet()], this)); break;
/*     */           } 
/* 173 */           this.field_146297_k.func_147108_a((GuiScreen)new GuiClearSet(this.modMain, I18n.func_135052_a(this.sets.getOptions()[this.sets.getCurrentSet()], new Object[0]), worldKeys[0], worldKeys[1], this.sets
/* 174 */                 .getOptions()[this.sets.getCurrentSet()], this));
/*     */           break;
/*     */         case 11:
/* 177 */           this.field_146297_k.func_147108_a(new GuiWaypointsOptions(this.modMain, this, this.displayedWorld));
/*     */           break;
/*     */         case 12:
/* 180 */           selected = this.selectedListSet.isEmpty() ? null : this.list.getWaypoint(((Integer)this.selectedListSet.first()).intValue());
/* 181 */           if (selected != null)
/* 182 */             this.modMain.getWaypointSharing().shareWaypoint(this, selected, this.displayedWorld); 
/*     */           break;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   private ArrayList<Waypoint> getSelectedWaypointsList(boolean includeServer) {
/* 188 */     ArrayList<Waypoint> result = new ArrayList<>();
/* 189 */     for (Integer i : this.selectedListSet) {
/* 190 */       Waypoint w = this.list.getWaypoint(i.intValue());
/* 191 */       if (includeServer || !w.isServerWaypoint())
/* 192 */         result.add(w); 
/*     */     } 
/* 194 */     return result;
/*     */   }
/*     */   
/*     */   public static boolean allWaypointsAre(ArrayList<Waypoint> waypoints, int predicate) {
/* 198 */     boolean allTrue = true;
/* 199 */     switch (predicate) {
/*     */       case 0:
/* 201 */         for (Waypoint w : waypoints) {
/* 202 */           if (!w.isTemporary()) {
/* 203 */             allTrue = false; break;
/*     */           } 
/*     */         } 
/*     */         break;
/*     */       case 1:
/* 208 */         for (Waypoint w : waypoints) {
/* 209 */           if (!w.isDisabled()) {
/* 210 */             allTrue = false; break;
/*     */           } 
/*     */         } 
/*     */         break;
/*     */       case 2:
/* 215 */         for (Waypoint w : waypoints) {
/* 216 */           if (!w.isServerWaypoint()) {
/* 217 */             allTrue = false; break;
/*     */           } 
/*     */         } 
/*     */         break;
/*     */       case 3:
/* 222 */         for (Waypoint w : waypoints) {
/* 223 */           if (!w.isRotation()) {
/* 224 */             allTrue = false; break;
/*     */           } 
/*     */         } 
/*     */         break;
/*     */     } 
/* 229 */     return allTrue;
/*     */   }
/*     */   
/*     */   public boolean shouldDeleteSet() {
/* 233 */     return (!this.sets.getOptions()[this.sets.getCurrentSet()].equals("gui.xaero_default") && this.displayedWorld.getCurrentSet().getList().isEmpty());
/*     */   }
/*     */   
/*     */   public void func_146274_d() throws IOException {
/* 237 */     super.func_146274_d();
/* 238 */     int wheel = Mouse.getEventDWheel() / 120;
/* 239 */     if (wheel != 0) {
/* 240 */       ScaledResolution scaledResolution = new ScaledResolution(this.field_146297_k);
/* 241 */       int mouseXScaled = Mouse.getX() / scaledResolution.func_78325_e();
/* 242 */       int mouseYScaled = scaledResolution.func_78328_b() - 1 - Mouse.getY() / scaledResolution.func_78325_e();
/* 243 */       for (GuiDropDown d : this.dropDowns) {
/* 244 */         if (!d.isClosed() && d.onDropDown(mouseXScaled, mouseYScaled, this.field_146295_m)) {
/* 245 */           d.mouseScrolled(wheel, mouseXScaled, mouseYScaled, this.field_146295_m);
/*     */           return;
/*     */         } 
/*     */       } 
/*     */     } 
/* 250 */     this.list.func_178039_p();
/*     */   }
/*     */   
/*     */   private void undrag() {
/* 254 */     this.draggingFromX = -1;
/* 255 */     this.draggingFromY = -1;
/* 256 */     this.draggingFromSlot = -1;
/* 257 */     this.draggingWaypoint = null;
/*     */   }
/*     */   
/*     */   protected void func_73864_a(int par1, int par2, int par3) throws IOException {
/* 261 */     for (GuiDropDown d : this.dropDowns) {
/* 262 */       if (!d.isClosed() && d.onDropDown(par1, par2, this.field_146295_m)) {
/* 263 */         d.mouseClicked(par1, par2, par3, this.field_146295_m);
/*     */         return;
/*     */       } 
/* 266 */       d.setClosed(true);
/*     */     } 
/* 268 */     for (GuiDropDown d : this.dropDowns) {
/* 269 */       if (d.onDropDown(par1, par2, this.field_146295_m)) {
/* 270 */         d.mouseClicked(par1, par2, par3, this.field_146295_m);
/*     */         return;
/*     */       } 
/* 273 */       d.setClosed(true);
/*     */     } 
/* 275 */     if (this.dropped)
/*     */       return; 
/* 277 */     if (par3 == 0) {
/* 278 */       if (par2 >= 58 && par2 < this.field_146295_m - 61 && this.displayedWorld.getContainer().getRootContainer().getSortType() == WaypointsSort.NONE) {
/* 279 */         this.draggingFromX = par1;
/* 280 */         this.draggingFromY = par2;
/* 281 */         this.draggingFromSlot = this.list.func_148124_c(par1, par2);
/* 282 */         if (this.draggingFromSlot >= this.displayedWorld.getCurrentSet().getList().size())
/* 283 */           this.draggingFromSlot = -1; 
/*     */       } 
/*     */     } else {
/* 286 */       this.list.setSelected(-1);
/* 287 */     }  super.func_73864_a(par1, par2, par3);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void func_146286_b(int par1, int par2, int par3) {
/*     */     try {
/* 294 */       if (this.draggingWaypoint != null)
/* 295 */         this.modMain.getSettings().saveWaypoints(this.displayedWorld); 
/* 296 */     } catch (IOException e) {
/* 297 */       e.printStackTrace();
/*     */     } 
/* 299 */     undrag();
/* 300 */     super.func_146286_b(par1, par2, par3);
/* 301 */     for (GuiDropDown d : this.dropDowns)
/* 302 */       d.mouseReleased(par1, par2, par3, this.field_146295_m); 
/*     */   }
/*     */   
/*     */   protected void func_73869_a(char par1, int par2) throws IOException {
/* 306 */     super.func_73869_a(par1, par2);
/* 307 */     switch (par2) {
/*     */       case 211:
/* 309 */         if (((GuiButton)this.field_146292_n.get(4)).field_146124_l) {
/* 310 */           for (Iterator<Integer> iterator = this.selectedListSet.iterator(); iterator.hasNext(); ) { int i = ((Integer)iterator.next()).intValue();
/* 311 */             this.list.getWaypoint(i).setTemporary(true); }
/* 312 */            func_146284_a(this.field_146292_n.get(4));
/*     */         } 
/*     */         break;
/*     */       case 20:
/* 316 */         func_146284_a(this.field_146292_n.get(3));
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void func_73863_a(int par1, int par2, float par3) {
/* 326 */     if (this.field_146297_k.field_71439_g == null) {
/* 327 */       this.field_146297_k.func_147108_a(null);
/*     */       return;
/*     */     } 
/* 330 */     updateButtons();
/* 331 */     this.list.func_148128_a(par1, par2, par3);
/* 332 */     func_73732_a(this.field_146289_q, I18n.func_135052_a("gui.xaero_world_server", new Object[0]), this.field_146294_l / 2 - 102, 5, 16777215);
/*     */     
/* 334 */     func_73732_a(this.field_146289_q, I18n.func_135052_a("gui.xaero_subworld_dimension", new Object[0]), this.field_146294_l / 2 + 102, 5, 16777215);
/*     */ 
/*     */     
/* 337 */     if (this.draggingFromSlot != -1) {
/* 338 */       int distance = (int)Math.sqrt(Math.pow((par1 - this.draggingFromX), 2.0D) + Math.pow((par2 - this.draggingFromY), 2.0D));
/* 339 */       int toSlot = Math.min(this.displayedWorld.getCurrentSet().getList().size() - 1, this.list.func_148124_c(par1, par2));
/* 340 */       if (distance > 4 && this.draggingWaypoint == null) {
/* 341 */         this.draggingWaypoint = this.displayedWorld.getCurrentSet().getList().get(this.draggingFromSlot);
/* 342 */         this.list.setSelected(-1);
/*     */       } 
/* 344 */       if (this.draggingWaypoint != null && this.draggingFromSlot != toSlot && toSlot != -1) {
/* 345 */         int direction = (toSlot > this.draggingFromSlot) ? 1 : -1; int i;
/* 346 */         for (i = this.draggingFromSlot; i != toSlot; i += direction)
/* 347 */           this.displayedWorld.getCurrentSet().getList().set(i, this.displayedWorld.getCurrentSet().getList().get(i + direction)); 
/* 348 */         this.displayedWorld.getCurrentSet().getList().set(toSlot, this.draggingWaypoint);
/* 349 */         this.draggingFromSlot = toSlot;
/*     */       } 
/* 351 */       int fromCenter = this.draggingFromX - this.list.field_148155_a / 2;
/* 352 */       this.list.drawWaypointSlot(this.draggingWaypoint, par1 - 108 - fromCenter, par2 - this.list.field_148149_f / 4);
/*     */     } 
/*     */ 
/*     */     
/* 356 */     if (this.dropped) {
/* 357 */       super.func_73863_a(0, 0, par3);
/*     */     } else {
/* 359 */       super.func_73863_a(par1, par2, par3);
/* 360 */     }  this.dropped = false; int k;
/* 361 */     for (k = 0; k < this.dropDowns.size(); k++) {
/* 362 */       if (((GuiDropDown)this.dropDowns.get(k)).isClosed()) {
/* 363 */         ((GuiDropDown)this.dropDowns.get(k)).drawButton(par1, par2, this.field_146295_m);
/*     */       } else {
/* 365 */         this.dropped = true;
/*     */       } 
/* 367 */     }  for (k = 0; k < this.dropDowns.size(); k++) {
/* 368 */       if (!((GuiDropDown)this.dropDowns.get(k)).isClosed())
/* 369 */         ((GuiDropDown)this.dropDowns.get(k)).drawButton(par1, par2, this.field_146295_m); 
/*     */     } 
/*     */   }
/*     */   private void updateButtons() {
/* 373 */     ((GuiButton)this.field_146292_n.get(4)).field_146124_l = !this.selectedListSet.isEmpty();
/* 374 */     ((GuiButton)this.field_146292_n.get(7)).field_146124_l = (this.selectedListSet.size() == 1);
/* 375 */     ((GuiButton)this.field_146292_n.get(3)).field_146124_l = (this.selectedListSet.size() == 1 && ((this.modMain.getSettings()).allowWrongWorldTeleportation || this.displayingTeleportableWorld) && this.displayedWorld.getContainer().getRootContainer().isTeleportationEnabled());
/* 376 */     ArrayList<Waypoint> selectedWaypointsList = getSelectedWaypointsList(true);
/* 377 */     ((GuiButton)this.field_146292_n.get(2)).field_146124_l = ((this.selectedListSet.isEmpty() && this.field_146297_k.field_71439_g != null) || !allWaypointsAre(selectedWaypointsList, 2));
/* 378 */     ((GuiButton)this.field_146292_n.get(5))
/* 379 */       .field_146126_j = I18n.func_135052_a(shouldDeleteSet() ? "gui.xaero_delete_set" : "gui.xaero_clear", new Object[0]);
/* 380 */     if (!this.selectedListSet.isEmpty() && allWaypointsAre(selectedWaypointsList, 0)) {
/* 381 */       ((GuiButton)this.field_146292_n.get(4)).field_146126_j = I18n.func_135052_a("gui.xaero_delete", new Object[0]);
/* 382 */       ((GuiButton)this.field_146292_n.get(0)).field_146126_j = I18n.func_135052_a("gui.xaero_restore", new Object[0]);
/*     */     } else {
/* 384 */       ((GuiButton)this.field_146292_n.get(0)).field_146126_j = I18n.func_135052_a("gui.xaero_delete", new Object[0]);
/* 385 */       String[] enabledisable = I18n.func_135052_a("gui.xaero_disable_enable", new Object[0]).split("/");
/* 386 */       ((GuiButton)this.field_146292_n.get(4)).field_146126_j = enabledisable[!allWaypointsAre(selectedWaypointsList, 1) ? 0 : 1];
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public GuiScreen getParentScreen() {
/* 392 */     return this.parentScreen;
/*     */   }
/*     */   
/*     */   @SideOnly(Side.CLIENT)
/*     */   class List extends GuiSlot {
/*     */     private int lastClickedThisFrame;
/*     */     
/* 399 */     public List() { super(GuiWaypoints.this.field_146297_k, GuiWaypoints.this.field_146294_l, GuiWaypoints.this.field_146295_m, 58, Math.max(62, GuiWaypoints.this.field_146295_m - 61), 18);
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
/* 429 */       this.lastClickedThisFrame = -1; }
/*     */     protected int func_148127_b() { int size = GuiWaypoints.this.displayedWorld.getCurrentSet().getList().size(); if (GuiWaypoints.this.waypointsManager.getServerWaypoints() != null)
/*     */         size += GuiWaypoints.this.waypointsManager.getServerWaypoints().size(); 
/* 432 */       return size; } protected void func_148144_a(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY) { if (slotIndex != this.lastClickedThisFrame) {
/* 433 */         setSelected(slotIndex);
/* 434 */         this.lastClickedThisFrame = slotIndex;
/*     */       }  }
/*     */     private Waypoint getWaypoint(int slotIndex) { Waypoint waypoint = null; if (slotIndex < GuiWaypoints.this.displayedWorld.getCurrentSet().getList().size()) { waypoint = GuiWaypoints.this.waypointsSorted.get(slotIndex); }
/*     */       else if (GuiWaypoints.this.waypointsManager.getServerWaypoints() != null) { int serverWPIndex = slotIndex - GuiWaypoints.this.displayedWorld.getCurrentSet().getList().size(); if (serverWPIndex < GuiWaypoints.this.waypointsManager.getServerWaypoints().size())
/*     */           waypoint = GuiWaypoints.this.waypointsManager.getServerWaypoints().get(serverWPIndex);  }
/* 439 */        return waypoint; } public void setSelected(int index) { if (index == -1) {
/* 440 */         GuiWaypoints.this.selectedListSet.clear();
/* 441 */         GuiWaypoints.this.shiftSelectFirst = -1;
/*     */         return;
/*     */       } 
/* 444 */       int currentSize = GuiWaypoints.this.selectedListSet.size();
/* 445 */       boolean shiftPressed = GuiScreen.func_146272_n();
/* 446 */       if ((currentSize > 1 || (currentSize == 1 && ((Integer)GuiWaypoints.this.selectedListSet.first()).intValue() != index)) && 
/* 447 */         !GuiScreen.func_146271_m() && !shiftPressed)
/*     */       {
/*     */         
/* 450 */         GuiWaypoints.this.selectedListSet.clear(); } 
/* 451 */       if (currentSize > 0 && shiftPressed) {
/* 452 */         int direction = (index > GuiWaypoints.this.shiftSelectFirst) ? 1 : -1;
/* 453 */         GuiWaypoints.this.selectedListSet.clear(); int i;
/* 454 */         for (i = GuiWaypoints.this.shiftSelectFirst; i != index + direction; i += direction)
/* 455 */           GuiWaypoints.this.selectedListSet.add(Integer.valueOf(i)); 
/* 456 */       } else if (GuiWaypoints.this.selectedListSet.contains(Integer.valueOf(index))) {
/* 457 */         GuiWaypoints.this.selectedListSet.remove(Integer.valueOf(index));
/*     */       } else {
/* 459 */         GuiWaypoints.this.shiftSelectFirst = index;
/* 460 */         GuiWaypoints.this.selectedListSet.add(Integer.valueOf(index));
/*     */       }  }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected boolean func_148131_a(int p_148131_1_) {
/* 471 */       return (!GuiWaypoints.this.selectedListSet.isEmpty() && GuiWaypoints.this.selectedListSet.contains(Integer.valueOf(p_148131_1_)));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected int func_148138_e() {
/* 478 */       return func_148127_b() * 18;
/*     */     }
/*     */     
/*     */     protected void func_148123_a() {
/* 482 */       this.lastClickedThisFrame = -1;
/* 483 */       GuiWaypoints.this.func_146276_q_();
/*     */     }
/*     */     
/*     */     public void func_192637_a(int p_192637_1_, int p_192637_2_, int p_192637_3_, int p_192637_4_, int p_192637_5_, int p_192637_6_, float p_192637_7_) {
/* 487 */       Waypoint w = getWaypoint(p_192637_1_);
/* 488 */       if (w == GuiWaypoints.this.draggingWaypoint)
/*     */         return; 
/* 490 */       drawWaypointSlot(w, p_192637_2_, p_192637_3_);
/*     */     }
/*     */     
/*     */     public void drawWaypointSlot(Waypoint w, int p_180791_2_, int p_180791_3_) {
/* 494 */       if (w == null)
/*     */         return; 
/* 496 */       GuiWaypoints.this.func_73732_a(GuiWaypoints.this.field_146289_q, w
/* 497 */           .getLocalizedName() + (w.isDisabled() ? (" §4" + I18n.func_135052_a("gui.xaero_disabled", new Object[0])) : (w.isTemporary() ? (" §4" + I18n.func_135052_a("gui.xaero_temporary", new Object[0])) : "")), p_180791_2_ + 110, p_180791_3_ + 1, 16777215);
/*     */       
/* 499 */       int rectX = p_180791_2_ + 8 + 4;
/* 500 */       int rectY = p_180791_3_ + 6;
/* 501 */       if (w.isGlobal())
/* 502 */         GuiWaypoints.this.func_73732_a(GuiWaypoints.this.field_146289_q, "*", rectX - 25, rectY - 3, 16777215); 
/* 503 */       GuiWaypoints.this.modMain.getInterfaces().getMinimapInterface().getWaypointsGuiRenderer().drawIconOnGUI(w, GuiWaypoints.this.modMain.getSettings(), rectX, rectY);
/*     */     }
/*     */     
/*     */     public boolean func_148125_i() {
/* 507 */       if (GuiWaypoints.this.dropped || GuiWaypoints.this.draggingWaypoint != null)
/* 508 */         return false; 
/* 509 */       return super.func_148125_i();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean onSelected(GuiDropDown menu, int selectedIndex) {
/* 515 */     if (menu == this.containersDD || menu == this.worldsDD) {
/* 516 */       if (menu == this.containersDD) {
/* 517 */         this.containers.current = selectedIndex;
/* 518 */         if (this.containers.current != this.containers.auto) {
/* 519 */           WaypointWorld firstWorld = this.waypointsManager.getWorldContainer(this.containers.getCurrentKey()).getFirstWorld();
/* 520 */           this.waypointsManager.setCustomContainerID(firstWorld.getContainer().getKey());
/* 521 */           this.waypointsManager.setCustomWorldID(firstWorld.getId());
/*     */         } else {
/* 523 */           this.waypointsManager.setCustomContainerID(null);
/* 524 */           this.waypointsManager.setCustomWorldID(null);
/*     */         } 
/* 526 */         this.displayedWorld = this.waypointsManager.getCurrentWorld();
/* 527 */         updateSortedList();
/* 528 */         this.worlds = new GuiWaypointWorlds(this.waypointsManager.getWorldContainer(this.containers.getCurrentKey()), this.waypointsManager, this.displayedWorld.getFullId());
/* 529 */         this.dropDowns.set(1, this.worldsDD = new GuiDropDown(this.worlds.options, this.field_146294_l / 2 + 2, 17, 200, Integer.valueOf(this.worlds.current), this));
/*     */       }
/* 531 */       else if (menu == this.worldsDD) {
/* 532 */         this.worlds.current = selectedIndex;
/* 533 */         if (this.worlds.current != this.worlds.auto) {
/* 534 */           String[] keys = this.worlds.getCurrentKeys();
/* 535 */           this.waypointsManager.setCustomContainerID(keys[0]);
/* 536 */           this.waypointsManager.setCustomWorldID(keys[1]);
/*     */         } else {
/* 538 */           this.waypointsManager.setCustomContainerID(null);
/* 539 */           this.waypointsManager.setCustomWorldID(null);
/*     */         } 
/* 541 */         this.displayedWorld = this.waypointsManager.getCurrentWorld();
/*     */         
/* 543 */         this.worlds = new GuiWaypointWorlds(this.waypointsManager.getWorldContainer(this.containers.getCurrentKey()), this.waypointsManager, this.displayedWorld.getFullId());
/* 544 */         this.dropDowns.set(1, this.worldsDD = new GuiDropDown(this.worlds.options, this.field_146294_l / 2 + 2, 17, 200, Integer.valueOf(this.worlds.current), this));
/* 545 */         updateSortedList();
/*     */       } 
/* 547 */       this.displayingTeleportableWorld = this.waypointsManager.isWorldTeleportable(this.displayedWorld);
/* 548 */       this.waypointsManager.updateWaypoints();
/* 549 */       this.list.setSelected(-1);
/* 550 */       this.sets = new GuiWaypointSets(true, this.displayedWorld);
/* 551 */       this.dropDowns.set(2, this.setsDD = new GuiDropDown(this.sets.getOptions(), this.field_146294_l / 2 - 100, 33, 200, Integer.valueOf(this.sets.getCurrentSet()), this));
/* 552 */       return true;
/* 553 */     }  if (menu == this.setsDD) {
/* 554 */       if (selectedIndex == menu.size() - 1) {
/* 555 */         System.out.println("New waypoint set gui");
/* 556 */         this.field_146297_k.func_147108_a(new GuiNewSet(this.modMain, this, this.displayedWorld));
/* 557 */         return false;
/*     */       } 
/* 559 */       this.sets.setCurrentSet(selectedIndex);
/* 560 */       this.displayedWorld.setCurrent(this.sets.getCurrentSetKey());
/* 561 */       updateSortedList();
/* 562 */       this.waypointsManager.updateWaypoints();
/* 563 */       this.list.setSelected(-1);
/*     */       try {
/* 565 */         this.modMain.getSettings().saveWaypoints(this.displayedWorld);
/* 566 */       } catch (IOException e) {
/* 567 */         e.printStackTrace();
/*     */       } 
/* 569 */       return true;
/*     */     } 
/* 571 */     return false;
/*     */   }
/*     */   
/*     */   private void updateSortedList() {
/* 575 */     WaypointsSort sortType = this.displayedWorld.getContainer().getRootContainer().getSortType();
/* 576 */     if (sortType == WaypointsSort.NONE) {
/* 577 */       this.waypointsSorted = this.displayedWorld.getCurrentSet().getList();
/*     */       return;
/*     */     } 
/* 580 */     distanceDivided = this.waypointsManager.divideBy8(this.displayedWorld.getContainer().getKey());
/* 581 */     ArrayList<KeySortableByOther<Waypoint>> sortableKeys = new ArrayList<>();
/* 582 */     for (Waypoint w : this.displayedWorld.getCurrentSet().getList()) {
/* 583 */       sortableKeys.add(new KeySortableByOther<>(w, new Comparable[] { (sortType == WaypointsSort.NAME) ? w.getComparisonName() : ((sortType == WaypointsSort.SYMBOL) ? w.getSymbol() : Double.valueOf(w.getComparisonDistance(this.field_146297_k.func_175606_aa(), distanceDivided))) }));
/* 584 */     }  Collections.sort(sortableKeys);
/* 585 */     this.waypointsSorted = new ArrayList<>();
/* 586 */     for (KeySortableByOther<Waypoint> k : sortableKeys)
/* 587 */       this.waypointsSorted.add(k.getKey()); 
/* 588 */     if (this.displayedWorld.getContainer().getRootContainer().isSortReversed())
/* 589 */       Collections.reverse(this.waypointsSorted); 
/*     */   }
/*     */ }


/* Location:              C:\Users\ioanm\Downloads\Xaeros_Minimap_20.15.3_Forge_1.12.jar!\xaero\common\gui\GuiWaypoints.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */