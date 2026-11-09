package tn.esprit.autoloc.web.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class ClientUpdateRequest {
    @NotBlank(message = "Le champ nom est obligatoire")
    @Size(max = 20, message = "Le champ nom dépasse 20 caractères")
    private String nom;

    @NotBlank(message = "Le champ prenom est obligatoire")
    @Size(max = 20, message = "Le champ prenom dépasse 20 caractères")
    private String prenom;

    @NotBlank(message = "L’email est obligatoire")
    @Email(message = "L’email doit être valide")
    @Size(max = 50, message = "Email limité à 50 caractères")
    private String email;

    @NotBlank(message = "Le champ telephone est obligatoire")
    @Size(max = 20, message = "Le champ telephone dépasse 20 caractères")
    private String telephone;

    @NotBlank(message = "Le champ numPermis est obligatoire")
    @Size(max = 20, message = "Le champ numPermis dépasse 20 caractères")
    private String numPermis;

    @NotNull(message = "Le champ dateInscription est obligatoire")
    private LocalDate dateInscription;
}
