package tn.esprit.autoloc.service;

import java.util.List;
import tn.esprit.autoloc.web.dto.VehiculeCreateRequest;
import tn.esprit.autoloc.web.dto.VehiculeResponse;
import tn.esprit.autoloc.web.dto.VehiculeUpdateRequest;

import tn.esprit.autoloc.web.dto.VehiculeDTO;
public interface IVehiculeService {
    VehiculeResponse findById(Long id);
    List<VehiculeResponse> findAll();
    VehiculeResponse create(VehiculeCreateRequest request);
    VehiculeResponse update(Long id, VehiculeUpdateRequest request);
    void deleteById(Long id);

    VehiculeDTO affecterVehiculeAEquipement(Long idVehicule, Long idEquipement);
    VehiculeDTO affecterMaintenanceAvehicule(String description, String immatriculation);
}
