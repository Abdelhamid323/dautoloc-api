package tn.esprit.autoloc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.autoloc.domain.Contrat;

import java.util.List;
import java.time.LocalDate;
public interface IContratRepository extends JpaRepository<Contrat, Long> {
    boolean existsByReservation_IdReservation(Long reservationId);

    List<Contrat> findByDateSignature(LocalDate dateSignature);
}
