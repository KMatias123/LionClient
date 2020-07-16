/*    */ package xaero.common.gui;
/*    */ 
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ import net.minecraft.client.gui.GuiButton;
/*    */ import net.minecraft.client.gui.GuiScreen;
/*    */ import net.minecraft.client.gui.ScaledResolution;
/*    */ import net.minecraft.client.resources.I18n;
/*    */ import xaero.common.IXaeroMinimap;
/*    */ import xaero.common.interfaces.Preset;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class GuiChoosePreset
/*    */   extends GuiScreen
/*    */ {
/*    */   private GuiScreen parentGuiScreen;
/*    */   protected String screenTitle;
/*    */   private IXaeroMinimap modMain;
/*    */   
/*    */   public GuiChoosePreset(IXaeroMinimap modMain, GuiScreen par1GuiScreen) {
/* 24 */     this.modMain = modMain;
/* 25 */     this.parentGuiScreen = par1GuiScreen;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void func_73866_w_() {
/* 33 */     this.screenTitle = I18n.func_135052_a("gui.xaero_choose_a_preset", new Object[0]);
/* 34 */     this.modMain.getInterfaces().setSelectedId(-1);
/* 35 */     this.modMain.getInterfaces().setDraggingId(-1);
/* 36 */     this.field_146292_n.clear();
/* 37 */     this.field_146292_n.add(new GuiButton(200, this.field_146294_l / 2 - 100, this.field_146295_m / 6 + 168, I18n.func_135052_a("gui.xaero_cancel", new Object[0])));
/* 38 */     Iterator<Preset> iterator = this.modMain.getInterfaces().getPresetsIterator();
/* 39 */     int i = 0;
/* 40 */     while (iterator.hasNext()) {
/*    */       
/* 42 */       Preset var7 = iterator.next();
/* 43 */       this.field_146292_n.add(new MySmallButton(i, this.field_146294_l / 2 - 155 + i % 2 * 160, this.field_146295_m / 7 + 24 * (i >> 1), var7.getName()));
/* 44 */       i++;
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void func_146284_a(GuiButton par1GuiButton) {
/* 54 */     if (par1GuiButton.field_146124_l) {
/*    */       
/* 56 */       int var2 = this.field_146297_k.field_71474_y.field_74335_Z;
/*    */       
/* 58 */       if (par1GuiButton.field_146127_k < 100 && par1GuiButton instanceof MySmallButton) {
/*    */         
/* 60 */         ((GuiEditMode)this.parentGuiScreen).applyPreset(par1GuiButton.field_146127_k);
/* 61 */         this.field_146297_k.func_147108_a(this.parentGuiScreen);
/*    */       } 
/*    */       
/* 64 */       if (par1GuiButton.field_146127_k == 200)
/*    */       {
/* 66 */         this.field_146297_k.func_147108_a(this.parentGuiScreen);
/*    */       }
/*    */       
/* 69 */       if (this.field_146297_k.field_71474_y.field_74335_Z != var2) {
/*    */         
/* 71 */         ScaledResolution var3 = new ScaledResolution(this.field_146297_k);
/* 72 */         int var4 = var3.func_78326_a();
/* 73 */         int var5 = var3.func_78328_b();
/* 74 */         func_146280_a(this.field_146297_k, var4, var5);
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public List getButtons() {
/* 81 */     return this.field_146292_n;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void func_73863_a(int par1, int par2, float par3) {
/* 89 */     func_146276_q_();
/* 90 */     func_73732_a(this.field_146289_q, this.screenTitle, this.field_146294_l / 2, 20, 16777215);
/* 91 */     super.func_73863_a(par1, par2, par3);
/*    */   }
/*    */ }


/* Location:              C:\Users\ioanm\Downloads\Xaeros_Minimap_20.15.3_Forge_1.12.jar!\xaero\common\gui\GuiChoosePreset.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */