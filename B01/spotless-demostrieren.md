# B01 – Spotless demonstrieren

## Ausführung

Die Formatierung wurde über Gradle mit dem Plugin Spotless ausgeführt.

Spotless wurde verwendet, um Java-Code automatisch nach festen Formatierungsregeln zu formatieren.

## Datei

```text
src/main/java/SpotlessDemonstrieren.java
```

## Programm vorher

Der Code wurde absichtlich schlecht eingerückt, um die Wirkung von Spotless zu demonstrieren.

```java
public class SpotlessDemonstrieren {
public static void main(String[] args) {
IO.println(System.getProperty("java.version"));
}
}
```

## Ausgeführter Befehl zur Formatierung

```powershell
.\gradlew.bat spotlessApply
```

## Konsolenausgabe nach `spotlessApply`

```text
BUILD SUCCESSFUL
```

## Programm nachher

Nach der Ausführung von `spotlessApply` wurde der Code automatisch formatiert.

```java
public class SpotlessDemonstrieren {
  public static void main(String[] args) {
    IO.println(System.getProperty("java.version"));
  }
}
```

## Ausgeführter Befehl zur Prüfung

```powershell
.\gradlew.bat spotlessCheck
```

## Konsolenausgabe nach `spotlessCheck`

```text
BUILD SUCCESSFUL
```

## Ergebnis

Spotless ist im Gradle-Projekt eingebunden.

Mit `spotlessApply` kann Java-Code automatisch formatiert werden.

Mit `spotlessCheck` kann geprüft werden, ob der Java-Code korrekt formatiert ist.

Die Aufgabe ist damit erfüllt.