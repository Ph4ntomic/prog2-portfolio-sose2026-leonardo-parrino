# ============================================================
# AUFGABE 2: CYCLE CHRONICLES, AEQUIVALENZKLASSEN UND MOCKING
# ============================================================

## 1. Aufgabenstellung

In Aufgabe 2 wurde mit dem Repository `prog2_ybel_cyclechronicles` gearbeitet.

Untersucht werden sollte die Methode:

```java
public boolean accept(Order o)
```

Diese Methode entscheidet, ob ein neuer Reparaturauftrag angenommen wird.

Ein Auftrag darf nur angenommen werden, wenn:

- es kein E-Bike ist,
- es kein Gravel-Bike ist,
- derselbe Kunde keinen anderen offenen Auftrag hat,
- hoechstens vier andere offene Auftraege vorhanden sind.

Danach sollten Aequivalenzklassen, Grenzwerte und konkrete Testfaelle erstellt
und mit JUnit und Mockito umgesetzt werden.

## 2. Umgesetzte Dateien

Geaendert wurde:

```text
src/main/java/cyclechronicles/Shop.java
```

Angelegt wurden:

```text
src/test/java/cyclechronicles/ShopAcceptTest.java
src/test/java/cyclechronicles/ShopWorkflowBonusTest.java
documentation/AUFGABE_05_ACCEPT_ANALYSIS.md
```

## 3. Ausgangscode

Die Methode `accept` war bereits implementiert:

```java
public boolean accept(Order o) {
    if (o.getBicycleType() == Type.GRAVEL) return false;
    if (o.getBicycleType() == Type.EBIKE) return false;
    if (pendingOrders.stream().anyMatch(x -> x.getCustomer().equals(o.getCustomer()))) return false;
    if (pendingOrders.size() > 4) return false;

    return pendingOrders.add(o);
}
```

Die Klasse `Order` war dagegen absichtlich unvollstaendig:

```java
public Type getBicycleType() {
    throw new UnsupportedOperationException();
}

public String getCustomer() {
    throw new UnsupportedOperationException();
}
```

Deshalb sind echte `Order`-Objekte fuer die Tests nicht sinnvoll verwendbar.

## 4. Aequivalenzklassen

| Merkmal | Gueltige Klasse | Ungueltige Klasse |
|---|---|---|
| Fahrradtyp | `RACE`, `SINGLE_SPEED`, `FIXIE` | `GRAVEL`, `EBIKE` |
| Kunde | noch kein offener Auftrag dieses Kunden | Kunde hat bereits offenen Auftrag |
| offene Auftraege | 0 bis 4 vorhandene Auftraege | 5 oder mehr vorhandene Auftraege |
| Rueckgabe | `true` und Auftrag wird eingereiht | `false` und Auftrag wird abgelehnt |

## 5. Grenzwertanalyse

Der wichtigste Zahlen-Grenzwert liegt bei der Queue-Groesse.

| Vorhandene offene Auftraege | Erwartung |
|---|---|
| 0 | Annahme moeglich |
| 4 | Annahme noch moeglich |
| 5 | Annahme nicht mehr moeglich |

Beim Kunden gibt es keinen Zahlenwert, aber einen logischen Grenzwechsel:

```text
kein gleicher Kunde offen -> Annahme moeglich
gleicher Kunde offen -> Annahme nicht moeglich
```

## 6. Mockito

Mockito wird verwendet, um `Order`-Objekte zu simulieren.

Wichtig:

```text
Shop wird nicht gemockt.
```

Die Tests sollen die echte Implementierung von `Shop#accept` pruefen.

Gemockt wird nur die unvollstaendige Abhaengigkeit `Order`:

```java
static Order order(Type type, String customer) {
    var order = mock(Order.class);
    when(order.getBicycleType()).thenReturn(type);
    when(order.getCustomer()).thenReturn(customer);
    return order;
}
```

Dadurch kann jeder Test genau festlegen:

```text
Welchen Fahrradtyp hat der Auftrag?
Zu welchem Kunden gehoert der Auftrag?
```

## 7. Tests fuer Shop#accept

Die Testklasse `ShopAcceptTest` enthaelt 9 Tests.

Geprueft wird:

- erlaubte Fahrradtypen bei leerer Queue,
- Ablehnung von `GRAVEL`,
- Ablehnung von `EBIKE`,
- Ablehnung eines zweiten Auftrags desselben Kunden,
- Annahme verschiedener Kunden bis zur Grenze von 5 offenen Auftraegen,
- Ablehnung des sechsten offenen Auftrags,
- dass ein abgelehnter E-Bike-Auftrag keinen Queue-Platz belegt.

Beispiel fuer den Kapazitaetsgrenzwert:

```java
for (int i = 1; i <= 5; i++) {
    assertTrue(shop.accept(order(Type.RACE, "customer-" + i)));
}

assertFalse(shop.accept(order(Type.FIXIE, "customer-6")));
```

## 8. Bonus: repair und deliver

Die Methoden `repair` und `deliver` waren in der Vorlage noch nicht
implementiert.

Sie wurden entsprechend der JavaDocs umgesetzt.

`repair`:

```java
public Optional<Order> repair() {
    var order = pendingOrders.poll();
    if (order == null) {
        return Optional.empty();
    }

    completedOrders.add(order);
    return Optional.of(order);
}
```

Bedeutung:

```text
Den aeltesten offenen Auftrag aus der Queue nehmen.
Als abgeschlossen speichern.
Optional mit diesem Auftrag zurueckgeben.
```

`deliver`:

```java
public Optional<Order> deliver(String c) {
    var order = completedOrders.stream()
        .filter(x -> Objects.equals(x.getCustomer(), c))
        .findAny();
    order.ifPresent(completedOrders::remove);
    return order;
}
```

Bedeutung:

```text
Einen abgeschlossenen Auftrag fuer den Kunden suchen.
Falls gefunden: aus completedOrders entfernen und zurueckgeben.
Falls nicht gefunden: Optional.empty().
```

## 9. Bonus-Tests

Die Testklasse `ShopWorkflowBonusTest` enthaelt 5 Tests.

Geprueft wird:

- `repair` liefert bei leerer Queue `Optional.empty()`,
- `repair` arbeitet nach FIFO-Reihenfolge,
- eine Reparatur gibt wieder Kapazitaet frei,
- `deliver` liefert einen passenden abgeschlossenen Auftrag,
- `deliver` entfernt ausgelieferte Auftraege,
- falsche Kunden entfernen keinen Auftrag.

## 10. Testuebersicht

| Testklasse | Tests | Zweck |
|---|---:|---|
| `ShopAcceptTest` | 9 | Pflichtteil fuer `accept` |
| `ShopWorkflowBonusTest` | 5 | Bonus fuer `repair` und `deliver` |

Insgesamt:

```text
14 Tests
0 Failures
0 Skipped
```

## 11. Verifikation

Ausgefuehrt wurde im Cycle-Chronicles-Repository:

```powershell
.\gradlew.bat clean test spotlessCheck
```

Ergebnis:

```text
BUILD SUCCESSFUL
14 Tests
0 Failures
0 Skipped
```

## 12. Repository-Hinweis

Lokal liegt das Repository unter:

```text
C:\Users\leona\Documents\GitHub\prog2_ybel_cyclechronicles
```

Die Remotes wurden so vorbereitet:

```text
origin   -> https://github.com/Ph4ntomic/prog2_ybel_cyclechronicles.git
upstream -> https://github.com/Programmiermethoden-CampusMinden/prog2_ybel_cyclechronicles.git
```

GitHub-Stand:

```text
master: d682d54 test: cover shop workflow with mock orders
```

## 13. Fazit

Die Methode `Shop#accept` ist durch Aequivalenzklassen und Grenzwerte
abgedeckt. Mockito wird gezielt nur fuer `Order` genutzt, weil diese Klasse in
der Vorlage noch nicht fertig implementiert ist. Der Bonus fuer `repair` und
`deliver` ist ebenfalls umgesetzt und getestet.
