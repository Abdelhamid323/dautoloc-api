package tn.esprit.autoloc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.domain.Equipement;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.repository.IAgenceRepository;
import tn.esprit.autoloc.repository.IEquipementRepository;
import tn.esprit.autoloc.repository.IVehiculeRepository;
import tn.esprit.autoloc.mapper.VehiculeMapper;
import tn.esprit.autoloc.exception.BusinessConflictException;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.web.dto.VehiculeCreateRequest;
import tn.esprit.autoloc.web.dto.VehiculeResponse;
import tn.esprit.autoloc.web.dto.VehiculeUpdateRequest;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VehiculeServiceImpl implements IVehiculeService {
    private final IVehiculeRepository repository;
    private final VehiculeMapper mapper;
    private final IAgenceRepository agenceRepository;
    private final IEquipementRepository equipementRepository;

    private Vehicule load(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Vehicule", id));
    }

    @Override
    public VehiculeResponse findById(Long id) { return mapper.toResponse(load(id)); }

    @Override
    public List<VehiculeResponse> findAll() {
        return repository.findAll().stream().map(mapper::toResponse).toList();
    }

    @Override
    @Transactional
    public VehiculeResponse create(VehiculeCreateRequest request) {
        if (request.getIdVehicule() != null) {
            throw new IllegalArgumentException("Un identifiant ne doit pas être fourni à la création");
        }
        if (repository.existsByImmatriculation(request.getImmatriculation())) {
            throw new BusinessConflictException("L’immatriculation est déjà utilisé(e)");
        }
        Vehicule entity = new Vehicule();
        mapper.copyScalars(request, entity);
        Agence agence = agenceRepository.findById(request.getAgenceId())
                .orElseThrow(() -> new ResourceNotFoundException("Agence", request.getAgenceId()));
        if (entity.getAgence() != null) { entity.getAgence().getVehicules().remove(entity); }
        entity.setAgence(agence);
        agence.getVehicules().add(entity);
        // Modifier la collection persistante du propriétaire, sans cascade vers les équipements.
        java.util.List<Equipement> equipements = new java.util.ArrayList<>();
        for (Long equipementId : new java.util.LinkedHashSet<>(request.getEquipementIds())) {
            equipements.add(equipementRepository.findById(equipementId)
                    .orElseThrow(() -> new ResourceNotFoundException("Equipement", equipementId)));
        }
        for (Equipement ancien : entity.getEquipements()) { ancien.getVehicules().remove(entity); }
        entity.getEquipements().clear();
        for (Equipement equipement : equipements) {
            entity.getEquipements().add(equipement);
            equipement.getVehicules().add(entity);
        }
        return mapper.toResponse(repository.saveAndFlush(entity));
    }

    @Override
    @Transactional
    public VehiculeResponse update(Long id, VehiculeUpdateRequest request) {
        Vehicule entity = load(id);
        if (repository.existsByImmatriculationAndIdVehiculeNot(request.getImmatriculation(), id)) {
            throw new BusinessConflictException("L’immatriculation est déjà utilisé(e)");
        }
        mapper.copyScalars(request, entity);
        Agence agence = agenceRepository.findById(request.getAgenceId())
                .orElseThrow(() -> new ResourceNotFoundException("Agence", request.getAgenceId()));
        if (entity.getAgence() != null) { entity.getAgence().getVehicules().remove(entity); }
        entity.setAgence(agence);
        agence.getVehicules().add(entity);
        // Modifier la collection persistante du propriétaire, sans cascade vers les équipements.
        java.util.List<Equipement> equipements = new java.util.ArrayList<>();
        for (Long equipementId : new java.util.LinkedHashSet<>(request.getEquipementIds())) {
            equipements.add(equipementRepository.findById(equipementId)
                    .orElseThrow(() -> new ResourceNotFoundException("Equipement", equipementId)));
        }
        for (Equipement ancien : entity.getEquipements()) { ancien.getVehicules().remove(entity); }
        entity.getEquipements().clear();
        for (Equipement equipement : equipements) {
            entity.getEquipements().add(equipement);
            equipement.getVehicules().add(entity);
        }
        return mapper.toResponse(repository.saveAndFlush(entity));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Vehicule entity = load(id);
        for (Equipement equipement : entity.getEquipements()) { equipement.getVehicules().remove(entity); }
        entity.getEquipements().clear();
        repository.delete(entity);
        repository.flush();
    }
}
