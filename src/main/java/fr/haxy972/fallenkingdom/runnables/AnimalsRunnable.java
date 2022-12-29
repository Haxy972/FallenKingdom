package fr.haxy972.fallenkingdom.runnables;

import fr.haxy972.fallenkingdom.game.GameManager;
import fr.haxy972.fallenkingdom.game.GameStatut;
import fr.haxy972.fallenkingdom.teams.Team;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.*;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AnimalsRunnable extends BukkitRunnable {

    private final GameManager gameManager;

    public AnimalsRunnable(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public void run() {
        int animalsCount = 0;
        for (Entity entity : gameManager.getWorld().getEntities()) {
            if (entity instanceof Sheep || entity instanceof Cow || entity instanceof Chicken) {
                animalsCount++;
            }
        }
        Bukkit.broadcastMessage("§e" + animalsCount);
        if (animalsCount <= 100) {
            List<Location> locationsList = new ArrayList<>();
            for (Team teams : gameManager.getTeamManager().getTeams()) {
                locationsList.add(teams.getSpawnLocation());
            }
            locationsList.add(gameManager.getLobbySpawn());
            for (Chest chest : gameManager.getChestGame()) {
                locationsList.add(chest.getLocation());
            }
            List<EntityType> entityTypeList = new ArrayList<>();
            entityTypeList.add(EntityType.COW);
            entityTypeList.add(EntityType.SHEEP);
            entityTypeList.add(EntityType.CHICKEN);
            for (Location location : locationsList) {
                Random random = new Random();
                for (int i = 0; i <= 3; i++) {
                    int incrementation_x = (random.nextInt(10) > 5 ? random.nextInt(200) : -random.nextInt(200));
                    int incrementation_z = (random.nextInt(10) > 5 ? random.nextInt(200) : -random.nextInt(200));
                    Location locationSpawn = new Location(location.getWorld(), location.getX() + 30 + incrementation_x, location.getY(), location.getZ() + 30 + incrementation_z);
                    Location underBlock = new Location(locationSpawn.getWorld(), locationSpawn.getX(), locationSpawn.getY() - 1, locationSpawn.getZ());
                    if (locationSpawn.getBlock().getType().equals(Material.AIR)) {
                        while (underBlock.getBlock().getType().equals(Material.AIR)) {
                            underBlock.setY(underBlock.getY() - 1);
                            if (underBlock.getY() <= 0) {
                                break;
                            }
                        }
                    } else {
                        while (underBlock.getBlock().getType().equals(Material.AIR)) {
                            underBlock.setY(underBlock.getY() + 1);
                            if (underBlock.getY() >= 256) {
                                break;
                            }
                        }
                    }
                    underBlock.setY(underBlock.getY() + 1);
                    locationSpawn = underBlock;

                    boolean isArea = false;
                    for (Team teams : gameManager.getTeamManager().getTeams()) {
                        if (gameManager.getGameUtils().locationInArea(locationSpawn, gameManager.getTeamManager().getTeamArea(teams, 10))) {
                            isArea = true;
                        }
                    }
                    if (!isArea) {
                        locationSpawn.getWorld().spawnEntity(locationSpawn, entityTypeList.get(random.nextInt(entityTypeList.size())));
                    }
                    Bukkit.broadcastMessage(locationSpawn.toString());
                }
            }
        }
    }
}
