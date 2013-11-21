package br.uff.ic.provmonitor.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Date utils methods
 * */
public class DateUtils {
	/**
	 * Parse String parameter as a Date with <code>dd/MM/YYYY-HH:mm:ss</code> format
	 * @param dateToBeParsed <code>String</code> - String with Date value to be parsed as a Date object
	 * @return Date - result object
	 * */
	public static Date dateParse(String date2BeParsed) throws ParseException{
		Date parsedDate = null;
		DateFormat df = new SimpleDateFormat("dd/MM/YYYY-HH:mm:ss:S");
		parsedDate = df.parse(date2BeParsed);
		return parsedDate;
	}
	
	/**
	 * Converts <b>from</b> <code>java.util.Date</code> <b>to</b> <code>java.sql.Date returnDate</code>
	 * */
	public static java.sql.Date utilsDate2SqlDate(Date date2BeConverted){
		java.sql.Date returnDate = null;
		
		if (date2BeConverted != null){
			returnDate = new java.sql.Date(date2BeConverted.getTime());
		}
		
		return returnDate;
	}
	
	/**
	 * Converts <b>from</b> <code>java.util.Date</code> <b>to</b> <code>java.sql.Timestamp</code>
	 * @param date2BeConverted - Date that will be converted.
	 * @return java.sql.Timestamp - Corresponding Timestamp
	 */
	public static Timestamp utilsDate2SqlTimeStamp(Date date2BeConverted){
		Timestamp returnTimeStamp = null;
		
		if (date2BeConverted !=null){
			returnTimeStamp = new Timestamp(date2BeConverted.getTime());
		}
		
		return returnTimeStamp;
	}
	
	
}
