package fr.haxy972.fallenkingdom.listeners;

import fr.haxy972.fallenkingdom.Main;
import fr.haxy972.fallenkingdom.game.GameManager;
import fr.haxy972.fallenkingdom.game.GameStatut;
import fr.haxy972.fallenkingdom.runnables.DeathRunnable;
import fr.haxy972.fallenkingdom.teams.Team;
import fr.haxy972.fallenkingdom.utils.TitleManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.weather.WeatherChangeEvent;

public class GlobalListener implements Listener {

    private final GameManager gameManager;

    public GlobalListener(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        event.setJoinMessage("");
        Player player = event.getPlayer();
        TitleManager.setPlayerList(player, gameManager.getPrefix().replace("§8> ","" + "\n"), "\n§eBon jeu sur notre serveur");

    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        Team team = gameManager.getTeamManager().getPlayerTeam(player);
        event.setCancelled(true);
        if(!gameManager.getPlayerDataManager().getPlayerData(player).isMute()) {
            if (team != null && gameManager.isGameStatut(GameStatut.GAME)) {
                if (event.getMessage().startsWith("@") || team.getPlayerAlive() <= 1) {
                    StringBuilder messageBuilder = new StringBuilder(event.getMessage());
                    Bukkit.broadcastMessage((player.isOp() ? "§6§lHOST " : "") + team.getColor() + team.getName() + " " + player.getName() + "§8> §b@§f" + (team.getPlayerAlive() <= 1 ? messageBuilder : messageBuilder.deleteCharAt(0)));
                } else {
                    for (Player players : team.getPlayersList()) {
                        players.sendMessage("§8§o(§7§oEquipe§8§o) " + (player.isOp() ? "§6§lHOST " : "") + team.getColor() + team.getName() + " " + player.getName() + "§8> §f" + event.getMessage());
                    }
                }
            } else {
                if(gameManager.getSpectatorsList().contains(player) || gameManager.isGameStatut(GameStatut.END)) {
                    for(Player players : Bukkit.getOnlinePlayers()){
                        if(gameManager.getSpectatorsList().contains(players)){
                            players.sendMessage("§8§o(§7§oSpectateur§8§o) " + (player.isOp() ? "§6§lHOST " : "") + "§7" + (team == null ? "" : team.getColor()) + player.getName() + "§8> §f" + event.getMessage());
                        }
                    }
                }else{
                    Bukkit.broadcastMessage((player.isOp() ? "§6§lHOST " : "") + "§7" + player.getName() + "§8> §f" + event.getMessage());
                }
            }
        }else{
            player.sendMessage("§cVous avez été mute, il est impossible de parler actuellement");
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event){
        event.setDeathMessage("");
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event){
        Player player = event.getPlayer();
        World fromWorld = event.getFrom();
        World toWorld = player.getWorld();
        player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1f,1f);
        if(!toWorld.getName().equals(gameManager.getWorld().getName())) {
            if(!gameManager.getSpectatorsList().contains(player)) {
                for (Player players : Bukkit.getOnlinePlayers()) {
                    players.playSound(players.getLocation(), Sound.AMBIENCE_CAVE, 1f, 1f);
                    players.sendMessage("§eUn joueur a emprunté une voie étrange §8(§6PORTAILS§8)");
                    if(toWorld.getName().contains("nether")){
                        gameManager.getPlayerRunnable().addPlayerNetherPortal(player, player.getLocation());
                    }
                }
            }
            if (toWorld.getName().contains("end")) {
                player.sendMessage("\n§7Vous pouvez rentrer dans le monde des royaumes en faisant §e§o\"/retour\"");
            }
        }else{
            if(fromWorld.getName().contains("nether")) {
                gameManager.getPlayerRunnable().removePlayerNetherPortal(player);
            }
        }
    }
    @EventHandler
    public void onWeatherChange (WeatherChangeEvent event){
        event.setCancelled(true);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event){
        Player player = event.getPlayer();
        if(player.getLocation().getY() < -10)
            if(player.getWorld().equals(gameManager.getWorld())) {
                if (gameManager.isGameStatut(GameStatut.GAME)) {
                    TitleManager.sendTitle(player, "§cMort", "§7Vous allez bientôt réapparaître", 20);
                    player.setGameMode(GameMode.SPECTATOR);
                    new DeathRunnable(gameManager, player).runTaskTimer(Main.getInstance(), 0,20);
                }
                player.teleport(gameManager.getLobbySpawn());
            }
    }


    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        Player player = event.getPlayer();
        event.setQuitMessage("");
        if(gameManager.getSpectatorsList().contains(player)) gameManager.removeSpectatorList(player);
    }
}
