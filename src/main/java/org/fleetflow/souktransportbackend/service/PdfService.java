package org.fleetflow.souktransportbackend.service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.draw.LineSeparator;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.fleetflow.souktransportbackend.entity.Cargaison;
import org.fleetflow.souktransportbackend.entity.Paiement;
import org.fleetflow.souktransportbackend.entity.Reservation;
import org.fleetflow.souktransportbackend.entity.Trajet;
import org.fleetflow.souktransportbackend.repository.PaiementRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class PdfService {

    private final PaiementRepository paiementRepository;

    @Transactional(readOnly = true)
    public byte[] genererRecuPaiement(Long paiementId) {
        Paiement paiement = paiementRepository.findById(paiementId).orElseThrow(() -> new EntityNotFoundException("Paiement introuvable avec l'id : " + paiementId));

        Reservation reservation = paiement.getReservation();
        Cargaison cargaison = reservation != null ? reservation.getCargaison() : null;
        Trajet trajet = reservation != null ? reservation.getTrajet() : null;

        String expediteurNom = (cargaison != null && cargaison.getExpediteur() != null) ? cargaison.getExpediteur().getNom() + " " + cargaison.getExpediteur().getPrenom() : "N/A";

        String transporteurNom = (trajet != null && trajet.getCamion() != null && trajet.getCamion().getTransporteur() != null) ? trajet.getCamion().getTransporteur().getNom() + " " + trajet.getCamion().getTransporteur().getPrenom() : "N/A";

        String descriptionCargaison = (cargaison != null && cargaison.getDescription() != null) ? cargaison.getDescription() : "N/A";

        String villeDepart = (trajet != null && trajet.getVilleDepart() != null) ? trajet.getVilleDepart() : "N/A";
        String villeArrivee = (trajet != null && trajet.getVilleArrivee() != null) ? trajet.getVilleArrivee() : "N/A";

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String dateTrajet = (trajet != null && trajet.getDateDepart() != null) ? trajet.getDateDepart().format(dateFormatter) : "N/A";

        String datePaiement = (reservation != null && reservation.getDateReservation() != null) ? reservation.getDateReservation().format(dateFormatter) : LocalDate.now().format(dateFormatter);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A5);

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11);
            Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 11);
            Font footerFont = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 10);

            Paragraph title = new Paragraph("REÇU DE PAIEMENT", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(8);
            document.add(title);

            document.add(new Chunk(new LineSeparator()));

            Paragraph pInfo = new Paragraph();
            pInfo.setSpacingBefore(8);
            pInfo.setSpacingAfter(8);
            pInfo.add(new Chunk("Paiement N° : ", headerFont));
            pInfo.add(new Chunk(paiement.getId().toString() + "\n", normalFont));
            pInfo.add(new Chunk("Date        : ", headerFont));
            pInfo.add(new Chunk(datePaiement + "\n", normalFont));
            document.add(pInfo);

            document.add(new Chunk(new LineSeparator()));

            Paragraph pActeurs = new Paragraph();
            pActeurs.setSpacingBefore(8);
            pActeurs.setSpacingAfter(8);
            pActeurs.add(new Chunk("Expéditeur  : ", headerFont));
            pActeurs.add(new Chunk(expediteurNom + "\n", normalFont));
            pActeurs.add(new Chunk("Transporteur: ", headerFont));
            pActeurs.add(new Chunk(transporteurNom + "\n", normalFont));
            document.add(pActeurs);

            document.add(new Chunk(new LineSeparator()));

            Paragraph pDetails = new Paragraph();
            pDetails.setSpacingBefore(8);
            pDetails.setSpacingAfter(8);
            pDetails.add(new Chunk("Cargaison   : ", headerFont));
            pDetails.add(new Chunk(descriptionCargaison + "\n", normalFont));
            pDetails.add(new Chunk("Trajet      : ", headerFont));
            pDetails.add(new Chunk(villeDepart + " → " + villeArrivee + "\n", normalFont));
            pDetails.add(new Chunk("Date trajet : ", headerFont));
            pDetails.add(new Chunk(dateTrajet + "\n", normalFont));
            document.add(pDetails);

            document.add(new Chunk(new LineSeparator()));

            Paragraph pMontant = new Paragraph();
            pMontant.setSpacingBefore(8);
            pMontant.setSpacingAfter(12);
            pMontant.add(new Chunk("Montant     : ", headerFont));
            pMontant.add(new Chunk(paiement.getMontantTotal() + " MAD\n", normalFont));
            pMontant.add(new Chunk("Méthode     : ", headerFont));
            pMontant.add(new Chunk((paiement.getMethodePaiement() != null ? paiement.getMethodePaiement().name() : "CASH") + "\n", normalFont));
            document.add(pMontant);

            document.add(new Chunk(new LineSeparator()));

            Paragraph footer = new Paragraph("Merci pour votre confiance", footerFont);
            footer.setAlignment(Element.ALIGN_CENTER);
            footer.setSpacingBefore(10);
            document.add(footer);

            document.close();
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la génération du reçu PDF", e);
        }

        return out.toByteArray();
    }
}
