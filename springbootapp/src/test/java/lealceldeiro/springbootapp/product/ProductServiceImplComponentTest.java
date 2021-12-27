package lealceldeiro.springbootapp.product;

import lealceldeiro.springbootapp.exception.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ProductServiceImplComponentTest {
  @Autowired
  private ProductService productService;

  @Test
  void getSimilarProductsReturnsNotFound() {
    var flux = productService.getSimilarProducts(String.valueOf(Integer.MAX_VALUE));
    Assertions.assertThrows(NotFoundException.class, flux::blockFirst);
  }
}
