/*    */ package xaero.common.gui.widget;
/*    */ 
/*    */ public class TextWidgetBuilder
/*    */   extends ScalableWidgetBuilder {
/*    */   private String text;
/*  6 */   private Alignment alignment = Alignment.LEFT;
/*    */   
/*    */   public void setText(String text) {
/*  9 */     this.text = text;
/*    */   }
/*    */   
/*    */   public void setAlignment(Alignment alignment) {
/* 13 */     this.alignment = alignment;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean validate() {
/* 18 */     return (super.validate() && this.text != null && this.alignment != null);
/*    */   }
/*    */ 
/*    */   
/*    */   public Widget build() {
/* 23 */     return new TextWidget(this.location, this.horizontalAnchor, this.verticalAnchor, this.onClick, this.onHover, this.x, this.y, this.scaledOffsetX, this.scaledOffsetY, this.url, this.tooltip, this.text, this.alignment, this.noGuiScale, this.scale);
/*    */   }
/*    */ }


/* Location:              C:\Users\ioanm\Downloads\Xaeros_Minimap_20.15.3_Forge_1.12.jar!\xaero\common\gui\widget\TextWidgetBuilder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */