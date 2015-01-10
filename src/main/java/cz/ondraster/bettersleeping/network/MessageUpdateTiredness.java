package cz.ondraster.bettersleeping.network;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cz.ondraster.bettersleeping.player.SleepingProperty;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

public class MessageUpdateTiredness implements IMessage, IMessageHandler<MessageUpdateTiredness, IMessage> {
   long tiredness;

   public MessageUpdateTiredness() {

   }

   public MessageUpdateTiredness(long level) {
      this.tiredness = level;
   }

   @Override
   public void fromBytes(ByteBuf buf) {
      tiredness = buf.readLong();
   }

   @Override
   public void toBytes(ByteBuf buf) {
      buf.writeLong(tiredness);
   }

   @Override
   public IMessage onMessage(MessageUpdateTiredness message, MessageContext ctx) {
      EntityPlayer player = FMLClientHandler.instance().getClientPlayerEntity();
      SleepingProperty property = SleepingProperty.get(player);
      property.sleepCounter = message.tiredness;

      return null;
   }
}
