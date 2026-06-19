package zoo;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import zoo.animal.Fish;
import zoo.animal.Trout;
import zoo.enclosure.Aquarium;

public final class ZooDemoMain {

  private ZooDemoMain() {}

  public static void main(String[] args) {
    setLogLevel(Level.FINE);

    var zoo = new Zoo();
    var aquarium = new Aquarium<Fish>("River Aquarium");

    zoo.addEnclosure(aquarium);
    zoo.admitAnimal(aquarium, new Trout("Toni"));
    zoo.summary();

    setLogLevel(Level.WARNING);
    zoo.findEnclosureByName("Missing Enclosure");
  }

  private static void setLogLevel(Level level) {
    var zooLogger = Logger.getLogger(Zoo.class.getName());
    zooLogger.setLevel(level);

    var rootLogger = Logger.getLogger("");
    rootLogger.setLevel(level);
    for (Handler handler : rootLogger.getHandlers()) {
      handler.setLevel(level);
    }
  }
}
