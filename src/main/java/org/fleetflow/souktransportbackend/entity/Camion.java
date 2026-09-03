package org.fleetflow.souktransportbackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.fleetflow.souktransportbackend.enums.TypeCamion;

import java.util.List;

@Entity
@Table(name = "camions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Camion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String marque;
    private String modele;
    @Enumerated(EnumType.STRING)
    private TypeCamion type;
    private String immatriculation;
    private Double capacite;
    private Boolean disponible = true;
    @ManyToOne
    @JoinColumn(name = "transporteur_id", nullable = false)
    private Transporteur transporteur;

    @OneToMany(mappedBy = "camion")
    private List<Trajet> trajets;

}
