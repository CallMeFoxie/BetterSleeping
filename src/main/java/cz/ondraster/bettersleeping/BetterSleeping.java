package cz.ondraster.bettersleeping;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cz.ondraster.bettersleeping.block.BlockClass;
import cz.ondraster.bettersleeping.client.gui.GuiHandlers;
import cz.ondraster.bettersleeping.network.Network;

@Mod(modid = BetterSleeping.MODID, version = BetterSleeping.VERSION)
public class BetterSleeping {
   public static final String MODID = "BetterSleeping";
   public static final String VERSION = "1.0";

   @Mod.Instance
   public static BetterSleeping INSTANCE;

   @EventHandler
   public void preinit(FMLPreInitializationEvent event) {
      BlockClass.register();
      Network network = new Network();
      NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandlers());
   }

   @EventHandler
   public void init(FMLInitializationEvent event) {
      // some example code

   }
}
