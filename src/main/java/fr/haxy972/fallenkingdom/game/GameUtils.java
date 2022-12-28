package fr.haxy972.fallenkingdom.game;

import fr.haxy972.fallenkingdom.Main;
import fr.haxy972.fallenkingdom.utils.ItemCreator;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GameUtils {

    public ItemStack getPlayerHead(String playerName, String title, String... lores) {
        ItemStack skull = new ItemCreator(Material.SKULL_ITEM, 1, (byte) 3).setName(title).setLores(lores).done();
        SkullMeta meta = (SkullMeta) skull.getItemMeta();
        meta.setOwner(playerName);
        skull.setItemMeta(meta);
        return skull;
    }

    public void addCustomsCraft () {
        Iterator<Recipe> it = Main.getInstance().getServer().recipeIterator();
        List<ShapedRecipe> craftItems = new ArrayList<>();
        Server server = Main.getInstance().getServer();
        Recipe recipe;
        while (it.hasNext()) {
            recipe = it.next();
            if (recipe != null && (recipe.getResult().getType() == Material.DIAMOND_CHESTPLATE || recipe.getResult().getType() == Material.DIAMOND_HELMET || recipe.getResult().getType() == Material.DIAMOND_LEGGINGS || recipe.getResult().getType() == Material.DIAMOND_BOOTS
                    || recipe.getResult().getType().equals(Material.DIAMOND_SWORD) || recipe.getResult().getType().equals(Material.DIAMOND_PICKAXE) || recipe.getResult().getType().equals(Material.DIAMOND_AXE) || recipe.getResult().getType().equals(Material.DIAMOND_HOE) || recipe.getResult().getType().equals(Material.DIAMOND_SPADE)
            )) {
                it.remove();
            }
        }

        craftItems.add(new ShapedRecipe(new ItemStack(Material.DIAMOND_CHESTPLATE)).shape("% %", "%%%", "%%%"));
        craftItems.add(new ShapedRecipe(new ItemStack(Material.DIAMOND_HELMET)).shape("%%%", "% %", "   "));
        craftItems.add(new ShapedRecipe(new ItemStack(Material.DIAMOND_BOOTS)).shape("   ", "% %", "% %"));
        craftItems.add(new ShapedRecipe(new ItemStack(Material.DIAMOND_LEGGINGS)).shape("%%%", "% %", "% %"));
        craftItems.add(new ShapedRecipe(new ItemStack(Material.DIAMOND_SWORD)).shape(" % ", " % ", " * "));
        craftItems.add(new ShapedRecipe(new ItemStack(Material.DIAMOND_AXE)).shape(" %%", " *%", " * "));
        craftItems.add(new ShapedRecipe(new ItemStack(Material.DIAMOND_PICKAXE)).shape("%%%", " * ", " * "));
        craftItems.add(new ShapedRecipe(new ItemStack(Material.DIAMOND_HOE)).shape(" %%", " * ", " * "));
        craftItems.add(new ShapedRecipe(new ItemStack(Material.DIAMOND_SPADE)).shape(" % ", " * ", " * "));

        for (ShapedRecipe shapedRecipe : craftItems) {
            boolean containStick = false;
            for (String shapeBar : shapedRecipe.getShape()) {
                if (shapeBar.contains("*")) {
                    containStick = true;
                }
            }
            if (containStick) shapedRecipe.setIngredient('*', Material.STICK);
            shapedRecipe.setIngredient('%', Material.DIAMOND_BLOCK);
            server.addRecipe(shapedRecipe);
            shapedRecipe.setIngredient('%', Material.EMERALD_BLOCK);
            if (containStick) shapedRecipe.setIngredient('*', Material.STICK);
            server.addRecipe(shapedRecipe);
        }
    }

    public void setEnderPortal(Location enderPortal, Material material) {
        for(int x = -1; x <= 1; x++){
            for(int z = -1; z <= 1; z++){
                Location location = new Location(enderPortal.getWorld(), enderPortal.getX() + x , enderPortal.getY(), enderPortal.getZ() + z);
                location.getBlock().setType(material);
            }
        }
    }
}
