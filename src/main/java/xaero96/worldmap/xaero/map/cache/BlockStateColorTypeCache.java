/*     */ package xaero.map.cache;
/*     */ 
/*     */ import java.util.Hashtable;
/*     */ import net.minecraft.block.material.MapColor;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.biome.Biome;
/*     */ import xaero.map.WorldMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BlockStateColorTypeCache
/*     */ {
/*  26 */   private Hashtable<IBlockState, Integer> colorTypes = new Hashtable<>();
/*  27 */   private Hashtable<IBlockState, Integer> overlayColorTypes = new Hashtable<>(); private int grassColor;
/*  28 */   private IBlockState grassState = Blocks.field_150349_c.func_176223_P(); private int foliageColor;
/*  29 */   private IBlockState oakLeavesState = Blocks.field_150362_t.func_176223_P();
/*  30 */   private IBlockState waterState = Blocks.field_150355_j.func_176223_P();
/*     */ 
/*     */ 
/*     */   
/*     */   public void getBlockBiomeColour(World world, IBlockState state, BlockPos pos, int[] dest, int biomeId) {
/*  35 */     dest[2] = 0; dest[0] = 0;
/*  36 */     dest[1] = -1;
/*  37 */     Integer cachedColorType = this.colorTypes.get(state);
/*  38 */     int colorType = (cachedColorType != null) ? cachedColorType.intValue() : -1;
/*  39 */     int customColour = -1;
/*  40 */     boolean gotFullCC = false;
/*  41 */     boolean isRenderThread = Minecraft.func_71410_x().func_152345_ab();
/*  42 */     if (colorType == -1 && isRenderThread) {
/*     */       try {
/*  44 */         customColour = Minecraft.func_71410_x().func_184125_al().func_186724_a(state, null, null, 0);
/*  45 */       } catch (Throwable t) {
/*  46 */         customColour = 0;
/*     */       } 
/*  48 */       if (customColour != -1 && customColour != this.grassColor && customColour != this.foliageColor) {
/*  49 */         Material material = state.func_185904_a();
/*  50 */         if (material != null && (material.func_151565_r() == MapColor.field_151661_c || material.func_151565_r() == MapColor.field_151669_i)) {
/*  51 */           customColour = Minecraft.func_71410_x().func_184125_al().func_186724_a(state, (IBlockAccess)world, pos, 0);
/*  52 */           gotFullCC = true;
/*  53 */           if (material.func_151565_r() == MapColor.field_151661_c && customColour == Minecraft.func_71410_x().func_184125_al().func_186724_a(this.grassState, (IBlockAccess)world, pos, 0)) {
/*  54 */             customColour = this.grassColor;
/*  55 */           } else if (material.func_151565_r() == MapColor.field_151669_i && customColour == Minecraft.func_71410_x().func_184125_al().func_186724_a(this.oakLeavesState, (IBlockAccess)world, pos, 0)) {
/*  56 */             customColour = this.foliageColor;
/*     */           } 
/*     */         } 
/*  59 */       }  if (customColour == this.grassColor) {
/*  60 */         colorType = 1;
/*  61 */       } else if (customColour == this.foliageColor) {
/*  62 */         colorType = 2;
/*     */       } else {
/*  64 */         if (!gotFullCC) {
/*  65 */           customColour = Minecraft.func_71410_x().func_184125_al().func_186724_a(state, (IBlockAccess)world, pos, 0);
/*  66 */           gotFullCC = true;
/*     */         } 
/*  68 */         if (customColour != 16777215 && customColour != -1) {
/*  69 */           colorType = 3;
/*     */         } else {
/*  71 */           colorType = 0;
/*     */         } 
/*  73 */       }  this.colorTypes.put(state, Integer.valueOf(colorType));
/*  74 */     } else if (colorType == 3 && !isRenderThread) {
/*  75 */       colorType = -1;
/*  76 */     }  if ((colorType == 1 || colorType == 2) && biomeId == -1)
/*  77 */       if (isRenderThread) {
/*  78 */         biomeId = Biome.func_185362_a(world.func_180494_b(pos));
/*     */       } else {
/*  80 */         colorType = -1;
/*     */       }  
/*  82 */     dest[0] = colorType;
/*  83 */     if (colorType == -1) {
/*  84 */       dest[1] = biomeId;
/*  85 */     } else if (colorType == 1 || colorType == 2) {
/*  86 */       dest[1] = biomeId;
/*  87 */     } else if (colorType == 3) {
/*  88 */       if (!gotFullCC)
/*  89 */         customColour = Minecraft.func_71410_x().func_184125_al().func_186724_a(state, (IBlockAccess)world, pos, 0); 
/*  90 */       dest[2] = customColour;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void getOverlayBiomeColour(World world, IBlockState state, BlockPos pos, int[] dest, int biomeId) {
/*  96 */     dest[2] = 0; dest[0] = 0;
/*  97 */     dest[1] = -1;
/*  98 */     Integer cachedColorType = this.overlayColorTypes.get(state);
/*  99 */     int colorType = (cachedColorType != null) ? cachedColorType.intValue() : -1;
/* 100 */     int customColour = -1;
/* 101 */     boolean gotFullCC = false;
/* 102 */     boolean isRenderThread = Minecraft.func_71410_x().func_152345_ab();
/* 103 */     if (colorType == -1 && isRenderThread) {
/* 104 */       if (state.func_177230_c() == Blocks.field_150355_j || state.func_177230_c() == Blocks.field_150358_i) {
/* 105 */         colorType = 1;
/*     */       } else {
/* 107 */         customColour = Minecraft.func_71410_x().func_184125_al().func_186724_a(state, (IBlockAccess)world, pos, 0);
/* 108 */         gotFullCC = true;
/* 109 */         colorType = 0;
/* 110 */         if (customColour != -1) {
/* 111 */           Material material = state.func_185904_a();
/* 112 */           if (material != null && material.func_151565_r() == MapColor.field_151662_n && customColour == Minecraft.func_71410_x().func_184125_al().func_186724_a(this.waterState, (IBlockAccess)world, pos, 0)) {
/* 113 */             colorType = 1;
/* 114 */           } else if (customColour != 16777215) {
/* 115 */             colorType = 2;
/*     */           } 
/*     */         } 
/* 118 */       }  this.overlayColorTypes.put(state, Integer.valueOf(colorType));
/* 119 */     } else if (colorType == 2 && !isRenderThread) {
/* 120 */       colorType = -1;
/* 121 */     }  dest[0] = colorType;
/* 122 */     if (colorType == 1) {
/* 123 */       if (biomeId == -1) {
/* 124 */         if (!gotFullCC)
/* 125 */           customColour = Minecraft.func_71410_x().func_184125_al().func_186724_a(state, (IBlockAccess)world, pos, 0); 
/* 126 */         if (customColour == 16777215) {
/* 127 */           dest[0] = 0;
/*     */         } else {
/* 129 */           biomeId = Biome.func_185362_a(world.func_180494_b(pos));
/* 130 */           dest[1] = biomeId;
/*     */         } 
/*     */       } 
/* 133 */       dest[1] = biomeId;
/* 134 */     } else if (colorType == 2) {
/* 135 */       if (!gotFullCC)
/* 136 */         customColour = Minecraft.func_71410_x().func_184125_al().func_186724_a(state, (IBlockAccess)world, pos, 0); 
/* 137 */       dest[2] = customColour;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void updateGrassColor() {
/* 142 */     this.grassColor = Minecraft.func_71410_x().func_184125_al().func_186724_a(this.grassState, null, null, 0);
/* 143 */     this.foliageColor = Minecraft.func_71410_x().func_184125_al().func_186724_a(this.oakLeavesState, null, null, 0);
/* 144 */     if (WorldMap.settings.debug)
/* 145 */       System.out.println("Default grass colour: " + this.grassColor); 
/*     */   }
/*     */ }


/* Location:              C:\Users\ioanm\Downloads\XaerosWorldMap_1.7.3_Forge_1.12.jar!\xaero\map\cache\BlockStateColorTypeCache.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */