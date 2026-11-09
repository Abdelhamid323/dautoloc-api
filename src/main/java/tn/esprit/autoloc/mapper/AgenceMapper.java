package tn.esprit.autoloc.mapper;

import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.web.dto.AgenceCreateRequest;
import tn.esprit.autoloc.web.dto.AgenceResponse;
import tn.esprit.autoloc.web.dto.AgenceUpdateRequest;

@Component
@RequiredArgsConstructor
public class AgenceMapper {
    public AgenceResponse toResponse(Agence entity) {
        AgenceResponse dto = new AgenceResponse();
        dto.setIdAgence(entity.getIdAgence());
        dto.setNom(entity.getNom());
        dto.setVille(entity.getVille());
        dto.setAdresse(entity.getAdresse());
        dto.setTelephone(entity.getTelephone());
        return dto;
    }

    public void copyScalars(AgenceCreateRequest dto, Agence entity) {
        entity.setNom(dto.getNom());
        entity.setVille(dto.getVille());
        entity.setAdresse(dto.getAdresse());
        entity.setTelephone(dto.getTelephone());
    }

    public void copyScalars(AgenceUpdateRequest dto, Agence entity) {
        entity.setNom(dto.getNom());
        entity.setVille(dto.getVille());
        entity.setAdresse(dto.getAdresse());
        entity.setTelephone(dto.getTelephone());
    }
}
