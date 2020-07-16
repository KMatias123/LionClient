/*     */ package xaero.common.gui;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import net.minecraft.client.gui.GuiButton;
/*     */ import net.minecraft.client.gui.GuiScreen;
/*     */ import net.minecraft.client.gui.ScaledResolution;
/*     */ import net.minecraft.client.resources.I18n;
/*     */ import org.lwjgl.input.Mouse;
/*     */ import xaero.common.IXaeroMinimap;
/*     */ import xaero.common.minimap.waypoints.Waypoint;
/*     */ import xaero.common.minimap.waypoints.WaypointSet;
/*     */ import xaero.common.minimap.waypoints.WaypointWorld;
/*     */ import xaero.common.minimap.waypoints.WaypointsManager;
/*     */ 
/*     */ public class GuiTransfer
/*     */   extends GuiScreen
/*     */   implements IDropDownCallback
/*     */ {
/*     */   private GuiScreen parentScreen;
/*     */   private MySmallButton transferButton;
/*  22 */   private ArrayList<GuiDropDown> dropDowns = new ArrayList<>();
/*     */   
/*     */   private GuiWaypointContainers containers1;
/*     */   
/*     */   private GuiWaypointWorlds worlds1;
/*     */   private GuiWaypointContainers containers2;
/*     */   private GuiWaypointWorlds worlds2;
/*     */   private GuiDropDown containers1DD;
/*     */   private GuiDropDown worlds1DD;
/*     */   private GuiDropDown containers2DD;
/*     */   private GuiDropDown worlds2DD;
/*     */   private IXaeroMinimap modMain;
/*     */   private WaypointsManager waypointsManager;
/*     */   private boolean dropped = false;
/*     */   
/*     */   public GuiTransfer(IXaeroMinimap modMain, GuiScreen par1) {
/*  38 */     this.modMain = modMain;
/*  39 */     this.waypointsManager = modMain.getWaypointsManager();
/*  40 */     this.parentScreen = par1;
/*  41 */     String currentContainer = this.waypointsManager.getCurrentContainerID().split("/")[0];
/*  42 */     String currentWorld = this.waypointsManager.getCurrentContainerAndWorldID();
/*  43 */     this.containers1 = new GuiWaypointContainers(modMain, this.waypointsManager, currentContainer);
/*  44 */     this.containers2 = new GuiWaypointContainers(modMain, this.waypointsManager, currentContainer);
/*  45 */     this.worlds1 = new GuiWaypointWorlds(this.waypointsManager.getWorldContainer(this.containers1.getCurrentKey()), this.waypointsManager, currentWorld);
/*  46 */     this.worlds2 = new GuiWaypointWorlds(this.waypointsManager.getWorldContainer(this.containers2.getCurrentKey()), this.waypointsManager, currentWorld);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void func_73866_w_() {
/*  54 */     this.field_146292_n.clear();
/*  55 */     this.field_146292_n.add(this.transferButton = new MySmallButton(5, this.field_146294_l / 2 - 155, this.field_146295_m / 7 + 120, I18n.func_135052_a("gui.xaero_transfer", new Object[0])));
/*  56 */     this.transferButton.field_146124_l = false;
/*  57 */     this.field_146292_n.add(new MySmallButton(6, this.field_146294_l / 2 + 5, this.field_146295_m / 7 + 120, I18n.func_135052_a("gui.xaero_cancel", new Object[0])));
/*  58 */     this.dropDowns.clear();
/*  59 */     this.dropDowns.add(this.worlds1DD = new GuiDropDown(this.worlds1.options, this.field_146294_l / 2 + 2, this.field_146295_m / 7 + 20, 200, Integer.valueOf(this.worlds1.current), this));
/*  60 */     this.dropDowns.add(this.worlds2DD = new GuiDropDown(this.worlds2.options, this.field_146294_l / 2 + 2, this.field_146295_m / 7 + 50, 200, Integer.valueOf(this.worlds2.current), this));
/*  61 */     this.dropDowns.add(this.containers1DD = new GuiDropDown(this.containers1.options, this.field_146294_l / 2 - 202, this.field_146295_m / 7 + 20, 200, Integer.valueOf(this.containers1.current), this));
/*  62 */     this.dropDowns.add(this.containers2DD = new GuiDropDown(this.containers2.options, this.field_146294_l / 2 - 202, this.field_146295_m / 7 + 50, 200, Integer.valueOf(this.containers2.current), this));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void func_73864_a(int par1, int par2, int par3) throws IOException {
/*  67 */     for (GuiDropDown d : this.dropDowns) {
/*  68 */       if (!d.isClosed() && d.onDropDown(par1, par2, this.field_146295_m)) {
/*  69 */         d.mouseClicked(par1, par2, par3, this.field_146295_m);
/*     */         return;
/*     */       } 
/*  72 */       d.setClosed(true);
/*     */     } 
/*  74 */     for (GuiDropDown d : this.dropDowns) {
/*  75 */       if (d.onDropDown(par1, par2, this.field_146295_m)) {
/*  76 */         d.mouseClicked(par1, par2, par3, this.field_146295_m);
/*     */         return;
/*     */       } 
/*  79 */       d.setClosed(true);
/*     */     } 
/*  81 */     if (this.dropped)
/*     */       return; 
/*  83 */     super.func_73864_a(par1, par2, par3);
/*     */   }
/*     */   
/*     */   private void openParent() {
/*  87 */     if (this.parentScreen instanceof GuiWaypoints) {
/*  88 */       this.field_146297_k.func_147108_a(new GuiWaypoints(this.modMain, ((GuiWaypoints)this.parentScreen).getParentScreen()));
/*     */     } else {
/*  90 */       this.field_146297_k.func_147108_a(this.parentScreen);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void func_146286_b(int par1, int par2, int par3) {
/*  95 */     super.func_146286_b(par1, par2, par3);
/*  96 */     for (GuiDropDown d : this.dropDowns) {
/*  97 */       d.mouseReleased(par1, par2, par3, this.field_146295_m);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void func_146284_a(GuiButton p_146284_1_) {
/* 102 */     if (p_146284_1_.field_146124_l)
/* 103 */       switch (p_146284_1_.field_146127_k) {
/*     */         
/*     */         case 5:
/* 106 */           transfer();
/*     */           break;
/*     */         case 6:
/* 109 */           openParent();
/*     */           break;
/*     */       }  
/*     */   }
/*     */   
/*     */   public void transfer() {
/*     */     try {
/* 116 */       String[] keys1 = this.worlds1.getCurrentKeys();
/* 117 */       String[] keys2 = this.worlds2.getCurrentKeys();
/* 118 */       WaypointWorld from = this.waypointsManager.getWorld(keys1[0], keys1[1]);
/* 119 */       WaypointWorld to = this.waypointsManager.getWorld(keys2[0], keys2[1]);
/* 120 */       Object[] keys = from.getSets().keySet().toArray();
/* 121 */       Object[] values = from.getSets().values().toArray();
/* 122 */       for (int i = 0; i < keys.length; i++) {
/* 123 */         String setName = (String)keys[i];
/* 124 */         WaypointSet fromSet = (WaypointSet)values[i];
/* 125 */         WaypointSet toSet = (WaypointSet)to.getSets().get(setName);
/* 126 */         if (toSet == null)
/* 127 */           toSet = new WaypointSet(setName); 
/* 128 */         ArrayList<Waypoint> list = fromSet.getList();
/* 129 */         for (int j = 0; j < list.size(); j++) {
/* 130 */           Waypoint w = list.get(j);
/* 131 */           Waypoint copy = new Waypoint(w.getX(), w.getY(), w.getZ(), w.getName(), w.getSymbol(), w.getColor(), w.getType());
/* 132 */           copy.setRotation(w.isRotation());
/* 133 */           copy.setDisabled(w.isDisabled());
/* 134 */           copy.setYaw(w.getYaw());
/* 135 */           toSet.getList().add(copy);
/*     */         } 
/* 137 */         to.getSets().put(setName, toSet);
/*     */       } 
/* 139 */       if (keys2[0] != null && !keys2[0].equals(this.waypointsManager.getCustomContainerID()))
/* 140 */         this.waypointsManager.setCustomContainerID(keys2[0]); 
/* 141 */       if (keys2[1] != null && !keys2[1].equals(this.waypointsManager.getCustomWorldID()))
/* 142 */         this.waypointsManager.setCustomWorldID(keys2[1]); 
/* 143 */       this.waypointsManager.updateWaypoints();
/* 144 */       openParent();
/* 145 */       this.modMain.getSettings().saveWaypoints(to);
/* 146 */     } catch (Exception e) {
/* 147 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void func_73863_a(int par1, int par2, float par3) {
/* 153 */     func_146276_q_();
/* 154 */     func_73732_a(this.field_146289_q, I18n.func_135052_a("gui.xaero_transfer_all", new Object[0]), this.field_146294_l / 2, 5, 16777215);
/* 155 */     func_73732_a(this.field_146289_q, I18n.func_135052_a("gui.xaero_from", new Object[0]).replace("§§", ":") + ":", this.field_146294_l / 2, this.field_146295_m / 7 + 10, 16777215);
/* 156 */     func_73732_a(this.field_146289_q, I18n.func_135052_a("gui.xaero_to", new Object[0]).replace("§§", ":") + ":", this.field_146294_l / 2, this.field_146295_m / 7 + 40, 16777215);
/*     */     
/* 158 */     if (this.dropped) {
/* 159 */       super.func_73863_a(0, 0, par3);
/*     */     } else {
/* 161 */       super.func_73863_a(par1, par2, par3);
/* 162 */     }  this.dropped = false; int k;
/* 163 */     for (k = 0; k < this.dropDowns.size(); k++) {
/* 164 */       if (((GuiDropDown)this.dropDowns.get(k)).isClosed()) {
/* 165 */         ((GuiDropDown)this.dropDowns.get(k)).drawButton(par1, par2, this.field_146295_m);
/*     */       } else {
/* 167 */         this.dropped = true;
/*     */       } 
/* 169 */     }  for (k = 0; k < this.dropDowns.size(); k++) {
/* 170 */       if (!((GuiDropDown)this.dropDowns.get(k)).isClosed())
/* 171 */         ((GuiDropDown)this.dropDowns.get(k)).drawButton(par1, par2, this.field_146295_m); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void func_146274_d() throws IOException {
/* 176 */     super.func_146274_d();
/* 177 */     int wheel = Mouse.getEventDWheel() / 120;
/* 178 */     if (wheel != 0) {
/* 179 */       ScaledResolution var3 = new ScaledResolution(this.field_146297_k);
/* 180 */       int mouseXScaled = Mouse.getX() / var3.func_78325_e();
/* 181 */       int mouseYScaled = var3.func_78328_b() - 1 - Mouse.getY() / var3.func_78325_e();
/* 182 */       for (GuiDropDown d : this.dropDowns) {
/* 183 */         if (!d.isClosed() && d.onDropDown(mouseXScaled, mouseYScaled, this.field_146295_m)) {
/* 184 */           d.mouseScrolled(wheel, mouseXScaled, mouseYScaled, this.field_146295_m);
/*     */           return;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean onSelected(GuiDropDown menu, int selected) {
/* 193 */     if (menu == this.containers1DD) {
/* 194 */       this.containers1.current = selected;
/* 195 */       this.worlds1 = new GuiWaypointWorlds(this.waypointsManager.getWorldContainer(this.containers1.getCurrentKey()), this.waypointsManager, this.waypointsManager.getCurrentContainerAndWorldID());
/* 196 */       this.dropDowns.set(0, this.worlds1DD = new GuiDropDown(this.worlds1.options, this.field_146294_l / 2 + 2, this.field_146295_m / 7 + 20, 200, Integer.valueOf(this.worlds1.current), this));
/*     */     }
/* 198 */     else if (menu == this.containers2DD) {
/* 199 */       this.containers2.current = selected;
/* 200 */       this.worlds2 = new GuiWaypointWorlds(this.waypointsManager.getWorldContainer(this.containers2.getCurrentKey()), this.waypointsManager, this.waypointsManager.getCurrentContainerAndWorldID());
/* 201 */       this.dropDowns.set(1, this.worlds2DD = new GuiDropDown(this.worlds2.options, this.field_146294_l / 2 + 2, this.field_146295_m / 7 + 50, 200, Integer.valueOf(this.worlds2.current), this));
/* 202 */     } else if (menu == this.worlds1DD) {
/* 203 */       this.worlds1.current = selected;
/* 204 */     } else if (menu == this.worlds2DD) {
/* 205 */       this.worlds2.current = selected;
/* 206 */     }  this.transferButton.field_146124_l = (this.containers1.current != this.containers2.current || this.worlds1.current != this.worlds2.current);
/* 207 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\ioanm\Downloads\Xaeros_Minimap_20.15.3_Forge_1.12.jar!\xaero\common\gui\GuiTransfer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */