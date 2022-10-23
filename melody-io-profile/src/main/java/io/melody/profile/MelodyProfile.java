package io.melody.profile;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveUserDetailsServiceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"io.melody.profile"})
@EnableAutoConfiguration(exclude = {
		ReactiveUserDetailsServiceAutoConfiguration.class})
public class MelodyProfile {

	public static void main(String[] args) {
		SpringApplication.run(MelodyProfile.class, args);
	}

}
