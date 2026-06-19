package zoo.animal;

public record Dolphin(String name) implements Mammal {
  public Dolphin {
    name = Animal.requireName(name);
  }
}
