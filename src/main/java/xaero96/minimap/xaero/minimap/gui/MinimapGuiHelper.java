/*    */ package xaero.minimap.gui;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.GuiScreen;
/*    */ import xaero.common.IXaeroMinimap;
/*    */ import xaero.common.gui.GuiHelper;
/*    */ import xaero.common.gui.GuiSettings;
/*    */ import xaero.common.gui.MyOptions;
/*    */ 
/*    */ public class MinimapGuiHelper extends GuiHelper {
/*    */   public MinimapGuiHelper(IXaeroMinimap modMain) {
/* 11 */     super(modMain);
/*    */   }
/*    */ 
/*    */   
/*    */   public void openInterfaceSettings(int i) {
/* 16 */     switch (i) {
/*    */       case 4:
/* 18 */         Minecraft.func_71410_x().func_147108_a((GuiScreen)new GuiMinimap(this.modMain, (Minecraft.func_71410_x()).field_71462_r));
/*    */         break;
/*    */     } 
/*    */   }
/*    */   
/*    */   public void onResetCancel() {
/* 24 */     Minecraft.func_71410_x().func_147108_a((GuiScreen)new GuiMinimap6(this.modMain, null));
/*    */   }
/*    */   
/*    */   public MyOptions getMyOptions() {
/* 28 */     return new MyOptions("gui.xaero_minimap_settings", (GuiSettings)new GuiMinimap(this.modMain, (Minecraft.func_71410_x()).field_71462_r), null, (Minecraft.func_71410_x()).field_71474_y);
/*    */   }
/*    */ }


/* Location:              C:\Users\ioanm\Downloads\Xaeros_Minimap_20.15.3_Forge_1.12.jar!\xaero\minimap\gui\MinimapGuiHelper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */