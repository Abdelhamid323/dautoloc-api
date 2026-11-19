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
public class VehiculeDTO {
    private Long idVehicule;
    private String immatriculation;
    private String marque;
    private String modele;
    private CategorieVehicule categorie;
    private BigDecimal tarifJournalier;
    private StatutVehicule statut;
    private Long agenceId;
    private List<Long> equipementIds;
    private List<Long> maintenanceIds;
}
