# Dokumentation zu Blatt 07

Thema: Generics, sealed Types, Records, Stream-API und Logging am Beispiel
einer kleinen Zoo-Verwaltung.

## Dateien

- `src/main/java/zoo/animal` - sealed Tierhierarchie und konkrete Records
- `src/main/java/zoo/enclosure` - generische und spezialisierte Gehege
- `src/main/java/zoo/Zoo.java` - Zoo-Verwaltung mit Stream-Abfragen und Logging
- `src/main/java/zoo/ZooDemoMain.java` - Demo fuer programmatische Log-Level
- `src/test/java/zoo` - Unit-Tests fuer Zoo, Gehege und Abfragen

## Kurzstand

Technisch erledigt:

- Aufgabe 1.1: Tierhierarchie mit `sealed interface` und `record`
- Aufgabe 1.2: `Enclosure<T extends Animal>` mit internem `LinkedHashSet`
- Aufgabe 1.3: `Aquarium`, `Terrarium`, `MammalHouse` und `CatHouse`
- Aufgabe 1.4: `Zoo` mit `List<Enclosure<? extends Animal>>`, `Optional` und
  Stream-Abfragen
- Aufgabe 2: Logging mit `java.util.logging.Logger`
- Aufgabe 3: Reflexion zu Generics, Logging und Streams

Lokal geprueft:

```powershell
cd B07
.\gradlew.bat clean test spotlessCheck
```

## Umsetzung

Die Tierhierarchie liegt vollstaendig im Package `zoo.animal`, weil sealed
Hierarchien ohne Module keine Unterpackages fuer direkt erlaubte Typen nutzen
koennen. `Animal` ist der gemeinsame Root-Typ. Darunter liegen `Mammal`, `Fish`,
`Reptile` und `Bird`; `Mammal` verzweigt weiter in `Primate`, `Rodent` und
`Cat`. Zu jedem Interface gibt es konkrete Records mit einem validierten
`String name`.

`Enclosure<T extends Animal>` kapselt die Bewohner in einem `LinkedHashSet`.
Dadurch kann ein Tier nicht doppelt im selben Gehege vorkommen, und die
Einfuegereihenfolge bleibt fuer Tests und Ausgaben stabil. `getInhabitants`
liefert eine unveraenderliche Snapshot-Liste.

Die spezialisierten Gehege spiegeln die Typgrenzen wider:

- `Aquarium<T extends Fish>`
- `Terrarium<T extends Reptile>`
- `MammalHouse<T extends Mammal>`
- `CatHouse extends Enclosure<Lion>`

`CatHouse` ist bewusst auf eine konkrete Cat-Implementierung festgelegt. Dadurch
akzeptiert dieses Gehege nur `Lion` und nicht beliebige andere `Cat`-Records.

`Zoo` verwaltet `List<Enclosure<? extends Animal>>`. Die Abfragen verwenden
Streams, insbesondere `flatMap`, `filter`, `map`, `toList` und
`Collectors.groupingBy`. `findEnclosureByName` gibt
`Optional<Enclosure<? extends Animal>>` zurueck.

## Logging

`Zoo` nutzt einen `Logger` mit folgenden Levels:

- `INFO`: Aufruf jeder public Zoo-Methode mit relevanten Parametern
- `FINE`: Zustand nach erfolgreicher Ausfuehrung
- `WARNING`: fehlende Gehege, fehlende Tiere oder doppelte Tiere
- `SEVERE`: schwere Inkonsistenzen wie doppelte Gehegenamen oder Rollback beim
  Umsetzen

`ZooDemoMain` zeigt, wie das Log-Level fuer den Zoo-Logger und die Console-
Handler programmatisch auf `FINE` oder `WARNING` gesetzt wird.

## Tests

Die Tests pruefen:

- sealed Hierarchie und Namensvalidierung
- Set-Semantik in `Enclosure`
- spezialisierte Gehege und Typsicherheit der Generics
- `Optional` bei `findEnclosureByName`
- Stream-Abfragen fuer alle Tiere, Saeugetiere, Predicate-Filter,
  Gruppierung nach Typ und ueberfuellte Gehege
- Zustandwechsel durch Aufnehmen, Umsetzen und Abgeben

Falsche Zuweisungen sind in einem disabled Test als Kommentar dokumentiert,
weil dieser Code wegen der Generic-Bounds nicht kompilieren darf.

## Aufgabe 3: Reflexion

Generics:

- Generics verhindern, dass falsche Tierarten ueberhaupt an ein Gehege
  uebergeben werden koennen.
- Beispiel: `Aquarium<Fish>` akzeptiert `Trout`, aber keinen `Lion`.
- `CatHouse extends Enclosure<Lion>` erlaubt nur `Lion`; ein `Tiger` passt dort
  bereits zur Compile-Zeit nicht.

Logging:

- Logger sind besser als `IO.println`, weil Log-Level, Handler und Ausgabeziele
  zentral steuerbar sind.
- `INFO` passt fuer normale Zoo-Aktionen wie Abfragen, Aufnehmen und Umsetzen.
- `WARNING` passt, wenn ein Tier oder Gehege nicht gefunden wird.
- `SEVERE` passt fuer Inkonsistenzen, etwa doppelte Gehegenamen oder einen
  fehlgeschlagenen Transfer nach bereits erfolgter Entfernung.

Streams:

- Streams machen Abfragen ueber alle Gehege sehr kompakt, besonders mit
  `flatMap`, `filter` und `groupingBy`.
- Fuer reine Auswahl-, Zaehl- und Gruppierlogik sind Streams lesbarer als
  verschachtelte Schleifen.
- Unuebersichtlich werden Streams, wenn neben der Abfrage viel Zustandslogik
  oder Fehlerbehandlung passiert. Deshalb bleiben Aufnahme, Abgabe und Transfer
  bewusst imperativ.
