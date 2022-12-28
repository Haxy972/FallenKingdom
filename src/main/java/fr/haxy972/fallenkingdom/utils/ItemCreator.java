package fr.haxy972.fallenkingdom.utils;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public class ItemCreator {
    private final ItemStack item;

    public ItemCreator(final ItemStack item) {
        this.item = item;
    }

    public ItemCreator(final Material mat) {
        this.item = new ItemStack(mat);
    }

    public ItemCreator(final Material mat, final int amount) {
        this.item = new ItemStack(mat, amount);
    }

    public ItemCreator(final Material mat, final byte data) {
        this.item = new ItemStack(mat, 1, data);
    }

    public ItemCreator(final Material mat, final int amount, final byte data) {
        this.item = new ItemStack(mat, amount, data);
    }

    public ItemCreator setName(final String name) {
        final ItemMeta meta = this.item.getItemMeta();
        meta.setDisplayName(name);
        this.item.setItemMeta(meta);
        return this;
    }

    public ItemCreator setData(final int data) {

        item.getData().setData((byte) data);
        return this;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public ItemCreator setLores(final String... lore) {
        final ItemMeta meta = this.item.getItemMeta();
        meta.setLore((List) Arrays.asList(lore));
        this.item.setItemMeta(meta);
        return this;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public ItemCreator setArrayLore(final List<String> lore) {
        final ItemMeta meta = this.item.getItemMeta();
        meta.setLore((List) lore);
        this.item.setItemMeta(meta);
        return this;
    }

    public ItemCreator setMaterial(Material material){
        this.item.setType(material);
        return this;
    }

    public ItemCreator setAmount(final int amount) {
        this.item.setAmount(amount);
        return this;
    }

    public ItemCreator setEnchant(final Enchantment e, final int lvl) {
        this.item.addUnsafeEnchantment(e, lvl);
        return this;
    }

    public ItemCreator unbreakable() {
        this.item.getItemMeta().spigot().setUnbreakable(true);
        return this;
    }

    public ItemCreator addItemFlag(ItemFlag flag) {
        this.item.getItemMeta().addItemFlags(flag);
        return this;
    }

    public ItemCreator addFlags() {
        for (ItemFlag itemFlag : ItemFlag.values()) {
            ItemMeta itemMeta = item.getItemMeta();
            itemMeta.addItemFlags(itemFlag);
            item.setItemMeta(itemMeta);
        }
        return this;
    }


    public ItemStack done() {
        return this.item;
    }

}
