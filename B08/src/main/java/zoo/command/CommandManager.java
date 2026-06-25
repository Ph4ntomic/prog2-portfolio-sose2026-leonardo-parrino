package zoo.command;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import zoo.animal.Animal;
import zoo.result.Result;
import zoo.result.ZooError;

public final class CommandManager<T> {

  private static final Logger LOGGER = Logger.getLogger(CommandManager.class.getName());

  private final Deque<Command<? super T, ZooError, Animal>> undoStack = new ArrayDeque<>();
  private final Deque<Command<? super T, ZooError, Animal>> redoStack = new ArrayDeque<>();

  public Result<ZooError, Animal> executeCommand(
      Command<? super T, ZooError, Animal> command, T target) {
    var checkedCommand = Objects.requireNonNull(command, "command");
    var checkedTarget = Objects.requireNonNull(target, "target");
    LOGGER.info(() -> "executeCommand: " + checkedCommand.description());

    var result = checkedCommand.execute(checkedTarget);
    switch (result) {
      case Result.Ok<ZooError, Animal> _ -> {
        undoStack.push(checkedCommand);
        redoStack.clear();
      }
      case Result.Err<ZooError, Animal>(var error) -> logFailure(checkedCommand, error);
    }

    logState(checkedTarget);
    return result;
  }

  public Result<ZooError, Animal> undo(T target) {
    var checkedTarget = Objects.requireNonNull(target, "target");
    LOGGER.info(() -> "undo(target=%s)".formatted(checkedTarget));

    if (undoStack.isEmpty()) {
      var result = Result.<ZooError, Animal>err(ZooError.NOTHING_TO_UNDO);
      LOGGER.warning(() -> "undo failed: " + ZooError.NOTHING_TO_UNDO);
      logState(checkedTarget);
      return result;
    }

    var command = undoStack.pop();
    var result = command.undo(checkedTarget);
    switch (result) {
      case Result.Ok<ZooError, Animal> _ -> redoStack.push(command);
      case Result.Err<ZooError, Animal>(var error) -> {
        undoStack.push(command);
        logFailure(command, error);
      }
    }

    logState(checkedTarget);
    return result;
  }

  public Result<ZooError, Animal> redo(T target) {
    var checkedTarget = Objects.requireNonNull(target, "target");
    LOGGER.info(() -> "redo(target=%s)".formatted(checkedTarget));

    if (redoStack.isEmpty()) {
      var result = Result.<ZooError, Animal>err(ZooError.NOTHING_TO_REDO);
      LOGGER.warning(() -> "redo failed: " + ZooError.NOTHING_TO_REDO);
      logState(checkedTarget);
      return result;
    }

    var command = redoStack.pop();
    var result = command.execute(checkedTarget);
    switch (result) {
      case Result.Ok<ZooError, Animal> _ -> undoStack.push(command);
      case Result.Err<ZooError, Animal>(var error) -> {
        redoStack.push(command);
        logFailure(command, error);
      }
    }

    logState(checkedTarget);
    return result;
  }

  private static void logFailure(Command<?, ZooError, Animal> command, ZooError error) {
    LOGGER.log(Level.WARNING, () -> "%s failed: %s".formatted(command.description(), error));
  }

  private void logState(T target) {
    LOGGER.fine(
        () -> "target=%s, undo=%d, redo=%d".formatted(target, undoStack.size(), redoStack.size()));
  }
}
