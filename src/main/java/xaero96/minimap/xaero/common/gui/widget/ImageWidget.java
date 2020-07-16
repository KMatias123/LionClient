/*    */ package xaero.common.gui.widget;
/*    */ 
/*    */ import net.minecraft.client.gui.GuiScreen;
/*    */ 
/*    */ 
/*    */ public class ImageWidget
/*    */   extends ScalableWidget
/*    */ {
/*    */   private String imageId;
/*    */   private int imageW;
/*    */   private int imageH;
/*    */   private int glTexture;
/*    */   
/*    */   public ImageWidget(Class<? extends GuiScreen> location, float horizontalAnchor, float verticalAnchor, ClickAction onClick, HoverAction onHover, int x, int y, int scaledOffsetX, int scaledOffsetY, String url, String tooltip, double scale, String imageId, int imageW, int imageH, int glTexture, boolean noGuiScale) {
/* 15 */     super(WidgetType.IMAGE, location, horizontalAnchor, verticalAnchor, onClick, onHover, x, y, scaledOffsetX, scaledOffsetY, url, tooltip, noGuiScale, scale);
/* 16 */     this.imageId = imageId;
/* 17 */     this.imageW = imageW;
/* 18 */     this.imageH = imageH;
/* 19 */     this.glTexture = glTexture;
/*    */   }
/*    */   
/*    */   public String getImageId() {
/* 23 */     return this.imageId;
/*    */   }
/*    */   
/*    */   public int getImageW() {
/* 27 */     return this.imageW;
/*    */   }
/*    */   
/*    */   public int getImageH() {
/* 31 */     return this.imageH;
/*    */   }
/*    */   
/*    */   public int getGlTexture() {
/* 35 */     return this.glTexture;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getW() {
/* 40 */     return this.imageW;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getH() {
/* 45 */     return this.imageH;
/*    */   }
/*    */ }


/* Location:              C:\Users\ioanm\Downloads\Xaeros_Minimap_20.15.3_Forge_1.12.jar!\xaero\common\gui\widget\ImageWidget.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */