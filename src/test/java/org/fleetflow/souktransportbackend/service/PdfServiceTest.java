package org.fleetflow.souktransportbackend.service;

import org.fleetflow.souktransportbackend.entity.*;
import org.fleetflow.souktransportbackend.enums.MethodePaiement;
import org.fleetflow.souktransportbackend.enums.StatutPaiement;
import org.fleetflow.souktransportbackend.repository.PaiementRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PdfServiceTest {

    @Mock
    private PaiementRepository paiementRepository;

    @InjectMocks
    private PdfService pdfService;

    @Test
    void genererRecuPaiementPdf() {
        Long paiementId = 15L;

        Expediteur expediteur = new Expediteur();
        expediteur.setNom("El Basraoui");
        expediteur.setPrenom("Soufiane");

        Transporteur transporteur = new Transporteur();
        transporteur.setNom("Ali");
        transporteur.setPrenom("Ahmed");

        Camion camion = new Camion();
        camion.setTransporteur(transporteur);

        Trajet trajet = new Trajet();
        trajet.setVilleDepart("Béni Mellal");
        trajet.setVilleArrivee("Casablanca");
        trajet.setDateDepart(LocalDateTime.now().plusDays(5));
        trajet.setCamion(camion);

        Cargaison cargaison = new Cargaison();
        cargaison.setDescription("Produits alimentaires");
        cargaison.setExpediteur(expediteur);

        Reservation reservation = new Reservation();
        reservation.setCargaison(cargaison);
        reservation.setTrajet(trajet);
        reservation.setDateReservation(LocalDateTime.now());

        Paiement paiement = new Paiement();
        paiement.setId(paiementId);
        paiement.setMontantTotal(1500.0);
        paiement.setMethodePaiement(MethodePaiement.CASH);
        paiement.setStatutPaiement(StatutPaiement.PAYE);
        paiement.setReservation(reservation);
        when(paiementRepository.findById(paiementId)).thenReturn(Optional.of(paiement));
        byte[] pdfBytes = pdfService.genererRecuPaiement(paiementId);
        assertNotNull(pdfBytes);
        assertTrue(pdfBytes.length > 0);
        String pdfHeader = new String(pdfBytes, 0, 4);
        assertEquals("%PDF", pdfHeader);
    }
}
