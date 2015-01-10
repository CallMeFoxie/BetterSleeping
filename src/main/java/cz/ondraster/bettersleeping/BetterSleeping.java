package cz.ondraster.bettersleeping;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cz.ondraster.bettersleeping.client.gui.SleepOverlay;
import cz.ondraster.bettersleeping.logic.Alarm;
import cz.ondraster.bettersleeping.logic.AlternateSleep;
import cz.ondraster.bettersleeping.network.MessageUpdateTiredness;
import cz.ondraster.bettersleeping.network.Network;
import cz.ondraster.bettersleeping.player.SleepingProperty;
import cz.ondraster.bettersleeping.proxy.ProxyCommon;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;

@Mod(modid = BetterSleeping.MODID, name = BetterSleeping.NAME)
public class BetterSleeping {
   public static final String MODID = "bettersleeping";
   public static final String NAME = "Better Sleeping";
   public static String AUTHOR = "OndraSter";

   private int ticksSinceLastCheck = 0;

   @SidedProxy(clientSide = "cz.ondraster.bettersleeping.proxy.ProxyClient", serverSide = "cz.ondraster.bettersleeping.proxy.ProxyCommon")
   public static ProxyCommon proxy;

   @Mod.Instance
   public static BetterSleeping INSTANCE;


   @EventHandler
   public void preinit(FMLPreInitializationEvent event) {
      proxy.preinit(event);
   }

   @EventHandler
   public void init(FMLInitializationEvent event) {
      proxy.init(event);
   }

   @SubscribeEvent
   public void onPreWorldTick(TickEvent.WorldTickEvent event) {
      if (!(event.world instanceof WorldServer))
         return;

      WorldServer world = (WorldServer) event.world;

      if (world.areAllPlayersAsleep()) {
         Alarm.sleepWorld(world);
      }

      if (ticksSinceLastCheck > 10) {
         int sleeping = AlternateSleep.getSleepingPeopleInWorld(event.world);
         if ((double) sleeping / event.world.playerEntities.size() >= Config.percentPeopleToSleep) {
            Alarm.sleepWorld(world);
         }
         ticksSinceLastCheck = 0;
      }

      ticksSinceLastCheck++;
   }

   @SubscribeEvent
   public void onPlayerTick(TickEvent.PlayerTickEvent event) {
      SleepingProperty property = null;
      if (event.player.worldObj.isRemote) {
         SleepOverlay.playerProperty = SleepingProperty.get(event.player);
         return;
      }

      if (Config.enableSleepCounter) {
         property = SleepingProperty.get(event.player);
         property.ticksSinceUpdate++;
         if (property.ticksSinceUpdate >= Config.ticksPerSleepCounter) {
            property.ticksSinceUpdate = 0;
            property.sleepCounter--;
            if (property.sleepCounter < 0)
               property.sleepCounter = 0;

         }

         if ((double) (Math.abs(property.sleepCounter - property.lastUpdate)) / Config.maximumSleepCounter > 1.0d / SleepOverlay.MAX_OFFSET && event.player instanceof EntityPlayerMP) {
            Network.networkChannel.sendTo(new MessageUpdateTiredness(property.sleepCounter), (EntityPlayerMP) event.player);
            property.lastUpdate = property.sleepCounter;
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

         if (property.sleepCounter == 0 && !event.player.isPlayerSleeping()) {
            event.player.addChatMessage(new ChatComponentTranslation("msg.tooTired"));
            event.player.sleepInBedAt((int) event.player.posX, (int) event.player.posY, (int) event.player.posZ);
         }
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
            event.entityPlayer.addChatComponentMessage(new ChatComponentTranslation("msg.notTired"));
            event.result = EntityPlayer.EnumStatus.OTHER_PROBLEM;
         }
      }
   }
}
