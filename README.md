# ModernLearning – Interaktive Lernplattform (Java & SQLite)

<p align="center">
  <img src="https://github.com/user-attachments/assets/50e6c142-cfe6-4428-b151-03ec1f9228a4" alt="SkillBuildersLogo" width="250"/>
</p>


ModernLearning ist eine Java-basierte Desktop-Anwendung zur Unterstützung von Lernenden durch interaktive Wissensvermittlung. Die Anwendung wurde mit dem Ziel entwickelt, personalisiertes und strukturiertes Lernen digital zu ermöglichen – insbesondere in schulischen oder akademischen Kontexten.

Dieses Projekt wurde unter dem Label **Skill Builders – Quick Thinkers** entwickelt und kombiniert didaktisch orientierte Benutzerführung mit technischer Modularität.

---

## Projektbeschreibung

Die Anwendung bietet grundlegende Funktionen einer digitalen Lernplattform. Lerninhalte werden kategorisiert gespeichert, und Nutzer:innen können sich durch individuell aufgebaute Lernmodule navigieren. Die SQLite-Datenbank sorgt für eine einfache, lokal verwaltete Datenstruktur. ModernLearning wurde als Java-Projekt mit Gradle aufgebaut und eignet sich für den Einsatz auf Windows-Systemen.

---

## Benutzeroberfläche

### Startansicht mit Klassenauswahl

[Video-Demo ansehen](./Projekt-ansicht.mp4)

*Video: kurze Projektdemo*

---

## Hauptfunktionen

- Lokale Benutzeroberfläche für den Zugriff auf Lerninhalte
- SQLite-Datenbank zur Verwaltung von Kursen, Modulen oder Fragen
- Auswahl von Klassenstufen und Arbeitsmaterialien
- Kalenderfunktion zur Wochen- und Monatsübersicht
- Zugriff auf lokale Dokumente und eigene Dateien

---

## Technologie-Stack

| Komponente         | Technologie           |
|--------------------|-----------------------|
| Programmiersprache | Java                  |
| Build Tool         | Gradle                |
| Datenbank          | SQLite (`Modernlearning.sqlite`) |
| GUI                | Swing / JavaFX (je nach Ausführung) |

---
<p align="center">
  <img src="https://github.com/user-attachments/assets/7fce6fcb-e065-4864-af5b-f7c3f182b1eb" alt="Systemarchitektur"/>
</p>
*Abbildung: Systemarchitektur*
## Projektstruktur

```
modernlearning/
├── src/                         → Java-Quellcode
├── Modernlearning.sqlite        → SQLite-Datenbank mit gespeicherten Inhalten
├── build.gradle                 → Build-Konfiguration (Gradle)
├── gradlew / gradlew.bat        → Gradle Wrapper
├── settings.gradle              → Projektdefinition
```

---

## Ausführung

### Voraussetzungen

- Java 17 oder höher
- Gradle (lokal installiert oder via Wrapper)

### Lokaler Start

```bash
git clone https://github.com/yazan1kasem/modernlearning.git
cd modernlearning
./gradlew run
```

---

## Weiterentwicklungsideen

- Integration eines Nutzerprofilsystems
- Unterstützung für Fortschrittsverfolgung & Lernstatistiken
- Import/Export von Lerninhalten (CSV, XML)
- Verbindung mit Online-Datenbanken oder Cloud-Diensten

---

## Lizenz

Dieses Projekt wurde zu Ausbildungszwecken erstellt. Lizenzinformationen sind im Repository nicht explizit enthalten. Eine kommerzielle Nutzung ist daher nur nach Rücksprache mit dem Autor empfohlen.
