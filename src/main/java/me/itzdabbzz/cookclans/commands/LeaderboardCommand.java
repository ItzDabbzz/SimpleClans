package me.itzdabbzz.cookclans.commands;

import me.itzdabbzz.cookclans.ChatBlock;
import me.itzdabbzz.cookclans.ClanPlayer;
import me.itzdabbzz.cookclans.Helper;
import me.itzdabbzz.cookclans.CookClans;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.List;

/**
 * @author phaed
 */
public class LeaderboardCommand {
    public LeaderboardCommand() {
    }

    /**
     * Execute the command
     *
     * @param player
     * @param arg
     */
    public void execute(Player player, String[] arg) {
        CookClans plugin = CookClans.getInstance();
        String headColor = plugin.getSettingsManager().getPageHeadingsColor();
        String subColor = plugin.getSettingsManager().getPageSubTitleColor();
        NumberFormat formatter = new DecimalFormat("#.#");

        if (arg.length != 0) {
            ChatBlock.sendMessage(player, ChatColor.RED + MessageFormat.format(plugin.getLang("usage.0.leaderboard"), plugin.getSettingsManager().getCommandClan()));
            return;
        }
        if (!plugin.getPermissionsManager().has(player, "simpleclans.anyone.leaderboard")) {
            ChatBlock.sendMessage(player, ChatColor.RED + plugin.getLang("insufficient.permissions"));
            return;
        }

        List<ClanPlayer> clanPlayers = plugin.getClanManager().getAllClanPlayers();
        plugin.getClanManager().sortClanPlayersByKDR(clanPlayers);

        ChatBlock chatBlock = new ChatBlock();

        ChatBlock.sendBlank(player);
        ChatBlock.saySingle(player, plugin.getSettingsManager().getServerName() + subColor + " " + plugin.getLang("leaderboard.command") + " " + headColor + Helper.generatePageSeparator(plugin.getSettingsManager().getPageSep()));
        ChatBlock.sendBlank(player);
        ChatBlock.sendMessage(player, headColor + MessageFormat.format(plugin.getLang("total.clan.players.0"), subColor + clanPlayers.size()));
        ChatBlock.sendBlank(player);

        chatBlock.setAlignment("c", "l", "c", "c", "c", "c");
        chatBlock.addRow("  " + headColor + plugin.getLang("rank"), plugin.getLang("player"), plugin.getLang("kdr"), plugin.getLang("clan"), plugin.getLang("seen"));

        int rank = 1;

        for (ClanPlayer cp : clanPlayers) {
            Player p = cp.toPlayer();

            boolean isOnline = false;

            if (p != null) {
                isOnline = true;
            }

            String name = (cp.isLeader() ? plugin.getSettingsManager().getPageLeaderColor() : (cp.isTrusted() ? plugin.getSettingsManager().getPageTrustedColor() : plugin.getSettingsManager().getPageUnTrustedColor())) + cp.getName();
            String lastSeen = isOnline ? ChatColor.GREEN + plugin.getLang("online") : ChatColor.WHITE + cp.getLastSeenDaysString();

            String clanTag = ChatColor.WHITE + plugin.getLang("free.agent");

            if (cp.getClan() != null) {
                clanTag = cp.getClan().getColorTag();
            }

            chatBlock.addRow("  " + rank, name, ChatColor.YELLOW + "" + formatter.format(cp.getKDR()), ChatColor.WHITE + clanTag, lastSeen);
            rank++;
        }

        boolean more = chatBlock.sendBlock(player, plugin.getSettingsManager().getPageSize());

        if (more) {
            plugin.getStorageManager().addChatBlock(player, chatBlock);
            ChatBlock.sendBlank(player);
            ChatBlock.sendMessage(player, headColor + MessageFormat.format(plugin.getLang("view.next.page"), plugin.getSettingsManager().getCommandMore()));
        }

        ChatBlock.sendBlank(player);
    }
}
