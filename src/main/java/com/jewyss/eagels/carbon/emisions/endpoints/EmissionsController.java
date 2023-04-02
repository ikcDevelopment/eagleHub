package com.jewyss.eagels.carbon.emisions.endpoints;

import com.jewyss.eagels.carbon.emisions.dba.InternalDataBase;
import com.jewyss.eagels.carbon.emisions.models.request.Emission;
import com.jewyss.eagels.carbon.emisions.models.request.EmissionCategories;
import com.jewyss.eagels.carbon.emisions.models.request.EmissionRequest;
import com.jewyss.eagels.carbon.emisions.models.responses.ResponseByPercentageCategory;
import com.jewyss.eagels.carbon.emisions.models.responses.ResponseObject;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;

/**
 * @project Eagle hub
 * @coder estuardo.wyss
 * @date 04/01/2023
 */
@RestController
@RequestMapping(value="/v1/emissions/admin")
@Tag(name = "Carbon emissions API")
@CrossOrigin(origins = "*")
@OpenAPIDefinition(
        info = @Info(
                title = "Hello World",
                version = "0.0",
                description = "My API",
                license = @License(name = "Apache 2.0", url = "https://foo.bar"),
                contact = @Contact(url = "https://gigantic-server.com", name = "Fred", email = "Fred@gigagantic-server.com")
        )
)
public class EmissionsController {

    InternalDataBase internalDataBase;
    @Autowired
    public void setInternalDataBase(InternalDataBase internalDataBase){
        this.internalDataBase = internalDataBase;
    }

    @Operation(summary = "Add new emission", description = "Add new emission bases in classes defined for faster coding.")
    @ApiResponse(responseCode = "200", description = "Successfully created.")
    @ApiResponse(responseCode = "404", description = "Not found - The endpoint to create an owner was not found.")
    @RequestMapping(
            value="/new",
            method = RequestMethod.POST
    )
    public ResponseEntity<ResponseObject> addEmission(
            @Parameter @RequestBody EmissionRequest emission
    ){
        ResponseObject eagleResponse = new ResponseObject();

        if(this.internalDataBase.addEmissionRecord(emission)){
            eagleResponse.setAmount(1);
            eagleResponse.setMessage(this.internalDataBase.getMessage());
            eagleResponse.setStatus("ok");
            eagleResponse.setSuccess(true);
        }else{
            eagleResponse.setAmount(0);
            eagleResponse.setMessage(this.internalDataBase.getMessage());
            eagleResponse.setStatus("err");
            eagleResponse.setSuccess(true);
        }

        return ResponseEntity.ok(eagleResponse);
    }

    @Operation(summary = "Update emission", description = "Updates emission bases in classes defined for faster coding.")
    @ApiResponse(responseCode = "200", description = "Successfully created.")
    @ApiResponse(responseCode = "404", description = "Not found - The endpoint to create an owner was not found.")
    @RequestMapping(
            value="/update",
            method = RequestMethod.PUT
    )
    public ResponseEntity<ResponseObject> updateEmission(
            @Parameter @RequestBody Emission emission
    ){
        ResponseObject eagleResponse = new ResponseObject();

        if(this.internalDataBase.updateEmissionRecord(emission)){
            eagleResponse.setAmount(1);
            eagleResponse.setMessage(this.internalDataBase.getMessage());
            eagleResponse.setStatus("ok");
            eagleResponse.setSuccess(true);
        }else{
            eagleResponse.setAmount(0);
            eagleResponse.setMessage(this.internalDataBase.getMessage());
            eagleResponse.setStatus("er");
            eagleResponse.setSuccess(true);
        }

        return ResponseEntity.ok(eagleResponse);
    }

    @Operation(summary = "Delete emission", description = "Deletes emission bases in classes defined for faster coding.")
    @ApiResponse(responseCode = "200", description = "Successfully created.")
    @ApiResponse(responseCode = "404", description = "Not found - The endpoint to create an owner was not found.")
    @RequestMapping(
            value="/delete",
            method = RequestMethod.DELETE
    )
    public ResponseEntity<ResponseObject> deleteEmission(
            @Parameter @RequestParam String emissionId
    ){
        ResponseObject eagleResponse = new ResponseObject();

        if(this.internalDataBase.deleteEmissionRecord(emissionId)){
            eagleResponse.setAmount(1);
            eagleResponse.setMessage(this.internalDataBase.getMessage());
            eagleResponse.setStatus("ok");
            eagleResponse.setSuccess(true);
        }else{
            eagleResponse.setAmount(0);
            eagleResponse.setMessage(this.internalDataBase.getMessage());
            eagleResponse.setStatus("err");
            eagleResponse.setSuccess(true);
        }

        return ResponseEntity.ok(eagleResponse);
    }

    @Operation(summary = "percentage Emission By Category",
            description = "percentage Emission By Category. " +
                    "Sending the accounts or categories, you are able to get the" +
                    "percentage related to each category you want to check.")
    @ApiResponse(responseCode = "200", description = "Successfully created.")
    @ApiResponse(responseCode = "404", description = "Not found - The endpoint to create an owner was not found.")
    @RequestMapping(
            value="/percentage/emission/category",
            method = RequestMethod.POST
    )
    public ResponseEntity<ResponseByPercentageCategory> percentageEmissionByCategory(@Parameter @RequestBody EmissionCategories categories){
        ResponseByPercentageCategory eagleResponse = new ResponseByPercentageCategory();

        Map<Integer, BigDecimal> values = this.internalDataBase.percentageEmissionByCategory(categories.getAccounts());

        if(Objects.nonNull(values)){
            eagleResponse.setAmount(values.size());
            eagleResponse.setMessage(this.internalDataBase.getMessage());
            eagleResponse.setStatus("ok");
            eagleResponse.setSuccess(true);
            eagleResponse.setCategories(values);
        }else{
            eagleResponse.setAmount(0);
            eagleResponse.setMessage("There was a problem calculating the percentage.");
            eagleResponse.setStatus("err");
            eagleResponse.setSuccess(true);
        }

        return ResponseEntity.ok(eagleResponse);
    }

    @RequestMapping(
            value="/ping",
            method = RequestMethod.GET
    )
    public String ping(){
        return "You are in";
    }
}
