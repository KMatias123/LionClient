/*    */ package xaero.common.gui.widget.init;
/*    */ 
/*    */ import net.minecraft.client.gui.GuiButton;
/*    */ import net.minecraft.client.gui.GuiScreen;
/*    */ import xaero.common.gui.widget.ButtonWidget;
/*    */ import xaero.common.gui.widget.GuiWidgetButton;
/*    */ import xaero.common.gui.widget.Widget;
/*    */ import xaero.common.gui.widget.WidgetScreen;
/*    */ 
/*    */ public class ButtonWidgetInitializer
/*    */   implements WidgetInitializer {
/*    */   public void init(WidgetScreen screen, int width, int height, final Widget widget) {
/* 13 */     ButtonWidget buttonWidget = (ButtonWidget)widget;
/* 14 */     screen.addButtonVisible((GuiButton)new GuiWidgetButton(-1, widget.getX(width), widget.getY(height), buttonWidget.getW(), buttonWidget.getH(), buttonWidget.getButtonText())
/*    */         {
/*    */           public void onClick(WidgetScreen screen) {
/* 17 */             (widget.getOnClick()).clickHandler.onClick(ButtonWidgetInitializer.this.toScreen(screen), widget);
/*    */           }
/*    */         });
/*    */   }
/*    */ 
/*    */   
/*    */   private GuiScreen toScreen(WidgetScreen screen) {
/* 24 */     GuiScreen result = screen.getScreen();
/* 25 */     if (result == screen)
/* 26 */       return result; 
/* 27 */     throw new RuntimeException("Incorrect usage of " + getClass());
/*    */   }
/*    */ }


/* Location:              C:\Users\ioanm\Downloads\Xaeros_Minimap_20.15.3_Forge_1.12.jar!\xaero\common\gui\widget\init\ButtonWidgetInitializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */