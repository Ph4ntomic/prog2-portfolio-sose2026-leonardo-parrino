package zoo.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;
import zoo.animal.Animal;
import zoo.animal.Lion;
import zoo.animal.Mammal;
import zoo.enclosure.Enclosure;
import zoo.enclosure.MammalHouse;
import zoo.result.Result;
import zoo.result.ZooError;

class AnimalCommandTest {

  @Test
  void addCommandExecutesAndUndoesItsChange() {
    Enclosure<Mammal> mammals = new MammalHouse<>("Mammals");
    var leo = new Lion("Leo");
    var command = new AddAnimalCommand<>(leo);

    assertSuccess(command.execute(mammals), leo);
    assertTrue(mammals.contains(leo));
    assertSuccess(command.undo(mammals), leo);
    assertFalse(mammals.contains(leo));
  }

  @Test
  void removeCommandExecutesAndUndoesItsChange() {
    Enclosure<Mammal> mammals = new MammalHouse<>("Mammals");
    var leo = new Lion("Leo");
    mammals.add(leo);
    var command = new RemoveAnimalCommand<>(leo);

    assertSuccess(command.execute(mammals), leo);
    assertFalse(mammals.contains(leo));
    assertSuccess(command.undo(mammals), leo);
    assertTrue(mammals.contains(leo));
  }

  @Test
  void commandsReturnDomainErrorsForInvalidStateChanges() {
    Enclosure<Mammal> mammals = new MammalHouse<>("Mammals");
    var leo = new Lion("Leo");
    var add = new AddAnimalCommand<>(leo);
    var remove = new RemoveAnimalCommand<>(leo);

    assertError(add.undo(mammals), ZooError.COMMAND_NOT_EXECUTED);
    assertError(remove.execute(mammals), ZooError.ANIMAL_NOT_FOUND);

    mammals.add(leo);
    assertError(add.execute(mammals), ZooError.ANIMAL_ALREADY_PRESENT);
    mammals.remove(leo);
    assertSuccess(add.execute(mammals), leo);
    assertError(add.execute(mammals), ZooError.COMMAND_ALREADY_EXECUTED);
  }

  @Test
  void removeCommandKeepsItsStateAfterFailedUndo() {
    Enclosure<Mammal> mammals = new MammalHouse<>("Mammals");
    var leo = new Lion("Leo");
    var remove = new RemoveAnimalCommand<>(leo);
    mammals.add(leo);

    assertSuccess(remove.execute(mammals), leo);
    assertError(remove.execute(mammals), ZooError.COMMAND_ALREADY_EXECUTED);

    mammals.add(leo);
    assertError(remove.undo(mammals), ZooError.ANIMAL_ALREADY_PRESENT);
    mammals.remove(leo);
    assertSuccess(remove.undo(mammals), leo);
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
