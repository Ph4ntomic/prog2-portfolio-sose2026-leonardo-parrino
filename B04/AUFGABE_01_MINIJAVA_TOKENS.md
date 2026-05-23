# ============================================================
# AUFGABE 1: MINIJAVATOKENS
# ============================================================

## 1. AUFGABENSTELLUNG

In Aufgabe 1 sollte die Klasse `highlighting.presets.MiniJavaTokens`
vervollständigt werden.

Die Methode

```java
public static List<Token> defaultTokens()
```

soll eine Liste von `Token`-Objekten zurückgeben. Jedes Token besteht aus:

- einem regulären Ausdruck (`Pattern`),
- einer Farbe aus `MiniJavaColours`,
- optional einer Match-Gruppe.

Mindestens sollten folgende Bereiche in Java-Quelltext erkannt werden:

- Strings,
- Character-Literale,
- Keywords,
- Annotationen,
- einzeilige Kommentare,
- Blockkommentare,
- Javadoc-Kommentare.

Zusätzlich sollten aussagekräftige JUnit-Tests für diese Tokens erstellt werden.

## 2. UMGESETZTE DATEIEN

Geändert wurde:

```text
src/main/java/highlighting/presets/MiniJavaTokens.java
```

Getestet wurde:

```text
src/test/java/highlighting/presets/MiniJavaTokensTest.java
src/test/java/highlighting/SyntaxHighlightingIntegrationTest.java
```

## 3. WICHTIGE TOKEN

In `MiniJavaTokens` wurden vorkompilierte Patterns angelegt.

Wichtig ist die Reihenfolge:

```java
Token.of(JAVADOC_COMMENT, MiniJavaColours.JAVADOC_COMMENT_COLOUR),
Token.of(BLOCK_COMMENT, MiniJavaColours.BLOCK_COMMENT_COLOUR),
Token.of(LINE_COMMENT, MiniJavaColours.LINE_COMMENT_COLOUR),
Token.of(STRING_LITERAL, MiniJavaColours.STRING_LITERAL_COLOUR),
Token.of(CHAR_LITERAL, MiniJavaColours.CHAR_LITERAL_COLOUR),
Token.of(ANNOTATION, MiniJavaColours.ANNOTATION_COLOUR),
Token.of(KEYWORD, MiniJavaColours.KEYWORD_COLOUR)
```

Die Reihenfolge ist fachlich wichtig:

- Javadoc steht vor normalen Blockkommentaren.
- Kommentare stehen vor Strings, Chars und Keywords.
- Keywords stehen am Ende, damit sie bei Konflikten nicht stärkere Regionen
  verdrängen.

## 4. REGEX-ENTSCHEIDUNGEN

Strings:

```java
"\"([^\"\\\\]|\\\\.)*\""
```

Damit werden normale Strings und escaped Zeichen innerhalb von Strings erkannt.
Beispiele:

- `"hello"`
- `"text mit //"`
- `"text mit /* */"`

------------------------------------------------------------

Characters:

```java
"(?<![\\w\\\\'])'(?:[^'\\\\\\r\\n]|\\\\.)'(?![\\w'])"
```

Damit werden genau ein normales Zeichen oder ein escaped Zeichen erkannt.

Beispiele:

- `'a'`
- `'\n'`

Nicht akzeptiert werden absichtliche Fehlerfälle wie:

- `'ab'`
- `''`

Diese Stelle wurde im zweiten Prüfdurchlauf bewusst korrigiert, weil ein zu
lockeres Pattern sonst Teiltreffer in ungültigen Character-Literalen erzeugen
könnte.

------------------------------------------------------------

Keywords:

```java
"\\b(?:package|import|class|public|private|final|return|null|new|protected|extends|void|char|this)\\b"
```

Keywords werden als ganze Wörter erkannt.

Dadurch wird `new` in `mynewThing` nicht als Keyword markiert.

## 5. JUNIT-TESTS

Die Testklasse `MiniJavaTokensTest` enthält 6 Tests:

```text
stringLiteralsMatchAtStartMiddleAndEnd
characterLiteralsMatchSingleCharactersOnly
keywordsMatchWholeWordsOnly
annotationsMatchAtLineStartAndAfterWhitespace
commentsMatchByCommentKind
tokensReturnNoMatchesForPlainText
```

Die Tests prüfen:

- Treffer am Anfang, in der Mitte und am Ende,
- mehrere Treffer im selben Text,
- keinen Treffer,
- Annotationen am Zeilenanfang und mit Einrückung,
- Kommentare nach Kommentarart,
- Keywords als ganze Wörter,
- Strings mit Kommentarzeichen im Inhalt,
- Character-Grenzfälle.

## 6. WARUM SIND DIE TESTS RELEVANT?

Die Tests sind relevant, weil die Token die Grundlage für beide Highlighter sind.
Wenn hier ein Pattern zu breit oder zu eng ist, wird später auch das Highlighting
falsch.

Besonders wichtig sind die Grenzfälle:

- Keywords dürfen nicht in normalen Bezeichnern matchen.
- Strings dürfen Kommentarzeichen enthalten, ohne selbst Kommentare zu werden.
- Kommentare dürfen keyword-ähnlichen Text enthalten.
- Character-Literale dürfen keine zufälligen Teiltreffer erzeugen.

## 7. WARUM SIND DIE TESTS UNTERSCHIEDLICH?

Die Tests prüfen unterschiedliche Tokenarten und unterschiedliche fachliche
Situationen.

Ein String-Test prüft andere Regeln als ein Keyword-Test. Ein Kommentar-Test
prüft wieder andere Grenzen als ein Annotation-Test. Der Plain-Text-Test stellt
sicher, dass die Patterns nicht zu aggressiv matchen.

Damit sind Erfolgsfälle, Fehlerfälle und Grenzfälle abgedeckt.

## 8. VERIFIKATION

Ausgeführt wurde:

```powershell
.\gradlew.bat clean classes test spotlessCheck
.\gradlew.bat test
.\gradlew.bat spotlessCheck
```

Ergebnis:

```text
BUILD SUCCESSFUL
23 Tests
0 Failures
0 Skipped
```

## 9. FAZIT

Aufgabe 1 ist umgesetzt. Die Tokenliste ist vollständig, die wichtigsten
MiniJava-Bereiche werden erkannt, und die Tests decken normale Fälle sowie
kritische Grenzfälle ab.

