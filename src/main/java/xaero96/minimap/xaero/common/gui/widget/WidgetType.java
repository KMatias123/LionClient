/*    */ package xaero.common.gui.widget;
/*    */ 
/*    */ import xaero.common.gui.widget.init.ButtonWidgetInitializer;
/*    */ import xaero.common.gui.widget.init.WidgetInitializer;
/*    */ import xaero.common.gui.widget.loader.ButtonWidgetLoader;
/*    */ import xaero.common.gui.widget.loader.ImageWidgetLoader;
/*    */ import xaero.common.gui.widget.loader.TextWidgetLoader;
/*    */ import xaero.common.gui.widget.loader.WidgetLoader;
/*    */ import xaero.common.gui.widget.render.ImageWidgetRenderer;
/*    */ import xaero.common.gui.widget.render.TextWidgetRenderer;
/*    */ import xaero.common.gui.widget.render.WidgetRenderer;
/*    */ 
/*    */ public enum WidgetType
/*    */ {
/* 15 */   IMAGE((WidgetLoader)new ImageWidgetLoader(), (WidgetRenderer)new ImageWidgetRenderer(), null),
/* 16 */   BUTTON((WidgetLoader)new ButtonWidgetLoader(), null, (WidgetInitializer)new ButtonWidgetInitializer()),
/* 17 */   TEXT((WidgetLoader)new TextWidgetLoader(), (WidgetRenderer)new TextWidgetRenderer(), null);
/*    */   
/*    */   public final WidgetLoader widgetLoader;
/*    */   
/*    */   public final WidgetRenderer widgetRenderer;
/*    */   
/*    */   public final WidgetInitializer widgetInit;
/*    */   
/*    */   WidgetType(WidgetLoader widgetLoader, WidgetRenderer widgetRenderer, WidgetInitializer widgetInit) {
/* 26 */     this.widgetLoader = widgetLoader;
/* 27 */     this.widgetRenderer = widgetRenderer;
/* 28 */     this.widgetInit = widgetInit;
/*    */   }
/*    */ }


/* Location:              C:\Users\ioanm\Downloads\Xaeros_Minimap_20.15.3_Forge_1.12.jar!\xaero\common\gui\widget\WidgetType.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */