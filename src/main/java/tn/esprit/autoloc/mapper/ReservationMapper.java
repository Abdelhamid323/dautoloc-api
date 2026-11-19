package tn.esprit.autoloc.mapper;

import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.web.dto.ReservationCreateRequest;
import tn.esprit.autoloc.web.dto.ReservationResponse;
import tn.esprit.autoloc.web.dto.ReservationUpdateRequest;
import tn.esprit.autoloc.web.dto.ReservationResponseDTO;
import tn.esprit.autoloc.web.dto.ContratDTO;


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

    public ReservationResponseDTO toDTO(Reservation entity) {
        ReservationResponseDTO dto = new ReservationResponseDTO();
        dto.setIdReservation(entity.getIdReservation()); dto.setDateDebut(entity.getDateDebut()); dto.setDateFin(entity.getDateFin()); dto.setStatut(entity.getStatut());
        dto.setClientId(entity.getClient().getIdClient()); dto.setClientNom(entity.getClient().getNom()); dto.setClientPrenom(entity.getClient().getPrenom());
        dto.setVehiculeId(entity.getVehicule().getIdVehicule()); dto.setVehiculeImmatriculation(entity.getVehicule().getImmatriculation());
        if (entity.getContrat() != null) {
            var contrat = entity.getContrat(); ContratDTO c = new ContratDTO();
            c.setIdContrat(contrat.getIdContrat()); c.setDateSignature(contrat.getDateSignature()); c.setMontantTotal(contrat.getMontantTotal()); c.setValide(contrat.isValide());
            c.setReservationId(entity.getIdReservation()); c.setReservationDateDebut(entity.getDateDebut()); c.setReservationDateFin(entity.getDateFin()); dto.setContrat(c);
        }
        return dto;
    }
}
