package zoo.animal;

public record Chimpanzee(String name) implements Primate {
  public Chimpanzee {
    name = Animal.requireName(name);
  }
}
