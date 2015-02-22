package cz.ondraster.bettersleeping.logic;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import cz.ondraster.bettersleeping.Config;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.ReportedException;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.ForgeModContainer;

import java.util.Iterator;
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

   private static boolean tickingWorld = false;

   public static void tickWorldCustom(WorldServer worldServer, long ticks) {
      CrashReport report;
      CrashReportCategory category;

      if (tickingWorld)
         return;

      tickingWorld = true;

      for (long i = 0; i < ticks; i++) {
         FMLCommonHandler.instance().onPreWorldTick(worldServer);

         // pretty much identical to vanilla code - BUT I am ticking only TEs
         Iterator iterator = worldServer.loadedTileEntityList.iterator();

         while (iterator.hasNext()) {
            TileEntity tileentity = (TileEntity) iterator.next();

            if (!tileentity.isInvalid() && tileentity.hasWorldObj() &&
                  worldServer.blockExists(tileentity.xCoord, tileentity.yCoord, tileentity.zCoord)) {
               try {
                  tileentity.updateEntity();
               } catch (Throwable throwable) {
                  report = CrashReport.makeCrashReport(throwable, "Ticking block entity");
                  category = report.makeCategory("Block entity being ticked");
                  tileentity.func_145828_a(category);

                  if (ForgeModContainer.removeErroringTileEntities) {
                     FMLLog.severe(report.getCompleteReport());
                     tileentity.invalidate();
                     worldServer.setBlockToAir(tileentity.xCoord, tileentity.yCoord, tileentity.zCoord);
                  } else {
                     throw new ReportedException(report);
                  }
               }
            }

            if (tileentity.isInvalid()) {
               iterator.remove();

               if (worldServer.checkChunksExist(tileentity.xCoord, tileentity.yCoord, tileentity.zCoord, tileentity.xCoord, tileentity
                     .yCoord, tileentity.zCoord)) {
                  Chunk chunk = worldServer.getChunkFromChunkCoords(tileentity.xCoord >> 4, tileentity.zCoord >> 4);

                  if (chunk != null) {
                     chunk.removeInvalidTileEntity(tileentity.xCoord & 15, tileentity.yCoord, tileentity.zCoord & 15);
                  }
               }
            }
         }

         FMLCommonHandler.instance().onPostWorldTick(worldServer);
      }

      tickingWorld = false;
   }

   public static void trySleepingWorld(World world) {
      trySleepingWorld(world, false);
   }

   public static void trySleepingWorld(World world, boolean subtractDisconnecting) {
      int sleeping = AlternateSleep.getSleepingPeopleInWorld(world);
      if (subtractDisconnecting)
         sleeping--;

      if ((double) sleeping / world.playerEntities.size() >= Config.percentPeopleToSleep) {
         Alarm.sleepWorld(world);
      } else if (sleeping > 0) {
         if (Config.enableSleepMessage) {
            for (EntityPlayer player : (List<EntityPlayer>) world.playerEntities)
               player.addChatMessage(new ChatComponentTranslation("msg.playersSleeping",
                     Math.floor((double) sleeping / world.playerEntities.size()) * 100));
         }
      }
   }
}
