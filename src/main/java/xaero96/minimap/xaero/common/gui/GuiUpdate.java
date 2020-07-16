/*     */ package xaero.common.gui;
/*     */ 
/*     */ import java.awt.Desktop;
/*     */ import java.io.IOException;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.GuiButton;
/*     */ import net.minecraft.client.gui.GuiScreen;
/*     */ import net.minecraft.client.gui.GuiYesNo;
/*     */ import net.minecraft.client.gui.ScaledResolution;
/*     */ import net.minecraft.client.resources.I18n;
/*     */ import org.lwjgl.input.Mouse;
/*     */ import xaero.common.IXaeroMinimap;
/*     */ import xaero.common.gui.widget.GuiWidgetButton;
/*     */ import xaero.common.gui.widget.WidgetScreen;
/*     */ import xaero.common.settings.ModSettings;
/*     */ 
/*     */ public class GuiUpdate
/*     */   extends GuiYesNo
/*     */   implements WidgetScreen {
/*     */   private IXaeroMinimap modMain;
/*     */   private int guiScaleFactor;
/*     */   
/*     */   public GuiUpdate(IXaeroMinimap modMain, String message1) {
/*  26 */     super(null, message1, "Would you like to update it (open the mod page)?", 0);
/*  27 */     this.modMain = modMain;
/*     */   }
/*     */   
/*     */   public void func_73866_w_() {
/*  31 */     super.func_73866_w_();
/*  32 */     ScaledResolution var3 = new ScaledResolution(this.field_146297_k);
/*  33 */     this.guiScaleFactor = var3.func_78325_e();
/*  34 */     this.field_146292_n.add(new GuiButton(200, this.field_146294_l / 2 - 100, this.field_146295_m / 6 + 144, 
/*  35 */           I18n.func_135052_a("Don't show again for this update", new Object[0])));
/*  36 */     this.modMain.getWidgetScreenHandler().initialize(this, this.field_146294_l, this.field_146295_m);
/*     */   }
/*     */   
/*     */   protected void func_146284_a(GuiButton button) throws IOException {
/*  40 */     switch (button.field_146127_k) {
/*     */       case 0:
/*  42 */         if (this.modMain.getModJAR() == null)
/*     */           return; 
/*     */         try {
/*  45 */           Desktop d = Desktop.getDesktop();
/*  46 */           d.browse(new URI(this.modMain.getUpdateLink()));
/*  47 */           d.open(this.modMain.getModJAR().getParentFile());
/*  48 */         } catch (Exception e) {
/*  49 */           e.printStackTrace();
/*     */         } 
/*  51 */         Minecraft.func_71410_x().func_71400_g();
/*     */         break;
/*     */       case 1:
/*  54 */         this.field_146297_k.func_147108_a(null);
/*     */         break;
/*     */       case 200:
/*  57 */         ModSettings.ignoreUpdate = this.modMain.getNewestUpdateID();
/*  58 */         this.modMain.getSettings().saveSettings();
/*  59 */         this.field_146297_k.func_147108_a(null);
/*     */         break;
/*     */       case 201:
/*     */         try {
/*  63 */           Desktop d = Desktop.getDesktop();
/*  64 */           d.browse(new URI((this.modMain.getPatreon()).changelogLink));
/*  65 */         } catch (URISyntaxException e) {
/*  66 */           e.printStackTrace();
/*     */         } 
/*     */         break;
/*     */     } 
/*     */     
/*  71 */     if (button instanceof GuiWidgetButton) {
/*  72 */       this.modMain.getWidgetScreenHandler().handleButton(this, (GuiWidgetButton)button);
/*     */     }
/*     */   }
/*     */   
/*     */   public void func_146276_q_() {
/*  77 */     super.func_146276_q_();
/*  78 */     int mouseX = (int)(Mouse.getX() * this.field_146294_l / this.field_146297_k.field_71443_c);
/*  79 */     int mouseY = (int)(Mouse.getY() * this.field_146295_m / this.field_146297_k.field_71440_d);
/*  80 */     this.modMain.getWidgetScreenHandler().render(this, this.field_146294_l, this.field_146295_m, mouseX, mouseY, this.guiScaleFactor);
/*     */   }
/*     */ 
/*     */   
/*     */   public void func_73863_a(int p_render_1_, int p_render_2_, float p_render_3_) {
/*  85 */     super.func_73863_a(p_render_1_, p_render_2_, p_render_3_);
/*  86 */     this.modMain.getWidgetScreenHandler().renderTooltips((GuiScreen)this, this.field_146294_l, this.field_146295_m, p_render_1_, p_render_2_, this.guiScaleFactor);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addButtonVisible(GuiButton button) {
/*  91 */     func_189646_b(button);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <S extends GuiScreen & WidgetScreen> S getScreen() {
/*  97 */     return (S)this;
/*     */   }
/*     */ 
/*     */   
/*     */   public void func_73864_a(int p_mouseClicked_1_, int p_mouseClicked_3_, int p_mouseClicked_5_) throws IOException {
/* 102 */     this.modMain.getWidgetScreenHandler().handleClick((GuiScreen)this, this.field_146294_l, this.field_146295_m, p_mouseClicked_1_, p_mouseClicked_3_, this.guiScaleFactor);
/* 103 */     super.func_73864_a(p_mouseClicked_1_, p_mouseClicked_3_, p_mouseClicked_5_);
/*     */   }
/*     */ }


/* Location:              C:\Users\ioanm\Downloads\Xaeros_Minimap_20.15.3_Forge_1.12.jar!\xaero\common\gui\GuiUpdate.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */