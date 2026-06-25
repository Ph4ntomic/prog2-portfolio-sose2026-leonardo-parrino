package zoo.animal;

public record Shark(String name) implements Fish {
  public Shark {
    name = Animal.requireName(name);
  }
}
