/*    */ package xaero.common.gui.widget.render;
/*    */ 
/*    */ import net.minecraft.client.gui.Gui;
/*    */ import net.minecraft.client.renderer.GlStateManager;
/*    */ import xaero.common.gui.widget.ImageWidget;
/*    */ import xaero.common.gui.widget.ScalableWidget;
/*    */ 
/*    */ public class ImageWidgetRenderer
/*    */   extends ScalableWidgetRenderer<ImageWidget>
/*    */ {
/*    */   protected void renderScaled(int width, int height, int mouseX, int mouseY, double guiScale, ImageWidget widget) {
/* 12 */     GlStateManager.func_179098_w();
/* 13 */     GlStateManager.func_179144_i(widget.getGlTexture());
/* 14 */     GlStateManager.func_179147_l();
/* 15 */     Gui.func_146110_a(0, 0, 0.0F, 0.0F, widget.getW(), widget.getH(), widget.getW(), widget.getH());
/* 16 */     GlStateManager.func_179144_i(0);
/*    */   }
/*    */ }


/* Location:              C:\Users\ioanm\Downloads\Xaeros_Minimap_20.15.3_Forge_1.12.jar!\xaero\common\gui\widget\render\ImageWidgetRenderer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */