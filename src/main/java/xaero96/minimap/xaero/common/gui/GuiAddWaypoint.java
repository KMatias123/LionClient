/*     */ package xaero.common.gui;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.FontRenderer;
/*     */ import net.minecraft.client.gui.GuiButton;
/*     */ import net.minecraft.client.gui.GuiScreen;
/*     */ import net.minecraft.client.gui.GuiTextField;
/*     */ import net.minecraft.client.gui.ScaledResolution;
/*     */ import net.minecraft.client.resources.I18n;
/*     */ import org.lwjgl.input.Keyboard;
/*     */ import org.lwjgl.input.Mouse;
/*     */ import xaero.common.IXaeroMinimap;
/*     */ import xaero.common.minimap.waypoints.Waypoint;
/*     */ import xaero.common.minimap.waypoints.WaypointSet;
/*     */ import xaero.common.minimap.waypoints.WaypointWorld;
/*     */ import xaero.common.minimap.waypoints.WaypointsManager;
/*     */ import xaero.common.misc.CustomTextFieldFontRendererWrapper;
/*     */ import xaero.common.misc.OptimizedMath;
/*     */ import xaero.common.settings.ModOptions;
/*     */ import xaero.common.settings.ModSettings;
/*     */ import xaero.common.validator.NumericFieldValidator;
/*     */ 
/*     */ 
/*     */ public class GuiAddWaypoint
/*     */   extends GuiScreen
/*     */   implements IDropDownCallback
/*     */ {
/*     */   private IXaeroMinimap modMain;
/*     */   private WaypointsManager waypointsManager;
/*     */   private GuiScreen parentGuiScreen;
/*     */   protected String screenTitle;
/*     */   
/*  38 */   private static final Function<WaypointEditForm, String> NAME_VALUE = new Function<WaypointEditForm, String>()
/*     */     {
/*     */       public String apply(WaypointEditForm w) {
/*  41 */         return w.getName();
/*     */       }
/*     */     };
/*  44 */   private static final Function<WaypointEditForm, String> X_VALUE = new Function<WaypointEditForm, String>()
/*     */     {
/*     */       public String apply(WaypointEditForm w) {
/*  47 */         return w.getxText();
/*     */       }
/*     */     };
/*  50 */   private static final Function<WaypointEditForm, String> Y_VALUE = new Function<WaypointEditForm, String>()
/*     */     {
/*     */       public String apply(WaypointEditForm w) {
/*  53 */         return w.getyText();
/*     */       }
/*     */     };
/*  56 */   private static final Function<WaypointEditForm, String> Z_VALUE = new Function<WaypointEditForm, String>()
/*     */     {
/*     */       public String apply(WaypointEditForm w) {
/*  59 */         return w.getzText();
/*     */       }
/*     */     };
/*  62 */   private static final Function<WaypointEditForm, String> YAW_VALUE = new Function<WaypointEditForm, String>()
/*     */     {
/*     */       public String apply(WaypointEditForm w) {
/*  65 */         return w.getYawText();
/*     */       }
/*     */     };
/*  68 */   private static final Function<WaypointEditForm, String> INITIAL_VALUE = new Function<WaypointEditForm, String>()
/*     */     {
/*     */       public String apply(WaypointEditForm w) {
/*  71 */         return w.getInitial();
/*     */       }
/*     */     };
/*  74 */   private static final Function<WaypointEditForm, Boolean> DISABLED_VALUE = new Function<WaypointEditForm, Boolean>()
/*     */     {
/*     */       public Boolean apply(WaypointEditForm w) {
/*  77 */         return Boolean.valueOf(w.isDisabled());
/*     */       }
/*     */     };
/*  80 */   private static final Function<WaypointEditForm, Boolean> GLOBAL_VALUE = new Function<WaypointEditForm, Boolean>()
/*     */     {
/*     */       public Boolean apply(WaypointEditForm w) {
/*  83 */         return Boolean.valueOf(w.isGlobal());
/*     */       }
/*     */     };
/*  86 */   private static final Function<WaypointEditForm, Integer> COLOR_VALUE = new Function<WaypointEditForm, Integer>()
/*     */     {
/*     */       public Integer apply(WaypointEditForm w) {
/*  89 */         return Integer.valueOf(w.getColor());
/*     */       }
/*     */     };
/*     */ 
/*     */   
/*     */   private GuiButton leftButton;
/*     */   private GuiButton rightButton;
/*     */   private GuiButton modeSwitchButton;
/*     */   private GuiTextField nameTextField;
/*     */   private GuiTextField xTextField;
/*     */   private GuiTextField yTextField;
/*     */   private GuiTextField zTextField;
/*     */   private GuiTextField yawTextField;
/*     */   private GuiTextField initialTextField;
/*     */   private WaypointEditForm mutualForm;
/*     */   private ArrayList<WaypointEditForm> editForms;
/*     */   private int selectedWaypointIndex;
/*     */   private ArrayList<GuiDropDown> dropDowns;
/*     */   private int defaultContainer;
/*     */   private WaypointWorld defaultWorld;
/*     */   private GuiWaypointContainers containers;
/*     */   private GuiWaypointWorlds worlds;
/*     */   private GuiWaypointSets sets;
/*     */   private GuiDropDown containersDD;
/*     */   private GuiDropDown worldsDD;
/*     */   private GuiDropDown setsDD;
/*     */   private GuiDropDown colorDD;
/*     */   private String fromSet;
/*     */   private ArrayList<Waypoint> waypointsEdited;
/*     */   private boolean dropped;
/*     */   private MySuperTinyButton disableButton;
/*     */   private MySuperTinyButton globalButton;
/*     */   private NumericFieldValidator fieldValidator;
/*     */   private boolean adding;
/*     */   private boolean prefilled;
/*     */   private boolean startPrefilled;
/*     */   private String namePlaceholder;
/*     */   private String xPlaceholder;
/*     */   private String yPlaceholder;
/*     */   private String zPlaceholder;
/*     */   private String yawPlaceholder;
/*     */   private String initialPlaceholder;
/*     */   private String colorPlaceholder;
/*     */   private GuiButton defaultYawButton;
/*     */   private GuiButton defaultDisabledButton;
/*     */   private GuiButton defaultGlobalButton;
/*     */   private int multiplyDefaultValueBy8Counter;
/*     */   private boolean censorCoordsIfNeeded;
/*     */   private CustomTextFieldFontRendererWrapper censoredTextFormatter;
/*     */   
/*     */   public GuiAddWaypoint(IXaeroMinimap modMain, GuiScreen par1GuiScreen, Waypoint point, String defaultParentContainer, WaypointWorld defaultWorld) {
/* 140 */     this(modMain, par1GuiScreen, (point == null) ? new ArrayList<>() : Lists.newArrayList((Object[])new Waypoint[] { point }, ), defaultParentContainer, defaultWorld, (point == null || point.getActualColor() == -1));
/*     */   }
/*     */   
/*     */   public GuiAddWaypoint(IXaeroMinimap modMain, GuiScreen par1GuiScreen, ArrayList<Waypoint> waypointsEdited, String defaultParentContainer, WaypointWorld defaultWorld, boolean adding) {
/* 144 */     this.parentGuiScreen = par1GuiScreen;
/* 145 */     this.waypointsEdited = waypointsEdited;
/* 146 */     this.modMain = modMain;
/* 147 */     this.waypointsManager = modMain.getWaypointsManager();
/* 148 */     this.fromSet = defaultWorld.getCurrent();
/* 149 */     this.defaultWorld = defaultWorld;
/* 150 */     this.containers = new GuiWaypointContainers(modMain, this.waypointsManager, defaultParentContainer);
/* 151 */     this.defaultContainer = this.containers.current;
/* 152 */     this.worlds = new GuiWaypointWorlds(this.waypointsManager.getWorldContainer(defaultParentContainer), this.waypointsManager, defaultWorld.getFullId());
/* 153 */     this.sets = new GuiWaypointSets(false, defaultWorld);
/* 154 */     this.dropDowns = new ArrayList<>();
/* 155 */     this.startPrefilled = this.prefilled = !waypointsEdited.isEmpty();
/*     */     
/* 157 */     createForms();
/* 158 */     this.fieldValidator = modMain.getFieldValidators().getNumericFieldValidator();
/* 159 */     this.adding = adding;
/*     */     
/* 161 */     this.namePlaceholder = "§8- " + I18n.func_135052_a("gui.xaero_waypoint_name", new Object[0]);
/* 162 */     this.xPlaceholder = "§8- x";
/* 163 */     this.yPlaceholder = "§8- y";
/* 164 */     this.zPlaceholder = "§8- z";
/* 165 */     this.yawPlaceholder = "§8- " + I18n.func_135052_a("gui.xaero_yaw", new Object[0]);
/* 166 */     this.initialPlaceholder = "§8- " + I18n.func_135052_a("gui.xaero_initial", new Object[0]);
/* 167 */     this.colorPlaceholder = "§8-";
/* 168 */     this.censorCoordsIfNeeded = true;
/* 169 */     this.censoredTextFormatter = new CustomTextFieldFontRendererWrapper(Minecraft.func_71410_x())
/*     */       {
/*     */         public String censor(String p_195610_0_) {
/* 172 */           if (!GuiAddWaypoint.this.censorCoordsIfNeeded)
/* 173 */             return p_195610_0_; 
/* 174 */           int formatIndex = p_195610_0_.indexOf("§".charAt(0));
/* 175 */           if (formatIndex == -1)
/* 176 */             return p_195610_0_.replaceAll("[^_]", "#"); 
/* 177 */           return p_195610_0_.substring(0, formatIndex).replaceAll(".", "#") + p_195610_0_.substring(formatIndex);
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   private void fillFormWaypoint(WaypointEditForm form, Waypoint w) {
/* 183 */     form.name = w.getLocalizedName();
/* 184 */     form.xText = w.getX() + "";
/* 185 */     form.yText = w.getY() + "";
/* 186 */     form.zText = w.getZ() + "";
/* 187 */     form.yawText = w.isRotation() ? (w.getYaw() + "") : "";
/* 188 */     form.initial = w.getSymbol() + "";
/* 189 */     form.disabled = w.isDisabled();
/* 190 */     form.color = 1 + ((w.getActualColor() == -1) ? (int)(Math.random() * (ModSettings.ENCHANT_COLORS.length - 1)) : w.getActualColor());
/* 191 */     form.global = w.isGlobal();
/*     */   }
/*     */   
/*     */   private void fillFormAutomatic(WaypointEditForm form) {
/* 195 */     boolean divideBy8 = this.waypointsManager.divideBy8(this.worlds.getCurrentKeys()[0]);
/* 196 */     Minecraft minecraft = Minecraft.func_71410_x();
/* 197 */     form.xText = "" + (OptimizedMath.myFloor(minecraft.field_71439_g.field_70165_t) * (divideBy8 ? 8 : 1));
/* 198 */     form.yText = "" + OptimizedMath.myFloor(minecraft.field_71439_g.field_70163_u);
/* 199 */     form.zText = "" + (OptimizedMath.myFloor(minecraft.field_71439_g.field_70161_v) * (divideBy8 ? 8 : 1));
/* 200 */     form.color = (int)(Math.random() * (ModSettings.ENCHANT_COLORS.length - 1)) + 1;
/*     */   }
/*     */   
/*     */   private void createForms() {
/* 204 */     this.editForms = new ArrayList<>();
/*     */ 
/*     */     
/* 207 */     this.mutualForm = new WaypointEditForm();
/*     */ 
/*     */     
/* 210 */     for (int i = 0; i < this.waypointsEdited.size(); i++) {
/* 211 */       Waypoint w = this.waypointsEdited.get(i);
/* 212 */       WaypointEditForm form = new WaypointEditForm();
/* 213 */       fillFormWaypoint(form, w);
/* 214 */       this.editForms.add(form);
/*     */     } 
/* 216 */     if (!this.startPrefilled) {
/* 217 */       WaypointEditForm createdForm = new WaypointEditForm();
/* 218 */       fillFormAutomatic(createdForm);
/* 219 */       this.editForms.add(createdForm);
/*     */     } 
/* 221 */     updateMutual();
/*     */   }
/*     */   
/*     */   private void resetCurrentForm() {
/* 225 */     if (this.selectedWaypointIndex >= this.waypointsEdited.size()) {
/* 226 */       WaypointEditForm freshForm = new WaypointEditForm();
/* 227 */       fillFormAutomatic(freshForm);
/* 228 */       this.editForms.set(this.selectedWaypointIndex, freshForm);
/*     */     } else {
/* 230 */       Waypoint w = this.waypointsEdited.get(this.selectedWaypointIndex);
/* 231 */       WaypointEditForm freshForm = new WaypointEditForm();
/* 232 */       fillFormWaypoint(freshForm, w);
/* 233 */       this.editForms.set(this.selectedWaypointIndex, freshForm);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void updateMutual() {
/* 238 */     String nameTextMutual = "";
/* 239 */     String initialMutual = "";
/* 240 */     String yawMutual = "";
/* 241 */     String xTextMutual = "";
/* 242 */     String yTextMutual = "";
/* 243 */     String zTextMutual = "";
/* 244 */     boolean waypointDisabledMutual = false;
/* 245 */     boolean waypointGlobalMutual = false;
/* 246 */     int colorMutual = 0;
/* 247 */     if (differentValues(COLOR_VALUE)) {
/* 248 */       colorMutual = 0;
/*     */     } else {
/* 250 */       colorMutual = ((WaypointEditForm)this.editForms.get(0)).color;
/*     */     } 
/* 252 */     xTextMutual = "";
/* 253 */     yTextMutual = "";
/* 254 */     zTextMutual = "";
/* 255 */     this.mutualForm.keepName = differentValues(NAME_VALUE);
/* 256 */     this.mutualForm.keepXText = differentValues(X_VALUE);
/* 257 */     this.mutualForm.keepYText = differentValues(Y_VALUE);
/* 258 */     this.mutualForm.keepZText = differentValues(Z_VALUE);
/* 259 */     this.mutualForm.defaultKeepYawText = this.mutualForm.keepYawText = differentValues(YAW_VALUE);
/* 260 */     this.mutualForm.keepInitial = differentValues(INITIAL_VALUE);
/* 261 */     this.mutualForm.defaultKeepDisabled = this.mutualForm.keepDisabled = differentValues(DISABLED_VALUE);
/* 262 */     this.mutualForm.defaultKeepGlobal = this.mutualForm.keepGlobal = differentValues(GLOBAL_VALUE);
/* 263 */     WaypointEditForm firstForm = this.editForms.get(0);
/* 264 */     if (!this.mutualForm.keepName)
/* 265 */       nameTextMutual = firstForm.name; 
/* 266 */     if (!this.mutualForm.keepXText)
/* 267 */       xTextMutual = firstForm.xText; 
/* 268 */     if (!this.mutualForm.keepYText)
/* 269 */       yTextMutual = firstForm.yText; 
/* 270 */     if (!this.mutualForm.keepZText)
/* 271 */       zTextMutual = firstForm.zText; 
/* 272 */     if (!this.mutualForm.keepYawText)
/* 273 */       yawMutual = firstForm.yawText; 
/* 274 */     if (!this.mutualForm.keepInitial)
/* 275 */       initialMutual = firstForm.initial; 
/* 276 */     if (!this.mutualForm.keepDisabled)
/* 277 */       waypointDisabledMutual = firstForm.disabled; 
/* 278 */     if (!this.mutualForm.keepGlobal) {
/* 279 */       waypointGlobalMutual = firstForm.global;
/*     */     }
/* 281 */     this.mutualForm.name = nameTextMutual;
/* 282 */     this.mutualForm.xText = xTextMutual;
/* 283 */     this.mutualForm.yText = yTextMutual;
/* 284 */     this.mutualForm.zText = zTextMutual;
/* 285 */     this.mutualForm.yawText = yawMutual;
/* 286 */     this.mutualForm.initial = initialMutual;
/* 287 */     this.mutualForm.disabled = waypointDisabledMutual;
/* 288 */     this.mutualForm.global = waypointGlobalMutual;
/* 289 */     this.mutualForm.color = colorMutual;
/*     */   }
/*     */   
/*     */   private void confirmMutual() {
/* 293 */     for (int i = 0; i < this.editForms.size(); i++) {
/* 294 */       WaypointEditForm individualForm = this.editForms.get(i);
/* 295 */       if (!this.mutualForm.keepName)
/* 296 */         individualForm.name = this.mutualForm.name; 
/* 297 */       if (!this.mutualForm.keepXText)
/* 298 */         individualForm.xText = this.mutualForm.xText; 
/* 299 */       if (!this.mutualForm.keepYText)
/* 300 */         individualForm.yText = this.mutualForm.yText; 
/* 301 */       if (!this.mutualForm.keepZText)
/* 302 */         individualForm.zText = this.mutualForm.zText; 
/* 303 */       if (!this.mutualForm.keepYawText)
/* 304 */         individualForm.yawText = this.mutualForm.yawText; 
/* 305 */       if (!this.mutualForm.keepInitial)
/* 306 */         individualForm.initial = this.mutualForm.initial; 
/* 307 */       if (!this.mutualForm.keepDisabled)
/* 308 */         individualForm.disabled = this.mutualForm.disabled; 
/* 309 */       if (!this.mutualForm.keepGlobal)
/* 310 */         individualForm.global = this.mutualForm.global; 
/* 311 */       if (this.mutualForm.color != 0)
/* 312 */         individualForm.color = this.mutualForm.color; 
/*     */     } 
/*     */   }
/*     */   
/*     */   private <T> boolean differentValues(Function<WaypointEditForm, T> s) {
/* 317 */     if (this.editForms.size() == 1)
/* 318 */       return false; 
/* 319 */     WaypointEditForm testWaypoint = this.editForms.get(0);
/* 320 */     for (int i = 1; i < this.editForms.size(); i++) {
/* 321 */       WaypointEditForm w = this.editForms.get(i);
/* 322 */       if (!s.apply(w).equals(s.apply(testWaypoint)))
/* 323 */         return true; 
/*     */     } 
/* 325 */     return false;
/*     */   }
/*     */   
/*     */   public String[] createColorOptions() {
/* 329 */     boolean unchangedOption = ((getCurrent()).color == 0);
/* 330 */     String[] options = new String[ModSettings.ENCHANT_COLOR_NAMES.length + (unchangedOption ? 1 : 0)];
/* 331 */     if (unchangedOption)
/* 332 */       options[0] = this.colorPlaceholder; 
/* 333 */     for (int i = 0; i < ModSettings.ENCHANT_COLOR_NAMES.length; i++) {
/* 334 */       if (i == 0) {
/* 335 */         options[i + (unchangedOption ? 1 : 0)] = I18n.func_135052_a(ModSettings.ENCHANT_COLOR_NAMES[i], new Object[0]);
/*     */       } else {
/* 337 */         options[i + (unchangedOption ? 1 : 0)] = "§" + ModSettings.ENCHANT_COLORS[i] + 
/* 338 */           I18n.func_135052_a(ModSettings.ENCHANT_COLOR_NAMES[i], new Object[0]);
/*     */       } 
/* 340 */     }  return options;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void func_73866_w_() {
/* 347 */     this.screenTitle = this.adding ? I18n.func_135052_a("gui.xaero_new_waypoint", new Object[0]) : I18n.func_135052_a("gui.xaero_edit_waypoint", new Object[0]);
/* 348 */     if (this.editForms.size() > 1)
/* 349 */       this.screenTitle += (this.editForms.size() > 1) ? (" (" + ((this.modMain.getSettings()).waypointsMutualEdit ? "" : ((this.selectedWaypointIndex + 1) + "/")) + this.editForms.size() + ")") : ""; 
/* 350 */     this.modMain.getInterfaces().setSelectedId(-1);
/* 351 */     this.modMain.getInterfaces().setDraggingId(-1);
/* 352 */     this.field_146292_n.clear();
/* 353 */     this.field_146292_n.add(new MySmallButton(200, this.field_146294_l / 2 - 155, this.field_146295_m / 6 + 168, 
/* 354 */           I18n.func_135052_a("gui.xaero_confirm", new Object[0])));
/* 355 */     this.field_146292_n.add(new MySmallButton(201, this.field_146294_l / 2 + 5, this.field_146295_m / 6 + 168, 
/* 356 */           I18n.func_135052_a("gui.xaero_cancel", new Object[0])));
/*     */     
/* 358 */     this.field_146292_n.add(this.leftButton = new GuiButton(208, this.field_146294_l / 2 - 203, 104, 20, 20, "<"));
/* 359 */     this.field_146292_n.add(this.rightButton = new GuiButton(209, this.field_146294_l / 2 + 183, 104, 20, 20, ">"));
/* 360 */     this.field_146292_n.add(this.modeSwitchButton = new MyTinyButton(210, this.field_146294_l / 2 + 129, 56, null, I18n.func_135052_a((this.modMain.getSettings()).waypointsMutualEdit ? "gui.xaero_waypoints_edit_mode_all" : "gui.xaero_waypoints_edit_mode_individually", new Object[0])));
/* 361 */     this.field_146292_n.add(new MyTinyButton(211, this.field_146294_l / 2 - 204, 56, null, I18n.func_135052_a("gui.xaero_waypoints_edit_reset", new Object[0])));
/*     */     
/* 363 */     this.nameTextField = new GuiTextField(0, this.field_146289_q, this.field_146294_l / 2 - 100, 104, 200, 20);
/* 364 */     this.xTextField = new GuiTextField(0, (this.modMain.getSettings()).hideWaypointCoordinates ? (FontRenderer)this.censoredTextFormatter : this.field_146289_q, this.field_146294_l / 2 - 109, 134, 50, 20);
/* 365 */     this.yTextField = new GuiTextField(0, (this.modMain.getSettings()).hideWaypointCoordinates ? (FontRenderer)this.censoredTextFormatter : this.field_146289_q, this.field_146294_l / 2 - 53, 134, 50, 20);
/* 366 */     this.zTextField = new GuiTextField(0, (this.modMain.getSettings()).hideWaypointCoordinates ? (FontRenderer)this.censoredTextFormatter : this.field_146289_q, this.field_146294_l / 2 + 3, 134, 50, 20);
/* 367 */     this.yawTextField = new GuiTextField(0, this.field_146289_q, this.field_146294_l / 2 + 59, 134, 50, 20);
/* 368 */     this.initialTextField = new GuiTextField(0, this.field_146289_q, this.field_146294_l / 2 - 25, 164, 50, 20);
/* 369 */     this.nameTextField.func_146180_a((getCurrent()).name);
/* 370 */     this.xTextField.func_146180_a((getCurrent()).xText);
/* 371 */     this.yTextField.func_146180_a((getCurrent()).yText);
/* 372 */     this.zTextField.func_146180_a((getCurrent()).zText);
/* 373 */     this.yawTextField.func_146180_a((getCurrent()).yawText);
/* 374 */     this.initialTextField.func_146180_a((getCurrent()).initial);
/* 375 */     String[] enabledisable = I18n.func_135052_a("gui.xaero_disable_enable", new Object[0]).split("/");
/* 376 */     this.field_146292_n.add(this.disableButton = new MySuperTinyButton(205, this.field_146294_l / 2 + 31, 164, enabledisable[(getCurrent()).disabled ? 1 : 0]));
/* 377 */     func_189646_b(this.globalButton = new MySuperTinyButton(213, this.field_146294_l / 2 - 81, 164, I18n.func_135052_a((getCurrent()).global ? "gui.xaero_waypoints_global" : "gui.xaero_waypoints_local", new Object[0])));
/*     */     
/* 379 */     if ((getCurrent()).defaultKeepYawText) {
/* 380 */       func_189646_b(this.defaultYawButton = new GuiButton(206, this.field_146294_l / 2 + 111, 134, 20, 20, "-"));
/* 381 */       this.defaultYawButton.field_146124_l = !(getCurrent()).keepYawText;
/*     */     } 
/* 383 */     if ((getCurrent()).defaultKeepDisabled) {
/* 384 */       func_189646_b(this.defaultDisabledButton = new GuiButton(207, this.field_146294_l / 2 + 81, 164, 20, 20, "-"));
/* 385 */       this.defaultDisabledButton.field_146124_l = !(getCurrent()).keepDisabled;
/*     */     } 
/* 387 */     if ((getCurrent()).defaultKeepGlobal) {
/* 388 */       func_189646_b(this.defaultGlobalButton = new GuiButton(214, this.field_146294_l / 2 - 101, 164, 20, 20, "-"));
/* 389 */       this.defaultGlobalButton.field_146124_l = !(getCurrent()).keepGlobal;
/*     */     } 
/* 391 */     if ((this.modMain.getSettings()).hideWaypointCoordinates) {
/* 392 */       func_189646_b(new MySuperTinyButton(212, this.field_146294_l / 2 + 115, 134, I18n.func_135052_a(this.censorCoordsIfNeeded ? "gui.xaero_waypoints_edit_show" : "gui.xaero_waypoints_edit_hide", new Object[0])));
/*     */     }
/*     */     
/* 395 */     this.dropDowns.clear();
/* 396 */     int currentColor = (getCurrent()).color;
/* 397 */     this.colorDD = new GuiDropDown(createColorOptions(), this.field_146294_l / 2 - 60, 82, 120, Integer.valueOf((currentColor == 0) ? 0 : (currentColor - 1)), this);
/* 398 */     this.dropDowns.add(this.colorDD);
/* 399 */     this.dropDowns.add(this.setsDD = new GuiDropDown(this.sets.getOptions(), this.field_146294_l / 2 - 101, 60, 201, Integer.valueOf(this.sets.getCurrentSet()), this));
/* 400 */     this.dropDowns.add(this.containersDD = new GuiDropDown(this.containers.options, this.field_146294_l / 2 - 203, 38, 200, Integer.valueOf(this.containers.current), this));
/* 401 */     this.dropDowns.add(this.worldsDD = new GuiDropDown(this.worlds.options, this.field_146294_l / 2 + 2, 38, 200, Integer.valueOf(this.worlds.current), this));
/*     */     
/* 403 */     this.nameTextField.func_146195_b(true);
/* 404 */     Keyboard.enableRepeatEvents(true);
/* 405 */     updateConfirmButton();
/*     */   }
/*     */   
/*     */   private WaypointEditForm getCurrent() {
/* 409 */     return (this.modMain.getSettings()).waypointsMutualEdit ? this.mutualForm : this.editForms.get(this.selectedWaypointIndex);
/*     */   }
/*     */   
/*     */   public void func_146281_b() {
/* 413 */     Keyboard.enableRepeatEvents(false);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void func_73869_a(char par1, int par2) throws IOException {
/* 419 */     if (this.nameTextField.func_146206_l()) {
/* 420 */       if (par2 == 15) {
/* 421 */         this.nameTextField.func_146195_b(false);
/* 422 */         this.xTextField.func_146195_b(true);
/*     */       } 
/* 424 */       this.nameTextField.func_146201_a(par1, par2);
/* 425 */       if ((getCurrent()).initial.length() == 0 && this.nameTextField.func_146179_b().length() > 0 && (!(getCurrent()).keepInitial || !(this.modMain.getSettings()).waypointsMutualEdit))
/* 426 */         this.initialTextField.func_146180_a(this.nameTextField.func_146179_b().substring(0, 1)); 
/* 427 */     } else if (this.xTextField.func_146206_l()) {
/* 428 */       if (par2 == 15) {
/* 429 */         this.xTextField.func_146195_b(false);
/* 430 */         this.yTextField.func_146195_b(true);
/*     */       } 
/* 432 */       this.xTextField.func_146201_a(par1, par2);
/* 433 */     } else if (this.yTextField.func_146206_l()) {
/* 434 */       if (par2 == 15) {
/* 435 */         this.yTextField.func_146195_b(false);
/* 436 */         this.zTextField.func_146195_b(true);
/*     */       } 
/* 438 */       this.yTextField.func_146201_a(par1, par2);
/* 439 */     } else if (this.zTextField.func_146206_l()) {
/* 440 */       if (par2 == 15) {
/* 441 */         this.zTextField.func_146195_b(false);
/* 442 */         this.yawTextField.func_146195_b(true);
/*     */       } 
/* 444 */       this.zTextField.func_146201_a(par1, par2);
/* 445 */     } else if (this.yawTextField.func_146206_l()) {
/* 446 */       if (par2 == 15) {
/* 447 */         this.yawTextField.func_146195_b(false);
/* 448 */         this.initialTextField.func_146195_b(true);
/*     */       } 
/* 450 */       this.yawTextField.func_146201_a(par1, par2);
/* 451 */       this.fieldValidator.validate(this.yawTextField);
/* 452 */       (getCurrent()).keepYawText = false;
/* 453 */       if (this.defaultYawButton != null) {
/* 454 */         this.defaultYawButton.field_146124_l = true;
/*     */       }
/* 456 */     } else if (this.initialTextField.func_146206_l()) {
/* 457 */       if (par2 == 15) {
/* 458 */         this.initialTextField.func_146195_b(false);
/* 459 */         this.nameTextField.func_146195_b(true);
/*     */       } 
/* 461 */       if (par2 != 57)
/* 462 */         this.initialTextField.func_146201_a(par1, par2); 
/*     */     } 
/* 464 */     if (par2 == 28 || par2 == 156) {
/* 465 */       func_146284_a(this.field_146292_n.get(0));
/*     */     }
/* 467 */     checkFields();
/* 468 */     updateConfirmButton();
/*     */     
/* 470 */     super.func_73869_a(par1, par2);
/*     */   }
/*     */   
/*     */   private boolean canConfirm() {
/* 474 */     WaypointEditForm current = getCurrent();
/* 475 */     return ((current.keepName || current.name.length() > 0) && (current.keepInitial || current.initial.length() > 0) && (current.keepXText || current.xText
/* 476 */       .length() > 0) && (current.keepYText || current.yText.length() > 0) && (current.keepZText || current.zText
/* 477 */       .length() > 0));
/*     */   }
/*     */   
/*     */   private void updateConfirmButton() {
/* 481 */     this.modeSwitchButton.field_146124_l = canConfirm();
/* 482 */     this.leftButton.field_146124_l = (!(this.modMain.getSettings()).waypointsMutualEdit && canConfirm() && this.selectedWaypointIndex > 0);
/* 483 */     this.rightButton.field_146124_l = (!(this.modMain.getSettings()).waypointsMutualEdit && canConfirm() && this.selectedWaypointIndex < this.editForms.size() - 1);
/*     */   }
/*     */   
/*     */   protected void checkFields() {
/* 487 */     this.fieldValidator.validate(this.xTextField);
/* 488 */     this.fieldValidator.validate(this.yTextField);
/* 489 */     this.fieldValidator.validate(this.zTextField);
/* 490 */     WaypointEditForm current = getCurrent();
/* 491 */     current.name = this.nameTextField.func_146179_b();
/* 492 */     current.xText = this.xTextField.func_146179_b();
/* 493 */     current.yText = this.yTextField.func_146179_b();
/* 494 */     current.zText = this.zTextField.func_146179_b();
/* 495 */     current.yawText = this.yawTextField.func_146179_b();
/* 496 */     current.initial = this.initialTextField.func_146179_b();
/* 497 */     current.initial = current.initial.toUpperCase();
/* 498 */     if (current.initial.length() > 2) {
/* 499 */       current.initial = current.initial.substring(0, 2);
/* 500 */       this.initialTextField.func_146180_a(current.initial);
/*     */     } 
/* 502 */     if (current.yawText.length() > 4) {
/* 503 */       current.yawText = current.yawText.substring(0, 4);
/* 504 */       this.yawTextField.func_146180_a(current.yawText);
/*     */     } 
/* 506 */     if (this.prefilled && this.editForms.size() > 1 && (this.modMain.getSettings()).waypointsMutualEdit) {
/* 507 */       current.keepName = current.name.isEmpty();
/* 508 */       current.keepXText = current.xText.isEmpty();
/* 509 */       current.keepYText = current.yText.isEmpty();
/* 510 */       current.keepZText = current.zText.isEmpty();
/* 511 */       current.keepInitial = current.initial.isEmpty();
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void func_73864_a(int par1, int par2, int par3) throws IOException {
/* 516 */     for (GuiDropDown d : this.dropDowns) {
/* 517 */       if (!d.isClosed() && d.onDropDown(par1, par2, this.field_146295_m)) {
/* 518 */         d.mouseClicked(par1, par2, par3, this.field_146295_m);
/*     */         return;
/*     */       } 
/* 521 */       d.setClosed(true);
/*     */     } 
/* 523 */     for (GuiDropDown d : this.dropDowns) {
/* 524 */       if (d.onDropDown(par1, par2, this.field_146295_m)) {
/* 525 */         d.mouseClicked(par1, par2, par3, this.field_146295_m);
/*     */         return;
/*     */       } 
/* 528 */       d.setClosed(true);
/*     */     } 
/* 530 */     super.func_73864_a(par1, par2, par3);
/* 531 */     this.nameTextField.func_146192_a(par1, par2, par3);
/* 532 */     this.xTextField.func_146192_a(par1, par2, par3);
/* 533 */     this.yTextField.func_146192_a(par1, par2, par3);
/* 534 */     this.zTextField.func_146192_a(par1, par2, par3);
/* 535 */     this.yawTextField.func_146192_a(par1, par2, par3);
/* 536 */     this.initialTextField.func_146192_a(par1, par2, par3);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void func_146286_b(int par1, int par2, int par3) {
/* 541 */     super.func_146286_b(par1, par2, par3);
/* 542 */     for (GuiDropDown d : this.dropDowns)
/* 543 */       d.mouseReleased(par1, par2, par3, this.field_146295_m); 
/*     */   }
/*     */   
/*     */   private void setFieldText(GuiTextField field, String text) {
/* 547 */     if (field.func_146179_b().equals(text))
/*     */       return; 
/* 549 */     field.func_146180_a(text);
/*     */   }
/*     */   
/*     */   public void func_73876_c() {
/* 553 */     if (this.field_146297_k.field_71439_g == null) {
/* 554 */       this.field_146297_k.func_147108_a(null);
/*     */       return;
/*     */     } 
/* 557 */     this.nameTextField.func_146178_a();
/* 558 */     this.xTextField.func_146178_a();
/* 559 */     this.yTextField.func_146178_a();
/* 560 */     this.zTextField.func_146178_a();
/* 561 */     this.yawTextField.func_146178_a();
/* 562 */     this.initialTextField.func_146178_a();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void func_146284_a(GuiButton par1GuiButton) {
/* 572 */     if (par1GuiButton.field_146124_l) {
/* 573 */       int var2 = this.field_146297_k.field_71474_y.field_74335_Z;
/*     */       
/* 575 */       if (par1GuiButton.field_146127_k < 100 && par1GuiButton instanceof MySmallButton) {
/*     */         try {
/* 577 */           this.modMain.getSettings().setOptionValue(((MySmallButton)par1GuiButton).returnModOptions(), 1);
/* 578 */         } catch (IOException e) {
/* 579 */           e.printStackTrace();
/*     */         } 
/* 581 */         par1GuiButton.field_146126_j = this.modMain.getSettings().getKeyBinding(ModOptions.getModOptions(par1GuiButton.field_146127_k));
/*     */       } 
/*     */       
/* 584 */       if (par1GuiButton.field_146127_k == 200) {
/* 585 */         if ((this.modMain.getSettings()).waypointsMutualEdit)
/* 586 */           confirmMutual(); 
/* 587 */         boolean creatingAWaypoint = (this.adding && this.waypointsEdited.size() < this.editForms.size()); int i;
/* 588 */         for (i = 0; i < this.waypointsEdited.size(); i++) {
/* 589 */           Waypoint w = this.waypointsEdited.get(i);
/* 590 */           WaypointEditForm waypointForm = this.editForms.get(i);
/* 591 */           String nameString = waypointForm.name;
/* 592 */           String xString = waypointForm.xText;
/* 593 */           String yString = waypointForm.yText;
/* 594 */           String zString = waypointForm.zText;
/* 595 */           String initialString = waypointForm.initial;
/* 596 */           int colorInt = waypointForm.color;
/*     */           
/* 598 */           if (w.getType() != 1 || !nameString.equals(I18n.func_135052_a("gui.xaero_deathpoint", new Object[0]))) {
/* 599 */             w.setName(nameString);
/* 600 */             if (w.getType() == 1)
/* 601 */               w.setType(0); 
/*     */           } 
/* 603 */           int x = xString.equals("-") ? 0 : Integer.parseInt(xString);
/* 604 */           int y = yString.equals("-") ? 0 : Integer.parseInt(yString);
/* 605 */           int z = zString.equals("-") ? 0 : Integer.parseInt(zString);
/* 606 */           w.setX(x);
/* 607 */           w.setY(y);
/* 608 */           w.setZ(z);
/* 609 */           w.setSymbol(initialString);
/* 610 */           w.setColor(colorInt - 1);
/*     */         } 
/* 612 */         if (creatingAWaypoint) {
/* 613 */           for (i = this.waypointsEdited.size(); i < this.editForms.size(); i++) {
/* 614 */             WaypointEditForm createdForm = this.editForms.get(i);
/* 615 */             String nameString = createdForm.name;
/* 616 */             String xString = createdForm.xText;
/* 617 */             String yString = createdForm.yText;
/* 618 */             String zString = createdForm.zText;
/* 619 */             String initialString = createdForm.initial;
/* 620 */             int colorInt = createdForm.color;
/*     */             
/* 622 */             int x = xString.equals("-") ? 0 : Integer.parseInt(xString);
/* 623 */             int y = yString.equals("-") ? 0 : Integer.parseInt(yString);
/* 624 */             int z = zString.equals("-") ? 0 : Integer.parseInt(zString);
/* 625 */             Waypoint created = new Waypoint(x, y, z, nameString, initialString, colorInt - 1);
/* 626 */             this.waypointsEdited.add(created);
/*     */           } 
/*     */         }
/* 629 */         for (i = 0; i < this.waypointsEdited.size(); i++) {
/* 630 */           Waypoint w = this.waypointsEdited.get(i);
/* 631 */           WaypointEditForm waypointForm = this.editForms.get(i);
/* 632 */           String yawText = waypointForm.yawText;
/* 633 */           boolean disableBool = waypointForm.disabled;
/*     */ 
/*     */ 
/*     */           
/* 637 */           boolean yawIsUsable = (yawText.length() > 0 && !yawText.equals("-"));
/* 638 */           w.setRotation(yawIsUsable);
/* 639 */           if (yawIsUsable)
/* 640 */             w.setYaw(Integer.parseInt(yawText)); 
/* 641 */           w.setDisabled(disableBool);
/* 642 */           w.setGlobal(waypointForm.global);
/*     */         } 
/* 644 */         WaypointWorld sourceWorld = this.defaultWorld;
/* 645 */         WaypointSet sourceSet = (WaypointSet)sourceWorld.getSets().get(this.fromSet);
/*     */         
/* 647 */         String[] destinationWorldKeys = this.worlds.getCurrentKeys();
/* 648 */         String destinationSetKey = this.sets.getCurrentSetKey();
/* 649 */         WaypointWorld destinationWorld = this.waypointsManager.getWorld(destinationWorldKeys[0], destinationWorldKeys[1]);
/* 650 */         WaypointSet destinationSet = (WaypointSet)destinationWorld.getSets().get(destinationSetKey);
/* 651 */         if (this.adding || sourceSet != destinationSet)
/* 652 */           destinationSet.getList().addAll(0, this.waypointsEdited); 
/* 653 */         if (sourceSet != destinationSet)
/* 654 */           sourceSet.getList().removeAll(this.waypointsEdited); 
/*     */         try {
/* 656 */           this.modMain.getSettings().saveWaypoints(sourceWorld);
/* 657 */           if (destinationWorld != sourceWorld)
/* 658 */             this.modMain.getSettings().saveWaypoints(destinationWorld); 
/* 659 */         } catch (IOException e) {
/* 660 */           e.printStackTrace();
/*     */         } 
/* 662 */         this.field_146297_k.func_147108_a(this.parentGuiScreen);
/*     */       } 
/*     */       
/* 665 */       if (par1GuiButton.field_146127_k == 201) {
/* 666 */         this.field_146297_k.func_147108_a(this.parentGuiScreen);
/*     */       }
/*     */       
/* 669 */       if (par1GuiButton.field_146127_k == 205) {
/* 670 */         String[] enabledisable = I18n.func_135052_a("gui.xaero_disable_enable", new Object[0]).split("/");
/* 671 */         (getCurrent()).disabled = !(getCurrent()).disabled;
/* 672 */         this.disableButton.field_146126_j = (getCurrent()).disabled ? enabledisable[1] : enabledisable[0];
/* 673 */         (getCurrent()).keepDisabled = false;
/* 674 */         if (this.defaultDisabledButton != null) {
/* 675 */           this.defaultDisabledButton.field_146124_l = true;
/*     */         }
/*     */       } 
/* 678 */       if (par1GuiButton.field_146127_k == 206) {
/* 679 */         (getCurrent()).keepYawText = true;
/* 680 */         (getCurrent()).yawText = "";
/* 681 */         this.yawTextField.func_146180_a((getCurrent()).yawText);
/* 682 */         par1GuiButton.field_146124_l = false;
/*     */       } 
/* 684 */       if (par1GuiButton.field_146127_k == 207) {
/* 685 */         String[] enabledisable = I18n.func_135052_a("gui.xaero_disable_enable", new Object[0]).split("/");
/* 686 */         (getCurrent()).keepDisabled = true;
/* 687 */         (getCurrent()).disabled = false;
/* 688 */         this.disableButton.field_146126_j = (getCurrent()).disabled ? enabledisable[1] : enabledisable[0];
/* 689 */         par1GuiButton.field_146124_l = false;
/*     */       } 
/* 691 */       if (par1GuiButton.field_146127_k == 208) {
/* 692 */         this.selectedWaypointIndex--;
/* 693 */         if (this.selectedWaypointIndex < 0)
/* 694 */           this.selectedWaypointIndex = 0; 
/* 695 */         func_146280_a(this.field_146297_k, this.field_146294_l, this.field_146295_m);
/*     */       } 
/* 697 */       if (par1GuiButton.field_146127_k == 209) {
/* 698 */         this.selectedWaypointIndex++;
/* 699 */         if (this.selectedWaypointIndex >= this.editForms.size())
/* 700 */           this.selectedWaypointIndex = this.editForms.size() - 1; 
/* 701 */         func_146280_a(this.field_146297_k, this.field_146294_l, this.field_146295_m);
/*     */       } 
/* 703 */       if (par1GuiButton.field_146127_k == 210) {
/* 704 */         (this.modMain.getSettings()).waypointsMutualEdit = !(this.modMain.getSettings()).waypointsMutualEdit;
/*     */         try {
/* 706 */           this.modMain.getSettings().saveSettings();
/* 707 */         } catch (IOException e) {
/* 708 */           e.printStackTrace();
/*     */         } 
/* 710 */         if ((this.modMain.getSettings()).waypointsMutualEdit) {
/* 711 */           this.prefilled = true;
/* 712 */           updateMutual();
/*     */         } else {
/* 714 */           confirmMutual();
/* 715 */         }  func_146280_a(this.field_146297_k, this.field_146294_l, this.field_146295_m);
/*     */       } 
/*     */       
/* 718 */       if (par1GuiButton.field_146127_k == 211) {
/* 719 */         if ((this.modMain.getSettings()).waypointsMutualEdit) {
/* 720 */           createForms();
/* 721 */           func_146280_a(this.field_146297_k, this.field_146294_l, this.field_146295_m);
/*     */         } else {
/* 723 */           resetCurrentForm();
/* 724 */           func_146280_a(this.field_146297_k, this.field_146294_l, this.field_146295_m);
/*     */         } 
/*     */       }
/*     */       
/* 728 */       if (par1GuiButton.field_146127_k == 212) {
/* 729 */         this.censorCoordsIfNeeded = !this.censorCoordsIfNeeded;
/* 730 */         par1GuiButton.field_146126_j = I18n.func_135052_a(this.censorCoordsIfNeeded ? "gui.xaero_waypoints_edit_show" : "gui.xaero_waypoints_edit_hide", new Object[0]);
/*     */       } 
/*     */       
/* 733 */       if (par1GuiButton.field_146127_k == 213) {
/* 734 */         (getCurrent()).global = !(getCurrent()).global;
/* 735 */         this.globalButton.field_146126_j = I18n.func_135052_a((getCurrent()).global ? "gui.xaero_waypoints_global" : "gui.xaero_waypoints_local", new Object[0]);
/* 736 */         (getCurrent()).keepGlobal = false;
/* 737 */         if (this.defaultGlobalButton != null) {
/* 738 */           this.defaultGlobalButton.field_146124_l = true;
/*     */         }
/*     */       } 
/* 741 */       if (par1GuiButton.field_146127_k == 214) {
/* 742 */         (getCurrent()).keepGlobal = true;
/* 743 */         (getCurrent()).global = false;
/* 744 */         this.globalButton.field_146126_j = I18n.func_135052_a((getCurrent()).global ? "gui.xaero_waypoints_global" : "gui.xaero_waypoints_local", new Object[0]);
/* 745 */         par1GuiButton.field_146124_l = false;
/*     */       } 
/*     */       
/* 748 */       if (this.field_146297_k.field_71474_y.field_74335_Z != var2) {
/* 749 */         ScaledResolution var3 = new ScaledResolution(this.field_146297_k);
/* 750 */         int var4 = var3.func_78326_a();
/* 751 */         int var5 = var3.func_78328_b();
/* 752 */         func_146280_a(this.field_146297_k, var4, var5);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public List getButtons() {
/* 759 */     return this.field_146292_n;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void func_73863_a(int par1, int par2, float par3) {
/* 767 */     func_146276_q_();
/* 768 */     func_73732_a(this.field_146289_q, this.screenTitle, this.field_146294_l / 2, 20, 16777215);
/*     */     
/* 770 */     WaypointEditForm current = getCurrent();
/* 771 */     if (!this.nameTextField.func_146206_l() && current.keepName)
/* 772 */       setFieldText(this.nameTextField, this.namePlaceholder); 
/* 773 */     if (!this.xTextField.func_146206_l()) {
/* 774 */       if (current.keepXText)
/* 775 */         setFieldText(this.xTextField, this.xPlaceholder); 
/* 776 */       if (this.multiplyDefaultValueBy8Counter > 0) {
/* 777 */         setFieldText(this.xTextField, this.xTextField.func_146179_b() + "§" + "8 * 8");
/* 778 */       } else if (this.multiplyDefaultValueBy8Counter < 0) {
/* 779 */         setFieldText(this.xTextField, this.xTextField.func_146179_b() + "§" + "8 / 8");
/*     */       } 
/* 781 */     }  if (!this.yTextField.func_146206_l() && current.keepYText)
/* 782 */       setFieldText(this.yTextField, this.yPlaceholder); 
/* 783 */     if (!this.zTextField.func_146206_l()) {
/* 784 */       if (current.keepZText)
/* 785 */         setFieldText(this.zTextField, this.zPlaceholder); 
/* 786 */       if (this.multiplyDefaultValueBy8Counter > 0) {
/* 787 */         setFieldText(this.zTextField, this.zTextField.func_146179_b() + "§" + "8 * 8");
/* 788 */       } else if (this.multiplyDefaultValueBy8Counter < 0) {
/* 789 */         setFieldText(this.zTextField, this.zTextField.func_146179_b() + "§" + "8 / 8");
/*     */       } 
/* 791 */     }  if (!this.yawTextField.func_146206_l() && (getCurrent()).yawText.isEmpty())
/* 792 */       if (current.keepYawText) {
/* 793 */         setFieldText(this.yawTextField, this.yawPlaceholder);
/*     */       } else {
/* 795 */         setFieldText(this.yawTextField, "§8" + I18n.func_135052_a("gui.xaero_yaw", new Object[0]));
/*     */       }  
/* 797 */     if (!this.initialTextField.func_146206_l() && (getCurrent()).initial.isEmpty())
/* 798 */       if (current.keepInitial) {
/* 799 */         setFieldText(this.initialTextField, this.initialPlaceholder);
/*     */       } else {
/* 801 */         setFieldText(this.initialTextField, "§8" + I18n.func_135052_a("gui.xaero_initial", new Object[0]));
/*     */       }  
/* 803 */     this.nameTextField.func_146194_f();
/* 804 */     this.xTextField.func_146194_f();
/* 805 */     this.yTextField.func_146194_f();
/* 806 */     this.zTextField.func_146194_f();
/* 807 */     this.yawTextField.func_146194_f();
/* 808 */     this.initialTextField.func_146194_f();
/* 809 */     setFieldText(this.nameTextField, (getCurrent()).name);
/* 810 */     setFieldText(this.xTextField, (getCurrent()).xText);
/* 811 */     setFieldText(this.yTextField, (getCurrent()).yText);
/* 812 */     setFieldText(this.zTextField, (getCurrent()).zText);
/* 813 */     setFieldText(this.yawTextField, (getCurrent()).yawText);
/* 814 */     setFieldText(this.initialTextField, (getCurrent()).initial);
/*     */ 
/*     */     
/* 817 */     if (this.dropped) {
/* 818 */       super.func_73863_a(0, 0, par3);
/*     */     } else {
/* 820 */       super.func_73863_a(par1, par2, par3);
/* 821 */     }  this.dropped = false;
/* 822 */     GuiDropDown openDropdown = null;
/* 823 */     for (int k = 0; k < this.dropDowns.size(); k++) {
/* 824 */       GuiDropDown dropdown = this.dropDowns.get(k);
/* 825 */       if (!dropdown.isClosed()) {
/* 826 */         this.dropped = true;
/* 827 */         openDropdown = dropdown;
/*     */       } else {
/* 829 */         dropdown.drawButton(par1, par2, this.field_146295_m);
/*     */       } 
/* 831 */     }  if (openDropdown != null) {
/* 832 */       openDropdown.drawButton(par1, par2, this.field_146295_m);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void func_146274_d() throws IOException {
/* 851 */     super.func_146274_d();
/* 852 */     int wheel = Mouse.getEventDWheel() / 120;
/* 853 */     if (wheel != 0) {
/* 854 */       ScaledResolution var3 = new ScaledResolution(this.field_146297_k);
/* 855 */       int mouseXScaled = Mouse.getX() / var3.func_78325_e();
/* 856 */       int mouseYScaled = var3.func_78328_b() - 1 - Mouse.getY() / var3.func_78325_e();
/* 857 */       for (GuiDropDown d : this.dropDowns) {
/* 858 */         if (!d.isClosed() && d.onDropDown(mouseXScaled, mouseYScaled, this.field_146295_m)) {
/* 859 */           d.mouseScrolled(wheel, mouseXScaled, mouseYScaled, this.field_146295_m);
/*     */           return;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean onSelected(GuiDropDown menu, int selected) {
/* 868 */     if (menu == this.setsDD) {
/* 869 */       this.sets.setCurrentSet(selected);
/* 870 */       if (this.waypointsManager.getCurrentContainerAndWorldID().equals(this.worlds.getCurrentKey())) {
/* 871 */         this.waypointsManager.getCurrentWorld().setCurrent(this.sets.getCurrentSetKey());
/* 872 */         this.waypointsManager.updateWaypoints();
/*     */         try {
/* 874 */           this.modMain.getSettings().saveWaypoints(this.waypointsManager.getCurrentWorld());
/* 875 */         } catch (IOException e) {
/* 876 */           e.printStackTrace();
/*     */         } 
/*     */       } 
/* 879 */     } else if (menu == this.colorDD) {
/* 880 */       (getCurrent()).color = (this.colorDD.size() > ModSettings.ENCHANT_COLORS.length) ? selected : (selected + 1);
/* 881 */     } else if (menu == this.containersDD) {
/* 882 */       WaypointWorld currentWorld; this.containers.current = selected;
/*     */       
/* 884 */       if (this.containers.current != this.defaultContainer) {
/* 885 */         currentWorld = this.waypointsManager.getWorldContainer(this.containers.getCurrentKey()).getFirstWorld();
/*     */       } else {
/* 887 */         currentWorld = this.defaultWorld;
/* 888 */       }  this.sets = new GuiWaypointSets(false, currentWorld);
/* 889 */       this.worlds = new GuiWaypointWorlds(this.waypointsManager.getWorldContainer(this.containers.getCurrentKey()), this.waypointsManager, currentWorld.getFullId());
/* 890 */       this.dropDowns.set(1, this.setsDD = new GuiDropDown(this.sets.getOptions(), this.field_146294_l / 2 - 101, 60, 201, Integer.valueOf(this.sets.getCurrentSet()), this));
/* 891 */       this.dropDowns.set(3, this.worldsDD = new GuiDropDown(this.worlds.options, this.field_146294_l / 2 + 2, 38, 200, Integer.valueOf(this.worlds.current), this));
/* 892 */     } else if (menu == this.worldsDD) {
/* 893 */       this.worlds.current = selected;
/* 894 */       String[] worldKeys = this.worlds.getCurrentKeys();
/* 895 */       this.sets = new GuiWaypointSets(false, this.waypointsManager.getWorld(worldKeys[0], worldKeys[1]));
/* 896 */       this.dropDowns.set(1, this.setsDD = new GuiDropDown(this.sets.getOptions(), this.field_146294_l / 2 - 101, 60, 201, Integer.valueOf(this.sets.getCurrentSet()), this));
/*     */     } 
/* 898 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\ioanm\Downloads\Xaeros_Minimap_20.15.3_Forge_1.12.jar!\xaero\common\gui\GuiAddWaypoint.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */