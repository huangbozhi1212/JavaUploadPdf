package com.hbz.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期工具类 
 */
public class DateUtil {
	
	private static final DateFormat DF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static final DateFormat DTF = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * 字符串转日期
	 * @param dateStr
	 * @return
	 */
	public static Date parse(String dateStr) {
		int length = dateStr.length();
		try {
			if ((length == "yyyy-MM-dd HH:mm:ss".length()) || (length == "yyyy-MM-dd HH:mm:ss".length() + 1)) {
				return DF.parse(dateStr);
			} else if (length == "yyyy-MM-dd".length()) {
				return DTF.parse(dateStr);
			}
		} catch (ParseException e) {
		}
		return null;
	}
	
	public static String format(Date date) {
		return DF.format(date);
	}
}