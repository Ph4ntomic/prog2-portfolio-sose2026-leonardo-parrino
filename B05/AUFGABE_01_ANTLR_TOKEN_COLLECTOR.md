# ============================================================
# AUFGABE 1.1: ANTLR-TOKEN-COLLECTOR
# ============================================================

## 1. Aufgabenstellung

In Aufgabe 1.1 sollte das Syntaxhighlighting nicht mehr ueber selbst
geschriebene regulaere Ausdruecke oder einen eigenen Scanner laufen.

Stattdessen sollte der von ANTLR generierte Lexer verwendet werden:

```java
MiniJavaLexer
```

Die Methode

```java
public List<HighlightRegion> collectMatches(String text)
```

soll den Eingabetext tokenisieren und daraus farbige
`HighlightRegion`-Objekte erzeugen.

## 2. Umgesetzte Dateien

Geaendert wurde:

```text
src/main/java/highlighting/antlr/AntlrTokenCollector.java
```

Getestet wurde:

```text
src/test/java/highlighting/antlr/AntlrTokenCollectorTest.java
src/test/java/highlighting/SyntaxHighlightingIntegrationTest.java
```

## 3. Grundidee

Der Eingabetext wird zuerst in einen ANTLR-CharStream umgewandelt:

```java
var lexer = new MiniJavaLexer(CharStreams.fromString(text));
```

Danach wird daraus ein Tokenstream erzeugt:

```java
var tokenStream = new CommonTokenStream(lexer);
tokenStream.fill();
```

Anschliessend werden alle Tokens durchlaufen. Das EOF-Token wird ignoriert.

## 4. Tokenarten und Farben

Folgende Tokenarten werden hervorgehoben:

| Tokenart | Farbe |
|---|---|
| Keywords | `MiniJavaColours.KEYWORD_COLOUR` |
| String-Literale | `MiniJavaColours.STRING_LITERAL_COLOUR` |
| Character-Literale | `MiniJavaColours.CHAR_LITERAL_COLOUR` |
| Zeilenkommentare | `MiniJavaColours.LINE_COMMENT_COLOUR` |
| Javadoc-Kommentare | `MiniJavaColours.JAVADOC_COMMENT_COLOUR` |
| Blockkommentare | `MiniJavaColours.BLOCK_COMMENT_COLOUR` |
| Annotationen | `MiniJavaColours.ANNOTATION_COLOUR` |

Beispiel fuer die Keyword-Abbildung:

```java
case MiniJavaLexer.PACKAGE,
    MiniJavaLexer.IMPORT,
    MiniJavaLexer.CLASS,
    MiniJavaLexer.PUBLIC,
    MiniJavaLexer.PRIVATE,
    MiniJavaLexer.FINAL,
    MiniJavaLexer.RETURN,
    MiniJavaLexer.NULL,
    MiniJavaLexer.NEW,
    MiniJavaLexer.IF,
    MiniJavaLexer.ELSE,
    MiniJavaLexer.WHILE,
    MiniJavaLexer.EXTENDS,
    MiniJavaLexer.IMPLEMENTS ->
    MiniJavaColours.KEYWORD_COLOUR;
```

## 5. Annotationen

Eine Annotation besteht im Tokenstream nicht aus einem einzigen Token.

Beispiel:

```java
@Demo
```

wird tokenisiert als:

```text
AT
IDENTIFIER
```

Deshalb wird beim `@` gemerkt, dass der direkt folgende Identifier ebenfalls zur
Annotation gehoert.

Sinngemaess:

```java
if (token.getType() == MiniJavaLexer.AT) {
    regions.add(toRegion(token, MiniJavaColours.ANNOTATION_COLOUR));
    nextIdentifierBelongsToAnnotation = true;
}
```

## 6. Start- und Endpositionen

ANTLR liefert fuer jedes Token:

```java
token.getStartIndex()
token.getStopIndex()
```

Der Stop-Index ist inklusiv. `HighlightRegion` erwartet aber ein halboffenes
Intervall:

```text
[start, end)
```

Deshalb wird `stopIndex + 1` verwendet:

```java
new HighlightRegion(token.getStartIndex(), token.getStopIndex() + 1, colour)
```

## 7. Konflikte und Normalisierung

Bei Regex-Highlighting aus Blatt 04 konnten Konflikte entstehen.

Beispiel:

```java
// public class
```

Ein Regex konnte hier theoretisch gleichzeitig den ganzen Kommentar und die
Keywords `public` und `class` finden.

Beim ANTLR-Lexer entsteht dieses Problem nicht. Der Lexer erzeugt eine
eindeutige Tokenfolge. Ein Bereich gehoert genau zu einem Token.

Deshalb muessen die Hook-Methoden nichts veraendern:

```java
public List<HighlightRegion> normalize(List<HighlightRegion> candidates) {
    return candidates;
}

public List<HighlightRegion> resolveConflicts(List<HighlightRegion> normalized) {
    return normalized;
}
```

## 8. Unterschied zu RegexHighlighter und ScanningHighlighter

| Variante | Arbeitsweise | Konflikte |
|---|---|---|
| RegexHighlighter | wendet alle Patterns unabhaengig an | moeglich |
| ScanningHighlighter | scannt links nach rechts | deutlich reduziert |
| AntlrTokenCollector | nutzt den generierten Lexer | praktisch keine Ueberlappungen |

Die ANTLR-Loesung ist im eigentlichen Highlighter kuerzer.

Dafuer haengt sie staerker von der Grammatik ab. Nur Tokens, die in der
Grammatik passend definiert sind, koennen direkt erkannt werden.

## 9. Tests

Die Testklasse `AntlrTokenCollectorTest` prueft:

```text
collectMatchesUsesMiniJavaLexerTokens
computeRegionsKeepsLineCommentAsSingleHiddenToken
computeRegionsDoesNotHighlightKeywordTextInsideIdentifier
tokenStreamRegionsDoNotNeedNormalizationOrConflictResolution
```

Wichtige Testfaelle:

- Annotation `@Demo`
- Keyword `class`
- String `"x"`
- Character `'y'`
- Zeilenkommentar
- Javadoc-Kommentar
- Blockkommentar
- kein Keyword innerhalb von `className`

## 10. Integrationstest

Die vorhandene Integrationstestklasse wurde erweitert:

```text
src/test/java/highlighting/SyntaxHighlightingIntegrationTest.java
```

Zusatztests:

```text
antlrTokenCollectorProducesValidRegionsForStartText
antlrTokenCollectorCoversAllConfiguredTokenKindsInStartText
```

Dadurch wird nicht nur ein kleines Einzelbeispiel getestet, sondern auch der
echte Beispieltext aus der Anwendung.

## 11. Verifikation

Ausgefuehrt wurde:

```powershell
.\gradlew.bat clean classes test spotlessCheck
```

Ergebnis:

```text
BUILD SUCCESSFUL
35 Tests
0 Failures
0 Skipped
```

## 12. Fazit

Der `AntlrTokenCollector` ist umgesetzt. Er nutzt den generierten Lexer,
erzeugt korrekte Highlight-Bereiche und benoetigt keine eigene
Konfliktaufloesung mehr.
