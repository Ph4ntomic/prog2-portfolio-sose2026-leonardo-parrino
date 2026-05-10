# Blatt 02: Git Branches, JUnit Basics und CI-Pipeline

## Inhaltsverzeichnis

1. Ziel des Übungsblatts  
2. Teil A: Git-Spiel  
   1. Ausgangssituation  
   2. Wichtige Git-Grundlagen  
   3. Aufgabe 1: Unterschiedliche Dateien ändern  
   4. Aufgabe 2: Gleiche Datei, andere Stelle ändern  
   5. Aufgabe 3: Gleiche Stelle ändern  
   6. Aufgabe 4: Branch `end` vor dem Merge auf `master` setzen  
   7. Merge-Konflikte lösen  
   8. Gesamtübersicht Git-Spiel  
3. Teil B: Katzen-Café mit Gradle  
   1. Ziel  
   2. Projektstruktur  
   3. `build.gradle`  
   4. `settings.gradle`  
   5. Gradle-Befehle  
4. Teil C: JUnit-Tests  
5. Teil D: CI-Pipeline mit GitHub Actions  
6. Befehlsübersicht  
7. Lernzusammenfassung  

---

# 1. Ziel des Übungsblatts

In Blatt 02 werden mehrere wichtige Grundlagen aus der praktischen Softwareentwicklung geübt.

Der erste Schwerpunkt ist der Umgang mit Git-Branches. Dabei wird untersucht, wie Git Änderungen aus verschiedenen Branches zusammenführt, wann ein Merge automatisch funktioniert und wann ein Merge-Konflikt entsteht.

Der zweite Schwerpunkt ist ein Java-Projekt mit Gradle. Gradle wird verwendet, um das Projekt zu bauen, Tests auszuführen, die Anwendung zu starten und die Formatierung zu prüfen.

Der dritte Schwerpunkt ist eine einfache CI-Pipeline mit GitHub Actions. Diese Pipeline soll automatisch prüfen, ob das Projekt kompiliert, ob die JUnit-Tests erfolgreich sind und ob die Formatierung korrekt ist.

---

# 2. Teil A: Git-Spiel

## 2.1 Ausgangssituation

Im Git-Spiel wird mit dem Repository zur Git-Quest gearbeitet.

Die Geschichte des Helden Markus findet im Branch `master` noch kein vollständiges Ende. Der Hilfsbranch `end` enthält zusätzliche Änderungen.

Die Aufgabe besteht darin, verschiedene Merge-Situationen zu erzeugen und zu beobachten.

Dabei soll für jede Teilaufgabe mit einem frischen Klon gestartet werden. Dadurch wird verhindert, dass Änderungen aus vorherigen Versuchen das Ergebnis verfälschen.

---

## 2.2 Wichtige Git-Grundlagen

## Branch anzeigen

```bash
git branch -a
```

Dieser Befehl zeigt alle Branches an.

Dabei werden lokale Branches und entfernte Branches angezeigt.

Beispiel:

```text
master
remotes/origin/HEAD -> origin/master
remotes/origin/end
remotes/origin/master
```

Bedeutung:

| Eintrag | Bedeutung |
|---|---|
| `master` | lokaler Hauptbranch |
| `origin/master` | entfernter Hauptbranch |
| `origin/end` | entfernter Branch `end` |

---

## Auf `master` wechseln

```bash
git checkout master
```

Dieser Befehl wechselt auf den Branch `master`.

Alternativ geht bei neueren Git-Versionen auch:

```bash
git switch master
```

In dieser Dokumentation wird überwiegend `git checkout` verwendet, weil es auch in den praktischen Versuchen benutzt wurde.

---

## Status prüfen

```bash
git status
```

Dieser Befehl zeigt den aktuellen Zustand des Arbeitsverzeichnisses.

Wichtige Ausgabe:

```text
nothing to commit, working tree clean
```

Bedeutung:

Der Arbeitsstand ist sauber. Es gibt keine offenen Änderungen.

Das ist besonders wichtig vor und nach einem Merge.

---

## Unterschiede zwischen Branches anzeigen

```bash
git diff --name-only master..origin/end
```

Bedeutung:

| Teil | Bedeutung |
|---|---|
| `git diff` | vergleicht zwei Stände |
| `--name-only` | zeigt nur Dateinamen, nicht den Inhalt |
| `master..origin/end` | vergleicht `master` mit `origin/end` |

Beispielausgabe:

```text
questlog.md
rucksack.md
stats.md
```

Das bedeutet:

Diese Dateien wurden im Branch `origin/end` verändert.

Wenn man eine Datei braucht, die in `end` nicht verändert wurde, sollte man also eine andere Datei nehmen, zum Beispiel:

```text
hero.md
```

---

## Commit-Verlauf grafisch anzeigen

```bash
git log --oneline --graph --decorate --all
```

Bedeutung der Optionen:

| Option | Bedeutung |
|---|---|
| `--oneline` | zeigt jeden Commit kurz in einer Zeile |
| `--graph` | zeigt Branches und Merges als Text-Grafik |
| `--decorate` | zeigt Branch-Namen und Tags neben Commits |
| `--all` | zeigt alle Branches |

Dieser Befehl ist wichtig, um nach einem Merge zu prüfen, ob der eigene Commit und der Merge sichtbar sind.

---

## Wichtiger Merksatz zu `git merge`

```bash
git merge origin/end
```

bedeutet nicht, dass man in `origin/end` wechselt.

Es bedeutet:

```text
Hole die Änderungen aus origin/end in meinen aktuellen Branch.
```

Wenn man aktuell auf `master` ist, dann bedeutet der Befehl:

```text
Führe origin/end in master zusammen.
```

---

# 3. Aufgabe 1: Unterschiedliche Dateien ändern

## 3.1 Aufgabenstellung

Eine Datei ändern, die im Branch `end` nicht verändert wurde.

Danach wird diese Änderung auf `master` committed.

Anschließend wird `origin/end` in `master` gemerged.

---

## 3.2 Vorgehen

Zuerst auf `master` wechseln:

```bash
git checkout master
```

Dann prüfen, welche Dateien in `origin/end` verändert wurden:

```bash
git diff --name-only master..origin/end
```

Beispielausgabe:

```text
questlog.md
rucksack.md
stats.md
```

Diese Dateien wurden in `end` bereits verändert.

Deshalb wird für Aufgabe 1 eine andere Datei verwendet, zum Beispiel:

```text
hero.md
```

Danach wird die Datei geändert.

Status prüfen:

```bash
git status
```

Datei vormerken:

```bash
git add hero.md
```

Commit erstellen:

```bash
git commit -m "docs: change hero file on master for git-game task b02"
```

Jetzt `origin/end` in den aktuellen Branch `master` mergen:

```bash
git merge origin/end
```

Danach wieder prüfen:

```bash
git status
```

Commit-Verlauf anzeigen:

```bash
git log --oneline --graph --decorate --all
```

---

## 3.3 Beobachtung

Der Merge läuft normalerweise sauber durch.

Es entsteht kein Merge-Konflikt.

---

## 3.4 Erklärung

Auf `master` wurde eine Datei geändert, die im Branch `end` nicht geändert wurde.

Git kann beide Änderungen automatisch zusammenführen, weil sie sich nicht überschneiden.

Git muss also nicht entscheiden, welche Änderung richtig ist.

---

## 3.5 Ergebnis

```text
Unterschiedliche Dateien geändert -> Merge läuft normalerweise sauber.
```

---

# 4. Aufgabe 2: Gleiche Datei, aber andere Stelle ändern

## 4.1 Aufgabenstellung

Eine Datei ändern, die auch im Branch `end` verändert wurde.

Wichtig:

Die Änderung soll an einer anderen Stelle erfolgen als die Änderung in `end`.

Danach wird die Änderung auf `master` committed.

Anschließend wird `origin/end` in `master` gemerged.

---

## 4.2 Vorgehen

Mit einem frischen Klon starten.

Auf `master` wechseln:

```bash
git checkout master
```

Prüfen, welche Dateien in `origin/end` verändert wurden:

```bash
git diff --name-only master..origin/end
```

Beispielausgabe:

```text
questlog.md
rucksack.md
stats.md
```

Für diese Aufgabe eignet sich zum Beispiel:

```text
questlog.md
```

Wichtig:

Im Branch `end` wurde zum Beispiel das Ende der Geschichte ergänzt.

Auf `master` wird deshalb an einer anderen Stelle ein zusätzlicher Satz eingefügt.

Danach Status prüfen:

```bash
git status
```

Datei vormerken:

```bash
git add questlog.md
```

Commit erstellen:

```bash
git commit -m "docs: change questlog on master"
```

Branch `origin/end` in `master` mergen:

```bash
git merge origin/end
```

Danach prüfen:

```bash
git status
```

Commit-Verlauf anzeigen:

```bash
git log --oneline --graph --decorate --all
```

---

## 4.3 Beobachtung

Der Merge läuft normalerweise sauber durch.

Es gibt keinen Konflikt, obwohl dieselbe Datei geändert wurde.

---

## 4.4 Erklärung

Gleiche Datei bedeutet nicht automatisch Konflikt.

Git versucht nicht nur auf Dateiebene zu mergen, sondern betrachtet auch die konkreten geänderten Stellen.

Wenn zwei Branches dieselbe Datei an unterschiedlichen Stellen verändern, kann Git diese Änderungen meistens automatisch zusammenführen.

---

## 4.5 Ergebnis

```text
Gleiche Datei, andere Stelle geändert -> Merge kann trotzdem sauber laufen.
```

---

# 5. Aufgabe 3: Gleiche Stelle ändern

## 5.1 Aufgabenstellung

Wie Aufgabe 2, aber diesmal wird eine Stelle geändert, die auch im Branch `end` verändert wurde.

Es gibt zwei Fälle:

| Fall | Situation |
|---|---|
| Fall A | Die Änderung auf `master` ist identisch zu der Änderung in `end` |
| Fall B | Die Änderung auf `master` ist anders als die Änderung in `end` |

---

# 5.2 Fall A: Gleiche Stelle und gleiche Änderung

## Vorgehen

Mit einem frischen Klon starten.

Auf `master` wechseln:

```bash
git checkout master
```

Für diese Aufgabe wird zum Beispiel die Datei verwendet:

```text
rucksack.md
```

Zuerst prüfen, was in `origin/end` verändert wurde:

```bash
git diff master..origin/end -- rucksack.md
```

Beispiel:

```text
| 1 | 1 Amulett |
```

Jetzt wird auf `master` an genau derselben Stelle dieselbe Änderung eingetragen:

```text
| 1 | 1 Amulett |
```

Danach:

```bash
git status
git add rucksack.md
git commit -m "docs: change rucksack on master"
git merge origin/end
```

---

## Beobachtung

Es entsteht normalerweise kein Konflikt.

---

## Erklärung

Obwohl dieselbe Stelle geändert wurde, ist das Ergebnis auf beiden Branches identisch.

Git erkennt:

```text
Beide Branches wollen denselben Endzustand.
```

Deshalb kann Git den Merge automatisch abschließen.

---

## Ergebnis

```text
Gleiche Stelle + gleiche Änderung -> normalerweise kein Konflikt.
```

---

# 5.3 Fall B: Gleiche Stelle, aber unterschiedliche Änderung

## Vorgehen

Mit einem frischen Klon starten.

Auf `master` wechseln:

```bash
git checkout master
```

Die Datei `rucksack.md` wird wieder verwendet.

Im Branch `end` steht zum Beispiel:

```text
| 1 | 1 Amulett |
```

Auf `master` wird dieselbe Stelle anders geändert:

```text
| 1 | 1 Ring |
```

Danach:

```bash
git status
git add rucksack.md
git commit -m "docs: change rucksack on master"
git merge origin/end
```

---

## Beobachtung

Jetzt entsteht ein Merge-Konflikt.

Typische Ausgabe:

```text
Auto-merging rucksack.md
CONFLICT (content): Merge conflict in rucksack.md
Automatic merge failed; fix conflicts and then commit the result.
```

Git kann nicht automatisch entscheiden, welche Änderung richtig ist.

---

## Konfliktmarker in der Datei

In der Datei erscheinen Konfliktmarker:

```text
<<<<<<< HEAD
| 1 | 1 Ring |
=======
| 1 | 1 Amulett |
>>>>>>> origin/end
```

Bedeutung:

| Marker | Bedeutung |
|---|---|
| `<<<<<<< HEAD` | Änderung aus dem aktuellen Branch, hier `master` |
| `=======` | Trennt beide Varianten |
| `>>>>>>> origin/end` | Änderung aus dem Branch, der hineingemerged wird |

---

## Konflikt manuell lösen

Die Datei muss geöffnet und manuell korrigiert werden.

Eine mögliche Lösung ist:

```text
| 1 | 1 Amulett und 1 Ring |
```

Danach müssen alle Konfliktmarker entfernt sein.

Falsch wäre:

```text
<<<<<<< HEAD
| 1 | 1 Ring |
=======
| 1 | 1 Amulett |
>>>>>>> origin/end
```

Richtig ist ein sauberer Endzustand ohne Konfliktmarker:

```text
| 1 | 1 Amulett und 1 Ring |
```

Danach:

```bash
git add rucksack.md
git commit -m "docs: resolve merge conflict"
```

Prüfen:

```bash
git status
git log --oneline --graph --decorate --all
```

---

## Ergebnis

```text
Gleiche Stelle + gleiche Änderung -> kein Konflikt.
Gleiche Stelle + unterschiedliche Änderung -> Merge-Konflikt.
```

---

# 6. Aufgabe 4: Branch `end` vor dem Merge auf `master` setzen

## 6.1 Aufgabenstellung

Wie Aufgabe 2, aber vor dem Merge soll der Branch `end` auf die Spitze von `master` gesetzt werden.

Dafür wird ein Rebase verwendet.

---

## 6.2 Wichtiges Vorwissen

`origin/end` ist ein Remote-Tracking-Branch.

Das bedeutet:

```text
origin/end
```

ist nur die lokale Referenz auf den entfernten Branch.

Um damit praktisch zu arbeiten, wird daraus zuerst ein lokaler Branch erstellt.

---

## 6.3 Vorgehen

Mit einem frischen Klon starten.

Auf `master` wechseln:

```bash
git checkout master
```

Eine Datei ändern, die auch in `end` verändert wurde, aber an anderer Stelle.

Beispiel:

```text
questlog.md
```

Dann:

```bash
git add questlog.md
git commit -m "docs: change questlog on master"
```

Jetzt wird ein lokaler Branch `end` aus `origin/end` erstellt:

```bash
git checkout -b end origin/end
```

Nun wird der lokale Branch `end` auf die Spitze von `master` gesetzt:

```bash
git rebase master
```

Bedeutung:

```text
git rebase master
```

verschiebt die Commits des aktuellen Branches so, als wären sie direkt nach dem aktuellen Stand von `master` entstanden.

Danach zurück auf `master` wechseln:

```bash
git checkout master
```

Jetzt `end` in `master` mergen:

```bash
git merge end
```

Prüfen:

```bash
git status
git log --oneline --graph --decorate --all
```

---

## 6.4 Beobachtung

Hier entsteht oft kein normaler Merge-Commit.

Stattdessen entsteht häufig ein Fast-Forward.

Typische Ausgabe:

```text
Updating <alter Commit>..<neuer Commit>
Fast-forward
```

---

## 6.5 Erklärung

Ohne Rebase haben `master` und `end` unterschiedliche Entwicklungslinien.

Dann muss Git diese Linien zusammenführen.

Mit Rebase wird `end` vorher so verschoben, dass `end` direkt auf dem aktuellen Stand von `master` aufbaut.

Danach ist `master` ein direkter Vorgänger von `end`.

Git muss dann keinen echten Merge-Commit erzeugen.

Git verschiebt nur den Branch-Zeiger von `master` nach vorne.

Das nennt man Fast-Forward.

---

## 6.6 Ergebnis

```text
Wenn end vorher auf master rebased wird, wird der spätere Merge oft einfacher.
Statt eines Merge-Commits gibt es häufig nur ein Fast-Forward.
```

---

# 7. Merge-Konflikte lösen

Ein Merge-Konflikt entsteht, wenn Git nicht automatisch entscheiden kann, welche Änderung übernommen werden soll.

Typischer Fall:

```text
Dieselbe Datei
dieselbe Stelle
unterschiedliche Änderung
```

---

## 7.1 Schritt 1: Status prüfen

```bash
git status
```

Git zeigt an, welche Datei im Konflikt steht.

---

## 7.2 Schritt 2: Datei öffnen

In der Datei stehen Konfliktmarker:

```text
<<<<<<<
=======
>>>>>>>
```

Diese Marker zeigen die unterschiedlichen Versionen.

---

## 7.3 Schritt 3: Inhalt manuell entscheiden

Man entscheidet, welcher Inhalt am Ende in der Datei stehen soll.

Möglichkeiten:

```text
Nur die eigene Änderung übernehmen.
Nur die Änderung aus dem anderen Branch übernehmen.
Beide Änderungen sinnvoll kombinieren.
Eine neue Lösung schreiben.
```

---

## 7.4 Schritt 4: Konfliktmarker entfernen

Alle Marker müssen entfernt werden.

Diese Zeichen dürfen nach der Konfliktlösung nicht mehr in der Datei stehen:

```text
<<<<<<<
=======
>>>>>>>
```

---

## 7.5 Schritt 5: Datei vormerken

```bash
git add <dateiname>
```

Beispiel:

```bash
git add rucksack.md
```

---

## 7.6 Schritt 6: Merge abschließen

```bash
git commit -m "docs: resolve merge conflict"
```

---

## 7.7 Schritt 7: Ergebnis prüfen

```bash
git status
```

Erwartete Ausgabe:

```text
nothing to commit, working tree clean
```

Dann den Verlauf prüfen:

```bash
git log --oneline --graph --decorate --all
```

---

# 8. Gesamtübersicht Git-Spiel

| Situation | Ergebnis |
|---|---|
| Unterschiedliche Dateien geändert | Merge läuft normalerweise sauber |
| Gleiche Datei, andere Stelle geändert | Merge läuft meistens sauber |
| Gleiche Datei, gleiche Stelle, gleiche Änderung | normalerweise kein Konflikt |
| Gleiche Datei, gleiche Stelle, unterschiedliche Änderung | Merge-Konflikt |
| Branch vorher auf `master` rebased | häufig Fast-Forward |

---

# 9. Teil B: Katzen-Café mit Gradle

## 9.1 Ziel

Im Katzen-Café-Projekt wurde eine einfache Java-Projektstruktur mit Gradle vorbereitet.

Gradle ist ein Build-Tool.

Ein Build-Tool automatisiert typische Projektschritte.

Dazu gehören:

```text
Projekt kompilieren
Tests ausführen
Abhängigkeiten verwalten
Programm starten
Formatierung prüfen
```

JUnit ist ein Test-Framework für Java.

Ein Test-Framework hilft dabei, Programmverhalten automatisch zu prüfen.

Spotless ist ein Formatierungswerkzeug.

Es prüft oder korrigiert, ob der Quellcode einheitlich formatiert ist.

---

## 9.2 Typische Projektstruktur

Eine einfache Gradle-Struktur für Java sieht so aus:

```text
cat-cafe
├── build.gradle
├── settings.gradle
├── gradlew
├── gradlew.bat
└── src
    ├── main
    │   └── java
    │       └── catcafe
    │           └── Main.java
    └── test
        └── java
            └── catcafe
                └── CatCafeTest.java
```

Bedeutung:

| Pfad oder Datei | Bedeutung |
|---|---|
| `src/main/java` | normaler Programmcode |
| `src/test/java` | Testcode |
| `build.gradle` | Gradle-Konfiguration |
| `settings.gradle` | Projektname |
| `gradlew` | Gradle Wrapper für Linux/macOS |
| `gradlew.bat` | Gradle Wrapper für Windows |

---

# 10. `build.gradle`

## 10.1 Vollständige Konfiguration

```gradle
plugins {
    id 'application'
    id 'com.diffplug.spotless' version '8.4.0'
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation 'org.junit.jupiter:junit-jupiter:6.0.3'
    testRuntimeOnly 'org.junit.platform:junit-platform-launcher'
}

application {
    mainClass = 'catcafe.Main'
}

test {
    useJUnitPlatform()
}

spotless {
    java {
        googleJavaFormat().aosp().reflowLongStrings()
        removeUnusedImports()
        trimTrailingWhitespace()
        endWithNewline()
    }
}
```

---

## 10.2 Erklärung der wichtigsten Teile

```gradle
plugins {
    id 'application'
    id 'com.diffplug.spotless' version '8.4.0'
}
```

`application` erlaubt das Starten einer Java-Anwendung über Gradle.

`com.diffplug.spotless` bindet Spotless für die Formatprüfung ein.

---

```gradle
repositories {
    mavenCentral()
}
```

`mavenCentral()` ist eine zentrale Paketquelle für Java-Bibliotheken.

Gradle lädt daraus externe Abhängigkeiten.

---

```gradle
dependencies {
    testImplementation 'org.junit.jupiter:junit-jupiter:6.0.3'
    testRuntimeOnly 'org.junit.platform:junit-platform-launcher'
}
```

`testImplementation` bedeutet:

Diese Bibliothek wird für Tests benötigt.

`junit-jupiter` ist JUnit für moderne JUnit-Tests.

`testRuntimeOnly` bedeutet:

Diese Bibliothek wird nur beim Ausführen der Tests benötigt.

---

```gradle
application {
    mainClass = 'catcafe.Main'
}
```

Hier wird festgelegt, welche Klasse beim Starten des Programms ausgeführt wird.

Die Klasse muss eine passende `main`-Methode enthalten.

---

```gradle
test {
    useJUnitPlatform()
}
```

Damit werden JUnit-Tests über die JUnit Platform ausgeführt.

---

```gradle
spotless {
    java {
        googleJavaFormat().aosp().reflowLongStrings()
        removeUnusedImports()
        trimTrailingWhitespace()
        endWithNewline()
    }
}
```

Hier wird die Java-Formatierung konfiguriert.

| Einstellung | Bedeutung |
|---|---|
| `googleJavaFormat()` | nutzt den Google Java Formatter |
| `.aosp()` | verwendet 4 Leerzeichen Einrückung |
| `reflowLongStrings()` | formatiert lange Strings |
| `removeUnusedImports()` | entfernt unbenutzte Imports |
| `trimTrailingWhitespace()` | entfernt Leerzeichen am Zeilenende |
| `endWithNewline()` | sorgt für eine neue Zeile am Dateiende |

---

# 11. `settings.gradle`

```gradle
rootProject.name = 'cat-cafe'
```

Diese Datei legt den Namen des Gradle-Projekts fest.

---

# 12. Gradle-Befehle

## 12.1 Windows PowerShell

Projekt bauen:

```powershell
.\gradlew.bat assemble
```

JUnit-Tests ausführen:

```powershell
.\gradlew.bat test
```

Formatierung prüfen:

```powershell
.\gradlew.bat spotlessCheck
```

Formatierung automatisch anwenden:

```powershell
.\gradlew.bat spotlessApply
```

Anwendung starten:

```powershell
.\gradlew.bat run
```

---

## 12.2 Linux, macOS oder GitHub Actions

Projekt bauen:

```bash
./gradlew assemble
```

JUnit-Tests ausführen:

```bash
./gradlew test
```

Formatierung prüfen:

```bash
./gradlew spotlessCheck
```

Formatierung automatisch anwenden:

```bash
./gradlew spotlessApply
```

Anwendung starten:

```bash
./gradlew run
```

---

# 13. Teil C: JUnit-Tests

## 13.1 Ziel

JUnit-Tests prüfen automatisch, ob Methoden und Klassen wie erwartet funktionieren.

Dadurch muss man nicht jedes Verhalten manuell testen.

Ein Test sollte möglichst eine konkrete Sache prüfen.

Beispiele:

```text
Anfangszustand prüfen
Katzen hinzufügen
Katzenanzahl prüfen
Rückgabewerte prüfen
Randfälle prüfen
```

---

## 13.2 Typischer Testordner

Tests gehören in:

```text
src/test/java
```

Wenn der Programmcode im Package `catcafe` liegt, sollte der Test ebenfalls in diesem Package liegen:

```text
src/test/java/catcafe/CatCafeTest.java
```

---

## 13.3 Beispielstruktur eines Tests

```java
package catcafe;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class CatCafeTest {

    @Test
    void exampleTest() {
        int expected = 2;
        int actual = 1 + 1;

        assertEquals(expected, actual);
    }
}
```

Erklärung:

| Teil | Bedeutung |
|---|---|
| `@Test` | markiert eine Methode als Test |
| `assertEquals(expected, actual)` | prüft, ob Erwartung und Ergebnis gleich sind |
| `expected` | erwarteter Wert |
| `actual` | tatsächlich berechneter Wert |

---

# 14. Teil D: CI-Pipeline mit GitHub Actions

## 14.1 Ziel

CI bedeutet Continuous Integration.

Auf Deutsch:

```text
kontinuierliche Integration
```

Das bedeutet:

Bei Änderungen am Repository wird automatisch geprüft, ob das Projekt noch funktioniert.

Eine einfache CI-Pipeline prüft hier:

```text
Baut das Projekt?
Laufen die Tests?
Ist die Formatierung korrekt?
```

---

## 14.2 Speicherort der Workflow-Datei

Die Workflow-Datei liegt unter:

```text
.github/workflows/ci.yml
```

Wichtig:

Der Ordner muss exakt so heißen:

```text
.github
```

Darin liegt:

```text
workflows
```

Darin liegt:

```text
ci.yml
```

---

## 14.3 Vollständige `ci.yml`

```yaml
name: CI

on:
    push:
        branches:
            - main
            - master
    pull_request:
        branches:
            - main
            - master

jobs:
    build-test-format:
        runs-on: ubuntu-latest

        steps:
            - name: Repository auschecken
              uses: actions/checkout@v4

            - name: Java einrichten
              uses: actions/setup-java@v4
              with:
                  distribution: temurin
                  java-version: '25'
                  cache: gradle

            - name: Gradle Wrapper ausführbar machen
              run: chmod +x ./gradlew

            - name: Projekt bauen
              run: ./gradlew assemble

            - name: JUnit-Tests ausführen
              run: ./gradlew test

            - name: Formatierung prüfen
              run: ./gradlew spotlessCheck
```

---

## 14.4 Erklärung der CI-Datei

```yaml
name: CI
```

Name der Pipeline.

Dieser Name wird in GitHub Actions angezeigt.

---

```yaml
on:
    push:
        branches:
            - main
            - master
    pull_request:
        branches:
            - main
            - master
```

Die Pipeline läuft bei:

```text
Push auf main
Push auf master
Pull Request nach main
Pull Request nach master
```

Dadurch funktioniert die Pipeline unabhängig davon, ob das Repository `main` oder `master` als Hauptbranch nutzt.

---

```yaml
jobs:
    build-test-format:
        runs-on: ubuntu-latest
```

Es wird ein Job namens `build-test-format` definiert.

Dieser Job läuft auf einer aktuellen Ubuntu-Umgebung.

---

```yaml
- name: Repository auschecken
  uses: actions/checkout@v4
```

Dieser Schritt lädt den Repository-Inhalt in die CI-Umgebung.

Ohne diesen Schritt hätte die Pipeline keinen Zugriff auf den Code.

---

```yaml
- name: Java einrichten
  uses: actions/setup-java@v4
  with:
      distribution: temurin
      java-version: '25'
      cache: gradle
```

Dieser Schritt installiert Java.

`temurin` ist eine OpenJDK-Distribution.

`java-version: '25'` legt Java 25 fest.

`cache: gradle` beschleunigt spätere Builds, weil Gradle-Abhängigkeiten zwischengespeichert werden.

---

```yaml
- name: Gradle Wrapper ausführbar machen
  run: chmod +x ./gradlew
```

Unter Linux muss die Datei `gradlew` ausführbar sein.

Da GitHub Actions hier auf Ubuntu läuft, wird dieser Schritt benötigt.

---

```yaml
- name: Projekt bauen
  run: ./gradlew assemble
```

Dieser Schritt kompiliert das Projekt.

Wenn der Code nicht kompiliert, schlägt die Pipeline fehl.

---

```yaml
- name: JUnit-Tests ausführen
  run: ./gradlew test
```

Dieser Schritt führt die Tests aus.

Wenn ein Test fehlschlägt, schlägt die Pipeline fehl.

---

```yaml
- name: Formatierung prüfen
  run: ./gradlew spotlessCheck
```

Dieser Schritt prüft die Formatierung.

Wenn der Code nicht korrekt formatiert ist, schlägt die Pipeline fehl.

Dann muss lokal ausgeführt werden:

```bash
./gradlew spotlessApply
```

oder auf Windows:

```powershell
.\gradlew.bat spotlessApply
```

Danach werden die Änderungen committed und gepusht.

---

# 15. Befehlsübersicht

## 15.1 Git

| Befehl | Zweck |
|---|---|
| `git status` | aktuellen Arbeitsstand prüfen |
| `git branch -a` | alle lokalen und entfernten Branches anzeigen |
| `git checkout master` | auf `master` wechseln |
| `git diff --name-only master..origin/end` | geänderte Dateien zwischen `master` und `origin/end` anzeigen |
| `git diff master..origin/end -- rucksack.md` | konkrete Unterschiede in `rucksack.md` anzeigen |
| `git add <dateiname>` | Datei für Commit vormerken |
| `git commit -m "Nachricht"` | Commit erstellen |
| `git merge origin/end` | `origin/end` in aktuellen Branch mergen |
| `git checkout -b end origin/end` | lokalen Branch `end` aus `origin/end` erstellen |
| `git rebase master` | aktuellen Branch auf die Spitze von `master` setzen |
| `git log --oneline --graph --decorate --all` | Commit-Historie grafisch anzeigen |

---

## 15.2 Gradle

| Befehl | Zweck |
|---|---|
| `./gradlew assemble` | Projekt bauen |
| `./gradlew test` | JUnit-Tests ausführen |
| `./gradlew spotlessCheck` | Formatierung prüfen |
| `./gradlew spotlessApply` | Formatierung automatisch anwenden |
| `./gradlew run` | Anwendung starten |

---

## 15.3 Gradle auf Windows

| Befehl | Zweck |
|---|---|
| `.\gradlew.bat assemble` | Projekt unter Windows bauen |
| `.\gradlew.bat test` | JUnit-Tests unter Windows ausführen |
| `.\gradlew.bat spotlessCheck` | Formatierung unter Windows prüfen |
| `.\gradlew.bat spotlessApply` | Formatierung unter Windows automatisch anwenden |
| `.\gradlew.bat run` | Anwendung unter Windows starten |

---

# 16. Lernzusammenfassung

Ein Branch ist ein Entwicklungszweig.

Ein Merge führt Änderungen aus einem anderen Branch in den aktuellen Branch ein.

Ein Merge-Konflikt entsteht nicht automatisch, wenn dieselbe Datei geändert wurde.

Entscheidend ist, ob dieselbe Stelle unterschiedlich geändert wurde.

Die wichtigsten Git-Fälle:

| Situation | Ergebnis |
|---|---|
| Unterschiedliche Dateien geändert | Merge läuft normalerweise sauber |
| Gleiche Datei, andere Stelle geändert | Merge läuft meistens sauber |
| Gleiche Datei, gleiche Stelle, gleiche Änderung | normalerweise kein Konflikt |
| Gleiche Datei, gleiche Stelle, unterschiedliche Änderung | Merge-Konflikt |
| Branch vorher auf `master` rebased | häufig Fast-Forward |

Ein Merge-Konflikt wird manuell gelöst.

Dazu werden die Konfliktmarker entfernt und ein sauberer Endzustand geschrieben.

Danach wird die Datei mit `git add` vorgemerkt und der Merge mit einem Commit abgeschlossen.

Gradle automatisiert Build, Tests, Starten und Formatprüfung.

JUnit dient zum automatischen Testen von Java-Code.

Spotless prüft und korrigiert die Formatierung.

GitHub Actions kann diese Prüfungen automatisch bei jedem Push oder Pull Request ausführen.

Dadurch erkennt man früh, ob das Projekt noch baut, ob Tests fehlschlagen oder ob die Formatierung nicht stimmt.

---

# 17. Kurzer Spickzettel

## Merge-Fälle

```text
Andere Datei geändert
-> kein Konflikt

Gleiche Datei, andere Stelle geändert
-> meistens kein Konflikt

Gleiche Datei, gleiche Stelle, gleiche Änderung
-> meistens kein Konflikt

Gleiche Datei, gleiche Stelle, unterschiedliche Änderung
-> Konflikt

Rebase vor Merge
-> oft Fast-Forward
```

---

## Konflikt lösen

```bash
git status
```

Konfliktdatei finden.

```text
<<<<<<<
=======
>>>>>>>
```

Konfliktmarker in Datei suchen.

```bash
git add <dateiname>
```

Gelöste Datei vormerken.

```bash
git commit -m "docs: resolve merge conflict"
```

Merge abschließen.

```bash
git status
```

Sauberen Arbeitsstand prüfen.

---

## CI lokal vorher prüfen

Vor dem Push sinnvoll:

```bash
./gradlew assemble
./gradlew test
./gradlew spotlessCheck
```

Auf Windows:

```powershell
.\gradlew.bat assemble
.\gradlew.bat test
.\gradlew.bat spotlessCheck
```

Wenn Spotless fehlschlägt:

```powershell
.\gradlew.bat spotlessApply
git status
git add .
git commit -m "style: apply spotless formatting"
git push
```