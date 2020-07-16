/*    */ package xaero.common.gui.widget;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import net.minecraft.client.gui.GuiScreen;
/*    */ import xaero.common.gui.widget.init.WidgetInitializer;
/*    */ import xaero.common.gui.widget.render.WidgetRenderer;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class WidgetScreenHandler
/*    */ {
/* 15 */   private List<Widget> widgets = new ArrayList<>();
/*    */ 
/*    */   
/*    */   void addWidget(Widget widget) {
/* 19 */     if (widget != null)
/* 20 */       this.widgets.add(widget); 
/*    */   }
/*    */   
/*    */   public void initialize(WidgetScreen screen, int width, int height) {
/* 24 */     for (Widget w : this.widgets) {
/* 25 */       if (w.getLocation().isAssignableFrom(screen.getClass())) {
/* 26 */         WidgetInitializer widgetInit = (w.getType()).widgetInit;
/* 27 */         if (widgetInit != null) {
/* 28 */           widgetInit.init(screen, width, height, w);
/*    */         }
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   public void render(WidgetScreen screen, int width, int height, int mouseX, int mouseY, double guiScale) {
/* 35 */     for (Widget w : this.widgets) {
/* 36 */       if (w.getLocation().isAssignableFrom(screen.getClass())) {
/*    */         
/* 38 */         WidgetRenderer renderer = (w.getType()).widgetRenderer;
/* 39 */         if (renderer != null)
/* 40 */           renderer.render(width, height, mouseX, mouseY, guiScale, w); 
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   public void renderTooltips(GuiScreen screen, int width, int height, int mouseX, int mouseY, double guiScale) {
/* 46 */     for (Widget w : this.widgets) {
/* 47 */       if (w.getLocation().isAssignableFrom(screen.getClass()))
/* 48 */         renderTooltip(width, height, mouseX, mouseY, guiScale, w); 
/*    */     } 
/*    */   }
/*    */   
/*    */   private void renderTooltip(int width, int height, int mouseX, int mouseY, double guiScale, Widget widget) {
/* 53 */     if (widget.getOnHover() != HoverAction.TOOLTIP || widget.getTooltip() == null)
/*    */       return; 
/* 55 */     int x = widget.getBoxX(width, guiScale);
/* 56 */     int y = widget.getBoxY(height, guiScale);
/* 57 */     int w = widget.getBoxW(guiScale);
/* 58 */     int h = widget.getBoxH(guiScale);
/* 59 */     if (mouseX >= x && mouseY >= y && mouseX < x + w && mouseY < y + h)
/* 60 */       widget.getCursorBox().drawBox(mouseX, mouseY, width, height); 
/*    */   }
/*    */   
/*    */   public void handleClick(GuiScreen screen, int width, int height, int mouseX, int mouseY, double guiScale) {
/* 64 */     for (Widget w : this.widgets) {
/* 65 */       if (w.getLocation().isAssignableFrom(screen.getClass()))
/* 66 */         handleWidgetClick(screen, width, height, mouseX, mouseY, guiScale, w); 
/*    */     } 
/*    */   }
/*    */   
/*    */   private void handleWidgetClick(GuiScreen screen, int width, int height, int mouseX, int mouseY, double guiScale, Widget widget) {
/* 71 */     if (widget.getOnClick() == ClickAction.NOTHING || widget.getType() == WidgetType.BUTTON)
/*    */       return; 
/* 73 */     int x = widget.getBoxX(width, guiScale);
/* 74 */     int y = widget.getBoxY(height, guiScale);
/* 75 */     int w = widget.getBoxW(guiScale);
/* 76 */     int h = widget.getBoxH(guiScale);
/* 77 */     if (mouseX >= x && mouseY >= y && mouseX < x + w && mouseY < y + h) {
/* 78 */       WidgetClickHandler clickHandler = (widget.getOnClick()).clickHandler;
/* 79 */       if (clickHandler != null)
/* 80 */         clickHandler.onClick(screen, widget); 
/*    */     } 
/*    */   }
/*    */   
/*    */   public void handleButton(WidgetScreen screen, GuiWidgetButton button) {
/* 85 */     button.onClick(screen);
/*    */   }
/*    */ }


/* Location:              C:\Users\ioanm\Downloads\Xaeros_Minimap_20.15.3_Forge_1.12.jar!\xaero\common\gui\widget\WidgetScreenHandler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */