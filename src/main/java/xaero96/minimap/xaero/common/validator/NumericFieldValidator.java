/*    */ package xaero.common.validator;
/*    */ 
/*    */ import net.minecraft.client.gui.GuiTextField;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class NumericFieldValidator
/*    */ {
/* 10 */   private StringBuilder stringBuilder = new StringBuilder();
/*    */ 
/*    */   
/*    */   private boolean charIsValid(char c, int index) {
/* 14 */     return ((c >= '0' && c <= '9') || (c == '-' && index == 0));
/*    */   }
/*    */   
/*    */   public void validate(GuiTextField field) {
/* 18 */     String text = field.func_146179_b();
/* 19 */     char[] charArray = text.toCharArray();
/* 20 */     this.stringBuilder.delete(0, this.stringBuilder.length());
/* 21 */     boolean validated = true;
/* 22 */     for (int i = 0; i < charArray.length; i++) {
/* 23 */       if (!charIsValid(charArray[i], i)) {
/* 24 */         validated = false;
/*    */       } else {
/* 26 */         this.stringBuilder.append(charArray[i]);
/*    */       } 
/* 28 */     }  boolean validFormat = false;
/* 29 */     while (!validFormat) {
/*    */       try {
/* 31 */         if (this.stringBuilder.length() != 0 && (this.stringBuilder.length() != 1 || this.stringBuilder.charAt(0) != '-'))
/* 32 */           Integer.parseInt(this.stringBuilder.toString()); 
/* 33 */         validFormat = true;
/* 34 */       } catch (NumberFormatException e) {
/* 35 */         this.stringBuilder.deleteCharAt(this.stringBuilder.length() - 1);
/* 36 */         validated = false;
/*    */       } 
/*    */     } 
/* 39 */     if (!validated)
/* 40 */       field.func_146180_a(this.stringBuilder.toString()); 
/*    */   }
/*    */ }


/* Location:              C:\Users\ioanm\Downloads\Xaeros_Minimap_20.15.3_Forge_1.12.jar!\xaero\common\validator\NumericFieldValidator.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */