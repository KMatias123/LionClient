/*    */ package xaero.deallocator;
/*    */ 
/*    */ import java.lang.reflect.Field;
/*    */ import java.lang.reflect.InvocationTargetException;
/*    */ import java.lang.reflect.Method;
/*    */ import java.nio.ByteBuffer;
/*    */ 
/*    */ public class ByteBufferDeallocator
/*    */ {
/*    */   private boolean usingInvokeCleanerMethod;
/* 11 */   private final String directBufferClassName = "java.nio.DirectByteBuffer";
/*    */   
/*    */   private Object theUnsafe;
/*    */   private Method invokeCleanerMethod;
/*    */   private Method directBufferCleanerMethod;
/*    */   private Method cleanerCleanMethod;
/*    */   
/*    */   public ByteBufferDeallocator() throws ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalArgumentException, IllegalAccessException {
/*    */     try {
/* 20 */       Class<?> unsafeClass = Class.forName("sun.misc.Unsafe");
/* 21 */       Field theUnsafeField = unsafeClass.getDeclaredField("theUnsafe");
/* 22 */       theUnsafeField.setAccessible(true);
/* 23 */       this.theUnsafe = theUnsafeField.get(null);
/* 24 */       theUnsafeField.setAccessible(false);
/* 25 */       this.invokeCleanerMethod = unsafeClass.getDeclaredMethod("invokeCleaner", new Class[] { ByteBuffer.class });
/* 26 */       this.usingInvokeCleanerMethod = true;
/* 27 */     } catch (NoSuchMethodException|NoSuchFieldException nse) {
/* 28 */       Class<?> directByteBufferClass = Class.forName("java.nio.DirectByteBuffer");
/* 29 */       this.directBufferCleanerMethod = directByteBufferClass.getDeclaredMethod("cleaner", new Class[0]);
/* 30 */       Class<?> cleanerClass = this.directBufferCleanerMethod.getReturnType();
/* 31 */       if (Runnable.class.isAssignableFrom(cleanerClass)) {
/* 32 */         this.cleanerCleanMethod = Runnable.class.getDeclaredMethod("run", new Class[0]);
/*    */       } else {
/* 34 */         this.cleanerCleanMethod = cleanerClass.getDeclaredMethod("clean", new Class[0]);
/*    */       } 
/*    */     } 
/*    */   }
/*    */   public synchronized void deallocate(ByteBuffer buffer, boolean debug) {
/* 39 */     if (buffer == null || !buffer.isDirect())
/*    */       return; 
/* 41 */     if (this.usingInvokeCleanerMethod) {
/*    */       try {
/* 43 */         this.invokeCleanerMethod.invoke(this.theUnsafe, new Object[] { buffer });
/*    */       }
/* 45 */       catch (IllegalAccessException e) {
/* 46 */         reportException(e);
/* 47 */       } catch (IllegalArgumentException e) {
/* 48 */         reportException(e);
/* 49 */       } catch (InvocationTargetException e) {
/* 50 */         reportException(e);
/*    */       } 
/*    */     } else {
/* 53 */       boolean cleanerAccessibleBU = this.directBufferCleanerMethod.isAccessible();
/* 54 */       boolean cleanAccessibleBU = this.cleanerCleanMethod.isAccessible();
/*    */       try {
/* 56 */         this.directBufferCleanerMethod.setAccessible(true);
/* 57 */         Object cleaner = this.directBufferCleanerMethod.invoke(buffer, new Object[0]);
/* 58 */         if (cleaner != null)
/* 59 */         { this.cleanerCleanMethod.setAccessible(true);
/* 60 */           this.cleanerCleanMethod.invoke(cleaner, new Object[0]); }
/* 61 */         else if (debug)
/* 62 */         { System.out.println("No cleaner to deallocate a buffer!"); } 
/* 63 */       } catch (IllegalAccessException e) {
/* 64 */         reportException(e);
/* 65 */       } catch (IllegalArgumentException e) {
/* 66 */         reportException(e);
/* 67 */       } catch (InvocationTargetException e) {
/* 68 */         reportException(e);
/*    */       } 
/* 70 */       this.directBufferCleanerMethod.setAccessible(cleanerAccessibleBU);
/* 71 */       this.cleanerCleanMethod.setAccessible(cleanAccessibleBU);
/*    */     } 
/*    */   }
/*    */   
/*    */   private void reportException(Exception e) {
/* 76 */     System.out.println("Failed to deallocate a direct byte buffer: ");
/* 77 */     e.printStackTrace();
/*    */   }
/*    */ }


/* Location:              C:\Users\ioanm\Downloads\XaerosWorldMap_1.7.3_Forge_1.12.jar!\xaero\deallocator\ByteBufferDeallocator.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */