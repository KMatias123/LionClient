/*     */ package xaero.map.events;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.settings.KeyBinding;
/*     */ import net.minecraftforge.client.settings.IKeyConflictContext;
/*     */ import net.minecraftforge.client.settings.KeyConflictContext;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ import net.minecraftforge.fml.common.gameevent.InputEvent;
/*     */ import net.minecraftforge.fml.common.gameevent.TickEvent;
/*     */ import net.minecraftforge.fml.relauncher.Side;
/*     */ import xaero.map.MapProcessor;
/*     */ import xaero.map.WorldMap;
/*     */ import xaero.map.controls.ControlsHandler;
/*     */ import xaero.map.controls.KeyEvent;
/*     */ 
/*     */ 
/*     */ public class FMLEvents
/*     */ {
/*  21 */   private ArrayList<KeyEvent> keyEvents = new ArrayList<>();
/*  22 */   private ArrayList<KeyEvent> oldKeyEvents = new ArrayList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean eventExists(KeyBinding kb) {
/*  28 */     for (KeyEvent o : this.keyEvents) {
/*  29 */       if (o.getKb() == kb)
/*  30 */         return true; 
/*  31 */     }  return oldEventExists(kb);
/*     */   }
/*     */   
/*     */   private boolean oldEventExists(KeyBinding kb) {
/*  35 */     for (KeyEvent o : this.oldKeyEvents) {
/*  36 */       if (o.getKb() == kb)
/*  37 */         return true; 
/*  38 */     }  return false;
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void playerTick(TickEvent.PlayerTickEvent event) throws Exception {
/*  43 */     if (event.side == Side.CLIENT && event.player == (Minecraft.func_71410_x()).field_71439_g && 
/*  44 */       event.phase == TickEvent.Phase.START) {
/*  45 */       Minecraft mc = Minecraft.func_71410_x();
/*     */       
/*     */       int i;
/*  48 */       for (i = 0; i < this.keyEvents.size(); i++) {
/*  49 */         KeyEvent ke = this.keyEvents.get(i);
/*  50 */         if (mc.field_71462_r == null) {
/*  51 */           WorldMap.ch.keyDown(ke.getKb(), ke.isTickEnd(), ke.isRepeat());
/*     */         }
/*     */ 
/*     */         
/*  55 */         if (!ke.isRepeat()) {
/*  56 */           if (!oldEventExists(ke.getKb()))
/*  57 */             this.oldKeyEvents.add(ke); 
/*  58 */           this.keyEvents.remove(i);
/*  59 */           i--;
/*  60 */         } else if (!ControlsHandler.isDown(ke.getKb())) {
/*  61 */           WorldMap.ch.keyUp(ke.getKb(), ke.isTickEnd());
/*     */ 
/*     */           
/*  64 */           this.keyEvents.remove(i);
/*  65 */           i--;
/*     */         } 
/*     */       } 
/*  68 */       for (i = 0; i < this.oldKeyEvents.size(); i++) {
/*  69 */         KeyEvent ke = this.oldKeyEvents.get(i);
/*  70 */         if (!ControlsHandler.isDown(ke.getKb())) {
/*  71 */           WorldMap.ch.keyUp(ke.getKb(), ke.isTickEnd());
/*     */ 
/*     */           
/*  74 */           this.oldKeyEvents.remove(i);
/*  75 */           i--;
/*     */         } 
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @SubscribeEvent
/*     */   public void clientTick(TickEvent.ClientTickEvent event) throws Exception {
/*  95 */     if (event.phase == TickEvent.Phase.START)
/*  96 */       MapProcessor.instance.onClientTickStart(); 
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onKeyInput(InputEvent event) {
/* 101 */     List<KeyBinding> kbs = WorldMap.ch.keybindings;
/* 102 */     for (int i = 0; i < kbs.size(); i++) {
/* 103 */       KeyBinding kb = kbs.get(i);
/*     */       try {
/* 105 */         boolean pressed = (kb.func_151468_f() && kb.getKeyModifier().isActive((IKeyConflictContext)KeyConflictContext.IN_GAME));
/*     */ 
/*     */         
/* 108 */         while (kb.func_151468_f());
/* 109 */         if ((Minecraft.func_71410_x()).field_71462_r == null && !eventExists(kb) && pressed)
/*     */         {
/* 111 */           this.keyEvents.add(new KeyEvent(kb, false, 
/* 112 */                 ControlsHandler.isKeyRepeat(kb), true));
/*     */         }
/* 114 */       } catch (Exception exception) {}
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\ioanm\Downloads\XaerosWorldMap_1.7.3_Forge_1.12.jar!\xaero\map\events\FMLEvents.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */