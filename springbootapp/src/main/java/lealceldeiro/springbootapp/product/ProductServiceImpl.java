package lealceldeiro.springbootapp.product;

import lealceldeiro.springbootapp.config.AppConf;
import lealceldeiro.springbootapp.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {
  private final WebClient webClient;

  private final String similarIdsUrl;
  private final String productDetailsUrl;

  public ProductServiceImpl(AppConf appConf, WebClient webClient) {
    this.webClient = webClient;

    String productBaseUrl = appConf.urlFor(AppConf.Endpoint.PRODUCT_BASE_URL);
    similarIdsUrl = productBaseUrl + appConf.urlFor(AppConf.Endpoint.SIMILAR_PRODUCTS);
    productDetailsUrl = productBaseUrl + appConf.urlFor(AppConf.Endpoint.PRODUCT_DETAILS);
  }

  public Flux<ProductDto> getSimilarProducts(String productId) {
    Flux<Integer> similarProductIds = getSimilarProductIds(productId);

    return getProductDetails(similarProductIds);
  }

  private Flux<Integer> getSimilarProductIds(String productId) {
    return webClient.get()
                    .uri(similarIdsUrl, productId)
                    .retrieve()
                    .bodyToMono(Integer[].class)
                    .flatMapMany(Flux::fromArray)
                    .onErrorResume(this::handleNotFoundException);
  }

  private <T> Publisher<? extends T> handleNotFoundException(Throwable e) {
    log.error("Error while fetch similar ids", e);

    boolean isNotFound = WebClientResponseException.class.isAssignableFrom(e.getClass()) &&
                         HttpStatus.NOT_FOUND == ((WebClientResponseException) e).getStatusCode();

    return isNotFound ? Mono.error(new NotFoundException()) : Flux.empty();
  }

  private Flux<ProductDto> getProductDetails(Flux<Integer> productIds) {
    return productIds.flatMap(id -> webClient.get()
                                             .uri(productDetailsUrl, id)
                                             .retrieve()
                                             .bodyToFlux(ProductDto.class)
                                             .onErrorResume(e -> {
                                               log.error("Fetch product id {} errored:", id, e);
                                               return Flux.empty();
                                             }));
  }
}
