package zoo.animal;

public record Gorilla(String name) implements Primate {
  public Gorilla {
    name = Animal.requireName(name);
  }
}
