/*    */ package xaero.map.graphics;
/*    */ 
/*    */ import org.lwjgl.opengl.GL11;
/*    */ import xaero.map.Misc;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class TextureUploadBenchmark
/*    */ {
/*    */   private long[] accumulators;
/*    */   private long[] results;
/*    */   private int[] totals;
/*    */   private boolean[] finished;
/*    */   private int[] nOfElements;
/*    */   private int nOfFinished;
/*    */   private boolean allFinished;
/*    */   
/*    */   public TextureUploadBenchmark(int... nOfElements) {
/* 22 */     int nOfTypes = nOfElements.length;
/* 23 */     this.accumulators = new long[nOfTypes];
/* 24 */     this.totals = new int[nOfTypes];
/* 25 */     this.results = new long[nOfTypes];
/* 26 */     this.finished = new boolean[nOfTypes];
/* 27 */     this.nOfElements = nOfElements;
/*    */   }
/*    */   
/*    */   public void pre() {
/* 31 */     Misc.timerPre();
/*    */   }
/*    */   
/*    */   public void post(int type) {
/* 35 */     GL11.glFinish();
/* 36 */     int passed = Misc.timerResult();
/* 37 */     this.accumulators[type] = this.accumulators[type] + passed;
/* 38 */     this.totals[type] = this.totals[type] + 1;
/* 39 */     if (this.totals[type] == this.nOfElements[type])
/*    */     {
/* 41 */       finish(type);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   private void finish(int type) {
/* 47 */     this.results[type] = this.accumulators[type] / this.totals[type];
/* 48 */     this.finished[type] = true;
/* 49 */     this.nOfFinished++;
/* 50 */     if (this.nOfFinished == this.finished.length)
/* 51 */       this.allFinished = true; 
/*    */   }
/*    */   
/*    */   public boolean isFinished() {
/* 55 */     return this.allFinished;
/*    */   }
/*    */   
/*    */   public boolean isFinished(int type) {
/* 59 */     return this.finished[type];
/*    */   }
/*    */   
/*    */   public long getAverage(int type) {
/* 63 */     if (this.finished[type]) {
/* 64 */       return this.results[type];
/*    */     }
/* 66 */     return this.accumulators[type] / this.totals[type];
/*    */   }
/*    */ }


/* Location:              C:\Users\ioanm\Downloads\XaerosWorldMap_1.7.3_Forge_1.12.jar!\xaero\map\graphics\TextureUploadBenchmark.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */