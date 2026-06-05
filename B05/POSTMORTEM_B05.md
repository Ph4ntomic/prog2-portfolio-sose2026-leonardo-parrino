# Post Mortem: Blatt 05

## Zusammenfassung

Ich habe fuer Blatt 05 zwei Bereiche bearbeitet. Im Syntaxhighlighting-Projekt
wurde der neue `AntlrTokenCollector` umgesetzt, der den generierten
`MiniJavaLexer` nutzt und daraus Highlight-Regionen fuer Keywords, Literale,
Kommentare und Annotationen erzeugt. Zusaetzlich wurde ein einfacher
Pretty-Printer auf Basis des ANTLR-Visitors implementiert. Er erzeugt einen
Parse-Tree, traversiert Klassenkoerper, Bloecke und Statements und gibt den Code
mit konsistenter Einrueckung wieder aus. Im Cycle-Chronicles-Projekt wurden
Aequivalenzklassen und Grenzwerte fuer `Shop#accept` abgeleitet und als
JUnit-Tests mit Mockito umgesetzt. Als Bonus wurden `repair` und `deliver`
entsprechend der JavaDocs implementiert und getestet.

## Details

Interessant war der Unterschied zwischen Regex-, Scanner- und ANTLR-Loesung.
Beim ANTLR-Collector entstehen keine ueberlappenden Regionen mehr, weil der
Lexer bereits eine eindeutige Tokenfolge liefert. Beim Pretty-Printer sieht man
gut, dass der Parse-Tree nicht mehr den kompletten Originaltext enthaelt:
Kommentare und urspruengliche Leerzeichen fehlen, weil sie uebersprungen oder
auf den Hidden Channel gelegt werden.

## Reflexion: schwierigster Teil

Am schwierigsten war die Trennung zwischen Strukturformatierung und
Ausdrucksformatierung. Ich habe das geloest, indem nur die wichtigen Regeln
gezielt behandelt werden: Klassenkoerper, Bloecke, `if`/`else`, `while` und
Semikolon-Statements. Fuer einfache Ausdruecke reicht der Standardbesuch der
Kindknoten.

## Reflexion: gelernt

Ich habe besser verstanden, warum ein Lexer Konflikte beim Highlighting stark
reduziert und warum Pretty Printing schnell komplex wird. Ausserdem wurde beim
Mocking klar, dass nicht die zu testende Klasse gemockt werden darf, sondern nur
die unvollstaendige Abhaengigkeit `Order`.

## Links

- Syntaxhighlighting:
  <https://github.com/Ph4ntomic/prog2_ybel_syntaxhighlighting/tree/master>
- Cycle Chronicles:
  <https://github.com/Ph4ntomic/prog2_ybel_cyclechronicles/tree/master>
