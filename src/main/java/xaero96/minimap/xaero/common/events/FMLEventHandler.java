/*    */ package xaero.common.events;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*    */ import net.minecraftforge.fml.common.gameevent.InputEvent;
/*    */ import net.minecraftforge.fml.common.gameevent.TickEvent;
/*    */ import net.minecraftforge.fml.relauncher.Side;
/*    */ import org.lwjgl.opengl.GL11;
/*    */ import xaero.common.IXaeroMinimap;
/*    */ import xaero.common.api.spigot.ServerWaypointStorage;
/*    */ import xaero.common.controls.event.KeyEventHandler;
/*    */ import xaero.common.minimap.MinimapProcessor;
/*    */ import xaero.common.minimap.waypoints.WaypointsManager;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FMLEventHandler
/*    */ {
/*    */   private IXaeroMinimap modMain;
/*    */   private MinimapProcessor minimap;
/*    */   protected WaypointsManager waypointsManager;
/*    */   private KeyEventHandler keyEventHandler;
/*    */   
/*    */   public FMLEventHandler(IXaeroMinimap modMain, KeyEventHandler keyEventHandler) {
/* 27 */     this.modMain = modMain;
/* 28 */     this.minimap = modMain.getInterfaces().getMinimap();
/* 29 */     this.waypointsManager = modMain.getWaypointsManager();
/* 30 */     this.keyEventHandler = keyEventHandler;
/*    */   }
/*    */   
/*    */   @SubscribeEvent
/*    */   public void handleClientTickEvent(TickEvent.ClientTickEvent event) {
/* 35 */     if (event.phase == TickEvent.Phase.START) {
/* 36 */       ServerWaypointStorage.update(this.modMain, this.waypointsManager);
/* 37 */       this.minimap.onClientTick();
/*    */     } 
/*    */   }
/*    */   
/*    */   @SubscribeEvent
/*    */   public void handlePlayerTickEvent(TickEvent.PlayerTickEvent event) {
/* 43 */     if (event.side == Side.CLIENT && event.player == (Minecraft.func_71410_x()).field_71439_g && 
/* 44 */       event.phase == TickEvent.Phase.START) {
/* 45 */       this.waypointsManager.updateWorldIds();
/* 46 */       this.minimap.onPlayerTick();
/* 47 */       if (this.modMain.getSettings() != null && (this.modMain
/* 48 */         .getSettings().getDeathpoints() || this.modMain.getSettings().getShowWaypoints() || this.modMain
/* 49 */         .getSettings().getShowIngameWaypoints() || (this.modMain.getSupportMods().worldmap() && (this.modMain.getSupportMods()).worldmapSupport.getWorldMapWaypoints()))) {
/* 50 */         this.waypointsManager.updateWaypoints();
/* 51 */       } else if (this.waypointsManager.getWaypoints() != null) {
/* 52 */         this.waypointsManager.setWaypoints(null);
/*    */       } 
/* 54 */       Minecraft mc = Minecraft.func_71410_x();
/* 55 */       this.keyEventHandler.handleEvents(mc, this.modMain);
/* 56 */       playerTickPostOverridable();
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void playerTickPostOverridable() {}
/*    */ 
/*    */ 
/*    */   
/*    */   @SubscribeEvent
/*    */   public void handleInputEvent(InputEvent event) {
/* 68 */     if ((Minecraft.func_71410_x()).field_71439_g != null) {
/* 69 */       Minecraft mc = Minecraft.func_71410_x();
/* 70 */       if (mc.field_71462_r == null) {
/* 71 */         this.keyEventHandler.onKeyInput(mc, this.modMain);
/*    */       }
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   @SubscribeEvent
/*    */   public void handleRenderTickEvent(TickEvent.RenderTickEvent event) {
/* 79 */     if (event.phase == TickEvent.Phase.START) {
/* 80 */       this.minimap.checkCrashes();
/* 81 */       synchronized (this.minimap.getTexturesToDelete()) {
/* 82 */         if (!this.minimap.getTexturesToDelete().isEmpty()) {
/* 83 */           int toDelete = ((Integer)this.minimap.getTexturesToDelete().get(0)).intValue();
/* 84 */           GL11.glDeleteTextures(toDelete);
/*    */           
/* 86 */           this.minimap.getTexturesToDelete().remove(0);
/*    */         } 
/*    */       } 
/* 89 */       this.minimap.setMainValues();
/* 90 */       this.minimap.getMinimapWriter().onRender();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\ioanm\Downloads\Xaeros_Minimap_20.15.3_Forge_1.12.jar!\xaero\common\events\FMLEventHandler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */