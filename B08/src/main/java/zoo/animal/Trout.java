package zoo.animal;

public record Trout(String name) implements Fish {
  public Trout {
    name = Animal.requireName(name);
  }
}
