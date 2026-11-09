package tn.esprit.autoloc.web.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;

import tn.esprit.autoloc.domain.RoleEmploye;

@Getter
@Setter
@NoArgsConstructor
public class EmployeResponse {
    private Long idEmploye;
    private String nom;
    private String prenom;
    private RoleEmploye role;
    private Long agenceId;
}
