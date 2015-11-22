package foxie.bettersleeping.block;

import foxie.bettersleeping.Config;
import foxie.bettersleeping.Registrator;
import foxie.bettersleeping.tileentity.TileEntityAlarm;
import net.minecraft.block.Block;

public class BlockClass {
   public static Block blockAlarm;

   public static void register() {
      if (Config.enableAlarmClock) {
         blockAlarm = new BlockAlarm();
         blockAlarm.setBlockName("alarm");
         Registrator.registerBlock(blockAlarm);

         Registrator.registerTileEntity(TileEntityAlarm.class, "alarm_te");
      }

   }
}
