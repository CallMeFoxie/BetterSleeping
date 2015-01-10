package cz.ondraster.bettersleeping.proxy;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cz.ondraster.bettersleeping.BetterSleeping;
import cz.ondraster.bettersleeping.block.BlockClass;
import cz.ondraster.bettersleeping.client.gui.GuiHandlers;
import cz.ondraster.bettersleeping.network.Network;
import net.minecraftforge.common.MinecraftForge;

public class ProxyCommon {
   public void preinit(FMLPreInitializationEvent event) {
      BlockClass.register();
      Network network = new Network();
   }

   public void init(FMLInitializationEvent event) {
      FMLCommonHandler.instance().bus().register(BetterSleeping.INSTANCE);
      MinecraftForge.EVENT_BUS.register(BetterSleeping.INSTANCE);

      NetworkRegistry.INSTANCE.registerGuiHandler(BetterSleeping.INSTANCE, new GuiHandlers());
   }
}
