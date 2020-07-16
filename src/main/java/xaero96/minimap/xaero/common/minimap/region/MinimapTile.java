/*     */ package xaero.common.minimap.region;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import xaero.common.settings.ModSettings;
/*     */ 
/*     */ 
/*     */ public class MinimapTile
/*     */ {
/*  11 */   public static List<MinimapTile> recycled = new ArrayList<>();
/*     */   private boolean wasTransfered;
/*  13 */   private long[][] comparisonCodes = new long[16][16];
/*  14 */   private byte[][] comparisonCodesAdd = new byte[16][16];
/*  15 */   private byte[][] comparisonCodesAdd2 = new byte[16][16];
/*  16 */   private int[][][] red = new int[5][16][16];
/*  17 */   private int[][][] green = new int[5][16][16];
/*  18 */   private int[][][] blue = new int[5][16][16];
/*     */   private boolean chunkGrid;
/*     */   private boolean slimeChunk;
/*     */   private int X;
/*     */   private int Z;
/*     */   private boolean success = true;
/*     */   private int[] lastHeights;
/*     */   private int[] lastSlopeShades;
/*     */   public int caveLevel;
/*     */   
/*     */   public static MinimapTile getANewTile(ModSettings settings, int X, int Z, Long seed) {
/*  29 */     if (recycled.isEmpty()) {
/*  30 */       return new MinimapTile(settings, X, Z, seed);
/*     */     }
/*  32 */     MinimapTile t = recycled.remove(0);
/*  33 */     t.create(settings, X, Z, seed);
/*  34 */     return t;
/*     */   }
/*     */ 
/*     */   
/*     */   public MinimapTile(ModSettings settings, int X, int Z, Long seed) {
/*  39 */     create(settings, X, Z, seed);
/*     */   }
/*     */ 
/*     */   
/*     */   private void create(ModSettings settings, int X, int Z, Long seed) {
/*  44 */     this.X = X;
/*  45 */     this.Z = Z;
/*  46 */     this.chunkGrid = ((X & 0x1) == (Z & 0x1));
/*  47 */     this.slimeChunk = isSlimeChunk(settings, X, Z, seed);
/*  48 */     this.lastHeights = new int[16];
/*  49 */     this.lastSlopeShades = new int[16];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void recycle() {
/*  56 */     this.success = true;
/*  57 */     recycled.add(this);
/*     */   }
/*     */   
/*     */   public static boolean isSlimeChunk(ModSettings settings, int xPosition, int zPosition, Long seed) {
/*     */     try {
/*  62 */       if (seed == null)
/*  63 */         return false; 
/*  64 */       Random rnd = new Random(seed.longValue() + (xPosition * xPosition * 4987142) + (xPosition * 5947611) + (zPosition * zPosition) * 4392871L + (zPosition * 389711) ^ 0x3AD8025FL);
/*     */       
/*  66 */       return (rnd.nextInt(10) == 0);
/*  67 */     } catch (Exception e) {
/*  68 */       return false;
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isWasTransfered() {
/*  73 */     return this.wasTransfered;
/*     */   }
/*     */   
/*     */   public void setWasTransfered(boolean wasTransfered) {
/*  77 */     this.wasTransfered = wasTransfered;
/*     */   }
/*     */   
/*     */   public boolean isChunkGrid() {
/*  81 */     return this.chunkGrid;
/*     */   }
/*     */   
/*     */   public boolean isSlimeChunk() {
/*  85 */     return this.slimeChunk;
/*     */   }
/*     */   
/*     */   public int getRed(int l, int x, int z) {
/*  89 */     return this.red[l][x][z];
/*     */   }
/*     */   
/*     */   public int getGreen(int l, int x, int z) {
/*  93 */     return this.green[l][x][z];
/*     */   }
/*     */   
/*     */   public int getBlue(int l, int x, int z) {
/*  97 */     return this.blue[l][x][z];
/*     */   }
/*     */   
/*     */   public void setRed(int l, int x, int z, int r) {
/* 101 */     this.red[l][x][z] = r;
/*     */   }
/*     */   
/*     */   public void setGreen(int l, int x, int z, int g) {
/* 105 */     this.green[l][x][z] = g;
/*     */   }
/*     */   
/*     */   public void setBlue(int l, int x, int z, int b) {
/* 109 */     this.blue[l][x][z] = b;
/*     */   }
/*     */   
/*     */   public boolean isSuccess() {
/* 113 */     return this.success;
/*     */   }
/*     */   
/*     */   public void setSuccess(boolean success) {
/* 117 */     this.success = success;
/*     */   }
/*     */   
/*     */   public int getX() {
/* 121 */     return this.X;
/*     */   }
/*     */   
/*     */   public int getZ() {
/* 125 */     return this.Z;
/*     */   }
/*     */   
/*     */   public int getLastHeight(int x) {
/* 129 */     return this.lastHeights[x];
/*     */   }
/*     */   
/*     */   public void setLastHeight(int x, int h) {
/* 133 */     this.lastHeights[x] = h;
/*     */   }
/*     */   
/*     */   public int getLastSlopeShade(int x) {
/* 137 */     return this.lastSlopeShades[x];
/*     */   }
/*     */   
/*     */   public void setLastSlopeShade(int x, int s) {
/* 141 */     this.lastSlopeShades[x] = s;
/*     */   }
/*     */   
/*     */   public boolean pixelChanged(int x, int z, long code, byte add, byte add2) {
/* 145 */     return (this.comparisonCodesAdd[x][z] != add || this.comparisonCodesAdd2[x][z] != add2 || this.comparisonCodes[x][z] != code);
/*     */   }
/*     */   
/*     */   public void setCode(int x, int z, long code, byte add, byte add2) {
/* 149 */     this.comparisonCodes[x][z] = code;
/* 150 */     this.comparisonCodesAdd[x][z] = add;
/* 151 */     this.comparisonCodesAdd2[x][z] = add2;
/*     */   }
/*     */ }


/* Location:              C:\Users\ioanm\Downloads\Xaeros_Minimap_20.15.3_Forge_1.12.jar!\xaero\common\minimap\region\MinimapTile.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */