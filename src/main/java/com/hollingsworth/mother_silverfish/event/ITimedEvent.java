package com.hollingsworth.mother_silverfish.event;

/**
 * A basic timed event for the EventQueue.
 */
public interface ITimedEvent {

    void tick(boolean serverSide);

    /**
     * If this event should be removed from the queue
     */
    boolean isExpired();

}
