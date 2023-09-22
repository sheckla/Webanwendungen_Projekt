# PCPart API V1.0 (Stand 20.07.2023)
Eine nutzerfreundliche Möglichkeit diverse Computerteile als API bereitzustellen und diese zu Verwalten: <br>
    - Speichern & Anzeigen von gängigen Computerteil-arten mit Detailinformationen. <br>
    - Schreiben & Anzeigen von Kommentare <br>
    - Zusammenstellen von Pc Konfigurationen

## Erste Schritte
Um die PcPart API mit Quarkus zu starten bitte folgende Schritte beachten.

### 1. Klonen in ein lokales Verzeichnis
```shell script
git clone https://gitlab.hs-osnabrueck.de/swa_mi_sose23/projekt18/swa-projekt-welkener-graf
```


### 2. Bereitstellen von Docker Containers
- Docker herunterladen & installieren: https://www.docker.com/products/docker-desktop/<br>
- Für die nächsten Schritte muss Docker auf dem System laufen!
Starten der Docker-container:
```shell script
docker-compose up -d
```

### 3. Kompilierung
```shell script
./mvnw clean compile
```

### 4. Testing
Nach der Kompilierung wird der code getestet.
```shell script
./mvnw test
```


### 5. Ausführung im dev-modus
```shell script
./mvnw quarkus:dev
```
Die Anwendung wird dann unter http://localhost:8080 verfügbar sein

### 6. Anwenden der PCPart API mit Swagger
PcPart zum nutzen mit Swagger verfügbar unter: http://localhost:8080/q/dev-ui/io.quarkus.quarkus-smallrye-openapi/swagger-ui
Swagger stellt grundlegend bereit:
 - zusammengehörige Pfade als 'Resource'
 - Eingabeformen und die Pfade zu testen

## Rollen & Zugänge:
Die PcPartAPI stellt 3 verschiedene Rollen für seine Nutzer bereit: 'admin', 'moderator' und 'user'.

### Admin
Diese Rolle hat die höchsten Rechte und kann somit neue Computerteile einreichen und löschen. <br>
<p>
<ul>
<li>Nutzername: 'admin'</li>
<li>Passwort: 'admin'</li>
</ul>
</p>

### Moderator
Diese Rolle kann Bewertungen und Kommentare entfernen.<br>
<p>
<ul>
<li>Nutzername: 'moderator'</li>
<li>Passwort: 'moderator'</li>
</ul>
</p>

### User
Diese Rolle kann eigene PC-Konfigurationen erstellen und auch andere Bewerten & Kommentieren.
<p>
<ul>
<li>Nutzername: 'user'</li>
<li>Passwort: 'user'</li>
</ul>
</p>
