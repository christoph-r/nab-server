package at.vienna.glg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableAutoConfiguration
@SpringBootApplication
public class NabServer {
	public static void main(String[] args) {
		SpringApplication.run(NabServer.class, args);
	}
}
