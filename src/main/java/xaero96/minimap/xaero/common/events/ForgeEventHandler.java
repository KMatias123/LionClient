/*     */ package xaero.common.events;
/*     */ 
/*     */ import com.mojang.realmsclient.dto.RealmsServer;
/*     */ import com.mojang.realmsclient.gui.screens.RealmsLongRunningMcoTaskScreen;
/*     */ import com.mojang.realmsclient.util.RealmsTasks;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Field;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.GuiScreen;
/*     */ import net.minecraft.client.gui.GuiScreenRealmsProxy;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraftforge.client.GuiIngameForge;
/*     */ import net.minecraftforge.client.event.ClientChatEvent;
/*     */ import net.minecraftforge.client.event.ClientChatReceivedEvent;
/*     */ import net.minecraftforge.client.event.GuiOpenEvent;
/*     */ import net.minecraftforge.client.event.GuiScreenEvent;
/*     */ import net.minecraftforge.client.event.RenderGameOverlayEvent;
/*     */ import net.minecraftforge.client.event.RenderWorldLastEvent;
/*     */ import net.minecraftforge.client.event.TextureStitchEvent;
/*     */ import net.minecraftforge.event.entity.player.PlayerSetSpawnEvent;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ import org.lwjgl.input.Keyboard;
/*     */ import xaero.common.IXaeroMinimap;
/*     */ import xaero.common.anim.OldAnimation;
/*     */ import xaero.common.gui.GuiEditMode;
/*     */ import xaero.common.gui.GuiUpdate;
/*     */ import xaero.common.settings.ModSettings;
/*     */ import xaero.patreon.GuiUpdateAll;
/*     */ import xaero.patreon.Patreon4;
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
/*     */ public class ForgeEventHandler
/*     */ {
/*  45 */   public static final RenderGameOverlayEvent.ElementType[] OVERLAY_LAYERS = new RenderGameOverlayEvent.ElementType[] { RenderGameOverlayEvent.ElementType.ALL, RenderGameOverlayEvent.ElementType.HELMET, RenderGameOverlayEvent.ElementType.HOTBAR, RenderGameOverlayEvent.ElementType.CROSSHAIRS, RenderGameOverlayEvent.ElementType.BOSSHEALTH, RenderGameOverlayEvent.ElementType.TEXT, RenderGameOverlayEvent.ElementType.POTION_ICONS, RenderGameOverlayEvent.ElementType.SUBTITLES, RenderGameOverlayEvent.ElementType.CHAT };
/*     */   
/*     */   private IXaeroMinimap modMain;
/*     */   private GuiScreen lastGuiOpen;
/*     */   private long died;
/*     */   private GuiScreen lastLastGuiOpen;
/*     */   private int deathCounter;
/*     */   private Field realmsTaskField;
/*     */   private Field realmsTaskServerField;
/*     */   private boolean askedToUpdate;
/*     */   private boolean crosshairDisabledByThisMod = false;
/*     */   
/*     */   public ForgeEventHandler(IXaeroMinimap modMain) {
/*  58 */     this.modMain = modMain;
/*  59 */     this.died = -1L;
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void handleGuiOpenEvent(GuiOpenEvent event) {
/*  64 */     if (event.getGui() == null && (
/*  65 */       this.lastGuiOpen instanceof net.minecraft.client.gui.GuiGameOver || (this.lastGuiOpen instanceof net.minecraft.client.gui.GuiYesNo && this.lastLastGuiOpen instanceof net.minecraft.client.gui.GuiGameOver))) {
/*  66 */       this.died = System.currentTimeMillis();
/*     */     }
/*     */ 
/*     */     
/*  70 */     if (event.getGui() instanceof net.minecraft.client.gui.GuiOptions) {
/*  71 */       if (!ModSettings.settingsButton) {
/*     */         return;
/*     */       }
/*  74 */       event.setGui((GuiScreen)this.modMain.getGuiHelper().getMyOptions());
/*     */       try {
/*  76 */         this.modMain.getSettings().saveSettings();
/*  77 */       } catch (IOException e) {
/*  78 */         e.printStackTrace();
/*     */       } 
/*     */     } 
/*  81 */     if (event.getGui() instanceof net.minecraft.client.gui.GuiMainMenu || event.getGui() instanceof net.minecraft.client.gui.GuiMultiplayer)
/*  82 */       this.modMain.getSettings().resetServerSettings(); 
/*  83 */     if (event.getGui() instanceof net.minecraft.client.gui.GuiGameOver) {
/*  84 */       this.deathCounter++;
/*  85 */       if ((this.deathCounter & 0x1) == 0)
/*  86 */         this.modMain.getWaypointsManager().createDeathpoint((EntityPlayer)(Minecraft.func_71410_x()).field_71439_g); 
/*     */     } 
/*  88 */     if (event.getGui() instanceof GuiScreenRealmsProxy && ((GuiScreenRealmsProxy)event.getGui()).func_154321_a() instanceof RealmsLongRunningMcoTaskScreen) {
/*     */       try {
/*  90 */         if (this.realmsTaskField == null) {
/*  91 */           this.realmsTaskField = RealmsLongRunningMcoTaskScreen.class.getDeclaredField("task");
/*  92 */           this.realmsTaskField.setAccessible(true);
/*     */         } 
/*  94 */         if (this.realmsTaskServerField == null) {
/*  95 */           this.realmsTaskServerField = RealmsTasks.RealmsGetServerDetailsTask.class.getDeclaredField("server");
/*  96 */           this.realmsTaskServerField.setAccessible(true);
/*     */         } 
/*  98 */         RealmsLongRunningMcoTaskScreen realmsTaskScreen = (RealmsLongRunningMcoTaskScreen)((GuiScreenRealmsProxy)event.getGui()).func_154321_a();
/*  99 */         Object task = this.realmsTaskField.get(realmsTaskScreen);
/* 100 */         if (task instanceof RealmsTasks.RealmsGetServerDetailsTask) {
/* 101 */           RealmsTasks.RealmsGetServerDetailsTask realmsTask = (RealmsTasks.RealmsGetServerDetailsTask)task;
/* 102 */           RealmsServer realm = (RealmsServer)this.realmsTaskServerField.get(realmsTask);
/* 103 */           if (realm != null && (this.modMain.getWaypointsManager().getLatestRealm() == null || realm.id != (this.modMain.getWaypointsManager().getLatestRealm()).id))
/*     */           {
/* 105 */             this.modMain.getWaypointsManager().setLatestRealm(realm);
/*     */           }
/*     */         } 
/* 108 */       } catch (Exception e) {
/* 109 */         e.printStackTrace();
/*     */       } 
/*     */     }
/* 112 */     this.lastLastGuiOpen = this.lastGuiOpen;
/* 113 */     this.lastGuiOpen = event.getGui();
/*     */   }
/*     */   
/*     */   protected void handleRenderGameOverlayEventPreOverridable(RenderGameOverlayEvent.Pre event) {
/* 117 */     if (event.getType() == OVERLAY_LAYERS[(this.modMain.getSettings()).renderLayerIndex]) {
/* 118 */       (Minecraft.func_71410_x()).field_71460_t.func_78478_c();
/* 119 */       GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
/* 120 */       this.modMain.getInterfaceRenderer().renderInterfaces(event.getPartialTicks());
/* 121 */       this.modMain.getInterfaces().getMinimapInterface().getWaypointsGuiRenderer().drawSetChange(event.getResolution());
/* 122 */       OldAnimation.tick();
/* 123 */       if (GuiIngameForge.renderCrosshairs && this.modMain.getInterfaces().getMinimap().isEnlargedMap() && (this.modMain.getSettings()).centeredEnlarged) {
/* 124 */         GuiIngameForge.renderCrosshairs = false;
/* 125 */         this.crosshairDisabledByThisMod = true;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void handleRenderGameOverlayEventPre(RenderGameOverlayEvent.Pre event) {
/* 132 */     if (Keyboard.isKeyDown(1))
/* 133 */       GuiEditMode.cancel(this.modMain.getInterfaces()); 
/* 134 */     handleRenderGameOverlayEventPreOverridable(event);
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   protected void handleRenderGameOverlayEventPost(RenderGameOverlayEvent.Post event) {
/* 139 */     if (event.getType() == RenderGameOverlayEvent.ElementType.ALL && 
/* 140 */       this.crosshairDisabledByThisMod) {
/* 141 */       GuiIngameForge.renderCrosshairs = true;
/* 142 */       this.crosshairDisabledByThisMod = false;
/*     */     } 
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void handleClientSendChatEvent(ClientChatEvent e) {
/* 148 */     if (e.getMessage().startsWith("xaero_waypoint_add:")) {
/* 149 */       String[] args = e.getMessage().replaceAll("§.", "").split(":");
/* 150 */       e.setMessage("");
/* 151 */       this.modMain.getWaypointSharing().onWaypointAdd(args);
/* 152 */     } else if (e.getMessage().equals("/xaero_tp_anyway")) {
/* 153 */       e.setMessage("");
/* 154 */       this.modMain.getWaypointsManager().teleportAnyway();
/*     */     } 
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void handleClientChatReceivedEvent(ClientChatReceivedEvent e) {
/* 160 */     if (e.getMessage() == null)
/*     */       return; 
/* 162 */     String text = e.getMessage().func_150254_d();
/* 163 */     if (text.contains("xaero_waypoint:") || text.contains("xaero_waypoint:".replace("_", "-")))
/* 164 */       this.modMain.getWaypointSharing().onWaypointReceived(text, e); 
/* 165 */     if (text.contains("§c §r§5 §r§1 §r§f")) {
/* 166 */       String code = text.substring(text.indexOf("f") + 1);
/* 167 */       code = code.replace("§", "").replace("r", "").replace(" ", "");
/* 168 */       this.modMain.getSettings().resetServerSettings();
/* 169 */       this.modMain.getSettings();
/* 170 */       ModSettings.serverSettings &= Integer.parseInt(code);
/* 171 */       System.out.println("Code: " + code);
/*     */     } 
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void handleRenderWorldLastEvent(RenderWorldLastEvent event) {
/* 177 */     if ((Minecraft.func_71410_x()).field_71441_e == (this.modMain.getInterfaces().getMinimap()).mainWorld)
/* 178 */       this.modMain.getInterfaces().getMinimapInterface().getWaypointsIngameRenderer().render(event.getPartialTicks()); 
/*     */   }
/*     */   
/*     */   protected void onOutdatedOverridable() {
/* 182 */     if (Patreon4.patronPledge >= 5) {
/* 183 */       Minecraft.func_71410_x().func_147108_a((GuiScreen)new GuiUpdateAll());
/*     */     } else {
/* 185 */       Minecraft.func_71410_x().func_147108_a((GuiScreen)new GuiUpdate(this.modMain, "A newer version of Xaero's Minimap is available!"));
/* 186 */     }  System.out.println("Minimap is outdated!");
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void handleDrawScreenEventPost(GuiScreenEvent.DrawScreenEvent.Post event) {
/* 191 */     if (event.getGui() instanceof GuiUpdate) {
/* 192 */       this.askedToUpdate = true;
/* 193 */     } else if (!this.askedToUpdate && this.modMain.isOutdated() && event.getGui() instanceof net.minecraft.client.gui.GuiMainMenu) {
/* 194 */       onOutdatedOverridable();
/* 195 */     } else if (this.modMain.isOutdated()) {
/* 196 */       this.modMain.setOutdated(false);
/*     */     } 
/*     */   }
/*     */   @SubscribeEvent
/*     */   public void handleTextureStitchEventPost(TextureStitchEvent.Post event) {
/* 201 */     this.modMain.getInterfaces().getMinimap().getMinimapWriter().setClearBlockColours(true);
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void handlePlayerSetSpawnEvent(PlayerSetSpawnEvent event) {
/* 206 */     if ((event.getEntityPlayer()).field_70170_p instanceof net.minecraft.client.multiplayer.WorldClient) {
/* 207 */       this.modMain.getWaypointsManager().setCurrentSpawn(event.getNewSpawn());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getLastGuiOpen() {
/* 215 */     return this.lastGuiOpen;
/*     */   }
/*     */   
/*     */   public long getDied() {
/* 219 */     return this.died;
/*     */   }
/*     */ }


/* Location:              C:\Users\ioanm\Downloads\Xaeros_Minimap_20.15.3_Forge_1.12.jar!\xaero\common\events\ForgeEventHandler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */