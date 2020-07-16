/*    */ package xaero.common.gui;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.client.gui.GuiButton;
/*    */ import net.minecraft.client.gui.GuiScreen;
/*    */ import net.minecraft.client.gui.GuiYesNo;
/*    */ import net.minecraft.client.resources.I18n;
/*    */ import xaero.common.IXaeroMinimap;
/*    */ import xaero.common.minimap.waypoints.WaypointsManager;
/*    */ 
/*    */ public class GuiDeleteSet
/*    */   extends GuiYesNo
/*    */ {
/*    */   private String container;
/*    */   private String world;
/*    */   private String name;
/*    */   private GuiScreen parentScreen;
/*    */   private IXaeroMinimap modMain;
/*    */   private WaypointsManager waypointsManager;
/*    */   
/*    */   public GuiDeleteSet(IXaeroMinimap modMain, String setName, String container, String world, String name, GuiScreen parent) {
/* 22 */     super(null, I18n.func_135052_a("gui.xaero_delete_set_message", new Object[0]) + ": " + setName.replace("§§", ":") + "?", I18n.func_135052_a("gui.xaero_delete_set_message2", new Object[0]), 0);
/* 23 */     this.modMain = modMain;
/* 24 */     this.waypointsManager = modMain.getWaypointsManager();
/* 25 */     this.parentScreen = parent;
/* 26 */     this.container = container;
/* 27 */     this.world = world;
/* 28 */     this.name = name;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void func_146284_a(GuiButton button) throws IOException {
/* 33 */     switch (button.field_146127_k) {
/*    */       case 0:
/* 35 */         this.waypointsManager.getWorld(this.container, this.world).getSets().remove(this.name);
/* 36 */         this.waypointsManager.getWorld(this.container, this.world).setCurrent("gui.xaero_default");
/* 37 */         this.waypointsManager.updateWaypoints();
/* 38 */         this.modMain.getSettings().saveWaypoints(this.waypointsManager.getWorld(this.container, this.world));
/* 39 */         this.field_146297_k.func_147108_a(new GuiWaypoints(this.modMain, ((GuiWaypoints)this.parentScreen).getParentScreen()));
/*    */         break;
/*    */       case 1:
/* 42 */         this.field_146297_k.func_147108_a(this.parentScreen);
/*    */         break;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\ioanm\Downloads\Xaeros_Minimap_20.15.3_Forge_1.12.jar!\xaero\common\gui\GuiDeleteSet.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */