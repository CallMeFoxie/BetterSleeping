package cz.ondraster.bettersleeping.compat;

import cpw.mods.fml.common.FMLLog;
import cz.ondraster.bettersleeping.BetterSleeping;
import cz.ondraster.bettersleeping.Config;
import cz.ondraster.bettersleeping.logic.Alarm;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.quetzi.morpheus.api.IMorpheusAPI;
import net.quetzi.morpheus.api.INewDayHandler;

public class CompatibilityMorpheus implements INewDayHandler {

   public CompatibilityMorpheus() {
      FMLLog.info("[" + BetterSleeping.MODID + "] Loading Morpheus compatibility...");

      try {
         Class clazz = Class.forName("net.quetzi.morpheus.MorpheusRegistry").getClass();
         IMorpheusAPI registry = (IMorpheusAPI) clazz.newInstance();
         registry.registerHandler(this, 0);

      } catch (Exception e) {
         FMLLog.warning("[" + BetterSleeping.MODID + "] Failed to load the Morpheus compatibility!");
      }

      FMLLog.info("[" + BetterSleeping.MODID + "] Morpheus compat seems to have loaded, disabling custom sleeping code.");
      Config.percentPeopleToSleep = 2d;
   }

   @Override
   public void startNewDay() {
      World world = MinecraftServer.getServer().worldServerForDimension(0);
      Alarm.sleepWorld(world);
   }
}
