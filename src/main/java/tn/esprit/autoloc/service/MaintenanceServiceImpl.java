package tn.esprit.autoloc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import tn.esprit.autoloc.domain.Maintenance;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.repository.IMaintenanceRepository;
import tn.esprit.autoloc.repository.IVehiculeRepository;
import tn.esprit.autoloc.mapper.MaintenanceMapper;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.web.dto.MaintenanceCreateRequest;
import tn.esprit.autoloc.web.dto.MaintenanceResponse;
import tn.esprit.autoloc.web.dto.MaintenanceUpdateRequest;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MaintenanceServiceImpl implements IMaintenanceService {
    private final IMaintenanceRepository repository;
    private final MaintenanceMapper mapper;
    private final IVehiculeRepository vehiculeRepository;

    private Maintenance load(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Maintenance", id));
    }

    @Override
    public MaintenanceResponse findById(Long id) { return mapper.toResponse(load(id)); }

    @Override
    public List<MaintenanceResponse> findAll() {
        return repository.findAll().stream().map(mapper::toResponse).toList();
    }

    @Override
    @Transactional
    public MaintenanceResponse create(MaintenanceCreateRequest request) {
        if (request.getIdMaintenance() != null) {
            throw new IllegalArgumentException("Un identifiant ne doit pas être fourni à la création");
        }
        if (request.getDateFin().isBefore(request.getDateDebut())) {
            throw new IllegalArgumentException("La date de fin doit être supérieure ou égale à la date de début");
        }
        Maintenance entity = new Maintenance();
        mapper.copyScalars(request, entity);
        Vehicule vehicule = vehiculeRepository.findById(request.getVehiculeId())
                .orElseThrow(() -> new ResourceNotFoundException("Vehicule", request.getVehiculeId()));
        if (entity.getVehicule() != null) { entity.getVehicule().getMaintenances().remove(entity); }
        entity.setVehicule(vehicule);
        vehicule.getMaintenances().add(entity);
        return mapper.toResponse(repository.saveAndFlush(entity));
    }

    @Override
    @Transactional
    public MaintenanceResponse update(Long id, MaintenanceUpdateRequest request) {
        Maintenance entity = load(id);
        if (request.getDateFin().isBefore(request.getDateDebut())) {
            throw new IllegalArgumentException("La date de fin doit être supérieure ou égale à la date de début");
        }
        mapper.copyScalars(request, entity);
        Vehicule vehicule = vehiculeRepository.findById(request.getVehiculeId())
                .orElseThrow(() -> new ResourceNotFoundException("Vehicule", request.getVehiculeId()));
        if (entity.getVehicule() != null) { entity.getVehicule().getMaintenances().remove(entity); }
        entity.setVehicule(vehicule);
        vehicule.getMaintenances().add(entity);
        return mapper.toResponse(repository.saveAndFlush(entity));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Maintenance entity = load(id);
        repository.delete(entity);
        repository.flush();
    }
}
