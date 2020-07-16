/*    */ package xaero.common.misc;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.FontRenderer;
/*    */ import net.minecraft.client.resources.IResource;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ 
/*    */ 
/*    */ public abstract class CustomTextFieldFontRendererWrapper
/*    */   extends FontRenderer
/*    */ {
/*    */   private FontRenderer actualRenderer;
/* 15 */   private static final IResource placeholderResource = new IResource() {
/* 16 */       private final InputStream placeholderInputStream = new InputStream()
/*    */         {
/*    */           public int read() throws IOException
/*    */           {
/* 20 */             return 0;
/*    */           }
/*    */           
/*    */           public int read(byte[] bytes) throws IOException {
/* 24 */             return 0;
/*    */           }
/*    */           
/*    */           public int read(byte[] bytes, int a, int b) throws IOException {
/* 28 */             return 0;
/*    */           }
/*    */         };
/*    */ 
/*    */ 
/*    */       
/*    */       public void close() throws IOException {}
/*    */ 
/*    */ 
/*    */       
/*    */       public ResourceLocation func_177241_a() {
/* 39 */         return null;
/*    */       }
/*    */ 
/*    */       
/*    */       public InputStream func_110527_b() {
/* 44 */         return this.placeholderInputStream;
/*    */       }
/*    */ 
/*    */       
/*    */       public boolean func_110528_c() {
/* 49 */         return false;
/*    */       }
/*    */ 
/*    */       
/*    */       public <T extends net.minecraft.client.resources.data.IMetadataSection> T func_110526_a(String sectionName) {
/* 54 */         return null;
/*    */       }
/*    */ 
/*    */       
/*    */       public String func_177240_d() {
/* 59 */         return null;
/*    */       }
/*    */     };
/*    */ 
/*    */   
/*    */   public CustomTextFieldFontRendererWrapper(Minecraft mc) {
/* 65 */     super(mc.field_71474_y, null, null, false);
/* 66 */     this.actualRenderer = mc.field_71466_p;
/*    */   }
/*    */ 
/*    */   
/*    */   public abstract String censor(String paramString);
/*    */   
/*    */   public String func_78269_a(String text, int width) {
/* 73 */     return this.actualRenderer.func_78269_a(censor(text), width);
/*    */   }
/*    */ 
/*    */   
/*    */   public String func_78262_a(String text, int width, boolean reverse) {
/* 78 */     return this.actualRenderer.func_78262_a(censor(text), width, reverse);
/*    */   }
/*    */ 
/*    */   
/*    */   public int func_175063_a(String text, float x, float y, int color) {
/* 83 */     return this.actualRenderer.func_175063_a(censor(text), x, y, color);
/*    */   }
/*    */ 
/*    */   
/*    */   public int func_78256_a(String text) {
/* 88 */     return this.actualRenderer.func_78256_a(censor(text));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void bindTexture(ResourceLocation location) {}
/*    */ 
/*    */ 
/*    */   
/*    */   protected IResource getResource(ResourceLocation location) throws IOException {
/* 99 */     return placeholderResource;
/*    */   }
/*    */ }


/* Location:              C:\Users\ioanm\Downloads\Xaeros_Minimap_20.15.3_Forge_1.12.jar!\xaero\common\misc\CustomTextFieldFontRendererWrapper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */