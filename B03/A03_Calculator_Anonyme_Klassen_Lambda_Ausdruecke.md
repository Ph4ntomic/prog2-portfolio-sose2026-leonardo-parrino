# Aufgabe 1: Calculator – Gradle, anonyme Klassen und Lambda-Ausdrücke

## 1. Kurzüberblick

In dieser Aufgabe wurde das Vorgabeprojekt `prog2_ybel_calculator` eingerichtet und erweitert.

Umgesetzt wurden:

1. Repository lokal geklont und in IntelliJ IDEA geöffnet.
2. Gradle-Konfiguration ergänzt.
3. Gradle Wrapper erzeugt.
4. Projekt mit Gradle gebaut und gestartet.
5. Neue Operation `Sub` als normale Java-Klasse erstellt.
6. Operation `Sub` im Calculator eingebunden.
7. Operation `Mul` als anonyme Klasse umgesetzt.
8. Operation `Div` als Lambda-Ausdruck umgesetzt.
9. Den `ActionListener` der `JComboBox` von anonymer Klasse zu Lambda-Ausdruck umgebaut.
10. Unnötige Imports entfernt.
11. Änderungen sauber in Git committed.
12. Push-Problem erkannt: lokales Repository zeigte noch auf das Hochschul-Originalrepo statt auf den eigenen Fork.

---

## 2. Ausgangspunkt

Das Projekt wurde lokal unter folgendem Pfad bearbeitet:

```text
C:\Users\leona\Documents\GitHub\prog2_ybel_calculator
```

Die ursprüngliche Projektstruktur enthielt bereits Java-Dateien im Package `calculator`:

```text
prog2_ybel_calculator
├── src
│   └── main
│       └── java
│           └── calculator
│               ├── Add.java
│               ├── Calculator.java
│               ├── Main.java
│               └── Operation.java
├── README.md
├── LICENSE.md
└── .gitignore
```

Es fehlten zunächst die Gradle-Dateien.

---

## 3. Gradle-Konfiguration

### 3.1 Ziel

Das Projekt sollte über Gradle gebaut und gestartet werden können.

Dafür wurden im Hauptordner des Projekts diese Dateien erstellt:

```text
build.gradle
settings.gradle
```

Wichtig:

Die Dateien liegen direkt im Hauptordner:

```text
prog2_ybel_calculator
```

Nicht hier:

```text
src
```

Nicht hier:

```text
src/main/java
```

Nicht hier:

```text
src/main/java/calculator
```

---

## 4. Datei `settings.gradle`

### Datei

```text
settings.gradle
```

### Inhalt

```gradle
rootProject.name = 'calculator'
```

### Erklärung

`settings.gradle` legt den Namen des Gradle-Projekts fest.

Hier heißt das Projekt:

```text
calculator
```

---

## 5. Datei `build.gradle`

### Datei

```text
build.gradle
```

### Inhalt

```gradle
plugins {
    id 'java'
    id 'application'
}

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

application {
    mainClass = 'calculator.Main'
}
```

### Erklärung

```gradle
plugins {
    id 'java'
    id 'application'
}
```

Aktiviert Java-Unterstützung und macht das Projekt als ausführbare Java-Anwendung startbar.

```gradle
repositories {
    mavenCentral()
}
```

Legt Maven Central als Paketquelle fest. Für diese Aufgabe werden zwar keine externen Bibliotheken benötigt, aber es ist eine übliche Gradle-Grundkonfiguration.

```gradle
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}
```

Legt Java 25 als Java-Version für das Projekt fest.

```gradle
application {
    mainClass = 'calculator.Main'
}
```

Legt fest, welche Klasse beim Start mit Gradle ausgeführt wird.

Hier ist die Startklasse:

```text
calculator.Main
```

---

## 6. Gradle Wrapper erzeugen

Im Projektordner wurde folgender Befehl ausgeführt:

```powershell
gradle wrapper --gradle-version 9.4.1
```

### Zweck

Der Gradle Wrapper sorgt dafür, dass das Projekt mit einer festgelegten Gradle-Version gebaut werden kann.

Dadurch muss nicht jeder Entwickler manuell dieselbe Gradle-Version installieren.

### Ergebnis

Es wurden diese Dateien erzeugt:

```text
gradlew
gradlew.bat
gradle/wrapper/gradle-wrapper.jar
gradle/wrapper/gradle-wrapper.properties
```

Für Windows wird meistens diese Datei genutzt:

```text
gradlew.bat
```

---

## 7. Gradle testen

### Build ausführen

```powershell
.\gradlew.bat build
```

### Zweck

Der Befehl kompiliert das Projekt und prüft, ob der Code technisch baubar ist.

### Programm starten

```powershell
.\gradlew.bat run
```

### Zweck

Der Befehl startet die in `build.gradle` definierte Main-Klasse:

```text
calculator.Main
```

### Beobachtung

Beim Start öffnet sich ein kleines Calculator-Fenster.

Das Terminal zeigt währenddessen zum Beispiel:

```text
> :run
```

oder:

```text
75% EXECUTING
```

Das ist bei GUI-Programmen normal.

Grund:

Das Java-Programm läuft weiter, solange das Fenster geöffnet ist.

Erst wenn das Calculator-Fenster geschlossen wird, beendet sich auch der Gradle-Task.

---

## 8. Gradle-Teil committen

Nach erfolgreichem Gradle-Test wurde der Gradle-Teil separat committed.

### Status prüfen

```powershell
git status
```

### Gradle-Dateien stagen

```powershell
git add build.gradle settings.gradle gradlew gradlew.bat gradle/wrapper/gradle-wrapper.jar gradle/wrapper/gradle-wrapper.properties
```

### Commit erstellen

```powershell
git commit -m "build: add Gradle configuration"
```

### Erklärung

Der Commit-Typ `build` passt, weil hier die Build-Konfiguration geändert wurde.

Es wurde noch keine fachliche Calculator-Funktion umgesetzt, sondern nur das Build-System eingerichtet.

---

## 9. Neue Klasse `Sub.java`

### Ziel

Es sollte eine neue Operation `Sub` erstellt werden.

Anforderung:

```text
Erstellen Sie eine neue Java-Klasse Sub, die das Interface Operation implementiert und eine Subtraktion bereitstellt.
Nutzen Sie hier keine anonymen Klassen oder Lambda-Ausdrücke.
```

---

## 10. Datei `Sub.java`

### Pfad

```text
src/main/java/calculator/Sub.java
```

### Inhalt

```java
package calculator;

public class Sub implements Operation {
    @Override
    public int doOperation(int a, int b) {
        return a - b;
    }
}
```

### Erklärung

```java
public class Sub implements Operation
```

bedeutet:

Die Klasse `Sub` implementiert das Interface `Operation`.

`implements` heißt:

Eine Klasse verpflichtet sich, die Methoden eines Interfaces bereitzustellen.

Das Interface `Operation` verlangt die Methode:

```java
int doOperation(int a, int b);
```

Deshalb muss `Sub` diese Methode überschreiben:

```java
@Override
public int doOperation(int a, int b) {
    return a - b;
}
```

### Ergebnis

Die Klasse `Sub` berechnet:

```text
a - b
```

Beispiel:

```text
8 - 3 = 5
```

---

## 11. `Sub.java` committen

### Datei stagen

```powershell
git add src/main/java/calculator/Sub.java
```

### Commit erstellen

```powershell
git commit -m "feat: add calculator sub operation"
```

### Erklärung

Der Commit-Typ `feat` passt, weil eine neue fachliche Funktion ergänzt wurde.

Die neue Funktion ist die Subtraktion.

---

## 12. `Calculator.java` bearbeiten

### Datei

```text
src/main/java/calculator/Calculator.java
```

### Gesuchte Methode

```java
private void setupOperationSelector()
```

Diese Methode verwaltet die auswählbaren Rechenoperationen.

Die Operationen werden in einer Map gespeichert:

```java
operations = new HashMap<>();
```

Dabei ist der Name der Operation der Schlüssel, zum Beispiel:

```text
Add
Sub
Mul
Div
```

Das zugehörige Objekt führt die Berechnung aus.

---

## 13. Ausgangspunkt in `Calculator.java`

Vorhanden war bereits:

```java
operations = new HashMap<>();

operations.put("Add", new Add());
```

`Add` war also bereits eingebunden.

Danach befanden sich mehrere TODO-Stellen für:

```text
Sub
Mul
Div
ActionListener
```

---

## 14. Operation `Sub` einbinden

### Code

```java
operations.put("Sub", new Sub());
```

### Erklärung

Hier wird eine Instanz der Klasse `Sub` erstellt:

```java
new Sub()
```

Diese Instanz wird unter dem Namen `"Sub"` in die Map `operations` eingefügt.

Dadurch erscheint `Sub` später im Dropdown des Calculators.

### Warum normale Klasse?

Die Aufgabe verlangt für `Sub` ausdrücklich:

```text
keine anonymen Klassen
keine Lambda-Ausdrücke
```

Deshalb wurde `Sub` als eigene Datei `Sub.java` erstellt.

---

## 15. Operation `Mul` als anonyme Klasse

### Code

```java
operations.put(
        "Mul",
        new Operation() {
            @Override
            public int doOperation(int a, int b) {
                return a * b;
            }
        });
```

### Erklärung

Hier wird keine eigene Datei `Mul.java` erstellt.

Stattdessen wird direkt ein namenloses Objekt erzeugt:

```java
new Operation() {
    ...
}
```

Das ist eine anonyme Klasse.

Eine anonyme Klasse ist eine Klasse ohne eigenen Klassennamen.

Sie wird direkt an der Stelle erzeugt, an der sie gebraucht wird.

### Bedeutung

```java
new Operation() {
    @Override
    public int doOperation(int a, int b) {
        return a * b;
    }
}
```

bedeutet:

Erzeuge ein Objekt, das `Operation` implementiert.

Dieses Objekt überschreibt die Methode `doOperation`.

Die Methode berechnet:

```text
a * b
```

Beispiel:

```text
8 * 3 = 24
```

---

## 16. Operation `Div` als Lambda-Ausdruck

### Code

```java
operations.put("Div", (a, b) -> a / b);
```

### Erklärung

Ein Lambda-Ausdruck ist eine kurze Schreibweise für die Implementierung eines funktionalen Interfaces.

Ein funktionales Interface ist ein Interface mit genau einer abstrakten Methode.

`Operation` passt dafür, weil es nur diese Methode verlangt:

```java
int doOperation(int a, int b);
```

Deshalb kann Java diesen Lambda-Ausdruck verwenden:

```java
(a, b) -> a / b
```

### Bedeutung

```text
Nimm zwei Werte a und b und gib a / b zurück.
```

### Wichtig: Integerdivision

Da `a` und `b` Integer-Werte sind, führt Java eine Integerdivision aus.

Beispiel:

```text
8 / 3 = 2
```

Nicht:

```text
8 / 3 = 2.666...
```

Der Nachkommabereich wird bei Integerdivision abgeschnitten.

---

## 17. ActionListener von anonymer Klasse zu Lambda

### Ziel

Die Aufgabe verlangte:

```text
Für die JComboBox operationSelector wird ein ActionListener mit Hilfe einer anonymen Klasse definiert.
Konvertieren Sie dies in einen entsprechenden Lambda-Ausdruck.
```

---

## 18. Vorheriger Code mit anonymer Klasse

Vorher stand sinngemäß:

```java
operationSelector.addActionListener(
        new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    result.setText("" + calculate());
                } catch (NumberFormatException ex) {
                    System.out.println("Invalid input.");
                }
            }
        });
```

### Erklärung

```java
new ActionListener() {
    ...
}
```

erzeugt ein namenloses Objekt, das `ActionListener` implementiert.

`ActionListener` reagiert auf ein Ereignis der Oberfläche.

Hier ist das Ereignis:

```text
Die ausgewählte Operation in der JComboBox wird geändert.
```

Dann wird diese Methode ausgeführt:

```java
actionPerformed(ActionEvent e)
```

---

## 19. Neuer Code mit Lambda-Ausdruck

Nachher steht:

```java
operationSelector.addActionListener(
        e -> {
            try {
                result.setText("" + calculate());
            } catch (NumberFormatException ex) {
                System.out.println("Invalid input.");
            }
        });
```

### Erklärung

```java
e -> {
    ...
}
```

bedeutet:

Wenn das Ereignis `e` passiert, führe den Code im Block aus.

Java weiß automatisch, dass `e` ein `ActionEvent` ist, weil `addActionListener(...)` einen `ActionListener` erwartet.

Deshalb muss `ActionEvent e` nicht mehr ausgeschrieben werden.

---

## 20. Entfernte Imports

Nach der Lambda-Umwandlung wurden diese Imports entfernt:

```java
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
```

### Grund

Diese Klassennamen werden nach der Umwandlung nicht mehr direkt im Code verwendet.

Der Code nutzt nur noch:

```java
e -> {
    ...
}
```

Deshalb sind die Imports überflüssig.

---

## 21. Vollständiger relevanter Abschnitt aus `Calculator.java`

```java
private void setupOperationSelector() {
    operations = new HashMap<>();

    operations.put("Add", new Add());

    // Sub als normale eigene Klasse.
    operations.put("Sub", new Sub());

    // Mul als anonyme Klasse.
    operations.put(
            "Mul",
            new Operation() {
                @Override
                public int doOperation(int a, int b) {
                    return a * b;
                }
            });

    // Div als Lambda-Ausdruck.
    operations.put("Div", (a, b) -> a / b);

    operationSelector = new JComboBox<>();

    operations.forEach((key, value) -> operationSelector.addItem(key));

    // ActionListener als Lambda-Ausdruck.
    operationSelector.addActionListener(
            e -> {
                try {
                    result.setText("" + calculate());
                } catch (NumberFormatException ex) {
                    System.out.println("Invalid input.");
                }
            });
}
```

---

## 22. Technische Begriffe

### Interface

Ein Interface beschreibt, welche Methoden eine Klasse bereitstellen muss.

Beispiel:

```java
public interface Operation {
    int doOperation(int a, int b);
}
```

Jede Klasse, die `Operation` implementiert, muss `doOperation` besitzen.

---

### `implements`

`implements` bedeutet:

Eine Klasse erfüllt ein Interface.

Beispiel:

```java
public class Sub implements Operation
```

Bedeutung:

`Sub` verpflichtet sich, alle Methoden aus `Operation` bereitzustellen.

---

### Anonyme Klasse

Eine anonyme Klasse ist eine Klasse ohne eigenen Namen.

Sie wird direkt dort erzeugt, wo sie gebraucht wird.

Beispiel:

```java
new Operation() {
    @Override
    public int doOperation(int a, int b) {
        return a * b;
    }
}
```

Vorteil:

Man braucht keine eigene Datei `Mul.java`.

Nachteil:

Bei größeren Implementierungen wird der Code schnell unübersichtlich.

---

### Lambda-Ausdruck

Ein Lambda-Ausdruck ist eine kurze Schreibweise für ein funktionales Interface.

Beispiel:

```java
(a, b) -> a / b
```

Bedeutung:

Nimm `a` und `b` und gib `a / b` zurück.

---

### Funktionales Interface

Ein funktionales Interface ist ein Interface mit genau einer abstrakten Methode.

Beispiele aus dieser Aufgabe:

```text
Operation
ActionListener
```

Deshalb sind Lambda-Ausdrücke hier möglich.

---

### ActionListener

Ein `ActionListener` reagiert auf Ereignisse in einer grafischen Oberfläche.

Beispiele:

```text
Button gedrückt
Dropdown-Auswahl geändert
Menüpunkt ausgewählt
```

Im Calculator reagiert der Listener auf Änderungen der `JComboBox`.

---

## 23. Test der Aufgabe

### Build ausführen

```powershell
.\gradlew.bat build
```

### Zweck

Der Code wird kompiliert.

Wenn Syntaxfehler vorhanden sind, zeigt Gradle Datei und Zeile an.

### Programm starten

```powershell
.\gradlew.bat run
```

### Zweck

Der Calculator wird als Swing-Fenster gestartet.

---

## 24. Manuelle Testfälle im Calculator

Im geöffneten Fenster wurden die Operationen getestet.

### Addition

```text
8 Add 3 = 11
```

### Subtraktion

