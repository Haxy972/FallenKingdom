package fr.haxy972.fallenkingdom.listeners;

import fr.haxy972.fallenkingdom.Main;
import fr.haxy972.fallenkingdom.game.GameManager;
import fr.haxy972.fallenkingdom.game.GameStatut;
import fr.haxy972.fallenkingdom.runnables.SneakRunnable;
import fr.haxy972.fallenkingdom.spectator.SpectatorInventory;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class SpectorListener implements Listener {

    private final GameManager gameManager;
    private static final HashMap<Player, Integer> sneakCount = new HashMap<>();
    private final SpectatorInventory spectatorInventory;

    public SpectorListener(GameManager gameManager) {
        this.gameManager = gameManager;
        spectatorInventory = new SpectatorInventory(gameManager);
    }


    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (gameManager.isGameStatut(GameStatut.LOBBY)) return;
        Player player = (Player) event.getEntity();

        if (gameManager.getSpectatorsList().contains(player)) event.setCancelled(true);
    }

    @EventHandler
    public void onItemPickup(PlayerPickupItemEvent event) {
        if (gameManager.isGameStatut(GameStatut.LOBBY)) return;
        Player player = event.getPlayer();
        if (gameManager.getSpectatorsList().contains(player)) event.setCancelled(true);

    }

    @EventHandler
    public void onDropItem(PlayerDropItemEvent event) {
        if (gameManager.isGameStatut(GameStatut.LOBBY)) return;
        Player player = event.getPlayer();
        if (gameManager.getSpectatorsList().contains(player)) event.setCancelled(true);
    }


    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (gameManager.isGameStatut(GameStatut.LOBBY)) return;
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        Action action = event.getAction();

        if (item == null && action != null) return;

        if (gameManager.getSpectatorsList().contains(player)) {
            switch (item.getType()) {
                case NETHER_STAR:
                    if (player.getGameMode().equals(GameMode.ADVENTURE)) {
                        player.setGameMode(GameMode.SPECTATOR);
                        new SneakRunnable(player).runTaskTimer(Main.getInstance(), 0, 5);
                    }

                    break;
                case BLAZE_POWDER:
                    boolean canSee = false;
                    if (gameManager.getSpectatorsList().size() == 1) {
                        player.sendMessage("§cVous êtes le seul spectateur présent");
                        return;
                    }
                    for (OfflinePlayer spectators : gameManager.getSpectatorsList()) {
                        Player spectator = (Player) spectators;
                        if (player != spectators) {
                            if (player.canSee(spectator)) {
                                canSee = true;
                            }
                        }
                    }
                    for (OfflinePlayer spectators : gameManager.getSpectatorsList()) {
                        Player spectator = (Player) spectators;
                        if (canSee) {
                            player.hidePlayer(spectator);
                        } else {
                            player.showPlayer(spectator);
                        }
                    }
                    player.sendMessage(canSee ? "§eVous avez caché les autres spectateurs" : "§aVous avez affiché les autres spectateurs");
                    break;
                case COMPASS:
                    player.openInventory(spectatorInventory.getInventory());
                    break;
                default:
                    player.sendMessage("§d§oBientôt");
                    break;

            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (event.getCurrentItem() == null) return;
        if (gameManager.getSpectatorsList().contains(player)) {
            event.setCancelled(true);
            if (event.getInventory().getTitle().equals(spectatorInventory.getTitle())) {
                ItemStack item = event.getCurrentItem();
                for (Player players : Bukkit.getOnlinePlayers()) {
                    if(item.hasItemMeta()) {
                        if (players.getName().equals(item.getItemMeta().getDisplayName().replace("§b", ""))) {
                            player.teleport(players);
                            player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1f, 1f);
                        }
                    }
                }
            }

        }
    }

    @EventHandler
    public void onFood(FoodLevelChangeEvent event){
        Player player = (Player) event.getEntity();
        if (gameManager.getSpectatorsList().contains(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onSneakEvent(PlayerToggleSneakEvent event) {
        // Switching player GameMode on double sneak
        Player player = event.getPlayer();
        if (gameManager.getSpectatorsList().contains(player)) {
            if (player.getGameMode().equals(GameMode.SPECTATOR)) {
                if (!sneakCount.containsKey(player)) {
                    sneakCount.put(player, 1);
                } else {
                    sneakCount.replace(player, sneakCount.get(player) + 1);
                }
                if (sneakCount.get(player) >= 3) {
                    if (player.getLocation().getBlock().getType() == Material.AIR) {
                        gameManager.setSpectatorsEffects(player);
                        player.sendMessage("§aVous avez quitté le mode spectateur");
                        player.playSound(player.getLocation(), Sound.NOTE_PLING, 3f, 3f);
                        sneakCount.remove(player);
                    } else {
                        player.sendMessage("§cVous êtes actuellement dans un bloc, spawn impossible");
                        player.playSound(player.getLocation(), Sound.NOTE_PLING, 0.3f, 0.3f);
                        sneakCount.remove(player);
                    }
                }
                // This part of code is to make a delay
                Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
                    @Override
                    public void run() {
                        if (sneakCount.containsKey(player)) {
                            if (sneakCount.get(player) > 1) {
                                sneakCount.replace(player, sneakCount.get(player) - 1);
                            } else {
                                sneakCount.remove(player);
                            }
                        }
                    }
                }, 5);
            }
        }
    }

}
