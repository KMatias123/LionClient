/*    */ package xaero.minimap.gui;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.GuiButton;
/*    */ import net.minecraft.client.gui.GuiScreen;
/*    */ import net.minecraft.client.resources.I18n;
/*    */ import xaero.common.IXaeroMinimap;
/*    */ import xaero.common.gui.GuiMinimapSettings;
/*    */ import xaero.common.gui.GuiWaypoints;
/*    */ import xaero.common.gui.MySmallButton;
/*    */ import xaero.common.settings.ModOptions;
/*    */ import xaero.common.settings.ModSettings;
/*    */ 
/*    */ public class GuiMinimap
/*    */   extends GuiMinimapSettings
/*    */ {
/*    */   private MySmallButton waypointsButton;
/*    */   
/*    */   public GuiMinimap(IXaeroMinimap modMain, GuiScreen par1GuiScreen) {
/* 20 */     super(modMain, par1GuiScreen);
/* 21 */     this.options = new ModOptions[] { ModOptions.MINIMAP, ModOptions.SIZE, ModOptions.ZOOM, ModOptions.DOTS, ModOptions.CAVE_MAPS, ModOptions.WAYPOINTS, ModOptions.NORTH, ModOptions.INGAME_WAYPOINTS, ModOptions.DEATHPOINTS, ModOptions.OLD_DEATHPOINTS, ModOptions.CHUNK_GRID, ModOptions.EDIT };
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
/*    */   public void func_73866_w_() {
/* 37 */     super.func_73866_w_();
/* 38 */     ((GuiButton)this.field_146292_n.get(12)).field_146126_j = I18n.func_135052_a("gui.xaero_change_position", new Object[0]);
/* 39 */     if (ModSettings.serverSettings != ModSettings.defaultSettings)
/* 40 */       this.screenTitle = "§e" + I18n.func_135052_a("gui.xaero_server_disabled", new Object[0]); 
/* 41 */     this.field_146292_n.add(this.waypointsButton = new MySmallButton(201, this.field_146294_l / 2 - 75, this.field_146295_m / 7 + 144, I18n.func_135052_a("gui.xaero_waypoints", new Object[0])));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void func_146284_a(GuiButton par1GuiButton) {
/* 46 */     if (par1GuiButton.field_146124_l && 
/* 47 */       par1GuiButton.field_146127_k == 201)
/*    */     {
/* 49 */       this.field_146297_k.func_147108_a((GuiScreen)new GuiWaypoints(this.modMain, (GuiScreen)this));
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 57 */     super.func_146284_a(par1GuiButton);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void func_73863_a(int par1, int par2, float par3) {
/* 65 */     this.waypointsButton.field_146124_l = ((Minecraft.func_71410_x()).field_71439_g != null && this.modMain.getWaypointsManager().getWaypoints() != null);
/* 66 */     super.func_73863_a(par1, par2, par3);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isNextButtonEnabled() {
/* 71 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isPrevButtonEnabled() {
/* 76 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void onNextButton() {
/* 81 */     this.field_146297_k.func_147108_a((GuiScreen)new GuiMinimap2(this.modMain, this.parentGuiScreen));
/*    */   }
/*    */   
/*    */   protected void onPrevButton() {}
/*    */ }


/* Location:              C:\Users\ioanm\Downloads\Xaeros_Minimap_20.15.3_Forge_1.12.jar!\xaero\minimap\gui\GuiMinimap.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */