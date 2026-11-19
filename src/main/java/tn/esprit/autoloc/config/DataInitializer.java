package tn.esprit.autoloc.config;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.*;
import tn.esprit.autoloc.repository.*;

@Component
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(name = "autoloc.seed.enabled", havingValue = "true")
public class DataInitializer implements CommandLineRunner {
    private final IAgenceRepository agenceRepository;
    private final IClientRepository clientRepository;
    private final IEquipementRepository equipementRepository;
    private final IVehiculeRepository vehiculeRepository;
    private final IMaintenanceRepository maintenanceRepository;
    private final IReservationRepository reservationRepository;
    private final IContratRepository contratRepository;
    private final IEmployeRepository employeRepository;
    private final IPaiementRepository paiementRepository;

    @Override
    @Transactional
    public void run(String... args) {
        if (agenceRepository.count() > 0 || clientRepository.count() > 0
                || equipementRepository.count() > 0 || vehiculeRepository.count() > 0
                || maintenanceRepository.count() > 0 || reservationRepository.count() > 0
                || contratRepository.count() > 0 || employeRepository.count() > 0
                || paiementRepository.count() > 0) {
            log.info("Donn?es d?j? pr?sentes, initialisation ignor?e");
            return;
        }

        Agence agence = new Agence();
        agence.setNom("Agence Tunis");
        agence.setVille("Tunis");
        agence.setAdresse("Av Bourguiba");
        agence.setTelephone("71000000");
        agenceRepository.save(agence);

        Client amine = client("Ben Salah", "Amine", "amine@mail.tn", "22000000", "P001", "2026-01-10");
        Client sarra = client("Trabelsi", "Sarra", "sarra@mail.tn", "23000000", "P002", "2026-02-15");
        Client yassine = client("Gharbi", "Yassine", "yassine@mail.tn", "24000000", "P003", "2026-03-20");
        Equipement gps = equipement("GPS");
        Equipement siege = equipement("Siege bebe");
        Equipement barres = equipement("Barres de toit");

        Vehicule kia = vehicule(agence, "123TU4567", "Kia", "Picanto", CategorieVehicule.CITADINE, "100.00", gps);
        Vehicule peugeot = vehicule(agence, "456TU7890", "Peugeot", "3008", CategorieVehicule.SUV, "150.00", null);
        Vehicule renault = vehicule(agence, "789TU1234", "Renault", "Kangoo", CategorieVehicule.UTILITAIRE, "120.00", null);
        Maintenance vidange = maintenance(kia, "Vidange", "2026-11-01", "2026-11-03");
        Maintenance pneus = maintenance(peugeot, "Changement pneus", "2026-11-05", "2026-11-06");
        Reservation r1 = reservation(amine, kia, "2026-11-10", "2026-11-15", StatutReservation.CONFIRMEE);
        Reservation r2 = reservation(amine, peugeot, "2026-12-10", "2026-12-12", StatutReservation.EN_ATTENTE);
        Reservation r3 = reservation(yassine, renault, "2026-12-01", "2026-12-03", StatutReservation.CONFIRMEE);
        Contrat c1 = contrat(r1, "2026-11-09", "500.00");
        Contrat c2 = contrat(r3, "2026-11-28", "240.00");
        contratRepository.flush();

        log.info("IDs agence: {}; clients Ben Salah={}, Trabelsi={}, Gharbi={}", agence.getIdAgence(), amine.getIdClient(), sarra.getIdClient(), yassine.getIdClient());
        log.info("IDs ?quipements GPS={}, Siege bebe={}, Barres de toit={}", gps.getIdEquipement(), siege.getIdEquipement(), barres.getIdEquipement());
        log.info("IDs v?hicules Kia={}, Peugeot={}, Renault={}; maintenances Vidange={}, Changement pneus={}", kia.getIdVehicule(), peugeot.getIdVehicule(), renault.getIdVehicule(), vidange.getIdMaintenance(), pneus.getIdMaintenance());
        log.info("IDs r?servations R1={}, R2={}, R3={}; contrats C1={}, C2={}", r1.getIdReservation(), r2.getIdReservation(), r3.getIdReservation(), c1.getIdContrat(), c2.getIdContrat());
        log.info("Test affecter-client: idReservation={}, nom=Trabelsi, prenom=Sarra", r2.getIdReservation());
        log.info("Test affecter-equipement: idVehicule={}, idEquipement={}", peugeot.getIdVehicule(), siege.getIdEquipement());
        log.info("Test affecter-maintenance: description=Vidange, immatriculation=789TU1234");
        log.info("Test affecter-reservation: dateSignature=2026-11-09, dateDebut=2026-12-10, dateFin=2026-12-12");
        log.info("Test avec-contrat: {\"dateDebut\":\"2027-01-10\",\"dateFin\":\"2027-01-12\",\"statut\":\"EN_ATTENTE\",\"clientId\":{},\"vehiculeId\":{},\"dateSignature\":\"2027-01-09\",\"valide\":true}", sarra.getIdClient(), renault.getIdVehicule());
    }

    private Client client(String nom, String prenom, String email, String telephone, String permis, String date) {
        Client client = new Client();
        client.setNom(nom); client.setPrenom(prenom); client.setEmail(email); client.setTelephone(telephone);
        client.setNumPermis(permis); client.setDateInscription(LocalDate.parse(date));
        return clientRepository.save(client);
    }

    private Equipement equipement(String libelle) {
        Equipement equipement = new Equipement(); equipement.setLibelle(libelle);
        return equipementRepository.save(equipement);
    }

    private Vehicule vehicule(Agence agence, String immatriculation, String marque, String modele,
            CategorieVehicule categorie, String tarif, Equipement equipement) {
        Vehicule vehicule = new Vehicule();
        vehicule.setImmatriculation(immatriculation); vehicule.setMarque(marque); vehicule.setModele(modele);
        vehicule.setCategorie(categorie); vehicule.setTarifJournalier(new BigDecimal(tarif));
        vehicule.setStatut(StatutVehicule.DISPONIBLE); vehicule.setAgence(agence); agence.getVehicules().add(vehicule);
        if (equipement != null) { vehicule.getEquipements().add(equipement); equipement.getVehicules().add(vehicule); }
        return vehiculeRepository.save(vehicule);
    }

    private Maintenance maintenance(Vehicule vehicule, String description, String debut, String fin) {
        Maintenance maintenance = new Maintenance();
        maintenance.setDescription(description); maintenance.setDateDebut(LocalDate.parse(debut)); maintenance.setDateFin(LocalDate.parse(fin));
        maintenance.setVehicule(vehicule); vehicule.getMaintenances().add(maintenance);
        return maintenanceRepository.save(maintenance);
    }

    private Reservation reservation(Client client, Vehicule vehicule, String debut, String fin, StatutReservation statut) {
        Reservation reservation = new Reservation();
        reservation.setDateDebut(LocalDate.parse(debut)); reservation.setDateFin(LocalDate.parse(fin)); reservation.setStatut(statut);
        reservation.setClient(client); client.getReservations().add(reservation);
        reservation.setVehicule(vehicule); vehicule.getReservations().add(reservation);
        return reservationRepository.save(reservation);
    }

    private Contrat contrat(Reservation reservation, String signature, String montant) {
        Contrat contrat = new Contrat();
        contrat.setDateSignature(LocalDate.parse(signature)); contrat.setMontantTotal(new BigDecimal(montant)); contrat.setValide(true);
        contrat.setReservation(reservation); reservation.setContrat(contrat);
        return contratRepository.save(contrat);
    }
}
