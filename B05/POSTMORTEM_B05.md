# Post Mortem: Blatt 05

## Zusammenfassung

Ich habe in Blatt 05 an zwei Projekten gearbeitet. Im Syntaxhighlighting-Projekt
habe ich den `AntlrTokenCollector` umgesetzt und dafuer den von ANTLR
generierten `MiniJavaLexer` verwendet. Dadurch werden Keywords, Literale,
Kommentare und Annotationen nicht mehr ueber eigene Regex-Patterns gesucht,
sondern direkt aus dem Tokenstream erzeugt. Ausserdem habe ich einen einfachen
Pretty-Printer mit ANTLR-Parser und Visitor gebaut. Dieser erzeugt einen
Parse-Tree und gibt MiniJava-Code mit konsistenten Bloecken, Zeilenumbruechen
und Einrueckungen aus. Im Cycle-Chronicles-Projekt habe ich
Aequivalenzklassen und Grenzwerte fuer `Shop#accept` abgeleitet und daraus
JUnit-Tests mit Mockito erstellt. Als Bonus habe ich `repair` und `deliver`
implementiert und ebenfalls getestet.

## Details

Besonders interessant fand ich den Unterschied zwischen Regex, Scanner und
ANTLR. Bei der ANTLR-Variante entstehen praktisch keine ueberlappenden
Highlight-Regionen mehr, weil der Lexer bereits eine eindeutige Tokenfolge
liefert. Beim Pretty-Printer ist mir aufgefallen, dass der Parse-Tree nicht mehr
den kompletten Originaltext enthaelt. Kommentare und urspruengliche Leerzeichen
fehlen, weil sie uebersprungen oder auf den Hidden Channel gelegt werden.

## Reflexion: schwierigster Teil

Am schwierigsten war fuer mich die Abgrenzung beim Pretty Printing. Ich musste
entscheiden, welche Regeln ich wirklich selbst formatiere und wo der normale
Besuch der Kindknoten reicht. Geloest habe ich das, indem ich mich auf
Klassenkoerper, Bloecke, `if`/`else`, `while` und Semikolon-Statements
konzentriert habe.

## Reflexion: gelernt

Ich habe besser verstanden, warum ein Lexer beim Highlighting robuster ist als
einzelne Regex-Treffer. Beim Mocking habe ich gelernt, dass ich nicht die zu
testende Klasse `Shop` mocken darf, sondern nur die unvollstaendige Abhaengigkeit
`Order`.

## Links

- Portfolio B05:
  <https://github.com/Ph4ntomic/prog2-portfolio-sose2026-leonardo-parrino/tree/main/B05>
- Syntaxhighlighting-Projekt:
  <https://github.com/Ph4ntomic/prog2_ybel_syntaxhighlighting/tree/master>
- Syntaxhighlighting B05-Dokumentation:
  <https://github.com/Ph4ntomic/prog2_ybel_syntaxhighlighting/blob/master/documentation/AUFGABE_05_ANTLR_PRETTY_PRINTING.md>
- Cycle-Chronicles-Projekt:
  <https://github.com/Ph4ntomic/prog2_ybel_cyclechronicles/tree/master>
- Cycle-Chronicles Analyse:
  <https://github.com/Ph4ntomic/prog2_ybel_cyclechronicles/blob/master/documentation/AUFGABE_05_ACCEPT_ANALYSIS.md>
- Review-Nachweise:
  <https://github.com/Ph4ntomic/prog2_ybel_syntaxhighlighting/tree/master/documentation/nachweisreviews>
