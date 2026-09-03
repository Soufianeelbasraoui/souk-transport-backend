package org.fleetflow.souktransportbackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fleetflow.souktransportbackend.enums.Role;
import org.fleetflow.souktransportbackend.enums.StatutUser;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransporteurDto {

    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String ville;
    private Role role;
    private StatutUser statutUser;
    private String cin;
    private String numeroPermis;
}