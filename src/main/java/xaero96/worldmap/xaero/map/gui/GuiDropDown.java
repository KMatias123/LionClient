/*     */ package xaero.map.gui;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.Gui;
/*     */ import net.minecraft.client.resources.I18n;
/*     */ 
/*     */ 
/*     */ public class GuiDropDown
/*     */   extends Gui
/*     */ {
/*  12 */   public static final Color background = new Color(0, 0, 0, 200);
/*  13 */   public static final Color trim = new Color(160, 160, 160, 255);
/*  14 */   public static final Color trimInside = new Color(50, 50, 50, 255);
/*     */   private static final int h = 11;
/*     */   private int x;
/*     */   private int y;
/*  18 */   private int xOffset = 0;
/*  19 */   private int yOffset = 0;
/*     */   private int w;
/*  21 */   private String[] realOptions = new String[0];
/*  22 */   private String[] options = new String[0];
/*  23 */   private int selected = 0;
/*     */   
/*     */   private boolean closed = true;
/*     */   private int scroll;
/*     */   private long scrollTime;
/*     */   private int autoScrolling;
/*     */   private IDropDownCallback callback;
/*     */   
/*     */   public GuiDropDown(String[] options, int x, int y, int w, Integer selected, IDropDownCallback callback) {
/*  32 */     this.x = x;
/*  33 */     this.y = y;
/*  34 */     this.w = w;
/*  35 */     this.realOptions = options;
/*  36 */     this.callback = callback;
/*  37 */     this.options = new String[this.realOptions.length + 1];
/*  38 */     System.arraycopy(this.realOptions, 0, this.options, 1, this.realOptions.length);
/*  39 */     selectId(selected.intValue(), false);
/*     */   }
/*     */   
/*     */   public int size() {
/*  43 */     return this.realOptions.length;
/*     */   }
/*     */   
/*     */   public int getXWithOffset() {
/*  47 */     return this.x + this.xOffset;
/*     */   }
/*     */   
/*     */   public int getYWithOffset() {
/*  51 */     return this.y + this.yOffset;
/*     */   }
/*     */   
/*     */   private void drawSlot(String text, int slotIndex, int pos, int mouseX, int mouseY, int scaledHeight) {
/*  55 */     if ((this.closed && onDropDown(mouseX, mouseY, scaledHeight)) || (!this.closed && onDropDownSlot(mouseX, mouseY, pos))) {
/*  56 */       func_73734_a(getXWithOffset(), getYWithOffset() + 11 * pos, getXWithOffset() + this.w, getYWithOffset() + 11 + 11 * pos, trimInside.hashCode());
/*     */     } else {
/*  58 */       func_73734_a(getXWithOffset(), getYWithOffset() + 11 * pos, getXWithOffset() + this.w, getYWithOffset() + 11 + 11 * pos, background.hashCode());
/*  59 */     }  func_73730_a(getXWithOffset() + 1, getXWithOffset() + this.w - 1, getYWithOffset() + 11 * pos, trimInside.hashCode());
/*  60 */     int textWidth = (Minecraft.func_71410_x()).field_71466_p.func_78256_a(text);
/*  61 */     boolean shortened = false;
/*  62 */     while (textWidth > this.w - 2) {
/*  63 */       text = text.substring(1);
/*  64 */       textWidth = (Minecraft.func_71410_x()).field_71466_p.func_78256_a("..." + text);
/*  65 */       shortened = true;
/*     */     } 
/*  67 */     if (shortened)
/*  68 */       text = "..." + text; 
/*  69 */     int textColor = (slotIndex - 1 == this.selected) ? 5592405 : 16777215;
/*  70 */     func_73732_a((Minecraft.func_71410_x()).field_71466_p, text, getXWithOffset() + this.w / 2, getYWithOffset() + 2 + 11 * pos, textColor);
/*     */   }
/*     */   
/*     */   private void drawMenu(int amount, int mouseX, int mouseY, int scaledHeight) {
/*  74 */     boolean scrolling = scrolling(scaledHeight);
/*  75 */     int totalH = 11 * (amount + (scrolling ? 2 : 0));
/*  76 */     int height = scaledHeight;
/*  77 */     if (this.y + totalH + 1 > height) {
/*  78 */       this.yOffset = height - this.y - totalH - 1;
/*     */     } else {
/*  80 */       this.yOffset = 0;
/*  81 */     }  int first = this.closed ? 0 : this.scroll;
/*  82 */     if (scrolling) {
/*  83 */       drawSlot(((this.scroll == 0) ? "§8" : "§7") + I18n.func_135052_a("gui.xaero_up", new Object[0]), -1, 0, mouseX, mouseY, scaledHeight);
/*  84 */       drawSlot(((this.scroll + optionLimit(scaledHeight) >= this.options.length) ? "§8" : "§7") + I18n.func_135052_a("gui.xaero_down", new Object[0]), -1, amount + 1, mouseX, mouseY, scaledHeight);
/*     */     } 
/*  86 */     for (int i = first; i < first + amount; i++)
/*  87 */       drawSlot(I18n.func_135052_a(this.options[i], new Object[0]).replace("§§", ":"), i, i - first + (scrolling ? 1 : 0), mouseX, mouseY, scaledHeight); 
/*  88 */     func_73728_b(getXWithOffset(), getYWithOffset(), getYWithOffset() + totalH, trim.hashCode());
/*  89 */     func_73728_b(getXWithOffset() + this.w, getYWithOffset(), getYWithOffset() + totalH, trim.hashCode());
/*  90 */     func_73730_a(getXWithOffset(), getXWithOffset() + this.w, getYWithOffset(), trim.hashCode());
/*  91 */     func_73730_a(getXWithOffset(), getXWithOffset() + this.w, getYWithOffset() + totalH, trim.hashCode());
/*     */   }
/*     */   
/*     */   private boolean scrolling(int scaledHeight) {
/*  95 */     return (this.options.length > optionLimit(scaledHeight) && !this.closed);
/*     */   }
/*     */ 
/*     */   
/*     */   public void mouseClicked(int mouseX, int mouseY, int mouseButton, int scaledHeight) {
/* 100 */     if (!this.closed) {
/* 101 */       int clickedId = getClickedId(mouseX, mouseY, scaledHeight);
/* 102 */       if (clickedId >= 0) {
/* 103 */         selectId(clickedId - 1, true);
/*     */       } else {
/* 105 */         this.autoScrolling = (clickedId == -1) ? 1 : -1;
/* 106 */         this.scrollTime = System.currentTimeMillis();
/* 107 */         mouseScrolled(this.autoScrolling, mouseX, mouseY, scaledHeight);
/*     */       } 
/* 109 */     } else if (this.options.length > 1) {
/* 110 */       this.closed = false;
/* 111 */       this.scroll = 0;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void mouseReleased(int mouseX, int mouseY, int mouseButton, int scaledHeight) {
/* 116 */     this.autoScrolling = 0;
/*     */   }
/*     */   
/*     */   private int getClickedId(int mouseX, int mouseY, int scaledHeight) {
/* 120 */     int yOnMenu = mouseY - getYWithOffset();
/* 121 */     int visibleSlotIndex = yOnMenu / 11;
/* 122 */     boolean upArrow = scrolling(scaledHeight);
/* 123 */     if (upArrow && visibleSlotIndex == 0)
/* 124 */       return -1; 
/* 125 */     if (visibleSlotIndex >= optionLimit(scaledHeight) + (upArrow ? 1 : 0))
/* 126 */       return -2; 
/* 127 */     int slot = this.scroll + visibleSlotIndex - (upArrow ? 1 : 0);
/* 128 */     if (slot >= this.options.length)
/* 129 */       slot = this.options.length - 1; 
/* 130 */     return slot;
/*     */   }
/*     */   
/*     */   public boolean onDropDown(int mouseX, int mouseY, int scaledHeight) {
/* 134 */     int xOnMenu = mouseX - getXWithOffset();
/* 135 */     int yOnMenu = mouseY - getYWithOffset();
/* 136 */     if (xOnMenu >= 0 && yOnMenu >= 0 && xOnMenu <= this.w) if (yOnMenu < (this.closed ? 11 : ((Math.min(this.options.length, optionLimit(scaledHeight)) + (scrolling(scaledHeight) ? 2 : 0)) * 11)))
/*     */       {
/* 138 */         return true; }  
/*     */     return false;
/*     */   }
/*     */   private boolean onDropDownSlot(int mouseX, int mouseY, int id) {
/* 142 */     int xOnMenu = mouseX - getXWithOffset();
/* 143 */     int yOnMenu = mouseY - getYWithOffset();
/* 144 */     if (xOnMenu < 0 || yOnMenu < id * 11 || xOnMenu > this.w || yOnMenu >= id * 11 + 11)
/* 145 */       return false; 
/* 146 */     return true;
/*     */   }
/*     */   
/*     */   public void selectId(int id, boolean callCallback) {
/* 150 */     if (id == -1) {
/* 151 */       this.closed = true;
/*     */       return;
/*     */     } 
/* 154 */     boolean newId = (id != this.selected);
/* 155 */     if (newId && (!callCallback || this.callback.onSelected(this, id)))
/* 156 */       this.selected = id; 
/* 157 */     this.closed = true;
/* 158 */     this.options[0] = this.realOptions[this.selected];
/*     */   }
/*     */   
/*     */   public void drawButton(int mouseX, int mouseY, int scaledHeight) {
/* 162 */     if (this.autoScrolling != 0 && System.currentTimeMillis() - this.scrollTime > 100L) {
/* 163 */       this.scrollTime = System.currentTimeMillis();
/* 164 */       mouseScrolled(this.autoScrolling, mouseX, mouseY, scaledHeight);
/*     */     } 
/* 166 */     drawMenu(this.closed ? 1 : Math.min(optionLimit(scaledHeight), this.options.length), mouseX, mouseY, scaledHeight);
/*     */   }
/*     */   
/*     */   public boolean isClosed() {
/* 170 */     return this.closed;
/*     */   }
/*     */   
/*     */   public void setClosed(boolean closed) {
/* 174 */     this.closed = closed;
/*     */   }
/*     */   
/*     */   public void mouseScrolled(int wheel, int mouseXScaled, int mouseYScaled, int scaledHeight) {
/* 178 */     int newScroll = this.scroll - wheel;
/* 179 */     int optionLimit = optionLimit(scaledHeight);
/* 180 */     if (newScroll + optionLimit > this.options.length)
/* 181 */       newScroll = this.options.length - optionLimit; 
/* 182 */     if (newScroll < 0)
/* 183 */       newScroll = 0; 
/* 184 */     this.scroll = newScroll;
/*     */   }
/*     */   
/*     */   private int optionLimit(int scaledHeight) {
/* 188 */     return Math.max(1, scaledHeight / 11 - 2);
/*     */   }
/*     */   
/*     */   public int getSelected() {
/* 192 */     return this.selected;
/*     */   }
/*     */ }


/* Location:              C:\Users\ioanm\Downloads\XaerosWorldMap_1.7.3_Forge_1.12.jar!\xaero\map\gui\GuiDropDown.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */