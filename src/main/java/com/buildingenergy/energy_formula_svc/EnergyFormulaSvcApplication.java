package com.buildingenergy.energy_formula_svc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EnergyFormulaSvcApplication {

	public static void main(String[] args) {
		SpringApplication.run(EnergyFormulaSvcApplication.class, args);
	}

}
