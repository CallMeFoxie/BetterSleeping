package cz.ondraster.bettersleeping.logic;

import cz.ondraster.bettersleeping.BSSavedData;
import cz.ondraster.bettersleeping.BetterSleeping;
import cz.ondraster.bettersleeping.Config;
import cz.ondraster.bettersleeping.api.PlayerData;
import cz.ondraster.bettersleeping.api.WorldSleepEvent;
import cz.ondraster.bettersleeping.tileentity.TileEntityAlarm;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;

import java.util.ArrayList;
import java.util.List;

public class Alarm {
   public static final int MINUTES_IN_HOUR = 50;

   public static List<TileEntityAlarm> getAllAlarms(List<EntityPlayer> playerEntities, List<EntityPlayer> outPlayers) {
      List alarms = new ArrayList<TileEntityAlarm>();

      for (EntityPlayer player : playerEntities) {
         TileEntityAlarm alarm = findNearbyAlarm(player);
         if (alarm != null) {
            alarms.add(alarm);
            outPlayers.add(player);
         }
      }

      return alarms;
   }

   public static TileEntityAlarm findNearbyAlarm(EntityPlayer player) {
      World world = player.worldObj;
      for (double x = player.posX - 2; x < player.posX + 2; x++) {
         for (double y = player.posY - 1; y < player.posY + 2; y++) {
            for (double z = player.posZ - 2; z < player.posZ + 2; z++) {
               TileEntity tileEntity = world.getTileEntity((int) x, (int) y, (int) z);
               if (tileEntity instanceof TileEntityAlarm)
                  return (TileEntityAlarm) tileEntity;
            }
         }
      }

      return null;
   }

   @SuppressWarnings("unchecked")
   public static void sleepWorld(World world) {
      MinecraftForge.EVENT_BUS.post(new WorldSleepEvent.Pre(world));

      List<EntityPlayer> playersWithAlarms = new ArrayList<EntityPlayer>();
      List<TileEntityAlarm> alarms = getAllAlarms(world.playerEntities, playersWithAlarms);
      int mntTotal = 0;
      for (TileEntityAlarm alarm : alarms) {
         mntTotal += MinecraftTime.extrapolateTime(alarm.getHour(), alarm.getMinute()); // (minutes + hours * minutesInHour) * ticks
      }

      if (alarms.size() != 0)
         mntTotal /= alarms.size();
      else
         mntTotal = 0;

      long curTime = world.getWorldTime();
      long reallySleptTime = 0;

      if (alarms.size() == 0) {
         long i = curTime + 24000L;
         i -= i % 24000;
         i += Config.defaultWakeUpTime;
         i += world.rand.nextInt(Config.oversleepWithoutAlarm);
         reallySleptTime = i - world.getWorldTime();
         world.setWorldTime(i);
      } else {
         long i = curTime;
         if (mntTotal <= curTime % 24000) // have to roll to another day
            i += 24000L;

         i -= i % 24000; // align to the next morning
         // append new time
         i += mntTotal;
         i += world.rand.nextInt(Config.oversleepWithAlarm / alarms.size());
         reallySleptTime = i - world.getWorldTime();
         world.setWorldTime(i);
      }

      MinecraftTime time = MinecraftTime.getFromWorldTime(world.getWorldTime());

      // reset all players
      for (EntityPlayer player : (List<EntityPlayer>) world.playerEntities) {
         if (player.isPlayerSleeping()) {
            player.wakeUpPlayer(false, false, true);
            if (playersWithAlarms.contains(player)) {
               //player.playSound(BetterSleeping.MODID + ":alarm", 0.5F, 2.0F);
               if (Config.alarmSoundLevel > 0)
                  world.playSoundEffect(player.posX, player.posY, player.posZ, BetterSleeping.MODID + ":alarm", (float) Config
                        .alarmSoundLevel, 1F);
            }

            if (Config.enableSleepCounter) {
               PlayerData property = BSSavedData.instance().getData(player);
               property.increaseSleepCounter((long) (reallySleptTime * Config.sleepPerSleptTick));
               property.decreaseCaffeineLevel((reallySleptTime * Config.caffeinePerTick));
            }
         }

         player.addChatMessage(new ChatComponentTranslation("msg.wakeUp", time.toString()));
      }

      if (Config.enableSleepTicks && world instanceof WorldServer) {
         AlternateSleep.tickWorldCustom((WorldServer) world, reallySleptTime);
      }

      // possibly reset weather?
      if (Config.chanceToStopRain >= world.rand.nextDouble())
         world.provider.resetRainAndThunder();

      MinecraftForge.EVENT_BUS.post(new WorldSleepEvent.Post(world, reallySleptTime));
   }

   public static boolean canNotSleep(EntityPlayer player) {
      return false;
   }

   public static boolean canSleep(EntityPlayer player) {
      PlayerData property = BSSavedData.instance().getData(player);
      if (property.getSleepCounter() < Config.maximumSleepCounter && Config.enableDebuffs && Config.enableSleepCounter &&
            Config.sleepOnGround) {
         return true;
      }

      return false;
   }


}
