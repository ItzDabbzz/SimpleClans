package me.itzdabbzz.cookclans.commands;

import java.text.MessageFormat;
import java.util.UUID;

import me.itzdabbzz.cookclans.ChatBlock;
import me.itzdabbzz.cookclans.ClanPlayer;
import me.itzdabbzz.cookclans.CookClans;
import me.itzdabbzz.cookclans.uuid.UUIDMigration;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author roinujnosde
 */
public class PurgeCommand {

    /**
     * Executes the command
     *
     * @param sender
     * @param args
     */
    public void execute(CommandSender sender, String args[]) {
        CookClans plugin = CookClans.getInstance();
        if (sender instanceof Player) {
            if (!plugin.getPermissionsManager().has((Player) sender, "simpleclans.admin.purge")) {
                ChatBlock.sendMessage(sender, ChatColor.RED + plugin.getLang("insufficient.permissions"));
                return;
            }
        }

        if (args.length == 1) {
            UUID uuid = UUIDMigration.getForcedPlayerUUID(args[0]);
            ClanPlayer cp = plugin.getClanManager().getClanPlayer(uuid);
            if (uuid == null || cp == null) {
                ChatBlock.sendMessage(sender, ChatColor.RED + plugin.getLang("no.player.matched"));
                return;
            }

            plugin.getClanManager().deleteClanPlayer(cp);
            ChatBlock.sendMessage(sender, ChatColor.AQUA + plugin.getLang("player.purged"));
            return;
        }

        ChatBlock.sendMessage(sender, ChatColor.RED
                + MessageFormat.format(plugin.getLang("usage.purge"), plugin.getSettingsManager().getCommandClan()));
    }
}
