# Post Mortem: Blatt 08

## Zusammenfassung

In Blatt 08 habe ich das Zoo-Modell aus Blatt 07 um die Themen `Optional`,
Command-Pattern, Undo/Redo und explizite Fehlerbehandlung erweitert. Fuer die
Suche nach Tieren liefern einzelne Gehege ein `Optional<T>`, waehrend der Zoo
wegen seiner unterschiedlichen Gehegetypen ein `Optional<Animal>` zurueckgibt.
Ausserdem habe ich Commands zum Hinzufuegen und Entfernen von Tieren sowie einen
generischen `CommandManager` mit Undo- und Redo-Historie umgesetzt.

## Details

Die Commands arbeiten mit `Enclosure<? super T>`. Dadurch kann beispielsweise
ein Command fuer einen Loewen sowohl auf einem Loewengehege als auch auf einem
allgemeinen Saeugetiergehege ausgefuehrt werden. Ein Fisch-Command kann dagegen
nicht auf ein Saeugetiergehege angewendet werden. Diese Einschraenkung wird
bereits beim Kompilieren geprueft.

Fehler werden nicht ueber Exceptions oder Wahrheitswerte dargestellt, sondern
mit dem sealed Interface `Result<E, R>`. Die beiden Records `Ok` und `Err`
bilden Erfolg und Fehler eindeutig ab. Der `CommandManager` wertet die
Ergebnisse mit Pattern Matching aus und uebernimmt zentral das Logging.

## Reflexion: schwierigster Teil

Am schwierigsten war die korrekte Verwaltung der beiden Stacks. Ein
erfolgreicher neuer Command muss auf dem Undo-Stack landen und den Redo-Stack
leeren. Bei einem fehlgeschlagenen Command darf sich die Historie dagegen nicht
veraendern. Auch ein fehlgeschlagenes Undo oder Redo muss den entnommenen
Command wieder auf den urspruenglichen Stack legen. Ich habe diese Faelle
einzeln modelliert und mit gezielten Unit-Tests abgesichert.

## Reflexion: gelernt

Ich habe besser verstanden, wie sich PECS praktisch auf ein Entwurfsmuster
anwenden laesst. Ausserdem ist mir klarer geworden, warum ein eigener
Ergebnistyp bei erwartbaren fachlichen Fehlern aussagekraeftiger als Exceptions
oder `boolean` ist. Durch die Trennung bleiben die Commands fuer die fachliche
Zustandsaenderung verantwortlich, waehrend der Manager Historie und Logging
koordiniert.

## Links

- B08:
  <https://github.com/Ph4ntomic/prog2-portfolio-sose2026-leonardo-parrino/tree/main/B08>
- Dokumentation:
  <https://github.com/Ph4ntomic/prog2-portfolio-sose2026-leonardo-parrino/blob/main/B08/README.md>
- Aufgabenstellung:
  <https://github.com/Programmiermethoden-CampusMinden/Prog2-Lecture-S26/blob/master/homework/b08.md>
- CommandManager:
  <https://github.com/Ph4ntomic/prog2-portfolio-sose2026-leonardo-parrino/blob/main/B08/src/main/java/zoo/command/CommandManager.java>
- Result:
  <https://github.com/Ph4ntomic/prog2-portfolio-sose2026-leonardo-parrino/blob/main/B08/src/main/java/zoo/result/Result.java>
- Tests:
  <https://github.com/Ph4ntomic/prog2-portfolio-sose2026-leonardo-parrino/tree/main/B08/src/test/java/zoo>
- Projekt-Setup:
  <https://github.com/Ph4ntomic/prog2-portfolio-sose2026-leonardo-parrino/commit/253ca96>
- Zoo-Modell:
  <https://github.com/Ph4ntomic/prog2-portfolio-sose2026-leonardo-parrino/commit/1a22834>
- Command-Workflow:
  <https://github.com/Ph4ntomic/prog2-portfolio-sose2026-leonardo-parrino/commit/1c5c174>
- Tests:
  <https://github.com/Ph4ntomic/prog2-portfolio-sose2026-leonardo-parrino/commit/f044470>
- Dokumentation:
  <https://github.com/Ph4ntomic/prog2-portfolio-sose2026-leonardo-parrino/commit/d0bec47>
