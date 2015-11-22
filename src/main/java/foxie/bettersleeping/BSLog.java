package foxie.bettersleeping;

import cpw.mods.fml.common.FMLLog;

public class BSLog {
   public static void warn(String message, Object... params) {
      FMLLog.warning("[" + BetterSleeping.MODID + "] " + message, params);
   }

   public static void err(String message, Object... params) {
      FMLLog.severe("[" + BetterSleeping.MODID + "] " + message, params);
   }

   public static void info(String message, Object... params) {
      FMLLog.info("[" + BetterSleeping.MODID + "] " + message, params);
   }
}
