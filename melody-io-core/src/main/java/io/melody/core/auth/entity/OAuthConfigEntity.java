package io.melody.core.auth.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.security.core.GrantedAuthority;

import lombok.Data;

@Data
@Document(collection = "oauthconfig")
public class OAuthConfigEntity implements Serializable {

	private static final long serialVersionUID = 9044542586437482856L;
	@Id
	private Long _id;
	@Field("oauthProvider")
	private String oauthProvider;

	@Field("applicationName")
	private String applicationName;

	@Field("additionalInformation")
	private String additionalInformation;

	@Field("autoApprove")
	private boolean autoApprove;

	@Field("resourcesIds")
	private List<String> resourcesIds;

	@Field("authorizedGrantTypes")
	private List<String> authorizedGrantTypes;

	@Field("refreshTokenValidity")
	private int refreshTokenValidity;

	@Field("accessTokenValidity")
	private int accessTokenValidity;

	@Field("authorities")
	private List<String> authorities;

	@Field("scopes")
	private List<String> scopes;

	@Field("redirectUri")
	private String redirectUri;

	@Field("tokenUri")
	private String tokenUri;

	@Field("checkTokenUri")
	private String checkTokenUri;

	@Field("secretRequired")
	private boolean secretRequired;

	@Field("clientId")
	private String clientId;

	@Field("clientSecret")
	private String clientSecret;

	@Field("clientSecretJson")
	private String clientSecretJson;

	@Field("serviceClientId")
	private String serviceClientId;

	@Field("serviceAccountUser")
	private String serviceAccountUser;

	@Field("serviceAccountP12")
	private String serviceAccountPrivate;

	@Field("serviceAccountEmail")
	private String serviceAccountEmail;
	
	@Field("databaseUri")
	private String databaseUri;
	
	public OAuthConfigEntity() {
	}
	
	public List<String> fromGrantedAuthorities(Collection<GrantedAuthority> grantedList) {
		List<String> aList = new ArrayList<String>();
		for (GrantedAuthority grandted : grantedList) {
			aList.add(grandted.getAuthority());
		}

		return aList;

	}

	public String fromAdditionalInformation(Map<String, Object> infos) {
		StringBuilder additionalInfos = new StringBuilder();

		for (Map.Entry<String, Object> entry : infos.entrySet()) {

			additionalInfos.append(ToStringBuilder.reflectionToString(entry.getValue()));
		}

		return additionalInfos.toString();

	}
}
