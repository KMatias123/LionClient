/*    */ package xaero.common.gui;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collections;
/*    */ import java.util.HashMap;
/*    */ import xaero.common.minimap.waypoints.WaypointWorldContainer;
/*    */ import xaero.common.minimap.waypoints.WaypointsManager;
/*    */ 
/*    */ public class GuiWaypointWorlds
/*    */   extends GuiDropdownHelper
/*    */ {
/*    */   public GuiWaypointWorlds(WaypointWorldContainer wc, WaypointsManager waypointsManager, String currentWorld) {
/* 13 */     String a = waypointsManager.getAutoContainerID() + "_" + waypointsManager.getAutoWorldID();
/* 14 */     this.current = -1;
/* 15 */     this.auto = -1;
/* 16 */     ArrayList<KeySortableByOther<String>> keysList = new ArrayList<>();
/* 17 */     HashMap<String, String> nameMap = new HashMap<>();
/*    */     
/* 19 */     addWorlds(wc, a, keysList, nameMap);
/*    */     
/* 21 */     Collections.sort(keysList);
/* 22 */     ArrayList<String> keysStringList = new ArrayList<>();
/* 23 */     ArrayList<String> optionsList = new ArrayList<>();
/* 24 */     for (int j = 0; j < keysList.size(); j++) {
/* 25 */       KeySortableByOther<String> keySortable = keysList.get(j);
/* 26 */       String key = keySortable.getKey();
/* 27 */       if (this.current == -1 && key.equals(currentWorld))
/* 28 */         this.current = j; 
/* 29 */       String option = "Error";
/*    */       
/*    */       try {
/* 32 */         if (this.auto == -1 && key.equals(a))
/* 33 */           this.auto = j; 
/* 34 */         option = nameMap.get(key);
/* 35 */         if (this.auto == j)
/* 36 */           option = option + " (auto)"; 
/* 37 */       } catch (Exception e) {
/* 38 */         e.printStackTrace();
/*    */       } 
/* 40 */       keysStringList.add(key);
/* 41 */       optionsList.add(option);
/*    */     } 
/* 43 */     if (this.current == -1)
/* 44 */       this.current = 0; 
/* 45 */     this.keys = keysStringList.<String>toArray(new String[0]);
/* 46 */     this.options = optionsList.<String>toArray(new String[0]);
/*    */   }
/*    */   
/*    */   private void addWorlds(WaypointWorldContainer wc, String a, ArrayList<KeySortableByOther<String>> keysList, HashMap<String, String> nameMap) {
/* 50 */     String[] worldKeys = (String[])wc.worlds.keySet().toArray((Object[])new String[0]);
/* 51 */     for (int j = 0; j < worldKeys.length; j++) {
/* 52 */       String worldKey = worldKeys[j];
/* 53 */       String containerName = wc.getSubName();
/* 54 */       String worldName = wc.getFullName(worldKey, containerName);
/* 55 */       String fullKey = wc.getKey() + "_" + worldKey;
/* 56 */       int firstNameSpace = worldName.indexOf(' ');
/* 57 */       String firstNameWord = (firstNameSpace != -1) ? worldName.substring(0, firstNameSpace) : "";
/* 58 */       int firstNameWordAsInt = 0;
/*    */       try {
/* 60 */         firstNameWordAsInt = Integer.parseInt(firstNameWord);
/* 61 */       } catch (NumberFormatException numberFormatException) {}
/* 62 */       keysList.add(new KeySortableByOther<>(fullKey, new Comparable[] { containerName.toLowerCase(), Integer.valueOf(firstNameWordAsInt), worldName.toLowerCase() }));
/* 63 */       nameMap.put(fullKey, worldName);
/*    */     } 
/* 65 */     WaypointWorldContainer[] subContainers = (WaypointWorldContainer[])wc.subContainers.values().toArray((Object[])new WaypointWorldContainer[0]);
/* 66 */     for (int i = 0; i < subContainers.length; i++)
/* 67 */       addWorlds(subContainers[i], a, keysList, nameMap); 
/*    */   }
/*    */   
/*    */   public String[] getCurrentKeys() {
/* 71 */     String fullKey = getCurrentKey();
/* 72 */     return new String[] { fullKey.substring(0, fullKey.lastIndexOf("_")), fullKey.substring(fullKey.lastIndexOf("_") + 1) };
/*    */   }
/*    */ }


/* Location:              C:\Users\ioanm\Downloads\Xaeros_Minimap_20.15.3_Forge_1.12.jar!\xaero\common\gui\GuiWaypointWorlds.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */