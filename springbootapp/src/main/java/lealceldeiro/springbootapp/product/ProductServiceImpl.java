package lealceldeiro.springbootapp.product;

import lealceldeiro.springbootapp.config.AppConf;
import lealceldeiro.springbootapp.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
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

  public Flux<ProductDto> getSimilarProducts(Integer productId) {
    Flux<Integer> similarProductIds = getSimilarProductIds(productId);

    return getProductDetails(similarProductIds);
  }

  private Flux<Integer> getSimilarProductIds(Integer productId) {
    return webClient.get()
                    .uri(similarIdsUrl, productId)
                    .retrieve()
                    .bodyToMono(Integer[].class)
                    .flatMapMany(Flux::fromArray)
                    .onErrorResume(e -> {
                      log.error("Error while fetching similar ids for product ({}). It'll be mapped"
                                + " to a 'Not Found product'", productId, e);
                      return Mono.error(new NotFoundException());
                    });
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
