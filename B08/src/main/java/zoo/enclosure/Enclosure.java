package zoo.enclosure;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import zoo.animal.Animal;

public class Enclosure<T extends Animal> {

  private final String name;
  private final Set<T> inhabitants = new LinkedHashSet<>();

  public Enclosure(String name) {
    this.name = requireName(name);
  }

  public String name() {
    return name;
  }

  public boolean add(T animal) {
    return inhabitants.add(Objects.requireNonNull(animal, "animal"));
  }

  public boolean remove(T animal) {
    return inhabitants.remove(Objects.requireNonNull(animal, "animal"));
  }

  public boolean contains(T animal) {
    return inhabitants.contains(Objects.requireNonNull(animal, "animal"));
  }

  public int size() {
    return inhabitants.size();
  }

  public List<T> getInhabitants() {
    return List.copyOf(inhabitants);
  }

  public Optional<T> findAnimalByName(String animalName) {
    var checkedName = requireName(animalName);
    return inhabitants.stream().filter(animal -> animal.name().equals(checkedName)).findFirst();
  }

  @Override
  public String toString() {
    return "%s[name=%s, inhabitants=%d]"
        .formatted(getClass().getSimpleName(), name, inhabitants.size());
  }

  private static String requireName(String name) {
    var cleaned = Objects.requireNonNull(name, "name").trim();
    if (cleaned.isBlank()) {
      throw new IllegalArgumentException("name must not be blank");
    }
    return cleaned;
  }
}
