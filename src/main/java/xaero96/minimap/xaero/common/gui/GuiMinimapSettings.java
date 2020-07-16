/*    */ package xaero.common.gui;
/*    */ 
/*    */ import net.minecraft.client.gui.GuiButton;
/*    */ import net.minecraft.client.gui.GuiScreen;
/*    */ import net.minecraft.client.resources.I18n;
/*    */ import xaero.common.IXaeroMinimap;
/*    */ import xaero.common.minimap.MinimapProcessor;
/*    */ 
/*    */ public abstract class GuiMinimapSettings
/*    */   extends GuiSettings {
/*    */   private MyTinyButton nextButton;
/*    */   private MyTinyButton prevButton;
/*    */   protected MinimapProcessor minimap;
/*    */   
/*    */   public GuiMinimapSettings(IXaeroMinimap modMain, GuiScreen par1Screen) {
/* 16 */     super(modMain, par1Screen);
/* 17 */     this.minimap = modMain.getInterfaces().getMinimap();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void func_73866_w_() {
/* 23 */     super.func_73866_w_();
/* 24 */     this.screenTitle = I18n.func_135052_a("gui.xaero_minimap_settings", new Object[0]);
/* 25 */     func_189646_b(this.nextButton = new MyTinyButton(-1, this.field_146294_l / 2 + 80, this.field_146295_m / 7 + 144, I18n.func_135052_a("gui.xaero_next", new Object[0])));
/* 26 */     this.nextButton.field_146124_l = isNextButtonEnabled();
/* 27 */     func_189646_b(this.prevButton = new MyTinyButton(-1, this.field_146294_l / 2 - 155, this.field_146295_m / 7 + 144, I18n.func_135052_a("gui.xaero_previous", new Object[0])));
/* 28 */     this.prevButton.field_146124_l = isPrevButtonEnabled();
/*    */   }
/*    */ 
/*    */   
/*    */   public void func_73863_a(int par1, int par2, float par3) {
/* 33 */     super.func_73863_a(par1, par2, par3);
/* 34 */     if (!(this.modMain.getSettings()).mapSafeMode && this.minimap.getMinimapFBORenderer().isTriedFBO() && !this.minimap.getMinimapFBORenderer().isLoadedFBO()) {
/* 35 */       func_73732_a(this.field_146289_q, "§4You've been forced into safe mode! :(", this.field_146294_l / 2, 2, 16777215);
/* 36 */       func_73732_a(this.field_146289_q, "§cTurn off Video Settings - Performance - Fast Render.", this.field_146294_l / 2, 11, 16777215);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   protected void func_146284_a(GuiButton par1GuiButton) {
/* 42 */     super.func_146284_a(par1GuiButton);
/* 43 */     if (par1GuiButton.field_146124_l)
/* 44 */       if (par1GuiButton == this.nextButton) {
/* 45 */         onNextButton();
/* 46 */       } else if (par1GuiButton == this.prevButton) {
/* 47 */         onPrevButton();
/*    */       }  
/*    */   }
/*    */   
/*    */   protected abstract boolean isNextButtonEnabled();
/*    */   
/*    */   protected abstract boolean isPrevButtonEnabled();
/*    */   
/*    */   protected abstract void onNextButton();
/*    */   
/*    */   protected abstract void onPrevButton();
/*    */ }


/* Location:              C:\Users\ioanm\Downloads\Xaeros_Minimap_20.15.3_Forge_1.12.jar!\xaero\common\gui\GuiMinimapSettings.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */