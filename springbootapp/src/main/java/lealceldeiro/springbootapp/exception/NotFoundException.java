package lealceldeiro.springbootapp.exception;

public class NotFoundException extends RuntimeException {
  private static final long serialVersionUID = 4946804406045908607L;

  public NotFoundException() {
    super("Product Not found");
  }
}
