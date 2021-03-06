package me.itzdabbzz.cookclans.conversation;

import java.text.MessageFormat;
import java.util.Arrays;
import me.itzdabbzz.cookclans.Clan;
import me.itzdabbzz.cookclans.ClanPlayer;
import me.itzdabbzz.cookclans.Helper;
import me.itzdabbzz.cookclans.CookClans;
import me.itzdabbzz.cookclans.managers.ClanManager;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;

/**
 *
 * @author roinujnosde
 */
public class ResignPrompt extends StringPrompt {

    @Override
    public Prompt acceptInput(ConversationContext cc, String input) {
        final CookClans plugin = ( CookClans ) cc.getPlugin();

        String yes = plugin.getLang("resign.yes");
        Player player = (Player) cc.getForWhom();
        ClanManager cm = plugin.getClanManager();
        ClanPlayer cp = cm.getClanPlayer(player);
        Clan clan = cp.getClan();

        if (yes.equalsIgnoreCase(input)) {
            if (!clan.isLeader(player) || clan.getLeaders().size() > 1) {
                clan.addBb(player.getName(), ChatColor.AQUA + MessageFormat.format(plugin.getLang("0.has.resigned"), Helper.capitalize(player.getName())));
                cp.addResignTime(clan.getTag());
                clan.removePlayerFromClan(player.getUniqueId());
                
                return new MessagePromptImpl(ChatColor.AQUA + plugin.getLang("resign.success"));
            } else if (clan.isLeader(player) && clan.getLeaders().size() == 1) {
                plugin.getClanManager().serverAnnounce(ChatColor.AQUA + MessageFormat.format(plugin.getLang("clan.has.been.disbanded"), clan.getName()));
                clan.disband();
            } else {
                return new MessagePromptImpl(ChatColor.RED + plugin.getLang("last.leader.cannot.resign.you.must.appoint.another.leader.or.disband.the.clan"));
            }
        }

        return END_OF_CONVERSATION;
    }

    @Override
    public String getPromptText(ConversationContext cc) {
        final CookClans plugin = ( CookClans ) cc.getPlugin();

        return ChatColor.RED + MessageFormat.format(
                plugin.getLang("resign.confirmation"), Arrays.asList(
                    plugin.getLang("resign.yes"), plugin.getLang("resign.no")));
    }

}
