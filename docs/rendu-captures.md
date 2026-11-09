# Guide des captures réelles à produire

Aucune capture d’exécution n’a été fabriquée. Démarrer d’abord l’application après la vérification du schéma décrite dans schema-notes.md. Importer la collection et l’environnement dans Postman ; sélectionner AutoLoc local 8081. Exécuter les dossiers dans l’ordre 01 → 02 → 03 → 04 et conserver la vue des assertions. Les exemples ci-dessous remplacent les IDs par des variables capturées par la collection.

## 1. Création Agence

POST http://localhost:8081/api/agences

```json
{
  "nom": "Agence {{runToken}}",
  "ville": "Tunis",
  "adresse": "10 rue des Jasmins",
  "telephone": "71000000"
}
```

Attendu : 201, en-tête Location, idAgence généré. Capturer la requête, la réponse et les tests. Point démontré : validation du DTO, références existantes et création persistante.

## 2. Création Client

POST http://localhost:8081/api/clients

```json
{
  "nom": "Ben Ali",
  "prenom": "Amine",
  "email": "demo.{{runToken}}@example.com",
  "telephone": "+21671000000",
  "numPermis": "P-{{runToken}}",
  "dateInscription": "2026-11-01"
}
```

Attendu : 201, en-tête Location, idClient généré. Capturer la requête, la réponse et les tests. Point démontré : validation du DTO, références existantes et création persistante.

## 3. Création Equipement

POST http://localhost:8081/api/equipements

```json
{
  "libelle": "GPS {{runToken}}"
}
```

Attendu : 201, en-tête Location, idEquipement généré. Capturer la requête, la réponse et les tests. Point démontré : validation du DTO, références existantes et création persistante.

## 4. Création Vehicule

POST http://localhost:8081/api/vehicules

```json
{
  "immatriculation": "V-{{runToken}}",
  "marque": "Renault",
  "modele": "Clio",
  "categorie": "CITADINE",
  "tarifJournalier": 80.5,
  "statut": "DISPONIBLE",
  "agenceId": {{agenceId}},
  "equipementIds": [
    {{equipementId}}
  ]
}
```

Attendu : 201, en-tête Location, idVehicule généré. Capturer la requête, la réponse et les tests. Point démontré : validation du DTO, références existantes et création persistante.

## 5. Création Employe

POST http://localhost:8081/api/employes

```json
{
  "nom": "Trabelsi",
  "prenom": "Sana",
  "role": "AGENT",
  "agenceId": {{agenceId}}
}
```

Attendu : 201, en-tête Location, idEmploye généré. Capturer la requête, la réponse et les tests. Point démontré : validation du DTO, références existantes et création persistante.

## 6. Création Reservation

POST http://localhost:8081/api/reservations

```json
{
  "dateDebut": "2026-11-10",
  "dateFin": "2026-11-12",
  "statut": "CONFIRMEE",
  "clientId": {{clientId}},
  "vehiculeId": {{vehiculeId}}
}
```

Attendu : 201, en-tête Location, idReservation généré. Capturer la requête, la réponse et les tests. Point démontré : validation du DTO, références existantes et création persistante.

## 7. Création Contrat

POST http://localhost:8081/api/contrats

```json
{
  "dateSignature": "2026-11-09",
  "montantTotal": 241.5,
  "valide": true,
  "reservationId": {{reservationId}},
  "paiements": [
    {
      "montant": 100,
      "datePaiement": "2026-11-09",
      "modePaiement": "CARTE"
    },
    {
      "montant": 141.5,
      "datePaiement": "2026-11-10",
      "modePaiement": "VIREMENT"
    }
  ]
}
```

Attendu : 201, en-tête Location, idContrat généré, deux paiements avec contratId identique et IDs distincts. Capturer la requête, la réponse et les tests. Point démontré : validation du DTO, références existantes et création persistante.

## 8. Création Maintenance

POST http://localhost:8081/api/maintenances

```json
{
  "dateDebut": "2026-11-15",
  "dateFin": "2026-11-16",
  "description": "Contrôle de démonstration",
  "vehiculeId": {{vehiculeId}}
}
```

Attendu : 201, en-tête Location, idMaintenance généré. Capturer la requête, la réponse et les tests. Point démontré : validation du DTO, références existantes et création persistante.

## 9. Lectures et CRUD des huit ressources

Dans le dossier 02, capturer GET collection (200, tableau JSON), GET /{id} (200, ID attendu), puis PUT /{id} (200, même ID et valeur modifiée) pour chaque ressource. Les corps exacts se trouvent dans la collection. Par exemple PUT /api/vehicules/{{vehiculeId}} conserve les références et fixe tarifJournalier à 85.75. Les DELETE sont regroupés dans le nettoyage pour respecter les clés étrangères.

## 10. PUT contrat sans paiements

PUT http://localhost:8081/api/contrats/{{contratId}}

```json
{"dateSignature":"2026-11-09","montantTotal":250,"valide":false}
```

Attendu : 200, même idContrat, même reservationId, exactement les deux IDs de paiement précédents. Capturer les assertions de conservation des paiements.

## 11. Paiements accessibles uniquement en lecture

GET /api/paiements et GET /api/paiements/{{paiement1Id}} : 200. Puis POST /api/paiements avec {"montant":10,"datePaiement":"2026-11-09","modePaiement":"CARTE"} : 405 et en-tête Allow comprenant GET. Capturer les trois résultats.

## 12. Erreurs de validation et références absentes

Exécuter le dossier 03 et capturer chaque cas :

| Requête / variation du corps de préparation | Attendu | Point démontré |
|---|---|---|
| POST agences, nom="" | 400, erreurs.nom | @NotBlank |
| POST clients, email="invalide" | 400 | @Email |
| POST vehicules, tarifJournalier=-1 | 400 | @DecimalMin |
| POST reservations, dateDebut=2026-11-12, dateFin=2026-11-10 | 400 | Règle d’intervalle |
| POST maintenances, dateDebut=2026-11-16, dateFin=2026-11-15 | 400 | Règle d’intervalle |
| POST agences, idAgence=123 | 400 | Refus d’ID au POST |
| POST contrats, paiements[0].montant=-1 | 400, erreurs.paiements[0].montant | @Valid imbriqué |
| POST employes, role="DIRECTEUR" | 400 | Enum invalide |
| POST reservations, dateDebut="pas-une-date" | 400 | Date illisible |
| PUT agences/{id}, idAgence dans le JSON | 400 | ID du chemin protégé |
| GET agences/abc | 400 | Type du paramètre |
| GET agences/9223372036854775807 | 404 | Absence de ressource |
| POST employes, agenceId=9223372036854775807 | 404 | Absence de référence |
| POST vehicules avec immatriculation créée | 409 | Unicité |
| POST clients avec permis créé | 409 | Unicité |
| POST contrats avec réservation déjà utilisée | 409 | OneToOne unique |
| DELETE agences/{{agenceId}} avant enfants | 409 | Protection des FK |
| POST agences avec Content-Type: text/plain | 415 | Statut HTTP conservé |

## 13. Nettoyage et cascade réelle

Exécuter le dossier 04. Ordre : maintenance → contrat → réservation → employé → véhicule → équipement → client → agence. Pour chaque suppression, capturer 204 sans corps, puis le GET confirmant 404. Après DELETE contrat, les deux GET /api/paiements/{idCapturé} doivent retourner 404 : preuve de cascade ALL, pas une simple liste masquée. Ne pas saisir d’ID préexistant manuellement dans les variables de démonstration.

## 14. Documentation OpenAPI

Ouvrir http://localhost:8081/api/swagger-ui.html ; capturer le titre AutoLoc API, les neuf tags, les DTO et les seuls GET de Paiement. Ouvrir http://localhost:8081/api/v3/api-docs et capturer la version 1.0.0 et les chemins sans double /api. Ne publier ces captures qu’après exécution réelle.
