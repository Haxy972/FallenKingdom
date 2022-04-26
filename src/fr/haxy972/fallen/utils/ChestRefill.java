package fr.haxy972.fallen.utils;

import fr.haxy972.fallen.Main;
import fr.haxy972.fallen.manager.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.Random;

public class ChestRefill {

    public static void Refill() {
        chestContent();
        Bukkit.broadcastMessage(Main.getPrefix() + MessageYaml.getValue("messages.partie.chest-refill").replace("&", "§"));

        for (Location loca : GameManager.chestsloc) {

            for (BlockState bs : loca.getChunk().getTileEntities()) {
                if (bs instanceof Chest) {
                    if (GameManager.chestsloc.contains(bs.getLocation())) {


                        Inventory chestinv = ((Chest) bs).getInventory();
                        chestinv.clear();

                        for (int i = 0; i < 10; i++) {
                            Random rand = new Random();
                            int randint = rand.nextInt(items.size());
                            int randslot = rand.nextInt(chestinv.getSize());
                            chestinv.setItem(randslot, items.get(randint));

                        }
                    }
                }

            }
        }
        items.clear();

    }


    public static ArrayList<ItemStack> items = new ArrayList<>();

    public static void chestContent() {
        items.add(getItem(null, Material.TNT, 1, null, null, null));
        items.add(getItem(null, Material.STONE_SWORD, 1, null, null, null));
        items.add(getItem(null, Material.STONE, 32, null, null, null));
        items.add(getItem(null, Material.SAND, 8, null, null, null));
        items.add(getItem(null, Material.IRON_PICKAXE, 1, null, null, null));
        items.add(getItem(null, Material.IRON_AXE, 1, null, null, null));
        items.add(getItem(null, Material.IRON_HOE, 1, null, null, null));
        items.add(getItem(null, Material.LEATHER_HELMET, 1, null, Enchantment.PROTECTION_ENVIRONMENTAL, 1));
        items.add(getItem(null, Material.IRON_SWORD, 1, null, null, null));
        items.add(getItem(null, Material.ARROW, 16, null, null, null));
        items.add(getItem(null, Material.ARROW, 16, null, null, null));
        items.add(getItem(null, Material.ARROW, 16, null, null, null));
        items.add(getItem(null, Material.BOW, 1, null, null, null));
        items.add(getItem(null, Material.IRON_BOOTS, 1, null, null, null));
        items.add(getItem(null, Material.IRON_CHESTPLATE, 1, null, null, null));
        items.add(getItem(null, Material.PUMPKIN, 1, null, null, null));
        items.add(getItem(null, Material.COOKED_BEEF, 8, null, null, null));
        items.add(getItem(null, Material.COOKED_CHICKEN, 8, null, null, null));
        items.add(getItem(null, Material.CARROT, 16, null, null, null));
        items.add(getItem(null, Material.BREAD, 16, null, null, null));
        items.add(getItem(null, Material.SHEARS, 1, null, null, null));
        items.add(getItem(null, Material.WOOD_PICKAXE, 1, null, null, null));
        items.add(getItem(null, Material.WOOD_AXE, 1, null, null, null));
        items.add(getItem(null, Material.SHEARS, 1, null, null, null));
        items.add(getItem(null, Material.FLINT_AND_STEEL, 1, null, null, null));
        items.add(getItem(null, Material.FLINT_AND_STEEL, 1, null, null, null));
        items.add(getItem(null, Material.WATER_BUCKET, 1, null, null, null));
        items.add(getItem(null, Material.BUCKET, 1, null, null, null));
        items.add(getItem(null, Material.LAVA_BUCKET, 1, null, null, null));
        items.add(getItem(null, Material.ENDER_PEARL, 2, null, null, null));
        items.add(getItem("§ePQ", Material.PAPER, 1, null, null, null));
        items.add(getItem("§ePQ", Material.PAPER, 1, null, null, null));
        items.add(getItem("§ePQ", Material.PAPER, 1, null, null, null));
        items.add(getItem(null, Material.TORCH, 16, null, null, null));
        items.add(getItem(null, Material.ANVIL, 1, null, null, null));
    }
//tnt 2/36


    public static ItemStack getItem(String name, Material material, Integer count, MaterialData data, Enchantment enchantment, Integer level) {

        ItemStack it = new ItemStack(material, count);
        ItemMeta im = it.getItemMeta();
        im.setDisplayName(name);
        it.setData(data);
        if (enchantment != null && level != null) {
            it.addEnchantment(enchantment, level);
        }

        it.setItemMeta(im);

        return it;

    }

}
