package org.fleetflow.souktransportbackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.fleetflow.souktransportbackend.enums.StatutTrajet;

import java.time.LocalDateTime;
import java.util.List;

@Entity(name = "trajets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Trajet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String villeDepart;
    private String villeArrivee;
    private LocalDateTime dateDepart;
    private Double prix;
    private double poidsDisponible;
    @Enumerated(EnumType.STRING)
    @Column(name = "statut")
    private StatutTrajet statutTrajet=StatutTrajet.PUBLIE;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "camion_id",nullable = false)
    private Camion camion;

    @OneToMany(mappedBy = "trajet")
    private List<Cargaison> cargaisons;



}
