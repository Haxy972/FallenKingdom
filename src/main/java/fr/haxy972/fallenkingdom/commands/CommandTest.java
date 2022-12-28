package fr.haxy972.fallenkingdom.commands;

import fr.haxy972.fallenkingdom.game.GameManager;
import fr.haxy972.fallenkingdom.teams.Team;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandTest implements CommandExecutor {

    private final GameManager gameManager;

    public CommandTest(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {

        if(args.length > 0) {
            gameManager.getGameRunnable().timer = gameManager.getDayDuration();
        }else{
            for (Chunk chunk : gameManager.getWorld().getLoadedChunks()) {
                for (BlockState blockState : chunk.getTileEntities()) {
                    if (blockState instanceof Chest) {
                        Bukkit.broadcastMessage(blockState.getLocation().toString());
                    }
                }
            }
        }


        return false;
    }
}
