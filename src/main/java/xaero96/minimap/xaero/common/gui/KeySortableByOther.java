/*    */ package xaero.common.gui;
/*    */ 
/*    */ 
/*    */ public class KeySortableByOther<T>
/*    */   implements Comparable<KeySortableByOther>
/*    */ {
/*    */   private T key;
/*    */   private Comparable[] dataToSortBy;
/*    */   
/*    */   public KeySortableByOther(T key, Comparable... dataToSortBy) {
/* 11 */     this.key = key;
/* 12 */     this.dataToSortBy = dataToSortBy;
/*    */   }
/*    */   
/*    */   public T getKey() {
/* 16 */     return this.key;
/*    */   }
/*    */   
/*    */   public Comparable[] getDataToSortBy() {
/* 20 */     return this.dataToSortBy;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int compareTo(KeySortableByOther arg0) {
/* 26 */     Comparable[] otherData = arg0.getDataToSortBy();
/* 27 */     for (int i = 0; i < this.dataToSortBy.length; i++) {
/* 28 */       int comparison = this.dataToSortBy[i].compareTo(otherData[i]);
/* 29 */       if (comparison != 0)
/* 30 */         return comparison; 
/*    */     } 
/* 32 */     return 0;
/*    */   }
/*    */ }


/* Location:              C:\Users\ioanm\Downloads\Xaeros_Minimap_20.15.3_Forge_1.12.jar!\xaero\common\gui\KeySortableByOther.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */