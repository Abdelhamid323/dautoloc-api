package tn.esprit.autoloc.web.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
public class AgenceCreateRequest {
    @Null(message = "Un identifiant ne doit pas être fourni à la création")
    private Long idAgence;

    @NotBlank(message = "Le champ nom est obligatoire")
    @Size(max = 20, message = "Le champ nom dépasse 20 caractères")
    private String nom;

    @NotBlank(message = "Le champ ville est obligatoire")
    @Size(max = 50, message = "Le champ ville dépasse 50 caractères")
    private String ville;

    @NotBlank(message = "Le champ adresse est obligatoire")
    @Size(max = 50, message = "Le champ adresse dépasse 50 caractères")
    private String adresse;

    @NotBlank(message = "Le champ telephone est obligatoire")
    @Size(max = 8, message = "Le champ telephone dépasse 8 caractères")
    private String telephone;
}
