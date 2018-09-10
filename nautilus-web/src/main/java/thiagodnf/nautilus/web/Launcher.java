package thiagodnf.nautilus.web;

import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Launcher {

	@PostConstruct
	public void initIt() {
		TimeZone.setDefault(TimeZone.getTimeZone("America/Sao_Paulo")); 
	}

	public static void main(String[] args) {
		SpringApplication.run(Launcher.class, args);
	}
}
