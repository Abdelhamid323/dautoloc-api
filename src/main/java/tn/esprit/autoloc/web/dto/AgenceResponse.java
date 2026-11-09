package tn.esprit.autoloc.web.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
public class AgenceResponse {
    private Long idAgence;
    private String nom;
    private String ville;
    private String adresse;
    private String telephone;
}
