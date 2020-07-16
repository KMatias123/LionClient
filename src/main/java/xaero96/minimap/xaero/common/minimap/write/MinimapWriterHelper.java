/*    */ package xaero.common.minimap.write;
/*    */ 
/*    */ public class MinimapWriterHelper
/*    */ {
/*    */   void getBrightestColour(int r, int g, int b, int[] result) {
/*  6 */     int max = Math.max(r, Math.max(g, b));
/*  7 */     if (max == 0) {
/*  8 */       result[0] = r;
/*  9 */       result[1] = g;
/* 10 */       result[2] = b;
/*    */       return;
/*    */     } 
/* 13 */     result[0] = 255 * r / max;
/* 14 */     result[1] = 255 * g / max;
/* 15 */     result[2] = 255 * b / max;
/*    */   }
/*    */ }


/* Location:              C:\Users\ioanm\Downloads\Xaeros_Minimap_20.15.3_Forge_1.12.jar!\xaero\common\minimap\write\MinimapWriterHelper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */