/*     */ package xaero.patreon;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.FileReader;
/*     */ import java.io.FileWriter;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.PrintWriter;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.entity.AbstractClientPlayer;
/*     */ import net.minecraft.entity.player.EnumPlayerModelParts;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ 
/*     */ 
/*     */ public class Patreon4
/*     */ {
/*  22 */   public static HashMap<Integer, ArrayList<String>> patrons = new HashMap<>();
/*     */   
/*     */   public static boolean loaded = false;
/*     */   public static boolean showCapes = true;
/*  26 */   public static int patronPledge = -1;
/*     */   public static String updateLocation;
/*  28 */   public static HashMap<String, PatreonMod2> mods = new HashMap<>();
/*  29 */   private static ArrayList<PatreonMod2> outdatedMods = new ArrayList<>();
/*     */   
/*     */   public static void checkPatreon() {
/*  32 */     synchronized (patrons) {
/*  33 */       if (loaded)
/*     */         return; 
/*  35 */       loadSettings();
/*  36 */       String s = "http://data.chocolateminecraft.com/Versions/Patreon.txt";
/*  37 */       s = s.replaceAll(" ", "%20");
/*     */       
/*     */       try {
/*  40 */         URL url = new URL(s);
/*     */         
/*  42 */         URLConnection conn = url.openConnection();
/*  43 */         conn.setConnectTimeout(900);
/*  44 */         BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
/*     */         
/*  46 */         int pledge = -1; String line;
/*  47 */         while ((line = reader.readLine()) != null && !line.equals("LAYOUTS")) {
/*  48 */           if (line.startsWith("PATREON")) {
/*  49 */             pledge = Integer.parseInt(line.substring(7));
/*  50 */             patrons.put(Integer.valueOf(pledge), new ArrayList<>());
/*     */             continue;
/*     */           } 
/*  53 */           if (pledge == -1)
/*     */             continue; 
/*  55 */           String[] args = line.split("\\t");
/*  56 */           ((ArrayList<String>)patrons.get(Integer.valueOf(pledge))).add(args[0]);
/*     */           
/*  58 */           if (args[0].equalsIgnoreCase(Minecraft.func_71410_x().func_110432_I().func_148256_e().getName()))
/*  59 */             patronPledge = pledge; 
/*     */         } 
/*  61 */         if (pledge >= 5) {
/*  62 */           updateLocation = reader.readLine();
/*  63 */           while ((line = reader.readLine()) != null) {
/*  64 */             String[] args = line.split("\\t");
/*  65 */             mods.put(args[0], new PatreonMod2(args[0], args[1], args[2], args[3]));
/*     */           } 
/*     */         } 
/*  68 */         reader.close();
/*  69 */         loaded = true;
/*  70 */       } catch (Exception e) {
/*  71 */         patrons.clear();
/*  72 */         mods.clear();
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void addOutdatedMod(PatreonMod2 mod) {
/*  78 */     synchronized (getOutdatedMods()) {
/*  79 */       getOutdatedMods().add(mod);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static int getPatronPledge(String name) {
/*  84 */     Integer[] keys = (Integer[])patrons.keySet().toArray((Object[])new Integer[0]);
/*  85 */     for (int i = 0; i < keys.length; i++) {
/*  86 */       if (((ArrayList)patrons.get(keys[i])).contains(name))
/*  87 */         return keys[i].intValue(); 
/*  88 */     }  return -1;
/*     */   }
/*     */   
/*  91 */   public static File optionsFile = new File("./config/xaeropatreon.txt");
/*     */   
/*     */   public static void saveSettings() {
/*     */     try {
/*  95 */       PrintWriter writer = new PrintWriter(new FileWriter(optionsFile));
/*  96 */       writer.println("showCapes:" + showCapes);
/*  97 */       writer.close();
/*  98 */     } catch (IOException e) {
/*  99 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void loadSettings() {
/*     */     try {
/* 105 */       if (!optionsFile.exists()) {
/* 106 */         saveSettings();
/*     */         
/*     */         return;
/*     */       } 
/* 110 */       BufferedReader reader = new BufferedReader(new FileReader(optionsFile));
/*     */       String line;
/* 112 */       while ((line = reader.readLine()) != null) {
/* 113 */         String[] args = line.split(":");
/* 114 */         if (args[0].equalsIgnoreCase("showCapes"))
/* 115 */           showCapes = args[1].equals("true"); 
/*     */       } 
/* 117 */       reader.close();
/* 118 */     } catch (IOException e) {
/* 119 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */   
/* 123 */   public static String rendersCapes = null;
/* 124 */   private static ResourceLocation cape1 = new ResourceLocation("xaeropatreon", "capes/cape1.png");
/* 125 */   private static ResourceLocation cape2 = new ResourceLocation("xaeropatreon", "capes/cape2.png");
/* 126 */   private static ResourceLocation cape3 = new ResourceLocation("xaeropatreon", "capes/cape3.png");
/* 127 */   private static ResourceLocation cape4 = new ResourceLocation("xaeropatreon", "capes/cape4.png"); private static boolean pauseCapes = false;
/*     */   
/*     */   public static ResourceLocation getPlayerCape(String modID, AbstractClientPlayer playerEntity) {
/* 130 */     if (!pauseCapes && showCapes && modID.equals(rendersCapes)) {
/* 131 */       ResourceLocation cape = null;
/* 132 */       int pledge = getPatronPledge(playerEntity.func_70005_c_());
/* 133 */       if (pledge == 2) {
/* 134 */         cape = cape1;
/* 135 */       } else if (pledge == 5) {
/* 136 */         cape = cape2;
/* 137 */       } else if (pledge == 10) {
/* 138 */         cape = cape3;
/* 139 */       } else if (pledge == 50) {
/* 140 */         cape = cape4;
/*     */       } 
/*     */ 
/*     */       
/* 144 */       if (cape == null)
/* 145 */         return null; 
/* 146 */       pauseCapes = true;
/* 147 */       ResourceLocation realCape = playerEntity.func_110303_q();
/* 148 */       boolean realIsWearing = playerEntity.func_175148_a(EnumPlayerModelParts.CAPE);
/* 149 */       pauseCapes = false;
/* 150 */       if (realCape != null && realIsWearing)
/* 151 */         return realCape; 
/* 152 */       return cape;
/*     */     } 
/* 154 */     return null;
/*     */   }
/*     */   
/*     */   public static Boolean isWearingCape(String modID, AbstractClientPlayer playerEntity) {
/* 158 */     if (!pauseCapes && showCapes && modID.equals(rendersCapes)) {
/* 159 */       pauseCapes = true;
/* 160 */       ResourceLocation realCape = playerEntity.func_110303_q();
/* 161 */       boolean realIsWearing = playerEntity.func_175148_a(EnumPlayerModelParts.CAPE);
/* 162 */       pauseCapes = false;
/* 163 */       if (realIsWearing || realCape == null)
/* 164 */         return Boolean.valueOf(realIsWearing); 
/* 165 */       int pledge = getPatronPledge(playerEntity.func_70005_c_());
/* 166 */       return Boolean.valueOf((pledge >= 2));
/*     */     } 
/* 168 */     return null;
/*     */   }
/*     */   
/*     */   public static ArrayList<PatreonMod2> getOutdatedMods() {
/* 172 */     return outdatedMods;
/*     */   }
/*     */ }


/* Location:              C:\Users\ioanm\Downloads\XaerosWorldMap_1.7.3_Forge_1.12.jar!\xaero\patreon\Patreon4.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */