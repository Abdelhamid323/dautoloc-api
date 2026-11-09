# Atelier 3 — Repositories

| Interface | Entité / clé | Usage |
|---|---|---|
| IAgenceRepository | Agence / Long | Persistance JPA et CRUD |
| IEmployeRepository | Employe / Long | Persistance JPA et CRUD |
| IVehiculeRepository | Vehicule / Long | Persistance JPA et CRUD |
| IEquipementRepository | Equipement / Long | Persistance JPA et CRUD |
| IClientRepository | Client / Long | Persistance JPA et CRUD |
| IReservationRepository | Reservation / Long | Persistance JPA et CRUD |
| IContratRepository | Contrat / Long | Persistance JPA et CRUD |
| IPaiementRepository | Paiement / Long | Lecture par le service ; écritures uniquement par cascade du contrat |
| IMaintenanceRepository | Maintenance / Long | Persistance JPA et CRUD |

Les neuf interfaces étendent JpaRepository : listes, CRUD, pagination, tri et flush sont fournis par Spring Data. Spring crée un proxy de repository ; aucune implémentation manuelle ni annotation @Repository n’est nécessaire ici.

- CrudRepository fournit le CRUD et des Iterable.
- ListCrudRepository spécialise les retours en List.
- PagingAndSortingRepository fournit pagination et tri. Depuis Spring Data 3, il n’hérite plus du CRUD : il faut combiner les interfaces si nécessaire.
- JpaRepository combine les interfaces de listes, pagination et opérations spécifiques JPA (flush, saveAndFlush…).

Cinq méthodes dérivées ont été ajoutées : existence de l’immatriculation et du permis pour create, leurs variantes hors ID courant pour update, et existence d’un contrat pour une réservation. Les contraintes SQL restent nécessaires pour les accès concurrents. Les vérifications applicatives donnent un message précis ; une course d’unicité est traduite en 409 par le handler d’intégrité.

Les suppressions utilisent delete sur une entité chargée et flush dans la transaction. Ne pas remplacer par deleteAllInBatch/deleteAllByIdInBatch : le SQL en batch contourne le cycle de vie des entités, les cascades et orphanRemoval et peut désynchroniser le contexte de persistance. Cela casserait notamment Contrat → Paiement.

Défauts réellement constatés et corrigés :

1. Vehicule.tarifJournalier était String : BigDecimal et DECIMAL(10,2).
2. Les dates métier étaient java.util.Date : LocalDate.
3. Equipement possédait une collection d’équipements autoréférencée et redéclarait la jointure : supprimée ; Vehicule est seul propriétaire.
4. Employe.agence et Vehicule.agence acceptaient NULL : associations et colonnes obligatoires.
5. Vehicule.maintenances manquait : relation inverse initialisée ajoutée.

SonarQube for IDE n’est pas accessible dans cette session : **relecture manuelle, analyse Sonar à réaliser**. Aucun résultat Sonar ni numéro de règle n’est revendiqué.
