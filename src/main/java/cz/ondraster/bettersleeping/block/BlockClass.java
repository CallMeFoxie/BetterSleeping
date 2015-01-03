package cz.ondraster.bettersleeping.block;

import cz.ondraster.bettersleeping.Registrator;
import cz.ondraster.bettersleeping.tileentity.TileEntityAlarm;
import net.minecraft.block.Block;

public class BlockClass {
   public static Block blockAlarm;

   public static void register() {
      blockAlarm = new BlockAlarm();
      blockAlarm.setBlockName("alarm");
      Registrator.registerBlock(blockAlarm);

      Registrator.registerTileEntity(TileEntityAlarm.class, "alarm_te");

   }
}
