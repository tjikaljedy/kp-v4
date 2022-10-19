package io.melody.core.infra.entity;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "corevalues")
public class CoreEntity {

}
