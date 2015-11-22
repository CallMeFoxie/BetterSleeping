package foxie.bettersleeping;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.oredict.OreDictionary;

public class Registrator {
   public static void registerBlock(Block block) {
      GameRegistry.registerBlock(block, BetterSleeping.MODID + "_" + block.getUnlocalizedName().substring(5));
   }

   public static void registerItem(Item item) {
      GameRegistry.registerItem(item, BetterSleeping.MODID + "_" + item.getUnlocalizedName().substring(5));
   }

   public static void registerMultiBlock(Block block, Class myClass) {
      GameRegistry.registerBlock(block, myClass, BetterSleeping.MODID + "_" + block.getUnlocalizedName().substring(5));
   }

   public static void registerTileEntity(Class<? extends TileEntity> te, String unlocalizedname) {
      GameRegistry.registerTileEntity(te, BetterSleeping.MODID + "_" + unlocalizedname);
   }

   public static void registerOreDict(Item item, String oreName) {
      OreDictionary.registerOre(oreName, new ItemStack(item));
   }

   public static ForgeDirection findMatching(int x, int y, int z) {
      for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
         if (dir.offsetX == x && dir.offsetY == y && dir.offsetZ == z)
            return dir;
      }

      return ForgeDirection.DOWN;
   }
}
