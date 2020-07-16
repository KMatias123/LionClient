/*    */ package xaero.map.file;
/*    */ 
/*    */ import java.io.DataInputStream;
/*    */ import java.io.EOFException;
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class IOHelper
/*    */ {
/*    */   public static void readToBuffer(byte[] buffer, int count, DataInputStream input) throws IOException {
/* 10 */     int currentTotal = 0;
/* 11 */     while (currentTotal < count) {
/* 12 */       int readCount = input.read(buffer, currentTotal, count - currentTotal);
/* 13 */       if (readCount == -1)
/* 14 */         throw new EOFException(); 
/* 15 */       currentTotal += readCount;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\ioanm\Downloads\XaerosWorldMap_1.7.3_Forge_1.12.jar!\xaero\map\file\IOHelper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */