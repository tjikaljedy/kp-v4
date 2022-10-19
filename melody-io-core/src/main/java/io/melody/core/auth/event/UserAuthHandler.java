package io.melody.core.auth.event;

import java.util.List;
import java.util.stream.Collectors;

import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.springframework.stereotype.Component;

import io.melody.core.auth.AuthDto;
import io.melody.core.auth.entity.UserAuthEntity;
import io.melody.core.auth.event.api.FindUserAuth;
import io.melody.core.auth.event.api.UserAddedEvent;
import io.melody.core.auth.event.api.UserLoginEvent;
import io.melody.core.auth.repo.UserAuthRepo;

@Component
@ProcessingGroup("user")
public class UserAuthHandler {
	private final UserAuthRepo userAuthRepo;
	private final QueryUpdateEmitter queryUpdateEmitter;

	UserAuthHandler(UserAuthRepo userAuthRepo,
			QueryUpdateEmitter queryUpdateEmitter) {
		this.userAuthRepo = userAuthRepo;
		this.queryUpdateEmitter = queryUpdateEmitter;
	}

	@EventHandler
	void on(UserLoginEvent event) {
		System.out.println(">>>>>>> EVENT");
		UserAuthEntity entity = new UserAuthEntity();
		this.userAuthRepo.save(entity);
		/*
		 * sending it to subscription queries of type FindPayment, but only if
		 * the payment id matches.
		 */
		queryUpdateEmitter.emit(FindUserAuth.class,
				query -> query.getEmail().equals(event.getEmail()),
				convert(entity));
	}

	// @QueryHandler
	// UserAuthDto handle(FindUserAuth query) {
	// System.out.println(">>>>>>>> HERE mE");
	/// return convert(userAuthRepo.getOne(query.getEmail()));
	// }

	@EventHandler
	void on(UserAddedEvent event) {
		UserAuthEntity entity = new UserAuthEntity();
		this.userAuthRepo.save(entity);

		queryUpdateEmitter.emit(FindUserAuth.class,
				query -> query.getEmail().equals(event.getEmail()),
				convert(entity));
	}

	@QueryHandler
	List<AuthDto> handle(FindUserAuth query) {
		return userAuthRepo.findAll().stream().map(this::convert)
				.collect(Collectors.toList());
	}

	// Convert to Entity -> DTO
	private AuthDto convert(UserAuthEntity entity) {
		return new AuthDto(entity.getEmail());
	}
}
