/*    */ package xaero.common.graphics;
/*    */ 
/*    */ import java.awt.image.BufferedImage;
/*    */ import java.nio.ByteBuffer;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.renderer.texture.SimpleTexture;
/*    */ import net.minecraft.client.renderer.texture.TextureUtil;
/*    */ import net.minecraft.client.resources.IResourceManager;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ import org.lwjgl.BufferUtils;
/*    */ 
/*    */ 
/*    */ public class MinimapTexture
/*    */   extends SimpleTexture
/*    */ {
/*    */   public BufferedImage bufferedimage;
/* 17 */   public ByteBuffer buffer512 = BufferUtils.createByteBuffer(1048576);
/* 18 */   public ByteBuffer buffer256 = BufferUtils.createByteBuffer(262144);
/* 19 */   public ByteBuffer buffer128 = BufferUtils.createByteBuffer(65536);
/*    */   
/*    */   public ByteBuffer getBuffer(int size) {
/* 22 */     switch (size) {
/*    */       case 128:
/* 24 */         return this.buffer128;
/*    */       case 256:
/* 26 */         return this.buffer256;
/*    */     } 
/* 28 */     return this.buffer512;
/*    */   }
/*    */   
/*    */   public MinimapTexture(ResourceLocation location) {
/* 32 */     super(location);
/* 33 */     func_110551_a(Minecraft.func_71410_x().func_110442_L());
/*    */   }
/*    */   
/*    */   public void func_110551_a(IResourceManager par1ResourceManager) {
/* 37 */     this.bufferedimage = new BufferedImage(512, 512, 5);
/* 38 */     TextureUtil.func_110989_a(func_110552_b(), this.bufferedimage, false, false);
/*    */   }
/*    */ }


/* Location:              C:\Users\ioanm\Downloads\Xaeros_Minimap_20.15.3_Forge_1.12.jar!\xaero\common\graphics\MinimapTexture.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */