package io.melody.core;

import java.util.concurrent.TimeUnit;

import javax.annotation.PreDestroy;

import org.camunda.bpm.BpmPlatform;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveUserDetailsServiceAutoConfiguration;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.EventListener;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
@EnableProcessApplication
@ComponentScan({"io.melody.core"})
@EnableAutoConfiguration(exclude = {
		ReactiveUserDetailsServiceAutoConfiguration.class})
public class MelodyCore {

	public static void main(String[] args) {
		SpringApplication.run(MelodyCore.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void readyToUse(ApplicationReadyEvent event) {
		log.info(">>> MIDWARE STARTUP <<<");
		try {
			TimeUnit.SECONDS.sleep(1);

		} catch (Exception e) {
			log.warn(">>> FAIL <<<");
		}
	}

	@PreDestroy
	public void onDestroy() throws Exception {
		log.info(">>> MIDWARE SHUTDOWN <<<");

	}

}
