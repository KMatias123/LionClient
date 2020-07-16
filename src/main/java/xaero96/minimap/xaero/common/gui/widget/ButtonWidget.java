/*    */ package xaero.common.gui.widget;
/*    */ 
/*    */ import net.minecraft.client.gui.GuiScreen;
/*    */ 
/*    */ 
/*    */ public class ButtonWidget
/*    */   extends Widget
/*    */ {
/*    */   private String buttonText;
/*    */   private int buttonW;
/*    */   private int buttonH;
/*    */   
/*    */   public ButtonWidget(Class<? extends GuiScreen> location, float horizontalAnchor, float verticalAnchor, ClickAction onClick, HoverAction onHover, int x, int y, String url, String tooltip, String buttonText, int buttonW, int buttonH) {
/* 14 */     super(WidgetType.BUTTON, location, horizontalAnchor, verticalAnchor, onClick, onHover, x, y, url, tooltip);
/* 15 */     this.buttonText = buttonText;
/* 16 */     this.buttonW = buttonW;
/* 17 */     this.buttonH = buttonH;
/*    */   }
/*    */   
/*    */   public String getButtonText() {
/* 21 */     return this.buttonText;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getW() {
/* 26 */     return this.buttonW;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getH() {
/* 31 */     return this.buttonH;
/*    */   }
/*    */ }


/* Location:              C:\Users\ioanm\Downloads\Xaeros_Minimap_20.15.3_Forge_1.12.jar!\xaero\common\gui\widget\ButtonWidget.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */