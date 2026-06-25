package zoo.result;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import org.junit.jupiter.api.Test;

class ResultTest {

  @Test
  void resultRepresentsSuccessAndFailure() {
    Result<ZooError, String> success = Result.ok("done");
    Result<ZooError, String> failure = Result.err(ZooError.ANIMAL_NOT_FOUND);

    var ok = assertInstanceOf(Result.Ok.class, success);
    var err = assertInstanceOf(Result.Err.class, failure);

    assertEquals("done", ok.value());
    assertEquals(ZooError.ANIMAL_NOT_FOUND, err.error());
  }
}
