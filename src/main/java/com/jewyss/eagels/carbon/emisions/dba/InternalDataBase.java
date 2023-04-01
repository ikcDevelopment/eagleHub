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
        emission.setMonth(emissionRequest.getMonth());
        emission.setYear(emission.getYear());
        emission.setAccountId(emissionRequest.getAccountId());
        emission.setDepartmentId(emissionRequest.getDepartmentId());
        emission.setUnitId(emissionRequest.getUnitId());
        return emission;
    }

    @PostConstruct
    private void loadDataBase(){
        loadAccountsDataBase();
        try {
            loadAnnualLimitsDataBase();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
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
        accountA3.setAccountId("701030");
        accountA3.setAccountName("Viajes");
        this.accountToControlList.add(accountA3);

        AccountToControl accountA4 = new AccountToControl();
        accountA4.setAccountId("701028");
        accountA4.setAccountName("Energía eléctrica");
        this.accountToControlList.add(accountA4);

        AccountToControl accountA5 = new AccountToControl();
        accountA5.setAccountId("701051");
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
        this.emissionLimitsList.add(emP1);

        EmissionLimits emP2 = new EmissionLimits();
        emP2.setLimitId(this.hashCreator.createMD5Hash("abcdefghijklmnopqrstuvwxyz0123456789@$&^%"));
        emP2.setAccountId("701050");
        emP2.setDepartmentId(Department.ADMIN.getDepartment());
        emP2.setUnitId(Unit.KW.getUnit());
        emP2.setLimit(new BigDecimal("1380"));
        this.emissionLimitsList.add(emP2);

        EmissionLimits emP3 = new EmissionLimits();
        emP3.setLimitId(this.hashCreator.createMD5Hash("abcdefghijklmnopqrstuvwxyz0123456789@$&^%"));
        emP3.setAccountId("701030");
        emP3.setDepartmentId(Department.ADMIN.getDepartment());
        emP3.setUnitId(Unit.TRIPS.getUnit());
        emP3.setLimit(new BigDecimal("36"));
        this.emissionLimitsList.add(emP3);

        EmissionLimits emP4 = new EmissionLimits();
        emP4.setLimitId(this.hashCreator.createMD5Hash("abcdefghijklmnopqrstuvwxyz0123456789@$&^%"));
        emP4.setAccountId("701051");
        emP4.setDepartmentId(Department.ADMIN.getDepartment());
        emP4.setUnitId(Unit.SHEETS.getUnit());
        emP4.setLimit(new BigDecimal("27600"));
        this.emissionLimitsList.add(emP4);

        EmissionLimits emP5 = new EmissionLimits();
        emP5.setLimitId(this.hashCreator.createMD5Hash("abcdefghijklmnopqrstuvwxyz0123456789@$&^%"));
        emP5.setAccountId("50125");
        emP5.setDepartmentId(Department.FACTORY.getDepartment());
        emP5.setUnitId(Unit.GALLONS.getUnit());
        emP5.setLimit(new BigDecimal("14"));
        this.emissionLimitsList.add(emP5);

        EmissionLimits emP6 = new EmissionLimits();
        emP6.setLimitId(this.hashCreator.createMD5Hash("abcdefghijklmnopqrstuvwxyz0123456789@$&^%"));
        emP6.setAccountId("501026");
        emP6.setDepartmentId(Department.FACTORY.getDepartment());
        emP6.setUnitId(Unit.GALLONS.getUnit());
        emP6.setLimit(new BigDecimal("2305"));
        this.emissionLimitsList.add(emP6);

        EmissionLimits emP7 = new EmissionLimits();
        emP7.setLimitId(this.hashCreator.createMD5Hash("abcdefghijklmnopqrstuvwxyz0123456789@$&^%"));
        emP7.setAccountId("501027");
        emP7.setDepartmentId(Department.FACTORY.getDepartment());
        emP7.setUnitId(Unit.GALLONS.getUnit());
        emP7.setLimit(new BigDecimal("4140"));
        this.emissionLimitsList.add(emP7);

        EmissionLimits emP8 = new EmissionLimits();
        emP8.setLimitId(this.hashCreator.createMD5Hash("abcdefghijklmnopqrstuvwxyz0123456789@$&^%"));
        emP8.setAccountId("501028");
        emP8.setDepartmentId(Department.FACTORY.getDepartment());
        emP8.setUnitId(Unit.KW.getUnit());
        emP8.setLimit(new BigDecimal("4140"));
        this.emissionLimitsList.add(emP8);

        EmissionLimits emP9 = new EmissionLimits();
        emP9.setLimitId(this.hashCreator.createMD5Hash("abcdefghijklmnopqrstuvwxyz0123456789@$&^%"));
        emP9.setAccountId("601026");
        emP9.setDepartmentId(Department.LOGISTIC.getDepartment());
        emP9.setUnitId(Unit.GALLONS.getUnit());
        emP9.setLimit(new BigDecimal("5755"));
        this.emissionLimitsList.add(emP9);

        EmissionLimits emP10 = new EmissionLimits();
        emP10.setLimitId(this.hashCreator.createMD5Hash("abcdefghijklmnopqrstuvwxyz0123456789@$&^%"));
        emP10.setAccountId("601029");
        emP10.setDepartmentId(Department.LOGISTIC.getDepartment());
        emP10.setUnitId(Unit.GALLONS.getUnit());
        emP10.setLimit(new BigDecimal("5"));
        this.emissionLimitsList.add(emP10);

        EmissionLimits emP11 = new EmissionLimits();
        emP11.setLimitId(this.hashCreator.createMD5Hash("abcdefghijklmnopqrstuvwxyz0123456789@$&^%"));
        emP11.setAccountId("601050");
        emP11.setDepartmentId(Department.LOGISTIC.getDepartment());
        emP11.setUnitId(Unit.GALLONS.getUnit());
        emP11.setLimit(new BigDecimal("24"));
        this.emissionLimitsList.add(emP11);
    }

    private void load3MonthsAlreadyEmitted() throws NoSuchAlgorithmException {
        Emission em1 = new Emission();
        em1.setUnitId(this.hashCreator.createMD5Hash("abcdefghijklmnopqrstuvwxyz0123456789@$&^%"));
        em1.setDepartmentId(Department.ADMIN.getDepartment());
        em1.setAccountId("701026");
        em1.setUnitId(Unit.GALLONS.getUnit());
        em1.setEmission(new BigDecimal(750));
        em1.setMonth(1);
        em1.setYear(2022);
        this.emissionRecordsTreeMap.put(em1.getEmissionId(), em1);

        Emission em2 = new Emission();
        em2.setUnitId(this.hashCreator.createMD5Hash("abcdefghijklmnopqrstuvwxyz0123456789@$&^%"));
        em2.setDepartmentId(Department.ADMIN.getDepartment());
        em2.setAccountId("701026");
        em2.setUnitId(Unit.GALLONS.getUnit());
        em2.setEmission(new BigDecimal(750));
        em2.setMonth(2);
        em2.setYear(2022);
        this.emissionRecordsTreeMap.put(em2.getEmissionId(), em2);

        Emission em3 = new Emission();
        em3.setUnitId(this.hashCreator.createMD5Hash("abcdefghijklmnopqrstuvwxyz0123456789@$&^%"));
        em3.setDepartmentId(Department.ADMIN.getDepartment());
        em3.setAccountId("701026");
        em3.setUnitId(Unit.GALLONS.getUnit());
        em3.setEmission(new BigDecimal(750));
        em3.setMonth(3);
        em3.setYear(2022);
        this.emissionRecordsTreeMap.put(em3.getEmissionId(), em3);

        // electricity
        Emission em4 = new Emission();
        em4.setUnitId(this.hashCreator.createMD5Hash("abcdefghijklmnopqrstuvwxyz0123456789@$&^%"));
        em4.setDepartmentId(Department.ADMIN.getDepartment());
        em4.setAccountId("701028");
        em4.setUnitId(Unit.KW.getUnit());
        em4.setEmission(new BigDecimal(100));
        em4.setMonth(1);
        em4.setYear(2022);
        this.emissionRecordsTreeMap.put(em4.getEmissionId(), em4);

        Emission em5 = new Emission();
        em5.setUnitId(this.hashCreator.createMD5Hash("abcdefghijklmnopqrstuvwxyz0123456789@$&^%"));
        em5.setDepartmentId(Department.ADMIN.getDepartment());
        em5.setAccountId("701028");
        em5.setUnitId(Unit.KW.getUnit());
        em5.setEmission(new BigDecimal(100));
        em5.setMonth(2);
        em5.setYear(2022);
        this.emissionRecordsTreeMap.put(em5.getEmissionId(), em5);

        Emission em6 = new Emission();
        em6.setUnitId(this.hashCreator.createMD5Hash("abcdefghijklmnopqrstuvwxyz0123456789@$&^%"));
        em6.setDepartmentId(Department.ADMIN.getDepartment());
        em6.setAccountId("701028");
        em6.setUnitId(Unit.KW.getUnit());
        em6.setEmission(new BigDecimal(100));
        em6.setMonth(3);
        em6.setYear(2022);
        this.emissionRecordsTreeMap.put(em6.getEmissionId(), em6);

        // trips
        Emission em7 = new Emission();
        em7.setUnitId(this.hashCreator.createMD5Hash("abcdefghijklmnopqrstuvwxyz0123456789@$&^%"));
        em7.setDepartmentId(Department.ADMIN.getDepartment());
        em7.setAccountId("701030");
        em7.setUnitId(Unit.TRIPS.getUnit());
        em7.setEmission(new BigDecimal(1));
        em7.setMonth(1);
        em7.setYear(2022);
        this.emissionRecordsTreeMap.put(em7.getEmissionId(), em7);

        Emission em8 = new Emission();
        em8.setUnitId(this.hashCreator.createMD5Hash("abcdefghijklmnopqrstuvwxyz0123456789@$&^%"));
        em8.setDepartmentId(Department.ADMIN.getDepartment());
        em8.setAccountId("701030");
        em8.setUnitId(Unit.TRIPS.getUnit());
        em8.setEmission(new BigDecimal(1));
        em8.setMonth(2);
        em8.setYear(2022);
        this.emissionRecordsTreeMap.put(em8.getEmissionId(), em8);

        Emission em9 = new Emission();
        em9.setUnitId(this.hashCreator.createMD5Hash("abcdefghijklmnopqrstuvwxyz0123456789@$&^%"));
        em9.setDepartmentId(Department.ADMIN.getDepartment());
        em9.setAccountId("701030");
        em9.setUnitId(Unit.TRIPS.getUnit());
        em9.setEmission(new BigDecimal(1));
        em9.setMonth(3);
        em9.setYear(2022);
        this.emissionRecordsTreeMap.put(em9.getEmissionId(), em9);
    }

}
