package tn.esprit.autoloc.web.dto;

import java.time.OffsetDateTime;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;

@Getter
@Setter
@AllArgsConstructor
public class ApiError {
    private OffsetDateTime horodatage;
    private int statut;
    private String message;
    private String chemin;
    private Map<String, String> erreurs;
}
