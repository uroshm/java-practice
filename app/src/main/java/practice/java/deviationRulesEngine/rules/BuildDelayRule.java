package practice.java.deviationRulesEngine.rules;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import lombok.Setter;
import practice.java.deviationRulesEngine.Rack;
import practice.java.deviationRulesEngine.alerts.Alert;
import practice.java.deviationRulesEngine.enums.Severity;

public class BuildDelayRule implements Rule {

    String ruleName;
    String rackId;

    @Setter LocalDate evaluationDate;

    @Override
    public Optional<Alert> run(Rack rack) throws Exception {
        var plannedBuildDate = rack.getPlannedBuildDate();
        var actualBuildDate = rack.getActualBuildDate();
        if (actualBuildDate != null) return Optional.empty();
        if (plannedBuildDate == null) throw new Exception("Need a planned build date!");
        if (evaluationDate == null) throw new Exception("No date to evaluate against.");

        var delta = ChronoUnit.DAYS.between(plannedBuildDate, evaluationDate);

        if (delta <= 1) return Optional.empty();
        else if (delta >= 2 && delta <= 6)
            return Optional.of(
                    issueAlert(
                            rack.getRackId(),
                            this.ruleName,
                            "You need to build this soon, delta of " + delta,
                            Severity.MEDIUM));
        else
            return Optional.of(
                    issueAlert(
                            rack.getRackId(),
                            this.ruleName,
                            "This is critical, report to higher ups! Delta of " + delta,
                            Severity.HIGH));
    }

    @Override
    public Alert issueAlert(String rackId, String ruleName, String message, Severity severity) {
        return new Alert(rackId, ruleName, severity, message);
    }

    @Override
    public void setRackId(String rackId) {
        this.rackId = rackId;
    }

    @Override
    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }
}
