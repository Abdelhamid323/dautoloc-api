# Vérifications réellement effectuées

Session du 9 novembre 2026. Toutes les commandes Maven ont été exécutées dans le dossier Maven existant `autoloc/dautoloc-api emo`, avec son Maven Wrapper.

| Commande / contrôle | Résultat réel |
|---|---|
| `.\mvnw.cmd -B -DskipTests clean compile` | BUILD SUCCESS ; 91 fichiers Java recompilés après nettoyage de target, javac release 17, avec le JDK 21 installé. La première tentative avait échoué par accès refusé au cache Maven ; la relance autorisée a téléchargé les dépendances puis réussi. |
| `.\mvnw.cmd -B test` | BUILD SUCCESS ; 10 tests exécutés avec succès, 0 échec, 0 erreur ; 1 test de contexte préexistant volontairement ignoré. Après les dernières corrections de sources/imports et d’unicité, la même commande a recompilé les 91 sources et réussi de nouveau avec ces mêmes résultats. |
| `.\mvnw.cmd -B dependency:tree "-Dincludes=org.springdoc:*,org.springframework:spring-webmvc,tools.jackson.core:jackson-databind,com.fasterxml.jackson.core:jackson-databind"` | BUILD SUCCESS. SpringDoc 3.2.0, Spring MVC 7.0.9, Jackson applicatif tools.jackson 3.1.5 ; Jackson com.fasterxml 2.21.5 est également apporté par SpringDoc/swagger-core. Les deux générations utilisent des packages différents ; aucune version Jackson n’a été forcée dans le pom. |
| `node postman/verify-collection.cjs` | Collection et environnement JSON valides ; 72 requêtes, 111 scripts JavaScript syntaxiquement valides et 32 corps JSON après substitution d’exemple. Aucun appel HTTP n’est effectué par ce script. |
| Inspection des sources | Neuf interfaces JpaRepository, neuf couples de services, neuf mappers, neuf contrôleurs ; tarif BigDecimal, dates métier LocalDate, unicités attendues, FK obligatoires, aucune exposition d’entité JPA par les contrôleurs. Une seule déclaration de vehicule_equipement et une seule composition avec cascade ALL/orphanRemoval. |

## Ce que les tests prouvent

ApiHttpTests charge une tranche MVC avec les neuf contrôleurs et des services simulés, **sans repositories ni MySQL**. Huit méthodes de test vérifient les neuf GET de collection (200 et []), POST paiement → 405 avec Allow, contenu non supporté → 415, ID de création refusé, nom vide, ID au PUT refusé, validation du montant imbriqué avec chemin `paiements[0].montant`, date illisible, paramètre de chemin invalide et exception de ressource absente → 404. Ces vérifications exercent réellement les mappings MVC, la conversion Jackson, Jakarta Validation et le conseil d’erreurs.

ContratServiceTests contient deux tests unitaires avec repositories simulés : création de deux paiements avec les deux côtés de la relation maintenus et mise à jour conservant l’ID, la réservation, la même instance de collection et les deux paiements. Les IDs attribués dans ces tests sont des simulations ; ils ne constituent pas un test de génération MySQL ni de cascade SQL.

## Ce qui reste à vérifier sur une installation réelle

- Aucune connexion MySQL ni inspection du schéma réel n’a été effectuée. La disponibilité et les identifiants de votre serveur ne sont donc pas validés.
- Le démarrage complet n’a pas été lancé : `ddl-auto=update` pourrait modifier les anciens types ou tenter de poser des FK sur les données existantes. Lire schema-notes.md, sauvegarder et vérifier sur une copie avant lancement.
- L’initialisation effective des neuf proxies de repositories et les contraintes/cascades SQL doivent être vérifiées lors de ce démarrage sécurisé.
- La collection n’a pas été exécutée contre un serveur HTTP réel. La cascade DELETE contrat → paiements et la persistance entre requêtes restent à contrôler avec son dossier 04.
- Les URLs Swagger et OpenAPI sont configurées et documentées, mais n’ont pas été consultées sur une application démarrée ; aucun résultat d’exécution ou capture Swagger n’est revendiqué.
- Aucun rapport Sonar : **relecture manuelle, analyse Sonar à réaliser**.

## Rejouer sans accéder à MySQL

```powershell
.\mvnw.cmd -B -DskipTests clean compile
.\mvnw.cmd -B test
node postman/verify-collection.cjs
```

Node est nécessaire seulement pour la vérification statique facultative de Postman, pas pour l’application. Ne pas activer `-Dautoloc.integration=true` sans base de test isolée ; cette option réactive l’ancien test de contexte complet.

Après vérification du schéma et de MySQL, démarrer avec `.\mvnw.cmd spring-boot:run`, ouvrir http://localhost:8081/api/swagger-ui.html et importer les deux fichiers Postman. Exécuter les dossiers 01, 02, 03 puis 04 ; le guide rendu-captures.md décrit les preuves à capturer réellement.
