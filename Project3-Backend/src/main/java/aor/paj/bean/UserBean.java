package aor.paj.bean;

import aor.paj.AppDataManager;
import aor.paj.dto.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;


@ApplicationScoped
public class UserBean {
    @Inject
    AppDataManager appDataManager;

    public UserBean() {

    }
    public boolean userExists(String username, String email){
        return appDataManager.confirmUserExists(username,email);
    }
    public void addUser(User user) {

        appDataManager.addUser(user);
    }

}