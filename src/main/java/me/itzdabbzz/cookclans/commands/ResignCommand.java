package me.itzdabbzz.cookclans.commands;

import me.itzdabbzz.cookclans.conversation.ResignPrompt;
import me.itzdabbzz.cookclans.ChatBlock;
import me.itzdabbzz.cookclans.ClanPlayer;
import me.itzdabbzz.cookclans.CookClans;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import org.bukkit.conversations.ConversationFactory;

/**
 * @author phaed
 */
public class ResignCommand {
    public ResignCommand() {
    }

    /**
     * Execute the command
     *
     * @param player
     * @param arg
     */
    public void execute(Player player, String[] arg) {
        CookClans plugin = CookClans.getInstance();

        if (!plugin.getPermissionsManager().has(player, "simpleclans.member.resign")) {
            ChatBlock.sendMessage(player, ChatColor.RED + plugin.getLang("insufficient.permissions"));
            return;
        }
        ClanPlayer cp = plugin.getClanManager().getClanPlayer(player);

        if (cp == null) {
            ChatBlock.sendMessage(player, ChatColor.RED + plugin.getLang("not.a.member.of.any.clan"));
            return;
        }
        
        new ConversationFactory(plugin)
                .withFirstPrompt(new ResignPrompt())
                .withLocalEcho(true)
                .withTimeout(10)
                .buildConversation(player).begin();
    }
}
