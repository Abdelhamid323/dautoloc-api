# Atelier 4 — Services

| Interface / implémentation | Dépendances injectées |
|---|---|
| IAgenceService / AgenceServiceImpl | IAgenceRepository, AgenceMapper |
| IEmployeService / EmployeServiceImpl | IEmployeRepository, IAgenceRepository, EmployeMapper |
| IVehiculeService / VehiculeServiceImpl | IVehiculeRepository, IAgenceRepository, IEquipementRepository, VehiculeMapper |
| IEquipementService / EquipementServiceImpl | IEquipementRepository, EquipementMapper |
| IClientService / ClientServiceImpl | IClientRepository, ClientMapper |
| IReservationService / ReservationServiceImpl | IReservationRepository, IClientRepository, IVehiculeRepository, ReservationMapper |
| IContratService / ContratServiceImpl | IContratRepository, IReservationRepository, ContratMapper, PaiementMapper |
| IPaiementService / PaiementServiceImpl | IPaiementRepository, PaiementMapper |
| IMaintenanceService / MaintenanceServiceImpl | IMaintenanceRepository, IVehiculeRepository, MaintenanceMapper |

Chaque implémentation est @Service et implémente son interface. @RequiredArgsConstructor construit le constructeur à partir des champs private final. L’injection par constructeur exprime les dépendances obligatoires et permet de tester sans contexte Spring ; l’injection par attribut cache ces dépendances et impose souvent la réflexion dans les tests.

## Questions de cours

- Qui instancie le service ? Le conteneur Spring détecte @Service lors du scan du package racine, construit le bean et injecte ses dépendances.
- Pourquoi une interface ? Elle expose le contrat métier aux contrôleurs sans les coupler à l’implémentation et permet des doublures en test. Il existe ici une implémentation par interface.
- Pourquoi singleton sans état ? C’est la portée par défaut, partagée entre requêtes. Les valeurs propres à une requête restent des variables locales ; aucune donnée de client n’est conservée dans un champ du service.
- Pourquoi charger avant update ? Pour vérifier l’existence, garder l’identifiant et la collection persistante, et recopier uniquement les champs modifiables sur l’objet géré. Un save direct de l’objet reçu pourrait insérer ou écraser des associations.
- @Component vs @Bean ? @Component (et @Service) permet la détection de classes ; @Bean déclare explicitement un objet retourné par une méthode de configuration, utile notamment pour un objet tiers. Les mappers manuels sont des @Component.
- @Primary vs @Qualifier ? @Primary choisit le candidat par défaut lorsqu’il y a plusieurs beans compatibles ; @Qualifier sélectionne un candidat précis au point d’injection. Aucun des deux n’est nécessaire avec une implémentation par interface ici.
- Pourquoi Paiement en lecture seule ? Le paiement est un composant du contrat. L’ajout passe uniquement par ContratCreateRequest ; cascade ALL assure sa persistance puis sa suppression avec le contrat. Pas de méthode publique create/update/delete dans IPaiementService.

## Erreurs d’injection

Absence de bean : vérifier @Service/@Component, le package scanné et l’interface implémentée. Plusieurs beans : supprimer le doublon involontaire ou choisir @Qualifier/@Primary si les implémentations sont intentionnelles. Dépendance circulaire : revoir les responsabilités et supprimer la boucle ; ne pas la masquer avec de l’injection par attribut. Ici les dépendances vont contrôleur → service → repository/mapper ; ContratMapper → PaiementMapper ne revient pas vers ContratMapper.

## Corrections et règles appliquées

Les couches de service étaient absentes. Neuf couples ont été ajoutés, avec signatures DTO et mappers explicites. Les écritures sont transactionnelles ; les lectures sont readOnly et construisent les DTO dans la transaction, avec open-in-view=false.

create refuse l’ID technique ; @Null et une vérification explicite protègent ce comportement. PUT ne propose aucun ID dans son DTO et Jackson refuse les champs inconnus : un ID fourni est donc rejeté en 400, même égal au chemin. L’ID du chemin reste la seule cible.

Les dates de fin sont vérifiées dans les services. Les références sont chargées et une absence produit ResourceNotFoundException en français. Les valeurs uniques sont vérifiées avant écriture et protégées en base. Les contrôleurs assurent la validation Jakarta via @Valid ; une éventuelle nouvelle entrée non HTTP devra également valider ses DTO.

Les mappers ne recopient que les scalaires, sans toucher automatiquement aux enfants. Les services gèrent les relations et leur côté inverse. Contrat.ajouterPaiement maintient contrat.paiements et paiement.contrat. PUT contrat modifie uniquement dateSignature, montantTotal et valide : réservation et paiements sont conservés. La collection persistante n’est jamais remplacée. Les équipements partagés sont détachés à la suppression d’un équipement, sans supprimer de véhicule. Les autres références bloquant une suppression donnent 409.
