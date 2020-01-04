package me.itzdabbzz.cookclans.executors;

import me.itzdabbzz.cookclans.ChatBlock;
import me.itzdabbzz.cookclans.Helper;
import me.itzdabbzz.cookclans.CookClans;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AllyCommandExecutor implements CommandExecutor {

    private final CookClans plugin;

    public AllyCommandExecutor() {
        plugin = CookClans.getInstance();
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player = (Player) commandSender;

        if (!plugin.getSettingsManager().isAllyChatEnable()) {
            return false;
        }

        if (!plugin.getPermissionsManager().has(player, "simpleclans.member.ally")) {
            ChatBlock.sendMessage(player, ChatColor.RED + plugin.getLang("insufficient.permissions"));
            return true;
        }

        plugin.getClanManager().processAllyChat(player, Helper.toMessage(strings));

        return false;
    }
}
