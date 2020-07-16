/*    */ package xaero.minimap.gui;
/*    */ 
/*    */ import net.minecraft.client.gui.GuiScreen;
/*    */ import net.minecraft.client.resources.I18n;
/*    */ import xaero.common.IXaeroMinimap;
/*    */ import xaero.common.gui.GuiMinimapSettings;
/*    */ import xaero.common.gui.ModOptionButton;
/*    */ import xaero.common.settings.ModOptions;
/*    */ import xaero.common.settings.ModSettings;
/*    */ 
/*    */ 
/*    */ public class GuiMinimap6
/*    */   extends GuiMinimapSettings
/*    */ {
/*    */   public GuiMinimap6(IXaeroMinimap modMain, GuiScreen par1GuiScreen) {
/* 16 */     super(modMain, par1GuiScreen);
/* 17 */     this.options = new ModOptions[] { ModOptions.COMPASS_ENABLED, ModOptions.COMPASS, ModOptions.WORLD_MAP, ModOptions.AA, ModOptions.SAFE_MAP, ModOptions.RENDER_LAYER, ModOptions.CAPES };
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
/* 31 */     super.func_73866_w_();
/* 32 */     if (ModSettings.serverSettings != ModSettings.defaultSettings)
/* 33 */       this.screenTitle = "§e" + I18n.func_135052_a("gui.xaero_server_disabled", new Object[0]); 
/* 34 */     ModOptions option = ModOptions.RESET;
/* 35 */     this.field_146292_n.add(new ModOptionButton(option, option.returnEnumOrdinal(), this.field_146294_l / 2 - 75, this.field_146295_m / 7 + 144, this.modMain
/* 36 */           .getSettings().getKeyBinding(option)));
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isNextButtonEnabled() {
/* 41 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isPrevButtonEnabled() {
/* 46 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void onNextButton() {}
/*    */ 
/*    */   
/*    */   protected void onPrevButton() {
/* 55 */     this.field_146297_k.func_147108_a((GuiScreen)new GuiMinimap5(this.modMain, this.parentGuiScreen));
/*    */   }
/*    */ }


/* Location:              C:\Users\ioanm\Downloads\Xaeros_Minimap_20.15.3_Forge_1.12.jar!\xaero\minimap\gui\GuiMinimap6.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */