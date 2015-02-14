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
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;

@Mod(modid = BetterSleeping.MODID, name = BetterSleeping.NAME, version = BetterSleeping.VERSION)
public class BetterSleeping {
   public static final String MODID = "bettersleeping";
   public static final String NAME = "Better Sleeping";
   public static final String AUTHOR = "OndraSter";
   public static final String VERSION = "BS_1710";

   @SidedProxy(clientSide = "cz.ondraster.bettersleeping.proxy.ProxyClient", serverSide = "cz.ondraster.bettersleeping.proxy.ProxyCommon")
   public static ProxyCommon proxy;

   @Mod.Instance
   public static BetterSleeping INSTANCE;

   private int ticksSinceUpdate = 0;


   @EventHandler
   public void preinit(FMLPreInitializationEvent event) {
      Config c = new Config(event.getSuggestedConfigurationFile().getAbsolutePath());
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

         // send update about tiredness to the client
         if ((double) (Math.abs(property.sleepCounter - property.lastUpdate)) / Config.maximumSleepCounter >
               1.0d / SleepOverlay.MAX_OFFSET && event.player instanceof EntityPlayerMP) {
            Network.networkChannel
                  .sendTo(new MessageUpdateTiredness(property.sleepCounter), (EntityPlayerMP) event.player);
            property.lastUpdate = property.sleepCounter;
         }
      }

      if (Config.enableDebuffs && Config.enableSleepCounter && ticksSinceUpdate > 20) {
         // check for debuffs
         for (int i = 0; i < Config.debuffs.length; i++) {
            if (Config.debuffs[i].enable && property.sleepCounter <= Config.debuffs[i].tiredLevel) {
               if (event.player.getActivePotionEffect(Config.debuffs[i].potion) == null) {
                  int scale =
                        (int) ((Config.debuffs[i].tiredLevel - property.sleepCounter) / (Config.debuffs[i].tiredLevel) *
                              ((1 - Config.debuffs[i].maxScale) / Config.debuffs[i].maxScale));
                  // TODO fix this maths, it is 100% broken but my brain is offline.
                  event.player.addPotionEffect(
                        new PotionEffect(Config.debuffs[i].potion.getId(), Config.POTION_DURATION, scale));
               }
            }
         }

         // should fall asleep on the ground
         if (property.sleepCounter == 0 && !event.player.isPlayerSleeping() && Config.sleepOnGround) {
            event.player.addChatMessage(new ChatComponentTranslation("msg.tooTired"));
            event.player.sleepInBedAt((int) event.player.posX, (int) event.player.posY, (int) event.player.posZ);
         }

         ticksSinceUpdate = 0;
      }

      ticksSinceUpdate++;
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


      // check for amount of people sleeping in this dimension
      if (event.entityPlayer.worldObj.isRemote)
         return;

      int sleeping = AlternateSleep.getSleepingPeopleInWorld(event.entityPlayer.worldObj);
      if ((double) sleeping / event.entityPlayer.worldObj.playerEntities.size() >= Config.percentPeopleToSleep) {
         Alarm.sleepWorld(event.entityPlayer.worldObj);
      }
   }
}
