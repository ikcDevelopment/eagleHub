package com.jewyss.eagels.carbon.emisions.dba;

/**
 * @company kappa.computacion
 * @coder estuardo.wyss
 * @date
 */
public enum Department {
    ADMIN("Administration"),
    LOGISTIC("Logistic"),
    FACTORY("Factory");

    private String depto;

    Department(String depto) {
        this.depto=depto;
    }

    public String getDepartment(){
        return depto;
    }
}
