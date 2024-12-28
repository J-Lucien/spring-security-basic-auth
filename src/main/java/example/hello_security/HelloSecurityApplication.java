package example.hello_security;

import example.hello_security.ip.FailedAttempt;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.concurrent.ConcurrentHashMap;

@SpringBootApplication
public class HelloSecurityApplication {

	public static void main(String[] args) {
		SpringApplication.run(HelloSecurityApplication.class, args);
	}

//	@Bean
//	public PasswordEncoder passwordEncoder() {
//		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
//	}

	@Bean(name = "attemptConcurrentHashMap")
	public ConcurrentHashMap<String, FailedAttempt> attemptConcurrentHashMap() {
		return new ConcurrentHashMap<>();
	}
}
