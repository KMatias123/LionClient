/*    */ package xaero.common.controls.event;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.settings.KeyBinding;
/*    */ import net.minecraftforge.client.settings.IKeyConflictContext;
/*    */ import net.minecraftforge.client.settings.KeyConflictContext;
/*    */ import xaero.common.IXaeroMinimap;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class KeyEventHandler
/*    */ {
/* 17 */   public ArrayList<KeyEvent> keyEvents = new ArrayList<>();
/* 18 */   public ArrayList<KeyEvent> oldKeyEvents = new ArrayList<>();
/*    */ 
/*    */   
/*    */   private boolean eventExists(KeyBinding kb) {
/* 22 */     for (KeyEvent o : this.keyEvents) {
/* 23 */       if (o.getKb() == kb)
/* 24 */         return true; 
/* 25 */     }  return oldEventExists(kb);
/*    */   }
/*    */   
/*    */   private boolean oldEventExists(KeyBinding kb) {
/* 29 */     for (KeyEvent o : this.oldKeyEvents) {
/* 30 */       if (o.getKb() == kb)
/* 31 */         return true; 
/* 32 */     }  return false;
/*    */   }
/*    */   public void handleEvents(Minecraft mc, IXaeroMinimap modMain) {
/*    */     int i;
/* 36 */     for (i = 0; i < this.oldKeyEvents.size(); i++) {
/* 37 */       KeyEvent ke = this.oldKeyEvents.get(i);
/* 38 */       if (!modMain.getControls().isDown(ke.getKb())) {
/* 39 */         modMain.getControls().keyUp(ke.getKb(), ke.isTickEnd());
/* 40 */         while (ke.getKb().func_151468_f());
/*    */         
/* 42 */         this.oldKeyEvents.remove(i);
/* 43 */         i--;
/*    */       } 
/*    */     } 
/* 46 */     for (i = 0; i < this.keyEvents.size(); i++) {
/* 47 */       KeyEvent ke = this.keyEvents.get(i);
/* 48 */       if (mc.field_71462_r == null && (!ke.wasFiredOnce() || modMain.getControls().isDown(ke.getKb()))) {
/* 49 */         modMain.getControls().keyDown(ke.getKb(), ke.isTickEnd(), ke.isRepeat());
/*    */       }
/*    */       
/* 52 */       if (!ke.isRepeat() || !modMain.getControls().isDown(ke.getKb())) {
/* 53 */         if (!oldEventExists(ke.getKb()))
/* 54 */           this.oldKeyEvents.add(ke); 
/* 55 */         this.keyEvents.remove(i);
/* 56 */         i--;
/*    */       } 
/* 58 */       ke.setFiredOnce();
/*    */     } 
/*    */   }
/*    */   
/*    */   public void onKeyInput(Minecraft mc, IXaeroMinimap modMain) {
/* 63 */     List<KeyBinding> kbs = (modMain.getControls()).keybindings;
/* 64 */     for (int i = 0; i < kbs.size(); i++) {
/* 65 */       KeyBinding kb = kbs.get(i);
/*    */       try {
/* 67 */         boolean pressed = (kb.func_151468_f() && kb.getKeyModifier().isActive((IKeyConflictContext)KeyConflictContext.IN_GAME));
/*    */ 
/*    */         
/* 70 */         while (kb.func_151468_f());
/* 71 */         if (mc.field_71462_r == null && !eventExists(kb) && pressed)
/*    */         {
/* 73 */           this.keyEvents.add(new KeyEvent(kb, false, modMain
/* 74 */                 .getSettings().isKeyRepeat(kb), true));
/*    */         }
/* 76 */       } catch (Exception exception) {}
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\ioanm\Downloads\Xaeros_Minimap_20.15.3_Forge_1.12.jar!\xaero\common\controls\event\KeyEventHandler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */