/*    */ package xaero.common.minimap;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import xaero.common.IXaeroMinimap;
/*    */ import xaero.common.interfaces.Interface;
/*    */ import xaero.common.interfaces.InterfaceManager;
/*    */ import xaero.common.minimap.render.MinimapFBORenderer;
/*    */ import xaero.common.minimap.render.MinimapSafeModeRenderer;
/*    */ import xaero.common.minimap.waypoints.render.WaypointsGuiRenderer;
/*    */ import xaero.common.minimap.waypoints.render.WaypointsIngameRenderer;
/*    */ import xaero.common.minimap.write.MinimapWriter;
/*    */ import xaero.common.settings.ModOptions;
/*    */ 
/*    */ public class MinimapInterface
/*    */   extends Interface {
/*    */   private IXaeroMinimap modMain;
/*    */   private MinimapProcessor minimap;
/*    */   private Minecraft mc;
/*    */   private WaypointsGuiRenderer waypointsGuiRenderer;
/*    */   private WaypointsIngameRenderer waypointsIngameRenderer;
/*    */   
/*    */   public MinimapInterface(IXaeroMinimap modMain, int id, InterfaceManager interfaces) {
/* 23 */     super(interfaces, "gui.xaero_minimap", id, 128, 128, ModOptions.MINIMAP);
/* 24 */     this.mc = Minecraft.func_71410_x();
/* 25 */     this.modMain = modMain;
/* 26 */     MinimapWriter minimapWriter = new MinimapWriter(this.modMain);
/* 27 */     MinimapRadar entityRadar = new MinimapRadar(this.modMain);
/* 28 */     this.waypointsGuiRenderer = new WaypointsGuiRenderer(modMain, this.mc);
/* 29 */     this.waypointsIngameRenderer = new WaypointsIngameRenderer(modMain, this.mc);
/* 30 */     MinimapFBORenderer minimapFBORenderer = new MinimapFBORenderer(modMain, this.mc, this.waypointsGuiRenderer);
/* 31 */     MinimapSafeModeRenderer minimapSafeModeRenderer = new MinimapSafeModeRenderer(modMain, this.mc, this.waypointsGuiRenderer);
/* 32 */     this.minimap = new MinimapProcessor(modMain, minimapWriter, minimapFBORenderer, minimapSafeModeRenderer, entityRadar);
/*    */   }
/*    */   
/*    */   public void drawInterface(int width, int height, int scale, float partial) {
/* 36 */     this.minimap.onRender(getX(), getY(), width, height, scale, getSize(), getW(scale), partial);
/* 37 */     super.drawInterface(width, height, scale, partial);
/*    */   }
/*    */   
/*    */   public MinimapProcessor getMinimap() {
/* 41 */     return this.minimap;
/*    */   }
/*    */   
/*    */   public int getW(int scale) {
/* 45 */     return (int)((getSize() / scale) * this.modMain.getSettings().getMinimapScale());
/*    */   }
/*    */   
/*    */   public int getH(int scale) {
/* 49 */     return getW(scale);
/*    */   }
/*    */   
/*    */   public int getWC(int scale) {
/* 53 */     return getW(scale);
/*    */   }
/*    */   
/*    */   public int getHC(int scale) {
/* 57 */     return getH(scale);
/*    */   }
/*    */   
/*    */   public int getW0(int scale) {
/* 61 */     return getW(scale);
/*    */   }
/*    */   
/*    */   public int getH0(int scale) {
/* 65 */     return getH(scale);
/*    */   }
/*    */   
/*    */   public int getSize() {
/* 69 */     return this.minimap.getMinimapSize() + 36 + 2;
/*    */   }
/*    */   
/*    */   public WaypointsGuiRenderer getWaypointsGuiRenderer() {
/* 73 */     return this.waypointsGuiRenderer;
/*    */   }
/*    */   
/*    */   public WaypointsIngameRenderer getWaypointsIngameRenderer() {
/* 77 */     return this.waypointsIngameRenderer;
/*    */   }
/*    */ }


/* Location:              C:\Users\ioanm\Downloads\Xaeros_Minimap_20.15.3_Forge_1.12.jar!\xaero\common\minimap\MinimapInterface.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */