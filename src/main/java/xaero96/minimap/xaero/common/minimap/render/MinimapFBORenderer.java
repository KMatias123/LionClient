/*     */ package xaero.common.minimap.render;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.entity.AbstractClientPlayer;
/*     */ import net.minecraft.client.gui.Gui;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.RenderHelper;
/*     */ import net.minecraft.client.settings.GameSettings;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.player.EnumPlayerModelParts;
/*     */ import org.lwjgl.input.Keyboard;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ import org.lwjgl.opengl.GL14;
/*     */ import org.lwjgl.opengl.GLContext;
/*     */ import xaero.common.IXaeroMinimap;
/*     */ import xaero.common.graphics.ImprovedFramebuffer;
/*     */ import xaero.common.interfaces.render.InterfaceRenderer;
/*     */ import xaero.common.minimap.MinimapProcessor;
/*     */ import xaero.common.minimap.region.MinimapChunk;
/*     */ import xaero.common.minimap.waypoints.render.WaypointsGuiRenderer;
/*     */ import xaero.common.misc.OptimizedMath;
/*     */ import xaero.common.settings.ModSettings;
/*     */ 
/*     */ 
/*     */ public class MinimapFBORenderer
/*     */   extends MinimapRenderer
/*     */ {
/*     */   private ImprovedFramebuffer scalingFramebuffer;
/*     */   private ImprovedFramebuffer rotationFramebuffer;
/*     */   private boolean triedFBO;
/*     */   private boolean loadedFBO;
/*     */   
/*     */   public MinimapFBORenderer(IXaeroMinimap modMain, Minecraft mc, WaypointsGuiRenderer waypointsGuiRenderer) {
/*  36 */     super(modMain, mc, waypointsGuiRenderer);
/*     */   }
/*     */   
/*     */   public void loadFrameBuffer() {
/*  40 */     if (!(GLContext.getCapabilities()).GL_EXT_framebuffer_object && !(GLContext.getCapabilities()).GL_ARB_framebuffer_object && !(GLContext.getCapabilities()).OpenGL30) {
/*  41 */       System.out.println("FBO not supported! Using minimap safe mode.");
/*     */     } else {
/*  43 */       if (!(Minecraft.func_71410_x()).field_71474_y.field_151448_g) {
/*  44 */         (Minecraft.func_71410_x()).field_71474_y.func_74306_a(GameSettings.Options.FBO_ENABLE, 0);
/*  45 */         System.out.println("FBO is supported but off. Turning it on.");
/*     */       } 
/*     */       
/*  48 */       this.scalingFramebuffer = new ImprovedFramebuffer(512, 512, false);
/*     */ 
/*     */       
/*  51 */       this.rotationFramebuffer = new ImprovedFramebuffer(512, 512, false);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  58 */       this.loadedFBO = (this.scalingFramebuffer.field_147616_f != -1 && this.rotationFramebuffer.field_147616_f != -1);
/*     */     } 
/*  60 */     this.triedFBO = true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void renderChunks(MinimapProcessor minimap, int mapSize, int bufferSize, float sizeFix, float partial, int lightLevel, boolean useWorldMap) {
/*  65 */     synchronized (MinimapProcessor.instance.getMinimapWriter()) {
/*  66 */       renderChunksToFBO(minimap, (EntityPlayer)this.mc.field_71439_g, this.mc.func_175606_aa(), bufferSize, mapSize, sizeFix, partial, lightLevel, true, useWorldMap);
/*     */     } 
/*  68 */     Minecraft.func_71410_x().func_147110_a().func_147610_a(false);
/*  69 */     GlStateManager.func_179083_b(0, 0, (Minecraft.func_71410_x().func_147110_a()).field_147621_c, (Minecraft.func_71410_x().func_147110_a()).field_147618_d);
/*  70 */     this.rotationFramebuffer.func_147612_c();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void renderChunksToFBO(MinimapProcessor minimap, EntityPlayer player, Entity renderEntity, int bufferSize, int viewW, float sizeFix, float partial, int level, boolean retryIfError, boolean useWorldMap) {
/*  76 */     double maxVisibleLength = this.modMain.getSettings().getLockNorth() ? viewW : (viewW * Math.sqrt(2.0D));
/*  77 */     int radius = (int)Math.ceil(maxVisibleLength / 2.0D / this.zoom / 64.0D);
/*     */     
/*  79 */     double playerX = minimap.getEntityRadar().getEntityX(renderEntity, partial);
/*  80 */     double playerZ = minimap.getEntityRadar().getEntityZ(renderEntity, partial);
/*  81 */     int xFloored = OptimizedMath.myFloor(playerX);
/*  82 */     int zFloored = OptimizedMath.myFloor(playerZ);
/*  83 */     int playerChunkX = xFloored >> 6;
/*  84 */     int playerChunkZ = zFloored >> 6;
/*  85 */     int offsetX = xFloored & 0x3F;
/*  86 */     int offsetZ = zFloored & 0x3F;
/*  87 */     boolean zooming = ((int)this.zoom != this.zoom);
/*     */     
/*  89 */     this.scalingFramebuffer.func_147610_a(true);
/*  90 */     GL11.glClear(16640);
/*     */     
/*  92 */     GL11.glEnable(3553);
/*  93 */     RenderHelper.func_74518_a();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  98 */     long before = System.currentTimeMillis();
/*     */ 
/*     */     
/* 101 */     GlStateManager.func_179086_m(256);
/* 102 */     GlStateManager.func_179128_n(5889);
/* 103 */     GL11.glPushMatrix();
/* 104 */     GlStateManager.func_179096_D();
/* 105 */     GlStateManager.func_179130_a(0.0D, 512.0D, 512.0D, 0.0D, 1000.0D, 3000.0D);
/* 106 */     GlStateManager.func_179128_n(5888);
/* 107 */     GL11.glPushMatrix();
/* 108 */     GlStateManager.func_179096_D();
/*     */ 
/*     */     
/* 111 */     before = System.currentTimeMillis();
/*     */     
/* 113 */     double xInsidePixel = minimap.getEntityRadar().getEntityX(renderEntity, partial) - xFloored;
/* 114 */     if (xInsidePixel < 0.0D)
/* 115 */       xInsidePixel++; 
/* 116 */     double zInsidePixel = minimap.getEntityRadar().getEntityZ(renderEntity, partial) - zFloored;
/* 117 */     if (zInsidePixel < 0.0D) {
/* 118 */       zInsidePixel++;
/*     */     }
/* 120 */     float halfWView = viewW / 2.0F;
/* 121 */     float angle = (float)(90.0D - getRenderAngle(renderEntity, partial));
/*     */     
/* 123 */     GlStateManager.func_179147_l();
/*     */     
/* 125 */     GlStateManager.func_179109_b(256.0F, 256.0F, -2000.0F);
/* 126 */     GlStateManager.func_179139_a(this.zoom, this.zoom, 1.0D);
/* 127 */     if (useWorldMap) {
/* 128 */       (this.modMain.getSupportMods()).worldmapSupport.drawMinimap(this.helper, xFloored, zFloored, radius, zooming, this.zoom);
/* 129 */     } else if (MinimapProcessor.instance.getMinimapWriter().getLoadedBlocks() != null) {
/* 130 */       Gui.func_73734_a(-256, -256, 256, 256, black.hashCode());
/* 131 */       GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
/* 132 */       int minX = playerChunkX - radius;
/* 133 */       int minZ = playerChunkZ - radius;
/* 134 */       int maxX = playerChunkX + radius + 1;
/* 135 */       int maxZ = playerChunkZ + radius + 1;
/* 136 */       for (int X = minX; X <= maxX; X++) {
/* 137 */         int canvasX = X - MinimapProcessor.instance.getMinimapWriter().getLoadedMapChunkX();
/* 138 */         if (canvasX >= 0 && canvasX < (MinimapProcessor.instance.getMinimapWriter().getLoadedBlocks()).length)
/*     */         {
/* 140 */           for (int Z = minZ; Z <= maxZ; Z++) {
/* 141 */             int canvasZ = Z - MinimapProcessor.instance.getMinimapWriter().getLoadedMapChunkZ();
/* 142 */             if (canvasZ >= 0 && canvasZ < (MinimapProcessor.instance.getMinimapWriter().getLoadedBlocks()).length) {
/*     */               
/* 144 */               MinimapChunk mchunk = MinimapProcessor.instance.getMinimapWriter().getLoadedBlocks()[canvasX][canvasZ];
/* 145 */               if (mchunk != null) {
/*     */                 
/* 147 */                 mchunk.bindTexture(level);
/* 148 */                 if (mchunk.isHasSomething() && level < mchunk.getLevelsBuffered() && mchunk.getGlTexture(level) != 0) {
/*     */ 
/*     */ 
/*     */ 
/*     */                   
/* 153 */                   if (!zooming) {
/* 154 */                     GL11.glTexParameteri(3553, 10240, 9728);
/*     */                   } else {
/* 156 */                     GL11.glTexParameteri(3553, 10240, 9729);
/* 157 */                   }  int drawX = (mchunk.getX() - playerChunkX) * 64 - offsetX;
/* 158 */                   int drawZ = (mchunk.getZ() - playerChunkZ) * 64 - offsetZ;
/* 159 */                   GlStateManager.func_179147_l();
/*     */                   
/* 161 */                   GL14.glBlendFuncSeparate(770, 771, 1, 771);
/* 162 */                   this.helper.drawMyTexturedModalRect(drawX, drawZ, 0, 64, 64.0F, 64.0F, -64.0F, 64.0F);
/* 163 */                   GL11.glTexParameteri(3553, 10240, 9728);
/*     */                   
/* 165 */                   int r = 0;
/* 166 */                   int g = 0;
/* 167 */                   int b = 0;
/* 168 */                   if ((this.modMain.getSettings()).chunkGrid > -1) {
/* 169 */                     int grid = ModSettings.COLORS[(this.modMain.getSettings()).chunkGrid];
/* 170 */                     r = grid >> 16 & 0xFF;
/* 171 */                     g = grid >> 8 & 0xFF;
/* 172 */                     b = grid & 0xFF;
/*     */                   } 
/* 174 */                   for (int t = 0; t < 16; t++) {
/* 175 */                     if (mchunk.getTile(t % 4, t / 4) != null && 
/* 176 */                       this.modMain.getSettings().getSlimeChunks() && mchunk.getTile(t % 4, t / 4).isSlimeChunk()) {
/* 177 */                       int slimeDrawX = drawX + 16 * t % 4;
/* 178 */                       int slimeDrawZ = drawZ + 16 * t / 4;
/* 179 */                       Gui.func_73734_a(slimeDrawX, slimeDrawZ, slimeDrawX + 16, slimeDrawZ + 16, slime.hashCode());
/*     */                     } 
/*     */                   } 
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
/* 196 */                   if ((this.modMain.getSettings()).chunkGrid > -1) {
/* 197 */                     GlStateManager.func_179090_x();
/* 198 */                     GlStateManager.func_179147_l();
/* 199 */                     GlStateManager.func_179094_E();
/* 200 */                     GlStateManager.func_179109_b((drawX + 64), (drawZ + 64), 0.0F);
/* 201 */                     GlStateManager.func_179139_a(1.0D / this.zoom, 1.0D / this.zoom, 1.0D);
/* 202 */                     GlStateManager.func_179131_c(r / 255.0F, g / 255.0F, b / 255.0F, 0.5F);
/* 203 */                     float top = (float)(-64.0D * this.zoom);
/* 204 */                     float left = top;
/* 205 */                     this.helper.drawMyColoredRect(left, -1.0F, 0.0F, 0.0F);
/* 206 */                     this.helper.drawMyColoredRect(left, (float)(-16.0D * this.zoom) - 1.0F, 0.0F, (float)(-16.0D * this.zoom));
/* 207 */                     this.helper.drawMyColoredRect(left, (float)(-32.0D * this.zoom) - 1.0F, 0.0F, (float)(-32.0D * this.zoom));
/* 208 */                     this.helper.drawMyColoredRect(left, (float)(-48.0D * this.zoom) - 1.0F, 0.0F, (float)(-48.0D * this.zoom));
/* 209 */                     this.helper.drawMyColoredRect(-1.0F, top, 0.0F, 0.0F);
/* 210 */                     this.helper.drawMyColoredRect((float)(-16.0D * this.zoom) - 1.0F, top, (float)(-16.0D * this.zoom), 0.0F);
/* 211 */                     this.helper.drawMyColoredRect((float)(-32.0D * this.zoom) - 1.0F, top, (float)(-32.0D * this.zoom), 0.0F);
/* 212 */                     this.helper.drawMyColoredRect((float)(-48.0D * this.zoom) - 1.0F, top, (float)(-48.0D * this.zoom), 0.0F);
/* 213 */                     GlStateManager.func_179121_F();
/* 214 */                     GlStateManager.func_179084_k();
/* 215 */                     GlStateManager.func_179098_w();
/*     */                   } 
/* 217 */                   GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
/*     */                 } 
/*     */               } 
/*     */             } 
/*     */           }  } 
/*     */       } 
/* 223 */       GL14.glBlendFuncSeparate(770, 771, 1, 0);
/*     */     } 
/*     */     
/* 226 */     this.scalingFramebuffer.func_147609_e();
/* 227 */     this.rotationFramebuffer.func_147610_a(false);
/* 228 */     GL11.glClear(16640);
/*     */     
/* 230 */     this.scalingFramebuffer.func_147612_c();
/* 231 */     GlStateManager.func_179096_D();
/*     */     
/* 233 */     if (this.modMain.getSettings().getAntiAliasing()) {
/* 234 */       GL11.glTexParameteri(3553, 10240, 9729);
/* 235 */       GL11.glTexParameteri(3553, 10241, 9729);
/*     */     } else {
/* 237 */       GL11.glTexParameteri(3553, 10240, 9728);
/* 238 */       GL11.glTexParameteri(3553, 10241, 9728);
/*     */     } 
/*     */ 
/*     */     
/* 242 */     GlStateManager.func_179109_b(halfWView, halfWView, -2000.0F);
/* 243 */     if (!this.modMain.getSettings().getLockNorth())
/* 244 */       GL11.glRotatef(-angle, 0.0F, 0.0F, 1.0F); 
/* 245 */     GL11.glPushMatrix();
/* 246 */     GlStateManager.func_179137_b(-xInsidePixel * this.zoom, -zInsidePixel * this.zoom, 0.0D);
/*     */     
/* 248 */     GlStateManager.func_179084_k();
/* 249 */     GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, (this.modMain.getSettings()).minimapOpacity / 100.0F);
/*     */     
/* 251 */     this.helper.drawMyTexturedModalRect(-256.0F, -256.0F, 0, 0, 512.0F, 512.0F, 512.0F);
/*     */ 
/*     */     
/* 254 */     GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
/*     */     
/* 256 */     GL11.glPopMatrix();
/*     */ 
/*     */ 
/*     */     
/* 260 */     before = System.currentTimeMillis();
/*     */     
/* 262 */     this.mc.func_110434_K().func_110577_a(InterfaceRenderer.guiTextures);
/* 263 */     if (this.modMain.getSettings().getSmoothDots()) {
/* 264 */       GL11.glTexParameteri(3553, 10240, 9729);
/* 265 */       GL11.glTexParameteri(3553, 10241, 9729);
/*     */     } else {
/* 267 */       GL11.glTexParameteri(3553, 10240, 9728);
/* 268 */       GL11.glTexParameteri(3553, 10241, 9728);
/*     */     } 
/* 270 */     GlStateManager.func_179147_l();
/* 271 */     GlStateManager.func_179120_a(770, 771, 1, 771);
/* 272 */     EntityPlayer p = player;
/* 273 */     renderEntityListToFBO(minimap, p, renderEntity, MinimapProcessor.instance.getEntityRadar().getEntitiesIterator(), angle, playerX, playerZ, partial);
/* 274 */     renderEntityListToFBO(minimap, p, renderEntity, MinimapProcessor.instance.getEntityRadar().getItemsIterator(), angle, playerX, playerZ, partial);
/* 275 */     renderEntityListToFBO(minimap, p, renderEntity, MinimapProcessor.instance.getEntityRadar().getLivingIterator(), angle, playerX, playerZ, partial);
/* 276 */     renderEntityListToFBO(minimap, p, renderEntity, MinimapProcessor.instance.getEntityRadar().getHostileIterator(), angle, playerX, playerZ, partial);
/* 277 */     renderEntityListToFBO(minimap, p, renderEntity, MinimapProcessor.instance.getEntityRadar().getPlayersIterator(), angle, playerX, playerZ, partial);
/* 278 */     this.mc.func_110434_K().func_110577_a(InterfaceRenderer.guiTextures);
/* 279 */     if ((this.modMain.getSettings()).mainEntityAs == 1) {
/* 280 */       renderEntityDotToFBO(minimap, p, renderEntity, renderEntity, angle, playerX, playerZ, partial);
/*     */     }
/*     */     
/* 283 */     GL11.glTexParameteri(3553, 10240, 9728);
/* 284 */     GL11.glTexParameteri(3553, 10241, 9728);
/*     */     
/* 286 */     this.rotationFramebuffer.func_147609_e();
/*     */ 
/*     */ 
/*     */     
/* 290 */     GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
/* 291 */     GlStateManager.func_179084_k();
/* 292 */     GlStateManager.func_179128_n(5889);
/* 293 */     GL11.glPopMatrix();
/* 294 */     GlStateManager.func_179128_n(5888);
/* 295 */     GL11.glPopMatrix();
/*     */   }
/*     */ 
/*     */   
/*     */   public void renderEntityListToFBO(MinimapProcessor minimap, EntityPlayer p, Entity renderEntity, Iterator<Entity> iter, float angle, double playerX, double playerZ, float partial) {
/* 300 */     while (iter.hasNext()) {
/* 301 */       Entity e = iter.next();
/* 302 */       if (renderEntity == e)
/*     */         continue; 
/* 304 */       if ((Keyboard.isKeyDown(15) || this.modMain.getSettings().getPlayerHeads()) && e instanceof AbstractClientPlayer) {
/*     */         
/* 306 */         renderPlayerHeadToFBO(minimap, p, renderEntity, (AbstractClientPlayer)e, angle, playerX, playerZ, partial); continue;
/*     */       } 
/* 308 */       renderEntityDotToFBO(minimap, p, renderEntity, e, angle, playerX, playerZ, partial);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void renderPlayerHeadToFBO(MinimapProcessor minimap, EntityPlayer p, Entity renderEntity, AbstractClientPlayer e, float angle, double playerX, double playerZ, float partial) {
/* 314 */     if (!minimap.getEntityRadar().shouldRenderEntity((Entity)e))
/*     */       return; 
/* 316 */     double offx = minimap.getEntityRadar().getEntityX((Entity)e, partial) - playerX;
/* 317 */     double offz = minimap.getEntityRadar().getEntityZ((Entity)e, partial) - playerZ;
/* 318 */     double offh = renderEntity.field_70163_u - e.field_70163_u;
/* 319 */     GL11.glPushMatrix();
/* 320 */     GlStateManager.func_179137_b(offx * this.zoom, offz * this.zoom, 0.0D);
/* 321 */     if (!this.modMain.getSettings().getLockNorth())
/* 322 */       GL11.glRotatef(angle, 0.0F, 0.0F, 1.0F); 
/* 323 */     GlStateManager.func_179139_a(2.0D, 2.0D, 1.0D);
/*     */ 
/*     */     
/* 326 */     double brightness = minimap.getEntityRadar().getEntityBrightness(offh);
/* 327 */     GL11.glColor3d(brightness, brightness, brightness);
/*     */ 
/*     */     
/* 330 */     boolean flag1 = (e != null && e.func_175148_a(EnumPlayerModelParts.CAPE) && (e.func_146103_bH().getName().equals("Dinnerbone") || e.func_146103_bH().getName().equals("Grumm")));
/* 331 */     Minecraft.func_71410_x().func_110434_K().func_110577_a(e.func_110306_p());
/* 332 */     int l2 = 8 + (flag1 ? 8 : 0);
/* 333 */     int i3 = 8 * (flag1 ? -1 : 1);
/* 334 */     Gui.func_152125_a(-4, -4, 8.0F, l2, 8, i3, 8, 8, 64.0F, 64.0F);
/* 335 */     if (this.modMain.getSettings().isPlayerNames()) {
/* 336 */       GlStateManager.func_179094_E();
/* 337 */       GlStateManager.func_179152_a(0.5F, 0.5F, 1.0F);
/* 338 */       GlStateManager.func_179129_p();
/* 339 */       int color = minimap.getEntityRadar().getEntityColour(p, (Entity)e, 0.0D);
/*     */       
/* 341 */       this.mc.field_71466_p.func_175065_a(e.func_145748_c_().func_150254_d(), (-this.mc.field_71466_p.func_78256_a(e.func_145748_c_().func_150254_d()) / 2), 11.0F, color, true);
/* 342 */       GlStateManager.func_179089_o();
/* 343 */       GlStateManager.func_179121_F();
/*     */     } 
/*     */     
/* 346 */     if (e != null && e.func_175148_a(EnumPlayerModelParts.HAT)) {
/* 347 */       Minecraft.func_71410_x().func_110434_K().func_110577_a(e.func_110306_p());
/* 348 */       int j3 = 8 + (flag1 ? 8 : 0);
/* 349 */       int k3 = 8 * (flag1 ? -1 : 1);
/* 350 */       Gui.func_152125_a(-4, -4, 40.0F, j3, 8, k3, 8, 8, 64.0F, 64.0F);
/*     */     } 
/* 352 */     GL11.glPopMatrix();
/*     */   }
/*     */   
/*     */   public void renderEntityDotToFBO(MinimapProcessor minimap, EntityPlayer p, Entity renderEntity, Entity e, float angle, double playerX, double playerZ, float partial) {
/* 356 */     if (!minimap.getEntityRadar().shouldRenderEntity(e))
/*     */       return; 
/* 358 */     double offx = minimap.getEntityRadar().getEntityX(e, partial) - playerX;
/* 359 */     double offz = minimap.getEntityRadar().getEntityZ(e, partial) - playerZ;
/* 360 */     double offh = renderEntity.field_70163_u - e.field_70163_u;
/* 361 */     GL11.glPushMatrix();
/* 362 */     GlStateManager.func_179137_b(offx * this.zoom, offz * this.zoom, 0.0D);
/* 363 */     if (!this.modMain.getSettings().getLockNorth()) {
/* 364 */       GL11.glRotatef(angle, 0.0F, 0.0F, 1.0F);
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
/* 379 */     int color = minimap.getEntityRadar().getEntityColour(p, e, offh);
/* 380 */     float f = (color >> 16 & 0xFF) / 255.0F;
/* 381 */     float f1 = (color >> 8 & 0xFF) / 255.0F;
/* 382 */     float f2 = (color & 0xFF) / 255.0F;
/* 383 */     GlStateManager.func_179131_c(f, f1, f2, 1.0F);
/*     */     
/* 385 */     GL11.glScalef((this.modMain.getSettings()).dotsScale, (this.modMain.getSettings()).dotsScale, 1.0F);
/* 386 */     if (this.modMain.getSettings().getSmoothDots()) {
/* 387 */       this.helper.drawMyTexturedModalRect(-3.5F, -3.5F, 1, 88, 8.0F, 8.0F, 256.0F);
/*     */     } else {
/* 389 */       this.helper.drawMyTexturedModalRect(-3.5F, -3.5F, 9, 77, 8.0F, 8.0F, 256.0F);
/* 390 */     }  if ((this.modMain.getSettings()).entityNametags && !(e instanceof EntityPlayer) && e.func_145818_k_()) {
/* 391 */       GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
/* 392 */       GlStateManager.func_179094_E();
/* 393 */       GlStateManager.func_179129_p();
/* 394 */       String name = e.func_174793_f().func_70005_c_();
/* 395 */       int nameW = this.mc.field_71466_p.func_78256_a(name);
/* 396 */       this.mc.field_71466_p.func_78276_b(name, -nameW / 2 + 1, 6, -12566464);
/* 397 */       this.mc.field_71466_p.func_78276_b(name, -nameW / 2, 5, -1);
/* 398 */       GlStateManager.func_179089_o();
/* 399 */       GlStateManager.func_179121_F();
/* 400 */       this.mc.func_110434_K().func_110577_a(InterfaceRenderer.guiTextures);
/*     */     } 
/*     */     
/* 403 */     GL11.glPopMatrix();
/*     */   }
/*     */   
/*     */   public void deleteFramebuffers() {
/* 407 */     this.scalingFramebuffer.func_147608_a();
/* 408 */     this.rotationFramebuffer.func_147608_a();
/*     */   }
/*     */   
/*     */   public boolean isLoadedFBO() {
/* 412 */     return this.loadedFBO;
/*     */   }
/*     */   
/*     */   public void setLoadedFBO(boolean loadedFBO) {
/* 416 */     this.loadedFBO = loadedFBO;
/*     */   }
/*     */   
/*     */   public boolean isTriedFBO() {
/* 420 */     return this.triedFBO;
/*     */   }
/*     */ }


/* Location:              C:\Users\ioanm\Downloads\Xaeros_Minimap_20.15.3_Forge_1.12.jar!\xaero\common\minimap\render\MinimapFBORenderer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */