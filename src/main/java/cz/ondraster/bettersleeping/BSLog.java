package cz.ondraster.bettersleeping;

import cpw.mods.fml.common.FMLLog;

public class BSLog {
   public static void warn(String message, String... params) {
      FMLLog.warning("[" + BetterSleeping.MODID + "] " + message, params);
   }

   public static void err(String message, String... params) {
      FMLLog.severe("[" + BetterSleeping.MODID + "] " + message, params);
   }

   public static void info(String message, String... params) {
      FMLLog.info("[" + BetterSleeping.MODID + "] " + message, params);
   }
}
