package zoo.enclosure;

import zoo.animal.Reptile;

public final class Terrarium<T extends Reptile> extends Enclosure<T> {

  public Terrarium(String name) {
    super(name);
  }
}
