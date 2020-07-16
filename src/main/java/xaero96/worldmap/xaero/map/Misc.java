/*    */ package xaero.map;
/*    */ 
/*    */ import net.minecraft.block.Block;
/*    */ import net.minecraft.block.state.IBlockState;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.init.Blocks;
/*    */ import org.lwjgl.input.Mouse;
/*    */ import org.lwjgl.opengl.GL11;
/*    */ 
/*    */ 
/*    */ public class Misc
/*    */ {
/*    */   private static long cpuTimerPreTime;
/*    */   private static long glTimerPreTime;
/*    */   
/*    */   public static double round(double a, int komaarvu) {
/* 17 */     double x = Math.pow(10.0D, komaarvu);
/* 18 */     return Math.round(a * x) / x;
/*    */   }
/*    */   
/*    */   public static IBlockState getStateById(int id) {
/*    */     try {
/* 23 */       return Block.func_176220_d(id);
/* 24 */     } catch (Exception e) {
/* 25 */       return getDefaultBlockStateForStateId(id);
/*    */     } 
/*    */   }
/*    */   
/*    */   private static IBlockState getDefaultBlockStateForStateId(int id) {
/*    */     try {
/* 31 */       Block block = Block.func_176220_d(id).func_177230_c();
/* 32 */       return block.func_176223_P();
/* 33 */     } catch (Exception e) {
/* 34 */       return Blocks.field_150350_a.func_176223_P();
/*    */     } 
/*    */   }
/*    */   
/*    */   public static void glTimerPre() {
/* 39 */     GL11.glFinish();
/* 40 */     glTimerPreTime = System.nanoTime();
/*    */   }
/*    */   
/*    */   public static int glTimerResult() {
/* 44 */     GL11.glFinish();
/* 45 */     return (int)(System.nanoTime() - glTimerPreTime);
/*    */   }
/*    */   
/*    */   public static void timerPre() {
/* 49 */     cpuTimerPreTime = System.nanoTime();
/*    */   }
/*    */   
/*    */   public static int timerResult() {
/* 53 */     return (int)(System.nanoTime() - cpuTimerPreTime);
/*    */   }
/*    */   
/*    */   public static double getMouseX(Minecraft mc) {
/* 57 */     return Mouse.getX();
/*    */   }
/*    */   
/*    */   public static double getMouseY(Minecraft mc) {
/* 61 */     return (mc.field_71440_d - Mouse.getY() - 1);
/*    */   }
/*    */ }


/* Location:              C:\Users\ioanm\Downloads\XaerosWorldMap_1.7.3_Forge_1.12.jar!\xaero\map\Misc.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */