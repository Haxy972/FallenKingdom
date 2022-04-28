package fr.haxy972.fallen.Team.select;

import java.util.HashMap;
import java.util.Map;

import fr.haxy972.fallen.utils.MessageYaml;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.haxy972.fallen.GameStatut;
import fr.haxy972.fallen.Main;

public class TeamSelect {

    public Player player;
    public static Map<Player, String> team = new HashMap<>();
    //Rouge
    public static int rougeNumber = 0;
    public static int rougeMaxNumber = Main.INSTANCE.getConfig().getInt("teams.max-players-per-team");
    //Bleu
    public static int bleuNumber = 0;
    public static int bleuMaxNumber = Main.INSTANCE.getConfig().getInt("teams.max-players-per-team");
    //Vert
    public static int vertNumber = 0;
    public static int vertMaxNumber = Main.INSTANCE.getConfig().getInt("teams.max-players-per-team");
    //Jaune
    public static int jauneNumber = 0;
    public static int jauneMaxNumber = Main.INSTANCE.getConfig().getInt("teams.max-players-per-team");


    public TeamSelect() {
    }

    public TeamSelect(Player player) {
        this.player = player;
    }


    public void setTeam(String team) {
        //Rouge
        if (team.equalsIgnoreCase("rouge")) {
            if (!Main.INSTANCE.getConfig().getBoolean("teams.rouge")) {
                player.sendMessage(Main.getPrefix() + MessageYaml.getValue("messages.team.disabled").replace("&", "§"));

                return;
            }
            if (rougeNumber < rougeMaxNumber) {
                if (!(TeamSelect.team.containsKey(player))) {
                    ((Map<Player, String>) TeamSelect.team).put(player, team);

                    player.sendMessage(Main.getPrefix() + MessageYaml.getValue("messages.team.joined").replace("&", "§").replace("{team}", "§c§lROUGE"));
                    if(Main.INSTANCE.getConfig().getBoolean("grades")) {
                        Main.INSTANCE.getServer().dispatchCommand(Main.INSTANCE.getServer().getConsoleSender(), "manuadd " + player.getName() + " rouge");
                    }
                    TeamSelect.rougeNumber++;
                    if (GameStatut.isStatut(GameStatut.LOBBY)) {
                        giveLobbyLaine(player, team);
                    }
                } else {
                    if (TeamSelect.team.get(player) != team) {
                        String str = TeamSelect.team.get(player);
                        if (str.equalsIgnoreCase("bleu")) {
                            TeamSelect.bleuNumber--;
                        }
                        if (str.equalsIgnoreCase("vert")) {
                            TeamSelect.vertNumber--;
                        }
                        if (str.equalsIgnoreCase("jaune")) {
                            TeamSelect.jauneNumber--;
                        }
                        TeamSelect.team.replace(player, team);
                        player.sendMessage(Main.getPrefix() + MessageYaml.getValue("messages.team.joined").replace("&", "§").replace("{team}", "§c§lROUGE"));
                        if(Main.INSTANCE.getConfig().getBoolean("grades")) {
                            Main.INSTANCE.getServer().dispatchCommand(Main.INSTANCE.getServer().getConsoleSender(), "manuadd " + player.getName() + " rouge");
                        }
                        giveLobbyLaine(player, team);
                    } else {
                        player.sendMessage(MessageYaml.getValue("messages.team.already").replace("&", "§"));
                    }

                }

            } else {

                player.sendMessage(MessageYaml.getValue("messages.team.full").replace("&", "§").replace("{team}", "§c§lROUGE"));
            }

        }
        //Bleu
        if (team.equalsIgnoreCase("bleu")) {
            if (!Main.INSTANCE.getConfig().getBoolean("teams.bleu")) {
                player.sendMessage(Main.getPrefix() + MessageYaml.getValue("messages.team.disabled").replace("&", "§"));

                return;
            }
            if (bleuNumber < bleuMaxNumber) {
                if (!(TeamSelect.team.containsKey(player))) {
                    ((Map<Player, String>) TeamSelect.team).put(player, team);

                    player.sendMessage(Main.getPrefix() + MessageYaml.getValue("messages.team.joined").replace("&", "§").replace("{team}", "§9§lBLEU"));
                    if(Main.INSTANCE.getConfig().getBoolean("grades")) {
                        Main.INSTANCE.getServer().dispatchCommand(Main.INSTANCE.getServer().getConsoleSender(), "manuadd " + player.getName() + " bleu");
                    }
                    TeamSelect.rougeNumber++;
                    if (GameStatut.isStatut(GameStatut.LOBBY)) {
                        giveLobbyLaine(player, team);
                    }
                } else {
                    if (TeamSelect.team.get(player) != team) {
                        String str = TeamSelect.team.get(player);
                        if (str.equalsIgnoreCase("rouge")) {
                            TeamSelect.rougeNumber--;
                        }
                        if (str.equalsIgnoreCase("vert")) {
                            TeamSelect.vertNumber--;
                        }
                        if (str.equalsIgnoreCase("jaune")) {
                            TeamSelect.jauneNumber--;
                        }
                        TeamSelect.team.replace(player, team);
                        if(Main.INSTANCE.getConfig().getBoolean("grades")) {
                            Main.INSTANCE.getServer().dispatchCommand(Main.INSTANCE.getServer().getConsoleSender(), "manuadd " + player.getName() + " bleu");
                        }
                        player.sendMessage(Main.getPrefix() + MessageYaml.getValue("messages.team.joined").replace("&", "§").replace("{team}", "§9§lBLEU"));                        giveLobbyLaine(player, team);

                    } else {
                        player.sendMessage(MessageYaml.getValue("messages.team.already").replace("&", "§"));
                    }

                }

            } else {

                player.sendMessage(MessageYaml.getValue("messages.team.full").replace("&", "§").replace("{team}", "§9§lBLEU"));
            }

        }
        //Vert
        if (team.equalsIgnoreCase("vert")) {
            if (!Main.INSTANCE.getConfig().getBoolean("teams.vert")) {
                player.sendMessage(Main.getPrefix() + MessageYaml.getValue("messages.team.disabled").replace("&", "§"));

                return;
            }

            if (vertNumber < vertMaxNumber) {
                if (!(TeamSelect.team.containsKey(player))) {
                    ((Map<Player, String>) TeamSelect.team).put(player, team);
                    if(Main.INSTANCE.getConfig().getBoolean("grades")) {
                        player.sendMessage(Main.getPrefix() + MessageYaml.getValue("messages.team.joined").replace("&", "§").replace("{team}", "§a§lVERT"));
                    }
                    Main.INSTANCE.getServer().dispatchCommand(Main.INSTANCE.getServer().getConsoleSender(), "manuadd " + player.getName() + " vert");
                    TeamSelect.rougeNumber++;
                    if (GameStatut.isStatut(GameStatut.LOBBY)) {
                        giveLobbyLaine(player, team);
                    }
                } else {
                    if (TeamSelect.team.get(player) != team) {
                        String str = TeamSelect.team.get(player);
                        if (str.equalsIgnoreCase("rouge")) {
                            TeamSelect.rougeNumber--;
                        }
                        if (str.equalsIgnoreCase("bleu")) {
                            TeamSelect.bleuNumber--;
                        }
                        if (str.equalsIgnoreCase("jaune")) {
                            TeamSelect.jauneNumber--;
                        }
                        TeamSelect.team.replace(player, team);
                        player.sendMessage(Main.getPrefix() + MessageYaml.getValue("messages.team.joined").replace("&", "§").replace("{team}", "§a§lVERT"));
                        if(Main.INSTANCE.getConfig().getBoolean("grades")) {
                            Main.INSTANCE.getServer().dispatchCommand(Main.INSTANCE.getServer().getConsoleSender(), "manuadd " + player.getName() + " vert");
                        }
                        giveLobbyLaine(player, team);

                    } else {
                        player.sendMessage(MessageYaml.getValue("messages.team.already").replace("&", "§"));
                    }

                }

            } else {

                player.sendMessage(MessageYaml.getValue("messages.team.full").replace("&", "§").replace("{team}", "§a§lVERT"));
            }

        }
        //Jaune
        if (team.equalsIgnoreCase("jaune")) {
            //SUPP

            if (!Main.INSTANCE.getConfig().getBoolean("teams.jaune")) {
                player.sendMessage(Main.getPrefix() + MessageYaml.getValue("messages.team.disabled").replace("&", "§"));

                return;
            }

            if (jauneNumber < jauneMaxNumber) {
                if (!(TeamSelect.team.containsKey(player))) {
                    ((Map<Player, String>) TeamSelect.team).put(player, team);

                    player.sendMessage(Main.getPrefix() + MessageYaml.getValue("messages.team.joined").replace("&", "§").replace("{team}", "§e§lJAUNE"));
                    if(Main.INSTANCE.getConfig().getBoolean("grades")) {
                        Main.INSTANCE.getServer().dispatchCommand(Main.INSTANCE.getServer().getConsoleSender(), "manuadd " + player.getName() + " jaune");
                    }
                    TeamSelect.rougeNumber++;
                    if (GameStatut.isStatut(GameStatut.LOBBY)) {
                        giveLobbyLaine(player, team);
                    }
                } else {
                    if (TeamSelect.team.get(player) != team) {
                        String str = TeamSelect.team.get(player);
                        if (str.equalsIgnoreCase("rouge")) {
                            TeamSelect.rougeNumber--;
                        }
                        if (str.equalsIgnoreCase("vert")) {
                            TeamSelect.vertNumber--;
                        }
                        if (str.equalsIgnoreCase("bleu")) {
                            TeamSelect.bleuNumber--;
                        }
                        TeamSelect.team.replace(player, team);
                        player.sendMessage(Main.getPrefix() + MessageYaml.getValue("messages.team.joined").replace("&", "§").replace("{team}", "§e§lJAUNE"));                        giveLobbyLaine(player, team);
                        if(Main.INSTANCE.getConfig().getBoolean("grades")) {
                            Main.INSTANCE.getServer().dispatchCommand(Main.INSTANCE.getServer().getConsoleSender(), "manuadd " + player.getName() + " jaune");
                        }
                    } else {
                        player.sendMessage(MessageYaml.getValue("messages.team.already").replace("&", "§"));
                    }

                }

            } else {

                player.sendMessage(MessageYaml.getValue("messages.team.full").replace("&", "§").replace("{team}", "§e§lJAUNE"));
            }

        }
        if (team.equalsIgnoreCase("reset")) {
            if (TeamSelect.team.containsKey(player)) {
                String str = TeamSelect.team.get(player);
                if (str.equalsIgnoreCase("rouge")) {
                    TeamSelect.rougeNumber--;
                }
                if (str.equalsIgnoreCase("vert")) {
                    TeamSelect.vertNumber--;
                }
                if (str.equalsIgnoreCase("bleu")) {
                    TeamSelect.bleuNumber--;
                }
                if (str.equalsIgnoreCase("jaune")) {
                    TeamSelect.jauneNumber--;
                }
                player.sendMessage(Main.getPrefix() + MessageYaml.getValue("messages.team.leave").replace("&", "§"));
                if(Main.INSTANCE.getConfig().getBoolean("grades")) {
                    Main.INSTANCE.getServer().dispatchCommand(Main.INSTANCE.getServer().getConsoleSender(), "manuadd " + player.getName() + " joueur");
                }
                giveLobbyLaine(player, team);

                TeamSelect.team.remove(player);
            } else {
                player.sendMessage(MessageYaml.getValue("messages.team.team-less").replace("&", "§"));
            }
        }
    }


    private void giveLobbyLaine(Player player2, String team2) {
        //Rouge
        if (team2.equalsIgnoreCase("rouge")) {
            ItemStack it = new ItemStack(Material.WOOL, 1, (short) 14);
            ItemMeta im = it.getItemMeta();
            im.setDisplayName("§b§lEquipe§8» §c§lROUGE");
            it.setItemMeta(im);
            player2.getInventory().setItem(4, it);
        }
        //Bleu
        if (team2.equalsIgnoreCase("bleu")) {
            ItemStack it = new ItemStack(Material.WOOL, 1, (short) 11);
            ItemMeta im = it.getItemMeta();
            im.setDisplayName("§b§lEquipe§8» §9§lBLEU");
            it.setItemMeta(im);
            player2.getInventory().setItem(4, it);
        }
        //Vert
        if (team2.equalsIgnoreCase("vert")) {
            ItemStack it = new ItemStack(Material.WOOL, 1, (short) 13);
            ItemMeta im = it.getItemMeta();
            im.setDisplayName("§b§lEquipe§8» §a§lVERT");
            it.setItemMeta(im);
            player2.getInventory().setItem(4, it);
        }
        //Jaune
        if (team2.equalsIgnoreCase("jaune")) {
            ItemStack it = new ItemStack(Material.WOOL, 1, (short) 4);
            ItemMeta im = it.getItemMeta();
            im.setDisplayName("§b§lEquipe§8» §e§lJAUNE");
            it.setItemMeta(im);
            player2.getInventory().setItem(4, it);
        }
        //RESET
        if (team2.equalsIgnoreCase("reset")) {
            ItemStack it = new ItemStack(Material.WOOL);
            ItemMeta im = it.getItemMeta();
            im.setDisplayName("§b§lEquipe§8» §7Aucune");
            it.setItemMeta(im);
            player2.getInventory().setItem(4, it);
        }
    }

    public String getTeam(Player player) {
        if (TeamSelect.team.containsKey(player)) {
            return TeamSelect.team.get(player);
        } else {
            return null;
        }

    }


}
