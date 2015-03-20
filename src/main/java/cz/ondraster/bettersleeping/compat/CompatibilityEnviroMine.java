package cz.ondraster.bettersleeping.compat;

import enviromine.handlers.EM_StatusManager;
import enviromine.trackers.EnviroDataTracker;
import net.minecraft.entity.player.EntityPlayer;

public class CompatibilityEnviroMine {
   public static EnviroDataTracker getTracker(EntityPlayer player) {
      return EM_StatusManager.lookupTracker(player);
   }

   public static float getSanity(EnviroDataTracker tracker) {
      return tracker.sanity;
   }

   public static void changeSanity(EntityPlayer player, float offset) {
      getTracker(player).sanity += offset;
   }
}
