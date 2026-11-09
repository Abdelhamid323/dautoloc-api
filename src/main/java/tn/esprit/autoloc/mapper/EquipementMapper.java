package tn.esprit.autoloc.mapper;

import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import tn.esprit.autoloc.domain.Equipement;
import tn.esprit.autoloc.web.dto.EquipementCreateRequest;
import tn.esprit.autoloc.web.dto.EquipementResponse;
import tn.esprit.autoloc.web.dto.EquipementUpdateRequest;

@Component
@RequiredArgsConstructor
public class EquipementMapper {
    public EquipementResponse toResponse(Equipement entity) {
        EquipementResponse dto = new EquipementResponse();
        dto.setIdEquipement(entity.getIdEquipement());
        dto.setLibelle(entity.getLibelle());
        return dto;
    }

    public void copyScalars(EquipementCreateRequest dto, Equipement entity) {
        entity.setLibelle(dto.getLibelle());
    }

    public void copyScalars(EquipementUpdateRequest dto, Equipement entity) {
        entity.setLibelle(dto.getLibelle());
    }
}
