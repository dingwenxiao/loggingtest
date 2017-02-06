package genband.loggingtest;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Filter.Result;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.mom.kafka.KafkaAppender;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.filter.CompositeFilter;
import org.apache.logging.log4j.core.filter.ThresholdFilter;
import org.apache.logging.log4j.core.layout.PatternLayout;


/**
 * Dynamically add kafka log4j appender
 * 
 * @author dixiao
 *
 */
public class LogConfigurationUtil {
  private final static String BOOTSTRAP_SERVERS = "bootstrap.servers";// bootstrap.servers address

  public static void addKafkaAppender(
      String appenderName, String topicName, Level thresholdFilterOneLevel,
      Level thresholdFilterTwoLevel) {
    final LoggerContext loggerContext = (LoggerContext) LogManager.getContext(false);
    final Configuration configuration = loggerContext.getConfiguration();

    Property[] properties =
        new Property[] {Property.createProperty(BOOTSTRAP_SERVERS, "192.168.33.10:9092")};

    Filter[] filter =
        new ThresholdFilter[] {ThresholdFilter.createFilter(thresholdFilterOneLevel, null, null),
            ThresholdFilter.createFilter(thresholdFilterTwoLevel, Result.DENY, Result.NEUTRAL)};

    CompositeFilter compositeFilter = CompositeFilter.createFilters(filter);

    PatternLayout patternLayout =
        PatternLayout.createLayout("%d{ISO8601}{UTC} ${sys:microserver.name.id} ${sys:hostname} %p %F %M %t %L %m", null,
            configuration, null, null, false, false, null, null);

    KafkaAppender kafkaAppender = KafkaAppender.createAppender(patternLayout, compositeFilter,
        appenderName, true, topicName, properties);
    kafkaAppender.start();

    configuration.addAppender(kafkaAppender);

    LoggerConfig loggerConfig = configuration.getLoggerConfig(LogManager.ROOT_LOGGER_NAME);
    loggerConfig.addAppender(kafkaAppender, Level.ALL, null);
    loggerContext.updateLoggers(configuration);
  }

}
