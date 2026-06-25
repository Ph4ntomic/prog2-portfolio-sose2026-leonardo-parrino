package zoo.animal;

public record Penguin(String name) implements Bird {
  public Penguin {
    name = Animal.requireName(name);
  }
}
