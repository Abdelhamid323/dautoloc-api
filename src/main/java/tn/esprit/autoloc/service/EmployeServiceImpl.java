package tn.esprit.autoloc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.domain.Employe;
import tn.esprit.autoloc.repository.IAgenceRepository;
import tn.esprit.autoloc.repository.IEmployeRepository;
import tn.esprit.autoloc.mapper.EmployeMapper;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.web.dto.EmployeCreateRequest;
import tn.esprit.autoloc.web.dto.EmployeResponse;
import tn.esprit.autoloc.web.dto.EmployeUpdateRequest;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmployeServiceImpl implements IEmployeService {
    private final IEmployeRepository repository;
    private final EmployeMapper mapper;
    private final IAgenceRepository agenceRepository;

    private Employe load(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Employe", id));
    }

    @Override
    public EmployeResponse findById(Long id) { return mapper.toResponse(load(id)); }

    @Override
    public List<EmployeResponse> findAll() {
        return repository.findAll().stream().map(mapper::toResponse).toList();
    }

    @Override
    @Transactional
    public EmployeResponse create(EmployeCreateRequest request) {
        if (request.getIdEmploye() != null) {
            throw new IllegalArgumentException("Un identifiant ne doit pas être fourni à la création");
        }
        Employe entity = new Employe();
        mapper.copyScalars(request, entity);
        Agence agence = agenceRepository.findById(request.getAgenceId())
                .orElseThrow(() -> new ResourceNotFoundException("Agence", request.getAgenceId()));
        if (entity.getAgence() != null) { entity.getAgence().getEmployes().remove(entity); }
        entity.setAgence(agence);
        agence.getEmployes().add(entity);
        return mapper.toResponse(repository.saveAndFlush(entity));
    }

    @Override
    @Transactional
    public EmployeResponse update(Long id, EmployeUpdateRequest request) {
        Employe entity = load(id);
        mapper.copyScalars(request, entity);
        Agence agence = agenceRepository.findById(request.getAgenceId())
                .orElseThrow(() -> new ResourceNotFoundException("Agence", request.getAgenceId()));
        if (entity.getAgence() != null) { entity.getAgence().getEmployes().remove(entity); }
        entity.setAgence(agence);
        agence.getEmployes().add(entity);
        return mapper.toResponse(repository.saveAndFlush(entity));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Employe entity = load(id);
        repository.delete(entity);
        repository.flush();
    }
}
