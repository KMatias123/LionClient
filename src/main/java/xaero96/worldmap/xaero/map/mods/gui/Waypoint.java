/*     */ package xaero.map.mods.gui;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.Gui;
/*     */ import net.minecraft.client.gui.GuiScreen;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.resources.I18n;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import xaero.map.MapProcessor;
/*     */ import xaero.map.WorldMap;
/*     */ import xaero.map.animation.SlowingAnimation;
/*     */ 
/*     */ public class Waypoint
/*     */   implements Comparable<Waypoint> {
/*     */   private Object original;
/*  17 */   public static final ResourceLocation minimapTextures = new ResourceLocation("xaerobetterpvp", "gui/guis.png");
/*  18 */   public static final int white = (new Color(255, 255, 255, 255)).hashCode();
/*     */   private int x;
/*     */   private int y;
/*     */   private int z;
/*     */   private String text;
/*     */   private String symbol;
/*     */   private int color;
/*     */   private boolean disabled = false;
/*  26 */   private int type = 0;
/*     */   private boolean rotation = false;
/*  28 */   private int yaw = 0;
/*     */   
/*  30 */   private float destAlpha = 0.0F;
/*  31 */   private float alpha = 0.0F;
/*  32 */   private SlowingAnimation alphaAnim = null;
/*     */   
/*     */   public Waypoint(Object original, int x, int y, int z, String name, String symbol, int color, int type, boolean editable) {
/*  35 */     this.original = original;
/*  36 */     this.x = x;
/*  37 */     this.y = y;
/*  38 */     this.z = z;
/*  39 */     this.symbol = symbol;
/*  40 */     this.color = color;
/*  41 */     this.type = type;
/*  42 */     this.text = name;
/*  43 */     this.editable = editable;
/*     */   }
/*     */   private boolean editable;
/*     */   public String getName() {
/*  47 */     return I18n.func_135052_a(this.text, new Object[0]);
/*     */   }
/*     */   
/*     */   public void renderWaypoint(GuiScreen gui, double guiScale, double scale, float x, float y, boolean viewing) {
/*  51 */     Minecraft mc = Minecraft.func_71410_x();
/*  52 */     mc.func_110434_K().func_110577_a(WorldMap.guiTextures);
/*  53 */     GlStateManager.func_179094_E();
/*  54 */     GlStateManager.func_179109_b(x, y, 0.0F);
/*  55 */     GlStateManager.func_179139_a(1.0D / scale, 1.0D / scale, 1.0D);
/*  56 */     GlStateManager.func_179094_E();
/*  57 */     GlStateManager.func_179147_l();
/*  58 */     GlStateManager.func_179124_c((this.color >> 16 & 0xFF) / 255.0F, (this.color >> 8 & 0xFF) / 255.0F, (this.color & 0xFF) / 255.0F);
/*     */     
/*  60 */     int flagU = 35;
/*  61 */     int flagV = 34;
/*  62 */     int flagW = 30;
/*  63 */     int flagH = 43;
/*  64 */     if (this.symbol.length() > 1) {
/*  65 */       flagU = 70;
/*  66 */       flagW = 43;
/*     */     } 
/*  68 */     GlStateManager.func_179109_b(-flagW / 2.0F, (-flagH + 1), 0.0F);
/*  69 */     Gui.func_146110_a(0, 0, flagU, flagV, flagW, flagH, 256.0F, 256.0F);
/*  70 */     GlStateManager.func_179124_c(1.0F, 1.0F, 1.0F);
/*  71 */     GlStateManager.func_179121_F();
/*  72 */     float oldDestAlpha = this.destAlpha;
/*  73 */     if (viewing) {
/*  74 */       this.destAlpha = 255.0F;
/*     */     } else {
/*  76 */       this.destAlpha = 0.0F;
/*  77 */     }  if (oldDestAlpha != this.destAlpha)
/*  78 */       this.alphaAnim = new SlowingAnimation(this.alpha, this.destAlpha, 0.8D, 1.0D); 
/*  79 */     if (this.alphaAnim != null)
/*  80 */       this.alpha = (float)this.alphaAnim.getCurrent(); 
/*  81 */     int symbolTexture = 0;
/*  82 */     int symbolVerticalOffset = 0;
/*  83 */     int symbolWidth = 0;
/*  84 */     int stringWidth = mc.field_71466_p.func_78256_a(this.symbol);
/*  85 */     int symbolFrameWidth = (stringWidth / 2 > 4) ? 64 : 32;
/*  86 */     GlStateManager.func_187428_a(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
/*  87 */     if (this.type == 0 && this.alpha < 200.0F) {
/*  88 */       symbolVerticalOffset = 5;
/*  89 */       symbolWidth = (stringWidth - 1) * 3;
/*  90 */       symbolTexture = WorldMap.waypointSymbolCreator.getSymbolTexture(this.symbol);
/*  91 */     } else if (this.type == 1) {
/*  92 */       symbolVerticalOffset = 3;
/*  93 */       symbolWidth = 27;
/*  94 */       symbolTexture = WorldMap.waypointSymbolCreator.getDeathSymbolTexture();
/*     */     } 
/*  96 */     if (symbolTexture != 0) {
/*  97 */       GlStateManager.func_179124_c(1.0F, 1.0F, 1.0F);
/*  98 */       GlStateManager.func_179144_i(symbolTexture);
/*  99 */       GlStateManager.func_179094_E();
/* 100 */       GlStateManager.func_179109_b(-1.0F - symbolWidth / 2.0F, (-11 + symbolVerticalOffset - 1), 0.0F);
/* 101 */       GlStateManager.func_179152_a(1.0F, -1.0F, 1.0F);
/* 102 */       Gui.func_146110_a(0, 0, 0.0F, 0.0F, symbolFrameWidth, 32, symbolFrameWidth, 32.0F);
/* 103 */       GlStateManager.func_179121_F();
/*     */     } 
/* 105 */     if ((int)this.alpha > 0) {
/* 106 */       int r = this.color >> 16 & 0xFF;
/* 107 */       int g = this.color >> 8 & 0xFF;
/* 108 */       int b = this.color & 0xFF;
/* 109 */       int c = (int)this.alpha << 24 | r << 16 | g << 8 | b;
/* 110 */       int tbg = (int)(this.alpha / 255.0F * 200.0F) << 24;
/* 111 */       int tc = (int)this.alpha << 24 | 0xFF0000 | 0xFF00 | 0xFF;
/* 112 */       String name = getName();
/* 113 */       int len = mc.field_71466_p.func_78256_a(name);
/*     */       
/* 115 */       GlStateManager.func_179109_b(0.0F, -38.0F, 0.0F);
/* 116 */       GlStateManager.func_179152_a(3.0F, 3.0F, 1.0F);
/* 117 */       int bgLen = Math.max(len + 2, 10);
/* 118 */       Gui.func_73734_a(-bgLen / 2, -1, bgLen / 2, 9, c);
/* 119 */       Gui.func_73734_a(-bgLen / 2, -1, bgLen / 2, 8, tbg);
/* 120 */       GlStateManager.func_179147_l();
/* 121 */       if ((int)this.alpha > 3) {
/* 122 */         mc.field_71466_p.func_175065_a(name, -(len - 1) / 2.0F, 0.0F, tc, false);
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 131 */     GlStateManager.func_187428_a(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
/* 132 */     GlStateManager.func_179124_c(1.0F, 1.0F, 1.0F);
/* 133 */     GlStateManager.func_179121_F();
/*     */   }
/*     */   
/*     */   public void renderSideWaypoint(GuiScreen gui, float x, float y, float scale) {
/* 137 */     Minecraft mc = Minecraft.func_71410_x();
/* 138 */     GlStateManager.func_179094_E();
/* 139 */     GlStateManager.func_179109_b(x, y, 0.0F);
/* 140 */     GlStateManager.func_179152_a(scale, scale, 1.0F);
/* 141 */     GlStateManager.func_179109_b(-4.0F, -4.0F, 0.0F);
/* 142 */     GlStateManager.func_179147_l();
/* 143 */     GlStateManager.func_179124_c((this.color >> 16 & 0xFF) / 255.0F, (this.color >> 8 & 0xFF) / 255.0F, (this.color & 0xFF) / 255.0F);
/* 144 */     if (this.type == 1) {
/* 145 */       Minecraft.func_71410_x().func_110434_K().func_110577_a(minimapTextures);
/* 146 */       gui.func_73729_b(0, 0, 0, 78, 9, 9);
/*     */     } else {
/* 148 */       GlStateManager.func_179090_x();
/* 149 */       Gui.func_73734_a(0, 0, 9, 9, this.color);
/*     */     } 
/* 151 */     GlStateManager.func_179124_c(255.0F, 255.0F, 255.0F);
/* 152 */     if (this.type == 0)
/* 153 */       mc.field_71466_p.func_175065_a(this.symbol, (5 - mc.field_71466_p.func_78256_a(this.symbol) / 2), 1.0F, white, true); 
/* 154 */     String name = getName();
/* 155 */     int len = mc.field_71466_p.func_78256_a(name);
/* 156 */     mc.field_71466_p.func_175065_a(name, (-3 - len), 0.0F, white, true);
/* 157 */     GlStateManager.func_179147_l();
/* 158 */     GlStateManager.func_179124_c(1.0F, 1.0F, 1.0F);
/* 159 */     GlStateManager.func_179121_F();
/*     */   }
/*     */   
/*     */   public void renderShadow(GuiScreen gui, double guiScale, double scale, float x, float y) {
/* 163 */     Minecraft.func_71410_x().func_110434_K().func_110577_a(WorldMap.guiTextures);
/* 164 */     GlStateManager.func_179094_E();
/* 165 */     GlStateManager.func_179109_b(x, y, 0.0F);
/* 166 */     GlStateManager.func_179139_a(guiScale / 3.0D / scale, guiScale / 3.0D / scale, 1.0D);
/* 167 */     GlStateManager.func_179109_b(-14.0F, -41.0F, 0.0F);
/* 168 */     GlStateManager.func_179147_l();
/* 169 */     GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, MapProcessor.instance.getBrightness());
/* 170 */     gui.func_73729_b(0, 19, 0, 117, 41, 22);
/* 171 */     GlStateManager.func_179121_F();
/* 172 */     GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public int compareTo(Waypoint arg0) {
/* 177 */     return (this.z > arg0.z) ? 1 : ((this.z != arg0.z) ? -1 : 0);
/*     */   }
/*     */   
/*     */   public String toString() {
/* 181 */     return getName();
/*     */   }
/*     */   
/*     */   public int getX() {
/* 185 */     return this.x;
/*     */   }
/*     */   
/*     */   public int getY() {
/* 189 */     return this.y;
/*     */   }
/*     */   
/*     */   public int getZ() {
/* 193 */     return this.z;
/*     */   }
/*     */   
/*     */   public boolean isDisabled() {
/* 197 */     return this.disabled;
/*     */   }
/*     */   
/*     */   public void setDisabled(boolean disabled) {
/* 201 */     this.disabled = disabled;
/*     */   }
/*     */   
/*     */   public int getType() {
/* 205 */     return this.type;
/*     */   }
/*     */   
/*     */   public int getYaw() {
/* 209 */     return this.yaw;
/*     */   }
/*     */   
/*     */   public void setYaw(int yaw) {
/* 213 */     this.yaw = yaw;
/*     */   }
/*     */   
/*     */   public boolean isRotation() {
/* 217 */     return this.rotation;
/*     */   }
/*     */   
/*     */   public void setRotation(boolean rotation) {
/* 221 */     this.rotation = rotation;
/*     */   }
/*     */   
/*     */   public boolean isEditable() {
/* 225 */     return this.editable;
/*     */   }
/*     */   
/*     */   public Object getOriginal() {
/* 229 */     return this.original;
/*     */   }
/*     */   
/*     */   public String getSymbol() {
/* 233 */     return this.symbol;
/*     */   }
/*     */ }


/* Location:              C:\Users\ioanm\Downloads\XaerosWorldMap_1.7.3_Forge_1.12.jar!\xaero\map\mods\gui\Waypoint.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */