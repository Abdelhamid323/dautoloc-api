package tn.esprit.autoloc.web.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ContratCreateRequest {
    @Null(message = "Un identifiant ne doit pas être fourni à la création")
    private Long idContrat;

    @NotNull(message = "Le champ dateSignature est obligatoire")
    private LocalDate dateSignature;

    @NotNull(message = "Le champ montantTotal est obligatoire")
    @DecimalMin(value = "0.00", message = "Le montant ne peut pas être négatif")
    @Digits(integer = 8, fraction = 2, message = "Montant limité à 8 chiffres entiers et 2 décimales")
    private BigDecimal montantTotal;

    @NotNull(message = "Le champ valide est obligatoire")
    private Boolean valide;

    @NotNull(message = "Le champ reservationId est obligatoire")
    @Positive(message = "Le champ reservationId doit être positif")
    private Long reservationId;

    @Valid
    private List<@NotNull(message = "Un paiement ne peut pas être null") PaiementCreateRequest> paiements;
}
