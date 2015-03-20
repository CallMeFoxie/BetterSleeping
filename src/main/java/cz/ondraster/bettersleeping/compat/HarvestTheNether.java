package cz.ondraster.bettersleeping.compat;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.relauncher.Side;
import cz.ondraster.bettersleeping.Config;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldProviderHell;

public class HarvestTheNether {
   public static boolean isSurfaceWorld(WorldProvider provider) {
      if(FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
         return false;

      StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
      if ((stackTraceElements[3].getMethodName().equals("sleepInBedAt") || stackTraceElements[3].getMethodName().equals("a")) &&
            stackTraceElements[3].getClassName().equals("net.minecraft.entity.player.EntityPlayer") || stackTraceElements[3].getClassName()
            .equals("yz")) {
         if (Loader.isModLoaded("harvestthenether") && provider instanceof WorldProviderHell && Config.enableCompatHarvestTN) {
            return true;
         }
      }

      return false;
   }
}
