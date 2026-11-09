# Schéma existant : vérification avant démarrage

Le programme conserve ddl-auto=update. Aucun script de suppression/recréation de base n’est fourni ni exécuté. Les identifiants MySQL existants sont préservés. L’URL existante conserve createDatabaseIfNotExist=true ; sur une installation neuve la base peut être créée, mais aucune base existante n’est supprimée.

## Propriétaires et fetch

| Relation | Propriétaire (clé étrangère) | Inverse |
|---|---|---|
| Agence–Employe | Employe.agence, agence_id obligatoire | Agence.employes |
| Agence–Vehicule | Vehicule.agence, agence_id obligatoire | Agence.vehicules |
| Client–Reservation | Reservation.client, client_id obligatoire | Client.reservations |
| Vehicule–Reservation | Reservation.vehicule, vehicule_id obligatoire | Vehicule.reservations |
| Vehicule–Maintenance | Maintenance.vehicule, vehicule_id obligatoire | Vehicule.maintenances |
| Vehicule–Equipement | Vehicule.equipements, vehicule_equipement(vehicule_id, equipement_id) | Equipement.vehicules |
| Reservation–Contrat | Contrat.reservation, reservation_id obligatoire et unique | Reservation.contrat |
| Contrat–Paiement | Paiement.contrat, contrat_id obligatoire | Contrat.paiements |

Les collections sont initialisées ; OneToMany et ManyToMany sont LAZY par défaut. Les ManyToOne sont explicitement LAZY. Les OneToOne demandent LAZY ; l’inverse Reservation.contrat peut nécessiter une requête de vérification et Hibernate peut charger davantage sans enhancement. Ne pas considérer LAZY comme une promesse d’absence de SQL. Les DTO sont construits dans la transaction et open-in-view est désactivé. Le coût possible en requêtes supplémentaires est acceptable pour ce cours ; aucune optimisation spéculative de graphe n’est ajoutée.

La seule cascade ALL et orphanRemoval=true est Contrat.paiements. Aucun REMOVE/PERSIST vers un parent ou un équipement partagé. Le service maintient les côtés modifiés ; l’aide ajouterPaiement lie toujours les deux côtés de la composition. La suppression d’un contrat s’effectue entité par entité via JPA et supprime ses paiements.

## Impacts de la correction des types

Le tarif passe de VARCHAR à DECIMAL(10,2) et les anciennes dates/timestamps passent à DATE. LocalDate retire la notion d’heure et de fuseau des dates métier. La conversion d’un timestamp contenant une heure perd cette heure ; la conversion d’un tarif invalide ou trop grand peut échouer ou tronquer des données selon le mode SQL.

**ddl-auto=update ne constitue pas une migration de données** et ne supprime pas automatiquement les anciennes colonnes, clés étrangères ou contraintes erronées. L’ancienne autoréférence Equipement peut avoir créé des contraintes sur vehicule_equipement incompatibles avec les véhicules. Ne pas démarrer automatiquement sur un schéma non vérifié : update peut tenter d’altérer les types au démarrage.

Procédure prudente à effectuer par le propriétaire de la base, non exécutée ici :

1. Arrêter l’application et sauvegarder autoloc_db avec les outils MySQL habituels. Valider la restauration sur une copie isolée.
2. Examiner SHOW CREATE TABLE vehicule, client, reservation, maintenance, contrat, paiement, employe, equipement et vehicule_equipement ; examiner INFORMATION_SCHEMA.COLUMNS et KEY_COLUMN_USAGE. Comparer les types, nullabilités et cibles des clés avec le tableau ci-dessus.
3. Sur la copie, inspecter les tarifs non numériques, avec virgule, négatifs, plus de deux décimales ou dépassant 99999999.99. Décider explicitement de leur correction ; ne pas les remplacer arbitrairement par zéro. Examiner les dates avec une heure et faire approuver la perte de précision.
4. Vérifier les agences manquantes dans employe/vehicule, les doublons d’immatriculation/permis/réservation de contrat, les liens orphelins et les équipements dupliqués dans la jointure. Réparer les références à partir de données métier fiables, sans supprimer les lignes automatiquement.
5. Une fois les tarifs assainis et leur conversion vérifiée sur la copie, la modification de type envisagée est ALTER TABLE vehicule MODIFY COLUMN tarif_journalier DECIMAL(10,2) NOT NULL. Vérifier d’abord le nom physique avec SHOW CREATE TABLE. Pour chaque date métier, appliquer sur la copie le même principe avec MODIFY COLUMN <nom_physique_vérifié> DATE NOT NULL, après validation de DATE(ancienne_valeur) et de la conservation du jour attendu.
6. Pour l’ancienne jointure, identifier exactement les contraintes erronées et leurs lignes ; préparer une migration examinée qui remplace uniquement les contraintes incorrectes par des FK vers vehicule(id_vehicule) et equipement(id_equipement). Préserver la table et les données ; les noms et réparations dépendent du schéma constaté. Aucun SQL automatique de retrait de contrainte n’est livré.
7. Faire approuver la migration vérifiée sur la copie, l’appliquer manuellement dans une fenêtre adaptée, puis comparer les données et contraintes. Ne pas changer ddl-auto=update et ne jamais utiliser create/create-drop sur la base réelle.

Ces étapes décrivent une procédure ; aucune connexion SQL, conversion ou migration n’a été effectuée dans cette session.
