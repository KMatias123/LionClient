/*    */ package xaero.common.graphics;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.Gui;
/*    */ import net.minecraft.client.resources.I18n;
/*    */ 
/*    */ 
/*    */ public class CursorBox
/*    */ {
/*    */   private ArrayList<String> strings;
/*    */   private String language;
/* 13 */   private String fullCode = "";
/*    */   private String formatting;
/* 15 */   private int boxWidth = 150;
/*    */   private static final int color = -939524096;
/*    */   private boolean customLines;
/*    */   
/*    */   public CursorBox(String code) {
/* 20 */     this(code, "");
/*    */   }
/*    */   
/*    */   public CursorBox(String code, String formatting) {
/* 24 */     this.fullCode = code;
/* 25 */     this.formatting = formatting;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public CursorBox(int size) {
/* 31 */     this.strings = new ArrayList<>();
/* 32 */     for (int i = 0; i < size; i++)
/* 33 */       this.strings.add(""); 
/* 34 */     this.customLines = true;
/*    */   }
/*    */   
/*    */   private String currentLanguage() {
/* 38 */     return Minecraft.func_71410_x().func_135016_M().func_135041_c().func_135034_a();
/*    */   }
/*    */   
/*    */   public void createLines(String text) {
/*    */     try {
/* 43 */       this.language = currentLanguage();
/* 44 */     } catch (NullPointerException e) {
/* 45 */       this.language = "en_us";
/*    */     } 
/* 47 */     this.strings = new ArrayList<>();
/* 48 */     String[] words = text.split(" ");
/* 49 */     int spaceWidth = (Minecraft.func_71410_x()).field_71466_p.func_78256_a(" ");
/* 50 */     StringBuilder line = new StringBuilder();
/* 51 */     int width = 0;
/* 52 */     for (int i = 0; i < words.length; i++) {
/* 53 */       int wordWidth = (Minecraft.func_71410_x()).field_71466_p.func_78256_a(words[i]);
/* 54 */       if (width == 0 && wordWidth > this.boxWidth - 15)
/* 55 */         this.boxWidth = wordWidth + 15; 
/* 56 */       if (width + wordWidth <= this.boxWidth - 15) {
/* 57 */         width += spaceWidth + wordWidth;
/* 58 */         line.append(words[i]).append(" ");
/*    */       } else {
/* 60 */         this.strings.add(this.formatting + line.toString());
/* 61 */         line.delete(0, line.length());
/* 62 */         width = 0;
/* 63 */         i--;
/*    */       } 
/* 65 */       if (i == words.length - 1)
/* 66 */         this.strings.add(this.formatting + line.toString()); 
/*    */     } 
/*    */   }
/*    */   
/*    */   public String getString(int line) {
/* 71 */     return this.strings.get(line);
/*    */   }
/*    */   
/*    */   public void drawBox(int x, int y, int width, int height) {
/*    */     try {
/* 76 */       if (!this.customLines && (this.language == null || !this.language.equals(currentLanguage())))
/* 77 */         createLines(I18n.func_135052_a(this.fullCode, new Object[0])); 
/* 78 */     } catch (Exception exception) {}
/*    */     
/* 80 */     int fix = (x + this.boxWidth > width) ? (x + this.boxWidth - width) : 0;
/* 81 */     int h = 5 + this.strings.size() * 10 + 5;
/* 82 */     int fiy = (y + h > height) ? (y + h - height) : 0;
/* 83 */     Gui.func_73734_a(x - fix, y - fiy, x + this.boxWidth - fix, y + h - fiy, -939524096);
/* 84 */     for (int i = 0; i < this.strings.size(); i++) {
/* 85 */       String s = getString(i);
/* 86 */       (Minecraft.func_71410_x()).field_71466_p.func_78276_b("§f" + s, x + 10 - fix, y + 5 + 10 * i - fiy, 16777215);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\ioanm\Downloads\Xaeros_Minimap_20.15.3_Forge_1.12.jar!\xaero\common\graphics\CursorBox.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */