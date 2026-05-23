# ============================================================
# AUFGABE 4: GIT, PULL REQUESTS UND CI
# ============================================================

## 1. AUFGABENSTELLUNG

Für Aufgabe 4 sollte eine einfache CI-Pipeline eingerichtet werden.

Die Pipeline sollte drei Jobs enthalten:

- `build`: Projekt nur kompilieren,
- `test`: JUnit-Tests ausführen,
- `format`: Spotless im prüfenden Modus ausführen.

Die Pipeline sollte nur bei Pull Requests und bei manueller Auslösung laufen.
Sie sollte nicht automatisch bei Pushs auf `master` oder `main` starten.

Zusätzlich sollten die Implementierungen über Feature-Branches und Pull Requests
entwickelt und gegenseitig reviewed werden.

## 2. UMGESETZTE DATEIEN

Angelegt wurde:

```text
.github/workflows/ci.yml
```

Zusätzlich für PR-Beschreibungen:

```text
PR_NOTES.md
```

## 3. CI-WORKFLOW

Der Workflow heißt:

```yaml
name: CI
```

Trigger:

```yaml
on:
  pull_request:
  workflow_dispatch:
```

Damit läuft die CI:

- bei Pull Requests,
- bei manueller Auslösung,
- nicht automatisch bei normalen Pushs.

Das entspricht der Aufgabenstellung.

## 4. JOB: BUILD

Der Build-Job kompiliert die Klassen:

```yaml
- name: Compile classes
  run: ./gradlew classes
```

Bedeutung:

Das Projekt wird gebaut, aber die Tests werden in diesem Job noch nicht
ausgeführt.

## 5. JOB: TEST

Der Test-Job führt die JUnit-Tests aus:

```yaml
- name: Run tests
  run: ./gradlew test
```

Bedeutung:

Alle Tests unter `src/test/java` werden über Gradle ausgeführt.

## 6. JOB: FORMAT

Der Format-Job prüft Spotless:

```yaml
- name: Check formatting
  run: ./gradlew spotlessCheck
```

Bedeutung:

Der Code wird nicht automatisch verändert. Es wird nur geprüft, ob der Code dem
Format entspricht.

Falls die Formatierung lokal fehlschlägt:

```powershell
.\gradlew.bat spotlessApply
.\gradlew.bat spotlessCheck
```

## 7. JAVA-VERSION

Alle Jobs verwenden Java 25:

```yaml
uses: actions/setup-java@v4
with:
  distribution: temurin
  java-version: "25"
```

Das passt zur vorhandenen Gradle-Konfiguration:

```gradle
java.toolchain.languageVersion = JavaLanguageVersion.of(25)
```

## 8. PR-NOTIZEN

In `PR_NOTES.md` stehen vorbereitete kurze Beschreibungen für:

- MiniJava-Tokens,
- RegexHighlighter,
- ScanningHighlighter,
- CI.

Diese Notizen können direkt als Grundlage für Pull-Request-Summary,
Pull-Request-Description und Review-Fokus genutzt werden.

## 9. WAS LOKAL ERLEDIGT WURDE

Lokal wurden die Änderungen als kleine Commits angelegt:

```text
feat: define MiniJava syntax tokens
fix: tighten character literal token matching
test: cover MiniJava token patterns
feat: implement regex highlighter
test: cover regex highlighter behavior
feat: implement scanning highlighter
test: cover scanning highlighter behavior
ci: add Gradle pull request workflow
docs: add pull request notes
test: add syntax highlighting integration checks
```

Die Commit-Historie ist dadurch gut nachvollziehbar.

## 10. WAS NOCH NICHT LOKAL ERLEDIGBAR IST

Folgende Punkte fehlen noch, weil sie nicht rein lokal abgeschlossen werden
können:

- den privaten GitHub-Fork als `origin` setzen,
- Änderungen in dein eigenes GitHub-Repository pushen,
- echte Feature-Branches auf GitHub anlegen,
- echte Pull Requests öffnen,
- Reviews von Kommiliton:innen einholen,
- selbst Reviews bei anderen abgeben,
- Kommentare auf GitHub beantworten oder schließen,
- Screenshot aus dem GitHub-Actions-Lauf einfügen.

Aktuell zeigt `origin` noch auf das öffentliche Vorgabe-Repository.

Vor dem Push muss deshalb die Remote geändert werden:

```powershell
git remote set-url origin <deine-private-repo-url>
git push -u origin master
```

## 11. VERIFIKATION

Lokal wurden die CI-Ziele mehrfach geprüft:

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

## 12. FAZIT

Die CI ist eingerichtet und lokal gegen dieselben Gradle-Ziele geprüft. Der
technische Teil von Aufgabe 4 ist erledigt.

Offen bleibt nur der organisatorische GitHub-Teil mit echten Pull Requests und
Reviews.

