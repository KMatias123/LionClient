/*     */ package xaero.patreon;
/*     */ 
/*     */ import java.awt.Desktop;
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.HttpURLConnection;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.net.URL;
/*     */ import java.nio.file.Path;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.GuiButton;
/*     */ import net.minecraft.client.gui.GuiYesNo;
/*     */ import net.minecraft.client.resources.I18n;
/*     */ 
/*     */ public class GuiUpdateAll
/*     */   extends GuiYesNo
/*     */ {
/*     */   public GuiUpdateAll() {
/*  25 */     super(null, "These mods are out-of-date: " + modListToNames(Patreon4.getOutdatedMods()), "Would you like to automatically update them?", 0);
/*     */   }
/*     */ 
/*     */   
/*     */   private static String modListToNames(List<PatreonMod2> list) {
/*  30 */     StringBuilder builder = new StringBuilder();
/*  31 */     for (int i = 0; i < list.size(); i++) {
/*  32 */       if (i != 0)
/*  33 */         builder.append(", "); 
/*  34 */       builder.append(((PatreonMod2)list.get(i)).modName);
/*     */     } 
/*  36 */     return builder.toString();
/*     */   }
/*     */   
/*     */   public void func_73866_w_() {
/*  40 */     super.func_73866_w_();
/*  41 */     this.field_146292_n.add(new GuiButton(201, this.field_146294_l / 2 - 100, this.field_146295_m / 6 + 120, I18n.func_135052_a("Changelogs", new Object[0])));
/*     */   }
/*     */   protected void func_146284_a(GuiButton button) throws IOException {
/*     */     int i;
/*  45 */     switch (button.field_146127_k) {
/*     */       case 0:
/*  47 */         if (Patreon4.patronPledge >= 5) {
/*  48 */           for (GuiButton b : this.field_146292_n)
/*  49 */             b.field_146124_l = false; 
/*  50 */           autoUpdate();
/*     */         } 
/*  52 */         Minecraft.func_71410_x().func_71400_g();
/*     */         break;
/*     */       case 1:
/*  55 */         this.field_146297_k.func_147108_a(null);
/*     */         break;
/*     */       case 201:
/*  58 */         for (i = 0; i < Patreon4.getOutdatedMods().size(); i++) {
/*  59 */           PatreonMod2 mod = Patreon4.getOutdatedMods().get(i);
/*     */           try {
/*  61 */             Desktop d = Desktop.getDesktop();
/*  62 */             d.browse(new URI(mod.changelogLink));
/*  63 */           } catch (URISyntaxException e) {
/*  64 */             e.printStackTrace();
/*     */           } 
/*     */         } 
/*     */         break;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void download(BufferedOutputStream output, InputStream input, boolean closeInput) throws IOException {
/*  72 */     byte[] buffer = new byte[256];
/*     */     while (true) {
/*  74 */       int read = input.read(buffer, 0, buffer.length);
/*  75 */       if (read < 0)
/*     */         break; 
/*  77 */       output.write(buffer, 0, read);
/*     */     } 
/*  79 */     output.flush();
/*  80 */     if (closeInput)
/*  81 */       input.close(); 
/*  82 */     output.close();
/*     */   }
/*     */   
/*     */   public void autoUpdate() {
/*     */     try {
/*  87 */       URL url = new URL("http://data.chocolateminecraft.com/jars/xaero_autoupdater_3.0.jar");
/*     */       
/*  89 */       HttpURLConnection conn = (HttpURLConnection)url.openConnection();
/*  90 */       conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/535.11 (KHTML, like Gecko) Chrome/17.0.963.56 Safari/535.11");
/*     */       
/*  92 */       InputStream input = conn.getInputStream();
/*  93 */       BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(new File("./xaero_autoupdater.jar")));
/*  94 */       download(output, input, true);
/*  95 */       ArrayList<String> command = new ArrayList<>();
/*     */       
/*  97 */       Path javaPath = (new File(System.getProperty("java.home"))).toPath().resolve("bin").resolve("java");
/*  98 */       command.add(javaPath.toString());
/*     */       
/* 100 */       command.add("-jar");
/* 101 */       command.add("./xaero_autoupdater.jar");
/* 102 */       command.add("5");
/* 103 */       command.add(Patreon4.updateLocation);
/* 104 */       for (int i = 0; i < Patreon4.getOutdatedMods().size(); i++) {
/* 105 */         PatreonMod2 m = Patreon4.getOutdatedMods().get(i);
/* 106 */         if (m.modJar != null) {
/*     */           
/* 108 */           command.add(m.modJar.getPath());
/* 109 */           command.add(m.latestVersionLayout);
/* 110 */           command.add(m.currentVersion.split("_")[1]);
/* 111 */           command.add(m.latestVersion);
/* 112 */           command.add(m.currentVersion.split("_")[0]);
/*     */         } 
/* 114 */       }  System.out.println(String.join(", ", (Iterable)command));
/* 115 */       Runtime.getRuntime().exec(command.<String>toArray(new String[0]));
/* 116 */     } catch (IOException e) {
/* 117 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\ioanm\Downloads\Xaeros_Minimap_20.15.3_Forge_1.12.jar!\xaero\patreon\GuiUpdateAll.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */