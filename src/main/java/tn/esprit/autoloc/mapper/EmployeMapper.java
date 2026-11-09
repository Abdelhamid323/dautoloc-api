package tn.esprit.autoloc.mapper;

import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import tn.esprit.autoloc.domain.Employe;
import tn.esprit.autoloc.web.dto.EmployeCreateRequest;
import tn.esprit.autoloc.web.dto.EmployeResponse;
import tn.esprit.autoloc.web.dto.EmployeUpdateRequest;

@Component
@RequiredArgsConstructor
public class EmployeMapper {
    public EmployeResponse toResponse(Employe entity) {
        EmployeResponse dto = new EmployeResponse();
        dto.setIdEmploye(entity.getIdEmploye());
        dto.setNom(entity.getNom());
        dto.setPrenom(entity.getPrenom());
        dto.setRole(entity.getRole());
        dto.setAgenceId(entity.getAgence().getIdAgence());
        return dto;
    }

    public void copyScalars(EmployeCreateRequest dto, Employe entity) {
        entity.setNom(dto.getNom());
        entity.setPrenom(dto.getPrenom());
        entity.setRole(dto.getRole());
    }

    public void copyScalars(EmployeUpdateRequest dto, Employe entity) {
        entity.setNom(dto.getNom());
        entity.setPrenom(dto.getPrenom());
        entity.setRole(dto.getRole());
    }
}
