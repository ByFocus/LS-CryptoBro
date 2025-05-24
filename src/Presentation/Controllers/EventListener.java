package Presentation.Controllers;

import Business.EventType;

/**
 * The interface Event listener.
 * Defines a listener for receiving event updates.
 */
public interface EventListener {
    /**
     * Update method to be called when an event occurs.
     *
     * @param context the event context that triggered the update
     */
    void update(EventType context);
}
