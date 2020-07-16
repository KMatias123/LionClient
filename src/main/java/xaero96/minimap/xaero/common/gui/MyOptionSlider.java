/*    */ package xaero.common.gui;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.GuiButton;
/*    */ import net.minecraft.client.renderer.GlStateManager;
/*    */ import net.minecraft.util.math.MathHelper;
/*    */ import xaero.common.IXaeroMinimap;
/*    */ import xaero.common.settings.ModOptions;
/*    */ 
/*    */ 
/*    */ public class MyOptionSlider
/*    */   extends GuiButton
/*    */ {
/*    */   private float sliderValue;
/*    */   private boolean dragging;
/*    */   private ModOptions options;
/*    */   private IXaeroMinimap modMain;
/*    */   
/*    */   public MyOptionSlider(IXaeroMinimap modMain, int p_i45016_1_, int p_i45016_2_, int p_i45016_3_, ModOptions p_i45016_4_) {
/* 21 */     this(modMain, p_i45016_1_, p_i45016_2_, p_i45016_3_, p_i45016_4_, 0.0F, 1.0F);
/*    */   }
/*    */ 
/*    */   
/*    */   public MyOptionSlider(IXaeroMinimap modMain, int p_i45017_1_, int p_i45017_2_, int p_i45017_3_, ModOptions p_i45017_4_, float p_i45017_5_, float p_i45017_6_) {
/* 26 */     super(p_i45017_1_, p_i45017_2_, p_i45017_3_, 150, 20, "");
/* 27 */     this.modMain = modMain;
/* 28 */     this.sliderValue = 1.0F;
/* 29 */     this.options = p_i45017_4_;
/* 30 */     this.sliderValue = p_i45017_4_.normalizeValue(modMain.getSettings().getOptionFloatValue(p_i45017_4_));
/* 31 */     this.field_146126_j = modMain.getSettings().getKeyBinding(p_i45017_4_);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected int func_146114_a(boolean mouseOver) {
/* 40 */     return 0;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void func_146119_b(Minecraft mc, int mouseX, int mouseY) {
/* 48 */     if (this.field_146125_m) {
/*    */       
/* 50 */       if (this.dragging)
/*    */       {
/* 52 */         updateValue(mouseX);
/*    */       }
/*    */       
/* 55 */       mc.func_110434_K().func_110577_a(field_146122_a);
/* 56 */       GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
/* 57 */       func_73729_b(this.field_146128_h + (int)(this.sliderValue * (this.field_146120_f - 8)), this.field_146129_i, 0, 66, 4, 20);
/* 58 */       func_73729_b(this.field_146128_h + (int)(this.sliderValue * (this.field_146120_f - 8)) + 4, this.field_146129_i, 196, 66, 4, 20);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean func_146116_c(Minecraft mc, int mouseX, int mouseY) {
/* 68 */     if (super.func_146116_c(mc, mouseX, mouseY)) {
/*    */       
/* 70 */       updateValue(mouseX);
/* 71 */       this.dragging = true;
/* 72 */       return true;
/*    */     } 
/*    */ 
/*    */     
/* 76 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   private void updateValue(double mouseX) {
/* 81 */     this.sliderValue = (float)(mouseX - (this.field_146128_h + 4)) / (this.field_146120_f - 8);
/* 82 */     this.sliderValue = MathHelper.func_76131_a(this.sliderValue, 0.0F, 1.0F);
/* 83 */     float f = this.options.denormalizeValue(this.sliderValue);
/*    */     try {
/* 85 */       this.modMain.getSettings().setOptionFloatValue(this.options, f);
/* 86 */     } catch (IOException e) {
/* 87 */       e.printStackTrace();
/*    */     } 
/* 89 */     this.sliderValue = this.options.normalizeValue(f);
/* 90 */     this.field_146126_j = this.modMain.getSettings().getKeyBinding(this.options);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void func_146118_a(int mouseX, int mouseY) {
/* 98 */     this.dragging = false;
/*    */   }
/*    */ }


/* Location:              C:\Users\ioanm\Downloads\Xaeros_Minimap_20.15.3_Forge_1.12.jar!\xaero\common\gui\MyOptionSlider.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */