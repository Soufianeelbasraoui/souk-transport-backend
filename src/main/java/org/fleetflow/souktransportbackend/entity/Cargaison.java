package org.fleetflow.souktransportbackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.fleetflow.souktransportbackend.enums.StatutCargaison;

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
    private StatutCargaison statutCargaison=StatutCargaison.EN_ATTENTE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trajet_id")
    private Trajet trajet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "expediteur_id")
    private Expediteur expediteur;

    @OneToOne(mappedBy = "cargaison" ,cascade = CascadeType.ALL)
    private Paiement paiement;
}
