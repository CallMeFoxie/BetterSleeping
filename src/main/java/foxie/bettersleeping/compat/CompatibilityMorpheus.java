package foxie.bettersleeping.compat;

import cpw.mods.fml.common.Optional;
import foxie.bettersleeping.BSLog;
import foxie.bettersleeping.Config;
import foxie.bettersleeping.logic.Alarm;
import foxie.bettersleeping.BSLog;
import foxie.bettersleeping.logic.Alarm;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.quetzi.morpheus.api.IMorpheusAPI;
import net.quetzi.morpheus.api.INewDayHandler;

@Optional.Interface(modid = "Morpheus", iface = "net.quetzi.morpheus.api.INewDayHandler")
public class CompatibilityMorpheus implements INewDayHandler {

   public CompatibilityMorpheus() {
      BSLog.info("Loading Morpheus compatibility...");

      try {
         Class clazz = Class.forName("net.quetzi.morpheus.MorpheusRegistry");
         IMorpheusAPI registry = (IMorpheusAPI) clazz.newInstance();
         registry.registerHandler(this, 0);
         BSLog.info("Morpheus compat seems to have loaded, disabling custom sleeping code.");
         Config.percentPeopleToSleep = 2d;
      } catch (Exception e) {
         BSLog.warn("Failed to load the Morpheus compatibility!");
      }

   }

   @Override
   public void startNewDay() {
      World world = MinecraftServer.getServer().worldServerForDimension(0);
      Alarm.sleepWorld(world);
   }
}
