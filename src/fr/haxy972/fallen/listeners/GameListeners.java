package fr.haxy972.fallen.listeners;

import fr.haxy972.fallen.manager.GameManager;
import fr.haxy972.fallen.utils.MessageYaml;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.event.world.PortalCreateEvent;
import org.bukkit.inventory.ItemStack;

import fr.haxy972.fallen.GameStatut;
import fr.haxy972.fallen.Main;
import fr.haxy972.fallen.Team.select.TeamSelect;
import fr.haxy972.fallen.runnable.DeathRunnable;
import fr.haxy972.fallen.runnable.daysRunnable;
import fr.haxy972.fallen.utils.TitleManager;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

import java.util.Iterator;

public class GameListeners implements Listener {


    @EventHandler
    public void onExplodeCreeper(CreeperPowerEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlocksExplosion(EntityExplodeEvent event) {
        if (!GameStatut.isStatut(GameStatut.GAME)) {
            return;
        }

        for (Block block : event.blockList()) {
            if (block.getType() == Material.BEACON || block.getType() == Material.STAINED_GLASS) {

                Bukkit.getScheduler().runTaskLater(Main.INSTANCE, new Runnable() {

                    final Material mat = block.getType();
                    @SuppressWarnings("deprecation")
                    final byte matid = block.getData();

                    @SuppressWarnings("deprecation")
                    @Override
                    public void run() {
                        block.getLocation().getBlock().setType(mat);
                        block.getLocation().getBlock().setData(matid);

                    }
                }, 1 / 1000000000);
            }
        }
        Bukkit.getScheduler().runTaskLater(Main.INSTANCE, () -> {
            for(Entity entity : event.getLocation().getChunk().getEntities()){
                if(entity instanceof Item){
                    entity.remove();
                }

            }
        },20);



    }
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        if(GameStatut.isStatut(GameStatut.END)){
            Player player = event.getPlayer();
            player.kickPlayer("§cServeur en cours de redémarrage...");
        }



        if(!GameStatut.isStatut(GameStatut.GAME)){
            return;
        }


        Player player = event.getPlayer();

        if(TeamSelect.team.containsKey(player)){
            event.setJoinMessage("§e" + player.getName() + " s'est reconnecté");
            player.setHealth(0);
        }else{
            Main.INSTANCE.getServer().dispatchCommand(Main.INSTANCE.getServer().getConsoleSender(), "manuadd " + player.getName() + " mort");
            player.setGameMode(GameMode.SPECTATOR);
            event.setJoinMessage("§e" + player.getName() + " regarde la partie");
        }

    }


    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        if (TeamSelect.team.containsKey(player)) {
            if (TeamSelect.team.get(player).equals("rouge")) {
                int i = 0;
                for (Player players : Bukkit.getOnlinePlayers()) {
                    if (TeamSelect.team.containsKey(players)) {
                        if(TeamSelect.team.get(players).equalsIgnoreCase("rouge")){
                            i++;
                        }

                    }
                }

                if (i == 1) {
                    GameManager.destroyNexus("rouge");
                    TeamSelect.team.remove(player);
                    daysRunnable.redalive = false;
                }



            } else if (TeamSelect.team.get(player).equals("bleu")) {
                int i = 0;
                for (Player players : Bukkit.getOnlinePlayers()) {
                    if (TeamSelect.team.containsKey(players)) {
                        if(TeamSelect.team.get(players).equalsIgnoreCase("bleu")) {
                            i++;
                        }
                    }
                }
                if (i == 1) {
                    GameManager.destroyNexus("bleu");
                    TeamSelect.team.remove(player);
                    daysRunnable.bluealive = false;
                }

            } else if (TeamSelect.team.get(player).equals("vert")) {
                int i = 0;
                for (Player players : Bukkit.getOnlinePlayers()) {
                    if (TeamSelect.team.containsKey(players)) {
                        if(TeamSelect.team.get(players).equalsIgnoreCase("vert")) {
                            i++;
                        }
                    }
                }
                if (i == 1) {
                    GameManager.destroyNexus("vert");
                    TeamSelect.team.remove(player);
                    daysRunnable.greenalive = false;

                }
            } else if (TeamSelect.team.get(player).equals("jaune")) {
                int i = 0;
                for (Player players : Bukkit.getOnlinePlayers()) {
                    if (TeamSelect.team.containsKey(players)) {
                        if(TeamSelect.team.get(players).equalsIgnoreCase("jaune")) {
                            i++;
                        }
                    }
                }
                if (i == 1) {

                    GameManager.destroyNexus("jaune");
                    TeamSelect.team.remove(player);
                    daysRunnable.yellowalive = false;
                }
            }

        }
    }


        @EventHandler
        public void onFoodLevelChange (FoodLevelChangeEvent event){
            if (!GameStatut.isStatut(GameStatut.GAME)) {
                return;
            }


            if (daysRunnable.day < Main.INSTANCE.getConfig().getInt("partie.jour-de-faim")) {
                Player player = (Player) event.getEntity();
                player.setFoodLevel(20);
                event.setCancelled(true);
            }
        }

        @EventHandler

        public void onDamage (EntityDamageByEntityEvent event){
            if (!GameStatut.isStatut(GameStatut.GAME)) {
                return;
            }
            int jourpvp = Main.INSTANCE.getConfig().getInt("partie.jour-de-pvp-2");
            if (!(event.getDamager() instanceof Player)) {
                return;
            }
            if (!(event.getEntity() instanceof Player)) {
                return;
            }

            Player attacker = (Player) event.getDamager();
            Entity victim = event.getEntity();
            if (!(victim instanceof Player)) {
                return;
            }
            if (daysRunnable.day < jourpvp) {
                event.setCancelled(true);
                attacker.sendMessage(Main.getPrefix() + MessageYaml.getValue("messages.pvp.pvp-disabled").replace("{jour}", Main.INSTANCE.getConfig().getString("partie.jour-de-pvp-2")).replace("&", "§"));

            }
        }

        @EventHandler
        public void onPlayerDeath (PlayerDeathEvent event){
            if (!GameStatut.isStatut(GameStatut.GAME)) {
                return;
            }
            event.setDeathMessage("");

            Player player = event.getEntity();
            player.setHealth(20);
            player.setFoodLevel(20);
            player.setGameMode(GameMode.SPECTATOR);
            if(player.getKiller() != null){
                Player killer = player.getKiller();
                if(TeamSelect.team.containsKey(player) && TeamSelect.team.containsKey(killer)){
                    Bukkit.broadcastMessage(TeamChat(player) + "§7 a été tué par " + TeamChat(killer));
                }else {
                    Bukkit.broadcastMessage("§e" + player.getPlayer() + "§7 a été tué par §c" + killer.getName());
                }

                if(!daysRunnable.deathCount.containsKey(player)){
                    daysRunnable.deathCount.put(player, 1);
                }else{
                    int value = daysRunnable.deathCount.get(player) + 1;
                    daysRunnable.deathCount.replace(player, value);

                }

                if(!daysRunnable.killCount.containsKey(killer)){
                    daysRunnable.killCount.put(killer, 1);
                }else{
                    int value = daysRunnable.killCount.get(killer) + 1;
                    daysRunnable.killCount.replace(killer, value);
                }

            }else{

                Bukkit.broadcastMessage(TeamChat(player) + "§7 est mort");
                if(!daysRunnable.deathCount.containsKey(player)){
                    daysRunnable.deathCount.put(player, 1);
                }else {
                    int value = daysRunnable.deathCount.get(player) + 1;
                    daysRunnable.deathCount.replace(player, value);
                }
            }


            TitleManager.sendTitle(player, MessageYaml.getValue("messages.death.title").replace("&", "§"), MessageYaml.getValue("messages.death.subtitle").replace("&", "§"), 3 * 20);
            new DeathRunnable(player).runTaskTimer(Main.INSTANCE, 0, 20);

        }

    private String TeamChat(Player player) {
        if(TeamSelect.team.get(player).equals("rouge")){
            return "§c" + player.getName();
        }else if(TeamSelect.team.get(player).equals("bleu")){
            return "§9" + player.getName();
        }else if(TeamSelect.team.get(player).equals("vert")){
            return "§a" + player.getName();
        }else if(TeamSelect.team.get(player).equals("jaune")){
            return "§e" + player.getName();
        }

        return "§8" + player.getName();
    }

    @EventHandler
        public void onMove (PlayerMoveEvent event){
            if (!GameStatut.isStatut(GameStatut.GAME)) {
                return;
            }
            int jourassault = Main.INSTANCE.getConfig().getInt("partie.jour-de-pvp");
            if (daysRunnable.day >= jourassault) {
                return;
            }
            Player player = event.getPlayer();
            if (!TeamSelect.team.containsKey(player)) {
                return;
            }



            //verifie rouge
            if ((player.getLocation().getBlockX() >= rougeMinX && player.getLocation().getBlockX() <= rougeMaxX) && (player.getLocation().getBlockZ() >= rougeMinZ && player.getLocation().getBlockZ() <= rougeMaxZ)) {
                if (!TeamSelect.team.get(player).equalsIgnoreCase("rouge")) {
                    player.sendMessage(MessageYaml.getValue("messages.pvp.assault-error").replace("{jour}", Main.INSTANCE.getConfig().getString("partie.jour-de-pvp")).replace("&", "§"));
                    double x = Main.INSTANCE.getConfig().getDouble("regions.teams.tp-rouge.x");
                    double y = Main.INSTANCE.getConfig().getDouble("regions.teams.tp-rouge.y");
                    double z = Main.INSTANCE.getConfig().getDouble("regions.teams.tp-rouge.z");
                    int yaw = Main.INSTANCE.getConfig().getInt("regions.teams.tp-rouge.yaw");
                    int pitch = Main.INSTANCE.getConfig().getInt("regions.teams.tp-rouge.pitch");

                    Location location = new Location(player.getWorld(), x, y, z, yaw, pitch);
                    player.teleport(location);
                }
            }

            //verifie bleu
            if ((player.getLocation().getBlockX() >= bleuMinX && player.getLocation().getBlockX() <= bleuMaxX) && (player.getLocation().getBlockZ() >= bleuMinZ && player.getLocation().getBlockZ() <= bleuMaxZ)) {
                if (!TeamSelect.team.get(player).equalsIgnoreCase("bleu")) {

                    player.sendMessage(MessageYaml.getValue("messages.pvp.assault-error").replace("{jour}", Main.INSTANCE.getConfig().getString("partie.jour-de-pvp")).replace("&", "§"));
                    double x = Main.INSTANCE.getConfig().getDouble("regions.teams.tp-bleu.x");
                    double y = Main.INSTANCE.getConfig().getDouble("regions.teams.tp-bleu.y");
                    double z = Main.INSTANCE.getConfig().getDouble("regions.teams.tp-bleu.z");
                    int yaw = Main.INSTANCE.getConfig().getInt("regions.teams.tp-bleu.yaw");
                    int pitch = Main.INSTANCE.getConfig().getInt("regions.teams.tp-bleu.pitch");

                    Location location = new Location(player.getWorld(), x, y, z, yaw, pitch);
                    player.teleport(location);
                }
            }
            //verifie vert
            if ((player.getLocation().getBlockX() >= vertMinX && player.getLocation().getBlockX() <= vertMaxX) && (player.getLocation().getBlockZ() >= vertMinZ && player.getLocation().getBlockZ() <= vertMaxZ)) {
                if (!TeamSelect.team.get(player).equalsIgnoreCase("vert")) {

                    player.sendMessage(MessageYaml.getValue("messages.pvp.assault-error").replace("{jour}", Main.INSTANCE.getConfig().getString("partie.jour-de-pvp")).replace("&", "§"));
                    double x = Main.INSTANCE.getConfig().getDouble("regions.teams.tp-vert.x");
                    double y = Main.INSTANCE.getConfig().getDouble("regions.teams.tp-vert.y");
                    double z = Main.INSTANCE.getConfig().getDouble("regions.teams.tp-vert.z");
                    int yaw = Main.INSTANCE.getConfig().getInt("regions.teams.tp-vert.yaw");
                    int pitch = Main.INSTANCE.getConfig().getInt("regions.teams.tp-vert.pitch");

                    Location location = new Location(player.getWorld(), x, y, z, yaw, pitch);
                    player.teleport(location);
                }
            }

            //verifie jaune
            if ((player.getLocation().getBlockX() >= jauneMinX && player.getLocation().getBlockX() <= jauneMaxX) && (player.getLocation().getBlockZ() >= jauneMinZ && player.getLocation().getBlockZ() <= jauneMaxZ)) {
                if (!TeamSelect.team.get(player).equalsIgnoreCase("jaune")) {

                    player.sendMessage(MessageYaml.getValue("messages.pvp.assault-error").replace("{jour}", Main.INSTANCE.getConfig().getString("partie.jour-de-pvp")).replace("&", "§"));
                    double x = Main.INSTANCE.getConfig().getDouble("regions.teams.tp-jaune.x");
                    double y = Main.INSTANCE.getConfig().getDouble("regions.teams.tp-jaune.y");
                    double z = Main.INSTANCE.getConfig().getDouble("regions.teams.tp-jaune.z");
                    int yaw = Main.INSTANCE.getConfig().getInt("regions.teams.tp-jaune.yaw");
                    int pitch = Main.INSTANCE.getConfig().getInt("regions.teams.tp-jaune.pitch");

                    Location location = new Location(player.getWorld(), x, y, z, yaw, pitch);


                    player.teleport(location);
                }
            }


        }

        @EventHandler
        public void onPortalCreate(PortalCreateEvent event){
            event.setCancelled(true);


        }




        @EventHandler
        public void onWeatherChange (WeatherChangeEvent event){
            if (!GameStatut.isStatut(GameStatut.GAME)) {
                return;
            }
            event.setCancelled(true);
        }

        //max == grande valeur et min == plus petite valeur

        public double rougeMaxX = Main.INSTANCE.getConfig().getDouble("regions.teams.rouge.max-x");
        public double rougeMinX = Main.INSTANCE.getConfig().getDouble("regions.teams.rouge.min-x");
        public double rougeMaxZ = Main.INSTANCE.getConfig().getDouble("regions.teams.rouge.max-z");
        public double rougeMinZ = Main.INSTANCE.getConfig().getDouble("regions.teams.rouge.min-z");

        public double bleuMaxX = Main.INSTANCE.getConfig().getDouble("regions.teams.bleu.max-x");
        public double bleuMinX = Main.INSTANCE.getConfig().getDouble("regions.teams.bleu.min-x");
        public double bleuMaxZ = Main.INSTANCE.getConfig().getDouble("regions.teams.bleu.max-z");
        public double bleuMinZ = Main.INSTANCE.getConfig().getDouble("regions.teams.bleu.min-z");

        public double vertMaxX = Main.INSTANCE.getConfig().getDouble("regions.teams.vert.max-x");
        public double vertMinX = Main.INSTANCE.getConfig().getDouble("regions.teams.vert.min-x");
        public double vertMaxZ = Main.INSTANCE.getConfig().getDouble("regions.teams.vert.max-z");
        public double vertMinZ = Main.INSTANCE.getConfig().getDouble("regions.teams.vert.min-z");

        public double jauneMaxX = Main.INSTANCE.getConfig().getDouble("regions.teams.jaune.max-x");
        public double jauneMinX = Main.INSTANCE.getConfig().getDouble("regions.teams.jaune.min-x");
        public double jauneMaxZ = Main.INSTANCE.getConfig().getDouble("regions.teams.jaune.max-z");
        public double jauneMinZ = Main.INSTANCE.getConfig().getDouble("regions.teams.jaune.min-z");

        //CASSER BASE
        @EventHandler
        @SuppressWarnings("deprecation")
        public void onBlockBreak2 (BlockBreakEvent event){
            if (!GameStatut.isStatut(GameStatut.GAME)) {
                return;
            }
            Player player = event.getPlayer();
            Location loc = event.getBlock().getLocation();


            if(event.getBlock().getType() == Material.CHEST){
                if(GameManager.chestsloc.contains(loc)){
                    event.setCancelled(true);
                    player.sendMessage(Main.getPrefix() + MessageYaml.getValue("messages.blocks.unbreakable").replace("&", "§"));
                    return;
                }
            }



            //max == grande valeur et min == plus petite valeur


            //ROUGE
            if ((loc.getBlockX() >= rougeMinX && loc.getBlockX() <= rougeMaxX) && (loc.getBlockZ() >= rougeMinZ && loc.getBlockZ() <= rougeMaxZ)) {
                if (!daysRunnable.redalive) {
                    return;
                }
                if (TeamSelect.team.containsKey(player)) {
                    if (!TeamSelect.team.get(player).equalsIgnoreCase("rouge")) {
                        if (event.getBlock().getType() != Material.BEACON && event.getBlock().getType() != Material.STAINED_GLASS) {
                            player.sendMessage(Main.getPrefix() + MessageYaml.getValue("messages.blocks.breakable-by-pickaxe").replace("&", "§"));
                            event.setCancelled(true);
                            return;

                        } else {
                            if(player.getItemInHand().getType() == Material.DIAMOND_PICKAXE || player.getItemInHand().getType() == Material.GOLD_PICKAXE || player.getItemInHand().getType() == Material.IRON_PICKAXE || player.getItemInHand().getType() == Material.STONE_PICKAXE || player.getItemInHand().getType() == Material.WOOD_PICKAXE) {
                                if (event.getBlock().getType() == Material.STAINED_GLASS || event.getBlock().getType() == Material.IRON_BLOCK) {
                                    event.setCancelled(true);
                                    player.sendMessage(Main.getPrefix() + MessageYaml.getValue("messages.blocks.unbreakable").replace("&", "§"));
                                    return;
                                }

                                return;
                            } else {
                                player.sendMessage(Main.getPrefix() + MessageYaml.getValue("messages.blocks.breakable-by-pickaxe").replace("&", "§"));
                                event.setCancelled(true);
                                return;
                            }
                        }
                    } else {
                        if (event.getBlock().getType() == Material.BEACON) {
                            event.setCancelled(true);
                            player.sendMessage(Main.getPrefix() + MessageYaml.getValue("messages.blocks.own-beacon").replace("&", "§"));
                            return;
                        } else if (event.getBlock().getType() == Material.STAINED_GLASS || event.getBlock().getType() == Material.IRON_BLOCK) {
                            event.setCancelled(true);
                            player.sendMessage(Main.getPrefix() + MessageYaml.getValue("messages.blocks.unbreakable").replace("&", "§"));
                            return;
                        }
                    }
                }
            }
            if ((player.getLocation().getBlockX() >= (rougeMinX - marge)) && (player.getLocation().getBlockX() <= (rougeMaxX + marge)) && (player.getLocation().getBlockZ() >= (rougeMinZ - marge)) && (player.getLocation().getBlockZ() <= (rougeMaxZ + marge))) {
                if (!daysRunnable.redalive) {
                    return;
                }
                if(event.isCancelled()){
                    return;
                }
                if (!TeamSelect.team.get(player).equalsIgnoreCase("rouge")) {


                    event.setCancelled(true);
                    player.sendMessage(Main.getPrefix() + MessageYaml.getValue("messages.blocks.region-marge-error").replace("&", "§"));


                }
            }
            //BLEU
            if ((loc.getBlockX() >= bleuMinX && loc.getBlockX() <= bleuMaxX) && (loc.getBlockZ() >= bleuMinZ && loc.getBlockZ() <= bleuMaxZ)) {
                if (!daysRunnable.bluealive) {
                    return;
                }
                if (TeamSelect.team.containsKey(player)) {
                    if (!TeamSelect.team.get(player).equalsIgnoreCase("bleu")) {
                        if (event.getBlock().getType() != Material.BEACON && event.getBlock().getType() != Material.STAINED_GLASS) {
                            player.sendMessage(Main.getPrefix() + MessageYaml.getValue("messages.blocks.breakable-by-pickaxe").replace("&", "§"));
                            event.setCancelled(true);
                            return;
                        } else {
                            if (player.getItemInHand().getType() == Material.DIAMOND_PICKAXE || player.getItemInHand().getType() == Material.GOLD_PICKAXE || player.getItemInHand().getType() == Material.IRON_PICKAXE || player.getItemInHand().getType() == Material.STONE_PICKAXE || player.getItemInHand().getType() == Material.WOOD_PICKAXE) {
                                if (event.getBlock().getType() == Material.STAINED_GLASS || event.getBlock().getType() == Material.IRON_BLOCK) {
                                    event.setCancelled(true);
                                    player.sendMessage(Main.getPrefix() + MessageYaml.getValue("messages.blocks.unbreakable").replace("&", "§"));
                                }
                                return;
                            } else {
                                player.sendMessage(Main.getPrefix() + MessageYaml.getValue("messages.blocks.breakable-by-pickaxe").replace("&", "§"));
                                event.setCancelled(true);
                                return;
                            }
                        }

                    } else {
                        if (event.getBlock().getType() == Material.BEACON) {
                            event.setCancelled(true);
                            player.sendMessage(Main.getPrefix() + MessageYaml.getValue("messages.blocks.own-beacon").replace("&", "§"));
                            return;
                        } else if (event.getBlock().getType() == Material.STAINED_GLASS || event.getBlock().getType() == Material.IRON_BLOCK) {
                            event.setCancelled(true);
                            player.sendMessage(Main.getPrefix() + MessageYaml.getValue("messages.blocks.unbreakable").replace("&", "§"));
                            return;
                        }
                    }
                }
            }
            if ((player.getLocation().getBlockX() >= (bleuMinX - marge)) && (player.getLocation().getBlockX() <= (bleuMaxX + marge)) && (player.getLocation().getBlockZ() >= (bleuMinZ - marge)) && (player.getLocation().getBlockZ() <= (bleuMaxZ + marge))) {
                if (!daysRunnable.bluealive) {
                    return;
                }
                if(event.isCancelled()){
                    return;
                }
                if (!TeamSelect.team.get(player).equalsIgnoreCase("bleu")) {


                    event.setCancelled(true);
                    player.sendMessage(Main.getPrefix() + MessageYaml.getValue("messages.blocks.region-marge-error").replace("&", "§"));


                }
            }
            //VERT
            if ((loc.getBlockX() >= vertMinX && loc.getBlockX() <= vertMaxX) && (loc.getBlockZ() >= vertMinZ && loc.getBlockZ() <= vertMaxZ)) {
                if (!daysRunnable.greenalive) {
                    return;
                }
                if (TeamSelect.team.containsKey(player)) {
                    if (!TeamSelect.team.get(player).equalsIgnoreCase("vert")) {
                        if (event.getBlock().getType() != Material.BEACON && event.getBlock().getType() != Material.STAINED_GLASS) {
                            player.sendMessage(Main.getPrefix() + MessageYaml.getValue("messages.blocks.breakable-by-pickaxe").replace("&", "§"));
                            event.setCancelled(true);
                            return;
                        } else {
                            if (player.getItemInHand().getType() == Material.DIAMOND_PICKAXE || player.getItemInHand().getType() == Material.GOLD_PICKAXE || player.getItemInHand().getType() == Material.IRON_PICKAXE || player.getItemInHand().getType() == Material.STONE_PICKAXE || player.getItemInHand().getType() == Material.WOOD_PICKAXE) {
                                if (event.getBlock().getType() == Material.STAINED_GLASS || event.getBlock().getType() == Material.IRON_BLOCK) {
                                    event.setCancelled(true);
                                    player.sendMessage(Main.getPrefix() + MessageYaml.getValue("messages.blocks.unbreakable").replace("&", "§"));
                                    return;
                                }
                                return;
                            } else {
                                player.sendMessage(Main.getPrefix() + MessageYaml.getValue("messages.blocks.breakable-by-pickaxe").replace("&", "§"));
                                event.setCancelled(true);
                                return;
                            }
                        }

                    } else {
                        if (event.getBlock().getType() == Material.BEACON) {
                            event.setCancelled(true);
                            player.sendMessage(Main.getPrefix() + MessageYaml.getValue("messages.blocks.own-beacon").replace("&", "§"));
                            return;
                        } else if (event.getBlock().getType() == Material.STAINED_GLASS || event.getBlock().getType() == Material.IRON_BLOCK) {
                            event.setCancelled(true);
                            player.sendMessage(Main.getPrefix() + MessageYaml.getValue("messages.blocks.unbreakable").replace("&", "§"));
                            return;
                        }
                    }
                }
            }
            if ((player.getLocation().getBlockX() >= (vertMinX - marge)) && (player.getLocation().getBlockX() <= (vertMaxX + marge)) && (player.getLocation().getBlockZ() >= (vertMinZ - marge)) && (player.getLocation().getBlockZ() <= (vertMaxZ + marge))) {
                if (!daysRunnable.greenalive) {
                    return;
                }
                if(event.isCancelled()){
                    return;
                }
                if (!TeamSelect.team.get(player).equalsIgnoreCase("vert")) {

                    event.setCancelled(true);
                    player.sendMessage(Main.getPrefix() + MessageYaml.getValue("messages.blocks.region-marge-error").replace("&", "§"));


                }
            }
            //JAUNE
            if ((loc.getBlockX() >= jauneMinX && loc.getBlockX() <= jauneMaxX) && (loc.getBlockZ() >= jauneMinZ && loc.getBlockZ() <= jauneMaxZ)) {
                if (!daysRunnable.yellowalive) {
                    return;
                }
                if (TeamSelect.team.containsKey(player)) {
                    if (!TeamSelect.team.get(player).equalsIgnoreCase("jaune")) {
                        if (event.getBlock().getType() != Material.BEACON && event.getBlock().getType() != Material.STAINED_GLASS) {
                            player.sendMessage(Main.getPrefix() + MessageYaml.getValue("messages.blocks.breakable-by-pickaxe").replace("&", "§"));
                            event.setCancelled(true);
                            return;
                        } else {
                            if (player.getItemInHand().getType() == Material.DIAMOND_PICKAXE || player.getItemInHand().getType() == Material.GOLD_PICKAXE || player.getItemInHand().getType() == Material.IRON_PICKAXE || player.getItemInHand().getType() == Material.STONE_PICKAXE || player.getItemInHand().getType() == Material.WOOD_PICKAXE) {
                                if (event.getBlock().getType() == Material.STAINED_GLASS || event.getBlock().getType() == Material.IRON_BLOCK) {
                                    event.setCancelled(true);
                                    player.sendMessage(Main.getPrefix() + MessageYaml.getValue("messages.blocks.unbreakable").replace("&", "§"));
                                    return;
                                }
                                return;
                            } else {
                                //player.sendMessage(Main.getPrefix() + "§cVous ne pouvez casser que le beacon §e(PIOCHE)");
                                player.sendMessage(Main.getPrefix() + MessageYaml.getValue("messages.blocks.breakable-by-pickaxe").replace("&", "§"));
                                event.setCancelled(true);
                                return;
                            }
                        }

                    } else {
                        if (event.getBlock().getType() == Material.BEACON) {
                            event.setCancelled(true);
                            player.sendMessage(Main.getPrefix() + MessageYaml.getValue("messages.blocks.own-beacon").replace("&", "§"));
                            return;
                        } else if (event.getBlock().getType() == Material.STAINED_GLASS || event.getBlock().getType() == Material.IRON_BLOCK) {
                            event.setCancelled(true);
                            //player.sendMessage(Main.getPrefix() + "§cCe bloc est incassable");
                            player.sendMessage(Main.getPrefix() + MessageYaml.getValue("messages.blocks.unbreakable").replace("&", "§"));
                            return;
                        }
                    }
                }
            }
            if ((player.getLocation().getBlockX() >= (jauneMinX - marge)) && (player.getLocation().getBlockX() <= (jauneMaxX + marge)) && (player.getLocation().getBlockZ() >= (jauneMinZ - marge)) && (player.getLocation().getBlockZ() <= (jauneMaxZ + marge))) {

                if (!daysRunnable.yellowalive) {
                    return;
                }
                if(event.isCancelled()){
                    return;
                }
                if (!TeamSelect.team.get(player).equalsIgnoreCase("jaune")) {

                    event.setCancelled(true);
                    player.sendMessage(Main.getPrefix() + MessageYaml.getValue("messages.blocks.region-marge-error").replace("&", "§"));


                }
            }
        }
        public int marge = Main.INSTANCE.getConfig().getInt("regions.marge");

        @EventHandler
        public void onBlockPlace (BlockPlaceEvent event){
            if (!GameStatut.isStatut(GameStatut.GAME)) {
                return;
            }
            Player player = event.getPlayer();
            Location loc = event.getBlock().getLocation();


            if (event.getBlock().getType() == Material.TNT) {
                if (daysRunnable.day < 5) {
                    event.setCancelled(true);
                    //player.sendMessage(Main.getPrefix() + "§cVous pouvez pas poser de TNT§e(JOUR 5)");
                    player.sendMessage(Main.getPrefix() + MessageYaml.getValue("messages.tnt.day-error").replace("{jour}", "" + Main.INSTANCE.getConfig().getString("partie.jour-de-pvp")).replace("&", "§"));
                } else {
                    return;
                }
            }




            //ROUGE
            if ((loc.getBlockX() >= rougeMinX && loc.getBlockX() <= rougeMaxX) && (loc.getBlockZ() >= rougeMinZ && loc.getBlockZ() <= rougeMaxZ)) {





                if (!daysRunnable.redalive) {
                    return;
                }
                if((loc.getX() + 0.5  == Main.getBeaconRed().getX() && loc.getZ() + 0.5  == Main.getBeaconRed().getZ()) && loc.getY() > Main.getBeaconRed().getY()){

                    event.setCancelled(true);
                    player.sendMessage(Main.getPrefix() + MessageYaml.getValue("messages.blocks.place-error").replace("&", "§"));

                }
                if (TeamSelect.team.containsKey(player)) {
                    if (!TeamSelect.team.get(player).equalsIgnoreCase("rouge")) {
                        player.sendMessage(Main.getPrefix() + MessageYaml.getValue("messages.blocks.place-error").replace("&", "§"));
                        event.setCancelled(true);


                    }
                }
            }
            if ((player.getLocation().getBlockX() >= (rougeMinX - marge)) && (player.getLocation().getBlockX() <= (rougeMaxX + marge)) && (player.getLocation().getBlockZ() >= (rougeMinZ - marge)) && (player.getLocation().getBlockZ() <= (rougeMaxZ + marge))) {

                if (!daysRunnable.redalive) {
                    return;
                }
                if(event.isCancelled()){
                    return;
                }
                if (!TeamSelect.team.get(player).equalsIgnoreCase("rouge")) {

                    event.setCancelled(true);
                    player.sendMessage(Main.getPrefix() + MessageYaml.getValue("messages.blocks.region-marge-error").replace("&", "§"));


                }
            }
            //BLEU
            if ((loc.getBlockX() >= bleuMinX && loc.getBlockX() <= bleuMaxX) && (loc.getBlockZ() >= bleuMinZ && loc.getBlockZ() <= bleuMaxZ)) {

                if (!daysRunnable.bluealive) {
                    return;
                }
                //verif au dessus du beacon
                if((loc.getX() + 0.5  == Main.getBeaconBlue().getX() && loc.getZ() + 0.5  == Main.getBeaconBlue().getZ()) && loc.getY() > Main.getBeaconBlue().getY()){

                    event.setCancelled(true);
                    player.sendMessage(Main.getPrefix() + MessageYaml.getValue("messages.blocks.place-error").replace("&", "§"));

                }

                if (TeamSelect.team.containsKey(player)) {
                    if (!TeamSelect.team.get(player).equalsIgnoreCase("bleu")) {
                        player.sendMessage(Main.getPrefix() + MessageYaml.getValue("messages.blocks.place-error").replace("&", "§"));
                        event.setCancelled(true);
                    }

                }
            }
            //dans les marges
            if ((player.getLocation().getBlockX() >= (bleuMinX - marge)) && (player.getLocation().getBlockX() <= (bleuMaxX + marge)) && (player.getLocation().getBlockZ() >= (bleuMinZ - marge)) && (player.getLocation().getBlockZ() <= (bleuMaxZ + marge))) {
                if (!daysRunnable.bluealive) {
                    return;
                }
                if(event.isCancelled()){
                    return;
                }
                if (!TeamSelect.team.get(player).equalsIgnoreCase("bleu")) {

                    event.setCancelled(true);
                    player.sendMessage(Main.getPrefix() + MessageYaml.getValue("messages.blocks.region-marge-error").replace("&", "§"));


                }
            }

            //VERT
            if ((loc.getBlockX() >= vertMinX && loc.getBlockX() <= vertMaxX) && (loc.getBlockZ() >= vertMinZ && loc.getBlockZ() <= vertMaxZ)) {
                if (!daysRunnable.greenalive) {
                    return;
                }
                //verif au dessus beacon


                if(loc.getX() + 0.5 == Main.getBeaconGreen().getX() && loc.getZ() + 0.5 == Main.getBeaconGreen().getZ() && loc.getY() > Main.getBeaconGreen().getY()){

                    event.setCancelled(true);
                    player.sendMessage(Main.getPrefix() + MessageYaml.getValue("messages.blocks.place-error").replace("&", "§"));

                }

                if (TeamSelect.team.containsKey(player)) {
                    if (!TeamSelect.team.get(player).equalsIgnoreCase("vert")) {
                        player.sendMessage(Main.getPrefix() + MessageYaml.getValue("messages.blocks.place-error").replace("&", "§"));
                        event.setCancelled(true);
                    }

                }
            }
            if ((player.getLocation().getBlockX() >= (vertMinX - marge)) && (player.getLocation().getBlockX() <= (vertMaxX + marge)) && (player.getLocation().getBlockZ() >= (vertMinZ - marge)) && (player.getLocation().getBlockZ() <= (vertMaxZ + marge))) {
                if (!daysRunnable.greenalive) {
                    return;
                }
                if(event.isCancelled()){
                    return;
                }
                if (!TeamSelect.team.get(player).equalsIgnoreCase("vert")) {

                    event.setCancelled(true);
                    player.sendMessage(Main.getPrefix() + MessageYaml.getValue("messages.blocks.region-marge-error").replace("&", "§"));


                }
            }


            //JAUNE
            if ((loc.getBlockX() >= jauneMinX && loc.getBlockX() <= jauneMaxX) && (loc.getBlockZ() >= jauneMinZ && loc.getBlockZ() <= jauneMaxZ)) {
                if (!daysRunnable.yellowalive) {
                    return;
                }
                if((loc.getX() + 0.5  == Main.getBeaconYellow().getX() && loc.getZ() + 0.5  == Main.getBeaconYellow().getZ()) && loc.getY() > Main.getBeaconYellow().getY()){

                    event.setCancelled(true);
                    player.sendMessage(Main.getPrefix() + MessageYaml.getValue("messages.blocks.place-error").replace("&", "§"));

                }

                if (TeamSelect.team.containsKey(player)) {
                    if (!TeamSelect.team.get(player).equalsIgnoreCase("jaune")) {
                        player.sendMessage(Main.getPrefix() + MessageYaml.getValue("messages.blocks.place-error").replace("&", "§"));
                        event.setCancelled(true);


                    }
                }
            }
            if ((player.getLocation().getBlockX() >= (jauneMinX - marge)) && (player.getLocation().getBlockX() <= (jauneMaxX + marge)) && (player.getLocation().getBlockZ() >= (jauneMinZ - marge)) && (player.getLocation().getBlockZ() <= (jauneMaxZ + marge))) {
                if (!daysRunnable.yellowalive) {
                    return;
                }
                if(event.isCancelled()){
                    return;
                }
                if (!TeamSelect.team.get(player).equalsIgnoreCase("jaune")) {
                    event.setCancelled(true);
                    player.sendMessage(Main.getPrefix() + MessageYaml.getValue("messages.blocks.region-marge-error").replace("&", "§"));


                }
            }

        }

        @EventHandler
        public void onPortalEnter (PlayerPortalEvent event){
            if (!GameStatut.isStatut(GameStatut.GAME)) {
                event.setCancelled(true);
                return;
            }


            if (event.getPlayer().getWorld() == Bukkit.getWorld(Main.getWorld().getName())) {
                if (!(daysRunnable.day >= Main.INSTANCE.getConfig().getInt("partie.jour-nether"))) {

                    Player player = event.getPlayer();
                    player.sendMessage(MessageYaml.getValue("messages.nether.error-nether").replace("{jour}", Main.INSTANCE.getConfig().getString("partie.jour-nether")).replace("&", "§"));
                    event.setCancelled(true);


                }
            }
        }
        @EventHandler
        public void onChat (AsyncPlayerChatEvent event){
            if (GameStatut.isStatut(GameStatut.GAME)) {
                String str = event.getMessage();
                Player player = event.getPlayer();
                if (TeamSelect.team.containsKey(player)) {
                    event.setCancelled(true);

                    if (TeamSelect.team.get(player).equals("rouge")) {
                        Bukkit.broadcastMessage("§c§lRouge §7" + player.getName() + "§8» §7" + str);
                    } else if (TeamSelect.team.get(player).equals("bleu")) {
                        Bukkit.broadcastMessage("§9§lBleu §7" + player.getName() + "§8» §7" + str);
                    } else if (TeamSelect.team.get(player).equals("vert")) {
                        Bukkit.broadcastMessage("§a§lVert §7" + player.getName() + "§8» §7" + str);
                    } else if (TeamSelect.team.get(player).equals("jaune")) {
                        Bukkit.broadcastMessage("§e§lJaune §7" + player.getName() + "§8» §7" + str);
                    }


                } else {


                    Bukkit.broadcastMessage("§7" + player.getName() + "§8» §7" + str);
                    event.setCancelled(true);
                }

            }
        }


        @EventHandler
        public static void onCraft () {
            if (!Main.INSTANCE.getConfig().getBoolean("partie.custom-craft")) {
                return;
            }


            Iterator<Recipe> it = Main.INSTANCE.getServer().recipeIterator();
            Recipe recipe;
            while (it.hasNext()) {
                recipe = it.next();
                if (recipe != null && (recipe.getResult().getType() == Material.DIAMOND_CHESTPLATE || recipe.getResult().getType() == Material.DIAMOND_HELMET || recipe.getResult().getType() == Material.DIAMOND_LEGGINGS || recipe.getResult().getType() == Material.DIAMOND_BOOTS)) {
                    it.remove();
                }
            }


            //HAUT
            ShapedRecipe diamondPart = new ShapedRecipe(new ItemStack(Material.DIAMOND_CHESTPLATE));

            diamondPart.shape("% %", "%%%", "%%%");
            diamondPart.setIngredient('%', Material.DIAMOND_BLOCK);

            Main.INSTANCE.getServer().getRecipesFor(new ItemStack(Material.DIAMOND_CHESTPLATE)).clear();
            Main.INSTANCE.getServer().addRecipe(diamondPart);

            //CASQUE
            diamondPart = new ShapedRecipe(new ItemStack(Material.DIAMOND_HELMET));

            diamondPart.shape("%%%", "% %", "   ");
            diamondPart.setIngredient('%', Material.DIAMOND_BLOCK);

            Main.INSTANCE.getServer().addRecipe(diamondPart);
            //BOTTES
            diamondPart = new ShapedRecipe(new ItemStack(Material.DIAMOND_BOOTS));

            diamondPart.shape("   ", "% %", "% %");
            diamondPart.setIngredient('%', Material.DIAMOND_BLOCK);

            Main.INSTANCE.getServer().addRecipe(diamondPart);
            //PANTALON
            diamondPart = new ShapedRecipe(new ItemStack(Material.DIAMOND_LEGGINGS));

            diamondPart.shape("%%%", "% %", "% %");
            diamondPart.setIngredient('%', Material.DIAMOND_BLOCK);

            Main.INSTANCE.getServer().addRecipe(diamondPart);

            // -----------------------------EMERAUDE-----------------------
            //HAUT
            diamondPart = new ShapedRecipe(new ItemStack(Material.DIAMOND_CHESTPLATE));

            diamondPart.shape("% %", "%%%", "%%%");
            diamondPart.setIngredient('%', Material.EMERALD_BLOCK);

            Main.INSTANCE.getServer().getRecipesFor(new ItemStack(Material.DIAMOND_CHESTPLATE)).clear();
            Main.INSTANCE.getServer().addRecipe(diamondPart);

            //CASQUE
            diamondPart = new ShapedRecipe(new ItemStack(Material.DIAMOND_HELMET));

            diamondPart.shape("%%%", "% %", "   ");
            diamondPart.setIngredient('%', Material.EMERALD_BLOCK);

            Main.INSTANCE.getServer().addRecipe(diamondPart);
            //BOTTES
            diamondPart = new ShapedRecipe(new ItemStack(Material.DIAMOND_BOOTS));

            diamondPart.shape("   ", "% %", "% %");
            diamondPart.setIngredient('%', Material.EMERALD_BLOCK);

            Main.INSTANCE.getServer().addRecipe(diamondPart);
            //PANTALON
            diamondPart = new ShapedRecipe(new ItemStack(Material.DIAMOND_LEGGINGS));

            diamondPart.shape("%%%", "% %", "% %");
            diamondPart.setIngredient('%', Material.EMERALD_BLOCK);

            Main.INSTANCE.getServer().addRecipe(diamondPart);

            //EPEE
            diamondPart = new ShapedRecipe(new ItemStack(Material.DIAMOND_SWORD));

            diamondPart.shape(" % ", " % ", " * ");
            diamondPart.setIngredient('%', Material.EMERALD);
            diamondPart.setIngredient('*', Material.STICK);

            Main.INSTANCE.getServer().addRecipe(diamondPart);
            //HACHE
            diamondPart = new ShapedRecipe(new ItemStack(Material.DIAMOND_AXE));

            diamondPart.shape(" %%", " *%", " * ");
            diamondPart.setIngredient('%', Material.EMERALD);
            diamondPart.setIngredient('*', Material.STICK);

            Main.INSTANCE.getServer().addRecipe(diamondPart);

            diamondPart = new ShapedRecipe(new ItemStack(Material.DIAMOND_AXE));

            diamondPart.shape("%% ", "%* ", " * ");
            diamondPart.setIngredient('%', Material.EMERALD);
            diamondPart.setIngredient('*', Material.STICK);

            Main.INSTANCE.getServer().addRecipe(diamondPart);

            //PIOCHE
            diamondPart = new ShapedRecipe(new ItemStack(Material.DIAMOND_PICKAXE));

            diamondPart.shape("%%%", " * ", " * ");
            diamondPart.setIngredient('%', Material.EMERALD);
            diamondPart.setIngredient('*', Material.STICK);

            Main.INSTANCE.getServer().addRecipe(diamondPart);

            //HOUE
            diamondPart = new ShapedRecipe(new ItemStack(Material.DIAMOND_HOE));

            diamondPart.shape(" %%", " * ", " * ");
            diamondPart.setIngredient('%', Material.EMERALD);
            diamondPart.setIngredient('*', Material.STICK);

            Main.INSTANCE.getServer().addRecipe(diamondPart);

            diamondPart = new ShapedRecipe(new ItemStack(Material.DIAMOND_HOE));

            diamondPart.shape("%% ", " * ", " * ");
            diamondPart.setIngredient('%', Material.EMERALD);
            diamondPart.setIngredient('*', Material.STICK);

            Main.INSTANCE.getServer().addRecipe(diamondPart);


        }


    }
