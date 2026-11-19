package practice.java.deviationRulesEngine.rules;

import java.util.Objects;
import java.util.Optional;
import practice.java.deviationRulesEngine.Rack;
import practice.java.deviationRulesEngine.alerts.Alert;
import practice.java.deviationRulesEngine.enums.Severity;

public class MaterialShortageRule implements Rule {
    String ruleName;
    String rackId;

    @Override
    public Optional<Alert> run(Rack rack) throws Exception {
        Objects.requireNonNull(rack.getMaterials());
        if (rack.getMaterials().isEmpty()) return Optional.empty();

        Optional<Alert> highestAlert = Optional.empty();
        for (var material : rack.getMaterials()) {
            Objects.requireNonNull(material.getAvailableQuantity());
            Objects.requireNonNull(material.getRequiredQuantity());
            int delta =
                    Math.max(0, material.getRequiredQuantity() - material.getAvailableQuantity());
            double shortagePct = (double) delta / (double) material.getRequiredQuantity();

            if (shortagePct > 0 && shortagePct <= 0.5) {
                if (highestAlert.isEmpty()) {
                    highestAlert =
                            Optional.of(
                                    issueAlert(
                                            rack.getRackId(),
                                            this.ruleName,
                                            "You are < 50% but over 0 percent short on this material "
                                                    + shortagePct,
                                            Severity.MEDIUM));
                }

            } else if (shortagePct > 0 && shortagePct > 0.5) {
                highestAlert =
                        Optional.of(
                                issueAlert(
                                        rack.getRackId(),
                                        this.ruleName,
                                        "You are > 50% short on this material " + shortagePct,
                                        Severity.HIGH));
            } else {
                continue;
            }
        }
        return highestAlert;
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
