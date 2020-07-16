/*    */ package xaero.common.minimap.waypoints;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ 
/*    */ public class WaypointSet
/*    */ {
/*    */   private String name;
/*    */   private ArrayList<Waypoint> list;
/*    */   
/*    */   public WaypointSet(String name) {
/* 11 */     this.name = name;
/* 12 */     this.list = new ArrayList<>();
/*    */   }
/*    */   
/*    */   public String getName() {
/* 16 */     return this.name;
/*    */   }
/*    */   
/*    */   public ArrayList<Waypoint> getList() {
/* 20 */     return this.list;
/*    */   }
/*    */ }


/* Location:              C:\Users\ioanm\Downloads\Xaeros_Minimap_20.15.3_Forge_1.12.jar!\xaero\common\minimap\waypoints\WaypointSet.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */