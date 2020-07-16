/*    */ package xaero.common.gui.widget.loader;
/*    */ 
/*    */ import java.util.Map;
/*    */ import xaero.common.gui.widget.ButtonWidgetBuilder;
/*    */ import xaero.common.gui.widget.Widget;
/*    */ import xaero.common.gui.widget.WidgetBuilder;
/*    */ 
/*    */ public class ButtonWidgetLoader
/*    */   extends WidgetLoader
/*    */ {
/*    */   public Widget load(Map<String, String> parsedArgs) {
/* 12 */     ButtonWidgetBuilder builder = new ButtonWidgetBuilder();
/* 13 */     commonLoad((WidgetBuilder)builder, parsedArgs);
/* 14 */     String button_text = parsedArgs.get("button_text");
/* 15 */     String button_w = parsedArgs.get("button_w");
/* 16 */     String button_h = parsedArgs.get("button_h");
/* 17 */     if (button_text != null)
/* 18 */       builder.setButtonText(button_text.replace("%semi%", ";")); 
/* 19 */     if (button_w != null)
/* 20 */       builder.setButtonW(Integer.parseInt(button_w)); 
/* 21 */     if (button_h != null)
/* 22 */       builder.setButtonH(Integer.parseInt(button_h)); 
/* 23 */     if (builder.validate())
/* 24 */       return builder.build(); 
/* 25 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\ioanm\Downloads\Xaeros_Minimap_20.15.3_Forge_1.12.jar!\xaero\common\gui\widget\loader\ButtonWidgetLoader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */