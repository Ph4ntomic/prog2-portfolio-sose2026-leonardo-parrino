# A03 – LockSnake, Lambda-Ausdrücke, Methodenreferenzen und Observer-Pattern

## 1. Ziel der Bearbeitung

In dieser Aufgabe wurde das Projekt `prog2_ybel_locksnake` bearbeitet.

Repository der Lösung:

```text
https://github.com/Ph4ntomic/prog2_ybel_locksnake/tree/solution-locksnake
```

Ziel war es, das vorhandene LockSnake-Grundgerüst zu einem spielbaren Java-Swing-Spiel zu erweitern.

Dabei wurden besonders folgende Themen aus Programmieren 2 angewendet:

| Thema | Bedeutung |
|---|---|
| `GameState` | Modelliert den aktuellen Spielzustand |
| `GameEngine` | Steuert den Spielablauf |
| Observer-Pattern | Entkoppelt GUI, Eingabe und Spiellogik |
| Lambda-Ausdrücke | Kürzere Schreibweise für Verhalten |
| Methodenreferenzen | Direkte Weitergabe vorhandener Methoden |
| JUnit-Tests | Automatische Prüfung der Spiellogik |
| Gradle | Build, Test und Formatierung des Projekts |

---

## 2. Ausgangspunkt

Das Vorgabeprojekt enthielt bereits ein lauffähiges Grundgerüst mit Fenster, Rendering und Level-Laden.

Vorhanden waren unter anderem:

```text
Main
GamePanel
Java2DRenderer
Level
LevelLoader
CellType
Position
Direction
Pin
Snake
```

Die wichtigsten offenen Stellen lagen in:

```text
GameState
GameEngine
Observer-Anbindung
JUnit-Tests
```

Die GUI konnte bereits ein Fenster anzeigen. Die eigentliche Spielzustandslogik war aber noch nicht vollständig umgesetzt.

Deshalb wurde zuerst geklärt:

```text
Was gehört zur GUI?
Was gehört zur Spiellogik?
Was gehört zum Modell?
```

---

## 3. Projektstruktur

Die zentrale Struktur des Projekts liegt unter:

```text
src/main/java/de/hsbi/lockgame
```

Wichtige Pakete:

```text
logic
model
ui
io
settings
```

Bedeutung:

| Paket | Aufgabe |
|---|---|
| `logic` | Spiellogik, GameEngine, GameState, Observer |
| `model` | Datenklassen wie Snake, Pin, Position, Direction, Level |
| `ui` | Swing-Oberfläche und Rendering |
| `io` | Laden von Level-Dateien |
| `settings` | Konstanten für Spiel, Eingabe und Level |

Tests liegen unter:

```text
src/test/java/de/hsbi/lockgame/logic
```

Dort befindet sich die Testklasse:

```text
GameStateTest.java
```

---

## 4. Gradle-Konfiguration

Das Projekt wurde mit Gradle konfiguriert.

Wichtige Bestandteile in `build.gradle`:

```gradle
plugins {
    id 'java'
    id 'application'
    id 'com.diffplug.spotless' version '8.4.0'
}
```

Bedeutung:

| Plugin | Zweck |
|---|---|
| `java` | Java-Code kompilieren |
| `application` | Anwendung über Gradle starten |
| `spotless` | Codeformatierung prüfen und anwenden |

Die Main-Klasse wurde festgelegt:

```gradle
application {
    mainClass = 'de.hsbi.lockgame.Main'
}
```

JUnit wurde für Tests eingebunden:

```gradle
dependencies {
    testImplementation platform('org.junit:junit-bom:6.0.3')
    testImplementation 'org.junit.jupiter:junit-jupiter'
    testRuntimeOnly 'org.junit.platform:junit-platform-launcher'
}
```

Java wurde über die Toolchain auf Java 25 gesetzt:

```gradle
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}
```

Spotless wurde für einheitliche Formatierung eingerichtet:

```gradle
spotless {
    java {
        googleJavaFormat().aosp().reflowLongStrings()
        removeUnusedImports()
        trimTrailingWhitespace()
        endWithNewline()
    }
}
```

---

## 5. Spielzustand mit `GameState`

Die Klasse `GameState` ist der zentrale Teil der Spiellogik.

Sie speichert:

```text
Level
Snake
Pins
Status
pendingDirection
```

Bedeutung:

| Feld | Bedeutung |
|---|---|
| `level` | aktuelles Spielfeld |
| `snake` | aktuelle Schlange |
| `pins` | aktuelle Pins mit Zustand |
| `status` | läuft, gewonnen oder verloren |
| `pendingDirection` | vorgemerkte Bewegungsrichtung |

Der Spielzustand wird nicht direkt verändert, sondern bei Änderungen wird ein neuer Zustand erzeugt.

Das ist sauberer, weil dadurch alte und neue Zustände klar getrennt sind.

---

## 6. Ablauf von `tick()`

Die Methode `tick()` berechnet den nächsten Spielzustand.

Der Ablauf ist:

```text
1. Prüfen, ob das Spiel noch läuft.
2. Prüfen, ob alle Pins bereits gesetzt sind.
3. Prüfen, ob überhaupt eine Richtung gesetzt wurde.
4. Neue Kopfposition berechnen.
5. Prüfen, ob die Position außerhalb des Spielfelds liegt.
6. Prüfen, ob eine Wand getroffen wird.
7. Prüfen, ob die Schlange sich selbst trifft.
8. Prüfen, ob ein Pin getroffen wird.
9. Falls nichts blockiert: Schlange wächst in Bewegungsrichtung.
```

Dadurch ist die Reihenfolge der Spielregeln klar und nachvollziehbar.

---

## 7. Bewegungslogik

Die Bewegungsrichtung wird über `Direction` gesteuert.

Beispiele:

```java
Direction.UP
Direction.DOWN
Direction.LEFT
Direction.RIGHT
Direction.NONE
```

Wenn `Direction.NONE` gesetzt ist, bewegt sich die Schlange nicht.

Wenn eine Richtung gesetzt ist, wird aus der aktuellen Kopfposition die nächste Kopfposition berechnet.

Beispiel:

```text
Kopf steht bei (1, 1)
Richtung ist RIGHT
nächste Kopfposition ist (2, 1)
```

Danach entscheidet `GameState`, ob diese Bewegung erlaubt ist.

---

## 8. Wand und Spielfeldgrenzen

Zwei einfache Blockadefälle wurden umgesetzt:

```text
Out-of-Bounds
Wand
```

Out-of-Bounds bedeutet:

```text
Die Schlange verlässt das Spielfeld.
```

Dann wird der Status gesetzt auf:

```java
LOST_OUT_OF_BOUNDS
```

Eine Wand wird anders behandelt:

```text
Die Bewegung wird blockiert.
Die Schlange bleibt stehen.
Die Richtung wird auf NONE gesetzt.
Das Spiel läuft weiter.
```

Das ist wichtig, weil eine Wand nicht automatisch als Verlust behandelt wird.

---

## 9. Selbstkollision

Eine Selbstkollision entsteht, wenn die nächste Kopfposition bereits vom Körper der Schlange belegt ist.

Beispiel:

```text
Die Schlange läuft in ihren eigenen Körper.
```

Dann wird der Status gesetzt auf:

```java
LOST_SELF_COLLISION
```

Dafür wird in `Snake` geprüft, ob die neue Position bereits im Körper enthalten ist.

Wichtig ist dabei, dass `Position` korrekt über Koordinaten verglichen wird.

---

## 10. Wertgleichheit bei `Position`

Für die Spiellogik ist `Position` sehr wichtig.

Zwei Positionen mit denselben Koordinaten müssen als gleich gelten.

Beispiel:

```java
new Position(2, 3)
new Position(2, 3)
```

Diese beiden Objekte sind technisch zwei verschiedene Java-Objekte, aber logisch dieselbe Spielfeldposition.

Deshalb wurden in `Position` diese Methoden ergänzt:

```java
equals()
hashCode()
toString()
```

Dadurch funktionieren Vergleiche in Listen, Tests und Pin-Suchen korrekt.

Ohne diese Ergänzung könnten Selbstkollisionen oder Pin-Treffer fehlerhaft erkannt werden.

---

## 11. Pin-Mechanik

Die Pins sind ein zentraler Teil von LockSnake.

Ein Pin besitzt:

```text
Position
Zustand
Aktivierungsrichtung
```

Ein Pin kann zum Beispiel LOW oder HIGH sein.

Ein LOW-Pin kann nur gesetzt werden, wenn die Schlange aus der richtigen Richtung kommt.

Beispiel:

```text
Pin erwartet Aktivierung von links nach rechts.
Schlange kommt von links.
Pin wird gesetzt.
```

Wenn die Richtung falsch ist, wird der Pin nicht gesetzt.

Dann gilt:

```text
Die Bewegung wird blockiert.
Die Richtung wird auf NONE gesetzt.
Der Pin bleibt LOW.
```

Ein bereits gesetzter HIGH-Pin blockiert ebenfalls.

---

## 12. Gewinnbedingung

Das Spiel ist gewonnen, wenn alle Pins gesetzt sind.

Die Prüfung wurde mit Streams umgesetzt.

Sinngemäß:

```java
pins.stream()
    .map(Pin::state)
    .allMatch(Pin.State::isSet);
```

Bedeutung:

| Ausdruck | Bedeutung |
|---|---|
| `pins.stream()` | Pins als Datenstrom verarbeiten |
| `map(Pin::state)` | Zustand jedes Pins auslesen |
| `allMatch(Pin.State::isSet)` | prüfen, ob alle Pins gesetzt sind |

Wenn alle Pins gesetzt sind, wird der Status gesetzt auf:

```java
WON
```

---

## 13. `GameEngine`

Die Klasse `GameEngine` verwaltet den aktuellen `GameState`.

Ihre Aufgaben:

```text
Startzustand erzeugen
Tastatureingaben übernehmen
tick() ausführen
Observer benachrichtigen
```

Beim Erzeugen der Engine wird aus dem Level ein Startzustand erzeugt.

Die Schlange startet an der Startposition aus dem Level.

Sinngemäß:

```text
Level laden
Snake an Startposition erzeugen
Pins aus Level übernehmen
Status RUNNING setzen
Richtung NONE setzen
```

Dadurch ist die Engine der zentrale Vermittler zwischen GUI und Spielmodell.

---

## 14. Observer-Pattern

Das Observer-Pattern wurde verwendet, um Klassen zu entkoppeln.

Ein Observer ist ein Objekt, das automatisch benachrichtigt wird, wenn sich etwas ändert.

In dieser Aufgabe gibt es zwei Beobachtungsrichtungen:

```text
GameEngine -> GamePanel
GamePanel -> GameEngine
```

---

## 15. Beobachtungsrichtung 1: Engine zu GUI

Die `GameEngine` benachrichtigt das `GamePanel`, wenn sich der Spielzustand geändert hat.

Verbindung:

```java
engine.addObserver(panel);
```

Bedeutung:

```text
GamePanel beobachtet den Spielzustand.
Wenn die Engine einen neuen Zustand berechnet, bekommt das Panel diesen Zustand.
Danach zeichnet sich das Panel neu.
```

Im `GamePanel` passiert dann:

```java
this.state = newState;
repaint();
```

Dadurch muss die GUI den Zustand nicht ständig selbst abfragen.

---

## 16. Beobachtungsrichtung 2: GUI zu Engine

Das `GamePanel` verarbeitet Tastatureingaben.

Wenn eine Taste gedrückt wird, wird daraus eine `Direction`.

Diese Richtung wird an die `GameEngine` gemeldet.

Verbindung:

```java
panel.addObserver(engine);
```

Bedeutung:

```text
GameEngine beobachtet Tastatureingaben.
GamePanel meldet gedrückte Richtungen.
GameEngine merkt sich diese Richtung.
Beim nächsten tick() wird die Bewegung berechnet.
```

Damit ist auch die zweite geforderte Observer-Richtung umgesetzt.

---

## 17. Generisches Observer-Interface

Für das Observer-Pattern wurde ein generisches Interface verwendet.

Sinngemäß:

```java
@FunctionalInterface
public interface GameObserver<T> {
    void update(T value);
}
```

Bedeutung:

| Teil | Bedeutung |
|---|---|
| `@FunctionalInterface` | Interface besitzt genau eine abstrakte Methode |
| `<T>` | Datentyp ist flexibel |
| `update(T value)` | Observer erhält einen neuen Wert |

Dadurch kann dasselbe Interface für verschiedene Daten verwendet werden.

Beispiele:

```java
GameObserver<GameState>
GameObserver<Direction>
```

Das ist sauberer als zwei getrennte Interfaces für Spielzustand und Richtung.

---

## 18. Lambda-Ausdrücke

Ein Lambda-Ausdruck ist eine kurze Schreibweise für eine Funktion.

Beispiel aus dem Projekt:

```java
SwingUtilities.invokeLater(() -> { ... });
```

Bedeutung:

```text
Der Code im Lambda wird später im Swing-Event-Thread ausgeführt.
```

Weiteres Beispiel:

```java
e -> {
    engine.tick();
    handleGameEnd(e, engine.state(), frame);
}
```

Bedeutung:

```text
Bei jedem Timer-Ereignis wird ein Spielschritt ausgeführt.
Danach wird geprüft, ob das Spiel beendet ist.
```

Weitere Lambda-Ausdrücke kommen unter anderem bei `forEach`, Streams und Tests vor.

Dadurch wird die geforderte Anzahl von mindestens drei Lambda-Ausdrücken erfüllt.

---

## 19. Methodenreferenzen

Eine Methodenreferenz ist eine Kurzform für einen Lambda-Ausdruck, wenn nur eine vorhandene Methode weitergegeben wird.

Beispiel:

```java
InputConstants.BINDINGS.forEach(this::setupKeyBindings);
```

Bedeutung:

```text
Für jeden Eintrag in den Tastaturbindungen wird setupKeyBindings aufgerufen.
```

Weiteres Beispiel:

```java
.map(Pin::state)
```

Bedeutung:

```text
Für jeden Pin wird die Methode state() aufgerufen.
```

Weiteres Beispiel:

```java
.allMatch(Pin.State::isSet)
```

Bedeutung:

```text
Für jeden Pin-Zustand wird geprüft, ob er gesetzt ist.
```

Damit wird die geforderte Anzahl von mindestens zwei Methodenreferenzen erfüllt.

---

## 20. Swing-Timer

Das Spiel läuft über einen Swing-Timer.

Der Timer ruft regelmäßig `engine.tick()` auf.

Dadurch entsteht eine einfache Game-Loop.

Sinngemäß:

```text
Timer startet
alle X Millisekunden:
    engine.tick()
    Spielende prüfen
```

Wenn das Spiel gewonnen oder verloren wurde, wird der Timer gestoppt.

Danach wird eine Meldung angezeigt und das Fenster geschlossen.

---

## 21. Tastatursteuerung

Die Tastatursteuerung wird im `GamePanel` eingerichtet.

Dort werden Key-Bindings registriert.

Das bedeutet:

```text
Taste drücken
Direction erzeugen
Observer benachrichtigen
GameEngine erhält Richtung
```

Die GUI entscheidet dabei nicht selbst über die Bewegung.

Sie gibt nur die Eingabe weiter.

Die Spielregeln bleiben in `GameState`.

Das ist eine saubere Trennung von Eingabe und Spiellogik.

---

## 22. JUnit-Tests

Die Spiellogik wurde mit JUnit getestet.

Die Tests liegen in:

```text
src/test/java/de/hsbi/lockgame/logic/GameStateTest.java
```

Getestet wird vor allem `GameState`, weil dort die eigentlichen Spielregeln umgesetzt sind.

Die Tests verwenden kleine ASCII-Level direkt im Testcode.

Beispielhaft:

```text
#####
#S..#
#..v#
#####
```

Bedeutung:

| Zeichen | Bedeutung |
|---|---|
| `#` | Wand |
| `S` | Startposition der Schlange |
| `.` | leeres Feld |
| `^`, `v`, `<`, `>` | Pin mit Aktivierungsrichtung |

Dadurch sind die Tests unabhängig von externen Level-Dateien und gut reproduzierbar.

---

## 23. Abgedeckte Testfälle

Die Tests decken die wichtigsten Kernfälle der Aufgabe ab.

| Testfall | Bedeutung |
|---|---|
| Initialzustand | Startwerte werden korrekt gespeichert |
| Tick ohne Richtung | Zustand bleibt unverändert |
| Bewegung auf freies Feld | Schlange bewegt sich und wächst |
| Wand voraus | Bewegung wird blockiert |
| Spielfeld verlassen | Status wird `LOST_OUT_OF_BOUNDS` |
| Selbstkollision | Status wird `LOST_SELF_COLLISION` |
| Pin aus falscher Richtung | Pin bleibt LOW |
| Pin aus richtiger Richtung | Pin wird HIGH |
| HIGH-Pin voraus | Bewegung wird blockiert |
| letzter Pin gesetzt | Status wird `WON` |
| alle Pins bereits gesetzt | Status wird `WON` |
| Spiel bereits beendet | Zustand bleibt unverändert |

Damit werden mehr als zehn sinnvolle Tests erreicht.

---

## 24. Given-When-Then-Prinzip

Die Tests folgen inhaltlich dem Given-When-Then-Prinzip.

Bedeutung:

```text
given: Ausgangslage vorbereiten
when: Aktion ausführen
then: Ergebnis prüfen
```

Beispiel:

```text
given: Schlange steht vor einem Pin
when: tick() wird mit Richtung RIGHT ausgeführt
then: Pin wird gesetzt oder blockiert abhängig von der Aktivierungsrichtung
```

Dadurch ist jeder Testfall fachlich nachvollziehbar.

---

## 25. Wichtige Gradle-Befehle

Projekt bauen:

```powershell
.\gradlew.bat assemble
```

Zweck:

```text
Kompiliert den Java-Code.
```

---

Tests ausführen:

```powershell
.\gradlew.bat test
```

Zweck:

```text
Führt alle JUnit-Tests aus.
```

---

Formatierung anwenden:

```powershell
.\gradlew.bat spotlessApply
```

Zweck:

```text
Formatiert den Java-Code automatisch.
```

---

Formatierung prüfen:

```powershell
.\gradlew.bat spotlessCheck
```

Zweck:

```text
Prüft, ob die Java-Dateien korrekt formatiert sind.
```

---

Alles prüfen:

```powershell
.\gradlew.bat check
```

Zweck:

```text
Führt zentrale Prüfungen wie Tests und Formatchecks aus.
```

---

Anwendung starten:

```powershell
.\gradlew.bat run
```

Zweck:

```text
Startet das LockSnake-Spiel.
```

---

## 26. Ergebnis der Umsetzung

Umgesetzt wurden:

```text
GameState als zentrale Spiellogik
GameEngine als Steuerung des Spiels
Observer-Pattern zwischen Engine und GUI
Observer-Pattern zwischen GUI und Engine
Pin-Mechanik mit Aktivierungsrichtung
Wandbehandlung
Out-of-Bounds-Behandlung
Selbstkollision
Gewinnbedingung
JUnit-Tests für die Kernlogik
Lambda-Ausdrücke
Methodenreferenzen
Gradle-Konfiguration mit JUnit und Spotless
```

Die Lösung ist damit nicht nur lauffähig, sondern auch testbar und strukturiert.

---

## 27. Checkliste

