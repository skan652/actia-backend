package com.example.air;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AirApplication {

	public static void main(String[] args) {
		SpringApplication.run(AirApplication.class, args);
	}
	@Bean
	public WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> webServerFactoryCustomizer() {
		return factory -> factory.setAddress(null); // Bind to all network interfaces
	}

	/*@Bean
	CommandLineRunner runner(Frame1Repository frame1Repository, Frame2Repository frame2Repository){
		return args -> {
			Frame1 frame1 = new Frame1(1600,200,300);
			frame1Repository.insert(frame1);
			Frame2 frame2 = new Frame2(300,250);
			frame2Repository.insert(frame2);
		};
	}*/
}
