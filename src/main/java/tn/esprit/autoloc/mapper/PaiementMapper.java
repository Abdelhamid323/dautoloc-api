package tn.esprit.autoloc.mapper;

import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import tn.esprit.autoloc.domain.Paiement;
import tn.esprit.autoloc.web.dto.PaiementCreateRequest;
import tn.esprit.autoloc.web.dto.PaiementResponse;

@Component
@RequiredArgsConstructor
public class PaiementMapper {
    public PaiementResponse toResponse(Paiement entity) {
        PaiementResponse dto = new PaiementResponse();
        dto.setIdPaiement(entity.getIdPaiement());
        dto.setMontant(entity.getMontant());
        dto.setDatePaiement(entity.getDatePaiement());
        dto.setModePaiement(entity.getModePaiement());
        dto.setContratId(entity.getContrat().getIdContrat());
        return dto;
    }

    public void copyScalars(PaiementCreateRequest dto, Paiement entity) {
        entity.setMontant(dto.getMontant());
        entity.setDatePaiement(dto.getDatePaiement());
        entity.setModePaiement(dto.getModePaiement());
    }
}
