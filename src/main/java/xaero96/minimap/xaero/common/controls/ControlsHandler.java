/*     */ package xaero.common.controls;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.GuiScreen;
/*     */ import net.minecraft.client.settings.GameSettings;
/*     */ import net.minecraft.client.settings.KeyBinding;
/*     */ import net.minecraftforge.fml.client.registry.ClientRegistry;
/*     */ import xaero.common.IXaeroMinimap;
/*     */ import xaero.common.gui.GuiAddWaypoint;
/*     */ import xaero.common.gui.GuiSlimeSeed;
/*     */ import xaero.common.gui.GuiWaypoints;
/*     */ import xaero.common.minimap.MinimapProcessor;
/*     */ import xaero.common.minimap.waypoints.WaypointWorld;
/*     */ import xaero.common.minimap.waypoints.WaypointsManager;
/*     */ import xaero.common.misc.OptimizedMath;
/*     */ import xaero.common.settings.ModOptions;
/*     */ import xaero.common.settings.ModSettings;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ControlsHandler
/*     */ {
/*     */   protected IXaeroMinimap modMain;
/*     */   protected MinimapProcessor minimap;
/*     */   protected WaypointsManager waypointsManager;
/*     */   public final List<KeyBinding> keybindings;
/*     */   
/*     */   public ControlsHandler(IXaeroMinimap modMain) {
/*  34 */     this.modMain = modMain;
/*  35 */     this.minimap = modMain.getInterfaces().getMinimap();
/*  36 */     this.waypointsManager = modMain.getWaypointsManager();
/*  37 */     this.keybindings = Lists.newArrayList((Object[])new KeyBinding[] { ModSettings.keySwitchSet, ModSettings.keyInstantWaypoint, ModSettings.keyToggleSlimes, ModSettings.keyToggleGrid, ModSettings.keyToggleWaypoints, ModSettings.keyToggleMapWaypoints, ModSettings.keyToggleMap, ModSettings.keyLargeMap, ModSettings.keyWaypoints, ModSettings.keyBindZoom, ModSettings.keyBindZoom1, ModSettings.newWaypoint, ModSettings.keyAllSets });
/*     */ 
/*     */     
/*  40 */     registerKeybindings();
/*     */   }
/*     */   
/*     */   protected void registerKeybindings() {
/*  44 */     for (KeyBinding kb : this.keybindings)
/*  45 */       ClientRegistry.registerKeyBinding(kb); 
/*     */   }
/*     */   
/*     */   public void setKeyState(KeyBinding kb, boolean pressed) {
/*  49 */     KeyBinding.func_74510_a(kb.func_151463_i(), pressed);
/*     */   }
/*     */   
/*     */   public boolean isDown(KeyBinding kb) {
/*  53 */     return GameSettings.func_100015_a(kb);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void keyDownPre(KeyBinding kb) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void keyDownPost(KeyBinding kb) {}
/*     */ 
/*     */   
/*     */   public void keyDown(KeyBinding kb, boolean tickEnd, boolean isRepeat) {
/*  66 */     Minecraft mc = Minecraft.func_71410_x();
/*  67 */     if (!tickEnd) {
/*  68 */       keyDownPre(kb);
/*  69 */       if (kb == ModSettings.newWaypoint && this.modMain.getSettings().waypointsGUI())
/*  70 */         mc.func_147108_a((GuiScreen)new GuiAddWaypoint(this.modMain, null, new ArrayList(), this.waypointsManager.getCurrentContainerID().split("/")[0], this.waypointsManager.getCurrentWorld(), true)); 
/*  71 */       if (kb == ModSettings.keyWaypoints && this.modMain.getSettings().waypointsGUI())
/*  72 */         mc.func_147108_a((GuiScreen)new GuiWaypoints(this.modMain, null)); 
/*  73 */       if (kb == ModSettings.keyLargeMap) {
/*  74 */         this.minimap.setEnlargedMap(true);
/*  75 */         this.minimap.setToResetImage(true);
/*  76 */         this.minimap.instantZoom();
/*     */       } 
/*  78 */       if (kb == ModSettings.keyToggleMap) {
/*     */         try {
/*  80 */           this.modMain.getSettings().setOptionValue(ModOptions.MINIMAP, 0);
/*  81 */           this.modMain.getSettings().saveSettings();
/*  82 */         } catch (IOException e) {
/*  83 */           e.printStackTrace();
/*     */         } 
/*     */       }
/*  86 */       if (kb == ModSettings.keyToggleWaypoints) {
/*     */         try {
/*  88 */           this.modMain.getSettings().setOptionValue(ModOptions.INGAME_WAYPOINTS, 0);
/*     */           
/*  90 */           this.modMain.getSettings().saveSettings();
/*  91 */         } catch (IOException e) {
/*  92 */           e.printStackTrace();
/*     */         } 
/*     */       }
/*  95 */       if (kb == ModSettings.keyToggleMapWaypoints) {
/*     */         try {
/*  97 */           this.modMain.getSettings().setOptionValue(ModOptions.WAYPOINTS, 0);
/*  98 */           this.modMain.getSettings().saveSettings();
/*  99 */         } catch (IOException e) {
/* 100 */           e.printStackTrace();
/*     */         } 
/*     */       }
/* 103 */       if (kb == ModSettings.keyToggleSlimes) {
/*     */         try {
/* 105 */           if (this.modMain.getSettings().customSlimeSeedNeeded() && this.modMain.getSettings().getBooleanValue(ModOptions.OPEN_SLIME_SETTINGS)) {
/* 106 */             Minecraft.func_71410_x().func_147108_a((GuiScreen)new GuiSlimeSeed(this.modMain, (Minecraft.func_71410_x()).field_71462_r));
/*     */           } else {
/* 108 */             (this.modMain.getSettings()).slimeChunks = !(this.modMain.getSettings()).slimeChunks;
/* 109 */             this.modMain.getSettings().saveSettings();
/*     */           } 
/* 111 */         } catch (IOException e) {
/* 112 */           e.printStackTrace();
/*     */         } 
/*     */       }
/* 115 */       if (kb == ModSettings.keyToggleGrid) {
/*     */         try {
/* 117 */           (this.modMain.getSettings()).chunkGrid = -(this.modMain.getSettings()).chunkGrid - 1;
/* 118 */           this.modMain.getSettings().saveSettings();
/* 119 */         } catch (IOException e) {
/* 120 */           e.printStackTrace();
/*     */         } 
/*     */       }
/* 123 */       if (kb == ModSettings.keyInstantWaypoint) {
/* 124 */         this.waypointsManager.createTemporaryWaypoints(this.waypointsManager.getCurrentWorld(), OptimizedMath.myFloor(mc.field_71439_g.field_70165_t), OptimizedMath.myFloor(mc.field_71439_g.field_70163_u), OptimizedMath.myFloor(mc.field_71439_g.field_70161_v));
/*     */       }
/* 126 */       if (kb == ModSettings.keySwitchSet) {
/* 127 */         WaypointWorld currentWorld = this.waypointsManager.getCurrentWorld();
/* 128 */         if (currentWorld != null) {
/* 129 */           String[] keys = (String[])currentWorld.getSets().keySet().toArray((Object[])new String[0]);
/* 130 */           for (int i = 0; i < keys.length; i++) {
/* 131 */             if (keys[i] != null && keys[i].equals(currentWorld.getCurrent())) {
/* 132 */               currentWorld.setCurrent(keys[(i + 1) % keys.length]);
/*     */               break;
/*     */             } 
/*     */           } 
/* 136 */           this.waypointsManager.updateWaypoints();
/* 137 */           this.waypointsManager.setChanged = System.currentTimeMillis();
/*     */           try {
/* 139 */             this.modMain.getSettings().saveWaypoints(currentWorld);
/* 140 */           } catch (IOException e) {
/* 141 */             e.printStackTrace();
/*     */           } 
/*     */         } 
/*     */       } 
/* 145 */       if (kb == ModSettings.keyAllSets) {
/*     */         try {
/* 147 */           this.modMain.getSettings().setOptionValue(ModOptions.WAYPOINTS_ALL_SETS, 0);
/* 148 */         } catch (IOException e) {
/* 149 */           e.printStackTrace();
/*     */         } 
/*     */       }
/* 152 */       keyDownPost(kb);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void keyUpPre(KeyBinding kb) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void keyUpPost(KeyBinding kb) {}
/*     */ 
/*     */   
/*     */   public void keyUp(KeyBinding kb, boolean tickEnd) {
/* 166 */     if (!tickEnd) {
/* 167 */       keyUpPre(kb);
/*     */       
/* 169 */       if (!this.modMain.getInterfaces().getMinimap().isEnlargedMap() || !(this.modMain.getSettings()).zoomedOutEnlarged) {
/* 170 */         if (kb == ModSettings.keyBindZoom) {
/*     */           try {
/* 172 */             this.modMain.getSettings().setOptionValue(ModOptions.ZOOM, 1);
/* 173 */           } catch (IOException e) {
/* 174 */             e.printStackTrace();
/*     */           } 
/*     */         }
/* 177 */         if (kb == ModSettings.keyBindZoom1) {
/* 178 */           (this.modMain.getSettings()).zoom--;
/* 179 */           if ((this.modMain.getSettings()).zoom == -1)
/* 180 */             (this.modMain.getSettings()).zoom = (this.modMain.getSettings()).zooms.length - 1; 
/*     */           try {
/* 182 */             this.modMain.getSettings().saveSettings();
/* 183 */           } catch (IOException e) {
/* 184 */             e.printStackTrace();
/*     */           } 
/*     */         } 
/*     */       } 
/* 188 */       if (kb == ModSettings.keyLargeMap) {
/* 189 */         this.minimap.setEnlargedMap(false);
/* 190 */         this.minimap.setToResetImage(true);
/* 191 */         this.minimap.instantZoom();
/*     */       } 
/* 193 */       keyUpPost(kb);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\ioanm\Downloads\Xaeros_Minimap_20.15.3_Forge_1.12.jar!\xaero\common\controls\ControlsHandler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */