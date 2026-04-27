## Commit-Meldungen analysieren

### Commit `46530b6`

Die Commit-Meldung lautet sinngemäß:

```text
Layer system 5 (#300)
```

Zusätzlich gibt es eine Beschreibung mit mehreren Punkten, zum Beispiel `rebase`, `spotlessApply`, `rebase/merge` und Änderungen an der `AbstractController`-API.

**Gut:**

Man erkennt grob, dass es um ein Layer-System und Controller-Strukturen geht.

**Störend:**

Die Meldung wirkt etwas überladen. Mehrere Themen stehen in einem Commit. Dadurch ist nicht sofort klar, was die Hauptänderung ist.

**Verbesserung:**

Besser wäre eine klarere Commit-Meldung, zum Beispiel:

```text
refactor: update layer system controller API
```

### Commit `3e37472`

Die Commit-Meldung lautet sinngemäß:

```text
[GAME] better Items and Crafting settings (#1090)
```

In der Beschreibung geht es um Items, `onUse`-Methoden und Crafting-Rezepte.

**Gut:**

Die Meldung ist leichter verständlich. Man erkennt direkt, dass es um Items und Crafting geht.

**Störend:**

Das Wort `better` ist etwas ungenau. Es sagt nicht genau, was verbessert wurde.

**Verbesserung:**

Besser wäre eine konkretere Commit-Meldung, zum Beispiel:

```text
feat: add item use effects and crafting recipes
```

## Ergebnis

Der zweite Commit ist für mich leichter zu verstehen, weil man direkt erkennt, welcher Spielbereich betroffen ist.

Eine gute Commit-Meldung sollte kurz sagen, was geändert wurde und welchen Bereich es betrifft. Außerdem sollte sie möglichst konkret sein und keine ungenauen Wörter wie `better` verwenden.