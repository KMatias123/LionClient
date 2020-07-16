/*    */ package xaero.minimap.interfaces;
/*    */ 
/*    */ import xaero.common.IXaeroMinimap;
/*    */ import xaero.common.interfaces.IInterfaceLoader;
/*    */ import xaero.common.interfaces.Interface;
/*    */ import xaero.common.interfaces.InterfaceManager;
/*    */ import xaero.common.interfaces.Preset;
/*    */ import xaero.common.minimap.MinimapInterface;
/*    */ import xaero.common.settings.ModOptions;
/*    */ 
/*    */ public class MinimapInterfaceLoader
/*    */   implements IInterfaceLoader
/*    */ {
/*    */   public void loadPresets(InterfaceManager interfaces) {
/* 15 */     interfaces.addPreset(new Preset("gui.xaero_preset_topleft", new int[][] { { 0, 0 }, , { 0, 10000 }, , { 0, 0 }, , { 0, 36 }, , { 0, 0 },  }, new boolean[][] { { true, false, false }, { true, false, false }, { false, true, false }, { true, false, false }, { false, false, false } }));
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
/* 27 */     interfaces.addPreset(new Preset("gui.xaero_preset_topright", new int[][] { { 0, 0 }, , { 0, 135 }, , { 120, 0 }, , { 0, 50 }, , { 0, 0 },  }, new boolean[][] { { false, true, false }, { false, false, false }, { true, false, false }, { true, false, false }, { false, true, false } }));
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
/* 39 */     interfaces.addPreset(new Preset("gui.xaero_preset_bottom_left", new int[][] { { 0, 0 }, , { 0, 135 }, , { 120, 0 }, , { 0, 50 }, , { 0, 10000 },  }, new boolean[][] { { false, true, false }, { false, false, false }, { true, false, false }, { true, false, false }, { false, false, false } }));
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
/* 51 */     interfaces.addPreset(new Preset("gui.xaero_preset_bottom_right", new int[][] { { 0, 0 }, , { 0, 135 }, , { 120, 0 }, , { 0, 50 }, , { 0, 10000 },  }, new boolean[][] { { false, true, false }, { false, false, false }, { true, false, false }, { true, false, false }, { false, true, false } }));
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
/*    */   public void load(IXaeroMinimap modMain, InterfaceManager interfaces) {
/* 67 */     for (int i = 0; i < 4; i++) {
/* 68 */       interfaces.add(new Interface(interfaces, "dummy", interfaces.getNextId(), 0, 0, ModOptions.DEFAULT) {  });
/* 69 */     }  interfaces.add((Interface)new MinimapInterface(modMain, interfaces.getNextId(), interfaces));
/*    */   }
/*    */ }


/* Location:              C:\Users\ioanm\Downloads\Xaeros_Minimap_20.15.3_Forge_1.12.jar!\xaero\minimap\interfaces\MinimapInterfaceLoader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */