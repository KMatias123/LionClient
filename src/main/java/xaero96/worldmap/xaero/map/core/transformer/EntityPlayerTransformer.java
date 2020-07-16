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
/*    */ public class EntityPlayerTransformer
/*    */   extends ClassNodeTransformer
/*    */ {
/*    */   public byte[] transform(String name, String transformedName, byte[] basicClass) {
/* 19 */     this.className = "net.minecraft.entity.player.EntityPlayer";
/* 20 */     return super.transform(name, transformedName, basicClass);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void transformNode(ClassNode classNode, boolean isObfuscated) {
/* 25 */     String methodName = isObfuscated ? "a" : "isWearing";
/* 26 */     String methodDesc = isObfuscated ? "(Laee;)Z" : "(Lnet/minecraft/entity/player/EnumPlayerModelParts;)Z";
/*    */     
/* 28 */     for (MethodNode methodNode : classNode.methods) {
/*    */       
/* 30 */       if (methodNode.name.equals(methodName) && methodNode.desc.equals(methodDesc)) {
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */         
/* 44 */         LabelNode MY_LABEL = new LabelNode(new Label());
/* 45 */         InsnList insnToInsert = new InsnList();
/* 46 */         insnToInsert.add((AbstractInsnNode)new VarInsnNode(25, 0));
/* 47 */         insnToInsert.add((AbstractInsnNode)new VarInsnNode(25, 1));
/* 48 */         insnToInsert.add((AbstractInsnNode)new MethodInsnNode(184, "xaero/map/core/XaeroWorldMapCore", "isWearing", "(Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/entity/player/EnumPlayerModelParts;)Ljava/lang/Boolean;", false));
/* 49 */         insnToInsert.add((AbstractInsnNode)new InsnNode(89));
/* 50 */         insnToInsert.add((AbstractInsnNode)new JumpInsnNode(198, MY_LABEL));
/* 51 */         insnToInsert.add((AbstractInsnNode)new FrameNode(4, 2, null, 1, new Object[] { "java/lang/Boolean" }));
/* 52 */         insnToInsert.add((AbstractInsnNode)new MethodInsnNode(182, "java/lang/Boolean", "booleanValue", "()Z", false));
/* 53 */         insnToInsert.add((AbstractInsnNode)new InsnNode(172));
/* 54 */         insnToInsert.add((AbstractInsnNode)MY_LABEL);
/* 55 */         insnToInsert.add((AbstractInsnNode)new FrameNode(4, 2, null, 1, new Object[] { "java/lang/Boolean" }));
/* 56 */         insnToInsert.add((AbstractInsnNode)new InsnNode(87));
/* 57 */         methodNode.instructions.insert(methodNode.instructions.get(0), insnToInsert);
/*    */         break;
/*    */       } 
/*    */     } 
/* 61 */     System.out.println();
/*    */   }
/*    */ }


/* Location:              C:\Users\ioanm\Downloads\XaerosWorldMap_1.7.3_Forge_1.12.jar!\xaero\map\core\transformer\EntityPlayerTransformer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */