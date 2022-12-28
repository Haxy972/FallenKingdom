package fr.haxy972.fallenkingdom.runnables;

import fr.haxy972.fallenkingdom.game.GameManager;
import fr.haxy972.fallenkingdom.game.ScoreboardManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LobbyTimerRunnable extends BukkitRunnable {

    private final ScoreboardManager scoreboardManager;
    private int timer = 3; //TODO Set good timer value
    private final int init_timer = timer;
    private final GameManager gameManager;
    private final List<Integer> timer_list = new ArrayList<>();

    public LobbyTimerRunnable(GameManager gameManager) {
        addTimerList(60, 30, 20, 10, 5, 4, 3, 2, 1);
        this.gameManager = gameManager;
        this.scoreboardManager = gameManager.getScoreboardManager();
    }


    @Override
    public void run() {
        for (Player players : Bukkit.getOnlinePlayers()) {
            players.setExp((float) timer / init_timer);
            players.setLevel(timer);
        }

        scoreboardManager.updateLobbyTimer(timer);
        if (timer_list.contains(timer)) {
            if(gameManager.timerIsRunning) {
                Bukkit.broadcastMessage(gameManager.getPrefix() + "§eLa partie commence dans §b" + getSecondes(timer));
                playSound(Sound.NOTE_PLING);
            }
        } else if (timer == 0) {
            if(gameManager.timerIsRunning) {
                Bukkit.broadcastMessage(gameManager.getPrefix() + "§eLa partie commence, à vos pioches !");
                for (OfflinePlayer players : gameManager.getPlayerList()) {
                    Player player = (Player) players;
                    player.closeInventory();
                }
                gameManager.launchGame();
                playSound(Sound.LEVEL_UP);
                gameManager.timerIsRunning = false;
                this.cancel();
            }
        }
        timer--;
    }



    private void addTimerList(Integer... integers) {
        timer_list.addAll(Arrays.asList(integers));
    }

    private void playSound(Sound sound) {
        for (Player players : Bukkit.getOnlinePlayers()) {
            players.playSound(players.getLocation(), sound, 1f, 1f);
        }
    }

    private String getSecondes(int timer) {
        if (timer == 1) return timer + " seconde";
        int count_of_minute = 0;
        while (timer >= 60) {
            timer -= 60;
            count_of_minute++;
        }
        return (count_of_minute == 1 ? count_of_minute + " minute " : (count_of_minute == 0 ? "" : count_of_minute + "mins ")) + (timer > 0 ? timer + " secondes" : "");
    }

    public void resetLevelToDefault(){
        for (OfflinePlayer players : gameManager.getPlayerList()) {
            Player player = (Player) players;
            player.setExp(0);
            player.setLevel(0);
        }
    }
}
