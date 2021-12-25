package lealceldeiro.springbootapp.config;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

@Setter
@Configuration
@ConfigurationProperties(prefix = "web")
public class WebConfig {
  private static final int DEFAULT_TIMEOUT = 240000;

  private int connectionTimeout = DEFAULT_TIMEOUT;
  private int readTimeout = DEFAULT_TIMEOUT;
  private int writeTimeout = DEFAULT_TIMEOUT;

  @Bean
  public WebClient webClient() {
    ChannelHandler readHandler = new ReadTimeoutHandler(readTimeout, TimeUnit.MILLISECONDS);
    ChannelHandler writeHandler = new WriteTimeoutHandler(writeTimeout, TimeUnit.MILLISECONDS);
    var httpClient = HttpClient.create()
                               .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectionTimeout)
                               .responseTimeout(Duration.ofMillis(connectionTimeout))
        // FIXME: configure properly the timeouts
                               /*.doOnConnected(conn -> conn.addHandlerLast(readHandler)
                                                          .addHandlerLast(writeHandler))*/;

    return WebClient.builder().clientConnector(new ReactorClientHttpConnector(httpClient)).build();
  }
}
