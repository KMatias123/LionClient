/*    */ package xaero.common.gui;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.GuiScreen;
/*    */ import xaero.common.IXaeroMinimap;
/*    */ import xaero.common.settings.ModOptions;
/*    */ 
/*    */ public abstract class GuiHelper
/*    */ {
/*    */   protected IXaeroMinimap modMain;
/*    */   
/*    */   public GuiHelper(IXaeroMinimap modMain) {
/* 13 */     this.modMain = modMain;
/*    */   }
/*    */   
/*    */   public void openSettingsGui(ModOptions returnModOptions) {
/* 17 */     GuiScreen current = (Minecraft.func_71410_x()).field_71462_r;
/* 18 */     if (returnModOptions == ModOptions.EDIT) {
/* 19 */       Minecraft.func_71410_x().func_147108_a(new GuiEditMode(this.modMain, current, "gui.xaero_minimap_guide", false));
/* 20 */     } else if (returnModOptions == ModOptions.RESET) {
/* 21 */       Minecraft.func_71410_x().func_147108_a((GuiScreen)new GuiReset(this.modMain));
/* 22 */     } else if (returnModOptions == ModOptions.DOTS) {
/* 23 */       Minecraft.func_71410_x().func_147108_a(new GuiDotColors(this.modMain, current));
/* 24 */     } else if (returnModOptions == ModOptions.WAYPOINTS_DEFAULT_TP) {
/* 25 */       Minecraft.func_71410_x().func_147108_a(new GuiDefaultTpCommand(this.modMain, current));
/*    */     } 
/*    */   }
/*    */   
/*    */   public abstract void openInterfaceSettings(int paramInt);
/*    */   
/*    */   public abstract void onResetCancel();
/*    */   
/*    */   public abstract MyOptions getMyOptions();
/*    */ }


/* Location:              C:\Users\ioanm\Downloads\Xaeros_Minimap_20.15.3_Forge_1.12.jar!\xaero\common\gui\GuiHelper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */