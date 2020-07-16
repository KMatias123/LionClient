/*    */ package xaero.minimap.controls;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.GuiScreen;
/*    */ import net.minecraft.client.settings.KeyBinding;
/*    */ import xaero.common.IXaeroMinimap;
/*    */ import xaero.common.controls.ControlsHandler;
/*    */ import xaero.minimap.gui.GuiMinimap;
/*    */ 
/*    */ public class MinimapControlsHandler extends ControlsHandler {
/* 11 */   public static KeyBinding keyBindSettings = new KeyBinding("gui.xaero_minimap_settings", 21, "Xaero's Minimap");
/*    */   
/*    */   public MinimapControlsHandler(IXaeroMinimap modMain) {
/* 14 */     super(modMain);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void registerKeybindings() {
/* 19 */     this.keybindings.add(keyBindSettings);
/* 20 */     super.registerKeybindings();
/*    */   }
/*    */ 
/*    */   
/*    */   public void keyDownPre(KeyBinding kb) {
/* 25 */     if (kb == keyBindSettings)
/* 26 */       Minecraft.func_71410_x().func_147108_a((GuiScreen)new GuiMinimap(this.modMain, null)); 
/*    */   }
/*    */ }


/* Location:              C:\Users\ioanm\Downloads\Xaeros_Minimap_20.15.3_Forge_1.12.jar!\xaero\minimap\controls\MinimapControlsHandler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */