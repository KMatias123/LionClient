/*     */ package xaero.common.gui;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.attribute.FileAttribute;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import net.minecraft.client.gui.GuiButton;
/*     */ import net.minecraft.client.gui.GuiScreen;
/*     */ import net.minecraft.client.gui.GuiYesNo;
/*     */ import net.minecraft.client.gui.GuiYesNoCallback;
/*     */ import net.minecraft.client.resources.I18n;
/*     */ import org.apache.commons.io.FileUtils;
/*     */ import xaero.common.IXaeroMinimap;
/*     */ import xaero.common.graphics.CursorBox;
/*     */ import xaero.common.minimap.waypoints.Waypoint;
/*     */ import xaero.common.minimap.waypoints.WaypointSet;
/*     */ import xaero.common.minimap.waypoints.WaypointWorld;
/*     */ import xaero.common.minimap.waypoints.WaypointWorldContainer;
/*     */ import xaero.common.minimap.waypoints.WaypointWorldRootContainer;
/*     */ import xaero.common.minimap.waypoints.WaypointsManager;
/*     */ import xaero.common.settings.ModSettings;
/*     */ 
/*     */ public class GuiWaypointsOptions
/*     */   extends GuiScreen implements GuiYesNoCallback {
/*     */   GuiScreen parent;
/*     */   private IXaeroMinimap modMain;
/*     */   private WaypointsManager waypointsManager;
/*     */   private MySmallButton automaticButton;
/*     */   private MySmallButton subAutomaticButton;
/*     */   private MySmallButton deleteButton;
/*     */   private MySmallButton subDeleteButton;
/*     */   private boolean buttonTest;
/*     */   private WaypointWorld waypointWorld;
/*     */   private boolean teleportationOptionShown;
/*  39 */   public CursorBox mwTooltip = new CursorBox("gui.xaero_use_multiworld_tooltip");
/*  40 */   public CursorBox teleportationTooltip = new CursorBox("gui.xaero_teleportation_tooltip", "§c");
/*     */   
/*     */   public GuiWaypointsOptions(IXaeroMinimap modMain, GuiScreen parent, WaypointWorld waypointWorld) {
/*  43 */     this.parent = parent;
/*  44 */     this.modMain = modMain;
/*  45 */     this.waypointsManager = modMain.getWaypointsManager();
/*  46 */     this.waypointWorld = waypointWorld;
/*  47 */     this.teleportationOptionShown = waypointWorld.getContainer().getRootContainer().isTeleportationEnabled();
/*     */   }
/*     */ 
/*     */   
/*     */   public void func_73866_w_() {
/*  52 */     this.parent.func_146280_a(this.field_146297_k, this.field_146294_l, this.field_146295_m);
/*  53 */     this.field_146292_n.clear();
/*  54 */     this.field_146292_n.add(new MyTinyButton(5, this.field_146294_l / 2 - 203, 32, I18n.func_135052_a("gui.xaero_close", new Object[0])));
/*  55 */     this.field_146292_n.add(new MySmallButton(6, this.field_146294_l / 2 - 203, 57, I18n.func_135052_a("gui.xaero_transfer", new Object[0])));
/*  56 */     this.field_146292_n.add(this.automaticButton = new MySmallButton(7, this.field_146294_l / 2 - 203, 82, I18n.func_135052_a("gui.xaero_make_automatic", new Object[0])));
/*  57 */     this.field_146292_n.add(this.subAutomaticButton = new MySmallButton(8, this.field_146294_l / 2 - 203, 107, I18n.func_135052_a("gui.xaero_make_multi_automatic", new Object[0])));
/*  58 */     this.field_146292_n.add(this.deleteButton = new MySmallButton(9, this.field_146294_l / 2 - 203, 132, I18n.func_135052_a("gui.xaero_delete_world", new Object[0])));
/*  59 */     this.field_146292_n.add(this.subDeleteButton = new MySmallButton(10, this.field_146294_l / 2 - 203, 157, I18n.func_135052_a("gui.xaero_delete_multi_world", new Object[0])));
/*  60 */     this.field_146292_n.add(new MySmallButton(11, this.field_146294_l / 2 - 203, 182, I18n.func_135052_a("gui.xaero_multiply_all_by_8", new Object[0])));
/*  61 */     this.field_146292_n.add(new MySmallButton(12, this.field_146294_l / 2 - 203, 207, I18n.func_135052_a("gui.xaero_divide_all_by_8", new Object[0])));
/*  62 */     this.field_146292_n.add(new MySmallButton(200, this.field_146294_l / 2 + 53, 57, getConfigButtonName(0)));
/*     */     MySmallButton teleportationEnabledButton;
/*  64 */     this.field_146292_n.add(teleportationEnabledButton = new MySmallButton(201, this.field_146294_l / 2 + 53, 82, getConfigButtonName(1)));
/*  65 */     teleportationEnabledButton.field_146124_l = this.teleportationOptionShown;
/*  66 */     func_189646_b(new MySmallButton(13, this.field_146294_l / 2 + 53, 107, I18n.func_135052_a("gui.xaero_world_teleport_command", new Object[0])));
/*  67 */     func_189646_b(new MySmallButton(202, this.field_146294_l / 2 + 53, 157, getConfigButtonName(2)));
/*  68 */     func_189646_b(new MySmallButton(203, this.field_146294_l / 2 + 53, 182, getConfigButtonName(3)));
/*     */   }
/*     */   
/*     */   private String getConfigButtonName(int buttonId) {
/*  72 */     switch (buttonId) {
/*     */       case 0:
/*  74 */         return I18n.func_135052_a("gui.xaero_use_multiworld", new Object[0]) + ": " + ModSettings.getTranslation(this.waypointWorld.getContainer().getRootContainer().isUsingMultiworldDetection());
/*     */       case 1:
/*  76 */         return I18n.func_135052_a("gui.xaero_teleportation", new Object[0]) + ": " + ModSettings.getTranslation(this.waypointWorld.getContainer().getRootContainer().isTeleportationEnabled());
/*     */       case 2:
/*  78 */         return I18n.func_135052_a("gui.xaero_sort", new Object[0]) + ": " + I18n.func_135052_a((this.waypointWorld.getContainer().getRootContainer().getSortType()).optionName, new Object[0]);
/*     */       case 3:
/*  80 */         return I18n.func_135052_a("gui.xaero_sort_reversed", new Object[0]) + ": " + ModSettings.getTranslation(this.waypointWorld.getContainer().getRootContainer().isSortReversed());
/*     */     } 
/*  82 */     return "";
/*     */   }
/*     */   
/*     */   private void onConfigButtonClick(MySmallButton button) {
/*  86 */     this.buttonTest = true;
/*  87 */     WaypointWorldRootContainer wc = this.waypointWorld.getContainer().getRootContainer();
/*  88 */     switch (button.getId() - 200) {
/*     */       case 0:
/*  90 */         wc.setUsingMultiworldDetection(!this.waypointWorld.getContainer().getRootContainer().isUsingMultiworldDetection());
/*  91 */         wc.setDefaultMultiworldId(null);
/*     */         break;
/*     */       case 1:
/*  94 */         wc.setTeleportationEnabled(!wc.isTeleportationEnabled());
/*     */         break;
/*     */       case 2:
/*  97 */         this.waypointWorld.getContainer().getRootContainer().toggleSortType();
/*  98 */         this.parent.func_146280_a(this.field_146297_k, this.field_146294_l, this.field_146295_m);
/*     */         break;
/*     */       case 3:
/* 101 */         this.waypointWorld.getContainer().getRootContainer().toggleSortReversed();
/* 102 */         this.parent.func_146280_a(this.field_146297_k, this.field_146294_l, this.field_146295_m);
/*     */         break;
/*     */     } 
/* 105 */     wc.saveConfig();
/* 106 */     button.field_146126_j = getConfigButtonName(button.getId() - 200);
/*     */   }
/*     */   
/*     */   protected void func_73864_a(int par1, int par2, int par3) throws IOException {
/* 110 */     this.buttonTest = false;
/* 111 */     super.func_73864_a(par1, par2, par3);
/* 112 */     if (!this.buttonTest)
/* 113 */       this.field_146297_k.func_147108_a(this.parent); 
/*     */   }
/*     */   
/*     */   protected void func_146284_a(GuiButton p_146284_1_) {
/* 117 */     this.buttonTest = true;
/* 118 */     if (p_146284_1_.field_146124_l) {
/* 119 */       if (p_146284_1_.field_146127_k >= 200) {
/* 120 */         onConfigButtonClick((MySmallButton)p_146284_1_);
/*     */         return;
/*     */       } 
/* 123 */       switch (p_146284_1_.field_146127_k) {
/*     */         case 5:
/* 125 */           this.field_146297_k.func_147108_a(this.parent);
/*     */           break;
/*     */         case 6:
/* 128 */           this.field_146297_k.func_147108_a(new GuiTransfer(this.modMain, this.parent));
/*     */           break;
/*     */         case 7:
/* 131 */           this.field_146297_k.func_147108_a((GuiScreen)new GuiYesNo(this, I18n.func_135052_a("gui.xaero_make_automatic_msg1", new Object[0]), I18n.func_135052_a("gui.xaero_make_automatic_msg2", new Object[0]), p_146284_1_.field_146127_k));
/*     */           break;
/*     */         case 8:
/* 134 */           this.field_146297_k.func_147108_a((GuiScreen)new GuiYesNo(this, I18n.func_135052_a("gui.xaero_make_multi_automatic_msg1", new Object[0]), I18n.func_135052_a("gui.xaero_make_multi_automatic_msg2", new Object[0]), p_146284_1_.field_146127_k));
/*     */           break;
/*     */         case 9:
/* 137 */           this.field_146297_k.func_147108_a((GuiScreen)new GuiYesNo(this, I18n.func_135052_a("gui.xaero_delete_world_msg1", new Object[0]), I18n.func_135052_a("gui.xaero_delete_world_msg2", new Object[0]), p_146284_1_.field_146127_k));
/*     */           break;
/*     */         case 10:
/* 140 */           this.field_146297_k.func_147108_a((GuiScreen)new GuiYesNo(this, I18n.func_135052_a("gui.xaero_delete_multi_world_msg1", new Object[0]), I18n.func_135052_a("gui.xaero_delete_multi_world_msg2", new Object[0]), p_146284_1_.field_146127_k));
/*     */           break;
/*     */         case 11:
/* 143 */           this.field_146297_k.func_147108_a((GuiScreen)new GuiYesNo(this, I18n.func_135052_a("gui.xaero_multiply_msg1", new Object[0]), I18n.func_135052_a("gui.xaero_multiply_msg2", new Object[0]), p_146284_1_.field_146127_k));
/*     */           break;
/*     */         case 12:
/* 146 */           this.field_146297_k.func_147108_a((GuiScreen)new GuiYesNo(this, I18n.func_135052_a("gui.xaero_multiply_msg1", new Object[0]), I18n.func_135052_a("gui.xaero_divide_msg2", new Object[0]), p_146284_1_.field_146127_k));
/*     */           break;
/*     */         case 13:
/* 149 */           this.field_146297_k.func_147108_a(new GuiWorldTpCommand(this.modMain, this, this.waypointWorld));
/*     */           break;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void func_73878_a(boolean result, int id) {
/* 156 */     if (result) {
/* 157 */       WaypointWorldRootContainer waypointWorldRootContainer; WaypointWorldContainer auto; String buKey; Path selectedPath; Path autoPath; Path tempFolder; WaypointWorld autoWorld; WaypointWorld selectedWorld; WaypointWorldContainer autoWc; WaypointWorldContainer selectedWc; String buSelected; String selectedRootContainerId; switch (id) {
/*     */         case 7:
/* 159 */           waypointWorldRootContainer = this.waypointWorld.getContainer().getRootContainer();
/* 160 */           auto = (WaypointWorldContainer)this.waypointsManager.getWaypointMap().get(this.waypointsManager.getAutoRootContainerID());
/* 161 */           if (waypointWorldRootContainer == null || auto == null) {
/*     */             break;
/*     */           }
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 168 */           buKey = waypointWorldRootContainer.getKey();
/* 169 */           this.waypointsManager.getWaypointMap().put(auto.getKey(), waypointWorldRootContainer);
/* 170 */           this.waypointsManager.getWaypointMap().put(buKey, auto);
/* 171 */           waypointWorldRootContainer.setKey(auto.getKey());
/* 172 */           auto.setKey(buKey);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 178 */           selectedPath = waypointWorldRootContainer.getDirectory().toPath();
/* 179 */           autoPath = auto.getDirectory().toPath();
/* 180 */           tempFolder = this.modMain.getWaypointsFolder().toPath().resolve("temp_to_add");
/*     */           
/*     */           try {
/* 183 */             Files.createDirectories(tempFolder, (FileAttribute<?>[])new FileAttribute[0]);
/* 184 */             Path selectedTemp = tempFolder.resolve(selectedPath.getFileName());
/* 185 */             Path autoTemp = tempFolder.resolve(autoPath.getFileName());
/*     */ 
/*     */             
/* 188 */             if (Files.exists(selectedPath, new java.nio.file.LinkOption[0]))
/* 189 */               Files.move(selectedPath, selectedTemp, new java.nio.file.CopyOption[0]); 
/* 190 */             if (Files.exists(autoPath, new java.nio.file.LinkOption[0])) {
/* 191 */               Files.move(autoPath, autoTemp, new java.nio.file.CopyOption[0]);
/*     */             }
/*     */             
/* 194 */             if (Files.exists(selectedTemp, new java.nio.file.LinkOption[0]))
/* 195 */               Files.move(selectedTemp, autoPath, new java.nio.file.CopyOption[0]); 
/* 196 */             if (Files.exists(autoTemp, new java.nio.file.LinkOption[0]))
/* 197 */               Files.move(autoTemp, selectedPath, new java.nio.file.CopyOption[0]); 
/* 198 */             Files.deleteIfExists(tempFolder);
/* 199 */           } catch (Throwable e) {
/* 200 */             this.modMain.getInterfaces().getMinimap().setCrashedWith(e);
/*     */           } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 207 */           this.waypointsManager.setCustomWorldID(null);
/* 208 */           this.waypointsManager.setCustomContainerID(null);
/* 209 */           this.waypointsManager.updateWaypoints();
/*     */           break;
/*     */         
/*     */         case 8:
/* 213 */           autoWorld = this.waypointsManager.getAutoWorld();
/* 214 */           selectedWorld = this.waypointWorld;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           try {
/* 221 */             Path autoFile = this.modMain.getSettings().getWaypointsFile(autoWorld).toPath();
/* 222 */             Path selectedFile = this.modMain.getSettings().getWaypointsFile(selectedWorld).toPath();
/* 223 */             Path autoTempFile = autoFile.getParent().resolve("temp_to_add").resolve(autoFile.getFileName());
/* 224 */             Path selectedTempFile = selectedFile.getParent().resolve("temp_to_add").resolve(selectedFile.getFileName());
/* 225 */             Files.createDirectories(autoTempFile.getParent(), (FileAttribute<?>[])new FileAttribute[0]);
/* 226 */             Files.createDirectories(selectedTempFile.getParent(), (FileAttribute<?>[])new FileAttribute[0]);
/*     */             
/* 228 */             if (!Files.exists(autoFile, new java.nio.file.LinkOption[0]))
/* 229 */               Files.createFile(autoFile, (FileAttribute<?>[])new FileAttribute[0]); 
/* 230 */             Files.move(autoFile, autoTempFile, new java.nio.file.CopyOption[0]);
/* 231 */             if (!Files.exists(selectedFile, new java.nio.file.LinkOption[0]))
/* 232 */               Files.createFile(selectedFile, (FileAttribute<?>[])new FileAttribute[0]); 
/* 233 */             Files.move(selectedFile, selectedTempFile, new java.nio.file.CopyOption[0]);
/*     */             
/* 235 */             if (Files.exists(autoTempFile, new java.nio.file.LinkOption[0]))
/* 236 */               Files.move(autoTempFile, selectedFile, new java.nio.file.CopyOption[0]); 
/* 237 */             if (Files.exists(selectedTempFile, new java.nio.file.LinkOption[0])) {
/* 238 */               Files.move(selectedTempFile, autoFile, new java.nio.file.CopyOption[0]);
/*     */             }
/* 240 */             Files.deleteIfExists(autoTempFile.getParent());
/* 241 */             Files.deleteIfExists(selectedTempFile.getParent());
/* 242 */           } catch (Throwable e) {
/* 243 */             this.modMain.getInterfaces().getMinimap().setCrashedWith(e);
/*     */ 
/*     */ 
/*     */             
/*     */             break;
/*     */           } 
/*     */ 
/*     */           
/* 251 */           autoWc = autoWorld.getContainer();
/* 252 */           selectedWc = selectedWorld.getContainer();
/* 253 */           autoWorld.setContainer(selectedWc);
/* 254 */           selectedWorld.setContainer(autoWc);
/* 255 */           selectedWc.worlds.put(selectedWorld.getId(), autoWorld);
/* 256 */           autoWc.worlds.put(autoWorld.getId(), selectedWorld);
/* 257 */           buSelected = selectedWorld.getId();
/* 258 */           selectedWorld.setId(autoWorld.getId());
/* 259 */           autoWorld.setId(buSelected);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 265 */           this.waypointsManager.setCustomWorldID(null);
/* 266 */           this.waypointsManager.setCustomContainerID(null);
/* 267 */           this.waypointsManager.updateWaypoints();
/*     */           break;
/*     */         
/*     */         case 9:
/* 271 */           selectedRootContainerId = this.waypointWorld.getContainer().getRootContainer().getKey();
/*     */           try {
/* 273 */             File directory = this.modMain.getWaypointsFolder().toPath().resolve(selectedRootContainerId).toFile();
/* 274 */             if (directory.exists())
/* 275 */               FileUtils.deleteDirectory(directory); 
/* 276 */           } catch (Throwable e) {
/* 277 */             this.modMain.getInterfaces().getMinimap().setCrashedWith(e);
/*     */             break;
/*     */           } 
/* 280 */           this.waypointsManager.getWaypointMap().remove(selectedRootContainerId);
/*     */           
/* 282 */           this.waypointsManager.setCustomWorldID(null);
/* 283 */           this.waypointsManager.setCustomContainerID(null);
/* 284 */           this.waypointsManager.updateWaypoints();
/*     */           break;
/*     */         case 10:
/* 287 */           selectedWorld = this.waypointWorld;
/*     */           try {
/* 289 */             Files.deleteIfExists(this.modMain.getSettings().getWaypointsFile(selectedWorld).toPath());
/* 290 */           } catch (IOException e) {
/* 291 */             e.printStackTrace();
/*     */           } 
/* 293 */           (selectedWorld.getContainer()).worlds.remove(selectedWorld.getId());
/* 294 */           selectedWorld.getContainer().removeName(selectedWorld.getId());
/*     */           
/* 296 */           this.waypointsManager.setCustomWorldID(null);
/* 297 */           this.waypointsManager.setCustomContainerID(null);
/* 298 */           this.waypointsManager.updateWaypoints();
/*     */           break;
/*     */         case 11:
/* 301 */           multiplyWaypoints(this.waypointWorld, 8.0D);
/*     */           break;
/*     */         case 12:
/* 304 */           multiplyWaypoints(this.waypointWorld, 0.125D);
/*     */           break;
/*     */       } 
/*     */     } 
/* 308 */     if (this.parent instanceof GuiWaypoints) {
/* 309 */       this.field_146297_k.func_147108_a(new GuiWaypoints(this.modMain, ((GuiWaypoints)this.parent).getParentScreen()));
/*     */     } else {
/* 311 */       this.field_146297_k.func_147108_a(this.parent);
/*     */     } 
/*     */   }
/*     */   private void multiplyWaypoints(WaypointWorld world, double factor) {
/* 315 */     HashMap<String, WaypointSet> sets = world.getSets();
/* 316 */     Iterator<WaypointSet> iter = sets.values().iterator();
/* 317 */     while (iter.hasNext()) {
/* 318 */       ArrayList<Waypoint> wpList = ((WaypointSet)iter.next()).getList();
/* 319 */       for (int i = 0; i < wpList.size(); i++) {
/* 320 */         Waypoint wp = wpList.get(i);
/* 321 */         wp.setX((int)Math.floor(wp.getX() * factor));
/* 322 */         wp.setZ((int)Math.floor(wp.getZ() * factor));
/*     */       } 
/*     */     } 
/*     */     try {
/* 326 */       this.modMain.getSettings().saveWaypoints(world);
/* 327 */     } catch (IOException e) {
/* 328 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void func_73863_a(int par1, int par2, float par3) {
/* 334 */     this.deleteButton.field_146124_l = (this.waypointsManager.getAutoRootContainerID() != null && !this.waypointsManager.getAutoRootContainerID().equals(this.waypointWorld.getContainer().getRootContainer().getKey()));
/* 335 */     this.subDeleteButton.field_146124_l = (!this.automaticButton.field_146124_l && this.waypointWorld != this.waypointsManager.getAutoWorld());
/* 336 */     this.parent.func_73863_a(0, 0, par3);
/* 337 */     func_146276_q_();
/* 338 */     super.func_73863_a(par1, par2, par3);
/* 339 */     for (int k = 0; k < this.field_146292_n.size(); k++) {
/* 340 */       GuiButton w = this.field_146292_n.get(k);
/* 341 */       if (w instanceof MySmallButton) {
/*     */         
/* 343 */         MySmallButton b = (MySmallButton)w;
/* 344 */         if (b.getId() >= 200 && par1 >= b.field_146128_h && par2 >= b.field_146129_i && par1 < b.field_146128_h + 150 && par2 < b.field_146129_i + 20)
/* 345 */           switch (b.getId() - 200) {
/*     */             case 0:
/* 347 */               this.mwTooltip.drawBox(par1, par2, this.field_146294_l, this.field_146295_m);
/*     */               break;
/*     */             case 1:
/* 350 */               this.teleportationTooltip.drawBox(par1, par2, this.field_146294_l, this.field_146295_m);
/*     */               break;
/*     */           }  
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\ioanm\Downloads\Xaeros_Minimap_20.15.3_Forge_1.12.jar!\xaero\common\gui\GuiWaypointsOptions.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */