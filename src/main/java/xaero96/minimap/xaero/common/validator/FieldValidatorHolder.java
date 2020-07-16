/*    */ package xaero.common.validator;
/*    */ 
/*    */ 
/*    */ public class FieldValidatorHolder
/*    */ {
/*    */   private NumericFieldValidator numericFieldValidator;
/*    */   
/*    */   public FieldValidatorHolder(NumericFieldValidator numericFieldValidator) {
/*  9 */     this.numericFieldValidator = numericFieldValidator;
/*    */   }
/*    */   
/*    */   public NumericFieldValidator getNumericFieldValidator() {
/* 13 */     return this.numericFieldValidator;
/*    */   }
/*    */ }


/* Location:              C:\Users\ioanm\Downloads\Xaeros_Minimap_20.15.3_Forge_1.12.jar!\xaero\common\validator\FieldValidatorHolder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */