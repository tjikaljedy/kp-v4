package io.melody.core.auth.cmd;

import java.util.UUID;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Saga
@Component
@ProcessingGroup("AuthSaga")
public class AuthSaga {
	@Autowired
	private transient CommandGateway commandGateway;
	private UUID inventoryRoomId;
}
