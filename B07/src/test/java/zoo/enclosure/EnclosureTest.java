package zoo.enclosure;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import zoo.animal.Fish;
import zoo.animal.Lion;
import zoo.animal.Trout;

class EnclosureTest {

  @Test
  void enclosureUsesSetSemanticsForDuplicateAnimals() {
    var lions = new CatHouse("Lion House");
    var leo = new Lion("Leo");

    assertTrue(lions.add(leo));
    assertFalse(lions.add(leo));
    assertEquals(1, lions.size());
    assertEquals(new Lion("Leo"), lions.getInhabitants().getFirst());
  }

  @Test
  void inhabitantsAreReturnedAsImmutableSnapshot() {
    var aquarium = new Aquarium<Fish>("River Aquarium");
    aquarium.add(new Trout("Toni"));

    var snapshot = aquarium.getInhabitants();

    assertThrows(UnsupportedOperationException.class, () -> snapshot.add(new Trout("Tamara")));
    assertEquals(1, aquarium.size());
  }

  @Test
  void specializedEnclosuresAcceptMatchingTypes() {
    var aquarium = new Aquarium<Fish>("Mixed Aquarium");
    var lions = new CatHouse("Lion House");

    assertTrue(aquarium.add(new Trout("Toni")));
    assertTrue(lions.add(new Lion("Leo")));
  }

  @Disabled("Dokumentiert Code, der wegen der Generic-Bounds nicht kompilieren darf.")
  @Test
  void genericsPreventWrongAssignmentsAtCompileTime() {
    // Aquarium<Fish> aquarium = new Aquarium<>("Aquarium");
    // aquarium.add(new Lion("Leo"));
    // CatHouse catHouse = new CatHouse("Cats");
    // catHouse.add(new Tiger("Tessa"));
    // Terrarium<Tiger> tigerTerrarium = new Terrarium<>("Wrong Bound");
  }
}
