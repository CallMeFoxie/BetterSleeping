package cz.ondraster.bettersleeping.block;

import cz.ondraster.bettersleeping.BetterSleeping;
import cz.ondraster.bettersleeping.client.gui.GuiHandlers;
import cz.ondraster.bettersleeping.tileentity.TileEntityAlarm;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockAlarm extends Block implements ITileEntityProvider {
   protected BlockAlarm() {
      super(Material.anvil);
      setCreativeTab(CreativeTabs.tabDecorations);
   }

   @Override
   public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
      if (player.isSneaking())
         return false;

      //if (!world.isRemote) {
      player.openGui(BetterSleeping.INSTANCE, GuiHandlers.GUI_ALARM, world, x, y, z);
      return true;
      //}

      //return false;
   }

   @Override
   public TileEntity createNewTileEntity(World world, int meta) {
      return new TileEntityAlarm();
   }
}
