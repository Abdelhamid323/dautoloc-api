package tn.esprit.autoloc.web.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ContratResponse {
    private Long idContrat;
    private LocalDate dateSignature;
    private BigDecimal montantTotal;
    private Boolean valide;
    private Long reservationId;
    private List<PaiementResponse> paiements;
}
