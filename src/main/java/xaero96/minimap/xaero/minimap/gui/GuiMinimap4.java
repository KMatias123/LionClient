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
/*    */ public class GuiMinimap4
/*    */   extends GuiMinimapSettings
/*    */ {
/*    */   public GuiMinimap4(IXaeroMinimap modMain, GuiScreen par1GuiScreen) {
/* 15 */     super(modMain, par1GuiScreen);
/* 16 */     this.options = new ModOptions[] { ModOptions.TERRAIN_DEPTH, ModOptions.TERRAIN_SLOPES, ModOptions.BLOCK_TRANSPARENCY, ModOptions.MAIN_ENTITY_AS, ModOptions.CENTERED_ENLARGED, ModOptions.PLAYER_ARROW_OPACITY, ModOptions.ZOOMED_OUT_ENLARGED, ModOptions.CAVE_MAPS_DEPTH, ModOptions.WAYPOINTS_DEFAULT_TP, ModOptions.CAVE_ZOOM };
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
/*    */   public void func_73866_w_() {
/* 30 */     super.func_73866_w_();
/* 31 */     if (ModSettings.serverSettings != ModSettings.defaultSettings) {
/* 32 */       this.screenTitle = "§e" + I18n.func_135052_a("gui.xaero_server_disabled", new Object[0]);
/*    */     }
/*    */   }
/*    */   
/*    */   protected boolean isNextButtonEnabled() {
/* 37 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isPrevButtonEnabled() {
/* 42 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void onNextButton() {
/* 47 */     this.field_146297_k.func_147108_a((GuiScreen)new GuiMinimap5(this.modMain, this.parentGuiScreen));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void onPrevButton() {
/* 52 */     this.field_146297_k.func_147108_a((GuiScreen)new GuiMinimap3(this.modMain, this.parentGuiScreen));
/*    */   }
/*    */ }


/* Location:              C:\Users\ioanm\Downloads\Xaeros_Minimap_20.15.3_Forge_1.12.jar!\xaero\minimap\gui\GuiMinimap4.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */