package zoo.animal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class AnimalHierarchyTest {

  @Test
  void recordsExposeValidatedNames() {
    var lion = new Lion("  Leo  ");

    assertEquals("Leo", lion.name());
    assertThrows(IllegalArgumentException.class, () -> new Trout("   "));
    assertThrows(NullPointerException.class, () -> new Eagle(null));
  }

  @Test
  void concreteRecordsFollowSealedHierarchy() {
    Animal trout = new Trout("Toni");
    Animal gorilla = new Gorilla("Gina");
    Animal penguin = new Penguin("Pia");

    assertInstanceOf(Fish.class, trout);
    assertInstanceOf(Primate.class, gorilla);
    assertInstanceOf(Mammal.class, gorilla);
    assertInstanceOf(Bird.class, penguin);
  }
}
