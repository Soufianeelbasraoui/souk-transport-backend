package org.fleetflow.souktransportbackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "expediteurs")
@PrimaryKeyJoinColumn(name = "id_users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Expediteur extends User {

    private String nomEntreprise;
    private String adresseEntreprise;

    @OneToMany(mappedBy = "expediteur", cascade = CascadeType.ALL)
    private List<Cargaison> cargaisons;
}