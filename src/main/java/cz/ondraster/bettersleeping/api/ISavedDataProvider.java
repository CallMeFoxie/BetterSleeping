package cz.ondraster.bettersleeping.api;

import java.util.UUID;

public interface ISavedDataProvider {
   PlayerData getPlayerData(UUID uuid);
}
