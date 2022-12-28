package fr.haxy972.fallenkingdom.data;

import org.bukkit.entity.Player;

public class PlayerData {

    private Player player;
    private int kills = 0;
    private int deaths = 0;
    private boolean mute = false;

    public PlayerData(Player player){
        this.player = player;
    }

    public int getKills() {
        return kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public void addKill(){
        this.kills++;
    }
    public void setMute(boolean mute){
        this.mute = mute;
    }

    public boolean isMute(){
        return mute;
    }
    public void addDeath(){
        this.deaths++;
    }
    public Player getPlayer(){
        return this.player;
    }

    public void setPlayer(Player player) {
        this.player= player;
    }
}

