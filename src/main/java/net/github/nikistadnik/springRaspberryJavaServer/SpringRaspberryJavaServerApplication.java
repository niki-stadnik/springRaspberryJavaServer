package net.github.nikistadnik.springRaspberryJavaServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.IOException;

@SpringBootApplication
@EnableScheduling
public class SpringRaspberryJavaServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringRaspberryJavaServerApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void camStart() throws IOException {

	}

	//todo оптимизиране на паузите от thread.sleep на millis
/*
	@Value("${local.port}")
	private int PORT;

	@Value("${enc.key}")
	private String key;

	@Autowired
	private Clients clt;



	@EventListener(ApplicationReadyEvent.class)
	public void AfterStartup() throws IOException {

		Encryption.setKey(key);

		ServerSocket s = new ServerSocket(PORT);
		System.out.println("Server Started");


		try {
			while (true) {
				Socket socket = s.accept();     // Blocks until a connection occurs:
				socket.setSoTimeout(10 * 1000); // Sets a timeout - if the time elapses without any data arriving the socket is closed
				try {
					new ServeOneClient(socket, clt);
					System.out.println("join a new client - total number " + clt.nCl());
				} catch (IOException e) {
					// If it fails, close the socket,
					// otherwise the thread will close it:
					socket.close();
					System.out.println("catch out");
					System.out.println(Thread.currentThread().getName());
				}
			}
		} finally {
			s.close();
			CentralCommand.terminate();
			//PostgreSQL.terminate();
		}

	}*/
}


//todo

//to build a new jar - Terminal : mvn package
//to run it - Terminal : java -jar target/springRaspberryJavaServer-0.0.1-SNAPSHOT.jar

/* Docker build
	maven tab to the side
		Lifecylce - double click Install
			this will create the homebrain.jar file as specified in pom.xml
	docker build -t docker/homebrain .
		with random name and . at the and, indicating to use the same directory
	docker images
		shows the created images and their id
	docker tag docker/homebrain localhost:5000/docker/homebrain
		tag the image for pull on nas
	docker push localhost:5000/docker/homebrain
		Push the Image to the Local Registry
	docker run -p 1994:1994 b22398153237
		testing

	truenas app
		include:
 			- /mnt/Mirror/NAS-Config/homebrain/compose.yaml
 */


//mvn package
//docker tag homebrain:9.26 nikistadnik/homebrain:latest
//docker push nikistadnik/homebrain:latest



//json parsera ot serveOneClient moje da se podobri poneje ponastoq6tem iska vun6na biblioteka
	//suotvetno da se mahne dependacito na json simple

//javax replaced by jakarta
//do i need any of the 2 json dep
