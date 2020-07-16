/*    */ package xaero.common.controls.event;
/*    */ 
/*    */ import net.minecraft.client.settings.KeyBinding;
/*    */ 
/*    */ public class KeyEvent {
/*    */   private KeyBinding kb;
/*    */   private boolean tickEnd;
/*    */   private boolean isRepeat;
/*    */   private boolean keyDown;
/*    */   private boolean firedOnce;
/*    */   
/*    */   public KeyEvent(KeyBinding kb, boolean tickEnd, boolean isRepeat, boolean keyDown) {
/* 13 */     this.kb = kb;
/* 14 */     this.tickEnd = tickEnd;
/* 15 */     this.isRepeat = isRepeat;
/* 16 */     this.keyDown = keyDown;
/*    */   }
/*    */   public KeyBinding getKb() {
/* 19 */     return this.kb;
/*    */   }
/*    */   public boolean isTickEnd() {
/* 22 */     return this.tickEnd;
/*    */   }
/*    */   public boolean isRepeat() {
/* 25 */     return this.isRepeat;
/*    */   }
/*    */   public boolean isKeyDown() {
/* 28 */     return this.keyDown;
/*    */   }
/*    */   public boolean wasFiredOnce() {
/* 31 */     return this.firedOnce;
/*    */   }
/*    */   public void setFiredOnce() {
/* 34 */     this.firedOnce = true;
/*    */   }
/*    */ }


/* Location:              C:\Users\ioanm\Downloads\Xaeros_Minimap_20.15.3_Forge_1.12.jar!\xaero\common\controls\event\KeyEvent.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */