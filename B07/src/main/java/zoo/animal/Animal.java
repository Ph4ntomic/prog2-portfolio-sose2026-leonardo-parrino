package zoo.animal;

import java.util.Objects;

public sealed interface Animal permits Mammal, Fish, Reptile, Bird, Ant, Coral {

  String name();

  static String requireName(String name) {
    var cleaned = Objects.requireNonNull(name, "name").trim();
    if (cleaned.isBlank()) {
      throw new IllegalArgumentException("name must not be blank");
    }
    return cleaned;
  }
}
