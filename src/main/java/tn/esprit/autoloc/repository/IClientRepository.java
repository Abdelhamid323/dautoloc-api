package tn.esprit.autoloc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.autoloc.domain.Client;

public interface IClientRepository extends JpaRepository<Client, Long>{
    boolean existsByNumPermis(String numPermis);

    boolean existsByNumPermisAndIdClientNot(String numPermis, Long id);
}
