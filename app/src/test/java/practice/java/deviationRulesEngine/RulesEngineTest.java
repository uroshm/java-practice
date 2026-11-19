package practice.java.deviationRulesEngine;

import java.time.LocalDate;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import practice.java.deviationRulesEngine.enums.Status;
import practice.java.deviationRulesEngine.rules.BuildDelayRule;
import practice.java.deviationRulesEngine.rules.MaterialShortageRule;

@SpringBootTest
@Slf4j
public class RulesEngineTest {

    @Test
    void rack101Ac() throws Exception {
        var powerSupplies = new Material("Power supplies", 8, 5);
        var networkSwitches = new Material("Network switches", 2, 2);
        var coolingUnits = new Material("Cooling units", 4, 4);

        Rack rack =
                new Rack(
                        "RACK-101",
                        LocalDate.of(2026, 11, 10),
                        null,
                        Status.MATERIAL_ORDERED,
                        List.of(powerSupplies, networkSwitches, coolingUnits));

        var buildDelayRule = new BuildDelayRule();
        buildDelayRule.setRackId("RACK-101");
        buildDelayRule.setRuleName("Build-Delay-Rule");
        buildDelayRule.setEvaluationDate(LocalDate.of(2026, 11, 18));

        var materialShortageRule = new MaterialShortageRule();
        materialShortageRule.setRuleName("Material-Shortage-Rule");
        materialShortageRule.setRackId("RACK-101");

        var engine = new RulesEngine(List.of(rack));
        engine.addRule(buildDelayRule);
        engine.addRule(materialShortageRule);

        engine.evaluateAll();

        var alerts = engine.getRankedAlerts();
        for (var alert : alerts) {
            log.info("Alert: " + alert.toString());
        }
    }
}
