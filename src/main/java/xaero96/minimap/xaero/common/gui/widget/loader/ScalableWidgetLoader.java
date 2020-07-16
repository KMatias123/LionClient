/*    */ package xaero.common.gui.widget.loader;
/*    */ 
/*    */ import java.util.Map;
/*    */ import xaero.common.gui.widget.ScalableWidgetBuilder;
/*    */ import xaero.common.gui.widget.WidgetBuilder;
/*    */ 
/*    */ 
/*    */ public abstract class ScalableWidgetLoader
/*    */   extends WidgetLoader
/*    */ {
/*    */   protected void commonLoad(WidgetBuilder builder, Map<String, String> parsedArgs) {
/* 12 */     String scale = parsedArgs.get("scale");
/* 13 */     String no_gui_scale = parsedArgs.get("no_gui_scale");
/* 14 */     String scaled_offset_x = parsedArgs.get("scaled_offset_x");
/* 15 */     String scaled_offset_y = parsedArgs.get("scaled_offset_y");
/* 16 */     if (scale != null)
/* 17 */       ((ScalableWidgetBuilder)builder).setScale(Double.parseDouble(scale)); 
/* 18 */     if (no_gui_scale != null)
/* 19 */       ((ScalableWidgetBuilder)builder).setNoGuiScale(no_gui_scale.equals("true")); 
/* 20 */     if (scaled_offset_x != null)
/* 21 */       ((ScalableWidgetBuilder)builder).setScaledOffsetX(Integer.parseInt(scaled_offset_x)); 
/* 22 */     if (scaled_offset_y != null)
/* 23 */       ((ScalableWidgetBuilder)builder).setScaledOffsetY(Integer.parseInt(scaled_offset_y)); 
/* 24 */     super.commonLoad(builder, parsedArgs);
/*    */   }
/*    */ }


/* Location:              C:\Users\ioanm\Downloads\Xaeros_Minimap_20.15.3_Forge_1.12.jar!\xaero\common\gui\widget\loader\ScalableWidgetLoader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */