package genband.loggingtest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Hello world!
 *
 */
public class App 
{
  public static Logger logger = LogManager.getLogger(App.class);
    public static void main( String[] args )
    {
      logger.warn( "Hello World!" );
    }
}
