/*     */ package xaero.common.minimap.waypoints.render;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.Gui;
/*     */ import net.minecraft.client.gui.ScaledResolution;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.resources.I18n;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ import xaero.common.IXaeroMinimap;
/*     */ import xaero.common.api.spigot.ServerWaypointStorage;
/*     */ import xaero.common.interfaces.render.InterfaceRenderer;
/*     */ import xaero.common.minimap.MinimapRadar;
/*     */ import xaero.common.minimap.waypoints.Waypoint;
/*     */ import xaero.common.minimap.waypoints.WaypointSet;
/*     */ import xaero.common.minimap.waypoints.WaypointsManager;
/*     */ import xaero.common.settings.ModSettings;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class WaypointsGuiRenderer
/*     */ {
/*     */   private IXaeroMinimap modMain;
/*     */   private Minecraft mc;
/*     */   private WaypointsManager waypointsManager;
/*     */   
/*     */   public WaypointsGuiRenderer(IXaeroMinimap modMain, Minecraft mc) {
/*  32 */     this.modMain = modMain;
/*  33 */     this.mc = mc;
/*  34 */     this.waypointsManager = modMain.getWaypointsManager();
/*     */   }
/*     */   
/*     */   public void render(double playerX, double playerZ, int specW, int specH, double ps, double pc, float partial, double zoom) {
/*  38 */     boolean divideBy8 = this.waypointsManager.divideBy8(this.waypointsManager.getCurrentContainerID());
/*  39 */     GlStateManager.func_179097_i();
/*  40 */     if ((this.modMain.getSettings()).compassOverWaypoints) {
/*  41 */       drawWaypoints(playerX, playerZ, specW, specH, ps, pc, partial, divideBy8, zoom);
/*  42 */       drawCompass(specW, specH, ps, pc, zoom);
/*     */     } else {
/*  44 */       drawCompass(specW, specH, ps, pc, zoom);
/*  45 */       drawWaypoints(playerX, playerZ, specW, specH, ps, pc, partial, divideBy8, zoom);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void drawWaypoints(double playerX, double playerZ, int specW, int specH, double ps, double pc, float partial, boolean divideBy8, double zoom) {
/*  50 */     if (this.modMain.getSettings().getShowWaypoints() && this.waypointsManager.getWaypoints() != null)
/*  51 */       if ((this.modMain.getSettings()).renderAllSets) {
/*  52 */         HashMap<String, WaypointSet> sets = this.waypointsManager.getCurrentWorld().getSets();
/*  53 */         for (Map.Entry<String, WaypointSet> setEntry : sets.entrySet())
/*  54 */           renderWaypointsList(((WaypointSet)setEntry.getValue()).getList(), playerX, playerZ, specW, specH, ps, pc, divideBy8, zoom); 
/*     */       } else {
/*  56 */         renderWaypointsList(this.waypointsManager.getWaypoints().getList(), playerX, playerZ, specW, specH, ps, pc, divideBy8, zoom);
/*     */       }  
/*  58 */     if (this.modMain.getSettings().getShowWaypoints() && ServerWaypointStorage.working() && this.waypointsManager.getServerWaypoints() != null)
/*  59 */       renderWaypointsList(this.waypointsManager.getServerWaypoints(), playerX, playerZ, specW, specH, ps, pc, divideBy8, zoom); 
/*  60 */     Hashtable<String, Hashtable<Integer, Waypoint>> customWaypoints = WaypointsManager.customWaypoints;
/*  61 */     if (this.modMain.getSettings().getShowWaypoints() && !customWaypoints.isEmpty()) {
/*  62 */       Iterator<Hashtable<Integer, Waypoint>> modIter = customWaypoints.values().iterator();
/*  63 */       while (modIter.hasNext()) {
/*  64 */         Hashtable<Integer, Waypoint> modCustomWaypoints = modIter.next();
/*  65 */         renderWaypointsList(modCustomWaypoints.values(), playerX, playerZ, specW, specH, ps, pc, divideBy8, zoom);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void renderWaypointsList(Collection<Waypoint> list, double playerX, double playerZ, int specW, int specH, double ps, double pc, boolean divideBy8, double zoom) {
/*  77 */     Iterator<Waypoint> iter = list.iterator();
/*  78 */     while (iter.hasNext()) {
/*  79 */       Waypoint w = iter.next();
/*  80 */       if (w == null || w.isDisabled() || (w.getType() == 1 && !this.modMain.getSettings().getDeathpoints()))
/*     */         continue; 
/*  82 */       double offx = w.getX(divideBy8) + 0.5D - playerX;
/*  83 */       double offz = w.getZ(divideBy8) + 0.5D - playerZ;
/*  84 */       double distance = Math.sqrt(offx * offx + offz * offz);
/*  85 */       if (w.getType() == 0 && !w.isGlobal() && (this.modMain.getSettings()).waypointsDistance != 0.0F && distance > (this.modMain.getSettings()).waypointsDistance) {
/*     */         continue;
/*     */       }
/*  88 */       translatePosition(specW, specH, ps, pc, offx, offz, zoom);
/*  89 */       GlStateManager.func_179152_a(2.0F, 2.0F, 1.0F);
/*  90 */       drawIconOnGUI(w, this.modMain.getSettings(), 0, 0);
/*  91 */       GL11.glPopMatrix();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void drawCompass(int specW, int specH, double ps, double pc, double zoom) {
/*  96 */     if (!(this.modMain.getSettings()).compass)
/*     */       return; 
/*  98 */     String[] nesw = { "N", "E", "S", "W" };
/*  99 */     for (int i = 0; i < 4; i++) {
/* 100 */       double offx = (i == 0 || i == 2) ? 0.0D : ((i == 1) ? '✐' : -10000);
/* 101 */       double offy = (i == 1 || i == 3) ? 0.0D : ((i == 2) ? '✐' : -10000);
/* 102 */       translatePosition(specW, specH, ps, pc, offx, offy, zoom);
/* 103 */       GlStateManager.func_179152_a(2.0F, 2.0F, 1.0F);
/* 104 */       GlStateManager.func_179097_i();
/* 105 */       (Minecraft.func_71410_x()).field_71466_p.func_175063_a(nesw[i], (1 - this.mc.field_71466_p
/* 106 */           .func_78256_a(nesw[i]) / 2), -3.0F, MinimapRadar.radarPlayers.hashCode());
/* 107 */       GL11.glPopMatrix();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void translatePosition(int specW, int specH, double ps, double pc, double offx, double offy, double zoom) {
/* 112 */     double Y = (pc * offx + ps * offy) * zoom;
/* 113 */     double X = (ps * offx - pc * offy) * zoom;
/* 114 */     double borderedX = X;
/* 115 */     double borderedY = Y;
/* 116 */     if (borderedX > (specW - 2)) {
/* 117 */       borderedX = (specW - 2);
/* 118 */       borderedY = Y * (specW - 2) / X;
/* 119 */     } else if (borderedX < -specW) {
/* 120 */       borderedX = -specW;
/* 121 */       borderedY = -Y * specW / X;
/*     */     } 
/* 123 */     if (borderedY > (specH - 2)) {
/* 124 */       borderedY = (specH - 2);
/* 125 */       borderedX = X * (specH - 2) / Y;
/* 126 */     } else if (borderedY < -specH) {
/* 127 */       borderedY = -specH;
/* 128 */       borderedX = -X * specH / Y;
/*     */     } 
/* 130 */     GL11.glPushMatrix();
/* 131 */     GlStateManager.func_179137_b(borderedX, borderedY, 0.0D);
/*     */   }
/*     */   
/*     */   public void drawIconOnGUI(Waypoint w, ModSettings settings, int drawX, int drawY) {
/* 135 */     if (w.getType() == 0) {
/* 136 */       int j = (Minecraft.func_71410_x()).field_71466_p.func_78256_a(w.getSymbol()) / 2;
/* 137 */       int addedFrame = (j > 4) ? (j - 4) : 0;
/* 138 */       int rectX1 = drawX - 4 - addedFrame;
/* 139 */       int rectY1 = drawY - 4 - addedFrame;
/* 140 */       int rectX2 = drawX + 5 + addedFrame;
/* 141 */       int rectY2 = drawY + 5 + addedFrame;
/* 142 */       int c = ModSettings.COLORS[w.getColor()];
/* 143 */       int r = c >> 16 & 0xFF;
/* 144 */       int g = c >> 8 & 0xFF;
/* 145 */       int b = c & 0xFF;
/* 146 */       int a = (int)(255.0F * settings.waypointOpacityMap / 100.0F);
/* 147 */       c = a << 24 | r << 16 | g << 8 | b;
/* 148 */       Gui.func_73734_a(rectX1, rectY1, rectX2, rectY2, c);
/* 149 */       GlStateManager.func_179097_i();
/* 150 */       (Minecraft.func_71410_x()).field_71466_p.func_175063_a(w.getSymbol(), (drawX + 1 - j), (drawY - 3), MinimapRadar.radarPlayers
/* 151 */           .hashCode());
/* 152 */     } else if (w.getType() == 1) {
/* 153 */       Minecraft.func_71410_x().func_110434_K().func_110577_a(InterfaceRenderer.guiTextures);
/* 154 */       (Minecraft.func_71410_x()).field_71456_v.func_73729_b(drawX - 4, drawY - 4, 0, 78, 9, 9);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void drawSetChange(ScaledResolution res) {
/* 161 */     if (this.waypointsManager.getWaypoints() != null && this.waypointsManager.setChanged != 0L) {
/* 162 */       int passed = (int)(System.currentTimeMillis() - this.waypointsManager.setChanged);
/* 163 */       if (passed < 1500) {
/* 164 */         int fadeTime = 300;
/* 165 */         boolean fading = (passed > 1500 - fadeTime);
/* 166 */         int alpha = 3 + (int)(252.0F * (fading ? ((1500 - passed) / fadeTime) : 1.0F));
/* 167 */         int c = 0xFFFFFF | alpha << 24;
/* 168 */         GlStateManager.func_179097_i();
/* 169 */         (Minecraft.func_71410_x()).field_71456_v.func_73732_a((Minecraft.func_71410_x()).field_71466_p, I18n.func_135052_a(this.waypointsManager.getWaypoints().getName(), new Object[0]), res.func_78326_a() / 2, res.func_78328_b() / 2 + 50, c);
/*     */       } else {
/* 171 */         this.waypointsManager.setChanged = 0L;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\ioanm\Downloads\Xaeros_Minimap_20.15.3_Forge_1.12.jar!\xaero\common\minimap\waypoints\render\WaypointsGuiRenderer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */