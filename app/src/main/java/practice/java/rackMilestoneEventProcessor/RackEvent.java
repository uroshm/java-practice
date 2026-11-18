package practice.java.rackMilestoneEventProcessor;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import practice.java.rackMilestoneEventProcessor.EventProcessor.State;

@Getter
@RequiredArgsConstructor
class RackEvent {
    private final String eventId;
    private final String rackId;
    private final Long version;
    private final State status;
    private final LocalDateTime timestamp;

    public RackEvent(RackEvent event) {
        this.eventId = event.getEventId();
        this.rackId = event.getRackId();
        this.version = event.getVersion();
        this.timestamp = LocalDateTime.now();
        this.status = State.UNKNOWN;
    }
}
