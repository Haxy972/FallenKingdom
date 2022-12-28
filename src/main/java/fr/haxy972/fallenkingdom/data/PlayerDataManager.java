package fr.haxy972.fallenkingdom.data;

import fr.haxy972.fallenkingdom.teams.Team;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashMap;

public class PlayerDataManager {

    private final HashMap<Player, PlayerData> playersDataList = new HashMap<>();


    public void addPlayerData(Player player){
        playersDataList.put(player,new PlayerData(player));
    }

    public void addPlayerData(Player player, PlayerData playerData){
        playerData.setPlayer(player);
        playersDataList.put(player,playerData);
    }

    public PlayerData getPlayerData(Player player){
        return playersDataList.get(player);
    }

    public void removePlayerData(Player player){
        playersDataList.remove(player);
    }

    public Collection<PlayerData> getAllDatas() {
        return playersDataList.values();
    }

    public void removePlayerDataByName(String name) {
        for(PlayerData playerData : playersDataList.values()){
            if(playerData.getPlayer().getName().equals(name)){
                playersDataList.remove(playerData);
            }
        }
    }

    public PlayerData getPlayerDataByPlayerName(Player player) {
        for (PlayerData playerData : getAllDatas()) {
            if (playerData.getPlayer().getName().equals(player.getName())) {
                return playerData;
            }
        }
        return null;
    }
}
