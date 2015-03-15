package cz.ondraster.bettersleeping;

import cz.ondraster.bettersleeping.api.BetterSleepingAPI;
import cz.ondraster.bettersleeping.api.PlayerDebuff;
import net.minecraft.potion.Potion;
import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class Config {
   // ALARM
   public static int defaultWakeUpTime = 0;           // daytime offset
   public static int oversleepWithoutAlarm = 2000;    // how much can you oversleep without alarm
   public static int oversleepWithAlarm = 400;        // how much can you oversleep with alarm

   // MAX
   public static int maximumSleepCounter = 18000;     // how much tiredness you can get the most
   public static long spawnSleepCounter = 18000;      // how much tiredness you are spawned in

   // BLOCKS & ITEMS
   public static boolean enableRingWatch = true;      // enable ring watch
   public static boolean enableAlarmClock = true;     // enable alarm clock

   // DRAIN
   public static int ticksPerSleepCounter = 4;        // how many ticks it takes to add tiredness
   public static int tirednessJump = 8;               // how many tiredness is added on a jump
   public static double multiplicatorWhenSprinting = 2;  // how many times more you get tired when sprinting

   // GAIN
   public static double sleepPerSleptTick = 0.5;      // how much tiredness is removed on a slept tick
   public static int giveSleepCounterOnSleep = ticksPerSleepCounter; // how much tiredness is added when you lay in bed

   // debuff related stuff
   public static boolean enableDebuffs = true;        // enable debuffs
   public static boolean sleepOnGround = true;        // sleep on ground

   // tweaks
   public static boolean enablePositionReset = true;  // enable position reset when sleeping on ground
   public static double chanceToStopRain = 0.9;       // chance to stop rain
   public static boolean disableSleeping = false;     // disable sleeping alltogether
   public static boolean resetCounterOnDeath = true;  // reset counter on death
   public static boolean enableSleepTicks = false;    // enable tick simulation when asleep
   public static double percentPeopleToSleep = 0.5;   // How many people need to sleep
   public static boolean enableSleepCounter = true;   // enable sleep counter at all

   // gui
   public static int guiOffsetLeft = 4;               // sleepybar offset left
   public static int guiOffsetTop = 8;                // sleepybar offset top
   public static boolean enableSleepyBar = true;      // display sleepybar at all
   public static boolean enableAlarmSound = true;     // play sound on woken up by alarm
   public static boolean enableSleepMessage = true;   // enable sleep message related to how many people need to sleep

   // PRIVATE
   public static final int POTION_DURATION = 40;      // duration of potion effect in ticks [INTERNAL]


   private Configuration cfg;

   public Config(String filename) {
      cfg = new Configuration(new File(filename), true);
      cfg.load();
      percentPeopleToSleep =
            cfg.get("config", "percentPeopleToSleep", percentPeopleToSleep, "How many players have to be in bed in a dimension to sleep.")
                  .getDouble();
      defaultWakeUpTime =
            cfg.getInt("defaultWakeUpTime", "config", defaultWakeUpTime, 0, 23999, "morning offset when no alarm is found [ticks]");
      oversleepWithoutAlarm = cfg.getInt("oversleepWithoutAlarm", "config", oversleepWithoutAlarm, 0, 23999,
            "how much at maximum should a person oversleep without an alarm [ticks]");
      oversleepWithAlarm = cfg.getInt("oversleepWithAlarm", "config", oversleepWithAlarm, 0, 23999,
            "how much at maximum should a person oversleep with an alarm [ticks]");
      chanceToStopRain =
            cfg.get("config", "chanceToStopRain", chanceToStopRain, "what is the chance that it will stop raining").getDouble();
      enableSleepCounter = cfg.getBoolean("enableSleepCounter", "config", enableSleepCounter, "Enable sleep counter for all sub features");
      ticksPerSleepCounter = cfg.getInt("ticksPerSleepCounter", "config", ticksPerSleepCounter, 1, 23999,
            "How many player ticks between decreasing sleep counter");
      sleepPerSleptTick =
            cfg.get("config", "sleepPerSleptTick", sleepPerSleptTick, "How much sleep is increased with every slept tick").getDouble();
      maximumSleepCounter = cfg.getInt("maximumSleepCounter", "config", maximumSleepCounter, 1, Integer.MAX_VALUE,
            "How much sleep counter you can reach before being denied sleep privilege.");
      enableDebuffs = cfg.getBoolean("enableDebuffs", "config", enableDebuffs, "Enable all debuffs related to exhaustion");

      sleepOnGround = cfg.getBoolean("sleepOnGround", "config", sleepOnGround, "sleep on ground when absolutely exhausted");

      guiOffsetLeft = cfg.getInt("guiOffsetLeft", "gui", guiOffsetLeft, 1, 256, "Left offset of the sleepybar");
      guiOffsetTop = cfg.getInt("guiOffsetTop", "gui", guiOffsetTop, 1, 256, "Top offset of the sleepybar");
      enableSleepyBar = cfg.getBoolean("enableSleepyBar", "gui", enableSleepyBar, "Whether to enable the sleepybar render");

      enableRingWatch = cfg.getBoolean("enableRingWatch", "gui", enableSleepCounter, "Enable Baubles Ring Watch");

      enableAlarmSound = cfg.getBoolean("enableAlarmSound", "gui", enableAlarmSound, "Enable alarm sound when woken up");

      enablePositionReset = cfg.getBoolean("enablePositionReset", "config", enablePositionReset, "Enable position reset when falling " +
            "asleep on the ground [EXPERIMENTAL]");

      enableSleepTicks = cfg.getBoolean("enableSleepTicks", "config", enableSleepTicks, "Enable world tick simulation while asleep " +
            "[CAN CAUSE HIGH SERVER PERFORMANCE SPIKES]");

      enableAlarmClock = cfg.getBoolean("enableAlarmClock", "config", enableAlarmClock, "Enable Alarm clock");

      giveSleepCounterOnSleep = cfg.getInt("giveSleepCounterOnSleep", "config", giveSleepCounterOnSleep, 0, 23999, "How many sleep ticks " +
            "are given to player when lying in bed");

      enableSleepMessage = cfg.getBoolean("enableSleepMessage", "config", enableSleepMessage, "Enable message informing players about how" +
            " many are asleep.");

      resetCounterOnDeath = cfg.getBoolean("resetCounterOnDeath", "config", resetCounterOnDeath, "Reset tiredness counter on death");

      disableSleeping = cfg.getBoolean("disableSleeping", "config", disableSleeping, "Enable sleeping at all. Remember to disable the " +
            "remaining modules of this mod too, or you will be stuck in loop!");

      tirednessJump = cfg.getInt("tirednessJump", "config", tirednessJump, 0, 23999, "How much tiredness is added when the player jumps");

      // debuffs
      String[] debuffNames = {"moveSlowdown", "digSlowdown", "harm", "confusion", "blindness", "hunger", "weakness", "poison", "wither"};
      boolean[] defaultEnable = {true, true, false, false, true, false, true, false, false};
      int[] defaultTiredLevel = {800, 800, 800, 800, 800, 800, 800, 800, 800};
      int[] defaultMaxScale = {3, 3, 1, 1, 2, 1, 3, 1, 1};
      int[] potionEffect = {Potion.moveSlowdown.getId(), Potion.digSlowdown.getId(), Potion.harm.getId(), Potion.confusion.getId(),
            Potion.blindness.getId(), Potion.hunger.getId(), Potion.weakness.getId(), Potion.poison.getId(), Potion.wither.getId()};

      PlayerDebuff[] debuffs = new PlayerDebuff[debuffNames.length];

      for (int i = 0; i < debuffNames.length; i++) {
         String baseName = debuffNames[i];
         PlayerDebuff debuff = new PlayerDebuff();
         debuff.potion = Potion.potionTypes[potionEffect[i]];
         debuff.enable = cfg.getBoolean(baseName + "_enable", "debuffs", defaultEnable[i], "Enable this debuff");
         debuff.maxScale = cfg.getInt(baseName + "_maxScale", "debuffs", defaultMaxScale[i], 0, 5, "Maximum scaling of this debuff");
         debuff.tiredLevel = cfg.getInt(baseName + "_level", "debuffs", defaultTiredLevel[i], 1, 23999, "At which level is this debuff " +
               "applied");
         debuffs[i] = debuff;
      }

      for (PlayerDebuff debuff : debuffs) {
         BetterSleepingAPI.addDebuff(debuff);
      }

      // sanity check for lazy mod authors
      if (disableSleeping) {
         if (enableSleepCounter) {
            BSLog.warn("You have disabled sleeping yet enabled sleeping counter. YOU MONSTER! Disabling sleep counter for you...");
            enableSleepCounter = false;
         }
      }

      save();
   }

   public void save() {
      cfg.save();
   }
}
