package me.itzdabbzz.cookclans.events;

import me.itzdabbzz.cookclans.Clan;
import me.itzdabbzz.cookclans.ClanPlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 *
 * @author NeT32
 */
public class PlayerKickedClanEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final Clan clan;
    private final ClanPlayer target;

    public PlayerKickedClanEvent(Clan clan, ClanPlayer target) {
        this.clan = clan;
        this.target = target;
    }

    public Clan getClan() {
        return this.clan;
    }

    public ClanPlayer getClanPlayer() {
        return this.target;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
