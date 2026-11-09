package tn.esprit.autoloc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import tn.esprit.autoloc.domain.Equipement;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.repository.IEquipementRepository;
import tn.esprit.autoloc.mapper.EquipementMapper;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.web.dto.EquipementCreateRequest;
import tn.esprit.autoloc.web.dto.EquipementResponse;
import tn.esprit.autoloc.web.dto.EquipementUpdateRequest;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EquipementServiceImpl implements IEquipementService {
    private final IEquipementRepository repository;
    private final EquipementMapper mapper;

    private Equipement load(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Equipement", id));
    }

    @Override
    public EquipementResponse findById(Long id) { return mapper.toResponse(load(id)); }

    @Override
    public List<EquipementResponse> findAll() {
        return repository.findAll().stream().map(mapper::toResponse).toList();
    }

    @Override
    @Transactional
    public EquipementResponse create(EquipementCreateRequest request) {
        if (request.getIdEquipement() != null) {
            throw new IllegalArgumentException("Un identifiant ne doit pas être fourni à la création");
        }
        Equipement entity = new Equipement();
        mapper.copyScalars(request, entity);
        return mapper.toResponse(repository.saveAndFlush(entity));
    }

    @Override
    @Transactional
    public EquipementResponse update(Long id, EquipementUpdateRequest request) {
        Equipement entity = load(id);
        mapper.copyScalars(request, entity);
        return mapper.toResponse(repository.saveAndFlush(entity));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Equipement entity = load(id);
        // Les équipements sont partagés : détacher la table de jointure, conserver les véhicules.
        for (Vehicule vehicule : new java.util.ArrayList<>(entity.getVehicules())) {
            vehicule.getEquipements().remove(entity);
        }
        entity.getVehicules().clear();
        repository.delete(entity);
        repository.flush();
    }
}
