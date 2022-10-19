package io.melody.profile.utils;

import java.lang.reflect.Modifier;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.TimeZone;

import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class AppUtils {
	private static final Logger log = LoggerFactory.getLogger(AppUtils.class);
	public static final String DATE_PATTERN = "yyyy-MM-dd";
	public static final String GA_DATE_PATTERN = "yyyyMMdd";
	public static final String MONGO_DATE_PATTERN = "EEE MMM dd HH:mm:ss Z yyyy";
	public static final String HUMAN_DATE_PATTERN = "dd MMM yyy";
	public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
	public static final String ZONE_UTC = "UTC";
	public static final String ZONE_GMT_PLUS7 = "GMT+7:00";
	public static final String ZONE_GMT_PLUS8 = "GMT+8:00";
	public static final String RFC3339 = "yyyy-MM-dd'T'HH:mm:ssZ";

	public static String dateToDateStringFormat(Date date, String dateFormat,
			String timeZone) {
		SimpleDateFormat df = new SimpleDateFormat(dateFormat);
		df.setTimeZone(TimeZone.getTimeZone(timeZone));
		return df.format(date);
	}

	// General
	public static Gson gsonInstanceWithFilter(String[] ignorableField) {
		Gson gson = new GsonBuilder()
				.excludeFieldsWithModifiers(Modifier.STATIC)
				.addSerializationExclusionStrategy(new ExclusionStrategy() {

					@Override
					public boolean shouldSkipField(FieldAttributes f) {
						Object foundField = Arrays.asList(ignorableField)
								.stream().filter(d -> d.equals(f.getName()))
								.findFirst().orElse(null);
						return foundField != null ? true : false;
					}

					@Override
					public boolean shouldSkipClass(Class<?> clazz) {
						// TODO Auto-generated method stub
						return false;
					}

				}).addDeserializationExclusionStrategy(new ExclusionStrategy() {
					@Override
					public boolean shouldSkipField(FieldAttributes f) {
						Object foundField = Arrays.asList(ignorableField)
								.stream().filter(d -> d.equals(f.getName()))
								.findFirst().orElse(null);
						return foundField != null ? true : false;
					}

					@Override
					public boolean shouldSkipClass(Class<?> aClass) {
						return false;
					}
				}).create();
		return gson;
	}

	public static Gson gsonInstance() {
		Gson gson = new GsonBuilder().excludeFieldsWithModifiers(Modifier.FINAL,
				Modifier.TRANSIENT, Modifier.STATIC).create();
		return gson;
	}

	public static String getAsStr(org.json.simple.JSONObject obj, String key) {
		return String.valueOf(obj.get(key));
	}

	public static String getAsStr(String rawObj, String key) {
		Gson gson = gsonInstance();
		org.json.simple.JSONObject obj = gson.fromJson(rawObj,
				org.json.simple.JSONObject.class);
		return String.valueOf(obj.get(key));
	}

	public static int getAsInt(org.json.simple.JSONObject obj, String key) {
		String s = getAsStr(obj, key);
		s = s.replaceFirst("\\.0*$|(\\.\\d*?)0+$", "$1");
		return NumberUtils.toInt(s);
	}

	public static long getAsLong(org.json.simple.JSONObject obj, String key) {
		String s = getAsStr(obj, key);
		s = s.replaceFirst("\\.0*$|(\\.\\d*?)0+$", "$1");
		return NumberUtils.toLong(s);
	}

	public static double getAsDouble(org.json.simple.JSONObject obj,
			String key) {
		return NumberUtils.toDouble(getAsStr(obj, key), 0);
	}

}
