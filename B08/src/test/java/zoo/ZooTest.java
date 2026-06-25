package zoo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import zoo.animal.Animal;
import zoo.animal.Bird;
import zoo.animal.Eagle;
import zoo.animal.Elephant;
import zoo.animal.Fish;
import zoo.animal.Gorilla;
import zoo.animal.Lion;
import zoo.animal.Mammal;
import zoo.animal.Penguin;
import zoo.animal.Reptile;
import zoo.animal.Shark;
import zoo.animal.Snake;
import zoo.animal.Trout;
import zoo.enclosure.Aquarium;
import zoo.enclosure.CatHouse;
import zoo.enclosure.Enclosure;
import zoo.enclosure.MammalHouse;
import zoo.enclosure.Terrarium;

class ZooTest {

  @BeforeAll
  static void disableLoggerNoise() {
    Logger.getLogger(Zoo.class.getName()).setLevel(Level.OFF);
  }

  @Test
  void findEnclosureByNameUsesOptional() {
    var fixture = zooFixture();

    assertTrue(fixture.zoo().findEnclosureByName("River Aquarium").isPresent());
    assertTrue(fixture.zoo().findEnclosureByName("Missing").isEmpty());
  }

  @Test
  void findAnimalByNameSearchesAllEnclosures() {
    var fixture = zooFixture();

    assertEquals(fixture.lion(), fixture.zoo().findAnimalByName("Leo").orElseThrow());
    assertTrue(fixture.zoo().findAnimalByName("Missing").isEmpty());
  }

  @Test
  void findAnimalByNameReturnsFirstMatchInEnclosureOrder() {
    var zoo = new Zoo();
    var first = new Enclosure<Animal>("First");
    var second = new Enclosure<Animal>("Second");
    var firstLeo = new Lion("Leo");
    var secondLeo = new Gorilla("Leo");
    first.add(firstLeo);
    second.add(secondLeo);
    zoo.addEnclosure(first);
    zoo.addEnclosure(second);

    assertEquals(firstLeo, zoo.findAnimalByName(" Leo ").orElseThrow());
  }

  @Test
  void getAllAnimalsFlattensAllEnclosuresInInsertionOrder() {
    var fixture = zooFixture();

    assertEquals(
        List.of(
            fixture.trout(),
            fixture.shark(),
            fixture.snake(),
            fixture.elephant(),
            fixture.gorilla(),
            fixture.lion(),
            fixture.eagle(),
            fixture.penguin()),
        fixture.zoo().getAllAnimals());
  }

  @Test
  void getAllMammalsFiltersAcrossAllEnclosures() {
    var fixture = zooFixture();

    assertEquals(
        List.of(fixture.elephant(), fixture.gorilla(), fixture.lion()),
        fixture.zoo().getAllMammals());
  }

  @Test
  void getAnimalsByPredicateUsesProvidedPredicate() {
    var fixture = zooFixture();

    var fish = fixture.zoo().getAnimalsByPredicate(Fish.class::isInstance);

    assertEquals(List.of(fixture.trout(), fixture.shark()), fish);
  }

  @Test
  void countAnimalsByTypeGroupsByConcreteRecordClass() {
    var counts = zooFixture().zoo().countAnimalsByType();

    assertEquals(1L, counts.get(Trout.class));
    assertEquals(1L, counts.get(Shark.class));
    assertEquals(1L, counts.get(Snake.class));
    assertEquals(1L, counts.get(Elephant.class));
    assertEquals(1L, counts.get(Gorilla.class));
    assertEquals(1L, counts.get(Lion.class));
    assertEquals(1L, counts.get(Eagle.class));
    assertEquals(1L, counts.get(Penguin.class));
  }

  @Test
  void overcrowdedEnclosuresAreSelectedByStreamFilter() {
    var fixture = zooFixture();

    assertEquals(
        List.of(fixture.aquarium(), fixture.mammalHouse(), fixture.aviary()),
        fixture.zoo().getOvercrowdedEnclosures(1));
  }

  @Test
  void summaryUsesStreamGroupingByAnimalCategory() {
    var summary = zooFixture().zoo().summary();

    assertEquals("Zoo mit 5 Gehegen und 8 Tieren: 3 Mammals, 2 Birds, 2 Fish, 1 Reptiles", summary);
  }

  @Test
  void zooStateChangesAdmitTransferAndReleaseAnimals() {
    var zoo = new Zoo();
    var lions = new CatHouse("Lion House");
    var mammals = new MammalHouse<Mammal>("Mammals");
    var leo = new Lion("Leo");

    zoo.addEnclosure(lions);
    zoo.addEnclosure(mammals);

    assertTrue(zoo.admitAnimal(lions, leo));
    assertTrue(zoo.transferAnimal(lions, mammals, leo));
    assertFalse(lions.getInhabitants().contains(leo));
    assertTrue(mammals.getInhabitants().contains(leo));
    assertTrue(zoo.releaseAnimal(mammals, leo));
    assertFalse(zoo.releaseAnimal(mammals, leo));
  }

  @Test
  void duplicateEnclosureNamesAreRejected() {
    var zoo = new Zoo();
    zoo.addEnclosure(new Aquarium<Fish>("Water"));

    assertThrows(
        IllegalArgumentException.class, () -> zoo.addEnclosure(new Aquarium<Fish>("Water")));
  }

  @Test
  void returnedEnclosureListIsImmutable() {
    var fixture = zooFixture();

    assertThrows(
        UnsupportedOperationException.class,
        () -> fixture.zoo().getEnclosures().add(new Enclosure<Animal>("Other")));
  }

  private static Fixture zooFixture() {
    var zoo = new Zoo();
    var aquarium = new Aquarium<Fish>("River Aquarium");
    var terrarium = new Terrarium<Reptile>("Desert Terrarium");
    var mammalHouse = new MammalHouse<Mammal>("Mammals");
    var lionHouse = new CatHouse("Lion House");
    var aviary = new Enclosure<Bird>("Aviary");

    var trout = new Trout("Toni");
    var shark = new Shark("Sam");
    var snake = new Snake("Sina");
    var elephant = new Elephant("Ella");
    var gorilla = new Gorilla("Gina");
    var lion = new Lion("Leo");
    var eagle = new Eagle("Erik");
    var penguin = new Penguin("Pia");

    zoo.addEnclosure(aquarium);
    zoo.addEnclosure(terrarium);
    zoo.addEnclosure(mammalHouse);
    zoo.addEnclosure(lionHouse);
    zoo.addEnclosure(aviary);

    zoo.admitAnimal(aquarium, trout);
    zoo.admitAnimal(aquarium, shark);
    zoo.admitAnimal(terrarium, snake);
    zoo.admitAnimal(mammalHouse, elephant);
    zoo.admitAnimal(mammalHouse, gorilla);
    zoo.admitAnimal(lionHouse, lion);
    zoo.admitAnimal(aviary, eagle);
    zoo.admitAnimal(aviary, penguin);

    return new Fixture(
        zoo,
        aquarium,
        terrarium,
        mammalHouse,
        lionHouse,
        aviary,
        trout,
        shark,
        snake,
        elephant,
        gorilla,
        lion,
        eagle,
        penguin);
  }

  private record Fixture(
      Zoo zoo,
      Aquarium<Fish> aquarium,
      Terrarium<Reptile> terrarium,
      MammalHouse<Mammal> mammalHouse,
      CatHouse lionHouse,
      Enclosure<Bird> aviary,
      Trout trout,
      Shark shark,
      Snake snake,
      Elephant elephant,
      Gorilla gorilla,
      Lion lion,
      Eagle eagle,
      Penguin penguin) {}
}
