package tn.esprit.autoloc.mapper;

import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.web.dto.ReservationCreateRequest;
import tn.esprit.autoloc.web.dto.ReservationResponse;
import tn.esprit.autoloc.web.dto.ReservationUpdateRequest;

@Component
@RequiredArgsConstructor
public class ReservationMapper {
    public ReservationResponse toResponse(Reservation entity) {
        ReservationResponse dto = new ReservationResponse();
        dto.setIdReservation(entity.getIdReservation());
        dto.setDateDebut(entity.getDateDebut());
        dto.setDateFin(entity.getDateFin());
        dto.setStatut(entity.getStatut());
        dto.setClientId(entity.getClient().getIdClient());
        dto.setVehiculeId(entity.getVehicule().getIdVehicule());
        dto.setContratId(entity.getContrat() == null ? null : entity.getContrat().getIdContrat());
        return dto;
    }

    public void copyScalars(ReservationCreateRequest dto, Reservation entity) {
        entity.setDateDebut(dto.getDateDebut());
        entity.setDateFin(dto.getDateFin());
        entity.setStatut(dto.getStatut());
    }

    public void copyScalars(ReservationUpdateRequest dto, Reservation entity) {
        entity.setDateDebut(dto.getDateDebut());
        entity.setDateFin(dto.getDateFin());
        entity.setStatut(dto.getStatut());
    }
}
