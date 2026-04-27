# B01 – Git Status erklären

## Aufgabenstellung

Die folgende Ausgabe von `git status` soll erklärt werden:

```text
pm-lecture % git status
On branch b03

Changes not staged for commit:
  (use "git add <file>..." to update what will be committed)
  (use "git restore <file>..." to discard changes in working directory)
        modified:   CONTRIBUTING.md
        modified:   homework/b03.md

Untracked files:
  (use "git add <file>..." to include in what will be committed)
        foo.java

no changes added to commit (use "git add" and/or "git commit -a")
```

Außerdem soll eine Befehlssequenz angegeben werden, mit der nur `foo.java` committed wird.

## Erklärung der Ausgabe

Der Befehl `git status` zeigt den aktuellen Zustand der lokalen Arbeitskopie.

Man befindet sich aktuell auf dem Branch `b03`.

Die Dateien `CONTRIBUTING.md` und `homework/b03.md` wurden verändert. Git kennt diese Dateien bereits, deshalb werden sie als `modified` angezeigt.

Diese Änderungen sind aber noch **not staged**. Das bedeutet: Sie sind noch nicht für den nächsten Commit vorgemerkt.

Die Datei `foo.java` ist **untracked**. Das bedeutet: Die Datei existiert im Arbeitsordner, wird aber von Git bisher noch nicht verfolgt.

Die Zeile

```text
no changes added to commit
```

bedeutet:

Aktuell liegt nichts in der Staging Area. Ein `git commit` würde deshalb noch keinen Commit mit diesen Änderungen erzeugen.

## Nur `foo.java` committen

Um nur `foo.java` zu committen, verwendet man:

```powershell
git add foo.java
git commit -m "Add foo.java"
```

## Erklärung der Befehle

```powershell
git add foo.java
```

nimmt nur `foo.java` in die Staging Area auf.

Vergleich mit GitHub Desktop:

```text
Nur bei foo.java einen Haken setzen.
```

```powershell
git commit -m "Add foo.java"
```

erstellt einen lokalen Commit mit der Commit-Nachricht `"Add foo.java"`.

Die Dateien `CONTRIBUTING.md` und `homework/b03.md` werden dabei nicht mitcommitted, weil sie nicht mit `git add` vorgemerkt wurden.

Ein `git push` ist für diese Aufgabe nicht erforderlich, weil nur nach dem lokalen Commit gefragt ist.

## Wichtige Begriffe

`modified` bedeutet: Git kennt die Datei bereits, aber sie wurde verändert.

`untracked` bedeutet: Git kennt die Datei noch nicht. Die Datei ist neu im Arbeitsordner.

`staged` bedeutet: Die Datei ist für den nächsten Commit vorgemerkt.

`commit` bedeutet: Ein gespeicherter Stand in Git.

`push` bedeutet: Lokale Commits werden in ein entferntes Repository hochgeladen.

`-m` steht bei `git commit -m` für `message`, also Commit-Nachricht.

## Zusatz: Push wäre erst danach möglich

Falls der Commit später hochgeladen werden soll, könnte man zusätzlich ausführen:

```powershell
git push origin b03
```

Dabei ist `origin` der Name des Remote-Repositorys, also der Online-Adresse des Repositories.

`b03` ist der Ziel-Branch auf dem Remote-Repository.

## Ergebnis

Die Ausgabe zeigt: Es gibt zwei veränderte bekannte Dateien und eine neue unbekannte Datei. Für den nächsten Commit ist noch nichts vorgemerkt. Mit `git add foo.java` und `git commit -m "Add foo.java"` wird nur die neue Datei `foo.java` committed.