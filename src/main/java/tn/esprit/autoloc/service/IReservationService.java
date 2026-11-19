package tn.esprit.autoloc.service;

import java.util.List;
import tn.esprit.autoloc.web.dto.ReservationCreateRequest;
import tn.esprit.autoloc.web.dto.ReservationResponse;
import tn.esprit.autoloc.web.dto.ReservationUpdateRequest;

import tn.esprit.autoloc.web.dto.ReservationResponseDTO;
import tn.esprit.autoloc.web.dto.ReservationWithContratRequestDTO;
public interface IReservationService {
    ReservationResponse findById(Long id);
    List<ReservationResponse> findAll();
    ReservationResponse create(ReservationCreateRequest request);
    ReservationResponse update(Long id, ReservationUpdateRequest request);
    void deleteById(Long id);

    ReservationResponseDTO affecterReservationAClient(Long idReservation, String nom, String prenom);
    ReservationResponseDTO creerReservationEtContratAssocie(ReservationWithContratRequestDTO requestDTO);
}
