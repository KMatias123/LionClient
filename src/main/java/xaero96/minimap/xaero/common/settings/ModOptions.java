/*     */ package xaero.common.settings;
/*     */ 
/*     */ import net.minecraft.client.resources.I18n;
/*     */ import net.minecraft.util.math.MathHelper;
/*     */ import xaero.common.graphics.CursorBox;
/*     */ 
/*     */ public enum ModOptions
/*     */ {
/*   9 */   DEFAULT("Default", false, true),
/*  10 */   DOTS("gui.xaero_entity_radar", false, true),
/*  11 */   MINIMAP("gui.xaero_minimap", false, true, new CursorBox("gui.xaero_box_minimap")),
/*  12 */   CAVE_MAPS("gui.xaero_cave_maps", false, true, new CursorBox("gui.xaero_box_cave_maps")),
/*  13 */   CAVE_ZOOM("gui.xaero_cave_zoom", false, true, new CursorBox("gui.xaero_box_cave_zoom")),
/*  14 */   DISPLAY_OTHER_TEAM("gui.xaero_display_teams", false, true),
/*  15 */   WAYPOINTS("gui.xaero_display_waypoints", false, true),
/*  16 */   INGAME_WAYPOINTS("gui.xaero_ingame_waypoints", false, true),
/*  17 */   PLAYERS("gui.xaero_display_players", false, true),
/*  18 */   HOSTILE("gui.xaero_display_hostile", false, true),
/*  19 */   MOBS("gui.xaero_display_mobs", false, true),
/*  20 */   ITEMS("gui.xaero_display_items", false, true),
/*  21 */   ENTITIES("gui.xaero_display_other", false, true),
/*  22 */   ZOOM("gui.xaero_zoom", false, true),
/*  23 */   SIZE("gui.xaero_minimap_size", false, true),
/*  24 */   EAMOUNT("gui.xaero_entity_amount", false, true, new CursorBox("gui.xaero_box_entity_amount")),
/*  25 */   COORDS("gui.xaero_display_coords", false, true),
/*  26 */   NORTH("gui.xaero_lock_north", false, true),
/*  27 */   DEATHPOINTS("gui.xaero_deathpoints", false, true),
/*  28 */   OLD_DEATHPOINTS("gui.xaero_old_deathpoints", false, true),
/*  29 */   CHUNK_GRID("gui.xaero_chunkgrid", true, false, -1.0F, (ModSettings.COLORS.length - 1), 1.0F),
/*  30 */   SLIME_CHUNKS("gui.xaero_slime_chunks", false, true),
/*  31 */   SAFE_MAP("gui.xaero_safe_mode", false, true, new CursorBox("gui.xaero_safe_mode_box")),
/*  32 */   OPACITY("gui.xaero_opacity", true, false, 30.0F, 100.0F, 1.0F),
/*  33 */   WAYPOINTS_SCALE("gui.xaero_waypoints_scale", true, false, 1.0F, 5.0F, 0.5F),
/*  34 */   AA("gui.xaero_antialiasing", false, true),
/*  35 */   DISTANCE("gui.xaero_show_distance", false, true, new CursorBox("gui.xaero_box_distance")),
/*  36 */   COLOURS("gui.xaero_block_colours", false, true),
/*  37 */   LIGHT("gui.xaero_lighting", false, true),
/*  38 */   REDSTONE("gui.xaero_display_redstone", false, true),
/*  39 */   DOTS_SCALE("gui.xaero_dots_scale", true, false, 1.0F, 2.0F, 0.5F),
/*  40 */   COMPASS("gui.xaero_compass_over_wp", false, true, new CursorBox("gui.xaero_box_compass_over_wp")),
/*  41 */   BIOME("gui.xaero_current_biome", false, true),
/*  42 */   ENTITY_HEIGHT("gui.xaero_entity_depth", false, true, new CursorBox("gui.xaero_box_entity_depth")),
/*  43 */   FLOWERS("gui.xaero_show_flowers", false, true),
/*  44 */   KEEP_WP_NAMES("gui.xaero_waypoint_names", false, true),
/*  45 */   WAYPOINTS_DISTANCE("gui.xaero_waypoints_distance", true, false, 0.0F, 25000.0F, 250.0F, new CursorBox("gui.xaero_box_waypoints_distance")),
/*  46 */   WAYPOINTS_DISTANCE_MIN("gui.xaero_waypoints_distance_min", true, false, 0.0F, 100.0F, 5.0F),
/*  47 */   WAYPOINTS_DEFAULT_TP("gui.xaero_teleport_default_command", false, true, new CursorBox("gui.xaero_box_teleport_default_command")),
/*  48 */   WAYPOINTS_ALL_SETS("gui.xaero_render_all_wp_sets", false, true),
/*  49 */   ARROW_SCALE("gui.xaero_arrow_scale", true, false, 1.0F, 2.0F, 0.1F, new CursorBox("gui.xaero_box_arrow_scale")),
/*  50 */   ARROW_COLOUR("gui.xaero_arrow_colour", false, true, new CursorBox("gui.xaero_box_arrow_color")),
/*  51 */   SMOOTH_DOTS("gui.xaero_smooth_dots", false, true),
/*  52 */   PLAYER_HEADS("gui.xaero_lock_player_heads", false, true, new CursorBox("gui.xaero_box_lock_player_heads")),
/*  53 */   PLAYER_NAMES("gui.xaero_player_names", false, true),
/*  54 */   ENTITY_NAMETAGS("gui.xaero_entity_nametags", false, true, new CursorBox("gui.xaero_box_entity_nametags")),
/*  55 */   HEIGHT_LIMIT("gui.xaero_height_limit", true, false, 10.0F, 260.0F, 5.0F, new CursorBox("gui.xaero_box_height_limit")),
/*  56 */   WORLD_MAP("gui.xaero_use_world_map", false, true),
/*  57 */   CAPES("gui.xaero_patron_capes", false, true),
/*  58 */   TERRAIN_DEPTH("gui.xaero_terrain_depth", false, true),
/*  59 */   TERRAIN_SLOPES("gui.xaero_terrain_slopes", false, true),
/*  60 */   MAIN_ENTITY_AS("gui.xaero_main_entity_as", false, true),
/*  61 */   BLOCK_TRANSPARENCY("gui.xaero_block_transparency", false, true),
/*  62 */   WAYPOINT_OPACITY_INGAME("gui.xaero_waypoint_opacity_ingame", true, false, 10.0F, 100.0F, 1.0F),
/*  63 */   WAYPOINT_OPACITY_MAP("gui.xaero_waypoint_opacity_map", true, false, 10.0F, 100.0F, 1.0F),
/*  64 */   WAYPOINT_LOOKING_ANGLE("gui.xaero_waypoint_distance_visibility_angle", true, false, 1.0F, 180.0F, 1.0F),
/*  65 */   WAYPOINT_VERTICAL_LOOKING_ANGLE("gui.xaero_waypoint_distance_vertical_visibility_angle", true, false, 1.0F, 180.0F, 1.0F),
/*  66 */   HIDE_WORLD_NAMES("gui.xaero_hide_world_names", false, true),
/*  67 */   OPEN_SLIME_SETTINGS("gui.xaero_open_slime", false, true),
/*  68 */   ALWAYS_SHOW_DISTANCE("gui.xaero_always_show_distance", false, true, new CursorBox("gui.xaero_box_always_distance")),
/*  69 */   SHOW_LIGHT_LEVEL("gui.xaero_show_light_level", false, true),
/*  70 */   RENDER_LAYER("gui.xaero_render_layer", false, true),
/*  71 */   SHOW_TIME("gui.xaero_show_time", false, true),
/*  72 */   BIOMES_VANILLA("gui.xaero_biomes_vanilla", false, true),
/*  73 */   CENTERED_ENLARGED("gui.xaero_centered_enlarged", false, true),
/*  74 */   ZOOMED_OUT_ENLARGED("gui.xaero_zoomed_out_enlarged", false, true),
/*  75 */   MINIMAP_TEXT_ALIGN("gui.xaero_minimap_text_align", false, true),
/*  76 */   SHOW_ANGLES("gui.xaero_show_angles", false, true),
/*  77 */   COMPASS_ENABLED("gui.xaero_compass", false, true),
/*  78 */   CAVE_MAPS_DEPTH("gui.xaero_cave_maps_depth", true, false, 0.0F, 64.0F, 1.0F),
/*  79 */   HIDE_WP_COORDS("gui.xaero_hide_wp_coords", false, true),
/*  80 */   PLAYER_ARROW_OPACITY("gui.xaero_player_arrow_opacity", true, false, 1.0F, 100.0F, 1.0F),
/*     */ 
/*     */   
/*  83 */   SHOW_EFFECTS("gui.xaero_potion_status", false, true, new CursorBox("gui.xaero_box_potion_effects")),
/*  84 */   SHOW_ARMOR("gui.xaero_armour_status", false, true, new CursorBox("gui.xaero_box_armour_status")),
/*  85 */   BETTER_SPRINT("gui.xaero_sprint", false, true, new CursorBox("gui.xaero_box_sprint")),
/*  86 */   KEEP_SNEAK("gui.xaero_sneak", false, true, new CursorBox("gui.xaero_box_sneak")),
/*  87 */   ENCHANT_COLOR("gui.xaero_enchants_color", false, true),
/*  88 */   DURABILITY("gui.xaero_durability", false, true),
/*  89 */   NOTIFICATIONS("gui.xaero_notifications", false, true, new CursorBox("gui.xaero_box_notifications")),
/*  90 */   NOTIFICATIONS_HUNGER("gui.xaero_hunger_setting", false, true),
/*  91 */   NOTIFICATIONS_HUNGER_LOW("gui.xaero_hunger_low", false, true),
/*  92 */   NOTIFICATIONS_HP("gui.xaero_hp_setting", false, true),
/*  93 */   NOTIFICATIONS_HP_LOW("gui.xaero_hp_low", false, true),
/*  94 */   NOTIFICATIONS_TNT("gui.xaero_explosion_setting", false, true),
/*  95 */   NOTIFICATIONS_ARROW("gui.xaero_being_shot_setting", false, true),
/*  96 */   NOTIFICATIONS_AIR("gui.xaero_air_setting", false, true),
/*  97 */   NOTIFICATIONS_AIR_LOW("gui.xaero_air_low", false, true),
/*  98 */   XP("gui.xaero_xp_setting", false, true, new CursorBox("gui.xaero_box_xp")),
/*  99 */   CUSTOMIZATION("gui.xaero_custom_settings", false, true),
/* 100 */   EDIT("gui.xaero_edit_mode", false, true),
/* 101 */   RESET("gui.xaero_reset_defaults", false, true),
/* 102 */   NUMBERS("gui.xaero_quick_use", false, true, new CursorBox("gui.xaero_box_quick_use")),
/* 103 */   SHOW_ENCHANTS("gui.xaero_show_enchants", false, true),
/* 104 */   ARCHERY("gui.xaero_archery_status", false, true, new CursorBox("gui.xaero_box_archery")),
/* 105 */   POTION_NAMES("gui.xaero_potion_names", false, true),
/* 106 */   POTION_EFFECTS_BLINK("gui.xaero_potion_effects_blink", false, true),
/* 107 */   ENTITY_INFO("gui.xaero_entity_info", false, true, new CursorBox("gui.xaero_box_entity_info")),
/* 108 */   ENTITY_INFO_STAY("gui.xaero_entity_info_stay", false, true),
/* 109 */   ENTITY_INFO_DISTANCE("gui.xaero_entity_info_distance", true, false, 1.0F, 40.0F, 1.0F),
/* 110 */   ENTITY_INFO_MAX_HEARTS("gui.xaero_entity_info_max_hearts", true, false, 10.0F, 1000.0F, 10.0F),
/* 111 */   ENTITY_INFO_NUMBERS("gui.xaero_entity_info_numbers", false, true),
/* 112 */   ENTITY_INFO_EFFECTS("gui.xaero_entity_info_potion_effects", false, true),
/* 113 */   ENTITY_INFO_EFFECTS_SCALE("gui.xaero_entity_info_potion_effects_scale", true, false, 1.0F, 4.0F, 1.0F),
/* 114 */   SHOW_FULL_AMOUNT("gui.xaero_show_full_amount", false, true),
/* 115 */   ENTITY_INFO_ARMOUR_NUMBERS("gui.xaero_entity_info_armour_numbers", false, true),
/* 116 */   ENTITY_INFO_ARMOUR("gui.xaero_entity_info_armour", false, true),
/* 117 */   SHOW_ENTITY_MODEL("gui.xaero_show_entity_model", false, true),
/* 118 */   ITEM_TOOLTIP("gui.xaero_item_tooltip", false, true),
/* 119 */   ITEM_TOOLTIP_MIN_LINES("gui.xaero_item_tooltip_min_lines", true, false, 0.0F, 10.0F, 1.0F),
/* 120 */   ITEM_TOOLTIP_TIME("gui.xaero_item_tooltip_time", true, false, 1.0F, 40.0F, 1.0F);
/*     */   
/*     */   private final boolean enumFloat;
/*     */   
/*     */   private final boolean enumBoolean;
/*     */   
/*     */   private final String enumString;
/*     */   
/*     */   private float valueMin;
/*     */   
/*     */   private float valueMax;
/*     */   private float valueStep;
/*     */   private CursorBox tooltip;
/*     */   
/*     */   public static ModOptions getModOptions(int par0) {
/* 135 */     ModOptions[] aenumoptions = values();
/* 136 */     int j = aenumoptions.length;
/*     */     
/* 138 */     for (int k = 0; k < j; k++) {
/* 139 */       ModOptions enumoptions = aenumoptions[k];
/*     */       
/* 141 */       if (enumoptions.returnEnumOrdinal() == par0) {
/* 142 */         return enumoptions;
/*     */       }
/*     */     } 
/*     */     
/* 146 */     return null;
/*     */   }
/*     */   
/*     */   ModOptions(String par3Str, boolean par4, boolean par5, CursorBox tooltip) {
/* 150 */     this.enumString = par3Str;
/* 151 */     this.enumFloat = par4;
/* 152 */     this.enumBoolean = par5;
/* 153 */     this.tooltip = tooltip;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   ModOptions(String p_i45004_3_, boolean p_i45004_4_, boolean p_i45004_5_, float p_i45004_6_, float p_i45004_7_, float p_i45004_8_, CursorBox tooltip) {
/* 163 */     this.enumString = p_i45004_3_;
/* 164 */     this.enumFloat = p_i45004_4_;
/* 165 */     this.enumBoolean = p_i45004_5_;
/* 166 */     this.valueMin = p_i45004_6_;
/* 167 */     this.valueMax = p_i45004_7_;
/* 168 */     this.valueStep = p_i45004_8_;
/* 169 */     this.tooltip = tooltip;
/*     */   }
/*     */   
/*     */   public boolean getEnumFloat() {
/* 173 */     return this.enumFloat;
/*     */   }
/*     */   
/*     */   public boolean getEnumBoolean() {
/* 177 */     return this.enumBoolean;
/*     */   }
/*     */   
/*     */   public int returnEnumOrdinal() {
/* 181 */     return ordinal();
/*     */   }
/*     */   
/*     */   public float getValueMax() {
/* 185 */     return this.valueMax;
/*     */   }
/*     */   
/*     */   public void setValueMax(float p_148263_1_) {
/* 189 */     this.valueMax = p_148263_1_;
/*     */   }
/*     */   
/*     */   public float normalizeValue(float p_148266_1_) {
/* 193 */     return MathHelper.func_76131_a((
/* 194 */         snapToStepClamp(p_148266_1_) - this.valueMin) / (this.valueMax - this.valueMin), 0.0F, 1.0F);
/*     */   }
/*     */   
/*     */   public float denormalizeValue(float p_148262_1_) {
/* 198 */     return snapToStepClamp(this.valueMin + (this.valueMax - this.valueMin) * 
/* 199 */         MathHelper.func_76131_a(p_148262_1_, 0.0F, 1.0F));
/*     */   }
/*     */   
/*     */   public float snapToStepClamp(float p_148268_1_) {
/* 203 */     p_148268_1_ = snapToStep(p_148268_1_);
/* 204 */     return MathHelper.func_76131_a(p_148268_1_, this.valueMin, this.valueMax);
/*     */   }
/*     */   
/*     */   protected float snapToStep(float p_148264_1_) {
/* 208 */     if (this.valueStep > 0.0F) {
/* 209 */       p_148264_1_ = this.valueStep * Math.round(p_148264_1_ / this.valueStep);
/*     */     }
/*     */     
/* 212 */     return p_148264_1_;
/*     */   }
/*     */   
/*     */   public String getEnumString() {
/* 216 */     return I18n.func_135052_a(this.enumString, new Object[0]);
/*     */   }
/*     */   
/*     */   public CursorBox getTooltip() {
/* 220 */     return this.tooltip;
/*     */   }
/*     */ }


/* Location:              C:\Users\ioanm\Downloads\Xaeros_Minimap_20.15.3_Forge_1.12.jar!\xaero\common\settings\ModOptions.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */