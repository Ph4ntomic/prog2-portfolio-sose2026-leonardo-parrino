package zoo.animal;

public record Ant(String name) implements Animal {
  public Ant {
    name = Animal.requireName(name);
  }
}