```text
8 Sub 3 = 5
```

### Multiplikation

```text
8 Mul 3 = 24
```

### Division

```text
8 Div 3 = 2
```

Wichtig:

`Div` ist Integerdivision.

Deshalb ergibt `8 / 3` den Wert `2`.

---

## 25. `Calculator.java` committen

Wenn in `Calculator.java` alle Operationen und der ActionListener umgesetzt wurden:

```powershell
git status
```

### Zweck

Prüft, welche Dateien geändert oder staged sind.

---

```powershell
git add src/main/java/calculator/Calculator.java
```

### Zweck

Staged die geänderte Datei `Calculator.java`.

---

```powershell
git commit -m "feat: implement calculator operation selector"
```

### Zweck

Speichert die vollständige Umsetzung des Operation Selectors.

Der Commit-Name passt, weil in `setupOperationSelector()` mehrere fachliche Erweiterungen umgesetzt wurden:

```text
Sub eingebunden
Mul ergänzt
Div ergänzt
ActionListener umgebaut
```

---

## 26. Alternative Commit-Namen

Für den Gradle-Teil:

```powershell
git commit -m "build: add Gradle configuration"
```

Für nur `Sub.java`:

```powershell
git commit -m "feat: add calculator sub operation"
```

Für `Sub`, `Mul`, `Div` und Operation Selector:

```powershell
git commit -m "feat: implement calculator operation selector"
```

Für nur die Umwandlung des ActionListeners:

```powershell
git commit -m "refactor: convert action listener to lambda"
```

---

## 27. Push-Problem

Beim Push kam dieser Fehler:

```text
remote: Permission to Programmiermethoden-CampusMinden/prog2_ybel_calculator.git denied to Ph4ntomic.
fatal: unable to access 'https://github.com/Programmiermethoden-CampusMinden/prog2_ybel_calculator.git/': The requested URL returned error: 403
```

### Bedeutung

Der lokale `origin` zeigte noch auf das Hochschul-Originalrepo:

```text
https://github.com/Programmiermethoden-CampusMinden/prog2_ybel_calculator.git
```

Auf dieses Repository hat man keine Schreibrechte.

Deshalb kann man dort nicht pushen.

---

## 28. Remote prüfen

```powershell
git remote -v
```

### Ausgabe

```text
origin  https://github.com/Programmiermethoden-CampusMinden/prog2_ybel_calculator.git (fetch)
origin  https://github.com/Programmiermethoden-CampusMinden/prog2_ybel_calculator.git (push)
```

### Erklärung

`origin` zeigt aktuell auf das falsche Push-Ziel.

Es zeigt auf das Hochschul-Repo.

Richtig wäre ein eigener Fork unter dem eigenen GitHub-Account.

---

## 29. Lösung für Push-Problem

### Schritt 1: Eigenen Fork auf GitHub erstellen

Auf GitHub das Originalrepo öffnen:

```text
https://github.com/Programmiermethoden-CampusMinden/prog2_ybel_calculator
```

Dann:

```text
Fork
Owner: Ph4ntomic
Create fork
```

Danach existiert das eigene Repo:

```text
https://github.com/Ph4ntomic/prog2_ybel_calculator
```

---

## 30. Remote-Ziele korrigieren

### Original-Repo umbenennen

```powershell
git remote rename origin upstream
```

### Zweck

Das Hochschul-Repo wird nicht gelöscht, sondern als `upstream` behalten.

`upstream` bedeutet hier:

```text
Originalquelle der Vorgabe
```

---

### Eigenen Fork als neues `origin` setzen

```powershell
git remote add origin https://github.com/Ph4ntomic/prog2_ybel_calculator.git
```

### Zweck

Der eigene Fork wird als neues Push-Ziel eingetragen.

---

### Prüfen

```powershell
git remote -v
```

### Erwartete Ausgabe

```text
origin    https://github.com/Ph4ntomic/prog2_ybel_calculator.git (fetch)
origin    https://github.com/Ph4ntomic/prog2_ybel_calculator.git (push)
upstream  https://github.com/Programmiermethoden-CampusMinden/prog2_ybel_calculator.git (fetch)
upstream  https://github.com/Programmiermethoden-CampusMinden/prog2_ybel_calculator.git (push)
```

---

## 31. Pushen

```powershell
git push -u origin master
```

### Zweck

Der lokale Branch `master` wird in den eigenen Fork hochgeladen.

### Wirkung

Die lokalen Commits erscheinen auf GitHub im eigenen Repository.

Danach reicht künftig meistens:

```powershell
git push
```

---

## 32. Wichtige Git-Erkenntnis

Ein Clone ist nur die lokale Kopie auf dem eigenen PC.

Ein Commit speichert Änderungen lokal.

Ein Push lädt lokale Commits zu GitHub hoch.

Wenn das Remote-Ziel falsch ist, funktioniert Push nicht.

In diesem Fall war der lokale Commit korrekt, aber das Push-Ziel zeigte auf das Hochschul-Repository.

---

## 33. Sinnvolle Reihenfolge der Arbeit

Die saubere Reihenfolge war:

```text
1. Projekt klonen
2. Projekt in IntelliJ öffnen
3. Gradle-Dateien erstellen
4. Gradle Wrapper erzeugen
5. Build testen
6. Run testen
7. Gradle-Konfiguration committen
8. Sub.java erstellen
9. Sub.java committen
10. Calculator.java bearbeiten
11. Build testen
12. Run testen
13. Calculator.java committen
14. Fork erstellen
15. Remote korrigieren
16. Pushen
```

---

## 34. Finaler Prüfstand

Aufgabe 1 ist abgeschlossen, wenn diese Punkte erfüllt sind:

```text
build.gradle existiert
settings.gradle existiert
Gradle Wrapper existiert
.\gradlew.bat build läuft erfolgreich
.\gradlew.bat run startet den Calculator
Sub.java existiert
Sub implementiert Operation
Sub berechnet a - b
Calculator bindet Sub ein
Calculator bindet Mul als anonyme Klasse ein
Calculator bindet Div als Lambda ein
ActionListener ist als Lambda umgesetzt
ActionEvent-Import wurde entfernt
ActionListener-Import wurde entfernt
Änderungen sind committed
Änderungen sind in den eigenen Fork gepusht
```

---

## 35. Kurze Lernzusammenfassung

`Sub` wurde als normale Klasse umgesetzt, weil die Aufgabe ausdrücklich keine anonyme Klasse und kein Lambda verlangt.

`Mul` wurde als anonyme Klasse umgesetzt, weil die Aufgabe dafür genau diese Technik verlangt.

`Div` wurde als Lambda-Ausdruck umgesetzt, weil `Operation` ein funktionales Interface ist.

Der `ActionListener` wurde ebenfalls als Lambda-Ausdruck umgesetzt, weil auch `ActionListener` funktional nutzbar ist.

Gradle wurde eingerichtet, damit das Projekt reproduzierbar gebaut und gestartet werden kann.

Git wurde genutzt, um Build-Konfiguration und fachliche Änderungen getrennt zu speichern.

---

## 36. Wichtigster Unterschied

### Normale Klasse

```java
public class Sub implements Operation {
    @Override
    public int doOperation(int a, int b) {
        return a - b;
    }
}
```

Eigene Datei.

Eigener Klassenname.

Gut für wiederverwendbare Logik.

---

### Anonyme Klasse

```java
new Operation() {
    @Override
    public int doOperation(int a, int b) {
        return a * b;
    }
}
```

Keine eigene Datei.

Kein eigener Klassenname.

Direkt dort definiert, wo sie gebraucht wird.

---

### Lambda-Ausdruck

```java
(a, b) -> a / b
```

Sehr kurze Schreibweise.

Nur möglich bei funktionalen Interfaces.

---

## 37. Ergebnis der Aufgabe

Der Calculator unterstützt nun diese Operationen:

```text
Add
Sub
Mul
Div
```

Die Operationen sind über die `JComboBox` auswählbar.

Bei Auswahländerung wird durch den Lambda-`ActionListener` neu gerechnet.

Die Aufgabe demonstriert drei verschiedene Arten, Verhalten in Java bereitzustellen:

```text
normale Klasse
anonyme Klasse
Lambda-Ausdruck
```