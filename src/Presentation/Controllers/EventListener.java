package Presentation.Controllers;

import Business.EventType;

/**
 * The interface Event listener.
 */
public interface EventListener {
    /**
     * Update.
     *
     * @param context the context
     */
    void update(EventType context);
}
