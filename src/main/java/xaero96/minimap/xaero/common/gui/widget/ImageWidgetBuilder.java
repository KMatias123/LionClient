/*    */ package xaero.common.gui.widget;
/*    */ 
/*    */ public class ImageWidgetBuilder
/*    */   extends ScalableWidgetBuilder {
/*    */   private String imageId;
/*    */   private int imageW;
/*    */   private int imageH;
/*    */   private int glTexture;
/*    */   
/*    */   public void setImageId(String imageId) {
/* 11 */     this.imageId = imageId;
/*    */   }
/*    */   
/*    */   public void setImageW(int imageW) {
/* 15 */     this.imageW = imageW;
/*    */   }
/*    */   
/*    */   public void setImageH(int imageH) {
/* 19 */     this.imageH = imageH;
/*    */   }
/*    */   
/*    */   public void setGlTexture(int glTexture) {
/* 23 */     this.glTexture = glTexture;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean validate() {
/* 28 */     return (super.validate() && this.imageId != null && this.imageW > 0 && this.imageH > 0 && this.glTexture > 0);
/*    */   }
/*    */ 
/*    */   
/*    */   public Widget build() {
/* 33 */     return new ImageWidget(this.location, this.horizontalAnchor, this.verticalAnchor, this.onClick, this.onHover, this.x, this.y, this.scaledOffsetX, this.scaledOffsetY, this.url, this.tooltip, this.scale, this.imageId, this.imageW, this.imageH, this.glTexture, this.noGuiScale);
/*    */   }
/*    */ }


/* Location:              C:\Users\ioanm\Downloads\Xaeros_Minimap_20.15.3_Forge_1.12.jar!\xaero\common\gui\widget\ImageWidgetBuilder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */