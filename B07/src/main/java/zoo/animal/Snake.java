package zoo.animal;

public record Snake(String name) implements Reptile {
  public Snake {
    name = Animal.requireName(name);
  }
}
