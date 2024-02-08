package aor.paj.bean;

import aor.paj.dto.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.ArrayList;


@ApplicationScoped
public class UserBean {
    final String filename = "allUser.json";

    private ArrayList<User> users;
    public UserBean() {
        File f = new File(filename);
        if(f.exists()){
            try {
                FileReader filereader = new FileReader(f);
                users = JsonbBuilder.create().fromJson(filereader, new
                        ArrayList<User>() {}.getClass().getGenericSuperclass());
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }else{
            users = new ArrayList<User>();
        }
    }
    public boolean userExists(String username, String email){
        for(User us : users){
            if(us.getEmail().equals(email) || us.getUsername().equals(username)){
                return true;
            }
        }
        return false;
    }

    public void addUser(User a) {
        users.add(a);
        writeIntoJsonFile();
    }
    private void writeIntoJsonFile(){
        Jsonb jsonb = JsonbBuilder.create(new
                JsonbConfig().withFormatting(true));
        try {
            jsonb.toJson(users, new FileOutputStream(filename));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}