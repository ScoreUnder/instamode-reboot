package com.github.scoreunder.instamode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.StringUtil;

public class InstaMode extends JavaPlugin {
    private boolean changePlayerMode(final Player player, final GameMode mode) {
        final String modeName = mode.name().toLowerCase();
        if (player.getGameMode() == mode) {
            player.sendMessage(ChatColor.RED + "You're already in " + modeName + " mode!");
            return true;
        }
        player.setGameMode(mode);
        player.sendMessage(ChatColor.GREEN + "Set to " + modeName + " mode!");
        getLogger().info(player.getName() + " is now in " + modeName + " mode.");
        return false;
    }

    private GameMode findGameMode(String name) {
        name = name.toLowerCase();
        for (final GameMode g : GameMode.values())
            if (g.name().toLowerCase().startsWith(name))
                return g;
        return null;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd, final String commandLabel, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can change gamemodes!");
            return true;
        }
        final Player player = (Player) sender;

        if (cmd.getName().equals("mode") && args.length == 1) {
            GameMode mode = findGameMode(args[0]);
            if (mode == null) {
                player.sendMessage(ChatColor.RED + "No such game mode!");
                return true;
            }
            if (!hasPermission(mode.name().toLowerCase(), player)) {
                player.sendMessage(ChatColor.RED + "You don't have permission to change to this game mode.");
                return true;
            }

            changePlayerMode(player, mode);
            return true;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(final CommandSender sender, final Command command, final String commandLabel, final String[] args) {
        if (args.length != 1) return Collections.emptyList();
        final String arg = args[0].toLowerCase();
        return Arrays.stream(GameMode.values())
                .map(e -> e.name())
                .filter(e -> e.toLowerCase().startsWith(arg))
                .collect(Collectors.toList());
    }

    public static boolean hasPermission(final String mode, final Player player) {
        return player.hasPermission("*") || player.hasPermission("instamode.*") || player.hasPermission("instamode." + mode.toLowerCase());
    }
}
