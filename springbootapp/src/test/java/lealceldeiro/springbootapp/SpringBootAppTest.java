package lealceldeiro.springbootapp;

import static org.mockito.Mockito.times;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.SpringApplication;

class SpringBootAppTest {
  @Test
  void applicationCanBeInstantiated() {
    var application = new SpringBootApp();
    Assertions.assertNotNull(application);
  }

  @Test
  void applicationRunsSpringRunner() {
    try (var mockedSpringApplication = Mockito.mockStatic(SpringApplication.class)) {
      SpringBootApp.main(new String[0]);
      mockedSpringApplication.verify(() -> SpringApplication.run(SpringBootApp.class), times(1));
    }
  }
}
