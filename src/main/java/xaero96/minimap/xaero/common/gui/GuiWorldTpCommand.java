/*     */ package xaero.common.gui;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import net.minecraft.client.gui.FontRenderer;
/*     */ import net.minecraft.client.gui.GuiButton;
/*     */ import net.minecraft.client.gui.GuiScreen;
/*     */ import net.minecraft.client.gui.GuiTextField;
/*     */ import net.minecraft.client.resources.I18n;
/*     */ import org.lwjgl.input.Keyboard;
/*     */ import xaero.common.IXaeroMinimap;
/*     */ import xaero.common.minimap.waypoints.WaypointWorld;
/*     */ import xaero.common.minimap.waypoints.WaypointWorldRootContainer;
/*     */ import xaero.common.settings.ModSettings;
/*     */ 
/*     */ 
/*     */ public class GuiWorldTpCommand
/*     */   extends GuiScreen
/*     */ {
/*     */   public String screenTitle;
/*     */   private GuiScreen parentScreen;
/*     */   private MySmallButton confirmButton;
/*     */   private IXaeroMinimap modMain;
/*     */   private GuiTextField commandTextField;
/*     */   private boolean usingDefault;
/*     */   private String command;
/*     */   private WaypointWorldRootContainer rootContainer;
/*     */   
/*     */   public GuiWorldTpCommand(IXaeroMinimap modMain, GuiScreen parent, WaypointWorld waypointWorld) {
/*  29 */     this.parentScreen = parent;
/*  30 */     this.modMain = modMain;
/*  31 */     this.rootContainer = waypointWorld.getContainer().getRootContainer();
/*  32 */     this.command = (this.rootContainer.getTeleportCommand() == null) ? (modMain.getSettings()).waypointTp : this.rootContainer.getTeleportCommand();
/*  33 */     this.usingDefault = this.rootContainer.isUsingDefaultTeleportCommand();
/*     */   }
/*     */ 
/*     */   
/*     */   public void func_73866_w_() {
/*  38 */     super.func_73866_w_();
/*  39 */     this.screenTitle = I18n.func_135052_a("gui.xaero_world_teleport_command", new Object[0]);
/*  40 */     this.parentScreen.func_146280_a(this.field_146297_k, this.field_146294_l, this.field_146295_m);
/*  41 */     this.commandTextField = new GuiTextField(0, this.field_146289_q, this.field_146294_l / 2 - 100, this.field_146295_m / 7 + 68, 200, 20)
/*     */       {
/*     */         public void func_146191_b(String textToWrite) {
/*  44 */           if (!GuiWorldTpCommand.this.usingDefault)
/*  45 */             super.func_146191_b(textToWrite); 
/*     */         }
/*     */         
/*     */         public boolean func_146192_a(int p_mouseClicked_1_, int p_mouseClicked_3_, int p_mouseClicked_5_) {
/*  49 */           if (!GuiWorldTpCommand.this.usingDefault)
/*  50 */             return super.func_146192_a(p_mouseClicked_1_, p_mouseClicked_3_, p_mouseClicked_5_); 
/*  51 */           return false;
/*     */         }
/*     */       };
/*  54 */     this.commandTextField.func_146180_a(this.command);
/*  55 */     func_189646_b(this
/*  56 */         .confirmButton = new MySmallButton(200, this.field_146294_l / 2 - 155, this.field_146295_m / 6 + 168, I18n.func_135052_a("gui.xaero_confirm", new Object[0])));
/*  57 */     func_189646_b(new MySmallButton(201, this.field_146294_l / 2 + 5, this.field_146295_m / 6 + 168, 
/*  58 */           I18n.func_135052_a("gui.xaero_cancel", new Object[0])));
/*  59 */     func_189646_b(new MySmallButton(202, this.field_146294_l / 2 - 75, this.field_146295_m / 7 + 28, 
/*  60 */           I18n.func_135052_a("gui.xaero_use_default", new Object[0]) + ": " + ModSettings.getTranslation(this.usingDefault)));
/*  61 */     Keyboard.enableRepeatEvents(true);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void func_146284_a(GuiButton par1GuiButton) throws IOException {
/*  66 */     if (par1GuiButton.field_146124_l) {
/*  67 */       if (par1GuiButton.field_146127_k == 200) {
/*  68 */         if (this.command.equals((this.modMain.getSettings()).waypointTp)) {
/*  69 */           this.usingDefault = true;
/*  70 */           this.command = null;
/*     */         } 
/*  72 */         this.rootContainer.setUsingDefaultTeleportCommand(this.usingDefault);
/*  73 */         this.rootContainer.setTeleportCommand(this.command);
/*  74 */         this.rootContainer.saveConfig();
/*  75 */         this.field_146297_k.func_147108_a(this.parentScreen);
/*  76 */       } else if (par1GuiButton.field_146127_k == 201) {
/*  77 */         this.field_146297_k.func_147108_a(this.parentScreen);
/*  78 */       } else if (par1GuiButton.field_146127_k == 202) {
/*  79 */         this.usingDefault = !this.usingDefault;
/*  80 */         par1GuiButton.field_146126_j = I18n.func_135052_a("gui.xaero_use_default", new Object[0]) + ": " + ModSettings.getTranslation(this.usingDefault);
/*     */       } 
/*     */     }
/*  83 */     super.func_146284_a(par1GuiButton);
/*     */   }
/*     */ 
/*     */   
/*     */   public void func_73863_a(int mouseX, int mouseY, float partial) {
/*  88 */     if (this.parentScreen instanceof GuiWaypointsOptions)
/*  89 */       ((GuiWaypointsOptions)this.parentScreen).parent.func_73863_a(0, 0, partial); 
/*  90 */     func_146276_q_();
/*  91 */     func_146276_q_();
/*  92 */     func_73732_a(this.field_146289_q, this.screenTitle, this.field_146294_l / 2, 20, 16777215);
/*  93 */     super.func_73863_a(mouseX, mouseY, partial);
/*  94 */     if (this.usingDefault)
/*  95 */       this.commandTextField.func_146180_a("§8" + (this.modMain.getSettings()).waypointTp); 
/*  96 */     this.commandTextField.func_146194_f();
/*  97 */     if (this.usingDefault) {
/*  98 */       this.commandTextField.func_146180_a(this.command);
/*     */     }
/*     */   }
/*     */   
/*     */   public void func_73876_c() {
/* 103 */     this.commandTextField.func_146178_a();
/* 104 */     this.command = this.commandTextField.func_146179_b();
/* 105 */     this.confirmButton.field_146124_l = (this.command != null && this.command.length() > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void func_73869_a(char par1, int par2) throws IOException {
/* 110 */     if (this.commandTextField.func_146206_l())
/* 111 */       this.commandTextField.func_146201_a(par1, par2); 
/* 112 */     if (par2 == 28) {
/* 113 */       func_146284_a(this.field_146292_n.get(0));
/*     */     }
/* 115 */     super.func_73869_a(par1, par2);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void func_73864_a(int par1, int par2, int par3) throws IOException {
/* 120 */     super.func_73864_a(par1, par2, par3);
/* 121 */     this.commandTextField.func_146192_a(par1, par2, par3);
/*     */   }
/*     */ }


/* Location:              C:\Users\ioanm\Downloads\Xaeros_Minimap_20.15.3_Forge_1.12.jar!\xaero\common\gui\GuiWorldTpCommand.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */