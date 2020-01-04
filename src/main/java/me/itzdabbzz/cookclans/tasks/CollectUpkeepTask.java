package me.itzdabbzz.cookclans.tasks;

import java.text.MessageFormat;

import me.itzdabbzz.cookclans.managers.SettingsManager;
import net.md_5.bungee.api.ChatColor;
import me.itzdabbzz.cookclans.Helper;
import me.itzdabbzz.cookclans.CookClans;
import org.bukkit.scheduler.BukkitRunnable;

/**
 *
 * @author roinujnosde
 */
public class CollectUpkeepTask extends BukkitRunnable {

    /**
     * Starts the repetitive task
     */
    public void start() {
        long delay = Helper.getDelayTo(1, 30);

        this.runTaskTimerAsynchronously( CookClans.getInstance(), delay * 20, 86400 * 20);
    }

    /**
     * (used internally)
     */
    @Override
    public void run() {
        final CookClans plugin = CookClans.getInstance();
        final SettingsManager settingsManager = plugin.getSettingsManager();
        plugin.getClanManager().getClans().forEach((clan) -> {
        	if (settingsManager.isChargeUpkeepOnlyIfMemberFeeEnabled() && !clan.isMemberFeeEnabled()) {
        		return;
        	}
            double upkeep = settingsManager.getClanUpkeep();
            if (settingsManager.isMultiplyUpkeepBySize()) {
                upkeep = upkeep * clan.getSize();
            }
            final double balance = clan.getBalance();
            if (balance >= upkeep) {
                clan.setBalance(balance - upkeep);
                clan.addBb(ChatColor.AQUA + MessageFormat.format(plugin.getLang("upkeep.collected"), upkeep), false);
            } else {
                clan.disband();
            }
        });
    }

}
