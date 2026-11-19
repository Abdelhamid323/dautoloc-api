package tn.esprit.autoloc.web.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.math.BigDecimal;
import java.util.List;
import jakarta.validation.constraints.*;
import tn.esprit.autoloc.domain.*;

@Getter
@Setter
@NoArgsConstructor
public class ContratDTO {
    private Long idContrat;
    private LocalDate dateSignature;
    private BigDecimal montantTotal;
    private boolean valide;
    private Long reservationId;
    private LocalDate reservationDateDebut;
    private LocalDate reservationDateFin;
}
