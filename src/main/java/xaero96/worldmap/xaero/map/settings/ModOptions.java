/*     */ package xaero.map.settings;
/*     */ 
/*     */ import net.minecraft.client.resources.I18n;
/*     */ import net.minecraft.util.math.MathHelper;
/*     */ 
/*     */ public enum ModOptions {
/*   7 */   DEBUG("gui.xaero_debug", false, true),
/*   8 */   COLOURS("gui.xaero_block_colours", false, true),
/*   9 */   LIGHTING("gui.xaero_lighting", false, true),
/*  10 */   UPDATE("gui.xaero_update_chunks", false, true),
/*  11 */   LOAD("gui.xaero_load_chunks", false, true),
/*  12 */   DEPTH("gui.xaero_terrain_depth", false, true),
/*  13 */   SLOPES("gui.xaero_terrain_slopes", false, true),
/*  14 */   STEPS("gui.xaero_footsteps", false, true),
/*  15 */   FLOWERS("gui.xaero_flowers", false, true),
/*  16 */   COMPRESSION("gui.xaero_texture_compression", false, true),
/*  17 */   COORDINATES("gui.xaero_wm_coordinates", false, true),
/*  18 */   BIOMES("gui.xaero_biome_colors", false, true),
/*  19 */   WAYPOINTS("gui.xaero_worldmap_waypoints", false, true),
/*  20 */   ARROW("gui.xaero_render_arrow", false, true),
/*  21 */   DISPLAY_ZOOM("gui.xaero_display_zoom", false, true);
/*     */   
/*     */   private final boolean enumFloat;
/*     */   
/*     */   private final boolean enumBoolean;
/*     */   private final String enumString;
/*     */   private float valueMin;
/*     */   private float valueMax;
/*     */   private float valueStep;
/*     */   
/*     */   public static ModOptions getModOptions(int par0) {
/*  32 */     ModOptions[] aenumoptions = values();
/*  33 */     int j = aenumoptions.length;
/*     */     
/*  35 */     for (int k = 0; k < j; k++) {
/*     */       
/*  37 */       ModOptions enumoptions = aenumoptions[k];
/*     */       
/*  39 */       if (enumoptions.returnEnumOrdinal() == par0)
/*     */       {
/*  41 */         return enumoptions;
/*     */       }
/*     */     } 
/*     */     
/*  45 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   ModOptions(String par3Str, boolean par4, boolean par5) {
/*  50 */     this.enumString = par3Str;
/*  51 */     this.enumFloat = par4;
/*  52 */     this.enumBoolean = par5;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   ModOptions(String p_i45004_3_, boolean p_i45004_4_, boolean p_i45004_5_, float p_i45004_6_, float p_i45004_7_, float p_i45004_8_) {
/*  58 */     this.enumString = p_i45004_3_;
/*  59 */     this.enumFloat = p_i45004_4_;
/*  60 */     this.enumBoolean = p_i45004_5_;
/*  61 */     this.valueMin = p_i45004_6_;
/*  62 */     this.valueMax = p_i45004_7_;
/*  63 */     this.valueStep = p_i45004_8_;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getEnumFloat() {
/*  68 */     return this.enumFloat;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getEnumBoolean() {
/*  73 */     return this.enumBoolean;
/*     */   }
/*     */ 
/*     */   
/*     */   public int returnEnumOrdinal() {
/*  78 */     return ordinal();
/*     */   }
/*     */ 
/*     */   
/*     */   public float getValueMax() {
/*  83 */     return this.valueMax;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setValueMax(float p_148263_1_) {
/*  88 */     this.valueMax = p_148263_1_;
/*     */   }
/*     */ 
/*     */   
/*     */   public float normalizeValue(float p_148266_1_) {
/*  93 */     return MathHelper.func_76131_a((snapToStepClamp(p_148266_1_) - this.valueMin) / (this.valueMax - this.valueMin), 0.0F, 1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public float denormalizeValue(float p_148262_1_) {
/*  98 */     return snapToStepClamp(this.valueMin + (this.valueMax - this.valueMin) * MathHelper.func_76131_a(p_148262_1_, 0.0F, 1.0F));
/*     */   }
/*     */ 
/*     */   
/*     */   public float snapToStepClamp(float p_148268_1_) {
/* 103 */     p_148268_1_ = snapToStep(p_148268_1_);
/* 104 */     return MathHelper.func_76131_a(p_148268_1_, this.valueMin, this.valueMax);
/*     */   }
/*     */ 
/*     */   
/*     */   protected float snapToStep(float p_148264_1_) {
/* 109 */     if (this.valueStep > 0.0F)
/*     */     {
/* 111 */       p_148264_1_ = this.valueStep * Math.round(p_148264_1_ / this.valueStep);
/*     */     }
/*     */     
/* 114 */     return p_148264_1_;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getEnumString() {
/* 119 */     return I18n.func_135052_a(this.enumString, new Object[0]);
/*     */   }
/*     */ }


/* Location:              C:\Users\ioanm\Downloads\XaerosWorldMap_1.7.3_Forge_1.12.jar!\xaero\map\settings\ModOptions.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */