package tn.esprit.autoloc.mapper;

import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import tn.esprit.autoloc.domain.Maintenance;
import tn.esprit.autoloc.web.dto.MaintenanceCreateRequest;
import tn.esprit.autoloc.web.dto.MaintenanceResponse;
import tn.esprit.autoloc.web.dto.MaintenanceUpdateRequest;

@Component
@RequiredArgsConstructor
public class MaintenanceMapper {
    public MaintenanceResponse toResponse(Maintenance entity) {
        MaintenanceResponse dto = new MaintenanceResponse();
        dto.setIdMaintenance(entity.getIdMaintenance());
        dto.setDateDebut(entity.getDateDebut());
        dto.setDateFin(entity.getDateFin());
        dto.setDescription(entity.getDescription());
        dto.setVehiculeId(entity.getVehicule().getIdVehicule());
        return dto;
    }

    public void copyScalars(MaintenanceCreateRequest dto, Maintenance entity) {
        entity.setDateDebut(dto.getDateDebut());
        entity.setDateFin(dto.getDateFin());
        entity.setDescription(dto.getDescription());
    }

    public void copyScalars(MaintenanceUpdateRequest dto, Maintenance entity) {
        entity.setDateDebut(dto.getDateDebut());
        entity.setDateFin(dto.getDateFin());
        entity.setDescription(dto.getDescription());
    }
}
