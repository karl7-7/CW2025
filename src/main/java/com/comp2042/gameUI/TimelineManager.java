package com.comp2042.gameUI;

import com.comp2042.events.EventSource;
import com.comp2042.events.EventType;
import com.comp2042.events.MoveEvent;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.util.function.Consumer;

/**
 * Manages a JavaFX {@link Timeline} that fires periodic DOWN {@link MoveEvent}s.
 * The period is configurable via {@link #setPeriodMillis(long)} and the timeline can be
 * started and stopped.
 */

public class TimelineManager { //manages a timeline that triggers periodic move events
    private Timeline timeline;
    private final Consumer<MoveEvent> onTick;
    private long periodMillis = 400;

    /**
     * Create a TimelineManager that will call the given consumer on each tick.
     *
     * @param onTick consumer that receives generated {@link MoveEvent} instances
     */

    public TimelineManager(Consumer<MoveEvent> onTick) { //constructor takes a consumer that handles move events
        this.onTick = onTick;
    }


    /**
     * Start the timeline. If already running it will be restarted with the current period.
     */

    public void start() { //starts the timeline to trigger move events at regular intervals
        if (timeline != null) timeline.stop();
        timeline = new Timeline(new KeyFrame(Duration.millis(periodMillis), ae ->
                onTick.accept(new MoveEvent(EventType.DOWN, EventSource.THREAD))
        ));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    /**
     * Stop the timeline if running.
     */

    public void stop() { //stops the timeline if it is running
        if (timeline != null) {
            timeline.stop();
        }
    }

    /**
     * Set the timeline tick period in milliseconds. If the timeline is running it will be restarted
     * with the new period.
     *
     * @param periodMillis new period in ms
     */

    public void setPeriodMillis(long periodMillis) { //sets the interval period for the timeline
        this.periodMillis = periodMillis;
        // if running, restart with new period
        if (timeline != null && timeline.getStatus() == Timeline.Status.RUNNING) {
            start();
        }
    }
}
