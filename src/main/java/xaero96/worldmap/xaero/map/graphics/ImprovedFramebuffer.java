/*     */ package xaero.map.graphics;
/*     */ 
/*     */ import java.nio.IntBuffer;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.texture.TextureUtil;
/*     */ import net.minecraft.client.shader.Framebuffer;
/*     */ import org.lwjgl.opengl.ARBFramebufferObject;
/*     */ import org.lwjgl.opengl.EXTFramebufferObject;
/*     */ import org.lwjgl.opengl.GL30;
/*     */ import org.lwjgl.opengl.GLContext;
/*     */ 
/*     */ 
/*     */ public class ImprovedFramebuffer
/*     */   extends Framebuffer
/*     */ {
/*     */   private int type;
/*     */   private static final int GL_FB_INCOMPLETE_ATTACHMENT = 36054;
/*     */   private static final int GL_FB_INCOMPLETE_MISS_ATTACH = 36055;
/*     */   private static final int GL_FB_INCOMPLETE_DRAW_BUFFER = 36059;
/*     */   private static final int GL_FB_INCOMPLETE_READ_BUFFER = 36060;
/*     */   
/*     */   public ImprovedFramebuffer(int width, int height, boolean useDepthIn) {
/*  23 */     super(width, height, useDepthIn);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void func_147613_a(int width, int height) {
/*  29 */     GlStateManager.func_179126_j();
/*     */     
/*  31 */     if (this.field_147616_f >= 0)
/*     */     {
/*  33 */       func_147608_a();
/*     */     }
/*     */     
/*  36 */     func_147605_b(width, height);
/*  37 */     func_147611_b();
/*  38 */     bindFramebuffer(this.type, 36160, 0);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void func_147605_b(int width, int height) {
/*  44 */     this.field_147621_c = width;
/*  45 */     this.field_147618_d = height;
/*  46 */     this.field_147622_a = width;
/*  47 */     this.field_147620_b = height;
/*     */     
/*  49 */     this.field_147616_f = genFrameBuffers();
/*  50 */     if (this.field_147616_f == -1) {
/*  51 */       func_147614_f();
/*     */       return;
/*     */     } 
/*  54 */     this.field_147617_g = TextureUtil.func_110996_a();
/*  55 */     if (this.field_147617_g == -1) {
/*  56 */       func_147614_f();
/*     */       
/*     */       return;
/*     */     } 
/*  60 */     if (this.field_147619_e) {
/*     */       
/*  62 */       this.field_147624_h = genRenderbuffers();
/*  63 */       if (this.field_147624_h == -1) {
/*  64 */         func_147614_f();
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/*  69 */     func_147607_a(9728);
/*  70 */     GlStateManager.func_179144_i(this.field_147617_g);
/*  71 */     GlStateManager.func_187419_a(3553, 0, 32856, this.field_147622_a, this.field_147620_b, 0, 6408, 5121, (IntBuffer)null);
/*  72 */     bindFramebuffer(this.type, 36160, this.field_147616_f);
/*  73 */     framebufferTexture2D(this.type, 36160, 36064, 3553, this.field_147617_g, 0);
/*     */     
/*  75 */     if (this.field_147619_e) {
/*     */       
/*  77 */       bindRenderbuffer(this.type, 36161, this.field_147624_h);
/*  78 */       if (!isStencilEnabled()) {
/*     */         
/*  80 */         renderbufferStorage(this.type, 36161, 33190, this.field_147622_a, this.field_147620_b);
/*  81 */         framebufferRenderbuffer(this.type, 36160, 36096, 36161, this.field_147624_h);
/*     */       }
/*     */       else {
/*     */         
/*  85 */         renderbufferStorage(this.type, 36161, 35056, this.field_147622_a, this.field_147620_b);
/*  86 */         framebufferRenderbuffer(this.type, 36160, 36096, 36161, this.field_147624_h);
/*  87 */         framebufferRenderbuffer(this.type, 36160, 36128, 36161, this.field_147624_h);
/*     */       } 
/*     */     } 
/*     */     
/*  91 */     func_147614_f();
/*  92 */     func_147606_d();
/*     */   }
/*     */   
/*     */   private int genFrameBuffers() {
/*  96 */     int fbo = -1;
/*  97 */     this.type = -1;
/*  98 */     if ((GLContext.getCapabilities()).OpenGL30) {
/*  99 */       fbo = GL30.glGenFramebuffers();
/* 100 */       this.type = 0;
/* 101 */     } else if ((GLContext.getCapabilities()).GL_ARB_framebuffer_object) {
/* 102 */       fbo = ARBFramebufferObject.glGenFramebuffers();
/* 103 */       this.type = 1;
/*     */     }
/* 105 */     else if ((GLContext.getCapabilities()).GL_EXT_framebuffer_object) {
/* 106 */       fbo = EXTFramebufferObject.glGenFramebuffersEXT();
/* 107 */       this.type = 2;
/*     */     } 
/* 109 */     return fbo;
/*     */   }
/*     */ 
/*     */   
/*     */   public int genRenderbuffers() {
/* 114 */     int rbo = -1;
/* 115 */     switch (this.type) {
/*     */       case 0:
/* 117 */         rbo = GL30.glGenRenderbuffers();
/*     */         break;
/*     */       case 1:
/* 120 */         rbo = ARBFramebufferObject.glGenRenderbuffers();
/*     */         break;
/*     */       case 2:
/* 123 */         rbo = EXTFramebufferObject.glGenRenderbuffersEXT();
/*     */         break;
/*     */     } 
/* 126 */     return rbo;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void func_147608_a() {
/* 132 */     func_147606_d();
/* 133 */     func_147609_e();
/*     */     
/* 135 */     if (this.field_147624_h > -1) {
/*     */       
/* 137 */       deleteRenderbuffers(this.field_147624_h);
/* 138 */       this.field_147624_h = -1;
/*     */     } 
/*     */     
/* 141 */     if (this.field_147617_g > -1) {
/*     */       
/* 143 */       TextureUtil.func_147942_a(this.field_147617_g);
/* 144 */       this.field_147617_g = -1;
/*     */     } 
/*     */     
/* 147 */     if (this.field_147616_f > -1) {
/*     */       
/* 149 */       bindFramebuffer(this.type, 36160, 0);
/* 150 */       deleteFramebuffers(this.field_147616_f);
/* 151 */       this.field_147616_f = -1;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void deleteFramebuffers(int framebufferIn) {
/* 156 */     switch (this.type) {
/*     */       
/*     */       case 0:
/* 159 */         GL30.glDeleteFramebuffers(framebufferIn);
/*     */         break;
/*     */       case 1:
/* 162 */         ARBFramebufferObject.glDeleteFramebuffers(framebufferIn);
/*     */         break;
/*     */       case 2:
/* 165 */         EXTFramebufferObject.glDeleteFramebuffersEXT(framebufferIn);
/*     */         break;
/*     */     } 
/*     */   }
/*     */   private void deleteRenderbuffers(int renderbuffer) {
/* 170 */     switch (this.type) {
/*     */       
/*     */       case 0:
/* 173 */         GL30.glDeleteRenderbuffers(renderbuffer);
/*     */         break;
/*     */       case 1:
/* 176 */         ARBFramebufferObject.glDeleteRenderbuffers(renderbuffer);
/*     */         break;
/*     */       case 2:
/* 179 */         EXTFramebufferObject.glDeleteRenderbuffersEXT(renderbuffer);
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void func_147611_b() {
/* 191 */     int i = checkFramebufferStatus(36160);
/*     */     
/* 193 */     if (i != 36053) {
/*     */       
/* 195 */       if (i == 36054)
/*     */       {
/* 197 */         throw new RuntimeException("GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT");
/*     */       }
/* 199 */       if (i == 36055)
/*     */       {
/* 201 */         throw new RuntimeException("GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT");
/*     */       }
/* 203 */       if (i == 36059)
/*     */       {
/* 205 */         throw new RuntimeException("GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER");
/*     */       }
/* 207 */       if (i == 36060)
/*     */       {
/* 209 */         throw new RuntimeException("GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER");
/*     */       }
/*     */ 
/*     */       
/* 213 */       throw new RuntimeException("glCheckFramebufferStatus returned unknown status:" + i);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private int checkFramebufferStatus(int target) {
/* 219 */     switch (this.type) {
/*     */       
/*     */       case 0:
/* 222 */         return GL30.glCheckFramebufferStatus(target);
/*     */       case 1:
/* 224 */         return ARBFramebufferObject.glCheckFramebufferStatus(target);
/*     */       case 2:
/* 226 */         return EXTFramebufferObject.glCheckFramebufferStatusEXT(target);
/*     */     } 
/* 228 */     return -1;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void bindFramebuffer(int type, int target, int framebufferIn) {
/* 233 */     switch (type) {
/*     */       
/*     */       case 0:
/* 236 */         GL30.glBindFramebuffer(target, framebufferIn);
/*     */         break;
/*     */       case 1:
/* 239 */         ARBFramebufferObject.glBindFramebuffer(target, framebufferIn);
/*     */         break;
/*     */       case 2:
/* 242 */         EXTFramebufferObject.glBindFramebufferEXT(target, framebufferIn);
/*     */         break;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void framebufferTexture2D(int type, int target, int attachment, int textarget, int texture, int level) {
/* 248 */     switch (type) {
/*     */       
/*     */       case 0:
/* 251 */         GL30.glFramebufferTexture2D(target, attachment, textarget, texture, level);
/*     */         break;
/*     */       case 1:
/* 254 */         ARBFramebufferObject.glFramebufferTexture2D(target, attachment, textarget, texture, level);
/*     */         break;
/*     */       case 2:
/* 257 */         EXTFramebufferObject.glFramebufferTexture2DEXT(target, attachment, textarget, texture, level);
/*     */         break;
/*     */     } 
/*     */   }
/*     */   public static void bindRenderbuffer(int type, int target, int renderbuffer) {
/* 262 */     switch (type) {
/*     */       
/*     */       case 0:
/* 265 */         GL30.glBindRenderbuffer(target, renderbuffer);
/*     */         break;
/*     */       case 1:
/* 268 */         ARBFramebufferObject.glBindRenderbuffer(target, renderbuffer);
/*     */         break;
/*     */       case 2:
/* 271 */         EXTFramebufferObject.glBindRenderbufferEXT(target, renderbuffer);
/*     */         break;
/*     */     } 
/*     */   }
/*     */   public static void renderbufferStorage(int type, int target, int internalFormat, int width, int height) {
/* 276 */     switch (type) {
/*     */       
/*     */       case 0:
/* 279 */         GL30.glRenderbufferStorage(target, internalFormat, width, height);
/*     */         break;
/*     */       case 1:
/* 282 */         ARBFramebufferObject.glRenderbufferStorage(target, internalFormat, width, height);
/*     */         break;
/*     */       case 2:
/* 285 */         EXTFramebufferObject.glRenderbufferStorageEXT(target, internalFormat, width, height);
/*     */         break;
/*     */     } 
/*     */   }
/*     */   public static void framebufferRenderbuffer(int type, int target, int attachment, int renderBufferTarget, int renderBuffer) {
/* 290 */     switch (type) {
/*     */       
/*     */       case 0:
/* 293 */         GL30.glFramebufferRenderbuffer(target, attachment, renderBufferTarget, renderBuffer);
/*     */         break;
/*     */       case 1:
/* 296 */         ARBFramebufferObject.glFramebufferRenderbuffer(target, attachment, renderBufferTarget, renderBuffer);
/*     */         break;
/*     */       case 2:
/* 299 */         EXTFramebufferObject.glFramebufferRenderbufferEXT(target, attachment, renderBufferTarget, renderBuffer);
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void func_147610_a(boolean p_147610_1_) {
/* 306 */     bindFramebuffer(this.type, 36160, this.field_147616_f);
/*     */     
/* 308 */     if (p_147610_1_)
/*     */     {
/* 310 */       GlStateManager.func_179083_b(0, 0, this.field_147621_c, this.field_147618_d);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void func_147609_e() {
/* 317 */     bindFramebuffer(this.type, 36160, 0);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void func_147612_c() {
/* 323 */     GlStateManager.func_179144_i(this.field_147617_g);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void func_147606_d() {
/* 329 */     GlStateManager.func_179144_i(0);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void func_147607_a(int framebufferFilterIn) {
/* 335 */     this.field_147623_j = framebufferFilterIn;
/* 336 */     GlStateManager.func_179144_i(this.field_147617_g);
/* 337 */     GlStateManager.func_187421_b(3553, 10241, framebufferFilterIn);
/* 338 */     GlStateManager.func_187421_b(3553, 10240, framebufferFilterIn);
/* 339 */     GlStateManager.func_187421_b(3553, 10242, 10496);
/* 340 */     GlStateManager.func_187421_b(3553, 10243, 10496);
/* 341 */     GlStateManager.func_179144_i(0);
/*     */   }
/*     */   
/*     */   public int getType() {
/* 345 */     return this.type;
/*     */   }
/*     */ }


/* Location:              C:\Users\ioanm\Downloads\XaerosWorldMap_1.7.3_Forge_1.12.jar!\xaero\map\graphics\ImprovedFramebuffer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */