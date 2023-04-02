I simplified the endpoint to register any kind of emissions ** addEmission()  ** using next class:
    `public class EmissionRequest {
        int month;
        int year;
        String accountId; 
        String departmentId;
        String unitId;
        BigDecimal emission;
    }`

I simplified the endpoint to update any kind of emissions ** updateEmission()  ** using next class:

    `public class Emission {
        String emissionId;
        int month;
        int year;
        String accountId;
        String departmentId;
        String unitId;
        BigDecimal emission;
    }`

I simplified the endpoint to calculate the consumption of any kind of emissions ** percentageEmissionByCategory()  ** using next class:

    Instead of a GET endpoint I am using a POST endpoint to be able to send a body request
    with a list of accounts ( emission ) the use want to rate: 

    public class EmissionCategories {
        List<String> accounts;
    }



InternalDataBase.java
    Is the data structure like database, I am using to solve this challenge.

    loadAccountsDataBase() method invoked at @Postconstruct the class
        is used to create the chart of accounts, to control the emissions, 
        used to relate and register them:

        Example:
        AccountToControl account1 = new AccountToControl();
        account1.setAccountId("501025");
        account1.setAccountName("Pérdida gases refrigerantes");
        this.accountToControlList.add(account1);
    
    loadAnnualLimitsDataBase() method  invoked at @Postconstruct the class
        I thought about this method targeting the future of the app.
        Here I set the max anually limits for each emission for a better
        control of the emissions. (Like a budget).

        Example:
        EmissionLimits emP1 = new EmissionLimits();
        emP1.setLimitId(getKeyCode.get_keyCode());
        emP1.setAccountId("701026");
        emP1.setDepartmentId(Department.ADMIN.getDepartment());
        emP1.setUnitId(Unit.GALLONS.getUnit());
        emP1.setLimit(new BigDecimal("3450"));
        this.emissionLimitsList.add(emP1);

    load3MonthsAlreadyEmitted() method  invoked at @Postconstruct the class
        I am registering the 3 months emissions using the accounts previously created.

        Example:
        Emission em1 = new Emission();
        em1.setEmissionId(getKeyCode.get_keyCode());
        em1.setDepartmentId(Department.ADMIN.getDepartment());
        em1.setAccountId("701026");
        em1.setUnitId(Unit.GALLONS.getUnit());
        em1.setEmission(new BigDecimal(750));
        em1.setMonth(1);
        em1.setYear(2022);
        this.emissionRecordsTreeMap.put(em1.getEmissionId(), em1);
