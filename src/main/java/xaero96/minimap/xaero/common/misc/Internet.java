/*    */ package xaero.common.misc;
/*    */ 
/*    */ import java.io.BufferedReader;
/*    */ import java.io.InputStreamReader;
/*    */ import java.net.URL;
/*    */ import java.net.URLConnection;
/*    */ import xaero.common.IXaeroMinimap;
/*    */ import xaero.common.settings.ModSettings;
/*    */ import xaero.patreon.Patreon4;
/*    */ 
/*    */ 
/*    */ public class Internet
/*    */ {
/*    */   public static void checkModVersion(IXaeroMinimap modMain) {
/* 15 */     String s = modMain.getVersionsURL();
/* 16 */     s = s.replaceAll(" ", "%20");
/*    */     
/*    */     try {
/* 19 */       URL url = new URL(s);
/*    */       
/* 21 */       URLConnection conn = url.openConnection();
/* 22 */       conn.setConnectTimeout(900);
/* 23 */       BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF8"));
/* 24 */       String line = reader.readLine();
/* 25 */       if (line != null)
/* 26 */         modMain.setMessage("§e§l" + line); 
/* 27 */       line = reader.readLine();
/* 28 */       if (line != null) {
/* 29 */         modMain.setNewestUpdateID(Integer.parseInt(line));
/* 30 */         if (!ModSettings.updateNotification || modMain.getNewestUpdateID() == ModSettings.ignoreUpdate) {
/* 31 */           modMain.setOutdated(false);
/* 32 */           reader.close();
/*    */           return;
/*    */         } 
/*    */       } 
/* 36 */       String[] current = modMain.getVersionID().split("_");
/* 37 */       while ((line = reader.readLine()) != null) {
/* 38 */         if (line.startsWith("data_widget") && line.length() > 11) {
/* 39 */           modMain.getWidgetLoader().loadWidget(line.substring(12));
/*    */           continue;
/*    */         } 
/* 42 */         if (line.equals(modMain.getVersionID())) {
/* 43 */           modMain.setOutdated(false); break;
/*    */         } 
/* 45 */         if (Patreon4.patronPledge >= 5 && line.startsWith(current[0] + "_")) {
/* 46 */           String[] args = line.split("_");
/* 47 */           if (args.length == current.length) {
/* 48 */             boolean sameType = true;
/* 49 */             if (current.length > 2)
/* 50 */               for (int i = 2; i < current.length && sameType; i++) {
/* 51 */                 if (!args[i].equals(current[i]))
/* 52 */                   sameType = false; 
/* 53 */               }   if (sameType)
/* 54 */               modMain.setLatestVersion(args[1]); 
/*    */           } 
/*    */         } 
/*    */       } 
/* 58 */       reader.close();
/* 59 */     } catch (Exception e) {
/* 60 */       e.printStackTrace();
/* 61 */       modMain.setOutdated(false);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\ioanm\Downloads\Xaeros_Minimap_20.15.3_Forge_1.12.jar!\xaero\common\misc\Internet.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */