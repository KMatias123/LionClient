/*    */ package xaero.map.controls;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import java.util.List;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.GuiScreen;
/*    */ import net.minecraft.client.settings.GameSettings;
/*    */ import net.minecraft.client.settings.KeyBinding;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraftforge.fml.client.registry.ClientRegistry;
/*    */ import xaero.map.MapProcessor;
/*    */ import xaero.map.gui.GuiMap;
/*    */ import xaero.map.gui.GuiWorldMapSettings;
/*    */ 
/*    */ public class ControlsHandler
/*    */ {
/* 17 */   public static final KeyBinding keyOpenMap = new KeyBinding("gui.xaero_open_map", 50, "Xaero's World Map");
/* 18 */   public static final KeyBinding keyOpenSettings = new KeyBinding("gui.xaero_open_settings", 27, "Xaero's World Map");
/*    */   
/* 20 */   public static final KeyBinding keyZoomIn = new KeyBinding("gui.xaero_map_zoom_in", 0, "Xaero's World Map");
/* 21 */   public static final KeyBinding keyZoomOut = new KeyBinding("gui.xaero_map_zoom_out", 0, "Xaero's World Map");
/* 22 */   public static final KeyBinding keyQuickConfirm = new KeyBinding("gui.xaero_quick_confirm", 54, "Xaero's World Map");
/*    */   
/* 24 */   public final List<KeyBinding> keybindings = Lists.newArrayList((Object[])new KeyBinding[] { keyOpenMap, keyOpenSettings, keyZoomIn, keyZoomOut, keyQuickConfirm });
/*    */   
/*    */   public ControlsHandler() {
/* 27 */     for (KeyBinding kb : this.keybindings)
/* 28 */       ClientRegistry.registerKeyBinding(kb); 
/*    */   }
/*    */   
/*    */   public static void setKeyState(KeyBinding kb, boolean pressed) {
/* 32 */     if (kb.func_151470_d() != pressed)
/* 33 */       KeyBinding.func_74510_a(kb.func_151463_i(), pressed); 
/*    */   }
/*    */   
/*    */   public static boolean isDown(KeyBinding kb) {
/* 37 */     return GameSettings.func_100015_a(kb);
/*    */   }
/*    */   
/*    */   public static boolean isKeyRepeat(KeyBinding kb) {
/* 41 */     if (kb == keyOpenMap || kb == keyOpenSettings)
/* 42 */       return false; 
/* 43 */     return true;
/*    */   }
/*    */   
/*    */   public void keyDown(KeyBinding kb, boolean tickEnd, boolean isRepeat) {
/* 47 */     Minecraft mc = Minecraft.func_71410_x();
/* 48 */     if (!tickEnd) {
/* 49 */       if (kb == keyOpenMap) {
/* 50 */         mc.func_147108_a((GuiScreen)new GuiMap((EntityPlayer)mc.field_71439_g));
/* 51 */       } else if (kb == keyOpenSettings) {
/* 52 */         mc.func_147108_a((GuiScreen)new GuiWorldMapSettings());
/* 53 */       } else if (kb == keyQuickConfirm) {
/* 54 */         MapProcessor.instance.quickConfirmMultiworld();
/*    */       } 
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void keyUp(KeyBinding kb, boolean tickEnd) {
/* 61 */     if (!tickEnd);
/*    */   }
/*    */ }


/* Location:              C:\Users\ioanm\Downloads\XaerosWorldMap_1.7.3_Forge_1.12.jar!\xaero\map\controls\ControlsHandler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */