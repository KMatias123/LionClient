/*    */ package xaero.map.mods;
/*    */ 
/*    */ public class SupportMods {
/*  4 */   public static SupportXaeroMinimap xaeroMinimap = null;
/*    */   
/*    */   public static boolean minimap() {
/*  7 */     return (xaeroMinimap != null && xaeroMinimap.modMain != null);
/*    */   }
/*    */   
/*    */   public static void load() {
/*    */     try {
/* 12 */       xaeroMinimap = new SupportXaeroMinimap();
/* 13 */     } catch (NoClassDefFoundError noClassDefFoundError) {}
/*    */   }
/*    */ }


/* Location:              C:\Users\ioanm\Downloads\XaerosWorldMap_1.7.3_Forge_1.12.jar!\xaero\map\mods\SupportMods.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */