package tn.esprit.autoloc.service;

import java.util.List;
import tn.esprit.autoloc.web.dto.EquipementCreateRequest;
import tn.esprit.autoloc.web.dto.EquipementResponse;
import tn.esprit.autoloc.web.dto.EquipementUpdateRequest;

public interface IEquipementService {
    EquipementResponse findById(Long id);
    List<EquipementResponse> findAll();
    EquipementResponse create(EquipementCreateRequest request);
    EquipementResponse update(Long id, EquipementUpdateRequest request);
    void deleteById(Long id);
}
