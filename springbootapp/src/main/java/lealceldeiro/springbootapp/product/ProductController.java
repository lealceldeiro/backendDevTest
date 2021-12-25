package lealceldeiro.springbootapp.product;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("product")
@RequiredArgsConstructor
public class ProductController {
  private final ProductService productService;

  @GetMapping("/{productId}/similar")
  public Flux<ProductDto> getSimilar(@PathVariable String productId) {
    return productService.getSimilarProducts(productId);
  }
}
