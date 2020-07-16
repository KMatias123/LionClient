/*    */ package xaero.common.gui.widget;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import xaero.common.gui.widget.loader.WidgetLoader;
/*    */ import xaero.patreon.Patreon4;
/*    */ 
/*    */ 
/*    */ public class WidgetLoadingHandler
/*    */ {
/* 11 */   private static int CURRENT_VERSION = 1;
/*    */   private WidgetScreenHandler handler;
/*    */   
/*    */   public WidgetLoadingHandler(WidgetScreenHandler destination) {
/* 15 */     this.handler = destination;
/*    */   }
/*    */   
/*    */   public void loadWidget(String serialized) {
/* 19 */     Widget widget = null;
/* 20 */     String[] args = serialized.split(";");
/* 21 */     Map<String, String> parsedArgs = new HashMap<>();
/* 22 */     for (String arg : args) {
/* 23 */       int splitIndex = arg.indexOf(':');
/* 24 */       if (splitIndex != -1) {
/*    */         
/* 26 */         String parameter = arg.substring(0, splitIndex);
/* 27 */         String value = arg.substring(splitIndex + 1);
/* 28 */         parsedArgs.put(parameter, value);
/*    */       } 
/*    */     }  try {
/* 31 */       String min_version = parsedArgs.remove("min_version");
/* 32 */       String max_version = parsedArgs.remove("max_version");
/* 33 */       if ((min_version != null && CURRENT_VERSION < Integer.parseInt(min_version)) || (max_version != null && CURRENT_VERSION > Integer.parseInt(max_version)))
/*    */         return; 
/* 35 */       String min_patronage = parsedArgs.remove("min_patronage");
/* 36 */       String max_patronage = parsedArgs.remove("max_patronage");
/* 37 */       if ((min_patronage != null && Patreon4.patronPledge < Integer.parseInt(min_patronage)) || (max_patronage != null && Patreon4.patronPledge > Integer.parseInt(max_patronage)))
/*    */         return; 
/* 39 */       WidgetType type = WidgetType.valueOf(parsedArgs.remove("type"));
/* 40 */       WidgetLoader loader = type.widgetLoader;
/* 41 */       widget = loader.load(parsedArgs);
/* 42 */       this.handler.addWidget(widget);
/* 43 */     } catch (Throwable t) {
/* 44 */       t.printStackTrace();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\ioanm\Downloads\Xaeros_Minimap_20.15.3_Forge_1.12.jar!\xaero\common\gui\widget\WidgetLoadingHandler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */