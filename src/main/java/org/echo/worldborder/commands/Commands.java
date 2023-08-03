package org.echo.worldborder.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.echo.worldborder.Border;
import org.echo.worldborder.Main;

import java.util.Map;

public class Commands implements CommandExecutor {
    private Main plugin;

    public Commands(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length >= 1) {
            String subCommand = args[0].toLowerCase();
            switch (subCommand) {
                case "help":
                    if (sender.hasPermission("worldborder.help")) {
                        displayHelp(sender);
                    } else {
                        sender.sendMessage(plugin.getMessages().getPermissionCommand());
                    }
                    return true;
                case "reload":
                    if (sender.hasPermission("worldborder.reload")) {
                        plugin.reloadPlugin();
                        sender.sendMessage(ChatColor.GREEN + "[WorldBorder] Reloaded successfully");
                    } else {
                        sender.sendMessage(plugin.getMessages().getPermissionCommand());
                    }
                    return true;
                case "set":
                    if (sender.hasPermission("worldborder.set")) {
                        handleSetCommand(sender, args);
                    } else {
                        sender.sendMessage(plugin.getMessages().getPermissionCommand());
                    }
                    return true;
                case "delete":
                    if (sender.hasPermission("worldborder.delete")) {
                        handleDeleteCommand(sender, args);
                    } else {
                        sender.sendMessage(plugin.getMessages().getPermissionCommand());
                    }
                    return true;
                case "list":
                    if (sender.hasPermission("worldborder.list")) {
                        handleListCommand(sender);
                    } else {
                        sender.sendMessage(plugin.getMessages().getPermissionCommand());
                    }
                    return true;
                default:
                    break;
            }
        }
        sender.sendMessage(ChatColor.RED + "Unknown command. Use '" + ChatColor.YELLOW + "/border help" + ChatColor.RED + "' for available commands.");
        return true;
    }

    private void displayHelp(CommandSender sender) {
        sender.sendMessage(ChatColor.GOLD + "[WorldBorder]");
        sender.sendMessage(ChatColor.YELLOW + "/border list " + ChatColor.GRAY + "Display all worlds border");
        sender.sendMessage(ChatColor.YELLOW + "/border help " + ChatColor.GRAY + "Display plugin help");
        sender.sendMessage(ChatColor.YELLOW + "/border set <world> <size> [centerX] [centerZ] " + ChatColor.GRAY + "Set a world border in specific world");
        sender.sendMessage(ChatColor.YELLOW + "/border delete <world> " + ChatColor.GRAY + "Delete a world border");
        sender.sendMessage(ChatColor.YELLOW + "/border reload " + ChatColor.GRAY + "Reload plugin");
    }

    private void handleSetCommand(CommandSender sender, String[] args) {
        if (args.length >= 3) {
            try {
                int borderSize = Integer.parseInt(args[2]);

                if (Bukkit.getWorld(args[1]) == null) {
                    sender.sendMessage(ChatColor.RED + "The world '" + ChatColor.YELLOW + args[1] + ChatColor.RED + "' doesn't exist");
                    return ;
                }

                int centerX = 0;
                int centerZ = 0;
                if (sender instanceof Player) {
                    centerX = ((Player)sender).getLocation().getBlockX();
                    centerZ = ((Player)sender).getLocation().getBlockZ();
                }
                if (args.length >= 5) {
                    centerX = Integer.parseInt(args[3]);
                    centerZ = Integer.parseInt(args[4]);
                }

                // Appel à une méthode pour définir la bordure du monde
                plugin.getManager().addBorder(args[1], borderSize, centerX, centerZ);

                sender.sendMessage(ChatColor.GREEN + "World border set for the world '" + ChatColor.YELLOW + args[1] + ChatColor.GREEN + "'");
            } catch (NumberFormatException e) {
                sender.sendMessage(ChatColor.RED + "Invalid arguments. Please provide valid numbers for size, centerX, and centerZ.");
            }
        } else {
            sender.sendMessage(ChatColor.RED + "Usage: " + ChatColor.YELLOW + "/border set <world> <size> [centerX] [centerZ]");
        }
    }

    private void handleListCommand(CommandSender sender) {

        Map<String, Border> borders = plugin.getMyConfig().getBorders();

        sender.sendMessage(ChatColor.GOLD + "List of borders:");

        for (World world : Bukkit.getWorlds()) {

            Border border = borders.get(world.getName());
            if (border != null)
                sender.sendMessage("- " + ChatColor.YELLOW + world.getName() + ": " + ChatColor.GREEN + "on");
            else
                sender.sendMessage("- " + ChatColor.YELLOW + world.getName() + ": " + ChatColor.RED + "off");
        }
    }

    private void handleDeleteCommand(CommandSender sender, String[] args) {
        if (args.length == 2) {
            String worldName = args[1];

            if (Bukkit.getWorld(worldName) != null) {
                plugin.getManager().deleteBorder(worldName);
                sender.sendMessage(ChatColor.GREEN + "World border deleted for the world '" + ChatColor.RED + worldName + ChatColor.GREEN + "'");
            }
            else {
                sender.sendMessage(ChatColor.RED + "The world '" + ChatColor.YELLOW + worldName + ChatColor.RED + "' doesn't exist");
            }
        } else {
            sender.sendMessage(ChatColor.RED + "Usage: " + ChatColor.YELLOW + "/worldborder delete <world>");
        }
    }
}
