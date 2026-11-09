package tn.esprit.autoloc.service;

import java.util.List;
import tn.esprit.autoloc.web.dto.ReservationCreateRequest;
import tn.esprit.autoloc.web.dto.ReservationResponse;
import tn.esprit.autoloc.web.dto.ReservationUpdateRequest;

public interface IReservationService {
    ReservationResponse findById(Long id);
    List<ReservationResponse> findAll();
    ReservationResponse create(ReservationCreateRequest request);
    ReservationResponse update(Long id, ReservationUpdateRequest request);
    void deleteById(Long id);
}
