/*     */ package xaero.map.gui;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.GuiButton;
/*     */ import net.minecraft.client.gui.GuiScreen;
/*     */ import net.minecraft.client.gui.GuiYesNo;
/*     */ import net.minecraft.client.resources.I18n;
/*     */ import xaero.map.MapProcessor;
/*     */ import xaero.map.WorldMap;
/*     */ import xaero.map.world.MapDimension;
/*     */ import xaero.map.world.MapWorld;
/*     */ 
/*     */ public class GuiDimensionSettings
/*     */ {
/*     */   private MapDimension settingsDimension;
/*     */   private String[] mwDropdownValues;
/*     */   private GuiButton settingsButton;
/*     */   private GuiButton multiworldTypeOptionButton;
/*     */   private GuiButton renameButton;
/*     */   private GuiButton deleteButton;
/*  24 */   private CursorBox mapSelectionBox = new CursorBox("gui.xaero_map_selection_box");
/*     */ 
/*     */   
/*     */   public boolean active;
/*     */ 
/*     */   
/*     */   public void init(final GuiMap mapScreen, List<GuiDropDown> dropdowns, final Minecraft minecraft, int width, int height) {
/*  31 */     this.multiworldTypeOptionButton = null;
/*  32 */     MapWorld mapWorld = MapProcessor.instance.getMapWorld();
/*  33 */     this.settingsDimension = (mapWorld == null) ? null : mapWorld.getCurrentDimension();
/*  34 */     if (this.settingsDimension != null && mapWorld.isMultiplayer()) {
/*  35 */       mapScreen.addGuiButton(this.settingsButton = new GuiMapSettingsButton(this.active, 0, height - 20));
/*     */       
/*  37 */       if (this.active) {
/*  38 */         String currentMultiworld = this.settingsDimension.getFutureMultiworldUnsynced();
/*  39 */         List<KeySortableByOther<String>> sortableList = new ArrayList<>();
/*  40 */         for (String mwId : this.settingsDimension.getMultiworldIdsCopy()) {
/*  41 */           sortableList.add(new KeySortableByOther<>(mwId, new Comparable[] { this.settingsDimension.getMultiworldName(mwId).toLowerCase() }));
/*  42 */         }  Collections.sort(sortableList);
/*  43 */         List<String> dropdownValuesList = new ArrayList<>();
/*  44 */         for (KeySortableByOther<String> sortableKey : sortableList)
/*  45 */           dropdownValuesList.add(sortableKey.getKey()); 
/*  46 */         int selected = getDropdownSelectionIdFromValue(dropdownValuesList, currentMultiworld);
/*  47 */         if (selected == dropdownValuesList.size())
/*  48 */           dropdownValuesList.add(currentMultiworld); 
/*  49 */         this.mwDropdownValues = dropdownValuesList.<String>toArray(new String[0]);
/*  50 */         List<String> mwDropdownNames = new ArrayList<>();
/*  51 */         for (String s : dropdownValuesList)
/*  52 */           mwDropdownNames.add(this.settingsDimension.getMultiworldName(s)); 
/*  53 */         if (this.settingsDimension.getMapWorld().isMultiplayer())
/*  54 */           mwDropdownNames.add("§8" + I18n.func_135052_a("gui.xaero_create_new_map", new Object[0])); 
/*  55 */         GuiDropDown createdDropdown = new GuiDropDown(mwDropdownNames.<String>toArray(new String[0]), width / 2 - 100, 30, 200, Integer.valueOf(selected), new IDropDownCallback()
/*     */             {
/*     */               public boolean onSelected(GuiDropDown dd, int i) {
/*  58 */                 if (i < GuiDimensionSettings.this.mwDropdownValues.length) {
/*  59 */                   MapProcessor.instance.setMultiworld(GuiDimensionSettings.this.settingsDimension, GuiDimensionSettings.this.mwDropdownValues[i]);
/*  60 */                   GuiDimensionSettings.this.updateButtons();
/*  61 */                   return true;
/*     */                 } 
/*  63 */                 minecraft.func_147108_a(new GuiMapName(mapScreen, GuiDimensionSettings.this.settingsDimension, null));
/*  64 */                 return false;
/*     */               }
/*     */             });
/*  67 */         dropdowns.add(createdDropdown);
/*     */         
/*  69 */         mapScreen.addGuiButton(this.multiworldTypeOptionButton = new GuiButton(-1, 0, height - 45, 150, 20, getMultiworldTypeButtonMessage()));
/*  70 */         mapScreen.addGuiButton(this.renameButton = new GuiButton(-1, width / 2 + 109, 26, 60, 20, I18n.func_135052_a("gui.xaero_rename", new Object[0])));
/*  71 */         mapScreen.addGuiButton(this.deleteButton = new GuiButton(-1, width / 2 - 168, 26, 60, 20, I18n.func_135052_a("gui.xaero_delete", new Object[0])));
/*  72 */         updateButtons();
/*  73 */         this.renameButton.field_146124_l = this.settingsDimension.getMapWorld().isMultiplayer();
/*  74 */         mapScreen.addGuiButton(new GuiButton(200, width / 2 - 50, 50, 100, 20, I18n.func_135052_a("gui.xaero_confirm", new Object[0])));
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void updateButtons() {
/*  80 */     this.settingsButton.field_146124_l = (this.settingsDimension != null && this.settingsDimension.futureMultiworldWritable);
/*  81 */     this.deleteButton.field_146124_l = (this.settingsDimension.getMapWorld().isMultiplayer() && this.mwDropdownValues.length > 1 && this.settingsDimension.getFutureCustomSelectedMultiworld() != null);
/*     */   }
/*     */   
/*     */   private String getMultiworldTypeButtonMessage() {
/*  85 */     int multiworldType = this.settingsDimension.getMapWorld().getFutureMultiworldType();
/*  86 */     return I18n.func_135052_a("gui.xaero_map_selection", new Object[0]) + ": " + I18n.func_135052_a((multiworldType == 0) ? "gui.xaero_mw_single" : ((multiworldType == 1) ? "gui.xaero_mw_manual" : "gui.xaero_mw_spawn"), new Object[0]);
/*     */   }
/*     */   
/*     */   public void confirm(GuiMap mapScreen, Minecraft minecraft, int width, int height) {
/*  90 */     MapProcessor.instance.confirmMultiworld();
/*  91 */     this.active = false;
/*  92 */     mapScreen.func_146280_a(minecraft, width, height);
/*     */   }
/*     */   
/*     */   private int getDropdownSelectionIdFromValue(List<String> values, String value) {
/*  96 */     int selected = 0;
/*  97 */     for (selected = 0; selected < values.size() && 
/*  98 */       !((String)values.get(selected)).equals(value); selected++);
/*     */     
/* 100 */     return selected;
/*     */   }
/*     */   
/*     */   public void preMapRender(GuiMap mapScreen, List<GuiDropDown> dropdowns, Minecraft minecraft, int width, int height) {
/* 104 */     MapWorld currentWorld = MapProcessor.instance.getMapWorld();
/* 105 */     MapDimension currentDim = (currentWorld == null) ? null : currentWorld.getCurrentDimension();
/* 106 */     if (currentDim != this.settingsDimension) {
/* 107 */       this.active = false;
/* 108 */       mapScreen.func_146280_a(minecraft, width, height);
/*     */     } 
/* 110 */     if (!this.active && this.settingsDimension != null && !this.settingsDimension.futureMultiworldWritable) {
/* 111 */       this.active = true;
/* 112 */       mapScreen.func_146280_a(minecraft, width, height);
/*     */     } 
/* 114 */     if (this.active && 
/* 115 */       this.settingsDimension != null && ((GuiDropDown)dropdowns.get(0)).isClosed()) {
/* 116 */       String currentMultiworld = this.settingsDimension.getFutureMultiworldUnsynced();
/* 117 */       String currentDropdownSelection = this.mwDropdownValues[((GuiDropDown)dropdowns.get(0)).getSelected()];
/* 118 */       if (!currentMultiworld.equals(currentDropdownSelection)) {
/* 119 */         mapScreen.func_146280_a(minecraft, width, height);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public void renderText(Minecraft minecraft, int mouseX, int mouseY, int width, int height) {
/* 125 */     if (!this.active)
/*     */       return; 
/* 127 */     String selectMapString = I18n.func_135052_a("gui.xaero_select_map", new Object[0]) + ":";
/* 128 */     minecraft.field_71466_p.func_175063_a(selectMapString, (width / 2 - minecraft.field_71466_p.func_78256_a(selectMapString) / 2), 19.0F, -1);
/*     */   }
/*     */   
/*     */   public void postMapRender(Minecraft minecraft, int mouseX, int mouseY, int width, int height) {
/* 132 */     if (this.multiworldTypeOptionButton != null && mouseX >= this.multiworldTypeOptionButton.field_146128_h && mouseX < this.multiworldTypeOptionButton.field_146128_h + this.multiworldTypeOptionButton.func_146117_b() && mouseY >= this.multiworldTypeOptionButton.field_146129_i && mouseY < this.multiworldTypeOptionButton.field_146129_i + this.multiworldTypeOptionButton.field_146121_g)
/*     */     {
/* 134 */       this.mapSelectionBox.drawBox(this.multiworldTypeOptionButton.field_146128_h + this.multiworldTypeOptionButton.func_146117_b(), mouseY, width, height); } 
/*     */   }
/*     */   
/*     */   public void actionPerformed(final GuiMap mapScreen, final Minecraft minecraft, final int width, final int height, GuiButton b) {
/* 138 */     if (b.field_146124_l)
/* 139 */       if (b == this.settingsButton) {
/* 140 */         this.active = !this.active;
/* 141 */         mapScreen.func_146280_a(minecraft, width, height);
/* 142 */       } else if (b == this.multiworldTypeOptionButton) {
/* 143 */         MapProcessor.instance.toggleMultiworldType();
/* 144 */         b.field_146126_j = getMultiworldTypeButtonMessage();
/* 145 */       } else if (b == this.renameButton) {
/* 146 */         minecraft.func_147108_a(new GuiMapName(mapScreen, this.settingsDimension, this.settingsDimension.getFutureMultiworldUnsynced()));
/* 147 */       } else if (b == this.deleteButton) {
/* 148 */         if (this.settingsDimension.getFutureCustomSelectedMultiworld() != null) {
/* 149 */           final String selectedMWId = this.settingsDimension.getFutureCustomSelectedMultiworld();
/* 150 */           minecraft.func_147108_a((GuiScreen)new GuiYesNo(new YesNoCallbackImplementation()
/*     */                 {
/*     */                   public void func_73878_a(boolean result, int id) {
/* 153 */                     if (result) {
/* 154 */                       String mapNameAndIdLine = I18n.func_135052_a("gui.xaero_delete_map_msg4", new Object[0]) + ": " + GuiDimensionSettings.this.settingsDimension.getMultiworldName(selectedMWId) + " (" + selectedMWId + ")";
/* 155 */                       minecraft.func_147108_a((GuiScreen)new GuiYesNo(new YesNoCallbackImplementation()
/*     */                             {
/*     */                               public void func_73878_a(boolean result2, int id) {
/* 158 */                                 if (result2) {
/* 159 */                                   synchronized (MapProcessor.instance.uiSync) {
/* 160 */                                     if (MapProcessor.instance.getLastNonNullWorld() == GuiDimensionSettings.this.settingsDimension.getMapWorld()) {
/* 161 */                                       MapWorld currentWorld = MapProcessor.instance.getMapWorld();
/* 162 */                                       MapDimension currentDimension = (currentWorld == null) ? null : currentWorld.getCurrentDimension();
/* 163 */                                       if (GuiDimensionSettings.this.settingsDimension == currentDimension && GuiDimensionSettings.this.settingsDimension.getCurrentMultiworld().equals(selectedMWId)) {
/* 164 */                                         if (WorldMap.settings.debug)
/* 165 */                                           System.out.println("Delayed map deletion!"); 
/* 166 */                                         MapProcessor.instance.requestCurrentMapDeletion();
/*     */                                       } else {
/* 168 */                                         if (WorldMap.settings.debug)
/* 169 */                                           System.out.println("Instant map deletion!"); 
/* 170 */                                         GuiDimensionSettings.this.settingsDimension.deleteMultiworldMapDataUnsynced(selectedMWId);
/*     */                                       } 
/* 172 */                                       GuiDimensionSettings.this.settingsDimension.deleteMultiworldId(selectedMWId);
/* 173 */                                       GuiDimensionSettings.this.settingsDimension.pickDefaultCustomMultiworldUnsynced();
/* 174 */                                       GuiDimensionSettings.this.settingsDimension.saveConfig();
/* 175 */                                       GuiDimensionSettings.this.settingsDimension.futureMultiworldWritable = false;
/* 176 */                                       mapScreen.func_146280_a(minecraft, width, height);
/*     */                                     } 
/*     */                                   } 
/*     */                                 }
/* 180 */                                 minecraft.func_147108_a(mapScreen);
/*     */                               }
/* 182 */                             }I18n.func_135052_a("gui.xaero_delete_map_msg3", new Object[0]), mapNameAndIdLine, -1));
/*     */                     } else {
/* 184 */                       minecraft.func_147108_a(mapScreen);
/*     */                     }  }
/* 186 */                 }I18n.func_135052_a("gui.xaero_delete_map_msg1", new Object[0]), I18n.func_135052_a("gui.xaero_delete_map_msg2", new Object[0]), -1));
/*     */         } 
/* 188 */       } else if (b.field_146127_k == 200) {
/* 189 */         confirm(mapScreen, minecraft, width, height);
/*     */       }  
/*     */   }
/*     */ }


/* Location:              C:\Users\ioanm\Downloads\XaerosWorldMap_1.7.3_Forge_1.12.jar!\xaero\map\gui\GuiDimensionSettings.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */