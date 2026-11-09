package tn.esprit.autoloc.service;

import java.util.List;
import tn.esprit.autoloc.web.dto.AgenceCreateRequest;
import tn.esprit.autoloc.web.dto.AgenceResponse;
import tn.esprit.autoloc.web.dto.AgenceUpdateRequest;

public interface IAgenceService {
    AgenceResponse findById(Long id);
    List<AgenceResponse> findAll();
    AgenceResponse create(AgenceCreateRequest request);
    AgenceResponse update(Long id, AgenceUpdateRequest request);
    void deleteById(Long id);
}
