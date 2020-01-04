package me.itzdabbzz.cookclans.events;

import me.itzdabbzz.cookclans.Request;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 *
 * @author NeT32
 */
public class RequestEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final Request RequestProcess;

    public RequestEvent(Request RequestProcess) {
        this.RequestProcess = RequestProcess;
    }

    public Request getRequest() {
        return this.RequestProcess;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
