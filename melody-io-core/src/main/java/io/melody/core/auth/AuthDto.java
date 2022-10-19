package io.melody.core.auth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.springframework.data.annotation.Transient;

import io.melody.core.activity.ActivityDto;
import io.melody.core.enums.InfoEnum;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class AuthDto {
	private String email;
	private String mobile;
	private String username;
	private String password;
	private String token;
	private String refreshToken;
	private String deviceId;
	private String type;
	private ActivityDto activity;
	
	@Setter(AccessLevel.NONE)
	@Getter(AccessLevel.NONE)
	private Map<String, List<String>> messages;

	@Transient
	private transient String accessToken;
	@Transient
	private transient String refeshToken;
	@Transient
	private transient org.json.simple.JSONObject profile;
	
	public AuthDto() {

	}

	public AuthDto(String email) {
		this.email = email;
	}

	public Map<String, List<String>> getMessages() {
		if (MapUtils.isEmpty(messages)) {
			messages = new HashMap<String, List<String>>();
		}

		return messages;
	}

	public void addingInfo(String info) {
		if (!this.getMessages().containsKey(InfoEnum.INFO.getName())) {
			List<String> infoItems = new ArrayList<String>();
			infoItems.add(info);
			this.getMessages().put(InfoEnum.INFO.getName(), infoItems);
		} else {
			this.getMessages().get(InfoEnum.INFO.getName()).add(info);
		}
	}

}
