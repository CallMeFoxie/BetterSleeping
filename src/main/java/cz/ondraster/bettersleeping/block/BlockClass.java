package cz.ondraster.bettersleeping.block;

import cpw.mods.fml.client.registry.ClientRegistry;
import cz.ondraster.bettersleeping.Registrator;
import cz.ondraster.bettersleeping.client.renderer.AlarmRenderer;
import cz.ondraster.bettersleeping.tileentity.TileEntityAlarm;
import net.minecraft.block.Block;

public class BlockClass {
   public static Block blockAlarm;

   public static void register() {
      blockAlarm = new BlockAlarm();
      blockAlarm.setBlockName("alarm");
      Registrator.registerBlock(blockAlarm);
      ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAlarm.class, new AlarmRenderer());

      Registrator.registerTileEntity(TileEntityAlarm.class, "alarm_te");

   }
}
