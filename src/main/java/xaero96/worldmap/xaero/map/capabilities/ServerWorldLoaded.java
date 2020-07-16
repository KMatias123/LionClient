/*    */ package xaero.map.capabilities;
/*    */ 
/*    */ import net.minecraft.nbt.NBTBase;
/*    */ import net.minecraft.nbt.NBTTagByte;
/*    */ import net.minecraft.util.EnumFacing;
/*    */ import net.minecraftforge.common.capabilities.Capability;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ServerWorldLoaded
/*    */ {
/*    */   public boolean loaded = true;
/*    */   
/*    */   public static class Storage
/*    */     implements Capability.IStorage<ServerWorldLoaded>
/*    */   {
/*    */     public NBTBase writeNBT(Capability<ServerWorldLoaded> capability, ServerWorldLoaded instance, EnumFacing side) {
/* 21 */       return (NBTBase)new NBTTagByte((byte)(instance.loaded ? 1 : 0));
/*    */     }
/*    */ 
/*    */     
/*    */     public void readNBT(Capability<ServerWorldLoaded> capability, ServerWorldLoaded instance, EnumFacing side, NBTBase nbt) {
/* 26 */       instance.loaded = (((NBTTagByte)nbt).func_150287_d() == 1);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ioanm\Downloads\XaerosWorldMap_1.7.3_Forge_1.12.jar!\xaero\map\capabilities\ServerWorldLoaded.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */