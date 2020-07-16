/*     */ package xaero.common.gui;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import net.minecraft.client.gui.GuiButton;
/*     */ import net.minecraft.client.gui.GuiScreen;
/*     */ import net.minecraft.client.gui.ScaledResolution;
/*     */ import net.minecraft.client.resources.I18n;
/*     */ import org.lwjgl.input.Mouse;
/*     */ import xaero.common.IXaeroMinimap;
/*     */ import xaero.common.settings.ModOptions;
/*     */ import xaero.common.settings.ModSettings;
/*     */ 
/*     */ public class GuiDotColors
/*     */   extends GuiScreen
/*     */   implements IDropDownCallback
/*     */ {
/*     */   private IXaeroMinimap modMain;
/*     */   private GuiScreen parentGuiScreen;
/*     */   protected String screenTitle;
/*     */   private ArrayList<GuiDropDown> dropDowns;
/*     */   private String[] colorOptions;
/*     */   private int mobsColor;
/*     */   private int hostileColor;
/*     */   private int itemsColor;
/*     */   private int otherColor;
/*     */   private int playerOption;
/*     */   private int teamOption;
/*     */   private GuiDropDown mobsColorDD;
/*     */   private GuiDropDown hostileColorDD;
/*     */   private GuiDropDown itemsColorDD;
/*     */   private GuiDropDown otherColorDD;
/*     */   private GuiDropDown playerOptionDD;
/*     */   private GuiDropDown teamOptionDD;
/*     */   private boolean[] displaySettingsChanged;
/*     */   private boolean dropped;
/*     */   
/*     */   public GuiDotColors(IXaeroMinimap modMain, GuiScreen par1GuiScreen) {
/*  39 */     this.modMain = modMain;
/*  40 */     this.parentGuiScreen = par1GuiScreen;
/*  41 */     this.dropDowns = new ArrayList<>();
/*  42 */     this.displaySettingsChanged = new boolean[6];
/*  43 */     this.colorOptions = createColorOptions();
/*  44 */     this.mobsColor = (modMain.getSettings()).mobsColor;
/*  45 */     this.hostileColor = (modMain.getSettings()).hostileColor;
/*  46 */     this.itemsColor = (modMain.getSettings()).itemsColor;
/*  47 */     this.otherColor = (modMain.getSettings()).otherColor;
/*  48 */     this.playerOption = ((modMain.getSettings()).playersColor != -1) ? (modMain.getSettings()).playersColor : this.colorOptions.length;
/*  49 */     this.teamOption = ((modMain.getSettings()).otherTeamColor != -1) ? ((modMain.getSettings()).otherTeamColor + 1) : 0;
/*     */   }
/*     */   
/*     */   private String getButtonText(ModOptions par1EnumOptions) {
/*  53 */     String s = "";
/*  54 */     int id = settingIdForEnum(par1EnumOptions);
/*  55 */     boolean changed = this.displaySettingsChanged[id];
/*  56 */     boolean currentClientSetting = this.modMain.getSettings().getClientBooleanValue(par1EnumOptions);
/*  57 */     boolean clientSetting = changed ? (!currentClientSetting) : currentClientSetting;
/*  58 */     boolean serverSetting = this.modMain.getSettings().getBooleanValue(par1EnumOptions);
/*     */     
/*  60 */     s = s + ModSettings.getTranslation(clientSetting) + ((serverSetting != currentClientSetting) ? ("§e (" + ModSettings.getTranslation(serverSetting) + ")") : "");
/*  61 */     return s;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void func_73866_w_() {
/*  68 */     this.screenTitle = I18n.func_135052_a("gui.xaero_entity_radar", new Object[0]);
/*  69 */     this.field_146292_n.clear();
/*  70 */     this.field_146292_n.add(new MySmallButton(200, this.field_146294_l / 2 - 155, this.field_146295_m / 6 + 168, 
/*  71 */           I18n.func_135052_a("gui.xaero_confirm", new Object[0])));
/*  72 */     this.field_146292_n.add(new MySmallButton(201, this.field_146294_l / 2 + 5, this.field_146295_m / 6 + 168, 
/*  73 */           I18n.func_135052_a("gui.xaero_cancel", new Object[0])));
/*  74 */     this.dropDowns.clear();
/*  75 */     String[] playerOptions = new String[this.colorOptions.length + 1];
/*  76 */     String[] teamOptions = new String[this.colorOptions.length + 1];
/*  77 */     for (int i = 0; i < this.colorOptions.length; i++) {
/*  78 */       playerOptions[i] = this.colorOptions[i];
/*  79 */       teamOptions[i + 1] = this.colorOptions[i];
/*     */     } 
/*  81 */     playerOptions[this.colorOptions.length] = "gui.xaero_team_colours";
/*  82 */     teamOptions[0] = "gui.xaero_players";
/*  83 */     this.dropDowns.add(this.mobsColorDD = new GuiDropDown(this.colorOptions, this.field_146294_l / 2 - 60, this.field_146295_m / 7 + 70, 120, Integer.valueOf(this.mobsColor), this));
/*  84 */     this.field_146292_n.add(new MyTinyButton(ModOptions.MOBS.returnEnumOrdinal(), this.field_146294_l / 2 + 63, this.field_146295_m / 7 + 66, ModOptions.MOBS, 
/*  85 */           getButtonText(ModOptions.MOBS)));
/*  86 */     this.dropDowns.add(this.hostileColorDD = new GuiDropDown(this.colorOptions, this.field_146294_l / 2 - 60, this.field_146295_m / 7 + 95, 120, Integer.valueOf(this.hostileColor), this));
/*  87 */     this.field_146292_n.add(new MyTinyButton(ModOptions.HOSTILE.returnEnumOrdinal(), this.field_146294_l / 2 + 63, this.field_146295_m / 7 + 91, ModOptions.HOSTILE, 
/*  88 */           getButtonText(ModOptions.HOSTILE)));
/*  89 */     this.dropDowns.add(this.itemsColorDD = new GuiDropDown(this.colorOptions, this.field_146294_l / 2 - 60, this.field_146295_m / 7 + 120, 120, Integer.valueOf(this.itemsColor), this));
/*  90 */     this.field_146292_n.add(new MyTinyButton(ModOptions.ITEMS.returnEnumOrdinal(), this.field_146294_l / 2 + 63, this.field_146295_m / 7 + 116, ModOptions.ITEMS, 
/*  91 */           getButtonText(ModOptions.ITEMS)));
/*  92 */     this.dropDowns.add(this.otherColorDD = new GuiDropDown(this.colorOptions, this.field_146294_l / 2 - 60, this.field_146295_m / 7 + 145, 120, Integer.valueOf(this.otherColor), this));
/*  93 */     this.field_146292_n.add(new MyTinyButton(ModOptions.ENTITIES.returnEnumOrdinal(), this.field_146294_l / 2 + 63, this.field_146295_m / 7 + 141, ModOptions.ENTITIES, 
/*  94 */           getButtonText(ModOptions.ENTITIES)));
/*  95 */     this.dropDowns.add(this.playerOptionDD = new GuiDropDown(playerOptions, this.field_146294_l / 2 - 60, this.field_146295_m / 7 + 20, 120, Integer.valueOf(this.playerOption), this));
/*  96 */     this.field_146292_n.add(new MyTinyButton(ModOptions.PLAYERS.returnEnumOrdinal(), this.field_146294_l / 2 + 63, this.field_146295_m / 7 + 16, ModOptions.PLAYERS, 
/*  97 */           getButtonText(ModOptions.PLAYERS)));
/*  98 */     this.dropDowns.add(this.teamOptionDD = new GuiDropDown(teamOptions, this.field_146294_l / 2 - 60, this.field_146295_m / 7 + 45, 120, Integer.valueOf(this.teamOption), this));
/*  99 */     this.field_146292_n.add(new MyTinyButton(ModOptions.DISPLAY_OTHER_TEAM.returnEnumOrdinal(), this.field_146294_l / 2 + 63, this.field_146295_m / 7 + 41, ModOptions.DISPLAY_OTHER_TEAM, 
/* 100 */           getButtonText(ModOptions.DISPLAY_OTHER_TEAM)));
/*     */   }
/*     */   
/*     */   private String[] createColorOptions() {
/* 104 */     String[] options = new String[ModSettings.ENCHANT_COLOR_NAMES.length];
/* 105 */     for (int i = 0; i < options.length; i++) {
/* 106 */       if (i == 0) {
/* 107 */         options[i] = I18n.func_135052_a(ModSettings.ENCHANT_COLOR_NAMES[i], new Object[0]);
/*     */       } else {
/* 109 */         options[i] = "§" + ModSettings.ENCHANT_COLORS[i] + 
/* 110 */           I18n.func_135052_a(ModSettings.ENCHANT_COLOR_NAMES[i], new Object[0]);
/*     */       } 
/* 112 */     }  return options;
/*     */   }
/*     */   
/*     */   private ModOptions settingEnumForId(int opt) {
/* 116 */     switch (opt) {
/*     */       case 0:
/* 118 */         return ModOptions.PLAYERS;
/*     */       case 1:
/* 120 */         return ModOptions.DISPLAY_OTHER_TEAM;
/*     */       case 2:
/* 122 */         return ModOptions.MOBS;
/*     */       case 3:
/* 124 */         return ModOptions.HOSTILE;
/*     */       case 4:
/* 126 */         return ModOptions.ITEMS;
/*     */     } 
/* 128 */     return ModOptions.ENTITIES;
/*     */   }
/*     */   
/*     */   private int settingIdForEnum(ModOptions opt) {
/* 132 */     if (opt == ModOptions.PLAYERS)
/* 133 */       return 0; 
/* 134 */     if (opt == ModOptions.DISPLAY_OTHER_TEAM)
/* 135 */       return 1; 
/* 136 */     if (opt == ModOptions.MOBS)
/* 137 */       return 2; 
/* 138 */     if (opt == ModOptions.HOSTILE)
/* 139 */       return 3; 
/* 140 */     if (opt == ModOptions.ITEMS)
/* 141 */       return 4; 
/* 142 */     return 5;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void func_146284_a(GuiButton par1GuiButton) {
/* 152 */     if (par1GuiButton.field_146124_l) {
/* 153 */       int var2 = this.field_146297_k.field_71474_y.field_74335_Z;
/*     */       
/* 155 */       if (par1GuiButton.field_146127_k < 100 && par1GuiButton instanceof MyTinyButton) {
/* 156 */         boolean changed = this.displaySettingsChanged[settingIdForEnum(((MyTinyButton)par1GuiButton).returnModOptions())];
/* 157 */         this.displaySettingsChanged[settingIdForEnum(((MyTinyButton)par1GuiButton).returnModOptions())] = !changed;
/* 158 */         par1GuiButton.field_146126_j = getButtonText(((MyTinyButton)par1GuiButton).returnModOptions());
/*     */       } 
/*     */ 
/*     */       
/* 162 */       if (par1GuiButton.field_146127_k == 200) {
/* 163 */         (this.modMain.getSettings()).mobsColor = this.mobsColor;
/* 164 */         (this.modMain.getSettings()).hostileColor = this.hostileColor;
/* 165 */         (this.modMain.getSettings()).itemsColor = this.itemsColor;
/* 166 */         (this.modMain.getSettings()).otherColor = this.otherColor;
/* 167 */         (this.modMain.getSettings()).playersColor = (this.playerOption < this.colorOptions.length) ? this.playerOption : -1;
/* 168 */         (this.modMain.getSettings()).otherTeamColor = (this.teamOption > 0) ? (this.teamOption - 1) : -1;
/*     */         
/* 170 */         for (int opt = 0; opt < this.displaySettingsChanged.length; opt++) {
/* 171 */           if (this.displaySettingsChanged[opt])
/*     */             try {
/* 173 */               this.modMain.getSettings().setOptionValue(settingEnumForId(opt), 1);
/* 174 */             } catch (IOException e) {
/* 175 */               e.printStackTrace();
/*     */             }  
/*     */         } 
/*     */         try {
/* 179 */           this.modMain.getSettings().saveSettings();
/* 180 */         } catch (IOException e) {
/* 181 */           e.printStackTrace();
/*     */         } 
/* 183 */         this.field_146297_k.func_147108_a(this.parentGuiScreen);
/*     */       } 
/* 185 */       if (par1GuiButton.field_146127_k == 201) {
/* 186 */         this.field_146297_k.func_147108_a(this.parentGuiScreen);
/*     */       }
/*     */       
/* 189 */       if (this.field_146297_k.field_71474_y.field_74335_Z != var2) {
/* 190 */         ScaledResolution var3 = new ScaledResolution(this.field_146297_k);
/* 191 */         int var4 = var3.func_78326_a();
/* 192 */         int var5 = var3.func_78328_b();
/* 193 */         func_146280_a(this.field_146297_k, var4, var5);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void func_73864_a(int par1, int par2, int par3) throws IOException {
/* 199 */     for (GuiDropDown d : this.dropDowns) {
/* 200 */       if (!d.isClosed() && d.onDropDown(par1, par2, this.field_146295_m)) {
/* 201 */         d.mouseClicked(par1, par2, par3, this.field_146295_m);
/*     */         return;
/*     */       } 
/* 204 */       d.setClosed(true);
/*     */     } 
/* 206 */     for (GuiDropDown d : this.dropDowns) {
/* 207 */       if (d.onDropDown(par1, par2, this.field_146295_m)) {
/* 208 */         d.mouseClicked(par1, par2, par3, this.field_146295_m);
/*     */         return;
/*     */       } 
/* 211 */       d.setClosed(true);
/*     */     } 
/* 213 */     super.func_73864_a(par1, par2, par3);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void func_146286_b(int par1, int par2, int par3) {
/* 218 */     super.func_146286_b(par1, par2, par3);
/* 219 */     for (GuiDropDown d : this.dropDowns) {
/* 220 */       d.mouseReleased(par1, par2, par3, this.field_146295_m);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void func_73863_a(int par1, int par2, float par3) {
/* 228 */     func_146276_q_();
/* 229 */     func_73732_a(this.field_146289_q, this.screenTitle, this.field_146294_l / 2, 20, 16777215);
/*     */     
/* 231 */     func_73732_a(this.field_146289_q, I18n.func_135052_a("gui.xaero_players", new Object[0]) + ":", this.field_146294_l / 2, this.field_146295_m / 7 + 10, 16777215);
/*     */     
/* 233 */     func_73732_a(this.field_146289_q, I18n.func_135052_a("gui.xaero_other_teams", new Object[0]) + ":", this.field_146294_l / 2, this.field_146295_m / 7 + 35, 16777215);
/*     */     
/* 235 */     func_73732_a(this.field_146289_q, I18n.func_135052_a("gui.xaero_mobs", new Object[0]) + ":", this.field_146294_l / 2, this.field_146295_m / 7 + 60, 16777215);
/*     */     
/* 237 */     func_73732_a(this.field_146289_q, I18n.func_135052_a("gui.xaero_hostile", new Object[0]) + ":", this.field_146294_l / 2, this.field_146295_m / 7 + 85, 16777215);
/*     */     
/* 239 */     func_73732_a(this.field_146289_q, I18n.func_135052_a("gui.xaero_items", new Object[0]) + ":", this.field_146294_l / 2, this.field_146295_m / 7 + 110, 16777215);
/*     */     
/* 241 */     func_73732_a(this.field_146289_q, I18n.func_135052_a("gui.xaero_other", new Object[0]) + ":", this.field_146294_l / 2, this.field_146295_m / 7 + 135, 16777215);
/*     */     
/* 243 */     if (this.dropped) {
/* 244 */       super.func_73863_a(0, 0, par3);
/*     */     } else {
/* 246 */       super.func_73863_a(par1, par2, par3);
/* 247 */     }  this.dropped = false; int k;
/* 248 */     for (k = 0; k < this.dropDowns.size(); k++) {
/* 249 */       if (((GuiDropDown)this.dropDowns.get(k)).isClosed()) {
/* 250 */         ((GuiDropDown)this.dropDowns.get(k)).drawButton(par1, par2, this.field_146295_m);
/*     */       } else {
/* 252 */         this.dropped = true;
/*     */       } 
/* 254 */     }  for (k = 0; k < this.dropDowns.size(); k++) {
/* 255 */       if (!((GuiDropDown)this.dropDowns.get(k)).isClosed())
/* 256 */         ((GuiDropDown)this.dropDowns.get(k)).drawButton(par1, par2, this.field_146295_m); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void func_146274_d() throws IOException {
/* 261 */     super.func_146274_d();
/* 262 */     int wheel = Mouse.getEventDWheel() / 120;
/* 263 */     if (wheel != 0) {
/* 264 */       ScaledResolution var3 = new ScaledResolution(this.field_146297_k);
/* 265 */       int mouseXScaled = Mouse.getX() / var3.func_78325_e();
/* 266 */       int mouseYScaled = var3.func_78328_b() - 1 - Mouse.getY() / var3.func_78325_e();
/* 267 */       for (GuiDropDown d : this.dropDowns) {
/* 268 */         if (!d.isClosed() && d.onDropDown(mouseXScaled, mouseYScaled, this.field_146295_m)) {
/* 269 */           d.mouseScrolled(wheel, mouseXScaled, mouseYScaled, this.field_146295_m);
/*     */           return;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean onSelected(GuiDropDown menu, int selected) {
/* 278 */     if (menu == this.mobsColorDD) {
/* 279 */       this.mobsColor = selected;
/* 280 */     } else if (menu == this.hostileColorDD) {
/* 281 */       this.hostileColor = selected;
/* 282 */     } else if (menu == this.itemsColorDD) {
/* 283 */       this.itemsColor = selected;
/* 284 */     } else if (menu == this.otherColorDD) {
/* 285 */       this.otherColor = selected;
/* 286 */     } else if (menu == this.playerOptionDD) {
/* 287 */       this.playerOption = selected;
/* 288 */     } else if (menu == this.teamOptionDD) {
/* 289 */       this.teamOption = selected;
/* 290 */     }  return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\ioanm\Downloads\Xaeros_Minimap_20.15.3_Forge_1.12.jar!\xaero\common\gui\GuiDotColors.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */