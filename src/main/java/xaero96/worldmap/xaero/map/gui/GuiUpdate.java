/*    */ package xaero.map.gui;
/*    */ 
/*    */ import java.awt.Desktop;
/*    */ import java.io.IOException;
/*    */ import java.net.URI;
/*    */ import java.net.URISyntaxException;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.GuiButton;
/*    */ import net.minecraft.client.gui.GuiYesNo;
/*    */ import net.minecraft.client.resources.I18n;
/*    */ import xaero.map.WorldMap;
/*    */ import xaero.map.settings.ModSettings;
/*    */ 
/*    */ public class GuiUpdate
/*    */   extends GuiYesNo
/*    */ {
/*    */   public GuiUpdate() {
/* 18 */     super(null, "A newer version of Xaero's World Map is available!", "Would you like to update it (open the mod page)?", 0);
/*    */   }
/*    */ 
/*    */   
/*    */   public void func_73866_w_() {
/* 23 */     super.func_73866_w_();
/* 24 */     this.field_146292_n.add(new GuiButton(200, this.field_146294_l / 2 - 100, this.field_146295_m / 6 + 144, 
/* 25 */           I18n.func_135052_a("Don't show again for this update", new Object[0])));
/*    */   }
/*    */   
/*    */   protected void func_146284_a(GuiButton button) throws IOException {
/* 29 */     switch (button.field_146127_k) {
/*    */       case 0:
/* 31 */         if (WorldMap.modJAR == null)
/*    */           return; 
/*    */         try {
/* 34 */           Desktop d = Desktop.getDesktop();
/* 35 */           d.browse(new URI("http://chocolateminecraft.com/update/wmapupdate.html"));
/* 36 */           d.open(WorldMap.modJAR.getParentFile());
/* 37 */         } catch (Exception e) {
/* 38 */           e.printStackTrace();
/*    */         } 
/* 40 */         Minecraft.func_71410_x().func_71400_g();
/*    */         break;
/*    */       case 1:
/* 43 */         this.field_146297_k.func_147108_a(null);
/*    */         break;
/*    */       case 200:
/* 46 */         ModSettings.ignoreUpdate = WorldMap.newestUpdateID;
/* 47 */         WorldMap.settings.saveSettings();
/* 48 */         this.field_146297_k.func_147108_a(null);
/*    */         break;
/*    */       case 201:
/*    */         try {
/* 52 */           Desktop d = Desktop.getDesktop();
/* 53 */           d.browse(new URI((WorldMap.getPatreon()).changelogLink));
/* 54 */         } catch (URISyntaxException e) {
/* 55 */           e.printStackTrace();
/*    */         } 
/*    */         break;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\ioanm\Downloads\XaerosWorldMap_1.7.3_Forge_1.12.jar!\xaero\map\gui\GuiUpdate.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */