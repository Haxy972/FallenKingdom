package fr.haxy972.fallenkingdom.runnables;

import fr.haxy972.fallenkingdom.Main;
import fr.haxy972.fallenkingdom.game.Days;
import fr.haxy972.fallenkingdom.game.GameManager;
import fr.haxy972.fallenkingdom.teams.Team;
import fr.haxy972.fallenkingdom.teams.TeamManager;
import fr.haxy972.fallenkingdom.utils.ItemCreator;
import fr.haxy972.fallenkingdom.utils.TitleManager;
import net.minecraft.server.v1_8_R3.BlockState;
import net.minecraft.server.v1_8_R3.TileEntity;
import org.bukkit.*;
import org.bukkit.block.Chest;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class GameRunnable extends BukkitRunnable {

    private final GameManager gameManager;
    public int timer = 0;

    public GameRunnable(GameManager gameManager) {
        this.gameManager = gameManager;
    }


    @Override
    public void run() {
        gameManager.getScoreboardManager().updateGameTimer(timer);
        gameManager.getScoreboardManager().updateTeamAlive();
        checkWin();
        if (timer >= gameManager.getDayDuration()) {
            gameManager.addGameDay();
            timer = 0;
            Bukkit.broadcastMessage(gameManager.getPrefix() + "§bUn nouveau jour se lève nous sommes actuellement au §e§l" + gameManager.getGameDay() + " ème jour");
            playSound(Sound.LEVEL_UP, 1);
            if (gameManager.getGameDay() >= Days.PORTALS.getDay()) {
                gameManager.getNetherPortal().getBlock().setType(Material.FIRE);
                gameManager.getGameUtils().setEnderPortal(gameManager.getEnderPortal(), Material.ENDER_PORTAL);
            }
            if (Days.PVP.getDay() == gameManager.getGameDay()) {
                Bukkit.broadcastMessage("§7Une lettre vous informe que votre royaume est prêt au pillage");
                Bukkit.broadcastMessage("§6PvP §8> §aActif");
                playSound(Sound.SKELETON_HURT, 1);
            }
            if (Days.PORTALS.getDay() == gameManager.getGameDay()) {
                Bukkit.broadcastMessage("§7Un sorcier de votre royaume a fuit par une voie étrange...");
                Bukkit.broadcastMessage("§6Portails §8> §aActif  §8(§7Place Centrale§8)");
                playSound(Sound.ENDERMAN_TELEPORT, 1);
            }
            if (Days.BED.getDay() == gameManager.getGameDay()) {
                Bukkit.broadcastMessage("§7Votre défense est défaîtes,c'est votre dernière chance \n Faîtes en bonne usage !");
                Bukkit.broadcastMessage("§6Nexus §8> §cInactif");
                destroyTeamsBed();
            }
            if (Days.HUNGER.getDay() == gameManager.getGameDay()) {
                Bukkit.broadcastMessage("§7Les ressources du royaumes ce font rares, débrouillez vous !");
                Bukkit.broadcastMessage("§6Nourriture §8> §cInactif");
                playSound(Sound.EAT, 1);
            }
            if (Days.ASSAULT.getDay() == gameManager.getGameDay()) {
                Bukkit.broadcastMessage("§7Les royaumes voisins vous incitent à piller");
                Bukkit.broadcastMessage("§6Assaults §8> §aActif");
                playSound(Sound.ZOMBIE_HURT, 1);
            }
            if (Days.CHEST.getDay() == gameManager.getGameDay()) {
                Bukkit.broadcastMessage("§7Les royaumes voisins veulent vous aider");
                Bukkit.broadcastMessage("§6Les coffres sont désormais rechargés tous les jours");
                Bukkit.broadcastMessage("§b§lLes loots contenus dans ces coffres sont évolutifs");
            }
        }
        setDayCycle();
        checkRefillChest();
        gameManager.getScoreboardManager().updateGameDayCount(gameManager.getGameDay());

        timer++;
    }


    private void checkWin() {
        TeamManager teamManager = gameManager.getTeamManager();
        Team winner = null;
        for (Team team : teamManager.getTeamList()) {
            if (!team.isAlive()) {
                if (team.getPlayerAlive() != 0) {
                    if (winner == null) {
                        winner = team;
                    } else {
                        return;
                    }
                }
            } else {
                if (winner == null) {
                    winner = team;
                } else {
                    return;
                }
            }
        }

        if (winner == null) {
            Main.getInstance().getServer().reload();
        } else {
            gameManager.endGame(winner);
        }
        this.cancel();
    }

    private void setDayCycle() {
        if (gameManager.getGameDay() % 2 == 1) {
            gameManager.getWorld().setTime(1000);
        } else {
            gameManager.getWorld().setTime(13000);
        }

    }

    public void checkRefillChest() {
        if (gameManager.getGameDay() >= Days.CHEST.getDay()) {
            List<ItemStack> itemsList = getChestItemsList(gameManager.getGameDay());
            Random random = new Random();
            for (Chest chests : gameManager.getChestGame()) {
                Inventory chestInventory = chests.getInventory();
                chestInventory.clear();

                for (int i = 0; i <= 8; i++) {
                    int randint = random.nextInt(itemsList.size());
                    int slot = random.nextInt(chestInventory.getSize());
                    chestInventory.setItem(slot, itemsList.get(randint));
                }
            }

        }

    }

    public List<ItemStack> getChestItemsList(int day) {
        Map<ItemStack, Integer> mapItems = new HashMap<>();

        mapItems.put(new ItemCreator(Material.COBBLESTONE, 32).done(), 7);
        mapItems.put(new ItemCreator(Material.SAND, 16).done(), 5);
        mapItems.put(new ItemCreator(Material.IRON_SWORD, 1).done(), 3);
        mapItems.put(new ItemCreator(Material.COOKED_BEEF, 8).done(), 4);
        mapItems.put(new ItemCreator(Material.BREAD, 8).done(), 8);
        mapItems.put(new ItemCreator(Material.ARROW, 16).done(), 7);
        mapItems.put(new ItemCreator(Material.BOW, 1).done(), 3);
        mapItems.put(new ItemCreator(Material.AIR, 1).done(), 8);

        if (day >= 3) {
            mapItems.put(new ItemCreator(Material.DIAMOND_SWORD, 1).done(), 1);
            mapItems.put(new ItemCreator(Material.GOLDEN_APPLE, 2).done(), 1);
            mapItems.put(new ItemCreator(Material.LAVA_BUCKET, 1).done(), 2);
            mapItems.put(new ItemCreator(Material.WATER_BUCKET, 1).done(), 3);
            mapItems.put(new ItemCreator(Material.DIAMOND_CHESTPLATE, 1).done(), 1);
            mapItems.put(new ItemCreator(Material.DIAMOND_BOOTS, 1).done(), 1);
            mapItems.put(new ItemCreator(Material.TORCH, 16).done(), 8);
            mapItems.put(gameManager.getGameUtils().getPlayerHead("benbilal", "§cTête de Snoob", "§eLe Bouffon du Roi", "§7N'a aucune utilité particulière"), 1);
        }
        if (day >= 5) {
            mapItems.put(new ItemCreator(Material.TNT, 1).done(), 2);
            mapItems.put(new ItemCreator(Material.GOLDEN_APPLE, 2).done(), 3);
            mapItems.put(new ItemCreator(Material.IRON_SWORD, 1).setEnchant(Enchantment.FIRE_ASPECT, 1).done(), 1);
            mapItems.put(new ItemCreator(Material.IRON_SWORD, 1).setEnchant(Enchantment.DAMAGE_ALL, 1).done(), 1);
        }
        if (day >= 7) {
            mapItems.put(new ItemCreator(Material.TNT, 2).done(), 4);
            mapItems.put(new ItemCreator(Material.GOLDEN_APPLE, 4).done(), 4);
        }


        return setProbrability(mapItems);
    }

    private List<ItemStack> setProbrability(Map<ItemStack, Integer> mapItems) {
        List<ItemStack> itemsList = new ArrayList<>();
        for (ItemStack items : mapItems.keySet()) {
            for (int i = 0; i < mapItems.get(items); i++) {
                itemsList.add(items);
            }
        }
        return itemsList;

    }

    private void destroyTeamsBed() {
        for (Team teams : gameManager.getTeamManager().getTeams()) {
            gameManager.getTeamManager().killTeam(teams, null, true);
        }
    }

    public void playSound(Sound sound, int level) {
        for (Player players : Bukkit.getOnlinePlayers()) {
            players.playSound(players.getLocation(), sound, level, level);
        }
    }
}
