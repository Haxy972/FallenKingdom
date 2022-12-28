package fr.haxy972.fallenkingdom.listeners;

import fr.haxy972.fallenkingdom.Main;
import fr.haxy972.fallenkingdom.data.PlayerData;
import fr.haxy972.fallenkingdom.data.PlayerDataManager;
import fr.haxy972.fallenkingdom.game.Days;
import fr.haxy972.fallenkingdom.game.GameManager;
import fr.haxy972.fallenkingdom.game.GameStatut;
import fr.haxy972.fallenkingdom.runnables.DeathRunnable;
import fr.haxy972.fallenkingdom.teams.Team;
import fr.haxy972.fallenkingdom.teams.TeamManager;
import fr.haxy972.fallenkingdom.utils.ItemCreator;
import fr.haxy972.fallenkingdom.utils.TitleManager;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.event.world.PortalCreateEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.text.DecimalFormat;
import java.util.*;

public class GameListener implements Listener {

    private final GameManager gameManager;
    private final static Map<Player, Map<Integer, Player>> lastDamager = new HashMap<>();
    private final PlayerDataManager playerDataManager;
    private final List<Location> blocksPlaced = new ArrayList<>();

    public GameListener(GameManager gameManager) {
        this.gameManager = gameManager;
        this.playerDataManager = gameManager.getPlayerDataManager();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (gameManager.isGameStatut(GameStatut.LOBBY)) return;
        Player player = event.getPlayer();
        TeamManager teamManager = gameManager.getTeamManager();
        gameManager.getScoreboardManager().loadGameScoreboard(player);
        gameManager.reconnectUpdatePlayersData(player);
        for (Team team : teamManager.getTeamList()) {
            if (team.isAlive()) {
                Player playerToRemove = (Player) team.getPlayerByName(player.getName());
                if (playerToRemove != null) {
                    if (team.isAlive()) {
                        team.removePlayer(playerToRemove);
                        team.addPlayer(player);
                        Bukkit.broadcastMessage(team.getColor() + player.getName() + " §7s'est reconnecté");
                        player.teleport(team.getSpawnLocation());
                        TitleManager.sendTitle(player, "§cMort", "§7Vous allez bientôt réapparaître", 20);
                        player.setGameMode(GameMode.SPECTATOR);
                        new DeathRunnable(gameManager, player).runTaskTimer(Main.getInstance(), 0, 20);
                    }
                }
            }
        }
        if (teamManager.getPlayerTeam(player) == null) {
            gameManager.addSpectatorList(player);
            gameManager.setSpectatorsEffects(player);
            Bukkit.broadcastMessage(player.getName() + " §7regarde la partie");
            for (Player players : gameManager.getPlayerList()) {
                players.hidePlayer(player);
            }
        }
        gameManager.reconnectUpdateGameList(player);
        gameManager.reconnectUpdateTeamPlayer(player);

    }

