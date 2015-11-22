package foxie.bettersleeping.compat;

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
      try {
         getTracker(player).sanity += offset;
      } catch (Exception e) {
         // sometimes may be null or any exception from EM that I cannot predict. Better than crashing I guess?
      }
   }
}
