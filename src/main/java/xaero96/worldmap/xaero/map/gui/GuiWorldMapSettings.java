/*    */ package xaero.map.gui;
/*    */ 
/*    */ import java.awt.Desktop;
/*    */ import java.io.IOException;
/*    */ import net.minecraft.client.gui.GuiButton;
/*    */ import net.minecraft.client.gui.GuiScreen;
/*    */ import org.lwjgl.opengl.OpenGLException;
/*    */ import xaero.map.MapProcessor;
/*    */ import xaero.map.WorldMap;
/*    */ import xaero.map.settings.ModOptions;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class GuiWorldMapSettings
/*    */   extends GuiSettings
/*    */ {
/*    */   public GuiButton pngButton;
/*    */   
/*    */   public GuiWorldMapSettings() {
/* 20 */     this((GuiScreen)null);
/*    */   }
/*    */   
/*    */   public GuiWorldMapSettings(GuiScreen parent) {
/* 24 */     super(parent, WorldMap.settings);
/* 25 */     this.screenTitle = "Xaero's World Map";
/* 26 */     this.options = new ModOptions[] { ModOptions.DEBUG, ModOptions.LIGHTING, ModOptions.COLOURS, ModOptions.LOAD, ModOptions.DEPTH, ModOptions.UPDATE, ModOptions.SLOPES, ModOptions.STEPS, ModOptions.FLOWERS, ModOptions.COMPRESSION, ModOptions.COORDINATES, ModOptions.BIOMES, ModOptions.WAYPOINTS, ModOptions.ARROW, ModOptions.DISPLAY_ZOOM };
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void func_73866_w_() {
/* 39 */     super.func_73866_w_();
/* 40 */     this.field_146292_n.add(this.pngButton = new MySmallButton(201, this.field_146294_l / 2 + 5, this.field_146295_m / 12 + 168, "Export PNG"));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void func_146284_a(GuiButton par1GuiButton) {
/* 48 */     super.func_146284_a(par1GuiButton);
/* 49 */     if (par1GuiButton.field_146127_k == 201) {
/* 50 */       this.pngButton.field_146124_l = false;
/*    */       try {
/* 52 */         MapProcessor.instance.getMapSaveLoad().exportPNG();
/* 53 */         Desktop d = Desktop.getDesktop();
/*    */         try {
/* 55 */           d.open(WorldMap.configFolder.toPath().getParent().resolve("map exports").toFile());
/* 56 */         } catch (IOException e) {
/* 57 */           e.printStackTrace();
/*    */         } 
/* 59 */       } catch (OpenGLException e) {
/* 60 */         MapProcessor.instance.setCrashedBy((Throwable)e);
/*    */       } 
/* 62 */       this.pngButton.field_146124_l = true;
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void func_73863_a(int par1, int par2, float par3) {
/* 90 */     super.func_73863_a(par1, par2, par3);
/* 91 */     for (int k = 0; k < this.field_146292_n.size(); k++) {
/* 92 */       GuiButton b = this.field_146292_n.get(k);
/* 93 */       if (par1 < b.field_146128_h || par2 < b.field_146129_i || par1 >= b.field_146128_h + 150 || par2 < b.field_146129_i + 20);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\ioanm\Downloads\XaerosWorldMap_1.7.3_Forge_1.12.jar!\xaero\map\gui\GuiWorldMapSettings.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */