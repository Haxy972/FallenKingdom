package fr.haxy972.fallen.runnable;

import java.text.SimpleDateFormat;
import java.util.Date;

import fr.haxy972.fallen.utils.MessageYaml;
import org.bukkit.Bukkit;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

import org.bukkit.scheduler.BukkitRunnable;

import fr.haxy972.fallen.Main;
import fr.haxy972.fallen.Scoreboard.ScoreboardManager;
import fr.haxy972.fallen.manager.GameManager;

public class LobbyRunnable extends BukkitRunnable {

    int timer = Main.INSTANCE.getConfig().getInt("partie.timer") + 1;
    static int sizesave = Bukkit.getOnlinePlayers().size();
    public static boolean alreadytimer;
    @Override
    public void run() {
        timer--;

        setLevel(timer);

        if (sizesave != Bukkit.getOnlinePlayers().size()) {
            timer = 31;
            Bukkit.broadcastMessage(Main.getPrefix() + MessageYaml.getValue("messages.partie.lobby.start-aborted").replace("&", "§"));
            for (Player players : Bukkit.getOnlinePlayers()) {
                players.playSound(players.getLocation(), Sound.BLOCK_NOTE_PLING, 0.2f, 0.2f);
                players.setLevel(0);
                if (ScoreboardManager.scoreboardGame.containsKey(players)) {
                    ScoreboardManager.scoreboardGame.get(players).destroy();
                    ScoreboardManager.scoreboardGame.remove(players);


                }

            }
            ScoreboardManager.scoreboardGame.clear();
            for (Player players : Bukkit.getOnlinePlayers()) {
                new ScoreboardManager(players).loadScoreboard();
            }


            this.cancel();
            alreadytimer = false;
        }

        if ((timer == 30) || (timer == 20) || (timer == 10) || (timer == 5) || (timer == 3) || (timer == 2) || (timer == 1)) {
            //Bukkit.broadcastMessage(Main.getPrefix() + "§7La partie commence dans §b§l" + timer + getSeconds(timer));
            Bukkit.broadcastMessage(Main.getPrefix() + MessageYaml.getValue("messages.partie.lobby.timer-countdown").replace("&", "§").replace("{nombre}", "" + timer).replace("{secondes}", getSeconds(timer)));

            for (Player players : Bukkit.getOnlinePlayers()) {
                players.playSound(players.getLocation(), Sound.BLOCK_NOTE_PLING, 1f, 1f);

            }

        }
        if (timer == 0) {
            timer = 31;
            GameManager.startGame();
            alreadytimer = false;


            this.cancel();

        }
    }

    private void setLevel(Integer timer) {
        for (Player players : Bukkit.getOnlinePlayers()) {
            players.setLevel(timer);
            if (ScoreboardManager.scoreboardGame.containsKey(players)) {
                ScoreboardManager.scoreboardGame.get(players).setLine(3, "§eTimer: §a" + new SimpleDateFormat("mm:ss").format(new Date(timer * 1000)));
            }

        }

    }

    private String getSeconds(Integer timer) {
        if (timer == 1) {
            return "seconde";
        } else {
            return "secondes";
        }

    }

}
