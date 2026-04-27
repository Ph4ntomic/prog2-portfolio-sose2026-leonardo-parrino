# B01 – Git-Quest Antworten

Repository:

```text
https://github.com/Programmiermethoden-CampusMinden/prog2_ybel_gitquest.git
```

## Verwendete Git-Befehle

```powershell
git log --oneline --reverse --decorate --all
git show <commit-hash>
git log -p --reverse -G "experience" -- stats.md
git log -p --reverse -G "hunger" -- stats.md
git log -p --reverse -- rucksack.md
git diff <alter-hash> <neuer-hash>
```

## Was passierte an `tag 01`?

Befehl:

```powershell
git log --oneline --reverse --decorate --all
git show <Hash-von-tag-01>
```

An `tag 01` beginnt die Geschichte. Man lernt den Helden Markus kennen. Markus betritt mit Schwert und leichter Rüstung den Dungeon, um das Amulett zu finden.

Hinweis: Der Zwerg und der Shop gehören nicht zu `tag 01`, sondern kommen später in der Geschichte.

## Wann hat der Held zum ersten Mal 4 `experience` Punkte?

Befehl:

```powershell
git log -p --reverse -G "experience" -- stats.md
```

Der Held hatte zum ersten Mal an `tag 01.1` genau 4 `experience` Punkte.

## Wann hat der Held zum ersten Mal 10 `hunger` Punkte?

Befehl:

```powershell
git log -p --reverse -G "hunger" -- stats.md
```

Der Held hatte zum ersten Mal an `tag 02` genau 10 `hunger` Punkte.

## Wie viele Heiltränke hat der Held insgesamt in seinem Rucksack gehabt?

Befehle:

```powershell
git log --oneline --reverse --decorate --all
git log -p --reverse -- rucksack.md
```

Im Diff sieht man unter anderem:

```text
Hast Du vielleicht auch einen Heiltrank? Die Ratten haben mir ganz schön zugesetzt.
```

Außerdem wird in `rucksack.md` nur ein Heiltrank eingetragen:

```text
| 0    | 1 Heiltrank |
```

Ergebnis:

Der Held hatte insgesamt **einen Heiltrank** in seinem Rucksack.

## Was hat der Held im Shop gekauft? Und wie viel Gold hat er dafür bezahlt?

Befehle:

```powershell
git log -p --reverse -- rucksack.md
```

`-p` zeigt die konkreten Änderungen in den Commits.

Ergebnis:

Der Held kaufte **ein Brot** und bezahlte dafür **5 Gold**.

## Was passierte zwischen `tag 03` und `tag 04`?

Befehle:

```powershell
git log --oneline --reverse --decorate --all
git diff 2ffebcf 39568d5
```

Verwendete Hashes:

```text
2ffebcf tag 03
39568d5 tag 04
```

Zwischen `tag 03` und `tag 04` wurde Markus gestärkt.

Änderungen:

```text
questlog.md:
Ein neuer Satz wurde ergänzt. Markus zieht erfrischt und gestärkt weiter.

rucksack.md:
10 Gold wurden entfernt.

stats.md:
health: 5 -> 10
hunger: 10 -> 0
experience: bleibt 10
```

Ergebnis:

Markus wurde geheilt, sein Hunger wurde auf `0` gesetzt und er gab `10 Gold` aus.

## Hat der Held etwas gegessen? Falls ja, was und wann?

Befehle:

```powershell
git log -p --reverse -G "hunger" -- stats.md
git show 13834cd
```

Hunger-Verlauf:

```text
tag 01.4: hunger von 0 auf 4
tag 02: hunger von 4 auf 10
tag 03.17: hunger von 10 auf 0
```

Bei `tag 03.17` sinkt `hunger` von `10` auf `0`.

Im Commit `13834cd` steht, dass Markus auf hartem Brot herumkaut. Es handelt sich um **hartes Zwergenbrot**. Zusätzlich trinkt er einen Heiltrank, der seine Wunden heilt.

Ergebnis:

Der Held hat bei `tag 03.17` **hartes Zwergenbrot** gegessen und zusätzlich einen Heiltrank getrunken.