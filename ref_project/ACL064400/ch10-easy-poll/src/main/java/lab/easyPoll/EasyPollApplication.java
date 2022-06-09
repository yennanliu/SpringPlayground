package lab.easyPoll;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.mangofactory.swagger.plugin.EnableSwagger;

@SpringBootApplication
//@EnableSwagger
public class EasyPollApplication {

	public static void main(String[] args) {
		SpringApplication.run(EasyPollApplication.class, args);
	}
	
}
