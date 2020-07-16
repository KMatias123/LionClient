/*    */ package xaero.common.interfaces;
/*    */ 
/*    */ import net.minecraft.client.resources.I18n;
/*    */ 
/*    */ public class Preset {
/*    */   private int[][] coords;
/*    */   private boolean[][] types;
/*    */   private String name;
/*    */   
/*    */   public Preset(String name, int[][] coords, boolean[][] types) {
/* 11 */     this.coords = coords;
/* 12 */     this.types = types;
/* 13 */     this.name = name;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 17 */     return I18n.func_135052_a(this.name, new Object[0]);
/*    */   }
/*    */   
/*    */   public int[] getCoords(int i) {
/* 21 */     return this.coords[i];
/*    */   }
/*    */   
/*    */   public boolean[] getTypes(int i) {
/* 25 */     return this.types[i];
/*    */   }
/*    */ }


/* Location:              C:\Users\ioanm\Downloads\Xaeros_Minimap_20.15.3_Forge_1.12.jar!\xaero\common\interfaces\Preset.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */