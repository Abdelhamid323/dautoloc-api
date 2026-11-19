package tn.esprit.autoloc.web.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.math.BigDecimal;
import java.util.List;
import jakarta.validation.constraints.*;
import tn.esprit.autoloc.domain.*;

@Getter
@Setter
@NoArgsConstructor
public class ReservationResponseDTO {
    private Long idReservation;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private StatutReservation statut;
    private Long clientId;
    private String clientNom;
    private String clientPrenom;
    private Long vehiculeId;
    private String vehiculeImmatriculation;
    private ContratDTO contrat;
}
