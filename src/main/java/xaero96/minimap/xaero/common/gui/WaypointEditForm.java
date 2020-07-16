/*    */ package xaero.common.gui;
/*    */ 
/*    */ public class WaypointEditForm
/*    */ {
/*  5 */   public String name = "";
/*  6 */   public String xText = "";
/*  7 */   public String yText = "";
/*  8 */   public String zText = "";
/*  9 */   public String yawText = "";
/* 10 */   public String initial = "";
/*    */   public boolean disabled;
/*    */   public boolean global;
/*    */   public int color;
/*    */   public boolean keepName;
/*    */   public boolean keepXText;
/*    */   public boolean keepYText;
/*    */   public boolean keepZText;
/*    */   public boolean keepYawText;
/*    */   public boolean keepInitial;
/*    */   public boolean keepDisabled;
/*    */   public boolean keepGlobal;
/*    */   public boolean defaultKeepYawText;
/*    */   public boolean defaultKeepDisabled;
/*    */   public boolean defaultKeepGlobal;
/*    */   
/*    */   public String getName() {
/* 27 */     return this.name;
/*    */   }
/*    */   public String getxText() {
/* 30 */     return this.xText;
/*    */   }
/*    */   public String getyText() {
/* 33 */     return this.yText;
/*    */   }
/*    */   public String getzText() {
/* 36 */     return this.zText;
/*    */   }
/*    */   public String getYawText() {
/* 39 */     return this.yawText;
/*    */   }
/*    */   public String getInitial() {
/* 42 */     return this.initial;
/*    */   }
/*    */   public boolean isGlobal() {
/* 45 */     return this.global;
/*    */   }
/*    */   public boolean isDisabled() {
/* 48 */     return this.disabled;
/*    */   }
/*    */   public int getColor() {
/* 51 */     return this.color;
/*    */   }
/*    */ }


/* Location:              C:\Users\ioanm\Downloads\Xaeros_Minimap_20.15.3_Forge_1.12.jar!\xaero\common\gui\WaypointEditForm.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */