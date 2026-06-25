package zoo.enclosure;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

  @Test
  void findAnimalByNameReturnsTypedOptional() {
    var aquarium = new Aquarium<Fish>("River Aquarium");
    var toni = new Trout("Toni");
    aquarium.add(toni);

    assertEquals(toni, aquarium.findAnimalByName("Toni").orElseThrow());
    assertTrue(aquarium.findAnimalByName("Missing").isEmpty());
  }
}
