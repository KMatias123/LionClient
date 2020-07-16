/*    */ package xaero.common.gui.widget;
/*    */ 
/*    */ import net.minecraft.client.gui.GuiScreen;
/*    */ 
/*    */ public abstract class WidgetBuilder
/*    */ {
/*    */   protected Class<? extends GuiScreen> location;
/*    */   protected float horizontalAnchor;
/*    */   protected float verticalAnchor;
/* 10 */   protected ClickAction onClick = ClickAction.NOTHING;
/* 11 */   protected HoverAction onHover = HoverAction.NOTHING;
/*    */   
/*    */   protected int x;
/*    */   protected int y;
/*    */   protected String url;
/*    */   protected String tooltip;
/*    */   
/*    */   public void setLocation(Class<? extends GuiScreen> location) {
/* 19 */     this.location = location;
/*    */   }
/*    */   
/*    */   public void setHorizontalAnchor(float horizontalAnchor) {
/* 23 */     this.horizontalAnchor = horizontalAnchor;
/*    */   }
/*    */   
/*    */   public void setVerticalAnchor(float verticalAnchor) {
/* 27 */     this.verticalAnchor = verticalAnchor;
/*    */   }
/*    */   
/*    */   public void setOnClick(ClickAction onClick) {
/* 31 */     this.onClick = onClick;
/*    */   }
/*    */   
/*    */   public void setOnHover(HoverAction onHover) {
/* 35 */     this.onHover = onHover;
/*    */   }
/*    */   
/*    */   public void setX(int x) {
/* 39 */     this.x = x;
/*    */   }
/*    */   
/*    */   public void setY(int y) {
/* 43 */     this.y = y;
/*    */   }
/*    */   
/*    */   public void setUrl(String url) {
/* 47 */     this.url = url;
/*    */   }
/*    */   
/*    */   public void setTooltip(String tooltip) {
/* 51 */     this.tooltip = tooltip;
/*    */   }
/*    */   
/*    */   public boolean validate() {
/* 55 */     return (this.location != null && (this.onHover != HoverAction.TOOLTIP || this.tooltip != null) && (this.onClick != ClickAction.URL || this.url != null));
/*    */   }
/*    */   
/*    */   public abstract Widget build();
/*    */ }


/* Location:              C:\Users\ioanm\Downloads\Xaeros_Minimap_20.15.3_Forge_1.12.jar!\xaero\common\gui\widget\WidgetBuilder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */