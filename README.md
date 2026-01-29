# 🛠️ Baustoff-Kalkulator (PoC)

Ein **Proof of Concept (PoC)** für eine interne Vertriebsanwendung im Baustoffhandel.  
Entwickelt als Portfolio-Projekt zur Demonstration von **Fullstack-Java-Entwicklung** mit Vaadin Flow und Spring Boot.

## 🎯 Projektziel
Ziel war die Entwicklung einer robusten Single-Page-Application (SPA), die Vertriebsmitarbeiter bei der schnellen Mengen- und Preiskalkulation unterstützt. Der Fokus lag auf **Typsicherheit**, **genauer kaufmännischer Berechnung** und einer sauberen **Enterprise-Architektur**.

## 🚀 Technologie-Stack

Dieses Projekt nutzt exakt den Technologie-Stack, der für moderne Enterprise-Lösungen (wie bei der InfoKom GmbH) relevant ist:

* **Frontend:** [Vaadin Flow](https://vaadin.com/flow) (100% Java, kein HTML/JS notwendig)
* **Backend:** Java 21 (LTS), Spring Boot 4.x
* **Build Tool:** Maven
* **Architektur:** Schichtenarchitektur (UI ↔ Service ↔ Data)

## 💡 Technische Highlights & Entscheidungen

### 1. Kaufmännische Präzision
Anstatt mit `double` zu rechnen (Gefahr von Floating-Point-Fehlern), nutzt dieses Projekt konsequent **`BigDecimal`** für alle Währungsberechnungen.
* *Code:* `backend/service/CalculatorService.java`
* *Feature:* Automatisches Runden (`RoundingMode.HALF_UP`) auf 2 Nachkommastellen.

### 2. Domain-Driven Design Ansätze
Die Logik ist strikt von der Oberfläche getrennt.
* **Logik:** Automatische Berechnung von Verschnitt (10%) und Aufrundung auf volle Gebindeeinheiten (Pakete), da Baustoffe nicht stückweise verkauft werden.
* **Validierung:** Eingabeprüfung (keine negativen Flächen, Pflichtfelder) direkt im Java-Code.

### 3. IPv6 Readiness / Modern Infrastructure
Die Anwendung ist "Dual Stack" konfiguriert und lauscht nativ auf IPv6-Adressen. Dies ist essenziell für zukunftssichere Cloud-Umgebungen (z.B. Kubernetes Cluster).
* *Config:* `server.address=::` in `application.properties`.

### 4. Usability (UX)
* **Reset-Flow:** Ein dedizierter Button leert das Formular und setzt den Fokus (Cursor) automatisch zurück in das erste Eingabefeld, um Mauswege zu sparen.
* **Feedback:** Visuelles Feedback über `Notification`-Toasts bei Fehlern oder Erfolg.

## 📂 Projektstruktur

```text
src/main/java/de/portfolio/
├── Application.java           # Entry Point
├── backend/
│   ├── data/                  # POJOs (Material.java)
│   └── service/               # Geschäftslogik (CalculatorService.java)
└── ui/
    └── MainView.java          # Vaadin UI Komponenten & Event Handling

🛠️ Installation & Start
Voraussetzung: Java 17 oder 21 JDK installiert.

Repository klonen:

Bash
git clone [https://github.com/DEIN-USER/baustoff-kalkulator.git](https://github.com/DEIN-USER/baustoff-kalkulator.git)
cd baustoff-kalkulator
Starten (Windows):

PowerShell
.\mvnw spring-boot:run
Starten (Mac/Linux):

Bash
./mvnw spring-boot:run
Aufrufen:

IPv4: http://localhost:8080

IPv6: http://[::1]:8080

Autor: Michael Radawicz