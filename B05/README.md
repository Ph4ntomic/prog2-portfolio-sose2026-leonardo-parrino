# Dokumentation zu Blatt 05

Diese Dokumentation ist nach den bearbeiteten Aufgabenteilen getrennt aufgebaut.

## Dateien

- `AUFGABE_01_ANTLR_TOKEN_COLLECTOR.md`
- `AUFGABE_02_PRETTY_PRINTER.md`
- `AUFGABE_03_CYCLE_CHRONICLES_MOCKING.md`
- `POSTMORTEM_B05.md`

## Kurzstand

Technisch erledigt:

- Aufgabe 1.1: `AntlrTokenCollector` mit generiertem `MiniJavaLexer`
- Aufgabe 1.2: Pretty Printer mit ANTLR-Parser und Visitor
- Aufgabe 1.3: Konsolen-Demo mit Eingabe der Einrueckungsbreite
- Aufgabe 2.1: Aequivalenzklassen und Grenzwertanalyse fuer `Shop#accept`
- Aufgabe 2.2: JUnit-Tests mit Mockito fuer `Shop#accept`
- Bonus 2.3: Tests und Implementierung fuer `repair` und `deliver`

Lokal geprueft:

```powershell
.\gradlew.bat clean classes test spotlessCheck
.\gradlew.bat clean test spotlessCheck
```

Ergebnis:

```text
Syntaxhighlighting:
35 Tests
0 Failures
0 Skipped
BUILD SUCCESSFUL

Cycle Chronicles:
15 Tests
0 Failures
0 Skipped
BUILD SUCCESSFUL
```

## Repositories

Syntaxhighlighting:

```text
https://github.com/Ph4ntomic/prog2_ybel_syntaxhighlighting
```

GitHub-Stand:

```text
master: e88a342 fix: preserve spaces around annotations
```

Cycle Chronicles:

```text
https://github.com/Ph4ntomic/prog2_ybel_cyclechronicles
```

GitHub-Stand:

```text
master: 7b5a751 test: cover repaired customer reordering
```
