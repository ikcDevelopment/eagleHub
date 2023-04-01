package com.jewyss.eagels.carbon.emisions.models;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @project Eagle hub
 * @coder estuardo.wyss
 * @date 04/01/2023
 */
@Getter
@Setter
public class EmissionLimits {
    String limitId;
    String accountId;
    String departmentId;
    String unitId;
    BigDecimal limit;
}
