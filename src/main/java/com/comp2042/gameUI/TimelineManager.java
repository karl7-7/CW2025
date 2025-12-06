package com.comp2042.gameUI;

import com.comp2042.events.EventSource;
import com.comp2042.events.EventType;
import com.comp2042.events.MoveEvent;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.util.function.Consumer;

public class TimelineManager { //manages a timeline that triggers periodic move events
    private Timeline timeline;
    private final Consumer<MoveEvent> onTick;
    private long periodMillis = 400;

    public TimelineManager(Consumer<MoveEvent> onTick) { //constructor takes a consumer that handles move events
        this.onTick = onTick;
    }

    public void start() { //starts the timeline to trigger move events at regular intervals
        if (timeline != null) timeline.stop();
        timeline = new Timeline(new KeyFrame(Duration.millis(periodMillis), ae ->
                onTick.accept(new MoveEvent(EventType.DOWN, EventSource.THREAD))
        ));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    public void stop() { //stops the timeline if it is running
        if (timeline != null) {
            timeline.stop();
        }
    }

    public void setPeriodMillis(long periodMillis) { //sets the interval period for the timeline
        this.periodMillis = periodMillis;
        // if running, restart with new period
        if (timeline != null && timeline.getStatus() == Timeline.Status.RUNNING) {
            start();
        }
    }
}
