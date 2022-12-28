package fr.haxy972.fallenkingdom.game;

import fr.haxy972.fallenkingdom.runnables.GameRunnable;
import fr.haxy972.fallenkingdom.teams.Team;
import fr.haxy972.fallenkingdom.utils.TitleManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class PlayerRunnable extends BukkitRunnable {

    private final GameManager gameManager;
    private final Map<Player, Location> playerNetherPortal = new HashMap<>();

    public PlayerRunnable(GameManager gameManager) {
        this.gameManager = gameManager;
    }


    @Override
    public void run() {
        updatePlayerActionBar();
    }


    private void updatePlayerActionBar() {
        for (Player players : Bukkit.getOnlinePlayers()) {
            if (gameManager.getPlayerList().contains(players)) {
                if(!players.getGameMode().equals(GameMode.SPECTATOR)) {
                    if (players.getWorld().equals(gameManager.getWorld())) {
                        Team team = gameManager.getTeamManager().getPlayerTeam(players);
                        TitleManager.sendActionBar(players, "§6" + getDirectionNexus(players.getLocation(), team.getNexusLocation()) + " §e" + getDistanceNexus(players.getLocation(), team.getNexusLocation()) + " blocs du §bRoyaume");
                    } else if (players.getWorld().getName().contains("nether")) {
                        Location netherPortal = playerNetherPortal.get(players);
                        TitleManager.sendActionBar(players, "§6" + getDirectionNexus(players.getLocation(), netherPortal) + " §e" + getDistanceNexus(players.getLocation(), netherPortal) + " de votre §cPortail");
                    } else if (players.getWorld().getName().contains("end")) {
                        TitleManager.sendActionBar(players, "§c/retour §7pour revenir §baux Royaumes");
                    }
                }
            }
        }

    }

    private int getDistanceNexus(Location playerLocation, Location destinationLocation) {
        double x_value = (destinationLocation.getX() - playerLocation.getX());
        double z_value = (destinationLocation.getZ() - playerLocation.getZ());
        x_value = x_value * x_value;
        z_value = z_value * z_value;
        double distance = Math.sqrt(x_value + z_value);
        return (int) distance;
    }

    private String getDirectionNexus(Location playerLocation, Location destinationLocation) {
        double x_value = (destinationLocation.getX() - playerLocation.getX());
        double z_value = (destinationLocation.getZ() - playerLocation.getZ());
        if (z_value > 0) {
            if (x_value > 0) {
//                90 = W -x & 0= S z-
                if (getYawOriginTrigo(playerLocation.getYaw()) > 270 && getYawOriginTrigo(playerLocation.getYaw()) < 360) {
                    return "⬆";
                } else if (getYawOriginTrigo(playerLocation.getYaw()) > 180 && getYawOriginTrigo(playerLocation.getYaw()) < 270) {
                    return "➡";
                } else if (getYawOriginTrigo(playerLocation.getYaw()) > 90 && getYawOriginTrigo(playerLocation.getYaw()) < 180) {
                    return "⬇";
                } else {
                    return "⬅";
                }
            } else {
                if (getYawOriginTrigo(playerLocation.getYaw()) > 0 && getYawOriginTrigo(playerLocation.getYaw()) < 90) {
                    return "⬆";
                } else if (getYawOriginTrigo(playerLocation.getYaw()) > 90 && getYawOriginTrigo(playerLocation.getYaw()) < 180) {
                    return "⬅";
                } else if (getYawOriginTrigo(playerLocation.getYaw()) > 180 && getYawOriginTrigo(playerLocation.getYaw()) < 270) {
                    return "⬇";
                } else {
                    return "➡";
                }
            }
        } else {
            if (x_value >= 0) {
                if (getYawOriginTrigo(playerLocation.getYaw()) > 0 && getYawOriginTrigo(playerLocation.getYaw()) < 90) {
                    return "⬇";
                } else if (getYawOriginTrigo(playerLocation.getYaw()) > 90 && getYawOriginTrigo(playerLocation.getYaw()) < 180) {
                    return "➡";
                } else if (getYawOriginTrigo(playerLocation.getYaw()) > 180 && getYawOriginTrigo(playerLocation.getYaw()) < 270) {
                    return "⬆";
                } else {
                    return "⬅";
                }
            } else {
                if (getYawOriginTrigo(playerLocation.getYaw()) > 0 && getYawOriginTrigo(playerLocation.getYaw()) < 90) {
                    return "➡";
                } else if (getYawOriginTrigo(playerLocation.getYaw()) > 90 && getYawOriginTrigo(playerLocation.getYaw()) < 180) {
                    return "⬆";
                } else if (getYawOriginTrigo(playerLocation.getYaw()) > 180 && getYawOriginTrigo(playerLocation.getYaw()) < 270) {
                    return "⬅";
                } else {
                    return "⬇";
                }
            }
        }
    }

    private double getYawOriginTrigo(double yaw) {
        if (yaw > 0) {
            for (int i = 0; i < 100; i++) {
                if (yaw - 360 >= 0) {
                    yaw -= 360;
                }
            }
            return yaw;
        } else {
            for (int i = 0; i < 100; i++) {
                if (yaw + 360 <= 0) {
                    yaw = yaw + 360;
                }
            }
            yaw += 360;
            return yaw;
        }
    }

    public Map<Player, Location> getPlayerNetherPortal() {
        return playerNetherPortal;
    }

    public void removePlayerNetherPortal(Player player) {
        playerNetherPortal.remove(player);
    }

    public void addPlayerNetherPortal(Player player, Location netherPortal) {
        playerNetherPortal.put(player, netherPortal);
    }
}
