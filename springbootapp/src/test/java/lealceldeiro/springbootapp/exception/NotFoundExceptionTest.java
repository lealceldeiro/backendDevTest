package lealceldeiro.springbootapp.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NotFoundExceptionTest {
  @Test
  void getMessage() {
    assertEquals("Product Not found", new NotFoundException().getMessage());
  }
}
