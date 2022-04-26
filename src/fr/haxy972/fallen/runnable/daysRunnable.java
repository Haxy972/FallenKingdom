package fr.haxy972.fallen.runnable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.sun.org.glassfish.external.statistics.annotations.Reset;
import fr.haxy972.fallen.listeners.LobbyListeners;
import fr.haxy972.fallen.listeners.ResetListeners;
import fr.haxy972.fallen.utils.ChestRefill;
import fr.haxy972.fallen.utils.MessageYaml;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import fr.haxy972.fallen.GameStatut;
import fr.haxy972.fallen.Main;
import fr.haxy972.fallen.Scoreboard.ScoreboardManager;
import fr.haxy972.fallen.Team.select.TeamSelect;
import fr.haxy972.fallen.manager.GameManager;

public class daysRunnable extends BukkitRunnable {

    public static boolean redalive = true;
    public static boolean bluealive = true;
    public static boolean greenalive = true;
    public static boolean yellowalive = true;
    public static boolean redbedalive = true;
    public static boolean bluebedalive = true;
    public static boolean greenbedalive = true;
    public static boolean yellowbedalive = true;
    public static boolean alreadywin = false;
    static int initialtimer = Main.INSTANCE.getConfig().getInt("partie.jour-duree") + 1;
    public static int day = Main.INSTANCE.getConfig().getInt("partie.jour-de-depart");
    public static int timer = initialtimer;

    @Override
    public void run() {
        timer--;

        checkTeamAlive();
        timerupdate(timer);

        if (alreadywin) {
            timerupdate(0);

            this.cancel();
        }


        if (timer == 0) {
            timer = initialtimer;
            day++;

            newDayComing();

        }

    }

