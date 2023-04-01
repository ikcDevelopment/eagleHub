package com.jewyss.eagels.carbon.emisions.models.responses;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseObject {
    private boolean success;
    private String message;
    private String status;
    private int amount;
}
