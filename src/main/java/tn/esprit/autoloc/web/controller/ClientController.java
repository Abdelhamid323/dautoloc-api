package tn.esprit.autoloc.web.controller;

import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.util.List;
import tn.esprit.autoloc.service.IClientService;
import tn.esprit.autoloc.web.dto.ApiError;
import tn.esprit.autoloc.web.dto.ClientCreateRequest;
import tn.esprit.autoloc.web.dto.ClientResponse;
import tn.esprit.autoloc.web.dto.ClientUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.*;

@RestController
@RequestMapping("/clients")
@RequiredArgsConstructor
@Tag(name = "Client", description = "Gestion des clients")
public class ClientController {
    private final IClientService service;

    @GetMapping
    @Operation(summary = "Lister les clients")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Liste, vide si aucune ressource", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ClientResponse.class)))),
        @ApiResponse(responseCode = "500", description = "Erreur interne", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)))
    })
    public List<ClientResponse> findAll() { return service.findAll(); }

    @GetMapping("/{id}")
    @Operation(summary = "Consulter une ressource par identifiant")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Ressource trouvée", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClientResponse.class))),
        @ApiResponse(responseCode = "400", description = "Identifiant invalide", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "404", description = "Ressource introuvable", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "500", description = "Erreur interne", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)))
    })
    public ClientResponse findById(@Parameter(description = "Identifiant de la ressource", required = true) @PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    @Operation(summary = "Créer une ressource")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Ressource créée", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClientResponse.class))),
        @ApiResponse(responseCode = "400", description = "Corps invalide", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "404", description = "Référence introuvable", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "409", description = "Conflit d’intégrité", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "415", description = "Type de contenu non supporté", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "500", description = "Erreur interne", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<ClientResponse> create(@Valid @RequestBody ClientCreateRequest request) {
        ClientResponse response = service.create(request);
        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(response.getIdClient()).toUri()).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier une ressource")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Ressource modifiée", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClientResponse.class))),
        @ApiResponse(responseCode = "400", description = "Corps ou identifiant invalide", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "404", description = "Ressource ou référence introuvable", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "409", description = "Conflit d’intégrité", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "415", description = "Type de contenu non supporté", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "500", description = "Erreur interne", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)))
    })
    public ClientResponse update(@Parameter(description = "Identifiant de la ressource", required = true) @PathVariable Long id,
            @Valid @RequestBody ClientUpdateRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une ressource")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Ressource supprimée", content = @Content),
        @ApiResponse(responseCode = "400", description = "Identifiant invalide", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "404", description = "Ressource introuvable", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "409", description = "Ressource encore référencée", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "500", description = "Erreur interne", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<Void> deleteById(@Parameter(description = "Identifiant de la ressource", required = true) @PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
