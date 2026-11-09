package tn.esprit.autoloc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import tn.esprit.autoloc.domain.Client;
import tn.esprit.autoloc.repository.IClientRepository;
import tn.esprit.autoloc.mapper.ClientMapper;
import tn.esprit.autoloc.exception.BusinessConflictException;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.web.dto.ClientCreateRequest;
import tn.esprit.autoloc.web.dto.ClientResponse;
import tn.esprit.autoloc.web.dto.ClientUpdateRequest;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClientServiceImpl implements IClientService {
    private final IClientRepository repository;
    private final ClientMapper mapper;

    private Client load(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Client", id));
    }

    @Override
    public ClientResponse findById(Long id) { return mapper.toResponse(load(id)); }

    @Override
    public List<ClientResponse> findAll() {
        return repository.findAll().stream().map(mapper::toResponse).toList();
    }

    @Override
    @Transactional
    public ClientResponse create(ClientCreateRequest request) {
        if (request.getIdClient() != null) {
            throw new IllegalArgumentException("Un identifiant ne doit pas être fourni à la création");
        }
        if (repository.existsByNumPermis(request.getNumPermis())) {
            throw new BusinessConflictException("Le numéro de permis est déjà utilisé(e)");
        }
        Client entity = new Client();
        mapper.copyScalars(request, entity);
        return mapper.toResponse(repository.saveAndFlush(entity));
    }

    @Override
    @Transactional
    public ClientResponse update(Long id, ClientUpdateRequest request) {
        Client entity = load(id);
        if (repository.existsByNumPermisAndIdClientNot(request.getNumPermis(), id)) {
            throw new BusinessConflictException("Le numéro de permis est déjà utilisé(e)");
        }
        mapper.copyScalars(request, entity);
        return mapper.toResponse(repository.saveAndFlush(entity));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Client entity = load(id);
        repository.delete(entity);
        repository.flush();
    }
}
