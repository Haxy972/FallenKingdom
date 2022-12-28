package fr.haxy972.fallenkingdom.runnables;

import fr.haxy972.fallenkingdom.game.GameManager;
import fr.haxy972.fallenkingdom.teams.TeamManager;
import fr.haxy972.fallenkingdom.utils.TitleManager;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class WorldChangeRunnable extends BukkitRunnable implements Listener {
    private final GameManager gameManager;
    private int timer = 10;
    private Player player;

    public WorldChangeRunnable(GameManager gameManager, Player player) {
        this.gameManager = gameManager;
        this.player = player;
    }


    @Override
    public void run() {
        if(timer <= 0){
            player.teleport(gameManager.getLobbySpawn());
            player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1f,1f);
            player = null;
            this.cancel();
        }
        timer--;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event){
        Player eventPlayer = event.getPlayer();
        if(player == eventPlayer) {
            if (event.getFrom().getX() != event.getTo().getX() && event.getFrom().getZ() != (event.getTo().getZ())){
                player.sendMessage("§cVous avez bougé, téléportation annulée");
                player.playSound(player.getLocation(), Sound.NOTE_PLING, 0.3f,0.3f);
                this.player = null;
                this.cancel();
            }
        }
    }


    private String getSeconds(int timer) {
        if (timer == 1) {
            return timer + " seconde";
        } else {
            return timer + " secondes";
        }
    }
}


