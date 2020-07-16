/*     */ package xaero.map.settings;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.FileReader;
/*     */ import java.io.FileWriter;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintWriter;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.resources.I18n;
/*     */ import xaero.map.MapProcessor;
/*     */ import xaero.map.WorldMap;
/*     */ 
/*     */ 
/*     */ public class ModSettings
/*     */ {
/*     */   public static int ignoreUpdate;
/*     */   public static final String format = "§";
/*     */   public static boolean updateNotification = true;
/*     */   public boolean debug = false;
/*     */   public boolean detailed_debug = false;
/*     */   public boolean lighting = true;
/*     */   public boolean loadChunks = true;
/*     */   public boolean updateChunks = true;
/*     */   public boolean terrainSlopes = true;
/*     */   public boolean terrainDepth = true;
/*     */   public boolean footsteps = true;
/*     */   public boolean flowers = true;
/*     */   public boolean compression = false;
/*     */   public boolean coordinates = true;
/*  30 */   public int colours = 0;
/*  31 */   public String[] colourNames = new String[] { "gui.xaero_accurate", "gui.xaero_vanilla" };
/*     */   
/*     */   public boolean biomeColorsVanillaMode = false;
/*     */   public boolean differentiateByServerAddress = true;
/*     */   public boolean waypoints = true;
/*     */   public boolean renderArrow = true;
/*     */   public boolean displayZoom = true;
/*     */   
/*     */   public void saveSettings() throws IOException {
/*  40 */     PrintWriter writer = new PrintWriter(new FileWriter(WorldMap.optionsFile));
/*  41 */     writer.println("ignoreUpdate:" + ignoreUpdate);
/*  42 */     writer.println("updateNotification:" + updateNotification);
/*  43 */     writer.println("differentiateByServerAddress:" + this.differentiateByServerAddress);
/*  44 */     writer.println("debug:" + this.debug);
/*  45 */     writer.println("lighting:" + this.lighting);
/*  46 */     writer.println("colours:" + this.colours);
/*  47 */     writer.println("loadChunks:" + this.loadChunks);
/*  48 */     writer.println("updateChunks:" + this.updateChunks);
/*  49 */     writer.println("terrainSlopes:" + this.terrainSlopes);
/*  50 */     writer.println("terrainDepth:" + this.terrainDepth);
/*  51 */     writer.println("footsteps:" + this.footsteps);
/*     */     
/*  53 */     writer.println("flowers:" + this.flowers);
/*  54 */     writer.println("compression:" + this.compression);
/*  55 */     writer.println("coordinates:" + this.coordinates);
/*  56 */     writer.println("biomeColorsVanillaMode:" + this.biomeColorsVanillaMode);
/*  57 */     writer.println("waypoints:" + this.waypoints);
/*  58 */     writer.println("renderArrow:" + this.renderArrow);
/*  59 */     writer.println("displayZoom:" + this.displayZoom);
/*  60 */     writer.println("globalVersion:" + WorldMap.globalVersion);
/*  61 */     writer.close();
/*     */   }
/*     */ 
/*     */   
/*     */   public void loadSettings() throws IOException {
/*  66 */     if (!WorldMap.optionsFile.exists()) {
/*  67 */       saveSettings();
/*     */       return;
/*     */     } 
/*  70 */     BufferedReader reader = new BufferedReader(new FileReader(WorldMap.optionsFile));
/*     */     String s;
/*  72 */     while ((s = reader.readLine()) != null) {
/*  73 */       String[] args = s.split(":");
/*     */       try {
/*  75 */         if (args[0].equalsIgnoreCase("ignoreUpdate")) {
/*  76 */           ignoreUpdate = Integer.parseInt(args[1]); continue;
/*  77 */         }  if (args[0].equalsIgnoreCase("updateNotification")) {
/*  78 */           updateNotification = args[1].equals("true"); continue;
/*  79 */         }  if (args[0].equalsIgnoreCase("differentiateByServerAddress")) {
/*  80 */           this.differentiateByServerAddress = args[1].equals("true"); continue;
/*  81 */         }  if (args[0].equalsIgnoreCase("debug")) {
/*  82 */           this.debug = args[1].equals("true"); continue;
/*  83 */         }  if (args[0].equalsIgnoreCase("lighting")) {
/*  84 */           this.lighting = args[1].equals("true"); continue;
/*  85 */         }  if (args[0].equalsIgnoreCase("colours")) {
/*  86 */           this.colours = Integer.parseInt(args[1]); continue;
/*  87 */         }  if (args[0].equalsIgnoreCase("loadChunks")) {
/*  88 */           this.loadChunks = args[1].equals("true"); continue;
/*  89 */         }  if (args[0].equalsIgnoreCase("updateChunks")) {
/*  90 */           this.updateChunks = args[1].equals("true"); continue;
/*  91 */         }  if (args[0].equalsIgnoreCase("terrainSlopes")) {
/*  92 */           this.terrainSlopes = args[1].equals("true"); continue;
/*  93 */         }  if (args[0].equalsIgnoreCase("terrainDepth")) {
/*  94 */           this.terrainDepth = args[1].equals("true"); continue;
/*  95 */         }  if (args[0].equalsIgnoreCase("footsteps")) {
/*  96 */           this.footsteps = args[1].equals("true");
/*     */           continue;
/*     */         } 
/*  99 */         if (args[0].equalsIgnoreCase("flowers")) {
/* 100 */           this.flowers = args[1].equals("true"); continue;
/* 101 */         }  if (args[0].equalsIgnoreCase("compression")) {
/* 102 */           this.compression = args[1].equals("true"); continue;
/* 103 */         }  if (args[0].equalsIgnoreCase("coordinates")) {
/* 104 */           this.coordinates = args[1].equals("true"); continue;
/* 105 */         }  if (args[0].equalsIgnoreCase("biomeColorsVanillaMode")) {
/* 106 */           this.biomeColorsVanillaMode = args[1].equals("true"); continue;
/* 107 */         }  if (args[0].equalsIgnoreCase("waypoints")) {
/* 108 */           this.waypoints = args[1].equals("true"); continue;
/* 109 */         }  if (args[0].equalsIgnoreCase("renderArrow")) {
/* 110 */           this.renderArrow = args[1].equals("true"); continue;
/* 111 */         }  if (args[0].equalsIgnoreCase("displayZoom")) {
/* 112 */           this.displayZoom = args[1].equals("true"); continue;
/* 113 */         }  if (args[0].equalsIgnoreCase("globalVersion"))
/* 114 */           WorldMap.globalVersion = Integer.parseInt(args[1]); 
/* 115 */       } catch (Exception e) {
/* 116 */         System.out.println("Skipping setting:" + args[0]);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String getKeyBinding(ModOptions par1EnumOptions) {
/* 123 */     String s = par1EnumOptions.getEnumString() + ": ";
/* 124 */     if (par1EnumOptions.getEnumFloat()) {
/* 125 */       float f1 = getOptionFloatValue(par1EnumOptions);
/* 126 */       return s + f1;
/* 127 */     }  if (par1EnumOptions == ModOptions.COLOURS) {
/* 128 */       s = s + I18n.func_135052_a(this.colourNames[this.colours], new Object[0]);
/*     */     } else {
/* 130 */       boolean clientSetting = getClientBooleanValue(par1EnumOptions);
/* 131 */       s = s + getTranslation(clientSetting);
/*     */     } 
/* 133 */     return s;
/*     */   }
/*     */   
/*     */   public boolean getClientBooleanValue(ModOptions o) {
/* 137 */     if (o == ModOptions.DEBUG)
/* 138 */       return this.debug; 
/* 139 */     if (o == ModOptions.LIGHTING)
/* 140 */       return this.lighting; 
/* 141 */     if (o == ModOptions.LOAD)
/* 142 */       return this.loadChunks; 
/* 143 */     if (o == ModOptions.UPDATE)
/* 144 */       return this.updateChunks; 
/* 145 */     if (o == ModOptions.DEPTH)
/* 146 */       return this.terrainDepth; 
/* 147 */     if (o == ModOptions.SLOPES)
/* 148 */       return this.terrainSlopes; 
/* 149 */     if (o == ModOptions.STEPS)
/* 150 */       return this.footsteps; 
/* 151 */     if (o == ModOptions.FLOWERS)
/* 152 */       return this.flowers; 
/* 153 */     if (o == ModOptions.COMPRESSION)
/* 154 */       return this.compression; 
/* 155 */     if (o == ModOptions.COORDINATES)
/* 156 */       return this.coordinates; 
/* 157 */     if (o == ModOptions.BIOMES)
/* 158 */       return this.biomeColorsVanillaMode; 
/* 159 */     if (o == ModOptions.WAYPOINTS)
/* 160 */       return this.waypoints; 
/* 161 */     if (o == ModOptions.ARROW)
/* 162 */       return this.renderArrow; 
/* 163 */     if (o == ModOptions.DISPLAY_ZOOM)
/* 164 */       return this.displayZoom; 
/* 165 */     return false;
/*     */   }
/*     */   
/*     */   private static String getTranslation(boolean o) {
/* 169 */     return I18n.func_135052_a("gui.xaero_" + (o ? "on" : "off"), new Object[0]);
/*     */   }
/*     */   
/*     */   public void setOptionValue(ModOptions par1EnumOptions, int par2) throws IOException {
/* 173 */     if (par1EnumOptions == ModOptions.DEBUG) {
/* 174 */       this.debug = !this.debug;
/*     */     }
/* 176 */     if (par1EnumOptions == ModOptions.COLOURS) {
/* 177 */       this.colours = (this.colours + 1) % 2;
/* 178 */       MapProcessor.instance.incrementGlobalVersion();
/*     */     } 
/* 180 */     if (par1EnumOptions == ModOptions.LIGHTING) {
/* 181 */       this.lighting = !this.lighting;
/*     */     }
/*     */     
/* 184 */     if (par1EnumOptions == ModOptions.LOAD) {
/* 185 */       this.loadChunks = !this.loadChunks;
/*     */     }
/* 187 */     if (par1EnumOptions == ModOptions.UPDATE) {
/* 188 */       this.updateChunks = !this.updateChunks;
/*     */     }
/* 190 */     if (par1EnumOptions == ModOptions.DEPTH) {
/* 191 */       this.terrainDepth = !this.terrainDepth;
/* 192 */       MapProcessor.instance.incrementGlobalVersion();
/*     */     } 
/* 194 */     if (par1EnumOptions == ModOptions.SLOPES) {
/* 195 */       this.terrainSlopes = !this.terrainSlopes;
/* 196 */       MapProcessor.instance.incrementGlobalVersion();
/*     */     } 
/* 198 */     if (par1EnumOptions == ModOptions.STEPS) {
/* 199 */       this.footsteps = !this.footsteps;
/*     */     }
/* 201 */     if (par1EnumOptions == ModOptions.FLOWERS) {
/* 202 */       this.flowers = !this.flowers;
/*     */     }
/* 204 */     if (par1EnumOptions == ModOptions.COMPRESSION) {
/* 205 */       this.compression = !this.compression;
/* 206 */       MapProcessor.instance.incrementGlobalVersion();
/*     */     } 
/* 208 */     if (par1EnumOptions == ModOptions.COORDINATES) {
/* 209 */       this.coordinates = !this.coordinates;
/*     */     }
/* 211 */     if (par1EnumOptions == ModOptions.BIOMES) {
/* 212 */       this.biomeColorsVanillaMode = !this.biomeColorsVanillaMode;
/* 213 */       if (this.colours == 1)
/* 214 */         MapProcessor.instance.incrementGlobalVersion(); 
/*     */     } 
/* 216 */     if (par1EnumOptions == ModOptions.WAYPOINTS) {
/* 217 */       this.waypoints = !this.waypoints;
/*     */     }
/* 219 */     if (par1EnumOptions == ModOptions.ARROW) {
/* 220 */       this.renderArrow = !this.renderArrow;
/*     */     }
/* 222 */     if (par1EnumOptions == ModOptions.DISPLAY_ZOOM) {
/* 223 */       this.displayZoom = !this.displayZoom;
/*     */     }
/* 225 */     saveSettings();
/* 226 */     if ((Minecraft.func_71410_x()).field_71462_r != null) {
/* 227 */       (Minecraft.func_71410_x()).field_71462_r.func_73866_w_();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void setOptionFloatValue(ModOptions options, float f) throws IOException {
/* 233 */     saveSettings();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public float getOptionFloatValue(ModOptions options) {
/* 239 */     return 1.0F;
/*     */   }
/*     */ }


/* Location:              C:\Users\ioanm\Downloads\XaerosWorldMap_1.7.3_Forge_1.12.jar!\xaero\map\settings\ModSettings.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */