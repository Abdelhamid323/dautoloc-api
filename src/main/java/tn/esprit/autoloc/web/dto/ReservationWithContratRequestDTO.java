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
public class ReservationWithContratRequestDTO {
    @NotNull private LocalDate dateDebut;
    @NotNull private LocalDate dateFin;
    @NotNull private StatutReservation statut;
    @NotNull private Long clientId;
    @NotNull private Long vehiculeId;
    @NotNull private LocalDate dateSignature;
    @NotNull private Boolean valide;
    @DecimalMin("0.00") @Digits(integer = 8, fraction = 2) private BigDecimal montantTotal;
}
