/*    */ package xaero.common.gui;
/*    */ 
/*    */ import xaero.common.settings.ModOptions;
/*    */ 
/*    */ public class ModOptionButton
/*    */   extends MySmallButton implements ModOptionWidget {
/*    */   private final ModOptions modOption;
/*    */   
/*    */   public ModOptionButton(ModOptions modOption, int id, int p_i51132_1_, int p_i51132_2_, String p_i51132_6_) {
/* 10 */     super(id, p_i51132_1_, p_i51132_2_, modOption, p_i51132_6_);
/* 11 */     this.modOption = modOption;
/*    */   }
/*    */ 
/*    */   
/*    */   public ModOptions getModOption() {
/* 16 */     return this.modOption;
/*    */   }
/*    */ }


/* Location:              C:\Users\ioanm\Downloads\Xaeros_Minimap_20.15.3_Forge_1.12.jar!\xaero\common\gui\ModOptionButton.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */