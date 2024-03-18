package tech.noetzold.healthcheckgate;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import tech.noetzold.healthcheckgate.security.service.AuthenticationService;
import tech.noetzold.healthcheckgate.security.user.RegisterRequest;
import tech.noetzold.healthcheckgate.security.user.Role;

@SpringBootApplication
@EnableFeignClients
@EnableRabbit
@EnableCaching
public class HealthcheckGateApplication {

	public static void main(String[] args) {
		SpringApplication.run(HealthcheckGateApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(
			AuthenticationService service
	) {
		return args -> {
			var admin = RegisterRequest.builder()
					.firstname("Admin")
					.lastname("Admin")
					.email("admin@mail.com")
					.password("password")
					.role(Role.ADMIN)
					.build();
			System.out.println("Admin token: " + service.register(admin).getAccessToken());
		};
	}

}
