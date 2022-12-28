package fr.haxy972.fallenkingdom.game;

import fr.haxy972.fallenkingdom.data.PlayerData;
import fr.haxy972.fallenkingdom.data.PlayerDataManager;
import fr.haxy972.fallenkingdom.teams.Team;
import fr.haxy972.fallenkingdom.teams.TeamManager;
import fr.haxy972.fallenkingdom.utils.ScoreboardSign;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScoreboardManager {

    private final GameManager gameManager;
    private final TeamManager teamManager;
    private final PlayerDataManager playerDataManager;
    private final Map<Player, ScoreboardSign> scoreboardGame = new HashMap<>();
    private ScoreboardSign scoreboard;

    public ScoreboardManager(GameManager gameManager) {
        this.gameManager = gameManager;
        this.playerDataManager = gameManager.getPlayerDataManager();
        this.teamManager = gameManager.getTeamManager();
    }

    public void loadLobbyScoreboard(Player player) {
        loadScoreboardBase(player);
        scoreboard.setLine(5, "§e");
        scoreboard.setLine(4, "§8| §7Joueurs: §e" + gameManager.getPlayerList().size() + "§8/§e" + Bukkit.getMaxPlayers());
        scoreboard.setLine(3, "§8| §7Début: §bEn attente");
        scoreboard.setLine(2, "§8| §7Carte: §a" + gameManager.getWorld().getName());
        scoreboard.setLine(1, "§d");
        scoreboard.setLine(0, "§6play.warlend.fr");
    }



    public void loadGameScoreboard(Player player) {
        int line = getTeamScoreBaseLine();
        int final_line = line + teamManager.getTeams().size();
        List<Team> teamsList = teamManager.getTeams();
        PlayerData playerData = playerDataManager.getPlayerData(player);
        loadScoreboardBase(player);
        scoreboard.setLine(final_line + 4, "§2");
        scoreboard.setLine(final_line + 3, "§8| §7Jour: §b" + gameManager.getGameDay());
        scoreboard.setLine(final_line + 2, "§8| §7Chrono: §b00:00");
        scoreboard.setLine( final_line + 1, "§a");
        for (Team teams : gameManager.getTeamManager().getTeams()){
            scoreboard.setLine(line, "§8| " + teams.getColor() + teams.getName() +" : §e" + (!teams.isAlive() ? "§c§lX" : "§a§l✔"));
            line++;
        }
        scoreboard.setLine(4, "§f");
        scoreboard.setLine(3, "§8| §7Carte: §a" + gameManager.getWorld().getName());

        /* This null verification is for check if player is a spectator.*/
        if(playerData != null) {
            scoreboard.setLine(2, "§8| §7K§8/§7D: §e" + playerData.getKills() + "§8/§e" + playerData.getDeaths());
        }
        scoreboard.setLine(1, "§d");
        scoreboard.setLine(0, "§6play.warlend.fr");
    }

    private int getTeamScoreBaseLine() {
        return 5;
    }

    private void loadScoreboardBase(Player player){
        setPlayerScoreboard(player);
        scoreboard.destroy();
        scoreboard.setObjectiveName("§7- " + gameManager.getPrefix().replace("§8> ", "")+" §7 -");
        scoreboard.create();
    }



    public void updateLobbyCount(Player player) {
        for (Player players : Bukkit.getOnlinePlayers()) {
            if (players != player) {
                setPlayerScoreboard(players);
                scoreboard.setLine(4, "§8| §7Joueurs: §e" + gameManager.getPlayerList().size() + "§8/§e" + Bukkit.getMaxPlayers());
            }
        }
    }

    public void updateLobbyTimer(int timer) {
        for (Player players : Bukkit.getOnlinePlayers()) {
            setPlayerScoreboard(players);
            scoreboard.setLine(3, "§8| §7Début: §b" + getTimerInMinutes(timer));
        }
    }
    public void updateGameTimer(int timer) {
        for (Player players : Bukkit.getOnlinePlayers()) {
            setPlayerScoreboard(players);
            int line = getTeamScoreBaseLine() + teamManager.getTeams().size() + 2;
            scoreboard.setLine(line, "§8| §7Chrono: §a" + getTimerInMinutes(timer));
        }
    }

    public void updateGameDayCount(int day){
        for (Player players : Bukkit.getOnlinePlayers()) {
            setPlayerScoreboard(players);
            int line = getTeamScoreBaseLine() + teamManager.getTeams().size() + 3;
            scoreboard.setLine(line, "§8| §7Jour: §b" + day);
        }
    }

    public void updateGameKillCount(Player player) {
        setPlayerScoreboard(player);
        PlayerData playerData = playerDataManager.getPlayerData(player);
        scoreboard.setLine(2, "§8| §7K§8/§7D: §e" + playerData.getKills() + "§8/§e" + playerData.getDeaths());
    }

    public void updateTeamAlive() {
        for (Player players : Bukkit.getOnlinePlayers()) {
            setPlayerScoreboard(players);
            int line = getTeamScoreBaseLine();
            for (Team teams : gameManager.getTeamManager().getTeams()){
                scoreboard.setLine(line, "§8| " + teams.getColor() + teams.getName() +" : §e" + (!teams.isAlive() ? (teams.getPlayerAlive() > 0 ? "§c"  + teams.getPlayerAlive() : "§c§lX" ): "§a§l✔"));
                line++;
            }

        }
    }

    public void deleteScoreboard(Player player){
        setPlayerScoreboard(player);
        scoreboard.destroy();
        scoreboardGame.remove(player);
    }

    private void setPlayerScoreboard(Player player) {
        if (!hasScoreboard(player)) {
            this.scoreboard = new ScoreboardSign(player, player.getName());
            scoreboardGame.put(player, scoreboard);
        } else {
            this.scoreboard = getPlayerScoreboard(player);
        }
    }

    private ScoreboardSign getPlayerScoreboard(Player player) {
        return scoreboardGame.get(player);
    }

    private boolean hasScoreboard(Player player) {
        return getPlayerScoreboard(player) != null;
    }


    private String getTimerInMinutes(Integer timer) {
        return new SimpleDateFormat("mm:ss").format(new Date(timer * 1000L));
    }
}
