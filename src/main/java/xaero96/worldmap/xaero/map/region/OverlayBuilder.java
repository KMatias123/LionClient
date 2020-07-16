/*    */ package xaero.map.region;
/*    */ 
/*    */ import net.minecraft.block.state.IBlockState;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*    */ import net.minecraft.world.World;
/*    */ import xaero.map.MapProcessor;
/*    */ import xaero.map.Misc;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class OverlayBuilder
/*    */ {
/*    */   private static final int MAX_OVERLAYS = 10;
/*    */   private Overlay[] overlayBuildingSet;
/*    */   private int currentOverlayIndex;
/*    */   private OverlayManager overlayManager;
/*    */   private TextureAtlasSprite prevIcon;
/*    */   
/*    */   public OverlayBuilder(OverlayManager overlayManager) {
/* 22 */     this.overlayManager = overlayManager;
/* 23 */     this.overlayBuildingSet = new Overlay[10];
/* 24 */     for (int i = 0; i < this.overlayBuildingSet.length; i++)
/* 25 */       this.overlayBuildingSet[i] = new Overlay(0, new int[3], 0, (byte)0, false); 
/* 26 */     this.currentOverlayIndex = -1;
/*    */   }
/*    */   
/*    */   public void startBuilding() {
/* 30 */     this.currentOverlayIndex = -1;
/*    */   }
/*    */   
/*    */   public void build(int state, int[] biome, int opacity, byte light, World world) {
/* 34 */     Overlay currentOverlay = null;
/* 35 */     if (this.currentOverlayIndex >= 0)
/* 36 */       currentOverlay = this.overlayBuildingSet[this.currentOverlayIndex]; 
/* 37 */     Overlay nextOverlay = null;
/* 38 */     if (this.currentOverlayIndex < this.overlayBuildingSet.length - 1)
/* 39 */       nextOverlay = this.overlayBuildingSet[this.currentOverlayIndex + 1]; 
/* 40 */     TextureAtlasSprite icon = null;
/* 41 */     boolean changed = false;
/* 42 */     if (currentOverlay == null || currentOverlay.getState() != state) {
/* 43 */       icon = Minecraft.func_71410_x().func_175602_ab().func_175023_a().func_178122_a(Misc.getStateById(state));
/* 44 */       changed = (icon != this.prevIcon);
/*    */     } 
/* 46 */     if (nextOverlay != null && (currentOverlay == null || changed)) {
/* 47 */       IBlockState s = Misc.getStateById(state);
/* 48 */       int intensity = (s.func_177230_c() instanceof net.minecraft.block.BlockLiquid) ? 2 : ((s.func_177230_c() instanceof net.minecraft.block.BlockIce) ? 5 : 1);
/* 49 */       boolean glowing = false;
/*    */       try {
/* 51 */         glowing = MapProcessor.instance.getMapWriter().isGlowing(s);
/*    */       
/*    */       }
/* 54 */       catch (Exception exception) {}
/*    */ 
/*    */       
/* 57 */       nextOverlay.write(state, biome, intensity, light, glowing);
/* 58 */       currentOverlay = nextOverlay;
/* 59 */       this.currentOverlayIndex++;
/*    */     } 
/* 61 */     currentOverlay.increaseOpacity(opacity);
/* 62 */     if (changed)
/* 63 */       this.prevIcon = icon; 
/*    */   }
/*    */   
/*    */   public void finishBuilding(MapBlock block) {
/* 67 */     for (int i = 0; i <= this.currentOverlayIndex; i++) {
/* 68 */       Overlay o = this.overlayBuildingSet[i];
/* 69 */       Overlay original = this.overlayManager.getOriginal(o);
/* 70 */       if (o == original)
/* 71 */         this.overlayBuildingSet[i] = new Overlay(0, new int[3], 0, (byte)0, false); 
/* 72 */       block.addOverlay(original);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\ioanm\Downloads\XaerosWorldMap_1.7.3_Forge_1.12.jar!\xaero\map\region\OverlayBuilder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */