/*    */ package xaero.map.animation;
/*    */ 
/*    */ public class SinAnimation
/*    */   extends Animation {
/*    */   public SinAnimation(double from, double to, long time) {
/*  6 */     super(from, to, time);
/*    */   }
/*    */   
/*    */   public double getCurrent() {
/* 10 */     double passed = Math.min(1.0D, (System.currentTimeMillis() - this.start) / this.time);
/* 11 */     double angle = 1.5707963267948966D * passed;
/* 12 */     return this.from + this.off * Math.sin(angle);
/*    */   }
/*    */ }


/* Location:              C:\Users\ioanm\Downloads\XaerosWorldMap_1.7.3_Forge_1.12.jar!\xaero\map\animation\SinAnimation.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */