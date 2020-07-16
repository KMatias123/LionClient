/*    */ package xaero.map.pool;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ 
/*    */ public abstract class MapPool<T extends PoolUnit>
/*    */ {
/*    */   private int maxSize;
/*    */   private List<T> units;
/*    */   
/*    */   public MapPool(int maxSize) {
/* 12 */     this.maxSize = maxSize;
/* 13 */     this.units = new ArrayList<>();
/*    */   }
/*    */   
/*    */   protected T get(Object... args) {
/* 17 */     T unit = null;
/* 18 */     synchronized (this.units) {
/* 19 */       if (!this.units.isEmpty())
/* 20 */         unit = takeFromPool(); 
/*    */     } 
/* 22 */     if (unit == null) {
/* 23 */       return construct(args);
/*    */     }
/* 25 */     unit.create(args);
/* 26 */     return unit;
/*    */   }
/*    */   
/*    */   public void addToPool(T unit) {
/* 30 */     synchronized (this.units) {
/* 31 */       if (this.units.size() < this.maxSize)
/* 32 */         this.units.add(unit); 
/*    */     } 
/*    */   }
/*    */   
/*    */   private T takeFromPool() {
/* 37 */     return this.units.remove(0);
/*    */   }
/*    */   
/*    */   public int size() {
/* 41 */     return this.units.size();
/*    */   }
/*    */   
/*    */   protected abstract T construct(Object... paramVarArgs);
/*    */ }


/* Location:              C:\Users\ioanm\Downloads\XaerosWorldMap_1.7.3_Forge_1.12.jar!\xaero\map\pool\MapPool.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */