package fr.haxy972.fallenkingdom;

import fr.haxy972.fallenkingdom.commands.CommandTest;
import fr.haxy972.fallenkingdom.commands.GamesCommands;
import fr.haxy972.fallenkingdom.game.GameManager;
import fr.haxy972.fallenkingdom.listeners.GameListener;
import fr.haxy972.fallenkingdom.listeners.GlobalListener;
import fr.haxy972.fallenkingdom.listeners.LobbyListeners;
import fr.haxy972.fallenkingdom.listeners.SpectorListener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private static Main instance;
    private GameManager gameManager = null;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        this.gameManager = new GameManager();
        loadListeners();
    }

    private void loadListeners() {
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new LobbyListeners(gameManager), this);
        pluginManager.registerEvents(new GlobalListener(gameManager), this);
        pluginManager.registerEvents(new GameListener(gameManager), this);
        pluginManager.registerEvents(new SpectorListener(gameManager), this);
        getCommand("test").setExecutor(new CommandTest(gameManager));
        getCommand("stats").setExecutor(new GamesCommands(gameManager));
        getCommand("retour").setExecutor(new GamesCommands(gameManager));
        getCommand("mute").setExecutor(new GamesCommands(gameManager));
        getCommand("team").setExecutor(new GamesCommands(gameManager));
        gameManager.kickAllPlayers();
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public static Main getInstance(){
        return instance;
    }
}
