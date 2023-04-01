package com.jewyss.eagels.carbon.emisions.dba;

/**
 * @company kappa.computacion
 * @coder estuardo.wyss
 * @date
 */
public enum Unit {
    GALLONS("gallons"),
    KW("kw"),
    TRIPS("trips"),
    SHEETS("sheets");

    private String unit;

    Unit(String unit){
        this.unit=unit;
    }

    public String getUnit() {
        return unit;
    }
}
