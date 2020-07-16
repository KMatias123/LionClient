/*    */ package xaero.common.gui.widget.loader;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.util.Map;
/*    */ import xaero.common.gui.widget.Alignment;
/*    */ import xaero.common.gui.widget.TextWidgetBuilder;
/*    */ import xaero.common.gui.widget.Widget;
/*    */ import xaero.common.gui.widget.WidgetBuilder;
/*    */ 
/*    */ public class TextWidgetLoader
/*    */   extends ScalableWidgetLoader
/*    */ {
/*    */   public Widget load(Map<String, String> parsedArgs) throws IOException {
/* 14 */     TextWidgetBuilder builder = new TextWidgetBuilder();
/* 15 */     commonLoad((WidgetBuilder)builder, parsedArgs);
/* 16 */     String text = parsedArgs.get("text");
/* 17 */     String alignment = parsedArgs.get("alignment");
/* 18 */     if (text != null)
/* 19 */       builder.setText(text.replace("%semi%", ";")); 
/* 20 */     if (alignment != null)
/* 21 */       builder.setAlignment(Alignment.valueOf(alignment)); 
/* 22 */     if (builder.validate())
/* 23 */       return builder.build(); 
/* 24 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\ioanm\Downloads\Xaeros_Minimap_20.15.3_Forge_1.12.jar!\xaero\common\gui\widget\loader\TextWidgetLoader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */