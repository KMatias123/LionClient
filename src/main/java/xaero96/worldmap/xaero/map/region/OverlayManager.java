/*    */ package xaero.map.region;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ 
/*    */ 
/*    */ public class OverlayManager
/*    */ {
/*    */   private HashMap<Integer, HashMap<Byte, HashMap<Integer, HashMap<Byte, HashMap<Short, Overlay>>>>> overlayMap;
/*  9 */   private int numberOfUniques = 0;
/*    */   private Object[] keyHolder;
/*    */   
/*    */   public OverlayManager() {
/* 13 */     this.overlayMap = new HashMap<>();
/* 14 */     this.keyHolder = new Object[5];
/*    */   }
/*    */   
/*    */   public synchronized Overlay getOriginal(Overlay o) {
/* 18 */     o.fillManagerKeyHolder(this.keyHolder);
/* 19 */     return getOriginal(this.overlayMap, o, 0);
/*    */   }
/*    */ 
/*    */   
/*    */   private Overlay getOriginal(HashMap<Object, Overlay> map, Overlay o, int index) {
/* 24 */     Object<Object, Object> byKey = (Object<Object, Object>)map.get(this.keyHolder[index]);
/* 25 */     if (index == this.keyHolder.length - 1) {
/* 26 */       if (byKey == null) {
/* 27 */         this.numberOfUniques++;
/*    */         
/* 29 */         map.put(this.keyHolder[index], o);
/* 30 */         return o;
/*    */       } 
/* 32 */       return (Overlay)byKey;
/*    */     } 
/* 34 */     if (byKey == null) {
/* 35 */       byKey = (Object<Object, Object>)new HashMap<>();
/* 36 */       map.put(this.keyHolder[index], byKey);
/*    */     } 
/* 38 */     return getOriginal((HashMap)byKey, o, ++index);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getNumberOfUniqueOverlays() {
/* 43 */     return this.numberOfUniques;
/*    */   }
/*    */ }


/* Location:              C:\Users\ioanm\Downloads\XaerosWorldMap_1.7.3_Forge_1.12.jar!\xaero\map\region\OverlayManager.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */