/*    */ package xaero.common.mods;
/*    */ 
/*    */ import xaero.common.IXaeroMinimap;
/*    */ import xaero.minimap.XaeroMinimap;
/*    */ import xaero.pvp.BetterPVP;
/*    */ 
/*    */ public class SupportMods {
/*  8 */   public SupportXaeroWorldmap worldmapSupport = null;
/*    */   
/*    */   private IXaeroMinimap modMain;
/*    */   
/*    */   public boolean worldmap() {
/* 13 */     return (this.worldmapSupport != null);
/*    */   }
/*    */   
/*    */   public boolean shouldUseWorldMapChunks() {
/* 17 */     return (worldmap() && this.modMain.getSettings().getUseWorldMap());
/*    */   }
/*    */   
/*    */   public static void checkForMinimapDuplicates() {
/*    */     try {
/* 22 */       if (XaeroMinimap.instance != null && BetterPVP.instance != null)
/* 23 */         throw new RuntimeException("Better PVP contains Xaero's Minimap by default. Do not install Better PVP and Xaero's Minimap together!"); 
/* 24 */     } catch (NoClassDefFoundError noClassDefFoundError) {}
/*    */   }
/*    */ 
/*    */   
/*    */   public SupportMods(IXaeroMinimap modMain) {
/* 29 */     this.modMain = modMain;
/*    */     try {
/* 31 */       this.worldmapSupport = new SupportXaeroWorldmap(modMain);
/* 32 */       System.out.println("Xaero's Minimap: World Map found!");
/* 33 */     } catch (NoClassDefFoundError noClassDefFoundError) {}
/*    */   }
/*    */ }


/* Location:              C:\Users\ioanm\Downloads\Xaeros_Minimap_20.15.3_Forge_1.12.jar!\xaero\common\mods\SupportMods.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */