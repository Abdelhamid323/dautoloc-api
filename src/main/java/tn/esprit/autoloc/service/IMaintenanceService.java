package tn.esprit.autoloc.service;

import java.util.List;
import tn.esprit.autoloc.web.dto.MaintenanceCreateRequest;
import tn.esprit.autoloc.web.dto.MaintenanceResponse;
import tn.esprit.autoloc.web.dto.MaintenanceUpdateRequest;

public interface IMaintenanceService {
    MaintenanceResponse findById(Long id);
    List<MaintenanceResponse> findAll();
    MaintenanceResponse create(MaintenanceCreateRequest request);
    MaintenanceResponse update(Long id, MaintenanceUpdateRequest request);
    void deleteById(Long id);
}
