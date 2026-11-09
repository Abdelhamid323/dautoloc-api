# Ateliers 5 et 6 — API REST et DTO

Base : http://localhost:8081/api. Les chemins du tableau sont relatifs à cette base. /api est configuré une seule fois dans server.servlet.context-path.

| Endpoint | Service appelé | Succès | Erreurs documentées |
|---|---|---|---|
| GET /agences | IAgenceService.findAll | 200 (liste, éventuellement []) | 500 |
| GET /agences/{id} | IAgenceService.findById | 200 | 400, 404, 500 |
| POST /agences | IAgenceService.create | 201 + Location + DTO | 400, 404 (référence), 409, 415, 500 |
| PUT /agences/{id} | IAgenceService.update | 200 | 400, 404, 409, 415, 500 |
| DELETE /agences/{id} | IAgenceService.deleteById | 204, corps vide | 400, 404, 409, 500 |
| GET /employes | IEmployeService.findAll | 200 (liste, éventuellement []) | 500 |
| GET /employes/{id} | IEmployeService.findById | 200 | 400, 404, 500 |
| POST /employes | IEmployeService.create | 201 + Location + DTO | 400, 404 (référence), 409, 415, 500 |
| PUT /employes/{id} | IEmployeService.update | 200 | 400, 404, 409, 415, 500 |
| DELETE /employes/{id} | IEmployeService.deleteById | 204, corps vide | 400, 404, 409, 500 |
| GET /vehicules | IVehiculeService.findAll | 200 (liste, éventuellement []) | 500 |
| GET /vehicules/{id} | IVehiculeService.findById | 200 | 400, 404, 500 |
| POST /vehicules | IVehiculeService.create | 201 + Location + DTO | 400, 404 (référence), 409, 415, 500 |
| PUT /vehicules/{id} | IVehiculeService.update | 200 | 400, 404, 409, 415, 500 |
| DELETE /vehicules/{id} | IVehiculeService.deleteById | 204, corps vide | 400, 404, 409, 500 |
| GET /equipements | IEquipementService.findAll | 200 (liste, éventuellement []) | 500 |
| GET /equipements/{id} | IEquipementService.findById | 200 | 400, 404, 500 |
| POST /equipements | IEquipementService.create | 201 + Location + DTO | 400, 404 (référence), 409, 415, 500 |
| PUT /equipements/{id} | IEquipementService.update | 200 | 400, 404, 409, 415, 500 |
| DELETE /equipements/{id} | IEquipementService.deleteById | 204, corps vide | 400, 404, 409, 500 |
| GET /clients | IClientService.findAll | 200 (liste, éventuellement []) | 500 |
| GET /clients/{id} | IClientService.findById | 200 | 400, 404, 500 |
| POST /clients | IClientService.create | 201 + Location + DTO | 400, 404 (référence), 409, 415, 500 |
| PUT /clients/{id} | IClientService.update | 200 | 400, 404, 409, 415, 500 |
| DELETE /clients/{id} | IClientService.deleteById | 204, corps vide | 400, 404, 409, 500 |
| GET /reservations | IReservationService.findAll | 200 (liste, éventuellement []) | 500 |
| GET /reservations/{id} | IReservationService.findById | 200 | 400, 404, 500 |
| POST /reservations | IReservationService.create | 201 + Location + DTO | 400, 404 (référence), 409, 415, 500 |
| PUT /reservations/{id} | IReservationService.update | 200 | 400, 404, 409, 415, 500 |
| DELETE /reservations/{id} | IReservationService.deleteById | 204, corps vide | 400, 404, 409, 500 |
| GET /contrats | IContratService.findAll | 200 (liste, éventuellement []) | 500 |
| GET /contrats/{id} | IContratService.findById | 200 | 400, 404, 500 |
| POST /contrats | IContratService.create | 201 + Location + DTO | 400, 404 (référence), 409, 415, 500 |
| PUT /contrats/{id} | IContratService.update | 200 | 400, 404, 409, 415, 500 |
| DELETE /contrats/{id} | IContratService.deleteById | 204, corps vide | 400, 404, 409, 500 |
| GET /paiements | IPaiementService.findAll | 200 (liste, éventuellement []) | 500 |
| GET /paiements/{id} | IPaiementService.findById | 200 | 400, 404, 500 |
| GET /maintenances | IMaintenanceService.findAll | 200 (liste, éventuellement []) | 500 |
| GET /maintenances/{id} | IMaintenanceService.findById | 200 | 400, 404, 500 |
| POST /maintenances | IMaintenanceService.create | 201 + Location + DTO | 400, 404 (référence), 409, 415, 500 |
| PUT /maintenances/{id} | IMaintenanceService.update | 200 | 400, 404, 409, 415, 500 |
| DELETE /maintenances/{id} | IMaintenanceService.deleteById | 204, corps vide | 400, 404, 409, 500 |

