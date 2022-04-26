package fr.haxy972.fallen.runnable;

import fr.haxy972.fallen.Main;
import fr.haxy972.fallen.Scoreboard.ScoreboardManager;
import fr.haxy972.fallen.listeners.ResetListeners;
import fr.haxy972.fallen.utils.TitleManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class restartRunnable extends BukkitRunnable {

    int seconds = 6;

    @Override
    public void run() {
        seconds --;

        if(seconds != 0) {
            Bukkit.broadcastMessage(Main.getPrefix() + "§7La partie redémarre dans §c" + seconds + getSeconds(seconds));
            for(Player players : Bukkit.getOnlinePlayers()){
                players.setLevel(seconds);
                TitleManager.sendTitle(players, "§c§lRedémarrage", "§7La partie redémarre dans §e" + seconds + getSeconds(seconds), 20);
                TitleManager.sendActionBar(players, "§eLa partie redémarre dans " + seconds + getSeconds(seconds));
            }
        }else{
            Bukkit.broadcastMessage(Main.getPrefix() + "§aLa partie redémarre dans un instant...");
        }

        if(seconds == 0){
            ScoreboardManager.scoreboardGame.clear();
            Main.kickAll();
            ResetListeners.reloadBlocks();
            Bukkit.reload();
            this.cancel();
        }
    }

    private String getSeconds(int seconds) {
        if (seconds == 1 ||seconds == 0){
            return " seconde";
        }else{
            return " secondes";
        }
    }
}
