package tn.esprit.autoloc.web.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.math.BigDecimal;

import tn.esprit.autoloc.domain.ModePaiement;

@Getter
@Setter
@NoArgsConstructor
public class PaiementResponse {
    private Long idPaiement;
    private BigDecimal montant;
    private LocalDate datePaiement;
    private ModePaiement modePaiement;
    private Long contratId;
}