POST, PUT et DELETE /paiements ne sont pas déclarés : **405**. Seul le POST /contrats peut créer des paiements imbriqués. OPTIONS et HEAD restent gérés normalement par Spring MVC.

## Circulation d’une requête

Le DispatcherServlet reçoit la requête ; HandlerMapping trouve la méthode du contrôleur selon le chemin et le verbe HTTP. Spring convertit le JSON et applique @Valid. Le contrôleur délègue à l’interface de service ; le service porte transactions et règles métier, les repositories accèdent à JPA, et les mappers construisent les réponses. Le convertisseur HTTP sérialise le DTO retourné.

200 : lecture/mise à jour réussie ; 201 : création avec ID généré et Location ; 204 : suppression sans corps ; 400 : entrée invalide ; 404 : ressource ou référence absente ; 405 : verbe non autorisé ; 409 : unicité ou dépendance empêchant l’opération ; 415 : média non pris en charge ; 500 : anomalie interne. Le handler central conserve les statuts et en-têtes HTTP standards (notamment Allow sur 405), donne des messages français et journalise les anomalies internes sans exposer les détails SQL au client.

## DTO et JSON

Les corps utilisent les champs des classes *CreateRequest et *UpdateRequest. Les relations sont des IDs positifs (agenceId, clientId, vehiculeId, reservationId, equipementIds). Les réponses contiennent idAgence, idVehicule, etc. Les listes ne sont jamais nulles en réponse de collection. Les dates sont AAAA-MM-JJ ; les montants sont des nombres décimaux limités à DECIMAL(10,2). Les longueurs DTO respectent les colonnes : par exemple agence.telephone 8 caractères maximum, sans règle nationale ou contrainte de longueur minimale.

ContratCreateRequest accepte paiements omis, null ou une liste. Les éléments doivent être non null et valides. ContratUpdateRequest contient seulement dateSignature, montantTotal, valide ; il ne permet pas de réaffecter la réservation ni de réécrire les paiements. Boolean + @NotNull distingue false d’une valeur absente, puis le mapper utilise le boolean de l’entité.

Les champs inconnus sont refusés. Un ID au POST est accepté comme champ technique mais interdit par @Null, donc 400. Un ID ou des paiements au PUT contrat sont des champs inconnus, donc 400 plutôt qu’une modification silencieuse.

ContratResponse inclut ses PaiementResponse ; chacun expose seulement contratId. Aucun DTO ne contient d’entité JPA. Aucun graphe récursif, proxy Hibernate, handler ou hibernateLazyInitializer n’est exposé.

À l’étape pédagogique d’exposition directe des entités, @JsonManagedReference/@JsonBackReference permettent de définir un sens de sérialisation d’une relation bidirectionnelle, et @JsonIgnore d’exclure un attribut. Elles ne règlent pas à elles seules le couplage du contrat API au schéma JPA, le chargement paresseux ou la modification involontaire d’un graphe. La version finale utilise les DTO et n’ajoute pas ces annotations aux entités.

## Swagger et versions

Spring Boot 4.1.1 est conservé. SpringDoc 3.2.0 appartient à la famille construite pour Boot 4.1.x (3.1.x–3.2.x), selon la [matrice officielle SpringDoc](https://springdoc.org/#what-is-the-compatibility-matrix-of-springdoc-openapi-with-spring-boot). Pas de dépendance Jackson ajoutée ou forcée : les versions transitives restent gérées par Boot et SpringDoc, compatibles avec leur prise en charge de Jackson 3. Aucun import d’annotation Jackson 2 n’est introduit dans les entités.

- Swagger : http://localhost:8081/api/swagger-ui.html (redirection possible vers /api/swagger-ui/index.html).
- OpenAPI JSON : http://localhost:8081/api/v3/api-docs.
- @Tag regroupe les ressources ; @Operation décrit chaque méthode ; les réponses indiquent les vrais DTO ou ApiError et les codes d’erreur ; @Parameter décrit les IDs de chemin.

Swagger permet d’explorer le contrat et d’exécuter une requête à la fois. Postman importe une collection, conserve les IDs et exécute une suite ordonnée avec assertions et nettoyage. Il faut une application et une base accessibles pour exécuter l’un ou l’autre.

Corrections concrètes : création des neuf contrôleurs absents, utilisation exclusive des interfaces de service, ajout de DTO non récursifs, validation imbriquée, statuts REST, Location et gestion centralisée sans convertir 405/415 en 500.
