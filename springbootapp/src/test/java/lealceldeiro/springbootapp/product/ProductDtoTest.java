package lealceldeiro.springbootapp.product;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.stream.Stream;
import lealceldeiro.springbootapp.TestUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ProductDtoTest {
  @Test
  void testDtoAccessors() {
    var random = new SecureRandom();

    var id = String.valueOf(random.nextInt());
    var name = TestUtil.randomString();
    var price = new BigDecimal(random.nextInt());
    var availability = random.nextBoolean();

    var dto = new ProductDto();
    dto.setId(id);
    dto.setName(name);
    dto.setPrice(price);
    dto.setAvailability(availability);

    assertEquals(id, dto.getId());
    assertEquals(name, dto.getName());
    assertEquals(price, dto.getPrice());
    assertEquals(availability, dto.isAvailability());
  }

  private static Stream<Arguments> testEqualsAndHashCodeArgs() {
    var random = new SecureRandom();

    var id = String.valueOf(random.nextInt());
    var name = TestUtil.randomString();
    var price = new BigDecimal(random.nextInt());
    var availability = random.nextBoolean();

    var baseDto = new ProductDto(id, name, price, availability);

    var equalDto1 = new ProductDto(id, name + TestUtil.randomString(), price, availability);

    var equalDto2 = new ProductDto(id, name + TestUtil.randomString(), price, availability);
    var equalDto3 = new ProductDto(id, name, price.add(new BigDecimal(1)), availability);
    var equalDto4 = new ProductDto(id, name, price, !availability);

    var dtoNotEqual = new ProductDto(id + 1, name, price, availability);

    return Stream.of(arguments(baseDto, equalDto1, true),
                     arguments(baseDto, equalDto2, true),
                     arguments(baseDto, equalDto3, true),
                     arguments(baseDto, equalDto4, true),
                     arguments(baseDto, dtoNotEqual, false));
  }

  @ParameterizedTest
  @MethodSource("testEqualsAndHashCodeArgs")
  void testEqualsAndHashCode(ProductDto dto1, ProductDto dto2, boolean equalExpected) {
    assertEquals(equalExpected, dto1.equals(dto2));
    assertEquals(equalExpected, dto1.hashCode() == dto2.hashCode());
  }

  @Test
  void dtoToStringShowsProperties() {
    var random = new SecureRandom();

    var id = String.valueOf(random.nextInt());
    var name = TestUtil.randomString();
    var price = new BigDecimal(random.nextInt());
    var availability = random.nextBoolean();
    var dto = new ProductDto(id, name, price, availability);

    assertNotNull(dto.toString());
    assertTrue(dto.toString().contains("id=" + id));
    assertTrue(dto.toString().contains("name=" + name));
    assertTrue(dto.toString().contains("price=" + price));
    assertTrue(dto.toString().contains("availability=" + availability));
  }
}
