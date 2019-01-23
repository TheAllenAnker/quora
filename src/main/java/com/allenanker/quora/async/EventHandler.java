package com.allenanker.quora.async;

import java.util.List;

public interface EventHandler {
    void doHandle(EventModel eventModel);

    List<EventType> getSupportedEvents();
}
