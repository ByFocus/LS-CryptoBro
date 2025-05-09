package Presentation.Controllers;

import Business.EventType;

public interface EventListener {
    void update(EventType context);
}
