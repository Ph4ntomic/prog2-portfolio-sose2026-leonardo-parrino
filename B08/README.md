# Dokumentation zu Blatt 08

In diesem Blatt erweitere ich das Zoo-Modell aus Blatt 07 um `Optional`, das
Command-Pattern, Undo/Redo und einen eigenen Ergebnistyp fuer Fehlerfaelle.

## Dateien

- `src/main/java/zoo/animal` - Tierhierarchie aus Blatt 07
- `src/main/java/zoo/enclosure` - generische Gehege und lokale Tiersuche
- `src/main/java/zoo/command` - Commands und Undo-/Redo-Manager
- `src/main/java/zoo/result` - `Result<E, R>` und `ZooError`
- `src/main/java/zoo/Zoo.java` - globale Tiersuche ueber alle Gehege
- `src/main/java/zoo/CommandDemoMain.java` - kurze Undo-/Redo-Demo
- `src/test/java/zoo` - Unit-Tests

## Aufgabe 1: Optional

In `Enclosure<T>` gebe ich bei `findAnimalByName` ein `Optional<T>` zurueck.
Das Gehege kennt seinen konkreten Tiertyp bereits durch den Typparameter und
kann ihn deshalb ohne Informationsverlust zurueckgeben.

Der `Zoo` verwaltet verschiedene `Enclosure<? extends Animal>`. Nach dem
Zusammenfuehren der Suchergebnisse ist nur noch der gemeinsame Basistyp sicher
bekannt. Deshalb verwende ich hier `Optional<Animal>`. Fuer die Suche nutze ich
`flatMap`, den Stream des jeweiligen `Optional` und abschliessend `findFirst`.

## Aufgabe 2: Command-Pattern

Mit `Command<T, E, R>` beschreibe ich eine Aktion auf einem Zielobjekt sowie
ihren Fehler- und Ergebnistyp. Die konkreten Commands sind ueber den Tiertyp
eingeschraenkt:

- `AddAnimalCommand<T extends Animal>`
- `RemoveAnimalCommand<T extends Animal>`
- Zieltyp: `Enclosure<? super T>`

Das `? super T` folgt dem PECS-Prinzip, weil das Gehege ein Tier vom Typ `T`
konsumiert. Ein `AddAnimalCommand<Lion>` funktioniert dadurch sowohl mit einem
`Enclosure<Lion>` als auch mit einem `Enclosure<Mammal>`. Ein
`AddAnimalCommand<Shark>` kann nicht mit einem Saeugetiergehege ausgefuehrt
werden.

Den `CommandManager` parametrisiere ich mit einem konkreten Gehegetyp, zum
Beispiel `CommandManager<Enclosure<Mammal>>`. Die Wildcard bleibt im Zieltyp
der Commands. So kann der Manager passende Untertypen akzeptieren, ohne die
Typsicherheit des Geheges aufzugeben.

Im `CommandManager` verwalte ich zwei Stacks. Erfolgreich ausgefuehrte Commands
landen auf dem Undo-Stack und loeschen die Redo-Historie. Ein erfolgreiches
Undo verschiebt den Command auf den Redo-Stack; Redo arbeitet in umgekehrter
Richtung. Bei einem Fehler bleibt der jeweilige Stack unveraendert.

## Aufgabe 3: Result und Logging

`Result<E, R>` ist als sealed Interface mit den Records `Ok<E, R>` und
`Err<E, R>` umgesetzt. Die Commands liefern `Result<ZooError, Animal>` und
enthalten keine Logging-Logik. Fachliche Fehler beschreibe ich mit `ZooError`:

- Tier bereits vorhanden oder nicht gefunden
- Command bereits ausgefuehrt oder noch nicht ausgefuehrt
- kein Undo oder Redo vorhanden

Im `CommandManager` werte ich jedes Ergebnis mit Pattern Matching aus und logge
zentral:

- `INFO`: Start von `executeCommand`, `undo` und `redo`
- `WARNING`: nicht ausfuehrbare oder fehlgeschlagene Aktion
- `FINE`: Zustand des Ziel-Geheges und Groesse beider Stacks

Damit bleiben die fachlichen Entscheidungen in den Commands und die technische
Protokollierung im Manager getrennt.

## Tests

Ich teste:

- `Optional<T>` im Gehege und `Optional<Animal>` im Zoo
- den ersten Treffer bei gleichen Tiernamen
- Erfolg und Fehler von `Result<E, R>`
- Ausfuehren und Rueckgaengigmachen beider Commands
- LIFO-Reihenfolge bei mehreren Commands
- vollstaendige Undo-/Redo-Sequenzen
- Verhalten der Historie nach erfolgreichen und fehlgeschlagenen Aktionen
- Logging-Level fuer Methodenstart, Fehler und Endzustand
- spezialisierte und allgemeine Gehegetypen
- Typsicherheit der Generic-Bounds durch nicht kompilierbaren Beispielcode

Lokal pruefen:

```powershell
cd B08
.\gradlew.bat clean test spotlessCheck
```

Demo ausfuehren:

```powershell
.\gradlew.bat run
```
