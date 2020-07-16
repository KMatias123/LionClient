/*    */ package xaero.common.gui.widget.render;
/*    */ 
/*    */ import net.minecraft.client.renderer.GlStateManager;
/*    */ import xaero.common.gui.widget.ScalableWidget;
/*    */ import xaero.common.gui.widget.Widget;
/*    */ 
/*    */ public abstract class ScalableWidgetRenderer<T extends ScalableWidget>
/*    */   implements WidgetRenderer<T> {
/*    */   public void render(int width, int height, int mouseX, int mouseY, double guiScale, T widget) {
/* 10 */     GlStateManager.func_179094_E();
/* 11 */     GlStateManager.func_179109_b(widget.getX(width), widget.getY(height), 0.0F);
/* 12 */     if (widget.isNoGuiScale())
/* 13 */       GlStateManager.func_179139_a(1.0D / guiScale, 1.0D / guiScale, 1.0D); 
/* 14 */     GlStateManager.func_179139_a(widget.getScale(), widget.getScale(), 1.0D);
/* 15 */     GlStateManager.func_179109_b(widget.getScaledOffsetX(), widget.getScaledOffsetY(), 0.0F);
/* 16 */     renderScaled(width, height, mouseX, mouseY, guiScale, widget);
/* 17 */     GlStateManager.func_179121_F();
/*    */   }
/*    */   
/*    */   protected abstract void renderScaled(int paramInt1, int paramInt2, int paramInt3, int paramInt4, double paramDouble, T paramT);
/*    */ }


/* Location:              C:\Users\ioanm\Downloads\Xaeros_Minimap_20.15.3_Forge_1.12.jar!\xaero\common\gui\widget\render\ScalableWidgetRenderer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */