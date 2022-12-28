package fr.haxy972.fallenkingdom.teams;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

public class TeamInvRunnable extends BukkitRunnable {
    private final Player player;
    private final TeamInventory teamInventory;

    public TeamInvRunnable(Player player, TeamManager teamManager) {
        this.player = player;
        this.teamInventory = teamManager.getTeamInventory();
    }

    @Override
    public void run() {
        if (player.getOpenInventory().getTitle().equals(teamInventory.getTitle())) {
            teamInventory.addItems(player.getOpenInventory().getTopInventory());
        } else {
            this.cancel();
        }

    }
}
