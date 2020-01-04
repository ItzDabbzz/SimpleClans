package me.itzdabbzz.cookclans.uuid;

import java.util.UUID;
import me.itzdabbzz.cookclans.ClanPlayer;
import me.itzdabbzz.cookclans.CookClans;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

/**
 *
 * @author NeT32
 */
public class UUIDMigration {

	private UUIDMigration() {}
	
    public static boolean canReturnUUID() {
        if(!CookClans.getInstance().getSettingsManager().isOnlineMode())
            return false;
        try {
            Bukkit.class.getDeclaredMethod("getPlayer", UUID.class);
            return true;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }

    public static UUID getForcedPlayerUUID(String playerDisplayName) {
        Player onlinePlayer = CookClans.getInstance().getServer().getPlayerExact(playerDisplayName);
        OfflinePlayer offlinePlayer = CookClans.getInstance().getServer().getOfflinePlayer(playerDisplayName);

        if (onlinePlayer != null) {
            return onlinePlayer.getUniqueId();
        } else {
            for (ClanPlayer cp : CookClans.getInstance().getClanManager().getAllClanPlayers()) {
                if (cp.getName().equalsIgnoreCase(playerDisplayName)) {
                    return cp.getUniqueId();
                }
            }
            try {
                return UUIDFetcher.getUUIDOf(playerDisplayName);
            } catch (Exception ex) {
                if (offlinePlayer != null) {
                    return offlinePlayer.getUniqueId();
                } else {
                    return null;
                }
            }
        }
    }

}
