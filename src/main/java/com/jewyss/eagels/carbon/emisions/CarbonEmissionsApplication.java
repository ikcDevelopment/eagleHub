package com.jewyss.eagels.carbon.emisions;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Carbon emissions API", version = "1.0", description = "To control and give metrics of the emissions of carbon."))
public class CarbonEmissionsApplication {

	public static void main(String[] args) {
		SpringApplication.run(CarbonEmissionsApplication.class, args);
	}

}
