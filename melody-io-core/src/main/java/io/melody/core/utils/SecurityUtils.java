package io.melody.core.utils;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.security.crypto.bcrypt.BCrypt;

import io.jsonwebtoken.Clock;
import io.jsonwebtoken.impl.DefaultClock;
import io.melody.core.enums.InfoEnum;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SecurityUtils {

	private SecurityUtils() {
	}

	public static Map<String, List<String>> produceInfo(String info) {
		Map<String, List<String>> messages = new HashMap<String, List<String>>();
		List<String> infoItems = new ArrayList<>();
		infoItems.add(info);
		messages.put(InfoEnum.INFO.getName(), infoItems);
		return messages;
	}

	public static Map<String, Object> produceErrorAttribute(
			Map<String, Object> srcAttributes) {
		Map<String, Object> errorRet = new HashMap<>();

		errorRet.put("response_code",
				srcAttributes.get(InfoEnum.STATUS.getName()));
		errorRet.put("messages", produceInfo(java.util.Objects
				.toString(srcAttributes.get(InfoEnum.ERROR.getName()))));

		return errorRet;
	}

	public static String hashOfPassword(String password) {
		return BCrypt.hashpw(password, BCrypt.gensalt(10));
	}

	public static boolean otpExpired(long expirationDate) {
		Clock clock = DefaultClock.INSTANCE;
		final Date now = clock.now();
		Date exp = new Date(expirationDate);

		return (exp != null && now.after(exp)) ? true : false;
	}

	public static long generateOTPExpiration(long expInMinutes) {
		Long expirationTimeLong = TimeUnit.MINUTES.toSeconds(expInMinutes);
		final Date createdDate = new Date();
		final Date expirationDate = new Date(
				createdDate.getTime() + expirationTimeLong * 1000);

		return expirationDate.getTime();
	}

	public static String generateOTP(int size) {
		StringBuilder generatedToken = new StringBuilder();
		try {
			SecureRandom number = SecureRandom.getInstance("SHA1PRNG");
			// Generate 20 integers 0..20
			for (int i = 0; i < size; i++) {
				generatedToken.append(number.nextInt(9));
			}
		} catch (NoSuchAlgorithmException e) {
			log.info(e.getMessage());
		}

		return generatedToken.toString();
	}

	public static String extractTokenKey(String value) {
		if (value == null) {
			return null;
		} else {
			MessageDigest digest = null;
			try {
				digest = MessageDigest.getInstance("SHA-512");
			} catch (NoSuchAlgorithmException var5) {
				throw new IllegalStateException(
						"SHA-512 algorithm not available.  Fatal (should be in the JDK).");
			}

			try {
				byte[] e = digest.digest(value.getBytes("UTF-8"));
				return String.format("%032x",
						new Object[]{new BigInteger(1, e)});
			} catch (UnsupportedEncodingException var4) {
				throw new IllegalStateException(
						"UTF-8 encoding not available.  Fatal (should be in the JDK).");
			}
		}
	}

}
