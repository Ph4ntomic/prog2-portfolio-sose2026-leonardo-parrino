package zoo.command;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import org.junit.jupiter.api.Test;
import zoo.animal.Mammal;
import zoo.enclosure.Enclosure;
import zoo.enclosure.MammalHouse;

class CommandManagerLoggingTest {

  @Test
  void managerLogsMethodStartFailureAndFinalState() {
    var logger = Logger.getLogger(CommandManager.class.getName());
    var previousLevel = logger.getLevel();
    var previousParentSetting = logger.getUseParentHandlers();
    var handler = new RecordingHandler();
    handler.setLevel(Level.ALL);

    logger.setUseParentHandlers(false);
    logger.setLevel(Level.ALL);
    logger.addHandler(handler);

    try {
      Enclosure<Mammal> mammals = new MammalHouse<>("Mammals");
      new CommandManager<Enclosure<Mammal>>().undo(mammals);

      assertTrue(handler.contains(Level.INFO, "undo(target="));
      assertTrue(handler.contains(Level.WARNING, "NOTHING_TO_UNDO"));
      assertTrue(handler.contains(Level.FINE, "target=MammalHouse"));
    } finally {
      logger.removeHandler(handler);
      logger.setLevel(previousLevel);
      logger.setUseParentHandlers(previousParentSetting);
    }
  }

  private static final class RecordingHandler extends Handler {

    private final List<LogRecord> records = new ArrayList<>();

    @Override
    public void publish(LogRecord record) {
      records.add(record);
    }

    @Override
    public void flush() {}

    @Override
    public void close() {}

    boolean contains(Level level, String text) {
      return records.stream()
          .anyMatch(
              record -> record.getLevel().equals(level) && record.getMessage().contains(text));
    }
  }
}
