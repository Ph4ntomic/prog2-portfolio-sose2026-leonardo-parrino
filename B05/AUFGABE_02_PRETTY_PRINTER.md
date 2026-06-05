# ============================================================
# AUFGABE 1.2 UND 1.3: ANTLR-PRETTY-PRINTER
# ============================================================

## 1. Aufgabenstellung

In Aufgabe 1.2 sollte mit den von ANTLR generierten Klassen ein einfacher
Pretty Printer fuer MiniJava-Code gebaut werden.

Der Ablauf ist:

```text
1. Lexer erzeugen
2. Parser erzeugen
3. Parse-Tree vom Typ CompilationUnitContext erzeugen
4. Parse-Tree mit einem Visitor traversieren
5. neu formatierten Quelltext ausgeben
```

Zusaetzlich sollte in Aufgabe 1.3 auf der Konsole nach der gewuenschten Anzahl
von Leerzeichen pro Einrueckungsstufe gefragt werden.

## 2. Umgesetzte Dateien

Angelegt wurden:

```text
src/main/java/highlighting/antlr/PrettyPrinter.java
src/main/java/highlighting/antlr/PrettyPrinterVisitor.java
```

Geaendert wurde:

```text
src/main/java/highlighting/Main.java
```

Getestet wurde:

```text
src/test/java/highlighting/antlr/PrettyPrinterTest.java
```

## 3. Parse-Tree erzeugen

Die Hilfsklasse `PrettyPrinter` kapselt den Aufbau von Lexer, Tokenstream und
Parser.

```java
public static MiniJavaParser.CompilationUnitContext parse(String source) {
    var lexer = new MiniJavaLexer(CharStreams.fromString(source));
    lexer.removeErrorListeners();
    var tokens = new CommonTokenStream(lexer);
    var parser = new MiniJavaParser(tokens);
    parser.removeErrorListeners();
    return parser.compilationUnit();
}
```

Die Formatierung wird dann so gestartet:

```java
public static String format(String source, int indentWidth) {
    var tree = parse(source);
    var visitor = new PrettyPrinterVisitor(indentWidth);
    visitor.visit(tree);
    return visitor.result();
}
```

## 4. Visitor

Der Visitor erweitert die generierte Basisklasse:

```java
public class PrettyPrinterVisitor extends MiniJavaBaseVisitor<Void>
```

Umgesetzt wurden besonders diese Methoden:

```text
visitCompilationUnit
visitClassBody
visitBlock
visitStatement
visitTerminal
```

Die Aufgabe verlangte mindestens die ersten vier Methoden. `visitTerminal`
wurde zusaetzlich angepasst, damit Tokens sinnvoll mit Leerzeichen und
Zeilenumbruechen ausgegeben werden koennen.

## 5. Formatierungsregeln

Umgesetzt wurden die wichtigsten Strukturregeln:

| Regel | Umsetzung |
|---|---|
| `{` oeffnet einen Block | danach Zeilenumbruch |
| Blockinhalt | eine Stufe eingerueckt |
| `}` | eigene Zeile auf passender Einrueckung |
| Statement mit `;` | eigene Zeile |
| `if`/`else` | strukturiert mit Block oder eingeruecktem Einzelstatement |
| `while` | strukturiert mit Block oder eingeruecktem Einzelstatement |

## 6. Beispiel: einfache Klasse

Eingabe:

```java
class Demo{private String name;public void run(){String x="a";return null;}}
```

Ausgabe mit Einrueckung `2`:

```java
class Demo {
  private String name;
  public void run() {
    String x = "a";
    return null;
  }
}
```

## 7. Beispiel: if, else und while

Eingabe:

```java
class Demo{public void run(){while(active){if(done){return null;}else{{return "open";}}}}}
```

Ausgabe:

```java
class Demo {
  public void run() {
    while (active) {
      if (done) {
        return null;
      } else {
        {
          return "open";
        }
      }
    }
  }
}
```

## 8. Beispiel: Einzelstatement ohne Block

Eingabe:

```java
class Demo{public void run(){if(done)return null;}}
```

Ausgabe:

```java
class Demo {
  public void run() {
    if (done)
      return null;
  }
}
```

## 9. Konsolen-Demo

In `Main` wird eine kleine Demo gestartet:

```java
runPrettyPrinterDemo();
```

Die Einrueckungsbreite wird ueber `System.in` gelesen:

```java
System.out.print("Einrueckung pro Stufe (2, 4 oder 8): ");
```

Wenn keine Eingabe vorhanden ist oder die Eingabe ungueltig ist, wird als
Fallback verwendet:

```text
4 Leerzeichen
```

Danach wird der Editor mit dem ANTLR-Highlighter gestartet:

```java
SyntaxHighlighter antlrToken = new AntlrTokenCollector();
EditorUI.show(Texts.START_TEXT, antlrToken);
```

## 10. Warum fehlen Kommentare?

Bei der Pretty-Printer-Ausgabe fehlen Kommentare und urspruengliche
Leerzeichen.

Der Grund liegt in der Grammatik:

```antlr
LINE_COMMENT    : '//' ~[\r\n]*           -> channel(HIDDEN);
JAVADOC_COMMENT : '/**' (.|[\r\n])*? '*/' -> channel(HIDDEN);
BLOCK_COMMENT   : '/*'  (.|[\r\n])*? '*/' -> channel(HIDDEN);
WS              : [ \t\r\n]+              -> skip;
```

Whitespace wird uebersprungen. Kommentare liegen auf dem Hidden Channel.

Der Visitor arbeitet auf dem Parse-Tree. Dadurch sieht er nur die syntaktisch
relevanten Bestandteile und nicht mehr den kompletten Originaltext.

## 11. Tests

Die Testklasse `PrettyPrinterTest` prueft:

```text
formatsClassMembersAndMethodBlocks
formatsIfElseWhileAndNestedBlocks
keepsPackageImportsAndTypesOnSeparateLines
indentsControlledStatementWithoutExplicitBlock
keepsSpaceAfterAnnotationWithArguments
keepsSpaceBeforeAnnotationAfterModifier
```

Damit werden einfache Klassen, Methoden, verschachtelte Bloecke,
Kontrollstrukturen, Package- und Import-Zeilen, Einzelstatements sowie
Annotationen vor und nach Modifiern getestet.

## 12. Verifikation

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

## 13. Fazit

Der Pretty Printer formatiert MiniJava-Code strukturiert. Er ist bewusst auf
die wichtigsten Regeln konzentriert und versucht nicht, jeden Ausdruck perfekt
zu verschonern. Dadurch bleibt die Loesung nachvollziehbar und passend zur
Aufgabenstellung.