    @EventHandler
    public void onDamageByEntity(EntityDamageByEntityEvent event) {

        if (event.getDamager() instanceof Arrow) {
            Arrow arrow = (Arrow) event.getDamager();
            if (arrow.getShooter() instanceof Player) {
                Player player = (Player) arrow.getShooter();
                if (gameManager.getGameDay() >= Days.PVP.getDay()) {
                    if (event.getEntity() instanceof Player) {
                        Player victim = (Player) event.getEntity();
                        DecimalFormat df = new DecimalFormat("0.0");

                        Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
                            @Override
                            public void run() {
                                String damage = df.format((float) (victim.getHealth() / 2));
                                if(victim.getGameMode().equals(GameMode.SPECTATOR)) damage = "0.0";
                                player.sendMessage("§eVous avez touché §b" + victim.getName() + "§e, point de vie: §c" + damage + " §c♥");
                            }
                        }, 2);

                        return;
                    }
                } else {
                    player.sendMessage("§cCombats Inactifs §8> §b" + Days.PVP.getDay() + " ème jour");
                    event.setCancelled(true);
                }
            }
        }

        if (gameManager.isGameStatut(GameStatut.LOBBY) || !(event.getEntity() instanceof Player) || !(event.getDamager() instanceof Player)) {
            return;
        }
        Player victim = (Player) event.getEntity();
        Player attacker = (Player) event.getDamager();
        TeamManager teamManager = gameManager.getTeamManager();
        if (gameManager.getSpectatorsList().contains(attacker)) {
            event.setCancelled(true);
            return;
        }
        if (teamManager.getPlayerTeam(attacker).equals(teamManager.getPlayerTeam(victim))) {
            event.setCancelled(true);
            return;
        }

        if (gameManager.getGameDay() < Days.PVP.getDay()) {
            attacker.sendMessage("§cCombats Inactifs §8> §b" + Days.PVP.getDay() + " ème jour");
            event.setCancelled(true);
            return;
        }

        Random random = new Random();
        int damageIndex = random.nextInt(200000);
        Map<Integer, Player> victimDamagerMap = (lastDamager.containsKey(victim) ? lastDamager.get(victim) : new HashMap<>());
        victimDamagerMap.put(damageIndex, attacker);
        lastDamager.put(victim, victimDamagerMap);
        Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
            @Override
            public void run() {
                Map<Integer, Player> victimDamagerMap = lastDamager.get(victim);
                victimDamagerMap.remove(damageIndex);
                if (victimDamagerMap.keySet().size() == 0) {
                    lastDamager.remove(victim);
                }
            }
        }, 100);


    }



    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (gameManager.isGameStatut(GameStatut.LOBBY) || !(event.getEntity() instanceof Player)) {
            return;
        }
        if (event.isCancelled()) return;
        if(gameManager.isGameStatut(GameStatut.END)){
            event.setCancelled(true);
            return;
        }
        Player victim = (Player) event.getEntity();
        PlayerData victimData = playerDataManager.getPlayerData(victim);
        TeamManager teamManager = gameManager.getTeamManager();
        if (event.getFinalDamage() >= victim.getHealth()) {
            victim.setHealth(victim.getMaxHealth());
            event.setDamage(0);
            Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
                @Override
                public void run() {
                    if (lastDamager.containsKey(victim)) {
                        List<Player> attackers = new ArrayList<>();
                        for (Player attacker : lastDamager.get(victim).values()) {
                            if (!attackers.contains(attacker)) attackers.add(attacker);
                        }
                        StringBuilder stringBuilder = new StringBuilder();
                        for (Player attacker : attackers) {
                            if (attackers.get(attackers.size() - 1) != attacker) {
                                stringBuilder.append(teamManager.getPlayerTeam(attacker).getColor() + attacker.getName() + (attackers.get(attackers.size() - 2) == attacker ? "" : "§7, "));
                            } else {
                                if (attackers.size() > 1) {
                                    stringBuilder.append(" §7et ");
                                }
                                stringBuilder.append(teamManager.getPlayerTeam(attacker).getColor() + attacker.getName());
                            }
                        }
                        Bukkit.broadcastMessage(teamManager.getPlayerTeam(victim).getColor() + victim.getName() + "§7 a été tué par " + stringBuilder);

                        for (Player attacker : attackers) {
                            PlayerData attackerData = playerDataManager.getPlayerData(attacker);
                            attackerData.addKill();
                            gameManager.getScoreboardManager().updateGameKillCount(attacker);
                        }
                    } else {
                        if(gameManager.getTeamManager().getPlayerTeam(victim) != null) {
                            Bukkit.broadcastMessage(gameManager.getTeamManager().getPlayerTeam(victim).getColor() + victim.getName() + "§7 est mort");
                        }else{
                            victim.teleport(gameManager.getLobbySpawn());
                        }
                    }

                    victimData.addDeath();
                    for (ItemStack itemStack : victim.getInventory()) {
                        if (itemStack != null) {
                            if (!itemStack.getType().equals(Material.AIR)) {
                                gameManager.getWorld().dropItemNaturally(victim.getLocation(), itemStack);
                            }
                        }
                    }
                    for (ItemStack itemStack : victim.getInventory().getArmorContents()) {
                        if (itemStack != null) {
                            if (!itemStack.getType().equals(Material.AIR)) {
                                gameManager.getWorld().dropItemNaturally(victim.getLocation(), itemStack);
                            }
                        }
                    }
                    TitleManager.sendTitle(victim, "§cMort", "§7Vous allez bientôt réapparaître", 20);
                    if (!teamManager.getPlayerTeam(victim).isAlive()) {
                        gameManager.removeGamePlayer(victim);
                        gameManager.addSpectatorList(victim);
                        gameManager.setSpectatorsEffects(victim);
                        victim.sendMessage("§cRéapparition annulé...votre royaume a été détruit");
                        Bukkit.broadcastMessage(teamManager.getPlayerTeam(victim).getColor() + victim.getName() + " §7 est éliminé");
                        gameManager.getScoreboardManager().updateTeamAlive();
                        return;
                    }
                    Vector vector = new Vector(0, 0, 0).normalize();
                    gameManager.getScoreboardManager().updateGameKillCount(victim);
                    victim.setVelocity(vector);
                    if (!victim.getGameMode().equals(GameMode.SPECTATOR)) {
                        victim.setGameMode(GameMode.SPECTATOR);
                        new DeathRunnable(gameManager, victim).runTaskTimer(Main.getInstance(), 0, 20);
                    }
                    victim.getInventory().clear();
                    victim.closeInventory();
                    if (!victim.getWorld().equals(gameManager.getWorld())) {
                        victim.teleport(teamManager.getPlayerTeam(victim).getSpawnLocation());
                    }
                }
            }, 1 / 10000000);

        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (gameManager.isGameStatut(GameStatut.LOBBY)) {
            return;
        }
        Player player = event.getPlayer();
        Block block = event.getBlock();
        Location blockLocation = event.getBlock().getLocation();

        if (locationInArea(blockLocation, getPortalArea())) {
            event.setCancelled(true);
            Bukkit.broadcastMessage("§cVous ne pouvez pas poser de blocs ici");
        }

        for (Team teams : gameManager.getTeamManager().getTeams()) {
            if (locationInArea(blockLocation, gameManager.getTeamManager().getTeamArea(teams, 5))) {
                if (teams != gameManager.getTeamManager().getPlayerTeam(player)) {
                    if (!block.getType().equals(Material.TNT)) {
                        event.setCancelled(true);
                        player.sendMessage("§cVous ne pouvez poser que de la §e§lTNT§c dans les bases ennemies");
                    }
                } else if (block.getType().equals(Material.STAINED_GLASS) || block.getType().equals(Material.IRON_BLOCK) || block.getType().equals(Material.BEACON)) {
                    player.sendMessage("§cVous ne pouvez pas poser ce bloc");
                    event.setCancelled(true);
                } else if (block.getType().equals(Material.TNT)) {
                    event.setCancelled(true);
                    player.sendMessage("§cVous ne pouvez pas poser de tnt dans votre base");
                }
            }
        }
        if (!event.isCancelled()) blocksPlaced.add(blockLocation);
    }

    @EventHandler
    public void onFood(FoodLevelChangeEvent event) {
        if (gameManager.isGameStatut(GameStatut.LOBBY)) {
            return;
        }
        Player player = (Player) event.getEntity();
        if (gameManager.getGameDay() < Days.HUNGER.getDay()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        if (gameManager.isGameStatut(GameStatut.LOBBY)) {
            return;
        }
        Player player = event.getPlayer();
        Team team = gameManager.getTeamManager().getPlayerTeam(player);
        if (team != null) {
            if (!team.isAlive()) {
                Bukkit.broadcastMessage(team.getColor() + player.getName() + " §7est éliminé");
            } else {
                Bukkit.broadcastMessage(team.getColor() + player.getName() + " §7est mort");
            }
            for (ItemStack items : player.getInventory().getContents()) {
                if (items != null) {
                    player.getWorld().dropItemNaturally(player.getLocation(), items);
                }
            }
            player.getInventory().clear();

        }
        Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> gameManager.getTeamManager().checkTeamsAlive(true), 5);


    }

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        Entity entity = event.getEntity();
        if (entity.getType().equals(EntityType.CREEPER)) {
            Creeper creeper = (Creeper) entity;
            creeper.setPowered(true);
        }
    }

    @EventHandler
    public void onCreeperExplode(EntityExplodeEvent event) {
        Entity entity = event.getEntity();
        if (entity.getType().equals(EntityType.CREEPER)) {
            for (Block block : event.blockList()) {
                Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
                    final Material mat = block.getType();
                    final byte matid = block.getData();

                    @Override
                    public void run() {
                        block.getLocation().getBlock().setType(mat);
                        block.getLocation().getBlock().setData(matid);
                        for (Entity entities : event.getLocation().getChunk().getEntities()) {
                            if (entities instanceof Item) {
                                entities.remove();
                            }
                        }
                    }
                }, 1 / 1000000000);

            }
        }
    }


    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (gameManager.isGameStatut(GameStatut.LOBBY)) {
            return;
        }
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();

        if (block != null) {
            if (block.getType().equals(Material.CHEST)) {
                Chest chest = (Chest) block.getState();
                if(gameManager.getChestGame().contains(chest)) {
                    if (getChestItems(chest).size() == 0) {
                        event.setCancelled(true);
                        player.sendMessage("§cCe coffre est vide");
                    }
                }
            }
        }
    }

    public List<ItemStack> getChestItems(Chest chest) {
        List<ItemStack> itemsList = new ArrayList<>();
        for (ItemStack items : chest.getInventory().getContents()) {
            if (items != null) {
                if (!items.getType().equals(Material.AIR)) {
                    itemsList.add(items);
                }
            }
        }
        return itemsList;
    }

    @EventHandler
    public void onPortalCreate(PortalCreateEvent event) {
        if (gameManager.isGameStatut(GameStatut.LOBBY)) {
            return;
        }
        if (gameManager.getGameDay() < Days.PORTALS.getDay()) {
            event.setCancelled(true);
        } else if (event.getBlocks().get(0).getLocation().getX() != gameManager.getNetherPortal().getX() && event.getBlocks().get(0).getLocation().getZ() != gameManager.getNetherPortal().getZ()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlocksExplosion(EntityExplodeEvent event) {
        if (gameManager.isGameStatut(GameStatut.LOBBY)) {
            return;
        }

        for (Block block : event.blockList()) {
            if (block.getType() == Material.BEACON || block.getType() == Material.STAINED_GLASS || block.getType().equals(Material.IRON_BLOCK)) {
                Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
                    final Material mat = block.getType();
                    final byte matid = block.getData();

                    @Override
                    public void run() {
                        block.getLocation().getBlock().setType(mat);
                        block.getLocation().getBlock().setData(matid);

                    }
                }, 1 / 1000000000);
            }
        }
        Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
            @Override
            public void run() {
                for (Entity entity : event.getLocation().getChunk().getEntities()) {
                    if (entity instanceof Item) {
                        entity.remove();
                    }
                }
            }
        }, 20);
    }


    @EventHandler
    public void entityDeathEvent(EntityDeathEvent event) {
        if (gameManager.isGameStatut(GameStatut.LOBBY)) {
            return;
        }
        Entity entity = event.getEntity();
        switch (entity.getType()) {
            case CREEPER:
                Creeper creeper = (Creeper) entity;
                if (creeper.isPowered()) {
                    event.getDrops().clear();
                    List<ItemStack> drops = getPercentDropItem(40, new ItemCreator(Material.TNT).done());
                    if (drops != null) {
                        dropItems(drops, entity.getLocation());
                    }
                }
                break;
            case ENDERMAN:
                event.getDrops().clear();
                List<ItemStack> drops = getPercentDropItem(30, new ItemCreator(Material.ENDER_PEARL).done());
                if (drops != null) {
                    dropItems(drops, entity.getLocation());
                }
                Random random = new Random();
                Location location = new Location(entity.getLocation().getWorld(), entity.getLocation().getX() + random.nextInt(30), entity.getLocation().getY(), entity.getLocation().getZ() + random.nextInt(30));
                location.getWorld().spawnEntity(location, EntityType.ENDERMAN);
                break;
        }

    }

    private void dropItems(List<ItemStack> items, Location location) {
        for (ItemStack item : items) {
            location.getWorld().dropItemNaturally(location, item);
        }
    }

    private List<ItemStack> getPercentDropItem(int percent, ItemStack... droppedItem) {
        Random random = new Random();
        List<ItemStack> droppedItems = Arrays.asList(droppedItem);
        int randPercent = random.nextInt(100);
        if (randPercent <= percent) {
            return droppedItems;
        }
        return null;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (gameManager.isGameStatut(GameStatut.LOBBY)) {
            return;
        }
        Player player = event.getPlayer();
        Block block = event.getBlock();
        Location blockLocation = event.getBlock().getLocation();

        for (Chest chest : gameManager.getChestGame()) {
            if (chest.getLocation().equals(blockLocation)) {
                event.setCancelled(true);
                player.sendMessage("§cVous ne pouvez pas cassez ce coffre");
            }
        }


        if (locationInArea(blockLocation, gameManager.getBlocksSpawnList())) {
            if (!blocksPlaced.contains(blockLocation)) {
                event.setCancelled(true);

            }
        }

        for (Team teams : gameManager.getTeamManager().getTeams()) {
            if (locationInArea(blockLocation, gameManager.getTeamManager().getTeamArea(teams, 5))) {
                if (teams != gameManager.getTeamManager().getPlayerTeam(player)) {
                    if (!block.getType().equals(Material.TNT) && !block.getType().equals(Material.BEACON)) {
                        event.setCancelled(true);
                        player.sendMessage("§cVous ne pouvez pas casser de bloc ici");
                    } else if (block.getType().equals(Material.BEACON)) {
                        if (player.getItemInHand().getType().equals(Material.DIAMOND_PICKAXE)) {
                            gameManager.getTeamManager().killTeam(teams, player, true);
                            event.setCancelled(true);
                            blockLocation.getBlock().setType(Material.AIR);
                        } else {
                            player.sendMessage("§cLe nexus de la base adverse ne peut être cassé qu'avec §e§lune PIOCHE EN DIAMANT");
                            event.setCancelled(true);
                        }
                    }
                } else if (block.getType().equals(Material.STAINED_GLASS) || block.getType().equals(Material.IRON_BLOCK) || block.getType().equals(Material.BEACON)) {
                    player.sendMessage("§cVous ne pouvez pas casser ce bloc");
                    event.setCancelled(true);
                }
            }
        }
        if (!event.isCancelled()) blocksPlaced.remove(blockLocation);
    }

    private List<Location> getPortalArea() {
        List<Location> portalArea = new ArrayList<>();
        for (int x = -5; x <= 5; x++) {
            for (int z = -5; z <= 5; z++) {
                portalArea.add(new Location(gameManager.getNetherPortal().getWorld(), gameManager.getNetherPortal().getX() + x, 0, gameManager.getNetherPortal().getZ() + z));
                portalArea.add(new Location(gameManager.getEnderPortal().getWorld(), gameManager.getEnderPortal().getX() + x, 0, gameManager.getEnderPortal().getZ() + z));
            }
        }
        return portalArea;
    }

    private boolean locationInArea(Location eventLocation, List<Location> locationToLoop) {
        for (Location locations : locationToLoop) {
            if (locations.getX() == eventLocation.getX() && locations.getZ() == eventLocation.getZ() && eventLocation.getWorld() == locations.getWorld()) {
                return true;
            }
        }
        return false;
    }


    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (gameManager.isGameStatut(GameStatut.LOBBY)) {
            return;
        }
        Player player = event.getPlayer();
        if (gameManager.getSpectatorsList().contains(player)) return;
        Location playerLocInteger = new Location(player.getWorld(), (int) player.getLocation().getX(), (int) player.getLocation().getY(), (int) player.getLocation().getZ(), player.getLocation().getYaw(), player.getLocation().getPitch());
        if (gameManager.getGameDay() < Days.ASSAULT.getDay()) {
            for (Team teams : gameManager.getTeamManager().getTeams()) {
                if (locationInArea(playerLocInteger, gameManager.getTeamManager().getTeamArea(teams, 5))) {
                    if (teams != gameManager.getTeamManager().getPlayerTeam(player)) {
                        Location teleportLocation = new Location(playerLocInteger.getWorld(), playerLocInteger.getX() - 5, teams.getSpawnLocation().getY() + 2, playerLocInteger.getZ(), playerLocInteger.getYaw(), playerLocInteger.getPitch());
                        if (locationInArea(teleportLocation, gameManager.getTeamManager().getTeamArea(teams, 5))) {
                            teleportLocation = new Location(playerLocInteger.getWorld(), playerLocInteger.getX(), teams.getSpawnLocation().getY() + 2, playerLocInteger.getZ() - 5, playerLocInteger.getYaw(), playerLocInteger.getPitch());
                        }
                        if (locationInArea(teleportLocation, gameManager.getTeamManager().getTeamArea(teams, 5))) {
                            teleportLocation = new Location(playerLocInteger.getWorld(), playerLocInteger.getX(), teams.getSpawnLocation().getY() + 2, playerLocInteger.getZ() + 5, playerLocInteger.getYaw(), playerLocInteger.getPitch());
                        }
                        if (locationInArea(teleportLocation, gameManager.getTeamManager().getTeamArea(teams, 5))) {
                            teleportLocation = new Location(playerLocInteger.getWorld(), playerLocInteger.getX() + 5, teams.getSpawnLocation().getY() + 2, playerLocInteger.getZ(), playerLocInteger.getYaw(), playerLocInteger.getPitch());
                        }
                        player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1f, 1f);
                        player.sendMessage("§cPillages Inactifs §8> §b" + Days.ASSAULT.getDay() + " ème jour");
                        player.teleport(teleportLocation);

                    }
                }
            }
        }
    }
}
