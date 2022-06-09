package lab.easyPoll;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@EnableSwagger
public class EasyPollApplication {

	public static void main(String[] args) {
		SpringApplication.run(EasyPollApplication.class, args);
	}

	@PostConstruct
	public void startDBManager() {
		System.setProperty("java.awt.headless", "false");
		org.hsqldb.util.DatabaseManagerSwing.main(new String[] { "--url", "jdbc:hsqldb:mem:testdb", "--noexit" });
	}

}
