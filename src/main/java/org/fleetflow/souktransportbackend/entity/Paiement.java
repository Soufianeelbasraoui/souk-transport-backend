package org.fleetflow.souktransportbackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.fleetflow.souktransportbackend.enums.MethodePaiement;
import org.fleetflow.souktransportbackend.enums.StatutPaiement;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "paiements")
public class Paiement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double montantTotal;
    @Enumerated(EnumType.STRING)
    @Column(name = "statut")
    private StatutPaiement statutPaiement=StatutPaiement.EN_ATTENTE;
    @Enumerated(EnumType.STRING)
    @Column(name = "methode")
    private MethodePaiement methodePaiement=MethodePaiement.CASH;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cargaison_id",nullable = false,unique = true)
    private Cargaison cargaison;
}
