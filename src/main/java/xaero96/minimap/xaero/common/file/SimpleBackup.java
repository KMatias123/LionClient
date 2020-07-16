/*    */ package xaero.common.file;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.nio.file.CopyOption;
/*    */ import java.nio.file.FileVisitOption;
/*    */ import java.nio.file.FileVisitResult;
/*    */ import java.nio.file.Files;
/*    */ import java.nio.file.Path;
/*    */ import java.nio.file.SimpleFileVisitor;
/*    */ import java.nio.file.StandardCopyOption;
/*    */ import java.nio.file.attribute.BasicFileAttributes;
/*    */ import java.nio.file.attribute.FileAttribute;
/*    */ import java.util.EnumSet;
/*    */ 
/*    */ public class SimpleBackup {
/*    */   public static Path moveToBackup(Path directory) {
/* 17 */     Path backupFolder = directory.getParent().resolve("backup");
/* 18 */     while (Files.exists(backupFolder, new java.nio.file.LinkOption[0]))
/* 19 */       backupFolder = backupFolder.getParent().resolve(backupFolder.getFileName().toString() + "-"); 
/* 20 */     Path backupPath = backupFolder.resolve(directory.getFileName());
/*    */     try {
/* 22 */       Files.createDirectories(backupFolder, (FileAttribute<?>[])new FileAttribute[0]);
/* 23 */       Files.move(directory, backupPath, new CopyOption[] { StandardCopyOption.REPLACE_EXISTING });
/* 24 */     } catch (IOException e) {
/* 25 */       throw new RuntimeException("Failed to backup a directory! Can't continue.", e);
/*    */     } 
/* 27 */     return backupPath;
/*    */   }
/*    */   
/*    */   public static void copyDirectoryWithContents(final Path from, final Path to, int maxDepth, CopyOption... copyOptions) throws IOException {
/* 31 */     Files.walkFileTree(from, EnumSet.of(FileVisitOption.FOLLOW_LINKS), maxDepth, new SimpleFileVisitor<Path>()
/*    */         {
/*    */           public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
/* 34 */             Path targetPath = to.resolve(from.relativize(dir));
/* 35 */             if (!Files.exists(targetPath, new java.nio.file.LinkOption[0]))
/* 36 */               Files.createDirectory(targetPath, (FileAttribute<?>[])new FileAttribute[0]); 
/* 37 */             return FileVisitResult.CONTINUE;
/*    */           }
/*    */ 
/*    */           
/*    */           public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
/* 42 */             Files.copy(file, to.resolve(from.relativize(file)), copyOptions);
/* 43 */             return FileVisitResult.CONTINUE;
/*    */           }
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\ioanm\Downloads\Xaeros_Minimap_20.15.3_Forge_1.12.jar!\xaero\common\file\SimpleBackup.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */