/*    */ package xaero.common.minimap.render;
/*    */ 
/*    */ import java.nio.ByteBuffer;
/*    */ import net.minecraft.client.renderer.BufferBuilder;
/*    */ import net.minecraft.client.renderer.GlStateManager;
/*    */ import net.minecraft.client.renderer.Tessellator;
/*    */ import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
/*    */ import org.lwjgl.opengl.GL11;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MinimapRendererHelper
/*    */ {
/*    */   void drawMyTexturedModalRect(float x, float y, int textureX, int textureY, float width, float height, float factor) {
/* 15 */     drawMyTexturedModalRect(x, y, textureX, textureY, width, height, height, factor);
/*    */   }
/*    */ 
/*    */   
/*    */   void drawMyTexturedModalRect(float x, float y, int textureX, int textureY, float width, float height, float theight, float factor) {
/* 20 */     float f = 1.0F / factor;
/* 21 */     float f1 = f;
/* 22 */     Tessellator tessellator = Tessellator.func_178181_a();
/* 23 */     BufferBuilder vertexBuffer = tessellator.func_178180_c();
/* 24 */     vertexBuffer.func_181668_a(7, DefaultVertexFormats.field_181707_g);
/* 25 */     vertexBuffer.func_181662_b((x + 0.0F), (y + height), 0.0D)
/* 26 */       .func_187315_a(((textureX + 0) * f), ((textureY + 0) * f1)).func_181675_d();
/* 27 */     vertexBuffer.func_181662_b((x + width), (y + height), 0.0D)
/* 28 */       .func_187315_a(((textureX + width) * f), ((textureY + 0) * f1))
/* 29 */       .func_181675_d();
/* 30 */     vertexBuffer.func_181662_b((x + width), (y + 0.0F), 0.0D)
/* 31 */       .func_187315_a(((textureX + width) * f), ((textureY + theight) * f1)).func_181675_d();
/* 32 */     vertexBuffer.func_181662_b((x + 0.0F), (y + 0.0F), 0.0D)
/* 33 */       .func_187315_a(((textureX + 0) * f), ((textureY + theight) * f1)).func_181675_d();
/* 34 */     tessellator.func_78381_a();
/*    */   }
/*    */   
/*    */   public void drawMyColoredRect(float x1, float y1, float x2, float y2) {
/* 38 */     Tessellator tessellator = Tessellator.func_178181_a();
/* 39 */     BufferBuilder vertexBuffer = tessellator.func_178180_c();
/* 40 */     vertexBuffer.func_181668_a(7, DefaultVertexFormats.field_181705_e);
/* 41 */     vertexBuffer.func_181662_b(x1, y2, 0.0D).func_181675_d();
/* 42 */     vertexBuffer.func_181662_b(x2, y2, 0.0D).func_181675_d();
/* 43 */     vertexBuffer.func_181662_b(x2, y1, 0.0D).func_181675_d();
/* 44 */     vertexBuffer.func_181662_b(x1, y1, 0.0D).func_181675_d();
/* 45 */     tessellator.func_78381_a();
/*    */   }
/*    */   
/*    */   void bindTextureBuffer(ByteBuffer image, int width, int height, int par0) {
/* 49 */     GlStateManager.func_179144_i(par0);
/* 50 */     GL11.glTexImage2D(3553, 0, 6407, width, height, 0, 6407, 5121, image);
/*    */   }
/*    */ 
/*    */   
/*    */   void putColor(byte[] bytes, int x, int y, int red, int green, int blue, int size) {
/* 55 */     int pixel = (y * size + x) * 3;
/* 56 */     bytes[pixel] = (byte)red;
/* 57 */     bytes[++pixel] = (byte)green;
/* 58 */     bytes[++pixel] = (byte)blue;
/*    */   }
/*    */   
/*    */   void gridOverlay(int[] result, int grid, int red, int green, int blue) {
/* 62 */     result[0] = (red * 3 + (grid >> 16 & 0xFF)) / 4;
/* 63 */     result[1] = (green * 3 + (grid >> 8 & 0xFF)) / 4;
/* 64 */     result[2] = (blue * 3 + (grid & 0xFF)) / 4;
/*    */   }
/*    */   
/*    */   void slimeOverlay(int[] result, int red, int green, int blue) {
/* 68 */     result[0] = (red + 82) / 2;
/* 69 */     result[1] = (green + 241) / 2;
/* 70 */     result[2] = (blue + 64) / 2;
/*    */   }
/*    */ }


/* Location:              C:\Users\ioanm\Downloads\Xaeros_Minimap_20.15.3_Forge_1.12.jar!\xaero\common\minimap\render\MinimapRendererHelper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */