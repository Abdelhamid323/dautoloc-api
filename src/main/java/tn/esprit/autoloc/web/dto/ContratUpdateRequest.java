package tn.esprit.autoloc.web.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class ContratUpdateRequest {
    @NotNull(message = "Le champ dateSignature est obligatoire")
    private LocalDate dateSignature;

    @NotNull(message = "Le champ montantTotal est obligatoire")
    @DecimalMin(value = "0.00", message = "Le montant ne peut pas être négatif")
    @Digits(integer = 8, fraction = 2, message = "Montant limité à 8 chiffres entiers et 2 décimales")
    private BigDecimal montantTotal;

    @NotNull(message = "Le champ valide est obligatoire")
    private Boolean valide;
}
