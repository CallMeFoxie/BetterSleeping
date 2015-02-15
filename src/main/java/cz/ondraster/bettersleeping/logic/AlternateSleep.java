package cz.ondraster.bettersleeping.logic;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChunkCoordinates;
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

   public static ChunkCoordinates getSafePosition(World world, double ox, double oy, double oz) {

      if (!world.getBlock((int) ox, (int) oy, (int) oz).getMaterial().isOpaque() &&
            !world.getBlock((int) ox, (int) oy + 1, (int) oz).getMaterial().isOpaque()) {
         return new ChunkCoordinates((int) ox, (int) oy, (int) oz);
      }

      for (int x = (int) ox - 2; x < ox + 2; x++) {
         for (int z = (int) oz - 2; z < oz + 2; z++) {
            if (!world.getBlock(x, (int) oy, z).getMaterial().isOpaque() && !world.getBlock(x, (int) oy + 1, z).getMaterial().isOpaque()) {
               return new ChunkCoordinates(x, (int) oy, z);
            }
         }
      }

      return new ChunkCoordinates((int) ox, (int) oy, (int) oz);
   }
}
