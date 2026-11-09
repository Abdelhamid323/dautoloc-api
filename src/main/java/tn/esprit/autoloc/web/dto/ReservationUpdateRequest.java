package tn.esprit.autoloc.web.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

import tn.esprit.autoloc.domain.StatutReservation;

@Getter
@Setter
@NoArgsConstructor
public class ReservationUpdateRequest {
    @NotNull(message = "Le champ dateDebut est obligatoire")
    private LocalDate dateDebut;

    @NotNull(message = "Le champ dateFin est obligatoire")
    private LocalDate dateFin;

    @NotNull(message = "Le champ statut est obligatoire")
    private StatutReservation statut;

    @NotNull(message = "Le champ clientId est obligatoire")
    @Positive(message = "Le champ clientId doit être positif")
    private Long clientId;

    @NotNull(message = "Le champ vehiculeId est obligatoire")
    @Positive(message = "Le champ vehiculeId doit être positif")
    private Long vehiculeId;
}
