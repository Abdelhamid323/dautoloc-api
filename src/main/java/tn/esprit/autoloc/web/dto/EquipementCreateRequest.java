package tn.esprit.autoloc.web.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
public class EquipementCreateRequest {
    @Null(message = "Un identifiant ne doit pas être fourni à la création")
    private Long idEquipement;

    @NotBlank(message = "Le champ libelle est obligatoire")
    @Size(max = 20, message = "Le champ libelle dépasse 20 caractères")
    private String libelle;
}
