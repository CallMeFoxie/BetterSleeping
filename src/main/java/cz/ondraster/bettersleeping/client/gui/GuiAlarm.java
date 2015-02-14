package cz.ondraster.bettersleeping.client.gui;

import cz.ondraster.bettersleeping.BetterSleeping;
import cz.ondraster.bettersleeping.container.ContainerAlarm;
import cz.ondraster.bettersleeping.logic.MinecraftTime;
import cz.ondraster.bettersleeping.network.MessageGuiAlarmButton;
import cz.ondraster.bettersleeping.network.Network;
import cz.ondraster.bettersleeping.tileentity.TileEntityAlarm;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiAlarm extends GuiContainer {

   public static final int BTN_HRUP = 1;
   public static final int BTN_HRDOWN = 2;
   public static final int BTN_MNTUP = 3;
   public static final int BTN_MNTDOWN = 4;
   public static final int BTN_TENHRUP = 5;
   public static final int BTN_TENHRDOWN = 6;
   public static final int BTN_TENMNTUP = 7;
   public static final int BTN_TENMNTDOWN = 8;

   private static final int NUMBER_WIDTH = 9;
   private static final int NUMBER_HEIGHT = 18;

   private static final int NUMBER_OFFSET_X = 13;
   private static final int NUMBER_OFFSET_Y = 103;

   TileEntityAlarm tileEntity;

   public GuiAlarm(TileEntityAlarm alarm) {
      super(new ContainerAlarm());
      this.tileEntity = alarm;

   }

   @SuppressWarnings("unchecked")
   @Override
   public void initGui() {
      xSize = 176;
      ySize = 100;
      super.initGui();
      buttonList.clear();
      GuiButton tenHrUp = new GuiInvisibleButton(BTN_TENHRUP, guiLeft + 123, guiTop + 53, 9, 8);
      GuiButton hrUp = new GuiInvisibleButton(BTN_HRUP, guiLeft + 133, guiTop + 53, 9, 8);
      GuiButton tenMntUp = new GuiInvisibleButton(BTN_TENMNTUP, guiLeft + 143, guiTop + 53, 9, 8);
      GuiButton mntUp = new GuiInvisibleButton(BTN_MNTUP, guiLeft + 153, guiTop + 53, 9, 8);

      GuiButton tenHrDown = new GuiInvisibleButton(BTN_TENHRDOWN, guiLeft + 123, guiTop + 80, 9, 8);
      GuiButton hrDown = new GuiInvisibleButton(BTN_HRDOWN, guiLeft + 133, guiTop + 80, 9, 8);
      GuiButton tenMntDown = new GuiInvisibleButton(BTN_TENMNTDOWN, guiLeft + 143, guiTop + 80, 9, 8);
      GuiButton mntDown = new GuiInvisibleButton(BTN_MNTDOWN, guiLeft + 153, guiTop + 80, 9, 8);

      buttonList.add(tenHrUp);
      buttonList.add(hrUp);
      buttonList.add(tenMntUp);
      buttonList.add(mntUp);

      buttonList.add(tenHrDown);
      buttonList.add(hrDown);
      buttonList.add(tenMntDown);
      buttonList.add(mntDown);
   }

   @Override
   protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      this.mc.getTextureManager().bindTexture(new ResourceLocation(BetterSleeping.MODID, "textures/gui/alarm.png"));
      int k = (this.width - this.xSize) / 2;
      int l = (this.height - this.ySize) / 2;
      this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
   }

   @Override
   public void drawScreen(int mouseX, int mouseY, float z) {
      super.drawScreen(mouseX, mouseY, z);
      GL11.glDisable(GL11.GL_LIGHTING);
      GL11.glDisable(GL11.GL_BLEND);
      MinecraftTime currentTime = MinecraftTime.getFromWorldTime(tileEntity.getWorldObj().getWorldTime());
      MinecraftTime alarmTime = MinecraftTime.getFromTime(tileEntity.getHour(), tileEntity.getMinute());
      this.mc.getTextureManager().bindTexture(new ResourceLocation(BetterSleeping.MODID, "textures/gui/alarm.png"));
      drawNumber(currentTime.getRealHour() / 10, 123, 21);
      drawNumber(currentTime.getRealHour() % 10, 133, 21);
      drawNumber(currentTime.getRealMinute() / 10, 143, 21);
      drawNumber(currentTime.getRealMinute() % 10, 153, 21);

      drawNumber(alarmTime.getRealHour() / 10, 123, 62);
      drawNumber(alarmTime.getRealHour() % 10, 133, 62);
      drawNumber(alarmTime.getRealMinute() / 10, 143, 62);
      drawNumber(alarmTime.getRealMinute() % 10, 153, 62);
   }

   @Override
   protected void actionPerformed(GuiButton btn) {
      Network.networkChannel.sendToServer(new MessageGuiAlarmButton(tileEntity, (char) (btn.id & 0xFF)));
   }

   private void drawNumber(int number, int x, int y) {
      drawTexturedModalRect(x + guiLeft, y + guiTop, NUMBER_OFFSET_X + (number * (NUMBER_WIDTH + 1)), NUMBER_OFFSET_Y, NUMBER_WIDTH,
            NUMBER_HEIGHT);
   }
}
