package fr.haxy972.fallenkingdom.teams;

import fr.haxy972.fallenkingdom.Main;
import fr.haxy972.fallenkingdom.data.PlayerDataManager;
import fr.haxy972.fallenkingdom.utils.ItemCreator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class TeamInventory {

    private Player player;
    private final Inventory inventory;
    private final TeamManager teamManager;
    private final String title = "             §bSélection d'équipe";

    public TeamInventory(TeamManager teamManager){
        this.teamManager = teamManager;
        this.inventory = Bukkit.createInventory(null, 9, title);
    }

    public void addItems(Inventory inventory) {
        inventory.clear();
        int base_slot = getBaseSlot(teamManager.getTeams().size());
        int slot = base_slot;
        for(Team team : teamManager.getTeams()){
            ItemStack item = new ItemCreator(Material.WOOL, team.getPlayersCount(), team.getColorID()).setName(team.getColor() + team.getName() + " §7" + team.getPlayersCount() + "§8/§7" + Team.getMaxTeamCount()).setArrayLore(getTeamLores(team)).done();
            item = setSelectedTeam(item, team);
            inventory.setItem(slot, item);
            slot ++;
        }
        ItemStack randomTeamItem = new ItemCreator(Material.BARRIER).setName("§cAucune").setLores("§8> §fCliquez pour choisir").done();
        if (teamManager.getPlayerTeam(player) == null) {
            randomTeamItem = setSelectedTeam(randomTeamItem, null);
        }
        inventory.setItem(inventory.getSize() - 1, randomTeamItem);
    }


    public Inventory getInventory(){
        addItems(inventory);
        return inventory;
    }

    public String getTitle() {
        return title;
    }

    private int getBaseSlot(int teamCount) {
        switch (teamCount) {
            case 3:
                return 3;
            case 2:
                return 4;
            default:
                return 2;
        }
    }

    private ItemStack setSelectedTeam(ItemStack item, Team teams) {
        if (teamManager.getPlayerTeam(player) != null) {
            if (teamManager.getPlayerTeam(player).equals(teams)) {
                item.addUnsafeEnchantment(Enchantment.LUCK, 0);
                item = new ItemCreator(item).addFlags().done();
            }
        }
        return item;
    }

    private List<String> getTeamLores(Team team) {
        List<String> teamLores = new ArrayList();
        for (OfflinePlayer players : team.getPlayersList()) {
            teamLores.add(" §8- §e" + players.getName());
        }
        teamLores.add("");
        Team playerTeam = teamManager.getPlayerTeam(player);
        if (playerTeam != null) {
            teamLores.add((playerTeam.equals(team) ? "§8> §aVous êtes dans cette équipe" : "§8> §fCliquez ici pour rejoindre"));
        } else {
            teamLores.add("§8> §fCliquez ici pour rejoindre");
        }
        return teamLores;
    }
}
