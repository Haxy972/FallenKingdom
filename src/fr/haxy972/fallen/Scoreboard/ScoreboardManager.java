package fr.haxy972.fallen.Scoreboard;

import java.util.HashMap;
import java.util.Map;

import fr.haxy972.fallen.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import fr.haxy972.fallen.runnable.daysRunnable;

public class ScoreboardManager {

    public Player player;
    public ScoreboardSign scoreboard;
    public static Map<Player, ScoreboardSign> scoreboardGame = new HashMap<>();

    public ScoreboardManager(Player players) {
        player = players;
        scoreboard = new ScoreboardSign(player, player.getName());
        scoreboardGame.put(player, scoreboard);

    }

    public void loadScoreboard() {
        scoreboard.setObjectiveName("§6§lFallen§b§lKingdom");
        scoreboard.create();
        ((ScoreboardSign) scoreboardGame.get(player)).setLine(0, "§c");
        ((ScoreboardSign) scoreboardGame.get(player)).setLine(1, "§fJoueur: §b§l" + player.getName());
        ((ScoreboardSign) scoreboardGame.get(player)).setLine(2, "§a");
        ((ScoreboardSign) scoreboardGame.get(player)).setLine(3, "§eTimer: §c§lAucun");
        ((ScoreboardSign) scoreboardGame.get(player)).setLine(4, "§6");
        ((ScoreboardSign) scoreboardGame.get(player)).setLine(5, "§eConnectés: §a" + Bukkit.getOnlinePlayers().size() + "/" + Main.INSTANCE.getConfig().getInt("partie.requiredtostart"));
        ((ScoreboardSign) scoreboardGame.get(player)).setLine(6, "§e");
        ((ScoreboardSign) scoreboardGame.get(player)).setLine(7, "     §bplay.warlend.fr");
    }

    public void loadScoreboardGame() {
        scoreboard.setObjectiveName("§6§lFallen§b§lKingdom");
        scoreboard.create();
        ((ScoreboardSign) scoreboardGame.get(player)).setLine(0, "§c");
        ((ScoreboardSign) scoreboardGame.get(player)).setLine(1, "§b§lJour n°" + daysRunnable.day);
        ((ScoreboardSign) scoreboardGame.get(player)).setLine(2, "§a");
        ((ScoreboardSign) scoreboardGame.get(player)).setLine(3, "§eTemps restant: §a10:00");
        ((ScoreboardSign) scoreboardGame.get(player)).setLine(4, "§6");
        ((ScoreboardSign) scoreboardGame.get(player)).setLine(5, "§a§l✓ §c§lRouge");
        ((ScoreboardSign) scoreboardGame.get(player)).setLine(6, "§a§l✓ §9§lBleu");
        ((ScoreboardSign) scoreboardGame.get(player)).setLine(7, "§a§l✓ §a§lVert");
        ((ScoreboardSign) scoreboardGame.get(player)).setLine(8, "§a§l✓ §e§lJaune");
        ((ScoreboardSign) scoreboardGame.get(player)).setLine(9, "§e");
        ((ScoreboardSign) scoreboardGame.get(player)).setLine(10, "     §bplay.warlend.fr");
    }

}
