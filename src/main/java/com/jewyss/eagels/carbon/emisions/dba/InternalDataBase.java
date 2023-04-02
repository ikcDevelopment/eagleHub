package com.jewyss.eagels.carbon.emisions.dba;

import com.jewyss.eagels.carbon.emisions.models.AccountToControl;
import com.jewyss.eagels.carbon.emisions.models.EmissionLimits;
import com.jewyss.eagels.carbon.emisions.models.request.Emission;
import com.jewyss.eagels.carbon.emisions.models.request.EmissionRequest;
import com.jewyss.eagels.carbon.emisions.security.GetKeyCode;
import com.jewyss.eagels.carbon.emisions.security.HashCreator;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.metrics.MetricsProperties;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.ApplicationScope;

import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @company kappa.computacion
 * @coder estuardo.wyss
 * @date
 */
@ApplicationScope
@Controller
public class InternalDataBase {
    GetKeyCode getKeyCode = new GetKeyCode();
    private HashCreator hashCreator;

    @Autowired
    public void setHashCreator(HashCreator hashCreator){
        this.hashCreator = hashCreator;
    }


    private TreeMap<String, Emission> emissionRecordsTreeMap;
    private List<AccountToControl> accountToControlList;

    private List<EmissionLimits> emissionLimitsList;
    @Getter
    private String message="";

    public InternalDataBase() {
        this.emissionRecordsTreeMap = new TreeMap<String, Emission>();
        this.accountToControlList = new ArrayList<>();
        this.emissionLimitsList = new ArrayList<>();
    }

