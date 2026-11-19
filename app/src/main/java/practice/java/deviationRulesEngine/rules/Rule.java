package practice.java.deviationRulesEngine.rules;

import java.util.Optional;
import practice.java.deviationRulesEngine.Rack;
import practice.java.deviationRulesEngine.alerts.Alert;
import practice.java.deviationRulesEngine.enums.Severity;

public interface Rule {
    void setRackId(String rackId);

    void setRuleName(String ruleName);

    Optional<Alert> run(Rack rack) throws Exception;

    Alert issueAlert(String rackId, String ruleName, String message, Severity severity);
}
