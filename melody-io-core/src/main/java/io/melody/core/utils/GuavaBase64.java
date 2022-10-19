package io.melody.core.utils;

import org.apache.commons.codec.binary.StringUtils;

import com.google.common.io.BaseEncoding;
import com.google.common.io.BaseEncoding.DecodingException;

public class GuavaBase64 {

	/**
	 * Encodes binary data using the base64 algorithm but does not chunk the output.
	 *
	 * @param binaryData binary data to encode or {@code null} for {@code null}
	 *                   result
	 * @return byte[] containing Base64 characters in their UTF-8 representation or
	 *         {@code null} for {@code null} input
	 */
	public static byte[] encodeBase64(byte[] binaryData) {
		return StringUtils.getBytesUtf8(encodeBase64String(binaryData));
	}

	/**
	 * Encodes binary data using the base64 algorithm but does not chunk the output.
	 *
	 * @param binaryData binary data to encode or {@code null} for {@code null}
	 *                   result
	 * @return String containing Base64 characters or {@code null} for {@code null}
	 *         input
	 */
	public static String encodeBase64String(byte[] binaryData) {
		if (binaryData == null) {
			return null;
		}
		return BaseEncoding.base64().encode(binaryData);
	}

	/**
	 * Encodes binary data using a URL-safe variation of the base64 algorithm but
	 * does not chunk the output. The url-safe variation emits - and _ instead of +
	 * and / characters.
	 *
	 * @param binaryData binary data to encode or {@code null} for {@code null}
	 *                   result
	 * @return byte[] containing Base64 characters in their UTF-8 representation or
	 *         {@code null} for {@code null} input
	 */
	public static byte[] encodeBase64URLSafe(byte[] binaryData) {
		return StringUtils.getBytesUtf8(encodeBase64URLSafeString(binaryData));
	}

	/**
	 * Encodes binary data using a URL-safe variation of the base64 algorithm but
	 * does not chunk the output. The url-safe variation emits - and _ instead of +
	 * and / characters.
	 *
	 * @param binaryData binary data to encode or {@code null} for {@code null}
	 *                   result
	 * @return String containing Base64 characters or {@code null} for {@code null}
	 *         input
	 */
	public static String encodeBase64URLSafeString(byte[] binaryData) {
		if (binaryData == null) {
			return null;
		}
		return BaseEncoding.base64Url().omitPadding().encode(binaryData);
	}

	/**
	 * Decodes Base64 data into octets. Note that this method handles both URL-safe
	 * and non-URL-safe base 64 encoded inputs.
	 *
	 * @param base64Data Byte array containing Base64 data or {@code null} for
	 *                   {@code null} result
	 * @return Array containing decoded data or {@code null} for {@code null} input
	 */
	public static byte[] decodeBase64(byte[] base64Data) {
		return decodeBase64(StringUtils.newStringUtf8(base64Data));
	}

	/**
	 * Decodes a Base64 String into octets. Note that this method handles both
	 * URL-safe and non-URL-safe base 64 encoded strings.
	 *
	 * @param base64String String containing Base64 data or {@code null} for
	 *                     {@code null} result
	 * @return Array containing decoded data or {@code null} for {@code null} input
	 */
	public static byte[] decodeBase64(String base64String) {
		if (base64String == null) {
			return null;
		}
		try {
			return BaseEncoding.base64().decode(base64String);
		} catch (IllegalArgumentException e) {
			if (e.getCause() instanceof DecodingException) {
				return BaseEncoding.base64Url().decode(base64String.trim());
			}
			throw e;
		}
	}

	private GuavaBase64() {
	}
}
