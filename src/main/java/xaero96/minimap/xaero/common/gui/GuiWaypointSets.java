/*    */ package xaero.common.gui;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collections;
/*    */ import net.minecraft.client.resources.I18n;
/*    */ import xaero.common.minimap.waypoints.WaypointWorld;
/*    */ 
/*    */ 
/*    */ public class GuiWaypointSets
/*    */ {
/*    */   private int currentSet;
/*    */   private String[] options;
/*    */   
/*    */   public GuiWaypointSets(boolean canCreate, WaypointWorld currentWorld) {
/* 15 */     String c = currentWorld.getCurrent();
/* 16 */     int size = currentWorld.getSets().size() + (canCreate ? 1 : 0);
/* 17 */     String[] keys = (String[])currentWorld.getSets().keySet().toArray((Object[])new String[0]);
/* 18 */     ArrayList<KeySortableByOther<String>> keysList = new ArrayList<>(); int i;
/* 19 */     for (i = 0; i < keys.length; i++) {
/* 20 */       String key = keys[i];
/* 21 */       keysList.add(new KeySortableByOther<>(key, new Comparable[] { I18n.func_135052_a(key, new Object[0]).toLowerCase() }));
/*    */     } 
/* 23 */     Collections.sort(keysList);
/* 24 */     this.options = new String[size];
/* 25 */     for (i = 0; i < keysList.size(); i++) {
/* 26 */       this.options[i] = ((KeySortableByOther<String>)keysList.get(i)).getKey();
/* 27 */       if (this.options[i].equals(c))
/* 28 */         this.currentSet = i; 
/*    */     } 
/* 30 */     if (canCreate)
/* 31 */       this.options[this.options.length - 1] = "§8" + I18n.func_135052_a("gui.xaero_create_set", new Object[0]); 
/*    */   }
/*    */   
/*    */   public String getCurrentSetKey() {
/* 35 */     return this.options[this.currentSet];
/*    */   }
/*    */   
/*    */   public int getCurrentSet() {
/* 39 */     return this.currentSet;
/*    */   }
/*    */   
/*    */   public void setCurrentSet(int currentSet) {
/* 43 */     this.currentSet = currentSet;
/*    */   }
/*    */   
/*    */   public String[] getOptions() {
/* 47 */     return this.options;
/*    */   }
/*    */ }


/* Location:              C:\Users\ioanm\Downloads\Xaeros_Minimap_20.15.3_Forge_1.12.jar!\xaero\common\gui\GuiWaypointSets.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */