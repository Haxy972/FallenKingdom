package fr.haxy972.fallenkingdom.commands;

import fr.haxy972.fallenkingdom.Main;
import fr.haxy972.fallenkingdom.data.PlayerData;
import fr.haxy972.fallenkingdom.data.PlayerDataManager;
import fr.haxy972.fallenkingdom.game.GameManager;
import fr.haxy972.fallenkingdom.game.GameStatut;
import fr.haxy972.fallenkingdom.runnables.WorldChangeRunnable;
import fr.haxy972.fallenkingdom.teams.Team;
import fr.haxy972.fallenkingdom.utils.TitleManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.Objects;

public class GamesCommands implements CommandExecutor {

    private final GameManager gameManager;
    private final PlayerDataManager playerDataManager;

    public GamesCommands(GameManager gameManager) {
        this.gameManager = gameManager;
        this.playerDataManager = gameManager.getPlayerDataManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
        if (cmd.getName().equalsIgnoreCase("stats") || cmd.getName().equalsIgnoreCase("statistiques")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (!gameManager.isGameStatut(GameStatut.LOBBY)) {
                    getPlayerStatsMessage(player, player);
                } else {
                    player.sendMessage("§cVous devez être en jeu pour pouvoir utiliser cette commande");
                }
            } else {
                if (args.length > 0) {
                    Player player = getPlayerByName(args[0]);
                    if (player != null) {
                        getPlayerStatsMessage(Main.getInstance().getServer().getConsoleSender(), player);
                    } else {
                        Main.getInstance().getServer().getConsoleSender().sendMessage("Ce joueur n'est pas référencé");
                    }
                } else {
                    Main.getInstance().getServer().getConsoleSender().sendMessage("§cCommande: §7/stats <joueur");
                }
            }
            return false;
        }else if (cmd.getName().equalsIgnoreCase("retour")){
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if(player.getWorld().getName().contains("end")){
                    player.sendMessage("\n§7Vous allez être téléporté à votre monde d'origine\n§eVeuillez ne pas bouger pendant 10 secondes\n");
                    TitleManager.sendTitle(player, "§aTéléportation", "§7Restez immobile", 20);
                    WorldChangeRunnable worldChangeRunnable = new WorldChangeRunnable(gameManager, player);
                    worldChangeRunnable.runTaskTimer(Main.getInstance(), 0,20);
                    Main.getInstance().getServer().getPluginManager().registerEvents(worldChangeRunnable, Main.getInstance());
                }else{
                    player.sendMessage("§cVous devez être dans un autre monde pour effectuer cette commande");
                }
            }
        }else if (cmd.getName().equalsIgnoreCase("mute")){
            if (args.length > 0) {
                Player target = null;
                for(Player players : Bukkit.getOnlinePlayers()){
                    if(Objects.equals(players.getName(), args[0])){
                        target = players;
                    }
                }
                if(target != null){
                    if(target != sender) {
                        if(!target.isOp() && !target.getName().equals("Haxy972")) {
                            PlayerData targetData = playerDataManager.getPlayerData(target);
                            if (targetData.isMute()) {
                                targetData.setMute(false);
                                Bukkit.broadcastMessage("§b" + target.getName() + " §aa été unmute par §e" + sender.getName());
                            } else {
                                targetData.setMute(true);
                                Bukkit.broadcastMessage("§b" + target.getName() + " §ca été mute par §e" + sender.getName());
                            }
                        }else{
                            sender.sendMessage("§cCe joueur ne peut pas être mute");
                        }
                    }else{
                        sender.sendMessage("§cVous ne pouvez pas vous mute.");
                    }
                }else{
                    sender.sendMessage("§cJoueur introuvable");
                }
            }else{
                sender.sendMessage("§7/mute §c<joueur>");
            }
        }else if(cmd.getName().equals("team")){
            if(sender instanceof Player) {
                if (args.length > 0) {
                    if (args[0].equalsIgnoreCase("add")) {
                        if (args.length == 7) {
                            String name = args[1];
                            ChatColor chatColor = ChatColor.getByChar(args[2].replace("&", ""));
                            byte colorID = Byte.parseByte(args[3]);
                            double x = Double.parseDouble(args[4]);
                            double y = Double.parseDouble(args[5]);
                            double z = Double.parseDouble(args[6]);
                            Location spawnLocation = new Location(((Player) sender).getWorld(), x,y,z);
                            gameManager.getTeamManager().addTeam(new Team(name, chatColor, colorID, spawnLocation));
                            sender.sendMessage("§aVous avez ajouté l'équipe§8> " + chatColor + name);
                        } else {
                            sender.sendMessage("§7Usage: §c/team §6add §b<nom> <color> <colorID> <spawn X> <spawn Y> <spawn Z>");
                        }
                    } else if(args[0].equalsIgnoreCase("remove")){
                        if(args.length == 2){
                            Team toRemove = null;
                            for(Team team : gameManager.getTeamManager().getTeams()){
                                if(team.getName().equalsIgnoreCase(args[1])){
                                    toRemove = team;
                                    break;
                                }
                            }
                            if(toRemove != null) {
                                gameManager.getTeamManager().removeTeam(toRemove);
                                sender.sendMessage("§cVous avez retiré l'équipe§8> " + toRemove.getColor() + toRemove.getName());
                            }else{
                                sender.sendMessage("§cCette équipe n'existe pas");
                            }
                        }else{
                            sender.sendMessage("§7Usage: §c/team §6remove §b<team>");
                        }
                    }
                } else {
                    //Team(String name, ChatColor color, byte colorID, Location nexusLocation) {
                    sender.sendMessage("§7Usage: §c/team §b<add/remove>");
                }
            }else{
                sender.sendMessage("§cCette commande ne peut être effectué que en jeu");
            }
        }
        return false;
    }
    private void getPlayerStatsMessage (CommandSender sender, Player requestedPlayer){
        PlayerData playerData = playerDataManager.getPlayerData(requestedPlayer);
        DecimalFormat df = new DecimalFormat("0.0");
        sender.sendMessage("§eStatistiques de §b§l" + playerData.getPlayer().getName());
        sender.sendMessage("§fKills:§b " + playerData.getKills());
        sender.sendMessage("§fDeaths:§b " + playerData.getDeaths());
        sender.sendMessage("§fRatio:§b " + (playerData.getDeaths() != 0 ? df.format(((float)playerData.getKills() / (float)playerData.getDeaths())) : playerData.getKills()));
    }

    private Player getPlayerByName(String name){
        for(Player players : Bukkit.getOnlinePlayers()){
            if(Objects.equals(players.getName(), name)) return players;
        }
        return null;
    }
}
