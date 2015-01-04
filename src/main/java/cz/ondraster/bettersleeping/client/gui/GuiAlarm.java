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

   TileEntityAlarm tileEntity;

   public GuiAlarm(TileEntityAlarm alarm) {
      super(new ContainerAlarm());
      this.tileEntity = alarm;

   }

   @SuppressWarnings("unchecked")
   @Override
   public void initGui() {
      super.initGui();
      buttonList.clear();
      GuiButton hrUp = new GuiButton(BTN_HRUP, guiLeft + 65, guiTop + 10, 20, 20, "^");
      GuiButton hrDown = new GuiButton(BTN_HRDOWN, guiLeft + 65, guiTop + 40, 20, 20, "v");
      GuiButton mntUp = new GuiButton(BTN_MNTUP, guiLeft + 85, guiTop + 10, 20, 20, "^");
      GuiButton mntDown = new GuiButton(BTN_MNTDOWN, guiLeft + 85, guiTop + 40, 20, 20, "v");
      buttonList.add(hrUp);
      buttonList.add(mntUp);
      buttonList.add(hrDown);
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
   public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
      super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
      GL11.glDisable(GL11.GL_LIGHTING);
      GL11.glDisable(GL11.GL_BLEND);
      String time = MinecraftTime.getFromTime(tileEntity.getHour(), tileEntity.getMinute()).toString();
      this.drawCenteredString(fontRendererObj, time, guiLeft + 75, guiTop + 30, 0xFFFFFF);
   }

   @Override
   protected void actionPerformed(GuiButton btn) {
      Network.networkChannel.sendToServer(new MessageGuiAlarmButton(tileEntity, (char) (btn.id & 0xFF)));
   }
}
