package tn.esprit.autoloc.web.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import tn.esprit.autoloc.service.IPaiementService;
import tn.esprit.autoloc.web.dto.ApiError;
import tn.esprit.autoloc.web.dto.PaiementResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.*;

@RestController
@RequestMapping("/paiements")
@RequiredArgsConstructor
@Tag(name = "Paiement", description = "Gestion des paiements")
public class PaiementController {
    private final IPaiementService service;

    @GetMapping
    @Operation(summary = "Lister les paiements")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Liste, vide si aucune ressource", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = PaiementResponse.class)))),
        @ApiResponse(responseCode = "500", description = "Erreur interne", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)))
    })
    public List<PaiementResponse> findAll() { return service.findAll(); }

    @GetMapping("/{id}")
    @Operation(summary = "Consulter une ressource par identifiant")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Ressource trouvée", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaiementResponse.class))),
        @ApiResponse(responseCode = "400", description = "Identifiant invalide", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "404", description = "Ressource introuvable", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "500", description = "Erreur interne", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)))
    })
    public PaiementResponse findById(@Parameter(description = "Identifiant de la ressource", required = true) @PathVariable Long id) {
        return service.findById(id);
    }
}
