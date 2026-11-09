# AutoLoc API — ateliers 1 à 6

Projet Maven existant, package racine tn.esprit.autoloc. Java 17 minimum (Java 21 disponible dans cette session), Spring Boot 4.1.1 conservé, JPA/Hibernate, MySQL, Lombok, Validation, Spring MVC et SpringDoc 3.2.0. Aucun frontend ni système de sécurité ajouté.

## Démarrer sous Windows

1. Installer un JDK 17 ou supérieur et MySQL. Vérifier JAVA_HOME et java -version.
2. Ouvrir **ce dossier** dans IntelliJ ou VS Code, ou importer son pom.xml comme projet Maven. Dans IntelliJ, sélectionner le JDK et activer l’annotation processing Lombok si nécessaire. Le plugin compiler Maven configure déjà Lombok pour compilation et test-compilation.
3. Lire [les précautions de schéma](docs/schema-notes.md) avant le premier démarrage sur une base existante : tarif et dates ont changé de type, et l’ancienne jointure pouvait porter des contraintes erronées. Faire une sauvegarde et vérifier la migration sur une copie. Aucune migration de vos données n’a été exécutée.
4. Préserver/adapter localement les identifiants présents dans src/main/resources/application.properties sans les publier. L’URL cible MySQL localhost:3306/autoloc_db ; le port HTTP reste **8081** et le context-path est **/api**. ddl-auto reste **update**.
5. Depuis la racine du workspace, exécuter dans PowerShell :

```powershell
Set-Location 'C:\Users\HP\Desktop\AutoLoc\autoloc\dautoloc-api emo'
.\mvnw.cmd -B -DskipTests clean compile
.\mvnw.cmd spring-boot:run
```

Dans IntelliJ, lancer alternativement tn.esprit.autoloc.DautolocApiEmoApplication. Ne pas lancer deux instances sur 8081. L’application nécessite une connexion MySQL valide. Le démarrage avec update peut adapter le schéma : effectuer d’abord les vérifications de l’étape 3.

## URLs

- Base API : http://localhost:8081/api
- Exemple : http://localhost:8081/api/agences
- Swagger : http://localhost:8081/api/swagger-ui.html ou http://localhost:8081/api/swagger-ui/index.html
- OpenAPI : http://localhost:8081/api/v3/api-docs

La racine /api n’est pas un endpoint de liste. Utiliser l’un des neuf chemins de ressource.

## Postman

Importer postman/AutoLoc.postman_collection.json et postman/AutoLoc-local.postman_environment.json (Import → fichiers). Sélectionner l’environnement **AutoLoc local 8081**. baseUrl vaut http://localhost:8081/api. Les IDs générés et runToken sont stockés dans les variables de collection ; ne pas créer de variables d’environnement homonymes qui les masqueraient.

Dans Collection Runner, exécuter 01 Préparation → 02 Lectures et modifications → 03 Erreurs attendues → 04 Nettoyage. Chaque requête teste le statut et les invariants utiles. Les premiers POST sont agence, client, équipement, véhicule, employé, réservation, contrat avec deux paiements, puis maintenance. Le dossier 02 prouve que PUT contrat conserve les paiements ; le dossier 04 prouve leur disparition après DELETE contrat.

La préparation génère des valeurs uniques et refuse de recommencer si des IDs de démonstration sont encore présents. Nettoyer avant de rejouer. Le nettoyage utilise uniquement les IDs capturés par cette collection et ignore une requête si son ID manque ; ne pas remplacer ces variables par des IDs existants. En cas d’arrêt partiel, exécuter le nettoyage et consulter les tests en échec avant de relancer. Aucun nettoyage global de base n’est proposé.

## Structure

```text
src/main/java/tn/esprit/autoloc/
  domain/           9 entités et 5 enums
  repository/       9 interfaces JpaRepository
  service/          9 interfaces et leurs implémentations
  exception/        exceptions métier
  mapper/           9 mappers manuels explicites
  web/dto/          requêtes, réponses et erreur structurée
  web/controller/   9 contrôleurs, conseil d’erreurs et OpenApiConfig
docs/               notes de cours et guide de captures
postman/            collection v2.1 et environnement local
```

## Corrections

Tarif String → BigDecimal, dates Date → LocalDate, suppression de l’autoréférence Equipement, jointure unique côté Vehicule, FK d’agence obligatoires et ajout Vehicule.maintenances. Ajout des services/contrôleurs absents, transactions, validation et mapping DTO sans récursion. Paiements créés uniquement avec le contrat et supprimés par composition ; PUT contrat conserve paiements et réservation. Gestion des erreurs françaises avec 400/404/405/409/415/500 et schémas OpenAPI. Les détails figurent dans les trois notes d’ateliers et schema-notes.md.

## Vérifications et limites

Voir [verification-notes.md](docs/verification-notes.md) pour les commandes réellement exécutées et leurs résultats. Les scénarios Postman et les captures sont fournis pour une exécution réelle après démarrage ; ne pas les considérer exécutés par leur seule présence.

Le test de contexte préexistant est désormais opt-in via -Dautoloc.integration=true pour éviter une connexion accidentelle à votre base réelle. Ne pas activer cette option sans configuration d’une base de test isolée. Relecture manuelle, analyse Sonar à réaliser. Les tests ordinaires ne doivent pas toucher MySQL.
