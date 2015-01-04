package cz.ondraster.bettersleeping;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cz.ondraster.bettersleeping.block.BlockClass;
import cz.ondraster.bettersleeping.client.gui.GuiHandlers;
import cz.ondraster.bettersleeping.logic.Alarm;
import cz.ondraster.bettersleeping.network.Network;
import cz.ondraster.bettersleeping.player.SleepingProperty;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;

@Mod(modid = BetterSleeping.MODID, version = BetterSleeping.VERSION, name = BetterSleeping.NAME)
public class BetterSleeping {
   public static final String MODID = "bettersleeping";
   public static final String VERSION = "1.0";
   public static final String NAME = "Better Sleeping";
   public static String AUTHOR = "OndraSter";

   @Mod.Instance
   public static BetterSleeping INSTANCE;


   @EventHandler
   public void preinit(FMLPreInitializationEvent event) {
      BlockClass.register();
      Network network = new Network();
      NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandlers());


   }

   @EventHandler
   public void init(FMLInitializationEvent event) {
      FMLCommonHandler.instance().bus().register(this);
      MinecraftForge.EVENT_BUS.register(this);

   }

   @SubscribeEvent
   public void onPreWorldTick(TickEvent.WorldTickEvent event) {
      if (!(event.world instanceof WorldServer))
         return;

      WorldServer world = (WorldServer) event.world;

      if (world.areAllPlayersAsleep()) {
         Alarm.sleepWorld(world);
      }
   }

   @SubscribeEvent
   public void onPlayerTick(TickEvent.PlayerTickEvent event) {
      SleepingProperty property = null;
      if (Config.enableSleepCounter) {
         property = SleepingProperty.get(event.player);
         property.ticksSinceUpdate++;
         if (property.ticksSinceUpdate >= Config.ticksPerSleepCounter) {
            property.ticksSinceUpdate = 0;
            property.sleepCounter--;
            if (property.sleepCounter < 0)
               property.sleepCounter = 0;
         }
      }

      if (Config.enableDebuffs && Config.enableSleepCounter) {
         if (property.sleepCounter <= Config.slownessDebuff) {
            if (event.player.getActivePotionEffect(Potion.moveSlowdown) == null)
               event.player.addPotionEffect(new PotionEffect(Potion.moveSlowdown.getId(), 20));
         }

         if (property.sleepCounter <= Config.visionDebuff) {
            if (event.player.getActivePotionEffect(Potion.blindness) == null)
               event.player.addPotionEffect(new PotionEffect(Potion.blindness.getId(), 20));
         }

         /*if (property.sleepCounter == 0 && !event.player.isPlayerSleeping()) {
            event.player.sleepInBedAt((int) event.player.posX, (int) event.player.posY, (int) event.player.posZ);
         }*/
      }
   }

   @SubscribeEvent
   public void onEntityConstructing(EntityEvent.EntityConstructing event) {
      if (event.entity instanceof EntityPlayer && SleepingProperty.get((EntityPlayer) event.entity) == null)
         SleepingProperty.register((EntityPlayer) event.entity);
   }

   @SubscribeEvent
   public void onPlayerSleepInBed(PlayerSleepInBedEvent event) {
      if (Config.enableSleepCounter) {
         SleepingProperty property = SleepingProperty.get(event.entityPlayer);

         if (property.sleepCounter >= Config.maximumSleepCounter) {
            event.entityPlayer.addChatComponentMessage(new ChatComponentText("You are not tired right now."));
            event.result = EntityPlayer.EnumStatus.OTHER_PROBLEM;
         }
      }
   }
}
