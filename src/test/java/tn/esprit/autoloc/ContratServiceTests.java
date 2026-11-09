package tn.esprit.autoloc;

import org.junit.jupiter.api.Test;
import tn.esprit.autoloc.domain.*;
import tn.esprit.autoloc.mapper.*;
import tn.esprit.autoloc.repository.*;
import tn.esprit.autoloc.service.ContratServiceImpl;
import tn.esprit.autoloc.web.dto.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/** Tests unitaires avec doubles de repositories, sans contexte Spring ni base. */
class ContratServiceTests {
    private final IContratRepository repository = mock(IContratRepository.class);
    private final IReservationRepository reservations = mock(IReservationRepository.class);
    private final PaiementMapper paiementMapper = new PaiementMapper();
    private final ContratServiceImpl service = new ContratServiceImpl(repository,
            new ContratMapper(paiementMapper), reservations, paiementMapper);

    @Test
    void updateConserveIdentifiantReservationEtCollectionDePaiements() {
        Contrat contrat = new Contrat();
        contrat.setIdContrat(4L);
        Reservation reservation = new Reservation();
        reservation.setIdReservation(8L);
        contrat.setReservation(reservation);
        for (long id = 1; id <= 2; id++) {
            Paiement paiement = new Paiement();
            paiement.setIdPaiement(id);
            contrat.ajouterPaiement(paiement);
        }
        List<Paiement> collectionPersistante = contrat.getPaiements();
        when(repository.findById(4L)).thenReturn(Optional.of(contrat));
        when(repository.saveAndFlush(contrat)).thenReturn(contrat);
        ContratUpdateRequest request = new ContratUpdateRequest();
        request.setDateSignature(LocalDate.of(2026, 11, 9));
        request.setMontantTotal(new BigDecimal("250.00"));
        request.setValide(false);

        ContratResponse response = service.update(4L, request);

        assertSame(collectionPersistante, contrat.getPaiements());
        assertSame(reservation, contrat.getReservation());
        assertEquals(4L, response.getIdContrat());
        assertEquals(List.of(1L, 2L), response.getPaiements().stream().map(PaiementResponse::getIdPaiement).toList());
        assertTrue(contrat.getPaiements().stream().allMatch(p -> p.getContrat() == contrat));
        assertEquals(new BigDecimal("250.00"), response.getMontantTotal());
        verifyNoInteractions(reservations);
    }

    @Test
    void creationDeDeuxPaiementsMaintientLesDeuxCotes() {
        Reservation reservation = new Reservation();
        reservation.setIdReservation(8L);
        when(reservations.findById(8L)).thenReturn(Optional.of(reservation));
        when(repository.saveAndFlush(any(Contrat.class))).thenAnswer(invocation -> {
            Contrat c = invocation.getArgument(0);
            c.setIdContrat(4L);
            assertEquals(2, c.getPaiements().size());
            assertTrue(c.getPaiements().stream().allMatch(p -> p.getContrat() == c));
            assertSame(c, reservation.getContrat());
            return c;
        });
        ContratCreateRequest request = new ContratCreateRequest();
        request.setReservationId(8L);
        request.setDateSignature(LocalDate.of(2026, 11, 9));
        request.setMontantTotal(new BigDecimal("200.00"));
        request.setValide(true);
        PaiementCreateRequest p1 = paiement("100.00");
        PaiementCreateRequest p2 = paiement("100.00");
        request.setPaiements(List.of(p1, p2));

        ContratResponse response = service.create(request);

        assertEquals(2, response.getPaiements().size());
        assertTrue(response.getPaiements().stream().allMatch(p -> p.getContratId().equals(4L)));
    }

    private PaiementCreateRequest paiement(String montant) {
        PaiementCreateRequest request = new PaiementCreateRequest();
        request.setMontant(new BigDecimal(montant));
        request.setDatePaiement(LocalDate.of(2026, 11, 9));
        request.setModePaiement(ModePaiement.CARTE);
        return request;
    }
}
