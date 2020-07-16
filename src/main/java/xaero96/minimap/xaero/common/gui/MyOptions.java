/*    */ package xaero.common.gui;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.GuiButton;
/*    */ import net.minecraft.client.gui.GuiOptions;
/*    */ import net.minecraft.client.gui.GuiScreen;
/*    */ import net.minecraft.client.resources.I18n;
/*    */ import net.minecraft.client.settings.GameSettings;
/*    */ 
/*    */ 
/*    */ public class MyOptions
/*    */   extends GuiOptions
/*    */ {
/*    */   private GuiSettings settingsScreen;
/*    */   private String buttonName;
/* 17 */   Minecraft field_146297_k = Minecraft.func_71410_x();
/*    */   
/*    */   public MyOptions(String buttonName, GuiSettings settingsScreen, GuiScreen par1GuiScreen, GameSettings par2GameSettings) {
/* 20 */     super(par1GuiScreen, par2GameSettings);
/* 21 */     this.buttonName = buttonName;
/* 22 */     this.settingsScreen = settingsScreen;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void func_73866_w_() {
/* 30 */     super.func_73866_w_();
/*    */     
/* 32 */     this.field_146292_n.add(new GuiButton(300, this.field_146294_l / 2 - 100, this.field_146295_m / 6 + 10, I18n.func_135052_a(this.buttonName, new Object[0])));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void func_146284_a(GuiButton par1GuiButton) throws IOException {
/* 41 */     super.func_146284_a(par1GuiButton);
/* 42 */     if (par1GuiButton.field_146124_l)
/*    */     {
/* 44 */       if (par1GuiButton.field_146127_k == 300) {
/*    */         
/* 46 */         this.field_146297_k.field_71474_y.func_74303_b();
/* 47 */         this.field_146297_k.func_147108_a(this.settingsScreen);
/*    */       } 
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ioanm\Downloads\Xaeros_Minimap_20.15.3_Forge_1.12.jar!\xaero\common\gui\MyOptions.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */