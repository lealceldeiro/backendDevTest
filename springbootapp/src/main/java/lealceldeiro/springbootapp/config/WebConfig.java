package lealceldeiro.springbootapp.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.logging.LogLevel;
import java.time.Duration;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.logging.AdvancedByteBufFormat;

@Slf4j
@Setter
@Configuration
public class WebConfig {
  @Value("${spring.mvc.async.request-timeout:15000}")
  private Duration connectionTimeout;

  @Bean
  public WebClient webClient() {
    log.info("Setting web client timeout: {}", connectionTimeout);

    var httpClient = HttpClient.create()
                               .option(ChannelOption.CONNECT_TIMEOUT_MILLIS,
                                       connectionTimeout.toMillisPart())
                               .responseTimeout(connectionTimeout)
                               .wiretap("logger", LogLevel.DEBUG, AdvancedByteBufFormat.TEXTUAL);

    return WebClient.builder().clientConnector(new ReactorClientHttpConnector(httpClient)).build();
  }
}
