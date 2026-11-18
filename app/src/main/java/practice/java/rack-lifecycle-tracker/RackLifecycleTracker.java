import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RackLifecycleTracker implements Observable {
    public RackLifecycleTracker() {
        initTransitionValidator();
    }

    private Map<String, Rack> racks = new ConcurrentHashMap<>();
    private HistoryService historyService = new HistoryService();
    private List<Observer> observers = new ArrayList<>();

    private final Map<State, List<State>> validTransitions = new HashMap<>(); // source,destinatoion

    private void initTransitionValidator() {
        validTransitions.put(State.PLANNED, List.of(State.MATERIAL_ORDERED, State.ON_HOLD));
        validTransitions.put(State.MATERIAL_ORDERED, List.of(State.BUILT, State.ON_HOLD));
        validTransitions.put(State.BUILT, List.of(State.SHIPPED, State.ON_HOLD));
        validTransitions.put(State.SHIPPED, List.of(State.LANDED, State.ON_HOLD));
        validTransitions.put(State.LANDED, List.of(State.LIVE, State.ON_HOLD));
        validTransitions.put(State.LIVE, Collections.emptyList());
        validTransitions.put(
                State.ON_HOLD,
                List.of(
                        State.PLANNED,
                        State.MATERIAL_ORDERED,
                        State.BUILT,
                        State.SHIPPED,
                        State.LANDED,
                        State.LIVE));
    }

    public boolean addRack(String newRackId) {
        Objects.requireNonNull(newRackId, "Rack Id may not be null");
        Rack previous = racks.putIfAbsent(newRackId, new Rack(newRackId));
        return previous == null;
    }

    public void transition(String rackId, State source, State destination) {
        var currentRack = racks.get(rackId);
        synchronized (currentRack) {
            validateTransitionInput(rackId, source, destination);

            if (!currentRack.getCurrentState().equals(source))
                throw new RuntimeException(
                        "Rack "
                                + currentRack.getRackId()
                                + " does not have a source state of "
                                + source);

            if (validTransitions.get(source).contains(destination)) {
                if (currentRack.getCurrentState().equals(State.ON_HOLD)
                        && !destination.equals(currentRack.getPrevState())) {
                    throw new RuntimeException(
                            "For on hold racks, we must return to previous state of "
                                    + currentRack.getPrevState());
                }
                currentRack.setCurrentState(destination);

                if (destination.equals(State.ON_HOLD)) currentRack.setPrevState(source);
                var newTransition =
                        new Transition(
                                currentRack.getRackId(), source, destination, LocalDateTime.now());
                historyService.addTransition(newTransition);
                notifyListeners(newTransition);

            } else {
                throw new RuntimeException(
                        "The transition you have requested is in valid, please try again.");
            }
        }
    }

    public void addObserver(Observer observer) {
        if (!observers.contains(observer)) observers.add(observer);
        else throw new RuntimeException("This observer already exists");
    }

    public State getRackStateById(String id) {
        if (racks.containsKey(id)) {
            return racks.get(id).getCurrentState();
        } else {
            return null;
        }
    }

    public List<Transition> getRackHistoryById(String id) {
        if (racks.containsKey(id)) {
            return historyService.getTransitionById(id);
        } else {
            return null;
        }
    }

    private void validateTransitionInput(String rackId, State source, State destination) {
        if (!racks.containsKey(rackId))
            throw new RuntimeException("Could not find rack with id " + rackId);
        if (!validTransitions.keySet().contains(source))
            throw new RuntimeException("Could not find source state.");
        if (!validTransitions.keySet().contains(destination))
            throw new RuntimeException("Could not find destination state.");
    }

    @Override
    public void notifyListeners(Transition transition) {
        for (var observer : observers) {
            try {
                observer.intake(transition);
            } catch (Exception ex) {
                log.warn("Observer " + observer + " had a problem ingesting this message.");
            }
        }
    }
}

@Slf4j
class HistoryService {
    private final Map<String, List<Transition>> history = new ConcurrentHashMap<>();

    public void addTransition(Transition transition) {
        history.computeIfAbsent(
                        transition.getRackId(),
                        k -> Collections.synchronizedList(new ArrayList<>()))
                .add(transition);
    }

    public List<Transition> getTransitionById(String id) {
        var histories = history.get(id);
        if (histories == null) {
            log.warn("Tried finding history for id " + id + " but could not find.");
            return Collections.emptyList();
        }
        synchronized (histories) {
            return List.copyOf(histories);
        }
    }
}

@Getter
@RequiredArgsConstructor
class Transition {
    private final String rackId;
    private final State source;
    private final State destination;
    private final LocalDateTime eventTime;
}

@Getter
@Setter
class Rack {
    String rackId;
    State currentState;
    State prevState;

    public Rack(String rackId) {
        this.rackId = rackId;
        this.currentState = State.PLANNED;
        this.prevState = State.PLANNED;
    }
}

enum State {
    PLANNED,
    MATERIAL_ORDERED,
    BUILT,
    SHIPPED,
    LANDED,
    LIVE,
    ON_HOLD;
}

interface Observer {
    public void intake(Transition transition);
}

interface Observable {
    public void notifyListeners(Transition transition);
}
