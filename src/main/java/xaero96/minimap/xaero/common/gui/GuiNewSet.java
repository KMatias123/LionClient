/*     */ package xaero.common.gui;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import net.minecraft.client.gui.GuiButton;
/*     */ import net.minecraft.client.gui.GuiScreen;
/*     */ import net.minecraft.client.gui.GuiTextField;
/*     */ import net.minecraft.client.gui.ScaledResolution;
/*     */ import net.minecraft.client.resources.I18n;
/*     */ import org.lwjgl.input.Keyboard;
/*     */ import xaero.common.IXaeroMinimap;
/*     */ import xaero.common.minimap.waypoints.WaypointWorld;
/*     */ import xaero.common.minimap.waypoints.WaypointsManager;
/*     */ 
/*     */ 
/*     */ public class GuiNewSet
/*     */   extends GuiScreen
/*     */ {
/*     */   private GuiScreen parentGuiScreen;
/*     */   protected String screenTitle;
/*     */   private GuiTextField nameTextField;
/*  21 */   private String nameText = "";
/*     */   private IXaeroMinimap modMain;
/*     */   private WaypointsManager waypointsManager;
/*     */   private WaypointWorld waypointWorld;
/*     */   
/*     */   public GuiNewSet(IXaeroMinimap modMain, GuiScreen par1GuiScreen, WaypointWorld waypointWorld) {
/*  27 */     this.modMain = modMain;
/*  28 */     this.waypointsManager = modMain.getWaypointsManager();
/*  29 */     this.parentGuiScreen = par1GuiScreen;
/*  30 */     this.waypointWorld = waypointWorld;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void func_73866_w_() {
/*  38 */     this.screenTitle = I18n.func_135052_a("gui.xaero_create_set", new Object[0]);
/*  39 */     this.field_146292_n.clear();
/*  40 */     this.field_146292_n.add(new MySmallButton(200, this.field_146294_l / 2 - 155, this.field_146295_m / 6 + 168, I18n.func_135052_a("gui.xaero_confirm", new Object[0])));
/*  41 */     this.field_146292_n.add(new MySmallButton(201, this.field_146294_l / 2 + 5, this.field_146295_m / 6 + 168, I18n.func_135052_a("gui.xaero_cancel", new Object[0])));
/*  42 */     this.nameTextField = new GuiTextField(0, this.field_146289_q, this.field_146294_l / 2 - 100, 60, 200, 20);
/*  43 */     this.nameTextField.func_146180_a(this.nameText);
/*  44 */     this.nameTextField.func_146195_b(true);
/*  45 */     Keyboard.enableRepeatEvents(true);
/*  46 */     updateConfirmButton();
/*     */   }
/*     */ 
/*     */   
/*     */   public void func_146281_b() {
/*  51 */     Keyboard.enableRepeatEvents(false);
/*     */   }
/*     */   
/*     */   private boolean canConfirm() {
/*  55 */     return (this.nameTextField.func_146179_b().length() > 0 && this.waypointWorld.getSets().get(this.nameTextField.func_146179_b()) == null);
/*     */   }
/*     */   
/*     */   private void updateConfirmButton() {
/*  59 */     ((GuiButton)this.field_146292_n.get(0)).field_146124_l = canConfirm();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void func_73869_a(char par1, int par2) throws IOException {
/*  65 */     if (this.nameTextField.func_146206_l()) {
/*     */       
/*  67 */       this.nameTextField.func_146201_a(par1, par2);
/*  68 */       this.nameTextField.func_146180_a(this.nameText = this.nameTextField.func_146179_b());
/*  69 */       updateConfirmButton();
/*     */     } 
/*  71 */     if (par2 == 28 || par2 == 156)
/*     */     {
/*  73 */       func_146284_a(this.field_146292_n.get(0));
/*     */     }
/*     */     
/*  76 */     super.func_73869_a(par1, par2);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void func_73864_a(int par1, int par2, int par3) throws IOException {
/*  81 */     super.func_73864_a(par1, par2, par3);
/*  82 */     this.nameTextField.func_146192_a(par1, par2, par3);
/*     */   }
/*     */ 
/*     */   
/*     */   public void func_73876_c() {
/*  87 */     this.nameTextField.func_146178_a();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void func_146284_a(GuiButton par1GuiButton) {
/*  92 */     if (par1GuiButton.field_146124_l) {
/*     */       
/*  94 */       int var2 = this.field_146297_k.field_71474_y.field_74335_Z;
/*     */       
/*  96 */       if (par1GuiButton.field_146127_k == 200)
/*     */       {
/*  98 */         if (canConfirm()) {
/*  99 */           String setName = this.nameTextField.func_146179_b().replace(":", "§§");
/* 100 */           this.waypointWorld.setCurrent(setName);
/* 101 */           this.waypointWorld.addSet(setName);
/* 102 */           this.waypointsManager.updateWaypoints();
/*     */           try {
/* 104 */             this.modMain.getSettings().saveWaypoints(this.waypointWorld);
/* 105 */           } catch (IOException e) {
/* 106 */             e.printStackTrace();
/*     */           } 
/* 108 */           this.field_146297_k.func_147108_a(new GuiWaypoints(this.modMain, ((GuiWaypoints)this.parentGuiScreen).getParentScreen()));
/*     */         } 
/*     */       }
/*     */       
/* 112 */       if (par1GuiButton.field_146127_k == 201)
/*     */       {
/* 114 */         this.field_146297_k.func_147108_a(this.parentGuiScreen);
/*     */       }
/*     */       
/* 117 */       if (this.field_146297_k.field_71474_y.field_74335_Z != var2) {
/*     */         
/* 119 */         ScaledResolution var3 = new ScaledResolution(this.field_146297_k);
/* 120 */         int var4 = var3.func_78326_a();
/* 121 */         int var5 = var3.func_78328_b();
/* 122 */         func_146280_a(this.field_146297_k, var4, var5);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void func_73863_a(int par1, int par2, float par3) {
/* 129 */     func_146276_q_();
/* 130 */     func_73732_a(this.field_146289_q, this.screenTitle, this.field_146294_l / 2, 20, 16777215);
/* 131 */     this.nameTextField.func_146194_f();
/* 132 */     super.func_73863_a(par1, par2, par3);
/*     */   }
/*     */ }


/* Location:              C:\Users\ioanm\Downloads\Xaeros_Minimap_20.15.3_Forge_1.12.jar!\xaero\common\gui\GuiNewSet.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */