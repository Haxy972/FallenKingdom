package fr.haxy972.fallen.utils;

import fr.haxy972.fallen.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import fr.haxy972.fallen.GameStatut;
import fr.haxy972.fallen.Scoreboard.ScoreboardManager;

public class RefresherLobby extends BukkitRunnable {

    @Override
    public void run() {

        if (GameStatut.isStatut(GameStatut.LOBBY)) {


            for (Player player : Bukkit.getOnlinePlayers()) {
                if (ScoreboardManager.scoreboardGame.containsKey(player)) {
                    ScoreboardManager.scoreboardGame.get(player).setLine(5, "§eConnectés: §a" + Bukkit.getOnlinePlayers().size() + "/" + Main.INSTANCE.getConfig().getInt("partie.requiredtostart"));
                }
            }
        }

    }

}
