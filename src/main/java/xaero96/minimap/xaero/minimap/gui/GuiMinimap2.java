/*    */ package xaero.minimap.gui;
/*    */ 
/*    */ import net.minecraft.client.gui.GuiScreen;
/*    */ import net.minecraft.client.resources.I18n;
/*    */ import xaero.common.IXaeroMinimap;
/*    */ import xaero.common.gui.GuiMinimapSettings;
/*    */ import xaero.common.settings.ModOptions;
/*    */ import xaero.common.settings.ModSettings;
/*    */ 
/*    */ 
/*    */ public class GuiMinimap2
/*    */   extends GuiMinimapSettings
/*    */ {
/*    */   public GuiMinimap2(IXaeroMinimap modMain, GuiScreen par1GuiScreen) {
/* 15 */     super(modMain, par1GuiScreen);
/* 16 */     this.options = new ModOptions[] { ModOptions.OPACITY, ModOptions.WAYPOINT_OPACITY_MAP, ModOptions.WAYPOINTS_ALL_SETS, ModOptions.WAYPOINT_OPACITY_INGAME, ModOptions.DISTANCE, ModOptions.WAYPOINTS_SCALE, ModOptions.WAYPOINTS_DISTANCE, ModOptions.KEEP_WP_NAMES, ModOptions.WAYPOINTS_DISTANCE_MIN, ModOptions.WAYPOINT_VERTICAL_LOOKING_ANGLE, ModOptions.ALWAYS_SHOW_DISTANCE, ModOptions.WAYPOINT_LOOKING_ANGLE };
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void func_73866_w_() {
/* 32 */     super.func_73866_w_();
/* 33 */     if (ModSettings.serverSettings != ModSettings.defaultSettings) {
/* 34 */       this.screenTitle = "§e" + I18n.func_135052_a("gui.xaero_server_disabled", new Object[0]);
/*    */     }
/*    */   }
/*    */   
/*    */   protected boolean isNextButtonEnabled() {
/* 39 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isPrevButtonEnabled() {
/* 44 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void onNextButton() {
/* 49 */     this.field_146297_k.func_147108_a((GuiScreen)new GuiMinimap3(this.modMain, this.parentGuiScreen));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void onPrevButton() {
/* 54 */     this.field_146297_k.func_147108_a((GuiScreen)new GuiMinimap(this.modMain, this.parentGuiScreen));
/*    */   }
/*    */ }


/* Location:              C:\Users\ioanm\Downloads\Xaeros_Minimap_20.15.3_Forge_1.12.jar!\xaero\minimap\gui\GuiMinimap2.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */