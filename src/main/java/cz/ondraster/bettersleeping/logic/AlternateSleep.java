package cz.ondraster.bettersleeping.logic;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import java.util.List;

public class AlternateSleep {
   @SuppressWarnings("unchecked")
   public static int getSleepingPeopleInWorld(World world) {
      int sum = 0;
      for (EntityPlayer player : (List<EntityPlayer>) world.playerEntities) {
         if (player.isPlayerFullyAsleep())
            sum++;
      }

      return sum;
   }
}
