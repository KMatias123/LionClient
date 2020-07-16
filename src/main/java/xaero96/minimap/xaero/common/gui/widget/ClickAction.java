/*    */ package xaero.common.gui.widget;
/*    */ 
/*    */ public enum ClickAction
/*    */ {
/*  5 */   NOTHING(null),
/*  6 */   URL(new WidgetUrlClickHandler());
/*    */   
/*    */   public final WidgetClickHandler clickHandler;
/*    */   
/*    */   ClickAction(WidgetClickHandler clickHandler) {
/* 11 */     this.clickHandler = clickHandler;
/*    */   }
/*    */ }


/* Location:              C:\Users\ioanm\Downloads\Xaeros_Minimap_20.15.3_Forge_1.12.jar!\xaero\common\gui\widget\ClickAction.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */