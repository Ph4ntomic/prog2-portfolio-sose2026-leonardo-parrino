package zoo.animal;

public record Coral(String name) implements Animal {
  public Coral {
    name = Animal.requireName(name);
  }
}
