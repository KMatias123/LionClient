/*     */ package xaero.common.interfaces.render;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.util.ConcurrentModificationException;
/*     */ import java.util.Iterator;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.Gui;
/*     */ import net.minecraft.client.gui.ScaledResolution;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import org.lwjgl.input.Mouse;
/*     */ import xaero.common.IXaeroMinimap;
/*     */ import xaero.common.interfaces.Interface;
/*     */ import xaero.common.interfaces.InterfaceManager;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class InterfaceRenderer
/*     */ {
/*  21 */   public static final ResourceLocation guiTextures = new ResourceLocation("xaerobetterpvp", "gui/guis.png");
/*     */   private IXaeroMinimap modMain;
/*  23 */   private final Color disabled = new Color(189, 189, 189, 80);
/*  24 */   private final Color enabled = new Color(255, 255, 255, 100);
/*  25 */   private final Color selected = new Color(255, 255, 255, 130);
/*     */   private ScaledResolution scaledresolution;
/*     */   
/*     */   public InterfaceRenderer(IXaeroMinimap modMain) {
/*  29 */     this.modMain = modMain;
/*     */   }
/*     */   
/*     */   public void renderInterfaces(float partial) {
/*  33 */     this.scaledresolution = new ScaledResolution(Minecraft.func_71410_x());
/*  34 */     int width = this.scaledresolution.func_78326_a();
/*  35 */     int height = this.scaledresolution.func_78328_b();
/*  36 */     int scale = this.scaledresolution.func_78325_e();
/*  37 */     int mouseX = Mouse.getX() * width / (Minecraft.func_71410_x()).field_71443_c;
/*  38 */     int mouseY = height - Mouse.getY() * height / (Minecraft.func_71410_x()).field_71440_d - 1;
/*  39 */     this.modMain.getInterfaces().updateInterfaces(mouseX, mouseY, width, height, scale);
/*  40 */     Iterator<Interface> iter = this.modMain.getInterfaces().getInterfaceIterator();
/*  41 */     while (iter.hasNext()) {
/*  42 */       Interface l = iter.next();
/*  43 */       if (this.modMain.getSettings().getBooleanValue(l.getOption())) {
/*     */         try {
/*  45 */           l.drawInterface(width, height, scale, partial);
/*  46 */         } catch (ConcurrentModificationException concurrentModificationException) {}
/*     */       }
/*     */     } 
/*     */     
/*  50 */     GlStateManager.func_179126_j();
/*     */   }
/*     */ 
/*     */   
/*     */   public void renderBoxes(int mouseX, int mouseY, int width, int height, int scale) {
/*  55 */     if (this.modMain.getEvents().getLastGuiOpen() instanceof xaero.common.gui.GuiEditMode) {
/*  56 */       int mouseOverId = this.modMain.getInterfaces().getInterfaceId(mouseX, mouseY, width, height, scale);
/*  57 */       InterfaceManager interfaces = this.modMain.getInterfaces();
/*  58 */       Iterator<Interface> iter = interfaces.getInterfaceIterator();
/*  59 */       int i = -1;
/*  60 */       while (iter.hasNext()) {
/*  61 */         i++;
/*  62 */         Interface l = iter.next();
/*  63 */         if (!this.modMain.getSettings().getBooleanValue(l.getOption()))
/*     */           continue; 
/*  65 */         int x = l.getX();
/*  66 */         if (l.isFromRight())
/*  67 */           x = width - x; 
/*  68 */         int y = l.getY();
/*  69 */         if (l.isFromBottom())
/*  70 */           y = height - y; 
/*  71 */         int w = l.getW(scale);
/*  72 */         int h = l.getH(scale);
/*  73 */         int x2 = x + w;
/*  74 */         int y2 = y + h;
/*  75 */         if (interfaces.getSelectedId() == i || (
/*  76 */           !interfaces.overAButton(mouseX, mouseY) && mouseX >= x && mouseX <= x2 && mouseY >= y && mouseY <= y2) || i == interfaces
/*  77 */           .getDraggingId()) {
/*  78 */           Gui.func_73734_a(x, y, x2, y2, (interfaces.getSelectedId() == i) ? this.selected.hashCode() : this.enabled.hashCode());
/*  79 */           if (interfaces.getDraggingId() == -1 && i == mouseOverId) {
/*  80 */             l.getcBox().drawBox(mouseX, mouseY, width, height);
/*     */           }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           continue;
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 100 */         Gui.func_73734_a(x, y, x2, y2, this.disabled.hashCode());
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\ioanm\Downloads\Xaeros_Minimap_20.15.3_Forge_1.12.jar!\xaero\common\interfaces\render\InterfaceRenderer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */