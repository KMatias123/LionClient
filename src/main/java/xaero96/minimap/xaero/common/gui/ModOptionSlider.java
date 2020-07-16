/*    */ package xaero.common.gui;
/*    */ 
/*    */ import xaero.common.IXaeroMinimap;
/*    */ import xaero.common.settings.ModOptions;
/*    */ 
/*    */ public class ModOptionSlider
/*    */   extends MyOptionSlider implements ModOptionWidget {
/*    */   private final ModOptions modOption;
/*    */   
/*    */   public ModOptionSlider(ModOptions modOption, IXaeroMinimap modMain, int p_i45017_1_, int p_i45017_2_, int p_i45017_3_) {
/* 11 */     super(modMain, p_i45017_1_, p_i45017_2_, p_i45017_3_, modOption);
/* 12 */     this.modOption = modOption;
/*    */   }
/*    */ 
/*    */   
/*    */   public ModOptions getModOption() {
/* 17 */     return this.modOption;
/*    */   }
/*    */ }


/* Location:              C:\Users\ioanm\Downloads\Xaeros_Minimap_20.15.3_Forge_1.12.jar!\xaero\common\gui\ModOptionSlider.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */