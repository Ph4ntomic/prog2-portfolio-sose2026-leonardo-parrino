# Post Mortem: Blatt 06

## Zusammenfassung

In Blatt 06 habe ich die Filter-DSL mit zwei unterschiedlichen Strategien in
einen AST uebersetzt. Der erste Ansatz nutzt einen zustandsbehafteten ANTLR-
Visitor mit Stacks fuer Ausdruecke und Werte. Der zweite Ansatz baut den AST
zustandslos ueber Switch-Expressions und Pattern Matching auf. Zusaetzlich habe
ich eine Normalisierung implementiert, die doppelte `Not`-Knoten rekursiv
entfernt.

## Details

Beim Visitor war die Reihenfolge der Operanden der wichtigste Punkt. Da die
Kinder von links nach rechts besucht werden, liegt nach dem Besuch einer
Operator-Kette der rechte Ausdruck oben auf dem Stack. Deshalb werden die
Ausdruecke vor dem Falten wieder in Quellreihenfolge gebracht. Dadurch entstehen
bei `and` und `or` left-assoziative Baeume.

Der Pattern-Matching-Builder ist einfacher nachzuvollziehen, weil jede Methode
direkt einen AST-Knoten zurueckgibt und keinen internen Zustand veraendert.
Dadurch sind die Fallunterscheidungen fuer Vergleiche, `in`-Listen, Klammern
und Negationen lokal sichtbar.

## Reflexion: schwierigster Teil

Am schwierigsten war die saubere Abbildung der Parse-Tree-Struktur auf einen
kompakten AST. Besonders bei verketteten `and`- und `or`-Ausdruecken musste die
Pop-Reihenfolge im Visitor exakt stimmen, damit Left- und Right-Knoten nicht
vertauscht werden.

## Reflexion: gelernt

Ich habe besser verstanden, wann ein Visitor sinnvoll ist und wann Pattern
Matching die klarere Architektur ergibt. Der Visitor passt gut zur ANTLR-
Traversierung, braucht aber klare Stack-Invarianten. Pattern Matching ist fuer
die AST-Transformation kompakter und leichter testbar, solange kein
Traversierungszustand gebraucht wird.

## Tests

Geprueft wurden konkrete AST-Strukturen, Approval-Tests ueber den `AstPrinter`
und Property-based Roundtrip-Tests mit jqwik.

Lokal ausgefuehrt:

```powershell
cd B06
.\gradlew.bat clean test spotlessCheck
```

Ergebnis:

```text
BUILD SUCCESSFUL
```

## Links

- Portfolio B06:
  <https://github.com/Ph4ntomic/prog2-portfolio-sose2026-leonardo-parrino/tree/main/B06>
- B06-Dokumentation:
  <https://github.com/Ph4ntomic/prog2-portfolio-sose2026-leonardo-parrino/blob/main/B06/README.md>
- Aufgabenstellung:
  <https://github.com/Programmiermethoden-CampusMinden/Prog2-Lecture-S26/blob/master/homework/b06.md>
- Filter-DSL-Ausgangsprojekt:
  <https://github.com/Programmiermethoden-CampusMinden/prog2_ybel_filterdsl>
- Visitor-Builder:
  <https://github.com/Ph4ntomic/prog2-portfolio-sose2026-leonardo-parrino/blob/main/B06/src/main/java/filter/ast/builder/AstBuilderVisitor.java>
- Pattern-Matching-Builder:
  <https://github.com/Ph4ntomic/prog2-portfolio-sose2026-leonardo-parrino/blob/main/B06/src/main/java/filter/ast/builder/AstBuilderPattern.java>
- Normalisierung:
  <https://github.com/Ph4ntomic/prog2-portfolio-sose2026-leonardo-parrino/blob/main/B06/src/main/java/filter/ast/builder/AstBuilders.java>
- JUnit-Tests:
  <https://github.com/Ph4ntomic/prog2-portfolio-sose2026-leonardo-parrino/blob/main/B06/src/test/java/filter/ast/AstTest.java>
- Approval-Tests:
  <https://github.com/Ph4ntomic/prog2-portfolio-sose2026-leonardo-parrino/blob/main/B06/src/test/java/filter/ast/ApprovalTest.java>
- Property-based Tests:
  <https://github.com/Ph4ntomic/prog2-portfolio-sose2026-leonardo-parrino/blob/main/B06/src/test/java/filter/ast/RoundtripPropertiesTest.java>
- Implementierungs-Commit:
  <https://github.com/Ph4ntomic/prog2-portfolio-sose2026-leonardo-parrino/commit/63a3846>
- Test-Commit:
  <https://github.com/Ph4ntomic/prog2-portfolio-sose2026-leonardo-parrino/commit/fd77486>
- Dokumentations-Commit:
  <https://github.com/Ph4ntomic/prog2-portfolio-sose2026-leonardo-parrino/commit/e95e1eb>
