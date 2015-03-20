package cz.ondraster.bettersleeping;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cz.ondraster.bettersleeping.api.PlayerData;
import cz.ondraster.bettersleeping.compat.CompatibilityEnviroMine;
import cz.ondraster.bettersleeping.logic.Alarm;
import cz.ondraster.bettersleeping.logic.AlternateSleep;
import cz.ondraster.bettersleeping.logic.CaffeineLogic;
import cz.ondraster.bettersleeping.logic.DebuffLogic;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.event.entity.player.PlayerUseItemEvent;

public class EventHandlers {

   public static EventHandlers INSTANCE;

   private int ticksSinceUpdate = 0;

   public EventHandlers() {
      INSTANCE = this;
   }

   @SubscribeEvent
   public void onPlayerDeath(LivingDeathEvent event) {
      if (event.entity.worldObj.isRemote)
         return;

      if (!Config.enableSleepCounter)
         return;

      if (!Config.resetCounterOnDeath)
         return;

      if (event.entity instanceof EntityPlayer) {
         EntityPlayer player = (EntityPlayer) event.entity;
         PlayerData data = BSSavedData.instance().getPlayerData(player.getUniqueID());
         data.reset(Config.spawnSleepCounter);
         BSSavedData.instance().markDirty();
      }
   }

   @SubscribeEvent
   public void onPreWorldTick(TickEvent.WorldTickEvent event) {
      if (!(event.world instanceof WorldServer))
         return;

      if (event.phase != TickEvent.Phase.START)
         return;

      WorldServer world = (WorldServer) event.world;

      if (world.areAllPlayersAsleep()) {
         Alarm.sleepWorld(world);
      }
   }

   @SubscribeEvent
   public void onPlayerTick(TickEvent.PlayerTickEvent event) {
      if (event.phase != TickEvent.Phase.START)
         return;

      PlayerData data = null;
      if (event.player.worldObj.isRemote)
         return;

      if (!event.player.isEntityAlive())
         return;

      if (Config.enableSleepCounter) {
         data = BSSavedData.instance().getData(event.player);
         data.ticksSinceUpdate++;
         double ticksPerSleepCounter = Config.ticksPerSleepCounter;

         if (event.player.isSprinting())
            ticksPerSleepCounter /= Config.multiplicatorWhenSprinting;

         if (data.ticksSinceUpdate >= ticksPerSleepCounter) {
            data.ticksSinceUpdate = 0;

            if (!event.player.capabilities.isCreativeMode)
               data.decreaseSleepCounter();

         }

         if (event.player.isPlayerSleeping() && Config.giveSleepCounterOnSleep > 0) {
            data.increaseSleepCounter(Config.giveSleepCounterOnSleep);
         }

         // send update about tiredness to the client
         DebuffLogic.updateClientIfNeeded(event.player, data);
      }

      if (data == null)
         return; // safety, should not happen except maybe some edge cases

      if (Config.enableDebuffs && Config.enableSleepCounter && ticksSinceUpdate > 20) {
         // check for debuffs
         DebuffLogic.checkForDebuffs(event, data);

         if (Config.enableCaffeine) {
            CaffeineLogic.checkDebuff(event.player);
         }

         if (Config.enviromineSanityDecrease > 0 && Loader.isModLoaded("enviromine")) {
            if (Config.enviromineSanityAt > ((float) data.getSleepCounter() / Config.maximumSleepCounter) * 100f)
               CompatibilityEnviroMine.changeSanity(event.player, Config.enviromineSanityDecrease * (-1));
         }

         ticksSinceUpdate = 0;
      }

      BSSavedData.instance().markDirty();

      ticksSinceUpdate++;
   }

   @SubscribeEvent
   public void onPlayerSleepInBed(PlayerSleepInBedEvent event) {
      if (event.entityPlayer.worldObj.isRemote)
         return;

      if (Config.disableSleeping) {
         event.entityPlayer.addChatComponentMessage(new ChatComponentTranslation("msg.sleepingDisabled"));
         event.result = EntityPlayer.EnumStatus.OTHER_PROBLEM;
         return;
      }

      if (Config.enableSleepCounter) {
         PlayerData data = BSSavedData.instance().getData(event.entityPlayer);

         if (data.getSleepCounter() >= Config.maximumSleepCounter) {
            event.entityPlayer.addChatComponentMessage(new ChatComponentTranslation("msg.notTired"));
            event.result = EntityPlayer.EnumStatus.OTHER_PROBLEM;
         }
      }

      // check for amount of people sleeping in this dimension
      AlternateSleep.trySleepingWorld(event.entityPlayer.worldObj);
   }

   @SubscribeEvent
   public void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
      if (event.player.worldObj == null)
         return;

      if (event.player.worldObj.isRemote)
         return;

      if (Config.percentPeopleToSleep > 1)
         return;

      AlternateSleep.trySleepingWorld(event.player.worldObj, true);
   }

   @SubscribeEvent
   public void onEntityJump(LivingEvent.LivingJumpEvent event) {
      if (event.entity instanceof EntityPlayer && Config.tirednessJump > 0) {
         EntityPlayer player = (EntityPlayer) event.entity;
         if (player.worldObj.isRemote)
            return;

         if (player.capabilities.isCreativeMode)
            return;

         PlayerData data = BSSavedData.instance().getData(player);
         if (player.isSprinting())
            data.decreaseSleepCounter((long) (Config.tirednessJump * Config.multiplicatorWhenSprinting));
         else
            data.decreaseSleepCounter(Config.tirednessJump);

         BSSavedData.instance().markDirty();

      }
   }

   @SubscribeEvent
   public void onPlayerUseItem(PlayerUseItemEvent.Finish event) {
      if (event.entityPlayer.worldObj.isRemote)
         return;

      if (CaffeineLogic.isCoffee(event.item)) {
         PlayerData data = BSSavedData.instance().getData(event.entityPlayer);
         if (event.result.getItem() instanceof ItemFood) {
            ItemFood food = (ItemFood) event.result.getItem();
            float hunger = food.func_150905_g(event.result);
            float saturation = food.func_150906_h(event.result);
            hunger *= Config.itemFoodHungerMult;
            saturation *= Config.itemFoodSaturationMult;
            data.increaseCaffeineLevel(hunger);
            data.increaseSleepCounter((int) saturation);
         } else {
            data.increaseCaffeineLevel(Config.caffeinePerItem);
            data.increaseSleepCounter(Config.tirednessPerCaffeine);
         }

         BSSavedData.instance().markDirty();
         // send update about tiredness to the client
         DebuffLogic.updateClientIfNeeded(event.entityPlayer, data);
      }
   }
}
