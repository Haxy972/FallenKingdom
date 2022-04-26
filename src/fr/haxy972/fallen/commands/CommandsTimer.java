package fr.haxy972.fallen.commands;

import fr.haxy972.fallen.GameStatut;
import fr.haxy972.fallen.runnable.daysRunnable;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.haxy972.fallen.Main;
import fr.haxy972.fallen.runnable.LobbyRunnable;

public class CommandsTimer implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }
        Player player = (Player) sender;
        if (cmd.getName().equalsIgnoreCase("forcestart")) {
            if(!GameStatut.isStatut(GameStatut.LOBBY)){
                player.sendMessage("§c§lERREUR§8» §7La partie n'est pas en phase de lobby");
                return false;
            }

            if(!LobbyRunnable.alreadytimer){

                Bukkit.broadcastMessage(Main.getPrefix() + "§aPartie en cours de lancement...");
                LobbyRunnable.alreadytimer = true;


                Bukkit.getScheduler().runTaskLater(Main.INSTANCE, new Runnable() {
                    @Override
                    public void run() {
                        new LobbyRunnable().runTaskTimer(Main.INSTANCE, 0, 20);
                    }
                }, 2*20);



            }else{
                player.sendMessage("§c§lERREUR§8» §7La partie est déjà en cours de lancement");
            }


        }
        if (cmd.getName().equalsIgnoreCase("nextday")) {
            if(!GameStatut.isStatut(GameStatut.GAME)){
                player.sendMessage("§c§lERREUR§8» §7La partie n'est pas en cours d'exécution");
                return false;
            }
            if(args.length == 0){
                daysRunnable.day ++;
                daysRunnable.newDayComing();

            }else{
                player.sendMessage("§c§lERREUR§8» §7/nextday");
            }

        }

        if (cmd.getName().equalsIgnoreCase("previousday")) {
            if(!GameStatut.isStatut(GameStatut.GAME)){
                player.sendMessage("§c§lERREUR§8» §7La partie n'est pas en cours d'exécution");
                return false;
            }


            if(args.length == 0){
                daysRunnable.day --;
                daysRunnable.newDayComing();
                daysRunnable.timer = Main.INSTANCE.getConfig().getInt("partie.jour-duree") + 1;

            }else{
                player.sendMessage("§c§lERREUR§8» §7/previousday");
            }

        }

        if (cmd.getName().equalsIgnoreCase("stats")) {
            if(GameStatut.isStatut(GameStatut.LOBBY)){
                player.sendMessage("§c§lERREUR§8» §7La partie n'a pas encore commencée");
                return true;
            }


            if(daysRunnable.killCount.containsKey(player) && daysRunnable.deathCount.containsKey(player)){
                player.sendMessage("§eVous avez tué §b§l" + daysRunnable.killCount.get(player) + " fois");
                player.sendMessage("§eVous êtes mort §b§l" + daysRunnable.deathCount.get(player) + " fois");
                if(daysRunnable.deathCount.get(player) != 0) {
                    player.sendMessage("§eVotre ratio est de §b§l" + daysRunnable.killCount.get(player) / daysRunnable.deathCount.get(player));
                }else{
                    player.sendMessage(Main.getPrefix() + "§aVous n'êtes pas mort durant cette partie");
                }
            }else{
                player.sendMessage("§c§lERREUR§8» §7Aucune donnée statistique enregistrée");
            }
        }
        return true;

    }
}
