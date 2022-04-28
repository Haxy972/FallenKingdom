package fr.haxy972.fallen.listeners;

import fr.haxy972.fallen.runnable.LobbyRunnable;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.haxy972.fallen.GameStatut;
import fr.haxy972.fallen.Main;
import fr.haxy972.fallen.Scoreboard.ScoreboardManager;
import fr.haxy972.fallen.utils.TitleManager;

public class onPlayerJoinListener implements Listener {


    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if(!GameStatut.isStatut(GameStatut.LOBBY)){
            return;
        }

        Player player = event.getPlayer();

        if (Bukkit.getOnlinePlayers().size() > Main.INSTANCE.getConfig().getInt("partie.requiredtostart")) {

            player.kickPlayer(Main.getPrefix() + "§cServeur Plein !");
            return;

        }

        if(Main.INSTANCE.getConfig().getBoolean("grades")) {
            Main.INSTANCE.getServer().dispatchCommand(Main.INSTANCE.getServer().getConsoleSender(), "manuadd " + player.getName() + " Joueur");
        }
        event.setJoinMessage("");
        sendJoinMessage(player);
        player.getInventory().clear();
        player.setHealth(20);
        player.setLevel(-1);
        player.setFoodLevel(20);

        player.teleport(Main.getLobbySpawn());
        TitleManager.sendTitle(player, "§6FallenKingdom", "§7Bienvenue sur l'évenement", 3 * 20);
        player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_BLAST, 1.0f, 1.0f);
        if (GameStatut.isStatut(GameStatut.LOBBY)) {

            new ScoreboardManager(player).loadScoreboard();


            player.setGameMode(GameMode.ADVENTURE);
            player.setInvulnerable(true);
            giveItems(player);
        }

        if(Bukkit.getOnlinePlayers().size() == Main.INSTANCE.getConfig().getInt("partie.requiredtostart")){
                    new LobbyRunnable().runTaskTimer(Main.INSTANCE, 0, 20);

            }



    }

    private void sendJoinMessage(Player player) {
        Bukkit.broadcastMessage(Main.getPrefix() + "§b§l" + player.getName() + " §e§ rejoint l'évènement");
        player.sendMessage("§eBienvenue §b§l" + player.getName() + "§e,");
        player.sendMessage("§7Choisis ton équipe avec la laine présente dans ta barre d'items");

    }

    private void giveItems(Player player) {
        //barrier with no namer give
        ItemStack it = new ItemStack(Material.BARRIER);
        ItemMeta im = it.getItemMeta();
        im.setDisplayName("§b");
        it.setItemMeta(im);

        if(Main.INSTANCE.getConfig().getBoolean("options.barrier") == true) {
            for (int i = 0; i < 9; i++) {

                player.getInventory().setItem(i, it);
            }
        }

        //laine give
        it = new ItemStack(Material.WOOL);
        im = it.getItemMeta();
        im.setDisplayName("§b§lEquipe§8» §7Aucune");
        it.setItemMeta(im);
        player.getInventory().setItem(4, it);


    }

}
