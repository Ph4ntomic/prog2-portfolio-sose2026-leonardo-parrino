# ============================================================
# AUFGABE 2: REGEXHIGHLIGHTER
# ============================================================

## 1. AUFGABENSTELLUNG

Der `RegexHighlighter` sollte als naive Highlighting-Strategie implementiert
werden.

Die Idee:

1. Alle Tokens aus `MiniJavaTokens` werden unabhängig voneinander auf den ganzen
   Text angewendet.
2. Alle Treffer werden gesammelt.
3. Die Template-Methode `computeRegions` ruft danach `normalize` auf.
4. Anschließend löst `resolveConflicts` überlappende Bereiche auf.

Die Basisklasse `SyntaxHighlighter` durfte nicht verändert werden.

## 2. UMGESETZTE DATEIEN

Geändert wurde:

```text
src/main/java/highlighting/regex/RegexHighlighter.java
```

Getestet wurde:

```text
src/test/java/highlighting/regex/RegexHighlighterTest.java
src/test/java/highlighting/SyntaxHighlightingIntegrationTest.java
```

## 3. COLLECTMATCHES

`collectMatches` sammelt alle Treffer aller Tokens.

Umgesetzt wurde:

```java
public List<HighlightRegion> collectMatches(String text) {
  var regions = new ArrayList<HighlightRegion>();

  for (Token token : tokens) {
    regions.addAll(token.test(text));
  }

  return regions;
}
```

Wichtig:

- Es wird bewusst noch nicht sortiert.
- Es wird bewusst noch nichts entfernt.
- Die Sortierung übernimmt `normalize` in `SyntaxHighlighter`.

## 4. KONSTRUKTOREN

Es gibt zwei Konstruktoren:

```java
public RegexHighlighter() {
  this(MiniJavaTokens.defaultTokens());
}

public RegexHighlighter(List<Token> tokens) {
  this.tokens = List.copyOf(tokens);
}
```

Der Standardkonstruktor verwendet die echten MiniJava-Tokens.

Der zweite Konstruktor ist für Tests hilfreich, weil damit kleine künstliche
Tokenlisten gebaut werden können. Dadurch lassen sich Konflikte gezielt testen,
ohne vom kompletten MiniJava-Tokenbestand abhängig zu sein.

## 5. KONFLIKTAUFLÖSUNG

Die Methode `resolveConflicts` geht die bereits normalisierte Liste von vorne
nach hinten durch.

Eine Region wird übernommen, wenn sie nicht mit einer bereits übernommenen Region
überlappt.

Umgesetzt wurde:

```java
int coveredUntil = 0;

for (HighlightRegion region : regions) {
  if (region.start() >= coveredUntil) {
    selected.add(region);
    coveredUntil = region.end();
  }
}
```

Warum funktioniert das?

Die Liste ist durch `normalize` bereits nach Startposition sortiert. Bei gleichem
Start steht die längere Region zuerst. Dadurch reicht es, sich das Ende der
zuletzt übernommenen Region zu merken.

Halboffene Intervalle werden korrekt behandelt:

```text
[0, 5) und [5, 10) überlappen nicht.
```

## 6. JUNIT-TESTS

Die Testklasse `RegexHighlighterTest` enthält 6 Tests:

```text
collectMatchesAppliesAllTokensIndependently
resolveConflictsKeepsFirstNonOverlappingRegions
computeRegionsKeepsCommentInsteadOfKeywordsInsideComment
computeRegionsKeepsJavadocAsSingleRegion
computeRegionsKeepsAdjacentRegions
computeRegionsReturnsEmptyListForEmptyTextAndTextsWithoutMatches
```

Zusätzlich prüft `SyntaxHighlightingIntegrationTest` den RegexHighlighter am
echten Beispieltext `Texts.START_TEXT`.

## 7. WARUM SIND DIE TESTS RELEVANT?

Die Tests prüfen genau die Stellen, an denen der RegexHighlighter fehleranfällig
ist.

Besonders relevant sind:

- Tokens werden unabhängig voneinander angewendet.
- Keywords innerhalb von Kommentaren werden später verworfen.
- Javadoc bleibt eine einzige Region.
- Direkt aufeinanderfolgende Regionen bleiben erhalten.
- Leere Texte führen nicht zu Fehlern.

## 8. WARUM SIND DIE TESTS UNTERSCHIEDLICH?

Die Tests prüfen verschiedene Ebenen:

- `collectMatches` prüft die rohe Sammlung.
- `resolveConflicts` prüft die Konfliktregel isoliert.
- `computeRegions` prüft das Zusammenspiel aus Sammeln, Normalisieren und
  Konfliktauflösung.
- Integrationstests prüfen den echten Beispieltext.

Damit werden Unit-Level und System-Level abgedeckt.

## 9. VERIFIKATION

Ausgeführt wurde:

```powershell
.\gradlew.bat clean classes test spotlessCheck
.\gradlew.bat test
.\gradlew.bat spotlessCheck
```

Ergebnis:

```text
BUILD SUCCESSFUL
RegexHighlighterTest: 6 Tests, 0 Failures, 0 Skipped
SyntaxHighlightingIntegrationTest: 5 Tests, 0 Failures, 0 Skipped
```

## 10. FAZIT

Aufgabe 2 ist umgesetzt. Der RegexHighlighter arbeitet bewusst einfach: erst
alles sammeln, dann normalisieren, dann Konflikte entfernen. Genau dieses
Verhalten ist durch gezielte Tests abgesichert.

