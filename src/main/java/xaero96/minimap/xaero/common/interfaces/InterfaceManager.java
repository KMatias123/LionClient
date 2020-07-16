/*     */ package xaero.common.interfaces;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.GuiButton;
/*     */ import org.lwjgl.input.Keyboard;
/*     */ import org.lwjgl.input.Mouse;
/*     */ import xaero.common.IXaeroMinimap;
/*     */ import xaero.common.gui.GuiEditMode;
/*     */ import xaero.common.minimap.MinimapInterface;
/*     */ import xaero.common.minimap.MinimapProcessor;
/*     */ 
/*     */ 
/*     */ public class InterfaceManager
/*     */ {
/*     */   private IXaeroMinimap modMain;
/*     */   private Minecraft mc;
/*     */   private ArrayList<Preset> presets;
/*     */   private ArrayList<Interface> list;
/*     */   private int actionTimer;
/*     */   private int selectedId;
/*     */   private int draggingId;
/*     */   private int draggingOffX;
/*     */   private int draggingOffY;
/*     */   private long lastFlip;
/*     */   
/*     */   public InterfaceManager(IXaeroMinimap modMain, IInterfaceLoader loader) {
/*  29 */     this.modMain = modMain;
/*  30 */     this.presets = new ArrayList<>();
/*  31 */     this.list = new ArrayList<>();
/*  32 */     this.mc = Minecraft.func_71410_x();
/*  33 */     this.selectedId = -1;
/*  34 */     this.draggingId = -1;
/*  35 */     loader.loadPresets(this);
/*  36 */     loader.load(modMain, this);
/*     */   }
/*     */   
/*     */   public MinimapInterface getMinimapInterface() {
/*  40 */     return (MinimapInterface)this.list.get(4);
/*     */   }
/*     */   
/*     */   public MinimapProcessor getMinimap() {
/*  44 */     return getMinimapInterface().getMinimap();
/*     */   }
/*     */   
/*     */   public boolean overAButton(int mouseX, int mouseY) {
/*  48 */     if (this.mc.field_71462_r instanceof GuiEditMode)
/*  49 */       for (int k = 0; k < ((GuiEditMode)this.mc.field_71462_r).getButtons().size(); k++) {
/*  50 */         GuiButton b = ((GuiEditMode)this.mc.field_71462_r).getButtons().get(k);
/*  51 */         if (mouseX >= b.field_146128_h && mouseY >= b.field_146129_i && mouseX < b.field_146128_h + 150 && mouseY < b.field_146129_i + 20)
/*     */         {
/*  53 */           return true;
/*     */         }
/*     */       }  
/*  56 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void updateBlinkingOverridable() {}
/*     */ 
/*     */   
/*     */   public void updateInterfaces(int mouseX, int mouseY, int width, int height, int scale) {
/*  64 */     if (this.actionTimer <= 0) {
/*  65 */       updateBlinkingOverridable();
/*  66 */       if (this.modMain.getEvents().getLastGuiOpen() instanceof GuiEditMode) {
/*  67 */         if (Mouse.isButtonDown(1))
/*  68 */           this.selectedId = -1; 
/*  69 */         int i = getInterfaceId(mouseX, mouseY, width, height, scale);
/*  70 */         if (i == -1)
/*  71 */           i = this.selectedId; 
/*  72 */         if (i != -1) {
/*  73 */           if (Mouse.isButtonDown(0) && this.draggingId == -1) {
/*  74 */             this.draggingId = i;
/*  75 */             this.selectedId = i;
/*  76 */             if (((Interface)this.list.get(i)).isFromRight())
/*  77 */               ((Interface)this.list.get(i)).setX(width - ((Interface)this.list.get(i)).getX()); 
/*  78 */             if (((Interface)this.list.get(i)).isFromBottom())
/*  79 */               ((Interface)this.list.get(i)).setY(height - ((Interface)this.list.get(i)).getY()); 
/*  80 */             this.draggingOffX = ((Interface)this.list.get(i)).getX() - mouseX;
/*  81 */             this.draggingOffY = ((Interface)this.list.get(i)).getY() - mouseY;
/*  82 */             if (((Interface)this.list.get(i)).isFromRight())
/*  83 */               ((Interface)this.list.get(i)).setX(width - ((Interface)this.list.get(i)).getX()); 
/*  84 */             if (((Interface)this.list.get(i)).isFromBottom())
/*  85 */               ((Interface)this.list.get(i)).setY(height - ((Interface)this.list.get(i)).getY()); 
/*  86 */           } else if (!Mouse.isButtonDown(0) && this.draggingId != -1) {
/*  87 */             this.draggingId = -1;
/*  88 */             this.draggingOffX = 0;
/*  89 */             this.draggingOffY = 0;
/*     */           } 
/*  91 */           if (this.selectedId != -1)
/*  92 */             i = this.selectedId; 
/*  93 */           if (Keyboard.isKeyDown(33) && System.currentTimeMillis() - this.lastFlip > 300L) {
/*  94 */             this.lastFlip = System.currentTimeMillis();
/*  95 */             ((Interface)this.list.get(i)).setFlipped(!((Interface)this.list.get(i)).isFlipped());
/*     */           } 
/*  97 */           if (Keyboard.isKeyDown(46) && System.currentTimeMillis() - this.lastFlip > 300L) {
/*  98 */             this.lastFlip = System.currentTimeMillis();
/*  99 */             ((Interface)this.list.get(i)).setCentered(!((Interface)this.list.get(i)).isCentered());
/*     */           } 
/* 101 */           if (Keyboard.isKeyDown(31)) {
/* 102 */             this.selectedId = -1;
/* 103 */             this.draggingId = -1;
/* 104 */             this.modMain.getGuiHelper().openInterfaceSettings(i);
/*     */           } 
/*     */         } 
/* 107 */         if (this.draggingId != -1) {
/* 108 */           Interface dragged = this.list.get(this.draggingId);
/* 109 */           if (!dragged.isCentered()) {
/* 110 */             dragged.setActualx(mouseX + this.draggingOffX);
/* 111 */             if (dragged.isFromRight())
/* 112 */               dragged.setActualx(width - dragged.getActualx()); 
/*     */           } 
/* 114 */           int centerX = dragged.getActualx() + dragged.getW() / 2 * (dragged.isFromRight() ? -1 : 1);
/* 115 */           if (dragged.isFromRight() && (width & 0x1) == 0)
/* 116 */             centerX++; 
/* 117 */           if (centerX > width / 2) {
/* 118 */             dragged.setFromRight(!dragged.isFromRight());
/*     */             
/* 120 */             dragged.setActualx(width - dragged.getActualx());
/*     */           } 
/* 122 */           dragged.setActualy(mouseY + this.draggingOffY);
/* 123 */           if (dragged.isFromBottom())
/* 124 */             dragged.setActualy(height - dragged.getActualy()); 
/* 125 */           int centerY = dragged.getActualy() + dragged.getH() / 2 * (dragged.isFromBottom() ? -1 : 1);
/* 126 */           if (dragged.isFromBottom() && (height & 0x1) == 0)
/* 127 */             centerY++; 
/* 128 */           if (centerY > height / 2) {
/*     */             
/* 130 */             dragged.setFromBottom(!dragged.isFromBottom());
/* 131 */             dragged.setActualy(height - dragged.getActualy());
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } else {
/* 136 */       this.actionTimer--;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 141 */     for (Interface j : this.list) {
/* 142 */       j.setX(j.getActualx());
/* 143 */       j.setY(j.getActualy());
/* 144 */       if (j.isFromRight())
/* 145 */         j.setX(width - j.getX()); 
/* 146 */       if (j.isFromBottom())
/* 147 */         j.setY(height - j.getY()); 
/* 148 */       if (j.isCentered()) {
/* 149 */         if (j.isMulti()) {
/* 150 */           j.setW(j.getWC(scale));
/* 151 */           j.setH(j.getHC(scale));
/*     */         } 
/* 153 */         j.setX(width / 2 - j.getW(scale) / 2);
/* 154 */       } else if (j.isMulti()) {
/* 155 */         j.setW(j.getW0(scale));
/* 156 */         j.setH(j.getH0(scale));
/*     */       } 
/* 158 */       if (j.getX() < 5)
/* 159 */         j.setX(0); 
/* 160 */       if (j.getY() < 5)
/* 161 */         j.setY(0); 
/* 162 */       if (j.getX() + j.getW(scale) > width - 5)
/* 163 */         j.setX(width - j.getW(scale)); 
/* 164 */       if (j.getY() + j.getH(scale) > height - 5)
/* 165 */         j.setY(height - j.getH(scale)); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public int getInterfaceId(int mouseX, int mouseY, int width, int height, int scale) {
/* 170 */     int toReturn = -1;
/* 171 */     int size = 0;
/* 172 */     for (int i = 0; i < this.list.size(); i++) {
/* 173 */       Interface l = this.list.get(i);
/* 174 */       int x = l.getX();
/* 175 */       if (l.isFromRight())
/* 176 */         x = width - x; 
/* 177 */       int y = l.getY();
/* 178 */       if (l.isFromBottom())
/* 179 */         y = height - y; 
/* 180 */       int x2 = x + l.getW(scale);
/* 181 */       int y2 = y + l.getH(scale);
/* 182 */       int isize = l.getSize();
/* 183 */       if (!l.getIname().equals("dummy") && (size == 0 || isize < size) && !overAButton(mouseX, mouseY) && mouseX >= x && mouseX < x2 && mouseY >= y && mouseY < y2) {
/*     */         
/* 185 */         size = isize;
/* 186 */         toReturn = i;
/*     */       } 
/*     */     } 
/* 189 */     return toReturn;
/*     */   }
/*     */   
/*     */   public void add(Interface i) {
/* 193 */     this.list.add(i);
/*     */   }
/*     */   
/*     */   public Preset getDefaultPreset() {
/* 197 */     return this.presets.get(0);
/*     */   }
/*     */   
/*     */   public Preset getPreset(int id) {
/* 201 */     return this.presets.get(id);
/*     */   }
/*     */   
/*     */   public int getNextId() {
/* 205 */     return this.list.size();
/*     */   }
/*     */   
/*     */   public void addPreset(Preset preset) {
/* 209 */     this.presets.add(preset);
/*     */   }
/*     */   
/*     */   public int getSelectedId() {
/* 213 */     return this.selectedId;
/*     */   }
/*     */   
/*     */   public void setSelectedId(int selectedId) {
/* 217 */     this.selectedId = selectedId;
/*     */   }
/*     */   
/*     */   public int getDraggingId() {
/* 221 */     return this.draggingId;
/*     */   }
/*     */   
/*     */   public void setDraggingId(int draggingId) {
/* 225 */     this.draggingId = draggingId;
/*     */   }
/*     */   
/*     */   public Iterator<Interface> getInterfaceIterator() {
/* 229 */     return this.list.iterator();
/*     */   }
/*     */   
/*     */   public Iterator<Preset> getPresetsIterator() {
/* 233 */     return this.presets.iterator();
/*     */   }
/*     */   
/*     */   public int getActionTimer() {
/* 237 */     return this.actionTimer;
/*     */   }
/*     */   
/*     */   public void setActionTimer(int actionTimer) {
/* 241 */     this.actionTimer = actionTimer;
/*     */   }
/*     */ }


/* Location:              C:\Users\ioanm\Downloads\Xaeros_Minimap_20.15.3_Forge_1.12.jar!\xaero\common\interfaces\InterfaceManager.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */