package de.hsos.swe.projektarbeit.Users.gateway.dto;
/**
 * @author Jannis Welkener
 */
public class UserInfoDTO {
    public UserInfoDTO(){}
    public String name;
    public long id;
    public String role;
    @Override
    public String toString() {
        return "UserInfoDTO [name=" + name + ", id=" + id + ", role=" + role + "]";
    }
}
