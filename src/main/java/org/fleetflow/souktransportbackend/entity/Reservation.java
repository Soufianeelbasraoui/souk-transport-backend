package org.fleetflow.souktransportbackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.fleetflow.souktransportbackend.enums.StatutReservation;

import java.time.LocalDateTime;

@Entity
@Table(name = "reservations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime dateReservation=LocalDateTime.now();
    private Double poidsReserve;
    private Double prixConvenu;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut")
    private StatutReservation statutReservation= StatutReservation.EN_ATTENTE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trajet_id",nullable = false)
    private Trajet trajet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cargaison_id",nullable = false)
    private Cargaison cargaison;

    @OneToOne(mappedBy = "reservation",cascade = CascadeType.ALL)
    private Paiement paiement;
}
