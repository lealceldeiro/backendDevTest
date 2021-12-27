package lealceldeiro.springbootapp.product;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.security.SecureRandom;
import lealceldeiro.springbootapp.AppExceptionHandler;
import lealceldeiro.springbootapp.TestUtil;
import lealceldeiro.springbootapp.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import reactor.core.publisher.Flux;

@AutoConfigureMockMvc
@SpringBootTest(classes = {ProductController.class, AppExceptionHandler.class})
class ProductControllerTest {
  @MockBean
  private ProductService productService;

  @Autowired
  private MockMvc mockMvc;

  @Test
  void getSimilarReturnsOK() {
    var random = new SecureRandom();
    var id = String.valueOf(random.nextInt());
    var sampleDto = new ProductDto(id, TestUtil.randomString(),
                                   new BigDecimal(random.nextInt()), random.nextBoolean());
    when(productService.getSimilarProducts(id)).thenReturn(Flux.just(sampleDto));

    var webTestClient = WebTestClient.bindToController(new ProductController(productService))
                                     .configureClient()
                                     .baseUrl("/product")
                                     .build();
    webTestClient.get()
                 .uri("/{productId}/similar", id)
                 .exchange()
                 .expectStatus().isOk()
                 .expectBodyList(ProductDto.class).contains(sampleDto);
  }

  @Test
  void getSimilarReturnsNotFoundIfTheProductIdDoesNotExistInSimilar3rdPartyAPI() throws Exception {
    when(productService.getSimilarProducts(any())).thenThrow(new NotFoundException());

    mockMvc.perform(MockMvcRequestBuilders.get("/product/{productId}/similar", 1))
           .andExpect(status().isNotFound());
  }
}