    private void checkTeamAlive() {


        //Bleu
        Location loc = Main.getBeaconBlue();
        if (loc.getBlock().getType() != Material.BEACON) {
            for (Player players : Bukkit.getOnlinePlayers()) {
                if (ScoreboardManager.scoreboardGame.containsKey(players)) {
                    int temp = 0;
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        if (TeamSelect.team.containsKey(player) && TeamSelect.team.get(player).equalsIgnoreCase("bleu")) {
                            temp++;
                        }
                    }
                    if (temp == 0) {
                        ScoreboardManager.scoreboardGame.get(players).setLine(6, "§c§lX §9§lBleu");
                        bluealive = false; //pas toucher
                        teamDead("bleu");
                        bluebedalive = false;
                    } else {
                        //ScoreboardManager.scoreboardGame.get(players).setLine(6, "§c§lX §9§lBleu§7: §e" + temp);
                        ScoreboardManager.scoreboardGame.get(players).setLine(6, "§c§lX §9§lBleu§7:§e " + temp);
                        bluealive = true; //pas toucher
                        bluebedalive = false;
                    }
                }


            }
        } else {
            for (Player players : Bukkit.getOnlinePlayers()) {
                if (ScoreboardManager.scoreboardGame.containsKey(players)) {
                    ScoreboardManager.scoreboardGame.get(players).setLine(6, "§a§l✓ §9§lBleu");
                }

                bluealive = true;
                bluebedalive = true;
            }
        }
        //Rouge
        loc = Main.getBeaconRed();
        if (loc.getBlock().getType() != Material.BEACON) {
            for (Player players : Bukkit.getOnlinePlayers()) {
                if (ScoreboardManager.scoreboardGame.containsKey(players)) {
                    int temp = 0;
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        if (TeamSelect.team.containsKey(player) && TeamSelect.team.get(player).equalsIgnoreCase("rouge")) {
                            temp++;
                        }
                    }
                    if (temp == 0) {
                        ScoreboardManager.scoreboardGame.get(players).setLine(5, "§c§lX §c§lRouge");
                        redalive = false; //pas toucher
                        teamDead("rouge");
                        redbedalive = false;
                    } else {


                        ScoreboardManager.scoreboardGame.get(players).setLine(6, "§c§lX Rouge§7:§e " + temp);
                        redalive = true; //pas toucher
                        redbedalive = false;
                    }
                }


            }
        } else {
            for (Player players : Bukkit.getOnlinePlayers()) {
                if (ScoreboardManager.scoreboardGame.containsKey(players)) {
                    ScoreboardManager.scoreboardGame.get(players).setLine(5, "§a§l✓ §c§lRouge");
                }

                redalive = true;
                redbedalive = true;
            }
        }
        //Vert
        loc = Main.getBeaconGreen();
        if (loc.getBlock().getType() != Material.BEACON) {
            for (Player players : Bukkit.getOnlinePlayers()) {
                if (ScoreboardManager.scoreboardGame.containsKey(players)) {
                    int temp = 0;
                    for (Player player : Bukkit.getOnlinePlayers()) {

                        if (TeamSelect.team.containsKey(player) && TeamSelect.team.get(player).equalsIgnoreCase("vert")) {
                            temp++;
                        }
                    }

                    if (temp == 0) {
                        ScoreboardManager.scoreboardGame.get(players).setLine(7, "§c§lX §a§lVert");
                        teamDead("vert");
                        greenalive = false; //pas toucher
                        greenbedalive = false;
                    } else {
                        ScoreboardManager.scoreboardGame.get(players).setLine(7, "§c§lX §a§lVert§7:§e " + temp);

                        greenalive = true; //pas toucher
                        greenbedalive = false;
                    }
                }


            }
        } else {
            for (Player players : Bukkit.getOnlinePlayers()) {
                if (ScoreboardManager.scoreboardGame.containsKey(players)) {
                    ScoreboardManager.scoreboardGame.get(players).setLine(7, "§a§l✓ §a§lVert");
                }

                greenalive = true;
                greenbedalive = true;
            }
        }

        //JAUNE VERIF BEACON
        loc = Main.getBeaconYellow();

        if (loc.getBlock().getType() != Material.BEACON) {
            for (Player players : Bukkit.getOnlinePlayers()) {
                if (ScoreboardManager.scoreboardGame.containsKey(players)) {
                    int temp = 0;
                    for (Player player : Bukkit.getOnlinePlayers()) {

                        if (TeamSelect.team.containsKey(player) && TeamSelect.team.get(player).equalsIgnoreCase("jaune")) {
                            temp++;
                        }
                    }

                    if (temp == 0) {
                        ScoreboardManager.scoreboardGame.get(players).setLine(8, "§c§lX §e§lJaune");
                        yellowalive = false; //pas toucher
                        yellowbedalive = false;
                        teamDead("jaune");

                    } else {
                        ScoreboardManager.scoreboardGame.get(players).setLine(8, "§c§lX §e§lJaune§7:§e " + temp);
                        yellowalive = true; //pas toucher
                        yellowbedalive = false;

                    }
                }


            }
        } else {
            for (Player players : Bukkit.getOnlinePlayers()) {
                if (ScoreboardManager.scoreboardGame.containsKey(players)) {
                    ScoreboardManager.scoreboardGame.get(players).setLine(8, "§a§l✓ §e§lJaune");
                }

                yellowalive = true;
                yellowbedalive = true;
            }
        }

        // AUCUN VAINQUEUR
        if (greenalive == false && redalive == false && bluealive == false && yellowalive == false) {
            GameStatut.setStatut(GameStatut.END);
            haveWin("§c§lAucune");
            timerupdate(1);

        }

        //VAINQUEUR ROUGE
        if (greenalive == false && redalive == true && bluealive == false && yellowalive == false) {
            int temp = 0;
            for (Player player : Bukkit.getOnlinePlayers()) {

                if (TeamSelect.team.containsKey(player) && TeamSelect.team.get(player).equalsIgnoreCase("bleu")) {
                    temp++;
                }
            }
            int blue = temp;
            temp = 0;
            for (Player player : Bukkit.getOnlinePlayers()) {

                if (TeamSelect.team.containsKey(player) && TeamSelect.team.get(player).equalsIgnoreCase("vert")) {
                    temp++;
                }
            }
            int green = temp;
            temp = 0;
            for (Player player : Bukkit.getOnlinePlayers()) {

                if (TeamSelect.team.containsKey(player) && TeamSelect.team.get(player).equalsIgnoreCase("jaune")) {
                    temp++;
                }
            }
            int yellow = temp;
            temp = 0;

            if (blue == 0 && green == 0 && yellow == 0) {
                GameStatut.setStatut(GameStatut.END);
                haveWin("§c§lROUGE");
                timerupdate(1);
            }
        }
        //VAINQUEUR BLEU
        if (greenalive == false && redalive == false && bluealive == true && yellowalive == false) {
            int temp = 0;
            for (Player player : Bukkit.getOnlinePlayers()) {

                if (TeamSelect.team.containsKey(player) && TeamSelect.team.get(player).equalsIgnoreCase("rouge")) {
                    temp++;
                }
            }
            int red = temp;
            temp = 0;
            for (Player player : Bukkit.getOnlinePlayers()) {

                if (TeamSelect.team.containsKey(player) && TeamSelect.team.get(player).equalsIgnoreCase("vert")) {
                    temp++;
                }
            }
            int green = temp;
            temp = 0;
            for (Player player : Bukkit.getOnlinePlayers()) {

                if (TeamSelect.team.containsKey(player) && TeamSelect.team.get(player).equalsIgnoreCase("jaune")) {
                    temp++;
                }
            }
            int yellow = temp;
            temp = 0;

            if (red == 0 && green == 0 && yellow == 0) {
                GameStatut.setStatut(GameStatut.END);
                haveWin("§9§lBLEU");
                timerupdate(1);

            }


        }
        // VAINQUEUR VERT
        if (greenalive == true && redalive == false && bluealive == false && yellowalive == false) {
            int temp = 0;
            for (Player player : Bukkit.getOnlinePlayers()) {

                if (TeamSelect.team.containsKey(player) && TeamSelect.team.get(player).equalsIgnoreCase("rouge")) {
                    temp++;
                }
            }
            int red = temp;
            temp = 0;
            for (Player player : Bukkit.getOnlinePlayers()) {

                if (TeamSelect.team.containsKey(player) && TeamSelect.team.get(player).equalsIgnoreCase("bleu")) {
                    temp++;
                }
            }
            int blue = temp;
            temp = 0;
            for (Player player : Bukkit.getOnlinePlayers()) {

                if (TeamSelect.team.containsKey(player) && TeamSelect.team.get(player).equalsIgnoreCase("jaune")) {
                    temp++;
                }
            }
            int yellow = temp;
            temp = 0;

            if (red == 0 && blue == 0 && yellow == 0) {
                GameStatut.setStatut(GameStatut.END);
                haveWin("§a§lVERT");
                timerupdate(1);

            }


        }
        // VAINQUEUR JAUNE
        if (greenalive == false && redalive == false && bluealive == false && yellowalive == true) {
            int temp = 0;
            for (Player player : Bukkit.getOnlinePlayers()) {

                if (TeamSelect.team.containsKey(player) && TeamSelect.team.get(player).equalsIgnoreCase("rouge")) {
                    temp++;
                }
            }
            int red = temp;
            temp = 0;
            for (Player player : Bukkit.getOnlinePlayers()) {

                if (TeamSelect.team.containsKey(player) && TeamSelect.team.get(player).equalsIgnoreCase("vert")) {
                    temp++;
                }
            }
            int green = temp;
            temp = 0;
            for (Player player : Bukkit.getOnlinePlayers()) {

                if (TeamSelect.team.containsKey(player) && TeamSelect.team.get(player).equalsIgnoreCase("bleu")) {
                    temp++;
                }
            }
            int blue = temp;
            temp = 0;

            if (red == 0 && green == 0 && blue == 0) {
                GameStatut.setStatut(GameStatut.END);
                haveWin("§e§lJAUNE");
                timerupdate(1);

            }


        }

    }


    private static boolean alreadyDeadRed = false;
    private static boolean alreadyDeadBlue = false;
    private static boolean alreadyDeadGreen = false;
    private static boolean alreadyDeadYellow = false;


    private void teamDead(String string) {
        if (string.equalsIgnoreCase("rouge")) {
            if (alreadyDeadRed == false) {
                for (Player players : Bukkit.getOnlinePlayers()) {
                    players.playSound(players.getLocation(), Sound.ENTITY_WITHER_DEATH, 1f, 1f);
                }
                Bukkit.broadcastMessage(Main.getPrefix() + "§7 L'équipe §c§lROUGE §7 est éliminé");
                alreadyDeadRed = true;
            }
        }
        if (string.equalsIgnoreCase("bleu")) {
            if (alreadyDeadBlue == false) {
                for (Player players : Bukkit.getOnlinePlayers()) {
                    players.playSound(players.getLocation(), Sound.ENTITY_WITHER_DEATH, 1f, 1f);
                }
                Bukkit.broadcastMessage(Main.getPrefix() + "§7 L'équipe §9§lBLEU §7 est éliminé");
                alreadyDeadBlue = true;
            }
        }
        if (string.equalsIgnoreCase("vert")) {
            if (alreadyDeadGreen == false) {
                for (Player players : Bukkit.getOnlinePlayers()) {
                    players.playSound(players.getLocation(), Sound.ENTITY_WITHER_DEATH, 1f, 1f);
                }
                Bukkit.broadcastMessage(Main.getPrefix() + "§7 L'équipe §a§lVERT §7 est éliminé");
                alreadyDeadGreen = true;
            }
        }
        if (string.equalsIgnoreCase("jaune")) {
            if (alreadyDeadYellow == false) {
                for (Player players : Bukkit.getOnlinePlayers()) {
                    players.playSound(players.getLocation(), Sound.ENTITY_WITHER_DEATH, 1f, 1f);
                }
                Bukkit.broadcastMessage(Main.getPrefix() + "§7 L'équipe §e§lJAUNE §7 est éliminé");
                alreadyDeadYellow = true;
            }
        }


    }

    private void haveWin(String team) {
        if (alreadywin) {
            return;
        }
        alreadywin = true;


        if (bluealive == false && greenalive == false && redalive == false && yellowalive == false) {
            Bukkit.broadcastMessage(Main.getPrefix() + MessageYaml.getValue("messages.partie.no-winner").replace("&", "§"));
            for (Player players : Bukkit.getOnlinePlayers()) {
                players.playSound(players.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f);
                players.setGameMode(GameMode.SPECTATOR);


                for (Chunk chunk : Bukkit.getWorld(Main.getWorld().getName()).getLoadedChunks()) {
                    for (Entity e : chunk.getEntities()) {
                        if (e instanceof Item) {
                            e.remove();

                        }
                    }
                }

                GameStatut.setStatut(GameStatut.END);
                BestKiller();
                BestDeath();

                if (!deathCount.containsKey(players)) {
                    return;
                }


                players.sendMessage(Main.getPrefix() + "§7Vous êtes mort §e" + deathCount.get(players) + " fois §7sur cette partie");
                players.sendMessage(Main.getPrefix() + "§7Vous avez fait §e" + killCount.get(players) + " kills §7sur cette partie");
                if (deathCount.get(players) != 0) {

                    players.sendMessage(Main.getPrefix() + "§7Votre ratio est de: §b" + killCount.get(players) / deathCount.get(players));
                } else {
                    players.sendMessage(Main.getPrefix() + "§aVous n'êtes pas mort durant cette partie");
                }


                Bukkit.getScheduler().runTaskLater(Main.INSTANCE, new Runnable() {

                    @Override
                    public void run() {


                        new restartRunnable().runTaskTimer(Main.INSTANCE, 0, 20);



                    }
                }, 20 * 30);

            }
            return;
        }


        Bukkit.broadcastMessage(Main.getPrefix() + MessageYaml.getValue("messages.partie.team-win").replace("&", "§").replace("{team}", team));
        for (Player players : Bukkit.getOnlinePlayers()) {
            players.playSound(players.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f);
            players.setGameMode(GameMode.SPECTATOR);
        }
        GameStatut.setStatut(GameStatut.END);
        for (Chunk chunk : Bukkit.getWorld(Main.getWorld().getName()).getLoadedChunks()) {
            for (Entity e : chunk.getEntities()) {
                if (e instanceof Item) {
                    e.remove();

                }
            }
        }
        BestKiller();
        BestDeath();
        for (Player players : Bukkit.getOnlinePlayers()) {
            if (!deathCount.containsKey(players)) {
                return;
            }


            players.sendMessage(Main.getPrefix() + "§7Vous êtes mort §e" + deathCount.get(players) + " fois §7sur cette partie");
            players.sendMessage(Main.getPrefix() + "§7Vous avez fait §e" + killCount.get(players) + " kills §7sur cette partie");
            if (deathCount.get(players) != 0) {

                players.sendMessage(Main.getPrefix() + "§7Votre ratio est de: §b" + killCount.get(players) / deathCount.get(players));
            } else {
                players.sendMessage(Main.getPrefix() + "§aVous n'êtes pas mort durant cette partie");
            }
        }


        Bukkit.getScheduler().runTaskLater(Main.INSTANCE, new Runnable() {

            @Override
            public void run() {
                new restartRunnable().runTaskTimer(Main.INSTANCE, 0, 20);

            }
        }, 20 * 30);
        //Bukkit.broadcastMessage(Main.getPrefix() + "§7Vous allez être mis en spectateur dans 10 secondes");


    }

    public static Map<Player, Integer> killCount = new HashMap<>();
    public static Map<Player, Integer> deathCount = new HashMap<>();


    private void BestKiller() {

        int bestkill1 = 0;
        String bestkillname1 = null;

        //defini le num 1
        for (Player players : Bukkit.getOnlinePlayers()) {
            if (killCount.containsKey(players)) {
                if (killCount.get(players) >= bestkill1) {
                    bestkillname1 = players.getName();
                    bestkill1 = killCount.get(players);
                }
            }
        }
        //defini le num 2
        int bestkill2 = 0;
        String bestkillname2 = null;

        for (Player players : Bukkit.getOnlinePlayers()) {
            if (killCount.containsKey(players)) {
                if (killCount.get(players) >= bestkill2 && players.getName() != bestkillname1) {
                    bestkillname2 = players.getName();
                    bestkill2 = killCount.get(players);
                }
            }
        }
        //defini le 3
        int bestkill3 = 0;
        String bestkillname3 = null;

        for (Player players : Bukkit.getOnlinePlayers()) {
            if (killCount.containsKey(players)) {
                if (killCount.get(players) >= bestkill3 && players.getName() != bestkillname1 && players.getName() != bestkillname2) {
                    bestkillname3 = players.getName();
                    bestkill3 = killCount.get(players);
                }
            }
        }
        Bukkit.broadcastMessage(" ");
        Bukkit.broadcastMessage("§f§m------§bClassement de Kill§f§m------");
        if (bestkill1 != 0 && bestkillname1 != null) {
            Bukkit.broadcastMessage("§6§l1er §e" + bestkillname1 + "§7§m--§7> §e" + bestkill1);
        } else {
            Bukkit.broadcastMessage("§6§l1er §c§lAucun");
        }
        if (bestkill2 != 0 && bestkillname2 != null) {
            Bukkit.broadcastMessage("§d§l2ème §e" + bestkillname2 + "§7§m--§7> §e" + bestkill2);

        } else {
            Bukkit.broadcastMessage("§d§l2ème §c§lAucun");
        }
        if (bestkill3 != 0 && bestkillname3 != null) {
            Bukkit.broadcastMessage("§b§l3ème §e" + bestkillname3 + "§7§m--§7> §e" + bestkill3);

        } else {
            Bukkit.broadcastMessage("§b§l3ème §c§lAucun");
        }


    }

    private void BestDeath() {
        int bestdeath1 = 0;
        String bestdeathname1 = null;

        //defini le num 1
        for (Player players : Bukkit.getOnlinePlayers()) {
            if (deathCount.containsKey(players)) {
                if (deathCount.get(players) >= bestdeath1) {
                    bestdeathname1 = players.getName();
                    bestdeath1 = deathCount.get(players);
                }
            }
        }
        //defini le num 2
        int bestdeath2 = 0;
        String bestdeathname2 = null;

        for (Player players : Bukkit.getOnlinePlayers()) {
            if (deathCount.containsKey(players)) {
                if (deathCount.get(players) >= bestdeath2 && players.getName() != bestdeathname1) {
                    bestdeathname2 = players.getName();
                    bestdeath2 = deathCount.get(players);
                }
            }
        }
        //defini le 3
        int bestdeath3 = 0;
        String bestdeathname3 = null;

        for (Player players : Bukkit.getOnlinePlayers()) {
            if (deathCount.containsKey(players)) {
                if (deathCount.get(players) >= bestdeath3 && players.getName() != bestdeathname1 && players.getName() != bestdeathname2) {
                    bestdeathname3 = players.getName();
                    bestdeath3 = deathCount.get(players);
                }
            }
        }
        Bukkit.broadcastMessage(" ");
        Bukkit.broadcastMessage("§f§m------§bClassement de Mort§f§m------");
        if (bestdeath1 != 0 && bestdeathname1 != null) {
            Bukkit.broadcastMessage("§6§l1er §e" + bestdeathname1 + "§7§m--§7> §e" + bestdeath1);
        } else {
            Bukkit.broadcastMessage("§6§l1er §c§lAucun");
        }
        if (bestdeath2 != 0 && bestdeathname2 != null) {
            Bukkit.broadcastMessage("§d§l2ème §e" + bestdeathname2 + "§7§m--§7> §e" + bestdeath2);

        } else {
            Bukkit.broadcastMessage("§d§l2ème §c§lAucun");
        }
        if (bestdeath3 != 0 && bestdeathname3 != null) {
            Bukkit.broadcastMessage("§b§l3ème §e" + bestdeathname3 + "§7§m--§7> §e" + bestdeath3);

        } else {
            Bukkit.broadcastMessage("§b§l3ème §c§lAucun");
        }
        Bukkit.broadcastMessage("§f");

    }

    private void timerupdate(Integer mytimer) {
        for (Player players : Bukkit.getOnlinePlayers()) {
            if (ScoreboardManager.scoreboardGame.containsKey(players)) {
                ScoreboardManager.scoreboardGame.get(players).setLine(3, "§eTemps restant: §a" + new SimpleDateFormat("mm:ss").format(new Date(mytimer * 1000)));
            }
        }


    }


    public static void newDayComing() {
        Bukkit.broadcastMessage(Main.getPrefix() + MessageYaml.getValue("messages.days.new").replace("{jour}", "" + daysRunnable.day).replace("&", "§"));
        for (Player players : Bukkit.getOnlinePlayers()) {
            players.playSound(players.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f);
            if (ScoreboardManager.scoreboardGame.containsKey(players)) {
                ScoreboardManager.scoreboardGame.get(players).setLine(1, "§b§lJour n§b°§b§l" + daysRunnable.day);

            }
        }
        if (day == Main.INSTANCE.getConfig().getInt("partie.jour-de-faim")) {
            Bukkit.broadcastMessage(Main.getPrefix() + MessageYaml.getValue("messages.days.feed-less").replace("&", "§"));
        }

        if (day == Main.INSTANCE.getConfig().getInt("partie.jour-de-pvp")) {
            Bukkit.broadcastMessage(Main.getPrefix() + MessageYaml.getValue("messages.days.pvp-activated").replace("&", "§"));
        }

        if (day == Main.INSTANCE.getConfig().getInt("partie.jour-nether")) {
            Bukkit.broadcastMessage(Main.getPrefix() + MessageYaml.getValue("messages.days.nether-activated").replace("&", "§"));
        }

        if (day == Main.INSTANCE.getConfig().getInt("partie.jour-bed-destroyed")) {
            Bukkit.broadcastMessage(Main.getPrefix() + MessageYaml.getValue("messages.days.bed-destroyed").replace("&", "§"));
            GameManager.destroyNexus("rouge");
            GameManager.destroyNexus("bleu");
            GameManager.destroyNexus("vert");
            GameManager.destroyNexus("jaune");

        }
        if (day == Main.INSTANCE.getConfig().getInt("partie.jour-bed-destroyed") - 1) {
            Bukkit.broadcastMessage(Main.getPrefix() + MessageYaml.getValue("messages.days.annonce-to-be-destroyed").replace("&", "§"));
        }

        if (day % 2 == 0) {
            Bukkit.getWorld(Main.getWorld().getName()).setTime(1000);

        } else {
            Bukkit.getWorld(Main.getWorld().getName()).setTime(13000);
        }

        if (day >= Main.INSTANCE.getConfig().getInt("partie.jour-de-coffre")) {
            ChestRefill.Refill();
        }
    }


}
