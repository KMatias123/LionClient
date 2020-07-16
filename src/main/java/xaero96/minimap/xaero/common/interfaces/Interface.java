/*     */ package xaero.common.interfaces;
/*     */ 
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.resources.I18n;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ import xaero.common.graphics.CursorBox;
/*     */ import xaero.common.settings.ModOptions;
/*     */ 
/*     */ 
/*     */ public abstract class Interface
/*     */ {
/*     */   private CursorBox cBox;
/*     */   private String iname;
/*     */   private int id;
/*     */   private int bx;
/*     */   private int by;
/*     */   private int x;
/*     */   private int y;
/*     */   private int actualx;
/*     */   private int actualy;
/*     */   private int w0;
/*     */   private int h0;
/*     */   private int w;
/*     */   private int h;
/*     */   private int wc;
/*     */   private int hc;
/*     */   private boolean multisized;
/*     */   private boolean centered;
/*     */   private boolean bcentered;
/*     */   private boolean flipped;
/*     */   private boolean bflipped;
/*     */   private boolean flippedInitial;
/*     */   private boolean fromRight;
/*     */   private boolean bfromRight;
/*     */   private boolean fromBottom;
/*     */   private boolean bfromBottom;
/*     */   private ModOptions option;
/*     */   
/*     */   public Interface(InterfaceManager interfaceHandler, String name, int id, int w, int h, ModOptions option) {
/*  40 */     this(interfaceHandler, name, id, w, h, w, h, option);
/*     */   }
/*     */   
/*     */   public Interface(InterfaceManager interfaceHandler, String name, int id, int w, int h, int wc, int hc, ModOptions option) {
/*  44 */     this.id = id;
/*  45 */     this.iname = name;
/*  46 */     this.w0 = this.w = w;
/*  47 */     this.h0 = this.h = h;
/*  48 */     this.wc = wc;
/*  49 */     this.hc = hc;
/*  50 */     this.multisized = (wc != w || hc != h);
/*  51 */     this.flippedInitial = this.flipped = this.bflipped = false;
/*  52 */     Preset preset = interfaceHandler.getDefaultPreset();
/*  53 */     this.bx = this.actualx = this.x = preset.getCoords(id)[0];
/*  54 */     this.by = this.actualy = this.y = preset.getCoords(id)[1];
/*  55 */     this.bcentered = this.centered = preset.getTypes(id)[0];
/*  56 */     this.bfromRight = this.fromRight = preset.getTypes(id)[1];
/*  57 */     this.bfromBottom = this.fromBottom = preset.getTypes(id)[2];
/*  58 */     this.option = option;
/*  59 */     this.cBox = new CursorBox(3) {
/*     */         public String getString(int line) {
/*  61 */           switch (line) {
/*     */             case 0:
/*  63 */               return I18n.func_135052_a(Interface.this.iname, new Object[0]);
/*     */             case 1:
/*  65 */               return I18n.func_135052_a("gui.xaero_centered", new Object[0]) + " " + I18n.func_135052_a(Interface.this.centered ? "gui.xaero_true" : "gui.xaero_false", new Object[0]) + " " + 
/*  66 */                 I18n.func_135052_a("gui.xaero_press_c", new Object[0]);
/*     */             case 2:
/*  68 */               return I18n.func_135052_a("gui.xaero_flipped", new Object[0]) + " " + I18n.func_135052_a(Interface.this.flipped ? "gui.xaero_true" : "gui.xaero_false", new Object[0]) + " " + 
/*  69 */                 I18n.func_135052_a("gui.xaero_press_f", new Object[0]);
/*     */           } 
/*  71 */           return "";
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public void drawInterface(int width, int height, int scale, float partial) {
/*  77 */     if (this.fromRight)
/*  78 */       this.x = width - this.x; 
/*  79 */     if (this.fromBottom)
/*  80 */       this.y = height - this.y; 
/*  81 */     GL11.glEnable(3008);
/*  82 */     GlStateManager.func_179147_l();
/*  83 */     GlStateManager.func_179112_b(770, 771);
/*     */   }
/*     */   
/*     */   public boolean shouldFlip(int width) {
/*  87 */     return ((this.flipped && this.x + this.w / 2 < width / 2) || (!this.flipped && this.x + this.w / 2 > width / 2));
/*     */   }
/*     */   
/*     */   public void backup() {
/*  91 */     this.bx = this.actualx;
/*  92 */     this.by = this.actualy;
/*  93 */     this.bcentered = this.centered;
/*  94 */     this.bflipped = this.flipped;
/*  95 */     this.bfromRight = this.fromRight;
/*  96 */     this.bfromBottom = this.fromBottom;
/*     */   }
/*     */   
/*     */   public void restore() {
/* 100 */     this.actualx = this.bx;
/* 101 */     this.actualy = this.by;
/* 102 */     this.centered = this.bcentered;
/* 103 */     this.flipped = this.bflipped;
/* 104 */     this.fromRight = this.bfromRight;
/* 105 */     this.fromBottom = this.bfromBottom;
/*     */   }
/*     */   
/*     */   public void applyPreset(Preset preset) {
/* 109 */     this.actualx = preset.getCoords(this.id)[0];
/* 110 */     this.actualy = preset.getCoords(this.id)[1];
/* 111 */     this.centered = preset.getTypes(this.id)[0];
/* 112 */     this.flipped = this.flippedInitial;
/* 113 */     this.fromRight = preset.getTypes(this.id)[1];
/* 114 */     this.fromBottom = preset.getTypes(this.id)[2];
/*     */   }
/*     */   
/*     */   public ModOptions getOption() {
/* 118 */     return this.option;
/*     */   }
/*     */   
/*     */   public boolean isFromRight() {
/* 122 */     return this.fromRight;
/*     */   }
/*     */   
/*     */   public void setFromRight(boolean fromRight) {
/* 126 */     this.fromRight = fromRight;
/*     */   }
/*     */   
/*     */   public int getW(int scale) {
/* 130 */     return this.w;
/*     */   }
/*     */   
/*     */   public int getH(int scale) {
/* 134 */     return this.h;
/*     */   }
/*     */   
/*     */   public int getWC(int scale) {
/* 138 */     return this.wc;
/*     */   }
/*     */   
/*     */   public int getHC(int scale) {
/* 142 */     return this.hc;
/*     */   }
/*     */   
/*     */   public int getW0(int scale) {
/* 146 */     return this.w0;
/*     */   }
/*     */   
/*     */   public int getH0(int scale) {
/* 150 */     return this.h0;
/*     */   }
/*     */   
/*     */   public int getSize() {
/* 154 */     return this.w * this.h;
/*     */   }
/*     */   
/*     */   public int getX() {
/* 158 */     return this.x;
/*     */   }
/*     */   
/*     */   public void setX(int x) {
/* 162 */     this.x = x;
/*     */   }
/*     */   
/*     */   public int getY() {
/* 166 */     return this.y;
/*     */   }
/*     */   
/*     */   public void setY(int y) {
/* 170 */     this.y = y;
/*     */   }
/*     */   
/*     */   public boolean isFlipped() {
/* 174 */     return this.flipped;
/*     */   }
/*     */   
/*     */   public void setFlipped(boolean flipped) {
/* 178 */     this.flipped = flipped;
/*     */   }
/*     */   
/*     */   public boolean isCentered() {
/* 182 */     return this.centered;
/*     */   }
/*     */   
/*     */   public void setCentered(boolean centered) {
/* 186 */     this.centered = centered;
/*     */   }
/*     */   
/*     */   public int getActualx() {
/* 190 */     return this.actualx;
/*     */   }
/*     */   
/*     */   public void setActualx(int actualx) {
/* 194 */     this.actualx = actualx;
/*     */   }
/*     */   
/*     */   public int getActualy() {
/* 198 */     return this.actualy;
/*     */   }
/*     */   
/*     */   public void setActualy(int actualy) {
/* 202 */     this.actualy = actualy;
/*     */   }
/*     */   
/*     */   public boolean isMulti() {
/* 206 */     return this.multisized;
/*     */   }
/*     */   
/*     */   public int getW() {
/* 210 */     return this.w;
/*     */   }
/*     */   
/*     */   public void setW(int w) {
/* 214 */     this.w = w;
/*     */   }
/*     */   
/*     */   public int getH() {
/* 218 */     return this.h;
/*     */   }
/*     */   
/*     */   public void setH(int h) {
/* 222 */     this.h = h;
/*     */   }
/*     */   
/*     */   public CursorBox getcBox() {
/* 226 */     return this.cBox;
/*     */   }
/*     */   
/*     */   public String getIname() {
/* 230 */     return this.iname;
/*     */   }
/*     */   
/*     */   public boolean isFromBottom() {
/* 234 */     return this.fromBottom;
/*     */   }
/*     */   
/*     */   public void setFromBottom(boolean fromBottom) {
/* 238 */     this.fromBottom = fromBottom;
/*     */   }
/*     */ }


/* Location:              C:\Users\ioanm\Downloads\Xaeros_Minimap_20.15.3_Forge_1.12.jar!\xaero\common\interfaces\Interface.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */