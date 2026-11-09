package tn.esprit.autoloc.service;

import java.util.List;
import tn.esprit.autoloc.web.dto.ClientCreateRequest;
import tn.esprit.autoloc.web.dto.ClientResponse;
import tn.esprit.autoloc.web.dto.ClientUpdateRequest;

public interface IClientService {
    ClientResponse findById(Long id);
    List<ClientResponse> findAll();
    ClientResponse create(ClientCreateRequest request);
    ClientResponse update(Long id, ClientUpdateRequest request);
    void deleteById(Long id);
}
