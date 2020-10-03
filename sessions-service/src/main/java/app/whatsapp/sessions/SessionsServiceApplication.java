package app.whatsapp.sessions;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("app.whatsapp")
public class SessionsServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SessionsServiceApplication.class, args);
	}

}
