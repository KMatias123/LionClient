/*    */ package xaero.common.gui;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.client.gui.GuiButton;
/*    */ import net.minecraft.client.gui.GuiScreen;
/*    */ import net.minecraft.client.resources.I18n;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class GuiInstructions
/*    */   extends GuiScreen
/*    */ {
/*    */   private GuiScreen parentGuiScreen;
/*    */   protected String screenTitle;
/*    */   
/*    */   public GuiInstructions(GuiScreen par1GuiScreen) {
/* 18 */     this.parentGuiScreen = par1GuiScreen;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void func_73866_w_() {
/* 26 */     this.screenTitle = I18n.func_135052_a("gui.xaero_instructions", new Object[0]);
/* 27 */     this.field_146292_n.clear();
/* 28 */     this.field_146292_n.add(new GuiButton(200, this.field_146294_l / 2 - 100, this.field_146295_m / 6 + 168, I18n.func_135052_a("gui.xaero_OK", new Object[0])));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void func_146284_a(GuiButton par1GuiButton) {
/* 37 */     if (par1GuiButton.field_146124_l)
/*    */     {
/* 39 */       if (par1GuiButton.field_146127_k == 200)
/*    */       {
/* 41 */         this.field_146297_k.func_147108_a(this.parentGuiScreen);
/*    */       }
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public List getButtons() {
/* 48 */     return this.field_146292_n;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void func_73863_a(int par1, int par2, float par3) {
/* 56 */     func_146276_q_();
/* 57 */     func_73732_a(this.field_146289_q, this.screenTitle, this.field_146294_l / 2, 20, 16777215);
/* 58 */     func_73732_a(this.field_146289_q, I18n.func_135052_a("gui.xaero_howto_select", new Object[0]), this.field_146294_l / 2, this.field_146295_m / 7 + 10, 16777215);
/* 59 */     func_73732_a(this.field_146289_q, I18n.func_135052_a("gui.xaero_howto_drag", new Object[0]), this.field_146294_l / 2, this.field_146295_m / 7 + 21, 16777215);
/* 60 */     func_73732_a(this.field_146289_q, I18n.func_135052_a("gui.xaero_howto_deselect", new Object[0]), this.field_146294_l / 2, this.field_146295_m / 7 + 32, 16777215);
/* 61 */     func_73732_a(this.field_146289_q, I18n.func_135052_a("gui.xaero_howto_center", new Object[0]), this.field_146294_l / 2, this.field_146295_m / 7 + 43, 16777215);
/* 62 */     func_73732_a(this.field_146289_q, I18n.func_135052_a("gui.xaero_different_centered", new Object[0]), this.field_146294_l / 2, this.field_146295_m / 7 + 54, 16777215);
/* 63 */     func_73732_a(this.field_146289_q, I18n.func_135052_a("gui.xaero_howto_flip", new Object[0]), this.field_146294_l / 2, this.field_146295_m / 7 + 65, 16777215);
/* 64 */     func_73732_a(this.field_146289_q, I18n.func_135052_a("gui.xaero_howto_settings", new Object[0]), this.field_146294_l / 2, this.field_146295_m / 7 + 76, 16777215);
/* 65 */     func_73732_a(this.field_146289_q, I18n.func_135052_a("gui.xaero_howto_preset", new Object[0]), this.field_146294_l / 2, this.field_146295_m / 7 + 87, 16777215);
/* 66 */     func_73732_a(this.field_146289_q, I18n.func_135052_a("gui.xaero_howto_save", new Object[0]), this.field_146294_l / 2, this.field_146295_m / 7 + 98, 16777215);
/* 67 */     func_73732_a(this.field_146289_q, I18n.func_135052_a("gui.xaero_howto_cancel", new Object[0]), this.field_146294_l / 2, this.field_146295_m / 7 + 109, 16777215);
/* 68 */     super.func_73863_a(par1, par2, par3);
/*    */   }
/*    */ }


/* Location:              C:\Users\ioanm\Downloads\Xaeros_Minimap_20.15.3_Forge_1.12.jar!\xaero\common\gui\GuiInstructions.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */