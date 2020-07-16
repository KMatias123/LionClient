/*     */ package xaero.map.world;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.PrintWriter;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.attribute.FileAttribute;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.WorldProvider;
/*     */ import net.minecraftforge.common.DimensionManager;
/*     */ import org.apache.commons.io.FileUtils;
/*     */ import xaero.map.MapProcessor;
/*     */ import xaero.map.file.RegionDetection;
/*     */ import xaero.map.region.MapRegion;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MapDimension
/*     */ {
/*     */   private final MapWorld mapWorld;
/*     */   private final int dimId;
/*     */   private final List<String> multiworldIds;
/*     */   private final Hashtable<String, String> multiworldNames;
/*     */   private final Hashtable<String, String> autoMultiworldBindings;
/*     */   private String futureAutoMultiworldBinding;
/*     */   private String futureCustomSelectedMultiworld;
/*     */   public boolean futureMultiworldWritable;
/*     */   private String currentMultiworld;
/*     */   public boolean currentMultiworldWritable;
/*     */   private String confirmedMultiworld;
/*     */   private Hashtable<Integer, Hashtable<Integer, MapRegion>> mapRegions;
/*     */   private List<MapRegion> mapRegionsList;
/*     */   private Hashtable<Integer, Hashtable<Integer, RegionDetection>> detectedRegions;
/*     */   public final ArrayList<MapRegion> regionsToCache;
/*     */   
/*     */   public MapDimension(MapWorld mapWorld, int dimId) {
/*  54 */     this.mapWorld = mapWorld;
/*  55 */     this.dimId = dimId;
/*  56 */     this.multiworldIds = new ArrayList<>();
/*  57 */     this.multiworldNames = new Hashtable<>();
/*  58 */     this.mapRegions = new Hashtable<>();
/*  59 */     this.mapRegionsList = new ArrayList<>();
/*  60 */     this.autoMultiworldBindings = new Hashtable<>();
/*  61 */     this.regionsToCache = new ArrayList<>();
/*     */   }
/*     */   
/*     */   public String getCurrentMultiworld() {
/*  65 */     return this.currentMultiworld;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<String> getMultiworldIdsCopy() {
/*  73 */     synchronized (this.multiworldIds) {
/*  74 */       return new ArrayList<>(this.multiworldIds);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void updateFutureAutomaticUnsynced(Minecraft mc, BlockPos dimSpawn) {
/*  79 */     if (mc.func_71401_C() != null) {
/*  80 */       this.futureAutoMultiworldBinding = "";
/*  81 */     } else if (dimSpawn != null) {
/*  82 */       this.futureAutoMultiworldBinding = "mw" + (dimSpawn.func_177958_n() >> 6) + "," + (dimSpawn.func_177956_o() >> 6) + "," + (dimSpawn.func_177952_p() >> 6);
/*     */     } else {
/*  84 */       this.futureAutoMultiworldBinding = "unknown";
/*     */     } 
/*     */   }
/*     */   
/*     */   public String getFutureCustomSelectedMultiworld() {
/*  89 */     return this.futureCustomSelectedMultiworld;
/*     */   }
/*     */   
/*     */   public String getFutureMultiworldUnsynced() {
/*  93 */     if (this.futureCustomSelectedMultiworld == null) {
/*  94 */       return getFutureAutoMultiworld();
/*     */     }
/*  96 */     return this.futureCustomSelectedMultiworld;
/*     */   }
/*     */   
/*     */   public void switchToFutureUnsynced() {
/* 100 */     this.currentMultiworld = getFutureMultiworldUnsynced();
/* 101 */     addMultiworldChecked(this.currentMultiworld);
/*     */   }
/*     */   
/*     */   public void switchToFutureMultiworldWritableValueUnsynced() {
/* 105 */     this.currentMultiworldWritable = this.futureMultiworldWritable;
/*     */   }
/*     */   
/*     */   public Hashtable<Integer, Hashtable<Integer, MapRegion>> getMapRegions() {
/* 109 */     return this.mapRegions;
/*     */   }
/*     */   
/*     */   public List<MapRegion> getMapRegionsList() {
/* 113 */     return this.mapRegionsList;
/*     */   }
/*     */   
/*     */   public Hashtable<Integer, Hashtable<Integer, RegionDetection>> getDetectedRegions() {
/* 117 */     return this.detectedRegions;
/*     */   }
/*     */   
/*     */   public Hashtable<Integer, Hashtable<Integer, RegionDetection>> createDetectedRegions() {
/* 121 */     if (this.detectedRegions == null)
/* 122 */       this.detectedRegions = new Hashtable<>(); 
/* 123 */     return this.detectedRegions;
/*     */   }
/*     */   
/*     */   public void clearLists() {
/* 127 */     this.mapRegions.clear();
/* 128 */     this.mapRegionsList.clear();
/* 129 */     this.detectedRegions = null;
/*     */   }
/*     */   
/*     */   public Path getMainFolderPath() {
/* 133 */     String mainWorldId = this.mapWorld.getMainIdNoDim().replace("%DIMENSION%", MapProcessor.instance.getDimensionName(this.dimId));
/* 134 */     return MapProcessor.instance.getMapSaveLoad().getMainFolder(mainWorldId, false);
/*     */   }
/*     */   
/*     */   public void saveConfig() {
/* 138 */     Path dimensionSavePath = getMainFolderPath();
/* 139 */     PrintWriter writer = null;
/*     */     try {
/* 141 */       writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(dimensionSavePath.resolve("dimension_config.txt").toFile()), StandardCharsets.UTF_8));
/* 142 */       if (this.mapWorld.isMultiplayer()) {
/* 143 */         if (this.confirmedMultiworld != null)
/* 144 */           writer.println("confirmedMultiworld:" + this.confirmedMultiworld); 
/* 145 */         for (Map.Entry<String, String> bindingEntry : this.autoMultiworldBindings.entrySet())
/* 146 */           writer.println("autoMWBinding:" + (String)bindingEntry.getKey() + ":" + (String)bindingEntry.getValue()); 
/* 147 */         for (Map.Entry<String, String> bindingEntry : this.multiworldNames.entrySet())
/* 148 */           writer.println("MWName:" + (String)bindingEntry.getKey() + ":" + ((String)bindingEntry.getValue()).replace(":", "^col^")); 
/*     */       } 
/* 150 */     } catch (IOException e) {
/* 151 */       e.printStackTrace();
/*     */     } finally {
/* 153 */       if (writer != null)
/* 154 */         writer.close(); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void loadConfig() {
/* 159 */     Path dimensionSavePath = getMainFolderPath();
/*     */     
/* 161 */     BufferedReader reader = null;
/*     */     try {
/* 163 */       Files.createDirectories(dimensionSavePath, (FileAttribute<?>[])new FileAttribute[0]);
/* 164 */       loadMultiworldsList(dimensionSavePath);
/* 165 */       Path configFile = dimensionSavePath.resolve("dimension_config.txt");
/* 166 */       if (Files.exists(configFile, new java.nio.file.LinkOption[0])) {
/* 167 */         reader = new BufferedReader(new InputStreamReader(new FileInputStream(configFile.toFile()), "UTF8"));
/*     */         String line;
/* 169 */         while ((line = reader.readLine()) != null) {
/* 170 */           String[] args = line.split(":");
/* 171 */           if (this.mapWorld.isMultiplayer()) {
/* 172 */             if (args[0].equals("confirmedMultiworld")) {
/* 173 */               if (this.multiworldIds.contains(args[1]))
/* 174 */                 this.confirmedMultiworld = args[1];  continue;
/* 175 */             }  if (args[0].equals("autoMWBinding")) {
/* 176 */               bindAutoMultiworld(args[1], args[2]); continue;
/* 177 */             }  if (args[0].equals("MWName")) {
/* 178 */               setMultiworldName(args[1], args[2].replace("^col^", ":"));
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/* 183 */     } catch (IOException e) {
/* 184 */       e.printStackTrace();
/*     */     } finally {
/* 186 */       if (reader != null)
/*     */         try {
/* 188 */           reader.close();
/* 189 */         } catch (IOException e) {
/* 190 */           e.printStackTrace();
/*     */         }  
/*     */     } 
/*     */   }
/*     */   
/*     */   public void pickDefaultCustomMultiworldUnsynced() {
/* 196 */     if (this.multiworldIds.isEmpty()) {
/* 197 */       this.futureCustomSelectedMultiworld = "mw$default";
/* 198 */       this.multiworldIds.add(this.futureCustomSelectedMultiworld);
/* 199 */       setMultiworldName(this.futureCustomSelectedMultiworld, "Default");
/*     */     } else {
/* 201 */       int indexOfAuto = this.multiworldIds.indexOf(getFutureAutoMultiworld());
/* 202 */       this.futureCustomSelectedMultiworld = this.multiworldIds.get((indexOfAuto != -1) ? indexOfAuto : 0);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void loadMultiworldsList(Path dimensionSavePath) {
/* 207 */     MapProcessor mp = MapProcessor.instance;
/* 208 */     String mainWorldId = this.mapWorld.getMainIdNoDim();
/* 209 */     boolean isMultiplayer = mp.isWorldMultiplayer(mp.isWorldRealms(mainWorldId), mainWorldId);
/* 210 */     if (!isMultiplayer) {
/*     */       return;
/*     */     }
/*     */     try {
/* 214 */       Stream<Path> subFolders = Files.list(dimensionSavePath);
/* 215 */       Iterator<Path> iter = subFolders.iterator();
/* 216 */       while (iter.hasNext()) {
/* 217 */         Path path = iter.next();
/* 218 */         if (path.toFile().isDirectory()) {
/* 219 */           String folderName = path.getFileName().toString();
/* 220 */           boolean autoMultiworldFormat = folderName.matches("^mw(-?\\d+),(-?\\d+),(-?\\d+)$");
/* 221 */           boolean customMultiworldFormat = folderName.startsWith("mw$");
/* 222 */           if (autoMultiworldFormat || customMultiworldFormat)
/*     */           {
/* 224 */             this.multiworldIds.add(folderName);
/*     */           }
/*     */         } 
/*     */       } 
/* 228 */       subFolders.close();
/* 229 */     } catch (IOException e) {
/* 230 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void confirmMultiworldUnsynced() {
/* 235 */     if (!this.futureMultiworldWritable) {
/* 236 */       this.futureMultiworldWritable = true;
/* 237 */       if (this.mapWorld.getFutureMultiworldType() == 2 && this.futureCustomSelectedMultiworld != null)
/* 238 */         makeCustomSelectedMultiworldAuto(); 
/* 239 */       this.confirmedMultiworld = getFutureMultiworldUnsynced();
/* 240 */       saveConfig();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void makeCustomSelectedMultiworldAuto() {
/* 245 */     String currentAutoMultiworld = getFutureAutoMultiworld();
/* 246 */     boolean currentBindingFound = false;
/* 247 */     for (Map.Entry<String, String> bindingEntry : this.autoMultiworldBindings.entrySet()) {
/* 248 */       if (((String)bindingEntry.getValue()).equals(this.futureCustomSelectedMultiworld)) {
/* 249 */         bindAutoMultiworld(bindingEntry.getKey(), currentAutoMultiworld);
/* 250 */         currentBindingFound = true;
/*     */         break;
/*     */       } 
/*     */     } 
/* 254 */     if (!currentBindingFound && !this.futureCustomSelectedMultiworld.startsWith("mw$"))
/* 255 */       bindAutoMultiworld(this.futureCustomSelectedMultiworld, currentAutoMultiworld); 
/* 256 */     bindAutoMultiworld(this.futureAutoMultiworldBinding, this.futureCustomSelectedMultiworld);
/* 257 */     this.futureCustomSelectedMultiworld = null;
/* 258 */     saveConfig();
/*     */   }
/*     */   
/*     */   private void bindAutoMultiworld(String binding, String multiworld) {
/* 262 */     if (binding.equals(multiworld)) {
/* 263 */       this.autoMultiworldBindings.remove(binding);
/*     */     } else {
/* 265 */       this.autoMultiworldBindings.put(binding, multiworld);
/*     */     } 
/*     */   }
/*     */   public void resetCustomMultiworldUnsynced() {
/* 269 */     this.futureCustomSelectedMultiworld = (this.mapWorld.getFutureMultiworldType() == 2) ? null : this.confirmedMultiworld;
/* 270 */     if (this.futureCustomSelectedMultiworld == null && this.mapWorld.getFutureMultiworldType() < 2)
/* 271 */       pickDefaultCustomMultiworldUnsynced(); 
/* 272 */     this.futureMultiworldWritable = (this.mapWorld.getFutureMultiworldType() != 1 && this.mapWorld.isFutureMultiworldTypeConfirmed());
/*     */   }
/*     */   
/*     */   public void setMultiworldUnsynced(String nextMW) {
/* 276 */     String cmw = (this.futureCustomSelectedMultiworld == null) ? getFutureMultiworldUnsynced() : this.futureCustomSelectedMultiworld;
/*     */     
/* 278 */     this.futureCustomSelectedMultiworld = nextMW;
/* 279 */     this.futureMultiworldWritable = false;
/* 280 */     System.out.println(cmw + " -> " + this.futureCustomSelectedMultiworld);
/*     */   }
/*     */   
/*     */   public boolean addMultiworldChecked(String mw) {
/* 284 */     synchronized (this.multiworldIds) {
/* 285 */       if (!this.multiworldIds.contains(mw)) {
/* 286 */         this.multiworldIds.add(mw);
/* 287 */         return true;
/*     */       } 
/*     */     } 
/* 290 */     return false;
/*     */   }
/*     */   
/*     */   public String getMultiworldName(String mwId) {
/* 294 */     String tableName = this.multiworldNames.get(mwId);
/* 295 */     if (tableName == null) {
/* 296 */       return mwId;
/*     */     }
/* 298 */     return tableName;
/*     */   }
/*     */   
/*     */   public void setMultiworldName(String mwId, String mwName) {
/* 302 */     this.multiworldNames.put(mwId, mwName);
/*     */   }
/*     */   
/*     */   private String getFutureAutoMultiworld() {
/* 306 */     if (this.futureAutoMultiworldBinding == null)
/* 307 */       return null; 
/* 308 */     String boundMultiworld = this.autoMultiworldBindings.get(this.futureAutoMultiworldBinding);
/* 309 */     if (boundMultiworld == null)
/* 310 */       return this.futureAutoMultiworldBinding; 
/* 311 */     return boundMultiworld;
/*     */   }
/*     */   
/*     */   public MapWorld getMapWorld() {
/* 315 */     return this.mapWorld;
/*     */   }
/*     */   
/*     */   public void deleteMultiworldMapDataUnsynced(String mwId) {
/*     */     try {
/* 320 */       Path currentDimFolder = getMainFolderPath();
/* 321 */       Path currentMWFolder = currentDimFolder.resolve(mwId);
/* 322 */       Path binFolder = currentDimFolder.resolve("last deleted");
/* 323 */       Path binMWFolder = binFolder.resolve(mwId);
/* 324 */       Files.createDirectories(binFolder, (FileAttribute<?>[])new FileAttribute[0]);
/* 325 */       FileUtils.cleanDirectory(binFolder.toFile());
/* 326 */       Files.move(currentMWFolder, binMWFolder, new java.nio.file.CopyOption[0]);
/* 327 */     } catch (Exception e) {
/* 328 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void deleteMultiworldId(String mwId) {
/* 333 */     synchronized (this.multiworldIds) {
/* 334 */       this.multiworldIds.remove(mwId);
/* 335 */       this.multiworldNames.remove(mwId);
/* 336 */       if (mwId.equals(this.confirmedMultiworld))
/* 337 */         this.confirmedMultiworld = null; 
/*     */     } 
/*     */   }
/*     */   
/*     */   public int getDimId() {
/* 342 */     return this.dimId;
/*     */   }
/*     */   
/*     */   public boolean hasConfirmedMultiworld() {
/* 346 */     return (this.confirmedMultiworld != null);
/*     */   }
/*     */   
/*     */   public void renameLegacyFolder(World world) {
/* 350 */     Path currentFolderPath = getMainFolderPath();
/* 351 */     if (!Files.exists(currentFolderPath, new java.nio.file.LinkOption[0])) {
/*     */       String legacyFolderName;
/* 353 */       if (world != null) {
/* 354 */         legacyFolderName = MapProcessor.instance.getDimensionLegacyName(world.field_73011_w);
/*     */       } else {
/* 356 */         WorldProvider dimWorldProvider = null;
/*     */         try {
/* 358 */           dimWorldProvider = DimensionManager.createProviderFor(this.dimId);
/* 359 */           legacyFolderName = MapProcessor.instance.getDimensionLegacyName(dimWorldProvider);
/* 360 */         } catch (RuntimeException re) {
/* 361 */           System.out.println("Couldn't create world provider to get dimension folder name: " + this.dimId);
/*     */           return;
/*     */         } 
/*     */       } 
/* 365 */       String mainWorldId = this.mapWorld.getMainIdNoDim().replace("%DIMENSION%", legacyFolderName);
/* 366 */       Path legacyFolderPath = MapProcessor.instance.getMapSaveLoad().getMainFolder(mainWorldId, this.mapWorld.isMultiplayer());
/* 367 */       if (Files.exists(legacyFolderPath, new java.nio.file.LinkOption[0]))
/*     */         try {
/* 369 */           Files.move(legacyFolderPath, currentFolderPath, new java.nio.file.CopyOption[0]);
/* 370 */         } catch (IOException e) {
/* 371 */           throw new RuntimeException(e);
/*     */         }  
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\ioanm\Downloads\XaerosWorldMap_1.7.3_Forge_1.12.jar!\xaero\map\world\MapDimension.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */