package org.fleetflow.souktransportbackend.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "transporteurs")
@PrimaryKeyJoinColumn(name = "id_users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Transporteur extends User {

    private String cin;
    private String numeroPermis;

    @OneToMany(mappedBy = "transporteur", cascade = CascadeType.ALL)
    private List<Camion> camions;
}