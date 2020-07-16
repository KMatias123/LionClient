/*     */ package xaero.common.minimap.render;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.util.ArrayList;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.RenderHelper;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.util.math.MathHelper;
/*     */ import net.minecraft.world.EnumSkyBlock;
/*     */ import net.minecraft.world.chunk.Chunk;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ import xaero.common.IXaeroMinimap;
/*     */ import xaero.common.interfaces.render.InterfaceRenderer;
/*     */ import xaero.common.minimap.MinimapProcessor;
/*     */ import xaero.common.minimap.MinimapRadar;
/*     */ import xaero.common.minimap.waypoints.render.WaypointsGuiRenderer;
/*     */ import xaero.common.misc.OptimizedMath;
/*     */ 
/*     */ 
/*     */ public abstract class MinimapRenderer
/*     */ {
/*  26 */   public static final Color black = new Color(0, 0, 0, 255);
/*  27 */   public static final Color slime = new Color(82, 241, 64, 128);
/*     */   
/*     */   protected IXaeroMinimap modMain;
/*     */   protected Minecraft mc;
/*     */   protected MinimapRendererHelper helper;
/*     */   private WaypointsGuiRenderer waypointsGuiRenderer;
/*     */   private int lastMinimapSize;
/*     */   private ArrayList<String> underText;
/*  35 */   protected double zoom = 1.0D;
/*     */   private BlockPos.MutableBlockPos mutableBlockPos;
/*     */   
/*     */   public MinimapRenderer(IXaeroMinimap modMain, Minecraft mc, WaypointsGuiRenderer waypointsGuiRenderer) {
/*  39 */     this.modMain = modMain;
/*  40 */     this.mc = mc;
/*  41 */     this.waypointsGuiRenderer = waypointsGuiRenderer;
/*  42 */     this.underText = new ArrayList<>();
/*  43 */     this.helper = new MinimapRendererHelper();
/*  44 */     this.mutableBlockPos = new BlockPos.MutableBlockPos();
/*     */   }
/*     */   
/*     */   public float getEntityYaw(Entity e, float partial) {
/*  48 */     if (e == this.mc.field_71439_g)
/*  49 */       return e.field_70177_z; 
/*  50 */     if (e instanceof EntityLivingBase)
/*  51 */       return ((EntityLivingBase)e).field_70758_at + (((EntityLivingBase)e).field_70759_as - ((EntityLivingBase)e).field_70758_at) * partial; 
/*  52 */     return e.field_70126_B + (e.field_70177_z - e.field_70126_B) * partial;
/*     */   }
/*     */   
/*     */   public double getRenderAngle(Entity player, float partial) {
/*  56 */     if (this.modMain.getSettings().getLockNorth())
/*  57 */       return 90.0D; 
/*  58 */     return getActualAngle(player, partial);
/*     */   }
/*     */   
/*     */   public double getActualAngle(Entity player, float partial) {
/*  62 */     double rotation = getEntityYaw(player, partial);
/*  63 */     if (rotation < 0.0D || rotation > 360.0D)
/*  64 */       rotation %= 360.0D; 
/*  65 */     double angle = 270.0D - rotation;
/*  66 */     if (angle < 0.0D || angle > 360.0D)
/*  67 */       angle %= 360.0D; 
/*  68 */     return angle;
/*     */   }
/*     */   
/*     */   protected abstract void renderChunks(MinimapProcessor paramMinimapProcessor, int paramInt1, int paramInt2, float paramFloat1, float paramFloat2, int paramInt3, boolean paramBoolean);
/*     */   
/*     */   public void renderMinimap(MinimapProcessor minimap, int x, int y, int width, int height, int scale, int size, float partial) {
/*  74 */     if (this.modMain.getSettings().getMinimapSize() != this.lastMinimapSize) {
/*  75 */       this.lastMinimapSize = this.modMain.getSettings().getMinimapSize();
/*  76 */       minimap.setToResetImage(true);
/*     */     } 
/*     */     
/*  79 */     long before = System.currentTimeMillis();
/*  80 */     int mapSize = minimap.getMinimapSize();
/*  81 */     int bufferSize = minimap.getMinimapBufferSize();
/*  82 */     if (minimap.usingFBO())
/*  83 */       bufferSize = minimap.getFBOBufferSize(); 
/*  84 */     float mapScale = scale / 2.0F / this.modMain.getSettings().getMinimapScale();
/*     */     
/*  86 */     minimap.updateZoom();
/*  87 */     this.zoom = minimap.getMinimapZoom();
/*     */ 
/*     */ 
/*     */     
/*  91 */     RenderHelper.func_74518_a();
/*  92 */     GlStateManager.func_179097_i();
/*  93 */     GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
/*     */     
/*  95 */     GlStateManager.func_187425_g(3317, 4);
/*  96 */     GlStateManager.func_187425_g(3316, 0);
/*  97 */     GlStateManager.func_187425_g(3315, 0);
/*  98 */     GlStateManager.func_187425_g(3314, 0);
/*     */     
/* 100 */     float sizeFix = bufferSize / 512.0F;
/*     */     
/* 102 */     boolean useWorldMap = (this.modMain.getSupportMods().shouldUseWorldMapChunks() && MinimapProcessor.instance.getMinimapWriter().getLoadedCaving() == -1);
/*     */ 
/*     */     
/* 105 */     int lightLevel = (int)((1.0F - Math.min(1.0F, this.mc.field_71441_e.getSunBrightnessFactor(1.0F))) * (minimap.getMinimapWriter().getLoadedLevels() - 1));
/* 106 */     if (useWorldMap || lightLevel >= 0)
/* 107 */       renderChunks(minimap, mapSize, bufferSize, sizeFix, partial, lightLevel, useWorldMap); 
/* 108 */     if (minimap.usingFBO()) {
/* 109 */       sizeFix = 1.0F;
/*     */     }
/*     */ 
/*     */     
/* 113 */     GlStateManager.func_179147_l();
/* 114 */     GlStateManager.func_179112_b(770, 771);
/*     */     
/* 116 */     GL11.glScalef(1.0F / mapScale, 1.0F / mapScale, 1.0F);
/* 117 */     int scaledX = (int)(x * mapScale);
/* 118 */     int scaledY = (int)(y * mapScale);
/* 119 */     int minimapFrameSize = (int)((mapSize / 2) / sizeFix);
/* 120 */     this.helper.drawMyTexturedModalRect((int)((scaledX + 9) / sizeFix), (int)((scaledY + 9) / sizeFix), 0, 256 - minimapFrameSize, minimapFrameSize, minimapFrameSize, 256.0F);
/*     */     
/* 122 */     if (!minimap.usingFBO()) {
/* 123 */       GL11.glScalef(1.0F / sizeFix, 1.0F / sizeFix, 1.0F);
/* 124 */       GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
/*     */     } 
/*     */     
/* 127 */     if ((this.modMain.getSettings()).mainEntityAs == 0 && !this.modMain.getSettings().getLockNorth()) {
/* 128 */       GlStateManager.func_179094_E();
/* 129 */       GlStateManager.func_179109_b((scaledX + 9), (scaledY + 9), 0.0F);
/* 130 */       GL11.glScalef(0.5F, 0.5F, 1.0F);
/*     */ 
/*     */       
/* 133 */       GlStateManager.func_179090_x();
/* 134 */       GlStateManager.func_179109_b((mapSize / 2), (mapSize / 2), 0.0F);
/* 135 */       GlStateManager.func_179120_a(775, 0, 1, 0);
/* 136 */       this.helper.drawMyColoredRect(-5.0F, -1.0F, 5.0F, 1.0F);
/* 137 */       this.helper.drawMyColoredRect(-1.0F, 3.0F, 1.0F, 5.0F);
/* 138 */       this.helper.drawMyColoredRect(-1.0F, -5.0F, 1.0F, -3.0F);
/*     */       
/* 140 */       GlStateManager.func_179112_b(770, 771);
/* 141 */       int crosshairColor = minimap.getEntityRadar().getEntityColour((EntityPlayer)this.mc.field_71439_g, this.mc.func_175606_aa(), 0.0D);
/* 142 */       GlStateManager.func_179131_c((crosshairColor >> 16 & 0xFF) / 255.0F, (crosshairColor >> 8 & 0xFF) / 255.0F, (crosshairColor & 0xFF) / 255.0F, 1.0F);
/* 143 */       this.helper.drawMyColoredRect(1.0F, -1.0F, 3.0F, 1.0F);
/* 144 */       this.helper.drawMyColoredRect(-3.0F, -1.0F, -1.0F, 1.0F);
/* 145 */       this.helper.drawMyColoredRect(-1.0F, 1.0F, 1.0F, 3.0F);
/* 146 */       this.helper.drawMyColoredRect(-1.0F, -3.0F, 1.0F, -1.0F);
/* 147 */       GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 164 */       GlStateManager.func_179098_w();
/* 165 */       GlStateManager.func_179147_l();
/* 166 */       GlStateManager.func_179121_F();
/*     */     } 
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
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 182 */     int specW = mapSize / 2 + 6;
/* 183 */     int specH = specW;
/* 184 */     double angle = Math.toRadians(getRenderAngle(this.mc.func_175606_aa(), partial));
/* 185 */     double ps = Math.sin(Math.PI - angle);
/* 186 */     double pc = Math.cos(Math.PI - angle);
/*     */     
/* 188 */     this.mc.func_110434_K().func_110577_a(InterfaceRenderer.guiTextures);
/* 189 */     if (this.modMain.getSettings().getLockNorth() || (this.modMain.getSettings()).mainEntityAs == 2) {
/* 190 */       float r, g, b, a; GL11.glTexParameteri(3553, 10240, 9729);
/* 191 */       GL11.glTexParameteri(3553, 10241, 9729);
/* 192 */       double arrowX = (2 * scaledX + 18 + mapSize / 2);
/* 193 */       double arrowY = (2 * scaledY + 18 + mapSize / 2);
/* 194 */       GL11.glPushMatrix();
/* 195 */       GL11.glScalef(0.5F, 0.5F, 1.0F);
/* 196 */       float arrowAngle = this.modMain.getSettings().getLockNorth() ? getEntityYaw(this.mc.func_175606_aa(), partial) : 180.0F;
/* 197 */       float arrowOpacity = (this.modMain.getSettings()).playerArrowOpacity / 100.0F;
/* 198 */       drawArrow(arrowAngle, arrowX, arrowY + 1.0D, 0.0F, 0.0F, 0.0F, 0.5F * arrowOpacity);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 203 */       if ((this.modMain.getSettings()).arrowColour != -1) {
/* 204 */         float[] c = (this.modMain.getSettings()).arrowColours[(this.modMain.getSettings()).arrowColour];
/* 205 */         r = c[0];
/* 206 */         g = c[1];
/* 207 */         b = c[2];
/* 208 */         a = c[3];
/*     */       } else {
/* 210 */         int rgb = minimap.getEntityRadar().getPlayerTeamColour((EntityPlayer)this.mc.field_71439_g);
/* 211 */         if (rgb != -1) {
/* 212 */           r = (rgb >> 16 & 0xFF) / 255.0F;
/* 213 */           g = (rgb >> 8 & 0xFF) / 255.0F;
/* 214 */           b = (rgb & 0xFF) / 255.0F;
/* 215 */           a = 1.0F;
/*     */         } else {
/* 217 */           float[] c = (this.modMain.getSettings()).arrowColours[0];
/* 218 */           r = c[0];
/* 219 */           g = c[1];
/* 220 */           b = c[2];
/* 221 */           a = c[3];
/*     */         } 
/*     */       } 
/* 224 */       a *= arrowOpacity;
/* 225 */       drawArrow(arrowAngle, arrowX, arrowY, r, g, b, a);
/* 226 */       GL11.glPopMatrix();
/* 227 */       GL11.glTexParameteri(3553, 10240, 9728);
/* 228 */       GL11.glTexParameteri(3553, 10241, 9728);
/*     */     } 
/*     */     
/* 231 */     this.mc.field_71456_v.func_73729_b(scaledX + 9 - 4, scaledY + 9 - 4, 0, 0, 17, 15);
/*     */     
/* 233 */     this.mc.field_71456_v.func_73729_b(scaledX + 9 - 4 + mapSize / 2 - 9, scaledY + 9 - 4, 0, 15, 17, 15);
/*     */ 
/*     */     
/* 236 */     this.mc.field_71456_v.func_73729_b(scaledX + 9 - 4, scaledY + 9 - 4 + mapSize / 2 - 7, 0, 30, 17, 15);
/*     */ 
/*     */ 
/*     */     
/* 240 */     this.mc.field_71456_v.func_73729_b(scaledX + 9 - 4 + mapSize / 2 - 9, scaledY + 9 - 4 + mapSize / 2 - 7, 0, 45, 17, 15);
/*     */ 
/*     */ 
/*     */     
/* 244 */     int horLineLength = (mapSize / 2 - 16) / 16;
/* 245 */     for (int i = 0; i < horLineLength; i++) {
/* 246 */       this.mc.field_71456_v.func_73729_b(scaledX + 9 - 4 + 17 + i * 16, scaledY + 9 - 4, 0, 60, 16, 4);
/*     */ 
/*     */       
/* 249 */       this.mc.field_71456_v.func_73729_b(scaledX + 9 - 4 + 17 + i * 16, scaledY + 9 - 4 + mapSize / 2 + 9 - 5, 0, 64, 16, 4);
/*     */     } 
/*     */ 
/*     */     
/* 253 */     int vertLineLength = (mapSize / 2 - 14) / 5;
/* 254 */     for (int j = 0; j < vertLineLength; j++) {
/* 255 */       this.mc.field_71456_v.func_73729_b(scaledX + 9 - 4, scaledY + 9 - 4 + 15 + j * 5, 0, 68, 4, 5);
/*     */ 
/*     */       
/* 258 */       this.mc.field_71456_v.func_73729_b(scaledX + 9 - 4 + mapSize / 2 + 9 - 5, scaledY + 9 - 4 + 15 + j * 5, 0, 73, 4, 5);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 264 */     GL11.glPushMatrix();
/* 265 */     GlStateManager.func_179152_a(0.5F, 0.5F, 1.0F);
/* 266 */     GlStateManager.func_179109_b((2 * scaledX + 18 + mapSize / 2), (2 * scaledY + 18 + mapSize / 2), 0.0F);
/*     */     
/* 268 */     double playerX = minimap.getEntityRadar().getEntityX(this.mc.func_175606_aa(), partial);
/* 269 */     double playerZ = minimap.getEntityRadar().getEntityZ(this.mc.func_175606_aa(), partial);
/* 270 */     this.waypointsGuiRenderer.render(playerX, playerZ, specW, specH, ps, pc, partial, this.zoom);
/* 271 */     GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
/*     */     
/* 273 */     GL11.glPopMatrix();
/* 274 */     if (this.modMain.getSettings().getShowCoords()) {
/* 275 */       int interfaceSize = size / 2;
/*     */       
/* 277 */       String coords = OptimizedMath.myFloor((this.mc.func_175606_aa()).field_70165_t) + ", " + OptimizedMath.myFloor((this.mc.func_175606_aa()).field_70163_u) + ", " + OptimizedMath.myFloor((this.mc.func_175606_aa()).field_70161_v);
/* 278 */       if (this.mc.field_71466_p.func_78256_a(coords) >= interfaceSize) {
/* 279 */         String stringLevel = "" + OptimizedMath.myFloor((this.mc.func_175606_aa()).field_70163_u);
/* 280 */         coords = OptimizedMath.myFloor((this.mc.func_175606_aa()).field_70165_t) + ", " + OptimizedMath.myFloor((this.mc.func_175606_aa()).field_70161_v);
/* 281 */         this.underText.add(coords);
/* 282 */         this.underText.add(stringLevel);
/*     */       } else {
/* 284 */         this.underText.add(coords);
/*     */       } 
/* 286 */     }  int playerBlockX = OptimizedMath.myFloor((this.mc.func_175606_aa()).field_70165_t);
/* 287 */     int playerBlockY = OptimizedMath.myFloor((this.mc.func_175606_aa().func_174813_aQ()).field_72338_b);
/* 288 */     int playerBlockZ = OptimizedMath.myFloor((this.mc.func_175606_aa()).field_70161_v);
/* 289 */     BlockPos.MutableBlockPos mutableBlockPos = this.mutableBlockPos.func_181079_c(playerBlockX, playerBlockY, playerBlockZ);
/* 290 */     Chunk chunk = this.mc.field_71441_e.func_175726_f((BlockPos)mutableBlockPos);
/* 291 */     if ((this.modMain.getSettings()).showBiome) {
/* 292 */       String biomeText = chunk.func_177411_a((BlockPos)mutableBlockPos, this.mc.field_71441_e.func_72959_q()).func_185359_l();
/* 293 */       if (this.mc.field_71466_p.func_78256_a(biomeText) * scale <= size) {
/* 294 */         this.underText.add(biomeText);
/*     */       } else {
/* 296 */         String[] biomeWords = biomeText.split(" ");
/* 297 */         StringBuilder lineBuilder = new StringBuilder();
/* 298 */         for (int k = 0; k < biomeWords.length; k++) {
/* 299 */           int wordStart = lineBuilder.length();
/* 300 */           if (k > 0)
/* 301 */             lineBuilder.append(' '); 
/* 302 */           lineBuilder.append(biomeWords[k]);
/* 303 */           if (k != 0) {
/*     */             
/* 305 */             int lineWidth = this.mc.field_71466_p.func_78256_a(lineBuilder.toString()) * scale;
/* 306 */             if (lineWidth > size) {
/* 307 */               lineBuilder.delete(wordStart, lineBuilder.length());
/* 308 */               this.underText.add(lineBuilder.toString());
/* 309 */               lineBuilder.delete(0, lineBuilder.length());
/* 310 */               lineBuilder.append(biomeWords[k]);
/*     */             } 
/*     */           } 
/* 313 */         }  this.underText.add(lineBuilder.toString());
/*     */       } 
/*     */     } 
/* 316 */     if ((this.modMain.getSettings()).showLightLevel) {
/* 317 */       int playerBlockLightLevel = 15;
/* 318 */       if (playerBlockY >= 0 && playerBlockY < 256)
/* 319 */         playerBlockLightLevel = chunk.func_177413_a(EnumSkyBlock.BLOCK, (BlockPos)mutableBlockPos); 
/* 320 */       this.underText.add(String.format("Light: %d", new Object[] { Integer.valueOf(playerBlockLightLevel) }));
/*     */     } 
/* 322 */     if ((this.modMain.getSettings()).showTime != 0) {
/* 323 */       long totalTime = 6000L + this.mc.field_71441_e.func_72820_D();
/* 324 */       int dayNumber = (int)(totalTime / 24000L) + 1;
/* 325 */       int dayTimeSinceMidnight = (int)(totalTime % 24000L);
/* 326 */       int timeHours = dayTimeSinceMidnight / 1000;
/* 327 */       int minutes = (int)((dayTimeSinceMidnight % 1000) / 1000.0D * 60.0D);
/* 328 */       if ((this.modMain.getSettings()).showTime == 1) {
/* 329 */         this.underText.add(String.format("Day %d, %02d:%02d", new Object[] { Integer.valueOf(dayNumber), Integer.valueOf(timeHours), Integer.valueOf(minutes) }));
/*     */       } else {
/* 331 */         String half = "AM";
/* 332 */         if (timeHours >= 12) {
/* 333 */           timeHours -= 12;
/* 334 */           half = "PM";
/*     */         } 
/* 336 */         if (timeHours == 0)
/* 337 */           timeHours = 12; 
/* 338 */         this.underText.add(String.format("Day %d, %02d:%02d %s", new Object[] { Integer.valueOf(dayNumber), Integer.valueOf(timeHours), Integer.valueOf(minutes), half }));
/*     */       } 
/*     */     } 
/* 341 */     if ((minimap.getMinimapWriter()).debugTotalTime) {
/* 342 */       this.underText.add(String.format("Min Time %d", new Object[] { Long.valueOf((minimap.getMinimapWriter()).minTimeDebug) }));
/* 343 */       this.underText.add(String.format("Average %d", new Object[] { Long.valueOf((minimap.getMinimapWriter()).averageTimeDebug) }));
/* 344 */       this.underText.add(String.format("Max Time %d", new Object[] { Long.valueOf((minimap.getMinimapWriter()).maxTimeDebug) }));
/*     */     } 
/* 346 */     if ((this.modMain.getSettings()).showAngles) {
/* 347 */       this.underText.add(String.format("%.1f / %.1f", new Object[] { Float.valueOf(MathHelper.func_76142_g((this.mc.func_175606_aa()).field_70177_z)), Float.valueOf(MathHelper.func_76142_g((this.mc.func_175606_aa()).field_70125_A)) }));
/*     */     }
/* 349 */     drawTextUnderMinimap(scaledX, scaledY, height, size, mapScale);
/* 350 */     GL11.glScalef(mapScale, mapScale, 1.0F);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void drawArrow(float angle, double arrowX, double arrowY, float r, float g, float b, float a) {
/* 356 */     GL11.glPushMatrix();
/* 357 */     GL11.glTranslated(arrowX, arrowY, 0.0D);
/* 358 */     GlStateManager.func_179114_b(angle, 0.0F, 0.0F, 1.0F);
/* 359 */     GL11.glScalef(0.5F * (this.modMain.getSettings()).arrowScale, 0.5F * (this.modMain.getSettings()).arrowScale, 1.0F);
/* 360 */     GL11.glTranslated(-13.0D, -6.0D, 0.0D);
/*     */     
/* 362 */     GlStateManager.func_179131_c(r, g, b, a);
/* 363 */     this.mc.field_71456_v.func_73729_b(0, 0, 49, 0, 26, 27);
/* 364 */     GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
/* 365 */     GL11.glPopMatrix();
/*     */   }
/*     */   
/*     */   public void drawTextUnderMinimap(int scaledX, int scaledY, int height, int size, float mapScale) {
/* 369 */     int interfaceSize = size / 2;
/* 370 */     int scaledHeight = (int)(height * mapScale);
/* 371 */     GlStateManager.func_179097_i();
/* 372 */     for (int i = 0; i < this.underText.size(); i++) {
/* 373 */       String s = this.underText.get(i);
/* 374 */       int stringWidth = this.mc.field_71466_p.func_78256_a(s);
/* 375 */       boolean under = (scaledY + interfaceSize / 2 < scaledHeight / 2);
/* 376 */       int stringY = scaledY + (under ? interfaceSize : -9) + i * 10 * (under ? 1 : -1);
/* 377 */       int align = (this.modMain.getSettings()).minimapTextAlign;
/* 378 */       (Minecraft.func_71410_x()).field_71466_p.func_175063_a(s, (scaledX + ((align == 0) ? (interfaceSize / 2 - stringWidth / 2) : ((align == 1) ? 6 : (interfaceSize - 6 - stringWidth)))), stringY, MinimapRadar.radarPlayers.hashCode());
/*     */     } 
/*     */     
/* 381 */     this.underText.clear();
/*     */   }
/*     */   
/*     */   public double getZoom() {
/* 385 */     return this.zoom;
/*     */   }
/*     */   
/*     */   public void setZoom(double zoom) {
/* 389 */     this.zoom = zoom;
/*     */   }
/*     */ }


/* Location:              C:\Users\ioanm\Downloads\Xaeros_Minimap_20.15.3_Forge_1.12.jar!\xaero\common\minimap\render\MinimapRenderer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */