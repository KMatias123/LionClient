/*     */ package xaero.common.gui;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import net.minecraft.client.gui.GuiButton;
/*     */ import net.minecraft.client.gui.GuiScreen;
/*     */ import net.minecraft.client.gui.ScaledResolution;
/*     */ import net.minecraft.client.resources.I18n;
/*     */ import xaero.common.IXaeroMinimap;
/*     */ import xaero.common.gui.widget.GuiWidgetButton;
/*     */ import xaero.common.gui.widget.WidgetScreen;
/*     */ import xaero.common.settings.ModOptions;
/*     */ 
/*     */ 
/*     */ public class GuiSettings
/*     */   extends GuiScreen
/*     */   implements WidgetScreen
/*     */ {
/*     */   protected GuiScreen parentGuiScreen;
/*     */   protected String screenTitle;
/*     */   protected ModOptions[] options;
/*     */   protected IXaeroMinimap modMain;
/*     */   private int guiScaleFactor;
/*     */   
/*     */   public GuiSettings(IXaeroMinimap modMain, GuiScreen par1GuiScreen) {
/*  25 */     this.modMain = modMain;
/*  26 */     this.parentGuiScreen = par1GuiScreen;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void func_73866_w_() {
/*  33 */     ScaledResolution var3 = new ScaledResolution(this.field_146297_k);
/*  34 */     this.guiScaleFactor = var3.func_78325_e();
/*  35 */     this.field_146292_n.clear();
/*  36 */     this.field_146292_n.add(new GuiButton(200, this.field_146294_l / 2 - 100, this.field_146295_m / 6 + 168, 
/*  37 */           I18n.func_135052_a("gui.xaero_back", new Object[0])));
/*  38 */     int var8 = 0;
/*  39 */     if (this.options != null) {
/*  40 */       int var10 = this.options.length;
/*     */       
/*  42 */       for (int var11 = 0; var11 < var10; var11++) {
/*  43 */         ModOptions option = this.options[var11];
/*     */         
/*  45 */         if (!option.getEnumFloat()) {
/*  46 */           this.field_146292_n.add(new ModOptionButton(option, option.returnEnumOrdinal(), this.field_146294_l / 2 - 155 + var8 % 2 * 160, this.field_146295_m / 7 + 24 * (var8 >> 1), this.modMain
/*  47 */                 .getSettings().getKeyBinding(option)));
/*     */         } else {
/*  49 */           this.field_146292_n.add(new ModOptionSlider(option, this.modMain, option.returnEnumOrdinal(), this.field_146294_l / 2 - 155 + var8 % 2 * 160, this.field_146295_m / 7 + 24 * (var8 >> 1)));
/*     */         } 
/*     */         
/*  52 */         var8++;
/*     */       } 
/*     */     } 
/*     */     
/*  56 */     this.modMain.getWidgetScreenHandler().initialize(this, this.field_146294_l, this.field_146295_m);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void func_146284_a(GuiButton par1GuiButton) {
/*  66 */     if (par1GuiButton.field_146124_l) {
/*  67 */       int var2 = this.field_146297_k.field_71474_y.field_74335_Z;
/*     */       
/*     */       try {
/*  70 */         if (par1GuiButton instanceof MySmallButton)
/*  71 */           this.modMain.getGuiHelper().openSettingsGui(((MySmallButton)par1GuiButton).returnModOptions()); 
/*  72 */       } catch (Exception e) {
/*  73 */         e.printStackTrace();
/*     */       } 
/*     */       
/*  76 */       if (par1GuiButton instanceof MySmallButton && ((MySmallButton)par1GuiButton).returnModOptions() != null) {
/*     */         try {
/*  78 */           this.modMain.getSettings().setOptionValue(((MySmallButton)par1GuiButton).returnModOptions(), 1);
/*  79 */         } catch (IOException e) {
/*  80 */           e.printStackTrace();
/*     */         } 
/*  82 */         par1GuiButton.field_146126_j = this.modMain.getSettings().getKeyBinding(ModOptions.getModOptions(par1GuiButton.field_146127_k));
/*     */       } 
/*     */       
/*  85 */       if (par1GuiButton.field_146127_k == 200) {
/*     */         try {
/*  87 */           this.modMain.getSettings().saveSettings();
/*  88 */         } catch (IOException e) {
/*  89 */           e.printStackTrace();
/*     */         } 
/*  91 */         this.field_146297_k.func_147108_a(this.parentGuiScreen);
/*     */       } 
/*     */       
/*  94 */       if (par1GuiButton instanceof GuiWidgetButton) {
/*  95 */         this.modMain.getWidgetScreenHandler().handleButton(this, (GuiWidgetButton)par1GuiButton);
/*     */       }
/*  97 */       if (this.field_146297_k.field_71474_y.field_74335_Z != var2) {
/*  98 */         ScaledResolution var3 = new ScaledResolution(this.field_146297_k);
/*  99 */         int var4 = var3.func_78326_a();
/* 100 */         int var5 = var3.func_78328_b();
/* 101 */         func_146280_a(this.field_146297_k, var4, var5);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void func_73863_a(int par1, int par2, float par3) {
/* 110 */     func_146276_q_();
/* 111 */     this.modMain.getWidgetScreenHandler().render(this, this.field_146294_l, this.field_146295_m, par1, par2, this.guiScaleFactor);
/* 112 */     func_73732_a(this.field_146289_q, this.screenTitle, this.field_146294_l / 2, 5, 16777215);
/* 113 */     super.func_73863_a(par1, par2, par3);
/* 114 */     this.modMain.getWidgetScreenHandler().renderTooltips(this, this.field_146294_l, this.field_146295_m, par1, par2, this.guiScaleFactor);
/* 115 */     for (int k = 0; k < this.field_146292_n.size(); k++) {
/* 116 */       GuiButton b = this.field_146292_n.get(k);
/* 117 */       if (b instanceof ModOptionWidget) {
/* 118 */         ModOptionWidget optionWidget = (ModOptionWidget)b;
/* 119 */         if (par1 >= b.field_146128_h && par2 >= b.field_146129_i && par1 < b.field_146128_h + b.field_146120_f && par2 < b.field_146129_i + b.field_146121_g && 
/* 120 */           optionWidget.getModOption().getTooltip() != null) {
/* 121 */           optionWidget.getModOption().getTooltip().drawBox(par1, par2, this.field_146294_l, this.field_146295_m);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void addButtonVisible(GuiButton button) {
/* 129 */     func_189646_b(button);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <S extends GuiScreen & WidgetScreen> S getScreen() {
/* 135 */     return (S)this;
/*     */   }
/*     */ 
/*     */   
/*     */   public void func_73864_a(int mouseX, int mouseY, int mouseButton) throws IOException {
/* 140 */     this.modMain.getWidgetScreenHandler().handleClick(this, this.field_146294_l, this.field_146295_m, mouseX, mouseY, this.guiScaleFactor);
/* 141 */     super.func_73864_a(mouseX, mouseY, mouseButton);
/*     */   }
/*     */ }


/* Location:              C:\Users\ioanm\Downloads\Xaeros_Minimap_20.15.3_Forge_1.12.jar!\xaero\common\gui\GuiSettings.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */