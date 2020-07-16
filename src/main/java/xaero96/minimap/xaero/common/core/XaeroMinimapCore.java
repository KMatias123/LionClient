/*    */ package xaero.common.core;
/*    */ 
/*    */ import java.lang.reflect.Field;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.entity.AbstractClientPlayer;
/*    */ import net.minecraft.client.multiplayer.WorldClient;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.entity.player.EnumPlayerModelParts;
/*    */ import net.minecraft.network.play.server.SPacketBlockChange;
/*    */ import net.minecraft.network.play.server.SPacketChunkData;
/*    */ import net.minecraft.network.play.server.SPacketMultiBlockChange;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ import net.minecraft.world.chunk.Chunk;
/*    */ import xaero.common.IXaeroMinimap;
/*    */ import xaero.patreon.Patreon4;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class XaeroMinimapCore
/*    */ {
/*    */   public static IXaeroMinimap modMain;
/* 22 */   public static Field chunkCleanField = null;
/*    */   
/*    */   public static void ensureField() {
/* 25 */     if (chunkCleanField == null) {
/*    */       try {
/* 27 */         chunkCleanField = Chunk.class.getDeclaredField("xaero_chunkClean");
/* 28 */       } catch (NoSuchFieldException|SecurityException e) {
/* 29 */         throw new RuntimeException(e);
/*    */       } 
/*    */     }
/*    */   }
/*    */   
/*    */   public static void chunkUpdateCallback(int chunkX, int chunkZ) {
/* 35 */     ensureField();
/* 36 */     WorldClient worldClient = (Minecraft.func_71410_x()).field_71441_e;
/* 37 */     if (worldClient != null) {
/*    */       try {
/* 39 */         for (int x = chunkX - 1; x < chunkX + 2; x++) {
/* 40 */           for (int z = chunkZ - 1; z < chunkZ + 2; z++)
/* 41 */           { Chunk chunk = worldClient.func_72964_e(x, z);
/*    */             
/* 43 */             if (chunk != null)
/* 44 */               chunkCleanField.set(chunk, Boolean.valueOf(false));  } 
/*    */         } 
/* 46 */       } catch (IllegalArgumentException|IllegalAccessException e) {
/* 47 */         throw new RuntimeException(e);
/*    */       } 
/*    */     }
/*    */   }
/*    */   
/*    */   public static void onChunkData(SPacketChunkData packetIn) {
/* 53 */     chunkUpdateCallback(packetIn.func_149273_e(), packetIn.func_149271_f());
/*    */   }
/*    */ 
/*    */   
/*    */   public static void onBlockChange(SPacketBlockChange packetIn) {
/* 58 */     chunkUpdateCallback(packetIn.func_179827_b().func_177958_n() >> 4, packetIn.func_179827_b().func_177952_p() >> 4);
/*    */   }
/*    */ 
/*    */   
/*    */   public static void onMultiBlockChange(SPacketMultiBlockChange packetIn) {
/* 63 */     chunkUpdateCallback(packetIn.func_179844_a()[0].func_180090_a().func_177958_n() >> 4, packetIn.func_179844_a()[0].func_180090_a().func_177952_p() >> 4);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static ResourceLocation getPlayerCape(AbstractClientPlayer player) {
/* 73 */     return Patreon4.getPlayerCape(modMain.getFileLayoutID(), player);
/*    */   }
/*    */   
/*    */   public static Boolean isWearing(EntityPlayer player, EnumPlayerModelParts part) {
/* 77 */     if (part != EnumPlayerModelParts.CAPE || !(player instanceof AbstractClientPlayer))
/* 78 */       return null; 
/* 79 */     return Patreon4.isWearingCape(modMain.getFileLayoutID(), (AbstractClientPlayer)player);
/*    */   }
/*    */ }


/* Location:              C:\Users\ioanm\Downloads\Xaeros_Minimap_20.15.3_Forge_1.12.jar!\xaero\common\core\XaeroMinimapCore.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */