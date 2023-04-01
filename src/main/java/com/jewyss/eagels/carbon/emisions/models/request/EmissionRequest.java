package com.jewyss.eagels.carbon.emisions.models.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @project Eagle hub
 * @coder estuardo.wyss
 * @date 04/01/2023
 */
@Getter
@Setter
public class EmissionRequest {
    int month;
    int year;
    String accountId;
    String departmentId;
    String unitId;
    BigDecimal emission;
}
