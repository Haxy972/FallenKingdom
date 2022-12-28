package fr.haxy972.fallenkingdom.runnables;

import fr.haxy972.fallenkingdom.game.GameManager;
import fr.haxy972.fallenkingdom.teams.TeamManager;
import fr.haxy972.fallenkingdom.utils.TitleManager;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class DeathRunnable extends BukkitRunnable {
    private final GameManager gameManager;
    private final TeamManager teamManager;
    private int timer = 5;
    private final Player player;

    public DeathRunnable(GameManager gameManager, Player player) {
        this.gameManager = gameManager;
        this.player = player;
        this.teamManager = gameManager.getTeamManager();
    }



    @Override
    public void run() {
        if(gameManager.getTeamManager().getPlayerTeam(player) == null)this.cancel();
        if(!teamManager.getPlayerTeam(player).isAlive()){
            player.sendMessage("§cRéapparition annulé...votre royaume a été détruit");
            player.playSound(player.getLocation(), Sound.NOTE_PLING, 0.3f, 0.3f);
            gameManager.removeGamePlayer(player);
            gameManager.addSpectatorList(player);
            gameManager.setSpectatorsEffects(player);
            this.cancel();
            return;
        }
        if (timer > 0) {
            if(gameManager.getTeamManager().getPlayerTeam(player) != null) {
                TitleManager.sendActionBar(player,"§7Respawn dans §e" + getSeconds(timer));
            }
        } else {
            if(gameManager.getPlayerDataManager().getPlayerData(player) != null) {
                gameManager.spawnPlayer(player, gameManager.getTeamManager().getPlayerTeam(player));
                player.sendMessage("§aRéapparition en cours..");
                player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1f, 1f);
            }

            this.cancel();
            return;
        }
        timer--;
    }


    private String getSeconds(int timer) {
        if (timer == 1) {
            return timer + " seconde";
        } else {
            return timer + " secondes";
        }
    }
}


