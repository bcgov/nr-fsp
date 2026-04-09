package ca.bc.gov.nrs.fsp.api.properties;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.Getter;
import org.jboss.threads.EnhancedQueueExecutor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.Executor;

@Component
@Getter
public class ApplicationProperties {
    public static final Executor bgTask = new EnhancedQueueExecutor.Builder()
            .setThreadFactory(new ThreadFactoryBuilder().setNameFormat("bg-task-executor-%d").build())
            .setCorePoolSize(1).setMaximumPoolSize(1).setKeepAliveTime(Duration.ofSeconds(60)).build();
  public static final String FSP_API = "FSP-API";
  public static final String CORRELATION_ID = "correlationID";
  
}
