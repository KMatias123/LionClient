/*    */ package xaero.common.gui;
/*    */ 
/*    */ public class GuiDropdownHelper
/*    */ {
/*    */   protected int current;
/*    */   protected int auto;
/*    */   protected String[] keys;
/*    */   protected String[] options;
/*    */   
/*    */   public String getCurrentKey() {
/* 11 */     return this.keys[this.current];
/*    */   }
/*    */   
/*    */   public String getCurrentName() {
/* 15 */     return this.options[this.current];
/*    */   }
/*    */ }


/* Location:              C:\Users\ioanm\Downloads\Xaeros_Minimap_20.15.3_Forge_1.12.jar!\xaero\common\gui\GuiDropdownHelper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */