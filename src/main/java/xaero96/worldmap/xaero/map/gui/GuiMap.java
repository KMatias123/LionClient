/*      */ package xaero.map.gui;
/*      */ 
/*      */ import java.awt.Color;
/*      */ import java.io.IOException;
/*      */ import java.nio.FloatBuffer;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collections;
/*      */ import java.util.List;
/*      */ import java.util.regex.Pattern;
/*      */ import java.util.regex.PatternSyntaxException;
/*      */ import net.minecraft.client.Minecraft;
/*      */ import net.minecraft.client.gui.GuiButton;
/*      */ import net.minecraft.client.gui.GuiScreen;
/*      */ import net.minecraft.client.gui.ScaledResolution;
/*      */ import net.minecraft.client.renderer.BufferBuilder;
/*      */ import net.minecraft.client.renderer.GLAllocation;
/*      */ import net.minecraft.client.renderer.GlStateManager;
/*      */ import net.minecraft.client.renderer.Tessellator;
/*      */ import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
/*      */ import net.minecraft.client.renderer.vertex.VertexFormat;
/*      */ import net.minecraft.client.renderer.vertex.VertexFormatElement;
/*      */ import net.minecraft.client.settings.GameSettings;
/*      */ import net.minecraft.entity.player.EntityPlayer;
/*      */ import org.lwjgl.input.Mouse;
/*      */ import org.lwjgl.opengl.GL11;
/*      */ import xaero.map.MapProcessor;
/*      */ import xaero.map.Misc;
/*      */ import xaero.map.WorldMap;
/*      */ import xaero.map.animation.Animation;
/*      */ import xaero.map.animation.SinAnimation;
/*      */ import xaero.map.animation.SlowingAnimation;
/*      */ import xaero.map.controls.ControlsHandler;
/*      */ import xaero.map.graphics.ImprovedFramebuffer;
/*      */ import xaero.map.mods.SupportMods;
/*      */ import xaero.map.mods.gui.Waypoint;
/*      */ import xaero.map.region.MapBlock;
/*      */ import xaero.map.region.MapRegion;
/*      */ import xaero.map.region.MapTile;
/*      */ import xaero.map.region.MapTileChunk;
/*      */ import xaero.map.region.Overlay;
/*      */ 
/*      */ 
/*      */ 
/*      */ public class GuiMap
/*      */   extends GuiScreen
/*      */ {
/*   47 */   public static final VertexFormat POSITION_TEX_TEX_TEX = new VertexFormat();
/*   48 */   public static final VertexFormatElement TEX_2F_1 = new VertexFormatElement(1, VertexFormatElement.EnumType.FLOAT, VertexFormatElement.EnumUsage.UV, 2);
/*   49 */   public static final VertexFormatElement TEX_2F_2 = new VertexFormatElement(2, VertexFormatElement.EnumType.FLOAT, VertexFormatElement.EnumUsage.UV, 2);
/*   50 */   public static final VertexFormatElement TEX_2F_3 = new VertexFormatElement(3, VertexFormatElement.EnumType.FLOAT, VertexFormatElement.EnumUsage.UV, 2);
/*      */   static {
/*   52 */     POSITION_TEX_TEX_TEX.func_181721_a(DefaultVertexFormats.field_181713_m)
/*   53 */       .func_181721_a(DefaultVertexFormats.field_181715_o)
/*   54 */       .func_181721_a(TEX_2F_1)
/*   55 */       .func_181721_a(TEX_2F_2)
/*   56 */       .func_181721_a(TEX_2F_3);
/*      */   }
/*   58 */   protected static FloatBuffer brightnessBuffer = GLAllocation.func_74529_h(4);
/*      */   
/*      */   private static final double ZOOM_STEP = 1.2D;
/*      */   public static final int WAYPOINT_MENU_SIZE = 7;
/*   62 */   private static final Color white = new Color(255, 255, 255);
/*   63 */   private static final Color whiteTrans = new Color(255, 255, 255, 40);
/*   64 */   private static final Color redTrans = new Color(255, 0, 0, 40);
/*   65 */   private static final Color black = new Color(0, 0, 0, 255);
/*   66 */   private static int lastAmountOfRegionsViewed = 1;
/*      */   private long loadingAnimationStart;
/*      */   private EntityPlayer player;
/*   69 */   private int screenScale = 0;
/*      */   
/*   71 */   private int mouseDownPosX = -1;
/*   72 */   private int mouseDownPosY = -1;
/*   73 */   private int prevMouseDownPosX = -1;
/*   74 */   private int prevMouseDownPosY = -1;
/*   75 */   private float mouseDownCameraX = -1.0F;
/*   76 */   private float mouseDownCameraZ = -1.0F;
/*   77 */   private long mouseDownStartTimeNano = -1L;
/*   78 */   private long prevMouseDownStartTimeNano = -1L;
/*   79 */   private float cameraX = 0.0F;
/*   80 */   private float cameraZ = 0.0F;
/*   81 */   private int[] cameraDestination = null;
/*   82 */   private SlowingAnimation cameraDestinationAnimX = null;
/*   83 */   private SlowingAnimation cameraDestinationAnimZ = null;
/*      */   private double scale;
/*   85 */   private static double destScale = 3.0D;
/*      */ 
/*      */   
/*   88 */   private Waypoint viewed = null;
/*   89 */   private float waypointHintAlpha = 0.0F;
/*   90 */   private float waypointHintAlphaDest = 0.0F;
/*      */   private Animation zoomAnim;
/*   92 */   private SlowingAnimation waypointHintAlphaAnim = null;
/*      */   private boolean waypointMenu = false;
/*      */   private boolean shouldRenderWaypointMenu = false;
/*   95 */   private int waypointMenuSelected = 0;
/*   96 */   private int waypointMenuOffset = 0;
/*   97 */   private Waypoint waypointMenuSelectedObject = null;
/*   98 */   private Animation waypointMenuAnimation = null;
/*   99 */   private StringBuilder waypointMenuSearch = new StringBuilder();
/*  100 */   private Pattern waypointMenuSearchPattern = null;
/*  101 */   private Pattern waypointMenuSearchStartPattern = null;
/*  102 */   private static ImprovedFramebuffer primaryScaleFBO = null;
/*  103 */   private float[] colourBuffer = new float[4];
/*  104 */   private ArrayList<MapRegion> regionBuffer = new ArrayList<>();
/*      */   
/*      */   private Integer lastViewedDimensionId;
/*      */   
/*      */   private String lastViewedMultiworldId;
/*      */   
/*      */   private int mouseBlockPosX;
/*      */   private int mouseBlockPosY;
/*      */   private int mouseBlockPosZ;
/*      */   private long lastStartTime;
/*      */   private List<GuiDropDown> dropdowns;
/*      */   private GuiDimensionSettings dimensionSettings;
/*      */   private MapMouseButtonPress leftMouseButton;
/*      */   private MapMouseButtonPress rightMouseButton;
/*      */   private boolean buttonClicked;
/*      */   
/*      */   public GuiMap(EntityPlayer player) {
/*  121 */     this.player = player;
/*  122 */     this.cameraX = (float)player.field_70165_t;
/*  123 */     this.cameraZ = (float)player.field_70161_v;
/*  124 */     this.leftMouseButton = new MapMouseButtonPress();
/*  125 */     this.rightMouseButton = new MapMouseButtonPress();
/*  126 */     this.dimensionSettings = new GuiDimensionSettings();
/*  127 */     this.dropdowns = new ArrayList<>();
/*      */     
/*  129 */     this.scale = destScale * 1.5D;
/*  130 */     this.zoomAnim = (Animation)new SlowingAnimation(this.scale, destScale, 0.88D, 0.001D);
/*      */   }
/*      */   
/*      */   public void addGuiButton(GuiButton b) {
/*  134 */     func_189646_b(b);
/*      */   }
/*      */   
/*      */   public void func_73866_w_() {
/*  138 */     super.func_73866_w_();
/*  139 */     this.dropdowns.clear();
/*  140 */     this.dimensionSettings.init(this, this.dropdowns, this.field_146297_k, this.field_146294_l, this.field_146295_m);
/*  141 */     this.loadingAnimationStart = System.currentTimeMillis();
/*  142 */     if (SupportMods.minimap())
/*  143 */       SupportMods.xaeroMinimap.requestWaypointsRefresh(); 
/*  144 */     ScaledResolution scaledresolution = new ScaledResolution(Minecraft.func_71410_x());
/*  145 */     this.screenScale = scaledresolution.func_78325_e();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getTextureSize(double scale) {
/*  154 */     if (scale >= 1.0D) {
/*  155 */       return 16;
/*      */     }
/*  157 */     return 8;
/*      */   }
/*      */   
/*      */   public void func_73863_a(int scaledMouseX, int scaledMouseY, float partialTicks) {
/*  161 */     Minecraft mc = Minecraft.func_71410_x();
/*  162 */     long startTime = System.currentTimeMillis();
/*  163 */     this.dimensionSettings.preMapRender(this, this.dropdowns, mc, this.field_146294_l, this.field_146295_m);
/*  164 */     if (!this.waypointMenu) {
/*  165 */       long passed = (this.lastStartTime == 0L) ? 16L : (startTime - this.lastStartTime);
/*  166 */       double passedScrolls = ((float)passed / 64.0F);
/*  167 */       int direction = ControlsHandler.isDown(ControlsHandler.keyZoomIn) ? 1 : (ControlsHandler.isDown(ControlsHandler.keyZoomOut) ? -1 : 0);
/*  168 */       if (direction != 0)
/*  169 */         changeZoom(direction * passedScrolls); 
/*      */     } 
/*  171 */     this.lastStartTime = startTime;
/*  172 */     if (this.cameraDestination != null) {
/*  173 */       this.cameraDestinationAnimX = new SlowingAnimation(this.cameraX, this.cameraDestination[0], 0.9D, 0.01D);
/*  174 */       this.cameraDestinationAnimZ = new SlowingAnimation(this.cameraZ, this.cameraDestination[1], 0.9D, 0.01D);
/*  175 */       this.cameraDestination = null;
/*      */     } 
/*  177 */     if (this.cameraDestinationAnimX != null) {
/*  178 */       this.cameraX = (float)this.cameraDestinationAnimX.getCurrent();
/*  179 */       if (this.cameraX == this.cameraDestinationAnimX.getDestination())
/*  180 */         this.cameraDestinationAnimX = null; 
/*      */     } 
/*  182 */     if (this.cameraDestinationAnimZ != null) {
/*  183 */       this.cameraZ = (float)this.cameraDestinationAnimZ.getCurrent();
/*  184 */       if (this.cameraZ == this.cameraDestinationAnimZ.getDestination())
/*  185 */         this.cameraDestinationAnimZ = null; 
/*      */     } 
/*  187 */     this.lastViewedDimensionId = null;
/*  188 */     this.lastViewedMultiworldId = null;
/*  189 */     this.mouseBlockPosY = -1;
/*  190 */     synchronized (MapProcessor.instance.renderThreadPauseSync) {
/*  191 */       if (!MapProcessor.instance.isRenderingPaused())
/*  192 */       { if (MapProcessor.instance.getCurrentWorldString() != null && MapProcessor.instance.getMapSaveLoad().isRegionDetectionComplete()) {
/*  193 */           this.lastViewedDimensionId = Integer.valueOf(MapProcessor.instance.getMapWorld().getCurrentDimension().getDimId());
/*  194 */           this.lastViewedMultiworldId = MapProcessor.instance.getMapWorld().getCurrentDimension().getCurrentMultiworld();
/*  195 */           if (SupportMods.minimap()) {
/*  196 */             SupportMods.xaeroMinimap.checkWaypoints(MapProcessor.instance.getMapWorld().isMultiplayer(), this.lastViewedDimensionId.intValue(), this.lastViewedMultiworldId);
/*      */           }
/*  198 */           int mouseXPos = (int)Misc.getMouseX(mc);
/*  199 */           int mouseYPos = (int)Misc.getMouseY(mc);
/*  200 */           if (!this.leftMouseButton.isDown) {
/*  201 */             if (this.mouseDownPosX != -1) {
/*  202 */               double downTime = (System.nanoTime() - this.mouseDownStartTimeNano);
/*  203 */               int draggedX = mouseXPos - this.mouseDownPosX;
/*  204 */               int draggedY = mouseYPos - this.mouseDownPosY;
/*  205 */               if (downTime < 1.6E7D && this.prevMouseDownStartTimeNano != -1L) {
/*      */                 
/*  207 */                 downTime = (System.nanoTime() - this.prevMouseDownStartTimeNano);
/*  208 */                 draggedX = mouseXPos - this.prevMouseDownPosX;
/*  209 */                 draggedY = mouseYPos - this.prevMouseDownPosY;
/*      */               } 
/*  211 */               double frameTime60FPS = 1.6666666666666666E7D;
/*  212 */               double speedScale = downTime / frameTime60FPS;
/*  213 */               double speed_x = -draggedX / this.scale / speedScale;
/*  214 */               double speed_z = -draggedY / this.scale / speedScale;
/*  215 */               this.prevMouseDownPosX = this.mouseDownPosX = -1;
/*  216 */               this.prevMouseDownPosX = this.mouseDownPosY = -1;
/*  217 */               this.prevMouseDownStartTimeNano = -1L;
/*  218 */               double speed = Math.sqrt(speed_x * speed_x + speed_z * speed_z);
/*  219 */               if (speed > 0.0D) {
/*  220 */                 double cos = speed_x / speed;
/*  221 */                 double sin = speed_z / speed;
/*  222 */                 speed = (Math.abs(speed) > 500.0D) ? Math.copySign(500.0D, speed) : speed;
/*      */ 
/*      */ 
/*      */                 
/*  226 */                 double speed_factor = 0.9D;
/*  227 */                 double ln = Math.log(speed_factor);
/*  228 */                 double move_distance = -speed / ln;
/*  229 */                 double moveX = cos * move_distance;
/*  230 */                 double moveZ = sin * move_distance;
/*  231 */                 this.cameraDestinationAnimX = new SlowingAnimation(this.cameraX, this.cameraX + moveX, 0.9D, 0.1D);
/*  232 */                 this.cameraDestinationAnimZ = new SlowingAnimation(this.cameraZ, this.cameraZ + moveZ, 0.9D, 0.1D);
/*      */               } 
/*      */             } 
/*      */           } else {
/*  236 */             if (this.mouseDownPosX != -1) {
/*  237 */               this.cameraX = (float)((this.mouseDownPosX - mouseXPos) / this.scale + this.mouseDownCameraX);
/*  238 */               this.cameraZ = (float)((this.mouseDownPosY - mouseYPos) / this.scale + this.mouseDownCameraZ);
/*      */             } 
/*  240 */             if (this.mouseDownPosX == -1 || System.nanoTime() - this.mouseDownStartTimeNano > 30000000L) {
/*  241 */               this.prevMouseDownPosX = this.mouseDownPosX;
/*  242 */               this.prevMouseDownPosY = this.mouseDownPosY;
/*  243 */               this.prevMouseDownStartTimeNano = this.mouseDownStartTimeNano;
/*  244 */               this.mouseDownPosX = mouseXPos;
/*  245 */               this.mouseDownPosY = mouseYPos;
/*  246 */               this.mouseDownCameraX = this.cameraX;
/*  247 */               this.mouseDownCameraZ = this.cameraZ;
/*  248 */               this.mouseDownStartTimeNano = System.nanoTime();
/*  249 */               this.cameraDestinationAnimX = null;
/*  250 */               this.cameraDestinationAnimZ = null;
/*      */             } 
/*      */           } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  262 */           int mouseFromCentreX = mouseXPos - mc.field_71443_c / 2;
/*  263 */           int mouseFromCentreY = mouseYPos - mc.field_71440_d / 2;
/*  264 */           double oldMousePosX = mouseFromCentreX / this.scale + this.cameraX;
/*  265 */           double oldMousePosZ = mouseFromCentreY / this.scale + this.cameraZ;
/*      */           
/*  267 */           double preScale = this.scale;
/*  268 */           if (destScale != this.scale) {
/*      */             
/*  270 */             if (this.zoomAnim != null)
/*  271 */               this.scale = this.zoomAnim.getCurrent(); 
/*  272 */             if (this.zoomAnim == null || Misc.round(this.zoomAnim.getDestination(), 4) != Misc.round(destScale, 4)) {
/*  273 */               this.zoomAnim = (Animation)new SinAnimation(this.scale, destScale, 100L);
/*      */             }
/*      */           } 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  280 */           if (this.scale > preScale) {
/*      */ 
/*      */             
/*  283 */             this.cameraX = (float)(oldMousePosX - mouseFromCentreX / this.scale);
/*  284 */             this.cameraZ = (float)(oldMousePosZ - mouseFromCentreY / this.scale);
/*      */           } 
/*      */           
/*  287 */           GlStateManager.func_179094_E();
/*  288 */           double mousePosX = mouseFromCentreX / this.scale + this.cameraX;
/*  289 */           double mousePosZ = mouseFromCentreY / this.scale + this.cameraZ;
/*      */           
/*  291 */           GlStateManager.func_179094_E();
/*  292 */           GlStateManager.func_179109_b(0.0F, 0.0F, 500.0F);
/*  293 */           if (WorldMap.settings.displayZoom)
/*  294 */             func_73731_b(mc.field_71466_p, (Math.round(destScale * 100.0D) / 100.0D) + "x", 2, 2, white.hashCode()); 
/*  295 */           this.mouseBlockPosX = (int)Math.floor(mousePosX);
/*  296 */           this.mouseBlockPosZ = (int)Math.floor(mousePosZ);
/*  297 */           int mouseRegX = this.mouseBlockPosX >> 9;
/*  298 */           int mouseRegZ = this.mouseBlockPosZ >> 9;
/*  299 */           MapRegion reg = MapProcessor.instance.getMapRegion(mouseRegX, mouseRegZ, false);
/*  300 */           MapTileChunk chunk = (reg != null) ? reg.getChunk(this.mouseBlockPosX >> 6 & 0x7, this.mouseBlockPosZ >> 6 & 0x7) : null;
/*  301 */           if (WorldMap.settings.debug) {
/*  302 */             if (reg != null) {
/*  303 */               if (chunk != null) {
/*  304 */                 func_73731_b(mc.field_71466_p, chunk.getX() + " " + chunk.getZ(), 5, 15, white.hashCode());
/*  305 */                 func_73731_b(mc.field_71466_p, "loadState: " + chunk.getLoadState(), 5, 25, white.hashCode());
/*  306 */                 func_73731_b(mc.field_71466_p, "shouldUpload: " + chunk.shouldUpload(), 5, 35, white.hashCode());
/*  307 */                 func_73731_b(mc.field_71466_p, String.format("buffers exist: %s, %s", new Object[] { Boolean.valueOf((chunk.getColorBuffer() != null)), Boolean.valueOf((chunk.getLightBuffer() != null)) }), 5, 45, white.hashCode());
/*  308 */                 func_73731_b(mc.field_71466_p, "glColorTexture: " + chunk.getGlColorTexture() + " glLightTexture: " + chunk.getGlLightTexture(), 5, 55, white.hashCode());
/*  309 */                 func_73731_b(mc.field_71466_p, String.format("success mask: %s bhv mask: %d", new Object[] { Byte.valueOf(chunk.getSuccessMask()), Integer.valueOf(chunk.getHeightValueMask() >> 12) }), 5, 65, white.hashCode());
/*  310 */                 func_73731_b(mc.field_71466_p, String.format("changed: %s include: %s", new Object[] { Boolean.valueOf(chunk.wasChanged()), Boolean.valueOf(chunk.includeInSave()) }), 5, 75, white.hashCode());
/*  311 */                 func_73731_b(mc.field_71466_p, "cachePrepared: " + chunk.isCachePrepared(), 5, 85, white.hashCode());
/*  312 */                 MapTile mouseTile = chunk.getTile(this.mouseBlockPosX >> 4 & 0x3, this.mouseBlockPosZ >> 4 & 0x3);
/*  313 */                 if (mouseTile != null) {
/*  314 */                   MapBlock block = mouseTile.getBlock(this.mouseBlockPosX & 0xF, this.mouseBlockPosZ & 0xF);
/*  315 */                   if (block != null) {
/*  316 */                     func_73732_a(mc.field_71466_p, block.toString(), this.field_146294_l / 2, 12, white.hashCode());
/*  317 */                     if (block.getNumberOfOverlays() != 0)
/*  318 */                       for (int m = 0; m < block.getOverlays().size(); m++)
/*  319 */                         func_73732_a(mc.field_71466_p, ((Overlay)block.getOverlays().get(m)).toString(), this.field_146294_l / 2, 22 + m * 10, white.hashCode());  
/*      */                   } 
/*      */                 } 
/*      */               } 
/*  323 */               func_73731_b(mc.field_71466_p, "paused: " + reg.isWritingPaused(), 5, 105, white.hashCode());
/*  324 */               func_73731_b(mc.field_71466_p, String.format("writing: %s refreshing: %s", new Object[] { Boolean.valueOf(reg.isBeingWritten()), Boolean.valueOf(reg.isRefreshing()) }), 5, 115, white.hashCode());
/*  325 */               func_73731_b(mc.field_71466_p, "shouldCache: " + reg.shouldCache(), 5, 125, white.hashCode());
/*  326 */               func_73731_b(mc.field_71466_p, "saveExists: " + reg.getSaveExists(), 5, 135, white.hashCode());
/*  327 */               func_73731_b(mc.field_71466_p, mouseRegX + " " + mouseRegZ, 5, 145, white.hashCode());
/*  328 */               func_73731_b(mc.field_71466_p, String.format("reg loadState: %s version: %d/%d", new Object[] { Byte.valueOf(reg.getLoadState()), Integer.valueOf(reg.getVersion()), Integer.valueOf(MapProcessor.instance.getGlobalVersion()) }), 5, 155, white.hashCode());
/*  329 */               func_73731_b(mc.field_71466_p, "processed: " + MapProcessor.instance.getToProcess().contains(reg), 5, 165, white.hashCode());
/*  330 */               func_73731_b(mc.field_71466_p, String.format("recache: %s reload: %s", new Object[] { Boolean.valueOf(reg.recacheHasBeenRequested()), Boolean.valueOf(reg.reloadHasBeenRequested()) }), 5, 175, white.hashCode());
/*      */             } 
/*  332 */             if (MapProcessor.instance.getMapWorld().isMultiplayer())
/*  333 */               func_73731_b(mc.field_71466_p, "MultiWorld ID: " + MapProcessor.instance.getMapWorld().getCurrentMultiworld(), 5, 185, white.hashCode()); 
/*  334 */             func_73731_b(mc.field_71466_p, String.format("regions: %d processed: %d viewed: %d", new Object[] { Integer.valueOf(MapProcessor.instance.getMapWorld().getCurrentDimension().getMapRegionsList().size()), Integer.valueOf(MapProcessor.instance.getToProcess().size()), Integer.valueOf(lastAmountOfRegionsViewed) }), 5, 195, white.hashCode());
/*  335 */             func_73731_b(mc.field_71466_p, String.format("toLoad: %d toSave: %d tile pool: %d overlays: %d", new Object[] { Integer.valueOf(MapProcessor.instance.getMapSaveLoad().getSizeOfToLoad()), Integer.valueOf(MapProcessor.instance.getMapSaveLoad().getToSave().size()), Integer.valueOf(MapProcessor.instance.getTilePool().size()), Integer.valueOf(MapProcessor.instance.getOverlayManager().getNumberOfUniqueOverlays()) }), 5, 205, white.hashCode());
/*  336 */             long i = Runtime.getRuntime().maxMemory();
/*  337 */             long j = Runtime.getRuntime().totalMemory();
/*  338 */             long k = Runtime.getRuntime().freeMemory();
/*  339 */             long l = j - k;
/*  340 */             func_73731_b(mc.field_71466_p, String.format("FPS: %d", new Object[] { Integer.valueOf(Minecraft.func_175610_ah()) }), 5, 225, white.hashCode());
/*  341 */             func_73731_b(mc.field_71466_p, String.format("Mem: % 2d%% %03d/%03dMB", new Object[] { Long.valueOf(l * 100L / i), Long.valueOf(bytesToMb(l)), Long.valueOf(bytesToMb(i)) }), 5, 235, white.hashCode());
/*  342 */             func_73731_b(mc.field_71466_p, String.format("Allocated: % 2d%% %03dMB", new Object[] { Long.valueOf(j * 100L / i), Long.valueOf(bytesToMb(j)) }), 5, 245, white.hashCode());
/*  343 */             func_73731_b(mc.field_71466_p, String.format("Available VRAM: %dMB", new Object[] { Integer.valueOf(MapProcessor.instance.getMapLimiter().getAvailableVRAM() / 1024) }), 5, 255, white.hashCode());
/*      */           } 
/*  345 */           if (chunk != null) {
/*  346 */             this.mouseBlockPosY = chunk.getHeight(this.mouseBlockPosX & 0x3F, this.mouseBlockPosZ & 0x3F);
/*      */           }
/*      */           
/*  349 */           if (WorldMap.settings.coordinates) {
/*  350 */             String coordsString = "X: " + this.mouseBlockPosX;
/*  351 */             if (this.mouseBlockPosY != -1)
/*  352 */               coordsString = coordsString + " Y: " + this.mouseBlockPosY; 
/*  353 */             coordsString = coordsString + " Z: " + this.mouseBlockPosZ;
/*  354 */             func_73732_a(mc.field_71466_p, coordsString, this.field_146294_l / 2, 2, white.hashCode());
/*      */           } 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  360 */           GlStateManager.func_179121_F();
/*  361 */           int preferredTextureSize = getTextureSize(this.scale);
/*  362 */           if (primaryScaleFBO == null || primaryScaleFBO.field_147621_c != mc.field_71443_c || primaryScaleFBO.field_147618_d != mc.field_71440_d) {
/*  363 */             if (!(Minecraft.func_71410_x()).field_71474_y.field_151448_g) {
/*  364 */               (Minecraft.func_71410_x()).field_71474_y.func_74306_a(GameSettings.Options.FBO_ENABLE, 0);
/*  365 */               System.out.println("FBO is off. Turning it on.");
/*      */             } 
/*  367 */             primaryScaleFBO = new ImprovedFramebuffer(mc.field_71443_c, mc.field_71440_d, false);
/*      */           } 
/*  369 */           double fboScale = 0.5D;
/*  370 */           if (preferredTextureSize > 8)
/*  371 */             fboScale = Math.max(1.0D, Math.floor(this.scale)); 
/*  372 */           double secondaryScale = this.scale / fboScale;
/*  373 */           if (primaryScaleFBO.field_147616_f == -1) {
/*  374 */             GlStateManager.func_179121_F();
/*      */             return;
/*      */           } 
/*  377 */           primaryScaleFBO.func_147610_a(false);
/*  378 */           func_73734_a(0, 0, mc.field_71443_c, mc.field_71440_d, black.hashCode());
/*  379 */           GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
/*  380 */           GlStateManager.func_179152_a(1.0F / this.screenScale, 1.0F / this.screenScale, 1.0F);
/*  381 */           GlStateManager.func_179109_b((mc.field_71443_c / 2), (mc.field_71440_d / 2), 0.0F);
/*  382 */           GlStateManager.func_179094_E();
/*  383 */           GlStateManager.func_179129_p();
/*  384 */           int flooredCameraX = (int)Math.floor(this.cameraX);
/*  385 */           int flooredCameraY = (int)Math.floor(this.cameraZ);
/*  386 */           double secondaryOffsetX = (this.cameraX - flooredCameraX) * fboScale;
/*  387 */           double secondaryOffsetY = (this.cameraZ - flooredCameraY) * fboScale;
/*  388 */           if (fboScale == 0.5D) {
/*  389 */             if ((flooredCameraX & 0x1) != 0) {
/*  390 */               flooredCameraX--;
/*  391 */               secondaryOffsetX += 0.5D;
/*      */             } 
/*  393 */             if ((flooredCameraY & 0x1) != 0) {
/*  394 */               flooredCameraY--;
/*  395 */               secondaryOffsetY += 0.5D;
/*      */             } 
/*      */           } else {
/*      */             
/*  399 */             if (secondaryOffsetX >= 1.0D) {
/*  400 */               int offset = (int)secondaryOffsetX;
/*  401 */               GlStateManager.func_179109_b(-offset, 0.0F, 0.0F);
/*  402 */               secondaryOffsetX -= offset;
/*      */             } 
/*  404 */             if (secondaryOffsetY >= 1.0D) {
/*  405 */               int offset = (int)secondaryOffsetY;
/*  406 */               GlStateManager.func_179109_b(0.0F, offset, 0.0F);
/*  407 */               secondaryOffsetY -= offset;
/*      */             } 
/*      */           } 
/*  410 */           GlStateManager.func_179139_a(fboScale, -fboScale, 1.0D);
/*  411 */           GlStateManager.func_179109_b(-flooredCameraX, -flooredCameraY, 0.0F);
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  416 */           GL11.glEnable(3553);
/*      */ 
/*      */           
/*  419 */           double leftBorder = this.cameraX - (mc.field_71443_c / 2) / this.scale;
/*  420 */           double rightBorder = leftBorder + mc.field_71443_c / this.scale;
/*  421 */           double topBorder = this.cameraZ - (mc.field_71440_d / 2) / this.scale;
/*  422 */           double bottomBorder = topBorder + mc.field_71440_d / this.scale;
/*  423 */           int minRegX = (int)leftBorder >> 9;
/*  424 */           int maxRegX = (int)rightBorder >> 9;
/*  425 */           int minRegZ = (int)topBorder >> 9;
/*  426 */           int maxRegZ = (int)bottomBorder >> 9;
/*  427 */           lastAmountOfRegionsViewed = (maxRegX - minRegX + 1) * (maxRegZ - minRegZ + 1);
/*  428 */           if (MapProcessor.instance.getMapLimiter().getMostRegionsAtATime() < lastAmountOfRegionsViewed)
/*  429 */             MapProcessor.instance.getMapLimiter().setMostRegionsAtATime(lastAmountOfRegionsViewed); 
/*  430 */           GlStateManager.func_179084_k();
/*  431 */           GlStateManager.func_179118_c();
/*  432 */           this.regionBuffer.clear();
/*  433 */           for (int regX = minRegX; regX <= maxRegX; regX++) {
/*  434 */             for (int regZ = minRegZ; regZ <= maxRegZ; regZ++) {
/*  435 */               MapRegion region = MapProcessor.instance.getMapRegion(regX, regZ, MapProcessor.instance.regionExists(regX, regZ));
/*  436 */               if (region != null) {
/*      */                 
/*  438 */                 synchronized (region) {
/*  439 */                   if (!region.recacheHasBeenRequested() && !region.reloadHasBeenRequested() && (region.getVersion() != MapProcessor.instance.getGlobalVersion() || (region.getLoadState() == 4 && region.shouldCache()))) {
/*  440 */                     if (region.isBeingWritten() && region.getLoadState() == 2) {
/*  441 */                       region.requestRefresh();
/*      */                     } else {
/*  443 */                       if (WorldMap.settings.detailed_debug)
/*  444 */                         System.out.println("Added to buffer: " + region + " " + region.getVersion() + " " + MapProcessor.instance.getGlobalVersion()); 
/*  445 */                       this.regionBuffer.add(region);
/*      */                     } 
/*      */                   }
/*      */                 } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */                 
/*  456 */                 if (!MapProcessor.instance.isUploadingPaused()) {
/*  457 */                   List<MapRegion> regions = MapProcessor.instance.getMapWorld().getCurrentDimension().getMapRegionsList();
/*      */                   
/*  459 */                   regions.remove(region);
/*  460 */                   regions.add(region);
/*      */                 } 
/*  462 */                 if (region.getLoadState() < 2) {
/*  463 */                   GlStateManager.func_179094_E();
/*  464 */                   GlStateManager.func_179109_b((512 * region.getRegionX() + 256), (512 * region.getRegionZ() + 256), 0.0F);
/*  465 */                   float loadingAnimationPassed = (float)(System.currentTimeMillis() - this.loadingAnimationStart);
/*  466 */                   if (loadingAnimationPassed > 0.0F) {
/*  467 */                     int period = 2000;
/*  468 */                     int numbersOfActors = 3;
/*  469 */                     float loadingAnimation = loadingAnimationPassed % period / period * 360.0F;
/*  470 */                     float step = 360.0F / numbersOfActors;
/*  471 */                     GlStateManager.func_179114_b(loadingAnimation, 0.0F, 0.0F, 1.0F);
/*  472 */                     int numberOfVisibleActors = 1 + (int)loadingAnimationPassed % 3 * period / period;
/*  473 */                     for (int i = 0; i < numberOfVisibleActors; i++) {
/*  474 */                       GlStateManager.func_179114_b(step, 0.0F, 0.0F, 1.0F);
/*  475 */                       func_73734_a(16, -8, 32, 8, white.hashCode());
/*      */                     } 
/*      */                   } 
/*  478 */                   GlStateManager.func_179121_F();
/*      */                 } 
/*  480 */                 for (int o = 0; o < 8; o++) {
/*  481 */                   for (int p = 0; p < 8; p++) {
/*  482 */                     chunk = region.getChunk(o, p);
/*  483 */                     if (chunk != null && chunk.getGlColorTexture() != -1)
/*      */                     {
/*  485 */                       if (((chunk.getX() + 1) * 64) >= leftBorder && (chunk.getX() * 64) <= rightBorder)
/*      */                       {
/*  487 */                         if ((chunk.getZ() * 64) <= bottomBorder && ((chunk.getZ() + 1) * 64) >= topBorder)
/*      */                         {
/*  489 */                           if (chunk.getGlColorTexture() != -1) {
/*      */                             
/*  491 */                             bindMapTextureWithLighting(chunk, 9728, (preferredTextureSize == 8) ? 1 : 0);
/*      */ 
/*      */                             
/*  494 */                             renderTexturedModalRectWithLighting(64.0F * chunk.getX(), 64.0F * chunk.getZ(), 0, 0, 64.0F, 64.0F);
/*  495 */                             if (WorldMap.settings.debug && p == 0 && chunk.getSuccessMask() != 15)
/*  496 */                             { restoreTextureStates();
/*  497 */                               func_73734_a(64 * chunk.getX(), 64 * chunk.getZ(), 64 * (chunk.getX() + 1), 64 * (chunk.getZ() + 1), redTrans.hashCode()); } 
/*      */                           }  }  }  } 
/*      */                   } 
/*  500 */                 }  restoreTextureStates();
/*  501 */                 if (WorldMap.settings.debug) {
/*  502 */                   GlStateManager.func_179094_E();
/*  503 */                   GlStateManager.func_179109_b((512 * region.getRegionX() + 32), (512 * region.getRegionZ() + 32), 0.0F);
/*  504 */                   GlStateManager.func_179152_a(10.0F, 10.0F, 1.0F);
/*  505 */                   func_73731_b(mc.field_71466_p, "" + region.getLoadState(), 0, 0, white.hashCode());
/*  506 */                   GlStateManager.func_179121_F();
/*      */                 } 
/*      */               } 
/*      */             } 
/*      */           } 
/*  511 */           MapRegion nextToLoad = MapProcessor.instance.getMapSaveLoad().getNextToLoadByViewing();
/*  512 */           boolean shouldRequest = false;
/*  513 */           if (nextToLoad != null) {
/*  514 */             synchronized (nextToLoad) {
/*  515 */               if (!nextToLoad.reloadHasBeenRequested())
/*  516 */                 shouldRequest = true; 
/*      */             } 
/*      */           } else {
/*  519 */             shouldRequest = true;
/*  520 */           }  if (shouldRequest) {
/*  521 */             int toRequest = 1;
/*  522 */             int counter = 0;
/*      */ 
/*      */             
/*  525 */             MapRegion.setComparison(this.mouseBlockPosX >> 9, this.mouseBlockPosZ >> 9);
/*  526 */             Collections.sort(this.regionBuffer);
/*  527 */             for (int i = 0; i < this.regionBuffer.size() && counter < toRequest; i++) {
/*  528 */               MapRegion region = this.regionBuffer.get(i);
/*  529 */               if (region != nextToLoad || this.regionBuffer.size() <= 1)
/*      */               {
/*  531 */                 synchronized (region) {
/*  532 */                   if (region.reloadHasBeenRequested() || region.recacheHasBeenRequested() || (region.getLoadState() != 0 && region.getLoadState() != 4)) {  }
/*      */                   else
/*  534 */                   { MapProcessor.instance.getMapSaveLoad().requestLoad(region, "Gui");
/*  535 */                     if (counter == 0)
/*  536 */                       MapProcessor.instance.getMapSaveLoad().setNextToLoadByViewing(region); 
/*  537 */                     counter++;
/*  538 */                     if (region.getLoadState() == 4)
/*      */                       break;  }
/*      */                 
/*      */                 } 
/*      */               }
/*      */             } 
/*      */           } 
/*  545 */           func_73734_a((this.mouseBlockPosX >> 4) * 16, (this.mouseBlockPosZ >> 4) * 16, ((this.mouseBlockPosX >> 4) + 1) * 16, ((this.mouseBlockPosZ >> 4) + 1) * 16, whiteTrans.hashCode());
/*  546 */           GlStateManager.func_179147_l();
/*  547 */           GlStateManager.func_179141_d();
/*  548 */           GlStateManager.func_179084_k();
/*  549 */           GlStateManager.func_179118_c();
/*  550 */           primaryScaleFBO.func_147609_e();
/*  551 */           Minecraft.func_71410_x().func_147110_a().func_147610_a(false);
/*  552 */           GlStateManager.func_179089_o();
/*  553 */           GlStateManager.func_179121_F();
/*  554 */           GlStateManager.func_179094_E();
/*  555 */           GlStateManager.func_179139_a(secondaryScale, secondaryScale, 1.0D);
/*  556 */           primaryScaleFBO.func_147612_c();
/*  557 */           GL11.glTexParameteri(3553, 10240, 9729);
/*  558 */           GL11.glTexParameteri(3553, 10241, 9729);
/*  559 */           int lineX = -mc.field_71443_c / 2;
/*  560 */           int lineY = mc.field_71440_d / 2 - 5;
/*  561 */           int lineW = mc.field_71443_c;
/*  562 */           int lineH = 6;
/*  563 */           func_73734_a(lineX, lineY, lineX + lineW, lineY + lineH, black.hashCode());
/*  564 */           lineX = mc.field_71443_c / 2 - 5;
/*  565 */           lineY = -mc.field_71440_d / 2;
/*  566 */           lineW = 6;
/*  567 */           lineH = mc.field_71440_d;
/*  568 */           func_73734_a(lineX, lineY, lineX + lineW, lineY + lineH, black.hashCode());
/*  569 */           GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
/*  570 */           renderTexturedModalRect((-mc.field_71443_c / 2) - (float)secondaryOffsetX, (-mc.field_71440_d / 2) - (float)secondaryOffsetY, mc.field_71443_c, mc.field_71440_d);
/*  571 */           GlStateManager.func_179121_F();
/*  572 */           GlStateManager.func_179139_a(this.scale, this.scale, 1.0D);
/*  573 */           GlStateManager.func_179109_b(-this.cameraX, -this.cameraZ, 0.0F);
/*  574 */           GlStateManager.func_179147_l();
/*  575 */           GlStateManager.func_179141_d();
/*  576 */           ArrayList<Waypoint> renderResult = null;
/*  577 */           if (SupportMods.minimap() && WorldMap.settings.waypoints) {
/*  578 */             GlStateManager.func_179129_p();
/*  579 */             Waypoint oldViewed = this.viewed;
/*  580 */             renderResult = SupportMods.xaeroMinimap.renderWaypoints(this, flooredCameraX, flooredCameraY, mc.field_71443_c, mc.field_71440_d, this.screenScale, this.scale, mousePosX, mousePosZ, this.waypointMenuSearchPattern, this.waypointMenuSearchStartPattern);
/*  581 */             boolean waypointMenuReal = (renderResult != null && (renderResult.size() > 1 || this.waypointMenu) && scaledMouseX > this.field_146294_l - 20 && scaledMouseY > this.field_146295_m - 20);
/*  582 */             if (this.waypointMenu != waypointMenuReal) {
/*  583 */               this.waypointMenu = waypointMenuReal;
/*  584 */               int animationStart = (this.waypointMenuAnimation != null) ? (int)this.waypointMenuAnimation.getCurrent() : (this.waypointMenu ? 500 : 0);
/*  585 */               int animationEnd = this.waypointMenu ? 0 : 500;
/*  586 */               if (this.waypointMenu) {
/*  587 */                 this.waypointMenuAnimation = (Animation)new SlowingAnimation(animationStart, animationEnd, 0.7D, 0.001D);
/*  588 */                 this.shouldRenderWaypointMenu = true;
/*      */               } else {
/*  590 */                 this.waypointMenuAnimation = new Animation(animationStart, animationEnd, 500L);
/*      */               } 
/*  592 */             }  if (!this.shouldRenderWaypointMenu) {
/*  593 */               this.waypointMenuSelected = 0;
/*  594 */               this.waypointMenuOffset = 0;
/*      */             } 
/*  596 */             this.viewed = (renderResult != null) ? renderResult.remove(0) : null;
/*  597 */             if (this.viewed != oldViewed) {
/*  598 */               this.waypointHintAlphaDest = (this.viewed != null) ? 255.0F : 0.0F;
/*  599 */               this.waypointHintAlphaAnim = new SlowingAnimation(this.waypointHintAlpha, this.waypointHintAlphaDest, (this.waypointHintAlpha < this.waypointHintAlphaDest) ? 0.9D : 0.95D, 1.0D);
/*      */             } 
/*  601 */             if (this.waypointHintAlphaAnim != null)
/*  602 */               this.waypointHintAlpha = (float)this.waypointHintAlphaAnim.getCurrent(); 
/*  603 */             GlStateManager.func_179089_o();
/*  604 */             GlStateManager.func_179147_l();
/*      */           } else {
/*  606 */             this.viewed = null;
/*  607 */           }  if (WorldMap.settings.footsteps)
/*  608 */             for (int i = 0; i < MapProcessor.instance.getFootprints().size(); i++) {
/*  609 */               Double[] coords = MapProcessor.instance.getFootprints().get(i);
/*  610 */               setColourBuffer(1.0F, 0.1F, 0.1F, 1.0F);
/*  611 */               drawDotOnMap(coords[0].doubleValue(), coords[1].doubleValue(), 0.0F, 1.0D / this.scale);
/*      */             }  
/*  613 */           if (WorldMap.settings.renderArrow) {
/*  614 */             boolean toTheLeft = (this.player.field_70165_t < leftBorder);
/*  615 */             boolean toTheRight = (this.player.field_70165_t > rightBorder);
/*  616 */             boolean down = (this.player.field_70161_v > bottomBorder);
/*  617 */             boolean up = (this.player.field_70161_v < topBorder);
/*  618 */             GlStateManager.func_179147_l();
/*  619 */             if (toTheLeft || toTheRight || up || down) {
/*  620 */               double arrowX = this.player.field_70165_t;
/*  621 */               double arrowZ = this.player.field_70161_v;
/*  622 */               float a = 0.0F;
/*  623 */               if (toTheLeft) {
/*  624 */                 a = up ? 1.5F : (down ? 0.5F : 1.0F);
/*  625 */                 arrowX = leftBorder;
/*  626 */               } else if (toTheRight) {
/*  627 */                 a = up ? 2.5F : (down ? 3.5F : 3.0F);
/*  628 */                 arrowX = rightBorder;
/*      */               } 
/*  630 */               if (down) {
/*  631 */                 arrowZ = bottomBorder;
/*  632 */               } else if (up) {
/*  633 */                 if (a == 0.0F)
/*  634 */                   a = 2.0F; 
/*  635 */                 arrowZ = topBorder;
/*      */               } 
/*  637 */               setColourBuffer(0.0F, 0.0F, 0.0F, 0.9F);
/*  638 */               drawFarArrowOnMap(arrowX, arrowZ + 2.0D / this.scale, a, 1.0D / this.scale);
/*  639 */               setColourBuffer(0.8F, 0.1F, 0.1F, 1.0F);
/*  640 */               drawFarArrowOnMap(arrowX, arrowZ, a, 1.0D / this.scale);
/*      */             } else {
/*  642 */               setColourBuffer(0.0F, 0.0F, 0.0F, 0.9F);
/*  643 */               drawArrowOnMap(this.player.field_70165_t, this.player.field_70161_v + 2.0D / this.scale, this.player.field_70177_z, 1.0D / this.scale);
/*  644 */               setColourBuffer(0.8F, 0.1F, 0.1F, 1.0F);
/*  645 */               drawArrowOnMap(this.player.field_70165_t, this.player.field_70161_v, this.player.field_70177_z, 1.0D / this.scale);
/*      */             } 
/*      */           } 
/*      */ 
/*      */           
/*  650 */           GlStateManager.func_179121_F();
/*      */           
/*  652 */           if (this.shouldRenderWaypointMenu && renderResult != null) {
/*  653 */             if (this.waypointMenuSelected >= renderResult.size()) {
/*  654 */               this.waypointMenuSelected = renderResult.size() - 1;
/*  655 */               this.waypointMenuOffset = Math.max(this.waypointMenuSelected - 7 + 2, 0);
/*      */             } 
/*  657 */             if (this.waypointMenuSelected < 0)
/*  658 */               this.waypointMenuSelected = 0; 
/*  659 */             if (renderResult.size() > 0) {
/*  660 */               this.waypointMenuSelectedObject = renderResult.get(this.waypointMenuSelected);
/*      */             } else {
/*  662 */               this.waypointMenuSelectedObject = null;
/*  663 */             }  SupportMods.xaeroMinimap.renderSideWaypoints(this, renderResult, this.scale, this.field_146294_l + (int)this.waypointMenuAnimation.getCurrent(), this.field_146295_m, this.waypointMenuOffset, this.waypointMenuSelected);
/*  664 */             if (this.waypointMenu && this.waypointMenuSearch != null && this.waypointMenuSearch.length() > 0)
/*  665 */               func_73732_a(mc.field_71466_p, this.waypointMenuSearch.toString(), this.field_146294_l / 2, this.field_146295_m - 15, Waypoint.white); 
/*  666 */             if (!this.waypointMenu && this.waypointMenuAnimation.getCurrent() == this.waypointMenuAnimation.getDestination()) {
/*  667 */               this.shouldRenderWaypointMenu = false;
/*  668 */               this.waypointMenuSearch = new StringBuilder();
/*  669 */               this.waypointMenuSearchPattern = null;
/*  670 */               this.waypointMenuSearchStartPattern = null;
/*      */             } 
/*      */           } 
/*  673 */           this.dimensionSettings.renderText(mc, scaledMouseX, scaledMouseY, this.field_146294_l, this.field_146295_m);
/*      */           
/*  675 */           if ((int)this.waypointHintAlpha > 3) {
/*  676 */             int hintColour = (int)this.waypointHintAlpha << 24 | 0xFF0000 | 0xFF00 | 0xFF;
/*  677 */             GlStateManager.func_179147_l();
/*  678 */             GlStateManager.func_179141_d();
/*  679 */             if (SupportMods.xaeroMinimap.canTeleport()) {
/*  680 */               func_73732_a(mc.field_71466_p, "Edit - Right Click     Teleport - T     Disable - H     Delete - Delete", this.field_146294_l / 2, this.field_146295_m - 25, hintColour);
/*      */             } else {
/*  682 */               func_73732_a(mc.field_71466_p, "Edit - Right Click     Disable - H     Delete - Delete", this.field_146294_l / 2, this.field_146295_m - 25, hintColour);
/*      */             } 
/*      */           } 
/*  685 */           GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
/*      */         } else {
/*      */           
/*  688 */           renderLoadingScreen();
/*      */         }  }
/*  690 */       else { renderLoadingScreen(); }
/*  691 */        mc.func_110434_K().func_110577_a(WorldMap.guiTextures);
/*  692 */       func_73729_b(this.field_146294_l - 35, 2, 0, 37, 32, 32);
/*      */     } 
/*  694 */     GlStateManager.func_179126_j();
/*      */     
/*  696 */     super.func_73863_a(scaledMouseX, scaledMouseY, partialTicks);
/*  697 */     for (GuiDropDown d : this.dropdowns) {
/*  698 */       if (d.isClosed())
/*  699 */         d.drawButton(scaledMouseX, scaledMouseY, this.field_146295_m); 
/*      */     } 
/*  701 */     for (GuiDropDown d : this.dropdowns) {
/*  702 */       if (!d.isClosed()) {
/*  703 */         d.drawButton(scaledMouseX, scaledMouseY, this.field_146295_m);
/*      */       }
/*      */     } 
/*  706 */     this.dimensionSettings.postMapRender(mc, scaledMouseX, scaledMouseY, this.field_146294_l, this.field_146295_m);
/*      */   }
/*      */   
/*      */   private void renderLoadingScreen() {
/*  710 */     func_73734_a(0, 0, this.field_146297_k.field_71443_c, this.field_146297_k.field_71440_d, black.hashCode());
/*  711 */     GlStateManager.func_179094_E();
/*  712 */     GlStateManager.func_179109_b(0.0F, 0.0F, 500.0F);
/*  713 */     ScaledResolution scaledResolution = new ScaledResolution(this.field_146297_k);
/*  714 */     func_73732_a(this.field_146297_k.field_71466_p, "Preparing World Map...", scaledResolution.func_78326_a() / 2, scaledResolution.func_78328_b() / 2, white.hashCode());
/*  715 */     GlStateManager.func_179121_F();
/*      */   }
/*      */   
/*      */   public void drawDotOnMap(double x, double z, float angle, double sc) {
/*  719 */     drawObjectOnMap(x, z, angle, sc, 2.5F, 2.5F, 0, 69, 5, 5, 9729);
/*      */   }
/*      */   
/*      */   public void drawArrowOnMap(double x, double z, float angle, double sc) {
/*  723 */     drawObjectOnMap(x, z, angle, sc, 13.0F, 5.0F, 0, 0, 28, 29, 9729);
/*      */   }
/*      */   
/*      */   public void drawFarArrowOnMap(double x, double z, float angle, double sc) {
/*  727 */     drawObjectOnMap(x, z, angle * 90.0F, sc, 27.0F, 13.0F, 26, 0, 54, 13, 9729);
/*      */   }
/*      */   
/*      */   public void drawObjectOnMap(double x, double z, float angle, double sc, float offX, float offY, int textureX, int textureY, int w, int h, int filter) {
/*  731 */     GlStateManager.func_179094_E();
/*  732 */     GlStateManager.func_179131_c(this.colourBuffer[0], this.colourBuffer[1], this.colourBuffer[2], this.colourBuffer[3]);
/*  733 */     GlStateManager.func_179137_b(x, z, 0.0D);
/*  734 */     GlStateManager.func_179139_a(sc, sc, 1.0D);
/*  735 */     if (angle != 0.0F)
/*  736 */       GlStateManager.func_179114_b(angle, 0.0F, 0.0F, 1.0F); 
/*  737 */     this.field_146297_k.func_110434_K().func_110577_a(WorldMap.guiTextures);
/*  738 */     GL11.glTexParameteri(3553, 10240, filter);
/*  739 */     GL11.glTexParameteri(3553, 10241, filter);
/*  740 */     func_175174_a(-offX, -offY, textureX, textureY, w, h);
/*  741 */     if (filter != 9728) {
/*  742 */       GL11.glTexParameteri(3553, 10240, 9728);
/*  743 */       GL11.glTexParameteri(3553, 10241, 9728);
/*      */     } 
/*  745 */     GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
/*  746 */     GlStateManager.func_179121_F();
/*      */   }
/*      */   
/*      */   public static void renderTexturedModalRectWithLighting(float x, float y, int textureX, int textureY, float width, float height) {
/*  750 */     Tessellator tessellator = Tessellator.func_178181_a();
/*  751 */     BufferBuilder vertexBuffer = tessellator.func_178180_c();
/*  752 */     vertexBuffer.func_181668_a(7, POSITION_TEX_TEX_TEX);
/*  753 */     vertexBuffer.func_181662_b((x + 0.0F), (y + height), 0.0D)
/*  754 */       .func_187315_a(0.0D, 1.0D).func_187315_a(0.0D, 1.0D).func_187315_a(0.0D, 1.0D).func_187315_a(0.0D, 1.0D)
/*  755 */       .func_181675_d();
/*  756 */     vertexBuffer.func_181662_b((x + width), (y + height), 0.0D)
/*  757 */       .func_187315_a(1.0D, 1.0D).func_187315_a(1.0D, 1.0D).func_187315_a(1.0D, 1.0D).func_187315_a(1.0D, 1.0D)
/*  758 */       .func_181675_d();
/*  759 */     vertexBuffer.func_181662_b((x + width), (y + 0.0F), 0.0D)
/*  760 */       .func_187315_a(1.0D, 0.0D).func_187315_a(1.0D, 0.0D).func_187315_a(1.0D, 0.0D).func_187315_a(1.0D, 0.0D)
/*  761 */       .func_181675_d();
/*  762 */     vertexBuffer.func_181662_b((x + 0.0F), (y + 0.0F), 0.0D)
/*  763 */       .func_187315_a(0.0D, 0.0D).func_187315_a(0.0D, 0.0D).func_187315_a(0.0D, 0.0D).func_187315_a(0.0D, 0.0D)
/*  764 */       .func_181675_d();
/*  765 */     tessellator.func_78381_a();
/*      */   }
/*      */   
/*      */   public static void renderTexturedModalRect(float x, float y, float width, float height) {
/*  769 */     Tessellator tessellator = Tessellator.func_178181_a();
/*  770 */     BufferBuilder vertexBuffer = tessellator.func_178180_c();
/*  771 */     vertexBuffer.func_181668_a(7, DefaultVertexFormats.field_181707_g);
/*  772 */     vertexBuffer.func_181662_b((x + 0.0F), (y + height), 0.0D)
/*  773 */       .func_187315_a(0.0D, 1.0D)
/*  774 */       .func_181675_d();
/*  775 */     vertexBuffer.func_181662_b((x + width), (y + height), 0.0D)
/*  776 */       .func_187315_a(1.0D, 1.0D)
/*  777 */       .func_181675_d();
/*  778 */     vertexBuffer.func_181662_b((x + width), (y + 0.0F), 0.0D)
/*  779 */       .func_187315_a(1.0D, 0.0D)
/*  780 */       .func_181675_d();
/*  781 */     vertexBuffer.func_181662_b((x + 0.0F), (y + 0.0F), 0.0D)
/*  782 */       .func_187315_a(0.0D, 0.0D)
/*  783 */       .func_181675_d();
/*  784 */     tessellator.func_78381_a();
/*      */   }
/*      */   
/*      */   public void mapClicked(int button, int x, int y) {
/*  788 */     if (!this.waypointMenu && this.viewed != null) {
/*  789 */       SupportMods.xaeroMinimap.openWaypoint(this, this.viewed);
/*  790 */       this.mouseDownPosX = -1;
/*  791 */       this.mouseDownPosY = -1;
/*      */     } 
/*  793 */     if (this.waypointMenu && this.waypointMenuSelectedObject != null)
/*  794 */       this.cameraDestination = new int[] { this.waypointMenuSelectedObject.getX(), this.waypointMenuSelectedObject.getZ() }; 
/*      */   }
/*      */   
/*      */   private void setSearch(String search, String preChange) {
/*      */     try {
/*  799 */       this.waypointMenuSearchPattern = Pattern.compile(search.toLowerCase());
/*  800 */       if (search.length() > 0)
/*  801 */       { if (search.charAt(0) == '^') {
/*  802 */           this.waypointMenuSearchStartPattern = this.waypointMenuSearchPattern;
/*      */         } else {
/*  804 */           this.waypointMenuSearchStartPattern = Pattern.compile('^' + search.toString().toLowerCase());
/*      */         }  }
/*  806 */       else { this.waypointMenuSearchPattern = null;
/*  807 */         this.waypointMenuSearchStartPattern = null; }
/*      */     
/*  809 */     } catch (PatternSyntaxException e) {
/*  810 */       if (preChange != null) {
/*  811 */         this.waypointMenuSearch = new StringBuilder(preChange);
/*  812 */         setSearch(preChange, (String)null);
/*      */       } else {
/*  814 */         throw e;
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   protected void func_73869_a(char par1, int par2) throws IOException {
/*  820 */     super.func_73869_a(par1, par2);
/*  821 */     if (this.waypointMenu) {
/*  822 */       if (par2 == ControlsHandler.keyZoomIn.func_151463_i()) {
/*  823 */         scrollWaypoints(1);
/*  824 */       } else if (par2 == ControlsHandler.keyZoomOut.func_151463_i()) {
/*  825 */         scrollWaypoints(-1);
/*      */       }
/*  827 */       else if (par1 != '\000') {
/*  828 */         String preChange = this.waypointMenuSearch.toString();
/*  829 */         if (par1 == '\b') {
/*  830 */           if (this.waypointMenuSearch.length() > 0)
/*  831 */             this.waypointMenuSearch.deleteCharAt(this.waypointMenuSearch.length() - 1); 
/*  832 */         } else if (par1 == '\r') {
/*  833 */           this.waypointMenuSearch = new StringBuilder();
/*      */         } else {
/*  835 */           this.waypointMenuSearch.append(par1);
/*      */         } 
/*  837 */         setSearch(this.waypointMenuSearch.toString(), preChange);
/*      */       } 
/*      */     } else {
/*      */       
/*  841 */       if (par2 == ControlsHandler.keyOpenMap.func_151463_i())
/*  842 */         super.func_73869_a(par1, 1); 
/*  843 */       if (par1 == '\r' && this.dimensionSettings.active)
/*  844 */         this.dimensionSettings.confirm(this, this.field_146297_k, this.field_146294_l, this.field_146295_m); 
/*  845 */       if (SupportMods.minimap() && this.lastViewedDimensionId != null) {
/*  846 */         if (this.viewed != null) {
/*  847 */           switch (par2) {
/*      */             case 20:
/*  849 */               SupportMods.xaeroMinimap.teleportToWaypoint(this, this.viewed);
/*      */               break;
/*      */             case 35:
/*  852 */               SupportMods.xaeroMinimap.disableWaypoint(this.viewed);
/*      */               break;
/*      */             case 211:
/*  855 */               SupportMods.xaeroMinimap.deleteWaypoint(this.viewed);
/*      */               break;
/*      */           } 
/*      */         }
/*  859 */         if (par2 == SupportMods.xaeroMinimap.getWaypointKeyCode() && WorldMap.settings.waypoints && 
/*  860 */           this.mouseBlockPosY != -1) {
/*  861 */           SupportMods.xaeroMinimap.createWaypoint(this, this.mouseBlockPosX, this.mouseBlockPosY + 1, this.mouseBlockPosZ);
/*      */         }
/*  863 */         if (par2 == SupportMods.xaeroMinimap.getTempWaypointKeyCode() && WorldMap.settings.waypoints && 
/*  864 */           this.mouseBlockPosY != -1) {
/*  865 */           SupportMods.xaeroMinimap.createTempWaypoint(this.mouseBlockPosX, this.mouseBlockPosY + 1, this.mouseBlockPosZ);
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void func_73864_a(int par1, int par2, int par3) throws IOException {
/*  873 */     this.buttonClicked = false;
/*  874 */     for (GuiDropDown d : this.dropdowns) {
/*  875 */       if (!d.isClosed() && d.onDropDown(par1, par2, this.field_146295_m)) {
/*  876 */         d.mouseClicked(par1, par2, par3, this.field_146295_m);
/*      */         return;
/*      */       } 
/*  879 */       d.setClosed(true);
/*      */     } 
/*  881 */     for (GuiDropDown d : this.dropdowns) {
/*  882 */       if (d.onDropDown(par1, par2, this.field_146295_m)) {
/*  883 */         d.mouseClicked(par1, par2, par3, this.field_146295_m);
/*      */         return;
/*      */       } 
/*  886 */       d.setClosed(true);
/*      */     } 
/*  888 */     super.func_73864_a(par1, par2, par3);
/*  889 */     if (!this.buttonClicked) {
/*  890 */       if (par3 == 0) {
/*  891 */         this.leftMouseButton.isDown = true;
/*  892 */         this.leftMouseButton.pressedAtX = Mouse.getX();
/*  893 */         this.leftMouseButton.pressedAtY = Mouse.getY();
/*  894 */       } else if (par3 == 1) {
/*  895 */         this.rightMouseButton.isDown = true;
/*  896 */         this.rightMouseButton.pressedAtX = Mouse.getX();
/*  897 */         this.rightMouseButton.pressedAtY = Mouse.getY();
/*  898 */       } else if (par3 == ControlsHandler.keyOpenMap.func_151463_i() + 100) {
/*  899 */         super.func_73869_a(' ', 1);
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   public void func_146286_b(int par1, int par2, int par3) {
/*  905 */     for (GuiDropDown d : this.dropdowns)
/*  906 */       d.mouseReleased(par1, par2, par3, this.field_146295_m); 
/*  907 */     int mouseX = Mouse.getX();
/*  908 */     int mouseY = Mouse.getY();
/*  909 */     if (this.leftMouseButton.isDown && par3 == 0) {
/*  910 */       this.leftMouseButton.isDown = false;
/*  911 */       if (Math.abs(this.leftMouseButton.pressedAtX - mouseX) < 5 && Math.abs(this.leftMouseButton.pressedAtY - mouseY) < 5)
/*  912 */         mapClicked(0, this.leftMouseButton.pressedAtX, this.leftMouseButton.pressedAtY); 
/*  913 */       this.leftMouseButton.pressedAtX = -1;
/*  914 */       this.leftMouseButton.pressedAtY = -1;
/*      */     } 
/*  916 */     if (this.rightMouseButton.isDown && par3 == 1) {
/*  917 */       this.rightMouseButton.isDown = false;
/*  918 */       if (Math.abs(this.rightMouseButton.pressedAtX - mouseX) < 5 && Math.abs(this.rightMouseButton.pressedAtY - mouseY) < 5)
/*  919 */         mapClicked(1, this.rightMouseButton.pressedAtX, this.rightMouseButton.pressedAtY); 
/*  920 */       this.rightMouseButton.pressedAtX = -1;
/*  921 */       this.rightMouseButton.pressedAtY = -1;
/*      */     } 
/*  923 */     super.func_146286_b(par1, par2, par3);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void func_146284_a(GuiButton button) throws IOException {
/*  929 */     this.dimensionSettings.actionPerformed(this, this.field_146297_k, this.field_146294_l, this.field_146295_m, button);
/*  930 */     this.buttonClicked = true;
/*      */   }
/*      */   
/*      */   public void func_146274_d() throws IOException {
/*  934 */     super.func_146274_d();
/*  935 */     int wheel = Mouse.getEventDWheel() / 120;
/*  936 */     if (wheel != 0) {
/*  937 */       if (wheel != 0) {
/*  938 */         ScaledResolution scaledresolution = new ScaledResolution(Minecraft.func_71410_x());
/*  939 */         int mouseXScaled = (int)(Misc.getMouseX(Minecraft.func_71410_x()) / scaledresolution.func_78325_e());
/*  940 */         int mouseYScaled = (int)(Misc.getMouseY(Minecraft.func_71410_x()) / scaledresolution.func_78325_e());
/*  941 */         for (GuiDropDown d : this.dropdowns) {
/*  942 */           if (!d.isClosed() && d.onDropDown(mouseXScaled, mouseYScaled, this.field_146295_m)) {
/*  943 */             d.mouseScrolled(wheel, mouseXScaled, mouseYScaled, this.field_146295_m);
/*      */             return;
/*      */           } 
/*      */         } 
/*      */       } 
/*  948 */       int direction = (wheel > 0) ? 1 : -1;
/*  949 */       if (this.waypointMenu) {
/*  950 */         scrollWaypoints(direction);
/*      */       } else {
/*  952 */         changeZoom(wheel);
/*      */       } 
/*      */     } 
/*      */   }
/*      */   private void scrollWaypoints(int direction) {
/*  957 */     this.waypointMenuSelected += direction;
/*  958 */     if (this.waypointMenuSelected < 0)
/*  959 */       this.waypointMenuSelected = 0; 
/*  960 */     if (this.waypointMenuSelected == this.waypointMenuOffset + 7 - 1)
/*  961 */       this.waypointMenuOffset++; 
/*  962 */     if (this.waypointMenuOffset > 0 && this.waypointMenuSelected == this.waypointMenuOffset)
/*  963 */       this.waypointMenuOffset--; 
/*      */   }
/*      */   
/*      */   private void changeZoom(double factor) {
/*  967 */     this.cameraDestinationAnimX = null;
/*  968 */     this.cameraDestinationAnimZ = null;
/*  969 */     destScale *= Math.pow(1.2D, factor);
/*  970 */     if (destScale < 0.5D) {
/*  971 */       destScale = 0.5D;
/*  972 */     } else if (destScale > 5.0D) {
/*  973 */       destScale = 5.0D;
/*      */     } 
/*      */   }
/*      */   
/*      */   public void func_146281_b() {
/*  978 */     super.func_146281_b();
/*  979 */     this.leftMouseButton.isDown = false;
/*  980 */     this.rightMouseButton.isDown = false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void bindMapTextureWithLighting(MapTileChunk chunk, int magFilter, int lod) {
/*  995 */     int glLightTexture = chunk.getGlLightTexture();
/*  996 */     float brightness = MapProcessor.instance.getBrightness();
/*      */     
/*  998 */     GlStateManager.func_179138_g(33984);
/*  999 */     GlStateManager.func_179098_w();
/* 1000 */     if (glLightTexture != -1) {
/* 1001 */       GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
/* 1002 */       chunk.bindLightTexture(false, magFilter);
/*      */       
/* 1004 */       float toSubtract = 1.0F - brightness;
/* 1005 */       brightnessBuffer.position(0);
/* 1006 */       brightnessBuffer.put(toSubtract);
/* 1007 */       brightnessBuffer.put(toSubtract);
/* 1008 */       brightnessBuffer.put(toSubtract);
/* 1009 */       brightnessBuffer.put(1.0F);
/* 1010 */       brightnessBuffer.flip();
/* 1011 */       GlStateManager.func_187448_b(8960, 8705, brightnessBuffer);
/* 1012 */       GlStateManager.func_187399_a(8960, 8704, 34160);
/* 1013 */       GlStateManager.func_187399_a(8960, 34161, 34023);
/* 1014 */       GlStateManager.func_187399_a(8960, 34176, 5890);
/* 1015 */       GlStateManager.func_187399_a(8960, 34192, 769);
/* 1016 */       GlStateManager.func_187399_a(8960, 34177, 34166);
/* 1017 */       GlStateManager.func_187399_a(8960, 34193, 768);
/* 1018 */       GlStateManager.func_187399_a(8960, 34162, 7681);
/* 1019 */       GlStateManager.func_187399_a(8960, 34184, 34166);
/* 1020 */       GlStateManager.func_187399_a(8960, 34200, 770);
/*      */     } else {
/* 1022 */       GlStateManager.func_179131_c(brightness, brightness, brightness, 1.0F);
/* 1023 */       chunk.bindColorTexture(false, magFilter);
/*      */       
/* 1025 */       GlStateManager.func_187399_a(8960, 8704, 8448);
/*      */     } 
/* 1027 */     GlStateManager.func_179138_g(33985);
/* 1028 */     GlStateManager.func_179090_x();
/* 1029 */     GlStateManager.func_179138_g(33986);
/* 1030 */     if (glLightTexture != -1) {
/* 1031 */       GlStateManager.func_179098_w();
/* 1032 */       chunk.bindLightTexture(false, magFilter);
/* 1033 */       GlStateManager.func_187399_a(8960, 8704, 260);
/*      */     } else {
/* 1035 */       GlStateManager.func_179090_x();
/* 1036 */     }  GlStateManager.func_179138_g(33987);
/* 1037 */     if (glLightTexture != -1) {
/* 1038 */       GlStateManager.func_179098_w();
/* 1039 */       chunk.bindColorTexture(false, magFilter);
/*      */       
/* 1041 */       GlStateManager.func_187399_a(8960, 8704, 8448);
/*      */     } else {
/* 1043 */       GlStateManager.func_179090_x();
/*      */     } 
/*      */   }
/*      */   public static void restoreTextureStates() {
/* 1047 */     GlStateManager.func_179138_g(33987);
/* 1048 */     GlStateManager.func_179090_x();
/* 1049 */     GlStateManager.func_179138_g(33986);
/* 1050 */     GlStateManager.func_179090_x();
/* 1051 */     GlStateManager.func_179138_g(33985);
/* 1052 */     GlStateManager.func_187399_a(8960, 8704, 8448);
/* 1053 */     GlStateManager.func_179090_x();
/* 1054 */     GlStateManager.func_179138_g(33984);
/* 1055 */     GlStateManager.func_187399_a(8960, 8704, 8448);
/*      */   }
/*      */ 
/*      */   
/*      */   private static long bytesToMb(long bytes) {
/* 1060 */     return bytes / 1024L / 1024L;
/*      */   }
/*      */   
/*      */   private void setColourBuffer(float r, float g, float b, float a) {
/* 1064 */     this.colourBuffer[0] = r;
/* 1065 */     this.colourBuffer[1] = g;
/* 1066 */     this.colourBuffer[2] = b;
/* 1067 */     this.colourBuffer[3] = a;
/*      */   }
/*      */ }


/* Location:              C:\Users\ioanm\Downloads\XaerosWorldMap_1.7.3_Forge_1.12.jar!\xaero\map\gui\GuiMap.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */