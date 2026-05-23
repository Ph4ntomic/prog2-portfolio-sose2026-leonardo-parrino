# ============================================================
# AUFGABE 3: SCANNINGHIGHLIGHTER
# ============================================================

## 1. AUFGABENSTELLUNG

Der `ScanningHighlighter` sollte wie ein einfacher Lexer arbeiten.

Die Regeln:

1. Der Text wird von links nach rechts gelesen.
2. An jeder Position wird geprüft, welches Token genau dort beginnt.
3. Das längste passende Token gewinnt.
4. Bei Gleichstand gewinnt das Token, das früher in der Tokenliste steht.
5. Wenn kein Token passt, wird der Index um ein Zeichen erhöht.
6. Wenn ein Token passt, springt der Index direkt hinter den Treffer.

Die erzeugte Trefferliste soll bereits sortiert, gültig und nicht überlappend
sein. Deshalb soll `normalize` nur noch die Eingabe unverändert zurückgeben.

## 2. UMGESETZTE DATEIEN

Geändert wurde:

```text
src/main/java/highlighting/regex/ScanningHighlighter.java
```

Getestet wurde:

```text
src/test/java/highlighting/regex/ScanningHighlighterTest.java
src/test/java/highlighting/SyntaxHighlightingIntegrationTest.java
```

## 3. COLLECTMATCHES

Der Scanner läuft mit einem Index durch den Text.

Kernidee:

```java
while (index < text.length()) {
  HighlightRegion bestRegion = null;
  int bestEnd = index;

  for (Token token : tokens) {
    ...
  }

  if (bestRegion == null) {
    index++;
  } else {
    regions.add(bestRegion);
    index = bestEnd;
  }
}
```

Wenn kein Token passt, geht der Scanner ein Zeichen weiter.

Wenn ein Token passt, wird die gefundene Region übernommen und der Scanner
springt direkt ans Ende des Matches.

Dadurch kann keine Endlosschleife entstehen.

## 4. LÄNGSTES MATCH GEWINNT

Innerhalb der Token-Schleife wird ein Treffer nur übernommen, wenn er länger ist
als der bisher beste Treffer:

```java
if (matcher.lookingAt() && matcher.end() > bestEnd) {
  ...
}
```

Wichtig ist das `>` und nicht `>=`.

Dadurch bleibt bei gleicher Länge automatisch das früher gefundene Token
erhalten. Das entspricht der Aufgabenstellung: Bei Gleichstand gewinnt das Token,
das in `MiniJavaTokens` früher steht.

## 5. TRANSPARENT BOUNDS

Beim Scannen wird pro Position mit `Matcher.region(...)` gearbeitet:

```java
matcher.region(index, text.length());
matcher.useTransparentBounds(true);
```

`useTransparentBounds(true)` ist wichtig, weil manche Patterns mit Wortgrenzen
oder Lookbehind arbeiten. Ohne transparente Bounds könnte ein Pattern am
Regionsanfang anders reagieren als im vollständigen Text.

Das ist besonders relevant für:

- Keywords mit `\b`,
- Character-Literale mit Lookbehind.

## 6. NORMALIZE

Für den Scanner ist `normalize` eine Identitätsfunktion:

```java
public List<HighlightRegion> normalize(List<HighlightRegion> candidates) {
  return candidates;
}
```

Das ist korrekt, weil `collectMatches` bereits nur gültige, sortierte und
nicht überlappende Regionen erzeugt.

## 7. JUNIT-TESTS

Die Testklasse `ScanningHighlighterTest` enthält 6 Tests:

```text
collectMatchesChoosesLongestMatchAtCurrentPosition
collectMatchesKeepsEarlierTokenWhenMatchesHaveSameLength
collectMatchesSkipsUnmatchedTextAndContinuesScanning
computeRegionsKeepsStringsAndCommentsAsSeparateScannerTokens
computeRegionsDoesNotMatchKeywordInsideIdentifier
normalizeReturnsCandidatesUnchanged
```

Zusätzlich prüft `SyntaxHighlightingIntegrationTest` den Scanner am echten
Beispieltext `Texts.START_TEXT`.

## 8. WARUM SIND DIE TESTS RELEVANT?

Die Tests prüfen die Kernregeln eines Scanners:

- längstes Match,
- Gleichstand nach Tokenreihenfolge,
- Fortschritt bei nicht passenden Zeichen,
- keine Keywords in Bezeichnern,
- keine unnötige Nachbearbeitung durch `normalize`.

Genau diese Punkte entscheiden, ob der Scanner wirklich wie ein Lexer arbeitet.

## 9. WARUM SIND DIE TESTS UNTERSCHIEDLICH?

Die Tests prüfen unterschiedliche Scanner-Situationen.

Ein Test erzeugt konkurrierende Tokens mit unterschiedlicher Länge. Ein anderer
erzeugt konkurrierende Tokens mit gleicher Länge. Ein weiterer prüft Textstellen
ohne Match. Die Integrationstests prüfen den echten Beispieltext.

Damit sind Normalfälle, Grenzfälle und das Zusammenspiel mit den echten Tokens
abgedeckt.

## 10. VERIFIKATION

Ausgeführt wurde:

```powershell
.\gradlew.bat clean classes test spotlessCheck
.\gradlew.bat test
.\gradlew.bat spotlessCheck
```

Ergebnis:

```text
BUILD SUCCESSFUL
ScanningHighlighterTest: 6 Tests, 0 Failures, 0 Skipped
SyntaxHighlightingIntegrationTest: 5 Tests, 0 Failures, 0 Skipped
```

## 11. FAZIT

Aufgabe 3 ist umgesetzt. Obwohl laut Aufgabenblatt nur Aufgabe 2 oder Aufgabe 3
gewählt werden musste, wurde der Scanner zusätzlich implementiert und getestet.
Damit ist die Lösung über dem Mindestumfang.

