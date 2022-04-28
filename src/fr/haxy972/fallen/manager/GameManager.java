package fr.haxy972.fallen.manager;

import java.util.ArrayList;
import fr.haxy972.fallen.utils.MessageYaml;
import org.bukkit.*;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;

import fr.haxy972.fallen.GameStatut;
import fr.haxy972.fallen.Main;
import fr.haxy972.fallen.Scoreboard.ScoreboardManager;
import fr.haxy972.fallen.Team.select.TeamSelect;
import fr.haxy972.fallen.runnable.daysRunnable;
import fr.haxy972.fallen.utils.TitleManager;


public class GameManager {
    public static ArrayList<Location> chestsloc = new ArrayList<>();

    public static void startGame() {
        GameStatut.setStatut(GameStatut.GAME);
        for (Player players : Bukkit.getOnlinePlayers()) {
            players.setGameMode(GameMode.SURVIVAL);
            players.setLevel(0);
            players.setFoodLevel(20);
            players.setHealth(20);
            if (ScoreboardManager.scoreboardGame.containsKey(players)) {
                ScoreboardManager.scoreboardGame.get(players).destroy();
                ScoreboardManager.scoreboardGame.remove(players);
            }
            daysRunnable.killCount.put(players, 0);
            daysRunnable.deathCount.put(players, 0);
            if (TeamSelect.team.containsKey(players)) {

                players.playSound(players.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
                if (TeamSelect.team.get(players).equalsIgnoreCase("rouge")) {
                    players.teleport(Main.getSpawnRed());
                    if(Main.INSTANCE.getConfig().getBoolean("grades")) {
                        Main.INSTANCE.getServer().dispatchCommand(Main.INSTANCE.getServer().getConsoleSender(), "manuadd " + players.getName() + " rouge");
                    }
                }
                if (TeamSelect.team.get(players).equalsIgnoreCase("bleu")) {
                    players.teleport(Main.getSpawnBlue());
                    if(Main.INSTANCE.getConfig().getBoolean("grades")) {
                        Main.INSTANCE.getServer().dispatchCommand(Main.INSTANCE.getServer().getConsoleSender(), "manuadd " + players.getName() + " bleu");
                    }
                }
                if (TeamSelect.team.get(players).equalsIgnoreCase("vert")) {
                    players.teleport(Main.getSpawnGreen());
                    if(Main.INSTANCE.getConfig().getBoolean("grades")) {
                        Main.INSTANCE.getServer().dispatchCommand(Main.INSTANCE.getServer().getConsoleSender(), "manuadd " + players.getName() + " vert");
                    }
                }
                if (TeamSelect.team.get(players).equalsIgnoreCase("jaune")) {
                    players.teleport(Main.getSpawnYellow());
                    if(Main.INSTANCE.getConfig().getBoolean("grades")) {
                        Main.INSTANCE.getServer().dispatchCommand(Main.INSTANCE.getServer().getConsoleSender(), "manuadd " + players.getName() + " jaune");
                    }
                }
                players.setInvulnerable(false);
                players.setHealth(20);
                players.setFoodLevel(20);
                players.getInventory().clear();
                TitleManager.sendTitle(players, MessageYaml.getValue("messages.partie.start-title").replace("&", "§"), MessageYaml.getValue("messages.partie.start-subtitle").replace("&", "§"), 5 * 20);
            } else {
                players.teleport(Main.getLobbySpawn());
                players.sendMessage(Main.getPrefix() + MessageYaml.getValue("messages.team.default-team").replace("&", "§"));
                players.setGameMode(GameMode.SPECTATOR);

            }
            for(Chunk chunk : Bukkit.getWorld(Main.getWorld().getName()).getLoadedChunks()){
                for(Entity e : chunk.getEntities()){
                    if(e instanceof Item){
                        e.remove();

                    }
                }
            }
            Bukkit.getScheduler().runTaskLater(Main.INSTANCE, () -> {
                new ScoreboardManager(players).loadScoreboardGame();
                checkForBeacon();
                for(Chunk chunk : Main.getWorld().getLoadedChunks()){
                    for(BlockState bs : chunk.getTileEntities()){
                        if(bs instanceof Chest){
                            Chest chest = (Chest) bs;
                            chest.getInventory().clear();
                            if(!chestsloc.contains(bs.getLocation())) {
                                chestsloc.add(bs.getLocation());
                            }

                        }
                    }
                }

            }, 20);


        }




        Bukkit.getWorld(Main.getWorld().getName()).setTime(1000);

        new daysRunnable().runTaskTimer(Main.INSTANCE, 0, 20);

    }




    private static void checkForBeacon() {

        Location brouge = Main.getBeaconRed();
        Location bbleu = Main.getBeaconBlue();
        Location bvert = Main.getBeaconGreen();
        Location byellow = Main.getBeaconYellow();

        brouge.getBlock().setType(Material.BEACON);
        bbleu.getBlock().setType(Material.BEACON);
        bvert.getBlock().setType(Material.BEACON);
        byellow.getBlock().setType(Material.BEACON);
        //BLEU

        int i = 0;
        for (Player players : Bukkit.getOnlinePlayers()) {
            if (TeamSelect.team.containsKey(players)) {
                if (TeamSelect.team.get(players).equalsIgnoreCase("bleu")) {
                    i++;
                }
            }
        }
        if (i == 0) {
            bbleu.getBlock().setType(Material.AIR);
        }
        //ROUGE
        i = 0;
        for (Player players : Bukkit.getOnlinePlayers()) {
            if (TeamSelect.team.containsKey(players)) {
                if (TeamSelect.team.get(players).equalsIgnoreCase("rouge")) {
                    i++;
                }
            }
        }
        if (i == 0) {
            brouge.getBlock().setType(Material.AIR);
        }
        //VERT
        i = 0;
        for (Player players : Bukkit.getOnlinePlayers()) {
            if (TeamSelect.team.containsKey(players)) {
                if (TeamSelect.team.get(players).equalsIgnoreCase("vert")) {
                    i++;
                }
            }
        }
        if (i == 0) {
            bvert.getBlock().setType(Material.AIR);
        }
        //JAUNE
        i = 0;
        for (Player players : Bukkit.getOnlinePlayers()) {
            if (TeamSelect.team.containsKey(players)) {
                if (TeamSelect.team.get(players).equalsIgnoreCase("jaune")) {
                    i++;
                }
            }
        }
        if (i == 0) {
            if(!Main.INSTANCE.getConfig().getBoolean("debug")){
                byellow.getBlock().setType(Material.AIR);
            }

        }
    }

    public static void destroyNexus(String team){
        Location brouge = Main.getBeaconRed();
        Location bbleu = Main.getBeaconBlue();
        Location bvert = Main.getBeaconGreen();
        Location bjaune = Main.getBeaconYellow();

        if(team.equals("rouge")){
            brouge.getBlock().setType(Material.AIR);
        }
        if(team.equals("bleu")){
            bbleu.getBlock().setType(Material.AIR);
        }
        if(team.equals("vert")){
            bvert.getBlock().setType(Material.AIR);
        }
        if(team.equals("rouge")){
            bjaune.getBlock().setType(Material.AIR);
        }

    }

}
