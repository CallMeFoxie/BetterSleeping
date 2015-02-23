package cz.ondraster.bettersleeping;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cz.ondraster.bettersleeping.api.BetterSleepingAPI;
import cz.ondraster.bettersleeping.api.PlayerDebuff;
import cz.ondraster.bettersleeping.api.SleepingProperty;
import cz.ondraster.bettersleeping.api.WorldSleepEvent;
import cz.ondraster.bettersleeping.client.gui.SleepOverlay;
import cz.ondraster.bettersleeping.logic.Alarm;
import cz.ondraster.bettersleeping.logic.AlternateSleep;
import cz.ondraster.bettersleeping.network.MessageUpdateTiredness;
import cz.ondraster.bettersleeping.network.Network;
import cz.ondraster.bettersleeping.proxy.ProxyCommon;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;

import java.util.List;

@Mod(modid = BetterSleeping.MODID, name = BetterSleeping.NAME, version = BetterSleeping.VERSION)
public class BetterSleeping {
   public static final String MODID = "bettersleeping";
   public static final String NAME = "Better Sleeping";
   public static final String AUTHOR = "OndraSter";
   public static final String VERSION = "@VERSION@";

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

         if (event.player.isPlayerSleeping() && Config.giveSleepCounterOnSleep > 0) {
            property.sleepCounter += Config.giveSleepCounterOnSleep;
         }

         // send update about tiredness to the client
         if ((double) (Math.abs(property.sleepCounter - property.lastUpdate)) / Config.maximumSleepCounter >
               1.0d / SleepOverlay.MAX_OFFSET && event.player instanceof EntityPlayerMP) {
            Network.networkChannel
                  .sendTo(new MessageUpdateTiredness(property.sleepCounter), (EntityPlayerMP) event.player);
            property.lastUpdate = property.sleepCounter;
         }
      }

      if (property == null)
         return; // safety, should not happen except maybe some edge cases

      if (Config.enableDebuffs && Config.enableSleepCounter && ticksSinceUpdate > 20) {
         // check for debuffs
         List<PlayerDebuff> debuffs = BetterSleepingAPI.getDebuffs();
         for (PlayerDebuff debuff : debuffs) {
            if (debuff.enable && property.sleepCounter < debuff.tiredLevel) {
               double percentTired = (debuff.tiredLevel - property.sleepCounter) / (double) (debuff.tiredLevel);
               int scale = (int) Math.ceil(percentTired * debuff.maxScale) - 1;
               event.player.addPotionEffect(
                     new PotionEffect(debuff.potion.getId(), Config.POTION_DURATION, scale));
            }
         }

         // should fall asleep on the ground
         if (property.sleepCounter == 0 && !event.player.isPlayerSleeping() && Config.sleepOnGround) {
            boolean result = MinecraftForge.EVENT_BUS.post(new WorldSleepEvent.SleepOnGround(event.player));

            if (!result) {
               EntityPlayer.EnumStatus sleepResult = event.player.sleepInBedAt((int) event.player.posX, (int) event.player.posY, (int) event
                     .player.posZ);

               if (sleepResult == EntityPlayer.EnumStatus.OK) {
                  if (Config.enablePositionReset) {
                     ChunkCoordinates chunkCoordinates =
                           AlternateSleep.getSafePosition(event.player.worldObj, event.player.posX, event.player
                                 .posY, event.player.posZ);
                     event.player.setPosition(chunkCoordinates.posX + 0.5f, chunkCoordinates.posY + 0.1f, chunkCoordinates.posZ + 0.5f);
                  }

                  event.player.addChatMessage(new ChatComponentTranslation("msg.tooTired"));
               }
            }
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

      AlternateSleep.trySleepingWorld(event.entityPlayer.worldObj);
   }

   @SubscribeEvent
   public void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
      if (event.player.worldObj == null)
         return;

      if (event.player.worldObj.isRemote)
         return;

      AlternateSleep.trySleepingWorld(event.player.worldObj, true);
   }

}
