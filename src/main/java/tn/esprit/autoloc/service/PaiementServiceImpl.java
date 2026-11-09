package tn.esprit.autoloc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import tn.esprit.autoloc.domain.Paiement;
import tn.esprit.autoloc.repository.IPaiementRepository;
import tn.esprit.autoloc.mapper.PaiementMapper;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.web.dto.PaiementResponse;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaiementServiceImpl implements IPaiementService {
    private final IPaiementRepository repository;
    private final PaiementMapper mapper;

    private Paiement load(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Paiement", id));
    }

    @Override
    public PaiementResponse findById(Long id) { return mapper.toResponse(load(id)); }

    @Override
    public List<PaiementResponse> findAll() {
        return repository.findAll().stream().map(mapper::toResponse).toList();
    }
}
