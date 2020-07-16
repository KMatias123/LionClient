/*    */ package xaero.map.gui;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.GuiButton;
/*    */ import org.lwjgl.opengl.GL11;
/*    */ import xaero.map.WorldMap;
/*    */ 
/*    */ public class GuiMapSettingsButton
/*    */   extends GuiButton
/*    */ {
/*    */   private boolean menuActive;
/*    */   
/*    */   public GuiMapSettingsButton(boolean menuActive, int p_i51141_3_, int p_i51141_4_) {
/* 14 */     super(-1, p_i51141_3_, p_i51141_4_, 20, 20, "");
/* 15 */     this.menuActive = menuActive;
/*    */   }
/*    */ 
/*    */   
/*    */   public void func_191745_a(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
/* 20 */     Minecraft.func_71410_x().func_110434_K().func_110577_a(WorldMap.guiTextures);
/* 21 */     int iconX = this.field_146128_h + this.field_146120_f / 2 - 8;
/* 22 */     int iconY = this.field_146129_i + this.field_146121_g / 2 - 8;
/* 23 */     if (this.field_146124_l)
/* 24 */     { if (mouseX >= this.field_146128_h && mouseY >= this.field_146129_i && mouseX < this.field_146128_h + this.field_146120_f && mouseY < this.field_146129_i + this.field_146121_g) {
/* 25 */         iconY--;
/* 26 */         GL11.glColor4f(0.9F, 0.9F, 0.9F, 1.0F);
/*    */       } else {
/* 28 */         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
/*    */       }  }
/* 30 */     else { GL11.glColor4f(0.25F, 0.25F, 0.25F, 1.0F); }
/* 31 */      if (this.menuActive) {
/* 32 */       func_73729_b(iconX, iconY, 97, 0, 16, 16);
/*    */     } else {
/* 34 */       func_73729_b(iconX, iconY, 81, 0, 16, 16);
/* 35 */     }  GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
/*    */   }
/*    */ }


/* Location:              C:\Users\ioanm\Downloads\XaerosWorldMap_1.7.3_Forge_1.12.jar!\xaero\map\gui\GuiMapSettingsButton.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */