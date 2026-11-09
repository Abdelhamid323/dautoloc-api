package tn.esprit.autoloc.service;

import java.util.List;
import tn.esprit.autoloc.web.dto.VehiculeCreateRequest;
import tn.esprit.autoloc.web.dto.VehiculeResponse;
import tn.esprit.autoloc.web.dto.VehiculeUpdateRequest;

public interface IVehiculeService {
    VehiculeResponse findById(Long id);
    List<VehiculeResponse> findAll();
    VehiculeResponse create(VehiculeCreateRequest request);
    VehiculeResponse update(Long id, VehiculeUpdateRequest request);
    void deleteById(Long id);
}
