/*   */ package xaero.common.gui.widget;
/*   */ 
/*   */ import net.minecraft.client.gui.GuiButton;
/*   */ 
/*   */ public abstract class GuiWidgetButton
/*   */   extends GuiButton {
/*   */   public GuiWidgetButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
/* 8 */     super(buttonId, x, y, widthIn, heightIn, buttonText);
/*   */   }
/*   */   
/*   */   public abstract void onClick(WidgetScreen paramWidgetScreen);
/*   */ }


/* Location:              C:\Users\ioanm\Downloads\Xaeros_Minimap_20.15.3_Forge_1.12.jar!\xaero\common\gui\widget\GuiWidgetButton.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */