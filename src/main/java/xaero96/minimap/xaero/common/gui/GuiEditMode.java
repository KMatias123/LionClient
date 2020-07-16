/*     */ package xaero.common.gui;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.GuiButton;
/*     */ import net.minecraft.client.gui.GuiScreen;
/*     */ import net.minecraft.client.gui.ScaledResolution;
/*     */ import net.minecraft.client.resources.I18n;
/*     */ import xaero.common.IXaeroMinimap;
/*     */ import xaero.common.interfaces.Interface;
/*     */ import xaero.common.interfaces.InterfaceManager;
/*     */ import xaero.common.settings.ModOptions;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GuiEditMode
/*     */   extends GuiScreen
/*     */ {
/*     */   private GuiScreen parentGuiScreen;
/*     */   protected String screenTitle;
/*     */   private IXaeroMinimap modMain;
/*     */   private String message;
/*     */   private boolean instructions;
/*     */   
/*     */   public GuiEditMode(IXaeroMinimap modMain, GuiScreen par1GuiScreen, String message, boolean instructions) {
/*  28 */     this.modMain = modMain;
/*  29 */     this.parentGuiScreen = par1GuiScreen;
/*  30 */     this.message = message;
/*  31 */     this.instructions = instructions;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void func_73866_w_() {
/*  39 */     this.screenTitle = I18n.func_135052_a("gui.xaero_edit_mode", new Object[0]);
/*  40 */     this.modMain.getInterfaces().setSelectedId(-1);
/*  41 */     this.modMain.getInterfaces().setDraggingId(-1);
/*  42 */     this.field_146292_n.clear();
/*  43 */     this.field_146292_n.add(new MySmallButton(200, this.field_146294_l / 2 - 155, this.field_146295_m / 6 + 143, I18n.func_135052_a("gui.xaero_confirm", new Object[0])));
/*  44 */     this.field_146292_n.add(new MySmallButton(202, this.field_146294_l / 2 + 5, this.field_146295_m / 6 + 143, I18n.func_135052_a("gui.xaero_choose_a_preset", new Object[0])));
/*  45 */     if (this.instructions) {
/*  46 */       this.field_146292_n.add(new MySmallButton(201, this.field_146294_l / 2 + 5, this.field_146295_m / 6 + 168, I18n.func_135052_a("gui.xaero_cancel", new Object[0])));
/*  47 */       this.field_146292_n.add(new MySmallButton(203, this.field_146294_l / 2 - 155, this.field_146295_m / 6 + 168, I18n.func_135052_a("gui.xaero_instructions", new Object[0])));
/*     */     } else {
/*  49 */       this.field_146292_n.add(new GuiButton(201, this.field_146294_l / 2 - 100, this.field_146295_m / 6 + 168, I18n.func_135052_a("gui.xaero_cancel", new Object[0])));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void func_146284_a(GuiButton par1GuiButton) {
/*  58 */     if (par1GuiButton.field_146124_l) {
/*     */       
/*  60 */       int var2 = this.field_146297_k.field_71474_y.field_74335_Z;
/*     */       
/*  62 */       if (par1GuiButton.field_146127_k < 100 && par1GuiButton instanceof MySmallButton) {
/*     */         
/*     */         try {
/*  65 */           this.modMain.getSettings().setOptionValue(((MySmallButton)par1GuiButton).returnModOptions(), 1);
/*  66 */         } catch (IOException e) {
/*  67 */           e.printStackTrace();
/*     */         } 
/*  69 */         par1GuiButton.field_146126_j = this.modMain.getSettings().getKeyBinding(ModOptions.getModOptions(par1GuiButton.field_146127_k));
/*     */       } 
/*     */       
/*  72 */       if (par1GuiButton.field_146127_k == 200) {
/*     */         
/*     */         try {
/*  75 */           confirm();
/*  76 */           this.modMain.getSettings().saveSettings();
/*  77 */         } catch (IOException e) {
/*  78 */           e.printStackTrace();
/*     */         } 
/*  80 */         this.field_146297_k.func_147108_a(this.parentGuiScreen);
/*     */       } 
/*     */       
/*  83 */       if (par1GuiButton.field_146127_k == 201) {
/*     */         
/*  85 */         cancel(this.modMain.getInterfaces());
/*  86 */         this.field_146297_k.func_147108_a(this.parentGuiScreen);
/*     */       } 
/*  88 */       if (par1GuiButton.field_146127_k == 202)
/*     */       {
/*     */ 
/*     */         
/*  92 */         this.field_146297_k.func_147108_a(new GuiChoosePreset(this.modMain, this));
/*     */       }
/*  94 */       if (par1GuiButton.field_146127_k == 203)
/*     */       {
/*     */ 
/*     */         
/*  98 */         this.field_146297_k.func_147108_a(new GuiInstructions(this));
/*     */       }
/*     */       
/* 101 */       if (this.field_146297_k.field_71474_y.field_74335_Z != var2) {
/*     */         
/* 103 */         ScaledResolution var3 = new ScaledResolution(this.field_146297_k);
/* 104 */         int var4 = var3.func_78326_a();
/* 105 */         int var5 = var3.func_78328_b();
/* 106 */         func_146280_a(this.field_146297_k, var4, var5);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public List getButtons() {
/* 113 */     return this.field_146292_n;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void func_73863_a(int par1, int par2, float par3) {
/* 121 */     if (this.modMain.getInterfaces().getDraggingId() == -1) {
/* 122 */       if (this.field_146297_k.field_71439_g == null) {
/* 123 */         func_146276_q_();
/* 124 */         func_73732_a(this.field_146289_q, I18n.func_135052_a("gui.xaero_not_ingame", new Object[0]), this.field_146294_l / 2, this.field_146295_m / 6 + 128, 16777215);
/*     */       } else {
/* 126 */         func_73732_a(this.field_146289_q, I18n.func_135052_a(this.message, new Object[0]), this.field_146294_l / 2, this.field_146295_m / 6 + 128, 16777215);
/* 127 */       }  super.func_73863_a(par1, par2, par3);
/*     */     } 
/* 129 */     if (this.field_146297_k.field_71439_g != null) {
/* 130 */       ScaledResolution scaledresolution = new ScaledResolution(Minecraft.func_71410_x());
/* 131 */       int width = scaledresolution.func_78326_a();
/* 132 */       int height = scaledresolution.func_78328_b();
/* 133 */       int scale = scaledresolution.func_78325_e();
/* 134 */       this.modMain.getInterfaceRenderer().renderBoxes(par1, par2, width, height, scale);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void confirm() {
/* 139 */     Iterator<Interface> iter = this.modMain.getInterfaces().getInterfaceIterator();
/* 140 */     while (iter.hasNext())
/* 141 */       ((Interface)iter.next()).backup(); 
/*     */   }
/*     */   
/*     */   public static void cancel(InterfaceManager interfaces) {
/* 145 */     Iterator<Interface> iter = interfaces.getInterfaceIterator();
/* 146 */     while (iter.hasNext())
/* 147 */       ((Interface)iter.next()).restore(); 
/*     */   }
/*     */   
/*     */   public void applyPreset(int id) {
/* 151 */     Iterator<Interface> iter = this.modMain.getInterfaces().getInterfaceIterator();
/* 152 */     this.modMain.getInterfaces().setActionTimer(10);
/* 153 */     while (iter.hasNext())
/* 154 */       ((Interface)iter.next()).applyPreset(this.modMain.getInterfaces().getPreset(id)); 
/*     */   }
/*     */ }


/* Location:              C:\Users\ioanm\Downloads\Xaeros_Minimap_20.15.3_Forge_1.12.jar!\xaero\common\gui\GuiEditMode.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */