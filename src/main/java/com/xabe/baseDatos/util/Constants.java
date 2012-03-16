package com.xabe.baseDatos.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public final class Constants {

	private Constants() {
	}
	
	public static final String DATE_FORMAT = "dd/MM/yyyy hh:mm:ss:SSS";
	private static final SimpleDateFormat FORMATTER_LOG = new SimpleDateFormat(DATE_FORMAT, new Locale("es", "ES", "Traditional_WIN"));
	
	public static synchronized String getDateString(Date date){
		return FORMATTER_LOG.format(date);
	}
	
	public static synchronized String getDateString(){
		return getDateString(new Date());
	}
}
