package practice.java.rackMilestoneEventProcessor;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import practice.java.rackMilestoneEventProcessor.EventProcessor.State;

@SpringBootTest
public class EventProcessorTest {

    @Test
    void createFirstEvent() {
        RackEvent event = new RackEvent("E1", "rack-123", 5L, State.LANDED, LocalDateTime.now());
        EventProcessor eventProcessor = new EventProcessor();
        eventProcessor.processEvent(event);
    }
}
