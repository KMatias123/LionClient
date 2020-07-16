/*    */ package xaero.common.gui;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.client.gui.GuiButton;
/*    */ import net.minecraft.client.gui.GuiYesNo;
/*    */ import net.minecraft.client.resources.I18n;
/*    */ import xaero.common.IXaeroMinimap;
/*    */ 
/*    */ public class GuiReset
/*    */   extends GuiYesNo
/*    */ {
/*    */   private IXaeroMinimap modMain;
/*    */   
/*    */   public GuiReset(IXaeroMinimap modMain) {
/* 15 */     super(null, I18n.func_135052_a("gui.xaero_reset_message", new Object[0]), I18n.func_135052_a("gui.xaero_reset_message2", new Object[0]), 0);
/* 16 */     this.modMain = modMain;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void func_146284_a(GuiButton button) throws IOException {
/* 21 */     switch (button.field_146127_k) {
/*    */       case 0:
/* 23 */         this.modMain.getInterfaces().getMinimap().setToResetImage(true);
/* 24 */         this.modMain.resetSettings();
/*    */       case 1:
/* 26 */         this.modMain.getGuiHelper().onResetCancel();
/*    */         break;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\ioanm\Downloads\Xaeros_Minimap_20.15.3_Forge_1.12.jar!\xaero\common\gui\GuiReset.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */