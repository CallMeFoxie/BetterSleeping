package foxie.bettersleeping.block;

import foxie.bettersleeping.BetterSleeping;
import foxie.bettersleeping.client.gui.GuiHandlers;
import foxie.bettersleeping.tileentity.TileEntityAlarm;
import foxie.bettersleeping.client.gui.GuiHandlers;
import foxie.bettersleeping.tileentity.TileEntityAlarm;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockAlarm extends Block implements ITileEntityProvider {
   protected BlockAlarm() {
      super(Material.plants);
      setCreativeTab(CreativeTabs.tabDecorations);
      setBlockBounds(0f, 0f, 0f, 1f, 0.3f, 1.0f);
      setBlockTextureName(BetterSleeping.MODID + ":alarm");
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

   @Override
   public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack itemstack) {
      int l = MathHelper.floor_double((double) (player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

      if (l == 0)
         world.setBlockMetadataWithNotify(x, y, z, 2, 2);


      if (l == 1)
         world.setBlockMetadataWithNotify(x, y, z, 5, 2);


      if (l == 2)
         world.setBlockMetadataWithNotify(x, y, z, 3, 2);


      if (l == 3)
         world.setBlockMetadataWithNotify(x, y, z, 4, 2);


   }
}
