package foxie.bettersleeping.asm;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.event.FMLConstructionEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import foxie.bettersleeping.BetterSleeping;

public class BetterSleepingCore extends DummyModContainer {

   public BetterSleepingCore() {
      super(new ModMetadata());
      ModMetadata metadata = getMetadata();
      metadata.modId = BetterSleeping.MODID + "core";
      metadata.name = BetterSleeping.NAME + " Core";
      metadata.version = "1.0";
      metadata.authorList.add(BetterSleeping.AUTHOR);
   }

   @Override
   public boolean registerBus(EventBus bus, LoadController controller) {
      bus.register(this);
      return true;
   }

   @Subscribe
   public void modConstruction(FMLConstructionEvent event) {

   }

   @EventHandler
   public void preinit(FMLPreInitializationEvent event) {

   }

   @EventHandler
   public void postInit(FMLPostInitializationEvent event) {
   }
}
