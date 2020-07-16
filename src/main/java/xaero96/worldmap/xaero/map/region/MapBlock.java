/*     */ package xaero.map.region;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.world.World;
/*     */ import xaero.map.MapWriter;
/*     */ 
/*     */ public class MapBlock
/*     */   extends MapPixel {
/*     */   private byte heightType;
/*     */   private byte signed_height;
/*     */   private ArrayList<Overlay> overlays;
/*     */   private boolean caveBlock = false;
/*  15 */   private int biome = -1;
/*     */   
/*     */   public boolean isGrass() {
/*  18 */     return ((this.state & 0xFFFF0000) == 0 && (this.state & 0xFFF) == 2);
/*     */   }
/*     */   
/*     */   public int getParametres() {
/*  22 */     int parametres = 0;
/*  23 */     parametres |= !isGrass() ? 1 : 0;
/*  24 */     parametres |= (getNumberOfOverlays() != 0) ? 2 : 0;
/*  25 */     parametres |= this.colourType << 2;
/*  26 */     parametres |= this.heightType << 4;
/*     */     
/*  28 */     parametres |= this.caveBlock ? 128 : 0;
/*  29 */     parametres |= this.light << 8;
/*  30 */     parametres |= getHeight() << 12;
/*  31 */     parametres |= (this.biome != -1) ? 1048576 : 0;
/*  32 */     return parametres;
/*     */   }
/*     */   
/*     */   public void getPixelColour(int[] result_dest, MapWriter mapWriter, World world, MapTileChunk tileChunk, MapTileChunk prevChunk, MapTile prevTile, MapTile mapTile, int x, int z, BlockPos.MutableBlockPos mutableGlobalPos) {
/*  36 */     getPixelColours(result_dest, mapWriter, world, tileChunk, prevChunk, prevTile, mapTile, x, z, this, getHeight(), this.overlays, mutableGlobalPos);
/*     */   }
/*     */   
/*     */   public String toString() {
/*  40 */     return "ID: " + Block.field_176229_d.func_148747_b(Block.func_176220_d(getState())) + ", S: " + getState() + ", HT: " + this.heightType + ", H: " + getHeight() + ", CT: " + this.colourType + ", B: " + this.biome + ", CC: " + this.customColour + ", L: " + this.light + ", G: " + this.glowing + ", CB: " + this.caveBlock + ", O: " + getNumberOfOverlays();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(MapBlock p) {
/*  45 */     boolean equal = (p != null && this.state == p.state && this.colourType == p.colourType && this.light == p.light && this.signed_height == p.signed_height && this.caveBlock == p.caveBlock && this.heightType == p.heightType && getNumberOfOverlays() == p.getNumberOfOverlays() && this.biome == p.biome && (this.colourType != 3 || this.customColour == p.customColour));
/*  46 */     if (equal && getNumberOfOverlays() != 0)
/*  47 */       for (int i = 0; i < this.overlays.size(); i++) {
/*  48 */         if (!((Overlay)this.overlays.get(i)).equals(p.overlays.get(i)))
/*  49 */           return false; 
/*     */       }  
/*  51 */     return equal;
/*     */   }
/*     */   
/*     */   public void fixHeightType(int x, int z, MapTile prevTile, MapTile mapTile, MapTileChunk tileChunk, MapTileChunk prevChunk) {
/*  55 */     int prevHeight = -1;
/*  56 */     if (z == 0) {
/*  57 */       if (prevTile != null && prevTile.isLoaded()) {
/*  58 */         prevHeight = prevTile.getBlock(x, 15).getHeight();
/*  59 */       } else if (prevChunk != null && (mapTile.getChunkZ() & 0x1F) == 0) {
/*  60 */         int inTileChunkX = (mapTile.getChunkX() & 0x3) * 16 + x;
/*  61 */         prevHeight = prevChunk.getHeight(inTileChunkX, 63);
/*     */       } 
/*     */     } else {
/*  64 */       MapBlock prevPixel = mapTile.getBlock(x, z - 1);
/*  65 */       prevHeight = prevPixel.getHeight();
/*     */     } 
/*     */ 
/*     */     
/*  69 */     int height = getHeight();
/*  70 */     if (prevHeight != -1) {
/*  71 */       if (height > prevHeight)
/*  72 */       { this.heightType = 1; }
/*  73 */       else if (height != prevHeight)
/*  74 */       { this.heightType = 2; }
/*     */       else
/*  76 */       { this.heightType = 0; } 
/*  77 */     } else if (z == 0 && (mapTile.getChunkZ() & 0x3) == 0) {
/*  78 */       tileChunk.setUnsuccessful(mapTile.getChunkX() & 0x3);
/*     */     } 
/*     */   }
/*     */   public void prepareForWriting() {
/*  82 */     if (this.overlays != null)
/*  83 */       this.overlays.clear(); 
/*  84 */     this.customColour = 0;
/*  85 */     this.colourType = 0;
/*  86 */     this.biome = -1;
/*  87 */     this.state = 0;
/*  88 */     this.heightType = 3;
/*  89 */     this.light = 0;
/*  90 */     this.glowing = false;
/*  91 */     this.signed_height = 0;
/*     */   }
/*     */   
/*     */   public void write(int state, int height, int[] biomeStuff, byte light, boolean glowing, boolean cave) {
/*  95 */     this.state = state;
/*  96 */     setHeight(height);
/*  97 */     setColourType((byte)biomeStuff[0]);
/*  98 */     if (biomeStuff[1] != -1)
/*  99 */       this.biome = biomeStuff[1]; 
/* 100 */     setCustomColour(biomeStuff[2]);
/* 101 */     this.light = light;
/* 102 */     this.glowing = glowing;
/* 103 */     this.caveBlock = (cave && !glowing);
/* 104 */     if (this.overlays != null && this.overlays.isEmpty())
/* 105 */       this.overlays = null; 
/*     */   }
/*     */   
/*     */   public void addOverlay(Overlay o) {
/* 109 */     if (this.overlays == null)
/* 110 */       this.overlays = new ArrayList<>(); 
/* 111 */     this.overlays.add(o);
/*     */   }
/*     */   
/*     */   public int getHeight() {
/* 115 */     return this.signed_height & 0xFF;
/*     */   }
/*     */   
/*     */   public byte getSignedHeight() {
/* 119 */     return this.signed_height;
/*     */   }
/*     */   
/*     */   public void setHeight(int h) {
/* 123 */     this.signed_height = (byte)h;
/*     */   }
/*     */   
/*     */   public int getBiome() {
/* 127 */     return this.biome;
/*     */   }
/*     */   
/*     */   public void setBiome(int biome) {
/* 131 */     this.biome = biome;
/*     */   }
/*     */   
/*     */   public ArrayList<Overlay> getOverlays() {
/* 135 */     return this.overlays;
/*     */   }
/*     */   
/*     */   public byte getHeightType() {
/* 139 */     return this.heightType;
/*     */   }
/*     */   
/*     */   public void setHeightType(byte heightType) {
/* 143 */     this.heightType = heightType;
/*     */   }
/*     */   
/*     */   public boolean isCaveBlock() {
/* 147 */     return this.caveBlock;
/*     */   }
/*     */   
/*     */   public void setCaveBlock(boolean caveBlock) {
/* 151 */     this.caveBlock = caveBlock;
/*     */   }
/*     */   
/*     */   public int getNumberOfOverlays() {
/* 155 */     return (this.overlays == null) ? 0 : this.overlays.size();
/*     */   }
/*     */ }


/* Location:              C:\Users\ioanm\Downloads\XaerosWorldMap_1.7.3_Forge_1.12.jar!\xaero\map\region\MapBlock.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */