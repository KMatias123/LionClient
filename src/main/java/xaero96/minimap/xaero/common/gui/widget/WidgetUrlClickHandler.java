/*    */ package xaero.common.gui.widget;
/*    */ 
/*    */ import java.awt.Desktop;
/*    */ import java.io.IOException;
/*    */ import java.net.URI;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.GuiConfirmOpenLink;
/*    */ import net.minecraft.client.gui.GuiScreen;
/*    */ import net.minecraft.client.gui.GuiYesNoCallback;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class WidgetUrlClickHandler
/*    */   implements WidgetClickHandler, GuiYesNoCallback
/*    */ {
/*    */   private GuiScreen clickedScreen;
/*    */   private String clickedURL;
/*    */   
/*    */   public void onClick(GuiScreen screen, Widget widget) {
/* 20 */     this.clickedScreen = screen;
/* 21 */     this.clickedURL = widget.getUrl();
/*    */     GuiConfirmOpenLink linkScreen;
/* 23 */     Minecraft.func_71410_x().func_147108_a((GuiScreen)(linkScreen = new GuiConfirmOpenLink(this, this.clickedURL, -1, true)));
/* 24 */     linkScreen.func_146358_g();
/*    */   }
/*    */ 
/*    */   
/*    */   public void func_73878_a(boolean confirmed, int id) {
/* 29 */     if (confirmed)
/*    */       try {
/* 31 */         Desktop d = Desktop.getDesktop();
/* 32 */         d.browse(new URI(this.clickedURL));
/* 33 */       } catch (IOException|java.net.URISyntaxException e) {
/* 34 */         e.printStackTrace();
/*    */       }  
/* 36 */     Minecraft.func_71410_x().func_147108_a(this.clickedScreen);
/*    */   }
/*    */ }


/* Location:              C:\Users\ioanm\Downloads\Xaeros_Minimap_20.15.3_Forge_1.12.jar!\xaero\common\gui\widget\WidgetUrlClickHandler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */