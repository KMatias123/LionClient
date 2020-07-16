/*     */ package xaero.common.minimap.waypoints.render;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.FontRenderer;
/*     */ import net.minecraft.client.renderer.BufferBuilder;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.RenderHelper;
/*     */ import net.minecraft.client.renderer.Tessellator;
/*     */ import net.minecraft.client.renderer.entity.RenderManager;
/*     */ import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.util.math.MathHelper;
/*     */ import net.minecraft.util.math.Vec3d;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ import xaero.common.IXaeroMinimap;
/*     */ import xaero.common.api.spigot.ServerWaypointStorage;
/*     */ import xaero.common.gui.GuiMisc;
/*     */ import xaero.common.interfaces.render.InterfaceRenderer;
/*     */ import xaero.common.minimap.waypoints.Waypoint;
/*     */ import xaero.common.minimap.waypoints.WaypointSet;
/*     */ import xaero.common.minimap.waypoints.WaypointsManager;
/*     */ import xaero.common.settings.ModSettings;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class WaypointsIngameRenderer
/*     */ {
/*     */   private IXaeroMinimap modMain;
/*     */   private WaypointsManager waypointsManager;
/*     */   
/*     */   public WaypointsIngameRenderer(IXaeroMinimap modMain, Minecraft mc) {
/*  37 */     this.modMain = modMain;
/*  38 */     this.waypointsManager = modMain.getWaypointsManager();
/*     */   }
/*     */   
/*     */   public void render(float partial) {
/*  42 */     if (this.modMain.getSettings().getShowIngameWaypoints()) {
/*  43 */       Entity entity = Minecraft.func_71410_x().func_175606_aa();
/*  44 */       double d3 = entity.field_70142_S + (entity.field_70165_t - entity.field_70142_S) * partial;
/*  45 */       double d4 = entity.field_70137_T + (entity.field_70163_u - entity.field_70137_T) * partial;
/*  46 */       double d5 = entity.field_70136_U + (entity.field_70161_v - entity.field_70136_U) * partial;
/*  47 */       Tessellator tessellator = Tessellator.func_178181_a();
/*  48 */       BufferBuilder bufferbuilder = tessellator.func_178180_c();
/*  49 */       boolean divideBy8 = this.waypointsManager.divideBy8(this.waypointsManager.getCurrentContainerID());
/*  50 */       if (this.waypointsManager.getWaypoints() != null)
/*  51 */         if ((this.modMain.getSettings()).renderAllSets) {
/*  52 */           HashMap<String, WaypointSet> sets = this.waypointsManager.getCurrentWorld().getSets();
/*  53 */           for (Map.Entry<String, WaypointSet> setEntry : sets.entrySet())
/*  54 */             renderWaypointsList(((WaypointSet)setEntry.getValue()).getList(), d3, d4, d5, entity, bufferbuilder, tessellator, divideBy8); 
/*     */         } else {
/*  56 */           renderWaypointsList(this.waypointsManager.getWaypoints().getList(), d3, d4, d5, entity, bufferbuilder, tessellator, divideBy8);
/*     */         }  
/*  58 */       Hashtable<String, Hashtable<Integer, Waypoint>> customWaypoints = WaypointsManager.customWaypoints;
/*  59 */       if (!customWaypoints.isEmpty()) {
/*  60 */         Iterator<Hashtable<Integer, Waypoint>> modIter = customWaypoints.values().iterator();
/*  61 */         while (modIter.hasNext()) {
/*  62 */           Hashtable<Integer, Waypoint> modCustomWaypoints = modIter.next();
/*  63 */           renderWaypointsList(modCustomWaypoints.values(), d3, d4, d5, entity, bufferbuilder, tessellator, divideBy8);
/*     */         } 
/*     */       } 
/*  66 */       if (ServerWaypointStorage.working() && this.waypointsManager.getServerWaypoints() != null) {
/*  67 */         renderWaypointsList(this.waypointsManager.getServerWaypoints(), d3, d4, d5, entity, bufferbuilder, tessellator, divideBy8);
/*     */       }
/*  69 */       RenderHelper.func_74518_a();
/*  70 */       GlStateManager.func_179126_j();
/*  71 */       GlStateManager.func_179132_a(true);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void renderWaypointsList(Collection<Waypoint> list, double d3, double d4, double d5, Entity entity, BufferBuilder bufferbuilder, Tessellator tessellator, boolean divideBy8) {
/*  76 */     float cameraAngleYaw = MathHelper.func_76142_g(entity.field_70177_z);
/*  77 */     Vec3d lookVector = entity.func_70040_Z();
/*  78 */     int lookVectorMultiplier = ((Minecraft.func_71410_x()).field_71474_y.field_74320_O == 2) ? -1 : 1;
/*  79 */     double eyesX = d3;
/*  80 */     double eyesY = d4 + entity.func_70047_e();
/*  81 */     double eyesZ = d5;
/*  82 */     if ((Minecraft.func_71410_x()).field_71474_y.field_74320_O > 0) {
/*  83 */       eyesX -= 4.0D * lookVector.field_72450_a * lookVectorMultiplier;
/*  84 */       eyesY -= 4.0D * lookVector.field_72448_b * lookVectorMultiplier;
/*  85 */       eyesZ -= 4.0D * lookVector.field_72449_c * lookVectorMultiplier;
/*     */     } 
/*  87 */     Iterator<Waypoint> iter = list.iterator();
/*  88 */     while (iter.hasNext()) {
/*  89 */       Waypoint w = iter.next();
/*  90 */       renderWaypointIngame(cameraAngleYaw, lookVector, lookVectorMultiplier, eyesX, eyesY, eyesZ, w, this.modMain, 12.0D, d3, d4, d5, entity, bufferbuilder, tessellator, divideBy8);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void renderWaypointIngame(float cameraAngleYaw, Vec3d lookVector, int lookVectorMultiplier, double eyesX, double eyesY, double eyesZ, Waypoint w, IXaeroMinimap modMain, double radius, double d3, double d4, double d5, Entity entity, BufferBuilder bufferBuilder, Tessellator tessellator, boolean divideBy8) {
/*  96 */     if (w.isDisabled() || (w.getType() == 1 && !modMain.getSettings().getDeathpoints()))
/*     */       return; 
/*  98 */     double wpRenderX = w.getX(divideBy8) + 0.5D;
/*  99 */     double wpRenderY = w.getY() + 1.0D;
/* 100 */     double wpRenderZ = w.getZ(divideBy8) + 0.5D;
/* 101 */     float offX = (float)(wpRenderX - d3);
/* 102 */     float offY = (float)(wpRenderY - d4);
/* 103 */     float offZ = (float)(wpRenderZ - d5);
/* 104 */     float fromEyesX = (float)(wpRenderX - eyesX);
/* 105 */     float fromEyesY = (float)(wpRenderY - eyesY);
/* 106 */     float fromEyesZ = (float)(wpRenderZ - eyesZ);
/* 107 */     if ((fromEyesX * lookVector.field_72450_a + fromEyesY * lookVector.field_72448_b + fromEyesZ * lookVector.field_72449_c) * lookVectorMultiplier < 0.0D)
/*     */       return; 
/* 109 */     double distance = Math.sqrt((offX * offX + offY * offY + offZ * offZ));
/* 110 */     double correctDistance = Math.sqrt((offX * offX + (offY - 1.0F) * (offY - 1.0F) + offZ * offZ));
/* 111 */     double distance2D = Math.sqrt((offX * offX + offZ * offZ));
/* 112 */     if ((w.getType() == 0 && !w.isGlobal() && (modMain.getSettings()).waypointsDistance != 0.0F && distance2D > (modMain.getSettings()).waypointsDistance) || ((modMain.getSettings()).waypointsDistanceMin != 0.0F && distance2D < (modMain.getSettings()).waypointsDistanceMin))
/*     */       return; 
/* 114 */     RenderManager renderManager = Minecraft.func_71410_x().func_175598_ae();
/* 115 */     FontRenderer fontrenderer = renderManager.func_78716_a();
/* 116 */     if (fontrenderer == null)
/*     */       return; 
/* 118 */     float f = 1.6F;
/* 119 */     float f1 = 0.016666668F * f;
/* 120 */     GlStateManager.func_179094_E();
/* 121 */     float textSize = 1.0F;
/* 122 */     String name = w.getLocalizedName();
/* 123 */     String distanceText = "";
/* 124 */     boolean showDistance = false;
/* 125 */     float zoomer2 = 1.0F;
/* 126 */     if ((modMain.getSettings()).keepWaypointNames)
/* 127 */       textSize = 1.6F; 
/* 128 */     if (distance > radius) {
/* 129 */       double maxDistance = (Minecraft.func_71410_x()).field_71474_y.field_151451_c * 16.0D;
/*     */       
/* 131 */       if (distance > maxDistance) {
/* 132 */         zoomer2 = (float)(maxDistance / radius);
/* 133 */         float zoomer = (float)(maxDistance / distance);
/* 134 */         offX *= zoomer;
/* 135 */         offY *= zoomer;
/* 136 */         offY += entity.func_70047_e() * (1.0F - zoomer);
/* 137 */         offZ *= zoomer;
/*     */       } else {
/* 139 */         zoomer2 = (float)(distance / radius);
/*     */       } 
/* 141 */     }  if (correctDistance > 20.0D || (modMain.getSettings()).alwaysShowDistance) {
/* 142 */       textSize = 1.6F;
/* 143 */       if ((modMain.getSettings()).distance == 1) {
/* 144 */         float Z = (float)((offZ == 0.0F) ? 0.001D : offZ);
/* 145 */         float angle = (float)Math.toDegrees(Math.atan((-offX / Z)));
/* 146 */         if (offZ < 0.0F)
/* 147 */           if (offX < 0.0F) {
/* 148 */             angle += 180.0F;
/*     */           } else {
/* 150 */             angle -= 180.0F;
/* 151 */           }   float offset = MathHelper.func_76142_g(angle - cameraAngleYaw);
/* 152 */         showDistance = (Math.abs(offset) < (modMain.getSettings()).lookingAtAngle);
/*     */         
/* 154 */         if ((modMain.getSettings()).lookingAtAngleVertical != 180) {
/* 155 */           float cameraAnglePitch = entity.field_70125_A;
/* 156 */           float verticalAngle = (float)Math.toDegrees(Math.asin(-offY / ((distance == 0.0D) ? 1.0E-5D : distance)));
/* 157 */           showDistance = (showDistance && Math.abs(verticalAngle - cameraAnglePitch) < (modMain.getSettings()).lookingAtAngleVertical);
/*     */         } 
/* 159 */       } else if ((modMain.getSettings()).distance == 2) {
/* 160 */         showDistance = true;
/* 161 */       }  if (showDistance) {
/* 162 */         distanceText = GuiMisc.simpleFormat.format(correctDistance) + "m";
/* 163 */         if (!(modMain.getSettings()).keepWaypointNames)
/* 164 */           name = ""; 
/*     */       } else {
/* 166 */         name = "";
/*     */       } 
/* 168 */     }  GlStateManager.func_179109_b(offX, offY, offZ);
/* 169 */     GL11.glNormal3f(0.0F, 1.0F, 0.0F);
/* 170 */     GlStateManager.func_179114_b(-renderManager.field_78735_i, 0.0F, 1.0F, 0.0F);
/* 171 */     GlStateManager.func_179114_b(renderManager.field_78732_j * (((Minecraft.func_71410_x()).field_71474_y.field_74320_O == 2) ? -1 : true), 1.0F, 0.0F, 0.0F);
/* 172 */     GlStateManager.func_179152_a(-f1, -f1, f1);
/* 173 */     GlStateManager.func_179152_a(zoomer2, zoomer2, 1.0F);
/* 174 */     GlStateManager.func_179140_f();
/* 175 */     GlStateManager.func_179132_a(false);
/* 176 */     GlStateManager.func_179097_i();
/* 177 */     GlStateManager.func_179147_l();
/* 178 */     GlStateManager.func_179120_a(770, 771, 1, 0);
/* 179 */     GlStateManager.func_179152_a(2.0F, 2.0F, 2.0F);
/*     */     
/* 181 */     drawIconInWorld(w, modMain.getSettings(), bufferBuilder, tessellator, fontrenderer, name, distanceText, textSize, showDistance);
/*     */     
/* 183 */     GlStateManager.func_179145_e();
/* 184 */     GlStateManager.func_179084_k();
/* 185 */     GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
/* 186 */     GlStateManager.func_179121_F();
/*     */   }
/*     */ 
/*     */   
/*     */   public void drawIconInWorld(Waypoint w, ModSettings settings, BufferBuilder vertexBuffer, Tessellator tessellator, FontRenderer fontrenderer, String name, String distance, float textSize, boolean showDistance) {
/* 191 */     GlStateManager.func_179152_a(settings.waypointsScale, settings.waypointsScale, 1.0F);
/* 192 */     int addedFrame = 0;
/* 193 */     if (w.getType() == 0) {
/* 194 */       int c = ModSettings.COLORS[w.getColor()];
/* 195 */       float l = (c >> 16 & 0xFF) / 255.0F;
/* 196 */       float i1 = (c >> 8 & 0xFF) / 255.0F;
/* 197 */       float j1 = (c & 0xFF) / 255.0F;
/* 198 */       int s = fontrenderer.func_78256_a(w.getSymbol()) / 2;
/*     */       
/* 200 */       GlStateManager.func_179090_x();
/* 201 */       vertexBuffer.func_181668_a(7, DefaultVertexFormats.field_181705_e);
/* 202 */       GlStateManager.func_179131_c(l, i1, j1, 133.3F * settings.waypointOpacityIngame / 100.0F / 255.0F);
/* 203 */       if (s > 4)
/* 204 */         addedFrame = s - 4; 
/* 205 */       vertexBuffer.func_181662_b(-5.0D - addedFrame, -9.0D - addedFrame, 0.0D).func_181675_d();
/* 206 */       vertexBuffer.func_181662_b(-5.0D - addedFrame, addedFrame, 0.0D).func_181675_d();
/* 207 */       vertexBuffer.func_181662_b(4.0D + addedFrame, addedFrame, 0.0D).func_181675_d();
/* 208 */       vertexBuffer.func_181662_b(4.0D + addedFrame, -9.0D - addedFrame, 0.0D).func_181675_d();
/* 209 */       tessellator.func_78381_a();
/* 210 */       GlStateManager.func_179098_w();
/*     */       
/* 212 */       fontrenderer.func_78276_b(w.getSymbol(), -s, -8, -1);
/* 213 */     } else if (w.getType() == 1) {
/* 214 */       Minecraft.func_71410_x().func_110434_K().func_110577_a(InterfaceRenderer.guiTextures);
/* 215 */       float f = 0.00390625F;
/* 216 */       float f1 = 0.00390625F;
/* 217 */       GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 250.0F * settings.waypointOpacityIngame / 100.0F / 255.0F);
/* 218 */       vertexBuffer.func_181668_a(7, DefaultVertexFormats.field_181707_g);
/* 219 */       vertexBuffer.func_181662_b(-5.0D, -9.0D, 0.0D).func_187315_a(0.0D, (78.0F * f1)).func_181675_d();
/* 220 */       vertexBuffer.func_181662_b(-5.0D, 0.0D, 0.0D).func_187315_a(0.0D, (87.0F * f1)).func_181675_d();
/* 221 */       vertexBuffer.func_181662_b(4.0D, 0.0D, 0.0D).func_187315_a((9.0F * f), (87.0F * f1)).func_181675_d();
/* 222 */       vertexBuffer.func_181662_b(4.0D, -9.0D, 0.0D).func_187315_a((9.0F * f), (78.0F * f1)).func_181675_d();
/* 223 */       tessellator.func_78381_a();
/* 224 */       if (!showDistance) {
/* 225 */         name = w.getLocalizedName();
/* 226 */         if (!settings.keepWaypointNames)
/* 227 */           textSize = 1.0F; 
/*     */       } 
/*     */     } 
/* 230 */     if (Minecraft.func_71410_x().func_152349_b())
/* 231 */       textSize *= 1.5F; 
/* 232 */     boolean showingName = (name.length() > 0);
/* 233 */     GlStateManager.func_179109_b(0.0F, (1 + addedFrame), 0.0F);
/* 234 */     GlStateManager.func_179152_a(textSize / 2.0F, textSize / 2.0F, 1.0F);
/*     */     
/* 236 */     if (distance.length() > 0) {
/* 237 */       int t = fontrenderer.func_78256_a(distance) / 2;
/* 238 */       GlStateManager.func_179090_x();
/* 239 */       GlStateManager.func_179131_c(0.0F, 0.0F, 0.0F, 0.27450982F);
/* 240 */       vertexBuffer.func_181668_a(7, DefaultVertexFormats.field_181705_e);
/* 241 */       vertexBuffer.func_181662_b(-t - 1.0D, (showingName ? 10 : false), 0.0D).func_181675_d();
/* 242 */       vertexBuffer.func_181662_b(-t - 1.0D, 9.0D + (showingName ? 10 : false), 0.0D).func_181675_d();
/* 243 */       vertexBuffer.func_181662_b(t, 9.0D + (showingName ? 10 : false), 0.0D).func_181675_d();
/* 244 */       vertexBuffer.func_181662_b(t, (showingName ? 10 : false), 0.0D).func_181675_d();
/* 245 */       tessellator.func_78381_a();
/* 246 */       GlStateManager.func_179098_w();
/* 247 */       fontrenderer.func_78276_b(distance, -t, 1 + (showingName ? 10 : 0), -1);
/*     */     } 
/* 249 */     if (showingName) {
/* 250 */       int t = fontrenderer.func_78256_a(name) / 2;
/* 251 */       fontrenderer.func_78276_b(name, -t, 1, -1);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\ioanm\Downloads\Xaeros_Minimap_20.15.3_Forge_1.12.jar!\xaero\common\minimap\waypoints\render\WaypointsIngameRenderer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */