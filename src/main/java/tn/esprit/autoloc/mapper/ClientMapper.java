package tn.esprit.autoloc.mapper;

import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import tn.esprit.autoloc.domain.Client;
import tn.esprit.autoloc.web.dto.ClientCreateRequest;
import tn.esprit.autoloc.web.dto.ClientResponse;
import tn.esprit.autoloc.web.dto.ClientUpdateRequest;

@Component
@RequiredArgsConstructor
public class ClientMapper {
    public ClientResponse toResponse(Client entity) {
        ClientResponse dto = new ClientResponse();
        dto.setIdClient(entity.getIdClient());
        dto.setNom(entity.getNom());
        dto.setPrenom(entity.getPrenom());
        dto.setEmail(entity.getEmail());
        dto.setTelephone(entity.getTelephone());
        dto.setNumPermis(entity.getNumPermis());
        dto.setDateInscription(entity.getDateInscription());
        return dto;
    }

    public void copyScalars(ClientCreateRequest dto, Client entity) {
        entity.setNom(dto.getNom());
        entity.setPrenom(dto.getPrenom());
        entity.setEmail(dto.getEmail());
        entity.setTelephone(dto.getTelephone());
        entity.setNumPermis(dto.getNumPermis());
        entity.setDateInscription(dto.getDateInscription());
    }

    public void copyScalars(ClientUpdateRequest dto, Client entity) {
        entity.setNom(dto.getNom());
        entity.setPrenom(dto.getPrenom());
        entity.setEmail(dto.getEmail());
        entity.setTelephone(dto.getTelephone());
        entity.setNumPermis(dto.getNumPermis());
        entity.setDateInscription(dto.getDateInscription());
    }
}
