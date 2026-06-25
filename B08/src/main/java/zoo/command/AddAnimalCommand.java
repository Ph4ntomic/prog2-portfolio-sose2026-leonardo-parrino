package zoo.command;

import java.util.Objects;
import zoo.animal.Animal;
import zoo.enclosure.Enclosure;
import zoo.result.Result;
import zoo.result.ZooError;

public final class AddAnimalCommand<T extends Animal>
    implements Command<Enclosure<? super T>, ZooError, Animal> {

  private final T animal;
  private boolean executed;

  public AddAnimalCommand(T animal) {
    this.animal = Objects.requireNonNull(animal, "animal");
  }

  @Override
  public Result<ZooError, Animal> execute(Enclosure<? super T> target) {
    Objects.requireNonNull(target, "target");
    if (executed) {
      return Result.err(ZooError.COMMAND_ALREADY_EXECUTED);
    }
    if (!target.add(animal)) {
      return Result.err(ZooError.ANIMAL_ALREADY_PRESENT);
    }

    executed = true;
    return Result.ok(animal);
  }

  @Override
  public Result<ZooError, Animal> undo(Enclosure<? super T> target) {
    Objects.requireNonNull(target, "target");
    if (!executed) {
      return Result.err(ZooError.COMMAND_NOT_EXECUTED);
    }
    if (!target.remove(animal)) {
      return Result.err(ZooError.ANIMAL_NOT_FOUND);
    }

    executed = false;
    return Result.ok(animal);
  }

  @Override
  public String description() {
    return "Add animal '%s'".formatted(animal.name());
  }
}
