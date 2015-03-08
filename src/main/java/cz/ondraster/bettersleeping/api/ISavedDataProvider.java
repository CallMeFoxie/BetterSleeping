package cz.ondraster.bettersleeping.api;

import java.util.UUID;

public interface ISavedDataProvider {
   public PlayerData getPlayerData(UUID uuid);
}
