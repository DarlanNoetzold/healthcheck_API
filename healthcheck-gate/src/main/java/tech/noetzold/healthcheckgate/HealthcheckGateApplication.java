package tech.noetzold.healthcheckgate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class HealthcheckGateApplication {

	public static void main(String[] args) {
		SpringApplication.run(HealthcheckGateApplication.class, args);
	}

}
