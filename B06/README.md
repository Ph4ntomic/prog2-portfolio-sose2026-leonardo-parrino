# Dokumentation zu Blatt 06

Thema: Visitor vs. Pattern Matching, Records und AST-Normalisierung fuer die
Filter-DSL.

## Dateien

- `build.gradle` - eigenstaendiges Gradle-Projekt fuer Blatt 06 mit ANTLR,
  ApprovalTests.Java und jqwik
- `src/main/antlr/filter/Filter.g4` - Grammatik der Filter-DSL
- `src/main/java/filter/ast/builder/AstBuilderVisitor.java` - zustandsbehafteter
  AST-Builder mit Visitor-Pattern
- `src/main/java/filter/ast/builder/AstBuilderPattern.java` - zustandsloser
  AST-Builder mit typbasiertem Pattern Matching
- `src/main/java/filter/ast/builder/AstBuilders.java` - Parser-Helfer und
  AST-Normalisierung
- `src/test/java/filter/ast/AstTest.java` - klassische JUnit-Testfaelle
- `src/test/java/filter/ast/ApprovalTest.java` - Approval-Tests ueber
  `AstPrinter`
- `src/test/java/filter/ast/RoundtripPropertiesTest.java` - Property-based Tests
  mit jqwik
- `src/test/java/filter/ast/*.approved.txt` - versionierte Approval-Orakel
- `LICENSE.md` - MIT-Lizenz

## Kurzstand

Technisch erledigt:

- Aufgabe 1: repraesentative JUnit-Tests fuer einfache Vergleiche, Praezedenz,
  Klammerung, In-Listen, Escapes, Normalisierung und Evaluierung
- Aufgabe 2: `AstBuilderVisitor` mit `Deque<Expr>` und `Deque<Value>`
- Aufgabe 3: `AstBuilderPattern` ohne internen Zustand, mit Switch-Expressions
- Aufgabe 4: fachlicher Vergleich Visitor vs. Pattern Matching
- Aufgabe 5: `simplify(Expr e)` mit tiefem Record-Pattern fuer doppelte
  `Not`-Knoten
- Aufgabe 6: Approval Testing mit `AstPrinter`
- Aufgabe 7: jqwik-Roundtrip-Properties und semantische Eigenschaften

Lokal geprueft:

```powershell
cd B06
.\gradlew.bat test
```

Ergebnis:

```text
BUILD SUCCESSFUL
```

## Umsetzung

### Aufgabe 2: Visitor-Builder

Der Visitor ist bewusst zustandsbehaftet implementiert. Beim Traversieren werden
fertige AST-Ausdruecke auf `Deque<Expr>` und Literale auf `Deque<Value>` gelegt.
Die kritische Stelle sind `andExpr` und `orExpr`: ANTLR besucht die Kinder von
links nach rechts, dadurch liegt nach dem Besuch der rechte Ausdruck oben auf dem
Stack. Deshalb werden die gepoppten Ausdruecke zuerst per `addFirst` in die
Quellreihenfolge zurueckgebracht und danach left-assoziativ gefaltet:

```text
a and b and c  ->  And(And(a, b), c)
```

Damit stimmen die Left/Right-Knoten auch bei langen Operator-Ketten exakt mit
der Grammatiksemantik ueberein.

### Aufgabe 3: Pattern-Matching-Builder

Der Pattern-Builder besitzt keinen internen Zustand. Jede Methode nimmt einen
ANTLR-Context entgegen und gibt direkt den passenden AST-Knoten zurueck.
Listenartige Produktionen wie `orExpr` und `andExpr` werden funktional ueber
eine lokale `foldLeft`-Hilfsmethode zusammengesetzt. Varianten in der Grammatik,
zum Beispiel `comparison` als Vergleich oder `in`-Liste, werden ueber
Switch-Expressions mit Guard-Bedingungen unterschieden.

### Aufgabe 5: Normalisierung

`AstBuilders.simplify` arbeitet als AST-zu-AST-Abbildung. Alle Kindknoten werden
rekursiv normalisiert. Doppelte Negationen werden mit einem tiefen Record-Pattern
erkannt:

```java
case Expr.Not(Expr.Not(var inner)) -> simplify(inner);
```

Damit werden auch verschachtelte Faelle wie `not not not x` korrekt zu `not x`
reduziert.

## Aufgabe 4: Vergleich Visitor vs. Pattern Matching

Der zustandsbehaftete Visitor passt sehr gut zu ANTLR, weil ANTLR die Visitor-
Basisklassen direkt generiert. Jede Grammatikregel bekommt eine eigene
Override-Methode, wodurch der Traversierungsfluss nah an der Grammatik bleibt.
Aus Architektursicht ist das stark, wenn waehrend der Traversierung bewusst
Zustand gesammelt werden soll, etwa Stacks, Symboltabellen oder Diagnostik.
Der Nachteil ist genau dieser Zustand: Stack-Invarianten sind implizit, Pop-
Reihenfolgen sind fehleranfaellig, und dieselbe Instanz ist nicht reentrant.
Der Code braucht deshalb klare lokale Regeln und gute Tests.

Das Pattern-Matching arbeitet funktionaler. Der AST-Aufbau wird als Rueckgabewert
modelliert, nicht als Seiteneffekt auf internen Stacks. Dadurch sind die Methoden
leichter isoliert zu testen, einfacher zu lesen und robuster gegen versehentliche
Zustandsfehler. Besonders bei sealed AST-Hierarchien und Records ist diese Form
architektonisch angenehm, weil Switch-Expressions die Varianten explizit machen.
Der Nachteil liegt in der Kopplung an die konkrete Parse-Tree-Struktur: Da die
ANTLR-Context-Klassen selbst nicht sealed sind, bekommt man weniger
Exhaustiveness-Sicherheit als beim eigenen AST. Bei sehr grossen Grammatiken
koennen ausserdem viele manuelle Fallunterscheidungen entstehen.

Meine Einschaetzung: Fuer diese Aufgabe ist der Pattern-Builder die sauberere
Produktionsvariante, weil der AST-Aufbau lokal, zustandslos und gut testbar ist.
Der Visitor ist didaktisch wertvoll und bleibt fuer komplexere Compilerphasen
nuetzlich, sobald bewusst Traversierungszustand gebraucht wird.

## Teststrategie

Die JUnit-Tests pruefen konkrete AST-Strukturen und damit besonders Praezedenz,
Klammerung und Left/Right-Reihenfolge. Die Approval-Tests verwenden den
vorbereiteten `AstPrinter` als lesbares Orakel und vergleichen Visitor und
Pattern-Builder ueber dieselben Query-Ausdruecke. Die jqwik-Properties erzeugen
gueltige Query-Fragmente, testen Roundtrips
`Query -> AST -> Pretty-Print -> AST` und pruefen semantische Eigenschaften wie
Kommutativitaet von `And` im Evaluator sowie Erhaltung der Bedeutung bei
doppelter Negation.
