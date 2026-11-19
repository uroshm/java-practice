package practice.java.deviationRulesEngine;

import java.time.LocalDate;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class Rack {
    private final String rackId;
    private final LocalDate plannedBuildDate;
    private final LocalDate actualBuildDate;
    private final List<Material> materials;
}
