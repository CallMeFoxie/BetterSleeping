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
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockAlarm extends Block implements ITileEntityProvider {
   protected BlockAlarm() {
      super(Material.plants);
      setCreativeTab(CreativeTabs.tabDecorations);
      setBlockBounds(0f, 0f, 0.35f, 1f, 0.6f, 0.75f);
   }

   @Override
   public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
      if (player.isSneaking())
         return false;

      player.openGui(BetterSleeping.INSTANCE, GuiHandlers.GUI_ALARM, world, x, y, z);
      return true;
      
   }

   @Override
   public TileEntity createNewTileEntity(World world, int meta) {
      return new TileEntityAlarm();
   }

   @Override
   public boolean shouldSideBeRendered(IBlockAccess iblockaccess, int i, int j, int k, int l) {
      return false;
   }

   //And this tell it that you can see through this block, and neighbor blocks should be rendered.
   @Override
   public boolean isOpaqueCube() {
      return false;
   }
}
