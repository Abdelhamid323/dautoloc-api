package tn.esprit.autoloc.mapper;

import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import tn.esprit.autoloc.domain.Contrat;
import tn.esprit.autoloc.web.dto.ContratCreateRequest;
import tn.esprit.autoloc.web.dto.ContratResponse;
import tn.esprit.autoloc.web.dto.ContratUpdateRequest;
import tn.esprit.autoloc.web.dto.ContratDTO;


@Component
@RequiredArgsConstructor
public class ContratMapper {
    private final PaiementMapper paiementMapper;
    public ContratResponse toResponse(Contrat entity) {
        ContratResponse dto = new ContratResponse();
        dto.setIdContrat(entity.getIdContrat());
        dto.setDateSignature(entity.getDateSignature());
        dto.setMontantTotal(entity.getMontantTotal());
        dto.setValide(entity.isValide());
        dto.setReservationId(entity.getReservation().getIdReservation());
        dto.setPaiements(entity.getPaiements().stream().map(paiementMapper::toResponse).toList());
        return dto;
    }

    public void copyScalars(ContratCreateRequest dto, Contrat entity) {
        entity.setDateSignature(dto.getDateSignature());
        entity.setMontantTotal(dto.getMontantTotal());
        entity.setValide(dto.getValide());
    }

    public void copyScalars(ContratUpdateRequest dto, Contrat entity) {
        entity.setDateSignature(dto.getDateSignature());
        entity.setMontantTotal(dto.getMontantTotal());
        entity.setValide(dto.getValide());
    }

    public ContratDTO toDTO(Contrat entity) {
        ContratDTO dto = new ContratDTO();
        dto.setIdContrat(entity.getIdContrat()); dto.setDateSignature(entity.getDateSignature());
        dto.setMontantTotal(entity.getMontantTotal()); dto.setValide(entity.isValide());
        dto.setReservationId(entity.getReservation().getIdReservation());
        dto.setReservationDateDebut(entity.getReservation().getDateDebut()); dto.setReservationDateFin(entity.getReservation().getDateFin());
        return dto;
    }
}
