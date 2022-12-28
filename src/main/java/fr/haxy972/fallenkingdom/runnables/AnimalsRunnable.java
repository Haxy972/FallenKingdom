package fr.haxy972.fallenkingdom.runnables;

import fr.haxy972.fallenkingdom.game.GameManager;
import fr.haxy972.fallenkingdom.teams.Team;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AnimalsRunnable extends BukkitRunnable {

    private final GameManager gameManager;

    public AnimalsRunnable(GameManager gameManager){
        this.gameManager = gameManager;
    }

    @Override
    public void run() {
        for(Team teams : gameManager.getTeamManager().getTeams()){
            Random random = new Random();
            List<EntityType> entityTypeList = new ArrayList<>();
            entityTypeList.add(EntityType.COW);
            entityTypeList.add(EntityType.SHEEP);
            entityTypeList.add(EntityType.CHICKEN);
            for(int i = 0; i <= 3; i++) {
                int incrementation = (random.nextInt(10) > 5 ? random.nextInt(100) : - random.nextInt(100));
                Location location = new Location(teams.getSpawnLocation().getWorld(), teams.getSpawnLocation().getX() + 20 + incrementation, teams.getSpawnLocation().getY(), teams.getSpawnLocation().getZ() + 20 + incrementation);
                location.getWorld().spawnEntity(location, entityTypeList.get(random.nextInt(entityTypeList.size())));
            }
        }
    }
}
