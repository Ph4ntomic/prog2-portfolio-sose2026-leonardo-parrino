package zoo;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import zoo.animal.Lion;
import zoo.animal.Mammal;
import zoo.command.AddAnimalCommand;
import zoo.command.CommandManager;
import zoo.enclosure.Enclosure;
import zoo.enclosure.MammalHouse;

public final class CommandDemoMain {

  private CommandDemoMain() {}

  public static void main(String[] args) {
    setLogLevel(Level.FINE);

    Enclosure<Mammal> mammalHouse = new MammalHouse<>("Mammal House");
    var manager = new CommandManager<Enclosure<Mammal>>();
    var addLion = new AddAnimalCommand<>(new Lion("Leo"));

    manager.executeCommand(addLion, mammalHouse);
    manager.undo(mammalHouse);
    manager.redo(mammalHouse);
  }

  private static void setLogLevel(Level level) {
    var rootLogger = Logger.getLogger("");
    rootLogger.setLevel(level);
    for (Handler handler : rootLogger.getHandlers()) {
      handler.setLevel(level);
    }
  }
}
