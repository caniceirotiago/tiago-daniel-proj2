package aor.paj.bean;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.ArrayList;

import aor.paj.dto.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;


@ApplicationScoped
public class UserBean {
    @Inject
    AppDataBean appDataBean;

    public UserBean() {

    }
    public boolean userExists(String username, String email){
        return appDataBean.confirmUserExists(username,email);
    }
    public void addUser(User user) {

        appDataBean.addUser(user);
    }

}