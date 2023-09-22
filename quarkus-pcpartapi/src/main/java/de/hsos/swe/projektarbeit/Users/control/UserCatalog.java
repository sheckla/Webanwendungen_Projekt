package de.hsos.swe.projektarbeit.Users.control;

import java.util.List;

import de.hsos.swe.projektarbeit.Users.gateway.dto.UserInfoDTO;
/**
 * @author Jannis Welkener
 */
public interface UserCatalog {
    List<UserInfoDTO> getAllUsers();
    void register(String name,String password, String role);
    boolean usernameNotAlreadyTaken(String name);
}
