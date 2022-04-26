package fr.haxy972.fallen.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;

import java.util.HashMap;
import java.util.Map;

public class ResetListeners implements Listener {

    static Map<Location, Block> blocksPlaced = new HashMap<>();
    static Map<Location, Material> blocksDestroyedMaterial = new HashMap<>();
    static Map<Location, Byte> blocksDestroyedData = new HashMap<>();

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (!event.isCancelled()) {

            blocksPlaced.put(event.getBlock().getLocation(), event.getBlock());
        }


    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (!event.isCancelled()) {

            if (!blocksDestroyedMaterial.containsKey(event.getBlock().getLocation())) {
                if(!blocksPlaced.containsKey(event.getBlock().getLocation())){

                    blocksDestroyedMaterial.put(event.getBlock().getLocation(), event.getBlock().getType());
                    blocksDestroyedData.put(event.getBlock().getLocation(), event.getBlock().getData());
                }
            }
        }


    }

    @EventHandler
    public void onExplosionPrime(EntityExplodeEvent event){

        for(Block block : event.blockList()){

            if(block.getType() != Material.BEACON && block.getType() != Material.STAINED_GLASS){
                if(!blocksDestroyedMaterial.containsKey(block.getLocation()))    {
                    if(!blocksPlaced.containsKey(block.getLocation()))
                        blocksDestroyedMaterial.put(block.getLocation(), block.getType());
                        blocksDestroyedData.put(block.getLocation(), block.getData());
                }




            }




        }




    }

    @EventHandler
    public static void reloadBlocks() {
        //replace par air
        for (Location loc : blocksPlaced.keySet()) {
            loc.getBlock().setType(Material.AIR);
        }
        //replace par normal
        for (Location loc : blocksDestroyedMaterial.keySet()) {
            Material mat = blocksDestroyedMaterial.get(loc);
            Byte data = blocksDestroyedData.get(loc);


            loc.getBlock().setType(mat);
            loc.getBlock().setData(data);






        }
        blocksPlaced.clear();
        blocksDestroyedMaterial.clear();
        blocksDestroyedData.clear();


    }


}
