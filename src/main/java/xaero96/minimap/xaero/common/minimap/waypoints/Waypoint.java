/*     */ package xaero.common.minimap.waypoints;
/*     */ 
/*     */ import net.minecraft.client.resources.I18n;
/*     */ import net.minecraft.entity.Entity;
/*     */ 
/*     */ public class Waypoint
/*     */ {
/*     */   private int x;
/*     */   private int y;
/*     */   private int z;
/*     */   private String name;
/*     */   private String symbol;
/*     */   private int color;
/*     */   private boolean global;
/*     */   private boolean disabled = false;
/*  16 */   private int type = 0;
/*     */   private boolean rotation = false;
/*  18 */   private int yaw = 0;
/*     */   private boolean temporary;
/*     */   
/*     */   public Waypoint(int x, int y, int z, String name, String symbol, int color) {
/*  22 */     this(x, y, z, name, symbol, color, 0, false);
/*     */   }
/*     */   
/*     */   public Waypoint(int x, int y, int z, String name, String symbol, int color, int type) {
/*  26 */     this(x, y, z, name, symbol, color, type, false);
/*     */   }
/*     */   
/*     */   public Waypoint(int x, int y, int z, String name, String symbol, int color, int type, boolean temp) {
/*  30 */     this.x = x;
/*  31 */     this.y = y;
/*  32 */     this.z = z;
/*  33 */     this.symbol = symbol;
/*  34 */     this.color = color;
/*  35 */     this.type = type;
/*  36 */     this.name = name;
/*  37 */     this.temporary = temp;
/*  38 */     if (this.type == 1)
/*  39 */       this.global = true; 
/*     */   }
/*     */   
/*     */   public double getDistanceSq(double x, double y, double z) {
/*  43 */     double d3 = this.x - x;
/*  44 */     double d4 = this.y - y;
/*  45 */     double d5 = this.z - z;
/*  46 */     return d3 * d3 + d4 * d4 + d5 * d5;
/*     */   }
/*     */   
/*     */   public String getLocalizedName() {
/*  50 */     return I18n.func_135052_a(this.name, new Object[0]);
/*     */   }
/*     */   
/*     */   public boolean isDisabled() {
/*  54 */     return this.disabled;
/*     */   }
/*     */   
/*     */   public void setDisabled(boolean b) {
/*  58 */     this.temporary = false;
/*  59 */     this.disabled = b;
/*     */   }
/*     */   
/*     */   public int getType() {
/*  63 */     return this.type;
/*     */   }
/*     */   
/*     */   public void setType(int type) {
/*  67 */     this.type = type;
/*  68 */     if (this.type == 1)
/*  69 */       this.global = true; 
/*     */   }
/*     */   
/*     */   public int getX() {
/*  73 */     return this.x;
/*     */   }
/*     */   
/*     */   public int getX(boolean divideBy8) {
/*  77 */     if (!divideBy8)
/*  78 */       return this.x; 
/*  79 */     return Math.floorDiv(this.x, 8);
/*     */   }
/*     */   
/*     */   public int getY() {
/*  83 */     return this.y;
/*     */   }
/*     */   
/*     */   public int getZ() {
/*  87 */     return this.z;
/*     */   }
/*     */   
/*     */   public int getZ(boolean divideBy8) {
/*  91 */     if (!divideBy8)
/*  92 */       return this.z; 
/*  93 */     return Math.floorDiv(this.z, 8);
/*     */   }
/*     */   
/*     */   public void setX(int x) {
/*  97 */     this.x = x;
/*     */   }
/*     */   
/*     */   public void setY(int y) {
/* 101 */     this.y = y;
/*     */   }
/*     */   
/*     */   public void setZ(int z) {
/* 105 */     this.z = z;
/*     */   }
/*     */   
/*     */   public String getName() {
/* 109 */     return this.name;
/*     */   }
/*     */   
/*     */   public String getNameSafe(String replacement) {
/* 113 */     return getName().replace(":", replacement);
/*     */   }
/*     */   
/*     */   public void setName(String name) {
/* 117 */     this.name = name;
/*     */   }
/*     */   
/*     */   public boolean isTemporary() {
/* 121 */     return this.temporary;
/*     */   }
/*     */   
/*     */   public void setTemporary(boolean temporary) {
/* 125 */     this.temporary = temporary;
/* 126 */     this.disabled = false;
/*     */   }
/*     */   
/*     */   public String getSymbol() {
/* 130 */     return this.symbol;
/*     */   }
/*     */   
/*     */   public void setSymbol(String symbol) {
/* 134 */     this.symbol = symbol;
/*     */   }
/*     */   
/*     */   public String getSymbolSafe(String replacement) {
/* 138 */     return getSymbol().replace(":", replacement);
/*     */   }
/*     */   
/*     */   public boolean isRotation() {
/* 142 */     return this.rotation;
/*     */   }
/*     */   
/*     */   public void setRotation(boolean rotation) {
/* 146 */     this.rotation = rotation;
/*     */   }
/*     */   
/*     */   public int getYaw() {
/* 150 */     return this.yaw;
/*     */   }
/*     */   
/*     */   public void setYaw(int yaw) {
/* 154 */     this.yaw = yaw;
/*     */   }
/*     */   
/*     */   public int getColor() {
/* 158 */     if (this.color < 0)
/* 159 */       return 0; 
/* 160 */     return this.color;
/*     */   }
/*     */   
/*     */   public int getActualColor() {
/* 164 */     return this.color;
/*     */   }
/*     */   
/*     */   public void setColor(int c) {
/* 168 */     this.color = c;
/*     */   }
/*     */   
/*     */   public boolean isGlobal() {
/* 172 */     return this.global;
/*     */   }
/*     */   
/*     */   public void setGlobal(boolean global) {
/* 176 */     if (this.type != 1)
/* 177 */       this.global = global; 
/*     */   }
/*     */   
/*     */   public static String getStringFromStringSafe(String stringSafe, String replacement) {
/* 181 */     return stringSafe.replace(replacement, ":");
/*     */   }
/*     */   
/*     */   public boolean isServerWaypoint() {
/* 185 */     return false;
/*     */   }
/*     */   
/*     */   public String getComparisonName() {
/* 189 */     String comparisonName = getLocalizedName().toLowerCase().trim();
/* 190 */     if (comparisonName.startsWith("the "))
/* 191 */       comparisonName = comparisonName.substring(4); 
/* 192 */     if (comparisonName.startsWith("a "))
/* 193 */       comparisonName = comparisonName.substring(2); 
/* 194 */     return comparisonName;
/*     */   }
/*     */   
/*     */   public double getComparisonDistance(Entity entity, boolean divided) {
/* 198 */     double offX = getX(divided) - entity.field_70165_t;
/* 199 */     double offY = getY() - entity.field_70163_u;
/* 200 */     double offZ = getZ(divided) - entity.field_70161_v;
/* 201 */     return offX * offX + offY * offY + offZ * offZ;
/*     */   }
/*     */ }


/* Location:              C:\Users\ioanm\Downloads\Xaeros_Minimap_20.15.3_Forge_1.12.jar!\xaero\common\minimap\waypoints\Waypoint.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */