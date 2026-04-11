package br.com.gva.imobfacil;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class ImobfacilApplication {

	public static void main(String[] args) {
		SpringApplication.run(ImobfacilApplication.class, args);
	}

}
