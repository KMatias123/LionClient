/*    */ package xaero.map.biome;
/*    */ 
/*    */ import net.minecraft.util.math.BlockPos;
/*    */ import net.minecraft.util.math.MathHelper;
/*    */ import net.minecraft.world.ColorizerFoliage;
/*    */ import net.minecraft.world.ColorizerGrass;
/*    */ import net.minecraft.world.biome.Biome;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MapBiomes
/*    */   extends Biome
/*    */ {
/*    */   public MapBiomes() {
/* 15 */     super(new Biome.BiomeProperties(""));
/*    */   }
/*    */   
/*    */   public int getBiomeGrassColour(int biomeId, Biome biome, BlockPos pos) {
/* 19 */     if (isVanilla(biomeId)) {
/* 20 */       if (biome instanceof net.minecraft.world.biome.BiomeForest)
/* 21 */         return forestGrassColor(biomeId, biome, pos); 
/* 22 */       if (biome instanceof net.minecraft.world.biome.BiomeMesa)
/* 23 */         return 9470285; 
/* 24 */       if (biome instanceof net.minecraft.world.biome.BiomeSwamp)
/* 25 */         return swampGrassColor(pos); 
/* 26 */       return defaultGrassColor(biome, pos);
/*    */     } 
/* 28 */     return biome.func_180627_b(pos);
/*    */   }
/*    */   
/*    */   public int getBiomeFoliageColour(int biomeId, Biome biome, BlockPos pos) {
/* 32 */     if (isVanilla(biomeId)) {
/* 33 */       if (biome instanceof net.minecraft.world.biome.BiomeMesa)
/* 34 */         return 10387789; 
/* 35 */       if (biome instanceof net.minecraft.world.biome.BiomeSwamp)
/* 36 */         return 6975545; 
/* 37 */       return defaultFoliageColor(biome, pos);
/*    */     } 
/* 39 */     return biome.func_180625_c(pos);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getBiomeWaterColour(int biomeId, Biome biome) {
/* 44 */     if (isVanilla(biomeId)) {
/* 45 */       if (biome instanceof net.minecraft.world.biome.BiomeSwamp)
/* 46 */         return 14745518; 
/* 47 */       return defaultWaterColor(biome);
/*    */     } 
/* 49 */     return biome.getWaterColorMultiplier();
/*    */   }
/*    */   
/*    */   private boolean isVanilla(int biomeId) {
/* 53 */     return (biomeId < 40 || biomeId == 127 || (biomeId >= 129 && biomeId < 135) || biomeId == 140 || biomeId == 149 || biomeId == 151 || (biomeId >= 155 && biomeId < 159) || (biomeId >= 160 && biomeId < 168));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private int defaultGrassColor(Biome biome, BlockPos pos) {
/* 59 */     double d0 = MathHelper.func_76131_a(biome.func_180626_a(pos), 0.0F, 1.0F);
/* 60 */     double d1 = MathHelper.func_76131_a(biome.func_76727_i(), 0.0F, 1.0F);
/* 61 */     return ColorizerGrass.func_77480_a(d0, d1);
/*    */   }
/*    */   
/*    */   private int forestGrassColor(int biomeId, Biome biome, BlockPos pos) {
/* 65 */     int i = defaultGrassColor(biome, pos);
/* 66 */     return (biomeId == 29 || biomeId == 157) ? ((i & 0xFEFEFE) + 2634762 >> 1) : i;
/*    */   }
/*    */ 
/*    */   
/*    */   private int swampGrassColor(BlockPos pos) {
/* 71 */     double d0 = field_180281_af.func_151601_a(pos.func_177958_n() * 0.0225D, pos.func_177952_p() * 0.0225D);
/* 72 */     return (d0 < -0.1D) ? 5011004 : 6975545;
/*    */   }
/*    */ 
/*    */   
/*    */   private int defaultFoliageColor(Biome biome, BlockPos pos) {
/* 77 */     double d0 = MathHelper.func_76131_a(biome.func_180626_a(pos), 0.0F, 1.0F);
/* 78 */     double d1 = MathHelper.func_76131_a(biome.func_76727_i(), 0.0F, 1.0F);
/* 79 */     return ColorizerFoliage.func_77470_a(d0, d1);
/*    */   }
/*    */   
/*    */   private int defaultWaterColor(Biome biome) {
/* 83 */     return 16777215;
/*    */   }
/*    */ }


/* Location:              C:\Users\ioanm\Downloads\XaerosWorldMap_1.7.3_Forge_1.12.jar!\xaero\map\biome\MapBiomes.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */