/*    */ package xaero.common.gui;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.client.gui.GuiButton;
/*    */ import net.minecraft.client.gui.GuiScreen;
/*    */ import net.minecraft.client.gui.GuiTextField;
/*    */ import net.minecraft.client.resources.I18n;
/*    */ import org.lwjgl.input.Keyboard;
/*    */ import xaero.common.IXaeroMinimap;
/*    */ 
/*    */ 
/*    */ public class GuiDefaultTpCommand
/*    */   extends GuiScreen
/*    */ {
/*    */   public String screenTitle;
/*    */   private GuiScreen parentScreen;
/*    */   private MySmallButton confirmButton;
/*    */   private IXaeroMinimap modMain;
/*    */   private GuiTextField commandTextField;
/*    */   private String command;
/*    */   
/*    */   public GuiDefaultTpCommand(IXaeroMinimap modMain, GuiScreen parent) {
/* 23 */     this.parentScreen = parent;
/* 24 */     this.modMain = modMain;
/*    */   }
/*    */ 
/*    */   
/*    */   public void func_73866_w_() {
/* 29 */     this.field_146292_n.clear();
/* 30 */     this.field_146292_n.add(this
/* 31 */         .confirmButton = new MySmallButton(200, this.field_146294_l / 2 - 155, this.field_146295_m / 6 + 168, I18n.func_135052_a("gui.xaero_confirm", new Object[0])));
/* 32 */     this.field_146292_n.add(new MySmallButton(201, this.field_146294_l / 2 + 5, this.field_146295_m / 6 + 168, 
/* 33 */           I18n.func_135052_a("gui.xaero_cancel", new Object[0])));
/* 34 */     this.screenTitle = I18n.func_135052_a("gui.xaero_teleport_default_command", new Object[0]);
/* 35 */     this.commandTextField = new GuiTextField(0, this.field_146289_q, this.field_146294_l / 2 - 100, this.field_146295_m / 7 + 68, 200, 20);
/* 36 */     if (this.command == null)
/* 37 */       this.command = (this.modMain.getSettings()).waypointTp; 
/* 38 */     this.commandTextField.func_146180_a(this.command);
/* 39 */     Keyboard.enableRepeatEvents(true);
/*    */   }
/*    */ 
/*    */   
/*    */   public void func_73863_a(int mouseX, int mouseY, float partial) {
/* 44 */     func_146276_q_();
/* 45 */     func_73732_a(this.field_146289_q, this.screenTitle, this.field_146294_l / 2, 20, 16777215);
/* 46 */     super.func_73863_a(mouseX, mouseY, partial);
/* 47 */     this.commandTextField.func_146194_f();
/*    */   }
/*    */ 
/*    */   
/*    */   public void func_73876_c() {
/* 52 */     this.commandTextField.func_146178_a();
/* 53 */     this.command = this.commandTextField.func_146179_b();
/* 54 */     this.confirmButton.field_146124_l = (this.command != null && this.command.length() > 0);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void func_73864_a(int par1, int par2, int par3) throws IOException {
/* 59 */     super.func_73864_a(par1, par2, par3);
/* 60 */     this.commandTextField.func_146192_a(par1, par2, par3);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void func_73869_a(char par1, int par2) throws IOException {
/* 65 */     if (this.commandTextField.func_146206_l())
/* 66 */       this.commandTextField.func_146201_a(par1, par2); 
/* 67 */     if (par2 == 28) {
/* 68 */       func_146284_a(this.field_146292_n.get(0));
/*    */     }
/* 70 */     super.func_73869_a(par1, par2);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void func_146284_a(GuiButton par1GuiButton) throws IOException {
/* 75 */     if (par1GuiButton.field_146124_l) {
/* 76 */       if (par1GuiButton.field_146127_k == 200) {
/* 77 */         (this.modMain.getSettings()).waypointTp = this.command;
/* 78 */         this.modMain.getSettings().saveSettings();
/*    */       } 
/* 80 */       this.field_146297_k.func_147108_a(this.parentScreen);
/*    */     } 
/* 82 */     super.func_146284_a(par1GuiButton);
/*    */   }
/*    */ }


/* Location:              C:\Users\ioanm\Downloads\Xaeros_Minimap_20.15.3_Forge_1.12.jar!\xaero\common\gui\GuiDefaultTpCommand.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */