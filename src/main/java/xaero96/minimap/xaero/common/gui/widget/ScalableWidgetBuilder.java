/*    */ package xaero.common.gui.widget;
/*    */ 
/*    */ public abstract class ScalableWidgetBuilder
/*    */   extends WidgetBuilder {
/*  5 */   protected double scale = 1.0D;
/*    */   protected int scaledOffsetX;
/*    */   protected int scaledOffsetY;
/*    */   protected boolean noGuiScale;
/*    */   
/*    */   public void setScale(double scale) {
/* 11 */     this.scale = scale;
/*    */   }
/*    */   public void setScaledOffsetX(int scaledOffsetX) {
/* 14 */     this.scaledOffsetX = scaledOffsetX;
/*    */   }
/*    */   
/*    */   public void setScaledOffsetY(int scaledOffsetY) {
/* 18 */     this.scaledOffsetY = scaledOffsetY;
/*    */   }
/*    */   
/*    */   public void setNoGuiScale(boolean noGuiScale) {
/* 22 */     this.noGuiScale = noGuiScale;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean validate() {
/* 27 */     return (super.validate() && this.scale != 0.0D);
/*    */   }
/*    */ }


/* Location:              C:\Users\ioanm\Downloads\Xaeros_Minimap_20.15.3_Forge_1.12.jar!\xaero\common\gui\widget\ScalableWidgetBuilder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */