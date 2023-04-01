package com.jewyss.eagels.carbon.emisions.dba;

/**
 * @company kappa.computacion
 * @coder estuardo.wyss
 * @date
 */
public enum Frequency {
    DAILY("daily"),
    WEEKLY("weekly"),
    MONTHLY("monthly"),
    YEARLY("yearly");

    private String frequency;

    Frequency(String frequency){
        this.frequency=frequency;
    }

    public String getFrequency() {
        return frequency;
    }
}
