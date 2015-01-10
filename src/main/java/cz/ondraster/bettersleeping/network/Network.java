package cz.ondraster.bettersleeping.network;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import cz.ondraster.bettersleeping.BetterSleeping;

public class Network {
   public static Network instance;
   public static SimpleNetworkWrapper networkChannel;

   public Network() {
      instance = this;
      networkChannel = NetworkRegistry.INSTANCE.newSimpleChannel(BetterSleeping.MODID);
      init();
   }

   public static void init() {
      networkChannel.registerMessage(MessageGuiAlarmButton.class, MessageGuiAlarmButton.class, 1, Side.SERVER);
   }

   public static void initClient() {
      networkChannel.registerMessage(MessageUpdateTiredness.class, MessageUpdateTiredness.class, 2, Side.CLIENT);
   }
}
