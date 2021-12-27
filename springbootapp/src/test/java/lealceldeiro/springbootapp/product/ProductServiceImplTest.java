package lealceldeiro.springbootapp.product;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;
import lealceldeiro.springbootapp.TestUtil;
import lealceldeiro.springbootapp.config.AppConf;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

class ProductServiceImplTest {
  private static final String BASE_URL = TestUtil.randomString();
  private static final String SIMILAR_PRODUCTS_URL = TestUtil.randomString();
  private static final String PRODUCTS_DETAILS_URL = TestUtil.randomString();

  private WebClient webClient;
  private AppConf appConf;

  private ProductService productService;

  @BeforeEach
  void setUp() {
    appConf = mock(AppConf.class);
    when(appConf.urlFor(AppConf.Endpoint.PRODUCT_BASE_URL)).thenReturn(BASE_URL);
    when(appConf.urlFor(AppConf.Endpoint.SIMILAR_PRODUCTS)).thenReturn(SIMILAR_PRODUCTS_URL);
    when(appConf.urlFor(AppConf.Endpoint.PRODUCT_DETAILS)).thenReturn(PRODUCTS_DETAILS_URL);
  }

  @ParameterizedTest
  @ValueSource(booleans = {true, false})
  void getSimilarProducts(boolean testErrorResponse) {
    var random = new SecureRandom();

    var dto1 = new ProductDto();
    dto1.setId(String.valueOf(random.nextInt()));
    var dto2 = new ProductDto();
    dto2.setId(String.valueOf(random.nextInt()));

    Integer[] similarIds = {random.nextInt(), random.nextInt()};
    ProductDto[] dtoValues = {dto1, dto2};
    var productId = String.valueOf(random.nextInt());

    var similarResponseSpecMock = mock(WebClient.ResponseSpec.class);
    when(similarResponseSpecMock.bodyToMono(Integer[].class)).thenReturn(Mono.just(similarIds));

    var similarUriSpecMock = mock(WebClient.RequestHeadersSpec.class);
    when(similarUriSpecMock.retrieve()).thenReturn(similarResponseSpecMock);

    var detailResponseSpecMock = mock(WebClient.ResponseSpec.class);
    when(detailResponseSpecMock.bodyToFlux(ProductDto.class))
        .thenReturn(testErrorResponse
                    ? Flux.generate(a -> Flux.error(new Throwable()))
                    : Flux.fromArray(dtoValues));

    var detailUriSpecMock = mock(WebClient.RequestHeadersSpec.class);
    when(detailUriSpecMock.retrieve()).thenReturn(detailResponseSpecMock);

    var requestHeadersUriSpecMock = mock(WebClient.RequestHeadersUriSpec.class);
    when(requestHeadersUriSpecMock.uri(BASE_URL + SIMILAR_PRODUCTS_URL, productId))
        .thenReturn(similarUriSpecMock);
    when(requestHeadersUriSpecMock.uri(eq(BASE_URL + PRODUCTS_DETAILS_URL), anyInt()))
        .thenReturn(detailUriSpecMock);

    webClient = mock(WebClient.class);
    when(webClient.get()).thenReturn(requestHeadersUriSpecMock);

    productService = new ProductServiceImpl(appConf, webClient);
    var actualSimilarProducts = productService.getSimilarProducts(productId);

    Collection<ProductDto> actual = new ArrayList<>();
    for (final ProductDto productDto : actualSimilarProducts.toIterable()) {
      actual.add(productDto);
    }

    assertTrue(testErrorResponse && actual.isEmpty()
               || actual.containsAll(Arrays.stream(dtoValues).collect(Collectors.toList())));
  }
}
