/*
 * This Java source code is authored by Gagondeep Srai(W1374553),  
 * for use in his final year project at University of Westminster.
 * Created: 24/03/2015
 */
package gp;

import java.io.IOException;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/*
 * Class LoggerHandler
 * Handles the task of logging, providing a simple API for logging 
 * top expressions.
 */
public class LoggerHandler {
	
	public Logger logger;
	private FileHandler fh;
	
	public LoggerHandler() {
		logger = Logger.getLogger("ExpressionLogger");
		logger.setUseParentHandlers(false);
		
		try {
			fh = new FileHandler("./expressions.log");
			logger.addHandler(fh);
			SimpleFormatter sf = new SimpleFormatter();
			fh.setFormatter(sf);
			logger.info("Logging GP run on " + new Date().toString());
		} catch (SecurityException se) {
			se.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		
	}
	
	public void log(String s) {
		logger.info(s);
	}
}
