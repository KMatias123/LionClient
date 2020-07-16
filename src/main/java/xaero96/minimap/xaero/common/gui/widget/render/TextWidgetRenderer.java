/*    */ package xaero.common.gui.widget.render;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import xaero.common.gui.widget.ScalableWidget;
/*    */ import xaero.common.gui.widget.TextWidget;
/*    */ 
/*    */ public class TextWidgetRenderer
/*    */   extends ScalableWidgetRenderer<TextWidget> {
/*    */   protected void renderScaled(int width, int height, int mouseX, int mouseY, double guiScale, TextWidget widget) {
/* 10 */     (Minecraft.func_71410_x()).field_71466_p.func_78276_b(widget.getText(), 0, 0, 16777215);
/*    */   }
/*    */ }


/* Location:              C:\Users\ioanm\Downloads\Xaeros_Minimap_20.15.3_Forge_1.12.jar!\xaero\common\gui\widget\render\TextWidgetRenderer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */