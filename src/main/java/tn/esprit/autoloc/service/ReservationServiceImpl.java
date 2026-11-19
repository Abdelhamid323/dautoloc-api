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
import tn.esprit.autoloc.web.dto.ReservationResponseDTO;
import tn.esprit.autoloc.web.dto.ReservationWithContratRequestDTO;
import tn.esprit.autoloc.domain.Contrat;
import tn.esprit.autoloc.exception.BusinessConflictException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.temporal.ChronoUnit;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservationServiceImpl implements IReservationService {
    private final IReservationRepository repository;
    private final ReservationMapper mapper;
    private final IClientRepository clientRepository;
    private final IVehiculeRepository vehiculeRepository;
    private final tn.esprit.autoloc.repository.IContratRepository contratRepository;

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

    @Override @Transactional
    public ReservationResponseDTO affecterReservationAClient(Long idReservation, String nom, String prenom) {
        Reservation reservation = load(idReservation);
        List<Client> clients = clientRepository.findByNomAndPrenom(nom.trim(), prenom.trim());
        if (clients.isEmpty()) throw new ResourceNotFoundException("Client introuvable");
        if (clients.size() > 1) throw new BusinessConflictException("Plusieurs clients correspondent");
        Client client = clients.get(0);
        if (reservation.getClient() != null) reservation.getClient().getReservations().remove(reservation);
        reservation.setClient(client);
        if (!client.getReservations().contains(reservation)) client.getReservations().add(reservation);
        return mapper.toDTO(repository.saveAndFlush(reservation));
    }

    @Override @Transactional
    public ReservationResponseDTO creerReservationEtContratAssocie(ReservationWithContratRequestDTO requestDTO) {
        if (requestDTO.getDateFin().isBefore(requestDTO.getDateDebut())) throw new IllegalArgumentException("Dates invalides");
        Client client = clientRepository.findById(requestDTO.getClientId()).orElseThrow(() -> new ResourceNotFoundException("Client", requestDTO.getClientId()));
        Vehicule vehicule = vehiculeRepository.findById(requestDTO.getVehiculeId()).orElseThrow(() -> new ResourceNotFoundException("Vehicule", requestDTO.getVehiculeId()));
        Reservation reservation = new Reservation();
        reservation.setDateDebut(requestDTO.getDateDebut()); reservation.setDateFin(requestDTO.getDateFin()); reservation.setStatut(requestDTO.getStatut());
        reservation.setClient(client); reservation.setVehicule(vehicule); client.getReservations().add(reservation); vehicule.getReservations().add(reservation);
        reservation = repository.saveAndFlush(reservation);
        Contrat contrat = new Contrat(); contrat.setDateSignature(requestDTO.getDateSignature()); contrat.setValide(requestDTO.getValide());
        BigDecimal montant = requestDTO.getMontantTotal();
        if (montant == null) montant = vehicule.getTarifJournalier().multiply(BigDecimal.valueOf(Math.max(1, ChronoUnit.DAYS.between(requestDTO.getDateDebut(), requestDTO.getDateFin())))).setScale(2, RoundingMode.HALF_UP);
        contrat.setMontantTotal(montant); contrat.setReservation(reservation); reservation.setContrat(contrat);
        contratRepository.saveAndFlush(contrat);
        return mapper.toDTO(reservation);
    }
}
