package net.github.nikistadnik.springRaspberryJavaServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SpringRaspberryJavaServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringRaspberryJavaServerApplication.class, args);
	}
}


//todo

//to build a new jar - Terminal : mvn package
//to run it - Terminal : java -jar target/springRaspberryJavaServer-0.0.1-SNAPSHOT.jar

//javax replaced by jakarta
//do i need any of the 2 json dep

