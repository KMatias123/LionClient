/*    */ package xaero.patreon;
/*    */ 
/*    */ import java.io.File;
/*    */ 
/*    */ public class PatreonMod2
/*    */ {
/*    */   public PatreonMod2(String fileLayoutID, String latestVersionLayout, String changelogLink, String modName) {
/*  8 */     this.fileLayoutID = fileLayoutID;
/*  9 */     this.latestVersionLayout = latestVersionLayout;
/* 10 */     this.changelogLink = changelogLink;
/* 11 */     this.modName = modName;
/*    */   }
/*    */   
/*    */   public String fileLayoutID;
/*    */   public String latestVersionLayout;
/*    */   public String changelogLink;
/*    */   public String modName;
/*    */   public File modJar;
/*    */   public String currentVersion;
/*    */   public String latestVersion;
/*    */ }


/* Location:              C:\Users\ioanm\Downloads\XaerosWorldMap_1.7.3_Forge_1.12.jar!\xaero\patreon\PatreonMod2.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */