package fr.haxy972.fallenkingdom.game;

import fr.haxy972.fallenkingdom.Main;
import fr.haxy972.fallenkingdom.data.PlayerData;
import fr.haxy972.fallenkingdom.data.PlayerDataManager;
import fr.haxy972.fallenkingdom.runnables.AnimalsRunnable;
import fr.haxy972.fallenkingdom.runnables.GameRunnable;
import fr.haxy972.fallenkingdom.teams.Team;
import fr.haxy972.fallenkingdom.teams.TeamManager;
import fr.haxy972.fallenkingdom.utils.ItemCreator;
import org.bukkit.*;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;

public class GameManager {

    private final World world;
    private GameStatut gameStatut;
    private final PlayerDataManager playerDataManager = new PlayerDataManager();
    private final TeamManager teamManager = new TeamManager(this);
    private final ScoreboardManager scoreboardManager = new ScoreboardManager(this);
    private final List<Player> playerList = new ArrayList<>();
    private final List<Player> spectatorsList = new ArrayList<>();
    private final GameUtils gameUtils = new GameUtils();
    private int gameDay = 1;
    private final int dayDuration = 15*60;
    private final GameRunnable gameRunnable = new GameRunnable(this);
    private final PlayerRunnable playerRunnable = new PlayerRunnable(this);
    private final AnimalsRunnable animalsRunnable = new AnimalsRunnable(this);
    private final Location netherPortal;
    private final Location enderPortal;
    private final List<Chest> chestGame = new ArrayList<>();
    private final Location lobbySpawn;
    public boolean timerIsRunning = false;
    private final List<Location> blocksSpawnList = new ArrayList<>();


    public GameManager() {
        this.world = Bukkit.getWorld("Christmas");
        this.gameStatut = GameStatut.LOBBY;
        this.lobbySpawn = new Location(world, -4.500, 82, 9.500);
        this.netherPortal = new Location(world, -20, 86, 24);
        this.enderPortal = new Location(world, 10, 84, 23);
        for(Entity entity : world.getEntities()) entity.remove();
        loadFloatingText();
        teamManager.initTeams();
        registerAllChests();
        registerAllBlocksSpawn();
        netherPortal.getBlock().setType(Material.AIR);
        gameUtils.setEnderPortal(enderPortal, Material.AIR);
        animalsRunnable.runTaskTimer(Main.getInstance(), 0,20*60);
    }

    public boolean isGameStatut(GameStatut statut) {
        return this.gameStatut == statut;
    }

    public void setGameStatut(GameStatut statut) {
        this.gameStatut = statut;
    }

    public World getWorld() {
        return world;
    }

    public PlayerDataManager getPlayerDataManager() {
        return playerDataManager;
    }

    public GameRunnable getGameRunnable() {
        return gameRunnable;
    }

    public GameUtils getGameUtils() {
        return gameUtils;
    }

    public TeamManager getTeamManager() {
        return teamManager;
    }

    public void addGamePlayer(Player player) {
        playerList.add(player);
    }

    public void removeGamePlayer(Player player) {
        playerList.remove(player);
    }

    public void removeGamePlayerByName(String playerName) {
        Player player = null;
        for (Player players : getPlayerList()) {
            if (players.getName().equals(playerName)) {
                player = players;
            }
        }
        playerList.remove(player);
    }

    public int getGameDay() {
        return gameDay;
    }

    public void addGameDay() {
        gameDay++;
    }

    public int getDayDuration() {
        return dayDuration;
    }

    public List<Player> getPlayerList() {
        return playerList;
    }

    public void addSpectatorList(Player player) {
        spectatorsList.add(player);
    }

    public void removeSpectatorList(Player player) {
        spectatorsList.remove(player);
    }

    public List<Player> getSpectatorsList() {
        return spectatorsList;
    }

    public Location getLobbySpawn() {
        return lobbySpawn;
    }

    public ScoreboardManager getScoreboardManager() {
        return scoreboardManager;
    }

    public List<Chest> getChestGame() {
        return chestGame;
    }

    public Location getNetherPortal() {
        return netherPortal;
    }

    public Location getEnderPortal() {
        return enderPortal;
    }

    public GameStatut getGameStatut() {
        return gameStatut;
    }

    public List<Location> getBlocksSpawnList() {
        return blocksSpawnList;
    }

    public void kickAllPlayers() {
        for (Player players : Bukkit.getOnlinePlayers()) {
            players.kickPlayer("§cRedémarrage en cours...");
        }
    }

    public void launchGame() {
        setGameStatut(GameStatut.GAME);
        teamManager.generateBeacons();
        teamManager.checkTeamsAlive(false);
        gameUtils.addCustomsCraft();

        for (Player players : Bukkit.getOnlinePlayers()) {
            Team playerTeam = teamManager.getPlayerTeam(players);
            players.closeInventory();
            players.getInventory().clear();
            scoreboardManager.loadGameScoreboard(players);
            if (playerTeam != null) {
                players.setMaxHealth(20);
                spawnPlayer(players, playerTeam);
                players.sendMessage("§aVous avez été téléporté au spawn de votre équipe");
            } else {
                addSpectatorList(players);
                removeGamePlayer(players);
                players.sendMessage("§7Vous avez rejoint la partie en tant que spectateur");
                setSpectatorsEffects(players);
            }
        }
        Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> gameRunnable.runTaskTimer(Main.getInstance(), 0, 20), 20);
        Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> playerRunnable.runTaskTimer(Main.getInstance(), 0, 2), 20);


    }

    public PlayerRunnable getPlayerRunnable() {
        return playerRunnable;
    }

    private void registerAllChests() {
        for (Chunk chunk : world.getLoadedChunks()) {
            for (BlockState blockState : chunk.getTileEntities()) {
                if (blockState instanceof Chest) {
                    Chest chest = (Chest) blockState;
                    chest.getInventory().clear();
                    chestGame.add(chest);
                }
            }
        }
    }

    private void registerAllBlocksSpawn() {
        for (int x = -100; x <= 100; x++) {
            for (int z = -100; z <= 100; z++) {
                blocksSpawnList.add(new Location(getLobbySpawn().getWorld(), (int) getLobbySpawn().getX() + x, 0, (int) getLobbySpawn().getZ() + z));
            }
        }
    }

    public void setSpectatorsEffects(Player player) {
        Inventory inventory = player.getInventory();
        inventory.clear();
        player.setMaxHealth(2);
        player.setHealth(player.getMaxHealth());
        player.setFoodLevel(20);
        player.setGameMode(GameMode.ADVENTURE);
        player.setAllowFlight(true);
        player.setFlying(true);
        inventory.setItem(0, new ItemCreator(Material.COMPASS).setName("§bTéléportation").setLores("§a> §fCliquez pour utiliser").done());
        inventory.setItem(7, new ItemCreator(Material.NETHER_STAR).setName("§cChanger de mode").setLores("§a§l> §fClick to switch mode").done());
        inventory.setItem(8, new ItemCreator(Material.BLAZE_POWDER).setName("§bCachez les spectateurs").setLores("§a§l> §fClick to hide").done());
        Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
            @Override
            public void run() {
                for(Player players : playerList){
                    players.hidePlayer(player);
                }
            }
        },10);


    }

    public void spawnPlayer(Player player, Team playerTeam) {
        player.setHealth(player.getMaxHealth());
        player.setFoodLevel(20);
        player.setGameMode(GameMode.SURVIVAL);
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        player.teleport(playerTeam.getSpawnLocation());
    }


    private void loadFloatingText() {
        removeFloatingText();
        Location textLocation = new Location(world, getLobbySpawn().getX(), getLobbySpawn().getY() + 3, getLobbySpawn().getZ() + 9);
        ArmorStand armorStand = (ArmorStand) world.spawnEntity(textLocation, EntityType.ARMOR_STAND);
        armorStand.setVisible(false);
        armorStand.setCanPickupItems(false);
        armorStand.setGravity(false);
        armorStand.setCustomName("§bBienvenue sur le §bFallen§9Kingdom");
        armorStand.setCustomNameVisible(true);
    }

    private void removeFloatingText() {
        for (Entity entity : world.getEntities()) {
            if (entity instanceof ArmorStand) {
                entity.remove();
            }
        }
    }

    public String getPrefix() {
        return "§bFallen§9Kingdom§8> ";
    }

    public void endGame(Team winner) {
        Bukkit.broadcastMessage("\n§f§ki§6§ki§r §7L'équipe " + winner.getColor() + "§l" + winner.getName() + "§7 remporte la partie §6§ki§f§ki\n");
        Bukkit.broadcastMessage("§e\"/stats\" §7pour voir vos statistiques de jeu \n");
        setGameStatut(GameStatut.END);
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.closeInventory();
            player.playSound(player.getLocation(), Sound.LEVEL_UP, 1f, 1f);
            player.playSound(player.getLocation(), Sound.FIREWORK_LAUNCH, 1f, 1f);
            if (teamManager.getPlayerTeam(player) != null) {
                if (teamManager.getPlayerTeam(player).equals(winner)) {
                    player.sendMessage("§aVotre équipe a gagné la partie");
                } else {
                    player.sendMessage("§cVotre équipe a perdu la partie");
                }
            }
            if (getPlayerList().contains(player)) {
                removeGamePlayerByName(player.getName());
            }
        }
        Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
            @Override
            public void run() {
                for (Player players : Bukkit.getOnlinePlayers()) {
                    players.playSound(players.getLocation(), Sound.FIREWORK_BLAST, 1f, 1f);
                    if (!spectatorsList.contains(players)) {
                        addSpectatorList(players);
                        setSpectatorsEffects(players);
                    }
                }
            }
        }, 20);

    }

    public void reconnectUpdatePlayersData(Player player) {
        PlayerData playerData = playerDataManager.getPlayerDataByPlayerName(player);

        if (playerData != null) {
            playerDataManager.removePlayerData(playerData.getPlayer());
            playerDataManager.addPlayerData(player, playerData);
        }
    }

    public void reconnectUpdateGameList(Player player) {
        Player playerReplaced = null;
        removeGamePlayerByName(player.getName());
        if(!spectatorsList.contains(player)) {
            addGamePlayer(player);
        }
    }

    public void reconnectUpdateTeamPlayer(Player player) {
        Team team = teamManager.getTeamPlayerByName(player);
        if (team != null) {
            team.removePlayerByName(player.getName());
            if(team.isAlive()) {
                team.addPlayer(player);
            }
        }
    }
}
