package zoo.animal;

public record Capybara(String name) implements Rodent {
  public Capybara {
    name = Animal.requireName(name);
  }
}
