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
public class PaiementCreateRequest {
    @Null(message = "Un identifiant ne doit pas être fourni à la création")
    private Long idPaiement;

    @NotNull(message = "Le montant est obligatoire")
    @Positive(message = "Le montant doit être strictement positif")
    @Digits(integer = 8, fraction = 2, message = "Montant limité à 8 chiffres entiers et 2 décimales")
    private BigDecimal montant;

    @NotNull(message = "Le champ datePaiement est obligatoire")
    private LocalDate datePaiement;

    @NotNull(message = "Le champ modePaiement est obligatoire")
    private ModePaiement modePaiement;
}
