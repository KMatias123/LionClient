/*    */ package xaero.common.anim;
/*    */ 
/*    */ public class OldAnimation
/*    */ {
/*  5 */   public static long lastTick = System.currentTimeMillis();
/*    */   public static final double animationThing = 16.666666666666668D;
/*    */   
/*    */   public static void tick() {
/*  9 */     lastTick = System.currentTimeMillis();
/*    */   }
/*    */ 
/*    */   
/*    */   public static double animate(double a, double factor) {
/* 14 */     double times = (System.currentTimeMillis() - lastTick) / 16.666666666666668D;
/*    */     
/* 16 */     a *= Math.pow(factor, times);
/* 17 */     return a;
/*    */   }
/*    */ }


/* Location:              C:\Users\ioanm\Downloads\Xaeros_Minimap_20.15.3_Forge_1.12.jar!\xaero\common\anim\OldAnimation.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */