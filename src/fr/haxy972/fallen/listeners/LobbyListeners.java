package fr.haxy972.fallen.listeners;

import java.util.ArrayList;

import fr.haxy972.fallen.Main;
import fr.haxy972.fallen.Scoreboard.ScoreboardManager;
import fr.haxy972.fallen.manager.GameManager;
import fr.haxy972.fallen.utils.MessageYaml;
import org.bukkit.*;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.haxy972.fallen.GameStatut;
import fr.haxy972.fallen.Team.select.TeamSelect;
import org.bukkit.scoreboard.Team;

public class LobbyListeners implements Listener {


    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (GameStatut.isStatut(GameStatut.LOBBY)) {
            if (event.getEntity() instanceof Player) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (GameStatut.isStatut(GameStatut.LOBBY)) {
            event.setCancelled(true);
            Player player = event.getPlayer();
            player.sendMessage(MessageYaml.getValue("messages.partie.lobby.block-place-break").replace("&", "§"));
        }
    }

    @EventHandler
    public void onPlaceBreak(BlockPlaceEvent event) {
        if (!GameStatut.isStatut(GameStatut.LOBBY)) {
            return;
        }
        if (GameStatut.isStatut(GameStatut.LOBBY)) {
            event.setCancelled(true);
            if ((event.getBlock().getType() != Material.WOOL) && (event.getBlock().getType() != Material.BARRIER)) {
                Player player = event.getPlayer();
                player.sendMessage(MessageYaml.getValue("messages.partie.lobby.block-place-break").replace("&", "§"));
            }


        }
    }

    @EventHandler
    public void onBouffe(FoodLevelChangeEvent event) {
        if (GameStatut.isStatut(GameStatut.LOBBY)) {


            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event){
        if(!GameStatut.isStatut(GameStatut.LOBBY)){
            return;
        }

        Player player = event.getPlayer();
        Location playerloc = player.getLocation();
        double playerlocy = player.getLocation().getY() - 1;
        Location newloc = new Location(player.getWorld(), playerloc.getX(), playerlocy, playerloc.getZ());
        if(newloc.getBlock().getType() == Material.STAINED_GLASS){
            if(TeamSelect.team.containsKey(player)){
                switch (TeamSelect.team.get(player)){

                    case "rouge":
                        newloc.getBlock().setData((byte) 14);
                        break;
                    case "bleu":
                        newloc.getBlock().setData((byte) 11);
                        break;
                    case "vert":
                        newloc.getBlock().setData((byte) 5);
                        break;
                    case "jaune":
                        newloc.getBlock().setData((byte) 4);
                        break;
                    default:
                        newloc.getBlock().setData((byte) 7);
                        break;


                }
            }
        }











        for(Chunk chunk : Main.getWorld().getLoadedChunks()){
            for(BlockState bs : chunk.getTileEntities()){
                if(bs instanceof Chest){
                    Chest chest = (Chest) bs;
                    chest.getInventory().clear();
                    if(!GameManager.chestsloc.contains(bs.getLocation())) {
                        GameManager.chestsloc.add(bs.getLocation());
                    }

                }
            }
        }


        Location spawn = Main.getLobbySpawn();

        int ison = 0;
        int rayon = Main.INSTANCE.getConfig().getInt("locations.rayon");
        for(Entity entity : Bukkit.getWorld(Main.getWorld().getName()).getNearbyEntities(spawn, rayon, rayon, rayon)){
            if(entity == player){
                ison = 1;
                for(Entity e : player.getWorld().getNearbyEntities(player.getLocation(), 200,200,200)){
                    if(!(e instanceof Player)){
                        e.remove();
                    }
                }
            }
        }
        if(ison == 0){
            player.sendMessage(Main.getPrefix() + MessageYaml.getValue("messages.partie.lobby.too-far").replace("&", "§"));
            player.teleport(spawn);
        }

    }


    @EventHandler
    public void onChat(AsyncPlayerChatEvent event){
        if(GameStatut.isStatut(GameStatut.LOBBY)) {
            String str = event.getMessage();
            Player player = event.getPlayer();
            Bukkit.broadcastMessage("§7" + player.getName() + "§8» §7" + str);
            event.setCancelled(true);

        }
    }


    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!GameStatut.isStatut(GameStatut.LOBBY)) {
            return;
        }
        Action action = event.getAction();
        ItemStack item = event.getItem();
        Player player = event.getPlayer();


        if (((item != null) && action == null) || (item == null && action != null)) {
            return;
        }

        event.setCancelled(true);
        switch (item.getType()) {
            case WOOL:
                if ((item.getItemMeta().getDisplayName() == "§b§lEquipe§8» §7Aucune") || (item.getItemMeta().getDisplayName() == "§b§lEquipe§8» §c§lROUGE") || (item.getItemMeta().getDisplayName() == "§b§lEquipe§8» §9§lBLEU") || (item.getItemMeta().getDisplayName() == "§b§lEquipe§8» §a§lVERT") || (item.getItemMeta().getDisplayName() == "§b§lEquipe§8» §e§lJAUNE")) {
                    if ((action == Action.RIGHT_CLICK_AIR) || (action == Action.RIGHT_CLICK_BLOCK)) {
                        player.closeInventory();
                        openTeamInventory(player);

                    }
                }

                break;

            default:
                break;
        }


    }

    private void openTeamInventory(Player player) {
        if (!GameStatut.isStatut(GameStatut.LOBBY)) {
            return;
        }
        Inventory inventory = Bukkit.createInventory(null, 9 * 1, "§7§b§lEquipes");
        //Rouge
        int i = 0;
        for (Player players : Bukkit.getOnlinePlayers()) {
            if (TeamSelect.team.containsKey(players)) {
                if (TeamSelect.team.get(players).equalsIgnoreCase("rouge")) {
                    i++;
                }

            }
        }
        if (i == 0) {
            i = 1;
        }


        ItemStack rouge = new ItemStack(Material.WOOL, i, (short) 14);
        ItemMeta rougemeta = rouge.getItemMeta();
        rougemeta.setDisplayName("§c§lRouge");
        ArrayList<String> rougelore = new ArrayList<>();
        rougelore.add("§a");

        int counter = 0;
        for (Player players : Bukkit.getOnlinePlayers()) {
            if (TeamSelect.team.get(players) == "rouge") {
                rougelore.add("§8» §7" + players.getName());
                counter++;
            }

        }
        if (counter > 0) {
            rougelore.add("§d");
        }

        rougelore.add("§8» §bCliquez pour rejoindre");
        rougemeta.setLore(rougelore);
        rouge.setItemMeta(rougemeta);
        inventory.setItem(2, rouge);
        rougelore.clear();

        //Bleu
        i = 0;
        for (Player players : Bukkit.getOnlinePlayers()) {
            if (TeamSelect.team.containsKey(players)) {
                if (TeamSelect.team.get(players).equalsIgnoreCase("bleu")) {
                    i++;
                }

            }
        }
        if (i == 0) {
            i = 1;
        }

        ItemStack bleu = new ItemStack(Material.WOOL, i, (short) 11);
        ItemMeta bleumeta = bleu.getItemMeta();
        bleumeta.setDisplayName("§9§lBleu");
        ArrayList<String> bleulore = new ArrayList<>();
        bleulore.add("§a");

        counter = 0;
        for (Player players : Bukkit.getOnlinePlayers()) {
            if (TeamSelect.team.get(players) == "bleu") {
                bleulore.add("§8» §7" + players.getName());
                counter++;
            }

        }
        if (counter > 0) {
            bleulore.add("§d");
        }

        bleulore.add("§8» §bCliquez pour rejoindre");
        bleumeta.setLore(bleulore);
        bleu.setItemMeta(bleumeta);
        inventory.setItem(3, bleu);

        //Vert
        i = 0;
        for (Player players : Bukkit.getOnlinePlayers()) {
            if (TeamSelect.team.containsKey(players)) {
                if (TeamSelect.team.get(players).equalsIgnoreCase("vert")) {
                    i++;
                }

            }
        }
        if (i == 0) {
            i = 1;
        }

        ItemStack vert = new ItemStack(Material.WOOL, i, (short) 13);
        ItemMeta vertmeta = vert.getItemMeta();
        vertmeta.setDisplayName("§a§lVert");
        ArrayList<String> vertlore = new ArrayList<>();
        vertlore.add("§a");

        counter = 0;
        for (Player players : Bukkit.getOnlinePlayers()) {
            if (TeamSelect.team.get(players) == "vert") {
                vertlore.add("§8» §7" + players.getName());
                counter++;
            }

        }
        if (counter > 0) {
            vertlore.add("§d");
        }

        vertlore.add("§8» §bCliquez pour rejoindre");
        vertmeta.setLore(vertlore);
        vert.setItemMeta(vertmeta);
        inventory.setItem(4, vert);

        //Jaune
        i = 0;
        for (Player players : Bukkit.getOnlinePlayers()) {
            if (TeamSelect.team.containsKey(players)) {
                if (TeamSelect.team.get(players).equalsIgnoreCase("jaune")) {
                    i++;
                }

            }
        }
        if (i == 0) {
            i = 1;
        }

        ItemStack jaune = new ItemStack(Material.WOOL, i, (short) 4);
        ItemMeta jaunemeta = jaune.getItemMeta();
        jaunemeta.setDisplayName("§e§lJaune");
        ArrayList<String> jaunelore = new ArrayList<>();
        jaunelore.add("§a");

        counter = 0;
        for (Player players : Bukkit.getOnlinePlayers()) {
            if (TeamSelect.team.get(players) == "jaune") {
                jaunelore.add("§8» §7" + players.getName());
                counter++;
            }

        }
        if (counter > 0) {
            jaunelore.add("§d");
        }

        jaunelore.add("§8» §bCliquez pour rejoindre");
        jaunemeta.setLore(jaunelore);
        jaune.setItemMeta(jaunemeta);
        inventory.setItem(5, jaune);

        //NO TEAM
        ItemStack it = new ItemStack(Material.BARRIER);
        ItemMeta im = it.getItemMeta();
        im.setDisplayName("§7Aucune");
        it.setItemMeta(im);
        inventory.setItem(8, it);


        player.openInventory(inventory);
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_PLING, 1f, 1f);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        if (!GameStatut.isStatut(GameStatut.LOBBY)) {
            return;
        }


        Player player = (Player) event.getWhoClicked();
        ItemStack it = event.getCurrentItem();
        if (it == null) {
            return;
        }
        event.setCancelled(true);

        switch (it.getType()) {
            case WOOL:
                if (it.getItemMeta().getDisplayName().equalsIgnoreCase("§c§lRouge")) {

                    new TeamSelect(player).setTeam("rouge");
                    player.closeInventory();
                } else if (it.getItemMeta().getDisplayName().equalsIgnoreCase("§9§lBleu")) {

                    new TeamSelect(player).setTeam("bleu");
                    player.closeInventory();

                } else if (it.getItemMeta().getDisplayName().equalsIgnoreCase("§a§lVert")) {
                    new TeamSelect(player).setTeam("vert");
                    player.closeInventory();
                } else if (it.getItemMeta().getDisplayName().equalsIgnoreCase("§e§lJaune")) {
                    new TeamSelect(player).setTeam("jaune");
                    player.closeInventory();
                }
                break;
            case BARRIER:
                if (it.getItemMeta().getDisplayName() == "§7Aucune") {
                    new TeamSelect(player).setTeam("reset");
                    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
                    player.closeInventory();
                }
            default:
                break;
        }
    }

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event) {
        if (!GameStatut.isStatut(GameStatut.LOBBY)) {
            return;
        }
        if (GameStatut.isStatut(GameStatut.LOBBY)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();


        if(ScoreboardManager.scoreboardGame.containsKey(player)){
            ScoreboardManager.scoreboardGame.remove(player);

        }




        if (!GameStatut.isStatut(GameStatut.LOBBY)) {
            return;
        }
        event.setQuitMessage("");
        if (TeamSelect.team.get(player) != null) {
            String str = TeamSelect.team.get(player);
            //Rouge
            if (str.equalsIgnoreCase("rouge")) {
                TeamSelect.rougeNumber--;
            }
            //Bleu
            if (str.equalsIgnoreCase("bleu")) {
                TeamSelect.bleuNumber--;
            }
            //Vert
            if (str.equalsIgnoreCase("vert")) {
                TeamSelect.vertNumber--;
            }
            //Jaune
            if (str.equalsIgnoreCase("jaune")) {
                TeamSelect.jauneNumber--;
            }
            TeamSelect.team.remove(player);
        }
    }


}
