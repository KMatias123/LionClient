/*    */ package xaero.common.gui;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collections;
/*    */ import xaero.common.IXaeroMinimap;
/*    */ import xaero.common.minimap.waypoints.WaypointsManager;
/*    */ 
/*    */ public class GuiWaypointContainers
/*    */   extends GuiDropdownHelper
/*    */ {
/*    */   public GuiWaypointContainers(IXaeroMinimap modMain, WaypointsManager waypointsManager, String currentContainer) {
/* 12 */     String c = currentContainer;
/* 13 */     String a = waypointsManager.getAutoContainerID().split("/")[0];
/* 14 */     this.current = -1;
/* 15 */     this.auto = -1;
/* 16 */     ArrayList<KeySortableByOther<String>> keysList = new ArrayList<>();
/* 17 */     ArrayList<String> keysStringList = new ArrayList<>();
/* 18 */     ArrayList<String> optionsList = new ArrayList<>();
/* 19 */     String[] containerKeys = (String[])waypointsManager.getWaypointMap().keySet().toArray((Object[])new String[0]); int i;
/* 20 */     for (i = 0; i < containerKeys.length; i++) {
/* 21 */       String containerKey = containerKeys[i];
/* 22 */       String sortName = containerKey;
/* 23 */       String[] details = containerKey.split("_");
/* 24 */       if (details.length > 1 && details[0].equals("Realms")) {
/* 25 */         sortName = "Realm ID " + details[1].substring(details[1].indexOf(".") + 1);
/*    */       } else {
/* 27 */         sortName = details[details.length - 1].replace("%us%", "_").replace("%fs%", "/").replace("%bs%", "\\");
/* 28 */       }  if ((modMain.getSettings()).hideWorldNames == 1 && details.length > 1 && details[0].equals("Multiplayer")) {
/* 29 */         String[] dotSplit = sortName.split("\\.");
/* 30 */         StringBuilder builder = new StringBuilder();
/* 31 */         for (int o = 0; o < dotSplit.length; o++) {
/* 32 */           if (o < dotSplit.length - 2)
/* 33 */           { builder.append("-."); }
/* 34 */           else if (o < dotSplit.length - 1)
/* 35 */           { builder.append(dotSplit[o].charAt(0) + "-."); }
/*    */           else
/* 37 */           { builder.append(dotSplit[o]); } 
/* 38 */         }  sortName = builder.toString();
/*    */       } 
/* 40 */       keysList.add(new KeySortableByOther<>(containerKey, new Comparable[] { Integer.valueOf(containerKey.startsWith("Multiplayer_") ? 1 : (containerKey.startsWith("Realms_") ? 2 : 0)), sortName.toLowerCase(), sortName }));
/*    */     } 
/* 42 */     Collections.sort(keysList);
/* 43 */     for (i = 0; i < keysList.size(); i++) {
/* 44 */       KeySortableByOther<String> k = keysList.get(i);
/* 45 */       String containerKey = k.getKey();
/* 46 */       String option = "Error";
/*    */       try {
/* 48 */         if (this.current == -1 && containerKey.equals(c))
/* 49 */           this.current = i; 
/* 50 */         if (this.auto == -1 && containerKey.equals(a))
/* 51 */           this.auto = i; 
/* 52 */         option = (String)k.getDataToSortBy()[2];
/* 53 */         if ((modMain.getSettings()).hideWorldNames == 2)
/* 54 */           option = "hidden " + optionsList.size(); 
/* 55 */         if (this.auto == optionsList.size())
/* 56 */           option = option + " (auto)"; 
/* 57 */       } catch (Exception e) {
/* 58 */         e.printStackTrace();
/*    */       } 
/* 60 */       keysStringList.add(containerKey);
/* 61 */       optionsList.add(option);
/*    */     } 
/* 63 */     this.keys = keysStringList.<String>toArray(new String[0]);
/* 64 */     this.options = optionsList.<String>toArray(new String[0]);
/*    */   }
/*    */ }


/* Location:              C:\Users\ioanm\Downloads\Xaeros_Minimap_20.15.3_Forge_1.12.jar!\xaero\common\gui\GuiWaypointContainers.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */