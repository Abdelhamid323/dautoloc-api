package tn.esprit.autoloc.web.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;
import tn.esprit.autoloc.domain.CategorieVehicule;
import tn.esprit.autoloc.domain.StatutVehicule;

@Getter
@Setter
@NoArgsConstructor
public class VehiculeCreateRequest {
    @Null(message = "Un identifiant ne doit pas être fourni à la création")
    private Long idVehicule;

    @NotBlank(message = "Le champ immatriculation est obligatoire")
    @Size(max = 20, message = "Le champ immatriculation dépasse 20 caractères")
    private String immatriculation;

    @NotBlank(message = "Le champ marque est obligatoire")
    @Size(max = 50, message = "Le champ marque dépasse 50 caractères")
    private String marque;

    @NotBlank(message = "Le champ modele est obligatoire")
    @Size(max = 50, message = "Le champ modele dépasse 50 caractères")
    private String modele;

    @NotNull(message = "Le champ categorie est obligatoire")
    private CategorieVehicule categorie;

    @NotNull(message = "Le champ tarifJournalier est obligatoire")
    @DecimalMin(value = "0.00", message = "Le montant ne peut pas être négatif")
    @Digits(integer = 8, fraction = 2, message = "Montant limité à 8 chiffres entiers et 2 décimales")
    private BigDecimal tarifJournalier;

    @NotNull(message = "Le champ statut est obligatoire")
    private StatutVehicule statut;

    @NotNull(message = "Le champ agenceId est obligatoire")
    @Positive(message = "Le champ agenceId doit être positif")
    private Long agenceId;

    @NotNull(message = "La liste des équipements est obligatoire, utiliser [] si vide")
    private List<@NotNull(message = "Identifiant équipement obligatoire")
    @Positive(message = "Identifiant équipement positif requis") Long> equipementIds = new ArrayList<>();
}
