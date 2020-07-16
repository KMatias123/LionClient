/*     */ package xaero.map.gui;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.attribute.FileAttribute;
/*     */ import net.minecraft.client.gui.GuiButton;
/*     */ import net.minecraft.client.gui.GuiScreen;
/*     */ import net.minecraft.client.gui.GuiTextField;
/*     */ import net.minecraft.client.resources.I18n;
/*     */ import org.lwjgl.input.Keyboard;
/*     */ import xaero.map.MapProcessor;
/*     */ import xaero.map.world.MapDimension;
/*     */ 
/*     */ public class GuiMapName
/*     */   extends GuiScreen
/*     */ {
/*     */   private GuiScreen parentGuiScreen;
/*     */   protected String screenTitle;
/*     */   private GuiTextField nameTextField;
/*     */   private MapDimension mapDimension;
/*     */   private String editingMWId;
/*     */   private String currentNameFieldContent;
/*     */   
/*     */   public GuiMapName(GuiScreen par1GuiScreen, MapDimension mapDimension, String editingMWId) {
/*  26 */     this.parentGuiScreen = par1GuiScreen;
/*  27 */     this.mapDimension = mapDimension;
/*  28 */     this.editingMWId = editingMWId;
/*  29 */     this.currentNameFieldContent = (editingMWId == null) ? "" : mapDimension.getMultiworldName(editingMWId);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void func_73866_w_() {
/*  38 */     super.func_73866_w_();
/*  39 */     this.screenTitle = I18n.func_135052_a("gui.xaero_map_name", new Object[0]);
/*  40 */     if (this.nameTextField != null)
/*  41 */       this.currentNameFieldContent = this.nameTextField.func_146179_b(); 
/*  42 */     this.nameTextField = new GuiTextField(0, this.field_146289_q, this.field_146294_l / 2 - 100, 60, 200, 20);
/*  43 */     this.nameTextField.func_146180_a(this.currentNameFieldContent);
/*  44 */     this.nameTextField.func_146195_b(true);
/*  45 */     func_189646_b(new MySmallButton(200, this.field_146294_l / 2 - 155, this.field_146295_m / 6 + 168, I18n.func_135052_a("gui.xaero_confirm", new Object[0])));
/*  46 */     func_189646_b(new MySmallButton(201, this.field_146294_l / 2 + 5, this.field_146295_m / 6 + 168, I18n.func_135052_a("gui.xaero_cancel", new Object[0])));
/*  47 */     Keyboard.enableRepeatEvents(true);
/*  48 */     updateConfirmButton();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void func_146284_a(GuiButton button) throws IOException {
/*  53 */     if (button.field_146124_l) {
/*  54 */       if (button.field_146127_k == 200) {
/*  55 */         if (canConfirm()) {
/*  56 */           synchronized (MapProcessor.instance.uiSync) {
/*  57 */             if (MapProcessor.instance.getLastNonNullWorld() == this.mapDimension.getMapWorld()) {
/*  58 */               String mwIdFixed, unfilteredName = this.nameTextField.func_146179_b();
/*     */               
/*  60 */               if (this.editingMWId == null) {
/*  61 */                 String mwId = unfilteredName.toLowerCase().replaceAll("[^a-z0-9]+", "");
/*  62 */                 if (mwId.isEmpty())
/*  63 */                   mwId = "map"; 
/*  64 */                 mwId = "mw$" + mwId;
/*  65 */                 boolean mwAdded = false;
/*  66 */                 mwIdFixed = mwId;
/*  67 */                 int fix = 1;
/*  68 */                 while (!mwAdded) {
/*  69 */                   mwAdded = this.mapDimension.addMultiworldChecked(mwIdFixed);
/*  70 */                   if (!mwAdded) {
/*  71 */                     fix++;
/*  72 */                     mwIdFixed = mwId + fix;
/*     */                   } 
/*     */                 } 
/*  75 */                 Path dimensionFolderPath = this.mapDimension.getMainFolderPath();
/*  76 */                 Path multiworldFolderPath = dimensionFolderPath.resolve(mwIdFixed);
/*     */                 try {
/*  78 */                   Files.createDirectories(multiworldFolderPath, (FileAttribute<?>[])new FileAttribute[0]);
/*  79 */                 } catch (IOException e) {
/*  80 */                   e.printStackTrace();
/*     */                 } 
/*  82 */                 this.mapDimension.setMultiworldUnsynced(mwIdFixed);
/*     */               } else {
/*  84 */                 mwIdFixed = this.editingMWId;
/*     */               } 
/*  86 */               this.mapDimension.setMultiworldName(mwIdFixed, unfilteredName);
/*  87 */               this.mapDimension.saveConfig();
/*  88 */               this.field_146297_k.func_147108_a(this.parentGuiScreen);
/*     */             } 
/*     */           } 
/*     */         }
/*  92 */       } else if (button.field_146127_k == 201) {
/*  93 */         this.field_146297_k.func_147108_a(this.parentGuiScreen);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void func_146281_b() {
/* 100 */     Keyboard.enableRepeatEvents(false);
/*     */   }
/*     */   
/*     */   private boolean canConfirm() {
/* 104 */     return (this.nameTextField.func_146179_b().length() > 0);
/*     */   }
/*     */   
/*     */   private void updateConfirmButton() {
/* 108 */     ((GuiButton)this.field_146292_n.get(0)).field_146124_l = canConfirm();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void func_73869_a(char par1, int par2) throws IOException {
/* 115 */     super.func_73869_a(par1, par2);
/* 116 */     if (this.nameTextField.func_146206_l()) {
/*     */       
/* 118 */       this.nameTextField.func_146201_a(par1, par2);
/* 119 */       updateConfirmButton();
/*     */     } 
/* 121 */     if ((par2 == 28 || par2 == 156) && canConfirm())
/*     */     {
/* 123 */       func_146284_a(this.field_146292_n.get(0));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void func_73876_c() {
/* 130 */     updateConfirmButton();
/* 131 */     this.nameTextField.func_146178_a();
/*     */   }
/*     */ 
/*     */   
/*     */   public void func_73863_a(int par1, int par2, float par3) {
/* 136 */     func_146276_q_();
/* 137 */     func_73732_a(this.field_146289_q, this.screenTitle, this.field_146294_l / 2, 20, 16777215);
/* 138 */     this.nameTextField.func_146194_f();
/* 139 */     super.func_73863_a(par1, par2, par3);
/*     */   }
/*     */ }


/* Location:              C:\Users\ioanm\Downloads\XaerosWorldMap_1.7.3_Forge_1.12.jar!\xaero\map\gui\GuiMapName.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */