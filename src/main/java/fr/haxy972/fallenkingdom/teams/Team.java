package fr.haxy972.fallenkingdom.teams;

import fr.haxy972.fallenkingdom.Main;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Team {

    private final String name;
    private final ChatColor color;
    private final byte colorID;
    private final Location nexusLocation;
    private boolean isAlive;
    private static int maxTeamCount;
    private final List<Player> playersList = new ArrayList<>();

    public Team(String name, ChatColor color, byte colorID, Location nexusLocation) {
        this.name = name;
        this.color = color;
        this.colorID = colorID;
        this.nexusLocation = nexusLocation;
        this.isAlive = true;
        this.maxTeamCount = Main.getInstance().getConfig().getInt("players-per-team");
    }

    public String getName() {
        return name;
    }

    public ChatColor getColor() {
        return color;
    }

    public byte getColorID() {
        return colorID;
    }

    public Location getNexusLocation() {
        return nexusLocation;
    }



    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    public void addPlayer(Player player){
        playersList.add(player);
    }

    public void removePlayer(Player player){
        playersList.remove(player);
    }

    public void removePlayerByName(String playerName){
        Player player = null;
        for (Player players : getPlayersList()) {
            if (players.getName().equals(playerName)) {
                player = players;
            }
        }
        playersList.remove(player);
    }

    public Player getPlayerByName(String name){
        for(Player players : getPlayersList()){
            if(players.getName().equals(name)){
                return players;
            }
        }
        return null;
    }

    public List<Player> getPlayersList(){
        return playersList;
    }

    public static int getMaxTeamCount() {
        return maxTeamCount;
    }

    public int getPlayersCount(){
        return playersList.size();
    }

    public Location getSpawnLocation() {
        return new Location(nexusLocation.getWorld(), nexusLocation.getX() + 0.5, nexusLocation.getY()+3, nexusLocation.getZ() + 0.5);
    }
    // TODO: Sync objects offlineplayer and player
    public int getPlayerAlive(){
        int playerAlive = 0;
        for(Player player : playersList){
            if(player != null){
                if(player.isOnline()) {
                    if (Main.getInstance().getGameManager().getPlayerList().contains(player) && !Main.getInstance().getGameManager().getSpectatorsList().contains(player)) {
                        playerAlive++;
                    }
                }
            }
        }
        return playerAlive;
    }
}
