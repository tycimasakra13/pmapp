# APLIKACJA WEBOWA DO ZARZĄDZANIA PROJEKTAMI

## Dane testowego użytkownika
login: user hasło: password
## Dane administratora
login: admin hasło: admin

## Dla bazy danych
Konsola bazy jest dostępna pod ścieżką /h2/ 

url bazy: jdbc:h2:mem:test_db

login: sa hasło: puste

## REST API
API jest wystawione pod adresem /api/projekt /api/student /api/zadanie

### Pliki
Pliki przyporządkowane do każdego projektu są dostępne pod adresem /api/projekt/{projektId}/{nazwaPliku}

Analogicznie dla zadania /api/zadanie/{zadanieId}/{nazwaPliku}

Wrzucanie jednego pliku jest pod adresem /api/projekt/uploadFile/{projektId}

Dla wielu plików jednoczesnie /api/projekt/uploadMultipleFiles/{projektId}

Analogicznie dla zadań

Pliki są zapisywane w folderze /tmp/ (może nie działać na windowsie)