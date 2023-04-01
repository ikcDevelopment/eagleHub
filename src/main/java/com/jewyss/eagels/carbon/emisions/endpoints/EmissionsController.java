package com.jewyss.eagels.carbon.emisions.endpoints;

import com.jewyss.eagels.carbon.emisions.models.request.Emission;
import com.jewyss.eagels.carbon.emisions.models.request.EmissionRequest;
import com.jewyss.eagels.carbon.emisions.models.responses.ResponseObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * @project Eagle hub
 * @coder estuardo.wyss
 * @date 04/01/2023
 */
@RestController
@RequestMapping(value="/v2/ikc-admin/owners")
@Tag(name = "Carbon emissions API")
@CrossOrigin(origins = "*")
public class EmissionsController {

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

        return ResponseEntity.ok(eagleResponse);
    }
}
