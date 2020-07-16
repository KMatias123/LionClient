/*    */ package xaero.map.core.transformer;
/*    */ 
/*    */ import net.minecraft.launchwrapper.IClassTransformer;
/*    */ import org.objectweb.asm.ClassReader;
/*    */ import org.objectweb.asm.ClassVisitor;
/*    */ import org.objectweb.asm.ClassWriter;
/*    */ import org.objectweb.asm.tree.ClassNode;
/*    */ 
/*    */ public abstract class ClassNodeTransformer
/*    */   implements IClassTransformer
/*    */ {
/*    */   protected String className;
/*    */   
/*    */   public byte[] transform(String name, String transformedName, byte[] basicClass) {
/* 15 */     if (transformedName.equals(this.className)) {
/* 16 */       System.out.println("Transforming class " + transformedName);
/* 17 */       boolean isObfuscated = !name.equals(transformedName);
/* 18 */       return transform(basicClass, isObfuscated);
/*    */     } 
/* 20 */     return basicClass;
/*    */   }
/*    */   
/*    */   private byte[] transform(byte[] basicClass, boolean isObfuscated) {
/* 24 */     ClassReader classReader = new ClassReader(basicClass);
/* 25 */     ClassNode classNode = new ClassNode(327680);
/* 26 */     classReader.accept((ClassVisitor)classNode, 0);
/* 27 */     transformNode(classNode, isObfuscated);
/* 28 */     ClassWriter classWriter = new ClassWriter(0);
/* 29 */     classNode.accept((ClassVisitor)classWriter);
/* 30 */     return classWriter.toByteArray();
/*    */   }
/*    */   
/*    */   protected abstract void transformNode(ClassNode paramClassNode, boolean paramBoolean);
/*    */ }


/* Location:              C:\Users\ioanm\Downloads\XaerosWorldMap_1.7.3_Forge_1.12.jar!\xaero\map\core\transformer\ClassNodeTransformer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */