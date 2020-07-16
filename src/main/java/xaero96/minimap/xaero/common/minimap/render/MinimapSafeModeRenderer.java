/*     */ package xaero.common.minimap.render;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.Iterator;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ import xaero.common.IXaeroMinimap;
/*     */ import xaero.common.graphics.MinimapTexture;
/*     */ import xaero.common.minimap.MinimapProcessor;
/*     */ import xaero.common.minimap.region.MinimapChunk;
/*     */ import xaero.common.minimap.region.MinimapTile;
/*     */ import xaero.common.minimap.waypoints.render.WaypointsGuiRenderer;
/*     */ import xaero.common.misc.OptimizedMath;
/*     */ import xaero.common.settings.ModSettings;
/*     */ 
/*     */ 
/*     */ public class MinimapSafeModeRenderer
/*     */   extends MinimapRenderer
/*     */ {
/*  24 */   private static final ResourceLocation mapTextures = new ResourceLocation("xaeromaptexture");
/*     */   
/*     */   private byte[] bytes;
/*     */   private byte drawYState;
/*     */   private final int[] tempColor;
/*     */   private MinimapTexture mapTexture;
/*     */   
/*     */   public MinimapSafeModeRenderer(IXaeroMinimap modMain, Minecraft mc, WaypointsGuiRenderer waypointsGuiRenderer) {
/*  32 */     super(modMain, mc, waypointsGuiRenderer);
/*  33 */     this.tempColor = new int[3];
/*  34 */     this.mapTexture = new MinimapTexture(mapTextures);
/*     */   }
/*     */   
/*     */   public void updateMapFrameSafeMode(MinimapProcessor minimap, EntityPlayer player, Entity renderEntity, int bufferSize, int mapW, float partial, int level) {
/*  38 */     EntityPlayer p = player;
/*     */     
/*  40 */     long before = System.currentTimeMillis();
/*  41 */     if (minimap.isToResetImage()) {
/*  42 */       this.bytes = new byte[bufferSize * bufferSize * 3];
/*  43 */       minimap.setToResetImage(false);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*  48 */     boolean motionBlur = (Minecraft.func_175610_ah() >= 35);
/*  49 */     int increaseY = motionBlur ? 2 : 1;
/*     */     
/*  51 */     int mapH = mapW;
/*  52 */     int halfW = mapW / 2;
/*  53 */     int halfH = mapH / 2;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  58 */     double halfWZoomed = halfW / this.zoom;
/*  59 */     double halfHZoomed = halfH / this.zoom;
/*  60 */     byte currentState = this.drawYState;
/*  61 */     double angle = Math.toRadians(getRenderAngle(renderEntity, partial));
/*  62 */     double ps = Math.sin(Math.PI - angle);
/*  63 */     double pc = Math.cos(Math.PI - angle);
/*     */ 
/*     */     
/*  66 */     double playerX = minimap.getEntityRadar().getEntityX(renderEntity, partial);
/*  67 */     double playerZ = minimap.getEntityRadar().getEntityZ(renderEntity, partial);
/*  68 */     for (int currentX = 0; currentX < mapW; currentX++) {
/*  69 */       double currentXZoomed = (currentX + 0.5D) / this.zoom;
/*  70 */       double offx = currentXZoomed - halfWZoomed;
/*  71 */       double psx = ps * offx;
/*  72 */       double pcx = pc * offx; int currentY;
/*  73 */       for (currentY = motionBlur ? currentState : 0; currentY < mapH; currentY += increaseY) {
/*  74 */         double offy = (currentY + 0.5D) / this.zoom - halfHZoomed;
/*  75 */         getLoadedBlockColor(minimap, this.tempColor, OptimizedMath.myFloor(playerX + psx + pc * offy), OptimizedMath.myFloor(playerZ + ps * offy - pcx), level);
/*  76 */         this.helper.putColor(this.bytes, currentX, bufferSize - 1 - currentY, this.tempColor[0], this.tempColor[1], this.tempColor[2], bufferSize);
/*     */       } 
/*  78 */       currentState = (byte)((currentState == 1) ? 0 : 1);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*  83 */     renderEntityListSafeMode(minimap, p, renderEntity, minimap.getEntityRadar().getEntitiesIterator(), pc, ps, mapW, bufferSize, halfW, halfH, playerX, playerZ, partial);
/*  84 */     renderEntityListSafeMode(minimap, p, renderEntity, minimap.getEntityRadar().getItemsIterator(), pc, ps, mapW, bufferSize, halfW, halfH, playerX, playerZ, partial);
/*  85 */     renderEntityListSafeMode(minimap, p, renderEntity, minimap.getEntityRadar().getHostileIterator(), pc, ps, mapW, bufferSize, halfW, halfH, playerX, playerZ, partial);
/*  86 */     renderEntityListSafeMode(minimap, p, renderEntity, minimap.getEntityRadar().getLivingIterator(), pc, ps, mapW, bufferSize, halfW, halfH, playerX, playerZ, partial);
/*  87 */     renderEntityListSafeMode(minimap, p, renderEntity, minimap.getEntityRadar().getPlayersIterator(), pc, ps, mapW, bufferSize, halfW, halfH, playerX, playerZ, partial);
/*  88 */     if ((this.modMain.getSettings()).mainEntityAs == 1)
/*  89 */       renderEntityDotSafeMode(minimap, p, renderEntity, renderEntity, pc, ps, mapW, bufferSize, halfW, halfH, playerX, playerZ, partial); 
/*  90 */     this.drawYState = (byte)((this.drawYState == 1) ? 0 : 1);
/*  91 */     ByteBuffer buffer = this.mapTexture.getBuffer(bufferSize);
/*  92 */     buffer.put(this.bytes);
/*  93 */     buffer.flip();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void renderEntityListSafeMode(MinimapProcessor minimap, EntityPlayer p, Entity renderEntity, Iterator<Entity> iter, double pc, double ps, int mapW, int bufferSize, int halfW, int halfH, double playerX, double playerZ, float partial) {
/* 104 */     while (iter.hasNext()) {
/* 105 */       Entity e = iter.next();
/* 106 */       if (renderEntity == e)
/*     */         continue; 
/* 108 */       if (!renderEntityDotSafeMode(minimap, p, renderEntity, e, pc, ps, mapW, bufferSize, halfW, halfH, playerX, playerZ, partial));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean renderEntityDotSafeMode(MinimapProcessor minimap, EntityPlayer p, Entity renderEntity, Entity e, double pc, double ps, int mapW, int bufferSize, int halfW, int halfH, double playerX, double playerZ, float partial) {
/* 115 */     if (!minimap.getEntityRadar().shouldRenderEntity(e))
/* 116 */       return false; 
/* 117 */     double offx = minimap.getEntityRadar().getEntityX(e, partial) - playerX;
/* 118 */     double offz = minimap.getEntityRadar().getEntityZ(e, partial) - playerZ;
/* 119 */     double offh = renderEntity.field_70163_u - e.field_70163_u;
/* 120 */     double Z = pc * offx + ps * offz;
/* 121 */     double X = ps * offx - pc * offz;
/* 122 */     double drawXDouble = halfW + X * this.zoom;
/* 123 */     double drawYDouble = halfH + Z * this.zoom;
/* 124 */     float drawLeft = (float)drawXDouble - 2.5F;
/* 125 */     float drawTop = (float)drawYDouble - 2.5F;
/* 126 */     int drawX = mapW - Math.round(mapW - drawLeft) + 2;
/* 127 */     int drawY = Math.round(drawTop) + 2;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 132 */     int color = minimap.getEntityRadar().getEntityColour(p, e, offh);
/* 133 */     for (int a = drawX - 2; a < drawX + 4; a++) {
/* 134 */       if (a >= 0 && a <= mapW)
/*     */       {
/* 136 */         for (int b = drawY - 2; b < drawY + 4; b++) {
/* 137 */           if (b >= 0 && b <= mapW)
/*     */           {
/* 139 */             if (((a != drawX - 2 && a != drawX + 3) || (b != drawY - 2 && b != drawY + 3)) && (a != drawX + 2 || b != drawY - 2) && (a != drawX + 3 || b != drawY - 1) && (a != drawX - 2 || b != drawY + 2) && (a != drawX - 1 || b != drawY + 3))
/*     */             {
/*     */ 
/*     */               
/* 143 */               if (a == drawX + 3 || b == drawY + 3 || (a == drawX + 2 && b == drawY + 2)) {
/* 144 */                 this.helper.putColor(this.bytes, a, bufferSize - 1 - b, 0, 0, 0, bufferSize);
/*     */               } else {
/* 146 */                 this.helper.putColor(this.bytes, a, bufferSize - 1 - b, color >> 16 & 0xFF, color >> 8 & 0xFF, color & 0xFF, bufferSize);
/*     */               }  }  } 
/*     */         }  } 
/* 149 */     }  return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void renderChunks(MinimapProcessor minimap, int mapSize, int bufferSize, float sizeFix, float partial, int lightLevel, boolean useWorldMap) {
/* 155 */     updateMapFrameSafeMode(minimap, (EntityPlayer)this.mc.field_71439_g, this.mc.func_175606_aa(), bufferSize, mapSize, partial, lightLevel);
/* 156 */     GL11.glScalef(sizeFix, sizeFix, 1.0F);
/* 157 */     this.helper.bindTextureBuffer(this.mapTexture.getBuffer(bufferSize), bufferSize, bufferSize, this.mapTexture
/* 158 */         .func_110552_b());
/* 159 */     GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, (this.modMain.getSettings()).minimapOpacity / 100.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   private void getLoadedBlockColor(MinimapProcessor minimap, int[] result, int par1, int par2, int level) {
/* 164 */     int tileX = par1 >> 4;
/* 165 */     int tileZ = par2 >> 4;
/* 166 */     int chunkX = (tileX >> 2) - minimap.getMinimapWriter().getLoadedMapChunkX();
/* 167 */     int chunkZ = (tileZ >> 2) - minimap.getMinimapWriter().getLoadedMapChunkZ();
/* 168 */     if (minimap.getMinimapWriter().getLoadedBlocks() == null || chunkX < 0 || chunkX >= (minimap.getMinimapWriter().getLoadedBlocks()).length || chunkZ < 0 || chunkZ >= (minimap.getMinimapWriter().getLoadedBlocks()).length) {
/* 169 */       result[2] = 1; result[1] = 1; result[0] = 1;
/*     */       return;
/*     */     } 
/*     */     try {
/* 173 */       MinimapChunk current = minimap.getMinimapWriter().getLoadedBlocks()[chunkX][chunkZ];
/* 174 */       if (current != null) {
/* 175 */         MinimapTile tile = current.getTile(tileX & 0x3, tileZ & 0x3);
/* 176 */         if (tile != null) {
/* 177 */           int insideX = par1 & 0xF;
/* 178 */           int insideZ = par2 & 0xF;
/* 179 */           chunkOverlay(result, tile.getRed(level, insideX, insideZ), tile.getGreen(level, insideX, insideZ), tile.getBlue(level, insideX, insideZ), tile);
/*     */           return;
/*     */         } 
/*     */       } 
/* 183 */     } catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {}
/* 184 */     result[2] = 1; result[1] = 1; result[0] = 1;
/*     */   }
/*     */   
/*     */   private void chunkOverlay(int[] result, int red, int green, int blue, MinimapTile c) {
/* 188 */     if (this.modMain.getSettings().getSlimeChunks() && c.isSlimeChunk()) {
/* 189 */       this.helper.slimeOverlay(result, red, green, blue);
/* 190 */     } else if ((this.modMain.getSettings()).chunkGrid > -1 && c.isChunkGrid()) {
/* 191 */       this.helper.gridOverlay(result, ModSettings.COLORS[(this.modMain.getSettings()).chunkGrid], red, green, blue);
/*     */     } else {
/* 193 */       result[0] = red;
/* 194 */       result[1] = green;
/* 195 */       result[2] = blue;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\ioanm\Downloads\Xaeros_Minimap_20.15.3_Forge_1.12.jar!\xaero\common\minimap\render\MinimapSafeModeRenderer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */