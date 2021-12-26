package lealceldeiro.springbootapp.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.netty.handler.logging.LogLevel;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.logging.AdvancedByteBufFormat;

class WebConfigTest {
  @Test
  void webClient() {
    try (var mockedHttpClient = mockStatic(HttpClient.class);
         var mockedWebClient = mockStatic(WebClient.class);
         var mockedHttpClientConstructor = mockConstruction(ReactorClientHttpConnector.class)) {
      // given
      var connTimeout = Duration.ofMillis(Math.abs(new SecureRandom().nextInt()));

      HttpClient httpClientMock = mock(HttpClient.class);
      when(httpClientMock.option(any(), eq(connTimeout.toMillisPart()))).thenReturn(httpClientMock);
      when(httpClientMock.responseTimeout(connTimeout)).thenReturn(httpClientMock);
      when(httpClientMock.wiretap(anyString(), any(LogLevel.class),
                                  any(AdvancedByteBufFormat.class))).thenReturn(httpClientMock);
      mockedHttpClient.when(HttpClient::create).thenReturn(httpClientMock);

      var expectedWebClient = mock(WebClient.class);

      var webClientBuilderMock = mock(WebClient.Builder.class);
      when(webClientBuilderMock.clientConnector(any(ReactorClientHttpConnector.class)))
          .thenReturn(webClientBuilderMock);
      when(webClientBuilderMock.build()).thenReturn(expectedWebClient);
      mockedWebClient.when(WebClient::builder).thenReturn(webClientBuilderMock);

      // when
      var webConfig = new WebConfig();
      webConfig.setConnectionTimeout(connTimeout);
      var actualWebClient = webConfig.webClient();

      // then
      assertEquals(expectedWebClient, actualWebClient);

      verify(httpClientMock, times(1)).option(any(), eq(connTimeout.toMillisPart()));
      verify(httpClientMock, times(1)).responseTimeout(connTimeout);

      var constructedHttpClientConnector = mockedHttpClientConstructor.constructed();
      assertEquals(1, constructedHttpClientConnector.size());
      verify(webClientBuilderMock, times(1)).clientConnector(constructedHttpClientConnector.get(0));
    }
  }
}
