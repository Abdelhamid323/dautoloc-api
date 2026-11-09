package tn.esprit.autoloc.service;

import java.util.List;
import tn.esprit.autoloc.web.dto.EmployeCreateRequest;
import tn.esprit.autoloc.web.dto.EmployeResponse;
import tn.esprit.autoloc.web.dto.EmployeUpdateRequest;

public interface IEmployeService {
    EmployeResponse findById(Long id);
    List<EmployeResponse> findAll();
    EmployeResponse create(EmployeCreateRequest request);
    EmployeResponse update(Long id, EmployeUpdateRequest request);
    void deleteById(Long id);
}
