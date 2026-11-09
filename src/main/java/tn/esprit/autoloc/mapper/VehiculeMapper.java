package tn.esprit.autoloc.mapper;

import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import tn.esprit.autoloc.domain.Equipement;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.web.dto.VehiculeCreateRequest;
import tn.esprit.autoloc.web.dto.VehiculeResponse;
import tn.esprit.autoloc.web.dto.VehiculeUpdateRequest;

@Component
@RequiredArgsConstructor
public class VehiculeMapper {
    public VehiculeResponse toResponse(Vehicule entity) {
        VehiculeResponse dto = new VehiculeResponse();
        dto.setIdVehicule(entity.getIdVehicule());
        dto.setImmatriculation(entity.getImmatriculation());
        dto.setMarque(entity.getMarque());
        dto.setModele(entity.getModele());
        dto.setCategorie(entity.getCategorie());
        dto.setTarifJournalier(entity.getTarifJournalier());
        dto.setStatut(entity.getStatut());
        dto.setAgenceId(entity.getAgence().getIdAgence());
        dto.setEquipementIds(entity.getEquipements().stream().map(Equipement::getIdEquipement).toList());
        return dto;
    }

    public void copyScalars(VehiculeCreateRequest dto, Vehicule entity) {
        entity.setImmatriculation(dto.getImmatriculation());
        entity.setMarque(dto.getMarque());
        entity.setModele(dto.getModele());
        entity.setCategorie(dto.getCategorie());
        entity.setTarifJournalier(dto.getTarifJournalier());
        entity.setStatut(dto.getStatut());
    }

    public void copyScalars(VehiculeUpdateRequest dto, Vehicule entity) {
        entity.setImmatriculation(dto.getImmatriculation());
        entity.setMarque(dto.getMarque());
        entity.setModele(dto.getModele());
        entity.setCategorie(dto.getCategorie());
        entity.setTarifJournalier(dto.getTarifJournalier());
        entity.setStatut(dto.getStatut());
    }
}
