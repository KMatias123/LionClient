/*    */ package xaero.map.region;
/*    */ 
/*    */ import net.minecraft.util.math.BlockPos;
/*    */ import net.minecraft.world.World;
/*    */ import xaero.map.MapWriter;
/*    */ 
/*    */ public class Overlay
/*    */   extends MapPixel {
/*    */   private short opacity;
/*    */   private byte intensity;
/*    */   
/*    */   public Overlay(int state, int[] biome, int intensity, byte light, boolean glowing) {
/* 13 */     write(state, biome, intensity, light, glowing);
/*    */   }
/*    */   
/*    */   public void write(int state, int[] biome, int intensity, byte light, boolean glowing) {
/* 17 */     this.opacity = 0;
/* 18 */     this.state = state;
/* 19 */     this.colourType = (byte)biome[0];
/* 20 */     this.customColour = (byte)biome[2];
/* 21 */     this.intensity = (byte)intensity;
/* 22 */     this.light = light;
/* 23 */     this.glowing = glowing;
/*    */   }
/*    */   
/*    */   public boolean isWater() {
/* 27 */     int id = this.state & 0xFFF;
/* 28 */     return ((this.state & 0xFFFF0000) == 0 && (id == 9 || id == 8));
/*    */   }
/*    */ 
/*    */   
/*    */   public int getParametres() {
/* 33 */     int parametres = 0;
/* 34 */     parametres |= !isWater() ? 1 : 0;
/*    */ 
/*    */     
/* 37 */     parametres |= (this.opacity > 1) ? 8 : 0;
/* 38 */     parametres |= this.light << 4;
/* 39 */     parametres |= this.colourType << 8;
/* 40 */     return parametres;
/*    */   }
/*    */   
/*    */   public void getPixelColour(MapBlock block, int[] result_dest, MapWriter mapWriter, World world, MapTileChunk tileChunk, MapTileChunk prevChunk, MapTile prevTile, MapTile mapTile, int x, int z, BlockPos.MutableBlockPos mutableGlobalPos) {
/* 44 */     getPixelColours(result_dest, mapWriter, world, tileChunk, prevChunk, prevTile, mapTile, x, z, block, -1, null, mutableGlobalPos);
/*    */   }
/*    */   
/*    */   public String toString() {
/* 48 */     return "(S: " + getState() + ", CT: " + this.colourType + ", CC: " + getCustomColour() + ", I: " + this.intensity + ", O: " + this.opacity + ", L: " + this.light + ")";
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Overlay p) {
/* 53 */     return (p != null && this.opacity == p.opacity && this.intensity == p.intensity && this.light == p.light && getState() == p.getState());
/*    */   }
/*    */   
/*    */   void fillManagerKeyHolder(Object[] keyHolder) {
/* 57 */     keyHolder[0] = Integer.valueOf(this.state);
/* 58 */     keyHolder[1] = Byte.valueOf(this.colourType);
/* 59 */     keyHolder[2] = Integer.valueOf(this.customColour);
/* 60 */     keyHolder[3] = Byte.valueOf(this.light);
/* 61 */     keyHolder[4] = Short.valueOf(this.opacity);
/*    */   }
/*    */   
/*    */   public int getIntensity() {
/* 65 */     return this.intensity;
/*    */   }
/*    */   
/*    */   public void setIntensity(int intensity) {
/* 69 */     this.intensity = (byte)intensity;
/*    */   }
/*    */   
/*    */   public void increaseOpacity(int toAdd) {
/* 73 */     this.opacity = (short)(this.opacity + toAdd);
/*    */   }
/*    */   
/*    */   public int getOpacity() {
/* 77 */     return this.opacity;
/*    */   }
/*    */ }


/* Location:              C:\Users\ioanm\Downloads\XaerosWorldMap_1.7.3_Forge_1.12.jar!\xaero\map\region\Overlay.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */