/*     */ package xaero.map.world;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.FileReader;
/*     */ import java.io.FileWriter;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintWriter;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.attribute.FileAttribute;
/*     */ import java.util.Hashtable;
/*     */ import net.minecraft.world.World;
/*     */ import xaero.map.MapProcessor;
/*     */ 
/*     */ 
/*     */ public class MapWorld
/*     */ {
/*     */   private boolean isMultiplayer;
/*     */   private String mainIdNoDim;
/*     */   private Hashtable<Integer, MapDimension> dimensions;
/*     */   private int currentDimensionId;
/*     */   private int futureDimensionId;
/*     */   private int futureMultiworldType;
/*     */   private int currentMultiworldType;
/*     */   private boolean futureMultiworldTypeConfirmed = true;
/*     */   private boolean currentMultiworldTypeConfirmed = false;
/*     */   
/*     */   public MapWorld(String mainIdNoDim) {
/*  29 */     this.mainIdNoDim = mainIdNoDim;
/*  30 */     this.isMultiplayer = MapProcessor.instance.isWorldMultiplayer(MapProcessor.instance.isWorldRealms(mainIdNoDim), mainIdNoDim);
/*  31 */     this.dimensions = new Hashtable<>();
/*  32 */     this.futureMultiworldType = this.currentMultiworldType = this.isMultiplayer ? 1 : 0;
/*     */   }
/*     */   
/*     */   public MapDimension getDimension(int dimId) {
/*  36 */     synchronized (this.dimensions) {
/*  37 */       return this.dimensions.get(Integer.valueOf(dimId));
/*     */     } 
/*     */   }
/*     */   
/*     */   public MapDimension createDimension(World world, int dimId) {
/*  42 */     synchronized (this.dimensions) {
/*  43 */       MapDimension result = this.dimensions.get(Integer.valueOf(dimId));
/*  44 */       if (result == null) {
/*  45 */         this.dimensions.put(Integer.valueOf(dimId), result = new MapDimension(this, dimId));
/*  46 */         result.renameLegacyFolder(world);
/*     */         
/*  48 */         result.loadConfig();
/*     */       } 
/*  50 */       return result;
/*     */     } 
/*     */   }
/*     */   
/*     */   public String getMainIdNoDim() {
/*  55 */     return this.mainIdNoDim;
/*     */   }
/*     */   
/*     */   public String getCurrentMultiworld() {
/*  59 */     MapDimension container = getDimension(this.currentDimensionId);
/*  60 */     return container.getCurrentMultiworld();
/*     */   }
/*     */   
/*     */   public String getFutureMultiworldUnsynced() {
/*  64 */     MapDimension container = getDimension(this.futureDimensionId);
/*  65 */     return container.getFutureMultiworldUnsynced();
/*     */   }
/*     */   
/*     */   public MapDimension getCurrentDimension() {
/*  69 */     return getDimension(this.currentDimensionId);
/*     */   }
/*     */   
/*     */   public MapDimension getFutureDimension() {
/*  73 */     return getDimension(this.futureDimensionId);
/*     */   }
/*     */   
/*     */   public int getCurrentDimensionId() {
/*  77 */     return this.currentDimensionId;
/*     */   }
/*     */   
/*     */   public int getFutureDimensionId() {
/*  81 */     return this.futureDimensionId;
/*     */   }
/*     */   
/*     */   public void setFutureDimensionId(int dimension) {
/*  85 */     this.futureDimensionId = dimension;
/*     */   }
/*     */   
/*     */   public void switchToFutureUnsynced() {
/*  89 */     this.currentDimensionId = this.futureDimensionId;
/*  90 */     getDimension(this.currentDimensionId).switchToFutureUnsynced();
/*     */   }
/*     */   
/*     */   public Hashtable<Integer, MapDimension> getDimensions() {
/*  94 */     return this.dimensions;
/*     */   }
/*     */   
/*     */   public int getCurrentMultiworldType() {
/*  98 */     return this.currentMultiworldType;
/*     */   }
/*     */   
/*     */   public boolean isMultiplayer() {
/* 102 */     return this.isMultiplayer;
/*     */   }
/*     */   
/*     */   public boolean isCurrentMultiworldTypeConfirmed() {
/* 106 */     return this.currentMultiworldTypeConfirmed;
/*     */   }
/*     */   
/*     */   public int getFutureMultiworldType() {
/* 110 */     return this.futureMultiworldType;
/*     */   }
/*     */   
/*     */   public void toggleMultiworldTypeUnsynced() {
/* 114 */     unconfirmMultiworldTypeUnsynced();
/* 115 */     this.futureMultiworldType = (this.futureMultiworldType + 1) % 3;
/* 116 */     getCurrentDimension().resetCustomMultiworldUnsynced();
/* 117 */     saveConfig();
/*     */   }
/*     */   
/*     */   public void unconfirmMultiworldTypeUnsynced() {
/* 121 */     this.futureMultiworldTypeConfirmed = false;
/*     */   }
/*     */   
/*     */   public void confirmMultiworldTypeUnsynced() {
/* 125 */     this.futureMultiworldTypeConfirmed = true;
/*     */   }
/*     */   
/*     */   public boolean isFutureMultiworldTypeConfirmed() {
/* 129 */     return this.futureMultiworldTypeConfirmed;
/*     */   }
/*     */   
/*     */   public void switchToFutureMultiworldTypeUnsynced() {
/* 133 */     this.currentMultiworldType = this.futureMultiworldType;
/* 134 */     this.currentMultiworldTypeConfirmed = this.futureMultiworldTypeConfirmed;
/*     */   }
/*     */   
/*     */   public void loadConfig() {
/* 138 */     MapProcessor mp = MapProcessor.instance;
/* 139 */     String mainWorldId = this.mainIdNoDim.replace("%DIMENSION%", "null");
/* 140 */     Path overworldSavePath = mp.getMapSaveLoad().getMainFolder(mainWorldId, false);
/* 141 */     BufferedReader reader = null;
/*     */     try {
/* 143 */       Files.createDirectories(overworldSavePath, (FileAttribute<?>[])new FileAttribute[0]);
/* 144 */       Path configFile = overworldSavePath.resolve("server_config.txt");
/* 145 */       if (Files.exists(configFile, new java.nio.file.LinkOption[0])) {
/* 146 */         reader = new BufferedReader(new FileReader(configFile.toFile()));
/*     */         String line;
/* 148 */         while ((line = reader.readLine()) != null) {
/* 149 */           String[] args = line.split(":");
/* 150 */           if (args[0].equals("multiworldType"))
/* 151 */             this.futureMultiworldType = Integer.parseInt(args[1]); 
/*     */         } 
/*     */       } 
/* 154 */     } catch (IOException e) {
/* 155 */       e.printStackTrace();
/*     */     } finally {
/* 157 */       if (reader != null)
/*     */         try {
/* 159 */           reader.close();
/* 160 */         } catch (IOException e) {
/* 161 */           e.printStackTrace();
/*     */         }  
/*     */     } 
/*     */   }
/*     */   
/*     */   public void saveConfig() {
/* 167 */     MapProcessor mp = MapProcessor.instance;
/* 168 */     String mainWorldId = this.mainIdNoDim.replace("%DIMENSION%", "null");
/* 169 */     Path overworldSavePath = mp.getMapSaveLoad().getMainFolder(mainWorldId, false);
/* 170 */     PrintWriter writer = null;
/*     */     try {
/* 172 */       writer = new PrintWriter(new FileWriter(overworldSavePath.resolve("server_config.txt").toFile()));
/* 173 */       writer.println("multiworldType:" + this.futureMultiworldType);
/* 174 */     } catch (IOException e) {
/* 175 */       e.printStackTrace();
/*     */     } finally {
/* 177 */       if (writer != null)
/* 178 */         writer.close(); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\ioanm\Downloads\XaerosWorldMap_1.7.3_Forge_1.12.jar!\xaero\map\world\MapWorld.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */