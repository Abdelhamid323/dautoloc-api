package tn.esprit.autoloc.service;

import java.util.List;
import tn.esprit.autoloc.web.dto.PaiementResponse;

public interface IPaiementService {
    PaiementResponse findById(Long id);
    List<PaiementResponse> findAll();
}
