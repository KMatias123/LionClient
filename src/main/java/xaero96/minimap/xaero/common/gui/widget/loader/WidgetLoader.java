/*    */ package xaero.common.gui.widget.loader;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.util.Map;
/*    */ import xaero.common.gui.GuiSettings;
/*    */ import xaero.common.gui.GuiUpdate;
/*    */ import xaero.common.gui.widget.ClickAction;
/*    */ import xaero.common.gui.widget.HoverAction;
/*    */ import xaero.common.gui.widget.Widget;
/*    */ import xaero.common.gui.widget.WidgetBuilder;
/*    */ 
/*    */ 
/*    */ public abstract class WidgetLoader
/*    */ {
/*    */   public abstract Widget load(Map<String, String> paramMap) throws IOException;
/*    */   
/*    */   protected void commonLoad(WidgetBuilder builder, Map<String, String> parsedArgs) {
/* 18 */     String location = parsedArgs.get("location");
/* 19 */     String anchor_hor = parsedArgs.get("anchor_hor");
/* 20 */     String anchor_vert = parsedArgs.get("anchor_vert");
/* 21 */     String on_click = parsedArgs.get("on_click");
/* 22 */     String on_hover = parsedArgs.get("on_hover");
/* 23 */     String x = parsedArgs.get("x");
/* 24 */     String y = parsedArgs.get("y");
/* 25 */     String url = parsedArgs.get("url");
/* 26 */     String tooltip = parsedArgs.get("tooltip");
/* 27 */     if (location != null)
/* 28 */       if (location.equals("SETTINGS")) {
/* 29 */         builder.setLocation(GuiSettings.class);
/* 30 */       } else if (location.equals("UPDATE")) {
/* 31 */         builder.setLocation(GuiUpdate.class);
/*    */       }  
/* 33 */     if (anchor_hor != null)
/* 34 */       builder.setHorizontalAnchor(Float.parseFloat(anchor_hor)); 
/* 35 */     if (anchor_vert != null)
/* 36 */       builder.setVerticalAnchor(Float.parseFloat(anchor_vert)); 
/* 37 */     if (on_click != null)
/* 38 */       builder.setOnClick(ClickAction.valueOf(on_click)); 
/* 39 */     if (on_hover != null)
/* 40 */       builder.setOnHover(HoverAction.valueOf(on_hover)); 
/* 41 */     if (x != null)
/* 42 */       builder.setX(Integer.parseInt(x)); 
/* 43 */     if (y != null)
/* 44 */       builder.setY(Integer.parseInt(y)); 
/* 45 */     if (url != null)
/* 46 */       builder.setUrl(url.replace("%semi%", ";")); 
/* 47 */     if (tooltip != null)
/* 48 */       builder.setTooltip(tooltip.replace("%semi%", ";")); 
/*    */   }
/*    */ }


/* Location:              C:\Users\ioanm\Downloads\Xaeros_Minimap_20.15.3_Forge_1.12.jar!\xaero\common\gui\widget\loader\WidgetLoader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */