/*    */ package xaero.map.core.transformer;
/*    */ 
/*    */ import org.objectweb.asm.Label;
/*    */ import org.objectweb.asm.tree.AbstractInsnNode;
/*    */ import org.objectweb.asm.tree.ClassNode;
/*    */ import org.objectweb.asm.tree.FrameNode;
/*    */ import org.objectweb.asm.tree.InsnList;
/*    */ import org.objectweb.asm.tree.InsnNode;
/*    */ import org.objectweb.asm.tree.JumpInsnNode;
/*    */ import org.objectweb.asm.tree.LabelNode;
/*    */ import org.objectweb.asm.tree.MethodInsnNode;
/*    */ import org.objectweb.asm.tree.MethodNode;
/*    */ import org.objectweb.asm.tree.VarInsnNode;
/*    */ 
/*    */ public class AbstractClientPlayerTransformer
/*    */   extends ClassNodeTransformer
/*    */ {
/*    */   public byte[] transform(String name, String transformedName, byte[] basicClass) {
/* 19 */     this.className = "net.minecraft.client.entity.AbstractClientPlayer";
/* 20 */     return super.transform(name, transformedName, basicClass);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void transformNode(ClassNode classNode, boolean isObfuscated) {
/* 25 */     String methodName = isObfuscated ? "q" : "getLocationCape";
/* 26 */     String methodDesc = isObfuscated ? "()Lnf;" : "()Lnet/minecraft/util/ResourceLocation;";
/*    */     
/* 28 */     for (MethodNode methodNode : classNode.methods) {
/*    */       
/* 30 */       if (methodNode.name.equals(methodName) && methodNode.desc.equals(methodDesc)) {
/* 31 */         methodNode.maxStack++;
/* 32 */         LabelNode MY_LABEL = new LabelNode(new Label());
/* 33 */         InsnList insnToInsert = new InsnList();
/* 34 */         insnToInsert.add((AbstractInsnNode)new VarInsnNode(25, 0));
/* 35 */         insnToInsert.add((AbstractInsnNode)new MethodInsnNode(184, "xaero/map/core/XaeroWorldMapCore", "getPlayerCape", "(Lnet/minecraft/client/entity/AbstractClientPlayer;)Lnet/minecraft/util/ResourceLocation;", false));
/* 36 */         insnToInsert.add((AbstractInsnNode)new InsnNode(89));
/* 37 */         insnToInsert.add((AbstractInsnNode)new JumpInsnNode(198, MY_LABEL));
/* 38 */         insnToInsert.add((AbstractInsnNode)new FrameNode(4, 1, null, 1, new Object[] { "net/minecraft/util/ResourceLocation" }));
/* 39 */         insnToInsert.add((AbstractInsnNode)new InsnNode(176));
/* 40 */         insnToInsert.add((AbstractInsnNode)MY_LABEL);
/* 41 */         insnToInsert.add((AbstractInsnNode)new FrameNode(4, 1, null, 1, new Object[] { "net/minecraft/util/ResourceLocation" }));
/* 42 */         insnToInsert.add((AbstractInsnNode)new InsnNode(87));
/* 43 */         methodNode.instructions.insert(methodNode.instructions.get(0), insnToInsert);
/*    */         break;
/*    */       } 
/*    */     } 
/* 47 */     System.out.println();
/*    */   }
/*    */ }


/* Location:              C:\Users\ioanm\Downloads\XaerosWorldMap_1.7.3_Forge_1.12.jar!\xaero\map\core\transformer\AbstractClientPlayerTransformer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */