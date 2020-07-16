/*    */ package xaero.common.gui.widget;
/*    */ 
/*    */ import net.minecraft.client.gui.GuiScreen;
/*    */ 
/*    */ 
/*    */ public class ScalableWidget
/*    */   extends Widget
/*    */ {
/*    */   private double scale;
/*    */   private int scaledOffsetX;
/*    */   private int scaledOffsetY;
/*    */   private boolean noGuiScale;
/*    */   
/*    */   public ScalableWidget(WidgetType type, Class<? extends GuiScreen> location, float horizontalAnchor, float verticalAnchor, ClickAction onClick, HoverAction onHover, int x, int y, int scaledOffsetX, int scaledOffsetY, String url, String tooltip, boolean noGuiScale, double scale) {
/* 15 */     super(type, location, horizontalAnchor, verticalAnchor, onClick, onHover, x, y, url, tooltip);
/*    */     
/* 17 */     this.scale = scale;
/* 18 */     this.scaledOffsetX = scaledOffsetX;
/* 19 */     this.scaledOffsetY = scaledOffsetY;
/* 20 */     this.noGuiScale = noGuiScale;
/*    */   }
/*    */   
/*    */   public double getScale() {
/* 24 */     return this.scale;
/*    */   }
/*    */   
/*    */   public int getScaledOffsetX() {
/* 28 */     return this.scaledOffsetX;
/*    */   }
/*    */   
/*    */   public int getScaledOffsetY() {
/* 32 */     return this.scaledOffsetY;
/*    */   }
/*    */   
/*    */   public boolean isNoGuiScale() {
/* 36 */     return this.noGuiScale;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getBoxX(int width, double guiScale) {
/* 41 */     int originX = getX(width);
/* 42 */     double combinedScale = getScale() / (isNoGuiScale() ? guiScale : 1.0D);
/* 43 */     return (int)(originX + getScaledOffsetX() * combinedScale);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getBoxY(int height, double guiScale) {
/* 48 */     int originY = getY(height);
/* 49 */     double combinedScale = getScale() / (isNoGuiScale() ? guiScale : 1.0D);
/* 50 */     return (int)(originY + getScaledOffsetY() * combinedScale);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getBoxW(double guiScale) {
/* 55 */     double combinedScale = getScale() / (isNoGuiScale() ? guiScale : 1.0D);
/* 56 */     return (int)(getW() * combinedScale);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getBoxH(double guiScale) {
/* 61 */     double combinedScale = getScale() / (isNoGuiScale() ? guiScale : 1.0D);
/* 62 */     return (int)(getH() * combinedScale);
/*    */   }
/*    */ }


/* Location:              C:\Users\ioanm\Downloads\Xaeros_Minimap_20.15.3_Forge_1.12.jar!\xaero\common\gui\widget\ScalableWidget.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */