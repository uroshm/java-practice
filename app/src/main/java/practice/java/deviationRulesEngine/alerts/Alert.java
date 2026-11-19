package practice.java.deviationRulesEngine.alerts;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import practice.java.deviationRulesEngine.enums.Severity;

@RequiredArgsConstructor
@Getter
public class Alert {
    private final String rackId;
    private final String ruleName;
    private final Severity severity;
    private final String message;

    @Override
    public String toString() {
        return new String(
                "Rack id: "
                        + rackId
                        + " ;; Rule name: "
                        + ruleName
                        + " ;; Severity: "
                        + severity
                        + " ;; Message: "
                        + message);
    }
}
