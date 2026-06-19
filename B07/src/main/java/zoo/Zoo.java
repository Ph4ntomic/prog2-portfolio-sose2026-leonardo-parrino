package zoo;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import zoo.animal.Animal;
import zoo.animal.Bird;
import zoo.animal.Fish;
import zoo.animal.Mammal;
import zoo.animal.Reptile;
import zoo.enclosure.Enclosure;

public class Zoo {

  private static final Logger LOGGER = Logger.getLogger(Zoo.class.getName());

  private final List<Enclosure<? extends Animal>> enclosures = new ArrayList<>();

  public void addEnclosure(Enclosure<? extends Animal> enclosure) {
    LOGGER.log(Level.INFO, () -> "addEnclosure(enclosure=%s)".formatted(enclosure));
    var checked = requireArgument(enclosure, "enclosure");

    if (enclosures.stream().anyMatch(existing -> existing.name().equals(checked.name()))) {
      LOGGER.severe(() -> "duplicate enclosure name: %s".formatted(checked.name()));
      throw new IllegalArgumentException("duplicate enclosure name: " + checked.name());
    }

    enclosures.add(checked);
    LOGGER.fine(this::stateSummary);
  }

  public <T extends Animal> boolean admitAnimal(Enclosure<? super T> enclosure, T animal) {
    LOGGER.log(
        Level.INFO, () -> "admitAnimal(enclosure=%s, animal=%s)".formatted(enclosure, animal));
    var checkedEnclosure = requireArgument(enclosure, "enclosure");
    var checkedAnimal = requireArgument(animal, "animal");

    if (!containsEnclosure(checkedEnclosure)) {
      LOGGER.warning(() -> "enclosure not found: %s".formatted(checkedEnclosure.name()));
      return false;
    }

    var added = checkedEnclosure.add(checkedAnimal);
    if (!added) {
      LOGGER.warning(() -> "animal already present: %s".formatted(checkedAnimal));
      return false;
    }

    LOGGER.fine(this::stateSummary);
    return true;
  }

  public <T extends Animal> boolean releaseAnimal(Enclosure<? super T> enclosure, T animal) {
    LOGGER.log(
        Level.INFO, () -> "releaseAnimal(enclosure=%s, animal=%s)".formatted(enclosure, animal));
    var checkedEnclosure = requireArgument(enclosure, "enclosure");
    var checkedAnimal = requireArgument(animal, "animal");

    if (!containsEnclosure(checkedEnclosure)) {
      LOGGER.warning(() -> "enclosure not found: %s".formatted(checkedEnclosure.name()));
      return false;
    }

    var removed = checkedEnclosure.remove(checkedAnimal);
    if (!removed) {
      LOGGER.warning(() -> "animal not found: %s".formatted(checkedAnimal));
      return false;
    }

    LOGGER.fine(this::stateSummary);
    return true;
  }

  public <T extends Animal> boolean transferAnimal(
      Enclosure<T> source, Enclosure<? super T> target, T animal) {
    LOGGER.log(
        Level.INFO,
        () -> "transferAnimal(source=%s, target=%s, animal=%s)".formatted(source, target, animal));
    var checkedSource = requireArgument(source, "source");
    var checkedTarget = requireArgument(target, "target");
    var checkedAnimal = requireArgument(animal, "animal");

    if (!containsEnclosure(checkedSource)) {
      LOGGER.warning(() -> "source enclosure not found: %s".formatted(checkedSource.name()));
      return false;
    }
    if (!containsEnclosure(checkedTarget)) {
      LOGGER.warning(() -> "target enclosure not found: %s".formatted(checkedTarget.name()));
      return false;
    }

    if (!checkedSource.remove(checkedAnimal)) {
      LOGGER.warning(() -> "animal not found: %s".formatted(checkedAnimal));
      return false;
    }

    if (!checkedTarget.add(checkedAnimal)) {
      checkedSource.add(checkedAnimal);
      LOGGER.severe(() -> "transfer target rejected animal, rollback applied: " + checkedAnimal);
      return false;
    }

    LOGGER.fine(this::stateSummary);
    return true;
  }

  public List<Enclosure<? extends Animal>> getEnclosures() {
    LOGGER.info("getEnclosures()");
    var result = List.copyOf(enclosures);
    LOGGER.fine(this::stateSummary);
    return result;
  }

  public Optional<Enclosure<? extends Animal>> findEnclosureByName(String name) {
    LOGGER.log(Level.INFO, () -> "findEnclosureByName(name=%s)".formatted(name));
    var checkedName = requireName(name);

    var result =
        enclosures.stream().filter(enclosure -> enclosure.name().equals(checkedName)).findFirst();

    if (result.isEmpty()) {
      LOGGER.warning(() -> "enclosure not found: %s".formatted(checkedName));
    } else {
      LOGGER.fine(this::stateSummary);
    }
    return result;
  }

  public List<Animal> getAllAnimals() {
    LOGGER.info("getAllAnimals()");
    var result = animalStream().toList();
    LOGGER.fine(this::stateSummary);
    return result;
  }

  public List<Mammal> getAllMammals() {
    LOGGER.info("getAllMammals()");
    var result = animalStream().filter(Mammal.class::isInstance).map(Mammal.class::cast).toList();
    LOGGER.fine(this::stateSummary);
    return result;
  }

  public List<Animal> getAnimalsByPredicate(Predicate<Animal> predicate) {
    LOGGER.log(Level.INFO, () -> "getAnimalsByPredicate(predicate=%s)".formatted(predicate));
    var checkedPredicate = requireArgument(predicate, "predicate");

    var result = animalStream().filter(checkedPredicate).toList();
    LOGGER.fine(this::stateSummary);
    return result;
  }

  public Map<Class<? extends Animal>, Long> countAnimalsByType() {
    LOGGER.info("countAnimalsByType()");
    var result =
        animalStream()
            .collect(Collectors.groupingBy(Zoo::typeOf, LinkedHashMap::new, Collectors.counting()));
    LOGGER.fine(this::stateSummary);
    return result;
  }

  public List<Enclosure<? extends Animal>> getOvercrowdedEnclosures(int maxAnimals) {
    LOGGER.log(Level.INFO, () -> "getOvercrowdedEnclosures(maxAnimals=%d)".formatted(maxAnimals));
    if (maxAnimals < 0) {
      LOGGER.severe(() -> "negative maxAnimals: %d".formatted(maxAnimals));
      throw new IllegalArgumentException("maxAnimals must not be negative");
    }

    var result = enclosures.stream().filter(enclosure -> enclosure.size() > maxAnimals).toList();
    LOGGER.fine(this::stateSummary);
    return result;
  }

  public String summary() {
    LOGGER.info("summary()");
    var animals = animalStream().toList();
    var counts =
        animals.stream()
            .collect(
                Collectors.groupingBy(Zoo::categoryOf, LinkedHashMap::new, Collectors.counting()));
    var grouped =
        Stream.of("Mammals", "Birds", "Fish", "Reptiles", "Other")
            .filter(counts::containsKey)
            .map(category -> "%d %s".formatted(counts.get(category), category))
            .collect(Collectors.joining(", "));

    var prefix = "Zoo mit %d Gehegen und %d Tieren".formatted(enclosures.size(), animals.size());
    var result = grouped.isBlank() ? prefix : prefix + ": " + grouped;
    LOGGER.fine(this::stateSummary);
    return result;
  }

  private Stream<Animal> animalStream() {
    return enclosures.stream()
        .flatMap(enclosure -> enclosure.getInhabitants().stream())
        .map(Animal.class::cast);
  }

  private boolean containsEnclosure(Enclosure<?> enclosure) {
    return enclosures.stream().anyMatch(registered -> registered == enclosure);
  }

  private String stateSummary() {
    var animalCount = enclosures.stream().mapToInt(Enclosure::size).sum();
    return "state(enclosures=%d, animals=%d)".formatted(enclosures.size(), animalCount);
  }

  private static String categoryOf(Animal animal) {
    return switch (animal) {
      case Mammal _ -> "Mammals";
      case Bird _ -> "Birds";
      case Fish _ -> "Fish";
      case Reptile _ -> "Reptiles";
      case Animal _ -> "Other";
    };
  }

  private static Class<? extends Animal> typeOf(Animal animal) {
    return animal.getClass().asSubclass(Animal.class);
  }

  private static String requireName(String name) {
    var checked = Objects.requireNonNull(name, "name").trim();
    if (checked.isBlank()) {
      LOGGER.severe("name must not be blank");
      throw new IllegalArgumentException("name must not be blank");
    }
    return checked;
  }

  private static <T> T requireArgument(T value, String argumentName) {
    if (value == null) {
      LOGGER.severe(() -> "%s must not be null".formatted(argumentName));
      throw new NullPointerException(argumentName);
    }
    return value;
  }
}
