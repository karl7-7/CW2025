package com.comp2042;

public final class MoveEvent { // This is an immutable data class that represents an action or command triggered in the game
    private final EventType eventType; //describes the type of action
    private final EventSource eventSource; //describes where the event came from

    public MoveEvent(EventType eventType, EventSource eventSource) {
        this.eventType = eventType; //constructor creates a new MoveEvent containing what happened and who triggered it
        this.eventSource = eventSource;
    }

    public EventType getEventType() {
        return eventType; //returns the type of action that occured
    }

    public EventSource getEventSource() {
        return eventSource; //returns the source that triggered this event
    }
}
