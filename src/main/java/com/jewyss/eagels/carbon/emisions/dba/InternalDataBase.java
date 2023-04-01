package com.jewyss.eagels.carbon.emisions.dba;

import com.jewyss.eagels.carbon.emisions.models.AccountToControl;
import com.jewyss.eagels.carbon.emisions.models.EmissionLimits;
import com.jewyss.eagels.carbon.emisions.models.request.Emission;
import com.jewyss.eagels.carbon.emisions.models.request.EmissionRequest;
import com.jewyss.eagels.carbon.emisions.security.HashCreator;
import com.jewyss.eagels.carbon.emisions.dba.*;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.annotation.ApplicationScope;

import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.TreeMap;

/**
 * @company kappa.computacion
 * @coder estuardo.wyss
 * @date
 */
@ApplicationScope
public class InternalDataBase {
    @Autowired
    private HashCreator hashCreator;

    private TreeMap<String, Emission> emissionRecordsTreeMap = new TreeMap<>();
    private List<AccountToControl> accountToControlList = new ArrayList<>();

    private List<EmissionLimits> emissionLimitsList = new ArrayList<>();
    @Getter
    private String message="";

    public boolean addEmissionRecord(EmissionRequest emissionRequest){
        this.message="Emission record successfully added.";

        try {
            Emission emission = this.convertEmission(emissionRequest);
            this.emissionRecordsTreeMap.put(emission.getEmissionId(), emission);
        }catch (NoSuchAlgorithmException ex){
            this.message = ex.getMessage();
        }



        return false;
    }

    public boolean updateEmissionRecord(Emission emission){
        this.message="Emission successfully updated.";

        if(Objects.nonNull(emission)){
            Emission em = this.emissionRecordsTreeMap.get(emission.getEmissionId());
            if(Objects.nonNull(em)){
                this.emissionRecordsTreeMap.replace(emission.getEmissionId(), emission);
                return true;
            }else{
                this.message="Emission does not exist on data base.";
            }
        }else{
            this.message="Null request.";
        }

        return false;
    }

    public boolean deleteEmissionRecord(Emission emission){
        this.message="Emission successfully deleted.";

        if(Objects.nonNull(emission)){
            Emission em = this.emissionRecordsTreeMap.get(emission.getEmissionId());
            if(Objects.nonNull(em)){
                this.emissionRecordsTreeMap.remove(emission.getEmissionId(), em);
                return true;
            }else{
                this.message="Emission does not exist on data base.";
            }
        }else{
            this.message="Null request.";
        }

        return false;
    }

    private Emission convertEmission(EmissionRequest emissionRequest) throws NoSuchAlgorithmException {
        Emission emission = new Emission();
        emission.setEmissionId(this.hashCreator.createMD5Hash("abcdefghijklmnopqrstuvwxyz0123456789@$&^%"));
        emission.setEmission(emissionRequest.getEmission());
        emission.setEmissionDate(emissionRequest.getEmissionDate());
        emission.setAccountId(emissionRequest.getAccountId());
        emission.setDepartmentId(emissionRequest.getDepartmentId());
        emission.setUnitId(emissionRequest.getUnitId());
        return emission;
    }

    @PostConstruct
    private void loadDataBase(){
        loadAccountsDataBase();
    }

