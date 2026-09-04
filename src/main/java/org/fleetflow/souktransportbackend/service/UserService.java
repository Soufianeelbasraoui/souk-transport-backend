package org.fleetflow.souktransportbackend.service;
import org.fleetflow.souktransportbackend.dto.request.UserRequestDto;
import org.fleetflow.souktransportbackend.dto.response.UserDto;
import org.springframework.data.domain.Page;
public interface UserService {

    UserDto ajouterUser(UserRequestDto dto);

    UserDto modifierUser(Long id, UserRequestDto dto);

    UserDto trouverUser(Long id);

    void supprimerUser(Long id);

    Page<UserDto> listerUsers(int page, int size);
}