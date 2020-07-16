/*     */ package xaero.map.gui;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import net.minecraft.client.gui.GuiButton;
/*     */ import net.minecraft.client.gui.GuiScreen;
/*     */ import net.minecraft.client.gui.ScaledResolution;
/*     */ import net.minecraft.client.resources.I18n;
/*     */ import xaero.map.settings.ModOptions;
/*     */ import xaero.map.settings.ModSettings;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GuiSettings
/*     */   extends GuiScreen
/*     */ {
/*     */   protected GuiScreen parentGuiScreen;
/*     */   protected String screenTitle;
/*     */   protected ModSettings guiModSettings;
/*     */   protected ModOptions[] options;
/*     */   
/*     */   public GuiSettings(GuiScreen par1GuiScreen, ModSettings par2ModSettings) {
/*  25 */     this.parentGuiScreen = par1GuiScreen;
/*  26 */     this.guiModSettings = par2ModSettings;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void func_73866_w_() {
/*  33 */     this.field_146292_n.clear();
/*  34 */     this.field_146292_n.add(new GuiButton(200, this.field_146294_l / 2 - 100, this.field_146295_m / 6 + 173, 
/*  35 */           I18n.func_135052_a("gui.xaero_back", new Object[0])));
/*  36 */     int var8 = 0;
/*  37 */     if (this.options == null)
/*     */       return; 
/*  39 */     int var10 = this.options.length;
/*     */     
/*  41 */     for (int var11 = 0; var11 < var10; var11++) {
/*  42 */       ModOptions option = this.options[var11];
/*     */       
/*  44 */       if (!option.getEnumFloat()) {
/*  45 */         this.field_146292_n.add(new MySmallButton(option.returnEnumOrdinal(), this.field_146294_l / 2 - 155 + var8 % 2 * 160, this.field_146295_m / 12 + 24 * (var8 >> 1), option, this.guiModSettings
/*  46 */               .getKeyBinding(option)));
/*     */       } else {
/*  48 */         this.field_146292_n.add(new MyOptionSlider(option.returnEnumOrdinal(), this.field_146294_l / 2 - 155 + var8 % 2 * 160, this.field_146295_m / 12 + 24 * (var8 >> 1), option));
/*     */       } 
/*     */       
/*  51 */       var8++;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void func_146284_a(GuiButton par1GuiButton) {
/*  62 */     if (par1GuiButton.field_146124_l) {
/*  63 */       int var2 = this.field_146297_k.field_71474_y.field_74335_Z;
/*     */       
/*     */       try {
/*  66 */         if (par1GuiButton instanceof MySmallButton);
/*     */ 
/*     */       
/*     */       }
/*  70 */       catch (Exception e) {
/*  71 */         e.printStackTrace();
/*     */       } 
/*     */       
/*  74 */       if (par1GuiButton.field_146127_k < 100 && par1GuiButton instanceof MySmallButton) {
/*     */         try {
/*  76 */           this.guiModSettings.setOptionValue(((MySmallButton)par1GuiButton).returnModOptions(), 1);
/*  77 */         } catch (IOException e) {
/*  78 */           e.printStackTrace();
/*     */         } 
/*  80 */         par1GuiButton
/*  81 */           .field_146126_j = this.guiModSettings.getKeyBinding(ModOptions.getModOptions(par1GuiButton.field_146127_k));
/*     */       } 
/*     */       
/*  84 */       if (par1GuiButton.field_146127_k == 200) {
/*     */         try {
/*  86 */           this.guiModSettings.saveSettings();
/*  87 */         } catch (IOException e) {
/*  88 */           e.printStackTrace();
/*     */         } 
/*  90 */         this.field_146297_k.func_147108_a(this.parentGuiScreen);
/*     */       } 
/*     */       
/*  93 */       if (this.field_146297_k.field_71474_y.field_74335_Z != var2) {
/*  94 */         ScaledResolution var3 = new ScaledResolution(this.field_146297_k);
/*  95 */         int var4 = var3.func_78326_a();
/*  96 */         int var5 = var3.func_78328_b();
/*  97 */         func_146280_a(this.field_146297_k, var4, var5);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void func_73863_a(int par1, int par2, float par3) {
/* 106 */     func_146276_q_();
/* 107 */     func_73732_a(this.field_146289_q, this.screenTitle, this.field_146294_l / 2, 5, 16777215);
/* 108 */     super.func_73863_a(par1, par2, par3);
/*     */   }
/*     */ }


/* Location:              C:\Users\ioanm\Downloads\XaerosWorldMap_1.7.3_Forge_1.12.jar!\xaero\map\gui\GuiSettings.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */