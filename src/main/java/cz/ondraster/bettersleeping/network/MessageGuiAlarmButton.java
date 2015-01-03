package cz.ondraster.bettersleeping.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cz.ondraster.bettersleeping.client.gui.GuiAlarm;
import cz.ondraster.bettersleeping.tileentity.TileEntityAlarm;
import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class MessageGuiAlarmButton implements IMessage, IMessageHandler<MessageGuiAlarmButton, IMessage> {
   int x, y, z;
   char button;

   public MessageGuiAlarmButton() {

   }

   public MessageGuiAlarmButton(TileEntityAlarm alarm, char button) {
      x = alarm.xCoord;
      y = alarm.yCoord;
      z = alarm.zCoord;
      this.button = button;
   }

   @Override
   public void fromBytes(ByteBuf buf) {
      x = buf.readInt();
      y = buf.readInt();
      z = buf.readInt();
      button = buf.readChar();
   }

   @Override
   public void toBytes(ByteBuf buf) {
      buf.writeInt(x);
      buf.writeInt(y);
      buf.writeInt(z);
      buf.writeChar(button);
   }

   @Override
   public IMessage onMessage(MessageGuiAlarmButton message, MessageContext ctx) {
      if (ctx.getServerHandler() == null)
         return null;

      World world = ctx.getServerHandler().playerEntity.worldObj;
      TileEntity tileEntity = world.getTileEntity(message.x, message.y, message.z);
      if (tileEntity instanceof TileEntityAlarm) {
         TileEntityAlarm alarm = (TileEntityAlarm) tileEntity;
         switch (message.button) {
            case GuiAlarm.BTN_HRDOWN:
               alarm.decHour();
               break;
            case GuiAlarm.BTN_HRUP:
               alarm.incHour();
               break;
            case GuiAlarm.BTN_MNTDOWN:
               alarm.decMinute();
               break;
            case GuiAlarm.BTN_MNTUP:
               alarm.incMinute();
               break;
         }

         alarm.getWorldObj().markBlockForUpdate(message.x, message.y, message.z);
      }

      return null;
   }
}
