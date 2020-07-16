/*    */ package xaero.map.gui;
/*    */ 
/*    */ import net.minecraft.client.gui.GuiButton;
/*    */ import net.minecraftforge.fml.relauncher.Side;
/*    */ import net.minecraftforge.fml.relauncher.SideOnly;
/*    */ import xaero.map.settings.ModOptions;
/*    */ 
/*    */ @SideOnly(Side.CLIENT)
/*    */ public class MySmallButton
/*    */   extends GuiButton
/*    */ {
/*    */   private final ModOptions modOptions;
/*    */   
/*    */   public MySmallButton(int par1, int par2, int par3, String par4Str) {
/* 15 */     this(par1, par2, par3, (ModOptions)null, par4Str);
/*    */   }
/*    */ 
/*    */   
/*    */   public MySmallButton(int par1, int par2, int par3, int par4, int par5, String par6Str) {
/* 20 */     super(par1, par2, par3, par4, par5, par6Str);
/* 21 */     this.modOptions = null;
/*    */   }
/*    */ 
/*    */   
/*    */   public MySmallButton(int par1, int par2, int par3, ModOptions par4EnumOptions, String par5Str) {
/* 26 */     super(par1, par2, par3, 150, 20, par5Str);
/* 27 */     this.modOptions = par4EnumOptions;
/*    */   }
/*    */ 
/*    */   
/*    */   public ModOptions returnModOptions() {
/* 32 */     return this.modOptions;
/*    */   }
/*    */ }


/* Location:              C:\Users\ioanm\Downloads\XaerosWorldMap_1.7.3_Forge_1.12.jar!\xaero\map\gui\MySmallButton.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */