package cz.ondraster.bettersleeping;

import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class Config {
   public static int defaultWakeUpTime = 0;
   public static int oversleepWithoutAlarm = 2000;
   public static int oversleepWithAlarm = 400;
   public static double chanceToStopRain = 0.9;
   public static boolean enableSleepCounter = true;
   public static long spawnSleepCounter = 12000;
   public static int ticksPerSleepCounter = 6;
   public static double sleepPerSleptTick = 0.25;
   public static int maximumSleepCounter = 84000;

   private Configuration cfg;

   public Config(String filename) {
      cfg = new Configuration(new File(filename));
      cfg.load();
      defaultWakeUpTime = cfg.getInt("defaultWakeUpTime", "config", defaultWakeUpTime, 0, 23999, "morning offset when no alarm is found [ticks]");
      oversleepWithoutAlarm = cfg.getInt("oversleepWithoutAlarm", "config", oversleepWithoutAlarm, 0, 23999, "how much at maximum should a person oversleep without an alarm [ticks]");
      oversleepWithAlarm = cfg.getInt("oversleepWithAlarm", "config", oversleepWithAlarm, 0, 23999, "how much at maximum should a person oversleep with an alarm [ticks]");
      chanceToStopRain = cfg.get("config", "chanceToStopRain", chanceToStopRain, "what is the chance that it will stop raining").getDouble();
      enableSleepCounter = cfg.getBoolean("enableSleepCounter", "config", enableSleepCounter, "Enable sleep counter for all sub features");
      ticksPerSleepCounter = cfg.getInt("ticksPerSleepCounter", "config", ticksPerSleepCounter, 1, 23999, "How many player ticks between decreasing sleep counter");
      sleepPerSleptTick = cfg.get("config", "sleepPerSleptTick", sleepPerSleptTick, "How much sleep is increased with every slept tick").getDouble();
      maximumSleepCounter = cfg.getInt("maximumSleepCounter", "config", maximumSleepCounter, 0, Integer.MAX_VALUE, "How much sleep counter you can reach before being denied sleep privilige.");

      save();
   }

   public void save() {
      cfg.save();
   }
}
