/*     */ package xaero.map.mods.gui;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.nio.IntBuffer;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.Gui;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ import xaero.map.MapProcessor;
/*     */ import xaero.map.graphics.ImprovedFramebuffer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class WaypointSymbolCreator
/*     */ {
/*  23 */   public static final ResourceLocation minimapTextures = new ResourceLocation("xaerobetterpvp", "gui/guis.png");
/*  24 */   public static final int white = (new Color(255, 255, 255, 255)).hashCode();
/*     */ 
/*     */ 
/*     */   
/*     */   private int deathSymbolTexture;
/*     */ 
/*     */ 
/*     */   
/*  32 */   private Minecraft mc = Minecraft.func_71410_x();
/*  33 */   private Map<String, Integer> charSymbols = new HashMap<>();
/*     */   private ImprovedFramebuffer symbolFramebuffer32;
/*     */   
/*     */   public int getDeathSymbolTexture() {
/*  37 */     if (this.deathSymbolTexture == 0)
/*  38 */       createDeathSymbolTexture(); 
/*  39 */     return this.deathSymbolTexture;
/*     */   }
/*     */   private ImprovedFramebuffer symbolFramebuffer64;
/*     */   private void createDeathSymbolTexture() {
/*  43 */     this.deathSymbolTexture = createCharSymbol(true, null);
/*     */   }
/*     */   
/*     */   public int getSymbolTexture(String c) {
/*     */     Integer textureId;
/*  48 */     synchronized (this.charSymbols) {
/*  49 */       textureId = this.charSymbols.get(c);
/*     */     } 
/*  51 */     if (textureId == null)
/*  52 */       textureId = Integer.valueOf(createCharSymbol(false, c)); 
/*  53 */     return textureId.intValue();
/*     */   }
/*     */   
/*     */   private int createCharSymbol(boolean death, String c) {
/*  57 */     if (this.symbolFramebuffer32 == null)
/*  58 */       this.symbolFramebuffer32 = new ImprovedFramebuffer(32, 32, false); 
/*  59 */     if (this.symbolFramebuffer64 == null)
/*  60 */       this.symbolFramebuffer64 = new ImprovedFramebuffer(64, 32, false); 
/*  61 */     int frameWidth = 32;
/*  62 */     if (this.mc.field_71466_p.func_78256_a(c) / 2 > 4)
/*  63 */       frameWidth = 64; 
/*  64 */     ImprovedFramebuffer symbolFramebuffer = (frameWidth == 32) ? this.symbolFramebuffer32 : this.symbolFramebuffer64;
/*  65 */     int textureId = GlStateManager.func_179146_y();
/*  66 */     GlStateManager.func_179144_i(textureId);
/*  67 */     GL11.glTexParameteri(3553, 33085, 0);
/*  68 */     GL11.glTexParameterf(3553, 33082, 0.0F);
/*  69 */     GL11.glTexParameterf(3553, 33083, 0.0F);
/*  70 */     GL11.glTexParameterf(3553, 34049, 0.0F);
/*  71 */     GL11.glTexParameteri(3553, 10241, 9729);
/*  72 */     GL11.glTexParameteri(3553, 10240, 9729);
/*  73 */     GL11.glTexParameteri(3553, 10242, 33071);
/*  74 */     GL11.glTexParameteri(3553, 10243, 33071);
/*  75 */     GlStateManager.func_187419_a(3553, 0, 32856, frameWidth, 32, 0, 6408, 5121, (IntBuffer)null);
/*  76 */     symbolFramebuffer.func_147610_a(true);
/*  77 */     symbolFramebuffer.field_147617_g = textureId;
/*  78 */     ImprovedFramebuffer.framebufferTexture2D(symbolFramebuffer.getType(), 36160, 36064, 3553, textureId, 0);
/*  79 */     symbolFramebuffer.func_147606_d();
/*  80 */     symbolFramebuffer.func_147610_a(true);
/*     */     
/*  82 */     GlStateManager.func_179082_a(0.0F, 0.0F, 0.0F, 0.0F);
/*  83 */     GlStateManager.func_179086_m(16384);
/*  84 */     GlStateManager.func_179128_n(5889);
/*  85 */     GlStateManager.func_179094_E();
/*  86 */     GlStateManager.func_179096_D();
/*  87 */     GlStateManager.func_179130_a(0.0D, frameWidth, 32.0D, 0.0D, -1.0D, 1000.0D);
/*  88 */     GlStateManager.func_179128_n(5888);
/*  89 */     GlStateManager.func_179094_E();
/*  90 */     GlStateManager.func_179096_D();
/*     */     
/*  92 */     GlStateManager.func_179109_b(1.0F, 1.0F, 0.0F);
/*  93 */     if (!death) {
/*  94 */       GlStateManager.func_179152_a(3.0F, 3.0F, 1.0F);
/*  95 */       this.mc.field_71466_p.func_175063_a(c, 0.0F, 0.0F, white);
/*     */     } else {
/*  97 */       GlStateManager.func_179152_a(3.0F, 3.0F, 1.0F);
/*  98 */       this.mc.func_110434_K().func_110577_a(minimapTextures);
/*  99 */       Gui.func_146110_a(0, 0, 0.0F, 78.0F, 9, 9, 256.0F, 256.0F);
/*     */     } 
/*     */     
/* 102 */     GlStateManager.func_179128_n(5889);
/* 103 */     GlStateManager.func_179121_F();
/* 104 */     GlStateManager.func_179128_n(5888);
/* 105 */     GlStateManager.func_179121_F();
/* 106 */     symbolFramebuffer.func_147609_e();
/* 107 */     this.mc.func_147110_a().func_147610_a(true);
/* 108 */     if (death) {
/* 109 */       this.deathSymbolTexture = textureId;
/*     */     } else {
/* 111 */       this.charSymbols.put(c, Integer.valueOf(textureId));
/* 112 */     }  return textureId;
/*     */   }
/*     */   
/*     */   public void resetChars() {
/* 116 */     synchronized (this.charSymbols) {
/* 117 */       Collection<Integer> valueSet = this.charSymbols.values();
/* 118 */       for (Integer textureId : valueSet)
/* 119 */         MapProcessor.instance.requestTextureDeletion(textureId.intValue()); 
/* 120 */       this.charSymbols.clear();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\ioanm\Downloads\XaerosWorldMap_1.7.3_Forge_1.12.jar!\xaero\map\mods\gui\WaypointSymbolCreator.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */