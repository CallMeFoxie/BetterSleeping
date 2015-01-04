package cz.ondraster.bettersleeping.logic;

import cz.ondraster.bettersleeping.Config;
import cz.ondraster.bettersleeping.tileentity.TileEntityAlarm;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import java.util.ArrayList;
import java.util.List;

public class Alarm {
   public static final int MINUTES_IN_HOUR = 50;

   public static List<TileEntityAlarm> getAllAlarms(List<EntityPlayer> playerEntities) {
      List alarms = new ArrayList<TileEntityAlarm>();

      for (EntityPlayer player : playerEntities) {
         TileEntityAlarm alarm = findNearbyAlarm(player);
         if (alarm != null)
            alarms.add(alarm);
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
   public static void sleepWorld(WorldServer world) {
      List<TileEntityAlarm> alarms = getAllAlarms(world.playerEntities);
      int mntTotal = 0;
      for (TileEntityAlarm alarm : alarms) {
         mntTotal += MinecraftTime.extrapolateTime(alarm.getHour(), alarm.getMinute()); // (minutes + hours * minutesInHour) * ticks
      }

      if (alarms.size() != 0)
         mntTotal /= alarms.size();
      else
         mntTotal = 0;

      long curTime = world.getWorldTime();

      if (alarms.size() == 0) {
         long i = curTime + 24000L;
         i -= i % 24000;
         i += Config.defaultWakeUpTime;
         i += world.rand.nextInt(Config.oversleepWithoutAlarm);
         world.setWorldTime(i);
      } else {
         long i = curTime;
         if (mntTotal <= curTime % 24000) // have to roll to another day
            i += 24000L;

         i -= i % 24000; // align to the next morning
         // append new time
         i += mntTotal;
         i += world.rand.nextInt(Config.oversleepWithAlarm / alarms.size());
         world.setWorldTime(i);
      }

      MinecraftTime time = MinecraftTime.getFromWorldTime(world.getWorldTime());

      // reset all players
      for (EntityPlayer player : (List<EntityPlayer>) world.playerEntities) {
         if (player.isPlayerSleeping())
            player.wakeUpPlayer(false, false, true);

         player.addChatMessage(new ChatComponentText("Wake up! It is " + time.toString()));
      }

      // possibly reset weather?
      if (Config.chanceToStopRain >= world.rand.nextDouble())
         world.provider.resetRainAndThunder();
   }

   public static boolean canNotSleep() {
      return false;
   }


}
