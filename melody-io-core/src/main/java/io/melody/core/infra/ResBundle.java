package io.melody.core.infra;

import java.util.ResourceBundle;

import dev.akkinoc.util.YamlResourceBundle;

public class ResBundle {
	private static volatile ResBundle resBundle;
	private static final Object lock = new Object();

	private ResourceBundle bundle;

	// Activity
	public static final String AC_ERR_DEFAULT = "activity.AC_ERR_DEFAULT";
	public static final String AC_ERR_UNAUTHORIZED = "activity.AC_ERR_UNAUTHORIZED";
	public static final String AC_ERR_REDUNDANCE = "activity.AC_ERR_REDUNDANCE";
	public static final String AC_ERR_USER_NOT_FOUND = "activity.AC_ERR_USER_NOT_FOUND";
	public static final String AC_ERR_MAX_OTP_ATTEMPT = "activity.AC_ERR_MAX_OTP_ATTEMPT";
	public static final String AC_ERR_OTP = "activity.AC_ERR_OTP";
	public static final String AC_ERR_LOGIN = "activity.AC_ERR_LOGIN";
	public static final String AC_ERR_TOKEN = "activity.AC_ERR_TOKEN";
	public static final String AC_ERR_TOKEN_VALID = "activity.AC_ERR_TOKEN_VALID";
	public static final String AC_ERR_CHANGE_PASS = "activity.AC_ERR_CHANGE_PASS";
	public static final String AC_ERR_LOGIN_EXIST = "activity.AC_ERR_LOGIN_EXIST";
	public static final String AC_ERR_GRANT_PREMIUM = "activity.AC_ERR_GRANT_PREMIUM";

	public static final String PR_DEFAULT_SUCCESS = "profile.PR_DEFAULT_SUCCESS";
	public static final String PR_SIGNUP_SUCCESS = "profile.PR_SIGNUP_SUCCESS";
	public static final String PR_OTP_SUCCESS = "profile.PR_OTP_SUCCESS";
	public static final String PR_RECOVER_PASS_SUCCESS = "profile.PR_RECOVER_PASS_SUCCESS";
	public static final String PR_UPDATE_PROFILE_SUCCESS = "profile.PR_UPDATE_PROFILE_SUCCESS";
	public static final String PR_CHANGE_PASS_SUCCESS = "profile.PR_CHANGE_PASS_SUCCESS";
	public static final String PR_LOGIN_SUCCESS = "profile.PR_LOGIN_SUCCESS";
	public static final String PR_LOGOUT_SUCCESS = "profile.PR_LOGOUT_SUCCESS";
	public static final String PR_TOKEN_SUCCESS = "profile.PR_TOKEN_SUCCESS";
	public static final String PR_DEVICE_ACTIVITY_SUCCESS = "profile.PR_DEVICE_ACTIVITY_SUCCESS";

	public static final String PR_GRANT_PREMIUM_SUCCESS = "profile.PR_GRANT_PREMIUM_SUCCESS";
	public static final String PR_GRANT_PREMIUM_REJECT = "profile.PR_GRANT_PREMIUM_REJECT";

	public static final String PR_PROFILE_INTEREST_SUCCESS = "profile.PR_PROFILE_INTEREST_SUCCESS";

	public static synchronized ResBundle instance() {
		ResBundle r = resBundle;
		if (r == null) {
			synchronized (lock) {
				r = resBundle;
				if (r == null) {
					ResourceBundle bundleInstance = ResourceBundle.getBundle("application_res",
							YamlResourceBundle.Control.INSTANCE);
					r = new ResBundle(bundleInstance);
				}
			}
		}
		return r;
	}

	public ResBundle(ResourceBundle bundle) {
		this.bundle = bundle;
	}

	public String bundleAsStr(String key) {
		return this.bundle.getString(key);
	}

}
