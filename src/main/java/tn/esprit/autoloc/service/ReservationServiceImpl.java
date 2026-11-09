package tn.esprit.autoloc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import tn.esprit.autoloc.domain.Client;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.repository.IClientRepository;
import tn.esprit.autoloc.repository.IReservationRepository;
import tn.esprit.autoloc.repository.IVehiculeRepository;
import tn.esprit.autoloc.mapper.ReservationMapper;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.web.dto.ReservationCreateRequest;
import tn.esprit.autoloc.web.dto.ReservationResponse;
import tn.esprit.autoloc.web.dto.ReservationUpdateRequest;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservationServiceImpl implements IReservationService {
    private final IReservationRepository repository;
    private final ReservationMapper mapper;
    private final IClientRepository clientRepository;
    private final IVehiculeRepository vehiculeRepository;

    private Reservation load(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Reservation", id));
    }

    @Override
    public ReservationResponse findById(Long id) { return mapper.toResponse(load(id)); }

    @Override
    public List<ReservationResponse> findAll() {
        return repository.findAll().stream().map(mapper::toResponse).toList();
    }

    @Override
    @Transactional
    public ReservationResponse create(ReservationCreateRequest request) {
        if (request.getIdReservation() != null) {
            throw new IllegalArgumentException("Un identifiant ne doit pas être fourni à la création");
        }
        if (request.getDateFin().isBefore(request.getDateDebut())) {
            throw new IllegalArgumentException("La date de fin doit être supérieure ou égale à la date de début");
        }
        Reservation entity = new Reservation();
        mapper.copyScalars(request, entity);
        Client client = clientRepository.findById(request.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException("Client", request.getClientId()));
        if (entity.getClient() != null) { entity.getClient().getReservations().remove(entity); }
        entity.setClient(client);
        client.getReservations().add(entity);
        Vehicule vehicule = vehiculeRepository.findById(request.getVehiculeId())
                .orElseThrow(() -> new ResourceNotFoundException("Vehicule", request.getVehiculeId()));
        if (entity.getVehicule() != null) { entity.getVehicule().getReservations().remove(entity); }
        entity.setVehicule(vehicule);
        vehicule.getReservations().add(entity);
        return mapper.toResponse(repository.saveAndFlush(entity));
    }

    @Override
    @Transactional
    public ReservationResponse update(Long id, ReservationUpdateRequest request) {
        Reservation entity = load(id);
        if (request.getDateFin().isBefore(request.getDateDebut())) {
            throw new IllegalArgumentException("La date de fin doit être supérieure ou égale à la date de début");
        }
        mapper.copyScalars(request, entity);
        Client client = clientRepository.findById(request.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException("Client", request.getClientId()));
        if (entity.getClient() != null) { entity.getClient().getReservations().remove(entity); }
        entity.setClient(client);
        client.getReservations().add(entity);
        Vehicule vehicule = vehiculeRepository.findById(request.getVehiculeId())
                .orElseThrow(() -> new ResourceNotFoundException("Vehicule", request.getVehiculeId()));
        if (entity.getVehicule() != null) { entity.getVehicule().getReservations().remove(entity); }
        entity.setVehicule(vehicule);
        vehicule.getReservations().add(entity);
        return mapper.toResponse(repository.saveAndFlush(entity));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Reservation entity = load(id);
        repository.delete(entity);
        repository.flush();
    }
}
