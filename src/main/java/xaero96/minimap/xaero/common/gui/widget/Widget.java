/*     */ package xaero.common.gui.widget;
/*     */ 
/*     */ import net.minecraft.client.gui.GuiScreen;
/*     */ import xaero.common.graphics.CursorBox;
/*     */ 
/*     */ 
/*     */ public class Widget
/*     */ {
/*     */   private WidgetType type;
/*     */   private Class<? extends GuiScreen> location;
/*     */   private float horizontalAnchor;
/*     */   private float verticalAnchor;
/*     */   private ClickAction onClick;
/*     */   private HoverAction onHover;
/*     */   private int x;
/*     */   private int y;
/*     */   private String url;
/*     */   private String tooltip;
/*     */   private CursorBox cursorBox;
/*     */   
/*     */   public Widget(WidgetType type, Class<? extends GuiScreen> location, float horizontalAnchor, float verticalAnchor, ClickAction onClick, HoverAction onHover, int x, int y, String url, String tooltip) {
/*  22 */     this.type = type;
/*  23 */     this.location = location;
/*  24 */     this.horizontalAnchor = horizontalAnchor;
/*  25 */     this.verticalAnchor = verticalAnchor;
/*  26 */     this.onClick = onClick;
/*  27 */     this.onHover = onHover;
/*  28 */     this.x = x;
/*  29 */     this.y = y;
/*  30 */     this.url = url;
/*  31 */     this.tooltip = tooltip;
/*  32 */     if (tooltip != null && !tooltip.isEmpty())
/*  33 */       this.cursorBox = new CursorBox(tooltip); 
/*     */   }
/*     */   
/*     */   public WidgetType getType() {
/*  37 */     return this.type;
/*     */   }
/*     */   
/*     */   public Class<? extends GuiScreen> getLocation() {
/*  41 */     return this.location;
/*     */   }
/*     */   
/*     */   public float getHorizontalAnchor() {
/*  45 */     return this.horizontalAnchor;
/*     */   }
/*     */   
/*     */   public float getVerticalAnchor() {
/*  49 */     return this.verticalAnchor;
/*     */   }
/*     */   
/*     */   public ClickAction getOnClick() {
/*  53 */     return this.onClick;
/*     */   }
/*     */   
/*     */   public HoverAction getOnHover() {
/*  57 */     return this.onHover;
/*     */   }
/*     */   
/*     */   public int getX(int width) {
/*  61 */     return (int)(width * this.horizontalAnchor + this.x);
/*     */   }
/*     */   
/*     */   public int getY(int height) {
/*  65 */     return (int)(height * this.verticalAnchor + this.y);
/*     */   }
/*     */   
/*     */   public int getW() {
/*  69 */     return 1;
/*     */   }
/*     */   
/*     */   public int getH() {
/*  73 */     return 1;
/*     */   }
/*     */   
/*     */   public int getBoxX(int width, double guiScale) {
/*  77 */     return getX(width);
/*     */   }
/*     */   
/*     */   public int getBoxY(int height, double guiScale) {
/*  81 */     return getX(height);
/*     */   }
/*     */   
/*     */   public int getBoxW(double guiScale) {
/*  85 */     return getW();
/*     */   }
/*     */   
/*     */   public int getBoxH(double guiScale) {
/*  89 */     return getH();
/*     */   }
/*     */   
/*     */   public String getUrl() {
/*  93 */     return this.url;
/*     */   }
/*     */   
/*     */   public String getTooltip() {
/*  97 */     return this.tooltip;
/*     */   }
/*     */   
/*     */   public CursorBox getCursorBox() {
/* 101 */     return this.cursorBox;
/*     */   }
/*     */ }


/* Location:              C:\Users\ioanm\Downloads\Xaeros_Minimap_20.15.3_Forge_1.12.jar!\xaero\common\gui\widget\Widget.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */