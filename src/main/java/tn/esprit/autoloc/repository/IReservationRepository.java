package tn.esprit.autoloc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.autoloc.domain.Reservation;

import java.util.List;
import java.time.LocalDate;
public interface IReservationRepository extends JpaRepository<Reservation, Long>{

    List<Reservation> findByDateDebutAndDateFin(LocalDate dateDebut, LocalDate dateFin);
}
