package plus.suja.teach.teachshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class TeachShopApplication {

	public static void main(String[] args) {
		SpringApplication.run(TeachShopApplication.class, args);
	}

}