    private void loadAccountsDataBase(){
        AccountToControl account1 = new AccountToControl();
        account1.setAccountId("501025");
        account1.setAccountName("Pérdida gases refrigerantes");
        this.accountToControlList.add(account1);

        AccountToControl account2 = new AccountToControl();
        account2.setAccountId("501026");
        account2.setAccountName("Combustible vehículos");
        this.accountToControlList.add(account2);

        AccountToControl account3 = new AccountToControl();
        account3.setAccountId("501027");
        account3.setAccountName("Aceite maquinaria");
        this.accountToControlList.add(account3);

        AccountToControl account4 = new AccountToControl();
        account4.setAccountId("501028");
        account4.setAccountName("Energia eléctrica");
        this.accountToControlList.add(account4);

        AccountToControl account5 = new AccountToControl();
        account5.setAccountId("501029");
        account5.setAccountName("Aceite vehículos");
        this.accountToControlList.add(account5);

        //*********************************************

        AccountToControl accountL1 = new AccountToControl();
        accountL1.setAccountId("601026");
        accountL1.setAccountName("Combustible vehículos");
        this.accountToControlList.add(accountL1);

        AccountToControl accountL2 = new AccountToControl();
        accountL2.setAccountId("601029");
        accountL2.setAccountName("Aceite vehículos");
        this.accountToControlList.add(accountL2);

        AccountToControl accountL3 = new AccountToControl();
        accountL3.setAccountId("601050");
        accountL3.setAccountName("Viajes");
        this.accountToControlList.add(accountL3);

        //*********************************************
        AccountToControl accountA1 = new AccountToControl();
        accountA1.setAccountId("701026");
        accountA1.setAccountName("Combustible vehículos");
        this.accountToControlList.add(accountA1);

        AccountToControl accountA2 = new AccountToControl();
        accountA2.setAccountId("701029");
        accountA2.setAccountName("Aceite vehículos");
        this.accountToControlList.add(accountA2);

        AccountToControl accountA3 = new AccountToControl();
        accountA3.setAccountId("701050");
        accountA3.setAccountName("Viajes");
        this.accountToControlList.add(accountA3);

        AccountToControl accountA4 = new AccountToControl();
        accountA4.setAccountId("701050");
        accountA4.setAccountName("Energía eléctrica");
        this.accountToControlList.add(accountA4);

        AccountToControl accountA5 = new AccountToControl();
        accountA5.setAccountId("701050");
        accountA5.setAccountName("Papel");
        this.accountToControlList.add(accountA5);
    }

    /**
     * This db is going to help to build the right response
     * for the dashboard about the consumption of any resource
     * thinking yearly.
     * I am guessing values
     * @throws NoSuchAlgorithmException
     */
    private void loadAnnualLimitsDataBase() throws NoSuchAlgorithmException {
        EmissionLimits emP1 = new EmissionLimits();
        emP1.setLimitId(this.hashCreator.createMD5Hash("abcdefghijklmnopqrstuvwxyz0123456789@$&^%"));
        emP1.setAccountId("701026");
        emP1.setDepartmentId(Department.ADMIN.getDepartment());
        emP1.setUnitId(Unit.GALLONS.getUnit());
        emP1.setLimit(new BigDecimal("3450"));

        EmissionLimits emP2 = new EmissionLimits();
        emP2.setLimitId(this.hashCreator.createMD5Hash("abcdefghijklmnopqrstuvwxyz0123456789@$&^%"));

        EmissionLimits emP3 = new EmissionLimits();
        emP3.setLimitId(this.hashCreator.createMD5Hash("abcdefghijklmnopqrstuvwxyz0123456789@$&^%"));

        EmissionLimits emP4 = new EmissionLimits();
        emP4.setLimitId(this.hashCreator.createMD5Hash("abcdefghijklmnopqrstuvwxyz0123456789@$&^%"));

        EmissionLimits emP5 = new EmissionLimits();
        emP5.setLimitId(this.hashCreator.createMD5Hash("abcdefghijklmnopqrstuvwxyz0123456789@$&^%"));

        EmissionLimits emP6 = new EmissionLimits();
        emP6.setLimitId(this.hashCreator.createMD5Hash("abcdefghijklmnopqrstuvwxyz0123456789@$&^%"));

        EmissionLimits emP7 = new EmissionLimits();
        emP7.setLimitId(this.hashCreator.createMD5Hash("abcdefghijklmnopqrstuvwxyz0123456789@$&^%"));

        EmissionLimits emP8 = new EmissionLimits();
        emP8.setLimitId(this.hashCreator.createMD5Hash("abcdefghijklmnopqrstuvwxyz0123456789@$&^%"));

        EmissionLimits emP9 = new EmissionLimits();
        emP9.setLimitId(this.hashCreator.createMD5Hash("abcdefghijklmnopqrstuvwxyz0123456789@$&^%"));

        EmissionLimits emP10 = new EmissionLimits();
        emP10.setLimitId(this.hashCreator.createMD5Hash("abcdefghijklmnopqrstuvwxyz0123456789@$&^%"));
    }

}
