/*    */ package xaero.common.api.spigot;
/*    */ 
/*    */ import xaero.common.minimap.waypoints.Waypoint;
/*    */ import xaero.common.minimap.waypoints.WaypointWorld;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ServerWaypoint
/*    */   extends Waypoint
/*    */ {
/*    */   private String worldUID;
/*    */   private int ID;
/*    */   
/*    */   public ServerWaypoint(String worldUID, int ID, int x, int y, int z, String name, String symbol, int color, boolean rotation, int yaw) {
/* 16 */     super(x, y, z, name, symbol, color, 0);
/* 17 */     this.worldUID = worldUID;
/* 18 */     this.ID = ID;
/* 19 */     setRotation(rotation);
/* 20 */     setYaw(yaw);
/*    */   }
/*    */   
/*    */   public String getWorldUID() {
/* 24 */     return this.worldUID;
/*    */   }
/*    */   
/*    */   public int getID() {
/* 28 */     return this.ID;
/*    */   }
/*    */   
/*    */   public boolean isDisabled() {
/* 32 */     WaypointWorld w = ServerWaypointStorage.getWorld(this.worldUID);
/* 33 */     Boolean d = (Boolean)w.getServerWaypointsDisabled().get(getX() + "_" + getY() + "_" + getZ());
/* 34 */     return (d != null && d.booleanValue());
/*    */   }
/*    */   
/*    */   public void setDisabled(boolean b) {
/* 38 */     ServerWaypointStorage.getWorld(this.worldUID).getServerWaypointsDisabled().put(getX() + "_" + getY() + "_" + getZ(), Boolean.valueOf(b));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isServerWaypoint() {
/* 43 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\ioanm\Downloads\Xaeros_Minimap_20.15.3_Forge_1.12.jar!\xaero\common\api\spigot\ServerWaypoint.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */