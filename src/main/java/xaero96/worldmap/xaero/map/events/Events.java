/*     */ package xaero.map.events;
/*     */ 
/*     */ import com.google.common.util.concurrent.ListenableFutureTask;
/*     */ import com.mojang.realmsclient.dto.RealmsServer;
/*     */ import com.mojang.realmsclient.gui.screens.RealmsLongRunningMcoTaskScreen;
/*     */ import com.mojang.realmsclient.util.RealmsTasks;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Field;
/*     */ import java.util.Queue;
/*     */ import java.util.concurrent.FutureTask;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.GuiScreen;
/*     */ import net.minecraft.client.gui.GuiScreenRealmsProxy;
/*     */ import net.minecraft.client.gui.ScaledResolution;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.WorldServer;
/*     */ import net.minecraftforge.client.ForgeHooksClient;
/*     */ import net.minecraftforge.client.event.GuiOpenEvent;
/*     */ import net.minecraftforge.client.event.GuiScreenEvent;
/*     */ import net.minecraftforge.client.event.RenderGameOverlayEvent;
/*     */ import net.minecraftforge.client.event.TextureStitchEvent;
/*     */ import net.minecraftforge.common.capabilities.ICapabilityProvider;
/*     */ import net.minecraftforge.event.AttachCapabilitiesEvent;
/*     */ import net.minecraftforge.event.entity.player.PlayerSetSpawnEvent;
/*     */ import net.minecraftforge.event.world.WorldEvent;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ import net.minecraftforge.fml.common.gameevent.TickEvent;
/*     */ import org.lwjgl.input.Mouse;
/*     */ import xaero.map.MapProcessor;
/*     */ import xaero.map.WorldMap;
/*     */ import xaero.map.capabilities.ServerWorldCapabilities;
/*     */ import xaero.map.gui.GuiUpdate;
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
/*     */ public class Events
/*     */ {
/*     */   private RealmsServer latestRealm;
/*     */   private Field realmsTaskField;
/*     */   private Field realmsTaskServerField;
/*     */   private static boolean askedToUpdate = false;
/*     */   
/*     */   @SubscribeEvent
/*     */   public void guiButtonClick(GuiScreenEvent.ActionPerformedEvent event) {}
/*     */   
/*     */   @SubscribeEvent
/*     */   public void guiOpen(GuiOpenEvent event) {
/*  57 */     if (event.getGui() instanceof GuiScreenRealmsProxy && ((GuiScreenRealmsProxy)event.getGui()).func_154321_a() instanceof RealmsLongRunningMcoTaskScreen) {
/*     */       try {
/*  59 */         if (this.realmsTaskField == null) {
/*  60 */           this.realmsTaskField = RealmsLongRunningMcoTaskScreen.class.getDeclaredField("task");
/*  61 */           this.realmsTaskField.setAccessible(true);
/*     */         } 
/*  63 */         if (this.realmsTaskServerField == null) {
/*  64 */           this.realmsTaskServerField = RealmsTasks.RealmsGetServerDetailsTask.class.getDeclaredField("server");
/*  65 */           this.realmsTaskServerField.setAccessible(true);
/*     */         } 
/*  67 */         RealmsLongRunningMcoTaskScreen realmsTaskScreen = (RealmsLongRunningMcoTaskScreen)((GuiScreenRealmsProxy)event.getGui()).func_154321_a();
/*  68 */         Object task = this.realmsTaskField.get(realmsTaskScreen);
/*  69 */         if (task instanceof RealmsTasks.RealmsGetServerDetailsTask) {
/*  70 */           RealmsTasks.RealmsGetServerDetailsTask realmsTask = (RealmsTasks.RealmsGetServerDetailsTask)task;
/*  71 */           RealmsServer realm = (RealmsServer)this.realmsTaskServerField.get(realmsTask);
/*  72 */           if (realm != null && (this.latestRealm == null || realm.id != this.latestRealm.id))
/*     */           {
/*  74 */             this.latestRealm = realm;
/*     */           }
/*     */         } 
/*  77 */       } catch (Exception e) {
/*  78 */         e.printStackTrace();
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @SubscribeEvent
/*     */   public void modelBake(TextureStitchEvent.Post event) throws IOException {
/*  87 */     MapProcessor.instance.getMapWriter().getColorTypeCache().updateGrassColor();
/*  88 */     MapProcessor.instance.getMapWriter().requestCachedColoursClear();
/*  89 */     MapProcessor.instance.incrementGlobalVersion();
/*     */   }
/*     */ 
/*     */   
/*     */   @SubscribeEvent
/*     */   public void renderTick(TickEvent.RenderTickEvent event) throws Exception {
/*  95 */     Minecraft mc = Minecraft.func_71410_x();
/*  96 */     if (event.phase == TickEvent.Phase.END) {
/*  97 */       MapProcessor.instance.onRenderProcess(mc);
/*  98 */       mc.field_71454_w = false;
/*  99 */       MapProcessor.instance.resetRenderStartTime();
/*     */       
/* 101 */       Queue<FutureTask<?>> minecraftScheduledTasks = MapProcessor.instance.getMinecraftScheduledTasks();
/* 102 */       ListenableFutureTask<?> listenablefuturetask = ListenableFutureTask.create(MapProcessor.instance.getRenderStartTimeUpdater());
/* 103 */       synchronized (minecraftScheduledTasks) {
/* 104 */         FutureTask[] arrayOfFutureTask = (FutureTask[])minecraftScheduledTasks.toArray((Object[])new FutureTask[0]);
/* 105 */         minecraftScheduledTasks.clear();
/* 106 */         minecraftScheduledTasks.add(listenablefuturetask);
/* 107 */         for (FutureTask<?> t : arrayOfFutureTask) {
/* 108 */           minecraftScheduledTasks.add(t);
/*     */         }
/*     */       } 
/* 111 */     } else if (event.phase == TickEvent.Phase.START) {
/* 112 */       if (mc.field_71462_r instanceof xaero.map.gui.GuiMap) {
/* 113 */         ScaledResolution scaledresolution = new ScaledResolution(mc);
/* 114 */         int i1 = scaledresolution.func_78326_a();
/* 115 */         int j1 = scaledresolution.func_78328_b();
/* 116 */         int k1 = Mouse.getX() * i1 / mc.field_71443_c;
/* 117 */         int l1 = j1 - Mouse.getY() * j1 / mc.field_71440_d - 1;
/* 118 */         GlStateManager.func_179126_j();
/* 119 */         GlStateManager.func_179083_b(0, 0, mc.field_71443_c, mc.field_71440_d);
/* 120 */         GlStateManager.func_179128_n(5889);
/* 121 */         GlStateManager.func_179096_D();
/* 122 */         GlStateManager.func_179128_n(5888);
/* 123 */         GlStateManager.func_179096_D();
/* 124 */         mc.field_71460_t.func_78478_c();
/* 125 */         GlStateManager.func_179086_m(256);
/* 126 */         ForgeHooksClient.drawScreen(mc.field_71462_r, k1, l1, 0.0F);
/* 127 */         mc.field_71454_w = true;
/*     */       } 
/* 129 */       if (MapProcessor.instance != null) {
/* 130 */         MapProcessor.instance.setMainValues();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @SubscribeEvent
/*     */   public void renderTick(GuiScreenEvent.DrawScreenEvent.Post event) {
/* 139 */     if (event.getGui() instanceof GuiUpdate) {
/* 140 */       askedToUpdate = true;
/* 141 */     } else if (!askedToUpdate && WorldMap.isOutdated && event.getGui() instanceof net.minecraft.client.gui.GuiMainMenu) {
/* 142 */       if (Patreon4.patronPledge >= 5) {
/* 143 */         Minecraft.func_71410_x().func_147108_a((GuiScreen)new GuiUpdateAll());
/*     */       } else {
/* 145 */         Minecraft.func_71410_x().func_147108_a((GuiScreen)new GuiUpdate());
/* 146 */       }  System.out.println("World Map is outdated!");
/* 147 */     } else if (WorldMap.isOutdated) {
/* 148 */       WorldMap.isOutdated = false;
/*     */     } 
/*     */   }
/*     */   @SubscribeEvent
/*     */   public void spawnSet(PlayerSetSpawnEvent event) {
/* 153 */     if ((event.getEntityPlayer()).field_70170_p instanceof net.minecraft.client.multiplayer.WorldClient) {
/* 154 */       MapProcessor.instance.updateWorldSpawn(event.getNewSpawn(), (event.getEntityPlayer()).field_70170_p);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @SubscribeEvent
/*     */   public void worldCapabilities(AttachCapabilitiesEvent<World> event) {
/* 165 */     if (event.getObject() instanceof WorldServer)
/* 166 */       event.addCapability(new ResourceLocation("xaeroworldmap", "server_world_caps"), (ICapabilityProvider)new ServerWorldCapabilities()); 
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void worldUnload(WorldEvent.Unload event) {
/* 171 */     if ((Minecraft.func_71410_x()).field_71439_g != null && event.getWorld() == MapProcessor.instance.mainWorld)
/* 172 */       MapProcessor.instance.onWorldUnload(); 
/* 173 */     if (event.getWorld() instanceof WorldServer) {
/* 174 */       WorldServer sw = (WorldServer)event.getWorld();
/* 175 */       MapProcessor.instance.getWorldDataHandler().onServerWorldUnload(sw);
/*     */     } 
/*     */   }
/*     */   
/*     */   public RealmsServer getLatestRealm() {
/* 180 */     return this.latestRealm;
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   protected void handleRenderGameOverlayEventPost(RenderGameOverlayEvent.Post event) {
/* 185 */     if (event.getType() == RenderGameOverlayEvent.ElementType.CROSSHAIRS) {
/* 186 */       String crosshairMessage = MapProcessor.instance.getCrosshairMessage();
/* 187 */       if (crosshairMessage != null) {
/* 188 */         int messageWidth = (Minecraft.func_71410_x()).field_71466_p.func_78256_a(crosshairMessage);
/* 189 */         GlStateManager.func_179084_k();
/* 190 */         (Minecraft.func_71410_x()).field_71466_p.func_175063_a(crosshairMessage, (event.getResolution().func_78326_a() / 2 - messageWidth / 2), (event.getResolution().func_78328_b() / 2 + 60), -1);
/* 191 */         GlStateManager.func_179147_l();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\ioanm\Downloads\XaerosWorldMap_1.7.3_Forge_1.12.jar!\xaero\map\events\Events.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */