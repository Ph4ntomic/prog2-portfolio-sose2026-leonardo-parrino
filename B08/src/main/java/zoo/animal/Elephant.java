package zoo.animal;

public record Elephant(String name) implements Mammal {
  public Elephant {
    name = Animal.requireName(name);
  }
}
