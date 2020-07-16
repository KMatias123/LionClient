/*     */ package xaero.common.api.spigot.message.in;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.IOException;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
/*     */ import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
/*     */ import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
/*     */ import xaero.common.api.spigot.ServerWaypoint;
/*     */ import xaero.common.api.spigot.ServerWaypointStorage;
/*     */ import xaero.common.api.spigot.message.MessageWaypoint;
/*     */ import xaero.common.minimap.waypoints.WaypointWorld;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class InMessageWaypoint
/*     */   extends MessageWaypoint
/*     */   implements IMessage
/*     */ {
/*     */   public void fromBytes(ByteBuf buf) {
/*  27 */     byte[] bytes = new byte[buf.readableBytes()];
/*  28 */     buf.readBytes(bytes);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  33 */     DataInputStream in = new DataInputStream(new ByteArrayInputStream(bytes)); try {
/*     */       int x, y, z; String name; char symbol; int color, ID; boolean hasRotation; int yaw;
/*  35 */       this.waypoint = null;
/*  36 */       this.packetID = in.readChar();
/*     */       
/*  38 */       switch (this.packetID) {
/*     */         case 'A':
/*  40 */           this.worldUID = in.readUTF();
/*     */           
/*  42 */           x = in.readInt();
/*     */           
/*  44 */           y = in.readInt();
/*     */           
/*  46 */           z = in.readInt();
/*     */           
/*  48 */           name = in.readUTF();
/*     */           
/*  50 */           symbol = in.readChar();
/*     */           
/*  52 */           color = in.read();
/*     */           
/*  54 */           ID = in.readInt();
/*     */           
/*  56 */           hasRotation = in.readBoolean();
/*     */           
/*  58 */           yaw = 0;
/*  59 */           if (hasRotation) {
/*  60 */             yaw = in.readShort();
/*     */           }
/*     */           
/*  63 */           this.waypoint = new ServerWaypoint(this.worldUID, ID, x, y, z, name, Character.toString(symbol), color, hasRotation, yaw);
/*     */           break;
/*     */         case 'R':
/*  66 */           this.waypointID = in.readInt();
/*     */           break;
/*     */         case 'W':
/*  69 */           this.worldUID = in.readUTF();
/*  70 */           this.worldName = in.readUTF();
/*     */           break;
/*     */       } 
/*  73 */       in.close();
/*  74 */     } catch (IOException e) {
/*  75 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void toBytes(ByteBuf buf) {
/*  81 */     System.err.println("Incorrect packet usage! (InMessageWaypoint)");
/*     */   }
/*     */ 
/*     */   
/*     */   public void run() {
/*     */     ServerWaypoint wp;
/*  87 */     switch (this.packetID) {
/*     */       case 'A':
/*  89 */         wp = this.waypoint;
/*  90 */         if (wp != null) {
/*  91 */           WaypointWorld world = ServerWaypointStorage.getWorld(wp.getWorldUID());
/*  92 */           if (world != null)
/*  93 */             world.getServerWaypoints().put(Integer.valueOf(wp.getID()), wp); 
/*     */         } 
/*     */         break;
/*     */       case 'R':
/*  97 */         ServerWaypointStorage.removeWaypoint(this.waypointID);
/*     */         break;
/*     */       case 'W':
/* 100 */         ServerWaypointStorage.addWorld(this.worldUID, this.worldName);
/* 101 */         ServerWaypointStorage.autoWorldUID = this.worldUID;
/*     */         break;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static class Handler
/*     */     implements IMessageHandler<InMessageWaypoint, IMessage>
/*     */   {
/*     */     public IMessage onMessage(InMessageWaypoint message, MessageContext ctx) {
/* 110 */       Minecraft minecraft = Minecraft.func_71410_x();
/* 111 */       minecraft.func_152344_a((Runnable)message);
/* 112 */       return null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ioanm\Downloads\Xaeros_Minimap_20.15.3_Forge_1.12.jar!\xaero\common\api\spigot\message\in\InMessageWaypoint.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */