package ifood.score;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan(basePackages="ifood.score")
@EnableJms
@EnableScheduling
@EnableCaching
public class Application {

	public static void main(String[] args) throws Exception {			
		SpringApplication.run(Application.class);
	}
	
}
