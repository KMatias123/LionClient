/*     */ package xaero.common.mods;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.Gui;
/*     */ import net.minecraft.client.gui.GuiScreen;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import org.lwjgl.opengl.GL14;
/*     */ import xaero.common.IXaeroMinimap;
/*     */ import xaero.common.minimap.region.MinimapTile;
/*     */ import xaero.common.minimap.render.MinimapRendererHelper;
/*     */ import xaero.common.settings.ModSettings;
/*     */ import xaero.map.MapProcessor;
/*     */ import xaero.map.WorldMap;
/*     */ import xaero.map.gui.GuiMap;
/*     */ import xaero.map.gui.GuiWorldMapSettings;
/*     */ import xaero.map.region.MapRegion;
/*     */ import xaero.map.region.MapTileChunk;
/*     */ import xaero.map.world.MapDimension;
/*     */ import xaero.map.world.MapWorld;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SupportXaeroWorldmap
/*     */ {
/*  28 */   private static final HashMap<MapTileChunk, Long> seedsUsed = new HashMap<>();
/*  29 */   public static final Color black = new Color(0, 0, 0, 255);
/*  30 */   public static final Color slime = new Color(82, 241, 64, 128);
/*     */   
/*     */   private IXaeroMinimap modMain;
/*     */   
/*     */   public SupportXaeroWorldmap(IXaeroMinimap modMain) {
/*  35 */     this.modMain = modMain;
/*     */   }
/*     */   
/*     */   public void drawMinimap(MinimapRendererHelper helper, int xFloored, int zFloored, int radius, boolean zooming, double zoom) {
/*  39 */     Gui.func_73734_a(-256, -256, 256, 256, black.hashCode());
/*  40 */     synchronized (MapProcessor.instance.renderThreadPauseSync) {
/*  41 */       if (!MapProcessor.instance.isRenderingPaused()) {
/*  42 */         if (MapProcessor.instance.getCurrentDimension() == null)
/*     */           return; 
/*  44 */         String worldString = MapProcessor.instance.getCurrentWorldString();
/*  45 */         if (worldString == null)
/*     */           return; 
/*  47 */         int mapX = xFloored >> 4;
/*  48 */         int mapZ = zFloored >> 4;
/*  49 */         int chunkX = mapX >> 2;
/*  50 */         int chunkZ = mapZ >> 2;
/*  51 */         int tileX = mapX & 0x3;
/*  52 */         int tileZ = mapZ & 0x3;
/*  53 */         int insideX = xFloored & 0xF;
/*  54 */         int insideZ = zFloored & 0xF;
/*     */         
/*  56 */         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
/*  57 */         GlStateManager.func_179147_l();
/*     */         
/*  59 */         int minX = (mapX >> 2) - 4;
/*  60 */         int maxX = (mapX >> 2) + 4;
/*  61 */         int minZ = (mapZ >> 2) - 4;
/*  62 */         int maxZ = (mapZ >> 2) + 4;
/*     */         
/*  64 */         int minViewX = (mapX >> 2) - radius;
/*  65 */         int maxViewX = (mapX >> 2) + radius + 1;
/*  66 */         int minViewZ = (mapZ >> 2) - radius;
/*  67 */         int maxViewZ = (mapZ >> 2) + radius + 1;
/*  68 */         for (int i = minX; i < maxX + 1; i++) {
/*  69 */           for (int j = minZ; j < maxZ + 1; j++) {
/*  70 */             MapRegion region = MapProcessor.instance.getMapRegion(i >> 3, j >> 3, MapProcessor.instance.regionExists(i >> 3, j >> 3));
/*  71 */             if (region != null) {
/*     */               
/*  73 */               synchronized (region) {
/*  74 */                 if (!region.recacheHasBeenRequested() && !region.reloadHasBeenRequested() && (region.getVersion() != MapProcessor.instance.getGlobalVersion() || (region.getLoadState() == 4 && region.shouldCache())))
/*  75 */                   if (region.isBeingWritten() && region.getLoadState() == 2) {
/*  76 */                     region.requestRefresh();
/*  77 */                   } else if (region.getLoadState() == 0 || region.getLoadState() == 4) {
/*  78 */                     MapProcessor.instance.getMapSaveLoad().requestLoad(region, "Minimap");
/*  79 */                     MapProcessor.instance.getMapSaveLoad().setNextToLoadByViewing(region);
/*     */                   }  
/*     */               } 
/*  82 */               if (!MapProcessor.instance.isUploadingPaused()) {
/*  83 */                 List<MapRegion> regions = MapProcessor.instance.getMapWorld().getCurrentDimension().getMapRegionsList();
/*     */                 
/*  85 */                 regions.remove(region);
/*  86 */                 regions.add(region);
/*     */               } 
/*  88 */               if (i >= minViewX && i <= maxViewX && j >= minViewZ && j <= maxViewZ) {
/*     */                 
/*  90 */                 MapTileChunk chunk = region.getChunk(i & 0x7, j & 0x7);
/*  91 */                 if (chunk != null && 
/*  92 */                   chunk.getGlColorTexture() != -1)
/*     */                 
/*  94 */                 { GuiMap.bindMapTextureWithLighting(chunk, zooming ? 9729 : 9728, 0);
/*  95 */                   int drawX = 64 * (chunk.getX() - chunkX) - 16 * tileX - insideX;
/*  96 */                   int drawZ = 64 * (chunk.getZ() - chunkZ) - 16 * tileZ - insideZ;
/*     */                   
/*  98 */                   GL14.glBlendFuncSeparate(770, 771, 1, 771);
/*  99 */                   GuiMap.renderTexturedModalRectWithLighting(drawX, drawZ, 0, 0, 64.0F, 64.0F);
/*     */                   
/* 101 */                   int r = 0;
/* 102 */                   int g = 0;
/* 103 */                   int b = 0;
/* 104 */                   if ((this.modMain.getSettings()).chunkGrid > -1) {
/* 105 */                     int grid = ModSettings.COLORS[(this.modMain.getSettings()).chunkGrid];
/* 106 */                     r = grid >> 16 & 0xFF;
/* 107 */                     g = grid >> 8 & 0xFF;
/* 108 */                     b = grid & 0xFF;
/*     */                   } 
/* 110 */                   Long seed = this.modMain.getSettings().getSlimeChunksSeed();
/* 111 */                   Long savedSeed = seedsUsed.get(chunk);
/* 112 */                   boolean newSeed = ((seed == null && savedSeed != null) || (seed != null && !seed.equals(savedSeed)));
/* 113 */                   if (newSeed) {
/* 114 */                     seedsUsed.put(chunk, seed);
/*     */                   }
/*     */                   
/* 117 */                   GuiMap.restoreTextureStates();
/* 118 */                   for (int t = 0; t < 16; t++) {
/* 119 */                     if (newSeed || (chunk.getTileGridsCache()[t % 4][t / 4] & 0x1) == 0)
/* 120 */                       chunk.getTileGridsCache()[t % 4][t / 4] = (byte)(true | (MinimapTile.isSlimeChunk(this.modMain.getSettings(), chunk.getX() * 4 + t % 4, chunk.getZ() * 4 + t / 4, seed) ? true : false)); 
/* 121 */                     if (this.modMain.getSettings().getSlimeChunks() && (chunk.getTileGridsCache()[t % 4][t / 4] & 0x2) != 0) {
/* 122 */                       int slimeDrawX = drawX + 16 * t % 4;
/* 123 */                       int slimeDrawZ = drawZ + 16 * t / 4;
/* 124 */                       Gui.func_73734_a(slimeDrawX, slimeDrawZ, slimeDrawX + 16, slimeDrawZ + 16, slime.hashCode());
/*     */                     } 
/*     */                   } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */                   
/* 134 */                   if ((this.modMain.getSettings()).chunkGrid > -1) {
/* 135 */                     GlStateManager.func_179090_x();
/* 136 */                     GlStateManager.func_179147_l();
/* 137 */                     GlStateManager.func_179094_E();
/* 138 */                     GlStateManager.func_179109_b((drawX + 64), (drawZ + 64), 0.0F);
/* 139 */                     GlStateManager.func_179139_a(1.0D / zoom, 1.0D / zoom, 1.0D);
/* 140 */                     GlStateManager.func_179131_c(r / 255.0F, g / 255.0F, b / 255.0F, 0.5F);
/* 141 */                     float top = (float)(-64.0D * zoom);
/* 142 */                     float left = top;
/* 143 */                     helper.drawMyColoredRect(left, -1.0F, 0.0F, 0.0F);
/* 144 */                     helper.drawMyColoredRect(left, (float)(-16.0D * zoom) - 1.0F, 0.0F, (float)(-16.0D * zoom));
/* 145 */                     helper.drawMyColoredRect(left, (float)(-32.0D * zoom) - 1.0F, 0.0F, (float)(-32.0D * zoom));
/* 146 */                     helper.drawMyColoredRect(left, (float)(-48.0D * zoom) - 1.0F, 0.0F, (float)(-48.0D * zoom));
/* 147 */                     helper.drawMyColoredRect(-1.0F, top, 0.0F, 0.0F);
/* 148 */                     helper.drawMyColoredRect((float)(-16.0D * zoom) - 1.0F, top, (float)(-16.0D * zoom), 0.0F);
/* 149 */                     helper.drawMyColoredRect((float)(-32.0D * zoom) - 1.0F, top, (float)(-32.0D * zoom), 0.0F);
/* 150 */                     helper.drawMyColoredRect((float)(-48.0D * zoom) - 1.0F, top, (float)(-48.0D * zoom), 0.0F);
/* 151 */                     GlStateManager.func_179121_F();
/* 152 */                     GlStateManager.func_179084_k();
/* 153 */                     GlStateManager.func_179098_w();
/*     */                   } 
/* 155 */                   GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
/* 156 */                   GlStateManager.func_179147_l(); } 
/*     */               } 
/*     */             } 
/*     */           } 
/* 160 */         }  GL14.glBlendFuncSeparate(770, 771, 1, 0);
/* 161 */         GlStateManager.func_179084_k();
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean getWorldMapWaypoints() {
/* 167 */     return WorldMap.settings.waypoints;
/*     */   }
/*     */   
/*     */   public int getWorldMapColours() {
/* 171 */     return WorldMap.settings.colours;
/*     */   }
/*     */   
/*     */   public boolean getWorldMapFlowers() {
/* 175 */     return WorldMap.settings.flowers;
/*     */   }
/*     */   
/*     */   public boolean getWorldMapLighting() {
/* 179 */     return WorldMap.settings.lighting;
/*     */   }
/*     */   
/*     */   public boolean getWorldMapTerrainDepth() {
/* 183 */     return WorldMap.settings.terrainDepth;
/*     */   }
/*     */   
/*     */   public boolean getWorldMapTerrainSlopes() {
/* 187 */     return WorldMap.settings.terrainSlopes;
/*     */   }
/*     */   
/*     */   public boolean getWorldMapBiomeColorsVanillaMode() {
/* 191 */     return WorldMap.settings.biomeColorsVanillaMode;
/*     */   }
/*     */   
/*     */   public String getMultiworldId(int dimId) {
/* 195 */     synchronized (MapProcessor.instance.uiSync) {
/* 196 */       MapWorld mapWorld = MapProcessor.instance.getMapWorld();
/* 197 */       MapDimension mapDim = (mapWorld == null) ? null : mapWorld.getDimension(dimId);
/* 198 */       return (mapDim == null || !mapDim.currentMultiworldWritable) ? null : mapDim.getCurrentMultiworld();
/*     */     } 
/*     */   }
/*     */   
/*     */   public String getMultiworldName(int dimId, String multiworldId) {
/* 203 */     synchronized (MapProcessor.instance.uiSync) {
/* 204 */       MapWorld mapWorld = MapProcessor.instance.getMapWorld();
/* 205 */       MapDimension mapDim = (mapWorld == null) ? null : mapWorld.createDimension(null, dimId);
/* 206 */       return (mapDim == null) ? null : mapDim.getMultiworldName(multiworldId);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void openSettings() {
/* 211 */     Minecraft.func_71410_x().func_147108_a((GuiScreen)new GuiWorldMapSettings((Minecraft.func_71410_x()).field_71462_r));
/*     */   }
/*     */ }


/* Location:              C:\Users\ioanm\Downloads\Xaeros_Minimap_20.15.3_Forge_1.12.jar!\xaero\common\mods\SupportXaeroWorldmap.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */