package io.melody.core.auth.provider;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.apache.commons.lang3.StringUtils;
import io.melody.core.activity.ActivityDto;
import io.melody.core.activity.entity.ActivityEventEntity;
import io.melody.core.activity.enums.EventEnrol;
import io.melody.core.activity.enums.StatusEnrol;
import io.melody.core.activity.provider.ActivityProvider;
import io.melody.core.auth.AuthDto;
import io.melody.core.auth.entity.JwtRefreshTokenEntity;
import io.melody.core.auth.entity.OAuthConfigEntity;
import io.melody.core.auth.entity.UserAuthEntity;
import io.melody.core.auth.entity.UserProfileEntity;
import io.melody.core.auth.repo.OAuthConfigRepo;
import io.melody.core.auth.repo.RefreshTokenRepo;
import io.melody.core.auth.repo.UserAuthRepo;
import io.melody.core.auth.repo.UserProfileRepo;
import io.melody.core.infra.ResBundle;
import reactor.core.publisher.Mono;

@Component
public class AuthUserManager implements ReactiveUserDetailsService {
	@Autowired
	private transient UserAuthRepo userAuthRepo;
	@Autowired
	private transient UserProfileRepo profileRepo;
	@Autowired
	private transient OAuthConfigRepo oauthConfigRepo;
	@Autowired
	private transient RefreshTokenRepo refreshTokenRepo;
	@Autowired
	private transient ActivityProvider activityProvider;

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

		UserProfileEntity profile = this.findProfileByDto(inDto);
		JwtRefreshTokenEntity token = this.findRefreshTokenByEmail(inDto.getEmail());
		return ((profile != null && profile.isAuthVerified() && token != null)
				&& profile.getUserAuth().isValidPassword(inDto.getPassword()))
						? this.initalLogin(inDto, profile)
						: Mono.just(this.fatalErrorActivity(type,
								ResBundle.instance().bundleAsStr(ResBundle.AC_ERR_USER_NOT_FOUND)));

	}

	/**
	 * Sign-up Checkpoint Process
	 * 
	 * @param inDto
	 * @return
	 */
	public Mono<AuthDto> signupCheckpoint(AuthDto inDto) {
		final String type = EventEnrol.ENROL.getName();

		boolean isNotInitial = !(inDto.getEvent().equals(EventEnrol.SIGN_UP.getName())
				&& inDto.getStatus().equals(StatusEnrol.INITIAL.getName()));
		UserProfileEntity profile = this.findProfileByDto(inDto);

		return profile != null && profile.getUserAuth() != null && isNotInitial
				? activityProvider.obtainParentEvent(profile.getUserAuth().getEmail(), type)
						.filter(parent -> parent != null)
						.flatMap(parent -> this.signupVerifyReply(inDto, profile, parent))
				: initialSignup(inDto);

	}

	private Mono<AuthDto> initalLogin(AuthDto inDto, UserProfileEntity profile) {

		return Mono.just(inDto);
	}

	private Mono<AuthDto> initialSignup(AuthDto inDto) {
		return Mono.just(inDto);
	}

	private Mono<AuthDto> signupVerifyReply(AuthDto inDto, UserProfileEntity profile, ActivityEventEntity parent) {
		return Mono.just(inDto);
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

	private UserProfileEntity findProfileByDto(AuthDto inDto) {
		List<UserProfileEntity> aList = profileRepo.findByProfileEmail(inDto.getEmail());
		return CollectionUtils.isNotEmpty(aList) ? aList.get(0) : null;
	}

	private JwtRefreshTokenEntity findRefreshTokenByEmail(String email) {
		List<JwtRefreshTokenEntity> aList = refreshTokenRepo.findByUserEmail(email);
		return !CollectionUtils.isEmpty(aList) ? aList.get(0) : null;
	}

	private AuthDto fatalErrorActivity(String type, String responseCode) {
		AuthDto inDto = new AuthDto();
		ActivityDto activity = new ActivityDto(type);
		activity.setValidActivity(false);
		inDto.setActivity(activity);
		if (StringUtils.isNoneBlank(responseCode))
			inDto.addingInfo(responseCode);

		return inDto;
	}

}
