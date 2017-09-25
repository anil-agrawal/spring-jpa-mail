//-----------------------------------------------------------------------------------------------------------
//					ORGANIZATION NAME
//Group							: Common - Project
//Product / Project				: spring-jpa-mail
//Module						: spring-jpa-mail
//Package Name					: com.san.util
//File Name						: DateTimeUtil.java
//Author						: anil
//Contact						: anilagrawal038@gmail.com
//Date written (DD MMM YYYY)	: 22-Sep-2017 10:11:29 PM
//Description					:  
//-----------------------------------------------------------------------------------------------------------
//					CHANGE HISTORY
//-----------------------------------------------------------------------------------------------------------
//Date			Change By		Modified Function 			Change Description (Bug No. (If Any))
//(DD MMM YYYY)
//22-Sep-2017   	anil			N/A							File Created
//-----------------------------------------------------------------------------------------------------------

package com.san.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.TimeZone;

public class DateTimeUtil {

	private static SimpleDateFormat simpleDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

	public static Date asDate(LocalDate localDate) {
		return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
	}

	public static Date asDate(LocalDateTime localDateTime) {
		return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
	}

	public static Date asDate(LocalTime localTime) {
		Instant instant = localTime.atDate(LocalDate.now()).atZone(ZoneId.systemDefault()).toInstant();
		return Date.from(instant);
	}

	public static Date asDate(OffsetDateTime offsetDateTime) {
		return Date.from(offsetDateTime.toInstant());
	}

	public static Date asDate(ZonedDateTime localDateTime) {
		return Date.from(localDateTime.toInstant());
	}

	public static LocalDate asLocalDate(Date date) {
		return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
	}

	public static LocalDateTime asLocalDateTime(Date date) {
		return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
	}

	public static ZonedDateTime asZoneDateTime(Date date) {
		return ZonedDateTime.of(Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime(), ZoneId.systemDefault());
	}

	public static OffsetDateTime asOffsetDateTime(Date date) {
		TimeZone timeZone = TimeZone.getTimeZone(ZoneId.systemDefault());
		ZoneOffset zoneOffset = ZoneOffset.ofTotalSeconds(timeZone.getOffset(System.currentTimeMillis()) / 1000);
		return OffsetDateTime.of(asLocalDateTime(date), zoneOffset);
	}

	public static Date addDaysToDate(Date date, int days) {
		LocalDateTime locaDateTime = asLocalDateTime(date);
		return asDate(locaDateTime.plusDays(days));
	}

	public static Date subtractDaysFromDate(Date date, int days) {
		LocalDateTime locaDateTime = asLocalDateTime(date);
		return asDate(locaDateTime.minusDays(days));
	}

	public static Date addHoursToDate(Date date, int hours) {
		LocalDateTime locaDateTime = asLocalDateTime(date);
		return asDate(locaDateTime.plusHours(hours));
	}

	public static Date subtractHoursFromDate(Date date, int hours) {
		LocalDateTime locaDateTime = asLocalDateTime(date);
		return asDate(locaDateTime.minusHours(hours));
	}

	public static Date addMinutesToDate(Date date, int minutes) {
		LocalDateTime locaDateTime = asLocalDateTime(date);
		return asDate(locaDateTime.plusMinutes(minutes));
	}

	public static Date subtractMinutesFromDate(Date date, int minutes) {
		LocalDateTime locaDateTime = asLocalDateTime(date);
		return asDate(locaDateTime.minusMinutes(minutes));
	}

	public static Date fetchDate(int year, int month, int day) {
		return asDate(LocalDate.of(year, month, day));
	}

	public static Date fetchTime(int hour, int min, int sec) {
		return asDate(LocalTime.of(hour, min, sec));
	}

	public static Date fetchDateWithTime(int year, int month, int day, int hour, int min, int sec) {
		return asDate(LocalDateTime.of(year, month, day, hour, min, sec));
	}

	public static Date fetchDateInGivenTimeZone(Date date, TimeZone timeZone) {
		LocalDateTime locaDateTime = asLocalDateTime(date);
		return asDate(ZonedDateTime.of(locaDateTime, ZoneId.of(timeZone.getID())));
	}

	public static String formatDateTime(Date date, String format) {
		simpleDateTimeFormat.applyPattern(format);
		return simpleDateTimeFormat.format(date);
	}

	public static String toDateString(Date date) {
		return simpleDateFormat.format(date);
	}

	public static String toDateTimeString(Date date) {
		return simpleDateTimeFormat.format(date);
	}

	public static Date parseDate(String dateString, String format) throws ParseException {
		simpleDateTimeFormat.applyPattern(format);
		return simpleDateTimeFormat.parse(dateString);
	}

	public static long fetchRemainingTime(int hour, int min, int sec) {
		Date date = fetchTime(hour, min, sec);
		long remainingTime = date.getTime() - System.currentTimeMillis();
		if (remainingTime < 0) {
			remainingTime += 24 * 60 * 60 * 1000;
		}
		return remainingTime;
	}

	public static String fetchTimeZoneOffset(TimeZone timeZone) {
		return ZoneOffset.ofTotalSeconds(timeZone.getOffset(System.currentTimeMillis()) / 1000).getId();
	}

	public static String fetchTimeZoneOffset() {
		TimeZone timeZone = TimeZone.getTimeZone(ZoneId.systemDefault());
		return fetchTimeZoneOffset(timeZone);
	}

	public static Date fetchDateInGivenTimeZoneOffset(Date date, String timeZoneOffset) {
		ZoneOffset offset = ZoneOffset.of(timeZoneOffset);
		return asDate(OffsetDateTime.of(asLocalDateTime(date), offset));
	}

}
