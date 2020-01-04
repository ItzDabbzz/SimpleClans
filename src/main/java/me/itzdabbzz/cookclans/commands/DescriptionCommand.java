package me.itzdabbzz.cookclans.commands;

import java.text.MessageFormat;

import me.itzdabbzz.cookclans.managers.SettingsManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.itzdabbzz.cookclans.ChatBlock;
import me.itzdabbzz.cookclans.Clan;
import me.itzdabbzz.cookclans.ClanPlayer;
import me.itzdabbzz.cookclans.Helper;
import me.itzdabbzz.cookclans.CookClans;

public class DescriptionCommand {

	/**
	 * Execute the command
	 *
	 * @param player
	 * @param args
	 */
	public void execute(Player player, String[] args) {
		final CookClans plugin = CookClans.getInstance();
		final SettingsManager sm = plugin.getSettingsManager();

		if (!plugin.getPermissionsManager().has(player, "simpleclans.leader.description")) {
			ChatBlock.sendMessage(player, ChatColor.RED + plugin.getLang("insufficient.permissions"));
			return;
		}

		ClanPlayer cp = plugin.getClanManager().getClanPlayer(player);
		if (cp == null) {
            ChatBlock.sendMessage(player, ChatColor.RED + plugin.getLang("not.a.member.of.any.clan"));
            return;
        }

        Clan clan = cp.getClan();

        if (!clan.isVerified()) {
            ChatBlock.sendMessage(player, ChatColor.RED + plugin.getLang("clan.is.not.verified"));
            return;
        }
        if (!clan.isLeader(player)) {
            ChatBlock.sendMessage(player, ChatColor.RED + plugin.getLang("no.leader.permissions"));
            return;
        }
        
        if (args.length < 1) {
            ChatBlock.sendMessage(player, ChatColor.RED + MessageFormat.format(plugin.getLang("usage.description"), plugin.getSettingsManager().getCommandClan()));
            return;
        }
        
        String description = Helper.toMessage(args);
        if (description.length() < sm.getClanMinDescriptionLength()) {
        	ChatBlock.sendMessage(player, ChatColor.RED + MessageFormat.format(plugin.getLang("your.clan.description.must.be.longer.than"), sm.getClanMinDescriptionLength()));
        	return;
        }
        if (description.length() > sm.getClanMaxDescriptionLength()) {
        	ChatBlock.sendMessage(player, ChatColor.RED + MessageFormat.format(plugin.getLang("your.clan.description.cannot.be.longer.than"), sm.getClanMaxDescriptionLength()));
        	return;
        }

        clan.setDescription(description);
        ChatBlock.sendMessage(player, ChatColor.AQUA + plugin.getLang("description.changed"));
        plugin.getStorageManager().updateClanAsync(clan);
	}
}
