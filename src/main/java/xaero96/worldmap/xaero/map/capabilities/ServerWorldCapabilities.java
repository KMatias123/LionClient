/*    */ package xaero.map.capabilities;
/*    */ 
/*    */ import java.util.concurrent.Callable;
/*    */ import net.minecraft.util.EnumFacing;
/*    */ import net.minecraftforge.common.capabilities.Capability;
/*    */ import net.minecraftforge.common.capabilities.CapabilityInject;
/*    */ import net.minecraftforge.common.capabilities.CapabilityManager;
/*    */ import net.minecraftforge.common.capabilities.ICapabilityProvider;
/*    */ 
/*    */ public class ServerWorldCapabilities
/*    */   implements ICapabilityProvider
/*    */ {
/*    */   @CapabilityInject(ServerWorldLoaded.class)
/* 14 */   public static final Capability<ServerWorldLoaded> LOADED_CAP = null;
/*    */   
/*    */   public static void registerCapabilities() {
/* 17 */     CapabilityManager.INSTANCE.register(ServerWorldLoaded.class, new ServerWorldLoaded.Storage(), new Callable<ServerWorldLoaded>()
/*    */         {
/*    */           public ServerWorldLoaded call() throws Exception {
/* 20 */             return new ServerWorldLoaded();
/*    */           }
/*    */         });
/*    */   }
/*    */   private ServerWorldLoaded loadedCapability;
/*    */   
/*    */   public ServerWorldCapabilities() {
/* 27 */     this.loadedCapability = new ServerWorldLoaded();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public <T> T getCapability(Capability<T> cap, EnumFacing side) {
/* 33 */     if (cap == LOADED_CAP)
/* 34 */       return (T)this.loadedCapability; 
/* 35 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean hasCapability(Capability<?> cap, EnumFacing facing) {
/* 40 */     if (cap == LOADED_CAP)
/* 41 */       return true; 
/* 42 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\ioanm\Downloads\XaerosWorldMap_1.7.3_Forge_1.12.jar!\xaero\map\capabilities\ServerWorldCapabilities.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */