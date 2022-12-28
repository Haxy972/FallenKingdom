package fr.haxy972.fallenkingdom.spectator;

import fr.haxy972.fallenkingdom.game.GameManager;
import fr.haxy972.fallenkingdom.game.GameStatut;
import fr.haxy972.fallenkingdom.teams.Team;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class SpectatorInventory {

    private final GameManager gameManager;
    private Player player;
    private Inventory inventory;

    private final String title = "             §bRegarder les joueurs";

    public SpectatorInventory(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public void addItems(Inventory inventory) {
        if (!gameManager.isGameStatut(GameStatut.END)) {
            for (Team team : gameManager.getTeamManager().getTeams()) {
                for (Player players : team.getPlayersList()) {
                    if (!gameManager.getSpectatorsList().contains(players) && gameManager.getPlayerList().contains(players)) {
                        inventory.addItem(gameManager.getGameUtils().getPlayerHead(players.getName(), "§b" + players.getName(), "§7Equipe§8> " + team.getColor() + team.getName()));
                    }
                }
            }
        } else {
            for (Player players : gameManager.getSpectatorsList()) {
                inventory.addItem(gameManager.getGameUtils().getPlayerHead(players.getName(), "§b" + players.getName(), "§7Equipe§8> §bSpectateurs"));
            }
        }
    }


    public Inventory getInventory() {
        inventory = Bukkit.createInventory(null, getInventorySize((gameManager.getPlayerList().size() == 0 ? gameManager.getSpectatorsList().size(): gameManager.getPlayerList().size())), title);
        addItems(inventory);
        return inventory;
    }

    public String getTitle() {
        return title;
    }

    private int getInventorySize(int size) {
        for (int i = 0; i < 6; i++) {
            if (i * 9 >= size) {
                return i * 9;
            }
        }
        return 9 * 6;
    }


}