    public boolean addEmissionRecord(EmissionRequest emissionRequest){
        this.message="Emission record successfully added.";

        try {
            Emission emission = this.convertEmission(emissionRequest);
            emissionRecordsTreeMap.put(emission.getEmissionId(), emission);
            System.out.println(emission.getEmissionId());
            System.out.println(emission.getAccountId());
            System.out.println(emission.getDepartmentId());
            System.out.println(emissionRecordsTreeMap.size());
            return true;
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

    public boolean deleteEmissionRecord(String emissionId){
        this.message="Emission successfully deleted.";

        if(Objects.nonNull(emissionId)){
            Emission em = this.emissionRecordsTreeMap.get(emissionId);
            if(Objects.nonNull(em)){
                this.emissionRecordsTreeMap.remove(em.getEmissionId(), em);
                return true;
            }else{
                this.message="Emission does not exist on data base.";
            }
        }else{
            this.message="Null request.";
        }

        return false;
    }

    /**
     * I used accounts to manage the categories
     * @param accounts
     */
    public Map<Integer, BigDecimal> percentageEmissionByCategory(List<String> accounts){
        this.message = "The percentages where calculated successfully!";

        List<Emission> emissionsInDb = new ArrayList<>();
        Map<Integer, BigDecimal> resultados  = new HashMap<>();
        Map<Integer, BigDecimal> resultsPercentage  = new HashMap<>();
        // filter accounts
        this.emissionRecordsTreeMap.forEach( (id, emission) ->{
            System.out.println(emission.getAccountId());
            if(accounts.contains(emission.getAccountId())){
                emissionsInDb.add(emission);
            }
        });
        // sort by month
        emissionsInDb.sort((d1, d2) -> {
            return d2.getMonth() - d1.getMonth();
        });

        System.out.println(emissionsInDb);

        String account = "";
        BigDecimal value = BigDecimal.ZERO;
        BigDecimal totalSum = BigDecimal.ZERO;
        int mes = 0;

        for (Emission emission : emissionsInDb) {
            value = emission.getEmission();
            mes = emission.getMonth();

            if (resultados.get(mes) != null) {
                BigDecimal valor = resultados.get(mes);
                valor = valor.add(value);
                totalSum = totalSum.add(value);
                resultados.replace(mes, valor);
            }else{
                resultados.put(mes, value);
            }
        }

        for (Map.Entry<Integer, BigDecimal> entry : resultados.entrySet()) {
            Integer month = entry.getKey();
            BigDecimal valueP = entry.getValue();
            BigDecimal percentage = valueP.divide(totalSum);
            resultsPercentage.put(month, percentage);
        }

        return resultsPercentage;
    }

    private Emission convertEmission(EmissionRequest emissionRequest) throws NoSuchAlgorithmException {
        Emission emission = new Emission();
        //emission.setEmissionId(getKeyCode.get_keyCode());
        emission.setEmissionId(getKeyCode.get_keyCode());
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
            load3MonthsAlreadyEmitted();
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
        emP1.setLimitId(getKeyCode.get_keyCode());
        emP1.setAccountId("701026");
        emP1.setDepartmentId(Department.ADMIN.getDepartment());
        emP1.setUnitId(Unit.GALLONS.getUnit());
        emP1.setLimit(new BigDecimal("3450"));
        this.emissionLimitsList.add(emP1);

        EmissionLimits emP2 = new EmissionLimits();
        emP2.setLimitId(getKeyCode.get_keyCode());
        emP2.setAccountId("701050");
        emP2.setDepartmentId(Department.ADMIN.getDepartment());
        emP2.setUnitId(Unit.KW.getUnit());
        emP2.setLimit(new BigDecimal("1380"));
        this.emissionLimitsList.add(emP2);

        EmissionLimits emP3 = new EmissionLimits();
        emP3.setLimitId(getKeyCode.get_keyCode());
        emP3.setAccountId("701030");
        emP3.setDepartmentId(Department.ADMIN.getDepartment());
        emP3.setUnitId(Unit.TRIPS.getUnit());
        emP3.setLimit(new BigDecimal("36"));
        this.emissionLimitsList.add(emP3);

        EmissionLimits emP4 = new EmissionLimits();
        emP4.setLimitId(getKeyCode.get_keyCode());
        emP4.setAccountId("701051");
        emP4.setDepartmentId(Department.ADMIN.getDepartment());
        emP4.setUnitId(Unit.SHEETS.getUnit());
        emP4.setLimit(new BigDecimal("27600"));
        this.emissionLimitsList.add(emP4);

        EmissionLimits emP5 = new EmissionLimits();
        emP5.setLimitId(getKeyCode.get_keyCode());
        emP5.setAccountId("50125");
        emP5.setDepartmentId(Department.FACTORY.getDepartment());
        emP5.setUnitId(Unit.GALLONS.getUnit());
        emP5.setLimit(new BigDecimal("14"));
        this.emissionLimitsList.add(emP5);

        EmissionLimits emP6 = new EmissionLimits();
        emP6.setLimitId(getKeyCode.get_keyCode());
        emP6.setAccountId("501026");
        emP6.setDepartmentId(Department.FACTORY.getDepartment());
        emP6.setUnitId(Unit.GALLONS.getUnit());
        emP6.setLimit(new BigDecimal("2305"));
        this.emissionLimitsList.add(emP6);

        EmissionLimits emP7 = new EmissionLimits();
        emP7.setLimitId(getKeyCode.get_keyCode());
        emP7.setAccountId("501027");
        emP7.setDepartmentId(Department.FACTORY.getDepartment());
        emP7.setUnitId(Unit.GALLONS.getUnit());
        emP7.setLimit(new BigDecimal("4140"));
        this.emissionLimitsList.add(emP7);

        EmissionLimits emP8 = new EmissionLimits();
        emP8.setLimitId(getKeyCode.get_keyCode());
        emP8.setAccountId("501028");
        emP8.setDepartmentId(Department.FACTORY.getDepartment());
        emP8.setUnitId(Unit.KW.getUnit());
        emP8.setLimit(new BigDecimal("4140"));
        this.emissionLimitsList.add(emP8);

        EmissionLimits emP9 = new EmissionLimits();
        emP9.setLimitId(getKeyCode.get_keyCode());
        emP9.setAccountId("601026");
        emP9.setDepartmentId(Department.LOGISTIC.getDepartment());
        emP9.setUnitId(Unit.GALLONS.getUnit());
        emP9.setLimit(new BigDecimal("5755"));
        this.emissionLimitsList.add(emP9);

        EmissionLimits emP10 = new EmissionLimits();
        emP10.setLimitId(getKeyCode.get_keyCode());
        emP10.setAccountId("601029");
        emP10.setDepartmentId(Department.LOGISTIC.getDepartment());
        emP10.setUnitId(Unit.GALLONS.getUnit());
        emP10.setLimit(new BigDecimal("5"));
        this.emissionLimitsList.add(emP10);

        EmissionLimits emP11 = new EmissionLimits();
        emP11.setLimitId(getKeyCode.get_keyCode());
        emP11.setAccountId("601050");
        emP11.setDepartmentId(Department.LOGISTIC.getDepartment());
        emP11.setUnitId(Unit.GALLONS.getUnit());
        emP11.setLimit(new BigDecimal("24"));
        this.emissionLimitsList.add(emP11);
    }

    private void load3MonthsAlreadyEmitted() throws NoSuchAlgorithmException {
        Emission em1 = new Emission();
        em1.setEmissionId(getKeyCode.get_keyCode());
        em1.setDepartmentId(Department.ADMIN.getDepartment());
        em1.setAccountId("701026");
        em1.setUnitId(Unit.GALLONS.getUnit());
        em1.setEmission(new BigDecimal(750));
        em1.setMonth(1);
        em1.setYear(2022);
        this.emissionRecordsTreeMap.put(em1.getEmissionId(), em1);

        Emission em2 = new Emission();
        em2.setEmissionId(getKeyCode.get_keyCode());
        em2.setDepartmentId(Department.ADMIN.getDepartment());
        em2.setAccountId("701026");
        em2.setUnitId(Unit.GALLONS.getUnit());
        em2.setEmission(new BigDecimal(750));
        em2.setMonth(2);
        em2.setYear(2022);
        this.emissionRecordsTreeMap.put(em2.getEmissionId(), em2);

        Emission em3 = new Emission();
        em3.setEmissionId(getKeyCode.get_keyCode());
        em3.setDepartmentId(Department.ADMIN.getDepartment());
        em3.setAccountId("701026");
        em3.setUnitId(Unit.GALLONS.getUnit());
        em3.setEmission(new BigDecimal(750));
        em3.setMonth(3);
        em3.setYear(2022);
        this.emissionRecordsTreeMap.put(em3.getEmissionId(), em3);

        // electricity
        Emission em4 = new Emission();
        em4.setEmissionId(getKeyCode.get_keyCode());
        em4.setDepartmentId(Department.ADMIN.getDepartment());
        em4.setAccountId("701028");
        em4.setUnitId(Unit.KW.getUnit());
        em4.setEmission(new BigDecimal(100));
        em4.setMonth(1);
        em4.setYear(2022);
        this.emissionRecordsTreeMap.put(em4.getEmissionId(), em4);

        Emission em5 = new Emission();
        em5.setEmissionId(getKeyCode.get_keyCode());
        em5.setDepartmentId(Department.ADMIN.getDepartment());
        em5.setAccountId("701028");
        em5.setUnitId(Unit.KW.getUnit());
        em5.setEmission(new BigDecimal(100));
        em5.setMonth(2);
        em5.setYear(2022);
        this.emissionRecordsTreeMap.put(em5.getEmissionId(), em5);

        Emission em6 = new Emission();
        em6.setEmissionId(getKeyCode.get_keyCode());
        em6.setDepartmentId(Department.ADMIN.getDepartment());
        em6.setAccountId("701028");
        em6.setUnitId(Unit.KW.getUnit());
        em6.setEmission(new BigDecimal(100));
        em6.setMonth(3);
        em6.setYear(2022);
        this.emissionRecordsTreeMap.put(em6.getEmissionId(), em6);

        // trips
        Emission em7 = new Emission();
        em7.setEmissionId(getKeyCode.get_keyCode());
        em7.setDepartmentId(Department.ADMIN.getDepartment());
        em7.setAccountId("701030");
        em7.setUnitId(Unit.TRIPS.getUnit());
        em7.setEmission(new BigDecimal(1));
        em7.setMonth(1);
        em7.setYear(2022);
        this.emissionRecordsTreeMap.put(em7.getEmissionId(), em7);

        Emission em8 = new Emission();
        em8.setEmissionId(getKeyCode.get_keyCode());
        em8.setDepartmentId(Department.ADMIN.getDepartment());
        em8.setAccountId("701030");
        em8.setUnitId(Unit.TRIPS.getUnit());
        em8.setEmission(new BigDecimal(1));
        em8.setMonth(2);
        em8.setYear(2022);
        this.emissionRecordsTreeMap.put(em8.getEmissionId(), em8);

        Emission em9 = new Emission();
        em9.setEmissionId(getKeyCode.get_keyCode());
        em9.setDepartmentId(Department.ADMIN.getDepartment());
        em9.setAccountId("701030");
        em9.setUnitId(Unit.TRIPS.getUnit());
        em9.setEmission(new BigDecimal(1));
        em9.setMonth(3);
        em9.setYear(2022);
        this.emissionRecordsTreeMap.put(em9.getEmissionId(), em9);

        // sheets
        Emission em10 = new Emission();
        em10.setEmissionId(getKeyCode.get_keyCode());
        em10.setDepartmentId(Department.ADMIN.getDepartment());
        em10.setAccountId("701051");
        em10.setUnitId(Unit.SHEETS.getUnit());
        em10.setEmission(new BigDecimal(100));
        em10.setMonth(1);
        em10.setYear(2022);
        this.emissionRecordsTreeMap.put(em10.getEmissionId(), em10);

        Emission em11 = new Emission();
        em11.setEmissionId(getKeyCode.get_keyCode());
        em11.setDepartmentId(Department.ADMIN.getDepartment());
        em11.setAccountId("701051");
        em11.setUnitId(Unit.SHEETS.getUnit());
        em11.setEmission(new BigDecimal(100));
        em11.setMonth(2);
        em11.setYear(2022);
        this.emissionRecordsTreeMap.put(em11.getEmissionId(), em11);

        Emission em12 = new Emission();
        em12.setEmissionId(getKeyCode.get_keyCode());
        em12.setDepartmentId(Department.ADMIN.getDepartment());
        em12.setAccountId("701051");
        em12.setUnitId(Unit.SHEETS.getUnit());
        em12.setEmission(new BigDecimal(100));
        em12.setMonth(3);
        em12.setYear(2022);
        this.emissionRecordsTreeMap.put(em12.getEmissionId(), em12);

        //production
        Emission em13 = new Emission();
        em13.setEmissionId(getKeyCode.get_keyCode());
        em13.setDepartmentId(Department.FACTORY.getDepartment());
        em13.setAccountId("501025");
        em13.setUnitId(Unit.GALLONS.getUnit());
        em13.setEmission(new BigDecimal(1));
        em13.setMonth(1);
        em13.setYear(2022);
        this.emissionRecordsTreeMap.put(em13.getEmissionId(), em13);

        Emission em14 = new Emission();
        em14.setEmissionId(getKeyCode.get_keyCode());
        em14.setDepartmentId(Department.FACTORY.getDepartment());
        em14.setAccountId("501025");
        em14.setUnitId(Unit.GALLONS.getUnit());
        em14.setEmission(new BigDecimal(1));
        em14.setMonth(2);
        em14.setYear(2022);
        this.emissionRecordsTreeMap.put(em14.getEmissionId(), em14);

        Emission em15 = new Emission();
        em15.setEmissionId(getKeyCode.get_keyCode());
        em15.setDepartmentId(Department.FACTORY.getDepartment());
        em15.setAccountId("501025");
        em15.setUnitId(Unit.GALLONS.getUnit());
        em15.setEmission(new BigDecimal(1));
        em15.setMonth(3);
        em15.setYear(2022);
        this.emissionRecordsTreeMap.put(em15.getEmissionId(), em15);

        // gas
        Emission em16 = new Emission();
        em16.setEmissionId(getKeyCode.get_keyCode());
        em16.setDepartmentId(Department.FACTORY.getDepartment());
        em16.setAccountId("501026");
        em16.setUnitId(Unit.GALLONS.getUnit());
        em16.setEmission(new BigDecimal("166.67"));
        em16.setMonth(1);
        em16.setYear(2022);
        this.emissionRecordsTreeMap.put(em16.getEmissionId(), em16);

        Emission em17 = new Emission();
        em17.setEmissionId(getKeyCode.get_keyCode());
        em17.setDepartmentId(Department.FACTORY.getDepartment());
        em17.setAccountId("501026");
        em17.setUnitId(Unit.GALLONS.getUnit());
        em17.setEmission(new BigDecimal("166.67"));
        em17.setMonth(2);
        em17.setYear(2022);
        this.emissionRecordsTreeMap.put(em17.getEmissionId(), em17);

        Emission em18 = new Emission();
        em18.setEmissionId(getKeyCode.get_keyCode());
        em18.setDepartmentId(Department.FACTORY.getDepartment());
        em18.setAccountId("501026");
        em18.setUnitId(Unit.GALLONS.getUnit());
        em18.setEmission(new BigDecimal("166.67"));
        em18.setMonth(3);
        em18.setYear(2022);
        this.emissionRecordsTreeMap.put(em18.getEmissionId(), em18);

        // oil
        Emission em19 = new Emission();
        em19.setEmissionId(getKeyCode.get_keyCode());
        em19.setDepartmentId(Department.FACTORY.getDepartment());
        em19.setAccountId("501027");
        em19.setUnitId(Unit.GALLONS.getUnit());
        em19.setEmission(new BigDecimal("300"));
        em19.setMonth(1);
        em19.setYear(2022);
        this.emissionRecordsTreeMap.put(em19.getEmissionId(), em19);

        Emission em20 = new Emission();
        em20.setEmissionId(getKeyCode.get_keyCode());
        em20.setDepartmentId(Department.FACTORY.getDepartment());
        em20.setAccountId("501027");
        em20.setUnitId(Unit.GALLONS.getUnit());
        em20.setEmission(new BigDecimal("300"));
        em20.setMonth(2);
        em20.setYear(2022);
        this.emissionRecordsTreeMap.put(em20.getEmissionId(), em20);

        Emission em21 = new Emission();
        em21.setEmissionId(getKeyCode.get_keyCode());
        em21.setDepartmentId(Department.FACTORY.getDepartment());
        em21.setAccountId("501027");
        em21.setUnitId(Unit.GALLONS.getUnit());
        em21.setEmission(new BigDecimal("300"));
        em21.setMonth(3);
        em21.setYear(2022);
        this.emissionRecordsTreeMap.put(em21.getEmissionId(), em21);

        // electricity
        Emission em22 = new Emission();
        em22.setEmissionId(getKeyCode.get_keyCode());
        em22.setDepartmentId(Department.FACTORY.getDepartment());
        em22.setAccountId("501028");
        em22.setUnitId(Unit.KW.getUnit());
        em22.setEmission(new BigDecimal("300"));
        em22.setMonth(1);
        em22.setYear(2022);
        this.emissionRecordsTreeMap.put(em22.getEmissionId(), em22);

        Emission em23 = new Emission();
        em23.setEmissionId(getKeyCode.get_keyCode());
        em23.setDepartmentId(Department.FACTORY.getDepartment());
        em23.setAccountId("501028");
        em23.setUnitId(Unit.KW.getUnit());
        em23.setEmission(new BigDecimal("300"));
        em23.setMonth(2);
        em23.setYear(2022);
        this.emissionRecordsTreeMap.put(em20.getEmissionId(), em20);

        Emission em24 = new Emission();
        em24.setEmissionId(getKeyCode.get_keyCode());
        em24.setDepartmentId(Department.FACTORY.getDepartment());
        em24.setAccountId("501028");
        em24.setUnitId(Unit.KW.getUnit());
        em24.setEmission(new BigDecimal("300"));
        em24.setMonth(3);
        em24.setYear(2022);
        this.emissionRecordsTreeMap.put(em24.getEmissionId(), em24);

        // logistic
        // gas
        Emission em25 = new Emission();
        em25.setEmissionId(getKeyCode.get_keyCode());
        em25.setDepartmentId(Department.LOGISTIC.getDepartment());
        em25.setAccountId("601026");
        em25.setUnitId(Unit.GALLONS.getUnit());
        em25.setEmission(new BigDecimal("416.67"));
        em25.setMonth(1);
        em25.setYear(2022);
        this.emissionRecordsTreeMap.put(em25.getEmissionId(), em25);

        Emission em26 = new Emission();
        em26.setEmissionId(getKeyCode.get_keyCode());
        em26.setDepartmentId(Department.LOGISTIC.getDepartment());
        em26.setAccountId("601026");
        em26.setUnitId(Unit.GALLONS.getUnit());
        em26.setEmission(new BigDecimal("416.67"));
        em26.setMonth(2);
        em26.setYear(2022);
        this.emissionRecordsTreeMap.put(em26.getEmissionId(), em26);

        Emission em27 = new Emission();
        em27.setEmissionId(getKeyCode.get_keyCode());
        em27.setDepartmentId(Department.LOGISTIC.getDepartment());
        em27.setAccountId("601026");
        em27.setUnitId(Unit.GALLONS.getUnit());
        em27.setEmission(new BigDecimal("416.66"));
        em27.setMonth(3);
        em27.setYear(2022);
        this.emissionRecordsTreeMap.put(em27.getEmissionId(), em27);

        // oil
        Emission em28 = new Emission();
        em28.setEmissionId(getKeyCode.get_keyCode());
        em28.setDepartmentId(Department.LOGISTIC.getDepartment());
        em28.setAccountId("601029");
        em28.setUnitId(Unit.GALLONS.getUnit());
        em28.setEmission(new BigDecimal("0.33"));
        em28.setMonth(1);
        em28.setYear(2022);
        this.emissionRecordsTreeMap.put(em28.getEmissionId(), em28);

        Emission em29 = new Emission();
        em29.setEmissionId(getKeyCode.get_keyCode());
        em29.setDepartmentId(Department.LOGISTIC.getDepartment());
        em29.setAccountId("601029");
        em29.setUnitId(Unit.GALLONS.getUnit());
        em29.setEmission(new BigDecimal("0.33"));
        em29.setMonth(2);
        em29.setYear(2022);
        this.emissionRecordsTreeMap.put(em29.getEmissionId(), em29);

        Emission em30 = new Emission();
        em30.setEmissionId(getKeyCode.get_keyCode());
        em30.setDepartmentId(Department.LOGISTIC.getDepartment());
        em30.setAccountId("601029");
        em30.setUnitId(Unit.GALLONS.getUnit());
        em30.setEmission(new BigDecimal("0.34"));
        em30.setMonth(3);
        em30.setYear(2022);
        this.emissionRecordsTreeMap.put(em30.getEmissionId(), em30);


        // trip
        Emission em31 = new Emission();
        em31.setEmissionId(getKeyCode.get_keyCode());
        em31.setDepartmentId(Department.LOGISTIC.getDepartment());
        em31.setAccountId("601050");
        em31.setUnitId(Unit.TRIPS.getUnit());
        em31.setEmission(new BigDecimal("2"));
        em31.setMonth(1);
        em31.setYear(2022);
        this.emissionRecordsTreeMap.put(em31.getEmissionId(), em31);

        Emission em32 = new Emission();
        em32.setEmissionId(getKeyCode.get_keyCode());
        em32.setDepartmentId(Department.LOGISTIC.getDepartment());
        em32.setAccountId("601050");
        em32.setUnitId(Unit.TRIPS.getUnit());
        em32.setEmission(new BigDecimal("2"));
        em32.setMonth(2);
        em32.setYear(2022);
        this.emissionRecordsTreeMap.put(em32.getEmissionId(), em32);

        Emission em33 = new Emission();
        em33.setEmissionId(getKeyCode.get_keyCode());
        em33.setDepartmentId(Department.LOGISTIC.getDepartment());
        em33.setAccountId("601050");
        em33.setUnitId(Unit.TRIPS.getUnit());
        em33.setEmission(new BigDecimal("2"));
        em33.setMonth(3);
        em33.setYear(2022);
        this.emissionRecordsTreeMap.put(em33.getEmissionId(), em33);

    }

}
