package zoo.animal;

public record Beaver(String name) implements Rodent {
  public Beaver {
    name = Animal.requireName(name);
  }
}
