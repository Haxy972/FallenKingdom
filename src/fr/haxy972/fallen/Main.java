package fr.haxy972.fallen;


import fr.haxy972.fallen.listeners.GameListeners;
import fr.haxy972.fallen.listeners.ResetListeners;
import fr.haxy972.fallen.utils.MessageYaml;
import fr.haxy972.fallen.utils.RefresherConfig;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import fr.haxy972.fallen.commands.CommandsTimer;
import fr.haxy972.fallen.manager.ListenersManager;
import fr.haxy972.fallen.utils.RefresherLobby;

public class Main extends JavaPlugin {


    public static Main INSTANCE;


    @Override
    public void onEnable() {
        INSTANCE = this;
        saveDefaultConfig();
        new ListenersManager(INSTANCE).registerEvent();
        GameListeners.onCraft();
        getCommand("forcestart").setExecutor(new CommandsTimer());
        getCommand("nextday").setExecutor(new CommandsTimer());
        getCommand("previousday").setExecutor(new CommandsTimer());
        getCommand("stats").setExecutor(new CommandsTimer());
        GameStatut.setStatut(GameStatut.LOBBY);

        new RefresherLobby().runTaskTimer(INSTANCE, 0, 20);
        kickAll();
        new RefresherConfig().runTaskTimer(INSTANCE, 0 ,10*20);
        MessageYaml.checkYaml();

        for(int x = -20; x <= 20; x++){
            for(int z = -20; z <= 20; z++) {
                double spawnlocx = Main.getLobbySpawn().getX() + x;
                double spawnlocz = Main.getLobbySpawn().getZ() + z;
                double spawnlocy = Main.getLobbySpawn().getY() - 1;
                Location loc = new Location(Main.getWorld(), spawnlocx, spawnlocy, spawnlocz);
                Block block = loc.getBlock();
                if (block.getType() == Material.STAINED_GLASS) {

                    block.setData((byte) 7);
                }
            }

        }

    }

    public static void kickAll() {
        for (Player players : Bukkit.getOnlinePlayers()) {

            players.kickPlayer(getPrefix() + MessageYaml.getValue("messages.partie.restart").replace("&", "§"));

        }
    }

    @Override
    public void onDisable() {
        for(Chunk chunk : Bukkit.getWorld(Main.getWorld().getName()).getLoadedChunks()){
            for(Entity e : chunk.getEntities()){
                if(e instanceof Item){
                    e.remove();

                }
            }
        }

        ResetListeners.reloadBlocks();
    }

    public static World getWorld() {
        return Bukkit.getWorld(Main.INSTANCE.getConfig().getString("partie.world"));

    }


    public static Location getLobbySpawn() {
        double x = Main.INSTANCE.getConfig().getDouble("locations.lobby.x");
        double y = Main.INSTANCE.getConfig().getDouble("locations.lobby.y");
        double z = Main.INSTANCE.getConfig().getDouble("locations.lobby.z");
        int pitch = Main.INSTANCE.getConfig().getInt("locations.lobby.pitch");
        int yaw = Main.INSTANCE.getConfig().getInt("locations.lobby.yaw");



        return new Location(getWorld(), x, y, z, yaw, pitch);

    }

    public static String getPrefix() {



        return INSTANCE.getConfig().getString("messages.prefix").replace("&","§");

    }

    public static Location getSpawnRed() {
        double x = Main.INSTANCE.getConfig().getDouble("locations.spawn-rouge.x");
        double y = Main.INSTANCE.getConfig().getDouble("locations.spawn-rouge.y");
        double z = Main.INSTANCE.getConfig().getDouble("locations.spawn-rouge.z");
        int pitch = Main.INSTANCE.getConfig().getInt("locations.spawn-rouge.pitch");
        int yaw = Main.INSTANCE.getConfig().getInt("locations.spawn-rouge.yaw");

        return new Location(Main.getWorld(), x, y, z, yaw, pitch);
    }

    public static Location getSpawnBlue() {
        double x = Main.INSTANCE.getConfig().getDouble("locations.spawn-bleu.x");
        double y = Main.INSTANCE.getConfig().getDouble("locations.spawn-bleu.y");
        double z = Main.INSTANCE.getConfig().getDouble("locations.spawn-bleu.z");
        int pitch = Main.INSTANCE.getConfig().getInt("locations.spawn-bleu.pitch");
        int yaw = Main.INSTANCE.getConfig().getInt("locations.spawn-bleu.yaw");

        return new Location(Main.getWorld(), x, y, z, yaw, pitch);
    }

    public static Location getSpawnGreen() {
        double x = Main.INSTANCE.getConfig().getDouble("locations.spawn-vert.x");
        double y = Main.INSTANCE.getConfig().getDouble("locations.spawn-vert.y");
        double z = Main.INSTANCE.getConfig().getDouble("locations.spawn-vert.z");
        int pitch = Main.INSTANCE.getConfig().getInt("locations.spawn-vert.pitch");
        int yaw = Main.INSTANCE.getConfig().getInt("locations.spawn-vert.yaw");
        return new Location(Main.getWorld(), x, y, z, yaw, pitch);
    }

    public static Location getSpawnYellow() {
        double x = Main.INSTANCE.getConfig().getDouble("locations.spawn-jaune.x");
        double y = Main.INSTANCE.getConfig().getDouble("locations.spawn-jaune.y");
        double z = Main.INSTANCE.getConfig().getDouble("locations.spawn-jaune.z");
        int pitch = Main.INSTANCE.getConfig().getInt("locations.spawn-jaune.pitch");
        int yaw = Main.INSTANCE.getConfig().getInt("locations.spawn-jaune.yaw");


        return new Location(Main.getWorld(), x, y, z, yaw, pitch);
    }

    //EMPLACEMENT BEACON
    public static Location getBeaconRed() {
        double x = Main.INSTANCE.getConfig().getDouble("locations.beacon-rouge.x");
        double y = Main.INSTANCE.getConfig().getDouble("locations.beacon-rouge.y");
        double z = Main.INSTANCE.getConfig().getDouble("locations.beacon-rouge.z");


        return new Location(Main.getWorld(), x, y, z);
    }

    public static Location getBeaconBlue() {
        double x = Main.INSTANCE.getConfig().getDouble("locations.beacon-bleu.x");
        double y = Main.INSTANCE.getConfig().getDouble("locations.beacon-bleu.y");
        double z = Main.INSTANCE.getConfig().getDouble("locations.beacon-bleu.z");
        return new Location(Main.getWorld(), x, y, z);
    }


    public static Location getBeaconGreen() {
        double x = Main.INSTANCE.getConfig().getDouble("locations.beacon-vert.x");
        double y = Main.INSTANCE.getConfig().getDouble("locations.beacon-vert.y");
        double z = Main.INSTANCE.getConfig().getDouble("locations.beacon-vert.z");
        return new Location(Main.getWorld(), x, y, z);
    }

    public static Location getBeaconYellow() {
        double x = Main.INSTANCE.getConfig().getDouble("locations.beacon-jaune.x");
        double y = Main.INSTANCE.getConfig().getDouble("locations.beacon-jaune.y");
        double z = Main.INSTANCE.getConfig().getDouble("locations.beacon-jaune.z");
        return new Location(Main.getWorld(), x, y, z);
    }
}
