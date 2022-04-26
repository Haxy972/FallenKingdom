package fr.haxy972.fallen.commands;


import fr.haxy972.fallen.Main;
import fr.haxy972.fallen.runnable.daysRunnable;
import fr.haxy972.fallen.utils.ChestRefill;
import fr.haxy972.fallen.utils.MessageYaml;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.haxy972.fallen.GameStatut;
import fr.haxy972.fallen.manager.GameManager;


public class CommandsDebug implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }

        if (cmd.getName().equalsIgnoreCase("reset")) {
            if (args.length > 0) {






            } else {
                Player player = (Player) sender;
                player.sendMessage(GameManager.chestsloc.toString());
            }

        }


        return false;
    }

}
