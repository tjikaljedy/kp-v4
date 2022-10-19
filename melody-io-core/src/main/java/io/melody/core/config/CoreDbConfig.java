package io.melody.core.config;

import org.axonframework.config.EventProcessingConfigurer;
import org.axonframework.eventsourcing.eventstore.EmbeddedEventStore;
import org.axonframework.eventsourcing.eventstore.EventStorageEngine;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.extensions.mongo.eventsourcing.eventstore.MongoEventStorageEngine;
import org.axonframework.modelling.saga.repository.SagaStore;
import org.axonframework.serialization.Serializer;
import org.axonframework.serialization.json.JacksonSerializer;
import org.axonframework.spring.config.AxonConfiguration;
import org.jongo.Jongo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mongodb.ConnectionString;

@Configuration
public class CoreDbConfig {
	@Value("${cqrs-db.core-host}")
	private String coreHost;
	@Value("${cqrs-db.core-port}")
	private int corePort;
	@Value("${cqrs-db.core-database}")
	private String coreDb;
	private final String PREFIX_DB = "mongodb://";

	@Autowired
	public void configure(EventProcessingConfigurer configurer) {
		configurer.usingSubscribingEventProcessors();
	}
	
	@Bean(name = "coreClient")
	public com.mongodb.client.MongoClient coreClient() {
		return com.mongodb.client.MongoClients.create(
				new ConnectionString(PREFIX_DB + coreHost + ":" + corePort));
	}
	
	@Bean(name = "jongoClient")
	public com.mongodb.MongoClient jongoClient() {
		return new com.mongodb.MongoClient(coreHost, corePort);
	}
	
	//Jongo
	@SuppressWarnings("deprecation")
	@Bean(name = "jongoCore")
	public Jongo jongoTrans(@Qualifier("jongoClient") final com.mongodb.MongoClient client) {
		final com.mongodb.DB database = client.getDB(coreDb);
		return new Jongo(database);
	}

	// MongoDB Axon
	@Bean(name = "cqrsTemplate")
	public org.axonframework.extensions.mongo.MongoTemplate cqrsTemplate(
			@Qualifier("coreClient") final com.mongodb.client.MongoClient mongoClient) {
		return org.axonframework.extensions.mongo.DefaultMongoTemplate.builder()
				.mongoDatabase(mongoClient, coreDb).build();
	}

	@Bean
	public EmbeddedEventStore eventStore(EventStorageEngine storageEngine,
			AxonConfiguration configuration) {
		return EmbeddedEventStore.builder().storageEngine(storageEngine)
				.messageMonitor(configuration.messageMonitor(EventStore.class,
						"eventStore"))
				.build();
	}

	@Bean
	public EventStorageEngine storageEngine(
			@Qualifier("cqrsTemplate") final org.axonframework.extensions.mongo.MongoTemplate cqrsTemplate) {
		return MongoEventStorageEngine.builder().mongoTemplate(cqrsTemplate)
				.eventSerializer(JacksonSerializer.defaultSerializer())
				.snapshotSerializer(JacksonSerializer.defaultSerializer())
				.build();
	}

	@Bean
	public SagaStore<Object> coreSaga(
			@Qualifier("cqrsTemplate") final org.axonframework.extensions.mongo.MongoTemplate cqrsTemplate,
			@Qualifier("eventSerializer") final Serializer eventSerializer) {
		return org.axonframework.extensions.mongo.eventhandling.saga.repository.MongoSagaStore
				.builder().mongoTemplate(cqrsTemplate)
				.serializer(eventSerializer).build();

	}

}
