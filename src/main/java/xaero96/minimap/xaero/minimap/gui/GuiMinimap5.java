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
/*    */ public class GuiMinimap5
/*    */   extends GuiMinimapSettings
/*    */ {
/*    */   public GuiMinimap5(IXaeroMinimap modMain, GuiScreen par1GuiScreen) {
/* 15 */     super(modMain, par1GuiScreen);
/* 16 */     this.options = new ModOptions[] { ModOptions.PLAYER_NAMES, ModOptions.PLAYER_HEADS, ModOptions.ARROW_SCALE, ModOptions.ARROW_COLOUR, ModOptions.ENTITY_NAMETAGS, ModOptions.EAMOUNT, ModOptions.DOTS_SCALE, ModOptions.SMOOTH_DOTS, ModOptions.ENTITY_HEIGHT, ModOptions.HEIGHT_LIMIT, ModOptions.HIDE_WP_COORDS, ModOptions.HIDE_WORLD_NAMES };
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
/*    */   public void func_73866_w_() {
/* 31 */     super.func_73866_w_();
/* 32 */     if (ModSettings.serverSettings != ModSettings.defaultSettings) {
/* 33 */       this.screenTitle = "§e" + I18n.func_135052_a("gui.xaero_server_disabled", new Object[0]);
/*    */     }
/*    */   }
/*    */   
/*    */   protected boolean isNextButtonEnabled() {
/* 38 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isPrevButtonEnabled() {
/* 43 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void onNextButton() {
/* 48 */     this.field_146297_k.func_147108_a((GuiScreen)new GuiMinimap6(this.modMain, this.parentGuiScreen));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void onPrevButton() {
/* 53 */     this.field_146297_k.func_147108_a((GuiScreen)new GuiMinimap4(this.modMain, this.parentGuiScreen));
/*    */   }
/*    */ }


/* Location:              C:\Users\ioanm\Downloads\Xaeros_Minimap_20.15.3_Forge_1.12.jar!\xaero\minimap\gui\GuiMinimap5.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */