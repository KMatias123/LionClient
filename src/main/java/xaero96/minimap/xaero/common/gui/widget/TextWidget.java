/*    */ package xaero.common.gui.widget;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.GuiScreen;
/*    */ 
/*    */ 
/*    */ public class TextWidget
/*    */   extends ScalableWidget
/*    */ {
/*    */   private String text;
/*    */   private Alignment alignment;
/*    */   
/*    */   public TextWidget(Class<? extends GuiScreen> location, float horizontalAnchor, float verticalAnchor, ClickAction onClick, HoverAction onHover, int x, int y, int scaledOffsetX, int scaledOffsetY, String url, String tooltip, String text, Alignment alignment, boolean noGuiScale, double scale) {
/* 14 */     super(WidgetType.TEXT, location, horizontalAnchor, verticalAnchor, onClick, onHover, x, y, scaledOffsetX, scaledOffsetY, url, tooltip, noGuiScale, scale);
/* 15 */     this.text = text;
/* 16 */     this.alignment = alignment;
/*    */   }
/*    */   
/*    */   public String getText() {
/* 20 */     return this.text;
/*    */   }
/*    */   
/*    */   public Alignment getAlignment() {
/* 24 */     return this.alignment;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getW() {
/* 29 */     return (Minecraft.func_71410_x()).field_71466_p.func_78256_a(this.text);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getH() {
/* 34 */     return 10;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getScaledOffsetX() {
/* 39 */     int pos = super.getScaledOffsetX();
/* 40 */     if (this.alignment == Alignment.RIGHT) {
/* 41 */       pos -= (Minecraft.func_71410_x()).field_71466_p.func_78256_a(this.text);
/* 42 */     } else if (this.alignment == Alignment.CENTER) {
/* 43 */       pos -= (Minecraft.func_71410_x()).field_71466_p.func_78256_a(this.text) / 2;
/* 44 */     }  return pos;
/*    */   }
/*    */ }


/* Location:              C:\Users\ioanm\Downloads\Xaeros_Minimap_20.15.3_Forge_1.12.jar!\xaero\common\gui\widget\TextWidget.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */