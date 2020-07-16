/*     */ package xaero.common.minimap.waypoints;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.Files;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import net.minecraft.world.DimensionType;
/*     */ import net.minecraftforge.common.DimensionManager;
/*     */ import xaero.common.IXaeroMinimap;
/*     */ import xaero.common.file.SimpleBackup;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class WaypointWorldContainer
/*     */ {
/*     */   private IXaeroMinimap modMain;
/*     */   private String key;
/*     */   public HashMap<String, WaypointWorldContainer> subContainers;
/*     */   public HashMap<String, WaypointWorld> worlds;
/*     */   private HashMap<String, String> multiworldNames;
/*     */   
/*     */   public WaypointWorldContainer(IXaeroMinimap modMain, String key) {
/*  31 */     this.modMain = modMain;
/*  32 */     this.key = key;
/*  33 */     this.worlds = new HashMap<>();
/*  34 */     this.subContainers = new HashMap<>();
/*  35 */     this.multiworldNames = new HashMap<>();
/*     */   }
/*     */   
/*     */   public void setKey(String key) {
/*  39 */     this.key = key;
/*  40 */     for (WaypointWorldContainer s : this.subContainers.values()) {
/*  41 */       String[] subKeySplit = s.getKey().split("/");
/*  42 */       s.setKey(key + "/" + subKeySplit[subKeySplit.length - 1]);
/*     */     } 
/*     */   }
/*     */   
/*     */   public WaypointWorldContainer addSubContainer(String subID) {
/*  47 */     WaypointWorldContainer c = this.subContainers.get(subID);
/*  48 */     if (c == null)
/*  49 */       this.subContainers.put(subID, c = new WaypointWorldContainer(this.modMain, this.key + "/" + subID)); 
/*  50 */     return c;
/*     */   }
/*     */   
/*     */   public boolean containsSub(String subId) {
/*  54 */     return this.subContainers.containsKey(subId);
/*     */   }
/*     */   
/*     */   public void deleteSubContainer(String subId) {
/*  58 */     this.subContainers.remove(subId);
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/*  62 */     return (this.subContainers.isEmpty() && this.worlds.isEmpty());
/*     */   }
/*     */   
/*     */   public WaypointWorld addWorld(String multiworldId) {
/*  66 */     WaypointWorld world = this.worlds.get(multiworldId);
/*  67 */     if (world == null) {
/*  68 */       WaypointWorld defaultWorld = this.worlds.get("waypoints");
/*  69 */       if (defaultWorld == null) {
/*  70 */         world = new WaypointWorld(this, multiworldId);
/*  71 */         this.worlds.put(multiworldId, world);
/*     */       } else {
/*  73 */         this.worlds.put(multiworldId, defaultWorld);
/*     */         try {
/*  75 */           File defaultFile = this.modMain.getSettings().getWaypointsFile(defaultWorld);
/*  76 */           defaultWorld.setId(multiworldId);
/*  77 */           File fixedFile = this.modMain.getSettings().getWaypointsFile(defaultWorld);
/*  78 */           Files.move(defaultFile.toPath(), fixedFile.toPath(), new java.nio.file.CopyOption[0]);
/*  79 */         } catch (IOException e) {
/*  80 */           e.printStackTrace();
/*     */         } 
/*  82 */         this.worlds.remove("waypoints");
/*  83 */         world = defaultWorld;
/*     */       } 
/*     */     } 
/*  86 */     return world;
/*     */   }
/*     */   
/*     */   public void addName(String id, String name) {
/*  90 */     String current = this.multiworldNames.get(id);
/*  91 */     if (current != null && !current.equals(name))
/*  92 */       ((WaypointWorld)this.worlds.get(id)).requestRemovalOnSave(current); 
/*  93 */     this.multiworldNames.put(id, name);
/*     */   }
/*     */   
/*     */   public String getName(String id) {
/*  97 */     if (id.equals("waypoints"))
/*  98 */       return null; 
/*  99 */     String name = this.multiworldNames.get(id);
/* 100 */     if (name == null) {
/* 101 */       int numericName = this.multiworldNames.size() + 1;
/*     */       while (true) {
/* 103 */         name = "" + numericName++;
/* 104 */         if (!this.multiworldNames.containsValue(name))
/* 105 */         { addName(id, name); break; } 
/*     */       } 
/* 107 */     }  return name;
/*     */   }
/*     */   
/*     */   public void removeName(String id) {
/* 111 */     this.multiworldNames.remove(id);
/*     */   }
/*     */   
/*     */   public String getSubId() {
/* 115 */     return this.key.contains("/") ? this.key.substring(this.key.lastIndexOf("/") + 1) : "";
/*     */   }
/*     */   
/*     */   public String getSubName() {
/* 119 */     String subName = getSubId();
/* 120 */     if (subName.startsWith("dim%")) {
/* 121 */       int dimId = Integer.parseInt(subName.substring(4));
/* 122 */       subName = "Dim. " + dimId;
/* 123 */       if (getRootContainer().getKey().equals(this.modMain.getWaypointsManager().getAutoRootContainerID())) {
/*     */         DimensionType dimType;
/*     */         try {
/* 126 */           dimType = DimensionManager.getProviderType(dimId);
/* 127 */         } catch (IllegalArgumentException iae) {
/* 128 */           dimType = null;
/*     */         } 
/* 130 */         if (dimType != null)
/* 131 */           subName = dimType.func_186065_b(); 
/*     */       } 
/*     */     } 
/* 134 */     return subName;
/*     */   }
/*     */   
/*     */   public String getFullName(String id, String containerName) {
/* 138 */     String name = getName(id);
/* 139 */     String subID = getSubId();
/* 140 */     if (subID.startsWith("dim%")) {
/* 141 */       int dimId = Integer.parseInt(subID.substring(4));
/* 142 */       if (this.modMain.getSupportMods().worldmap() && getRootContainer().getKey().equals(this.modMain.getWaypointsManager().getAutoRootContainerID())) {
/* 143 */         String worldMapMWName = (this.modMain.getSupportMods()).worldmapSupport.getMultiworldName(dimId, id);
/* 144 */         if (worldMapMWName != null && !worldMapMWName.equals(id))
/* 145 */           name = worldMapMWName; 
/*     */       } 
/*     */     } 
/* 148 */     if (name == null || (this.worlds.size() < 2 && !containerName.isEmpty()))
/* 149 */       return containerName; 
/* 150 */     return (containerName.length() > 0) ? (name + " - " + containerName) : name;
/*     */   }
/*     */   
/*     */   public String getKey() {
/* 154 */     return this.key;
/*     */   }
/*     */   
/*     */   public WaypointWorld getFirstWorld() {
/* 158 */     if (!this.worlds.isEmpty())
/* 159 */       return ((WaypointWorld[])this.worlds.values().toArray((T[])new WaypointWorld[0]))[0]; 
/* 160 */     WaypointWorldContainer[] subs = (WaypointWorldContainer[])this.subContainers.values().toArray((Object[])new WaypointWorldContainer[0]);
/* 161 */     for (int i = 0; i < subs.length; i++) {
/* 162 */       WaypointWorld subFirst = subs[i].getFirstWorld();
/* 163 */       if (subFirst != null)
/* 164 */         return subFirst; 
/*     */     } 
/* 166 */     return null;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 170 */     return this.key + " sc:" + this.subContainers.size() + " w:" + this.worlds.size();
/*     */   }
/*     */   
/*     */   public ArrayList<WaypointWorld> getAllWorlds() {
/* 174 */     ArrayList<WaypointWorld> allWorlds = new ArrayList<>(this.worlds.values());
/* 175 */     WaypointWorldContainer[] subs = (WaypointWorldContainer[])this.subContainers.values().toArray((Object[])new WaypointWorldContainer[0]);
/* 176 */     for (int i = 0; i < subs.length; i++)
/* 177 */       allWorlds.addAll(subs[i].getAllWorlds()); 
/* 178 */     return allWorlds;
/*     */   }
/*     */   
/*     */   public String getEqualIgnoreCaseSub(String cId) {
/* 182 */     if (cId.equalsIgnoreCase(this.key))
/* 183 */       return this.key; 
/* 184 */     if (this.subContainers.isEmpty())
/* 185 */       return null; 
/* 186 */     Set<Map.Entry<String, WaypointWorldContainer>> entries = this.subContainers.entrySet();
/* 187 */     for (Map.Entry<String, WaypointWorldContainer> entry : entries) {
/* 188 */       String subSearch = ((WaypointWorldContainer)entry.getValue()).getEqualIgnoreCaseSub(cId);
/* 189 */       if (subSearch != null) {
/* 190 */         return subSearch;
/*     */       }
/*     */     } 
/* 193 */     String[] keyArgs = this.key.split("/");
/* 194 */     String[] otherKeyArgs = cId.split("/");
/* 195 */     if (keyArgs.length >= otherKeyArgs.length)
/* 196 */       return null; 
/* 197 */     for (int i = 0; i < keyArgs.length; i++) {
/* 198 */       if (!otherKeyArgs[i].equalsIgnoreCase(keyArgs[i]))
/* 199 */         return null; 
/* 200 */       otherKeyArgs[i] = keyArgs[i];
/*     */     } 
/* 202 */     return String.join("/", (CharSequence[])otherKeyArgs);
/*     */   }
/*     */   
/*     */   public WaypointWorldRootContainer getRootContainer() {
/* 206 */     if (!this.key.contains("/"))
/* 207 */       return (WaypointWorldRootContainer)this; 
/* 208 */     return (WaypointWorldRootContainer)this.modMain.getWaypointsManager().getWorldContainer(this.key.substring(0, this.key.indexOf("/")));
/*     */   }
/*     */   
/*     */   public void renameOldContainer(String containerID) {
/* 212 */     if (this.subContainers.isEmpty())
/*     */       return; 
/* 214 */     String dimensionPart = containerID.split("/")[1];
/* 215 */     if (this.subContainers.containsKey(dimensionPart))
/*     */       return; 
/* 217 */     Integer dimId = this.modMain.getWaypointsManager().getDimensionForDirectoryName(dimensionPart);
/* 218 */     if (dimId == null)
/*     */       return; 
/* 220 */     DimensionType dt = null;
/*     */     try {
/* 222 */       dt = DimensionManager.getProviderType(dimId.intValue());
/* 223 */     } catch (IllegalArgumentException illegalArgumentException) {}
/* 224 */     if (dt == null) {
/*     */       return;
/*     */     }
/* 227 */     String currentCustomID = this.modMain.getWaypointsManager().getCustomContainerID();
/* 228 */     WaypointWorldContainer currentCustomContainer = (currentCustomID == null) ? null : this.modMain.getWaypointsManager().getWorldContainer(currentCustomID);
/* 229 */     for (Map.Entry<String, WaypointWorldContainer> subContainerEntry : this.subContainers.entrySet()) {
/* 230 */       String subKey = subContainerEntry.getKey();
/* 231 */       if (subKey.equals(dt.func_186065_b().replaceAll("[^a-zA-Z0-9_]+", ""))) {
/* 232 */         WaypointWorldContainer subContainer = subContainerEntry.getValue();
/* 233 */         boolean currentlySelected = (currentCustomContainer == subContainer);
/* 234 */         this.subContainers.put(dimensionPart, subContainer);
/* 235 */         this.subContainers.remove(subKey);
/* 236 */         SimpleBackup.moveToBackup(subContainer.getDirectory().toPath());
/* 237 */         subContainer.setKey(this.key + "/" + dimensionPart);
/* 238 */         if (currentlySelected) {
/* 239 */           this.modMain.getWaypointsManager().setCustomContainerID(subContainer.getKey());
/* 240 */           this.modMain.getWaypointsManager().updateWaypoints();
/*     */         } 
/*     */         try {
/* 243 */           this.modMain.getSettings().saveWorlds(getAllWorlds());
/* 244 */         } catch (IOException e) {
/* 245 */           throw new RuntimeException("Failed to rename a dimension! Can't continue.", e);
/*     */         } 
/*     */         return;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public File getDirectory() {
/* 253 */     return new File(this.modMain.getWaypointsFolder(), this.key);
/*     */   }
/*     */ }


/* Location:              C:\Users\ioanm\Downloads\Xaeros_Minimap_20.15.3_Forge_1.12.jar!\xaero\common\minimap\waypoints\WaypointWorldContainer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */