package org.fleetflow.souktransportbackend.repository;

import org.fleetflow.souktransportbackend.dto.response.UserDto;
import org.fleetflow.souktransportbackend.entity.User;
import org.fleetflow.souktransportbackend.enums.Role;
import org.fleetflow.souktransportbackend.enums.StatutUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.nio.channels.FileChannel;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Page<User> findAll(Pageable pageable);
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    Page<User>findByNomContainingIgnoreCase(String nome,Pageable pageable);
    Page<User> findByStatutUser(StatutUser statutUser, Pageable pageable);
    Page<User> findByRole(Role role, Pageable pageable);
}
