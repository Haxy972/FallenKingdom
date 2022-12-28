package fr.haxy972.fallenkingdom.teams;

import fr.haxy972.fallenkingdom.game.GameManager;
import fr.haxy972.fallenkingdom.utils.TitleManager;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TeamManager {

    private final List<Team> teamList = new ArrayList<>();
    private TeamInventory teamInventory = null;
    private final GameManager gameManager;

    public TeamManager(GameManager gameManager) {
        this.gameManager = gameManager;
        this.teamInventory = new TeamInventory(this);
    }


    public List<Team> getTeamList() {
        return teamList;
    }

    public List<Team> getTeams() {
        return teamList;
    }

    public Team getPlayerTeam(Player player) {
        for (Team team : getTeams()) {
            if (team.getPlayersList().contains(player)) {
                return team;
            }
        }
        return null;
    }

    public TeamInventory getTeamInventory() {
        return teamInventory;
    }

    public void initTeams() {
        teamList.add(new Team("Rouge", ChatColor.RED, (byte) 14, new Location(gameManager.getWorld(), 348, 68, -316, 58, -0)));
        teamList.add(new Team("Bleu", ChatColor.BLUE, (byte) 11, new Location(gameManager.getWorld(), 366, 75, 1)));
        teamList.add(new Team("Vert", ChatColor.GREEN, (byte) 5, new Location(gameManager.getWorld(), -212, 76, -307, -86, 0)));
        teamList.add(new Team("Jaune", ChatColor.YELLOW, (byte) 4, new Location(gameManager.getWorld(), -205, 76, 303, 178, 0)));
    }

    public void addTeam(Team team){
        teamList.add(team);
    }

    public void removeTeam(Team team){
        teamList.remove(team);
    }

    public List<Location> getTeamArea(Team team, int marge) {
        List<Location> teamArea = new ArrayList<>();
        Location nexusLocation = team.getNexusLocation();

        for (int x = -15 - marge; x <= 15 + marge; x++) {
            for (int z = -15 - marge; z <= 15 + marge; z++) {
                teamArea.add(new Location(nexusLocation.getWorld(), nexusLocation.getX() + x, 0, nexusLocation.getZ() + z));
            }
        }
        return teamArea;
    }

    public Team getTeamByName(String name) {
        for (Team team : getTeamList()) {
            if (team.getName().equalsIgnoreCase(name)) {
                return team;
            }
        }
        return null;
    }

    public void generateBeacons() {
        for (Team teams : getTeamList()) {
            teams.getNexusLocation().getBlock().setType(Material.BEACON);
            Location glassLocation = new Location(gameManager.getWorld(), teams.getNexusLocation().getX(), teams.getNexusLocation().getY() + 1, teams.getNexusLocation().getZ());
            glassLocation.getBlock().setType(Material.STAINED_GLASS);
            glassLocation.getBlock().setData(teams.getColorID());
            generateIronBeacon(teams.getNexusLocation());
        }
    }

    public void generateIronBeacon(Location beaconLocation) {
        Location underBeacon = new Location(beaconLocation.getWorld(), beaconLocation.getX(), beaconLocation.getY() - 1, beaconLocation.getZ());
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                Location ironBlockLoc = new Location(underBeacon.getWorld(), underBeacon.getX() + x, underBeacon.getY(), underBeacon.getZ() + z);
                ironBlockLoc.getBlock().setType(Material.IRON_BLOCK);
            }
        }
    }

    public void killTeam(Team team, Player player, boolean sound) {
        if (team.isAlive()) {
            team.setAlive(false);
            team.getNexusLocation().getBlock().setType(Material.AIR);
            for (Player players : Bukkit.getOnlinePlayers()) {
                if (sound) players.playSound(players.getLocation(), Sound.WITHER_DEATH, 1f, 1f);
                if (gameManager.getPlayerList().contains(players) && getPlayerTeam(players) != null) {
                    if(!players.isOnline()) team.removePlayer(player);
                    if (getPlayerTeam(players).equals(team)) {
                        TitleManager.sendTitle(players, "§8x §cDétruit §8x", "§7Votre royaume est tombé", 60);
                    }
                }
            }
            if (player != null) {
                Bukkit.broadcastMessage(gameManager.getPrefix() + "§7L'équipe " + team.getColor() + team.getName() + " §7a perdu son royaume à cause de " + getPlayerTeam(player).getColor() + player.getName());
            } else {
                Bukkit.broadcastMessage(gameManager.getPrefix() + "§7L'équipe " + team.getColor() + team.getName() + " §7a perdu son royaume");
            }
        }
    }

    public void checkTeamsAlive(boolean sound) {
        for (Team teams : getTeamList()) {
            if (teams.getPlayersCount() == 0) {
                killTeam(teams, null, sound);
            } else {
                int onlineCounter = 0;
                for (Player players : Bukkit.getOnlinePlayers()) {
                    if (getPlayerTeam(players) != null) {
                        if (getPlayerTeam(players).equals(teams)) {
                            onlineCounter++;
                        }
                    }
                }
                if (onlineCounter == 0) {
                    killTeam(teams, null, sound);
                }
            }
        }
    }

    public Team getTeamPlayerByName(Player player) {
        for (Team team : getTeamList()) {
            if (team.getName().equalsIgnoreCase(player.getName())) {
                return team;
            }
        }
        return null;
    }
}