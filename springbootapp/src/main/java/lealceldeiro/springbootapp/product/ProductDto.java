package lealceldeiro.springbootapp.product;

import java.math.BigDecimal;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ProductDto {
  @EqualsAndHashCode.Include
  private int id;
  private String name;
  private BigDecimal price;
  private boolean availability;
}