| Anforderung | Status |
|---|---|
| Vorgabeprojekt geforkt / geklont | erledigt |
| Gradle eingerichtet | erledigt |
| Java-Application-Plugin eingerichtet | erledigt |
| JUnit 6 eingebunden | erledigt |
| Spotless eingebunden | erledigt |
| `GameState` umgesetzt | erledigt |
| `GameEngine` umgesetzt | erledigt |
| Wandbehandlung umgesetzt | erledigt |
| Pin blockiert korrekt | erledigt |
| Pin aktivierbar | erledigt |
| Selbstkollision umgesetzt | erledigt |
| Gewinnbedingung umgesetzt | erledigt |
| Observer: `GamePanel` beobachtet `GameState` | erledigt |
| Observer: `GameEngine` beobachtet `Direction` | erledigt |
| mindestens 3 Lambda-Ausdrücke | erledigt |
| mindestens 2 Methodenreferenzen | erledigt |
| mindestens 10 JUnit-Tests | erledigt |
| Given-When-Then-Teststruktur | erledigt |

---

## 28. Typische Fehlerquellen

Bei dieser Aufgabe waren besonders diese Punkte fehleranfällig:

```text
Positionen nur über Objektidentität vergleichen
Pin-Feld versehentlich betreten lassen
Pin aus falscher Richtung trotzdem setzen
HIGH-Pin nicht mehr blockieren lassen
nach WON oder LOST weiterrechnen
Observer registrieren, aber nicht benachrichtigen
GUI und Spiellogik vermischen
Tests zu stark von echten Level-Dateien abhängig machen
Off-by-One-Fehler bei Koordinaten
```

---

## 29. Lernzusammenfassung

Diese Aufgabe zeigt gut, warum eine saubere Trennung zwischen Modell, Spiellogik und GUI wichtig ist.

Die GUI zeigt nur den Zustand an und verarbeitet Tastatureingaben. Die eigentlichen Spielregeln liegen in `GameState`. Die `GameEngine` verbindet Eingabe, Zustand und Benachrichtigung.

Das Observer-Pattern sorgt dafür, dass Klassen nicht direkt voneinander abhängig sein müssen. Das `GamePanel` bekommt automatisch neue Spielzustände, und die `GameEngine` bekommt automatisch neue Richtungen.

Lambda-Ausdrücke und Methodenreferenzen machen den Code kürzer, wenn Verhalten oder vorhandene Methoden weitergegeben werden. Besonders bei Swing, Streams und Observer-Listen ist das praktisch.

JUnit-Tests sichern die wichtigsten Regeln ab und machen sichtbar, ob spätere Änderungen die Spiellogik beschädigen.

---

## 30. Git-Vorgang für diese Portfolio-Datei

Status prüfen:

```powershell
git status
```

Zweck:

```text
Prüfen, ob die neue Markdown-Datei erkannt wird.
```

---

Datei vormerken:

```powershell
git add B03/A03_Lambda-Ausdrücke_Methodenreferenzen_Observer-Pattern.md
```

Zweck:

```text
Die Portfolio-Dokumentation für den Commit vormerken.
```

---

Commit erstellen:

```powershell
git commit -m "docs: add locksnake observer writeup"
```

Zweck:

```text
Die Vorgangsbeschreibung lokal speichern.
```

---

Pushen:

```powershell
git push
```

Zweck:

```text
Die Portfolio-Datei auf GitHub hochladen.
```

---

## 31. Kurzfazit

In der LockSnake-Aufgabe wurde ein vorhandenes Java-Swing-Projekt zu einem spielbaren und testbaren Spiel erweitert.

Die zentrale Logik liegt in `GameState`. Die `GameEngine` steuert den Ablauf und verteilt Änderungen über das Observer-Pattern. Das `GamePanel` zeigt den Zustand an und meldet Tastatureingaben zurück.

Durch JUnit-Tests, Gradle, Spotless, Lambda-Ausdrücke und Methodenreferenzen erfüllt die Lösung die fachlichen Anforderungen der Aufgabe und ist gleichzeitig als Lernbeispiel für spätere Aufgaben gut nachvollziehbar.