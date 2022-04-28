package fr.haxy972.fallen.runnable;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import fr.haxy972.fallen.Main;
import fr.haxy972.fallen.Team.select.TeamSelect;
import fr.haxy972.fallen.utils.TitleManager;

public class DeathRunnable extends BukkitRunnable {
    int timer = 11;
    private Player player;

    public DeathRunnable(Player player) {
        this.player = player;
    }


    @Override
    public void run() {
        timer--;
        if (timer == 0) {
            TitleManager.sendActionBar(player, "§eRéapparition en cours");
        } else {
            TitleManager.sendActionBar(player, "§eRéapparition dans " + timer + getSeconds(timer));
        }


        if (TeamSelect.team.get(player).equalsIgnoreCase("rouge")) {
            if (!daysRunnable.redbedalive) {
                player.sendMessage(Main.getPrefix() + "§cVous ne pouvez plus réapparaitre, votre nexus est détruit");
                Main.INSTANCE.getServer().dispatchCommand(Main.INSTANCE.getServer().getConsoleSender(), "manuadd " + player.getName() + " mort");
                TeamSelect.team.remove(player);
                this.cancel();
            }
        } else if (TeamSelect.team.get(player).equalsIgnoreCase("bleu")) {
            if (!daysRunnable.bluebedalive) {
                player.sendMessage(Main.getPrefix() + "§cVous ne pouvez plus réapparaitre, votre nexus est détruit");
                Main.INSTANCE.getServer().dispatchCommand(Main.INSTANCE.getServer().getConsoleSender(), "manuadd " + player.getName() + " mort");
                TeamSelect.team.remove(player);
                this.cancel();
            }

        } else if (TeamSelect.team.get(player).equalsIgnoreCase("vert")) {
            if (!daysRunnable.greenbedalive) {
                player.sendMessage(Main.getPrefix() + "§cVous ne pouvez plus réapparaitre, votre nexus est détruit");
                Main.INSTANCE.getServer().dispatchCommand(Main.INSTANCE.getServer().getConsoleSender(), "manuadd " + player.getName() + " mort");
                TeamSelect.team.remove(player);
                this.cancel();
            }
        } else if (TeamSelect.team.get(player).equalsIgnoreCase("jaune")) {
            if (!daysRunnable.yellowbedalive) {
                player.sendMessage(Main.getPrefix() + "§cVous ne pouvez plus réapparaitre, votre nexus est détruit");
                Main.INSTANCE.getServer().dispatchCommand(Main.INSTANCE.getServer().getConsoleSender(), "manuadd " + player.getName() + " mort");
                TeamSelect.team.remove(player);
                this.cancel();
            }
        }


        if (timer == 0) {
            if (TeamSelect.team.get(player).equalsIgnoreCase("rouge")) {
                if (daysRunnable.redbedalive) {
                    player.teleport(Main.getSpawnRed());
                } else {
                    player.sendMessage("§c§lERREUR§8» §7Votre nexus a été détruit, réapparition impossible");
                    Main.INSTANCE.getServer().dispatchCommand(Main.INSTANCE.getServer().getConsoleSender(), "manuadd " + player.getName() + " mort");
                    TeamSelect.team.remove(player);
                }
            }
            if (TeamSelect.team.get(player).equalsIgnoreCase("bleu")) {
                if (daysRunnable.bluebedalive) {
                    player.teleport(Main.getSpawnBlue());
                } else {
                    player.sendMessage("§c§lERREUR§8» §7Votre nexus a été détruit, réapparition impossible");
                    Main.INSTANCE.getServer().dispatchCommand(Main.INSTANCE.getServer().getConsoleSender(), "manuadd " + player.getName() + " mort");
                    TeamSelect.team.remove(player);
                }
            }
            if (TeamSelect.team.get(player).equalsIgnoreCase("vert")) {
                if (daysRunnable.greenbedalive) {
                    player.teleport(Main.getSpawnGreen());
                } else {
                    player.sendMessage("§c§lERREUR§8» §7Votre nexus a été détruit, réapparition impossible");
                    Main.INSTANCE.getServer().dispatchCommand(Main.INSTANCE.getServer().getConsoleSender(), "manuadd " + player.getName() + " mort");
                    TeamSelect.team.remove(player);
                }
            }
            if (TeamSelect.team.get(player).equalsIgnoreCase("jaune")) {
                if (daysRunnable.yellowbedalive) {
                    player.teleport(Main.getSpawnYellow());
                } else {
                    player.sendMessage("§c§lERREUR§8» §7Votre nexus a été détruit, réapparition impossible");
                    Main.INSTANCE.getServer().dispatchCommand(Main.INSTANCE.getServer().getConsoleSender(), "manuadd " + player.getName() + " mort");
                    TeamSelect.team.remove(player);
                }
            }
            player.setGameMode(GameMode.SURVIVAL);
            this.cancel();
        }
    }


    private String getSeconds(int timer) {
        if (timer == 1 || timer == 0) {
            return " seconde";
        } else {
            return " secondes";
        }

    }

}
