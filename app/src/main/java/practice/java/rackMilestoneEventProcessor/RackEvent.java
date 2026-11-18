package practice.java.rackMilestoneEventProcessor;

import java.time.LocalDateTime;
import lombok.Getter;
import practice.java.rackMilestoneEventProcessor.EventProcessor.State;

@Getter
class RackEvent {
    String eventId;
    String rackId;
    Long version;
    State status;
    LocalDateTime timestamp;

    public RackEvent(
            String eventId, String rackId, Long version, State status, LocalDateTime timestamp) {
        this.eventId = eventId;
        this.rackId = rackId;
        this.version = version;
        this.status = status;
        this.timestamp = timestamp;
    }

    public RackEvent(RackEvent event) {
        this.eventId = event.getEventId();
        this.rackId = event.getRackId();
        this.version = event.getVersion();
        this.timestamp = LocalDateTime.now();
        this.status = State.UNKNOWN;
    }
}
