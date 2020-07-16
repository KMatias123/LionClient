/*    */ package xaero.map.animation;
/*    */ 
/*    */ public class SlowingAnimation
/*    */   extends Animation {
/*    */   public static final double animationThing = 16.666666666666668D;
/*    */   private double dest;
/*    */   private double zero;
/*    */   private double factor;
/*    */   
/*    */   public SlowingAnimation(double from, double to, double factor, double zero) {
/* 11 */     super(from, to, 0L);
/* 12 */     this.dest = to;
/* 13 */     this.zero = zero;
/* 14 */     this.factor = factor;
/*    */   }
/*    */   
/*    */   public double getCurrent() {
/* 18 */     double times = (System.currentTimeMillis() - this.start) / 16.666666666666668D;
/* 19 */     double currentOff = this.off * Math.pow(this.factor, times);
/* 20 */     return this.dest - ((Math.abs(currentOff) <= this.zero) ? 0.0D : currentOff);
/*    */   }
/*    */   
/*    */   public double getDestination() {
/* 24 */     return this.dest;
/*    */   }
/*    */ }


/* Location:              C:\Users\ioanm\Downloads\XaerosWorldMap_1.7.3_Forge_1.12.jar!\xaero\map\animation\SlowingAnimation.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */