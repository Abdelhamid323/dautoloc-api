package tn.esprit.autoloc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.service.*;
import tn.esprit.autoloc.web.controller.*;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/** Tests MVC isolés : aucun repository ni connexion à MySQL. */
@WebMvcTest(controllers = {AgenceController.class, EmployeController.class, VehiculeController.class,
        EquipementController.class, ClientController.class, ReservationController.class,
        ContratController.class, PaiementController.class, MaintenanceController.class})
@Import(GlobalExceptionHandler.class)
class ApiHttpTests {
    @Autowired private MockMvc mvc;
    @MockitoBean private IAgenceService agences;
    @MockitoBean private IEmployeService employes;
    @MockitoBean private IVehiculeService vehicules;
    @MockitoBean private IEquipementService equipements;
    @MockitoBean private IClientService clients;
    @MockitoBean private IReservationService reservations;
    @MockitoBean private IContratService contrats;
    @MockitoBean private IPaiementService paiements;
    @MockitoBean private IMaintenanceService maintenances;

    @Test
    void lesNeufCollectionsRetournentUnTableauVide() throws Exception {
        when(agences.findAll()).thenReturn(List.of());
        when(employes.findAll()).thenReturn(List.of());
        when(vehicules.findAll()).thenReturn(List.of());
        when(equipements.findAll()).thenReturn(List.of());
        when(clients.findAll()).thenReturn(List.of());
        when(reservations.findAll()).thenReturn(List.of());
        when(contrats.findAll()).thenReturn(List.of());
        when(paiements.findAll()).thenReturn(List.of());
        when(maintenances.findAll()).thenReturn(List.of());
        for (String chemin : List.of("agences", "employes", "vehicules", "equipements", "clients",
                "reservations", "contrats", "paiements", "maintenances")) {
            mvc.perform(get("/api/" + chemin).contextPath("/api"))
                    .andExpect(status().isOk()).andExpect(content().json("[]"));
        }
    }

    @Test
    void paiementAutonomeRefuseAvecAllow() throws Exception {
        mvc.perform(post("/api/paiements").contextPath("/api")
                        .contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(header().string("Allow", org.hamcrest.Matchers.containsString("GET")))
                .andExpect(jsonPath("$.statut").value(405))
                .andExpect(jsonPath("$.chemin").value("/api/paiements"));
        verifyNoInteractions(paiements);
    }

    @Test
    void typeDeContenuNonSupporteReste415() throws Exception {
        mvc.perform(post("/api/agences").contextPath("/api")
                        .contentType(MediaType.TEXT_PLAIN).content("texte"))
                .andExpect(status().isUnsupportedMediaType())
                .andExpect(jsonPath("$.statut").value(415));
    }

    @Test
    void identifiantDeCreationEtNomVideSontRefuses() throws Exception {
        mvc.perform(post("/api/agences").contextPath("/api").contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"idAgence":12,"nom":"","ville":"Tunis","adresse":"Rue A","telephone":"71000000"}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erreurs.idAgence").exists())
                .andExpect(jsonPath("$.erreurs.nom").exists());
        verifyNoInteractions(agences);
    }

    @Test
    void idDansPutNePeutPasEcraserLeChemin() throws Exception {
        mvc.perform(put("/api/agences/1").contextPath("/api").contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"idAgence":2,"nom":"Agence","ville":"Tunis","adresse":"Rue A","telephone":"71000000"}
                                """))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(agences);
    }

    @Test
    void validationImbriqueeConserveLeCheminDuChamp() throws Exception {
        mvc.perform(post("/api/contrats").contextPath("/api").contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"dateSignature":"2026-11-09","montantTotal":100,"valide":false,"reservationId":1,
                                 "paiements":[{"montant":-1,"datePaiement":"2026-11-09","modePaiement":"CARTE"}]}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erreurs['paiements[0].montant']").exists());
        verifyNoInteractions(contrats);
    }

    @Test
    void dateIllisibleEtParametreInvalideDonnent400() throws Exception {
        mvc.perform(post("/api/contrats").contextPath("/api").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"dateSignature\":\"invalide\"}"))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.statut").value(400));
        mvc.perform(get("/api/agences/abc").contextPath("/api"))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.statut").value(400));
    }

    @Test
    void absenceDeRessourceDonne404() throws Exception {
        when(agences.findById(99L)).thenThrow(new ResourceNotFoundException("Agence", 99L));
        mvc.perform(get("/api/agences/99").contextPath("/api"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Agence introuvable avec l’identifiant 99"));
    }
}
