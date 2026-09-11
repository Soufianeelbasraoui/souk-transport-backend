package org.fleetflow.souktransportbackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.fleetflow.souktransportbackend.enums.StatutCargaison;

import java.util.List;

@Entity
@Table(name = "cargaisons")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Cargaison {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;
    private Double poids;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut")
    private StatutCargaison statutCargaison = StatutCargaison.SOUMISE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "expediteur_id")
    private Expediteur expediteur;
    @OneToMany(mappedBy = "cargaison")
    private List<Reservation> reservations;
}
