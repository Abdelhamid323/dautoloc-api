package tn.esprit.autoloc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.repository.IAgenceRepository;
import tn.esprit.autoloc.mapper.AgenceMapper;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.web.dto.AgenceCreateRequest;
import tn.esprit.autoloc.web.dto.AgenceResponse;
import tn.esprit.autoloc.web.dto.AgenceUpdateRequest;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AgenceServiceImpl implements IAgenceService {
    private final IAgenceRepository repository;
    private final AgenceMapper mapper;

    private Agence load(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Agence", id));
    }

    @Override
    public AgenceResponse findById(Long id) { return mapper.toResponse(load(id)); }

    @Override
    public List<AgenceResponse> findAll() {
        return repository.findAll().stream().map(mapper::toResponse).toList();
    }

    @Override
    @Transactional
    public AgenceResponse create(AgenceCreateRequest request) {
        if (request.getIdAgence() != null) {
            throw new IllegalArgumentException("Un identifiant ne doit pas être fourni à la création");
        }
        Agence entity = new Agence();
        mapper.copyScalars(request, entity);
        return mapper.toResponse(repository.saveAndFlush(entity));
    }

    @Override
    @Transactional
    public AgenceResponse update(Long id, AgenceUpdateRequest request) {
        Agence entity = load(id);
        mapper.copyScalars(request, entity);
        return mapper.toResponse(repository.saveAndFlush(entity));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Agence entity = load(id);
        repository.delete(entity);
        repository.flush();
    }
}
