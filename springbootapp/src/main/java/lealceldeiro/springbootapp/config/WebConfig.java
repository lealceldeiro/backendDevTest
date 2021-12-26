package lealceldeiro.springbootapp.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.logging.LogLevel;
import java.time.Duration;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.logging.AdvancedByteBufFormat;

@Slf4j
@Setter
@Configuration
@ConfigurationProperties(prefix = "web")
public class WebConfig {
  private static final int DEFAULT_TIMEOUT = 15000;

  private int connectionTimeout = DEFAULT_TIMEOUT;

  @Bean
  public WebClient webClient() {
    Duration responseTimeoutDuration = Duration.ofMillis(connectionTimeout);
    log.info("Setting web client timeout: {}", responseTimeoutDuration);

    var httpClient = HttpClient.create()
                               .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectionTimeout)
                               .responseTimeout(responseTimeoutDuration)
                               .wiretap("logger", LogLevel.DEBUG, AdvancedByteBufFormat.TEXTUAL);

    return WebClient.builder().clientConnector(new ReactorClientHttpConnector(httpClient)).build();
  }
}
