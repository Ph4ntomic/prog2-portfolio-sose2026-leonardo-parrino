# GitQuest – Aufgaben 04.5 bis 04.8

## Übersicht

In diesem Abschnitt wurden mehrere Änderungen an der GitQuest durchgeführt:

| Aufgabe | Inhalt | Geänderte Dateien |
|---|---|---|
| 04.5 | Letzten Commit nachträglich korrigieren | `stats.md` |
| 04.6 | Geschichte in `questlog.md` fortsetzen | `questlog.md` |
| 04.7 | Mehrere Dateien gemeinsam committen | `stats.md`, `rucksack.md` |
| 04.8 | Ausrüstung aus `stats.md` in `gear.md` verschieben | `stats.md`, `gear.md` |

---

# Aufgabe 04.5 – Letzten Commit ändern

## Aufgabenstellung

Beim letzten Commit `tag 04.5` ist etwas schiefgelaufen.  
Es wurden versehentlich zu wenig Experience-Punkte eingetragen.

Der letzte Commit sollte deshalb nachträglich geändert werden.  
Die Experience-Punkte wurden in `stats.md` auf `42` angepasst.

## Geänderte Datei

`stats.md`

## Änderung in `stats.md`

Vorher:

```md
| experience | 40 |
```

Nachher:

```md
| experience | 42 |
```

## Verwendete Git-Befehle

```powershell
git status
```

Prüft den aktuellen Zustand des Repositorys.

```powershell
notepad stats.md
```

Öffnet die Datei `stats.md`, damit der Wert angepasst werden kann.

```powershell
git diff stats.md
```

Zeigt die Änderung an der Datei `stats.md`.

```powershell
git add stats.md
```

Fügt nur die geänderte Datei `stats.md` zur Staging Area hinzu.

```powershell
git commit --amend --no-edit
```

Ändert den letzten Commit, ohne die bestehende Commit-Nachricht zu verändern.

```powershell
git log --oneline -1
```

Prüft den neuen letzten Commit.

```powershell
git show HEAD -- stats.md
```

Kontrolliert, ob `experience` im letzten Commit korrekt auf `42` steht.

```powershell
git push --force-with-lease origin master
```

Aktualisiert den bereits gepushten Commit sicher auf GitHub.

## Erklärung zu `--amend`

`--amend` bedeutet, dass der letzte Commit nachträglich geändert wird.

Dabei wird kein zusätzlicher neuer Commit erstellt.  
Stattdessen wird der letzte Commit durch eine korrigierte Version ersetzt.

`--no-edit` bedeutet, dass die vorhandene Commit-Nachricht beibehalten wird.

## Ergebnis

Der letzte Commit wurde nachträglich korrigiert.  
Die Datei `stats.md` enthält jetzt den korrekten Wert:

```md
| experience | 42 |
```

Damit ist Aufgabe 04.5 erfüllt.

---

# Aufgabe 04.6 – Questlog fortsetzen

## Aufgabenstellung

Die Geschichte sollte in der Datei `questlog.md` fortgeschrieben werden.  
Dabei durfte nur die Datei `questlog.md` geändert werden.

Die Änderung sollte als neuer Commit für `tag 04.6` eingecheckt werden.

## Geänderte Datei

`questlog.md`

## Inhalt von `questlog.md`

```md
# Questlog

Der Held Markus geht glücklich mit seinem Amulett zurück nach Hause an seinen Computer.
```

## Verwendete Git-Befehle

```powershell
git status
```

Prüft, welche Datei geändert wurde.

```powershell
git add questlog.md
```

Fügt nur die Datei `questlog.md` zur Staging Area hinzu.

```powershell
git status
```

Kontrolliert, ob wirklich nur `questlog.md` für den Commit vorgemerkt ist.

```powershell
git commit -m "Die Geschichte geht weiter..."
```

Erstellt einen neuen Commit für Aufgabe 04.6.

```powershell
git log --oneline -1
```

Prüft den zuletzt erstellten Commit.

## Ergebnis

Die Geschichte wurde in `questlog.md` fortgesetzt.  
Es wurde ein neuer Commit erstellt.

Damit ist Aufgabe 04.6 erfüllt.

## Hinweis zum Push

Beim Push kam folgende Meldung:

```text
remote: Permission to Programmiermethoden-CampusMinden/prog2_ybel_gitquest.git denied to Ph4ntomic.
```

Das bedeutet:  
Der Push ging auf ein Repository, für das keine Schreibrechte vorhanden sind.

Die lokalen Commits sind trotzdem vorhanden.  
Für GitHub müsste auf das eigene Repository gepusht werden.

---

# Aufgabe 04.7 – Mehrere Dateien gemeinsam committen

## Aufgabenstellung

Die Geschichte sollte weiter fortgeschrieben werden.  
Diesmal sollten mehrere Dateien geändert und gemeinsam in einem neuen Commit eingecheckt werden.

Geänderte Dateien:

```text
stats.md
rucksack.md
```

## Änderung in `stats.md`

```md
# Stats

| Property   | Value         |
|------------|---------------|
| health     | 10            |
| experience | 40            |
| hunger     | 10            |
| weapon     | sword (3 dmg) |
| armor      | light (2 dmg) |
```

## Änderung in `rucksack.md`

```md
# Rucksack

| Slot | Content     |
|------|-------------|
| 0    | 1 Heiltrank |
| 1    | 1 Amulett   |
| 2    |             |
| 3    |             |
| 4    |             |
| 5    |             |
| 6    |             |
```

## Verwendete Git-Befehle

```powershell
git status
```

Prüft, welche Dateien geändert wurden.

```powershell
git add stats.md rucksack.md
```

Fügt genau die beiden benötigten Dateien zur Staging Area hinzu.

```powershell
git status
```

Kontrolliert, ob `stats.md` und `rucksack.md` vorgemerkt sind.

```powershell
git commit -m "Der Held sitzt jetzt glücklich an seinem Computer"
```

Erstellt einen neuen Commit mit beiden Dateien.

```powershell
git log --oneline -1
```

Prüft den zuletzt erstellten Commit.

## Ergebnis

Die Dateien `stats.md` und `rucksack.md` wurden gemeinsam in einem Commit eingecheckt.

Damit ist Aufgabe 04.7 erfüllt.

---

# Aufgabe 04.8 – Ausrüstung aus `stats.md` in `gear.md` verschieben

## Aufgabenstellung

Bisher wurden Statuspunkte und Ausrüstung gemeinsam in der Datei `stats.md` geführt.

Das sollte korrigiert werden:

- Statuswerte bleiben in `stats.md`
- Ausrüstung wird in eine neue Datei `gear.md` verschoben
- Die Änderung wird als neuer Commit für `tag 04.8` eingecheckt
- Die Änderung muss nur am aktuellen Stand des `master`-Branches erfolgen
- Die Historie muss nicht rückwirkend korrigiert werden

## Geänderte Dateien

```text
stats.md
gear.md
```

## Neuer Inhalt von `stats.md`

```md
# Stats

| Property   | Value |
|------------|-------|
| health     | 10    |
| experience | 40    |
| hunger     | 10    |
```

## Neuer Inhalt von `gear.md`

```md
# Gear

| Item   | Stats         |
|--------|---------------|
| weapon | sword (3 dmg) |
| armor  | light (2 dmg) |
```

## Erklärung der Änderung

Die Ausrüstungsgegenstände `weapon` und `armor` wurden aus `stats.md` entfernt.

Sie stehen jetzt sauber getrennt in der neuen Datei `gear.md`.

Dadurch sind Statuswerte und Ausrüstung logisch voneinander getrennt.

## Verwendete Git-Befehle

```powershell
git status
```

Prüft, welche Dateien geändert oder neu erstellt wurden.

```powershell
git add stats.md gear.md
```

Fügt die geänderte Datei `stats.md` und die neue Datei `gear.md` zur Staging Area hinzu.

```powershell
git status
```

Kontrolliert, ob beide Dateien korrekt vorgemerkt sind.

```powershell
git commit -m "remove gear from stats table and move it in gear table"
```

Erstellt einen neuen Commit für Aufgabe 04.8.

```powershell
git log --oneline -1
```

Prüft den zuletzt erstellten Commit.

## Ergebnis

Die Ausrüstung wurde aus `stats.md` entfernt und in die neue Datei `gear.md` verschoben.

Damit sind Statuswerte und Ausrüstung sauber getrennt.

Aufgabe 04.8 ist damit erfüllt.

---

# Wichtiger Prüfhinweis

In Aufgabe 04.5 wurde `experience` auf `42` korrigiert.

In den späteren Abschnitten steht `experience` wieder auf `40`.

Das sollte vor der finalen Abgabe geprüft werden.

Wenn `experience` dauerhaft `42` bleiben soll, muss der Wert in `stats.md` auch bei Aufgabe 04.7 und 04.8 auf `42` stehen.

Korrekte Variante wäre dann:

```md
| experience | 42 |
```

---

# Kurzes Post Mortem

## Was wurde gemacht?

Ich habe gelernt, wie man mit Git einzelne Dateien gezielt staged, Commits erstellt und den letzten Commit mit `--amend` nachträglich korrigiert.

Außerdem habe ich gelernt, dass zusammengehörige Änderungen gemeinsam committed werden sollten.  
Bei Aufgabe 04.7 wurden deshalb `stats.md` und `rucksack.md` gemeinsam eingecheckt.

Bei Aufgabe 04.8 wurde die Projektstruktur verbessert, indem Ausrüstung aus `stats.md` ausgelagert und in eine eigene Datei `gear.md` verschoben wurde.

## Was war schwierig?

Schwierig war vor allem der Unterschied zwischen normalen Commits und dem nachträglichen Ändern eines Commits mit `git commit --amend`.

Außerdem war wichtig zu erkennen, wann nur eine einzelne Datei geändert werden darf und wann mehrere Dateien gemeinsam committed werden sollen.

## Was habe ich gelernt?

Ich habe gelernt:

```text
git add <datei>
```

fügt gezielt eine Datei zur Staging Area hinzu.

```text
git commit -m "Nachricht"
```

erstellt einen neuen Commit.

```text
git commit --amend --no-edit
```

ändert den letzten Commit, ohne die Commit-Nachricht zu verändern.

```text
git status
```

ist der wichtigste Kontrollbefehl vor jedem Commit.

## Ergebnis

Die Aufgaben 04.5 bis 04.8 wurden bearbeitet und dokumentiert.

Die wichtigsten Git-Konzepte waren:

```text
Staging Area
Commit
Amend
gezieltes Hinzufügen einzelner Dateien
gemeinsames Committen zusammengehöriger Änderungen
saubere Dateistruktur
```