package zoo.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import zoo.animal.Animal;
import zoo.animal.Gorilla;
import zoo.animal.Lion;
import zoo.animal.Mammal;
import zoo.enclosure.CatHouse;
import zoo.enclosure.Enclosure;
import zoo.enclosure.MammalHouse;
import zoo.result.Result;
import zoo.result.ZooError;

class CommandManagerTest {

  @BeforeAll
  static void disableLoggerNoise() {
    Logger.getLogger(CommandManager.class.getName()).setLevel(Level.OFF);
  }

  @Test
  void managerExecutesUndoesAndRedoesCommands() {
    Enclosure<Mammal> mammals = new MammalHouse<>("Mammals");
    var manager = new CommandManager<Enclosure<Mammal>>();
    var leo = new Lion("Leo");
    var add = new AddAnimalCommand<>(leo);

    assertSuccess(manager.executeCommand(add, mammals), leo);
    assertTrue(mammals.contains(leo));

    assertSuccess(manager.undo(mammals), leo);
    assertFalse(mammals.contains(leo));

    assertSuccess(manager.redo(mammals), leo);
    assertTrue(mammals.contains(leo));
  }

  @Test
  void managerUsesLifoOrderForAddAndRemoveCommands() {
    Enclosure<Mammal> mammals = new MammalHouse<>("Mammals");
    var manager = new CommandManager<Enclosure<Mammal>>();
    var leo = new Lion("Leo");

    manager.executeCommand(new AddAnimalCommand<>(leo), mammals);
    manager.executeCommand(new RemoveAnimalCommand<>(leo), mammals);
    assertFalse(mammals.contains(leo));

    manager.undo(mammals);
    assertTrue(mammals.contains(leo));
    manager.undo(mammals);
    assertFalse(mammals.contains(leo));

    manager.redo(mammals);
    assertTrue(mammals.contains(leo));
    manager.redo(mammals);
    assertFalse(mammals.contains(leo));
  }

  @Test
  void successfulNewCommandClearsRedoHistory() {
    Enclosure<Mammal> mammals = new MammalHouse<>("Mammals");
    var manager = new CommandManager<Enclosure<Mammal>>();
    var leo = new Lion("Leo");
    var nala = new Lion("Nala");

    manager.executeCommand(new AddAnimalCommand<>(leo), mammals);
    manager.undo(mammals);
    manager.executeCommand(new AddAnimalCommand<>(nala), mammals);

    assertError(manager.redo(mammals), ZooError.NOTHING_TO_REDO);
    assertEquals(nala, mammals.findAnimalByName("Nala").orElseThrow());
  }

  @Test
  void failedNewCommandKeepsRedoHistory() {
    Enclosure<Mammal> mammals = new MammalHouse<>("Mammals");
    var manager = new CommandManager<Enclosure<Mammal>>();
    var leo = new Lion("Leo");

    manager.executeCommand(new AddAnimalCommand<>(leo), mammals);
    manager.undo(mammals);
    manager.executeCommand(new RemoveAnimalCommand<>(new Lion("Missing")), mammals);

    assertSuccess(manager.redo(mammals), leo);
    assertTrue(mammals.contains(leo));
  }

  @Test
  void failedCommandsAreNotAddedToHistory() {
    Enclosure<Mammal> mammals = new MammalHouse<>("Mammals");
    var manager = new CommandManager<Enclosure<Mammal>>();
    var missing = new Lion("Missing");

    assertError(
        manager.executeCommand(new RemoveAnimalCommand<>(missing), mammals),
        ZooError.ANIMAL_NOT_FOUND);
    assertError(manager.undo(mammals), ZooError.NOTHING_TO_UNDO);
  }

  @Test
  void failedUndoKeepsCommandOnUndoStack() {
    Enclosure<Mammal> mammals = new MammalHouse<>("Mammals");
    var manager = new CommandManager<Enclosure<Mammal>>();
    var leo = new Lion("Leo");

    manager.executeCommand(new AddAnimalCommand<>(leo), mammals);
    mammals.remove(leo);

    assertError(manager.undo(mammals), ZooError.ANIMAL_NOT_FOUND);
    mammals.add(leo);
    assertSuccess(manager.undo(mammals), leo);
  }

  @Test
  void failedRedoKeepsCommandOnRedoStack() {
    Enclosure<Mammal> mammals = new MammalHouse<>("Mammals");
    var manager = new CommandManager<Enclosure<Mammal>>();
    var leo = new Lion("Leo");

    manager.executeCommand(new AddAnimalCommand<>(leo), mammals);
    manager.undo(mammals);
    mammals.add(leo);

    assertError(manager.redo(mammals), ZooError.ANIMAL_ALREADY_PRESENT);
    mammals.remove(leo);
    assertSuccess(manager.redo(mammals), leo);
  }

  @Test
  void emptyHistoryReturnsExplicitErrors() {
    Enclosure<Mammal> mammals = new MammalHouse<>("Mammals");
    var manager = new CommandManager<Enclosure<Mammal>>();

    assertError(manager.undo(mammals), ZooError.NOTHING_TO_UNDO);
    assertError(manager.redo(mammals), ZooError.NOTHING_TO_REDO);
  }

  @Test
  void genericBoundsRejectFishCommandsForMammalEnclosures() {
    Enclosure<Mammal> mammals = new MammalHouse<>("Mammals");
    var manager = new CommandManager<Enclosure<Mammal>>();
    var lionCommand = new AddAnimalCommand<>(new Lion("Leo"));
    var gorillaCommand = new AddAnimalCommand<>(new Gorilla("Gina"));

    manager.executeCommand(lionCommand, mammals);
    manager.executeCommand(gorillaCommand, mammals);

    // AddAnimalCommand<zoo.animal.Shark> sharkCommand =
    //     new AddAnimalCommand<>(new zoo.animal.Shark("Nemo"));
    // manager.executeCommand(sharkCommand, mammals);
    assertTrue(mammals.findAnimalByName("Leo").isPresent());
    assertTrue(mammals.findAnimalByName("Gina").isPresent());
  }

  @Test
  void specializedCatManagerAcceptsLionCommands() {
    var cats = new CatHouse("Lion House");
    var manager = new CommandManager<Enclosure<Lion>>();
    var leo = new Lion("Leo");

    assertSuccess(manager.executeCommand(new AddAnimalCommand<>(leo), cats), leo);
    assertTrue(cats.contains(leo));
  }

  private static void assertSuccess(Result<ZooError, Animal> result, Lion expected) {
    switch (result) {
      case Result.Ok<ZooError, Animal>(var animal) -> assertEquals(expected, animal);
      case Result.Err<ZooError, Animal>(var error) -> fail("Unexpected error: " + error);
    }
  }

  private static void assertError(Result<ZooError, Animal> result, ZooError expected) {
    switch (result) {
      case Result.Ok<ZooError, Animal>(var animal) -> fail("Unexpected success: " + animal);
      case Result.Err<ZooError, Animal>(var error) -> assertEquals(expected, error);
    }
  }
}
