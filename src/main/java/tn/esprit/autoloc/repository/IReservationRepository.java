package tn.esprit.autoloc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.autoloc.domain.Resevation;

public interface IReservationRepository extends JpaRepository<Resevation, Long>{
}
