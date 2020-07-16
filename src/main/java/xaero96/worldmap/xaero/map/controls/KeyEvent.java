/*    */ package xaero.map.controls;
/*    */ 
/*    */ import net.minecraft.client.settings.KeyBinding;
/*    */ 
/*    */ public class KeyEvent {
/*    */   private KeyBinding kb;
/*    */   private boolean tickEnd;
/*    */   private boolean isRepeat;
/*    */   private boolean keyDown;
/*    */   
/*    */   public KeyEvent(KeyBinding kb, boolean tickEnd, boolean isRepeat, boolean keyDown) {
/* 12 */     this.kb = kb;
/* 13 */     this.tickEnd = tickEnd;
/* 14 */     this.isRepeat = isRepeat;
/* 15 */     this.keyDown = keyDown;
/*    */   }
/*    */   
/*    */   public KeyBinding getKb() {
/* 19 */     return this.kb;
/*    */   }
/*    */   
/*    */   public boolean isTickEnd() {
/* 23 */     return this.tickEnd;
/*    */   }
/*    */   
/*    */   public boolean isRepeat() {
/* 27 */     return this.isRepeat;
/*    */   }
/*    */   
/*    */   public boolean isKeyDown() {
/* 31 */     return this.keyDown;
/*    */   }
/*    */ }


/* Location:              C:\Users\ioanm\Downloads\XaerosWorldMap_1.7.3_Forge_1.12.jar!\xaero\map\controls\KeyEvent.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */