package foxie.bettersleeping.proxy;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import foxie.bettersleeping.BetterSleeping;
import foxie.bettersleeping.EventHandlers;
import foxie.bettersleeping.block.BlockClass;
import foxie.bettersleeping.client.gui.GuiHandlers;
import foxie.bettersleeping.item.ItemClass;
import foxie.bettersleeping.network.Network;
import foxie.bettersleeping.EventHandlers;
import foxie.bettersleeping.block.BlockClass;
import foxie.bettersleeping.client.gui.GuiHandlers;
import foxie.bettersleeping.network.Network;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class ProxyCommon {
   public void preinit(FMLPreInitializationEvent event) {
      BlockClass.register();
      ItemClass.register();

      new Network();
      Network.initClient();

      new EventHandlers();
   }

   public void init(FMLInitializationEvent event) {
      FMLCommonHandler.instance().bus().register(EventHandlers.INSTANCE);
      MinecraftForge.EVENT_BUS.register(EventHandlers.INSTANCE);

      NetworkRegistry.INSTANCE.registerGuiHandler(BetterSleeping.INSTANCE, new GuiHandlers());

      if (BlockClass.blockAlarm != null)
         // register recipes
         GameRegistry.addRecipe(
               new ShapedOreRecipe(new ItemStack(BlockClass.blockAlarm), "iii", "igi", "srs", 'i', Items.iron_ingot, 'g', Items.gold_nugget,
                     's', Blocks.stone, 'r', Items.redstone));

      if (ItemClass.itemRingWatch != null)
         GameRegistry.addRecipe(
               new ShapedOreRecipe(new ItemStack(ItemClass.itemRingWatch), "iii", "igi", "iri", 'i', Items.iron_ingot, 'g',
                     Items.gold_nugget, 'r', Items.redstone));

      if (ItemClass.itemPillCaffeine != null) {
         GameRegistry.addRecipe(
               new ShapelessOreRecipe(new ItemStack(ItemClass.itemPillCaffeine), Items.nether_wart, Items.potionitem, Blocks
                     .brown_mushroom, Items.sugar, Items.speckled_melon));
         GameRegistry.addRecipe(
               new ShapelessOreRecipe(new ItemStack(ItemClass.itemPillCaffeine), Items.nether_wart, Items.potionitem, Blocks
                     .red_mushroom, Items.sugar, Items.speckled_melon));
      }

      if (ItemClass.itemPillSleeping != null) {
         GameRegistry.addRecipe(
               new ShapelessOreRecipe(new ItemStack(ItemClass.itemPillCaffeine), Items.nether_wart, Items.potionitem, Blocks
                     .brown_mushroom, Items.sugar, "dyeBrown"));
         GameRegistry.addRecipe(
               new ShapelessOreRecipe(new ItemStack(ItemClass.itemPillCaffeine), Items.nether_wart, Items.potionitem, Blocks
                     .red_mushroom, Items.sugar, Items.speckled_melon));
      }
   }

   public EntityPlayer getPlayer() {
      return null;
   }
}
