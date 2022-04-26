package fr.haxy972.fallen.utils;

import org.bukkit.scheduler.BukkitRunnable;

public class RefresherConfig extends BukkitRunnable {
    @Override
    public void run() {
        MessageYaml.checkYaml();

    }
}
