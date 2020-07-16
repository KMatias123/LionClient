/*    */ package xaero.map.gui;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.GuiButton;
/*    */ import net.minecraft.client.renderer.GlStateManager;
/*    */ import net.minecraft.util.math.MathHelper;
/*    */ import xaero.map.WorldMap;
/*    */ import xaero.map.settings.ModOptions;
/*    */ 
/*    */ 
/*    */ public class MyOptionSlider
/*    */   extends GuiButton
/*    */ {
/*    */   private float sliderValue;
/*    */   private boolean dragging;
/*    */   private ModOptions options;
/*    */   
/*    */   public MyOptionSlider(int p_i45016_1_, int p_i45016_2_, int p_i45016_3_, ModOptions p_i45016_4_) {
/* 20 */     this(p_i45016_1_, p_i45016_2_, p_i45016_3_, p_i45016_4_, 0.0F, 1.0F);
/*    */   }
/*    */ 
/*    */   
/*    */   public MyOptionSlider(int p_i45017_1_, int p_i45017_2_, int p_i45017_3_, ModOptions p_i45017_4_, float p_i45017_5_, float p_i45017_6_) {
/* 25 */     super(p_i45017_1_, p_i45017_2_, p_i45017_3_, 150, 20, "");
/* 26 */     this.sliderValue = 1.0F;
/* 27 */     this.options = p_i45017_4_;
/* 28 */     this.sliderValue = p_i45017_4_.normalizeValue(WorldMap.settings.getOptionFloatValue(p_i45017_4_));
/* 29 */     this.field_146126_j = WorldMap.settings.getKeyBinding(p_i45017_4_);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected int func_146114_a(boolean mouseOver) {
/* 38 */     return 0;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void func_146119_b(Minecraft mc, int mouseX, int mouseY) {
/* 44 */     if (this.field_146125_m) {
/*    */       
/* 46 */       if (this.dragging)
/*    */       {
/* 48 */         updateValue(mouseX);
/*    */       }
/*    */       
/* 51 */       mc.func_110434_K().func_110577_a(field_146122_a);
/* 52 */       GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
/* 53 */       func_73729_b(this.field_146128_h + (int)(this.sliderValue * (this.field_146120_f - 8)), this.field_146129_i, 0, 66, 4, 20);
/* 54 */       func_73729_b(this.field_146128_h + (int)(this.sliderValue * (this.field_146120_f - 8)) + 4, this.field_146129_i, 196, 66, 4, 20);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean func_146116_c(Minecraft mc, int mouseX, int mouseY) {
/* 64 */     if (super.func_146116_c(mc, mouseX, mouseY)) {
/*    */       
/* 66 */       updateValue(mouseX);
/* 67 */       this.dragging = true;
/* 68 */       return true;
/*    */     } 
/*    */ 
/*    */     
/* 72 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   private void updateValue(double mouseX) {
/* 77 */     this.sliderValue = (float)(mouseX - (this.field_146128_h + 4)) / (this.field_146120_f - 8);
/* 78 */     this.sliderValue = MathHelper.func_76131_a(this.sliderValue, 0.0F, 1.0F);
/* 79 */     float f = this.options.denormalizeValue(this.sliderValue);
/*    */     try {
/* 81 */       WorldMap.settings.setOptionFloatValue(this.options, f);
/* 82 */     } catch (IOException e) {
/* 83 */       e.printStackTrace();
/*    */     } 
/* 85 */     this.sliderValue = this.options.normalizeValue(f);
/* 86 */     this.field_146126_j = WorldMap.settings.getKeyBinding(this.options);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void func_146118_a(int mouseX, int mouseY) {
/* 94 */     this.dragging = false;
/*    */   }
/*    */ }


/* Location:              C:\Users\ioanm\Downloads\XaerosWorldMap_1.7.3_Forge_1.12.jar!\xaero\map\gui\MyOptionSlider.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */