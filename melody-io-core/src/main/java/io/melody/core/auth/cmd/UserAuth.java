package io.melody.core.auth.cmd;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;

import io.melody.core.auth.cmd.api.AddUserCommand;
import io.melody.core.auth.event.api.UserAddedEvent;

//@Aggregate(snapshotTriggerDefinition = "userSnapshotTriggerDefinition", cache = "cache")
@Aggregate
public class UserAuth {
	@AggregateIdentifier
	private String email;

	@SuppressWarnings("unused")
	private UserAuth() {
	}

	@CommandHandler
	UserAuth(AddUserCommand command) {
		apply(new UserAddedEvent(command.getEmail()));
	}
	
	@EventSourcingHandler
    void on(UserAddedEvent event) {
       this.email = event.getEmail();
    }
}
