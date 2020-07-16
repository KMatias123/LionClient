/*    */ package xaero.common.minimap.waypoints;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import java.nio.file.Files;
/*    */ import java.nio.file.Path;
/*    */ import java.util.ArrayList;
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
/*    */ 
/*    */ public class WaypointWorld
/*    */ {
/*    */   private String id;
/*    */   private HashMap<String, WaypointSet> sets;
/*    */   private HashMap<Integer, Waypoint> serverWaypoints;
/*    */   private HashMap<String, Boolean> serverWaypointsDisabled;
/* 17 */   private String current = "gui.xaero_default";
/*    */   private WaypointWorldContainer container;
/*    */   private List<String> toRemoveOnSave;
/*    */   
/*    */   public WaypointWorld(WaypointWorldContainer container, String id) {
/* 22 */     this.container = container;
/* 23 */     this.id = id;
/* 24 */     this.sets = new HashMap<>();
/* 25 */     this.serverWaypoints = new HashMap<>();
/* 26 */     this.serverWaypointsDisabled = new HashMap<>();
/* 27 */     addSet("gui.xaero_default");
/* 28 */     this.toRemoveOnSave = new ArrayList<>();
/*    */   }
/*    */   
/*    */   public WaypointSet getCurrentSet() {
/* 32 */     return this.sets.get(this.current);
/*    */   }
/*    */   
/*    */   public void addSet(String s) {
/* 36 */     this.sets.put(s, new WaypointSet(s));
/*    */   }
/*    */   
/*    */   public void onSaveCleanup(File worldFile) throws IOException {
/* 40 */     Path folder = worldFile.toPath().getParent();
/* 41 */     for (int i = 0; i < this.toRemoveOnSave.size(); i++) {
/* 42 */       String s = this.toRemoveOnSave.get(i);
/* 43 */       Path path = folder.resolve(this.id + "_" + s + ".txt");
/* 44 */       Files.deleteIfExists(path);
/*    */     } 
/*    */   }
/*    */   
/*    */   public HashMap<String, Boolean> getServerWaypointsDisabled() {
/* 49 */     return this.serverWaypointsDisabled;
/*    */   }
/*    */   
/*    */   public HashMap<Integer, Waypoint> getServerWaypoints() {
/* 53 */     return this.serverWaypoints;
/*    */   }
/*    */   
/*    */   public HashMap<String, WaypointSet> getSets() {
/* 57 */     return this.sets;
/*    */   }
/*    */   
/*    */   public String getCurrent() {
/* 61 */     return this.current;
/*    */   }
/*    */   
/*    */   public void setCurrent(String current) {
/* 65 */     this.current = current;
/*    */   }
/*    */   
/*    */   public String getId() {
/* 69 */     return this.id;
/*    */   }
/*    */   
/*    */   public String getFullId() {
/* 73 */     return this.container.getKey() + "_" + this.id;
/*    */   }
/*    */   
/*    */   public void setId(String id) {
/* 77 */     this.id = id;
/*    */   }
/*    */   
/*    */   public WaypointWorldContainer getContainer() {
/* 81 */     return this.container;
/*    */   }
/*    */   
/*    */   public void setContainer(WaypointWorldContainer container) {
/* 85 */     this.container = container;
/*    */   }
/*    */   
/*    */   public void requestRemovalOnSave(String name) {
/* 89 */     this.toRemoveOnSave.add(name);
/*    */   }
/*    */   
/*    */   public boolean hasSomethingToRemoveOnSave() {
/* 93 */     return !this.toRemoveOnSave.isEmpty();
/*    */   }
/*    */ }


/* Location:              C:\Users\ioanm\Downloads\Xaeros_Minimap_20.15.3_Forge_1.12.jar!\xaero\common\minimap\waypoints\WaypointWorld.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */