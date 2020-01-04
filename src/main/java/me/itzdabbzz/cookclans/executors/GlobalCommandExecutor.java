package me.itzdabbzz.cookclans.executors;

import me.itzdabbzz.cookclans.CookClans;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GlobalCommandExecutor implements CommandExecutor {
    CookClans plugin;

    public GlobalCommandExecutor() {
        plugin = CookClans.getInstance();
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player = (Player) commandSender;

        if (strings.length == 0) {
            return false;
        }

        String subCommand = strings[0];
        return plugin.getClanManager().processGlobalChat(player, subCommand);
    }
}
