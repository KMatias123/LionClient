package xaero.common.gui.widget;

import net.minecraft.client.gui.GuiButton;

public interface WidgetScreen {
  @Deprecated
  <S extends net.minecraft.client.gui.GuiScreen & WidgetScreen> S getScreen();
  
  void addButtonVisible(GuiButton paramGuiButton);
}


/* Location:              C:\Users\ioanm\Downloads\Xaeros_Minimap_20.15.3_Forge_1.12.jar!\xaero\common\gui\widget\WidgetScreen.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */