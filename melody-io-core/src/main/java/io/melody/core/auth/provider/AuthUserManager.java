package io.melody.core.auth.provider;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.melody.core.activity.enums.EventEnrol;
import io.melody.core.auth.AuthDto;
import io.melody.core.auth.entity.OAuthConfigEntity;
import io.melody.core.auth.entity.UserAuthEntity;
import io.melody.core.auth.repo.OAuthConfigRepo;
import io.melody.core.auth.repo.RefreshTokenRepo;
import io.melody.core.auth.repo.UserAuthRepo;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class AuthUserManager implements ReactiveUserDetailsService {
	@Autowired
	private transient UserAuthRepo userAuthRepo;
	@Autowired
	private transient OAuthConfigRepo oauthConfigRepo;
	@Autowired
	private transient RefreshTokenRepo refreshTokenRepo;
	
	@Override
	public Mono<UserDetails> findByUsername(String username) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Business Logic Checkpoint
	 * 
	 * @param inDto
	 * @return
	 */
	public Mono<AuthDto> loginCheckpoint(AuthDto inDto) {
		final String type = EventEnrol.LOGIN.getName();
		return Mono.just(inDto);
		//@formatter:off
		//UserProfile profile = this.findProfileByDto(inDto);
		//io.melody.core.domain.JwtRefreshToken token = this.findRefreshTokenByEmail(inDto.getEmail());
		//return ((profile != null && profile.isAuthVerified() && token != null)
		//		&& profile.getUserAuth().isValidPassword(inDto.getPassword()))
		//				? this.initalLogin(inDto, profile)
		//				: Mono.just(this.fatalErrorActivity(type,
		//						ResBundle.instance().bundleAsStr(ResBundle.AC_ERR_USER_NOT_FOUND)));
		//@formatter:on
	}
	
	/**
	 * Sign-up Checkpoint Process
	 * 
	 * @param inDto
	 * @return
	 */
	public Mono<AuthDto> signupCheckpoint(AuthDto inDto) {
		final String type = EventEnrol.ENROL.getName();
		return Mono.empty();
		//@formatter:off
		//boolean isNotInitial = !(inDto.getEvent().equals(EventEnrol.SIGN_UP.getName())
		//		&& inDto.getStatus().equals(StatusEnrol.INITIAL.getName()));
		//UserProfile profile = this.findProfileByDto(inDto);

		//return profile != null && profile.getUserAuth() != null && isNotInitial
		//		? activityService.obtainParentEvent(profile.getUserAuth().getEmail(), type)
		//				.filter(parent -> parent != null)
		//				.flatMap(parent -> this.signupVerifyReply(inDto, profile, parent))
		//		: initialSignup(inDto);
		//@formatter:on
	}
	
	/**
	 * Login Related
	 * 
	 * @param email
	 * @return
	 */
	public Mono<UserAuthEntity> obtainUserAuthByEmailMono(String email) {
		return this.findUserAuthByEmail(email);
	}

	public UserAuthEntity obtainUserAuthByEmail(String email) {
		List<UserAuthEntity> aList = userAuthRepo.findByEmail(email);
		return CollectionUtils.isNotEmpty(aList) ? aList.get(0) : null;
	}
	
	public Mono<UserAuthEntity> obtainUserAuthByMobileMono(String mobile) {
		return this.findUserAuthByMobile(mobile);
	}

	public UserAuthEntity obtainUserAuthByMobile(String mobile) {
		List<UserAuthEntity> aList = userAuthRepo.findByMobile(mobile);
		return CollectionUtils.isNotEmpty(aList) ? aList.get(0) : null;
	}
	
	public OAuthConfigEntity obtainClientOAuth(String appName) {
		List<OAuthConfigEntity> aList = oauthConfigRepo
				.findClienByApplicationName(appName);
		return !CollectionUtils.isEmpty(aList) ? aList.get(0) : null;
	}
	
	
	/**
	 * Private
	 */
	private Mono<UserAuthEntity> findUserAuthByEmail(String email) {
		List<UserAuthEntity> aList = userAuthRepo.findByEmail(email);
		return Mono.justOrEmpty(
				!CollectionUtils.isEmpty(aList) ? aList.get(0) : null);
	}
	
	private Mono<UserAuthEntity> findUserAuthByMobile(String mobile) {
		List<UserAuthEntity> aList = userAuthRepo.findByMobile(mobile);
		return Mono.justOrEmpty(
				!CollectionUtils.isEmpty(aList) ? aList.get(0) : null);
	}




}
