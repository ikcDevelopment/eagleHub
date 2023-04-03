package com.jewyss.eagels.carbon.emisions.models;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @company kappa.computacion
 * @coder estuardo.wyss
 * @date
 */
@Getter
@Setter
public class AverageUseOfSegment {
    String unitUsed;
    Map<Integer, BigDecimal> segment;
}
