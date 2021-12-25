package lealceldeiro.springbootapp.product;

import lealceldeiro.springbootapp.config.AppConf;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Service
public class ProductServiceImpl implements ProductService {
  private final WebClient webClient;

  private final String similarIdsUrl;
  private final String productDetailsUrl;

  public ProductServiceImpl(AppConf appConf, WebClient webClient) {
    this.webClient = webClient;
    similarIdsUrl = appConf.getProductEndpoint() + "product/{productId}/similarids";
    productDetailsUrl = appConf.getProductEndpoint() + "product/{productId}";
  }

  public Flux<ProductDto> getSimilarProducts(String productId) {
    return webClient.get()
                    .uri(similarIdsUrl, productId)
                    .retrieve()
                    .bodyToMono(Integer[].class)
                    .flatMapMany(Flux::fromArray)
                    .flatMap(similarProductId -> webClient.get()
                                                          .uri(productDetailsUrl, similarProductId)
                                                          .retrieve()
                                                          .bodyToFlux(ProductDto.class));
  }
}
