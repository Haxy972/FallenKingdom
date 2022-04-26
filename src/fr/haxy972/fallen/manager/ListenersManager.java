package fr.haxy972.fallen.manager;

import fr.haxy972.fallen.listeners.ResetListeners;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import fr.haxy972.fallen.listeners.GameListeners;
import fr.haxy972.fallen.listeners.LobbyListeners;
import fr.haxy972.fallen.listeners.onPlayerJoinListener;


public class ListenersManager {

    private Plugin plugin;
    private PluginManager pluginManager;

    public ListenersManager(Plugin plugin) {
        this.plugin = plugin;
        this.pluginManager = plugin.getServer().getPluginManager();
    }


    public void registerEvent() {

        this.pluginManager.registerEvents(new onPlayerJoinListener(), plugin);
        this.pluginManager.registerEvents(new LobbyListeners(), plugin);
        this.pluginManager.registerEvents(new GameListeners(), plugin);
        this.pluginManager.registerEvents(new ResetListeners(), plugin);
    }


}
