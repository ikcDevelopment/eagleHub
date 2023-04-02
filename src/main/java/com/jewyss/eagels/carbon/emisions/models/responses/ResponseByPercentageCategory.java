package com.jewyss.eagels.carbon.emisions.models.responses;

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
public class ResponseByPercentageCategory extends  ResponseObject{
    Map<Integer, BigDecimal> categories;
}
