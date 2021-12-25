package lealceldeiro.springbootapp.product;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDto {
  private int id;
  private String name;
  private BigDecimal price;
  private boolean availability;
}

