package io.melody.core.auth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import javax.annotation.PostConstruct;

import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import io.melody.core.activity.ActivityDto;
import io.melody.core.auth.provider.AuthUserManager;
import io.melody.core.enums.InfoEnum;
import io.melody.core.infra.ResBundle;
import io.melody.core.pubsub.ProfileService;
import io.melody.core.utils.AppUtils;
import reactor.core.publisher.Mono;

@Component
public class AuthSlice {
	@Autowired
	private transient AuthUserManager authProvider;

	@Autowired
	private transient ProfileService profileService;
	private transient Gson gson;

	@PostConstruct
	public void init() {
		gson = AppUtils.gsonInstance();
	}

	public Mono<org.json.simple.JSONObject> authorizeLogin(AuthDto inDto) {
		return authProvider.loginCheckpoint(inDto).flatMap(authDto->{ 
			return profileService.verityProfile(authDto).flatMap(out->{
				return loginSuccess(authDto);
			}).switchIfEmpty(Mono.defer(() -> 
				Mono.fromFuture(CompletableFuture.supplyAsync(() -> {
					return loginFail(inDto);
				}))
			));
		});
	}

	public Mono<org.json.simple.JSONObject> authorizeSignup(AuthDto inDto) {
		// @formatter:off
		return authProvider.signupCheckpoint(inDto).flatMap(authDto->{
			return signupSuccess(authDto);
		}).switchIfEmpty(Mono.defer(() -> 
			Mono.fromFuture(CompletableFuture.supplyAsync(() -> {
				return verifyAccountFail(inDto);
			}))
		));
		// @formatter:on
	}

	@SuppressWarnings("unchecked")
	public Mono<org.json.simple.JSONObject> loginSuccess(AuthDto dto) {

		final int retHttpStatus = (dto.getActivity() != null
				&& dto.getActivity().isValidActivity())
						? HttpStatus.ACCEPTED.value()
						: HttpStatus.NOT_ACCEPTABLE.value();
		org.json.simple.JSONObject retVal = new org.json.simple.JSONObject();
		retVal.put("response_code", retHttpStatus);
		retVal.put("messages", dto.getMessages());

		if (dto.getActivity() != null && dto.getActivity().isValidActivity()) {
			org.json.simple.JSONObject data = dto.getProfile();
			data.put("token", "Bearer " + dto.getAccessToken());
			data.put("refreshToken", dto.getRefeshToken());
			retVal.put("data", data);
		}
		return Mono.justOrEmpty(retVal);
	}

	// TODO
	@SuppressWarnings("unchecked")
	public Mono<org.json.simple.JSONObject> signupSuccess(AuthDto dto) {

		final int retHttpStatus = (dto.getActivity() != null
				&& dto.getActivity().isValidActivity())
						? HttpStatus.ACCEPTED.value()
						: HttpStatus.NOT_ACCEPTABLE.value();

		org.json.simple.JSONObject retVal = new org.json.simple.JSONObject();
		retVal.put("response_code", retHttpStatus);

		if (dto.getActivity().isValidActivity()
		/*
		 * &&
		 * dto.getUserProfile().getStatus().equals(StatusEnrol.NOTIFY.getName())
		 */) {
			org.json.simple.JSONObject data = dto.getProfile();
			retVal.put("data", data);
		}
		retVal.put("messages", dto.getMessages());
		return Mono.justOrEmpty(retVal);
	}

	// Common
	public void buildErrorInfo(AuthDto inDto, String activityType,
			String responseCode) {
		ActivityDto activity = new ActivityDto(activityType);
		inDto.setActivity(activity);

		if (org.apache.commons.lang3.StringUtils.isNoneBlank(responseCode))
			inDto.addingInfo(responseCode);

	}

	public Map<String, List<String>> produceInfo(String info) {
		Map<String, List<String>> messages = new HashMap<String, List<String>>();
		List<String> infoItems = new ArrayList<String>();
		infoItems.add(info);
		messages.put(InfoEnum.INFO.getName(), infoItems);
		return messages;
	}

	@SuppressWarnings("unchecked")
	public org.json.simple.JSONObject loginFail(AuthDto dto) {
		org.json.simple.JSONObject retVal = new org.json.simple.JSONObject();
		retVal.put("messages",
				(dto != null && MapUtils.isNotEmpty(dto.getMessages())
						? dto.getMessages()
						: produceInfo(ResBundle.instance()
								.bundleAsStr(ResBundle.AC_ERR_DEFAULT))));
		retVal.put("response_code", HttpStatus.NOT_ACCEPTABLE.value());
		return retVal;
	}

	@SuppressWarnings("unchecked")
	public org.json.simple.JSONObject verifyAccountFail(AuthDto dto) {
		org.json.simple.JSONObject retVal = new org.json.simple.JSONObject();
		retVal.put("messages",
				(dto != null && MapUtils.isNotEmpty(dto.getMessages())
						? dto.getMessages()
						: produceInfo(ResBundle.instance()
								.bundleAsStr(ResBundle.AC_ERR_DEFAULT))));
		retVal.put("response_code", HttpStatus.NOT_ACCEPTABLE.value());
		return retVal;
	}

	// All Fail or no need to proceed
	@SuppressWarnings("unchecked")
	public Mono<org.json.simple.JSONObject> unexpectedFail(
			org.json.simple.JSONObject messages) {
		org.json.simple.JSONObject retVal = new org.json.simple.JSONObject();
		// retVal.put("messages", messages);
		retVal.put("messages", produceInfo(
				ResBundle.instance().bundleAsStr(ResBundle.AC_ERR_DEFAULT)));
		retVal.put("response_code", HttpStatus.NOT_ACCEPTABLE.value());
		return Mono.justOrEmpty(retVal);
	}

	@SuppressWarnings("unchecked")
	public ResponseEntity<org.json.simple.JSONObject> unexpectedFail(
			AuthDto dto) {
		org.json.simple.JSONObject retVal = new org.json.simple.JSONObject();
		retVal.put("messages",
				(dto != null && MapUtils.isNotEmpty(dto.getMessages())
						? dto.getMessages()
						: produceInfo(ResBundle.instance()
								.bundleAsStr(ResBundle.AC_ERR_DEFAULT))));
		retVal.put("response_code", HttpStatus.NOT_ACCEPTABLE.value());
		return new ResponseEntity<org.json.simple.JSONObject>(retVal,
				HttpStatus.NOT_ACCEPTABLE);
	}

	/**
	 * No JWT filter validated previously
	 * 
	 * @param message
	 * @return
	 */
	public Mono<AuthDto> consume(org.json.simple.JSONObject message) {
		AuthDto userAuthDto = null;
		if (!message.isEmpty()) {
			userAuthDto = gson.fromJson(message.toJSONString(), AuthDto.class);
		}
		return Mono.just(userAuthDto);
	}

}
