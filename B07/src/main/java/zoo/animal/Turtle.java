package zoo.animal;

public record Turtle(String name) implements Reptile {
  public Turtle {
    name = Animal.requireName(name);
  }
}
