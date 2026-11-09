package tn.esprit.autoloc.service;

import java.util.List;
import tn.esprit.autoloc.web.dto.ContratCreateRequest;
import tn.esprit.autoloc.web.dto.ContratResponse;
import tn.esprit.autoloc.web.dto.ContratUpdateRequest;

public interface IContratService {
    ContratResponse findById(Long id);
    List<ContratResponse> findAll();
    ContratResponse create(ContratCreateRequest request);
    ContratResponse update(Long id, ContratUpdateRequest request);
    void deleteById(Long id);
}
