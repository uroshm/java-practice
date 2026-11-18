package practice.java.rackMilestoneEventProcessor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EventProcessor {
    Map<String, RackEvent> eventByRackId = new HashMap<>();
    Set<String> eventIds = new HashSet<>();

    void processEvent(RackEvent event) {
        Objects.requireNonNull(event, "Event must not be null!");
        Objects.requireNonNull(event.getEventId());
        Objects.requireNonNull(event.getRackId());
        Objects.requireNonNull(event.getVersion());
        Objects.requireNonNull(event.getStatus());
        Objects.requireNonNull(event.getTimestamp());

        if (eventIds.contains(event.getEventId())) {
            log.warn("This event id already exists: " + event.getEventId());
            return;
        }

        eventIds.add(event.getEventId());
        var currentEvent = eventByRackId.get(event.getRackId());

        if (currentEvent == null) {
            eventByRackId.put(
                    event.getRackId(),
                    new RackEvent(
                            event.getEventId(),
                            event.getRackId(),
                            event.getVersion(),
                            event.getStatus(),
                            event.getTimestamp()));
        } else if (event.getVersion() > currentEvent.getVersion()) {
            eventByRackId.put(event.getRackId(), event);
        } else {
            log.warn("Event with id " + event.getEventId() + " is older than latest stored.");
        }
    }

    State getLatestState(String rackId) {
        var event = eventByRackId.getOrDefault(rackId, null);
        if (event != null) return event.getStatus();
        else return State.UNKNOWN;
    }

    enum State {
        UNKNOWN,
        PLANNED,
        MATERIAL_ORDERED,
        BUILT,
        SHIPPED,
        LANDED,
        LIVE;
    }
}
