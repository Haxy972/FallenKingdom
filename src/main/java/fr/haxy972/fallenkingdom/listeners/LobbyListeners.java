package fr.haxy972.fallenkingdom.listeners;

import fr.haxy972.fallenkingdom.Main;
import fr.haxy972.fallenkingdom.data.PlayerDataManager;
import fr.haxy972.fallenkingdom.game.GameManager;
import fr.haxy972.fallenkingdom.game.GameStatut;
import fr.haxy972.fallenkingdom.game.ScoreboardManager;
import fr.haxy972.fallenkingdom.runnables.LobbyTimerRunnable;
import fr.haxy972.fallenkingdom.teams.Team;
import fr.haxy972.fallenkingdom.teams.TeamInvRunnable;
import fr.haxy972.fallenkingdom.teams.TeamManager;
import fr.haxy972.fallenkingdom.utils.ItemCreator;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

public class LobbyListeners implements Listener {

    private final PlayerDataManager playerDataManager;
    private final GameManager gameManager;
    private LobbyTimerRunnable lobbyTimerRunnable;

    public LobbyListeners(GameManager gameManager) {
        this.gameManager = gameManager;
        this.playerDataManager = gameManager.getPlayerDataManager();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (!gameManager.isGameStatut(GameStatut.LOBBY)) return;
        Player player = event.getPlayer();

        for (Player players : Bukkit.getOnlinePlayers()) {
            players.showPlayer(player);
            player.showPlayer(players);
        }
        player.getInventory().clear();
        playerDataManager.addPlayerData(player);
        player.getInventory().setArmorContents(null);
        player.setHealth(player.getMaxHealth());
        player.setFoodLevel(20);
        player.setMaxHealth(2);
        player.setAllowFlight(false);
        player.setLevel(0);
        for (PotionEffect potionEffect : player.getActivePotionEffects()) {
            player.removePotionEffect(potionEffect.getType());
        }
        player.setGameMode(GameMode.ADVENTURE);
        player.teleport(gameManager.getLobbySpawn());
        gameManager.addGamePlayer(player);
        gameManager.getScoreboardManager().loadLobbyScoreboard(player);
        Inventory inventory = player.getInventory();
        Bukkit.broadcastMessage("§e" + player.getName() + " §7a rejoint la partie §8(§a" + gameManager.getPlayerList().size() + "§8/§a" + Bukkit.getMaxPlayers() + "§8)");
        player.getInventory().setItem(0, new ItemCreator(Material.WOOL).setName("§eChoisir une équipe").setLores("§a§l> §fCliquez pour choisir une équipe").done());
        player.getInventory().setItem(4, gameManager.getGameUtils().getPlayerHead(player.getName(), "§bStatistiques", "§a§l> §fCliquez pour voir vos statistiques"));
        if (player.isOp()) {
            player.getInventory().setItem(8, new ItemCreator(Material.BLAZE_POWDER).setName("§6Lanceur").setLores("§a§l> §fCliquez pour lancer la partie").done());
        }
        gameManager.getScoreboardManager().updateLobbyCount(player);
        player.sendMessage("§7Bienvenue sur le serveur évènement");
        player.playSound(player.getLocation(), Sound.LEVEL_UP, 1f, 1f);
        Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
            @Override
            public void run() {
                player.sendMessage("§eDéveloppé par §bLéo §8(§7Haxy972§8)");
                player.playSound(player.getLocation(), Sound.NOTE_PLING, 2f, 2f);
            }
        }, 20);
    }


    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (!gameManager.isGameStatut(GameStatut.LOBBY)) return;
        Player player = event.getPlayer();
        ItemStack item = event.getItem();


        if (!player.getGameMode().equals(GameMode.ADVENTURE)) return;
        Block block = event.getClickedBlock();
        if (block != null) {
            event.setCancelled(true);
        }

        if (event.getItem() == null) return;
        switch (item.getType()) {
            case WOOL:
                player.openInventory(gameManager.getTeamManager().getTeamInventory().getInventory());
                new TeamInvRunnable(player, gameManager.getTeamManager()).runTaskTimer(Main.getInstance(), 0, 1);
                break;
            case SKULL_ITEM:
                player.sendMessage("§d§oBientôt...");
                break;
            case BLAZE_POWDER:
                if (!gameManager.timerIsRunning) {
                    gameManager.timerIsRunning = true;
                    lobbyTimerRunnable = new LobbyTimerRunnable(gameManager);
                    lobbyTimerRunnable.runTaskTimer(Main.getInstance(), 0, 20);
                } else {
                    gameManager.timerIsRunning = false;
                    lobbyTimerRunnable.cancel();
                    lobbyTimerRunnable.resetLevelToDefault();
                    Bukkit.broadcastMessage("§bFallen§9Kingdom§8> §cLancement annulé par l'organisateur");
                    for (Player players : Bukkit.getOnlinePlayers()) {
                        players.playSound(players.getLocation(), Sound.NOTE_PLING, 0.3f, 0.3f);
                    }
                }
                break;
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!gameManager.isGameStatut(GameStatut.LOBBY)) return;
        Player player = (Player) event.getWhoClicked();
        ItemStack item = event.getCurrentItem();
        if (player.getGameMode().equals(GameMode.CREATIVE)) return;
        event.setCancelled(true);
        TeamManager teamManager = gameManager.getTeamManager();

        if (player.getOpenInventory().getTitle().equals(teamManager.getTeamInventory().getTitle())) {
            if (item == null || item.getType().equals(Material.AIR)) return;
            for (Team teams : gameManager.getTeamManager().getTeams()) {
                if (teams.getColorID() == item.getData().getData()) {
                    Team playerTeam = teamManager.getPlayerTeam(player);
                    if (playerTeam != null) {
                        playerTeam.removePlayer(player);
                    }
                    teams.addPlayer(player);
                    player.getInventory().setItem(0, new ItemCreator(Material.WOOL, 1, teams.getColorID()).setName("§eChoisir une équipe §8> " + teams.getColor() + teams.getName()).setLores("§a§l> §fCliquez pour choisir une équipe").done());
                    return;
                }
            }
            if (item.getItemMeta().getDisplayName().contains("Aucune")) {
                Team playerTeam = teamManager.getPlayerTeam(player);
                if (playerTeam != null) {
                    playerTeam.removePlayer(player);
                    player.sendMessage("§aVous avez quitté votre équipe");
                    player.closeInventory();
                    player.getInventory().setItem(0, new ItemCreator(Material.WOOL).setName("§eChoisir une équipe").setLores("§a§l> §fCliquez pour choisir une équipe").done());
                } else {
                    player.sendMessage("§cVous n'avez pas d'équipe actuellement");
                }
            }
        }

    }


    @EventHandler
    public void onFood(FoodLevelChangeEvent event) {
        if (!gameManager.isGameStatut(GameStatut.LOBBY)) return;
        Player player = (Player) event.getEntity();
        event.setCancelled(true);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!gameManager.isGameStatut(GameStatut.LOBBY)) return;
        if (!(event.getEntity() instanceof Player)) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        if (!gameManager.isGameStatut(GameStatut.LOBBY)) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onItemPickup(PlayerPickupItemEvent event) {
        if (!gameManager.isGameStatut(GameStatut.LOBBY)) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        if (!gameManager.isGameStatut(GameStatut.LOBBY)) return;
        Player player = event.getPlayer();
        gameManager.removeGamePlayer(player);
        playerDataManager.removePlayerData(player);
        gameManager.getScoreboardManager().updateLobbyCount(player);
        Bukkit.broadcastMessage("§e" + player.getName() + " §7a quitté la partie §8(§c" + gameManager.getPlayerList().size() + "§8/§c" + Bukkit.getMaxPlayers() + "§8)");
        if (gameManager.getTeamManager().getPlayerTeam(player) != null) {
            gameManager.getTeamManager().getPlayerTeam(player).removePlayer(player);
        }
    }


}
