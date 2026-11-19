package tn.esprit.autoloc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.autoloc.domain.Vehicule;
import java.util.Optional;
public interface IVehiculeRepository extends JpaRepository<Vehicule, Long>{
    boolean existsByImmatriculation(String immatriculation);

    boolean existsByImmatriculationAndIdVehiculeNot(String immatriculation, Long id);

    Optional<Vehicule> findByImmatriculation(String immatriculation);
}
