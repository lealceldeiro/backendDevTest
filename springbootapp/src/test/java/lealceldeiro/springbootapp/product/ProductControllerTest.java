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
  void getSimilarReturnsOK() throws Exception {
    var random = new SecureRandom();
    var sampleDto = new ProductDto(random.nextInt(), TestUtil.randomString(),
                                   new BigDecimal(random.nextInt()), random.nextBoolean());
    when(productService.getSimilarProducts(sampleDto.getId())).thenReturn(Flux.just());

    mockMvc.perform(MockMvcRequestBuilders.get("/product/{productId}/similar", sampleDto.getId()))
           .andExpect(status().isOk());
  }

  @Test
  void getSimilarReturnsNotFoundIfTheProductIdDoesNotExistInSimilar3rdPartyAPI() throws Exception {
    when(productService.getSimilarProducts(any())).thenThrow(new NotFoundException());

    mockMvc.perform(MockMvcRequestBuilders.get("/product/{productId}/similar", 1))
           .andExpect(status().isNotFound());
  }
}
