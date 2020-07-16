/*    */ package xaero.map.region;
/*    */ 
/*    */ import xaero.map.pool.PoolUnit;
/*    */ 
/*    */ public class MapTile implements PoolUnit {
/*    */   private boolean loaded;
/*    */   private int chunkX;
/*    */   private int chunkZ;
/*    */   private MapBlock[][] blocks;
/*    */   private MapTile prevTile;
/*    */   private boolean writtenOnce;
/*    */   
/*    */   public MapTile(Object... args) {
/* 14 */     this.blocks = new MapBlock[16][16];
/* 15 */     create(args);
/*    */   }
/*    */ 
/*    */   
/*    */   public void create(Object... args) {
/* 20 */     this.chunkX = ((Integer)args[1]).intValue();
/* 21 */     this.chunkZ = ((Integer)args[2]).intValue();
/* 22 */     this.loaded = false;
/* 23 */     this.writtenOnce = false;
/* 24 */     this.prevTile = null;
/*    */   }
/*    */   
/*    */   public boolean isLoaded() {
/* 28 */     return this.loaded;
/*    */   }
/*    */   
/*    */   public void setLoaded(boolean loaded) {
/* 32 */     this.loaded = loaded;
/*    */   }
/*    */   
/*    */   public MapTile getPrevTile() {
/* 36 */     return this.prevTile;
/*    */   }
/*    */   
/*    */   public void setPrevTile(MapTile prevTile) {
/* 40 */     this.prevTile = prevTile;
/*    */   }
/*    */   
/*    */   public MapBlock getBlock(int x, int z) {
/* 44 */     return this.blocks[x][z];
/*    */   }
/*    */   
/*    */   public MapBlock[] getBlockColumn(int x) {
/* 48 */     return this.blocks[x];
/*    */   }
/*    */   
/*    */   public void setBlock(int x, int z, MapBlock block) {
/* 52 */     this.blocks[x][z] = block;
/*    */   }
/*    */   
/*    */   public int getChunkX() {
/* 56 */     return this.chunkX;
/*    */   }
/*    */   
/*    */   public int getChunkZ() {
/* 60 */     return this.chunkZ;
/*    */   }
/*    */   
/*    */   public boolean wasWrittenOnce() {
/* 64 */     return this.writtenOnce;
/*    */   }
/*    */   
/*    */   public void setWrittenOnce(boolean writtenOnce) {
/* 68 */     this.writtenOnce = writtenOnce;
/*    */   }
/*    */ }


/* Location:              C:\Users\ioanm\Downloads\XaerosWorldMap_1.7.3_Forge_1.12.jar!\xaero\map\region\MapTile.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */