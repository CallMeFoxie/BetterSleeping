package foxie.bettersleeping.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

public class GuiInvisibleButton extends GuiButton {

   public GuiInvisibleButton(int id, int left, int right, int width, int height) {
      super(id, left, right, width, height, "");
   }

   @Override
   public void drawButton(Minecraft p_146112_1_, int p_146112_2_, int p_146112_3_) {

   }
}
