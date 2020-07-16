/*    */ package xaero.map.pool;
/*    */ 
/*    */ import xaero.map.region.MapTile;
/*    */ 
/*    */ public class MapTilePool
/*    */   extends MapPool<MapTile> {
/*    */   public MapTilePool() {
/*  8 */     super(2048);
/*    */   }
/*    */ 
/*    */   
/*    */   protected MapTile construct(Object... args) {
/* 13 */     return new MapTile(args);
/*    */   }
/*    */   
/*    */   public MapTile get(String dimension, int chunkX, int chunkZ) {
/* 17 */     return (MapTile)get(new Object[] { dimension, Integer.valueOf(chunkX), Integer.valueOf(chunkZ) });
/*    */   }
/*    */ }


/* Location:              C:\Users\ioanm\Downloads\XaerosWorldMap_1.7.3_Forge_1.12.jar!\xaero\map\pool\MapTilePool.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */