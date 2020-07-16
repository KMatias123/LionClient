/*     */ package xaero.common.minimap.waypoints;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.FileReader;
/*     */ import java.io.FileWriter;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintWriter;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.attribute.FileAttribute;
/*     */ import xaero.common.IXaeroMinimap;
/*     */ 
/*     */ public class WaypointWorldRootContainer
/*     */   extends WaypointWorldContainer {
/*     */   public boolean configLoaded = false;
/*     */   private boolean usingMultiworldDetection = true;
/*     */   private String defaultMultiworldId;
/*     */   private boolean teleportationEnabled = true;
/*     */   private boolean usingDefaultTeleportCommand = true;
/*     */   private String teleportCommand;
/*  23 */   private WaypointsSort sortType = WaypointsSort.NONE;
/*     */   private boolean sortReversed;
/*     */   
/*     */   public WaypointWorldRootContainer(IXaeroMinimap modMain, String key) {
/*  27 */     super(modMain, key);
/*     */   }
/*     */   
/*     */   private File getConfigFile() {
/*  31 */     Path directoryPath = getDirectory().toPath();
/*     */     try {
/*  33 */       Files.createDirectories(directoryPath, (FileAttribute<?>[])new FileAttribute[0]);
/*  34 */     } catch (IOException e) {
/*  35 */       e.printStackTrace();
/*     */     } 
/*  37 */     return directoryPath.resolve("config.txt").toFile();
/*     */   }
/*     */   
/*     */   public void saveConfig() {
/*  41 */     File configFile = getConfigFile();
/*  42 */     PrintWriter writer = null;
/*     */     try {
/*  44 */       writer = new PrintWriter(new FileWriter(configFile));
/*  45 */       writer.println("usingMultiworldDetection:" + this.usingMultiworldDetection);
/*  46 */       if (this.defaultMultiworldId != null)
/*  47 */         writer.println("defaultMultiworldId:" + this.defaultMultiworldId); 
/*  48 */       writer.println("teleportationEnabled:" + this.teleportationEnabled);
/*  49 */       writer.println("usingDefaultTeleportCommand:" + this.usingDefaultTeleportCommand);
/*  50 */       if (this.teleportCommand != null)
/*  51 */         writer.println("teleportCommand:" + this.teleportCommand.replace(":", "^col^")); 
/*  52 */       writer.println("sortType:" + this.sortType.name());
/*  53 */       writer.println("sortReversed:" + this.sortReversed);
/*  54 */     } catch (IOException e) {
/*  55 */       e.printStackTrace();
/*     */     } 
/*  57 */     if (writer != null)
/*  58 */       writer.close(); 
/*     */   }
/*     */   
/*     */   public void loadConfig() {
/*  62 */     this.configLoaded = true;
/*  63 */     File configFile = getConfigFile();
/*  64 */     if (!configFile.exists())
/*     */       return; 
/*  66 */     BufferedReader reader = null;
/*     */     try {
/*  68 */       reader = new BufferedReader(new FileReader(configFile));
/*     */       String line;
/*  70 */       while ((line = reader.readLine()) != null) {
/*  71 */         String[] args = line.split(":");
/*  72 */         if (args[0].equals("usingMultiworldDetection")) {
/*  73 */           this.usingMultiworldDetection = args[1].equals("true"); continue;
/*  74 */         }  if (args[0].equals("defaultMultiworldId")) {
/*  75 */           this.defaultMultiworldId = args[1]; continue;
/*  76 */         }  if (args[0].equals("teleportationEnabled")) {
/*  77 */           this.teleportationEnabled = args[1].equals("true"); continue;
/*  78 */         }  if (args[0].equals("usingDefaultTeleportCommand")) {
/*  79 */           this.usingDefaultTeleportCommand = args[1].equals("true"); continue;
/*  80 */         }  if (args[0].equals("teleportCommand")) {
/*  81 */           this.teleportCommand = args[1].replace("^col^", ":"); continue;
/*  82 */         }  if (args[0].equals("sortType")) {
/*  83 */           this.sortType = WaypointsSort.valueOf(args[1]); continue;
/*  84 */         }  if (args[0].equals("sortReversed"))
/*  85 */           this.sortReversed = args[1].equals("true"); 
/*     */       } 
/*  87 */     } catch (FileNotFoundException e) {
/*  88 */       e.printStackTrace();
/*  89 */     } catch (IOException e) {
/*  90 */       e.printStackTrace();
/*     */     } 
/*  92 */     if (reader != null)
/*     */       try {
/*  94 */         reader.close();
/*  95 */       } catch (IOException e) {
/*  96 */         e.printStackTrace();
/*     */       }  
/*     */   }
/*     */   
/*     */   public boolean isUsingMultiworldDetection() {
/* 101 */     return this.usingMultiworldDetection;
/*     */   }
/*     */   
/*     */   public void setUsingMultiworldDetection(boolean usingMultiworldDetection) {
/* 105 */     this.usingMultiworldDetection = usingMultiworldDetection;
/*     */   }
/*     */   
/*     */   public String getDefaultMultiworldId() {
/* 109 */     return this.defaultMultiworldId;
/*     */   }
/*     */   
/*     */   public void setDefaultMultiworldId(String defaultMultiworldId) {
/* 113 */     this.defaultMultiworldId = defaultMultiworldId;
/*     */   }
/*     */   
/*     */   public boolean isTeleportationEnabled() {
/* 117 */     return this.teleportationEnabled;
/*     */   }
/*     */   
/*     */   public void setTeleportationEnabled(boolean teleportation) {
/* 121 */     this.teleportationEnabled = teleportation;
/*     */   }
/*     */   
/*     */   public boolean isUsingDefaultTeleportCommand() {
/* 125 */     return this.usingDefaultTeleportCommand;
/*     */   }
/*     */   
/*     */   public void setUsingDefaultTeleportCommand(boolean usingDefaultTeleportCommand) {
/* 129 */     this.usingDefaultTeleportCommand = usingDefaultTeleportCommand;
/*     */   }
/*     */   
/*     */   public String getTeleportCommand() {
/* 133 */     return this.teleportCommand;
/*     */   }
/*     */   
/*     */   public void setTeleportCommand(String teleportCommand) {
/* 137 */     this.teleportCommand = teleportCommand;
/*     */   }
/*     */   
/*     */   public WaypointsSort getSortType() {
/* 141 */     return this.sortType;
/*     */   }
/*     */   
/*     */   public void toggleSortType() {
/* 145 */     this.sortType = WaypointsSort.values()[(this.sortType.ordinal() + 1) % (WaypointsSort.values()).length];
/*     */   }
/*     */   
/*     */   public boolean isSortReversed() {
/* 149 */     return this.sortReversed;
/*     */   }
/*     */   
/*     */   public void toggleSortReversed() {
/* 153 */     this.sortReversed = !this.sortReversed;
/*     */   }
/*     */ }


/* Location:              C:\Users\ioanm\Downloads\Xaeros_Minimap_20.15.3_Forge_1.12.jar!\xaero\common\minimap\waypoints\WaypointWorldRootContainer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */