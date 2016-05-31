package pl.edu.agh.io.pdptw.logging;


import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class LoggingUtils {
	private static Logger logger = LogManager
			.getLogger(LoggingUtils.class.getName());
	private static StringWriter sw = new StringWriter();
	private static PrintWriter pw = new PrintWriter(sw);
	
	static {
		BasicConfigurator.configure();
		LogManager.getRootLogger().setLevel(Level.INFO);
	}
		
	public static void info(Object message) {
		logger.info(message.toString());
	}
	
	public static void debug(Object message) {
		logger.debug(message.toString());
	}
	
	public static void warn(Object message) {
		logger.warn(message.toString());
	}
	
	public static void error(Object message) {
		logger.error(message.toString());
	}
	
	public static void fatal(Object message) {
		logger.fatal(message.toString());
	}
	
	public static String getStackTraceAsString(Throwable throwable) {
		sw.flush();
		throwable.printStackTrace(pw);
		
		return sw.toString();
	}
	
	public static void logStackTrace(Throwable throwable) {
		logger.error(getStackTraceAsString(throwable));
	}
}
