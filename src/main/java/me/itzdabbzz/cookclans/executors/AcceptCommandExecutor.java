package me.itzdabbzz.cookclans.executors;

import me.itzdabbzz.cookclans.*;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.MessageFormat;

public class AcceptCommandExecutor implements CommandExecutor {
    CookClans plugin;

    public AcceptCommandExecutor() {
        plugin = CookClans.getInstance();
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player = (Player) commandSender;

        if (plugin.getSettingsManager().isBanned(player.getUniqueId())) {
            ChatBlock.sendMessage(player, ChatColor.RED + plugin.getLang("banned"));
            return false;
        }

        ClanPlayer cp = plugin.getClanManager().getClanPlayer(player);

        if (cp != null) {
            Clan clan = cp.getClan();

            if (!clan.isLeader(player)) {
                ChatBlock.sendMessage(player, ChatColor.RED + plugin.getLang("no.leader.permissions"));
                return false;
            }
            if (!plugin.getRequestManager().hasRequest(clan.getTag())) {
                ChatBlock.sendMessage(player, ChatColor.RED + plugin.getLang("nothing.to.accept"));
                return false;
            }
            if (cp.getVote() != null) {
                ChatBlock.sendMessage(player, ChatColor.RED + plugin.getLang("you.have.already.voted"));
                return false;
            }
            plugin.getRequestManager().accept(cp);
            clan.leaderAnnounce(ChatColor.GREEN + MessageFormat.format(plugin.getLang("voted.to.accept"), Helper.capitalize(player.getName())));
        } else {
            if (!plugin.getRequestManager().hasRequest(player.getName().toLowerCase())) {
                ChatBlock.sendMessage(player, ChatColor.RED + plugin.getLang("nothing.to.accept"));
                return false;
            }
            cp = plugin.getClanManager().getCreateClanPlayer(player.getUniqueId());
            cp.setName(player.getName());
            plugin.getRequestManager().accept(cp);
        }

        return true;
    }
}
