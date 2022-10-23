package io.melody.core.infra.provider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.jongo.Jongo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;

import io.melody.core.infra.entity.ValidatorEntity;
import io.melody.core.utils.AppUtils;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CoreProvider {
	@Autowired
	@Qualifier("jongoCore")
	private Jongo jongoCore;

	private transient ObjectMapper mapper;
	private transient Gson gson;
	private transient Map<String, JsonSchema> jsonSchemas;

	@Value("${core-config.collections.template}")
	private String COLL_TEMPLATES;
	@Value("${core-config.collections.core}")
	private String COLL_CORE_VALUES;

	private final String ERR_FIELD_MSG = "ERROR";

	@PostConstruct
	public void init() {
		mapper = new ObjectMapper().configure(
				DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		gson = AppUtils.gsonInstance();
		this.jsonSchemaLoad();
	}

	public void jsonSchemaLoad() {
		jsonSchemas = new HashMap<String, JsonSchema>();
		JsonSchemaFactory factory = JsonSchemaFactory
				.getInstance(SpecVersion.VersionFlag.V201909);

		org.jongo.MongoCollection collection = jongoCore
				.getCollection(COLL_CORE_VALUES);
		org.jongo.MongoCursor<ValidatorEntity> items = collection
				.find(ValidatorEntity.QRY).as(ValidatorEntity.class);
		while (items.hasNext()) {
			ValidatorEntity item = items.next();
			JsonSchema jsonSchema = factory.getSchema(item.getJsonSchema());
			jsonSchemas.put(item.getEvent(), jsonSchema);
		}

	}

	@SuppressWarnings("unchecked")
	public org.json.simple.JSONObject validationDto(String event, Object dto) {
		org.json.simple.JSONObject dtoAsJson = gson.fromJson(gson.toJson(dto),
				org.json.simple.JSONObject.class);
		org.json.simple.JSONObject retValue = new org.json.simple.JSONObject();
		List<String> errorMessage = new ArrayList<String>();

		try {
			JsonNode node = mapper.readTree(dtoAsJson.toJSONString());
			Set<com.networknt.schema.ValidationMessage> messages = this.jsonSchemas
					.get(event).validate(node);
			messages.forEach(message -> {
				errorMessage.add(message.getMessage());
			});
		} catch (Exception e) {
			errorMessage.add(e.getMessage());
			log.info(e.getMessage());
		}
		if (!org.apache.commons.collections.CollectionUtils
				.isEmpty(errorMessage)) {
			retValue.put(ERR_FIELD_MSG, errorMessage);
		}
		return retValue;
	}

	
}
