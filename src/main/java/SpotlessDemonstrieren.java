// nachher:
public class SpotlessDemonstrieren {
  public static void main(String[] args) {
    IO.println(System.getProperty("java.version"));
  }
}

/*
vorher:
}
}
absichtlich "hässlich" untereinander um Spotless zu demostrieren.

ausführen zum fixen:
.\gradlew.bat spotlessApply
in src/main/java/SpotelessDemostrieren.java
 */
