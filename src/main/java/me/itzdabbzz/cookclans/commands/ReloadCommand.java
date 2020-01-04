package me.itzdabbzz.cookclans.commands;

import me.itzdabbzz.cookclans.ChatBlock;
import me.itzdabbzz.cookclans.Clan;
import me.itzdabbzz.cookclans.CookClans;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author phaed
 */
public class ReloadCommand
{
    public ReloadCommand()
    {
    }

    /**
     * Execute the command
     *
     * @param sender
     * @param arg
     */
    public void execute(CommandSender sender, String[] arg)
    {
        CookClans plugin = CookClans.getInstance();

        if (sender instanceof Player && !plugin.getPermissionsManager().has((Player)sender, "simpleclans.admin.reload"))
        {
        	ChatBlock.sendMessage(sender, ChatColor.RED + "Does not match a clan command");
        	return;
        }
        plugin.reloadConfig();
        plugin.getSettingsManager().load();
        plugin.getLanguageManager().load();
        plugin.getStorageManager().importFromDatabase();
        CookClans.getInstance().getPermissionsManager().loadPermissions();

        for (Clan clan : plugin.getClanManager().getClans())
        {
            CookClans.getInstance().getPermissionsManager().updateClanPermissions(clan);
        }
        ChatBlock.sendMessage(sender, ChatColor.AQUA + plugin.getLang("configuration.reloaded"));

    }
}
