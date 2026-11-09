package tn.esprit.autoloc.web.controller;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.LinkedHashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import tn.esprit.autoloc.exception.BusinessConflictException;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.web.dto.ApiError;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private ApiError error(int statut, String message, WebRequest request, Map<String, String> erreurs) {
        String chemin = request instanceof ServletWebRequest servlet
                ? servlet.getRequest().getRequestURI() : "";
        return new ApiError(OffsetDateTime.now(), statut, message, chemin, erreurs);
    }

    private ResponseEntity<Object> response(HttpStatus status, String message, WebRequest request) {
        return ResponseEntity.status(status).body(error(status.value(), message, request, Map.of()));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> notFound(ResourceNotFoundException ex, WebRequest request) {
        return response(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> badRequest(IllegalArgumentException ex, WebRequest request) {
        return response(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    @ExceptionHandler(BusinessConflictException.class)
    public ResponseEntity<Object> conflict(BusinessConflictException ex, WebRequest request) {
        return response(HttpStatus.CONFLICT, ex.getMessage(), request);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> integrity(DataIntegrityViolationException ex, WebRequest request) {
        return response(HttpStatus.CONFLICT,
                "Opération impossible : valeur unique déjà utilisée ou ressource encore référencée. Vérifiez l’immatriculation, le permis, la réservation et les dépendances.", request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, String> fields = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(field ->
                fields.merge(field.getField(), field.getDefaultMessage(), (a, b) -> a + "; " + b));
        return handleExceptionInternal(ex, error(400, "La requête contient des champs invalides", request, fields),
                headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        if (!(body instanceof ApiError)) {
            String message = switch (status.value()) {
                case 400 -> "Requête invalide : vérifiez le JSON, les champs autorisés, les dates (AAAA-MM-JJ), les énumérations et les paramètres";
                case 404 -> "Ressource ou chemin introuvable";
                case 405 -> "Méthode HTTP non autorisée pour cette ressource";
                case 406 -> "Format de réponse non accepté";
                case 415 -> "Type de contenu non supporté : utilisez application/json";
                default -> status.is5xxServerError() ? "Une erreur interne est survenue" : "La requête ne peut pas être traitée (HTTP " + status.value() + ")";
            };
            if (status.is5xxServerError()) { log.error("Erreur HTTP interne", ex); }
            body = error(status.value(), message, request, Map.of());
        }
        return super.handleExceptionInternal(ex, body, headers, status, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> unexpected(Exception ex, WebRequest request) {
        // ErrorResponse conserve également les statuts des exceptions HTTP non couvertes par la classe mère.
        if (ex instanceof ErrorResponse httpError) {
            return handleExceptionInternal(ex, null, httpError.getHeaders(), httpError.getStatusCode(), request);
        }
        log.error("Erreur interne inattendue", ex);
        return response(HttpStatus.INTERNAL_SERVER_ERROR, "Une erreur interne est survenue", request);
    }
}
