package tn.esprit.autoloc.web.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class MaintenanceCreateRequest {
    @Null(message = "Un identifiant ne doit pas être fourni à la création")
    private Long idMaintenance;

    @NotNull(message = "Le champ dateDebut est obligatoire")
    private LocalDate dateDebut;

    @NotNull(message = "Le champ dateFin est obligatoire")
    private LocalDate dateFin;

    @NotBlank(message = "Le champ description est obligatoire")
    @Size(max = 50, message = "Le champ description dépasse 50 caractères")
    private String description;

    @NotNull(message = "Le champ vehiculeId est obligatoire")
    @Positive(message = "Le champ vehiculeId doit être positif")
    private Long vehiculeId;
}
