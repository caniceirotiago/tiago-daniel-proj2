package aor.paj.bean;

import aor.paj.dto.User;
import aor.paj.dto.UserWithNoPassword;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;
import jakarta.json.bind.JsonbException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.ArrayList;

/**
 * UserBean is a managed bean responsible for managing user data within the application. It provides functionality
 * to read and write user information from and to a JSON file, 'allUser.json'. This bean supports operations such as
 * user registration, login verification, retrieving user information by username, and converting User objects to
 * UserWithNoPassword objects for security purposes. It also includes methods to check user existence, update user
 * information, and manage user passwords. UserBean ensures that user data is persistently stored and accessible
 * throughout the application lifecycle.
 */

@ApplicationScoped
public class UserBean {
    final String filename = "allUser.json";
    private ArrayList<User> users;
    public UserBean() {
        readJsonFile();
        users.forEach(System.out::println);
    }
    public void readJsonFile(){
        File f = new File(filename);
        if(f.exists()){
            try {
                FileReader filereader = new FileReader(f);
                users = JsonbBuilder.create().fromJson(filereader, new
                        ArrayList<User>() {}.getClass().getGenericSuperclass());
            } catch (FileNotFoundException e) {
                throw new RuntimeException("Json file not found " + e);
            } catch (JsonbException e) {
                throw new RuntimeException("Json file processing error " + e);
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
    public boolean loginConfirmation(String username, String password){
        for(User us : users){
            if(us.getUsername().equals(username) && us.getPassword().equals(password)){
                return true;
            }
        }
        return false;
    }
    public String getPhotoURLByUsername(String username){
        for(User us : users){
            if(us.getUsername().equals(username)){
                System.out.println(us);
                System.out.println(us.getPhotoURL());
                return us.getPhotoURL();
            }
        }
        return null;
    }
    public String getFirstNameByUsername(String username){
        for(User us : users){
            if(us.getUsername().equals(username)){
                return us.getFirstName();
            }
        }
        return null;
    }
    public User getUserByUsername(String username){
        for(User us : users){
            if(us.getUsername().equals(username)){
                return us;
            }
        }
        return null;
    }
    public UserWithNoPassword convertUserToUserWithNoPassword(User user){
        return new UserWithNoPassword(user.getUsername(),
                user.getPhoneNumber(), // Corrigido: phoneNumber antes de email
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getPhotoURL());
    }

    public ArrayList<User> getAllUsers() {
        return users;
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
    public boolean updateUser(String username, User updatedUser) {
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            if (user.getUsername().equals(username)) {
                if(updatedUser.getPhoneNumber() != null)user.setPhoneNumber(updatedUser.getPhoneNumber());
                if(updatedUser.getEmail() != null) user.setEmail(updatedUser.getEmail());
                if(updatedUser.getFirstName() != null) user.setFirstName(updatedUser.getFirstName());
                if(updatedUser.getLastName() != null) user.setLastName(updatedUser.getLastName());
                if(updatedUser.getPhotoURL() != null) user.setPhotoURL(updatedUser.getPhotoURL());
                writeIntoJsonFile(); // Atualiza o arquivo JSON
                return true;
            }
        }
        return false;
    }
    public boolean updatePassWord(String username, String password) {
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            if (user.getUsername().equals(username)) {
                user.setPassword(password);
                writeIntoJsonFile(); // Atualiza o arquivo JSON
                return true;
            }
        }
        return false;
    }

}