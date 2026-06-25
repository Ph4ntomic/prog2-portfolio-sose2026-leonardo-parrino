<p align="center">
  <img src="./assets/HSBI_Logo_RGB_schwarz.png" alt="Hochschule Bielefeld" width="480">
</p>

<h1 align="center">Programmieren 2 – Lösungen</h1>

<p align="center">
  <img src="https://img.shields.io/badge/Semester-SoSe%202026-005A8B?style=for-the-badge" alt="Sommersemester 2026">
  <img src="https://img.shields.io/badge/Fortschritt-8%20von%208%20erledigt-2E7D32?style=for-the-badge" alt="8 von 8 Blättern erledigt">
</p>

<p align="center">
  <a href="#uebersicht">Übersicht</a> ·
  <a href="#aufbau">Aufbau</a> ·
  <a href="#hinweise">Hinweise</a> ·
  <a href="#post-mortems">Post Mortems</a> ·
  <a href="#modulreflexion">Modulreflexion</a>
</p>

Dieses Repository enthält meine Lösungen und Arbeitsstände zu den Übungsblättern im Modul **Programmieren 2** im Sommersemester 2026.

**Stand: 25. Juni 2026.** Die Blätter B01 bis B08 sind bearbeitet und im jeweiligen Ordner dokumentiert.

---

<a id="uebersicht"></a>
## Übersicht

| Blatt | Thema | Status | Ordner |
|---|---|---|---|
| B01 | Git-Grundlagen und Gradle | erledigt | [`B01`](./B01) |
| B02 | Git-Branches, JUnit und CI | erledigt | [`B02`](./B02) |
| B03 | Lambda-Ausdrücke, Methodenreferenzen und Observer | erledigt | [`B03`](./B03) |
| B04 | Reguläre Ausdrücke, Scanner und Syntaxhighlighting | erledigt | [`B04`](./B04) |
| B05 | ANTLR, Visitor und Mocking | erledigt | [`B05`](./B05) |
| B06 | Visitor, Pattern Matching und AST-Normalisierung | erledigt | [`B06`](./B06) |
| B07 | Generics, sealed Types, Streams und Logging | erledigt | [`B07`](./B07) |
| B08 | Optional, Command-Pattern, Undo/Redo und Result | erledigt | [`B08`](./B08) |

---

<a id="aufbau"></a>
## Aufbau

Jedes Übungsblatt erhält einen eigenen Ordner.

Beispiel:

```text
B08/
├── README.md
├── build.gradle
└── src/
    ├── main/java/
    └── test/java/
```

---

<a id="hinweise"></a>
## Hinweise

Dieses Repository enthält nur relevante Lösungen, Quelltexte, Notizen und Artefakte zu den Übungsblättern.

Private Daten, Zugangsdaten, Tokens und API-Keys werden nicht im Repository gespeichert.

---

<a id="post-mortems"></a>
## Post Mortems

Die Post Mortems werden separat in ILIAS abgegeben. Dort verlinke ich die passenden Artefakte aus diesem Repository.

---

<a id="modulreflexion"></a>
## Modulreflexion

Das Modul hat mir besonders gut gefallen, weil es nah an realistischen Entwicklungsabläufen aufgebaut war. Ein großer Teil der Organisation lief über GitHub. Dabei wurden Branches, Pull Requests, Reviews, Tests und Continuous Integration nicht nur theoretisch behandelt, sondern praktisch eingesetzt. Dadurch habe ich Programmierung nicht nur als Schreiben von Java-Code, sondern als vollständigen Entwicklungsprozess kennengelernt.

Auch die überwiegend remote organisierte Arbeitsweise war für mich sehr angenehm. Inhalte, Aufgabenstellungen und Diskussionen blieben dauerhaft nachvollziehbar. Dadurch konnte ich Anforderungen und Hinweise jederzeit nachlesen und die Zusammenhänge zwischen den einzelnen Themen besser erkennen.

Besonders positiv fand ich die sorgfältige Vorbereitung des Moduls. Die Verbindung aus klarer GitHub-Struktur, Aufgabenblättern, konkreten Projekten und praktischen Workflows machte das Arbeiten transparent und half mir, die Inhalte von Programmieren 2 praxisnah zu verstehen.

Für mein weiteres Studium nehme ich vor allem mit, dass eine saubere Projektstruktur, nachvollziehbare Commits, automatisierte Tests, Reviews und Dokumentation genauso wichtig sind wie die eigentliche Implementierung.
