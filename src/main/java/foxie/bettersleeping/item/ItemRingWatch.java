package foxie.bettersleeping.item;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import cpw.mods.fml.common.Optional;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

@Optional.Interface(iface = "baubles.api.IBauble", modid = "Baubles|API")
public class ItemRingWatch extends Item implements IBauble {
   public ItemRingWatch() {
      setUnlocalizedName("ringwatch");
      setTextureName("bettersleeping:ringwatch");
      setCreativeTab(CreativeTabs.tabTools);
      setMaxStackSize(1);
   }

   @Override
   public BaubleType getBaubleType(ItemStack itemstack) {
      return BaubleType.RING;
   }

   @Override
   public void onWornTick(ItemStack itemstack, EntityLivingBase player) {

   }

   @Override
   public void onEquipped(ItemStack itemstack, EntityLivingBase player) {

   }

   @Override
   public void onUnequipped(ItemStack itemstack, EntityLivingBase player) {

   }

   @Override
   public boolean canEquip(ItemStack itemstack, EntityLivingBase player) {
      return true;
   }

   @Override
   public boolean canUnequip(ItemStack itemstack, EntityLivingBase player) {
      return true;
   }
}
