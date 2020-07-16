/*     */ package xaero.common.minimap;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.entity.EntityPlayerSP;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.world.World;
/*     */ import org.lwjgl.opengl.GLContext;
/*     */ import xaero.common.IXaeroMinimap;
/*     */ import xaero.common.anim.OldAnimation;
/*     */ import xaero.common.minimap.render.MinimapFBORenderer;
/*     */ import xaero.common.minimap.render.MinimapSafeModeRenderer;
/*     */ import xaero.common.minimap.write.MinimapWriter;
/*     */ import xaero.common.settings.ModSettings;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MinimapProcessor
/*     */ {
/*     */   public static final boolean DEBUG = false;
/*     */   public static final int FRAME = 9;
/*     */   private IXaeroMinimap modMain;
/*     */   private Throwable crashedWith;
/*     */   public static MinimapProcessor instance;
/*     */   private MinimapWriter minimapWriter;
/*     */   private MinimapFBORenderer minimapFBORenderer;
/*     */   private MinimapSafeModeRenderer minimapSafeModeRenderer;
/*     */   private MinimapRadar entityRadar;
/*     */   private ArrayList<Integer> texturesToDelete;
/*     */   private double minimapZoom;
/*     */   private boolean enlargedMap = false;
/*     */   protected final int[] minimapSizes;
/*     */   protected final int[] bufferSizes;
/*     */   private boolean toResetImage;
/*     */   public final Object mainStuffSync;
/*     */   public World mainWorld;
/*     */   public double mainPlayerX;
/*     */   public double mainPlayerY;
/*     */   public double mainPlayerZ;
/*     */   
/*     */   public MinimapProcessor(IXaeroMinimap modMain, MinimapWriter minimapWriter, MinimapFBORenderer minimapFBORenderer, MinimapSafeModeRenderer minimapSafeModeRenderer, MinimapRadar entityRadar) {
/*  44 */     this.modMain = modMain;
/*  45 */     this.minimapWriter = minimapWriter;
/*  46 */     this.minimapFBORenderer = minimapFBORenderer;
/*  47 */     this.minimapSafeModeRenderer = minimapSafeModeRenderer;
/*  48 */     this.entityRadar = entityRadar;
/*  49 */     this.texturesToDelete = new ArrayList<>();
/*  50 */     this.minimapZoom = 1.0D;
/*  51 */     this.minimapSizes = new int[] { 114, 170, 226, 338 };
/*  52 */     this.bufferSizes = new int[] { 128, 256, 256, 512 };
/*  53 */     instance = this;
/*  54 */     this.toResetImage = true;
/*  55 */     this.mainStuffSync = new Object();
/*     */   }
/*     */   
/*     */   public boolean usingFBO() {
/*  59 */     return (this.minimapFBORenderer.isLoadedFBO() && !(this.modMain.getSettings()).mapSafeMode);
/*     */   }
/*     */   
/*     */   public Throwable getCrashedWith() {
/*  63 */     return this.crashedWith;
/*     */   }
/*     */   
/*     */   public void setCrashedWith(Throwable crashedWith) {
/*  67 */     if (this.crashedWith == null)
/*  68 */       this.crashedWith = crashedWith; 
/*     */   }
/*     */   
/*     */   public void requestTextureDelete(int texture) {
/*  72 */     synchronized (this.texturesToDelete) {
/*  73 */       this.texturesToDelete.add(Integer.valueOf(texture));
/*     */     } 
/*     */   }
/*     */   
/*     */   public double getMinimapZoom() {
/*  78 */     return this.minimapZoom;
/*     */   }
/*     */ 
/*     */   
/*     */   private double getTargetZoom() {
/*  83 */     float settingsZoom = (this.modMain.getSettings()).zooms[(this.modMain.getSettings()).zoom];
/*  84 */     if (this.modMain.getInterfaces().getMinimap().isEnlargedMap() && (this.modMain.getSettings()).zoomedOutEnlarged)
/*  85 */       settingsZoom = 1.0F; 
/*  86 */     float target = settingsZoom * (((this.modMain.getSettings()).caveZoom > 0 && this.minimapWriter.getLoadedCaving() != -1) ? (1 + (this.modMain.getSettings()).caveZoom) : 1.0F);
/*  87 */     if (target > (this.modMain.getSettings()).zooms[(this.modMain.getSettings()).zooms.length - 1])
/*  88 */       target = (this.modMain.getSettings()).zooms[(this.modMain.getSettings()).zooms.length - 1]; 
/*  89 */     return target;
/*     */   }
/*     */   
/*     */   public void instantZoom() {
/*  93 */     this.minimapZoom = getTargetZoom();
/*     */   }
/*     */   
/*     */   public void updateZoom() {
/*  97 */     double target = getTargetZoom();
/*  98 */     double off = target - this.minimapZoom;
/*  99 */     if (off > 0.01D || off < -0.01D) {
/* 100 */       off = (float)OldAnimation.animate(off, 0.8D);
/*     */     } else {
/* 102 */       off = 0.0D;
/*     */     } 
/*     */     
/* 105 */     this.minimapZoom = target - off;
/*     */   }
/*     */   
/*     */   public MinimapWriter getMinimapWriter() {
/* 109 */     return this.minimapWriter;
/*     */   }
/*     */   
/*     */   public MinimapFBORenderer getMinimapFBORenderer() {
/* 113 */     return this.minimapFBORenderer;
/*     */   }
/*     */   
/*     */   public MinimapSafeModeRenderer getMinimapSafeModeRenderer() {
/* 117 */     return this.minimapSafeModeRenderer;
/*     */   }
/*     */   
/*     */   public boolean canUseFrameBuffer() {
/* 121 */     return ((GLContext.getCapabilities()).OpenGL14 && ((GLContext.getCapabilities()).GL_ARB_framebuffer_object || (GLContext.getCapabilities()).GL_EXT_framebuffer_object || (GLContext.getCapabilities()).OpenGL30));
/*     */   }
/*     */   
/*     */   public int getFBOBufferSize() {
/* 125 */     return 512;
/*     */   }
/*     */   
/*     */   public int getMinimapSize() {
/* 129 */     return this.enlargedMap ? 450 : this.minimapSizes[this.modMain.getSettings().getMinimapSize()];
/*     */   }
/*     */   
/*     */   public int getMinimapBufferSize() {
/* 133 */     return this.enlargedMap ? 512 : this.bufferSizes[this.modMain.getSettings().getMinimapSize()];
/*     */   }
/*     */   
/*     */   public void onClientTick() {
/* 137 */     World world = null;
/* 138 */     EntityPlayerSP entityPlayerSP = (Minecraft.func_71410_x()).field_71439_g;
/* 139 */     if (entityPlayerSP != null)
/* 140 */       world = ((EntityPlayer)entityPlayerSP).field_70170_p; 
/* 141 */     if (world != null && entityPlayerSP != null && this.modMain.getSettings().getMinimap())
/* 142 */       this.entityRadar.updateRadar(world, (EntityPlayer)entityPlayerSP, Minecraft.func_71410_x().func_175606_aa()); 
/*     */   }
/*     */   
/*     */   public void onPlayerTick() {
/* 146 */     if (this.modMain.getWaypointsManager().getCurrentContainerID() != null && this.modMain.getWaypointsManager().getCurrentWorldID() != null)
/* 147 */       this.minimapWriter.setSeedForLoading(this.modMain.getSettings().getSlimeChunksSeed()); 
/*     */   }
/*     */   
/*     */   public void onRender(int x, int y, int width, int height, int scale, int size, int boxSize, float partial) {
/*     */     try {
/* 152 */       if (this.minimapFBORenderer.isLoadedFBO() && !canUseFrameBuffer()) {
/* 153 */         this.minimapFBORenderer.setLoadedFBO(false);
/* 154 */         this.minimapFBORenderer.deleteFramebuffers();
/* 155 */         this.toResetImage = true;
/*     */       } 
/* 157 */       if (!getMinimapFBORenderer().isLoadedFBO() && !(this.modMain.getSettings()).mapSafeMode && 
/* 158 */         canUseFrameBuffer()) {
/* 159 */         this.minimapFBORenderer.loadFrameBuffer();
/*     */       }
/* 161 */       if (this.enlargedMap && (this.modMain.getSettings()).centeredEnlarged) {
/* 162 */         x = (width - boxSize) / 2;
/* 163 */         y = (height - boxSize) / 2;
/*     */       } 
/* 165 */       if (usingFBO())
/* 166 */       { this.minimapFBORenderer.renderMinimap(this, x, y, width, height, scale, size, partial); }
/*     */       else
/* 168 */       { this.minimapSafeModeRenderer.renderMinimap(this, x, y, width, height, scale, size, partial); } 
/* 169 */     } catch (Throwable e) {
/* 170 */       setCrashedWith(e);
/* 171 */       checkCrashes();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void checkCrashes() {
/* 176 */     if (this.crashedWith != null)
/* 177 */       throw new RuntimeException("Xaero's Minimap has crashed! Please contact the author at planetminecraft.com/member/xaero96 or minecraftforum.net/members/xaero96", this.crashedWith); 
/*     */   }
/*     */   
/*     */   public static boolean hasMinimapItem(EntityPlayer player) {
/* 181 */     for (int i = 0; i < 9; i++) {
/* 182 */       if (player.field_71071_by.field_70462_a.get(i) != null && ((ItemStack)player.field_71071_by.field_70462_a.get(i)).func_77973_b() == ModSettings.minimapItem)
/* 183 */         return true; 
/* 184 */     }  return false;
/*     */   }
/*     */   
/*     */   public boolean isToResetImage() {
/* 188 */     return this.toResetImage;
/*     */   }
/*     */   
/*     */   public void setToResetImage(boolean toResetImage) {
/* 192 */     this.toResetImage = toResetImage;
/*     */   }
/*     */   
/*     */   public MinimapRadar getEntityRadar() {
/* 196 */     return this.entityRadar;
/*     */   }
/*     */   
/*     */   public boolean isEnlargedMap() {
/* 200 */     return this.enlargedMap;
/*     */   }
/*     */   
/*     */   public void setEnlargedMap(boolean enlargedMap) {
/* 204 */     this.enlargedMap = enlargedMap;
/*     */   }
/*     */   
/*     */   public ArrayList<Integer> getTexturesToDelete() {
/* 208 */     return this.texturesToDelete;
/*     */   }
/*     */   
/*     */   public void setMainValues() {
/* 212 */     synchronized (this.mainStuffSync) {
/* 213 */       Entity player = Minecraft.func_71410_x().func_175606_aa();
/* 214 */       if (player != null) {
/* 215 */         this.mainWorld = player.field_70170_p;
/* 216 */         this.mainPlayerX = player.field_70165_t;
/* 217 */         this.mainPlayerY = player.field_70163_u;
/* 218 */         this.mainPlayerZ = player.field_70161_v;
/*     */       } else {
/* 220 */         this.mainWorld = null;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\ioanm\Downloads\Xaeros_Minimap_20.15.3_Forge_1.12.jar!\xaero\common\minimap\MinimapProcessor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */