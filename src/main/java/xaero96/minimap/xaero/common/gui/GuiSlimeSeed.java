/*    */ package xaero.common.gui;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.client.gui.GuiButton;
/*    */ import net.minecraft.client.gui.GuiScreen;
/*    */ import net.minecraft.client.gui.GuiTextField;
/*    */ import net.minecraft.client.resources.I18n;
/*    */ import org.apache.commons.lang3.StringUtils;
/*    */ import org.lwjgl.input.Keyboard;
/*    */ import xaero.common.IXaeroMinimap;
/*    */ import xaero.common.settings.ModOptions;
/*    */ 
/*    */ 
/*    */ public class GuiSlimeSeed
/*    */   extends GuiSettings
/*    */ {
/*    */   public GuiTextField seedTextField;
/*    */   private IXaeroMinimap modMain;
/*    */   
/*    */   public GuiSlimeSeed(IXaeroMinimap modMain, GuiScreen parent) {
/* 21 */     super(modMain, parent);
/* 22 */     this.modMain = modMain;
/* 23 */     this.options = new ModOptions[] { ModOptions.SLIME_CHUNKS, ModOptions.OPEN_SLIME_SETTINGS };
/*    */   }
/*    */   
/*    */   public void func_73866_w_() {
/* 27 */     super.func_73866_w_();
/* 28 */     this.screenTitle = I18n.func_135052_a("gui.xaero_slime_chunks", new Object[0]);
/* 29 */     this.seedTextField = new GuiTextField(0, this.field_146289_q, this.field_146294_l / 2 - 100, this.field_146295_m / 7 + 68, 200, 20);
/* 30 */     this.modMain.getSettings();
/* 31 */     this.modMain.getSettings();
/* 32 */     this.seedTextField.func_146180_a("" + (
/* 33 */         (this.modMain.getSettings().getSlimeChunksSeed() == null) ? "" : (String)this.modMain.getSettings().getSlimeChunksSeed()));
/* 34 */     Keyboard.enableRepeatEvents(true);
/*    */   }
/*    */   
/*    */   public void func_73863_a(int mouseX, int mouseY, float partial) {
/* 38 */     super.func_73863_a(mouseX, mouseY, partial);
/* 39 */     this.seedTextField.func_146194_f();
/* 40 */     func_73732_a(this.field_146289_q, I18n.func_135052_a("gui.xaero_used_seed", new Object[0]), this.field_146294_l / 2, this.field_146295_m / 7 + 55, 16777215);
/*    */   }
/*    */ 
/*    */   
/*    */   public void func_73876_c() {
/* 45 */     this.seedTextField.func_146178_a();
/*    */   }
/*    */   
/*    */   public void func_73864_a(int par1, int par2, int par3) throws IOException {
/* 49 */     super.func_73864_a(par1, par2, par3);
/* 50 */     this.seedTextField.func_146192_a(par1, par2, par3);
/*    */   }
/*    */   
/*    */   protected void func_73869_a(char par1, int par2) throws IOException {
/* 54 */     if (this.seedTextField.func_146206_l()) {
/* 55 */       this.seedTextField.func_146201_a(par1, par2);
/*    */     }
/* 57 */     if (par2 == 28) {
/* 58 */       func_146284_a(this.field_146292_n.get(0));
/*    */     }
/*    */     
/* 61 */     String s = this.seedTextField.func_146179_b();
/* 62 */     if (!StringUtils.isEmpty(s)) {
/*    */       try {
/* 64 */         long j = Long.parseLong(s);
/* 65 */         this.modMain.getSettings().setSlimeChunksSeed(j);
/* 66 */       } catch (NumberFormatException numberformatexception) {
/* 67 */         this.modMain.getSettings().setSlimeChunksSeed(s.hashCode());
/*    */       } 
/*    */     }
/* 70 */     this.modMain.getSettings().saveSettings();
/*    */     
/* 72 */     super.func_73869_a(par1, par2);
/*    */   }
/*    */   
/*    */   protected void func_146284_a(GuiButton par1GuiButton) {
/* 76 */     if (par1GuiButton.field_146124_l && 
/* 77 */       par1GuiButton.field_146127_k == 200) {
/* 78 */       this.field_146297_k.func_147108_a(this.parentGuiScreen);
/*    */     }
/*    */     
/* 81 */     super.func_146284_a(par1GuiButton);
/*    */   }
/*    */ }


/* Location:              C:\Users\ioanm\Downloads\Xaeros_Minimap_20.15.3_Forge_1.12.jar!\xaero\common\gui\GuiSlimeSeed.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */