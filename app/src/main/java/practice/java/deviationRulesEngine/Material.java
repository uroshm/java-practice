package practice.java.deviationRulesEngine;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public abstract class Material {
    private final String materialId;
    private final Integer requiredQuantity;
    private final Integer availableQuantity;
}
