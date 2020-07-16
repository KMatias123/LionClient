/*    */ package xaero.common.core.transformer;
/*    */ 
/*    */ import org.objectweb.asm.tree.AbstractInsnNode;
/*    */ import org.objectweb.asm.tree.ClassNode;
/*    */ import org.objectweb.asm.tree.InsnList;
/*    */ import org.objectweb.asm.tree.MethodInsnNode;
/*    */ import org.objectweb.asm.tree.MethodNode;
/*    */ import org.objectweb.asm.tree.VarInsnNode;
/*    */ 
/*    */ 
/*    */ public class NetHandlerPlayClientTransformer
/*    */   extends ClassNodeTransformer
/*    */ {
/*    */   public byte[] transform(String name, String transformedName, byte[] basicClass) {
/* 15 */     this.className = "net.minecraft.client.network.NetHandlerPlayClient";
/* 16 */     return super.transform(name, transformedName, basicClass);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void transformNode(ClassNode classNode, boolean isObfuscated) {
/* 21 */     String methodNameHandleMultiBlockChange = isObfuscated ? "a" : "handleMultiBlockChange";
/* 22 */     String methodNameHandleChunkData = isObfuscated ? "a" : "handleChunkData";
/* 23 */     String methodNameHandleBlockChange = isObfuscated ? "a" : "handleBlockChange";
/* 24 */     String methodDescHandleMultiBlockChange = isObfuscated ? "(Lio;)V" : "(Lnet/minecraft/network/play/server/SPacketMultiBlockChange;)V";
/* 25 */     String methodDescHandleChunkData = isObfuscated ? "(Lje;)V" : "(Lnet/minecraft/network/play/server/SPacketChunkData;)V";
/* 26 */     String methodDescHandleBlockChange = isObfuscated ? "(Lij;)V" : "(Lnet/minecraft/network/play/server/SPacketBlockChange;)V";
/* 27 */     boolean multiBlockChangeRedirected = false;
/* 28 */     boolean chunkDataRedirected = false;
/* 29 */     boolean blockChangeRedirected = false;
/* 30 */     for (MethodNode mn : classNode.methods) {
/*    */       
/* 32 */       if (mn.name.equals(methodNameHandleMultiBlockChange) && mn.desc.equals(methodDescHandleMultiBlockChange)) {
/* 33 */         clientPacketRedirectTransform(mn, new MethodInsnNode(184, "xaero/common/core/XaeroMinimapCore", "onMultiBlockChange", methodDescHandleMultiBlockChange, false));
/*    */         
/* 35 */         multiBlockChangeRedirected = true;
/* 36 */       } else if (mn.name.equals(methodNameHandleChunkData) && mn.desc.equals(methodDescHandleChunkData)) {
/* 37 */         clientPacketRedirectTransform(mn, new MethodInsnNode(184, "xaero/common/core/XaeroMinimapCore", "onChunkData", methodDescHandleChunkData, false));
/*    */         
/* 39 */         chunkDataRedirected = true;
/* 40 */       } else if (mn.name.equals(methodNameHandleBlockChange) && mn.desc.equals(methodDescHandleBlockChange)) {
/* 41 */         clientPacketRedirectTransform(mn, new MethodInsnNode(184, "xaero/common/core/XaeroMinimapCore", "onBlockChange", methodDescHandleBlockChange, false));
/*    */         
/* 43 */         blockChangeRedirected = true;
/*    */       } 
/* 45 */       if (multiBlockChangeRedirected && chunkDataRedirected && blockChangeRedirected)
/*    */         break; 
/*    */     } 
/*    */   }
/*    */   
/*    */   private void clientPacketRedirectTransform(MethodNode methodNode, MethodInsnNode methodInsnNode) {
/* 51 */     InsnList instructions = methodNode.instructions;
/* 52 */     InsnList patchList = new InsnList();
/* 53 */     patchList.add((AbstractInsnNode)new VarInsnNode(25, 1));
/*    */     
/* 55 */     patchList.add((AbstractInsnNode)methodInsnNode);
/* 56 */     for (int i = 0; i < instructions.size(); i++) {
/* 57 */       AbstractInsnNode insn = instructions.get(i);
/* 58 */       if (insn.getOpcode() == 184) {
/* 59 */         MethodInsnNode methodInsn = (MethodInsnNode)insn;
/* 60 */         if (methodInsn.owner.equals("net/minecraft/network/PacketThreadUtil") && (methodInsn.name.equals("checkThreadAndEnqueue") || methodInsn.name.equals("func_180031_a"))) {
/* 61 */           instructions.insert(insn, patchList);
/*    */           break;
/*    */         } 
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\ioanm\Downloads\Xaeros_Minimap_20.15.3_Forge_1.12.jar!\xaero\common\core\transformer\NetHandlerPlayClientTransformer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */