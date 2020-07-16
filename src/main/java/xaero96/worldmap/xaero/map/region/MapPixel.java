/*     */ package xaero.map.region;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ import net.minecraft.world.World;
/*     */ import xaero.map.MapWriter;
/*     */ import xaero.map.Misc;
/*     */ import xaero.map.WorldMap;
/*     */ 
/*     */ public class MapPixel
/*     */ {
/*  14 */   protected int state = 0;
/*  15 */   protected byte colourType = -1;
/*     */   
/*     */   protected int customColour;
/*  18 */   protected byte light = 0;
/*     */ 
/*     */   
/*     */   protected boolean glowing = false;
/*     */ 
/*     */   
/*     */   public void getPixelColours(int[] result_dest, MapWriter mapWriter, World world, MapTileChunk tileChunk, MapTileChunk prevChunk, MapTile prevTile, MapTile mapTile, int x, int z, MapBlock block, int height, ArrayList<Overlay> overlays, BlockPos.MutableBlockPos mutableGlobalPos) {
/*  25 */     int colour = 0;
/*  26 */     int topLightValue = this.light;
/*  27 */     float brightness = 1.0F;
/*  28 */     mutableGlobalPos.func_181079_c(mapTile.getChunkX() * 16 + x, height, mapTile.getChunkZ() * 16 + z);
/*  29 */     if (this.state != 0) {
/*  30 */       if (WorldMap.settings.colours == 0) {
/*  31 */         colour = mapWriter.loadBlockColourFromTexture(this.state, true, world, (BlockPos)mutableGlobalPos);
/*     */       } else {
/*     */         try {
/*  34 */           IBlockState blockState = Misc.getStateById(this.state);
/*  35 */           if (blockState.func_177230_c() instanceof net.minecraft.block.BlockLiquid && blockState.func_177230_c().getLightOpacity(blockState, (IBlockAccess)world, (BlockPos)mutableGlobalPos) != 255 && blockState.func_177230_c().getLightOpacity(blockState, (IBlockAccess)world, (BlockPos)mutableGlobalPos) != 0)
/*  36 */           { colour = 25825; }
/*     */           else
/*  38 */           { colour = (blockState.func_185909_g((IBlockAccess)world, (BlockPos)mutableGlobalPos)).field_76291_p; } 
/*  39 */         } catch (Exception exception) {}
/*     */       } 
/*     */     }
/*     */     
/*  43 */     int r = colour >> 16 & 0xFF;
/*  44 */     int g = colour >> 8 & 0xFF;
/*  45 */     int b = colour & 0xFF;
/*  46 */     boolean isFinalBlock = this instanceof MapBlock;
/*  47 */     if (this.colourType == -1) {
/*     */ 
/*     */       
/*  50 */       IBlockState blockState = Misc.getStateById(this.state);
/*  51 */       if (isFinalBlock) {
/*  52 */         mapWriter.getColorTypeCache().getBlockBiomeColour(world, blockState, (BlockPos)mutableGlobalPos, result_dest, block.getBiome());
/*  53 */         this.colourType = (byte)result_dest[0];
/*  54 */         if (result_dest[1] != -1)
/*  55 */           block.setBiome(result_dest[1]); 
/*  56 */         this.customColour = result_dest[2];
/*     */       } else {
/*  58 */         mapWriter.getColorTypeCache().getOverlayBiomeColour(world, blockState, (BlockPos)mutableGlobalPos, result_dest, 1);
/*  59 */         this.colourType = (byte)result_dest[0];
/*  60 */         this.customColour = result_dest[2];
/*     */       } 
/*     */     } 
/*  63 */     if (this.colourType != 0 && (WorldMap.settings.biomeColorsVanillaMode || WorldMap.settings.colours == 0)) {
/*  64 */       int c = this.customColour;
/*  65 */       if (isFinalBlock) {
/*  66 */         if (this.colourType == 1) {
/*  67 */           c = mapWriter.getBiomeColor(0, mutableGlobalPos, mapTile, world);
/*  68 */         } else if (this.colourType == 2) {
/*  69 */           c = mapWriter.getBiomeColor(1, mutableGlobalPos, mapTile, world);
/*     */         } 
/*  71 */       } else if (this.colourType == 1) {
/*  72 */         c = mapWriter.getBiomeColor(2, mutableGlobalPos, mapTile, world);
/*     */       } 
/*  74 */       float rMultiplier = r / 255.0F;
/*  75 */       float gMultiplier = g / 255.0F;
/*  76 */       float bMultiplier = b / 255.0F;
/*     */       
/*  78 */       r = (int)((c >> 16 & 0xFF) * rMultiplier);
/*  79 */       g = (int)((c >> 8 & 0xFF) * gMultiplier);
/*  80 */       b = (int)((c & 0xFF) * bMultiplier);
/*     */     } 
/*  82 */     if (this.glowing) {
/*  83 */       int brightest = Math.max(r, Math.max(g, b));
/*  84 */       if (brightest != 0) {
/*  85 */         r = r * 255 / brightest;
/*  86 */         g = g * 255 / brightest;
/*  87 */         b = b * 255 / brightest;
/*     */       } 
/*  89 */       topLightValue = 15;
/*     */     } 
/*  91 */     int overlayRed = 0;
/*  92 */     int overlayGreen = 0;
/*  93 */     int overlayBlue = 0;
/*  94 */     float divider = 1.0F;
/*     */     
/*  96 */     if (overlays != null && !overlays.isEmpty()) {
/*  97 */       int sun = 15;
/*  98 */       for (int i = 0; i < overlays.size(); i++) {
/*  99 */         Overlay o = overlays.get(i);
/* 100 */         o.getPixelColour(block, result_dest, mapWriter, world, tileChunk, prevChunk, prevTile, mapTile, x, z, mutableGlobalPos);
/* 101 */         if (i == 0)
/* 102 */           topLightValue = o.light; 
/* 103 */         float overlayIntensity = o.getIntensity() * getBlockBrightness(5.0F, o.light, sun);
/* 104 */         divider += overlayIntensity;
/* 105 */         overlayRed = (int)(overlayRed + result_dest[0] * overlayIntensity);
/* 106 */         overlayGreen = (int)(overlayGreen + result_dest[1] * overlayIntensity);
/* 107 */         overlayBlue = (int)(overlayBlue + result_dest[2] * overlayIntensity);
/* 108 */         sun -= o.getOpacity();
/* 109 */         if (sun < 0)
/* 110 */           sun = 0; 
/*     */       } 
/* 112 */       if (!this.glowing)
/* 113 */         brightness = getBlockBrightness(5.0F, this.light, sun); 
/*     */     } 
/* 115 */     if ((isFinalBlock & (!this.glowing ? 1 : 0)) != 0) {
/*     */       
/* 117 */       if (block.getHeightType() != 3 && (mutableGlobalPos.func_177952_p() & 0x1FF) == 0)
/* 118 */         block.setHeightType((byte)3); 
/* 119 */       if (block.getHeightType() == 3 && block.getState() != 0) {
/* 120 */         block.fixHeightType(x, z, prevTile, mapTile, tileChunk, prevChunk);
/* 121 */         if (block.getHeightType() != 3 && (mutableGlobalPos.func_177952_p() & 0x1FF) != 0 && tileChunk.getInRegion().isMultiplayer())
/* 122 */           tileChunk.getInRegion().setBeingWritten(true); 
/*     */       } 
/* 124 */       float depthBrightness = 1.0F;
/* 125 */       int block_height = block.getHeight();
/* 126 */       boolean caving = (block.isCaveBlock() && block_height != -1 && block_height < 127);
/* 127 */       float caveBrightness = block_height / 127.0F;
/* 128 */       if (!caving && WorldMap.settings.terrainDepth && block_height != -1) {
/* 129 */         depthBrightness = height / 63.0F;
/* 130 */         if (depthBrightness > 1.15D) {
/* 131 */           depthBrightness = 1.15F;
/* 132 */         } else if (depthBrightness < 0.7D) {
/* 133 */           depthBrightness = 0.7F;
/*     */         } 
/* 135 */       }  if (WorldMap.settings.terrainSlopes)
/* 136 */         if (block.getHeightType() == 1) {
/* 137 */           depthBrightness = (float)(depthBrightness * 1.15D);
/* 138 */         } else if (block.getHeightType() == 2) {
/* 139 */           depthBrightness = (float)(depthBrightness * 0.85D);
/*     */         }  
/* 141 */       if (caving)
/* 142 */         brightness = caveBrightness; 
/* 143 */       brightness *= depthBrightness;
/* 144 */       if (brightness < 0.0F) {
/* 145 */         brightness = 0.0F;
/*     */       }
/*     */     } 
/*     */     
/* 149 */     result_dest[0] = (int)((r * brightness + overlayRed) / divider);
/* 150 */     if (result_dest[0] > 255)
/* 151 */       result_dest[0] = 255; 
/* 152 */     result_dest[1] = (int)((g * brightness + overlayGreen) / divider);
/* 153 */     if (result_dest[1] > 255)
/* 154 */       result_dest[1] = 255; 
/* 155 */     result_dest[2] = (int)((b * brightness + overlayBlue) / divider);
/* 156 */     if (result_dest[2] > 255)
/* 157 */       result_dest[2] = 255; 
/* 158 */     result_dest[3] = (int)(getPixelLight(topLightValue) * 255.0F);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getBlockBrightness(float min, int l, int sun) {
/* 168 */     return (min + Math.max(sun, l)) / (15.0F + min);
/*     */   }
/*     */   
/*     */   private float getPixelLight(int topLightValue) {
/* 172 */     return (topLightValue == 0) ? 0.0F : ((5 + topLightValue) / 20.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getState() {
/* 177 */     return this.state;
/*     */   }
/*     */   
/*     */   public void setState(int state) {
/* 181 */     this.state = state;
/*     */   }
/*     */   
/*     */   public void setLight(byte light) {
/* 185 */     this.light = light;
/*     */   }
/*     */   
/*     */   public void setGlowing(boolean glowing) {
/* 189 */     this.glowing = glowing;
/*     */   }
/*     */   
/*     */   public byte getColourType() {
/* 193 */     return this.colourType;
/*     */   }
/*     */   
/*     */   public void setColourType(byte colourType) {
/* 197 */     this.colourType = colourType;
/*     */   }
/*     */   
/*     */   public int getCustomColour() {
/* 201 */     return this.customColour;
/*     */   }
/*     */   
/*     */   public void setCustomColour(int customColour) {
/* 205 */     this.customColour = customColour;
/*     */   }
/*     */ }


/* Location:              C:\Users\ioanm\Downloads\XaerosWorldMap_1.7.3_Forge_1.12.jar!\xaero\map\region\MapPixel.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */