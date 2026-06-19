package zoo.animal;

public record Lion(String name) implements Cat {
  public Lion {
    name = Animal.requireName(name);
  }
}
