package de.hsos.swe.projektarbeit.Users.gateway;

import java.util.ArrayList;
import java.util.List;

import de.hsos.swe.projektarbeit.Users.control.UserCatalog;
import de.hsos.swe.projektarbeit.Users.entity.User;
import de.hsos.swe.projektarbeit.Users.gateway.dto.UserInfoDTO;
import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.enterprise.context.RequestScoped;
/**
 * @author Jannis Welkener
 */
@RequestScoped
public class UserRepository implements UserCatalog{

    @Override
    public UserInfoDTO findById(String username) {
        UserInfoDTO user = new UserInfoDTO();
        List<PanacheEntityBase> users = User.listAll();
        for (int i = 0; i < users.size(); i++) {
            if (((User)users.get(i)).username.equals(username)) {
                user = new UserInfoDTO();
                user.name = ((User)users.get(i)).username;
                user.id = ((User)users.get(i)).id;
                user.role = ((User)users.get(i)).role;
            }
        }
        return user;
    }

    @Override
    public List<UserInfoDTO> getAllUsers(){
        ArrayList<UserInfoDTO> list = new ArrayList<>();

        User.listAll().forEach(
            u->{
            UserInfoDTO userInfo = new UserInfoDTO();
            userInfo.name = ((User)u).username;
            userInfo.id = ((User)u).id;
            userInfo.role = ((User)u).role;

            list.add(userInfo);
        });

        return list;
    }

    @Override
    public void register(String name,String password, String role){
        User user = new User();

        user.username = name;
        user.password = BcryptUtil.bcryptHash(password);
        user.role = role;
        user.persist();
    }

    @Override
    public boolean usernameNotAlreadyTaken(String name){

        User u = (User)User.find("username", name).firstResult();

        //If no user is found
        if(u == null){
            return true;
        }

        //if a user is found, double check name
        return !name.equals(((User)User.find("username", name).firstResult()).username);
    }

}
