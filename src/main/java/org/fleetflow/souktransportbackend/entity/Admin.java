package org.fleetflow.souktransportbackend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "admins")
@Getter
@Setter

@NoArgsConstructor
@PrimaryKeyJoinColumn(name = "id_users")
public class Admin extends User{
}
