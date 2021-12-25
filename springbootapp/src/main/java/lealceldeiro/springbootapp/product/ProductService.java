package lealceldeiro.springbootapp.product;

import reactor.core.publisher.Flux;

public interface ProductService {
  Flux<ProductDto> getSimilarProducts(String productId);
}
