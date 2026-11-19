package practice.java.deviationRulesEngine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import practice.java.deviationRulesEngine.alerts.Alert;
import practice.java.deviationRulesEngine.enums.Severity;
import practice.java.deviationRulesEngine.rules.Rule;

public class RulesEngine {
    List<Alert> alerts = new ArrayList<>();
    List<Rule> rules = new ArrayList<>();
    List<Rack> racks = new ArrayList<>();
    Map<Severity, Integer> severityByRank = new HashMap<>();

    public RulesEngine() {
        instantiateSeverityRanks();
    }

    private void instantiateSeverityRanks() {
        severityByRank.put(Severity.LOW, 0);
        severityByRank.put(Severity.MEDIUM, 3);
        severityByRank.put(Severity.HIGH, 5);
        severityByRank.put(Severity.CRITICAL, 7);
    }

    void addRule(Rule rule) {
        rules.add(rule);
    }

    void evaluateRack(Rack rack) throws Exception {
        Objects.requireNonNull(rack);
        if (rules.isEmpty()) return;

        for (var rule : rules) {
            var result = rule.run(rack);
            result.ifPresent(alerts::add);
        }
    }

    void evaluateAll() {}

    List<Alert> getRankedAlerts() {
        if (alerts.size() == 0) return Collections.emptyList();
        return alerts.stream()
                .sorted(
                        (a, b) ->
                                Integer.compare(
                                        severityByRank.get(b.getSeverity()),
                                        severityByRank.get(a.getSeverity())))
                .toList();
    }
}
