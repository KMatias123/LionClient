/*    */ package xaero.common.gui.widget;
/*    */ 
/*    */ public class ButtonWidgetBuilder
/*    */   extends WidgetBuilder {
/*    */   protected String buttonText;
/*    */   protected int buttonW;
/*    */   protected int buttonH;
/*    */   
/*    */   public void setButtonText(String buttonText) {
/* 10 */     this.buttonText = buttonText;
/*    */   }
/*    */   
/*    */   public void setButtonW(int buttonW) {
/* 14 */     this.buttonW = buttonW;
/*    */   }
/*    */   
/*    */   public void setButtonH(int buttonH) {
/* 18 */     this.buttonH = buttonH;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean validate() {
/* 23 */     return (super.validate() && this.buttonText != null && this.buttonW > 0 && this.buttonH > 0);
/*    */   }
/*    */ 
/*    */   
/*    */   public Widget build() {
/* 28 */     return new ButtonWidget(this.location, this.horizontalAnchor, this.verticalAnchor, this.onClick, this.onHover, this.x, this.y, this.url, this.tooltip, this.buttonText, this.buttonW, this.buttonH);
/*    */   }
/*    */ }


/* Location:              C:\Users\ioanm\Downloads\Xaeros_Minimap_20.15.3_Forge_1.12.jar!\xaero\common\gui\widget\ButtonWidgetBuilder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */