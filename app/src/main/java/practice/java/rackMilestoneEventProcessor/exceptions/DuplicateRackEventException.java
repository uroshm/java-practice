package practice.java.rackMilestoneEventProcessor.exceptions;

public class DuplicateRackEventException extends RuntimeException {
    public DuplicateRackEventException(String message) {
        super(message);
    }
}
