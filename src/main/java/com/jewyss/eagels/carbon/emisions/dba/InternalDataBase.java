package com.jewyss.eagels.carbon.emisions.dba;

import com.jewyss.eagels.carbon.emisions.models.request.Emission;
import com.jewyss.eagels.carbon.emisions.models.request.EmissionRequest;
import com.jewyss.eagels.carbon.emisions.security.HashCreator;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;

import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.TreeMap;

/**
 * @company kappa.computacion
 * @coder estuardo.wyss
 * @date
 */
public class InternalDataBase {
    @Autowired
    private HashCreator hashCreator;

    private final TreeMap<String, Emission> emissionRecordsTreeMap = new TreeMap<>();
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

}
