package tn.esprit.autoloc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import tn.esprit.autoloc.domain.Contrat;
import tn.esprit.autoloc.domain.Paiement;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.repository.IContratRepository;
import tn.esprit.autoloc.repository.IReservationRepository;
import tn.esprit.autoloc.mapper.ContratMapper;
import tn.esprit.autoloc.mapper.PaiementMapper;
import tn.esprit.autoloc.exception.BusinessConflictException;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.web.dto.ContratCreateRequest;
import tn.esprit.autoloc.web.dto.ContratResponse;
import tn.esprit.autoloc.web.dto.ContratUpdateRequest;
import tn.esprit.autoloc.web.dto.PaiementCreateRequest;
import java.time.LocalDate;
import tn.esprit.autoloc.web.dto.ContratDTO;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ContratServiceImpl implements IContratService {
    private final IContratRepository repository;
    private final ContratMapper mapper;
    private final IReservationRepository reservationRepository;
    private final PaiementMapper paiementMapper;

    private Contrat load(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Contrat", id));
    }

    @Override
    public ContratResponse findById(Long id) { return mapper.toResponse(load(id)); }

    @Override
    public List<ContratResponse> findAll() {
        return repository.findAll().stream().map(mapper::toResponse).toList();
    }

    @Override
    @Transactional
    public ContratResponse create(ContratCreateRequest request) {
        if (request.getIdContrat() != null) {
            throw new IllegalArgumentException("Un identifiant ne doit pas être fourni à la création");
        }
        if (repository.existsByReservation_IdReservation(request.getReservationId())) {
            throw new BusinessConflictException("Cette réservation possède déjà un contrat");
        }
        Contrat entity = new Contrat();
        mapper.copyScalars(request, entity);
        Reservation reservation = reservationRepository.findById(request.getReservationId())
                .orElseThrow(() -> new ResourceNotFoundException("Reservation", request.getReservationId()));
        entity.setReservation(reservation);
        reservation.setContrat(entity);
        if (request.getPaiements() != null) {
            for (PaiementCreateRequest paiementRequest : request.getPaiements()) {
                if (paiementRequest.getIdPaiement() != null) {
                    throw new IllegalArgumentException("Un identifiant de paiement ne doit pas être fourni à la création");
                }
                Paiement paiement = new Paiement();
                paiementMapper.copyScalars(paiementRequest, paiement);
                entity.ajouterPaiement(paiement);
            }
        }
        return mapper.toResponse(repository.saveAndFlush(entity));
    }

    @Override
    @Transactional
    public ContratResponse update(Long id, ContratUpdateRequest request) {
        Contrat entity = load(id);
        mapper.copyScalars(request, entity);
        return mapper.toResponse(repository.saveAndFlush(entity));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Contrat entity = load(id);
        entity.getReservation().setContrat(null);
        repository.delete(entity);
        repository.flush();
    }

    @Override @Transactional
    public ContratDTO affecterContratAReservation(LocalDate dateSignature, LocalDate dateDebut, LocalDate dateFin) {
        if (dateFin.isBefore(dateDebut)) throw new IllegalArgumentException("Dates invalides");
        List<Contrat> contrats = repository.findByDateSignature(dateSignature);
        if (contrats.isEmpty()) throw new ResourceNotFoundException("Contrat introuvable");
        if (contrats.size() > 1) throw new BusinessConflictException("Plusieurs contrats correspondent");
        List<Reservation> reservations = reservationRepository.findByDateDebutAndDateFin(dateDebut, dateFin);
        if (reservations.isEmpty()) throw new ResourceNotFoundException("Reservation introuvable");
        if (reservations.size() > 1) throw new BusinessConflictException("Plusieurs reservations correspondent");
        Contrat contrat = contrats.get(0); Reservation reservation = reservations.get(0);
        if (reservation.getContrat() != null && !reservation.getContrat().getIdContrat().equals(contrat.getIdContrat())) throw new BusinessConflictException("Cette reservation possede deja un contrat");
        if (contrat.getReservation() != null) contrat.getReservation().setContrat(null);
        contrat.setReservation(reservation); reservation.setContrat(contrat);
        return mapper.toDTO(repository.saveAndFlush(contrat));
    }
}
