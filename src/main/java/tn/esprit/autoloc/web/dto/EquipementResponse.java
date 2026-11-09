package tn.esprit.autoloc.web.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
public class EquipementResponse {
    private Long idEquipement;
    private String libelle;
}
