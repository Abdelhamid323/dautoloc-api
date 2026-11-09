package tn.esprit.autoloc.web.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;

import tn.esprit.autoloc.domain.RoleEmploye;

@Getter
@Setter
@NoArgsConstructor
public class EmployeCreateRequest {
    @Null(message = "Un identifiant ne doit pas être fourni à la création")
    private Long idEmploye;

    @NotBlank(message = "Le champ nom est obligatoire")
    @Size(max = 20, message = "Le champ nom dépasse 20 caractères")
    private String nom;

    @NotBlank(message = "Le champ prenom est obligatoire")
    @Size(max = 20, message = "Le champ prenom dépasse 20 caractères")
    private String prenom;

    @NotNull(message = "Le champ role est obligatoire")
    private RoleEmploye role;

    @NotNull(message = "Le champ agenceId est obligatoire")
    @Positive(message = "Le champ agenceId doit être positif")
    private Long agenceId;
}
